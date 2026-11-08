package controlador.dao;

import biblioteca.socio.Socio;
import java.util.List;

public interface SocioDAO {

    List<Socio> obtenerPorBiblioteca(int idBiblioteca);

    void insertar(Socio socio, int idBiblioteca);

    Socio buscarPorDni(int dniSocio);

    boolean eliminar(int dniSocio, String tipo);
}
