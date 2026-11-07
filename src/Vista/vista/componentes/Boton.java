package vista.componentes;

import vista.Tema;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

    /**
    * boton redondeado con distintos estilos para usar según el fondo y la importancia de cada acción
    * también tiene un efecto al pasar el mouse y un indicador cuando recibe el foco del teclado
    */
public class Boton extends JButton {

    public enum Estilo { PRINCIPAL, OSCURO, CLARO }

    private Estilo estilo = Estilo.PRINCIPAL;
    private boolean encima;

    public Boton(String texto) {
        super(texto);
        
        // dejamos el dibujo del fondo a cargo de nuestra implementación
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setFont(Tema.sans(Font.BOLD, 13));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 22));
        setEstilo(Estilo.PRINCIPAL);
        
        // detectamos cuándo el mouse entra o sale para cambiar la apariencia
        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { encima = true; repaint(); }
            @Override public void mouseExited(MouseEvent e) { encima = false; repaint(); }
        });
    }

    public Boton(String texto, Estilo estilo) {
        this(texto);
        setEstilo(estilo);
    }

    public void setEstilo(Estilo estilo) {
        this.estilo = estilo;
        
        // el texto cambia de color según el estilo elegido
        setForeground(estilo == Estilo.OSCURO ? Color.WHITE
                : estilo == Estilo.CLARO ? Tema.TEXTO : Tema.TINTA_MENTA);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = Tema.suavizar(g);
        int w = getWidth(), h = getHeight();
        
        // elegimos el fondo según el estado del botón y el estilo
        if (!isEnabled()) {
            g2.setColor(Tema.LINEA);
        } else if (estilo == Estilo.OSCURO) {
            g2.setColor(encima ? new Color(0x4C5252) : Tema.CARBON_2);
        } else if (estilo == Estilo.CLARO) {
            g2.setColor(encima ? Tema.SELECCION : Tema.LINEA);
        } else if (encima) {
            g2.setPaint(new GradientPaint(0, 0, Tema.MENTA_OSC, w, h, new Color(0x0E9FCC)));
        } else {
            g2.setPaint(Tema.degradado(w, h));
        }
        
        // dibujamos el fondo redondeado
        g2.fillRoundRect(0, 0, w, h, h, h);
        
        // marcamos el contorno cuando el botón recibe el foco del teclado
        if (isFocusOwner()) {
            g2.setColor(estilo == Estilo.OSCURO ? Tema.MENTA : Tema.CARBON);
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(2, 2, w - 5, h - 5, h, h);
        }
        g2.dispose();
        
        // dejamos que swing dibuje el texto y los demás elementos del botón
        super.paintComponent(g);
    }
}
