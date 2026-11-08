package vista.paneles;

import biblioteca.excepciones.LibroNoPrestadoException;
import controlador.BibliotecaController;
import vista.Tema;
import vista.componentes.Boton;
import vista.formularios.FormularioPrestamo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Pantalla de préstamos: formulario para prestar y tabla de libros prestados
 * para devolver.
 *
 * @author Yleana Torres
 * @version TP7 - 2026
 */
public class PanelPrestamos extends PanelSeccion {

    /**
     * Controlador que provee los datos y las operaciones de préstamos.
     */
    private final BibliotecaController controlador;

    /**
     * Modelo que administra los datos mostrados en la tabla.
     */
    private final DefaultTableModel modeloTabla;

    /**
     * Tabla que muestra los libros en estado de préstamo.
     */
    private final JTable tabla;

    /**
     * Botón que devuelve el libro de la fila seleccionada.
     */
    private final Boton btnDevolver;

    /**
     * Construye la pantalla con el formulario de préstamo, el panel de
     * devolución y la tabla de préstamos activos.
     *
     * @param controlador controlador que provee los datos de la biblioteca
     */
    public PanelPrestamos(BibliotecaController controlador) {
        this.controlador = controlador;

        add(encabezado("Préstamos y Devoluciones",
                "Gestión de retiros de libros y registro de devoluciones"), BorderLayout.NORTH);

        JPanel contenedorPrincipal = new JPanel(new BorderLayout(16, 16));
        contenedorPrincipal.setOpaque(false);

        // 1. Panel Superior: formulario de préstamo y panel de devolución lado a lado
        JPanel panelFormularios = new JPanel(new GridLayout(1, 2, 16, 0));
        panelFormularios.setOpaque(false);

        FormularioPrestamo formPrestamo = new FormularioPrestamo(controlador, this::refrescar);
        panelFormularios.add(formPrestamo);

        btnDevolver = new Boton("Devolver libro seleccionado", Boton.Estilo.OSCURO);
        btnDevolver.setEnabled(false); // se habilita al seleccionar una fila
        btnDevolver.addActionListener(e -> devolverSeleccionado());
        panelFormularios.add(crearPanelDevolucion(btnDevolver));

        contenedorPrincipal.add(panelFormularios, BorderLayout.NORTH);

        // 2. Panel Inferior: tabla de préstamos
        String[] columnas = {"Título del Libro", "DNI", "Socio", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabla de solo lectura
            }
        };

        tabla = new JTable(modeloTabla);
        configurarTabla();
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // una sola fila a la vez
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                btnDevolver.setEnabled(tabla.getSelectedRow() >= 0);
            }
        });

        JScrollPane scrollTabla = new JScrollPane(tabla);
        Tema.estilizar(scrollTabla);
        scrollTabla.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Tema.LINEA),
                " Libros en Estado de Préstamo / Consulta ",
                0, 0,
                Tema.sans(Font.BOLD, 14),
                Tema.TEXTO
        ));

        contenedorPrincipal.add(scrollTabla, BorderLayout.CENTER);

        // Agregar todo al contenido de PanelSeccion
        add(contenedorPrincipal, BorderLayout.CENTER);
    }

    /**
     * Configura la apariencia visual de la tabla de préstamos: altura de las
     * filas, colores, tipografía, selección y cabecera.
     */
    private void configurarTabla() {
        tabla.setRowHeight(40);
        tabla.setFont(Tema.sans(Font.PLAIN, 14));
        tabla.setForeground(Tema.TEXTO);
        tabla.setBackground(Tema.TINTA_TERRACOTA);
        tabla.setSelectionBackground(Tema.SELECCION);
        tabla.setSelectionForeground(Tema.TEXTO);
        tabla.setShowVerticalLines(false);
        tabla.setGridColor(Tema.LINEA);
        tabla.setFillsViewportHeight(true);

        tabla.getTableHeader().setFont(Tema.sans(Font.BOLD, 13));
        tabla.getTableHeader().setBackground(Tema.FONDO);
        tabla.getTableHeader().setForeground(Tema.TEXTO);
        tabla.getTableHeader().setReorderingAllowed(false);
    }

    /**
     * Arma el recuadro de devolución con una indicación y el botón.
     *
     * @param boton botón que registra la devolución
     * @return el panel con la indicación y el botón
     */
    private static JPanel crearPanelDevolucion(Boton boton) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Tema.LINEA),
                " Registrar Devolución ",
                0, 0,
                Tema.sans(Font.BOLD, 14),
                Tema.TEXTO
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;

        JLabel indicacion = new JLabel("Seleccioná un libro de la tabla y presioná el botón.");
        indicacion.setFont(Tema.sans(Font.PLAIN, 14));
        indicacion.setForeground(Tema.TEXTO);
        panel.add(indicacion, gbc);

        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(boton, gbc);
        return panel;
    }

    /**
     * Devuelve el libro de la fila seleccionada. Si el libro no estaba
     * prestado, muestra el mensaje de la excepción.
     */
    private void devolverSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccioná un libro de la tabla para devolverlo.",
                    "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String titulo = (String) modeloTabla.getValueAt(fila, 0);
        int dni = (Integer) modeloTabla.getValueAt(fila, 1);
        try {
            // se devuelve el ejemplar que tiene ese socio, aunque haya otros del mismo título
            controlador.devolverLibro(titulo, dni);
            JOptionPane.showMessageDialog(this, "¡Devolución registrada correctamente!",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            refrescar();
        } catch (LibroNoPrestadoException ex) {
            // Cumpliendo consigna: muestra ÚNICAMENTE ex.getMessage() sin alterarlo
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Atención", JOptionPane.WARNING_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Libro inexistente", JOptionPane.WARNING_MESSAGE);
            refrescar();
        }
    }

    /**
     * Actualiza la tabla con los préstamos activos de la biblioteca
     * seleccionada.
     */
    @Override
    public void refrescar() {
        modeloTabla.setRowCount(0);
        try {
            for (Object[] fila : controlador.filasPrestamosActivos()) {
                modeloTabla.addRow(fila); // título, DNI, socio y estado
            }
        } catch (IllegalStateException ex) {
            modeloTabla.setRowCount(0);
        }
    }
}