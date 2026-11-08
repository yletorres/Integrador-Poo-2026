package vista.componentes;

import vista.Tema;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;

/**
 * Indicador circular que muestra un porcentaje de progreso. Se dibuja a mano
 * para poder controlar el grosor, los colores y el texto del centro sin
 * depender de componentes externos.
 *
 * @author Yleana Torres
 * @version TP7 - 2026
 */
public class Anillo extends JComponent {

    /**
     * Color del círculo de fondo del anillo.
     */
    private final Color pista;

    /**
     * Color del arco que indica el progreso.
     */
    private final Color arco;

    /**
     * Color del texto del centro.
     */
    private final Color letra;

    /**
     * Grosor del anillo en píxeles.
     */
    private final float grosor;

    /**
     * Valor actual del progreso, entre 0 y 1.
     */
    private double valor;

    /**
     * Texto que aparece en el medio del indicador.
     */
    private String centro = "0%";

    /**
     * Crea el anillo con los colores y el grosor indicados.
     *
     * @param pista  color del círculo de fondo
     * @param arco   color del arco de progreso
     * @param letra  color del texto del centro
     * @param grosor grosor del anillo en píxeles
     */
    public Anillo(Color pista, Color arco, Color letra, float grosor) {
        this.pista = pista;
        this.arco = arco;
        this.letra = letra;
        this.grosor = grosor;

        // tamaño inicial del componente
        setPreferredSize(new Dimension(120, 120));
    }

    /**
     * Establece el progreso y el texto del centro, y vuelve a dibujar el
     * anillo.
     *
     * @param valor  progreso entre 0 y 1; si queda fuera de ese rango se limita
     * @param centro texto que se muestra en el centro
     */
    public void setValor(double valor, String centro) {
        // limitamos el progreso para que nunca salga del rango 0-1
        this.valor = Math.max(0, Math.min(1, valor));
        this.centro = centro;

        // le avisamos a swing que tiene que volver a dibujarlo
        repaint();
    }

    /**
     * Dibuja el círculo de fondo, el arco de progreso y el texto del centro.
     *
     * @param g contexto gráfico donde se dibuja
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = Tema.suavizar(g);

        // calculamos el diámetro disponible para que el anillo
        // mantenga sus proporciones y no se salga del componente
        double d = Math.min(getWidth(), getHeight()) - grosor - 4;
        double x = (getWidth() - d) / 2, y = (getHeight() - d) / 2;

        // primero dibujamos el círculo completo que sirve de fondos
        g2.setColor(pista);
        g2.setStroke(new BasicStroke(grosor));
        g2.draw(new Ellipse2D.Double(x, y, d, d));

        // encima dibujamos solamente la parte correspondiente al progreso
        if (valor > 0) {
            g2.setColor(arco);
            g2.setStroke(new BasicStroke(grosor, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            // arranca desde arriba y avanza en sentido horario
            g2.draw(new Arc2D.Double(x, y, d, d, 90, -360 * valor, Arc2D.OPEN));
        }

        // centramos el porcentaje dentro del círculo
        g2.setColor(letra);
        g2.setFont(Tema.sans(Font.BOLD, (float) (d * 0.24)));
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(centro, (float) (getWidth() - fm.stringWidth(centro)) / 2,
                (float) (getHeight() - fm.getHeight()) / 2 + fm.getAscent());

        // liberamos los recursos gráficos utilizados
        g2.dispose();
    }
}