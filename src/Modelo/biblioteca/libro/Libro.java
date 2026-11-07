package biblioteca.libro;

import biblioteca.prestamo.Prestamo;

import java.util.ArrayList;

/**
 * Clase Libro
 * - ultimoPrestamo(): retorna el último préstamo del libro.
 * - prestado(): devuelve true si el libro se encuentra prestado.
 * - toString(): devuelve el siguiente String: Titulo: <<titulo>>
 * Ejemplo: Titulo: JAVA. Como Programar
 * 
 * @author (Fernández, Lucía Belén)
 */

public class Libro {
    /*
     * 
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
     * @param p_titulo    Título del libro.
     * @param p_edicion   Número de edición.
     * @param p_editorial Nombre de la editorial.
     * @param p_anio      Año de publicación.
     *
     * @author (Fernández, Lucía Belén)
     */
    public Libro(String p_titulo, int p_edicion, String p_editorial, int p_anio) {
        this.setTitulo(p_titulo);
        this.setEdicion(p_edicion);
        this.setEditorial(p_editorial);
        this.setAnio(p_anio);
        this.setPrestamos(new ArrayList<Prestamo>());
    }

    /**
     * Constructor (Caso n): Inicializa un libro asignándole un historial de
     * préstamos preexistente.
     *
     * @param p_titulo    Título del libro.
     * @param p_edicion   Número de edición.
     * @param p_editorial Nombre de la editorial.
     * @param p_anio      Año de publicación.
     * @param p_prestamos Colección con el historial de préstamos del libro.
     * @author (Fernández, Lucía Belén)
     */
    public Libro(String p_titulo, int p_edicion, String p_editorial, int p_anio, ArrayList<Prestamo> p_prestamos) {
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
     * @author (Fernández, Lucía Belén)
     */
    private void setTitulo(String p_titulo) {
        this.titulo = p_titulo;
    }

    /**
     * Asigna el número de edición.
     *
     * @param p_edicion Número de edición.
     * @author (Fernández, Lucía Belén)
     */
    private void setEdicion(int p_edicion) {
        this.edicion = p_edicion;
    }

    /**
     * Asigna el nombre de la editorial.
     *
     * @param p_editorial Nombre de la editorial.
     * @author (Fernández, Lucía Belén)
     */
    private void setEditorial(String p_editorial) {
        this.editorial = p_editorial;
    }

    /**
     * Asigna el año de publicación.
     *
     * @param p_anio Año de publicación.
     * @author (Fernández, Lucía Belén)
     */
    private void setAnio(int p_anio) {
        this.anio = p_anio;
    }

    /**
     * Asigna la colección de préstamos.
     * Si recibe null, inicializa una colección vacía para evitar
     * NullPointerException en los métodos que operan sobre ella.
     *
     * @param p_prestamos Colección de tipo ArrayList.
     * @author (Fernández, Lucía Belén)
     */
    private void setPrestamos(ArrayList<Prestamo> p_prestamos) {
        if (p_prestamos == null) {
            this.prestamos = new ArrayList<Prestamo>();
        } else {
            this.prestamos = p_prestamos;
        }
    }

    /** GETTERS */
    /**
     * Devuelve el título del libro.
     *
     * @return Título actual.
     * @author (Fernández, Lucía Belén)
     */
    public String getTitulo() {
        return this.titulo;
    }

    /**
     * Devuelve el número de edición.
     *
     * @return Número de edición actual.
     * @author (Fernández, Lucía Belén)
     */
    public int getEdicion() {
        return this.edicion;
    }

    /**
     * Devuelve el nombre de la editorial.
     *
     * @return Nombre de la editorial actual.
     * @author (Fernández, Lucía Belén)
     */
    public String getEditorial() {
        return this.editorial;
    }

    /**
     * Devuelve el año de publicación.
     *
     * @return Año de publicación actual.
     * @author (Fernández, Lucía Belén)
     */
    public int getAnio() {
        return this.anio;
    }

    /**
     * Devuelve el historial de préstamos del libro.
     *
     * @return Colección de objetos Prestamo.
     * @author (Fernández, Lucía Belén)
     */
    public ArrayList<Prestamo> getPrestamos() {
        return this.prestamos;
    }

    /** METODOS */

    /**
     * Agrega un préstamo al historial de préstamos del libro.
     * Usa doble encapsulamiento accediendo a la colección mediante
     * this.getPrestamos() en lugar del atributo directo, y delega
     * la inserción en el método add().
     *
     * @param p_prestamo Objeto Prestamo que se desea registrar en el historial del
     *                   libro.
     * @return true si el préstamo se agregó correctamente a la colección
     * @author (Fernández, Lucía Belén)
     */
    public boolean agregarPrestamo(Prestamo p_prestamo) {
        return this.getPrestamos().add(p_prestamo);
    }

    /**
     * Quita un préstamo del historial de préstamos del libro.
     * Utiliza doble encapsulamiento accediendo a la colección mediante
     * this.getPrestamos() en lugar del atributo directo, y delega
     * la eliminación en el método remove().
     * Si la colección está vacía, no se intenta la eliminación y se retorna
     * false directamente.
     *
     * @param p_prestamo Objeto Prestamo que se desea eliminar del historial del
     *                   libro.
     * @return true si el préstamo se eliminó correctamente;
     *         false si la colección está vacía o el préstamo no se encuentra en
     *         ella.
     * @author (Fernández, Lucía Belén)
     */
    public boolean quitarPrestamo(Prestamo p_prestamo) {
        if (this.getPrestamos().size() > 0) {
            return this.getPrestamos().remove(p_prestamo);
        }
        return false;
    }

    /**
     * Retorna el último préstamo del libro. Si la lista contiene elementos,
     * calcula la última posición restándole 1 al tamaño total de la colección
     * (size() - 1) y extrae el objeto Prestamo ubicado ahí.
     * Si la lista está vacía, retorna null para evitar un error.
     * 
     * @return Prestamo, último préstamo del libro
     * @author (Fernández, Lucía Belén)
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
     * todavía es nula (o no está registrada), significa que el libro no fue
     * devuelto.
     * 
     * @author (Fernández, Lucía Belén)
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
     * @author (Fernández, Lucía Belén)
     */
    public String toString() {
        return "Titulo: " + this.getTitulo();
    }

}