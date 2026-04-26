package ma.acme.contacts.dao;

import ma.acme.contacts.model.Contact;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Couche d'accès aux données pour la table "contact".
 */
public class ContactDao {

    /**
     * Récupère tous les contacts, triés par nom puis prénom.
     */
    public List<Contact> findAll() throws SQLException {
        String sql = "SELECT id, nom, prenom, email, telephone, categorie "
                   + "FROM contact ORDER BY nom, prenom";

        List<Contact> result = new ArrayList<>();

        // try-with-resources : ferme automatiquement Connection, Statement et ResultSet.
        try (Connection cn = Database.getConnection();
             Statement st  = cn.createStatement();
             ResultSet rs  = st.executeQuery(sql)) {

            while (rs.next()) {
                result.add(new Contact(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("email"),
                    rs.getString("telephone"),
                    rs.getString("categorie")
                ));
            }
        }
        return result;
    }
    
    /** Compte les contacts par catégorie : { "Ami" => 2, "Travail" => 3, ... } */
    public Map<String, Integer> countByCategorie() throws SQLException {
        String sql = "SELECT categorie, COUNT(*) AS nb "
                   + "FROM contact GROUP BY categorie ORDER BY categorie";

        Map<String, Integer> result = new LinkedHashMap<>();

        try (Connection cn = Database.getConnection();
             Statement st  = cn.createStatement();
             ResultSet rs  = st.executeQuery(sql)) {

            while (rs.next()) {
                result.put(rs.getString("categorie"), rs.getInt("nb"));
            }
        }
        return result;
    }
    
    /** Insère un nouveau contact. L'ID est généré par la BDD et réinjecté dans l'objet. */
    public void insert(Contact c) throws SQLException {
        String sql = "INSERT INTO contact(nom, prenom, email, telephone, categorie) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection cn = Database.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql,
                                     Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, c.getNom());
            ps.setString(2, c.getPrenom());
            ps.setString(3, c.getEmail());
            ps.setString(4, c.getTelephone());
            ps.setString(5, c.getCategorie());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) c.setId(keys.getInt(1));
            }
        }
    }

    /** Met à jour un contact existant, identifié par son ID. */
    public void update(Contact c) throws SQLException {
        String sql = "UPDATE contact SET nom=?, prenom=?, email=?, "
                   + "telephone=?, categorie=? WHERE id=?";

        try (Connection cn = Database.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, c.getNom());
            ps.setString(2, c.getPrenom());
            ps.setString(3, c.getEmail());
            ps.setString(4, c.getTelephone());
            ps.setString(5, c.getCategorie());
            ps.setInt(6, c.getId());
            ps.executeUpdate();
        }
    }

    /** Supprime un contact par son ID. */
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM contact WHERE id=?";

        try (Connection cn = Database.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
    
    public List<Contact> findByName(String motCle) throws SQLException {
        String sql = "SELECT id, nom, prenom, email, telephone, categorie "
                   + "FROM contact "
                   + "WHERE LOWER(nom) LIKE ? OR LOWER(prenom) LIKE ? "
                   + "ORDER BY nom, prenom";

        List<Contact> result = new ArrayList<>();
        String pattern = "%" + motCle.toLowerCase() + "%";

        try (Connection cn = Database.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(new Contact(
                        rs.getInt("id"), rs.getString("nom"),
                        rs.getString("prenom"), rs.getString("email"),
                        rs.getString("telephone"), rs.getString("categorie")
                    ));
                }
            }
        }
        return result;
    }
}