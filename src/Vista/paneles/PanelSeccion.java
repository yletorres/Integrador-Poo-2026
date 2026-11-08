package vista.paneles;

import vista.Tema;

import javax.swing.*;
import java.awt.*;

// base que comparten las pantallas del menu con el mismo fondo, margenes y encabezado
abstract class PanelSeccion extends JPanel {

    PanelSeccion() {
        setBackground(Tema.FONDO);
        setLayout(new BorderLayout(0, 24));
        setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 34));
    }

    // se ejecuta cuando se muestra la pantalla para actualizar los datos desde el controlador
    abstract void refrescar();

    // arma el encabezado con el titulo y el texto que aparece debajo
    static JComponent encabezado(JLabel titulo, JLabel bajada) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        titulo.setFont(Tema.sans(Font.BOLD, 30));
        titulo.setForeground(Tema.TEXTO);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        bajada.setFont(Tema.sans(Font.PLAIN, 14));
        bajada.setForeground(Tema.TEXTO_SUAVE);
        bajada.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(titulo);
        p.add(Box.createVerticalStrut(2));
        p.add(bajada);
        return p;
    }

    // permite crear el encabezado directamente a partir de dos textos
    static JComponent encabezado(String titulo, String bajada) {
        return encabezado(new JLabel(titulo), new JLabel(bajada));
    }

    // crea el titulo que se usa dentro de cada bloque
    static JLabel subtitulo(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(Tema.sans(Font.BOLD, 16));
        l.setForeground(Tema.TEXTO);
        return l;
    }
}
