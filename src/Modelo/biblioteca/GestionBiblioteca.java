package biblioteca;

//import com.formdev.flatlaf.FlatDarkLaf;
import controlador.BibliotecaController;
import controlador.BibliotecaControllerImpl;
//import controlador.BibliotecaControllerImpl;
import vista.Tema;
import vista.VentanaInicial;
import vista.paneles.VentanaPrincipal;

import javax.swing.*;

public class GestionBiblioteca {

    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        SwingUtilities.invokeLater(() -> {
            //FlatDarkLaf.setup();
            Tema.instalar();

            // muestra el menu inicial con las dos opciones disponibles
            new VentanaInicial(
                GestionBiblioteca::iniciarSistema,
                GestionBiblioteca::ejecutarDemostracion
            ).setVisible(true);
        });
    }

    // abre la ventana principal de la biblioteca
    private static void iniciarSistema() {
        BibliotecaController controlador = new BibliotecaControllerImpl(); //aplicar cuando lo tengamos

        new VentanaPrincipal(controlador).setVisible(true);
    }

    // ejecuta la demostracion del practico
    private static void ejecutarDemostracion() {
        JOptionPane.showMessageDialog(
            null,
            "La demostración del práctico todavía no está implementada.",
            "Demostración",
            JOptionPane.INFORMATION_MESSAGE
        );

        //acá se aplican las demostraciones,borrar el handler de error ese, se reemplaza
        //por mensajes a los metodos estaticos que hagan los 5 integrantes

        //Biblioteca biblioteca = new Biblioteca();

        // crear libros
        // crear estudiantes y docentes
        // registrar socios y libros
        // simular prestamos y devoluciones
        // ejecutar los informes solicitados
        // mostrar los resultados
    }
}