package Controlador.Biblioteca;

import Controlador.libro.Libro;
import Controlador.prestamo.Prestamo;
import Controlador.socio.Docente;
import Controlador.socio.Socio;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Representa una biblioteca que administra sus socios y sus libros.
 *
 * @author Yleana Torres
 * @author Iara Azula
 * @author Josefina Vallejo
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
     * Devuelve el nombre de la biblioteca.
     *
     * @author Yleana Torres
     * @return nombre de la biblioteca
     */
    public String getNombre() {
        return this.nombre;
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