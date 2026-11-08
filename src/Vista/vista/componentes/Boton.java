package vista.componentes;

import vista.Tema;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Botón redondeado con distintos estilos para usar según el fondo y la
 * importancia de cada acción. También tiene un efecto al pasar el mouse y un
 * indicador cuando recibe el foco del teclado.
 *
 * @author Yleana Torres
 * @version TP7 - 2026
 */
public class Boton extends JButton {

    /**
     * Estilos disponibles para el botón.
     *
     * @author Yleana Torres
     * @version TP7 - 2026
     */
    public enum Estilo {

        /**
         * Botón con degradado terracota para la acción principal.
         */
        PRINCIPAL,

        /**
         * Botón café para acciones secundarias.
         */
        OSCURO,

        /**
         * Botón claro para usar sobre fondos claros.
         */
        CLARO,

        /**
         * Botón transparente con borde para usar sobre fondos oscuros.
         */
        SALIDA,

        /**
         * Botón sin fondo que solo muestra el texto.
         */
        TEXTO
    }

    /**
     * Estilo con el que se dibuja el botón.
     */
    private Estilo estilo = Estilo.PRINCIPAL;

    /**
     * Indica si el cursor está encima del botón.
     */
    private boolean encima;

    /**
     * Indica si el foco llegó por el teclado, para dibujar el contorno.
     */
    private boolean focoPorTeclado;

    /**
     * Crea un botón con el texto indicado y el estilo principal.
     *
     * @param texto texto del botón
     */
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
        addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                focoPorTeclado = e.getCause().name().startsWith("TRAVERSAL");
                repaint();
            }
            @Override public void focusLost(FocusEvent e) { focoPorTeclado = false; repaint(); }
        });
    }

    /**
     * Crea un botón con el texto y el estilo indicados.
     *
     * @param texto  texto del botón
     * @param estilo estilo con el que se dibuja el botón
     */
    public Boton(String texto, Estilo estilo) {
        this(texto);
        setEstilo(estilo);
    }

    /**
     * Establece el estilo del botón y actualiza el color de su texto.
     *
     * @param estilo estilo con el que se dibuja el botón
     */
    public void setEstilo(Estilo estilo) {
        this.estilo = estilo;

        // el texto cambia de color según el estilo elegido
        setForeground(estilo == Estilo.OSCURO ? Tema.TINTA_TERRACOTA
                : estilo == Estilo.CLARO ? Tema.TEXTO
                : estilo == Estilo.SALIDA ? Tema.TEXTO_CLARO
                : estilo == Estilo.TEXTO ? Tema.conAlfa(Tema.TEXTO_CLARO, 210) : Tema.TINTA_TERRACOTA);
        repaint();
    }

    /**
     * Dibuja el fondo redondeado según el estilo y el estado del botón, y
     * luego su contenido.
     *
     * @param g contexto gráfico donde se dibuja
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = Tema.suavizar(g);
        int w = getWidth(), h = getHeight();

        // elegimos el fondo según el estado del botón y el estilo
        if (!isEnabled()) {
            g2.setColor(Tema.LINEA);
        } else if (estilo == Estilo.OSCURO) {
            g2.setColor(encima ? Tema.PANEL : Tema.CAFE);
        } else if (estilo == Estilo.SALIDA || estilo == Estilo.TEXTO) {
            g2.setColor(encima ? Tema.CAFE : Tema.conAlfa(Tema.CAFE, 0));
        } else if (estilo == Estilo.CLARO) {
            g2.setColor(encima ? Tema.SELECCION : Tema.LINEA);
        } else if (encima) {
            g2.setPaint(new GradientPaint(0, 0, Tema.TERRACOTA_OSC, w, h, Tema.TERRACOTA));
        } else {
            g2.setPaint(Tema.degradado(w, h));
        }

        // dibujamos el fondo redondeado
        g2.fillRoundRect(0, 0, w, h, h, h);
        if (estilo == Estilo.SALIDA && isEnabled()) {
            g2.setColor(Tema.conAlfa(Tema.TEXTO_CLARO, 120));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(1, 1, w - 3, h - 3, h - 2, h - 2);
        }

        // marcamos el contorno cuando el botón recibe el foco del teclado
        if (isFocusOwner() && focoPorTeclado) {
            g2.setColor(estilo == Estilo.PRINCIPAL || estilo == Estilo.CLARO ? Tema.CAFE_OSCURO : Tema.TERRACOTA);
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(2, 2, w - 5, h - 5, h, h);
        }
        g2.dispose();

        // dejamos que swing dibuje el texto y los demás elementos del botón
        super.paintComponent(g);
    }
}