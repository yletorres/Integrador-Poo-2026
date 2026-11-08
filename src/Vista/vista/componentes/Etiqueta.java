package vista.componentes;

import vista.Tema;

import javax.swing.*;
import java.awt.*;

/**
 * Etiqueta de estado con forma de píldora para mostrar si un libro está
 * prestado o disponible.
 *
 * @author Yleana Torres
 * @version TP7 - 2026
 */
public class Etiqueta extends JLabel {

    /**
     * Color de fondo de la píldora.
     */
    private Color fondo = Tema.TERRACOTA;

    /**
     * Crea la etiqueta con su fuente y su margen interno.
     */
    public Etiqueta() {
        setOpaque(false);
        setFont(Tema.sans(Font.BOLD, 13));
        setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));
    }

    /**
     * Muestra la etiqueta con el texto y los colores indicados.
     *
     * @param texto texto que muestra la etiqueta
     * @param fondo color de fondo de la píldora
     * @param letra color del texto
     */
    public void mostrar(String texto, Color fondo, Color letra) {
        this.fondo = fondo;
        setText(texto);
        setForeground(letra);
        setVisible(true);
        repaint();
    }

    /**
     * Dibuja el fondo en forma de píldora y luego el texto.
     *
     * @param g contexto gráfico donde se dibuja
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = Tema.suavizar(g);
        g2.setColor(fondo);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
        g2.dispose();
        super.paintComponent(g);
    }
}