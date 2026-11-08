package vista.componentes;

import vista.Tema;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Avatar circular con un anillo degradado y un libro abierto en el centro.
 * Todo se dibuja con vectores para poder ajustar el tamaño sin perder
 * calidad.
 *
 * @author Yleana Torres
 * @version TP7 - 2026
 */
public class Avatar extends JComponent {

    /**
     * Diámetro del avatar en píxeles.
     */
    private final int diametro;

    /**
     * Crea el avatar con el diámetro indicado y fija su tamaño.
     *
     * @param diametro diámetro del avatar en píxeles
     */
    public Avatar(int diametro) {
        this.diametro = diametro;

        // mantenemos el mismo tamaño para que no se deforme al acomodarlo en la ventana
        Dimension d = new Dimension(diametro, diametro);
        setPreferredSize(d);
        setMinimumSize(d);
        setMaximumSize(d);
    }

    /**
     * Crea las imágenes del avatar en distintos tamaños para usarlas como
     * íconos de la ventana.
     *
     * @return la lista de imágenes para los íconos
     */
    public static List<Image> iconosDeVentana() {
        List<Image> iconos = new ArrayList<>();
        for (int lado : new int[]{16, 24, 32, 48, 64}) {
            Avatar avatar = new Avatar(lado);
            avatar.setSize(lado, lado);
            BufferedImage img = new BufferedImage(lado, lado, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = img.createGraphics();
            avatar.paint(g);
            g.dispose();
            iconos.add(img);
        }
        return iconos;
    }

    /**
     * Dibuja el círculo con su borde degradado y el libro abierto en el
     * centro.
     *
     * @param g contexto gráfico donde se dibuja
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = Tema.suavizar(g);

        // calculamos el grosor del borde en base al tamaño del avatar
        float grosor = Math.max(3f, diametro / 22f);
        double m = grosor / 2 + 1, d = diametro - 2 * m;

        // dibujamos el fondo circular y después el borde con degradado
        g2.setColor(Tema.CAFE);
        g2.fill(new Ellipse2D.Double(m, m, d, d));
        g2.setPaint(new GradientPaint(0, 0, Tema.TERRACOTA, diametro, diametro, Tema.CARAMELO));
        g2.setStroke(new BasicStroke(grosor));
        g2.draw(new Ellipse2D.Double(m, m, d, d));

        // dibujamos las dos páginas del libro dejando una pequeña separación en el centro
        double c = diametro / 2.0, u = diametro / 100.0;
        g2.setPaint(new GradientPaint(0, 0, Tema.TERRACOTA, diametro, diametro, Tema.CARAMELO));

        // usamos el mismo trazado para ambos lados cambiando la dirección
        for (int lado = -1; lado <= 1; lado += 2) {
            Path2D.Double pagina = new Path2D.Double();
            pagina.moveTo(c + lado * 3 * u, c - 14 * u);
            pagina.curveTo(c + lado * 14 * u, c - 22 * u, c + lado * 26 * u, c - 22 * u, c + lado * 31 * u, c - 16 * u);
            pagina.lineTo(c + lado * 31 * u, c + 14 * u);
            pagina.curveTo(c + lado * 26 * u, c + 8 * u, c + lado * 14 * u, c + 8 * u, c + lado * 3 * u, c + 16 * u);
            pagina.closePath();
            g2.fill(pagina);
        }
        g2.dispose();
    }
}