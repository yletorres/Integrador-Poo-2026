package controlador.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import biblioteca.Biblioteca;
import biblioteca.libro.Libro;
import biblioteca.socio.Socio;
import controlador.dao.BibliotecaDAO;
import controlador.dao.LibroDAO;
import controlador.dao.PrestamoDAO;
import controlador.dao.SocioDAO;

/**
 * Data Access Object para la entidad {@link Biblioteca}.
 * Implementa las operaciones definidas en {@link BibliotecaDAO}.
 *
 * @author (Fernández, Lucía Belén)
 */
public class BibliotecaDAOImpl implements BibliotecaDAO {

    // ========================= SQL =========================

    private static final String SQL_OBTENER_NOMBRES = "SELECT nombre FROM biblioteca ORDER BY nombre";

    private static final String SQL_BUSCAR_POR_NOMBRE = "SELECT idBiblioteca, nombre FROM biblioteca WHERE nombre = ?";

    private static final String SQL_OBTENER_ID_POR_NOMBRE = "SELECT idBiblioteca FROM biblioteca WHERE nombre = ?";

    private static final String SQL_INSERTAR = "INSERT INTO biblioteca (nombre) VALUES (?)";

    // ====================== ATRIBUTOS ======================

    private Connection conexion;
    private LibroDAO libroDAO;
    private SocioDAO socioDAO;
    private PrestamoDAO prestamoDAO;

    // ===================== CONSTRUCTOR =====================

    /**
     * Constructor por defecto que obtiene la conexión activa.
     *
     * @throws SQLException si ocurre un error al conectar.
     */
    public BibliotecaDAOImpl() throws SQLException {
        this.conexion = ConexionDB.getConexion();
        this.libroDAO = new LibroDAOImpl();
        this.socioDAO = new SocioDAOImpl();
        this.prestamoDAO = new PrestamoDAOImpl();
    }

    /**
     * Constructor con inyección de DAOs dependientes.
     */
    public BibliotecaDAOImpl(LibroDAO p_libroDAO, SocioDAO p_socioDAO, PrestamoDAO p_prestamoDAO) throws SQLException {
        this.conexion = ConexionDB.getConexion();
        this.libroDAO = p_libroDAO;
        this.socioDAO = p_socioDAO;
        this.prestamoDAO = p_prestamoDAO;
    }

    // ===================== MÉTODOS DEL CONTRATO =====================

    @Override
    public List<String> obtenerNombres() {
        List<String> nombres = new ArrayList<>();
        try {
            PreparedStatement ps = this.conexion.prepareStatement(SQL_OBTENER_NOMBRES);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                nombres.add(rs.getString("nombre"));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.err.println("[BibliotecaDAO] Error al obtener nombres: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return nombres;
    }

    @Override
    public Biblioteca buscarPorNombre(String nombre) {
        Biblioteca biblioteca = null;
        try {
            PreparedStatement ps = this.conexion.prepareStatement(SQL_BUSCAR_POR_NOMBRE);
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int idBiblioteca = rs.getInt("idBiblioteca");
                String nom = rs.getString("nombre");

                // Cargar libros y socios de la biblioteca
                List<Libro> libros = this.libroDAO.obtenerPorBiblioteca(idBiblioteca);
                List<Socio> socios = this.socioDAO.obtenerPorBiblioteca(idBiblioteca);

                // Cargar y vincular préstamos
                this.prestamoDAO.cargarPrestamos(libros, socios, idBiblioteca);

                // Armar el objeto Biblioteca
                ArrayList<Libro> listaLibros = new ArrayList<>(libros);
                ArrayList<Socio> listaSocios = new ArrayList<>(socios);
                biblioteca = new Biblioteca(nom, listaSocios, listaLibros);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.err.println("[BibliotecaDAO] Error al buscar por nombre: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return biblioteca;
    }

    @Override
    public int obtenerIdPorNombre(String nombre) {
        int id = -1;
        try {
            PreparedStatement ps = this.conexion.prepareStatement(SQL_OBTENER_ID_POR_NOMBRE);
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                id = rs.getInt("idBiblioteca");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.err.println("[BibliotecaDAO] Error al obtener id por nombre: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return id;
    }

    @Override
    public void insertar(Biblioteca biblioteca) {
        try {
            PreparedStatement ps = this.conexion.prepareStatement(SQL_INSERTAR);
            ps.setString(1, biblioteca.getNombre());
            ps.executeUpdate();
            ps.close();
            System.out.println("[BibliotecaDAO] Biblioteca insertada: " + biblioteca.getNombre());
        } catch (SQLException e) {
            System.err.println("[BibliotecaDAO] Error al insertar: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
