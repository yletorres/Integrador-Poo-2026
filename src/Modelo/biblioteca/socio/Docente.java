package biblioteca.socio;

import biblioteca.prestamo.Prestamo;

import java.util.Calendar;

/**
 * Representa un docente que es socio de la institución.
 * 
 * @author Juan Martín Rodas
 * @version 1.0
 */
public class Docente extends Socio {
    private String area;
    
    /**
     * Constructor para objetos de la clase docente.
     * 
     * @_dniSocio DNI del socio.
     * @param p_nombre Nombre del socio.
     * @param p_area Área a la que pertenece el docente.
     */
    public Docente(int p_dniSocio, String p_nombre, String p_area) {
        super(p_dniSocio, p_nombre, 5);
        this.setArea(p_area);
    }
    
    /**
     * Retorna el área del docente.
     * 
     * @return String con el área.
     */
    public String getArea() {
        return this.area;
    }
    
    /**
     * Establece el área del docente.
     * 
     * @param p_area Nuevo valor para el área.
     */
    private void setArea(String p_area) {
        this.area = p_area;
    }
    
    /**
     * Verifica si el docente es responsable (no tiene préstamos vencidos).
     * 
     * @return boolean true si es responsable, false en caso contrario.
     */
    public boolean esResponsable() {
        boolean responsable = true;
        for (Prestamo p : this.getPrestamos()) {
            if (p.getFechaDevolucion() != null) {
                if (p.vencido(p.getFechaDevolucion())) {
                responsable = false;
                break;
                }
            } else {
                if (p.vencido(Calendar.getInstance())) {
                    responsable = false;
                    break;
                }
            }
        }
        return responsable;
    }
    
    /**
     * Retorna la clase a la que pertenece la instancia.
     * 
     * @return String con el nombre de la clase.
     */
    public String soyDeLaClase() {
        return "Docente";
    }
    
    /**
     * Modifica los días de préstamo sumando el valor recibido.
     * 
     * @param p_dias Días a sumar al préstamo.
     */
    public void cambiarDiasDePrestamo(int p_dias) {
        this.setDiasPrestamo(this.getDiasPrestamo() + p_dias);
    }    

    /**
     * Indica si el docente puede realizar nuevos préstamos.
     * 
     * @return boolean true si puede pedir, false si tiene préstamos vencidos sin devolver.
     */
    public boolean puedePedir() {
        boolean puede = true;
        for (Prestamo p : this.getPrestamos()) {
            if (p.getFechaDevolucion() == null && p.vencido(Calendar.getInstance())) {
                puede = false;
                break;
            }
        }
        return puede;
    }
}
