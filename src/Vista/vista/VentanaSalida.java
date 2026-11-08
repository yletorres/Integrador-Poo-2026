package vista;

import vista.componentes.Boton;
import vista.componentes.Listado;
import vista.componentes.Tarjeta;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana que muestra los resultados de la demostración del práctico.
 * Permite ver la salida aunque el programa se ejecute sin consola.
 * Cerrarla no cierra el programa.
 *
 * @author Yleana Torres
 * @version TP7 - 2026
 */
public class VentanaSalida extends JFrame {

    /**
     * Crea la ventana con un título y el texto a mostrar.
     *
     * @author Yleana Torres
     * @param p_titulo título de la ventana y encabezado del listado
     * @param p_texto  texto que se muestra en la hoja
     */
    public VentanaSalida(String p_titulo, String p_texto) {
        super(p_titulo);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE); // cerrarla no cierra el programa
        this.setSize(900, 640);
        this.setMinimumSize(new Dimension(640, 420));
        this.setLocationRelativeTo(null);

        JPanel raiz = new JPanel(new BorderLayout(0, 20));
        raiz.setBackground(Tema.FONDO);
        raiz.setBorder(BorderFactory.createEmptyBorder(28, 32, 26, 32));

        JLabel encabezado = new JLabel(p_titulo);
        encabezado.setFont(Tema.sans(Font.BOLD, 26));
        encabezado.setForeground(Tema.TEXTO);
        raiz.add(encabezado, BorderLayout.NORTH);

        Listado listado = new Listado();
        listado.setTexto(p_texto);
        Tarjeta hoja = new Tarjeta(Color.WHITE, 28);
        hoja.setBorder(BorderFactory.createEmptyBorder(22, 28, 22, 28));
        hoja.add(listado, BorderLayout.CENTER);
        raiz.add(hoja, BorderLayout.CENTER);

        Boton cerrar = new Boton("Cerrar");
        cerrar.addActionListener(e -> this.dispose());
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        pie.setOpaque(false);
        pie.add(cerrar);
        raiz.add(pie, BorderLayout.SOUTH);

        this.setContentPane(raiz);
    }

    /**
     * Crea y muestra la ventana desde el hilo de Swing.
     *
     * @author Yleana Torres
     * @param p_titulo título de la ventana
     * @param p_texto  texto que se muestra en la hoja
     */
    public static void mostrar(String p_titulo, String p_texto) {
        SwingUtilities.invokeLater(() -> new VentanaSalida(p_titulo, p_texto).setVisible(true));
    }
}