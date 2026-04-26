package ma.acme.contacts.ui;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import ma.acme.contacts.dao.ContactDao;
import ma.acme.contacts.model.Contact;

import java.sql.SQLException;
import java.util.List;
import javafx.scene.chart.PieChart;
import java.util.Map;

public class MainView extends BorderPane {

    private final ContactDao dao = new ContactDao();
    private final ObservableList<Contact> contacts = FXCollections.observableArrayList();
    private final TableView<Contact> table = new TableView<>(contacts);
    
 // --- Champs du formulaire ---
    private final TextField tfNom       = new TextField();
    private final TextField tfPrenom    = new TextField();
    private final TextField tfEmail     = new TextField();
    private final TextField tfTelephone = new TextField();
    private final ComboBox<String> cbCategorie = new ComboBox<>();

    // --- Boutons ---
    private final Button bAjouter   = new Button("Ajouter");
    private final Button bModifier  = new Button("Modifier");
    private final Button bSupprimer = new Button("Supprimer");
    private final Button bEffacer   = new Button("Effacer");
    

 // --- Graphique ---
 private final PieChart graphique = new PieChart();
 private final TextField tfRecherche = new TextField();


 public MainView() {
	    buildTable();

	    graphique.setTitle("Répartition par catégorie");
	    graphique.setPrefHeight(220);

	    setTop(buildTop());
	    setCenter(table);
	    setRight(buildForm());
	    setBottom(buildButtons());
	    cablerEvenements();
	    rafraichir();
	}

    private void buildTable() {
        TableColumn<Contact, String> cNom       = new TableColumn<>("Nom");
        TableColumn<Contact, String> cPrenom    = new TableColumn<>("Prénom");
        TableColumn<Contact, String> cEmail     = new TableColumn<>("Email");
        TableColumn<Contact, String> cTelephone = new TableColumn<>("Téléphone");
        TableColumn<Contact, String> cCategorie = new TableColumn<>("Catégorie");

        cNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        cPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        cEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        cTelephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        cCategorie.setCellValueFactory(new PropertyValueFactory<>("categorie"));

        cNom.setPrefWidth(140);
        cPrenom.setPrefWidth(140);
        cEmail.setPrefWidth(220);
        cTelephone.setPrefWidth(130);
        cCategorie.setPrefWidth(110);

        table.getColumns().addAll(cNom, cPrenom, cEmail, cTelephone, cCategorie);
    }

    private void rafraichir() {
        try {
            // 1) Mise à jour du tableau
            contacts.setAll(dao.findAll());

            // 2) Mise à jour du graphique
            Map<String, Integer> stats = dao.countByCategorie();
            ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
            for (Map.Entry<String, Integer> e : stats.entrySet()) {
                data.add(new PieChart.Data(
                    e.getKey() + " (" + e.getValue() + ")",
                    e.getValue()
                ));
            }
            graphique.setData(data);

        } catch (SQLException e) {
            afficherErreur("Erreur : " + e.getMessage());
        }
    }
    private VBox buildTop() {
        tfRecherche.setPromptText("Rechercher par nom ou prénom...");

        tfRecherche.textProperty().addListener((obs, ancien, nouveau) -> {
            try {
                contacts.setAll(dao.findByName(nouveau));
            } catch (SQLException e) {
                afficherErreur(e.getMessage());
            }
        });

        VBox haut = new VBox(10, tfRecherche, graphique);
        haut.setPadding(new Insets(10));

        return haut;
    }
    
    private VBox buildForm() {
        cbCategorie.getItems().addAll("Famille", "Travail", "Ami", "Autre");
        cbCategorie.setValue("Autre");

        VBox box = new VBox(8,
            new Label("Nom"),       tfNom,
            new Label("Prénom"),    tfPrenom,
            new Label("Email"),     tfEmail,
            new Label("Téléphone"), tfTelephone,
            new Label("Catégorie"), cbCategorie
        );
        box.setPadding(new Insets(10));
        box.setPrefWidth(260);
        return box;
    }

    private HBox buildButtons() {
        HBox box = new HBox(10, bAjouter, bModifier, bSupprimer, bEffacer);
        box.setPadding(new Insets(10));
        return box;
    }
    
    private void cablerEvenements() {

        // Sélection d'une ligne : remplit le formulaire
        table.getSelectionModel().selectedItemProperty()
            .addListener((obs, ancien, nouveau) -> {
                if (nouveau != null) remplirFormulaire(nouveau);
            });

        // Bouton AJOUTER
        bAjouter.setOnAction(e -> {
            try {
                Contact c = lireFormulaire();
                dao.insert(c);
                rafraichir();
                effacerFormulaire();
            } catch (SQLException ex) {
                afficherErreur("Ajout impossible : " + ex.getMessage());
            }
        });

        // Bouton MODIFIER
        bModifier.setOnAction(e -> {
            Contact sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { afficherErreur("Sélectionnez un contact."); return; }
            try {
                Contact c = lireFormulaire();
                c.setId(sel.getId());        // reprend l'ID de la ligne sélectionnée
                dao.update(c);
                rafraichir();
            } catch (SQLException ex) {
                afficherErreur("Modification impossible : " + ex.getMessage());
            }
        });

        // Bouton SUPPRIMER
        bSupprimer.setOnAction(e -> {
            Contact sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { afficherErreur("Sélectionnez un contact."); return; }
            try {
                dao.delete(sel.getId());
                rafraichir();
                effacerFormulaire();
            } catch (SQLException ex) {
                afficherErreur("Suppression impossible : " + ex.getMessage());
            }
        });

        // Bouton EFFACER (vide les champs)
        bEffacer.setOnAction(e -> effacerFormulaire());
    }

    private Contact lireFormulaire() {
        Contact c = new Contact();
        c.setNom(tfNom.getText().trim());
        c.setPrenom(tfPrenom.getText().trim());
        c.setEmail(tfEmail.getText().trim());
        c.setTelephone(tfTelephone.getText().trim());
        c.setCategorie(cbCategorie.getValue());
        return c;
    }

    private void remplirFormulaire(Contact c) {
        tfNom.setText(c.getNom());
        tfPrenom.setText(c.getPrenom());
        tfEmail.setText(c.getEmail());
        tfTelephone.setText(c.getTelephone());
        cbCategorie.setValue(c.getCategorie());
    }

    private void effacerFormulaire() {
        tfNom.clear();
        tfPrenom.clear();
        tfEmail.clear();
        tfTelephone.clear();
        cbCategorie.setValue("Autre");
        table.getSelectionModel().clearSelection();
    }

    private void afficherErreur(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).show();
    }
}