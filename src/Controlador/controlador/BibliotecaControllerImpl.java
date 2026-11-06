package controlador;

import biblioteca.Biblioteca;
import biblioteca.excepciones.LibroNoPrestadoException;
import controlador.dao.BibliotecaDAO;

import java.util.List;

public class BibliotecaControllerImpl implements BibliotecaController{

    private BibliotecaDAO bibliotecaDAO;
    private Biblioteca bibliotecaSeleccionada;
    private int idBibliotecaSeleccionada;

    public BibliotecaControllerImpl(BibliotecaDAO bibliotecaDAO) {
        this.bibliotecaDAO = bibliotecaDAO;
        this.bibliotecaSeleccionada = null;
        this.idBibliotecaSeleccionada = -1;
    }

    // >>>> bibliotecas <<<<<
    @Override
    public List<String> nombresDeBibliotecas(){
        return bibliotecaDAO.obtenerNombres();
    }

    @Override
    public void seleccionarBiblioteca(String nombre){

        Integer id = bibliotecaDAO.obtenerIdPorNombre(nombre);

        if (id == null) {
            throw new IllegalArgumentException(
                    "No existe una biblioteca con ese nombre"
            );
        }

        this.idBibliotecaSeleccionada = id;
    }

    @Override
    public void crearBiblioteca(String nombre) {

        Biblioteca biblioteca = new Biblioteca(nombre);

        bibliotecaDAO.insertar(biblioteca);

        seleccionarBiblioteca(nombre);
    }

    // >>> libros <<<<
    /** col: titulo, edicion, editorial, anio, prestado ("Si"/"No") como da la catedra*/
    List<Object[]> filasLibros(){


    }

    void nuevoLibro(String titulo, int edicion, String editorial, int anio){


    }

    // >>> socios <<<
    /** col: dni, nombre, tipo ("Docente"/"Estudiante"), librosPrestados */
    List<Object[]> filasSocios(){

    }
    void nuevoSocioEstudiante(int dni, String nombre, String carrera){

    }
    void nuevoSocioDocente(int dni, String nombre, String area){

    }

    // >>> préstamos <<<
    /** @return false si el socio no puede pedir o el libro ya está prestado, como pide la excepción */
    boolean prestarLibro(int dni, String titulo);
    void devolverLibro(String titulo) throws LibroNoPrestadoException;

    // >>> informes de la consigna <<<
    int cantidadDeSociosPorTipo(String tipo);
    String quienTieneElLibro(String titulo) throws LibroNoPrestadoException;
    String listaDeSocios();
    String listaDeLibros();
    String listaDeTitulos();
    String listaDeDocentesResponsables();



}
