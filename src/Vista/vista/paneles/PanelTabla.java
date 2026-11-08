package vista.paneles;

import vista.Tema;
import vista.componentes.Tarjeta;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.function.Supplier;

/**
 * Pantalla que muestra en una tabla los datos que entrega el controlador.
 * Sirve como base para las pantallas de libros y de socios: la tabla es de
 * solo lectura y se vuelve a cargar cada vez que se muestra la pantalla.
 *
 * @author Torres Yleana
 * @version TP7 - 2026
 */
class PanelTabla extends PanelSeccion {

    /** Modelo de la tabla: guarda las filas que se muestran. */
    private final DefaultTableModel modelo;

    /** Origen de los datos: devuelve las filas actuales cada vez que se lo consulta. */
    private final Supplier<List<Object[]>> origen;

    /**
     * Constructor de la pantalla: arma el encabezado y la tabla con su estilo.
     *
     * @param p_titulo       Título de la pantalla.
     * @param p_bajada       Texto que se muestra debajo del título.
     * @param p_columnas     Nombres de las columnas de la tabla.
     * @param p_columnaAncha Posición de la columna a la que se le da más ancho.
     * @param p_origen       Proveedor de las filas, normalmente un método del controlador.
     */
    PanelTabla(String p_titulo, String p_bajada, String[] p_columnas, int p_columnaAncha,
               Supplier<List<Object[]>> p_origen) {
        this.origen = p_origen;
        add(encabezado(p_titulo, p_bajada), BorderLayout.NORTH);

        modelo = new DefaultTableModel(p_columnas, 0) {
            /**
             * Impide editar las celdas: la tabla es solo de consulta.
             *
             * @param p_fila    Fila de la celda.
             * @param p_columna Columna de la celda.
             * @return false siempre.
             */
            @Override public boolean isCellEditable(int p_fila, int p_columna) { return false; }
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
        for (int i = 0; i < p_columnas.length; i++) {
            tabla.getColumnModel().getColumn(i).setPreferredWidth(i == p_columnaAncha ? 360 : 120);
        }

        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            /**
             * Dibuja una celda del cuerpo de la tabla. Resalta con el color de acento
             * las celdas que indican que un libro está prestado ("Si").
             *
             * @param p_tabla Tabla que se dibuja.
             * @param p_valor Valor de la celda.
             * @param p_sel   true si la fila está seleccionada.
             * @param p_foco  true si la celda tiene el foco.
             * @param p_fila  Fila de la celda.
             * @param p_col   Columna de la celda.
             * @return el componente con el que se dibuja la celda.
             */
            @Override public Component getTableCellRendererComponent(JTable p_tabla, Object p_valor, boolean p_sel, boolean p_foco, int p_fila, int p_col) {
                super.getTableCellRendererComponent(p_tabla, p_valor, p_sel, false, p_fila, p_col);
                boolean si = "Si".equals(p_valor); // resalta con el color de acento las celdas que indican que un libro esta prestado
                setOpaque(true);
                setBackground(p_sel ? Tema.SELECCION : Tema.PANEL);
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
            /**
             * Dibuja una celda del encabezado de la tabla.
             *
             * @param p_tabla Tabla que se dibuja.
             * @param p_valor Texto de la columna.
             * @param p_sel   Indica si está seleccionada (no se usa en el encabezado).
             * @param p_foco  Indica si tiene el foco (no se usa en el encabezado).
             * @param p_fila  Fila de la celda (no se usa en el encabezado).
             * @param p_col   Columna de la celda.
             * @return el componente con el que se dibuja el encabezado.
             */
            @Override public Component getTableCellRendererComponent(JTable p_tabla, Object p_valor, boolean p_sel, boolean p_foco, int p_fila, int p_col) {
                super.getTableCellRendererComponent(p_tabla, p_valor, false, false, p_fila, p_col);
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

    /**
     * Vuelve a cargar la tabla con los datos actuales del controlador.
     * Se ejecuta cada vez que se muestra la pantalla.
     */
    @Override
    void refrescar() {
        modelo.setRowCount(0);
        for (Object[] fila : origen.get()) {
            modelo.addRow(fila);
        }
    }
}