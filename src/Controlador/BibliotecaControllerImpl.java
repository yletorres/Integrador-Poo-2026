package controlador;

import biblioteca.Biblioteca;
import biblioteca.excepciones.LibroNoPrestadoException;
import biblioteca.libro.Libro;
import biblioteca.socio.Docente;
import biblioteca.socio.Estudiante;
import biblioteca.socio.Socio;
import biblioteca.ConexionDB;

import java.sql.*;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class BibliotecaControllerImpl implements BibliotecaController {

    private Biblioteca biblioteca;
    private String bibliotecaActiva = "Biblioteca Central";

    public BibliotecaControllerImpl() {
        cargarDatosDesdeDB();
    }

    private int obtenerIdBibliotecaActual(Connection conn) throws SQLException {
        String sql = "SELECT idBiblioteca FROM biblioteca WHERE nombre = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, bibliotecaActiva);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("idBiblioteca");
            }
        }
        return 1; // Por defecto
    }

    private void cargarDatosDesdeDB() {
        this.biblioteca = new Biblioteca(bibliotecaActiva);
        try (Connection conn = ConexionDB.getConexion()) {
            int idLib = obtenerIdBibliotecaActual(conn);

            // 1. Cargar Libros de la biblioteca activa
            String sqlLibros = "SELECT idLibro, titulo, edicion, editorial, anio FROM libro WHERE idBiblioteca = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlLibros)) {
                stmt.setInt(1, idLib);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String titulo = rs.getString("titulo");
                    int edicion = rs.getInt("edicion");
                    String editorial = rs.getString("editorial");
                    int anio = rs.getInt("anio");

                    Libro libro = new Libro(titulo, edicion, editorial, anio);
                    biblioteca.agregarLibro(libro);
                }
            }

            // 2. Cargar Socios de la biblioteca activa
            String sqlSocios = "SELECT s.dniSocio, s.nombre, s.tipo, d.area, e.carrera " +
                    "FROM socio s " +
                    "LEFT JOIN docente d ON s.dniSocio = d.dniSocio " +
                    "LEFT JOIN estudiante e ON s.dniSocio = e.dniSocio " +
                    "WHERE s.idBiblioteca = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlSocios)) {
                stmt.setInt(1, idLib);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    int dni = rs.getInt("dniSocio");
                    String nombre = rs.getString("nombre");
                    String tipo = rs.getString("tipo");

                    if ("Docente".equalsIgnoreCase(tipo)) {
                        String area = rs.getString("area");
                        Docente docente = new Docente(dni, nombre, area);
                        biblioteca.agregarSocio(docente);
                    } else if ("Estudiante".equalsIgnoreCase(tipo)) {
                        String carrera = rs.getString("carrera");
                        Estudiante estudiante = new Estudiante(dni, nombre, carrera);
                        biblioteca.agregarSocio(estudiante);
                    }
                }
            }

            // 3. Cargar Préstamos activos
            String sqlPrestamos = "SELECT p.fechaRetiro, p.dniSocio, l.titulo " +
                    "FROM prestamo p " +
                    "JOIN libro l ON p.idLibro = l.idLibro " +
                    "WHERE p.fechaDevolucion IS NULL";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sqlPrestamos)) {
                while (rs.next()) {
                    int dni = rs.getInt("dniSocio");
                    String tituloLibro = rs.getString("titulo");
                    String fechaRetiroStr = rs.getString("fechaRetiro");

                    Socio socio = biblioteca.buscarSocio(dni);
                    Libro libro = null;
                    for (Libro l : biblioteca.getLibros()) {
                        if (l.getTitulo().equalsIgnoreCase(tituloLibro) && !l.prestado()) {
                            libro = l;
                            break;
                        }
                    }

                    if (socio != null && libro != null && fechaRetiroStr != null) {
                        String[] partes = fechaRetiroStr.split("-");
                        Calendar cal = new GregorianCalendar(
                                Integer.parseInt(partes[0]),
                                Integer.parseInt(partes[1]) - 1,
                                Integer.parseInt(partes[2])
                        );
                        biblioteca.prestarLibro(cal, socio, libro);
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("[Controller] Error al sincronizar con la base de datos: " + e.getMessage());
        }
    }

    public Biblioteca getBiblioteca() {
        return this.biblioteca;
    }

    @Override
    public boolean agregarSocioEstudiante(int dni, String nombre, String carrera) {
        try (Connection conn = ConexionDB.getConexion()) {
            int idLib = obtenerIdBibliotecaActual(conn);
            String sqlSocio = "INSERT INTO socio (dniSocio, nombre, diasPrestamo, tipo, idBiblioteca) VALUES (?, ?, 20, 'Estudiante', ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sqlSocio)) {
                stmt.setInt(1, dni);
                stmt.setString(2, nombre);
                stmt.setInt(3, idLib);
                stmt.executeUpdate();
            }
            String sqlEst = "INSERT INTO estudiante (dniSocio, carrera) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sqlEst)) {
                stmt.setInt(1, dni);
                stmt.setString(2, carrera);
                stmt.executeUpdate();
            }
            biblioteca.nuevoSocioEstudiante(dni, nombre, carrera);
            return true;
        } catch (SQLException e) {
            System.err.println("Error al insertar estudiante en BD: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean agregarSocioDocente(int dni, String nombre, String area) {
        try (Connection conn = ConexionDB.getConexion()) {
            int idLib = obtenerIdBibliotecaActual(conn);
            String sqlSocio = "INSERT INTO socio (dniSocio, nombre, diasPrestamo, tipo, idBiblioteca) VALUES (?, ?, 5, 'Docente', ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sqlSocio)) {
                stmt.setInt(1, dni);
                stmt.setString(2, nombre);
                stmt.setInt(3, idLib);
                stmt.executeUpdate();
            }
            String sqlDoc = "INSERT INTO docente (dniSocio, area) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sqlDoc)) {
                stmt.setInt(1, dni);
                stmt.setString(2, area);
                stmt.executeUpdate();
            }
            biblioteca.nuevoSocioDocente(dni, nombre, area);
            return true;
        } catch (SQLException e) {
            System.err.println("Error al insertar docente en BD: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean agregarLibro(String titulo, int edicion, String editorial, int anio) {
        try (Connection conn = ConexionDB.getConexion()) {
            int idLib = obtenerIdBibliotecaActual(conn);
            String sql = "INSERT INTO libro (titulo, edicion, editorial, anio, idBiblioteca) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, titulo);
                stmt.setInt(2, edicion);
                stmt.setString(3, editorial);
                stmt.setInt(4, anio);
                stmt.setInt(5, idLib);
                stmt.executeUpdate();
            }
            biblioteca.nuevoLibro(titulo, edicion, editorial, anio);
            return true;
        } catch (SQLException e) {
            System.err.println("Error al insertar libro en BD: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean quitarLibro(String titulo) {
        Libro libroEncontrado = null;
        for (Libro l : biblioteca.getLibros()) {
            if (l.getTitulo().equalsIgnoreCase(titulo) && !l.prestado()) {
                libroEncontrado = l;
                break;
            }
        }

        if (libroEncontrado != null) {
            try (Connection conn = ConexionDB.getConexion()) {
                String sql = "DELETE FROM libro WHERE titulo = ? AND idBiblioteca = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, titulo);
                    stmt.setInt(2, obtenerIdBibliotecaActual(conn));
                    stmt.executeUpdate();
                }
                biblioteca.quitarLibro(libroEncontrado);
                return true;
            } catch (SQLException e) {
                System.err.println("Error al quitar libro de BD: " + e.getMessage());
            }
        }
        return false;
    }

    @Override
    public boolean realizarPrestamo(int dniSocio, String tituloLibro, Calendar fechaRetiro) {
        Socio socio = biblioteca.buscarSocio(dniSocio);
        Libro libro = null;
        for (Libro l : biblioteca.getLibros()) {
            if (l.getTitulo().equalsIgnoreCase(tituloLibro) && !l.prestado()) {
                libro = l;
                break;
            }
        }

        if (socio != null && libro != null) {
            boolean exito = biblioteca.prestarLibro(fechaRetiro, socio, libro);
            if (exito) {
                try (Connection conn = ConexionDB.getConexion()) {
                    int idLibro = -1;
                    String sqlId = "SELECT idLibro FROM libro WHERE titulo = ? AND idBiblioteca = ? LIMIT 1";
                    try (PreparedStatement stmtId = conn.prepareStatement(sqlId)) {
                        stmtId.setString(1, tituloLibro);
                        stmtId.setInt(2, obtenerIdBibliotecaActual(conn));
                        ResultSet rs = stmtId.executeQuery();
                        if (rs.next()) {
                            idLibro = rs.getInt("idLibro");
                        }
                    }

                    if (idLibro != -1) {
                        String fechaStr = String.format("%d-%02d-%02d",
                                fechaRetiro.get(Calendar.YEAR),
                                fechaRetiro.get(Calendar.MONTH) + 1,
                                fechaRetiro.get(Calendar.DAY_OF_MONTH)
                        );
                        String sqlIns = "INSERT INTO prestamo (fechaRetiro, fechaDevolucion, dniSocio, idLibro) VALUES (?, NULL, ?, ?)";
                        try (PreparedStatement stmtIns = conn.prepareStatement(sqlIns)) {
                            stmtIns.setString(1, fechaStr);
                            stmtIns.setInt(2, dniSocio);
                            stmtIns.setInt(3, idLibro);
                            stmtIns.executeUpdate();
                        }
                    }
                } catch (SQLException e) {
                    System.err.println("Error al registrar préstamo en BD: " + e.getMessage());
                }
            }
            return exito;
        }
        return false;
    }

    @Override
    public boolean devolverLibro(String tituloLibro) throws LibroNoPrestadoException {
        Libro libro = null;
        for (Libro l : biblioteca.getLibros()) {
            if (l.getTitulo().equalsIgnoreCase(tituloLibro) && l.prestado()) {
                libro = l;
                break;
            }
        }

        if (libro != null) {
            biblioteca.devolverLibro(libro);
            try (Connection conn = ConexionDB.getConexion()) {
                String fechaDevStr = String.format("%d-%02d-%02d",
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH) + 1,
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                );
                String sqlUpd = "UPDATE prestamo SET fechaDevolucion = ? WHERE idLibro IN (SELECT idLibro FROM libro WHERE titulo = ?) AND fechaDevolucion IS NULL";
                try (PreparedStatement stmt = conn.prepareStatement(sqlUpd)) {
                    stmt.setString(1, fechaDevStr);
                    stmt.setString(2, tituloLibro);
                    stmt.executeUpdate();
                }
            } catch (SQLException e) {
                System.err.println("Error al actualizar devolución en BD: " + e.getMessage());
            }
            return true;
        }
        throw new LibroNoPrestadoException("El libro no está prestado o no existe.");
    }

    @Override
    public String quienTieneElLibro(String tituloLibro) throws LibroNoPrestadoException {
        Libro libro = null;
        for (Libro l : biblioteca.getLibros()) {
            if (l.getTitulo().equalsIgnoreCase(tituloLibro)) {
                libro = l;
                break;
            }
        }
        if (libro != null) {
            return biblioteca.quienTieneElLibro(libro);
        }
        throw new LibroNoPrestadoException("El libro no existe en el sistema");
    }

    @Override
    public int cantidadDeSociosPorTipo(String tipo) {
        return biblioteca.cantidadDeSociosPorTipo(tipo);
    }

    @Override
    public String listaDeDocentesResponsables() {
        return biblioteca.listaDeDocentesResponsables();
    }

    @Override
    public String listaDeSocios() {
        return biblioteca.listaDeSocios();
    }

    @Override
    public String listaDeLibros() {
        return biblioteca.listaDeLibros();
    }

    @Override
    public String listaDeTitulos() {
        StringBuilder sb = new StringBuilder();
        for (Libro l : biblioteca.getLibros()) {
            sb.append("- ").append(l.getTitulo()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public List<Object[]> filasLibros() {
        List<Libro> libros = biblioteca.getLibros();
        Object[][] filas = new Object[libros.size()][5];
        for (int i = 0; i < libros.size(); i++) {
            Libro l = libros.get(i);
            filas[i][0] = l.getTitulo();
            filas[i][1] = l.getEdicion();
            filas[i][2] = l.getEditorial();
            filas[i][3] = l.getAnio();
            filas[i][4] = l.prestado() ? "Si" : "No";
        }
        return Arrays.asList(filas);
    }

    @Override
    public List<Object[]> filasSocios() {
        List<Socio> socios = biblioteca.getSocios();
        Object[][] filas = new Object[socios.size()][4];
        for (int i = 0; i < socios.size(); i++) {
            Socio s = socios.get(i);
            filas[i][0] = s.getDniSocio();
            filas[i][1] = s.getNombre();
            filas[i][2] = s.soyDeLaClase();
            filas[i][3] = s.cantLibrosPrestados();
        }
        return Arrays.asList(filas);
    }

    @Override
    public String[] nombresDeBibliotecas() {
        return new String[]{
                "Biblioteca Central",
                "Facultad de Ciencias Exactas",
                "Facultad de Derecho",
                "Facultad de Medicina",
                "Biblioteca Campus"
        };
    }

    @Override
    public void seleccionarBiblioteca(String nombre) {
        this.bibliotecaActiva = nombre;
        cargarDatosDesdeDB(); // Recarga los datos según la biblioteca seleccionada en el menú
    }
}