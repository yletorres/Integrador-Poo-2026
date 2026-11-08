package controlador.dao;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

    /** Ruta de la base cuando se ejecuta desde el código fuente. */
    private static final Path BASE_DESARROLLO = Paths.get("src", "Recursos", "biblioteca.db");

    /** Nombre del recurso empaquetado con la base inicial. */
    private static final String RECURSO_BASE = "/biblioteca.db";

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
            conexion = DriverManager.getConnection("jdbc:sqlite:" + rutaBase());
            // Activar claves foráneas (SQLite las ignora por defecto)
            Statement stmt = conexion.createStatement();
            stmt.execute("PRAGMA foreign_keys = ON");
            stmt.close();
            System.out.println("[ConexionDB] Conexión establecida con biblioteca.db");
        }
        return conexion;
    }

    private static Path rutaBase() throws SQLException {
        if (Files.exists(BASE_DESARROLLO)) {
            return BASE_DESARROLLO.toAbsolutePath();
        }
        String datos = System.getenv("APPDATA");
        Path carpeta = datos != null ? Paths.get(datos, "Biblioteca")
                : Paths.get(System.getProperty("user.home"), ".biblioteca");
        Path base = carpeta.resolve("biblioteca.db");
        if (!Files.exists(base)) {
            try (InputStream inicial = ConexionDB.class.getResourceAsStream(RECURSO_BASE)) {
                if (inicial == null) {
                    throw new SQLException("No se encontró la base de datos inicial");
                }
                Files.createDirectories(carpeta);
                Files.copy(inicial, base, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new SQLException("No se pudo crear la base de datos en " + carpeta, e);
            }
        }
        return base;
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
