package controlador.dao;

import biblioteca.libro.Libro;
import java.util.List;

/**
 * Define las operaciones de acceso a datos para la gestión de libros.
 *
 * @author Yleana Torres
 * @version TP7 - 2026
 */
public interface LibroDAO {

    /**
     * Devuelve los libros asociados a una biblioteca.
     *
     * @param p_idBiblioteca identificador de la biblioteca
     * @return lista de libros asociados a la biblioteca
     */
    List<Libro> obtenerPorBiblioteca(int p_idBiblioteca);

    /**
     * Registra un libro asociado a una biblioteca.
     *
     * @param p_libro libro que se desea insertar
     * @param p_idBiblioteca identificador de la biblioteca
     */
    void insertar(Libro p_libro, int p_idBiblioteca);

    /**
     * Elimina un libro a partir de su identificador.
     *
     * @param p_idLibro identificador del libro que se desea eliminar
     * @return true si se pudo eliminar, false en caso contrario
     */
    boolean eliminar(int p_idLibro);

    /**
     * Devuelve las filas de libros de una biblioteca incluyendo sus identificadores.
     *
     * @param p_idBiblioteca identificador de la biblioteca
     * @return lista de filas con los datos de los libros
     */
    List<Object[]> obtenerFilasConIdPorBiblioteca(int p_idBiblioteca);
}