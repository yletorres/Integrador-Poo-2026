package Modelo.biblioteca.socio;

import Modelo.biblioteca.prestamo.Prestamo;

import java.util.Calendar;

/**
 * Representa a un estudiante que es socio de la institución.
 * 
 * @author Juan Martín Rodas
 * @version 1.0
 */
public class Estudiante extends Socio {
    private String carrera;
    
    /**
     * Constructor para objetos de la clase estudiante.
     * 
     * @param p_dniSocio DNI del socio.
     * @param p_nombre Nombre del socio.
     * @param p_carrera Carrera que cursa el estudiante.
     */
    public Estudiante(int p_dniSocio, String p_nombre, String p_carrera) {
        super(p_dniSocio, p_nombre, 20);
        this.setCarrera(p_carrera);
    }
    
    /**
     * Retorna la carrera del estudiante.
     * 
     * @return String con la carrera.
     */
    public String getCarrera() {
        return this.carrera;
    }
    
    /**
     * Establece la carrera del estudiante.
     * 
     * @param p_carrera Nuevo valor para la carrera.
     */
    private void setCarrera(String p_carrera) {
        this.carrera = p_carrera;
    }
    
    /**
     * Indica si el estudiante puede realizar nuevos préstamos 
     * (si no tiene préstamos vencidos y tiene menos de 3 libros prestados).
     * 
     * @return boolean true si puede pedir, false en caso contrario.
     */
    public boolean puedePedir() {
        boolean sinVencidos = true;
    
        for (Prestamo p : this.getPrestamos()) {
            if (p.getFechaDevolucion() == null && p.vencido(Calendar.getInstance())) {
                sinVencidos = false;
                break;
            }
        }
    
        return sinVencidos && (this.cantLibrosPrestados() < 3);
    }
    
    /**
     * Retorna la clase a la que pertenece la instancia.
     * 
     * @return String con el nombre de la clase.
     */
    public String soyDeLaClase() {
        return "Estudiante";
    }
}
