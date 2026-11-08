package controlador;

import biblioteca.excepciones.LibroNoPrestadoException;

import biblioteca.Biblioteca;
import biblioteca.libro.Libro;
import biblioteca.prestamo.Prestamo;
import biblioteca.socio.Docente;
import biblioteca.socio.Estudiante;
import biblioteca.socio.Socio;
import controlador.dao.BibliotecaDAO;
import controlador.dao.LibroDAO;
import controlador.dao.PrestamoDAO;
import controlador.dao.SocioDAO;

import java.sql.SQLException;
import controlador.dao.impl.BibliotecaDAOImpl;
import controlador.dao.impl.LibroDAOImpl;
import controlador.dao.impl.PrestamoDAOImpl;
import controlador.dao.impl.SocioDAOImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Controlador real de la biblioteca. Implementa el contrato que usa la vista
 * (BibliotecaController): trabaja sobre el modelo y persiste cada cambio a
 * través de las interfaces DAO.
 * Contrato con el DAO: BibliotecaDAO.buscarPorNombre devuelve la biblioteca
 * ya armada desde la base de datos, con sus libros, socios y préstamos
 *
 * @author Yleana Torres
 * @version TP7 - 2026
 */
public class BibliotecaControllerImpl implements BibliotecaController {

    /** DAO de bibliotecas. */
    private BibliotecaDAO bibliotecaDAO;

    /** DAO de libros. */
    private LibroDAO libroDAO;

    /** DAO de socios. */
    private SocioDAO socioDAO;

    /** DAO de préstamos. */
    private PrestamoDAO prestamoDAO;

    /** Biblioteca con la que se está trabajando (null si no hay ninguna). */
    private Biblioteca bibliotecaSeleccionada;

    /** Id en la base de datos de la biblioteca seleccionada (-1 si no hay ninguna). */
    private int idBibliotecaSeleccionada;

    /**
     * Crea el controlador armando él mismo los DAOs, una sola vez cada uno,
     * y compartiéndolos con el DAO de bibliotecas.
     *
     * @author Yleana Torres
     * @throws SQLException si ocurre un error al conectar con la base de datos
     */
    public BibliotecaControllerImpl() throws SQLException {
        this.setLibroDAO(new LibroDAOImpl());
        this.setSocioDAO(new SocioDAOImpl());
        this.setPrestamoDAO(new PrestamoDAOImpl());
        this.setBibliotecaDAO(new BibliotecaDAOImpl(
                this.getLibroDAO(), this.getSocioDAO(), this.getPrestamoDAO()));
        this.setBibliotecaSeleccionada(null);
        this.setIdBibliotecaSeleccionada(-1);
    }

    /**
     * Establece el DAO de bibliotecas.
     *
     * @author Yleana Torres
     * @param p_bibliotecaDAO DAO de bibliotecas
     */
    private void setBibliotecaDAO(BibliotecaDAO p_bibliotecaDAO) {
        this.bibliotecaDAO = p_bibliotecaDAO;
    }

    /**
     * Establece el DAO de libros.
     *
     * @author Yleana Torres
     * @param p_libroDAO DAO de libros
     */
    private void setLibroDAO(LibroDAO p_libroDAO) {
        this.libroDAO = p_libroDAO;
    }

    /**
     * Establece el DAO de socios.
     *
     * @author Yleana Torres
     * @param p_socioDAO DAO de socios
     */
    private void setSocioDAO(SocioDAO p_socioDAO) {
        this.socioDAO = p_socioDAO;
    }

    /**
     * Establece el DAO de préstamos.
     *
     * @author Yleana Torres
     * @param p_prestamoDAO DAO de préstamos
     */
    private void setPrestamoDAO(PrestamoDAO p_prestamoDAO) {
        this.prestamoDAO = p_prestamoDAO;
    }

    /**
     * Establece la biblioteca seleccionada.
     *
     * @author Yleana Torres
     * @param p_bibliotecaSeleccionada biblioteca seleccionada, o null si no hay ninguna
     */
    private void setBibliotecaSeleccionada(Biblioteca p_bibliotecaSeleccionada) {
        this.bibliotecaSeleccionada = p_bibliotecaSeleccionada;
    }

    /**
     * Establece el id de la biblioteca seleccionada.
     *
     * @author Yleana Torres
     * @param p_idBibliotecaSeleccionada id en la base de datos, o -1 si no hay ninguna
     */
    private void setIdBibliotecaSeleccionada(int p_idBibliotecaSeleccionada) {
        this.idBibliotecaSeleccionada = p_idBibliotecaSeleccionada;
    }

    /**
     * Devuelve el DAO de bibliotecas.
     *
     * @author Yleana Torres
     * @return DAO de bibliotecas
     */
    public BibliotecaDAO getBibliotecaDAO() {
        return this.bibliotecaDAO;
    }

    /**
     * Devuelve el DAO de libros.
     *
     * @author Yleana Torres
     * @return DAO de libros
     */
    public LibroDAO getLibroDAO() {
        return this.libroDAO;
    }

    /**
     * Devuelve el DAO de socios.
     *
     * @author Yleana Torres
     * @return DAO de socios
     */
    public SocioDAO getSocioDAO() {
        return this.socioDAO;
    }

    /**
     * Devuelve el DAO de préstamos.
     *
     * @author Yleana Torres
     * @return DAO de préstamos
     */
    public PrestamoDAO getPrestamoDAO() {
        return this.prestamoDAO;
    }

    /**
     * Devuelve la biblioteca seleccionada.
     *
     * @author Yleana Torres
     * @return biblioteca seleccionada, o null si no hay ninguna
     */
    public Biblioteca getBibliotecaSeleccionada() {
        return this.bibliotecaSeleccionada;
    }

    /**
     * Devuelve el id de la biblioteca seleccionada.
     *
     * @author Yleana Torres
     * @return id en la base de datos, o -1 si no hay ninguna
     */
    public int getIdBibliotecaSeleccionada() {
        return this.idBibliotecaSeleccionada;
    }

    // >>>> bibliotecas <<<<<

    @Override
    public List<String> nombresDeBibliotecas() {
        return this.getBibliotecaDAO().obtenerNombres();
    }

    @Override
    public void seleccionarBiblioteca(String p_nombre) {
        Biblioteca biblioteca = this.getBibliotecaDAO().buscarPorNombre(p_nombre);

        if (biblioteca == null) {
            throw new IllegalArgumentException("No existe una biblioteca con ese nombre");
        }

        this.setBibliotecaSeleccionada(biblioteca);
        this.setIdBibliotecaSeleccionada(this.getBibliotecaDAO().obtenerIdPorNombre(p_nombre));
    }

    @Override
    public void crearBiblioteca(String p_nombre) {
        Biblioteca biblioteca = new Biblioteca(p_nombre);

        this.getBibliotecaDAO().insertar(biblioteca);

        this.seleccionarBiblioteca(p_nombre);
    }

    // >>> libros <<<<

    @Override
    public List<Object[]> filasLibros() {
        List<Object[]> filas = new ArrayList<>();

        if (this.getBibliotecaSeleccionada() == null) {
            return filas;
        }

        for (Libro libroActual : this.getBibliotecaSeleccionada().getLibros()) {
            String prestado = libroActual.prestado() ? "Si" : "No";

            Object[] fila = {
                    libroActual.getTitulo(),
                    libroActual.getEdicion(),
                    libroActual.getEditorial(),
                    libroActual.getAnio(),
                    prestado
            };

            filas.add(fila);
        }

        return filas;
    }

    @Override
    public List<Object[]> filasLibrosConId() {
        if (this.getBibliotecaSeleccionada() == null) {
            return new ArrayList<>();
        }

        return this.getLibroDAO().obtenerFilasConIdPorBiblioteca( this.getIdBibliotecaSeleccionada());
    }

    @Override
    public void eliminarLibro(int p_idLibro) {
        Biblioteca biblioteca = this.bibliotecaActiva();

        // Verificar que el libro no esté prestado.
        for (Object[] fila : this.getLibroDAO().obtenerFilasConIdPorBiblioteca(this.getIdBibliotecaSeleccionada())) {

            int idLibro = (int) fila[0];
            String prestado = (String) fila[5];

            if (idLibro == p_idLibro && "Si".equals(prestado)) {
                throw new IllegalStateException("No se puede eliminar un libro que está prestado.");
            }
        }

        boolean eliminado = this.getLibroDAO().eliminar(p_idLibro);

        if (!eliminado) {
            throw new IllegalStateException("No se pudo eliminar el libro. Puede tener préstamos asociados.");
        }

        // Recargar la biblioteca para actualizar los libros y sus préstamos.
        this.setBibliotecaSeleccionada(this.getBibliotecaDAO().buscarPorNombre(biblioteca.getNombre()));
    }

    @Override
    public void nuevoLibro(String p_titulo, int p_edicion, String p_editorial, int p_anio) {
        Biblioteca biblioteca = this.bibliotecaActiva();

        Libro libro = new Libro(p_titulo, p_edicion, p_editorial, p_anio);

        // Primero se guarda en la base de datos; si falla, el modelo no cambia.
        this.getLibroDAO().insertar(libro, this.getIdBibliotecaSeleccionada());
        biblioteca.agregarLibro(libro);
    }


    // >>> socios <<<

    @Override
    public List<Object[]> filasSocios() {
        List<Object[]> filas = new ArrayList<>();

        if (this.getBibliotecaSeleccionada() == null) {
            return filas;
        }

        for (Socio socioActual : this.getBibliotecaSeleccionada().getSocios()) {
            Object[] fila = {
                    socioActual.getDniSocio(),
                    socioActual.getNombre(),
                    socioActual.soyDeLaClase(),
                    socioActual.cantLibrosPrestados()
            };

            filas.add(fila);
        }

        return filas;
    }

    @Override
    public void eliminarSocio(int p_dni) {
        Biblioteca biblioteca = this.bibliotecaActiva();

        Socio socio = biblioteca.buscarSocio(p_dni);

        if (socio == null) {
            throw new IllegalArgumentException("No se encontró el socio seleccionado.");
        }

        if (socio.cantLibrosPrestados() > 0) {
            throw new IllegalStateException("No se puede eliminar un socio que tiene libros prestados.");
        }

        boolean eliminado = this.getSocioDAO().eliminar(
                p_dni, socio.soyDeLaClase());

        if (!eliminado) {
            throw new IllegalStateException("No se pudo eliminar el socio. Puede tener préstamos asociados.");
        }

        biblioteca.quitarSocio(socio);
    }

    @Override
    public void nuevoSocioEstudiante(int p_dni, String p_nombre, String p_carrera) {
        this.registrarSocio(new Estudiante(p_dni, p_nombre, p_carrera));
    }

    @Override
    public void nuevoSocioDocente(int p_dni, String p_nombre, String p_area) {
        this.registrarSocio(new Docente(p_dni, p_nombre, p_area));
    }

    // >>> préstamos <<<

    @Override
    public boolean prestarLibro(int p_dni, String p_titulo) {
        Biblioteca biblioteca = this.bibliotecaActiva();

        Socio socio = biblioteca.buscarSocio(p_dni);
        // Un título puede tener varios ejemplares: se presta uno que esté disponible.
        Libro libro = this.buscarLibro(p_titulo, false);

        if (socio == null || libro == null) {
            return false;
        }

        boolean prestado = biblioteca.prestarLibro(new GregorianCalendar(), socio, libro);

        if (prestado) {
            Prestamo prestamo = libro.ultimoPrestamo();
            try {
                this.getPrestamoDAO().insertar(prestamo, this.getIdBibliotecaSeleccionada());
            } catch (RuntimeException excepcion) {
                // Si no se pudo guardar, el modelo vuelve a como estaba.
                libro.quitarPrestamo(prestamo);
                socio.quitarPrestamo(prestamo);
                throw excepcion;
            }
        }

        return prestado;
    }

    @Override
    public void devolverLibro(String p_titulo) throws LibroNoPrestadoException {
        // Lanza IllegalArgumentException si el título no existe.
        Libro libro = this.ejemplarDelTitulo(p_titulo);

        this.devolverEjemplar(libro);
    }

    @Override
    public void devolverLibro(String p_titulo, int p_dni) throws LibroNoPrestadoException {
        // Lanza IllegalArgumentException si el título no existe.
        this.ejemplarDelTitulo(p_titulo);

        // Entre los ejemplares prestados del título, busca el que tiene ese socio.
        Libro libro = this.buscarLibro(p_titulo, true, p_dni);

        if (libro == null) {
            throw new LibroNoPrestadoException(this.mensajeNoSePuedeDevolver(p_titulo));
        }

        this.devolverEjemplar(libro);
    }

    /**
     * Devuelve un ejemplar en el modelo y guarda la devolución en la base de datos.
     *
     * @param p_libro ejemplar que se devuelve
     * @throws LibroNoPrestadoException si el ejemplar no está prestado
     */
    private void devolverEjemplar(Libro p_libro) throws LibroNoPrestadoException {
        // Lanza LibroNoPrestadoException si el ejemplar no está prestado.
        this.bibliotecaActiva().devolverLibro(p_libro);

        Prestamo prestamo = p_libro.ultimoPrestamo();
        try {
            // se guarda por el mismo préstamo que modificó el modelo, no por título
            this.getPrestamoDAO().registrarDevolucion(prestamo, prestamo.getFechaDevolucion());
        } catch (RuntimeException excepcion) {
            // Si no se pudo guardar, el libro vuelve a figurar como prestado.
            prestamo.registrarFechaDevolucion(null);
            throw excepcion;
        }
    }

    @Override
    public List<Object[]> filasPrestamosActivos() {
        List<Object[]> filas = new ArrayList<>();

        if (this.getBibliotecaSeleccionada() == null) {
            return filas;
        }

        // La biblioteca ya sabe cuáles son los préstamos vencidos a hoy.
        ArrayList<Prestamo> vencidos = this.getBibliotecaSeleccionada().prestamosVencidos();
        for (Libro libroActual : this.getBibliotecaSeleccionada().getLibros()) {
            if (libroActual.prestado()) {
                Prestamo prestamo = libroActual.ultimoPrestamo();
                Object[] fila = {
                        libroActual.getTitulo(),
                        prestamo.getSocio().getDniSocio(),
                        prestamo.getSocio().getNombre(),
                        vencidos.contains(prestamo) ? "Vencido" : "Prestado"
                };
                filas.add(fila);
            }
        }

        return filas;
    }

    // >>> informes de la consigna <<<

    @Override
    public int cantidadDeSociosPorTipo(String p_tipo) {
        return this.bibliotecaActiva().cantidadDeSociosPorTipo(p_tipo);
    }

    @Override
    public String quienTieneElLibro(String p_titulo) throws LibroNoPrestadoException {
        // Lanza IllegalArgumentException si el título no existe.
        Libro libro = this.ejemplarDelTitulo(p_titulo);

        // Lanza LibroNoPrestadoException si el ejemplar está en la biblioteca.
        return this.bibliotecaActiva().quienTieneElLibro(libro);
    }

    @Override
    public String listaDeSocios() {
        return this.bibliotecaActiva().listaDeSocios();
    }

    @Override
    public String listaDeLibros() {
        return this.bibliotecaActiva().listaDeLibros();
    }

    @Override
    public String listaDeTitulos() {
        return this.bibliotecaActiva().listaDeTitulos();
    }

    @Override
    public String listaDeDocentesResponsables() {
        return this.bibliotecaActiva().listaDeDocentesResponsables();
    }

    // >>> auxiliares <<<

    /**
     * Devuelve la biblioteca seleccionada o avisa si todavía no hay ninguna.
     *
     * @author Yleana Torres
     * @return biblioteca seleccionada
     * @throws IllegalStateException si no hay una biblioteca seleccionada
     */
    private Biblioteca bibliotecaActiva() {
        if (this.getBibliotecaSeleccionada() == null) {
            throw new IllegalStateException("No hay una biblioteca seleccionada");
        }
        return this.getBibliotecaSeleccionada();
    }

    /**
     * Guarda un socio nuevo en la base de datos y lo agrega a la biblioteca.
     *
     * @author Yleana Torres
     * @param p_socio socio a registrar
     * @throws IllegalArgumentException si ya existe un socio con ese DNI
     */
    private void registrarSocio(Socio p_socio) {
        Biblioteca biblioteca = this.bibliotecaActiva();

        if (biblioteca.buscarSocio(p_socio.getDniSocio()) != null) {
            throw new IllegalArgumentException("Ya existe un socio con ese DNI");
        }

        // Primero se guarda en la base de datos; si falla, el modelo no cambia.
        this.getSocioDAO().insertar(p_socio, this.getIdBibliotecaSeleccionada());
        biblioteca.agregarSocio(p_socio);
    }

    /**
     * Busca un ejemplar por título según esté prestado o no.
     *
     * @author Yleana Torres
     * @param p_titulo   título buscado
     * @param p_prestado true para buscar un ejemplar prestado, false para uno disponible
     * @return el primer ejemplar que cumple, o null si no hay
     */
    private Libro buscarLibro(String p_titulo, boolean p_prestado) {
        return this.buscarLibro(p_titulo, p_prestado, null);
    }

    /**
     * Busca un ejemplar por título según esté prestado o no y, si se indica un DNI,
     * que lo tenga ese socio. Es el único lugar donde se recorren los libros por título.
     *
     * @param p_titulo   título buscado
     * @param p_prestado true para buscar un ejemplar prestado, false para uno disponible
     * @param p_dni      DNI del socio que debe tenerlo (solo con p_prestado en true), o null para cualquiera
     * @return el primer ejemplar que cumple, o null si no hay
     */
    private Libro buscarLibro(String p_titulo, boolean p_prestado, Integer p_dni) {
        for (Libro libroActual : this.bibliotecaActiva().getLibros()) {
            if (libroActual.getTitulo().equals(p_titulo) && libroActual.prestado() == p_prestado
                    && (p_dni == null || libroActual.ultimoPrestamo().getSocio().getDniSocio() == p_dni)) {
                return libroActual;
            }
        }
        return null;
    }

    /**
     * Busca un ejemplar de un título que existe, para devolverlo o consultar quién lo tiene.
     *
     * @param p_titulo título buscado
     * @return ejemplar del título (prestado si hay alguno prestado)
     * @throws IllegalArgumentException si no existe ningún libro con ese título
     */
    private Libro ejemplarDelTitulo(String p_titulo) {
        Libro libro = this.buscarLibroParaConsulta(p_titulo);
        if (libro == null) {
            throw new IllegalArgumentException("No existe un libro con el título " + p_titulo);
        }
        return libro;
    }

    /**
     * Mensaje cuando se intenta devolver un libro que está en la biblioteca.
     *
     * @param p_titulo título del libro
     * @return mensaje para la excepción
     */
    private String mensajeNoSePuedeDevolver(String p_titulo) {
        return "El libro " + p_titulo + " no se puede devolver ya que se encuentra en la biblioteca";
    }

    /**
     * Busca un ejemplar para devolverlo o consultar quién lo tiene: prefiere uno
     * prestado y, si no hay, devuelve cualquier ejemplar de ese título.
     *
     * @author Yleana Torres
     * @param p_titulo título buscado
     * @return ejemplar encontrado, o null si no existe el título
     */
    private Libro buscarLibroParaConsulta(String p_titulo) {
        Libro libro = this.buscarLibro(p_titulo, true);
        if (libro == null) {
            libro = this.buscarLibro(p_titulo, false);
        }
        return libro;
    }
}