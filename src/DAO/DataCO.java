package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataCO {

    // Paramètres de connexion à la base de données
    private static final String URL = "jdbc:mysql://localhost:3306/shopping2025";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    /**
     * Renvoie une connexion à la base de données.
     *
     * @return Connection vers la BDD shopping2025.
     * @throws SQLException si la connexion échoue.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void main(String[] args) {
        // Test de la connexion à la BDD
        try (Connection connection = getConnection()) {
            System.out.println("Connexion réussie !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la connexion à la base de données :");
            e.printStackTrace();
        }
    }
}
