package vista;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.geom.Path2D;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Colores, fuentes y utilidades de dibujo que comparten los componentes de
 * la interfaz.
 *
 * @author Yleana Torres
 * @version TP7 - 2026
 */
public final class Tema {

    /**
     * Evita que la clase de utilidades sea instanciada.
     */
    private Tema() { }

    /**
     * Color del menú lateral y las barras oscuras.
     */
    public static final Color CAFE_OSCURO     = new Color(0x3B2A22);

    /**
     * Color de los elementos que van sobre el café oscuro.
     */
    public static final Color CAFE            = new Color(0x4F382E);

    /**
     * Acento principal de la interfaz.
     */
    public static final Color TERRACOTA       = new Color(0x9E4739);

    /**
     * Variante oscura del terracota.
     */
    public static final Color TERRACOTA_OSC   = new Color(0x7A3429);

    /**
     * Color donde termina el degradado.
     */
    public static final Color CARAMELO        = new Color(0xD4A373);

    /**
     * Color del texto sobre terracota o caramelo.
     */
    public static final Color TINTA_TERRACOTA = new Color(0xFFF3E0);

    /**
     * Terracota legible sobre gris claro.
     */
    public static final Color ACENTO_TEXTO    = new Color(0x9E4739);

    /**
     * Color del fondo general.
     */
    public static final Color FONDO           = new Color(0xEBDCC4);

    /**
     * Color de los paneles y las tarjetas.
     */
    public static final Color PANEL           = new Color(0x5C3A21);

    /**
     * Color de la fila seleccionada.
     */
    public static final Color SELECCION       = new Color(0xCAA374);

    /**
     * Color de las líneas divisorias.
     */
    public static final Color LINEA           = new Color(0xCBB79E);

    /**
     * Color del texto principal.
     */
    public static final Color TEXTO           = new Color(0x3B2A22);

    /**
     * Color del texto secundario.
     */
    public static final Color TEXTO_SUAVE     = new Color(0x7A6555);

    /**
     * Color del texto sobre fondos oscuros.
     */
    public static final Color TEXTO_CLARO     = new Color(0xF3E8D6);

    /**
     * Familias de fuentes disponibles en el sistema, para usar las primeras
     * que se encuentren.
     */
    private static final Set<String> FAMILIAS = new HashSet<>(Arrays.asList(
            GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));

    /**
     * Familia sans serif elegida para los textos de la interfaz.
     */
    private static final String SANS = elegir(Font.SANS_SERIF,
            "Poppins", "Montserrat", "Segoe UI", "Helvetica Neue", "Calibri", "Arial");

    /**
     * Familia de ancho fijo elegida para los listados.
     */
    private static final String MONO = elegir(Font.MONOSPACED,
            "Cascadia Mono", "Consolas", "JetBrains Mono", "Menlo", "DejaVu Sans Mono");

    /**
     * Devuelve la primera familia preferida que esté disponible, o la
     * familia por defecto si no hay ninguna.
     *
     * @param porDefecto familia a usar si no se encuentra ninguna preferida
     * @param preferidas familias a buscar, en orden de preferencia
     * @return el nombre de la familia elegida
     */
    private static String elegir(String porDefecto, String... preferidas) {
        for (String f : preferidas) {
            if (FAMILIAS.contains(f)) return f;
        }
        return porDefecto;
    }

    /**
     * Devuelve una fuente sans serif con el estilo y el tamaño indicados.
     *
     * @param estilo   estilo de la fuente (Font.PLAIN, Font.BOLD, etc.)
     * @param tamanio  tamaño de la fuente
     * @return la fuente creada
     */
    public static Font sans(int estilo, float tamanio) {
        return new Font(SANS, estilo, 12).deriveFont(tamanio);
    }

    /**
     * Devuelve una fuente de ancho fijo para que los listados mantengan su
     * formato.
     *
     * @param tamanio tamaño de la fuente
     * @return la fuente creada
     */
    public static Font mono(float tamanio) {
        return new Font(MONO, Font.PLAIN, 12).deriveFont(tamanio);
    }

    /**
     * Crea una copia del contexto gráfico con el suavizado activado, para
     * dibujar los componentes personalizados.
     *
     * @param g contexto gráfico original
     * @return el contexto gráfico con suavizado
     */
    public static Graphics2D suavizar(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        return g2;
    }

    /**
     * Devuelve el mismo color con la transparencia indicada.
     *
     * @param c    color original
     * @param alfa transparencia, de 0 (transparente) a 255 (opaco)
     * @return el color con el nuevo valor alfa
     */
    public static Color conAlfa(Color c, int alfa) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), alfa);
    }

    /**
     * Crea una figura con un radio independiente para cada esquina.
     *
     * @param x  posición horizontal
     * @param y  posición vertical
     * @param w  ancho
     * @param h  alto
     * @param tl radio de la esquina superior izquierda
     * @param tr radio de la esquina superior derecha
     * @param br radio de la esquina inferior derecha
     * @param bl radio de la esquina inferior izquierda
     * @return la figura creada
     */
    public static Path2D forma(double x, double y, double w, double h,
                               double tl, double tr, double br, double bl) {
        final double k = 0.4477; // 1 - 0.5523: aproxima un cuarto de círculo con una curva Bézier
        double max = Math.min(w, h) / 2; // un radio mayor a la mitad del lado deforma la figura
        tl = Math.min(tl, max); tr = Math.min(tr, max); br = Math.min(br, max); bl = Math.min(bl, max);
        Path2D.Double p = new Path2D.Double();
        p.moveTo(x + tl, y);
        p.lineTo(x + w - tr, y);
        p.curveTo(x + w - tr * k, y, x + w, y + tr * k, x + w, y + tr);
        p.lineTo(x + w, y + h - br);
        p.curveTo(x + w, y + h - br * k, x + w - br * k, y + h, x + w - br, y + h);
        p.lineTo(x + bl, y + h);
        p.curveTo(x + bl * k, y + h, x, y + h - bl * k, x, y + h - bl);
        p.lineTo(x, y + tl);
        p.curveTo(x, y + tl * k, x + tl * k, y, x + tl, y);
        p.closePath();
        return p;
    }

    /**
     * Genera el degradado diagonal de terracota a caramelo.
     *
     * @param ancho ancho del área a cubrir
     * @param alto  alto del área a cubrir
     * @return el degradado creado
     */
    public static Paint degradado(int ancho, int alto) {
        return new GradientPaint(0, 0, TERRACOTA, ancho, alto, CARAMELO);
    }

    /**
     * Configura las fuentes que usan los componentes estándar de Swing.
     */
    public static void instalar() {
        UIManager.put("OptionPane.messageFont", sans(Font.PLAIN, 14));
        UIManager.put("OptionPane.buttonFont", sans(Font.BOLD, 13));
        UIManager.put("ToolTip.font", sans(Font.PLAIN, 12));
        UIManager.put("Label.font", sans(Font.PLAIN, 14));
        UIManager.put("TextField.font", sans(Font.PLAIN, 15));
        UIManager.put("ComboBox.font", sans(Font.PLAIN, 14));
    }

    /**
     * Personaliza las barras de desplazamiento para que sean más finas y no
     * tengan flechas.
     *
     * @param sp panel de desplazamiento a personalizar
     */
    public static void estilizar(JScrollPane sp) {
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        for (JScrollBar barra : new JScrollBar[]{sp.getVerticalScrollBar(), sp.getHorizontalScrollBar()}) {
            barra.setUI(new BasicScrollBarUI() {
                @Override protected void configureScrollBarColors() {
                    thumbColor = new Color(0xB3BCBC);
                    trackColor = PANEL;
                }
                @Override protected JButton createDecreaseButton(int orientation) { return sinBoton(); }
                @Override protected JButton createIncreaseButton(int orientation) { return sinBoton(); }
                @Override protected void paintTrack(Graphics g, JComponent c, Rectangle r) { }
                @Override protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
                    Graphics2D g2 = suavizar(g);
                    g2.setColor(thumbColor);
                    g2.fillRoundRect(r.x + 3, r.y + 2, r.width - 6, r.height - 4, 8, 8);
                    g2.dispose();
                }
            });
            barra.setPreferredSize(new Dimension(12, 12));
            barra.setUnitIncrement(16);
        }
    }

    /**
     * Crea un botón sin tamaño, para quitar las flechas de las barras de
     * desplazamiento.
     *
     * @return el botón invisible
     */
    private static JButton sinBoton() {
        JButton b = new JButton();
        b.setPreferredSize(new Dimension(0, 0));
        return b;
    }
}