package controlador.dao;

import biblioteca.socio.Socio;
import java.util.List;

/**
 * Contrato de acceso a los datos de los socios (docentes y estudiantes):
 * cómo se leen, se guardan y se eliminan en la base de datos.
 *
 * @author Yleana Torres
 * @version TP7 - 2026
 */
public interface SocioDAO {

    /**
     * Devuelve todos los socios de una biblioteca.
     *
     * @param p_idBiblioteca id de la biblioteca en la base de datos
     * @return lista de socios (docentes y estudiantes), vacía si no hay ninguno
     */
    List<Socio> obtenerPorBiblioteca(int p_idBiblioteca);

    /**
     * Guarda un socio nuevo en la base de datos.
     *
     * @param p_socio        socio que se guarda (docente o estudiante)
     * @param p_idBiblioteca id de la biblioteca a la que pertenece
     */
    void insertar(Socio p_socio, int p_idBiblioteca);

    /**
     * Elimina un socio de la base de datos.
     *
     * @param p_dniSocio DNI del socio que se elimina
     * @param p_tipo     tipo de socio: "Docente" o "Estudiante"
     * @return true si se eliminó el socio; false en caso contrario
     */
    boolean eliminar(int p_dniSocio, String p_tipo);
}