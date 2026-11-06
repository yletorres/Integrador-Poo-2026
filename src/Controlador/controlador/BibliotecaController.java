package controlador;

import biblioteca.excepciones.LibroNoPrestadoException;
import java.util.List;

/**
 * Contrato entre la vista y la lógica. La vista solo usa esta interfaz:
 * no importa java.sql ni clases del modelo.
 *
 * Las tablas se entregan como filas (Object[]) para que la vista no dependa
 * de cómo está implementado el modelo.
 *
 * Cambiar una firma requiere avisar al equipo.
 */

public interface BibliotecaController { /*define métodos que otras clases están obligadas a implementar */

    // >>>> bibliotecas <<<<<
    public abstract List<String> nombresDeBibliotecas();
    public abstract void seleccionarBiblioteca(String nombre);
    public abstract void crearBiblioteca(String nombre);

    // >>> libros <<<<
    /** col: titulo, edicion, editorial, anio, prestado ("Si"/"No") como da la catedra*/
    public abstract List<Object[]> filasLibros();
    public abstract void nuevoLibro(String titulo, int edicion, String editorial, int anio);

    // >>> socios <<<
    /** col: dni, nombre, tipo ("Docente"/"Estudiante"), librosPrestados */
    public abstract List<Object[]> filasSocios();
    public abstract void nuevoSocioEstudiante(int dni, String nombre, String carrera);
    public abstract void nuevoSocioDocente(int dni, String nombre, String area);

    // >>> préstamos <<<
    /** @return false si el socio no puede pedir o el libro ya está prestado, como pide la excepción */
    public abstract boolean prestarLibro(int dni, String titulo);
    public abstract void devolverLibro(String titulo) throws LibroNoPrestadoException;

    // >>> informes de la consigna <<<
    public abstract int cantidadDeSociosPorTipo(String tipo);
    public abstract String quienTieneElLibro(String titulo) throws LibroNoPrestadoException;
    public abstract String listaDeSocios();
    public abstract String listaDeLibros();
    public abstract String listaDeTitulos();
    public abstract String listaDeDocentesResponsables();
}