package vista.paneles;

import vista.Tema;
import vista.componentes.Tarjeta;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.function.Supplier;

// pantalla que muestra los datos del controlador en una tabla y sirve como base para libros y socios
class PanelTabla extends PanelSeccion {

    private final DefaultTableModel modelo;
    private final Supplier<List<Object[]>> origen;

    PanelTabla(String titulo, String bajada, String[] columnas, int columnaAncha,
                Supplier<List<Object[]>> origen) {
        this.origen = origen;
        add(encabezado(titulo, bajada), BorderLayout.NORTH);

        modelo = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int fila, int columna) { return false; }
        };
        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(46);
        tabla.setShowGrid(false);
        tabla.setIntercellSpacing(new Dimension(0, 0));
        tabla.setOpaque(false);
        tabla.setFillsViewportHeight(true);
        tabla.setSelectionBackground(Tema.SELECCION);
        tabla.setSelectionForeground(Tema.TEXTO);
        
        // ajusta el ancho de las columnas para darle mas espacio a la que lo necesita
        for (int i = 0; i < columnas.length; i++) {
            tabla.getColumnModel().getColumn(i).setPreferredWidth(i == columnaAncha ? 360 : 120);
        }

        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object valor, boolean sel, boolean foco, int fila, int col) {
                super.getTableCellRendererComponent(t, valor, sel, false, fila, col);
                boolean si = "Si".equals(valor); // resalta con el color de acento las celdas que indican que un libro esta prestado
                setOpaque(true);
                setBackground(sel ? Tema.SELECCION : Tema.PANEL);
                setForeground(si ? Tema.ACENTO_TEXTO : Tema.TEXTO);
                setFont(Tema.sans(si ? Font.BOLD : Font.PLAIN, 14));
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, Tema.LINEA),
                        BorderFactory.createEmptyBorder(0, 18, 0, 18)));
                return this;
            }
        });

        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.getTableHeader().setPreferredSize(new Dimension(0, 44));
        tabla.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object valor, boolean sel, boolean foco, int fila, int col) {
                super.getTableCellRendererComponent(t, valor, false, false, fila, col);
                setHorizontalAlignment(LEFT);
                setOpaque(true);
                setBackground(Tema.PANEL);
                setForeground(Tema.TEXTO_SUAVE);
                setFont(Tema.sans(Font.BOLD, 13));
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 3, 0, Tema.TERRACOTA),
                        BorderFactory.createEmptyBorder(0, 18, 0, 18)));
                return this;
            }
        });

        JScrollPane scroll = new JScrollPane(tabla);
        Tema.estilizar(scroll);

        Tarjeta tarjeta = new Tarjeta(Tema.PANEL, 32);
        tarjeta.setBorder(BorderFactory.createEmptyBorder(18, 18, 20, 18));
        tarjeta.add(scroll, BorderLayout.CENTER);
        add(tarjeta, BorderLayout.CENTER);
    }

    @Override
    void refrescar() {
        modelo.setRowCount(0);
        // vuelve a cargar la tabla con los datos actuales del controlador
        for (Object[] fila : origen.get()) {
            modelo.addRow(fila);
        }
    }
}
