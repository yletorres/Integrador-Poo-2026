package biblioteca;

import biblioteca.libro.Libro;
import biblioteca.prestamo.Prestamo;
import biblioteca.socio.Socio;
import biblioteca.socio.Docente;
import biblioteca.socio.Estudiante;
import biblioteca.excepciones.LibroNoPrestadoException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
/**
 * Representa una biblioteca que administra sus socios y sus libros.
 *
 * @author Yleana Torres
 * @author Iara Azula
 * @author Josefina Vallejos Romero
 * @version TP5 - 2026
 */
public class Biblioteca {

    /** Nombre de la biblioteca. */
    private String nombre;

    /** Socios de la biblioteca. */
    private ArrayList<Socio> socios;

    /** Libros de la biblioteca. */
    private ArrayList<Libro> libros;

    /**
     * Crea una biblioteca sin socios ni libros.
     *
     * @author Yleana Torres
     * @param p_nombre nombre de la biblioteca
     * @throws IllegalArgumentException si p_nombre es null o está vacío
     */
    public Biblioteca(String p_nombre) {
        this.setNombre(p_nombre);
        this.setSocios(new ArrayList<Socio>());
        this.setLibros(new ArrayList<Libro>());
    }

    /**
     * Crea una biblioteca con un socio y una lista de libros.
     *
     * @author Yleana Torres
     * @param p_nombre nombre de la biblioteca
     * @param p_socio  socio inicial
     * @param p_libros libros iniciales
     * @throws IllegalArgumentException si p_nombre es null o está vacío
     */
    public Biblioteca(String p_nombre, Socio p_socio, ArrayList<Libro> p_libros) {
        this.setNombre(p_nombre);
        this.setSocios(new ArrayList<Socio>());
        this.agregarSocio(p_socio);
        this.setLibros(p_libros);
    }

    /**
     * Crea una biblioteca con una lista de socios y una lista de libros.
     *
     * @author Yleana Torres
     * @param p_nombre nombre de la biblioteca
     * @param p_socios socios iniciales
     * @param p_libros libros iniciales
     * @throws IllegalArgumentException si p_nombre es null o está vacío
     */
    public Biblioteca(String p_nombre, ArrayList<Socio> p_socios, ArrayList<Libro> p_libros) {
        this.setNombre(p_nombre);
        this.setSocios(p_socios);
        this.setLibros(p_libros);
    }

    /**
     * Construye una biblioteca con un nombre y un libro.
     * Inicializa una colección vacía de socios y agrega el libro recibido.
     *
     * @author Azula Iara
     * @param p_nombre Nombre de la biblioteca.
     * @param p_libro Libro que se incorpora inicialmente.
     */
    public Biblioteca(String p_nombre, Libro p_libro) {
        this.setNombre(p_nombre);
        this.setSocios(new ArrayList<Socio>());
        this.setLibros(new ArrayList<Libro>());
        this.agregarLibro(p_libro);
    }

    /**
     * Construye una biblioteca con un nombre y una colección de libros.
     * Inicializa una colección vacía de socios.
     *
     *  @author Azula Iara
     * @param p_nombre Nombre de la biblioteca.
     * @param p_libros Colección de libros de la biblioteca.
     */
    public Biblioteca(String p_nombre, ArrayList<Libro> p_libros) {
        this.setNombre(p_nombre);
        this.setSocios(new ArrayList<Socio>());
        this.setLibros(p_libros);
    }

    /**
     * Construye una biblioteca con un nombre, un socio y un libro.
     * Inicializa las colecciones y agrega el socio y el libro recibidos.
     *
     * @author Azula Iara
     * @param p_nombre Nombre de la biblioteca.
     * @param p_socio Socio que se incorpora inicialmente.
     * @param p_libro Libro que se incorpora inicialmente.
     */
    public Biblioteca(String p_nombre, Socio p_socio, Libro p_libro) {
        this.setNombre(p_nombre);
        this.setSocios(new ArrayList<Socio>());
        this.setLibros(new ArrayList<Libro>());
        this.agregarSocio(p_socio);
        this.agregarLibro(p_libro);
    }

    /**
     * Construye una biblioteca con un nombre y un socio.
     * Inicializa una coleccion vacia de Libros y agrega el socio recibido.
     *
     * @author Josefina Vallejos Romero
     * @param p_nombre Nombre de la biblioteca.
     * @param p_socio Socio que se incorpora inicialmente.
     */
    public Biblioteca(String p_nombre,Socio p_socio){
        this.setNombre(p_nombre);
        this.setLibros(new ArrayList<Libro>());
        this.setSocios(new ArrayList<Socio>());
        this.agregarSocio(p_socio);
    }

    /**
     * Construye una biblioteca con un nombre y una colección de socios.
     * Inicializa la colección de libros vacía.
     *
     * @author Josefina Vallejos Romero
     * @param p_socios Colección de socios que se incorpora inicialmente.
     * @param p_nombre Nombre de la biblioteca.
     */
    public Biblioteca (ArrayList<Socio> p_socios,String p_nombre){
        this.setNombre(p_nombre);
        this.setSocios(p_socios);
        this.setLibros(new ArrayList<Libro>());
    }

    /**
     * Construye una biblioteca con un nombre, una colección de socios y un libro.
     * Inicializa las colecciones y agrega el libro recibido.
     *
     * @author Josefina Vallejos Romero
     * @param p_nombre Nombre de la biblioteca.
     * @param p_socios Colección de socios que se incorpora inicialmente.
     * @param p_libro Libro que se incorpora inicialmente.
     */
    public Biblioteca (String p_nombre,ArrayList<Socio>p_socios,Libro p_libro){
        this.setNombre(p_nombre);
        this.setSocios(p_socios);
        this.setLibros(new ArrayList<Libro>());
        this.agregarLibro(p_libro);
    }

    /**
     * Establece el nombre de la biblioteca. El nombre no puede ser null ni
     * estar vacío; se eliminan los espacios sobrantes al inicio y al final.
     *
     * @author Yleana Torres
     * @param p_nombre nombre de la biblioteca
     * @throws IllegalArgumentException si p_nombre es null o está vacío
     */
    private void setNombre(String p_nombre) {
        if (p_nombre == null || p_nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("La biblioteca debe tener un nombre");
        }
        this.nombre = p_nombre.trim();
    }

    /**
     * Asigna la colección de libros de la biblioteca.
     * Si recibe null, inicializa una colección vacía.
     *
     * @author Azula Iara
     * @param p_libros Colección de libros que se asignará.
     */
    private void setLibros(ArrayList<Libro> p_libros) {
        if (p_libros == null) {
            this.libros = new ArrayList<Libro>();
        } else {
            this.libros = p_libros;
        }
    }

    /**
     * Asigna una colección de socios a la biblioteca.
     *Si recibe null, inicializa una colección vacía.
     *
     * @author Josefina Vallejos Romero
     * @param p_socios Colección de socios a asignar.
     */
    private void setSocios(ArrayList<Socio> p_socios){
        if(p_socios != null){
            this.socios=p_socios;
        }else{
            this.socios=new ArrayList<Socio>();
        }
    }

    /**
     * Devuelve el nombre de la biblioteca.
     *
     * @author Yleana Torres
     * @return nombre de la biblioteca
     */
    public String getNombre() {
        return this.nombre;
    }

    /**
     * Obtiene la colección de libros de la biblioteca.
     *
     * @author Azula Iara
     * @return Colección de libros.
     */
    public ArrayList<Libro> getLibros() {
        return this.libros;
    }

    /**
     * Devuelve la colección de socios de la biblioteca.
     *
     * @author Josefina Vallejos Romero
     * @return Colección de socios registrados en la biblioteca.
     */
    public ArrayList<Socio> getSocios(){
        return this.socios;
    }

    /**
     * Agrega un libro a la colección de libros de la biblioteca.
     *
     * @author Azula Iara
     * @param p_libro Libro que se desea agregar.
     * @return true si el libro se agregó correctamente.
     */
    public boolean agregarLibro(Libro p_libro) {
        return this.getLibros().add(p_libro);
    }

    /**
     * Quita un libro de la colección de libros de la biblioteca.
     *
     * @author Azula Iara
     * @param p_libro Libro que se desea quitar.
     * @return true si el libro se eliminó correctamente; false si la
     * colección está vacía o el libro no se encuentra en ella.
     */
    public boolean quitarLibro(Libro p_libro) {
        if(this.getLibros().size() > 0){
            return this.getLibros().remove(p_libro);
        }
        return false;
    }

    /**
     * Agrega un socio a la colección de socios de la biblioteca.
     *
     * @author Josefina Vallejos Romero
     * @param p_socio Socio que se desea incorporar.
     * @return true si el socio se agregó correctamente.
     */
    public boolean agregarSocio(Socio p_socio){
        return this.getSocios().add(p_socio);
    }

    /**
     * Elimina un socio de la colección de socios de la biblioteca.
     *
     * @author Josefina Vallejos Romero
     * @param p_socio Socio que se desea eliminar.
     * @return true si el socio se eliminó correctamente.
     * false en caso contrario.
     */
    public boolean quitarSocio(Socio p_socio){
        return this.getSocios().remove(p_socio);
    }

    /**
     * Crea y agrega un nuevo libro.
     *
     * @author Azula Iara
     * @param p_titulo    Título del libro.
     * @param p_edicion   Número de edición.
     * @param p_editorial Editorial del libro.
     * @param p_anio      Año de publicación.
     */
    public void nuevoLibro(String p_titulo, int p_edicion, String p_editorial, int p_anio){
        this.agregarLibro(new Libro(p_titulo, p_edicion, p_editorial, p_anio));
    }

    /**
     * Crea un nuevo socio estudiante y lo incorpora a la biblioteca.
     *
     * @author Josefina Vallejos Romero
     * @param p_dniSocio DNI del estudiante.
     * @param p_nombre Nombre del estudiante.
     * @param p_carrera Carrera del estudiante.
     */
    public void nuevoSocioEstudiante(int p_dniSocio,String p_nombre,String p_carrera){
        this.agregarSocio(new Estudiante(p_dniSocio,p_nombre,p_carrera));
    }

    /**
     * Crea un nuevo socio docente y lo incorpora a la biblioteca.
     *
     * @author Josefina Vallejos Romero
     * @param p_dniSocio DNI del docente.
     * @param p_nombre Nombre del docente.
     * @param p_area Área de desempeño del docente.
     */
    public void nuevoSocioDocente(int p_dniSocio, String p_nombre, String p_area){
        this.agregarSocio(new Docente(p_dniSocio,p_nombre,p_area));
    }

    /**
     * Registra el préstamo de un libro a un socio.
     *
     * @author Azula Iara
     * @param p_fechaRetiro Fecha de retiro del libro.
     * @param p_socio Socio que realiza el préstamo.
     * @param p_libro Libro a prestar.
     * @return true si el préstamo se realizó correctamente.
     */
    public boolean prestarLibro(Calendar p_fechaRetiro, Socio p_socio, Libro p_libro){
        if(p_socio.puedePedir() && !p_libro.prestado()){

            Prestamo nuevoPrestamo = new Prestamo(p_fechaRetiro, p_socio, p_libro);

            p_libro.agregarPrestamo(nuevoPrestamo);
            p_socio.agregarPrestamo(nuevoPrestamo);
            return true;
        }
        return false;
    }

    /**
     * Registra la devolución de un libro prestado.
     *
     * @author Azula Iara
     * @param p_libro Libro a devolver.
     * @throws LibroNoPrestadoException si el libro no estaba prestado.
     */
    public void devolverLibro(Libro p_libro) throws LibroNoPrestadoException {
        if (p_libro.prestado()) {
            Calendar fechaDevolucion = Calendar.getInstance();

            p_libro.ultimoPrestamo().registrarFechaDevolucion(fechaDevolucion);

        } else {
            throw new LibroNoPrestadoException(
                    "El libro " + p_libro.getTitulo() +
                            " no se puede devolver ya que se encuentra en la biblioteca"
            );
        }
    }

    /**
     * Devuelve la cantidad de socios del tipo indicado.
     *
     * @author Yleana Torres
     * @param p_objeto tipo de socio ("Docente" o "Estudiante")
     * @return cantidad de socios de ese tipo
     */
    public int cantidadDeSociosPorTipo(String p_objeto) {
        int cantidad = 0;
        for (Socio socioActual : this.getSocios()) {
            if (socioActual.soyDeLaClase().equals(p_objeto)) {
                cantidad++;
            }
        }
        return cantidad;
    }

    /**
     * Devuelve los préstamos vencidos al día de la fecha.
     *
     * @author Yleana Torres
     * @return préstamos vencidos de los libros que están prestados
     */
    public ArrayList<Prestamo> prestamosVencidos() {
        ArrayList<Prestamo> vencidos = new ArrayList<Prestamo>();
        Calendar hoy = new GregorianCalendar();
        for (Libro libroActual : this.getLibros()) {
            if (libroActual.prestado() && libroActual.ultimoPrestamo().vencido(hoy)) {
                vencidos.add(libroActual.ultimoPrestamo());
            }
        }
        return vencidos;
    }

    /**
     * Devuelve los docentes responsables de la biblioteca.
     *
     * @author Yleana Torres
     * @return docentes que nunca tuvieron ni tienen un préstamo vencido
     */
    public ArrayList<Docente> docentesResponsables() {
        ArrayList<Docente> responsables = new ArrayList<Docente>();
        for (Socio socioActual : this.getSocios()) {
            // Se prefirió soyDeLaClase() antes que instanceof para que sea más manejable dentro de nuestro propio código
            if (socioActual.soyDeLaClase().equals("Docente")) {
                // Downcasting: se pasa de Socio (superclase) a Docente (subclase). Es seguro porque
                // soyDeLaClase() ya confirmó que el socio es un Docente, y es necesario para usar esResponsable()
                Docente docenteActual = (Docente) socioActual;
                if (docenteActual.esResponsable()) {
                    responsables.add(docenteActual);
                }
            }
        }
        return responsables;
    }

    /**
     * Devuelve el nombre del socio que tiene prestado un libro.
     *
     * @author Azula Iara
     * @param p_libro Libro a consultar.
     * @return Nombre del socio que tiene el libro.
     * @throws LibroNoPrestadoException Si el libro no está prestado.
     */
    public String quienTieneElLibro(Libro p_libro) throws LibroNoPrestadoException {
        if (!p_libro.prestado()) {
            throw new LibroNoPrestadoException("El libro se encuentra en la biblioteca");
        }

        return p_libro.ultimoPrestamo().getSocio().getNombre();
    }

    /**
     * Genera un listado con los socios registrados en la biblioteca,
     * indicando la cantidad de estudiantes y docentes.
     *
     * @author Josefina Vallejos Romero
     * @return Cadena con la información de los socios de la biblioteca y
     * la cantidad de socios de cada tipo.
     */
    public String listaDeSocios(){
        int totalE=this.cantidadDeSociosPorTipo("Estudiante");
        int totalD=this.cantidadDeSociosPorTipo("Docente");

        int item=1; //item de los socios,empieza en 1
        String lista = "Lista de Socios:\n\n";


        for(Socio soc :this.getSocios()){
            lista += item +") " +soc.toString()+" || Libros Prestados: " +soc.cantLibrosPrestados() +"\n";
          item++;
        }

        lista +="*".repeat(40);
        lista +="\nCantidad de Socios del tipo Estudiante: " +totalE;
        lista+="\nCantidad de Socios del tipo Docente: " +totalD +"\n";
        lista +="*".repeat(40) + "\n";

        return lista;
    }

    /**
     * Busca un socio en la biblioteca a partir de su DNI.
     *
     * @author Josefina Vallejos Romero
     * @param p_dni DNI del socio buscado.
     * @return El socio encontrado o null si no existe un socio con el DNI indicado.
     */
    public Socio buscarSocio(int p_dni){
        for(Socio soc :this.getSocios()){
            if(soc.getDniSocio()==p_dni){
                return soc;
            }
        }
        return null;
    }

    /**
     * Devuelve el listado de docentes responsables (formato 3).
     *
     * @author Yleana Torres
     * @return listado de docentes responsables
     */
    public String listaDeDocentesResponsables() {
        String lista = "Lista de Docentes Responsables:\n\n";
        for (Docente docenteActual : this.docentesResponsables()) {
            lista += "* " + docenteActual.toString() + " || Libros Prestados: " + docenteActual.cantLibrosPrestados() + "\n";
        }
        return lista;
    }

    /**
     * Devuelve el listado de títulos de la biblioteca, sin repetir.
     *
     * @author Yleana Torres
     * @return listado de títulos
     */
    public String listaDeTitulos() {
        ArrayList<String> titulos = new ArrayList<String>();
        for (Libro libroActual : this.getLibros()) {
            if (!titulos.contains(libroActual.getTitulo())) {
                titulos.add(libroActual.getTitulo());
            }
        }
        String lista = "Lista de Titulos:\n\n";
        int numero = 1;
        for (String tituloActual : titulos) {
            lista += numero + ") " + tituloActual + "\n";
            numero++;
        }
        return lista;
    }

    /**
     * Devuelve el listado de libros (formato 2).
     *
     * @author Yleana Torres
     * @return listado de libros indicando si están prestados
     */
    public String listaDeLibros() {
        String lista = "Lista de Libros:\n\n";
        int numero = 1;
        for (Libro libroActual : this.getLibros()) {
            String prestado = "No";
            if (libroActual.prestado()) {
                prestado = "Si";
            }
            lista += numero + ") " + libroActual.toString() + " || Prestado: (" + prestado + ")\n";
            numero++;
        }
        return lista;
    }
}