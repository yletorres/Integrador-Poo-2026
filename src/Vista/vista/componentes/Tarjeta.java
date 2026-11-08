package vista.componentes;

import vista.Tema;

import javax.swing.*;
import java.awt.*;

/**
 * Panel con esquinas redondeadas que puede tener un color liso o un
 * degradado.
 *
 * @author Yleana Torres
 * @version TP7 - 2026
 */
public class Tarjeta extends JPanel {

    /**
     * Color liso con el que se pinta la tarjeta.
     */
    private final Color color;

    /**
     * Indica si la tarjeta se pinta con degradado en vez de color liso.
     */
    private final boolean conDegradado;

    /**
     * Radio de la esquina superior izquierda.
     */
    private final double tl;

    /**
     * Radio de la esquina superior derecha.
     */
    private final double tr;

    /**
     * Radio de la esquina inferior derecha.
     */
    private final double br;

    /**
     * Radio de la esquina inferior izquierda.
     */
    private final double bl;

    /**
     * Crea una tarjeta de color liso con el mismo redondeo en todas las
     * esquinas.
     *
     * @param color color con el que se pinta la tarjeta
     * @param radio radio de redondeo de las cuatro esquinas
     */
    public Tarjeta(Color color, int radio) {
        this(color, false, radio, radio, radio, radio);
    }

    /**
     * Crea una tarjeta indicando si lleva degradado y el redondeo de cada
     * esquina.
     *
     * @param color        color liso de la tarjeta
     * @param conDegradado true si se pinta con degradado
     * @param tl           radio de la esquina superior izquierda
     * @param tr           radio de la esquina superior derecha
     * @param br           radio de la esquina inferior derecha
     * @param bl           radio de la esquina inferior izquierda
     */
    private Tarjeta(Color color, boolean conDegradado, int tl, int tr, int br, int bl) {
        this.color = color;
        this.conDegradado = conDegradado;
        this.tl = tl; this.tr = tr; this.br = br; this.bl = bl;
        setOpaque(false);
        setLayout(new BorderLayout());
    }

    /**
     * Crea una tarjeta con degradado y permite elegir el redondeo de cada
     * esquina.
     *
     * @param tl radio de la esquina superior izquierda
     * @param tr radio de la esquina superior derecha
     * @param br radio de la esquina inferior derecha
     * @param bl radio de la esquina inferior izquierda
     * @return la tarjeta con degradado
     */
    public static Tarjeta degradada(int tl, int tr, int br, int bl) {
        return new Tarjeta(Tema.TERRACOTA, true, tl, tr, br, bl);
    }

    /**
     * Dibuja la tarjeta con una sombra suave y su color o degradado.
     *
     * @param g contexto gráfico donde se dibuja
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = Tema.suavizar(g);
        int w = getWidth() - 2, h = getHeight() - 4;
        // agrega una sombra suave debajo de la tarjeta
        g2.setColor(new Color(0, 0, 0, 16));
        g2.fill(Tema.forma(1, 3, w, h, tl, tr, br, bl));

        // aplica el degradado o el color que se paso al crear la tarjeta
        if (conDegradado) {
            g2.setPaint(Tema.degradado(w, h));
        } else {
            g2.setColor(color);
        }
        g2.fill(Tema.forma(0, 0, w, h, tl, tr, br, bl));
        g2.dispose();
    }
}