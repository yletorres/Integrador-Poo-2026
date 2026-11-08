package vista;

import vista.componentes.Avatar;
import vista.componentes.Boton;
import vista.componentes.Icono;

import javax.swing.*;
import java.awt.*;

/**
 * Pantalla inicial con las opciones para entrar al sistema, ejecutar la
 * demostración o salir. Recibe las acciones desde afuera y se encarga
 * solamente de mostrarlas.
 *
 * @author Yleana Torres
 * @version TP7 - 2026
 */
public class VentanaInicial extends JFrame {

    /**
     * Anchos de los lomos de libros dibujados en el fondo.
     */
    private static final int[] ANCHOS_LOMO = {16, 22, 14, 26, 18, 12, 24, 20, 15, 28, 17, 21, 13, 25, 19};

    /**
     * Altos de los lomos de libros dibujados en el fondo.
     */
    private static final int[] ALTOS_LOMO = {40, 52, 34, 58, 46, 30, 54, 42, 48, 60, 36, 50, 32, 56, 44};

    /**
     * Crea la ventana inicial con sus botones y el pie de página.
     *
     * @param iniciarSistema       acción que se ejecuta al elegir "Iniciar sistema de biblioteca"
     * @param ejecutarDemostracion acción que se ejecuta al elegir "Ejecutar demostración del práctico"
     */
    public VentanaInicial(Runnable iniciarSistema, Runnable ejecutarDemostracion) {
        super("Gestión de Biblioteca");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(780, 560);
        setResizable(false);
        setLocationRelativeTo(null);
        setIconImages(Avatar.iconosDeVentana());

        Avatar avatar = new Avatar(108);
        JPanel fondo = new FondoInicial(avatar);
        fondo.setLayout(new BoxLayout(fondo, BoxLayout.Y_AXIS));
        fondo.setBorder(BorderFactory.createEmptyBorder(42, 40, 18, 40));

        avatar.setAlignmentX(Component.CENTER_ALIGNMENT);
        fondo.add(avatar);
        fondo.add(Box.createVerticalStrut(18));
        fondo.add(centrado("Gestión de", Tema.sans(Font.PLAIN, 16), Tema.conAlfa(Tema.TEXTO_CLARO, 200)));
        fondo.add(centrado("Biblioteca", Tema.sans(Font.BOLD, 42), Tema.TINTA_TERRACOTA));
        fondo.add(Box.createVerticalStrut(8));
        fondo.add(centrado("Te damos la bienvenida. Elegí una opción para comenzar.", Tema.sans(Font.PLAIN, 15), Tema.TEXTO_CLARO));
        fondo.add(Box.createVerticalStrut(30));

        Boton sistema = new Boton("Iniciar sistema de biblioteca");
        sistema.setFont(Tema.sans(Font.BOLD, 15));
        sistema.addActionListener(e -> {
            dispose();
            iniciarSistema.run();
        });
        fondo.add(conTamanio(sistema, 360, 54));
        fondo.add(Box.createVerticalStrut(12));

        Boton demostracion = new Boton("Ejecutar demostración del práctico", Boton.Estilo.OSCURO);
        demostracion.addActionListener(e -> ejecutarDemostracion.run());
        fondo.add(conTamanio(demostracion, 300, 42));
        fondo.add(Box.createVerticalGlue());

        Boton salir = new Boton("Salir", Boton.Estilo.TEXTO);
        salir.setIcon(new Icono(Icono.Tipo.SALIR, 16));
        salir.setIconTextGap(8);
        salir.setFont(Tema.sans(Font.PLAIN, 13));
        salir.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        salir.addActionListener(e -> System.exit(0));

        JLabel version = new JLabel("Biblioteca · Trabajo Integrador · POO 2026");
        version.setFont(Tema.sans(Font.PLAIN, 12));
        version.setForeground(Tema.conAlfa(Tema.TEXTO_CLARO, 120));

        JPanel pie = new JPanel(new BorderLayout());
        pie.setOpaque(false);
        pie.add(version, BorderLayout.WEST);
        pie.add(salir, BorderLayout.EAST);
        pie.setAlignmentX(Component.CENTER_ALIGNMENT);
        pie.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        fondo.add(pie);

        setContentPane(fondo);
    }

    /**
     * Crea una etiqueta de texto centrada para reutilizar el mismo formato.
     *
     * @param texto  texto que muestra la etiqueta
     * @param fuente fuente del texto
     * @param color  color del texto
     * @return la etiqueta creada
     */
    private static JLabel centrado(String texto, Font fuente, Color color) {
        JLabel l = new JLabel(texto);
        l.setFont(fuente);
        l.setForeground(color);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        return l;
    }

    /**
     * Fija el tamaño de un componente y lo centra horizontalmente.
     *
     * @param c     componente a ajustar
     * @param ancho ancho en píxeles
     * @param alto  alto en píxeles
     * @return el componente ya ajustado
     */
    private static JComponent conTamanio(JComponent c, int ancho, int alto) {
        Dimension d = new Dimension(ancho, alto);
        c.setPreferredSize(d);
        c.setMaximumSize(d);
        c.setAlignmentX(Component.CENTER_ALIGNMENT);
        return c;
    }

    /**
     * Panel que dibuja el fondo de la pantalla inicial: color base, resplandores
     * detrás del logo, degradado inferior y lomos de libros.
     *
     * @author Yleana Torres
     * @version TP7 - 2026
     */
    private static final class FondoInicial extends JPanel {

        /**
         * Logo sobre el que se centran los resplandores del fondo.
         */
        private final JComponent logo;

        /**
         * Crea el fondo tomando como referencia la posición del logo.
         *
         * @param logo componente del logo
         */
        FondoInicial(JComponent logo) {
            this.logo = logo;
            setOpaque(true);
        }

        /**
         * Dibuja el fondo completo del panel.
         *
         * @param g contexto gráfico donde se dibuja
         */
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = Tema.suavizar(g);
            int w = getWidth(), h = getHeight();
            g2.setColor(Tema.CAFE_OSCURO);
            g2.fillRect(0, 0, w, h);

            Point c = SwingUtilities.convertPoint(logo, logo.getWidth() / 2, logo.getHeight() / 2, this);
            g2.setPaint(new RadialGradientPaint(c, 340f, new float[]{0f, 1f},
                    new Color[]{Tema.conAlfa(Tema.TERRACOTA, 70), Tema.conAlfa(Tema.TERRACOTA, 0)}));
            g2.fillRect(0, 0, w, h);
            g2.setPaint(new RadialGradientPaint(c, 96f, new float[]{0f, 1f},
                    new Color[]{Tema.conAlfa(Tema.CARAMELO, 80), Tema.conAlfa(Tema.CARAMELO, 0)}));
            g2.fillRect(0, 0, w, h);

            g2.setPaint(new GradientPaint(0, h * 0.62f, Tema.conAlfa(Tema.TERRACOTA, 0),
                    0, h, Tema.conAlfa(Tema.TERRACOTA, 110)));
            g2.fillRect(0, 0, w, h);

            g2.setColor(Tema.conAlfa(Tema.CARAMELO, 14));
            int x = 0, i = 0;
            while (x < w) {
                int ancho = ANCHOS_LOMO[i % ANCHOS_LOMO.length];
                int alto = ALTOS_LOMO[i % ALTOS_LOMO.length];
                g2.fillRoundRect(x, h - alto, ancho, alto + 6, 4, 4);
                x += ancho + 3;
                i++;
            }
            g2.dispose();
        }
    }
}