package vista.paneles;

import controlador.BibliotecaController;
import vista.componentes.Boton;
import vista.componentes.Listado;
import vista.componentes.Tarjeta;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

// panel que muestra los listados de socios, libros y titulos que pide la consigna
// el texto llega armado desde el modelo y aca solamente se muestra
public class PanelListados extends PanelSeccion {

    private final Listado listado = new Listado();
    private final List<Boton> botones = new ArrayList<>();
    private Supplier<String> actual;

    public PanelListados(BibliotecaController controlador) {
        add(encabezado("Listados", "Socios, libros y títulos, con el formato que pide la consigna"), BorderLayout.NORTH);

        JPanel fila = new JPanel();
        fila.setOpaque(false);
        fila.setLayout(new BoxLayout(fila, BoxLayout.X_AXIS));
        agregarOpcion(fila, "Socios", controlador::listaDeSocios);
        agregarOpcion(fila, "Libros", controlador::listaDeLibros);
        agregarOpcion(fila, "Títulos", controlador::listaDeTitulos);
        fila.add(Box.createHorizontalGlue());
        elegir(botones.get(0), controlador::listaDeSocios);

        Tarjeta hoja = new Tarjeta(Color.WHITE, 30);
        hoja.setBorder(BorderFactory.createEmptyBorder(24, 30, 24, 30));
        hoja.add(listado, BorderLayout.CENTER);

        JPanel centro = new JPanel(new BorderLayout(0, 18));
        centro.setOpaque(false);
        centro.add(fila, BorderLayout.NORTH);
        centro.add(hoja, BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);
    }

    // crea una opcion para consultar un listado y actualiza la vista al seleccionarla
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

    // marca el boton elegido y guarda la consulta que corresponde
    private void elegir(Boton elegido, Supplier<String> consulta) {
        actual = consulta;
        for (Boton b : botones) {
            b.setEstilo(b == elegido ? Boton.Estilo.PRINCIPAL : Boton.Estilo.CLARO);
        }
    }

    @Override
    void refrescar() {
        listado.setTexto(actual.get());
    }
}