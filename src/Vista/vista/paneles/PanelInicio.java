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

/**
 * Pantalla principal con un resumen de la biblioteca y el estado de los
 * préstamos. Los datos se obtienen del controlador y se actualizan cada vez
 * que se refresca el panel.
 *
 * @author Yleana Torres
 * @version TP7 - 2026
 */
class PanelInicio extends PanelSeccion {

    /**
     * Cantidad máxima de socios que se muestran en el gráfico de barras.
     */
    private static final int MAX_BARRAS = 7;

    /**
     * Controlador que provee los datos de la biblioteca.
     */
    private final BibliotecaController controlador;

    /**
     * Proveedor del nombre de la biblioteca activa.
     */
    private final Supplier<String> bibliotecaActiva;

    /**
     * Saludo según la hora del día.
     */
    private final JLabel saludo = new JLabel();

    /**
     * Fecha actual en formato largo.
     */
    private final JLabel fecha = new JLabel();

    /**
     * Nombre de la biblioteca activa.
     */
    private final JLabel nombre = new JLabel();

    /**
     * Cantidad de libros y de socios de la biblioteca.
     */
    private final JLabel detalleBiblioteca = new JLabel();

    /**
     * Texto con la cantidad de libros prestados sobre el total.
     */
    private final JLabel resumen = new JLabel();

    /**
     * Anillo que muestra la proporción de libros prestados.
     */
    private final Anillo anilloResumen = new Anillo(Tema.conAlfa(Tema.TINTA_TERRACOTA, 110), Tema.CAFE_OSCURO, Tema.TINTA_TERRACOTA, 11f);

    /**
     * Gráfico de barras con los libros prestados por socio.
     */
    private final GraficoBarras grafico = new GraficoBarras();

    /**
     * Anillo que muestra la proporción de socios con préstamos.
     */
    private final Anillo anilloSocios = anilloEstado();

    /**
     * Anillo que muestra la proporción de docentes entre los socios.
     */
    private final Anillo anilloDocentes = anilloEstado();

    /**
     * Detalle de los socios con préstamos.
     */
    private final JLabel detalleSocios = new JLabel();

    /**
     * Detalle de los docentes entre los socios.
     */
    private final JLabel detalleDocentes = new JLabel();

    /**
     * Construye la pantalla de inicio con las tarjetas de resumen, el
     * gráfico y el estado de los préstamos.
     *
     * @param controlador      controlador que provee los datos de la biblioteca
     * @param bibliotecaActiva proveedor del nombre de la biblioteca activa
     */
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

    /**
     * Crea la tarjeta con el logo, el nombre y el detalle de la biblioteca.
     *
     * @return la tarjeta de la biblioteca
     */
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

    /**
     * Crea la tarjeta con el anillo de libros en circulación.
     *
     * @return la tarjeta del resumen
     */
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

    /**
     * Crea la tarjeta con el gráfico de libros prestados por socio.
     *
     * @return la tarjeta del gráfico
     */
    private Tarjeta tarjetaGrafico() {
        Tarjeta t = new Tarjeta(Tema.PANEL, 32);
        t.setLayout(new BorderLayout(0, 10));
        t.setBorder(BorderFactory.createEmptyBorder(22, 28, 20, 24));
        t.add(subtituloClaro("Libros prestados por socio"), BorderLayout.NORTH);
        grafico.setAyuda("Registrá un préstamo desde la sección Préstamos.");
        t.add(grafico, BorderLayout.CENTER);
        return t;
    }

    /**
     * Crea la tarjeta con los anillos del estado de los préstamos.
     *
     * @return la tarjeta del estado
     */
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

    /**
     * Crea los anillos que muestran las proporciones del resumen.
     *
     * @return el anillo configurado
     */
    private static Anillo anilloEstado() {
        Anillo a = new Anillo(Tema.conAlfa(Tema.CARAMELO, 70), Tema.CARAMELO, Tema.TEXTO_CLARO, 10f);
        a.setPreferredSize(new Dimension(84, 84));
        return a;
    }

    /**
     * Acomoda el anillo y los textos de cada indicador.
     *
     * @param anillo  anillo del indicador
     * @param titulo  título del indicador
     * @param detalle etiqueta con el detalle del indicador
     * @return el panel con el indicador armado
     */
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

    /**
     * Crea un subtítulo con el color claro para las tarjetas oscuras.
     *
     * @param texto texto del subtítulo
     * @return la etiqueta con el subtítulo
     */
    private static JLabel subtituloClaro(String texto) {
        JLabel l = subtitulo(texto);
        l.setForeground(Tema.TEXTO_CLARO);
        return l;
    }

    /**
     * Centra un componente en altura dentro de un panel.
     *
     * @param c componente a centrar
     * @return el panel que contiene al componente
     */
    private static JPanel centradoVertical(JComponent c) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);
        p.add(c);
        return p;
    }

    /**
     * Crea una etiqueta con el formato usado en las tarjetas.
     *
     * @param s       texto de la etiqueta
     * @param tamanio tamaño de la fuente
     * @param estilo  estilo de la fuente
     * @return la etiqueta creada
     */
    private static JLabel texto(String s, int tamanio, int estilo) {
        JLabel l = new JLabel(s);
        l.setFont(Tema.sans(estilo, tamanio));
        l.setForeground(Tema.TINTA_TERRACOTA);
        return l;
    }

    /**
     * Acomoda varios componentes en una columna y los centra en altura.
     *
     * @param partes componentes a acomodar
     * @return el panel con la columna armada
     */
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

    /**
     * Actualiza el saludo, la fecha, los anillos y el gráfico con los datos
     * actuales de la biblioteca.
     */
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

    /**
     * Obtiene la cantidad de libros prestados de la cuarta columna de
     * filasSocios.
     *
     * @param fila fila de datos de un socio
     * @return la cantidad de libros prestados por el socio
     */
    private static int cantidad(Object[] fila) {
        return ((Number) fila[3]).intValue();
    }

    /**
     * Convierte una proporción en un porcentaje para mostrar en pantalla.
     *
     * @param proporcion valor entre 0 y 1
     * @return el porcentaje como texto, por ejemplo "45%"
     */
    private static String porcentaje(double proporcion) {
        return Math.round(proporcion * 100) + "%";
    }
}