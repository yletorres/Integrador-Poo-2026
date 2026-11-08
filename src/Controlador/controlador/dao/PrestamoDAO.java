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

        /**
         * Registra la fecha de devolución de un préstamo, identificándolo por su
         * id en la base de datos (el mismo objeto Prestamo que modificó el modelo).
         *
         * @param prestamo         préstamo del modelo que se devolvió
         * @param fechaDevolucion  fecha de devolución
         */
        void registrarDevolucion(
                Prestamo prestamo,
                Calendar fechaDevolucion);
}