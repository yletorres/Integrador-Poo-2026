package Modelo.biblioteca.prestamo;

import Modelo.biblioteca.libro.Libro;
import Modelo.biblioteca.socio.Socio;


/**
 * Representa un prestamo de un libro por parte de un socio
 * 
 * @author (Palacios, Evangelina) 
 * @version (1.0)
 */

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Prestamo {
    private Calendar fechaRetiro;
    private Calendar fechaDevolucion;
    private Socio socio;
    private Libro libro;

    public Prestamo(Calendar p_fechaRetiro, Socio p_socio, Libro p_libro) {
        this.setFechaRetiro(p_fechaRetiro);
        this.setFechaDevolucion(null);
        this.setSocio(p_socio);
        this.setLibro(p_libro);
    }

    private void setFechaRetiro(Calendar p_fechaRetiro) {
        this.fechaRetiro = p_fechaRetiro;
    }

    private void setFechaDevolucion(Calendar p_fechaDevolucion) {
        this.fechaDevolucion = p_fechaDevolucion;
    }

    private void setSocio(Socio p_socio) {
        this.socio = p_socio;
    }

    private void setLibro(Libro p_libro) {
        this.libro = p_libro;
    }

    public Calendar getFechaRetiro() {
        return this.fechaRetiro;
    }

    public Calendar getFechaDevolucion() {
        return this.fechaDevolucion;
    }

    public Socio getSocio() {
        return this.socio;
    }

    public Libro getLibro() {
        return this.libro;
    }

    /**
     * Registra la fecha en que se devolvio el libro
     *
     * @param p_fecha fecha de devolución
     */
    public void registrarFechaDevolucion(Calendar p_fecha) {
        this.setFechaDevolucion(p_fecha);
    }

    /**
     * Indica si el prestamo esta vencido, el vencimiento es la
     * fecha de retiro mas los dias de prestamo
     *
     * @param p_fecha fecha conla cual se compara
     * @return true si p_fecha es posterior a la fecha de vencimiento
     */
    public boolean vencido(Calendar p_fecha) {
        Calendar vencimiento = (Calendar) this.getFechaRetiro().clone();
        vencimiento.add(Calendar.DAY_OF_MONTH, this.getSocio().getDiasPrestamo());
        return p_fecha.after(vencimiento);
    }

    /**
     * Describe el prestamo con fechas, título del libro y nombre del socio
     *
     * @return texto con el formato pedido
     */
   public String toString() {
    SimpleDateFormat formato = new SimpleDateFormat("yyyy/MM/dd");

    String devolucion;
      if (this.getFechaDevolucion() == null) {
        devolucion = "Sin devolver";
      } else {
        devolucion = formato.format(this.getFechaDevolucion().getTime());
     }

    String resultado = "Retiro: " + formato.format(this.getFechaRetiro().getTime());
    resultado = resultado + " - Devolucion: " + devolucion;
    resultado = resultado + "\nLibro: " + this.getLibro().getTitulo();
    resultado = resultado + "\nSocio: " + this.getSocio().getNombre();

    return resultado;
}
