package biblioteca;

import controlador.BibliotecaController;
import controlador.BibliotecaControllerImpl;
import vista.paneles.VentanaPrincipal;

import javax.swing.SwingUtilities;

public class GestionBiblioteca {

    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        SwingUtilities.invokeLater(() -> {
            BibliotecaController controlador = new BibliotecaControllerImpl();

            controlador.agregarLibro("JAVA. Como Programar", 10, "Pearson", 2018);
            controlador.agregarLibro("Programando con JAVA", 3, "Anaya", 2021);
            controlador.agregarSocioDocente(14524782, "Juan Perez", "Exactas");
            controlador.agregarSocioEstudiante(28987498, "Francisco Paenza", "Licenciatura en Sistemas");

            VentanaPrincipal ventana = new VentanaPrincipal(controlador);
            ventana.setVisible(true);
        });
    }
}