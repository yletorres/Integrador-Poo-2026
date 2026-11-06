package vista.componentes;

import vista.Tema;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;


    /**
    * indicador circular que muestra un porcentaje de progreso
    * se dibuja a mano para poder controlar el grosor, los colores
    * y el texto del centro sin depender de componentes externos
    */


public class Anillo extends JComponent {

    // colores del fondo del anillo, el progreso y el porcentaje
    private final Color pista, arco, letra;
    
    // grosor del anillo y valor actual del progreso
    private final float grosor;
    private double valor;
    
    // texto que aparece en el medio del indicador
    private String centro = "0%";

    public Anillo(Color pista, Color arco, Color letra, float grosor) {
        this.pista = pista;
        this.arco = arco;
        this.letra = letra;
        this.grosor = grosor;
        
        // tamaño inicial del componente
        setPreferredSize(new Dimension(120, 120));
    }

    public void setValor(double valor, String centro) {
        // limitamos el progreso para que nunca salga del rango 0-1
        this.valor = Math.max(0, Math.min(1, valor));
        this.centro = centro;
        
        // le avisamos a swing que tiene que volver a dibujarlo
        repaint();
    }

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
