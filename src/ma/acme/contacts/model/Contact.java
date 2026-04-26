package ma.acme.contacts.model;

/**
 * Un contact du carnet : une ligne de la table SQL "contact".
 */
public class Contact {

    // --- Attributs (une colonne SQL = un attribut Java) ---
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String categorie;

    // Constructeur vide : utile pour le formulaire.
    public Contact() {}

    // Constructeur complet : utile pour remplir depuis la BDD.
    public Contact(int id, String nom, String prenom,
                   String email, String telephone, String categorie) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.categorie = categorie;
    }

    // --- Getters / Setters (obligatoires pour JavaFX) ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }
}