import java.util.Calendar;

public class docente extends socio{
    private String area;
    
    public docente(int p_dniSocio, String p_nombre, String p_area) {
        super(p_dniSocio, p_nombre, 5);
        this.setArea(p_area);
    }
    
    public String getArea(){
        return this.area;
    }
    private void setArea(String p_area){
        this.area = p_area;
    }
    
    
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
    
    public String soyDeLaClase(){
        return "Docente";
    }
    
    public void cambiarDiasDePrestamo(int p_dias) {
        this.setDiasPrestamo(this.getDiasPrestamo() + p_dias);
    }   
    

    public boolean puedePedir() {
        boolean puede = true;
        for (Prestamo p : this.getPrestamos()) {
            // Si el préstamo no fue devuelto y está vencido a la fecha de hoy
            if (p.getFechaDevolucion() == null && p.vencido(Calendar.getInstance())) {
                puede = false;
                break;
            }
        }
        return puede;
    }
}