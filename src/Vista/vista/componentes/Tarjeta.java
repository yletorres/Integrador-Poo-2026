package vista.componentes;

import vista.Tema;

import javax.swing.*;
import java.awt.*;

// panel con esquinas redondeadas que puede tener un color liso o un degradado
public class Tarjeta extends JPanel {

    private final Color color;
    private final boolean conDegradado;
    private final double tl, tr, br, bl;

    // crea una tarjeta con el mismo redondeo en todas las esquinas
    public Tarjeta(Color color, int radio) {
        this(color, false, radio, radio, radio, radio);
    }

    private Tarjeta(Color color, boolean conDegradado, int tl, int tr, int br, int bl) {
        this.color = color;
        this.conDegradado = conDegradado;
        this.tl = tl; this.tr = tr; this.br = br; this.bl = bl;
        setOpaque(false);
        setLayout(new BorderLayout());
    }

    // crea una tarjeta con degradado y permite elegir el redondeo de cada esquina
    public static Tarjeta degradada(int tl, int tr, int br, int bl) {
        return new Tarjeta(Tema.TERRACOTA, true, tl, tr, br, bl);
    }

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
