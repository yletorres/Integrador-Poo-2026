package vista.componentes;

import vista.Tema;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

// fondo oscuro con un brillo menta en la parte inferior
public class FondoOscuro extends JPanel {

    private final int radio;

    public FondoOscuro(int radio) {
        this.radio = radio;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = Tema.suavizar(g);
        int w = getWidth(), h = getHeight();
        Shape forma = new RoundRectangle2D.Float(0, 0, w, h, radio, radio);
        
        // pinta la base oscura y aplica el brillo en la parte baja
        g2.setColor(Tema.CARBON);
        g2.fill(forma);
        g2.setClip(forma);
        g2.setPaint(new GradientPaint(0, h * 0.58f, Tema.conAlfa(Tema.MENTA, 0),
                0, h, Tema.conAlfa(Tema.MENTA, 140)));
        g2.fillRect(0, 0, w, h);
        g2.dispose();
    }
}
