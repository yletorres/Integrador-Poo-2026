package vista.paneles;

import controlador.BibliotecaController;
import vista.Tema;
import vista.componentes.Boton;
import vista.componentes.Listado;
import vista.componentes.Tarjeta;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Panel que muestra los listados de socios, libros y títulos que pide la
 * consigna. El texto llega armado desde el modelo y acá solamente se
 * muestra.
 *
 * @author Yleana Torres
 * @version TP7 - 2026
 */
class PanelListados extends PanelSeccion {

    /**
     * Componente que muestra el texto del listado elegido.
     */
    private final Listado listado = new Listado();

    /**
     * Botones para elegir el listado a consultar.
     */
    private final List<Boton> botones = new ArrayList<>();

    /**
     * Consulta que corresponde al listado elegido actualmente.
     */
    private Supplier<String> actual;

    /**
     * Construye el panel con los botones de cada listado y la hoja donde se
     * muestra el texto.
     *
     * @param controlador controlador que provee los listados armados
     */
    PanelListados(BibliotecaController controlador) {
        add(encabezado("Listados", "Socios, libros y títulos, con el formato que pide la consigna"), BorderLayout.NORTH);

        JPanel fila = new JPanel();
        fila.setOpaque(false);
        fila.setLayout(new BoxLayout(fila, BoxLayout.X_AXIS));
        agregarOpcion(fila, "Socios", controlador::listaDeSocios);
        agregarOpcion(fila, "Libros", controlador::listaDeLibros);
        agregarOpcion(fila, "Títulos", controlador::listaDeTitulos);
        fila.add(Box.createHorizontalGlue());
        elegir(botones.get(0), controlador::listaDeSocios);

        Tarjeta hoja = new Tarjeta(Tema.TINTA_TERRACOTA, 30);
        hoja.setBorder(BorderFactory.createEmptyBorder(24, 30, 24, 30));
        hoja.add(listado, BorderLayout.CENTER);

        JPanel centro = new JPanel(new BorderLayout(0, 18));
        centro.setOpaque(false);
        centro.add(fila, BorderLayout.NORTH);
        centro.add(hoja, BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);
    }

    /**
     * Crea una opción para consultar un listado y actualiza la vista al
     * seleccionarla.
     *
     * @param fila     panel donde se agrega el botón
     * @param nombre   texto del botón
     * @param consulta consulta que devuelve el texto del listado
     */
    private void agregarOpcion(JPanel fila, String nombre, Supplier<String> consulta) {
        Boton b = new Boton(nombre);
        b.addActionListener(e -> {
            elegir(b, consulta);
            refrescar();
        });
        if (!botones.isEmpty()) fila.add(Box.createHorizontalStrut(10));
        botones.add(b);
        fila.add(b);
    }

    /**
     * Marca el botón elegido y guarda la consulta que corresponde.
     *
     * @param elegido  botón seleccionado
     * @param consulta consulta del listado elegido
     */
    private void elegir(Boton elegido, Supplier<String> consulta) {
        actual = consulta;
        for (Boton b : botones) {
            b.setEstilo(b == elegido ? Boton.Estilo.PRINCIPAL : Boton.Estilo.CLARO);
        }
    }

    /**
     * Actualiza el texto mostrado con el listado elegido.
     */
    @Override
    void refrescar() {
        listado.setTexto(actual.get());
    }
}