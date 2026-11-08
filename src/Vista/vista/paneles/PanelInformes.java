package vista.paneles;

import biblioteca.excepciones.LibroNoPrestadoException;
import controlador.BibliotecaController;
import vista.Tema;
import vista.componentes.Boton;
import vista.componentes.Etiqueta;
import vista.componentes.Listado;
import vista.componentes.Tarjeta;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Panel con las consultas de la consigna sobre socios, préstamos y docentes
 * responsables. Recibe los resultados del controlador y se encarga de
 * mostrarlos.
 *
 * @author Yleana Torres
 * @version TP7 - 2026
 */
class PanelInformes extends PanelSeccion {

    /**
     * Controlador que provee los resultados de las consultas.
     */
    private final BibliotecaController controlador;

    /**
     * Componente que muestra el listado de docentes responsables.
     */
    private final Listado docentes = new Listado();

    /**
     * Construye el panel con las tarjetas de cantidad de socios, búsqueda de
     * libros y docentes responsables.
     *
     * @param controlador controlador que provee los resultados de las consultas
     */
    PanelInformes(BibliotecaController controlador) {
        this.controlador = controlador;
        add(encabezado("Informes", "Las consultas que necesita la administración de la biblioteca"), BorderLayout.NORTH);
        docentes.setColorTexto(Tema.TEXTO_CLARO);

        JPanel arriba = new JPanel(new GridLayout(1, 2, 22, 0));
        arriba.setOpaque(false);
        arriba.add(tarjetaCantidad());
        arriba.add(tarjetaQuienTiene());

        JPanel cuerpo = new JPanel(new GridLayout(2, 1, 0, 22));
        cuerpo.setOpaque(false);
        cuerpo.add(arriba);
        cuerpo.add(tarjetaDocentes());
        add(cuerpo, BorderLayout.CENTER);
    }

    /**
     * Crea la tarjeta que muestra la cantidad de socios según su tipo.
     *
     * @return la tarjeta de cantidad de socios
     */
    private Tarjeta tarjetaCantidad() {
        JLabel numero = new JLabel("–");
        numero.setFont(Tema.sans(Font.BOLD, 56));
        numero.setForeground(Tema.TEXTO_CLARO);
        JLabel detalle = new JLabel("Elegí un tipo de socio.");
        detalle.setFont(Tema.sans(Font.PLAIN, 15));
        detalle.setForeground(Tema.conAlfa(Tema.TEXTO_CLARO, 190));

        Boton estudiantes = new Boton("Estudiantes", Boton.Estilo.SALIDA);
        Boton docentesBtn = new Boton("Docentes", Boton.Estilo.SALIDA);
        estudiantes.addActionListener(e -> {
            numero.setText(String.valueOf(controlador.cantidadDeSociosPorTipo("Estudiante")));
            detalle.setText("estudiantes");
            estudiantes.setEstilo(Boton.Estilo.PRINCIPAL);
            docentesBtn.setEstilo(Boton.Estilo.SALIDA);
        });
        docentesBtn.addActionListener(e -> {
            numero.setText(String.valueOf(controlador.cantidadDeSociosPorTipo("Docente")));
            detalle.setText("docentes");
            docentesBtn.setEstilo(Boton.Estilo.PRINCIPAL);
            estudiantes.setEstilo(Boton.Estilo.SALIDA);
        });

        JPanel resultado = new JPanel();
        resultado.setOpaque(false);
        resultado.setLayout(new BoxLayout(resultado, BoxLayout.X_AXIS));
        numero.setAlignmentY(0.6f);
        detalle.setAlignmentY(0.6f);
        resultado.add(numero);
        resultado.add(Box.createHorizontalStrut(14));
        resultado.add(detalle);
        resultado.add(Box.createHorizontalGlue());

        return tarjeta("¿Cuántos socios hay de cada tipo?", fila(estudiantes, docentesBtn), resultado);
    }

    /**
     * Crea la tarjeta que consulta quién tiene un libro. Muestra si el libro
     * está prestado o si sigue en la biblioteca.
     *
     * @return la tarjeta de búsqueda de libros
     */
    private Tarjeta tarjetaQuienTiene() {
        JTextField campo = new JTextField();
        campo.setOpaque(false);
        campo.setBorder(BorderFactory.createEmptyBorder());
        campo.setFont(Tema.sans(Font.PLAIN, 15));
        campo.setForeground(Tema.TEXTO);
        campo.setCaretColor(Tema.CAFE_OSCURO);
        Tarjeta caja = new Tarjeta(Tema.TINTA_TERRACOTA, 40);
        caja.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        caja.add(campo, BorderLayout.CENTER);

        Etiqueta estado = new Etiqueta();
        estado.setVisible(false);
        estado.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel resultado = new JLabel("Escribí el título tal como figura en el catálogo.");
        resultado.setFont(Tema.sans(Font.PLAIN, 16));
        resultado.setForeground(Tema.conAlfa(Tema.TEXTO_CLARO, 170));
        resultado.setAlignmentX(Component.LEFT_ALIGNMENT);

        // busca el libro y muestra quién lo tiene o si sigue en la biblioteca
        ActionListener buscar = e -> {
            String titulo = campo.getText().trim();
            resultado.setForeground(Tema.TEXTO_CLARO);
            if (titulo.isEmpty()) {
                estado.setVisible(false);
                resultado.setText("Escribí el título del libro.");
                return;
            }
            try {
                String socio = controlador.quienTieneElLibro(titulo);
                estado.mostrar("Prestado", Tema.TERRACOTA, Tema.TINTA_TERRACOTA);
                resultado.setText("Lo tiene " + socio + ".");
            } catch (LibroNoPrestadoException ex) {
                estado.mostrar("En biblioteca", Tema.CARAMELO, Tema.TEXTO);
                resultado.setText(ex.getMessage()); // mensaje del modelo, sin reescribir
            } catch (IllegalArgumentException ex) {
                // el título no existe en la biblioteca
                estado.setVisible(false);
                resultado.setText(ex.getMessage());
            }
        };
        campo.addActionListener(buscar);
        Boton boton = new Boton("Buscar");
        boton.addActionListener(buscar);

        JPanel entrada = new JPanel(new BorderLayout(12, 0));
        entrada.setOpaque(false);
        entrada.add(caja, BorderLayout.CENTER);
        entrada.add(boton, BorderLayout.EAST);

        JPanel salida = new JPanel();
        salida.setOpaque(false);
        salida.setLayout(new BoxLayout(salida, BoxLayout.Y_AXIS));
        salida.add(estado);
        salida.add(Box.createVerticalStrut(10));
        salida.add(resultado);

        return tarjeta("¿Quién tiene este libro?", entrada, salida);
    }

    /**
     * Crea la tarjeta con los docentes responsables.
     *
     * @return la tarjeta de docentes que nunca adeudaron libros
     */
    private Tarjeta tarjetaDocentes() {
        return tarjeta("Docentes que nunca adeudaron libros", null, docentes);
    }

    /**
     * Arma una tarjeta con título, controles opcionales y contenido.
     *
     * @param titulo    título de la tarjeta
     * @param controles componente con los controles, o null si no tiene
     * @param contenido componente con el contenido principal
     * @return la tarjeta armada
     */
    private static Tarjeta tarjeta(String titulo, JComponent controles, JComponent contenido) {
        Tarjeta t = new Tarjeta(Tema.PANEL, 30);
        t.setLayout(new BorderLayout(0, 14));
        t.setBorder(BorderFactory.createEmptyBorder(22, 26, 20, 26));
        JLabel encabezadoTarjeta = subtitulo(titulo);
        encabezadoTarjeta.setForeground(Tema.TEXTO_CLARO);
        t.add(encabezadoTarjeta, BorderLayout.NORTH);
        JPanel cuerpo = new JPanel(new BorderLayout(0, 14));
        cuerpo.setOpaque(false);
        if (controles != null) cuerpo.add(controles, BorderLayout.NORTH);
        cuerpo.add(contenido, BorderLayout.CENTER);
        t.add(cuerpo, BorderLayout.CENTER);
        return t;
    }

    /**
     * Acomoda varios botones en una misma fila con separación entre ellos.
     *
     * @param botones componentes a acomodar
     * @return el panel con la fila armada
     */
    private static JPanel fila(JComponent... botones) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        for (int i = 0; i < botones.length; i++) {
            if (i > 0) p.add(Box.createHorizontalStrut(10));
            p.add(botones[i]);
        }
        p.add(Box.createHorizontalGlue());
        return p;
    }

    /**
     * Actualiza el listado de docentes responsables con los datos actuales.
     */
    @Override
    void refrescar() {
        docentes.setTexto(controlador.listaDeDocentesResponsables());
    }
}