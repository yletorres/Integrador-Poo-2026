
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Clase principal que integra la capa de persistencia (SQLite + DAOs)
 * con el modelo de dominio de la biblioteca.
 * <p>
 * {@code iniciarAplicacion()} abre la conexión y crea los DAOs.
 * {@code ejecutarPruebas()} instancia objetos, los persiste vía los
 * DAOs y los recupera mostrándolos por consola.
 * </p>
 *
 * @author (Fernández, Lucía Belén)
 */
public class GestionBiblioteca {

    /** DAOs compartidos por los métodos estáticos. */
    private static LibroDAO libroDAO;
    private static SocioDAO socioDAO;
    private static PrestamoDAO prestamoDAO;

    /** Identificador de la biblioteca de prueba en la BD. */
    private static final int ID_BIBLIOTECA = 1;

    // ======================== MAIN =========================

    public static void main(String[] args) {
        iniciarAplicacion();
        ejecutarPruebas();
    }

    // =================== INICIALIZACIÓN ====================

    /**
     * Establece la conexión con SQLite e instancia los tres DAOs.
     * Si la conexión falla, termina la aplicación con un mensaje de error.
     */
    public static void iniciarAplicacion() {
        System.out.println("=".repeat(60));
        System.out.println("  GESTIÓN DE BIBLIOTECA - Capa de Persistencia SQLite");
        System.out.println("=".repeat(60));

        try {
            // 1. Abrir conexión (Singleton)
            ConexionDB.getConexion();

            // 2. Instanciar DAOs
            libroDAO = new LibroDAO();
            socioDAO = new SocioDAO();
            prestamoDAO = new PrestamoDAO();

            System.out.println("[Inicio] DAOs inicializados correctamente.\n");
        } catch (SQLException e) {
            System.err.println("[Error Fatal] No se pudo conectar a la BD: " + e.getMessage());
            System.exit(1);
        }
    }

    // ====================== PRUEBAS ========================

    /**
     * Ejecuta una secuencia completa de pruebas:
     * <ol>
     * <li>Lee los datos precargados desde la BD (los del script SQL).</li>
     * <li>Crea nuevos objetos y los persiste.</li>
     * <li>Registra un préstamo y una devolución.</li>
     * <li>Recupera y muestra los listados para verificación visual.</li>
     * <li>Cierra la conexión al finalizar.</li>
     * </ol>
     */
    public static void ejecutarPruebas() {
        try {
            // ─── 1. RECUPERAR DATOS EXISTENTES DE LA BD ────────────────
            seccion("1. LIBROS CARGADOS EN LA BD");
            ArrayList<Libro> libros = libroDAO.listarTodos(ID_BIBLIOTECA);
            for (Libro libro : libros) {
                System.out.println("   - " + libro.toString()
                        + " | Ed. " + libro.getEdicion()
                        + " | " + libro.getEditorial()
                        + " (" + libro.getAnio() + ")");
            }
            System.out.println("   Total: " + libros.size() + " libros.\n");

            seccion("2. SOCIOS CARGADOS EN LA BD");
            ArrayList<Socio> socios = socioDAO.listarTodos(ID_BIBLIOTECA);
            for (Socio socio : socios) {
                System.out.println("   - " + socio.toString());
            }
            System.out.println("   Total: " + socios.size() + " socios.\n");

            // ─── 2. CREAR Y GUARDAR UN NUEVO LIBRO ─────────────────────
            seccion("3. GUARDAR NUEVO LIBRO");
            Libro nuevoLibro = new Libro("Patrones de Diseño", 1, "Addison-Wesley", 1995);
            int idNuevoLibro = libroDAO.guardar(nuevoLibro, ID_BIBLIOTECA);
            System.out.println("   Libro creado con id=" + idNuevoLibro + "\n");

            // ─── 3. CREAR Y GUARDAR UN NUEVO SOCIO ESTUDIANTE ──────────
            seccion("4. GUARDAR NUEVO SOCIO (Estudiante)");
            Estudiante nuevoEstudiante = new Estudiante(40123456, "Ana García", "Ingeniería en Informática");
            socioDAO.guardar(nuevoEstudiante, ID_BIBLIOTECA);

            // ─── 4. CREAR Y GUARDAR UN NUEVO SOCIO DOCENTE ─────────────
            seccion("5. GUARDAR NUEVO SOCIO (Docente)");
            Docente nuevoDocente = new Docente(20567890, "Carlos López", "Ingeniería de Software");
            socioDAO.guardar(nuevoDocente, ID_BIBLIOTECA);

            // ─── 5. REGISTRAR UN PRÉSTAMO NUEVO ────────────────────────
            seccion("6. REGISTRAR PRÉSTAMO");
            Calendar fechaRetiro = new GregorianCalendar(2026, Calendar.NOVEMBER, 5);
            Prestamo nuevoPrestamo = new Prestamo(fechaRetiro, nuevoEstudiante, nuevoLibro);

            // Agregar el préstamo al libro y al socio (lógica de negocio)
            nuevoLibro.agregarPrestamo(nuevoPrestamo);
            nuevoEstudiante.addPrestamo(nuevoPrestamo);

            // Persistir en la BD
            int idPrestamo = prestamoDAO.guardar(nuevoPrestamo, idNuevoLibro);
            System.out.println("   Préstamo registrado con id=" + idPrestamo);
            System.out.println("   Libro prestado: " + nuevoLibro.prestado() + "\n");

            // ─── 6. VERIFICAR RECUPERACIÓN DEL PRÉSTAMO ────────────────
            seccion("7. RECUPERAR PRÉSTAMO DESDE LA BD");
            Prestamo prestamoRecuperado = prestamoDAO.buscarPorId(idPrestamo, libroDAO, socioDAO);
            if (prestamoRecuperado != null) {
                System.out.println("   " + prestamoRecuperado.toString() + "\n");
            }

            // ─── 7. LISTAR LIBROS ACTUALIZADOS ─────────────────────────
            seccion("8. LISTA ACTUALIZADA DE LIBROS");
            ArrayList<Libro> librosActualizados = libroDAO.listarTodos(ID_BIBLIOTECA);
            int num = 1;
            for (Libro libro : librosActualizados) {
                System.out.println("   " + num + ") " + libro.toString());
                num++;
            }
            System.out.println("   Total: " + librosActualizados.size() + " libros.\n");

            // ─── 8. LISTAR SOCIOS ACTUALIZADOS ─────────────────────────
            seccion("9. LISTA ACTUALIZADA DE SOCIOS");
            ArrayList<Socio> sociosActualizados = socioDAO.listarTodos(ID_BIBLIOTECA);
            num = 1;
            for (Socio socio : sociosActualizados) {
                System.out.println("   " + num + ") " + socio.toString());
                num++;
            }
            System.out.println("   Total: " + sociosActualizados.size() + " socios.\n");

            // ─── 9. MODIFICAR UN LIBRO ─────────────────────────────────
            seccion("10. MODIFICAR LIBRO");
            Libro libroModificado = new Libro("Patrones de Diseño (Edición Aniversario)", 2, "Addison-Wesley", 2020);
            boolean modificado = libroDAO.modificar(idNuevoLibro, libroModificado);
            System.out.println("   Modificación exitosa: " + modificado);
            Libro libroVerificado = libroDAO.buscarPorId(idNuevoLibro);
            if (libroVerificado != null) {
                System.out.println("   Verificación: " + libroVerificado.toString()
                        + " | Ed. " + libroVerificado.getEdicion()
                        + " (" + libroVerificado.getAnio() + ")\n");
            }

            // ─── 10. LISTAR PRÉSTAMOS DE LA BIBLIOTECA ─────────────────
            seccion("11. TODOS LOS PRÉSTAMOS");
            ArrayList<Prestamo> todosPrestamos = prestamoDAO.listarTodos(ID_BIBLIOTECA, libroDAO, socioDAO);
            for (Prestamo p : todosPrestamos) {
                System.out.println("   ---");
                System.out.println("   " + p.toString().replace("\n", "\n   "));
            }
            System.out.println("   ---");
            System.out.println("   Total: " + todosPrestamos.size() + " préstamos.\n");

        } catch (Exception e) {
            System.err.println("[Error] " + e.getMessage());
            e.printStackTrace();
        } finally {
            // ─── CERRAR CONEXIÓN ───────────────────────────────────────
            ConexionDB.cerrarConexion();
            System.out.println("=".repeat(60));
            System.out.println("  Pruebas finalizadas.");
            System.out.println("=".repeat(60));
        }
    }

    // ====================== UTILIDAD =======================

    /**
     * Imprime un encabezado de sección para mejorar la legibilidad
     * de la salida por consola.
     *
     * @param titulo Nombre de la sección.
     */
    private static void seccion(String titulo) {
        System.out.println("-".repeat(60));
        System.out.println("  " + titulo);
        System.out.println("-".repeat(60));
    }
}
