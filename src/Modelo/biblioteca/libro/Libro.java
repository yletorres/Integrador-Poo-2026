package Modelo.biblioteca.libro;

import Modelo.biblioteca.prestamo.Prestamo;

import java.util.ArrayList;
/**
 * Clase Libro
 * - ultimoPrestamo(): retorna el último préstamo del libro. 
 * - prestado(): devuelve true si el libro se encuentra prestado. 
 * - toString(): devuelve el siguiente String:  Titulo: <<titulo>>
 *                                    Ejemplo:  Titulo: JAVA. Como Programar
 * 
 * @author (Fernández, Lucía Belén) 
 */

public class Libro {
    /*

     * Atributos
     */
    private String titulo;
    private int edicion;
    private String editorial;
    private int anio;
    private ArrayList<Prestamo> prestamos;

    /**
     * Constructor (Caso 0): Inicializa un libro recién adquirido.
     * Al no tener historial previo, se instancia la colección de préstamos vacía.
     *
     * @param p_titulo Título del libro.
     * @param p_edicion Número de edición.
     * @param p_editorial Nombre de la editorial.
     * @param p_anio Año de publicación.
     */
    public Libro (String p_titulo, int p_edicion, String p_editorial, int p_anio){
        this.setTitulo(p_titulo);
        this.setEdicion(p_edicion);
        this.setEditorial(p_editorial);
        this.setAnio(p_anio);
        this.setPrestamos(new ArrayList() );
    }

    /**
     * Constructor (Caso n): Inicializa un libro asignándole un historial de préstamos preexistente.
     *
     * @param p_titulo Título del libro.
     * @param p_edicion Número de edición.
     * @param p_editorial Nombre de la editorial.
     * @param p_anio Año de publicación.
     * @param p_prestamos Colección con el historial de préstamos del libro.
     */
    public Libro (String p_titulo, int p_edicion, String p_editorial, int p_anio, ArrayList<Prestamo> p_prestamos){
        this.setTitulo(p_titulo);
        this.setEdicion(p_edicion);
        this.setEditorial(p_editorial);
        this.setAnio(p_anio);
        this.setPrestamos(p_prestamos);
    }
    
    /** SETTERS */
    /**
     * Asigna el título al libro.
     *
     * @param p_titulo Título del libro.
     */
    private void setTitulo(String p_titulo){
        this.titulo = p_titulo;
    }

    /**
     * Asigna el número de edición.
     *
     * @param p_edicion Número de edición.
     */
    private void setEdicion(int p_edicion){
        this.edicion = p_edicion;
    }

    /**
     * Asigna el nombre de la editorial.
     *
     * @param p_editorial Nombre de la editorial.
     */
    private void setEditorial(String p_editorial){
        this.editorial = p_editorial;
    }

    /**
     * Asigna el año de publicación.
     *
     * @param p_anio Año de publicación.
     */
    private void setAnio(int p_anio){
        this.anio = p_anio;
    }

    /**
     * Asigna la colección de préstamos.
     *
     * @param p_prestamos Colección de tipo ArrayList.
     */
    private void setPrestamos(ArrayList<Prestamo> p_prestamos){
        this.prestamos = p_prestamos;
    }
    
    /** GETTERS */
    /**
     * Devuelve el título del libro.
     *
     * @return Título actual.
     */
    public String getTitulo (){
        return this.titulo;
    }

    /**
     * Devuelve el número de edición.
     *
     * @return Número de edición actual.
     */
    public int getEdicion (){
        return this.edicion;
    }

    /**
     * Devuelve el nombre de la editorial.
     *
     * @return Nombre de la editorial actual.
     */
    public String getEditorial (){
        return this.editorial;
    }

    /**
     * Devuelve el año de publicación.
     *
     * @return Año de publicación actual.
     */
    public int getAnio (){
        return this.anio;
    }

    /**
     * Devuelve el historial de préstamos del libro.
     *
     * @return Colección de objetos Prestamo.
     */
    public ArrayList<Prestamo> getPrestamos (){
        return this.prestamos;
    }
    
    /** METODOS */

    /**
     * Retorna el último préstamo del libro. Si la lista contiene elementos,
     * calcula la última posición restándole 1 al tamaño total de la colección
     * (size() - 1) y extrae el objeto Prestamo ubicado ahí.
     * Si la lista está vacía, retorna null para evitar un error.
     */
    public Prestamo ultimoPrestamo() {
        if (this.getPrestamos().isEmpty()) {
            return null;
        }
        return this.getPrestamos().get(this.getPrestamos().size() - 1);
    }

    /**
     * Devuelve true si el libro se encuentra prestado.
     * Se obtiene el último préstamo. Si existe y su fecha de devolución
     * todavía es nula (o no está registrada), significa que el libro no fue devuelto.
     */
    public boolean prestado() {
        Prestamo ultimo = this.ultimoPrestamo();
        if (ultimo != null && ultimo.getFechaDevolucion() == null) {
            return true;
        }
        return false;
    }

    /**
     * Devuelve una cadena de texto con el título del libro.
     *
     * @return Texto con el formato "Titulo: [titulo del libro]".
     */
    public String toString () {
        return "Titulo: " + this.getTitulo();
    }
    
}