package vista;

import vista.componentes.Boton;
import vista.componentes.Listado;
import vista.componentes.Tarjeta;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Ventana que muestra los resultados de la demostración del práctico.
 * Permite ver la salida aunque el programa se ejecute sin consola.
 * Cerrarla no cierra el programa.
 * <p>
 * Puede mostrar un texto simple o una lista de {@link Seccion}: cada una indica
 * la consigna a la que responde, su resultado y, en un panel desplegable, el
 * método que se usó para obtenerlo.
 *
 * @author Yleana Torres
 * @version TP7 - 2026
 */
public class VentanaSalida extends JFrame {

    /**
     * Un paso de la demostración: la consigna, el método usado y lo que se obtuvo.
     */
    public static class Seccion {
        private final String etiqueta;
        private final String consigna;
        private final String metodo;
        private final String resultado;

        /**
         * @param etiqueta  rótulo corto de la sección (por ejemplo "Consigna 1")
         * @param consigna  pregunta o consigna del enunciado a la que responde
         * @param metodo    método(s) invocado(s), se muestra en el panel desplegable
         * @param resultado salida obtenida
         */
        public Seccion(String etiqueta, String consigna, String metodo, String resultado) {
            this.etiqueta = etiqueta;
            this.consigna = consigna;
            this.metodo = metodo;
            this.resultado = resultado;
        }
    }

    /**
     * Crea la ventana con un título y el texto a mostrar.
     *
     * @author Yleana Torres
     * @param p_titulo título de la ventana y encabezado del listado
     * @param p_texto  texto que se muestra en la hoja
     */
    public VentanaSalida(String p_titulo, String p_texto) {
        this(p_titulo);
        Listado listado = new Listado();
        listado.setTexto(p_texto);
        Tarjeta hoja = new Tarjeta(Color.WHITE, 28);
        hoja.setBorder(BorderFactory.createEmptyBorder(22, 28, 22, 28));
        hoja.add(listado, BorderLayout.CENTER);
        ((JPanel) getContentPane()).add(hoja, BorderLayout.CENTER);
    }

    /**
     * Crea la ventana con una lista de secciones, cada una con su consigna,
     * resultado y método desplegable.
     *
     * @param p_titulo   título de la ventana y encabezado
     * @param p_secciones pasos de la demostración
     */
    public VentanaSalida(String p_titulo, List<Seccion> p_secciones) {
        this(p_titulo);

        // panel que se estira al ancho de la ventana mientras entre el contenido
        JPanel contenido = new PanelVertical();
        for (Seccion s : p_secciones) {
            contenido.add(crearTarjeta(s));
            contenido.add(Box.createVerticalStrut(14));
        }

        JScrollPane scroll = new JScrollPane(contenido);
        Tema.estilizar(scroll);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        ((JPanel) getContentPane()).add(scroll, BorderLayout.CENTER);
    }

    // arma la estructura comun: titulo arriba y boton cerrar abajo
    private VentanaSalida(String p_titulo) {
        super(p_titulo);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE); // cerrarla no cierra el programa
        this.setSize(900, 680);
        this.setMinimumSize(new Dimension(640, 420));
        this.setLocationRelativeTo(null);

        JPanel raiz = new JPanel(new BorderLayout(0, 20));
        raiz.setBackground(Tema.FONDO);
        raiz.setBorder(BorderFactory.createEmptyBorder(28, 32, 26, 32));

        JLabel encabezado = new JLabel(p_titulo);
        encabezado.setFont(Tema.sans(Font.BOLD, 26));
        encabezado.setForeground(Tema.TEXTO);
        raiz.add(encabezado, BorderLayout.NORTH);

        Boton cerrar = new Boton("Cerrar");
        cerrar.addActionListener(e -> this.dispose());
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        pie.setOpaque(false);
        pie.add(cerrar);
        raiz.add(pie, BorderLayout.SOUTH);

        this.setContentPane(raiz);
    }

    // tarjeta de una seccion: rotulo, consigna, resultado y metodo desplegable
    private JComponent crearTarjeta(Seccion s) {
        Tarjeta tarjeta = new Tarjeta(Color.WHITE, 22) {
            @Override
            public Dimension getMaximumSize() {
                return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
            }
        };
        tarjeta.setAlignmentX(Component.LEFT_ALIGNMENT);
        tarjeta.setBorder(BorderFactory.createEmptyBorder(16, 22, 16, 22));

        JPanel cuerpo = new JPanel();
        cuerpo.setOpaque(false);
        cuerpo.setLayout(new BoxLayout(cuerpo, BoxLayout.Y_AXIS));

        JLabel etiqueta = new JLabel(s.etiqueta.toUpperCase());
        etiqueta.setFont(Tema.sans(Font.BOLD, 12));
        etiqueta.setForeground(Tema.ACENTO_TEXTO);
        cuerpo.add(izquierda(etiqueta));
        cuerpo.add(Box.createVerticalStrut(4));

        JLabel consigna = new JLabel("<html><body style='width:640px'>" + escapar(s.consigna) + "</body></html>");
        consigna.setFont(Tema.sans(Font.BOLD, 16));
        consigna.setForeground(Tema.TEXTO);
        cuerpo.add(izquierda(consigna));
        cuerpo.add(Box.createVerticalStrut(10));

        JTextArea resultado = areaTexto(s.resultado.stripTrailing(), Tema.TEXTO, null);
        cuerpo.add(izquierda(resultado));

        // metodo desplegable
        JTextArea metodo = areaTexto(s.metodo, Tema.TEXTO, Tema.TINTA_TERRACOTA);
        metodo.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        metodo.setVisible(false);

        JLabel toggle = new JLabel("▶  Método utilizado");
        toggle.setFont(Tema.sans(Font.BOLD, 13));
        toggle.setForeground(Tema.ACENTO_TEXTO);
        toggle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        toggle.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                boolean abrir = !metodo.isVisible();
                metodo.setVisible(abrir);
                toggle.setText((abrir ? "▼" : "▶") + "  Método utilizado");
                tarjeta.revalidate();
                tarjeta.repaint();
                Container padre = tarjeta.getParent();
                if (padre != null) {
                    padre.revalidate();
                }
            }
        });
        cuerpo.add(Box.createVerticalStrut(12));
        cuerpo.add(izquierda(toggle));
        cuerpo.add(Box.createVerticalStrut(6));
        cuerpo.add(izquierda(metodo));

        tarjeta.add(cuerpo, BorderLayout.CENTER);
        return tarjeta;
    }

    private static JTextArea areaTexto(String texto, Color color, Color fondo) {
        JTextArea area = new JTextArea(texto);
        area.setEditable(false);
        area.setFont(Tema.mono(13));
        area.setForeground(color);
        area.setSelectionColor(Tema.SELECCION);
        area.setBorder(BorderFactory.createEmptyBorder());
        if (fondo != null) {
            area.setBackground(fondo);
        } else {
            area.setOpaque(false);
        }
        return area;
    }

    private static JComponent izquierda(JComponent c) {
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        return c;
    }

    private static String escapar(String t) {
        return t.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    /**
     * Panel vertical que acompaña el ancho del visor cuando hay lugar y
     * habilita el desplazamiento horizontal cuando el contenido es más ancho.
     */
    private static class PanelVertical extends JPanel implements Scrollable {
        PanelVertical() {
            setOpaque(false);
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle r, int o, int d) {
            return 18;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle r, int o, int d) {
            return o == SwingConstants.VERTICAL ? r.height - 40 : r.width - 40;
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return getParent() != null && getParent().getWidth() > getPreferredSize().width;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }
    }

    /**
     * Crea y muestra la ventana desde el hilo de Swing.
     *
     * @author Yleana Torres
     * @param p_titulo título de la ventana
     * @param p_texto  texto que se muestra en la hoja
     */
    public static void mostrar(String p_titulo, String p_texto) {
        SwingUtilities.invokeLater(() -> new VentanaSalida(p_titulo, p_texto).setVisible(true));
    }

    /**
     * Crea y muestra la ventana de secciones desde el hilo de Swing.
     *
     * @param p_titulo    título de la ventana
     * @param p_secciones pasos de la demostración
     */
    public static void mostrar(String p_titulo, List<Seccion> p_secciones) {
        SwingUtilities.invokeLater(() -> new VentanaSalida(p_titulo, p_secciones).setVisible(true));
    }
}
