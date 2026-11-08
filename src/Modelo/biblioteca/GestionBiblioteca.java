package biblioteca;

//import com.formdev.flatlaf.FlatDarkLaf;
import controlador.BibliotecaController;
import controlador.BibliotecaControllerImpl;
import controlador.dao.BibliotecaDAO;
import controlador.dao.impl.BibliotecaDAOImpl;
import controlador.dao.impl.LibroDAOImpl;
import controlador.dao.impl.PrestamoDAOImpl;
import controlador.dao.impl.SocioDAOImpl;
import vista.Tema;
import vista.VentanaInicial;
import vista.paneles.VentanaPrincipal;

import javax.swing.*;
import java.sql.SQLException;

public class GestionBiblioteca {

    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        SwingUtilities.invokeLater(() -> {
            // FlatDarkLaf.setup();
            Tema.instalar();

            // muestra el menu inicial con las dos opciones disponibles
            new VentanaInicial(
                    GestionBiblioteca::iniciarSistema,
                    GestionBiblioteca::ejecutarDemostracion).setVisible(true);
        });
    }

    // abre la ventana principal de la biblioteca
    private static void iniciarSistema() {
        try {
            BibliotecaController controlador = new BibliotecaControllerImpl();
            new VentanaPrincipal(controlador).setVisible(true);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "No se pudo conectar con la base de datos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ejecuta la demostracion del practico
    private static void ejecutarDemostracion() {
        JOptionPane.showMessageDialog(
                null,
                "La demostración del práctico todavía no está implementada.",
                "Demostración",
                JOptionPane.INFORMATION_MESSAGE);

        // acá se aplican las demostraciones,borrar el handler de error ese, se
        // reemplaza
        // por mensajes a los metodos estaticos que hagan los 5 integrantes

        // Biblioteca biblioteca = new Biblioteca();

        // crear libros
        // crear estudiantes y docentes
        // registrar socios y libros
        // simular prestamos y devoluciones
        // ejecutar los informes solicitados
        // mostrar los resultados
    }
}