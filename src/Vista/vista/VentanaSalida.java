package vista;

import vista.componentes.Boton;
import vista.componentes.Listado;
import vista.componentes.Tarjeta;

import javax.swing.*;
import java.awt.*;

// ventana que muestra los resultados de la demostracion del practico
// permite ver la salida aunque el programa se ejecute sin consola
public class VentanaSalida extends JFrame {

    public VentanaSalida(String titulo, String texto) {
        super(titulo);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // cerrarla no cierra el programa
        setSize(900, 640);
        setMinimumSize(new Dimension(640, 420));
        setLocationRelativeTo(null);

        JPanel raiz = new JPanel(new BorderLayout(0, 20));
        raiz.setBackground(Tema.FONDO);
        raiz.setBorder(BorderFactory.createEmptyBorder(28, 32, 26, 32));

        JLabel encabezado = new JLabel(titulo);
        encabezado.setFont(Tema.sans(Font.BOLD, 26));
        encabezado.setForeground(Tema.TEXTO);
        raiz.add(encabezado, BorderLayout.NORTH);

        Listado listado = new Listado();
        listado.setTexto(texto);
        Tarjeta hoja = new Tarjeta(Color.WHITE, 28);
        hoja.setBorder(BorderFactory.createEmptyBorder(22, 28, 22, 28));
        hoja.add(listado, BorderLayout.CENTER);
        raiz.add(hoja, BorderLayout.CENTER);

        Boton cerrar = new Boton("Cerrar");
        cerrar.addActionListener(e -> dispose());
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        pie.setOpaque(false);
        pie.add(cerrar);
        raiz.add(pie, BorderLayout.SOUTH);

        setContentPane(raiz);
    }

    // crea y muestra la ventana desde el hilo de swing
    public static void mostrar(String titulo, String texto) {
        SwingUtilities.invokeLater(() -> new VentanaSalida(titulo, texto).setVisible(true));
    }
}
