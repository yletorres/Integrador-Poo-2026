package vista.componentes;

import vista.Tema;

import javax.swing.*;
import java.awt.*;

// etiqueta de estado con forma de píldora para mostrar si un libro esta prestado o disponible
public class Etiqueta extends JLabel {

    private Color fondo = Tema.MENTA;

    public Etiqueta() {
        setOpaque(false);
        setFont(Tema.sans(Font.BOLD, 13));
        setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));
    }

    public void mostrar(String texto, Color fondo, Color letra) {
        this.fondo = fondo;
        setText(texto);
        setForeground(letra);
        setVisible(true);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = Tema.suavizar(g);
        g2.setColor(fondo);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
        g2.dispose();
        super.paintComponent(g);
    }
}
