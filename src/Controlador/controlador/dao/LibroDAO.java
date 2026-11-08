package controlador.dao;

import biblioteca.libro.Libro;
import java.util.List;

public interface LibroDAO {

    List<Libro> obtenerPorBiblioteca(int idBiblioteca);

    void insertar(Libro libro, int idBiblioteca);

    Libro buscarPorId(int idLibro);

    boolean eliminar(int idLibro);

    List<Object[]> obtenerFilasConIdPorBiblioteca(int idBiblioteca);
}
