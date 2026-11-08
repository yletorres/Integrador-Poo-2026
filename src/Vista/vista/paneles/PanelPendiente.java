package vista.paneles;

import vista.Tema;
import vista.componentes.Tarjeta;

import javax.swing.*;
import java.awt.*;

/**
 * Pantalla provisoria para las secciones que todavía no tienen su
 * formulario.
 *
 * @author Yleana Torres
 * @version TP7 - 2026
 */
class PanelPendiente extends PanelSeccion {

    /**
     * Crea la pantalla provisoria con un encabezado y un aviso.
     *
     * @param titulo título de la sección
     * @param bajada texto descriptivo que aparece debajo del título
     * @param aviso  mensaje que se muestra dentro de la tarjeta
     */
    PanelPendiente(String titulo, String bajada, String aviso) {
        add(encabezado(titulo, bajada), BorderLayout.NORTH);
        Tarjeta tarjeta = new Tarjeta(Tema.TINTA_TERRACOTA, 30);
        tarjeta.setBorder(BorderFactory.createEmptyBorder(28, 32, 28, 32));
        JLabel texto = new JLabel(aviso);
        texto.setFont(Tema.sans(Font.PLAIN, 16));
        texto.setForeground(Tema.TEXTO_SUAVE);
        texto.setVerticalAlignment(SwingConstants.TOP);
        tarjeta.add(texto, BorderLayout.CENTER);
        add(tarjeta, BorderLayout.CENTER);
    }

    /**
     * No hace nada, porque la pantalla provisoria no tiene datos para
     * actualizar.
     */
    @Override
    void refrescar() { }
}