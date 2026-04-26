package ma.acme.contacts.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Ouvre une connexion à la base PostgreSQL.
 * Adaptez USER et PASSWORD selon votre installation.
 */
public class Database {

    // Format d'URL JDBC : jdbc:postgresql://[host]:[port]/[nom_base]
    private static final String URL      = "jdbc:postgresql://localhost:5432/carnet_contacts";
    private static final String USER     = "postgres";
    private static final String PASSWORD = "1234";   // ← à remplacer

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}