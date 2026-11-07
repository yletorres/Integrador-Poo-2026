package vista.componentes;

import vista.Tema;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;

// selector redondeado para elegir la biblioteca activa
public class Selector extends JComboBox<String> {

    public Selector() {
        setUI(new BasicComboBoxUI() {
            @Override protected JButton createArrowButton() {
                JButton flecha = new JButton() {
                    @Override public void paint(Graphics g) {
                        Graphics2D g2 = Tema.suavizar(g);
                        g2.setColor(Tema.TEXTO_CLARO);
                        int cx = getWidth() / 2, cy = getHeight() / 2;
                        g2.fillPolygon(new int[]{cx - 4, cx + 4, cx}, new int[]{cy - 2, cy - 2, cy + 3}, 3);
                        g2.dispose();
                    }
                };
                flecha.setBorder(BorderFactory.createEmptyBorder());
                flecha.setFocusable(false);
                flecha.setContentAreaFilled(false);
                return flecha;
            }
            @Override public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = Tema.suavizar(g);
                g2.setColor(Tema.CARBON_2);
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), c.getHeight(), c.getHeight());
                g2.dispose();
                super.paint(g, c);
                
                // muestra el borde menta cuando se selecciona con el teclado
                if (c.hasFocus()) { // hover visible por teclado
                    Graphics2D f = Tema.suavizar(g);
                    f.setColor(Tema.MENTA);
                    f.setStroke(new BasicStroke(2f));
                    f.drawRoundRect(1, 1, c.getWidth() - 3, c.getHeight() - 3, c.getHeight(), c.getHeight());
                    f.dispose();
                }
            }
            @Override public void paintCurrentValueBackground(Graphics g, Rectangle b, boolean foco) { }
            @Override public void paintCurrentValue(Graphics g, Rectangle b, boolean foco) {
                ListCellRenderer<? super Object> r = comboBox.getRenderer();
                Component c = r.getListCellRendererComponent(listBox, comboBox.getSelectedItem(), -1, false, false);
                currentValuePane.paintComponent(g, c, comboBox, b.x, b.y, b.width, b.height, false);
            }
        });
        setOpaque(false);
        setForeground(Tema.TEXTO_CLARO);
        setFont(Tema.sans(Font.PLAIN, 14));
        setBorder(BorderFactory.createEmptyBorder());
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(JList<?> lista, Object valor,
                                                                    int indice, boolean seleccionado, boolean foco) {
                super.getListCellRendererComponent(lista, valor, indice, seleccionado, false);
                setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 12));
                setFont(Tema.sans(Font.PLAIN, 14));
                boolean enLista = indice >= 0;
                setOpaque(enLista); // el valor visible se dibuja sobre el fondo redondeado
                // cambia los colores segun si el elemento esta seleccionado
                setForeground(enLista && seleccionado ? Tema.TINTA_MENTA : Tema.TEXTO_CLARO);
                setBackground(enLista && seleccionado ? Tema.MENTA : Tema.CARBON_2);
                return this;
            }
        });
        try { // intenta aplicar el borde del menú desplegable con la paleta propia
            Object popup = getAccessibleContext().getAccessibleChild(0);
            if (popup instanceof JComponent) {
                ((JComponent) popup).setBorder(BorderFactory.createLineBorder(Tema.CARBON_2));
            }
        } catch (RuntimeException ignorada) { }
    }
}
