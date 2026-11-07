package vista;

import controlador.BibliotecaController;
import controlador.ControladorPrueba;
import vista.paneles.VentanaPrincipal;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        SwingUtilities.invokeLater(() -> {
            Tema.instalar();

            // muestra el menu inicial con las dos opciones disponibles
            new VentanaInicial(
                Main::iniciarSistema,
                Main::ejecutarDemostracion
            ).setVisible(true);
        });
    }

    // abre la ventana principal de la biblioteca
    private static void iniciarSistema() {
        BibliotecaController controlador = new ControladorPrueba();

        new VentanaPrincipal(controlador).setVisible(true);
    }


     //Ejecuta la demostración del práctico.

    private static void ejecutarDemostracion() {
        // Pendiente: conectar con la demostración real de GestionBiblioteca.
        JOptionPane.showMessageDialog(
            null,
            "La demostración del práctico todavía no está implementada.",
            "Demostración",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}