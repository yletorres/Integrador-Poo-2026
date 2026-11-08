package vista;

import controlador.BibliotecaController;
import controlador.BibliotecaControllerImpl;
import vista.paneles.VentanaPrincipal; // O la ventana de inicio correspondiente

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Configuraciones estéticas para suavizado de fuentes en Swing
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        SwingUtilities.invokeLater(() -> {
            // 1. Inicializar el controlador y el modelo de la biblioteca
            BibliotecaController controlador = new BibliotecaControllerImpl();

            // 2. Aplicar el tema visual (si Tema.java lo provee)
            Tema.instalar();

            // 3. Levantar la ventana principal pasando el controlador
            VentanaPrincipal ventana = new VentanaPrincipal(controlador);
            ventana.setVisible(true);
        });
    }
}