package vista.paneles;

import vista.Tema;

import javax.swing.*;
import java.awt.*;

/**
 * Base que comparten las pantallas del menú con el mismo fondo, márgenes y
 * encabezado.
 *
 * @author Yleana Torres
 * @version TP7 - 2026
 */
abstract class PanelSeccion extends JPanel {

    /**
     * Crea la sección con el fondo, el diseño y los márgenes comunes a todas
     * las pantallas.
     */
    PanelSeccion() {
        setBackground(Tema.FONDO);
        setLayout(new BorderLayout(0, 24));
        setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 34));
    }

    /**
     * Actualiza los datos de la pantalla desde el controlador. Se ejecuta
     * cuando se muestra la pantalla.
     */
    abstract void refrescar();

    /**
     * Arma el encabezado con el título y el texto que aparece debajo.
     *
     * @param titulo etiqueta con el título de la pantalla
     * @param bajada etiqueta con el texto descriptivo
     * @return el componente con el encabezado armado
     */
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

    /**
     * Crea el encabezado directamente a partir de dos textos.
     *
     * @param titulo texto del título de la pantalla
     * @param bajada texto descriptivo que aparece debajo del título
     * @return el componente con el encabezado armado
     */
    static JComponent encabezado(String titulo, String bajada) {
        return encabezado(new JLabel(titulo), new JLabel(bajada));
    }

    /**
     * Crea el título que se usa dentro de cada bloque.
     *
     * @param texto texto del subtítulo
     * @return la etiqueta con el subtítulo
     */
    static JLabel subtitulo(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(Tema.sans(Font.BOLD, 16));
        l.setForeground(Tema.TEXTO);
        return l;
    }
}