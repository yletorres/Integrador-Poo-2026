package controlador.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import biblioteca.socio.Docente;
import biblioteca.socio.Estudiante;
import biblioteca.socio.Socio;
import controlador.dao.SocioDAO;
import controlador.dao.ConexionDB;

/**
 * Data Access Object para la jerarquía {@link Socio} / {@link Docente} /
 * {@link Estudiante}.
 * <p>
 * Respeta el modelo de herencia por tablas separadas definido en
 * {@code biblioteca.sql}: la tabla {@code socio} almacena los datos comunes
 * y las tablas {@code docente} / {@code estudiante} los específicos de cada
 * subclase.
 * </p>
 *
 * @author (Fernández, Lucía Belén)
 */
public class SocioDAOImpl implements SocioDAO {

    // ========================= SQL =========================

    private static final String SQL_INSERTAR_SOCIO = "INSERT INTO socio (dniSocio, nombre, diasPrestamo, tipo, idBiblioteca) VALUES (?, ?, ?, ?, ?)";

    private static final String SQL_INSERTAR_DOCENTE = "INSERT INTO docente (dniSocio, area) VALUES (?, ?)";

    private static final String SQL_INSERTAR_ESTUDIANTE = "INSERT INTO estudiante (dniSocio, carrera) VALUES (?, ?)";

    private static final String SQL_ELIMINAR_DOCENTE = "DELETE FROM docente WHERE dniSocio = ?";

    private static final String SQL_ELIMINAR_ESTUDIANTE = "DELETE FROM estudiante WHERE dniSocio = ?";

    private static final String SQL_ELIMINAR_SOCIO = "DELETE FROM socio WHERE dniSocio = ?";

    private static final String SQL_LISTAR_TODOS = "SELECT s.dniSocio, s.nombre, s.diasPrestamo, s.tipo, " +
            "d.area, e.carrera " +
            "FROM socio s " +
            "LEFT JOIN docente    d ON s.dniSocio = d.dniSocio " +
            "LEFT JOIN estudiante e ON s.dniSocio = e.dniSocio " +
            "WHERE s.idBiblioteca = ?";

    // ====================== ATRIBUTOS ======================

    /** Conexión JDBC reutilizada en cada operación. */
    private Connection conexion;

    // ===================== CONSTRUCTOR =====================

    /**
     * Crea el DAO obteniendo la conexión activa desde {@link ConexionDB}.
     *
     * @throws SQLException si no se puede establecer la conexión.
     */
    public SocioDAOImpl() throws SQLException {
        this.conexion = ConexionDB.getConexion();
    }

    // ======================== CRUD =========================

    /**
     * Inserta un socio (docente o estudiante) en las tablas correspondientes.
     * Primero inserta en {@code socio}, luego en la subtabla según el tipo.
     *
     * @param p_socio        Objeto {@link Socio} (será {@link Docente} o
     *                       {@link Estudiante}).
     * @param p_idBiblioteca Identificador de la biblioteca a la que pertenece.
     * @author (Fernández, Lucía Belén)
     */
    public void insertar(Socio p_socio, int p_idBiblioteca) {
        try {
            this.conexion.setAutoCommit(false);
            // 1. Insertar en la tabla socio
            PreparedStatement psSocio = this.conexion.prepareStatement(SQL_INSERTAR_SOCIO);
            psSocio.setInt(1, p_socio.getDniSocio());
            psSocio.setString(2, p_socio.getNombre());
            psSocio.setInt(3, p_socio.getDiasPrestamo());
            psSocio.setString(4, p_socio.soyDeLaClase());
            psSocio.setInt(5, p_idBiblioteca);
            psSocio.executeUpdate();
            psSocio.close();

            // 2. Insertar en la subtabla correspondiente
            if (p_socio instanceof Docente) {
                Docente docente = (Docente) p_socio;
                PreparedStatement psDoc = this.conexion.prepareStatement(SQL_INSERTAR_DOCENTE);
                psDoc.setInt(1, docente.getDniSocio());
                psDoc.setString(2, docente.getArea());
                psDoc.executeUpdate();
                psDoc.close();
            } else if (p_socio instanceof Estudiante) {
                Estudiante estudiante = (Estudiante) p_socio;
                PreparedStatement psEst = this.conexion.prepareStatement(SQL_INSERTAR_ESTUDIANTE);
                psEst.setInt(1, estudiante.getDniSocio());
                psEst.setString(2, estudiante.getCarrera());
                psEst.executeUpdate();
                psEst.close();
            }

            System.out.println("[SocioDAO] Socio guardado: " + p_socio.getNombre()
                    + " (DNI=" + p_socio.getDniSocio() + ", " + p_socio.soyDeLaClase() + ")");
            this.conexion.commit();
        } catch (SQLException e) {
            System.err.println("[SocioDAO] Error al guardar: " + e.getMessage());
            try {
                this.conexion.rollback();
            } catch (SQLException ignorada) {
            }
            throw new RuntimeException(e);
        } finally {
            try {
                this.conexion.setAutoCommit(true);
            } catch (SQLException ignorada) {
            }
        }
    }

    /**
     * Elimina un socio de la base de datos. Borra primero la subtabla
     * y luego la fila padre en {@code socio} para respetar las FK.
     *
     * @param p_dniSocio DNI del socio a eliminar.
     * @param p_tipo     Tipo del socio: {@code "Docente"} o {@code "Estudiante"}.
     * @return {@code true} si se eliminó al menos una fila.
     * @author (Fernández, Lucía Belén)
     */
    public boolean eliminar(int p_dniSocio, String p_tipo) {
        boolean exito = false;
        try {
            // 1. Eliminar de la subtabla primero (FK)
            String sqlSub = p_tipo.equals("Docente") ? SQL_ELIMINAR_DOCENTE : SQL_ELIMINAR_ESTUDIANTE;
            PreparedStatement psSub = this.conexion.prepareStatement(sqlSub);
            psSub.setInt(1, p_dniSocio);
            psSub.executeUpdate();
            psSub.close();

            // 2. Eliminar de la tabla socio
            PreparedStatement psSocio = this.conexion.prepareStatement(SQL_ELIMINAR_SOCIO);
            psSocio.setInt(1, p_dniSocio);
            exito = psSocio.executeUpdate() > 0;
            psSocio.close();

            if (exito) {
                System.out.println("[SocioDAO] Socio eliminado (DNI=" + p_dniSocio + ")");
            }
        } catch (SQLException e) {
            System.err.println("[SocioDAO] Error al eliminar: " + e.getMessage());
        }
        return exito;
    }

        /**
     * Recupera todos los socios de una biblioteca.
     *
     * @param p_idBiblioteca Identificador de la biblioteca.
     * @return Lista de objetos {@link Socio}; vacía si no hay registros.
     * @author (Fernández, Lucía Belén)
     */
    public List<Socio> obtenerPorBiblioteca(int p_idBiblioteca) {
        ArrayList<Socio> socios = new ArrayList<Socio>();
        try {
            PreparedStatement ps = this.conexion.prepareStatement(SQL_LISTAR_TODOS);
            ps.setInt(1, p_idBiblioteca);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                socios.add(construirSocio(rs));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.err.println("[SocioDAO] Error al listar: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return socios;
    }

    // ====================== AUXILIAR =======================

    /**
     * Construye la instancia concreta de {@link Socio} a partir del
     * {@link ResultSet} actual, leyendo el campo {@code tipo} para
     * decidir si crear un {@link Docente} o un {@link Estudiante}.
     *
     * @param rs ResultSet posicionado en la fila a leer.
     * @return Instancia concreta del socio.
     * @throws SQLException si ocurre un error al leer las columnas.
     */
    private Socio construirSocio(ResultSet rs) throws SQLException {
        String tipo = rs.getString("tipo");
        int dni = rs.getInt("dniSocio");
        String nombre = rs.getString("nombre");

        if (tipo.equals("Docente")) {
            String area = rs.getString("area");
            Docente docente = new Docente(dni, nombre, area);
            // Restaurar los días reales de la BD (puede diferir del default 5)
            int diasBD = rs.getInt("diasPrestamo");
            if (diasBD != docente.getDiasPrestamo()) {
                docente.cambiarDiasDePrestamo(diasBD - docente.getDiasPrestamo());
            }
            return docente;
        } else {
            String carrera = rs.getString("carrera");
            return new Estudiante(dni, nombre, carrera);
        }
    }
}
