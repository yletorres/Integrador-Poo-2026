package biblioteca;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionDB {

    // Ruta directa a la raíz del proyecto
    private static final String URL = "jdbc:sqlite:biblioteca.db";

    private static Connection conexion = null;

    private ConexionDB() {
    }

    public static Connection getConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            conexion = DriverManager.getConnection(URL);
            Statement stmt = conexion.createStatement();
            stmt.execute("PRAGMA foreign_keys = ON");
            stmt.close();
            System.out.println("[ConexionDB] Conexión establecida con biblioteca.db en la raíz.");
        }
        return conexion;
    }

    public static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("[ConexionDB] Conexión cerrada.");
            }
        } catch (SQLException e) {
            System.err.println("[ConexionDB] Error al cerrar: " + e.getMessage());
        }
    }
}