package vista.paneles;

import controlador.BibliotecaController;
import vista.Tema;
import vista.componentes.Avatar;
import vista.componentes.Boton;
import vista.componentes.BotonMenu;
import vista.componentes.FondoOscuro;
import vista.componentes.Icono;
import vista.componentes.Selector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Ventana principal con el menú lateral y las distintas pantallas del
 * sistema.
 *
 * @author Yleana Torres
 * @version TP7 - 2026
 */
public class VentanaPrincipal extends JFrame {

    /**
     * Controlador que provee los datos y las operaciones de la biblioteca.
     */
    private final BibliotecaController controlador;

    /**
     * Administrador de diseño que permite cambiar entre pantallas.
     */
    private final CardLayout cartas = new CardLayout();

    /**
     * Panel que contiene las pantallas del sistema.
     */
    private final JPanel contenido = new JPanel(cartas);

    /**
     * Selector de la biblioteca activa.
     */
    private final Selector selector = new Selector();

    /**
     * Botones del menú lateral, indexados por el nombre de su pantalla.
     */
    private final Map<String, BotonMenu> botones = new LinkedHashMap<>();

    /**
     * Pantallas del sistema, indexadas por su nombre.
     */
    private final Map<String, PanelSeccion> pantallas = new HashMap<>();

    /**
     * Crea la ventana principal con el menú lateral y todas las pantallas.
     *
     * @param controlador controlador que provee los datos de la biblioteca
     */
    public VentanaPrincipal(BibliotecaController controlador) {
        super("Gestión de Biblioteca");
        this.controlador = controlador;

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 700));
        setSize(1260, 800);
        setLocationRelativeTo(null);
        setIconImages(iconosDeVentana());
        getContentPane().setBackground(Tema.FONDO);
        setLayout(new BorderLayout());

        contenido.setBackground(Tema.FONDO);
        add(crearMenuLateral(), BorderLayout.WEST);
        add(contenido, BorderLayout.CENTER);

        agregar("Inicio", new PanelInicio(controlador, this::nombreDeBiblioteca));
        agregar("Libros", new PanelTabla("Libros", "Todos los ejemplares del catálogo",
                new String[]{"Título", "Edición", "Editorial", "Año", "Prestado"}, 0, controlador::filasLibros));
        agregar("Socios", new PanelTabla("Socios", "Docentes y estudiantes registrados",
                new String[]{"D.N.I.", "Nombre", "Tipo", "Libros prestados"}, 1, controlador::filasSocios));
        agregar("Préstamos", new PanelPrestamos(controlador));

        agregar("Libros", new PanelLibros(controlador));
        agregar("Socios", new PanelSocios(controlador));
        agregar("Listados", new PanelListados(controlador));
        agregar("Informes", new PanelInformes(controlador));

        mostrar("Inicio");
    }

    /**
     * Devuelve el nombre de la biblioteca seleccionada.
     *
     * @return el nombre de la biblioteca activa, o "Sin biblioteca" si no hay ninguna
     */
    private String nombreDeBiblioteca() {
        String n = (String) selector.getSelectedItem();
        return n == null ? "Sin biblioteca" : n;
    }

    /**
     * Agrega una pantalla al contenido y la guarda para poder encontrarla
     * después.
     *
     * @param nombre   nombre con el que se identifica la pantalla
     * @param pantalla pantalla a agregar
     */
    private void agregar(String nombre, PanelSeccion pantalla) {
        pantallas.put(nombre, pantalla);
        contenido.add(pantalla, nombre);
    }

    /**
     * Cambia de pantalla y actualiza sus datos antes de mostrarla.
     *
     * @param nombre nombre de la pantalla a mostrar
     */
    public void mostrar(String nombre) {
        cartas.show(contenido, nombre);
        botones.forEach((clave, boton) -> boton.setActivo(clave.equals(nombre)));
        pantallas.get(nombre).refrescar();
    }

    /**
     * Actualiza la pantalla que se encuentra visible.
     */
    private void refrescarVisible() {
        for (PanelSeccion p : pantallas.values()) {
            if (p.isVisible()) p.refrescar();
        }
    }

    /**
     * Crea el menú lateral con el selector de biblioteca, los botones de
     * cada pantalla y el botón de salida.
     *
     * @return el componente con el menú lateral
     */
    private JComponent crearMenuLateral() {
        JPanel menu = new FondoOscuro(40);
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(BorderFactory.createEmptyBorder(34, 0, 26, 0));

        Avatar avatar = new Avatar(96);
        avatar.setAlignmentX(Component.CENTER_ALIGNMENT);
        menu.add(avatar);
        menu.add(Box.createVerticalStrut(14));
        menu.add(centrado("Administrador", Tema.sans(Font.BOLD, 16), Color.WHITE));
        menu.add(Box.createVerticalStrut(2));
        menu.add(centrado("Gestión de Biblioteca", Tema.sans(Font.PLAIN, 12), Tema.TEXTO_CLARO));
        menu.add(Box.createVerticalStrut(28));

        menu.add(centrado("Biblioteca activa", Tema.sans(Font.PLAIN, 12), Tema.TEXTO_CLARO));
        menu.add(Box.createVerticalStrut(6));

        // carga las bibliotecas disponibles en el selector
        for (String nombre : controlador.nombresDeBibliotecas()) {
            selector.addItem(nombre);
        }

        // cambia la biblioteca activa y actualiza los datos de la pantalla actual
        selector.addActionListener(e -> {
            controlador.seleccionarBiblioteca((String) selector.getSelectedItem());
            refrescarVisible();
        });

        //start de info
        if (selector.getItemCount() > 0 ){
            controlador.seleccionarBiblioteca((String) selector.getSelectedItem());
            refrescarVisible();
        }

        menu.add(conMargen(selector, 44));
        menu.add(Box.createVerticalStrut(26));

        // crea los botones del menu y los conecta con cada pantalla
        for (String nombre : new String[]{"Inicio", "Libros", "Socios", "Préstamos", "Listados", "Informes"}) {
            BotonMenu boton = new BotonMenu(nombre, Icono.para(nombre, 20));
            boton.addActionListener(e -> mostrar(nombre));
            botones.put(nombre, boton);
            menu.add(boton);
        }

        menu.add(Box.createVerticalGlue());
        Boton salir = new Boton("Salir", Boton.Estilo.SALIDA);
        salir.setIcon(new Icono(Icono.Tipo.SALIR, 18));
        salir.setIconTextGap(10);
        salir.addActionListener(e ->
                dispatchEvent(new WindowEvent(VentanaPrincipal.this, WindowEvent.WINDOW_CLOSING)));
        menu.add(conMargen(salir, 48));

        // deja un espacio alrededor del menu para que no quede pegado a los bordes de la ventana
        JPanel marco = new JPanel(new BorderLayout());
        marco.setBackground(Tema.FONDO);
        marco.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 0));
        marco.setPreferredSize(new Dimension(268, 0));
        marco.add(menu, BorderLayout.CENTER);
        return marco;
    }

    /**
     * Crea las imágenes del avatar en distintos tamaños para usarlas como
     * íconos de la ventana.
     *
     * @return la lista de imágenes para los íconos
     */
    private static java.util.List<Image> iconosDeVentana() {
        java.util.List<Image> iconos = new ArrayList<>();
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
     * Crea una etiqueta centrada con la fuente y el color indicados.
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
     * Agrega margen a los costados de un componente para que no toque los
     * bordes del menú.
     *
     * @param c    componente al que se le agrega el margen
     * @param alto alto máximo del panel resultante
     * @return el panel que contiene al componente con su margen
     */
    private static JPanel conMargen(JComponent c, int alto) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setBorder(BorderFactory.createEmptyBorder(0, 22, 0, 22));
        p.add(c, BorderLayout.CENTER);
        p.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, alto));
        return p;
    }
}