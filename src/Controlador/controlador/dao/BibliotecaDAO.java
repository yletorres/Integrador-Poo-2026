package controlador.dao;

import biblioteca.Biblioteca;
import java.util.List;

public interface BibliotecaDAO {

        List<String> obtenerNombres();

        Biblioteca buscarPorNombre(String nombre);

        int obtenerIdPorNombre(String nombre);

        void insertar(Biblioteca biblioteca);
}
