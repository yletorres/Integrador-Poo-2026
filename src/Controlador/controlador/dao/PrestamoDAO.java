package controlador.dao;

import biblioteca.libro.Libro;
import biblioteca.socio.Socio;
import biblioteca.prestamo.Prestamo;
import java.util.List;
import java.util.Calendar;

/**
 * Contrato de acceso a los datos de los préstamos: cómo se cargan, se guardan
 * y se registra su devolución en la base de datos.
 *
 * @author Yleana Torres
 * @version TP7 - 2026
 */
public interface PrestamoDAO {

        /**
         * Carga los préstamos de una biblioteca y los vincula con sus libros y socios.
         *
         * @param p_libros       libros de la biblioteca
         * @param p_socios       socios de la biblioteca
         * @param p_idBiblioteca id de la biblioteca en la base de datos
         */
        void cargarPrestamos(
                List<Libro> p_libros,
                List<Socio> p_socios,
                int p_idBiblioteca);

        /**
         * Guarda un préstamo nuevo en la base de datos.
         *
         * @param p_prestamo     préstamo que se guarda
         * @param p_idBiblioteca id de la biblioteca en la base de datos
         */
        void insertar(Prestamo p_prestamo, int p_idBiblioteca);

        /**
         * Registra la fecha de devolución de un préstamo, identificándolo por su
         * id en la base de datos (el mismo objeto Prestamo que modificó el modelo).
         *
         * @param p_prestamo        préstamo del modelo que se devolvió
         * @param p_fechaDevolucion fecha de devolución
         */
        void registrarDevolucion(
                Prestamo p_prestamo,
                Calendar p_fechaDevolucion);
}