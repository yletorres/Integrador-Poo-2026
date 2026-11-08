package vista.paneles;

import biblioteca.excepciones.LibroNoPrestadoException;
import controlador.BibliotecaController;
import vista.Tema;
import vista.componentes.Boton;
import vista.formularios.FormularioPrestamo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

// pantalla de prestamos: formulario para prestar y tabla de libros prestados para devolver
public class PanelPrestamos extends PanelSeccion {

    private final BibliotecaController controlador;
    private final DefaultTableModel modeloTabla;
    private final JTable tabla;
    private final Boton btnDevolver;

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
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // una sola fila a la vez
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                btnDevolver.setEnabled(tabla.getSelectedRow() >= 0);
            }
        });

        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setBorder(BorderFactory.createTitledBorder(" Libros en Estado de Préstamo / Consulta "));

        contenedorPrincipal.add(scrollTabla, BorderLayout.CENTER);

        // Agregar todo al contenido de PanelSeccion
        add(contenedorPrincipal, BorderLayout.CENTER);
    }

    // arma el recuadro de devolucion con una indicacion y el boton
    private static JPanel crearPanelDevolucion(Boton boton) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                " Registrar Devolución ",
                0, 0,
                Tema.sans(Font.BOLD, 14),
                Color.WHITE
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;

        JLabel indicacion = new JLabel("Seleccioná un libro de la tabla y presioná el botón.");
        indicacion.setForeground(Color.WHITE);
        panel.add(indicacion, gbc);

        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(boton, gbc);
        return panel;
    }

    // devuelve el libro de la fila seleccionada
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
        }
    }

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