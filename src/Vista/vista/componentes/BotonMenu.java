package vista.componentes;

import vista.Tema;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Botón del menú lateral que marca la sección seleccionada.
 *
 * @author Yleana Torres
 * @version TP7 - 2026
 */
public class BotonMenu extends JButton {

    /**
     * Indica si la sección del botón es la que está seleccionada.
     */
    private boolean activo;

    /**
     * Indica si el cursor está encima del botón.
     */
    private boolean encima;

    /**
     * Indica si el foco llegó por un clic del mouse, para no dibujar el
     * borde de teclado.
     */
    private boolean focoPorMouse;

    /**
     * Crea el botón con el texto indicado y el estilo del menú.
     *
     * @param texto texto del botón
     */
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

    /**
     * Crea el botón con el texto y el ícono indicados, alineado a la
     * izquierda.
     *
     * @param texto texto del botón
     * @param icono ícono que se muestra junto al texto
     */
    public BotonMenu(String texto, Icon icono) {
        this(texto);
        setIcon(icono);
        setIconTextGap(14);
        setHorizontalAlignment(LEFT);
        setBorder(BorderFactory.createEmptyBorder(12, 40, 12, 12));
    }

    /**
     * Establece si el botón está seleccionado y actualiza su color y su
     * fuente.
     *
     * @param activo true si la sección del botón es la actual
     */
    public void setActivo(boolean activo) {
        this.activo = activo;
        setForeground(activo ? Tema.TINTA_TERRACOTA : Tema.TEXTO_CLARO);
        setFont(Tema.sans(activo ? Font.BOLD : Font.PLAIN, 14));
        repaint();
    }

    /**
     * Dibuja el fondo del botón según su estado (seleccionado, con el
     * cursor encima o con el foco del teclado) y luego su contenido.
     *
     * @param g contexto gráfico donde se dibuja
     */
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