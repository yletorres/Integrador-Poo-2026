package controlador.dao;

import biblioteca.libro.Libro;
import biblioteca.socio.Socio;
import biblioteca.prestamo.Prestamo;
import java.util.List;
import java.util.Calendar;

public interface PrestamoDAO {

        void cargarPrestamos(
                        List<Libro> libros,
                        List<Socio> socios,
                        int idBiblioteca);

        void insertar(Prestamo prestamo, int idBiblioteca);

        void registrarDevolucion(
                        String titulo,
                        int idBiblioteca,
                        Calendar fechaDevolucion);
}
