package vista.paneles;

import controlador.BibliotecaController;
import vista.Tema;
import vista.componentes.Anillo;
import vista.componentes.Avatar;
import vista.componentes.GraficoBarras;
import vista.componentes.Tarjeta;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

// pantalla principal con un resumen de la biblioteca y el estado de los prestamos
// los datos se obtienen del controlador y se actualizan cada vez que se refresca el panel
class PanelInicio extends PanelSeccion {

    private static final int MAX_BARRAS = 7;

    private final BibliotecaController controlador;
    private final Supplier<String> bibliotecaActiva;

    private final JLabel saludo = new JLabel();
    private final JLabel fecha = new JLabel();
    private final JLabel nombre = new JLabel();
    private final JLabel detalleBiblioteca = new JLabel();
    private final JLabel resumen = new JLabel();
    private final Anillo anilloResumen = new Anillo(Tema.conAlfa(Color.WHITE, 110), Tema.CAFE_OSCURO, Tema.TINTA_TERRACOTA, 11f);
    private final GraficoBarras grafico = new GraficoBarras();
    private final Anillo anilloSocios = anilloEstado();
    private final Anillo anilloDocentes = anilloEstado();
    private final JLabel detalleSocios = new JLabel();
    private final JLabel detalleDocentes = new JLabel();

    PanelInicio(BibliotecaController controlador, Supplier<String> bibliotecaActiva) {
        this.controlador = controlador;
        this.bibliotecaActiva = bibliotecaActiva;

        add(encabezado(saludo, fecha), BorderLayout.NORTH);
        saludo.setFont(Tema.sans(Font.BOLD, 36));

        JPanel superior = new JPanel(new GridLayout(1, 2, 22, 0));
        superior.setOpaque(false);
        superior.setPreferredSize(new Dimension(0, 170));
        superior.add(tarjetaBiblioteca());
        superior.add(tarjetaResumen());

        JPanel inferior = new JPanel(new GridBagLayout());
        inferior.setOpaque(false);
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.BOTH;
        gc.weighty = 1;
        gc.gridx = 0; gc.weightx = 0.66; gc.insets = new Insets(0, 0, 0, 22);
        inferior.add(tarjetaGrafico(), gc);
        gc.gridx = 1; gc.weightx = 0.34; gc.insets = new Insets(0, 0, 0, 0);
        inferior.add(tarjetaEstado(), gc);

        JPanel cuerpo = new JPanel(new BorderLayout(0, 22));
        cuerpo.setOpaque(false);
        cuerpo.add(superior, BorderLayout.NORTH);
        cuerpo.add(inferior, BorderLayout.CENTER);
        add(cuerpo, BorderLayout.CENTER);
    }

    // bloques 
    private Tarjeta tarjetaBiblioteca() {
        Tarjeta t = Tarjeta.degradada(36, 36, 80, 36);
        t.setLayout(new BorderLayout(22, 0));
        t.setBorder(BorderFactory.createEmptyBorder(22, 28, 22, 40));
        t.add(centradoVertical(new Avatar(96)), BorderLayout.WEST);

        JLabel rotulo = texto("Biblioteca", 13, Font.BOLD);
        nombre.setFont(Tema.sans(Font.BOLD, 24));
        nombre.setForeground(Tema.TINTA_TERRACOTA);
        detalleBiblioteca.setFont(Tema.sans(Font.PLAIN, 14));
        detalleBiblioteca.setForeground(Tema.TINTA_TERRACOTA);
        t.add(columna(rotulo, nombre, detalleBiblioteca), BorderLayout.CENTER);
        return t;
    }

    private Tarjeta tarjetaResumen() {
        Tarjeta t = Tarjeta.degradada(80, 36, 36, 36);
        t.setLayout(new BorderLayout(22, 0));
        t.setBorder(BorderFactory.createEmptyBorder(18, 34, 18, 28));
        anilloResumen.setPreferredSize(new Dimension(118, 118));
        t.add(anilloResumen, BorderLayout.WEST);

        JLabel rotulo = texto("Libros en circulación", 15, Font.BOLD);
        resumen.setFont(Tema.sans(Font.PLAIN, 14));
        resumen.setForeground(Tema.TINTA_TERRACOTA);
        t.add(columna(rotulo, resumen), BorderLayout.CENTER);
        return t;
    }

    private Tarjeta tarjetaGrafico() {
        Tarjeta t = new Tarjeta(Tema.PANEL, 32);
        t.setLayout(new BorderLayout(0, 10));
        t.setBorder(BorderFactory.createEmptyBorder(22, 28, 20, 24));
        t.add(subtituloClaro("Libros prestados por socio"), BorderLayout.NORTH);
        grafico.setAyuda("Registrá un préstamo desde la sección Préstamos.");
        t.add(grafico, BorderLayout.CENTER);
        return t;
    }

    private Tarjeta tarjetaEstado() {
        Tarjeta t = new Tarjeta(Tema.PANEL, 32);
        t.setLayout(new BorderLayout(0, 12));
        t.setBorder(BorderFactory.createEmptyBorder(22, 26, 20, 22));
        t.add(subtituloClaro("Estado de préstamos"), BorderLayout.NORTH);

        JPanel filas = new JPanel(new GridLayout(2, 1, 0, 8));
        filas.setOpaque(false);
        filas.add(filaEstado(anilloSocios, "Socios con préstamos", detalleSocios));
        filas.add(filaEstado(anilloDocentes, "Docentes entre los socios", detalleDocentes));
        t.add(filas, BorderLayout.CENTER);
        return t;
    }

    // crea los anillos que muestran las proporciones del resumen
    private static Anillo anilloEstado() {
        Anillo a = new Anillo(Tema.conAlfa(Tema.CARAMELO, 70), Tema.CARAMELO, Tema.TEXTO_CLARO, 10f);
        a.setPreferredSize(new Dimension(84, 84));
        return a;
    }

    // acomoda el anillo y los textos de cada indicador
    private static JPanel filaEstado(Anillo anillo, String titulo, JLabel detalle) {
        JPanel p = new JPanel(new BorderLayout(16, 0));
        p.setOpaque(false);
        p.add(anillo, BorderLayout.WEST);
        JLabel t = new JLabel(titulo);
        t.setFont(Tema.sans(Font.BOLD, 15));
        t.setForeground(Tema.TEXTO_CLARO);
        detalle.setFont(Tema.sans(Font.PLAIN, 14));
        detalle.setForeground(Tema.conAlfa(Tema.TEXTO_CLARO, 190));
        p.add(columna(t, detalle), BorderLayout.CENTER);
        return p;
    }

    private static JLabel subtituloClaro(String texto) {
        JLabel l = subtitulo(texto);
        l.setForeground(Tema.TEXTO_CLARO);
        return l;
    }

    private static JPanel centradoVertical(JComponent c) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);
        p.add(c);
        return p;
    }

    // crea una etiqueta con el formato usado en las tarjetas
    private static JLabel texto(String s, int tamanio, int estilo) {
        JLabel l = new JLabel(s);
        l.setFont(Tema.sans(estilo, tamanio));
        l.setForeground(Tema.TINTA_TERRACOTA);
        return l;
    }

    // acomoda varios componentes en una columna y los centra en altura
    private static JPanel columna(JComponent... partes) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(Box.createVerticalGlue());
        for (int i = 0; i < partes.length; i++) {
            if (i > 0) p.add(Box.createVerticalStrut(5));
            partes[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            p.add(partes[i]);
        }
        p.add(Box.createVerticalGlue());
        return p;
    }

    // datos 
    @Override
    void refrescar() {
        int hora = LocalTime.now().getHour();
        saludo.setText(hora < 12 ? "Buenos días" : hora < 20 ? "Buenas tardes" : "Buenas noches");
        String f = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE d 'de' MMMM 'de' yyyy",
                Locale.forLanguageTag("es-AR")));
        fecha.setText(Character.toUpperCase(f.charAt(0)) + f.substring(1));
        nombre.setText(bibliotecaActiva.get());

        List<Object[]> libros = controlador.filasLibros();
        List<Object[]> socios = controlador.filasSocios();

        int totalLibros = libros.size();
        int prestados = 0;
        for (Object[] l : libros) if ("Si".equals(l[4])) prestados++;
        double proporcionLibros = totalLibros == 0 ? 0 : prestados / (double) totalLibros;

        anilloResumen.setValor(proporcionLibros, porcentaje(proporcionLibros));
        resumen.setText(prestados + " de " + totalLibros + " libros están prestados");
        detalleBiblioteca.setText(totalLibros + " libros · " + socios.size() + " socios");

        int conPrestamos = 0;
        for (Object[] s : socios) if (cantidad(s) > 0) conPrestamos++;
        double proporcionSocios = socios.isEmpty() ? 0 : conPrestamos / (double) socios.size();
        anilloSocios.setValor(proporcionSocios, porcentaje(proporcionSocios));
        detalleSocios.setText(socios.isEmpty() ? "Sin actividad" : conPrestamos + " de " + socios.size());

        int docentes = 0;
        for (Object[] sc : socios) if ("Docente".equals(sc[2])) docentes++;
        double proporcionDocentes = socios.isEmpty() ? 0 : docentes / (double) socios.size();
        anilloDocentes.setValor(proporcionDocentes, porcentaje(proporcionDocentes));
        detalleDocentes.setText(socios.isEmpty() ? "Sin actividad" : docentes + " de " + socios.size());

        List<Object[]> ordenados = new ArrayList<>(socios);
        ordenados.sort((a, b) -> Integer.compare(cantidad(b), cantidad(a)));
        List<String> nombres = new ArrayList<>();
        List<Integer> valores = new ArrayList<>();
        for (int i = 0; i < Math.min(MAX_BARRAS, ordenados.size()); i++) {
            nombres.add(String.valueOf(ordenados.get(i)[1]));
            valores.add(cantidad(ordenados.get(i)));
        }
        grafico.setDatos(nombres, valores, "Ningún socio tiene libros prestados.");
    }

    // obtiene la cantidad de libros prestados de la cuarta columna de filasSocios
    private static int cantidad(Object[] fila) {
        return ((Number) fila[3]).intValue();
    }

    // convierte una proporcion en un porcentaje para mostrar en pantalla
    private static String porcentaje(double proporcion) {
        return Math.round(proporcion * 100) + "%";
    }
}
