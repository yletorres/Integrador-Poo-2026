package vista.componentes;

import vista.Tema;

import javax.swing.*;
import java.awt.*;

// muestra los listados tal como llegan del controlador para conservar su formato
public class Listado extends JScrollPane {

    private final JTextArea area;

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

    public void setColorTexto(Color color) {
        area.setForeground(color);
        area.setCaretColor(color);
    }

    public void setTexto(String texto) {
        area.setText(texto);
        area.setCaretPosition(0);
    }
}
