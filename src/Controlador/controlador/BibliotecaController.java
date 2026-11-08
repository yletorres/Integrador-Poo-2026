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
 * Cambiar una firma requiere avisar al equipo
 */
public interface BibliotecaController { /* define métodos que otras clases están obligadas a implementar */

    // >>>> bibliotecas <<<<<

    /**
     * Devuelve los nombres de todas las bibliotecas, para el selector de bibliotecas.
     *
     * @return lista con los nombres de las bibliotecas
     */
    public abstract List<String> nombresDeBibliotecas();

    /**
     * Selecciona la biblioteca con la que se va a trabajar. Las demás operaciones
     * se aplican sobre la biblioteca seleccionada.
     *
     * @param p_nombre nombre de la biblioteca a seleccionar
     * @throws IllegalArgumentException si no existe una biblioteca con ese nombre
     */
    public abstract void seleccionarBiblioteca(String p_nombre);

    /**
     * Crea una biblioteca nueva y la deja seleccionada.
     *
     * @param p_nombre nombre de la nueva biblioteca
     * @throws IllegalArgumentException si el nombre es null o está vacío
     */
    public abstract void crearBiblioteca(String p_nombre);

    /**
     * Elimina la biblioteca seleccionada junto con sus libros, socios y préstamos.
     * Después de eliminarla no queda ninguna biblioteca seleccionada.
     *
     * @throws IllegalStateException si no hay una biblioteca seleccionada
     */
    public abstract void eliminarBibliotecaSeleccionada();

    // >>> libros <<<<

    /**
     * Devuelve las filas de la tabla de libros de la biblioteca seleccionada.
     * Columnas: título, edición, editorial, año y prestado ("Si" o "No"),
     * como indica la cátedra.
     *
     * @return una fila por cada libro
     */
    public abstract List<Object[]> filasLibros();

    /**
     * Agrega un libro a la biblioteca seleccionada.
     *
     * @param p_titulo    título del libro
     * @param p_edicion   número de edición
     * @param p_editorial editorial del libro
     * @param p_anio      año de publicación
     */
    public abstract void nuevoLibro(String p_titulo, int p_edicion, String p_editorial, int p_anio);

    // >>> socios <<<

    /**
     * Devuelve las filas de la tabla de socios de la biblioteca seleccionada.
     * Columnas: DNI, nombre, tipo ("Docente" o "Estudiante") y cantidad de
     * libros prestados.
     *
     * @return una fila por cada socio
     */
    public abstract List<Object[]> filasSocios();

    public abstract void eliminarSocio(int p_dni);
    /**
     * Registra un socio estudiante en la biblioteca seleccionada.
     *
     * @param p_dni     DNI del estudiante
     * @param p_nombre  nombre y apellido del estudiante
     * @param p_carrera carrera que cursa
     */
    public abstract void nuevoSocioEstudiante(int p_dni, String p_nombre, String p_carrera);

    /**
     * Registra un socio docente en la biblioteca seleccionada.
     *
     * @param p_dni    DNI del docente
     * @param p_nombre nombre y apellido del docente
     * @param p_area   área a la que pertenece
     */
    public abstract void nuevoSocioDocente(int p_dni, String p_nombre, String p_area);

    // >>> préstamos <<<

    /**
     * Presta un libro a un socio con la fecha de hoy. Si el título tiene varios
     * ejemplares, se presta uno que esté disponible.
     *
     * @param p_dni    DNI del socio que retira el libro
     * @param p_titulo título del libro a prestar
     * @return true si se registró el préstamo; false si no existe el socio, no hay un
     *         ejemplar disponible del título o el socio no puede pedir libros
     */
    public abstract boolean prestarLibro(int p_dni, String p_titulo);

    /**
     * Registra la devolución de un libro con la fecha de hoy.
     *
     * @param p_titulo título del libro que se devuelve
     * @throws LibroNoPrestadoException si ningún ejemplar de ese título está prestado
     * @throws IllegalArgumentException si no existe ningún libro con ese título
     */
    public abstract void devolverLibro(String p_titulo) throws LibroNoPrestadoException;

    /**
     * Registra la devolución del ejemplar de un título que tiene un socio determinado,
     * con la fecha de hoy. Sirve cuando hay varios ejemplares prestados del mismo título.
     *
     * @param p_titulo título del libro que se devuelve
     * @param p_dni    DNI del socio que lo devuelve
     * @throws LibroNoPrestadoException si ese socio no tiene en préstamo un ejemplar de ese título
     * @throws IllegalArgumentException si no existe ningún libro con ese título
     */
    public abstract void devolverLibro(String p_titulo, int p_dni) throws LibroNoPrestadoException;

    /**
     * Arma las filas de los libros que están prestados, con quién los tiene.
     * Columnas: título, DNI del socio, nombre del socio y estado ("Prestado" o "Vencido").
     *
     * @return una fila por cada ejemplar prestado de la biblioteca seleccionada
     */
    public abstract List<Object[]> filasPrestamosActivos();

    // >>> informes de la consigna <<<

    /**
     * Cuenta los socios de la biblioteca seleccionada que son de un tipo dado.
     *
     * @param p_tipo tipo de socio: "Docente" o "Estudiante"
     * @return cantidad de socios de ese tipo
     */
    public abstract int cantidadDeSociosPorTipo(String p_tipo);

    /**
     * Informa quién tiene un libro en su poder.
     *
     * @param p_titulo título del libro a consultar
     * @return nombre del socio que tiene el libro
     * @throws LibroNoPrestadoException si el libro no está prestado
     * @throws IllegalArgumentException si no existe ningún libro con ese título
     */
    public abstract String quienTieneElLibro(String p_titulo) throws LibroNoPrestadoException;

    /**
     * Arma el listado de socios, con la cantidad de estudiantes y de docentes.
     *
     * @return texto con la lista de socios
     */
    public abstract String listaDeSocios();

    /**
     * Arma el listado de libros, indicando si cada uno está prestado.
     *
     * @return texto con la lista de libros
     */
    public abstract String listaDeLibros();

    /**
     * Arma el listado de títulos, sin repetir los que tienen varios ejemplares.
     *
     * @return texto con la lista de títulos
     */
    public abstract String listaDeTitulos();

    /**
     * Arma el listado de docentes responsables, es decir, los que no tienen
     * préstamos vencidos.
     *
     * @return texto con la lista de docentes responsables
     */
    public abstract String listaDeDocentesResponsables();

    /**
     * Devuelve las filas de libros con el id como primera columna.
     * Columnas: id, título, edición, editorial, año y prestado ("Si" o "No").
     *
     * @return una fila por cada libro
     */
    public abstract List<Object[]> filasLibrosConId();

    /**
     * Elimina un libro de la biblioteca seleccionada.
     *
     * @param p_idLibro id del libro a eliminar
     * @throws IllegalArgumentException si no existe un libro con ese id
     * @throws IllegalStateException    si el libro está prestado
     */
    public abstract void eliminarLibro(int p_idLibro);

}