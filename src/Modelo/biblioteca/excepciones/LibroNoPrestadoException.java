package biblioteca.excepciones;

/**
 * Excepción personalizada que se lanza cuando se intenta devolver
 * o consultar un libro que no está actualmente prestado.
 *
 * Esta excepción se utiliza para manejar de forma controlada los casos en los que
 * se realiza una operación inválida sobre un libro disponible en la biblioteca.
 *
 * Por ejemplo, se lanza cuando se intenta devolver un libro que no ha sido prestado
 * o consultar qué socio lo tiene cuando en realidad está en la biblioteca.
 *
 * @author Azula Iara Elizabeth
 */
public class LibroNoPrestadoException extends Exception {

    /**
     * Crea una nueva excepción con el mensaje de error especificado.
     *
     * @param p_mensaje Mensaje descriptivo del error.
     */
    public LibroNoPrestadoException(String p_mensaje) {
        super(p_mensaje);
    }
}