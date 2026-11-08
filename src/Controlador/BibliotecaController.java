package controlador;

import biblioteca.excepciones.LibroNoPrestadoException;
import java.util.Calendar;
import java.util.List;

public interface BibliotecaController {
    boolean agregarSocioEstudiante(int dni, String nombre, String carrera);
    boolean agregarSocioDocente(int dni, String nombre, String area);
    boolean agregarLibro(String titulo, int edicion, String editorial, int anio);
    boolean quitarLibro(String titulo);
    boolean realizarPrestamo(int dniSocio, String tituloLibro, Calendar fechaRetiro);
    boolean devolverLibro(String tituloLibro) throws LibroNoPrestadoException;

    String quienTieneElLibro(String tituloLibro) throws LibroNoPrestadoException;
    int cantidadDeSociosPorTipo(String tipo);
    String listaDeDocentesResponsables();

    String listaDeSocios();
    String listaDeLibros();
    String listaDeTitulos();

    List<Object[]> filasLibros();
    List<Object[]> filasSocios();

    String[] nombresDeBibliotecas();
    void seleccionarBiblioteca(String nombre);
}