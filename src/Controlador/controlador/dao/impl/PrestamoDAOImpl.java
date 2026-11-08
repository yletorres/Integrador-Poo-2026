package controlador.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import biblioteca.libro.Libro;
import biblioteca.prestamo.Prestamo;
import biblioteca.socio.Socio;
import controlador.dao.PrestamoDAO;
import controlador.dao.ConexionDB;

/**
 * Data Access Object para la entidad {@link Prestamo}.
 * <p>
 * Las fechas se persisten como texto {@code 'AAAA-MM-DD'} tal como
 * lo define {@code biblioteca.sql}. El campo {@code fechaDevolucion}
 * puede ser {@code NULL} cuando el libro aún no fue devuelto.
 * </p>
 * <p>
 * Cada préstamo se vincula con su ejemplar por {@code idLibro}, no por título,
 * porque un título puede tener varios ejemplares.
 * </p>
 *
 * @author (Fernández, Lucía Belén)
 */
public class PrestamoDAOImpl implements PrestamoDAO {

    private static final String SQL_INSERTAR = "INSERT INTO prestamo (fechaRetiro, fechaDevolucion, dniSocio, idLibro) "
            + "VALUES (?, ?, ?, ?)";

    private static final String SQL_REGISTRAR_DEVOLUCION_ID = "UPDATE prestamo SET fechaDevolucion = ? WHERE idPrestamo = ?";

    private static final String SQL_LISTAR_TODOS = "SELECT p.idPrestamo, p.fechaRetiro, p.fechaDevolucion, p.dniSocio, p.idLibro "
            + "FROM prestamo p "
            + "INNER JOIN libro l ON p.idLibro = l.idLibro "
            + "WHERE l.idBiblioteca = ? "
            + "ORDER BY p.fechaRetiro, p.idPrestamo";

    /** Conexión JDBC reutilizada en cada operación. */
    private Connection conexion;

    /**
     * Relaciona cada {@link Prestamo} del modelo con su idPrestamo en la base de datos.
     * Usa IdentityHashMap para comparar por referencia (el mismo objeto Java) y no por
     * equals(), porque el modelo no guarda el id. Se completa al cargar los préstamos
     * y al insertar uno nuevo, y se usa para registrar la devolución del préstamo correcto.
     */
    private final Map<Prestamo, Integer> idsPorPrestamo = new IdentityHashMap<>();

    /** Formato de fecha compatible con el esquema SQLite ('AAAA-MM-DD'). */
    private static final SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Crea el DAO obteniendo la conexión activa desde {@link ConexionDB}.
     *
     * @throws SQLException si no se puede establecer la conexión.
     */
    public PrestamoDAOImpl() throws SQLException {
        this.conexion = ConexionDB.getConexion();
    }

    /**
     * Carga y vincula los préstamos de una biblioteca a sus respectivos libros y socios.
     * Cada préstamo se asocia al ejemplar con el mismo {@code idLibro}.
     *
     * @param libros        Colección de libros de la biblioteca.
     * @param socios        Colección de socios de la biblioteca.
     * @param idBiblioteca  Identificador de la biblioteca.
     */
    @Override
    public void cargarPrestamos(List<Libro> libros, List<Socio> socios, int idBiblioteca) {
        this.idsPorPrestamo.clear();
        try {
            PreparedStatement ps = this.conexion.prepareStatement(SQL_LISTAR_TODOS);
            ps.setInt(1, idBiblioteca);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int dni = rs.getInt("dniSocio");
                int idLibro = rs.getInt("idLibro");
                String strRetiro = rs.getString("fechaRetiro");
                String strDevolucion = rs.getString("fechaDevolucion");

                Socio socioEncontrado = null;
                for (Socio s : socios) {
                    if (s.getDniSocio() == dni) {
                        socioEncontrado = s;
                        break;
                    }
                }

                Libro libroEncontrado = null;
                for (Libro l : libros) {
                    if (l.getIdLibro() == idLibro) {
                        libroEncontrado = l;
                        break;
                    }
                }

                if (socioEncontrado != null && libroEncontrado != null) {
                    Prestamo prestamo = new Prestamo(parsearFecha(strRetiro), socioEncontrado, libroEncontrado);
                    if (strDevolucion != null) {
                        prestamo.registrarFechaDevolucion(parsearFecha(strDevolucion));
                    }
                    this.idsPorPrestamo.put(prestamo, rs.getInt("idPrestamo"));
                    libroEncontrado.agregarPrestamo(prestamo);
                    socioEncontrado.agregarPrestamo(prestamo);
                }
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.err.println("[PrestamoDAO] Error al cargar préstamos: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Inserta un nuevo préstamo en la base de datos, asociado al ejemplar exacto
     * (por su {@code idLibro}) que figura en el préstamo.
     *
     * @param p_prestamo     Objeto {@link Prestamo} a persistir.
     * @param p_idBiblioteca Identificador de la biblioteca.
     */
    @Override
    public void insertar(Prestamo p_prestamo, int p_idBiblioteca) {
        try {
            PreparedStatement ps = this.conexion.prepareStatement(SQL_INSERTAR);
            ps.setString(1, FORMATO_FECHA.format(p_prestamo.getFechaRetiro().getTime()));

            if (p_prestamo.getFechaDevolucion() != null) {
                ps.setString(2, FORMATO_FECHA.format(p_prestamo.getFechaDevolucion().getTime()));
            } else {
                ps.setNull(2, java.sql.Types.VARCHAR);
            }

            ps.setInt(3, p_prestamo.getSocio().getDniSocio());
            ps.setInt(4, p_prestamo.getLibro().getIdLibro());
            ps.executeUpdate();
            ps.close();

            try (Statement st = this.conexion.createStatement();
                 ResultSet rs = st.executeQuery("SELECT last_insert_rowid()")) {
                if (rs.next()) {
                    this.idsPorPrestamo.put(p_prestamo, rs.getInt(1));
                }
            }
            System.out.println("[PrestamoDAO] Préstamo guardado (libro=" + p_prestamo.getLibro().getIdLibro()
                    + ", socio=" + p_prestamo.getSocio().getDniSocio() + ")");
        } catch (SQLException e) {
            System.err.println("[PrestamoDAO] Error al guardar: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Registra la fecha de devolución de un préstamo, identificándolo por su id en la base.
     *
     * @param p_prestamo        Préstamo del modelo que se devolvió.
     * @param p_fechaDevolucion Fecha de devolución.
     * @throws IllegalStateException si el préstamo no fue cargado ni insertado desde la base.
     */
    @Override
    public void registrarDevolucion(Prestamo p_prestamo, Calendar p_fechaDevolucion) {
        Integer id = this.idsPorPrestamo.get(p_prestamo);
        if (id == null) {
            throw new IllegalStateException("El préstamo no está guardado en la base de datos");
        }
        try {
            PreparedStatement ps = this.conexion.prepareStatement(SQL_REGISTRAR_DEVOLUCION_ID);
            ps.setString(1, FORMATO_FECHA.format(p_fechaDevolucion.getTime()));
            ps.setInt(2, id);
            ps.executeUpdate();
            ps.close();
            System.out.println("[PrestamoDAO] Devolución registrada (idPrestamo=" + id + ")");
        } catch (SQLException e) {
            System.err.println("[PrestamoDAO] Error al registrar devolución: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private Calendar parsearFecha(String p_fecha) {
        String[] partes = p_fecha.split("-");
        int anio = Integer.parseInt(partes[0]);
        int mes = Integer.parseInt(partes[1]) - 1;
        int dia = Integer.parseInt(partes[2]);
        return new GregorianCalendar(anio, mes, dia);
    }
}
