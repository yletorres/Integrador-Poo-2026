package vista.componentes;

import vista.Tema;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Fondo oscuro con un brillo terracota en la parte inferior.
 *
 * @author Yleana Torres
 * @version TP7 - 2026
 */
public class FondoOscuro extends JPanel {

    /**
     * Radio de redondeo de las esquinas del fondo.
     */
    private final int radio;

    /**
     * Crea el fondo oscuro con el redondeo indicado.
     *
     * @param radio radio de redondeo de las esquinas
     */
    public FondoOscuro(int radio) {
        this.radio = radio;
        setOpaque(false);
    }

    /**
     * Dibuja la base oscura con esquinas redondeadas y el brillo terracota
     * en la parte baja.
     *
     * @param g contexto gráfico donde se dibuja
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = Tema.suavizar(g);
        int w = getWidth(), h = getHeight();
        Shape forma = new RoundRectangle2D.Float(0, 0, w, h, radio, radio);

        // pinta la base oscura y aplica el brillo en la parte baja
        g2.setColor(Tema.CAFE_OSCURO);
        g2.fill(forma);
        g2.setClip(forma);
        g2.setPaint(new GradientPaint(0, h * 0.58f, Tema.conAlfa(Tema.TERRACOTA, 0),
                0, h, Tema.conAlfa(Tema.TERRACOTA, 140)));
        g2.fillRect(0, 0, w, h);
        g2.dispose();
    }
}