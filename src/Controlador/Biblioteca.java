package Controlador;

import java.util.ArrayList;
import java.util.Calendar;
import Controlador.libro.Libro;
import Controlador.excepciones.LibroNoPrestadoException;
/**
 * Representa una biblioteca que administra socios, libros y préstamos.
 * Permite agregar y quitar libros, registrar préstamos y devoluciones,
 * y consultar información relacionada con los libros y socios.
 *
 * @author Azula Iara Elizabeth
 */
public class Biblioteca {
    private String nombre;
    private ArrayList<Socio> socios;
    private ArrayList<Libro> libros;

    /**
     * Construye una biblioteca con un nombre y un libro.
     * Inicializa una colección vacía de socios y agrega el libro recibido.
     *
     * @param p_nombre Nombre de la biblioteca.
     * @param p_libro Libro que se incorpora inicialmente.
     */
    public Biblioteca(String p_nombre, Libro p_libro) {
        this.setNombre(p_nombre);
        this.setSocios(new ArrayList<Socio>());
        this.setLibros(new ArrayList<Libro>());
        this.agregarLibro(p_libro);
    }

    /**
     * Construye una biblioteca con un nombre y una colección de libros.
     * Inicializa una colección vacía de socios.
     *
     * @param p_nombre Nombre de la biblioteca.
     * @param p_libros Colección de libros de la biblioteca.
     */
    public Biblioteca(String p_nombre, ArrayList<Libro> p_libros) {
        this.setNombre(p_nombre);
        this.setSocios(new ArrayList<Socio>());
        this.setLibros(p_libros);
    }

    /**
     * Construye una biblioteca con un nombre, un socio y un libro.
     * Inicializa las colecciones y agrega el socio y el libro recibidos.
     *
     * @param p_nombre Nombre de la biblioteca.
     * @param p_socio Socio que se incorpora inicialmente.
     * @param p_libro Libro que se incorpora inicialmente.
     */
    public Biblioteca(String p_nombre, Socio p_socio, Libro p_libro) {
        this.setNombre(p_nombre);
        this.setSocios(new ArrayList<Socio>());
        this.setLibros(new ArrayList<Libro>());
        this.agregarSocio(p_socio);
        this.agregarLibro(p_libro);
    }

    // SETTERS

    /**
     * Asigna el nombre de la biblioteca.
     *
     * @param p_nombre Nombre de la biblioteca.
     * @throws NullPointerException si el nombre es nulo o vacío.
     */
    private void setNombre(String p_nombre) {
        if (p_nombre == null || p_nombre.isEmpty()) {
            throw new NullPointerException("El nombre de la biblioteca no puede ser nulo o vacío");
        }
        this.nombre = p_nombre;
    }

    /**
     * Asigna la colección de libros de la biblioteca.
     * Si recibe null, inicializa una colección vacía.
     *
     * @param p_libros Colección de libros que se asignará.
     */
    private void setLibros(ArrayList<Libro> p_libros) {
        if (p_libros == null) {
            this.libros = new ArrayList<Libro>();
        } else {
            this.libros = p_libros;
        }
    }

// GETTERS

    /**
     * Obtiene el nombre de la biblioteca.
     *
     * @return Nombre de la biblioteca.
     */
    public String getNombre() {
        return this.nombre;
    }

    /**
     * Obtiene la colección de libros de la biblioteca.
     *
     * @return Colección de libros.
     */
    public ArrayList<Libro> getLibros() {
        return this.libros;
    }

    /**
     * Agrega un libro a la colección de libros de la biblioteca.
     *
     * @param p_libro Libro que se desea agregar.
     * @return true si el libro se agregó correctamente.
     */
    public boolean agregarLibro(Libro p_libro) {
        return this.getLibros().add(p_libro);
    }

    /**
     * Quita un libro de la colección de libros de la biblioteca.
     *
     * @param p_libro Libro que se desea quitar.
     * @return true si el libro se eliminó correctamente; false si la
     * colección está vacía o el libro no se encuentra en ella.
     */
    public boolean quitarLibro(Libro p_libro) {
        if(this.getLibros().size() > 0){
            return this.getLibros().remove(p_libro);
        }
        return false;
    }

    /**
     * Crea y agrega un nuevo libro.
     *
     * @param p_titulo    Título del libro.
     * @param p_edicion   Número de edición.
     * @param p_editorial Editorial del libro.
     * @param p_anio      Año de publicación.
     */
    public void nuevoLibro(String p_titulo, int p_edicion, String p_editorial, int p_anio){
        this.agregarLibro(new Libro(p_titulo, p_edicion, p_editorial, p_anio));
    }

    /**
     * Registra el préstamo de un libro a un socio.
     *
     * @param p_fechaRetiro Fecha de retiro del libro.
     * @param p_socio Socio que realiza el préstamo.
     * @param p_libro Libro a prestar.
     * @return true si el préstamo se realizó correctamente.
     */
    public boolean prestarLibro(Calendar p_fechaRetiro, Socio p_socio, Libro p_libro){
        if(p_socio.puedePedir() && !p_libro.prestado()){

            Prestamo nuevoPrestamo = new Prestamo(p_fechaRetiro, p_socio, p_libro);

            p_libro.agregarPrestamo(nuevoPrestamo);
            p_socio.agregarPrestamo(nuevoPrestamo);
            return true;
        }
        return false;
    }

    /**
     * Registra la devolución de un libro prestado.
     *
     * @param p_libro Libro a devolver.
     * @throws LibroNoPrestadoException si el libro no estaba prestado.
     */
    public void devolverLibro(Libro p_libro) throws LibroNoPrestadoException {
        if (p_libro.prestado()) {
            Calendar fechaDevolucion = Calendar.getInstance();

            p_libro.ultimoPrestamo().registrarFechaDevolucion(fechaDevolucion);

        } else {
            throw new LibroNoPrestadoException(
                    "El libro " + p_libro.getTitulo() +
                            " no se puede devolver ya que se encuentra en la biblioteca"
            );
        }
    }
        /**
         * Devuelve el nombre del socio que tiene prestado un libro.
         *
         * @param p_libro Libro a consultar.
         * @return Nombre del socio que tiene el libro.
         * @throws LibroNoPrestadoException Si el libro no está prestado.
         */
        public String quienTieneElLibro(Libro p_libro) throws LibroNoPrestadoException {
            if (!p_libro.prestado()) {
                throw new LibroNoPrestadoException("El libro se encuentra en la biblioteca");
            }

            return p_libro.ultimoPrestamo().getSocio().getNombre();
        }
    }
