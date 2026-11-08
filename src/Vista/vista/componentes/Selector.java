package vista.componentes;

import vista.Tema;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
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
                        g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                        int cx = getWidth() / 2 - 4, cy = getHeight() / 2;
                        g2.drawPolyline(new int[]{cx - 5, cx, cx + 5}, new int[]{cy - 2, cy + 3, cy - 2}, 3);
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
                g2.setColor(Tema.CAFE);
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), c.getHeight(), c.getHeight());
                g2.dispose();
                super.paint(g, c);
                
                // muestra el borde terracota cuando se selecciona con el teclado
                if (c.hasFocus()) { // hover visible por teclado
                    Graphics2D f = Tema.suavizar(g);
                    f.setColor(Tema.TERRACOTA);
                    f.setStroke(new BasicStroke(2f));
                    f.drawRoundRect(1, 1, c.getWidth() - 3, c.getHeight() - 3, c.getHeight(), c.getHeight());
                    f.dispose();
                }
            }
            @Override protected ComboPopup createPopup() {
                BasicComboPopup lista = new BasicComboPopup(comboBox) {
                    @Override protected void configurePopup() {
                        super.configurePopup();
                        setOpaque(false);
                        setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
                    }
                    @Override protected void configureList() {
                        super.configureList();
                        list.setOpaque(false);
                        list.setBackground(Tema.conAlfa(Tema.CAFE, 0));
                    }
                    @Override protected void configureScroller() {
                        super.configureScroller();
                        scroller.setOpaque(false);
                        scroller.getViewport().setOpaque(false);
                        scroller.setBorder(BorderFactory.createEmptyBorder());
                    }
                    @Override protected Rectangle computePopupBounds(int px, int py, int pw, int ph) {
                        Rectangle r = super.computePopupBounds(px, py, pw, ph);
                        r.y += 4;
                        return r;
                    }
                    @Override protected void paintComponent(Graphics g) {
                        Graphics2D g2 = Tema.suavizar(g);
                        g2.setColor(Tema.CAFE);
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                        g2.setColor(Tema.conAlfa(Tema.TEXTO_CLARO, 40));
                        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                        g2.dispose();
                    }
                };
                lista.setLightWeightPopupEnabled(true);
                return lista;
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
            private boolean resaltado;
            @Override protected void paintComponent(Graphics g) {
                if (resaltado) {
                    Graphics2D g2 = Tema.suavizar(g);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                    g2.dispose();
                }
                super.paintComponent(g);
            }
            @Override public Component getListCellRendererComponent(JList<?> lista, Object valor,
                                                                    int indice, boolean seleccionado, boolean foco) {
                super.getListCellRendererComponent(lista, valor, indice, seleccionado, false);
                setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 12));
                setFont(Tema.sans(Font.PLAIN, 14));
                boolean enLista = indice >= 0;
                setOpaque(false);
                resaltado = enLista && seleccionado;
                // cambia los colores segun si el elemento esta seleccionado
                setForeground(enLista && seleccionado ? Tema.TINTA_TERRACOTA : Tema.TEXTO_CLARO);
                setBackground(enLista && seleccionado ? Tema.TERRACOTA : Tema.CAFE);
                return this;
            }
        });
        try { // intenta aplicar el borde del menú desplegable con la paleta propia
            Object popup = getAccessibleContext().getAccessibleChild(0);
            if (popup instanceof JComponent) {
                ((JComponent) popup).setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
            }
        } catch (RuntimeException ignorada) { }
    }
}
