package vista;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.geom.Path2D;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

// colores, fuentes y utilidades de dibujo que comparten los componentes de la interfaz
public final class Tema {

    private Tema() { }

    // paleta
    public static final Color CARBON       = new Color(0x3B2A22); // menú lateral, barras oscuras
    public static final Color CARBON_2     = new Color(0x4F382E); // elementos sobre el carbón
    public static final Color MENTA        = new Color(0x9E4739); // acento principal
    public static final Color MENTA_OSC    = new Color(0x7A3429);
    public static final Color CELESTE      = new Color(0xD4A373); // fin del degradado
    public static final Color TINTA_MENTA  = new Color(0xFFF3E0); // texto sobre menta/celeste
    public static final Color ACENTO_TEXTO = new Color(0x9E4739); // menta legible sobre gris claro
    public static final Color FONDO        = new Color(0xEBDCC4); // fondo general
    public static final Color PANEL        = new Color(0x5C3A21); // paneles y tarjetas
    public static final Color SELECCION    = new Color(0xCAA374); // fila seleccionada
    public static final Color LINEA        = new Color(0xCBB79E);
    public static final Color TEXTO        = new Color(0x3B2A22);
    public static final Color TEXTO_SUAVE  = new Color(0x7A6555);
    public static final Color TEXTO_CLARO  = new Color(0xF3E8D6);

    // busca las fuentes disponibles para usar las primeras que encuentre
    private static final Set<String> FAMILIAS = new HashSet<>(Arrays.asList(
            GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));

    private static final String SANS = elegir(Font.SANS_SERIF,
            "Poppins", "Montserrat", "Segoe UI", "Helvetica Neue", "Calibri", "Arial");

    private static final String MONO = elegir(Font.MONOSPACED,
            "Cascadia Mono", "Consolas", "JetBrains Mono", "Menlo", "DejaVu Sans Mono");

    private static String elegir(String porDefecto, String... preferidas) {
        for (String f : preferidas) {
            if (FAMILIAS.contains(f)) return f;
        }
        return porDefecto;
    }

    public static Font sans(int estilo, float tamanio) {
        return new Font(SANS, estilo, 12).deriveFont(tamanio);
    }

    // fuente de ancho fijo para que los listados mantengan su formato
    public static Font mono(float tamanio) {
        return new Font(MONO, Font.PLAIN, 12).deriveFont(tamanio);
    }

    // utilidades para dibujar los componentes personalizados
    public static Graphics2D suavizar(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        return g2;
    }

    public static Color conAlfa(Color c, int alfa) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), alfa);
    }

    // crea una figura con un radio independiente para cada esquina
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

    // genera el degradado diagonal de menta a celeste
    public static Paint degradado(int ancho, int alto) {
        return new GradientPaint(0, 0, MENTA, ancho, alto, CELESTE);
    }

    // configura las fuentes que usan los componentes estandar de swing
    public static void instalar() {
        UIManager.put("OptionPane.messageFont", sans(Font.PLAIN, 14));
        UIManager.put("OptionPane.buttonFont", sans(Font.BOLD, 13));
        UIManager.put("ToolTip.font", sans(Font.PLAIN, 12));
        UIManager.put("Label.font", sans(Font.PLAIN, 14));
        UIManager.put("TextField.font", sans(Font.PLAIN, 15));
        UIManager.put("ComboBox.font", sans(Font.PLAIN, 14));
    }

    // personaliza las barras de desplazamiento para que sean mas finas y no tengan flechas
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

    private static JButton sinBoton() {
        JButton b = new JButton();
        b.setPreferredSize(new Dimension(0, 0));
        return b;
    }
}