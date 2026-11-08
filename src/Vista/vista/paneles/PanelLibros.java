
package vista.paneles;

import vista.Tema;
import vista.componentes.Tarjeta;
import controlador.BibliotecaController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

/**
 * Panel encargado de mostrar y gestionar los libros de una biblioteca.
 * Permite registrar nuevos libros, eliminarlos y visualizar sus datos
 * mediante una tabla.
 *
 * La comunicación con la lógica del sistema se realiza a través de
 * la interfaz BibliotecaController.
 *
 * @author Azula Iara
 * @version TP7 - 2026
 */
public class PanelLibros extends PanelSeccion {

    /** Controlador que permite realizar las operaciones sobre los libros. */
    private final BibliotecaController controlador;
    /** Modelo que administra los datos mostrados en la tabla. */
    private final DefaultTableModel modelo;
    /** Tabla que presenta los libros de la biblioteca seleccionada. */
    private final JTable tabla;

    /**
     * Construye el panel de libros, configura la tabla y agrega los botones
     * para registrar y eliminar libros.
     *
     * @param controlador controlador encargado de gestionar las operaciones
     *                    relacionadas con los libros
     */
    public PanelLibros(BibliotecaController controlador) {
        this.controlador = controlador;

        add(encabezado("Libros","Administrá los ejemplares del catálogo"), BorderLayout.NORTH);

        modelo = new DefaultTableModel(new String[]{"ID", "Título", "Edición", "Editorial", "Año", "Prestado"},0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        configurarTabla();

        JScrollPane scroll = new JScrollPane(tabla);
        Tema.estilizar(scroll);

        Tarjeta tarjeta = new Tarjeta(Tema.PANEL, 32);
        tarjeta.setBorder(BorderFactory.createEmptyBorder(18, 18, 20, 18));
        tarjeta.add(scroll, BorderLayout.CENTER);

        JButton btnNuevo = crearBoton(" + Nuevo libro ");
        JButton btnEliminar = crearBoton("Eliminar");

        btnNuevo.addActionListener(e -> nuevoLibro());
        btnEliminar.addActionListener(e -> eliminarLibro());

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        acciones.setOpaque(false);
        acciones.add(btnNuevo);
        acciones.add(btnEliminar);

        JPanel contenido = new JPanel(new BorderLayout(0, 15));
        contenido.setOpaque(false);
        contenido.add(acciones, BorderLayout.NORTH);
        contenido.add(tarjeta, BorderLayout.CENTER);

        add(contenido, BorderLayout.CENTER);

        refrescar();
    }

    /**
     * Crea y configura un botón para las acciones del panel.
     *
     * @param texto texto que se mostrará en el botón
     * @return botón configurado
     */
    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(Tema.sans(Font.BOLD, 14));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

    /**
     * Configura la apariencia de la tabla de libros.
     *
     * Establece la altura de las filas, los colores, el renderizado
     * de las celdas y oculta la columna del identificador sin eliminarla
     * del modelo de datos.
     */
    private void configurarTabla() {
        tabla.setRowHeight(46);
        tabla.setShowGrid(false);
        tabla.setIntercellSpacing(new Dimension(0, 0));
        tabla.setSelectionBackground(Tema.SELECCION);
        tabla.setSelectionForeground(Tema.TEXTO);
        tabla.setFillsViewportHeight(true);

        tabla.setDefaultRenderer(Object.class,
                new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable t, Object valor, boolean sel,boolean foco, int fila, int columna) {

                        super.getTableCellRendererComponent(t, valor, sel, false, fila, columna);

                        setOpaque(true);
                        setBackground(sel ? Tema.SELECCION : Tema.PANEL);
                        boolean prestado = "Si".equals(valor);

                        setForeground(prestado ? Tema.ACENTO_TEXTO : Tema.TEXTO);
                        setFont(Tema.sans(prestado ? Font.BOLD : Font.PLAIN, 14));

                        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Tema.LINEA),
                                BorderFactory.createEmptyBorder(0, 18, 0, 18)));

                        return this;
                    }
                });

        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.getTableHeader().setPreferredSize(new Dimension(0, 44));

        // Ocultar la columna ID sin eliminarla del modelo.
        tabla.getColumnModel().removeColumn(tabla.getColumnModel().getColumn(0));

        tabla.getColumnModel().getColumn(0).setPreferredWidth(360);
    }

    /**
     * Solicita los datos necesarios para registrar un libro nuevo.
     *
     * Valida que los campos estén completos, que la edición y el año
     * sean valores numéricos positivos y comunica el resultado al usuario.
     *
     * Si los datos son válidos, solicita al controlador que registre el libro
     * y actualiza la tabla.
     */
    private void nuevoLibro() {
        JTextField titulo = new JTextField();
        JTextField edicion = new JTextField();
        JTextField editorial = new JTextField();
        JTextField anio = new JTextField();

        Object[] campos = {"Título:", titulo, "Edición:", edicion, "Editorial:", editorial, "Año:", anio};

        int opcion = JOptionPane.showConfirmDialog(this, campos,"Registrar libro", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (opcion != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            String nombre = titulo.getText().trim();
            String nombreEditorial = editorial.getText().trim();

            if (nombre.isEmpty() || edicion.getText().trim().isEmpty() || nombreEditorial.isEmpty() || anio.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,"Completá todos los campos.");
                return;
            }

            int numeroEdicion = Integer.parseInt(edicion.getText().trim());
            int numeroAnio = Integer.parseInt(anio.getText().trim());

            if (numeroEdicion <= 0 || numeroAnio <= 0) {
                JOptionPane.showMessageDialog(this,"La edición y el año deben ser mayores que cero.");
                return;
            }

            controlador.nuevoLibro(nombre, numeroEdicion, nombreEditorial, numeroAnio);

            refrescar();

            JOptionPane.showMessageDialog(this,"Libro registrado correctamente.");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,"La edición y el año deben ser valores numéricos.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,"No se pudo registrar el libro: " + ex.getMessage());
        }
    }

    /**
     * Elimina el libro seleccionado de la biblioteca.
     *
     * Verifica que exista una fila seleccionada, solicita confirmación
     * al usuario y comunica la operación al controlador.
     * Finalmente, actualiza la tabla.
     */
    private void eliminarLibro() {
        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this,"Seleccioná el libro que deseas eliminar.");
            return;
        }

        int filaModelo = tabla.convertRowIndexToModel(fila);
        int idLibro = ((Number) modelo.getValueAt(filaModelo, 0)).intValue();

        String titulo = modelo.getValueAt(filaModelo, 1).toString();

        int opcion = JOptionPane.showConfirmDialog(this,"¿Estás seguro de que deseas eliminar el libro \"" + titulo + "\"?",
                "Eliminar libro", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            controlador.eliminarLibro(idLibro);

            refrescar();

            JOptionPane.showMessageDialog(this,"Libro eliminado correctamente.");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,"No se pudo eliminar el libro: " + ex.getMessage());
        }
    }

    /**
     * Actualiza el contenido de la tabla con los libros de la biblioteca
     * seleccionada.
     *
     * Primero elimina las filas existentes y luego incorpora las filas
     * devueltas por el controlador.
     */
    @Override
    void refrescar() {
        modelo.setRowCount(0);

        List<Object[]> filas = controlador.filasLibrosConId();

        for (Object[] fila : filas) {
            modelo.addRow(fila);
        }
    }
}
