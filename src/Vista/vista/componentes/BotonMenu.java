package vista.componentes;

import vista.Tema;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// boton del menu lateral que marca la seccion seleccionada
public class BotonMenu extends JButton {

    private boolean activo;
    private boolean encima;
    private boolean focoPorMouse;

    public BotonMenu(String texto) {
        super(texto);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setHorizontalAlignment(CENTER);
        setFont(Tema.sans(Font.PLAIN, 14));
        setForeground(Tema.TEXTO_CLARO);
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { encima = true; repaint(); }
            @Override public void mouseExited(MouseEvent e) { encima = false; repaint(); }
            @Override public void mousePressed(MouseEvent e) { focoPorMouse = true; }
        });
        addFocusListener(new FocusAdapter() {
            @Override public void focusLost(FocusEvent e) { focoPorMouse = false; repaint(); }
        });
    }

    public BotonMenu(String texto, Icon icono) {
        this(texto);
        setIcon(icono);
        setIconTextGap(14);
        setHorizontalAlignment(LEFT);
        setBorder(BorderFactory.createEmptyBorder(12, 40, 12, 12));
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
        setForeground(activo ? Tema.TINTA_TERRACOTA : Tema.TEXTO_CLARO);
        setFont(Tema.sans(activo ? Font.BOLD : Font.PLAIN, 14));
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = Tema.suavizar(g);
        int x = 18, y = 4, w = getWidth() - 36, h = getHeight() - 8;
        
        // dibuja el fondo con degradado cuando la opcion esta seleccionada
        if (activo) {
            g2.setPaint(new GradientPaint(x, y, Tema.TERRACOTA_OSC, x + w, y + h, Tema.TERRACOTA));
            g2.fillRoundRect(x, y, w, h, h, h);
        } else if (encima) {
            
            // marca el boton cuando el cursor pasa por encima
            g2.setColor(Tema.CAFE);
            g2.fillRoundRect(x, y, w, h, h, h);
        }
        
        // muestra un borde cuando el boton tiene el foco
        if (isFocusOwner() && !focoPorMouse) {
            g2.setColor(Tema.CARAMELO);
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(x, y, w - 1, h - 1, h, h);
        }
        g2.dispose();
        super.paintComponent(g);
    }
}
