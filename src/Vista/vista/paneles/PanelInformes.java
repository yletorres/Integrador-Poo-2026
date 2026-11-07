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


// panel con las consultas de la consigna sobre socios, prestamos y docentes responsables
// recibe los resultados del controlador y se encarga de mostrarlos
class PanelInformes extends PanelSeccion {

    private final BibliotecaController controlador;
    private final Listado docentes = new Listado();

    PanelInformes(BibliotecaController controlador) {
        this.controlador = controlador;
        add(encabezado("Informes", "Las consultas que necesita la administración de la biblioteca"), BorderLayout.NORTH);

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

    // cantidad de socios por tipo
    private Tarjeta tarjetaCantidad() {
        JLabel numero = new JLabel("–");
        numero.setFont(Tema.sans(Font.BOLD, 56));
        numero.setForeground(Tema.TEXTO);
        JLabel detalle = new JLabel("Elegí un tipo de socio.");
        detalle.setFont(Tema.sans(Font.PLAIN, 15));
        detalle.setForeground(Tema.TEXTO_SUAVE);

        Boton estudiantes = new Boton("Estudiantes");
        estudiantes.addActionListener(e -> {
            numero.setText(String.valueOf(controlador.cantidadDeSociosPorTipo("Estudiante")));
            detalle.setText("estudiantes");
        });
        Boton docentesBtn = new Boton("Docentes");
        docentesBtn.addActionListener(e -> {
            numero.setText(String.valueOf(controlador.cantidadDeSociosPorTipo("Docente")));
            detalle.setText("docentes");
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

    // consulta de quién tiene un libro
    private Tarjeta tarjetaQuienTiene() {
        JTextField campo = new JTextField();
        campo.setOpaque(false);
        campo.setBorder(BorderFactory.createEmptyBorder());
        campo.setFont(Tema.sans(Font.PLAIN, 15));
        campo.setForeground(Tema.TEXTO);
        campo.setCaretColor(Tema.CARBON);
        Tarjeta caja = new Tarjeta(Color.WHITE, 40);
        caja.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        caja.add(campo, BorderLayout.CENTER);

        Etiqueta estado = new Etiqueta();
        estado.setVisible(false);
        estado.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel resultado = new JLabel("Escribí el título tal como figura en el catálogo.");
        resultado.setFont(Tema.sans(Font.PLAIN, 16));
        resultado.setForeground(Tema.TEXTO_SUAVE);
        resultado.setAlignmentX(Component.LEFT_ALIGNMENT);

        // busca el libro y muestra quién lo tiene o si sigue en la biblioteca
        ActionListener buscar = e -> {
            String titulo = campo.getText().trim();
            resultado.setForeground(Tema.TEXTO);
            if (titulo.isEmpty()) {
                estado.setVisible(false);
                resultado.setText("Escribí el título del libro.");
                return;
            }
            try {
                String socio = controlador.quienTieneElLibro(titulo);
                estado.mostrar("Prestado", Tema.MENTA, Tema.TINTA_MENTA);
                resultado.setText("Lo tiene " + socio + ".");
            } catch (LibroNoPrestadoException ex) {
                estado.mostrar("En biblioteca", Tema.CARBON, Color.WHITE);
                resultado.setText(ex.getMessage()); // mensaje del modelo, sin reescribir
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

    // docentes responsables
    private Tarjeta tarjetaDocentes() {
        return tarjeta("Docentes que nunca adeudaron libros", null, docentes);
    }

    // utilidades 
    // arma una tarjeta con titulo, controles opcionales y contenido
    private static Tarjeta tarjeta(String titulo, JComponent controles, JComponent contenido) {
        Tarjeta t = new Tarjeta(Tema.PANEL, 30);
        t.setLayout(new BorderLayout(0, 14));
        t.setBorder(BorderFactory.createEmptyBorder(22, 26, 20, 26));
        t.add(subtitulo(titulo), BorderLayout.NORTH);
        JPanel cuerpo = new JPanel(new BorderLayout(0, 14));
        cuerpo.setOpaque(false);
        if (controles != null) cuerpo.add(controles, BorderLayout.NORTH);
        cuerpo.add(contenido, BorderLayout.CENTER);
        t.add(cuerpo, BorderLayout.CENTER);
        return t;
    }

    //acomoda varios botones en una misma fila con separacion entre ellos
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

    @Override
    void refrescar() {
        docentes.setTexto(controlador.listaDeDocentesResponsables());
    }
}
