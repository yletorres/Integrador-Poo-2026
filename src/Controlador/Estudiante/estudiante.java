import java.util.Calendar;

public class estudiante extends socio{
    private String carrera;
    

    public Estudiante(int p_dniSocio, String p_nombre, String p_carrera) {
        super(p_dniSocio, p_nombre, 20);
        this.setCarrera(p_carrera);
    }
    
    public String getCarrera(){
        return this.carrera;
    }
    private void setCarrera(String p_carrera){
        this.carrera = p_carrera;
    }
    
    
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
    
    public String soyDeLaClase(){
        return "Estudiante";
    }
}