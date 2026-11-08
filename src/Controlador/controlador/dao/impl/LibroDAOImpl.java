package controlador.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import controlador.dao.ConexionDB;

import biblioteca.libro.Libro;
import controlador.dao.LibroDAO;

public class LibroDAOImpl implements LibroDAO {
    // ========================= SQL =========================

    private static final String SQL_INSERTAR = "INSERT INTO libro (titulo, edicion, editorial, anio, idBiblioteca) VALUES (?, ?, ?, ?, ?)";

    private static final String SQL_ELIMINAR = "DELETE FROM libro WHERE idLibro = ?";

    private static final String SQL_LISTAR_TODOS = "SELECT l.idLibro, l.titulo, l.edicion, l.editorial, l.anio, " +
                    "CASE WHEN EXISTS (" + "SELECT 1 FROM prestamo p " +  "WHERE p.idLibro = l.idLibro AND p.fechaDevolucion IS NULL" +") " +
                    "THEN 'Si' ELSE 'No' END AS prestado " + "FROM libro l WHERE l.idBiblioteca = ?";

    // ====================== ATRIBUTOS ======================

    /** Conexión JDBC reutilizada en cada operación. */
    private Connection conexion;

    // ===================== CONSTRUCTOR =====================

    /**
     * Crea el DAO obteniendo la conexión activa desde {@link ConexionDB}.
     *
     * @throws SQLException si no se puede establecer la conexión.
     */
    public LibroDAOImpl() throws SQLException {
        this.conexion = ConexionDB.getConexion();
    }

    // ======================== CRUD =========================

    /**
     * Inserta un nuevo libro en la base de datos.
     *
     * @param p_libro        Objeto {@link Libro} a persistir.
     * @param p_idBiblioteca Identificador de la biblioteca a la que pertenece.
     * @return El {@code idLibro} generado por la base de datos, o {@code -1} si
     *         falló.
     * @author (Fernández, Lucía Belén)
     */
    public void insertar(Libro p_libro, int p_idBiblioteca) {
        int idGenerado = -1;
        try {
            PreparedStatement ps = this.conexion.prepareStatement(SQL_INSERTAR, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, p_libro.getTitulo());
            ps.setInt(2, p_libro.getEdicion());
            ps.setString(3, p_libro.getEditorial());
            ps.setInt(4, p_libro.getAnio());
            ps.setInt(5, p_idBiblioteca);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                idGenerado = rs.getInt(1);
            }
            rs.close();
            ps.close();
            p_libro.setIdLibro(idGenerado);
            System.out.println("[LibroDAO] Libro guardado: " + p_libro.getTitulo() + " (id=" + idGenerado + ")");
        } catch (SQLException e) {
            System.err.println("[LibroDAO] Error al guardar: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

        /**
     * Elimina un libro de la base de datos por su identificador.
     *
     * @param p_idLibro Identificador del libro a eliminar.
     * @return {@code true} si se eliminó al menos una fila.
     * @author (Fernández, Lucía Belén)
     */
    public boolean eliminar(int p_idLibro) {
        boolean exito = false;
        try {
            PreparedStatement ps = this.conexion.prepareStatement(SQL_ELIMINAR);
            ps.setInt(1, p_idLibro);
            exito = ps.executeUpdate() > 0;
            ps.close();
            if (exito) {
                System.out.println("[LibroDAO] Libro eliminado (id=" + p_idLibro + ")");
            }
        } catch (SQLException e) {
            System.err.println("[LibroDAO] Error al eliminar: " + e.getMessage());
        }
        return exito;
    }

        /**
     * Recupera todos los libros de una biblioteca.
     *
     * @param p_idBiblioteca Identificador de la biblioteca.
     * @return Lista de objetos {@link Libro}; vacía si no hay registros.
     * @author (Fernández, Lucía Belén)
     */
    public List<Libro> obtenerPorBiblioteca(int p_idBiblioteca) {
        ArrayList<Libro> libros = new ArrayList<Libro>();
        try {
            PreparedStatement ps = this.conexion.prepareStatement(SQL_LISTAR_TODOS);
            ps.setInt(1, p_idBiblioteca);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Libro libro = new Libro(
                        rs.getString("titulo"),
                        rs.getInt("edicion"),
                        rs.getString("editorial"),
                        rs.getInt("anio"));
                libro.setIdLibro(rs.getInt("idLibro"));
                libros.add(libro);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.err.println("[LibroDAO] Error al listar: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return libros;
    }

    /**
     * Recupera los datos de los libros junto con su identificador.
     *
     * @param p_idBiblioteca identificador de la biblioteca.
     * @return lista de filas con ID, título, edición, editorial y año.
     */
    @Override
    public List<Object[]> obtenerFilasConIdPorBiblioteca(int p_idBiblioteca) {
        List<Object[]> filas = new ArrayList<>();

        try {
            PreparedStatement ps = this.conexion.prepareStatement(SQL_LISTAR_TODOS);
            ps.setInt(1, p_idBiblioteca);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Object[] fila = {
                        rs.getInt("idLibro"),
                        rs.getString("titulo"),
                        rs.getInt("edicion"),
                        rs.getString("editorial"),
                        rs.getInt("anio"),
                        rs.getString("prestado")
                };

                filas.add(fila);
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            System.err.println("[LibroDAO] Error al listar libros con ID: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return filas;
    }
}