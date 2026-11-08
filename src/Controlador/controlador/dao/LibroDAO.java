package controlador.dao;

import biblioteca.libro.Libro;
import java.util.List;

public interface LibroDAO {

    List<Libro> obtenerPorBiblioteca(int idBiblioteca);

    void insertar(Libro libro, int idBiblioteca);

    boolean eliminar(int idLibro);

    List<Object[]> obtenerFilasConIdPorBiblioteca(int idBiblioteca);
}
