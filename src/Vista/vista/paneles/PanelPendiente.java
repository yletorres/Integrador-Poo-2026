package vista.paneles;

import vista.Tema;
import vista.componentes.Tarjeta;

import javax.swing.*;
import java.awt.*;

// pantalla provisoria para las secciones que todavia no tienen su formulario
class PanelPendiente extends PanelSeccion {

    PanelPendiente(String titulo, String bajada, String aviso) {
        add(encabezado(titulo, bajada), BorderLayout.NORTH);
        Tarjeta tarjeta = new Tarjeta(Tema.PANEL, 30);
        tarjeta.setBorder(BorderFactory.createEmptyBorder(28, 32, 28, 32));
        JLabel texto = new JLabel(aviso);
        texto.setFont(Tema.sans(Font.PLAIN, 16));
        texto.setForeground(Tema.TEXTO_SUAVE);
        texto.setVerticalAlignment(SwingConstants.TOP);
        tarjeta.add(texto, BorderLayout.CENTER);
        add(tarjeta, BorderLayout.CENTER);
    }

    @Override void refrescar() { }
}
