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

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Controlador real de la biblioteca. Implementa el contrato que usa la vista
 * (BibliotecaController): trabaja sobre el modelo y persiste cada cambio a
 * través de las interfaces DAO.
 *
 * Contrato con el DAO: BibliotecaDAO.buscarPorNombre devuelve la biblioteca
 * ya armada desde la base de datos, con sus libros, socios y préstamos.
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
     * Crea el controlador sin ninguna biblioteca seleccionada.
     *
     * @author Yleana Torres
     * @param p_bibliotecaDAO DAO de bibliotecas
     * @param p_libroDAO      DAO de libros
     * @param p_socioDAO      DAO de socios
     * @param p_prestamoDAO   DAO de préstamos
     */
    public BibliotecaControllerImpl(
            BibliotecaDAO p_bibliotecaDAO,
            LibroDAO p_libroDAO,
            SocioDAO p_socioDAO,
            PrestamoDAO p_prestamoDAO) {

        this.setBibliotecaDAO(p_bibliotecaDAO);
        this.setLibroDAO(p_libroDAO);
        this.setSocioDAO(p_socioDAO);
        this.setPrestamoDAO(p_prestamoDAO);
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

        for (Libro libroActual : this.bibliotecaActiva().getLibros()) {
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
    public void nuevoLibro(String p_titulo, int p_edicion, String p_editorial, int p_anio) {
        Biblioteca biblioteca = this.bibliotecaActiva();

        Libro libro = new Libro(p_titulo, p_edicion, p_editorial, p_anio);

        // Primero se guarda en la base de datos; si falla, el modelo no cambia.
        this.getLibroDAO().insertar(libro, this.getIdBibliotecaSeleccionada());
        biblioteca.agregarLibro(libro);
    }


    @Override
    public List<Object[]> filasLibrosConId() {
        List<Object[]> filas = this.getLibroDAO().obtenerFilasConIdPorBiblioteca(this.getIdBibliotecaSeleccionada());

        List<Libro> libros = this.bibliotecaActiva().getLibros();

        if (filas.size() != libros.size()) {
            throw new IllegalStateException("Los libros de la base de datos no coinciden con los de la biblioteca.");
        }

        for (int i = 0; i < filas.size(); i++) {
            Object[] fila = filas.get(i);

            String prestado = libros.get(i).prestado() ? "Si" : "No";

            filas.set(i, new Object[]{
                    fila[0],
                    fila[1],
                    fila[2],
                    fila[3],
                    fila[4],
                    prestado
            });
        }

        return filas;
    }


    @Override
    public void modificarLibro(int p_idLibro, String p_titulo, int p_edicion, String p_editorial, int p_anio) {

        Biblioteca biblioteca = this.bibliotecaActiva();

        List<Object[]> filas = this.getLibroDAO().obtenerFilasConIdPorBiblioteca(this.getIdBibliotecaSeleccionada());

        int indice = -1;

        for (int i = 0; i < filas.size(); i++) {
            if ((int) filas.get(i)[0] == p_idLibro) {
                indice = i;
                break;
            }
        }

        if (indice == -1 || indice >= biblioteca.getLibros().size()) {
            throw new IllegalArgumentException("No se encontró el libro seleccionado.");
        }

        Libro libroActual = biblioteca.getLibros().get(indice);

        if (libroActual.prestado()) {
            throw new IllegalStateException("No se puede modificar un libro que está prestado.");
        }

        Libro libroModificado = new Libro(p_titulo, p_edicion, p_editorial, p_anio, libroActual.getPrestamos());

        boolean modificado = this.getLibroDAO().modificar(p_idLibro, libroModificado);

        if (!modificado) {
            throw new IllegalStateException("No se pudo modificar el libro.");
        }

        biblioteca.getLibros().set(indice, libroModificado);
    }



    @Override
    public void eliminarLibro(int p_idLibro) {
        Biblioteca biblioteca = this.bibliotecaActiva();

        List<Object[]> filas = this.getLibroDAO().obtenerFilasConIdPorBiblioteca(this.getIdBibliotecaSeleccionada());

        int indice = -1;

        for (int i = 0; i < filas.size(); i++) {
            if ((int) filas.get(i)[0] == p_idLibro) {
                indice = i;
                break;
            }
        }

        if (indice == -1 || indice >= biblioteca.getLibros().size()) {
            throw new IllegalArgumentException("No se encontró el libro seleccionado.");
        }

        Libro libro = biblioteca.getLibros().get(indice);

        if (libro.prestado()) {
            throw new IllegalStateException("No se puede eliminar un libro que está prestado.");
        }

        boolean eliminado = this.getLibroDAO().eliminar(p_idLibro);

        if (!eliminado) {
            throw new IllegalStateException("No se pudo eliminar el libro.");
        }

        biblioteca.quitarLibro(libro);
    }



    // >>> socios <<<

    @Override
    public List<Object[]> filasSocios() {
        List<Object[]> filas = new ArrayList<>();

        for (Socio socioActual : this.bibliotecaActiva().getSocios()) {
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
    public void nuevoSocioEstudiante(int p_dni, String p_nombre, String p_carrera) {
        this.registrarSocio(new Estudiante(p_dni, p_nombre, p_carrera));
    }

    @Override
    public void nuevoSocioDocente(int p_dni, String p_nombre, String p_area) {
        this.registrarSocio(new Docente(p_dni, p_nombre, p_area));
    }

    @Override
    public String datoEspecificoSocio(int p_dni) {

        Socio socio = this.bibliotecaActiva().buscarSocio(p_dni);

        if (socio == null) {
            throw new IllegalArgumentException("No se encontró el socio.");
        }

        if (socio instanceof Estudiante) {
            return ((Estudiante) socio).getCarrera();
        }

        if (socio instanceof Docente) {
            return ((Docente) socio).getArea();
        }

        throw new IllegalStateException("Tipo de socio desconocido.");
    }

    @Override
    public void modificarSocio(
            int p_dni, String p_nombre, String p_datoEspecifico) {

        Biblioteca biblioteca = this.bibliotecaActiva();

        Socio socioActual = biblioteca.buscarSocio(p_dni);

        if (socioActual == null) {
            throw new IllegalArgumentException("No se encontró el socio seleccionado.");
        }

        Socio socioModificado;

        if (socioActual instanceof Docente) {
            Docente docenteActual = (Docente) socioActual;

            Docente docenteModificado = new Docente(p_dni, p_nombre, p_datoEspecifico);

            // Conserva los días de préstamo originales.
            docenteModificado.cambiarDiasDePrestamo(docenteActual.getDiasPrestamo() - docenteModificado.getDiasPrestamo());

            socioModificado = docenteModificado;

        } else {
            socioModificado = new Estudiante(p_dni, p_nombre, p_datoEspecifico);
        }

        // Conserva el historial de préstamos del socio.
        for (Prestamo prestamo : socioActual.getPrestamos()) {
            socioModificado.agregarPrestamo(prestamo);
        }

        boolean modificado = this.getSocioDAO().modificar(socioModificado);

        if (!modificado) {
            throw new IllegalStateException("No se pudo modificar el socio.");
        }

        int indice = biblioteca.getSocios().indexOf(socioActual);
        biblioteca.getSocios().set(indice, socioModificado);
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

        boolean eliminado = this.getSocioDAO().eliminar(p_dni, socio.soyDeLaClase());

        if (!eliminado) {
            throw new IllegalStateException("No se pudo eliminar el socio. Puede tener préstamos registrados.");
        }

        biblioteca.quitarSocio(socio);
    }
    // >>> préstamos <<<

    @Override
    public boolean prestarLibro(int p_dni, String p_titulo) {
        Biblioteca biblioteca = this.bibliotecaActiva();

        Socio socio = biblioteca.buscarSocio(p_dni);
        Libro libro = this.buscarLibro(p_titulo, false);

        if (socio == null || libro == null) {
            return false;
        }

        boolean prestado = biblioteca.prestarLibro(new GregorianCalendar(), socio, libro);

        if (prestado) {
            Prestamo prestamo = libro.ultimoPrestamo();

            try {
                int idLibro = this.obtenerIdLibro(libro);

                this.getPrestamoDAO().insertar(prestamo, idLibro);

            } catch (RuntimeException excepcion) {
                libro.quitarPrestamo(prestamo);
                socio.quitarPrestamo(prestamo);
                throw excepcion;
            }
        }

        return prestado;
    }

    @Override
    public void devolverLibro(String p_titulo) throws LibroNoPrestadoException {
        Biblioteca biblioteca = this.bibliotecaActiva();

        Libro libro = this.buscarLibroParaConsulta(p_titulo);

        if (libro == null || !libro.prestado()) {
            throw new LibroNoPrestadoException("El libro " + p_titulo+ " no se puede devolver ya que se encuentra en la biblioteca");
        }

        biblioteca.devolverLibro(libro);

        Prestamo prestamo = libro.ultimoPrestamo();

        try {
            int idLibro = this.obtenerIdLibro(libro);

            this.getPrestamoDAO().registrarDevolucion(
                    idLibro,
                    prestamo.getFechaDevolucion());

        } catch (RuntimeException excepcion) {
            prestamo.registrarFechaDevolucion(null);
            throw excepcion;
        }
    }

    // >>> informes de la consigna <<<

    @Override
    public int cantidadDeSociosPorTipo(String p_tipo) {
        return this.bibliotecaActiva().cantidadDeSociosPorTipo(p_tipo);
    }

    @Override
    public String quienTieneElLibro(String p_titulo) throws LibroNoPrestadoException {
        Libro libro = this.buscarLibroParaConsulta(p_titulo);

        if (libro == null) {
            throw new LibroNoPrestadoException("El libro se encuentra en la biblioteca");
        }

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
     * Obtiene el identificador de base de datos correspondiente a un libro.
     *
     * La asociación se realiza respetando el orden por idLibro utilizado
     * por LibroDAO y la colección de libros de Biblioteca.
     *
     * @param p_libro libro cuyo identificador se desea obtener
     * @return identificador del libro en la base de datos
     */
    private int obtenerIdLibro(Libro p_libro) {

        List<Object[]> filas = this.getLibroDAO().obtenerFilasConIdPorBiblioteca(this.getIdBibliotecaSeleccionada());

        List<Libro> libros = this.bibliotecaActiva().getLibros();

        if (filas.size() != libros.size()) {
            throw new IllegalStateException("Los libros de la base de datos no coinciden con los de la biblioteca.");
        }

        for (int i = 0; i < libros.size(); i++) {

            if (libros.get(i) == p_libro) {
                return ((Number) filas.get(i)[0]).intValue();
            }
        }

        throw new IllegalArgumentException("No se encontró el identificador del libro seleccionado.");
    }

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
        for (Libro libroActual : this.bibliotecaActiva().getLibros()) {
            if (libroActual.getTitulo().equals(p_titulo) && libroActual.prestado() == p_prestado) {
                return libroActual;
            }
        }
        return null;
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
