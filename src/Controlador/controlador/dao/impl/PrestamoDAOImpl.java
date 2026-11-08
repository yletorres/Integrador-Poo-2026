package controlador.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import biblioteca.libro.Libro;
import biblioteca.prestamo.Prestamo;
import biblioteca.socio.Socio;
import controlador.dao.LibroDAO;
import controlador.dao.PrestamoDAO;
import controlador.dao.SocioDAO;
import controlador.dao.ConexionDB;

/**
 * Data Access Object para la entidad {@link Prestamo}.
 * <p>
 * Las fechas se persisten como texto {@code 'AAAA-MM-DD'} tal como
 * lo define {@code biblioteca.sql}. El campo {@code fechaDevolucion}
 * puede ser {@code NULL} cuando el libro aún no fue devuelto.
 * </p>
 *
 * @author (Fernández, Lucía Belén)
 */
public class PrestamoDAOImpl implements PrestamoDAO {

    // ========================= SQL =========================

    private static final String SQL_INSERTAR_CON_BIBLIO = "INSERT INTO prestamo (fechaRetiro, fechaDevolucion, dniSocio, idLibro) "
            + "VALUES (?, ?, ?, (SELECT l.idLibro FROM libro l WHERE l.titulo = ? AND l.idBiblioteca = ? LIMIT 1))";

    private static final String SQL_INSERTAR_CON_LIBRO = "INSERT INTO prestamo (fechaRetiro, fechaDevolucion, dniSocio, idLibro) VALUES (?, ?, ?, ?)";

    private static final String SQL_MODIFICAR = "UPDATE prestamo SET fechaRetiro = ?, fechaDevolucion = ?, dniSocio = ?, idLibro = ? WHERE idPrestamo = ?";

    private static final String SQL_REGISTRAR_DEVOLUCION_TITULO = "UPDATE prestamo SET fechaDevolucion = ? WHERE idPrestamo = ("
            + "SELECT p.idPrestamo FROM prestamo p "
            + "INNER JOIN libro l ON p.idLibro = l.idLibro "
            + "WHERE l.titulo = ? AND l.idBiblioteca = ? AND p.fechaDevolucion IS NULL "
            + "LIMIT 1)";

    private static final String SQL_REGISTRAR_DEVOLUCION_ID = "UPDATE prestamo SET fechaDevolucion = ? WHERE idPrestamo = ?";

    private static final String SQL_ELIMINAR = "DELETE FROM prestamo WHERE idPrestamo = ?";

    private static final String SQL_BUSCAR_POR_ID = "SELECT idPrestamo, fechaRetiro, fechaDevolucion, dniSocio, idLibro FROM prestamo WHERE idPrestamo = ?";

    private static final String SQL_LISTAR_POR_LIBRO = "SELECT idPrestamo, fechaRetiro, fechaDevolucion, dniSocio, idLibro FROM prestamo WHERE idLibro = ?";

    private static final String SQL_LISTAR_POR_SOCIO = "SELECT idPrestamo, fechaRetiro, fechaDevolucion, dniSocio, idLibro FROM prestamo WHERE dniSocio = ?";

    private static final String SQL_LISTAR_TODOS = "SELECT p.idPrestamo, p.fechaRetiro, p.fechaDevolucion, p.dniSocio, p.idLibro, l.titulo "
            + "FROM prestamo p "
            + "INNER JOIN libro l ON p.idLibro = l.idLibro "
            + "WHERE l.idBiblioteca = ?";

    // ====================== ATRIBUTOS ======================

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

    // ===================== CONSTRUCTOR =====================

    /**
     * Crea el DAO obteniendo la conexión activa desde {@link ConexionDB}.
     *
     * @throws SQLException si no se puede establecer la conexión.
     */
    public PrestamoDAOImpl() throws SQLException {
        this.conexion = ConexionDB.getConexion();
    }

    // ======================== MÉTODOS DEL CONTRATO PrestamoDAO =========================

    /**
     * Carga y vincula los préstamos de una biblioteca a sus respectivos libros y socios.
     *
     * @param libros        Colección de libros de la biblioteca.
     * @param socios        Colección de socios de la biblioteca.
     * @param idBiblioteca  Identificador de la biblioteca.
     */
    @Override
    public void cargarPrestamos(List<Libro> libros, List<Socio> socios, int idBiblioteca) {
        // los objetos de una carga anterior ya no se usan
        this.idsPorPrestamo.clear();
        try {
            PreparedStatement ps = this.conexion.prepareStatement(SQL_LISTAR_TODOS);
            ps.setInt(1, idBiblioteca);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int dni = rs.getInt("dniSocio");
                String titulo = rs.getString("titulo");
                String strRetiro = rs.getString("fechaRetiro");
                String strDevolucion = rs.getString("fechaDevolucion");

                // Buscar socio coincidente
                Socio socioEncontrado = null;
                for (Socio s : socios) {
                    if (s.getDniSocio() == dni) {
                        socioEncontrado = s;
                        break;
                    }
                }

                // Buscar libro coincidente
                Libro libroEncontrado = null;
                for (Libro l : libros) {
                    if (l.getTitulo().equals(titulo)) {
                        // Si está devuelto o libre, puede ser este ejemplar
                        if (strDevolucion != null || !l.prestado()) {
                            libroEncontrado = l;
                            break;
                        }
                    }
                }

                if (socioEncontrado != null && libroEncontrado != null) {
                    Calendar fechaRetiro = parsearFecha(strRetiro);
                    Prestamo prestamo = new Prestamo(fechaRetiro, socioEncontrado, libroEncontrado);
                    if (strDevolucion != null) {
                        prestamo.registrarFechaDevolucion(parsearFecha(strDevolucion));
                    }
                    // guarda el id de la base para poder devolver este mismo préstamo
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
     * Inserta un nuevo préstamo en la base de datos para una biblioteca.
     *
     * @param p_prestamo     Objeto {@link Prestamo} a persistir.
     * @param p_idBiblioteca Identificador de la biblioteca.
     */
    @Override
    public void insertar(Prestamo p_prestamo, int p_idBiblioteca) {
        try {
            PreparedStatement ps = this.conexion.prepareStatement(SQL_INSERTAR_CON_BIBLIO);
            ps.setString(1, FORMATO_FECHA.format(p_prestamo.getFechaRetiro().getTime()));

            if (p_prestamo.getFechaDevolucion() != null) {
                ps.setString(2, FORMATO_FECHA.format(p_prestamo.getFechaDevolucion().getTime()));
            } else {
                ps.setNull(2, java.sql.Types.VARCHAR);
            }

            ps.setInt(3, p_prestamo.getSocio().getDniSocio());
            ps.setString(4, p_prestamo.getLibro().getTitulo());
            ps.setInt(5, p_idBiblioteca);
            ps.executeUpdate();
            ps.close();

            // guarda el id que le asignó la base para poder devolverlo después
            try (Statement st = this.conexion.createStatement();
                 ResultSet rs = st.executeQuery("SELECT last_insert_rowid()")) {
                if (rs.next()) {
                    this.idsPorPrestamo.put(p_prestamo, rs.getInt(1));
                }
            }
            System.out.println("[PrestamoDAO] Préstamo guardado (libro=" + p_prestamo.getLibro().getTitulo()
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

    // ======================== MÉTODOS CRUD ADICIONALES =========================

    /**
     * Inserta un nuevo préstamo indicando el id directo del libro.
     *
     * @param p_prestamo Objeto {@link Prestamo} a persistir.
     * @param p_idLibro  Identificador del libro prestado.
     * @return El {@code idPrestamo} generado, o {@code -1} si falló.
     */
    public int guardar(Prestamo p_prestamo, int p_idLibro) {
        int idGenerado = -1;
        try {
            PreparedStatement ps = this.conexion.prepareStatement(SQL_INSERTAR_CON_LIBRO, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, FORMATO_FECHA.format(p_prestamo.getFechaRetiro().getTime()));

            if (p_prestamo.getFechaDevolucion() != null) {
                ps.setString(2, FORMATO_FECHA.format(p_prestamo.getFechaDevolucion().getTime()));
            } else {
                ps.setNull(2, java.sql.Types.VARCHAR);
            }

            ps.setInt(3, p_prestamo.getSocio().getDniSocio());
            ps.setInt(4, p_idLibro);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                idGenerado = rs.getInt(1);
            }
            rs.close();
            ps.close();
            System.out.println("[PrestamoDAO] Préstamo guardado (id=" + idGenerado
                    + ", libro=" + p_idLibro + ", socio=" + p_prestamo.getSocio().getDniSocio() + ")");
        } catch (SQLException e) {
            System.err.println("[PrestamoDAO] Error al guardar: " + e.getMessage());
        }
        return idGenerado;
    }

    /**
     * Actualiza todos los campos de un préstamo existente.
     *
     * @param p_idPrestamo Identificador del préstamo a modificar.
     * @param p_prestamo   Objeto {@link Prestamo} con los nuevos valores.
     * @param p_idLibro    Identificador del libro asociado.
     * @return {@code true} si se modificó al menos una fila.
     */
    public boolean modificar(int p_idPrestamo, Prestamo p_prestamo, int p_idLibro) {
        boolean exito = false;
        try {
            PreparedStatement ps = this.conexion.prepareStatement(SQL_MODIFICAR);
            ps.setString(1, FORMATO_FECHA.format(p_prestamo.getFechaRetiro().getTime()));

            if (p_prestamo.getFechaDevolucion() != null) {
                ps.setString(2, FORMATO_FECHA.format(p_prestamo.getFechaDevolucion().getTime()));
            } else {
                ps.setNull(2, java.sql.Types.VARCHAR);
            }

            ps.setInt(3, p_prestamo.getSocio().getDniSocio());
            ps.setInt(4, p_idLibro);
            ps.setInt(5, p_idPrestamo);
            exito = ps.executeUpdate() > 0;
            ps.close();
            if (exito) {
                System.out.println("[PrestamoDAO] Préstamo modificado (id=" + p_idPrestamo + ")");
            }
        } catch (SQLException e) {
            System.err.println("[PrestamoDAO] Error al modificar: " + e.getMessage());
        }
        return exito;
    }

    /**
     * Registra la fecha de devolución de un préstamo por su id.
     *
     * @param p_idPrestamo      Identificador del préstamo.
     * @param p_fechaDevolucion Fecha en que se devuelve el libro.
     * @return {@code true} si se actualizó correctamente.
     */
    public boolean registrarDevolucion(int p_idPrestamo, Calendar p_fechaDevolucion) {
        boolean exito = false;
        try {
            PreparedStatement ps = this.conexion.prepareStatement(SQL_REGISTRAR_DEVOLUCION_ID);
            ps.setString(1, FORMATO_FECHA.format(p_fechaDevolucion.getTime()));
            ps.setInt(2, p_idPrestamo);
            exito = ps.executeUpdate() > 0;
            ps.close();
            if (exito) {
                System.out.println("[PrestamoDAO] Devolución registrada (préstamo id=" + p_idPrestamo + ")");
            }
        } catch (SQLException e) {
            System.err.println("[PrestamoDAO] Error al registrar devolución: " + e.getMessage());
        }
        return exito;
    }

    /**
     * Elimina un préstamo de la base de datos por su identificador.
     *
     * @param p_idPrestamo Identificador del préstamo a eliminar.
     * @return {@code true} si se eliminó al menos una fila.
     */
    public boolean eliminar(int p_idPrestamo) {
        boolean exito = false;
        try {
            PreparedStatement ps = this.conexion.prepareStatement(SQL_ELIMINAR);
            ps.setInt(1, p_idPrestamo);
            exito = ps.executeUpdate() > 0;
            ps.close();
            if (exito) {
                System.out.println("[PrestamoDAO] Préstamo eliminado (id=" + p_idPrestamo + ")");
            }
        } catch (SQLException e) {
            System.err.println("[PrestamoDAO] Error al eliminar: " + e.getMessage());
        }
        return exito;
    }

    /**
     * Recupera un préstamo por su identificador.
     *
     * @param p_idPrestamo Identificador del préstamo buscado.
     * @param p_libroDAO   DAO de libros para reconstruir el libro.
     * @param p_socioDAO   DAO de socios para reconstruir el socio.
     * @return Objeto {@link Prestamo} encontrado, o {@code null} si no existe.
     */
    public Prestamo buscarPorId(int p_idPrestamo, LibroDAO p_libroDAO, SocioDAO p_socioDAO) {
        Prestamo prestamo = null;
        try {
            PreparedStatement ps = this.conexion.prepareStatement(SQL_BUSCAR_POR_ID);
            ps.setInt(1, p_idPrestamo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                prestamo = construirPrestamo(rs, p_libroDAO, p_socioDAO);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.err.println("[PrestamoDAO] Error al buscar: " + e.getMessage());
        }
        return prestamo;
    }

    /**
     * Recupera todos los préstamos de un libro específico.
     *
     * @param p_idLibro  Identificador del libro.
     * @param p_libroDAO DAO de libros para reconstruir el libro.
     * @param p_socioDAO DAO de socios para reconstruir el socio.
     * @return Lista de objetos {@link Prestamo}; vacía si no hay registros.
     */
    public ArrayList<Prestamo> listarPorLibro(int p_idLibro, LibroDAO p_libroDAO, SocioDAO p_socioDAO) {
        return listarConQuery(SQL_LISTAR_POR_LIBRO, p_idLibro, p_libroDAO, p_socioDAO);
    }

    /**
     * Recupera todos los préstamos de un socio específico.
     *
     * @param p_dniSocio DNI del socio.
     * @param p_libroDAO DAO de libros para reconstruir el libro.
     * @param p_socioDAO DAO de socios para reconstruir el socio.
     * @return Lista de objetos {@link Prestamo}; vacía si no hay registros.
     */
    public ArrayList<Prestamo> listarPorSocio(int p_dniSocio, LibroDAO p_libroDAO, SocioDAO p_socioDAO) {
        return listarConQuery(SQL_LISTAR_POR_SOCIO, p_dniSocio, p_libroDAO, p_socioDAO);
    }

    /**
     * Recupera todos los préstamos de una biblioteca.
     *
     * @param p_idBiblioteca Identificador de la biblioteca.
     * @param p_libroDAO     DAO de libros para reconstruir el libro.
     * @param p_socioDAO     DAO de socios para reconstruir el socio.
     * @return Lista de objetos {@link Prestamo}; vacía si no hay registros.
     */
    public ArrayList<Prestamo> listarTodos(int p_idBiblioteca, LibroDAO p_libroDAO, SocioDAO p_socioDAO) {
        return listarConQuery(SQL_LISTAR_TODOS, p_idBiblioteca, p_libroDAO, p_socioDAO);
    }

    // ====================== AUXILIARES =======================

    private ArrayList<Prestamo> listarConQuery(String sql, int parametro,
                                               LibroDAO p_libroDAO, SocioDAO p_socioDAO) {
        ArrayList<Prestamo> prestamos = new ArrayList<Prestamo>();
        try {
            PreparedStatement ps = this.conexion.prepareStatement(sql);
            ps.setInt(1, parametro);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Prestamo p = construirPrestamo(rs, p_libroDAO, p_socioDAO);
                if (p != null) {
                    prestamos.add(p);
                }
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.err.println("[PrestamoDAO] Error al listar: " + e.getMessage());
        }
        return prestamos;
    }

    private Prestamo construirPrestamo(ResultSet rs, LibroDAO p_libroDAO, SocioDAO p_socioDAO)
            throws SQLException {
        Calendar fechaRetiro = parsearFecha(rs.getString("fechaRetiro"));
        String strDevolucion = rs.getString("fechaDevolucion");
        Calendar fechaDevolucion = (strDevolucion != null) ? parsearFecha(strDevolucion) : null;

        int idLibro = rs.getInt("idLibro");
        int dniSocio = rs.getInt("dniSocio");

        Libro libro = p_libroDAO.buscarPorId(idLibro);
        Socio socio = p_socioDAO.buscarPorDni(dniSocio);

        if (libro == null || socio == null) {
            return null;
        }

        Prestamo prestamo = new Prestamo(fechaRetiro, socio, libro);
        if (fechaDevolucion != null) {
            prestamo.registrarFechaDevolucion(fechaDevolucion);
        }
        return prestamo;
    }

    private Calendar parsearFecha(String p_fecha) {
        String[] partes = p_fecha.split("-");
        int anio = Integer.parseInt(partes[0]);
        int mes = Integer.parseInt(partes[1]) - 1;
        int dia = Integer.parseInt(partes[2]);
        return new GregorianCalendar(anio, mes, dia);
    }
}