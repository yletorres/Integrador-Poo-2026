package vista.componentes;

import vista.Tema;

import javax.swing.*;
import java.awt.*;

/**
 * Muestra los listados tal como llegan del controlador para conservar su
 * formato.
 *
 * @author Yleana Torres
 * @version TP7 - 2026
 */
public class Listado extends JScrollPane {

    /**
     * Área de texto de solo lectura donde se muestra el listado.
     */
    private final JTextArea area;

    /**
     * Crea el listado con un área de texto de fuente monoespaciada y las
     * barras de desplazamiento del tema.
     */
    public Listado() {
        super(new JTextArea());
        area = (JTextArea) getViewport().getView();
        area.setEditable(false);
        area.setOpaque(false);
        area.setLineWrap(false);
        area.setFont(Tema.mono(14));
        area.setForeground(Tema.TEXTO);
        area.setCaretColor(Tema.CAFE_OSCURO);
        area.setSelectionColor(Tema.SELECCION);
        area.setBorder(BorderFactory.createEmptyBorder());
        Tema.estilizar(this);
    }

    /**
     * Establece el color del texto y del cursor del listado.
     *
     * @param color color que se aplica al texto y al cursor
     */
    public void setColorTexto(Color color) {
        area.setForeground(color);
        area.setCaretColor(color);
    }

    /**
     * Establece el texto que muestra el listado y vuelve al principio.
     *
     * @param texto texto del listado
     */
    public void setTexto(String texto) {
        area.setText(texto);
        area.setCaretPosition(0);
    }
}