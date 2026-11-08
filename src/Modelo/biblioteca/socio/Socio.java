package biblioteca.socio;

import biblioteca.prestamo.Prestamo;


/**
 * Socio de una biblioteca
 * 
 * @author (Palacios, Evangelina) 
 * @version (1.0)
 */

 

import java.util.ArrayList;
import java.util.Calendar;

public abstract class Socio {
    private int dniSocio;
    private String nombre;
    private int diasPrestamo;
    private ArrayList<Prestamo> prestamos;

    /**
     * Crea un socio sin prestamos
     *
     * @param p_dniSocio     DNI del socio
     * @param p_nombre       nombre y apellido del socio
     * @param p_diasPrestamo días que puede conservar un libro antes de vencer
     */
    public Socio(int p_dniSocio, String p_nombre, int p_diasPrestamo) {
        this.setDniSocio(p_dniSocio);
        this.setNombre(p_nombre);
        this.setDiasPrestamo(p_diasPrestamo);
        this.setPrestamos(new ArrayList<Prestamo>());
    }

    private void setDniSocio(int p_dniSocio) {
        this.dniSocio = p_dniSocio;
    }

    private void setNombre(String p_nombre) {
        this.nombre = p_nombre;
    }
    
    /**
     * Protect para ser utilizado en esta y sus clases hijas pero no externas
     */
    protected void setDiasPrestamo(int p_diasPrestamo) {
        this.diasPrestamo = p_diasPrestamo;
    }

    private void setPrestamos(ArrayList<Prestamo> p_prestamos) {
        this.prestamos = p_prestamos;
    }

    public int getDniSocio() {
        return this.dniSocio;
    }

    public String getNombre() {
        return this.nombre;
    }

    public int getDiasPrestamo() {
        return this.diasPrestamo;
    }

    public ArrayList<Prestamo> getPrestamos() {
        return this.prestamos;
    }

    /**
     * Agrega un prestamo a la ficha del socio
     *
     * @param p_prestamo prestamo a agregar
     */
    public void agregarPrestamo(Prestamo p_prestamo) {
        this.getPrestamos().add(p_prestamo);
    }

    /**
     * Elimina un prestamo de la ficha del socio
     *
     * @param p_prestamo prestamo a eliminar
     */
    public void quitarPrestamo(Prestamo p_prestamo) {
        this.getPrestamos().remove(p_prestamo);
    }

    /**
     * Cuenta los libros que el socio tiene,los prestamos sin fecha de devolucion
     *
     * @return cantidad de libros prestados
     */
    public int cantLibrosPrestados() {
        int cantidad = 0;

        for (Prestamo prestamo : this.getPrestamos()) {
            if (prestamo.getFechaDevolucion() == null) {
                cantidad++;
            }
        }

        return cantidad;
    }

    /**
     * Indica si el socio puede pedir un libro, no debe tener
     * ningun prestamo vencido sin devolver a la fecha actual
     *
     * @return true si no tiene prestamos vencidos
     */
    public boolean puedePedir() {
        Calendar hoy = Calendar.getInstance();

        for (Prestamo prestamo : this.getPrestamos()) {
            if (prestamo.getFechaDevolucion() == null && prestamo.vencido(hoy)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Indica el tipo de socio
     *
     * @return "Docente" o "Estudiante"
     */
    public abstract String soyDeLaClase();

    /**
     * Describe al socio con DNI, nombre y tipo
     *
     * @return texto con el formato pedido en la consigna
     */
    public String toString() {
        return "D.N.I.: " + this.getDniSocio() + " || " + this.getNombre()
                + " (" + this.soyDeLaClase() + ");
    }
}
