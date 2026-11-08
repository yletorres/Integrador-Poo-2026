package vista.paneles;

import controlador.BibliotecaController;
import vista.Tema;
import vista.componentes.Avatar;
import vista.componentes.Boton;
import vista.componentes.BotonMenu;
import vista.componentes.FondoOscuro;
import vista.componentes.Selector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;



// ventana principal con el menu lateral y las distintas pantallas del sistema
public class VentanaPrincipal extends JFrame {

    private final BibliotecaController controlador;
    private final CardLayout cartas = new CardLayout();
    private final JPanel contenido = new JPanel(cartas);
    private final Selector selector = new Selector();
    private final Map<String, BotonMenu> botones = new LinkedHashMap<>();
    private final Map<String, PanelSeccion> pantallas = new HashMap<>();

    public VentanaPrincipal(BibliotecaController controlador) {
        super("Gestión de Biblioteca");
        this.controlador = controlador;

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 700));
        setSize(1260, 800);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Tema.FONDO);
        setLayout(new BorderLayout());

        contenido.setBackground(Tema.FONDO);
        add(crearMenuLateral(), BorderLayout.WEST);
        add(contenido, BorderLayout.CENTER);

        agregar("Inicio", new PanelInicio(controlador, this::nombreDeBiblioteca));
        agregar("Libros", new PanelLibros(controlador));
        agregar("Socios", new PanelSocios(controlador));
        agregar("Préstamos", new PanelPendiente("Préstamos", "Retiros y devoluciones",
                "Acá vas a poder registrar un préstamo y una devolución."));
        agregar("Listados", new PanelListados(controlador));
        agregar("Informes", new PanelInformes(controlador));

        mostrar("Inicio");
    }

    // devuelve el nombre de la biblioteca seleccionada
    private String nombreDeBiblioteca() {
        String n = (String) selector.getSelectedItem();
        return n == null ? "Sin biblioteca" : n;
    }

    // agrega una pantalla al contenido y la guarda para poder encontrarla despues
    private void agregar(String nombre, PanelSeccion pantalla) {
        pantallas.put(nombre, pantalla);
        contenido.add(pantalla, nombre);
    }

    // cambia de pantalla y actualiza sus datos antes de mostrarla
    public void mostrar(String nombre) {
        cartas.show(contenido, nombre);
        botones.forEach((clave, boton) -> boton.setActivo(clave.equals(nombre)));
        pantallas.get(nombre).refrescar();
    }

    // actualiza la pantalla que se encuentra visible
    private void refrescarVisible() {
        for (PanelSeccion p : pantallas.values()) {
            if (p.isVisible()) p.refrescar();
        }
    }

    // menu lateral 
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
        menu.add(conMargen(selector, 44));
        menu.add(Box.createVerticalStrut(26));

        // crea los botones del menu y los conecta con cada pantalla
        for (String nombre : new String[]{"Inicio", "Libros", "Socios", "Préstamos", "Listados", "Informes"}) {
            BotonMenu boton = new BotonMenu(nombre);
            boton.addActionListener(e -> mostrar(nombre));
            botones.put(nombre, boton);
            menu.add(boton);
        }

        menu.add(Box.createVerticalGlue());
        Boton salir = new Boton("Salir");
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

    // crea una etiqueta centrada con la fuente y el color indicados
    private static JLabel centrado(String texto, Font fuente, Color color) {
        JLabel l = new JLabel(texto);
        l.setFont(fuente);
        l.setForeground(color);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        return l;
    }

    // agrega margen a los costados de un componente para que no toque los bordes del menu
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
