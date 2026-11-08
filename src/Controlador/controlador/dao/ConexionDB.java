package controlador.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Gestiona la conexión JDBC a la base de datos SQLite {@code biblioteca.db}.
 * <p>
 * Utiliza el patrón Singleton para garantizar que toda la aplicación
 * comparta una única conexión abierta. Al conectar, activa las
 * claves foráneas ({@code PRAGMA foreign_keys = ON}) tal como lo
 * indica el script {@code biblioteca.sql}.
 * </p>
 *
 * @author (Fernández, Lucía Belén)
 */
public class ConexionDB {

    /**
     * Ruta relativa al archivo SQLite. Se ubica en {@code src/Modelo/}
     * siguiendo la estructura existente del proyecto.
     */
    private static final String URL = "jdbc:sqlite:src/Recursos/biblioteca.db";

    /** Instancia única de la conexión (Singleton). */
    private static Connection conexion = null;

    /**
     * Constructor privado para impedir la instanciación externa.
     */
    private ConexionDB() {
    }

    /**
     * Devuelve la conexión activa a la base de datos. Si aún no existe
     * o fue cerrada, la crea y activa las claves foráneas.
     *
     * @return Objeto {@link Connection} listo para operar.
     * @throws SQLException si ocurre un error al establecer la conexión.
     */
    public static Connection getConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            conexion = DriverManager.getConnection(URL);
            // Activar claves foráneas (SQLite las ignora por defecto)
            Statement stmt = conexion.createStatement();
            stmt.execute("PRAGMA foreign_keys = ON");
            stmt.close();
            System.out.println("[ConexionDB] Conexión establecida con biblioteca.db");
        }
        return conexion;
    }

    /**
     * Cierra la conexión activa, si existe y está abierta.
     */
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
