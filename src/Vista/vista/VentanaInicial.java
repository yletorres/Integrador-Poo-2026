package vista;

import vista.componentes.Avatar;
import vista.componentes.Boton;
import vista.componentes.FondoOscuro;

import javax.swing.*;
import java.awt.*;

// pantalla inicial con las opciones para entrar al sistema, ejecutar la demostracion o salir
// recibe las acciones desde afuera y se encarga solamente de mostrarlas
public class VentanaInicial extends JFrame {

    public VentanaInicial(Runnable iniciarSistema, Runnable ejecutarDemostracion) {
        super("Gestión de Biblioteca");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(520, 640);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel fondo = new FondoOscuro(0);
        fondo.setLayout(new BoxLayout(fondo, BoxLayout.Y_AXIS));
        fondo.setBorder(BorderFactory.createEmptyBorder(56, 64, 56, 64));

        Avatar avatar = new Avatar(124);
        avatar.setAlignmentX(Component.CENTER_ALIGNMENT);
        fondo.add(avatar);
        fondo.add(Box.createVerticalStrut(24));
        fondo.add(centrado("Gestión de Biblioteca", Tema.sans(Font.BOLD, 28), Color.WHITE));
        fondo.add(Box.createVerticalStrut(6));
        fondo.add(centrado("Elegí cómo querés ejecutar el programa", Tema.sans(Font.PLAIN, 14), Tema.TEXTO_CLARO));
        fondo.add(Box.createVerticalGlue());

        Boton sistema = new Boton("Iniciar sistema de biblioteca");
        sistema.addActionListener(e -> {
            dispose();
            iniciarSistema.run();
        });
        Boton demostracion = new Boton("Ejecutar demostración del práctico", Boton.Estilo.OSCURO);
        demostracion.addActionListener(e -> ejecutarDemostracion.run());
        Boton salir = new Boton("Salir", Boton.Estilo.OSCURO);
        salir.addActionListener(e -> System.exit(0));

        
        // agrega los botones al menu manteniendo el mismo tamaño y separacion
        for (Boton b : new Boton[]{sistema, demostracion, salir}) {
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
            fondo.add(b);
            fondo.add(Box.createVerticalStrut(12));
        }
        setContentPane(fondo);
    }

    // crea las etiquetas de texto centradas para reutilizar el mismo formato
    private static JLabel centrado(String texto, Font fuente, Color color) {
        JLabel l = new JLabel(texto);
        l.setFont(fuente);
        l.setForeground(color);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        return l;
    }
}
