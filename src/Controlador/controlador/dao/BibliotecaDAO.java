package controlador.dao;

import biblioteca.Biblioteca;
import java.util.List;

/**
 * Define las operaciones de acceso a datos para la gestión de bibliotecas.
 *
 * @author Yleana Torres
 * @version TP7 - 2026
 */
public interface BibliotecaDAO {

        /**
         * Devuelve los nombres de todas las bibliotecas registradas.
         *
         * @return lista con los nombres de las bibliotecas
         */
        List<String> obtenerNombres();

        /**
         * Busca una biblioteca a partir de su nombre.
         *
         * @param p_nombre nombre de la biblioteca que se desea buscar
         * @return biblioteca encontrada, o null si no existe
         */
        Biblioteca buscarPorNombre(String p_nombre);

        /**
         * Devuelve el identificador de una biblioteca a partir de su nombre.
         *
         * @param p_nombre nombre de la biblioteca
         * @return identificador de la biblioteca
         */
        int obtenerIdPorNombre(String p_nombre);

        /**
         * Registra una nueva biblioteca.
         *
         * @param p_biblioteca biblioteca que se desea insertar
         */
        void insertar(Biblioteca p_biblioteca);

        /**
         * Elimina una biblioteca con todos sus libros, socios y préstamos.
         *
         * @param p_idBiblioteca id de la biblioteca a eliminar
         * @return true si se eliminó la biblioteca
         */
        boolean eliminar(int p_idBiblioteca);
}