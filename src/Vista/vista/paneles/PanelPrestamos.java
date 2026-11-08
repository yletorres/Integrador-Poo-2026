package vista.paneles;

import controlador.BibliotecaController;
import vista.formularios.FormularioDevolucion;
import vista.formularios.FormularioPrestamo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelPrestamos extends PanelSeccion {

    private final BibliotecaController controlador;
    private final DefaultTableModel modeloTabla;

    public PanelPrestamos(BibliotecaController controlador) {
        this.controlador = controlador;

        add(encabezado("Préstamos y Devoluciones",
                "Gestión de retiros de libros y registro de devoluciones"), BorderLayout.NORTH);

        JPanel contenedorPrincipal = new JPanel(new BorderLayout(16, 16));
        contenedorPrincipal.setOpaque(false);

        // 1. Panel Superior: Formularios lado a lado
        JPanel panelFormularios = new JPanel(new GridLayout(1, 2, 16, 0));
        panelFormularios.setOpaque(false);

        FormularioPrestamo formPrestamo = new FormularioPrestamo(controlador, this::refrescar);
        FormularioDevolucion formDevolucion = new FormularioDevolucion(controlador, this::refrescar);

        panelFormularios.add(formPrestamo);
        panelFormularios.add(formDevolucion);

        contenedorPrincipal.add(panelFormularios, BorderLayout.NORTH);

        // 2. Panel Inferior: Tabla de préstamos
        String[] columnas = {"Título del Libro", "DNI / Socio", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabla de solo lectura
            }
        };

        JTable tabla = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setBorder(BorderFactory.createTitledBorder(" Libros en Estado de Préstamo / Consulta "));

        contenedorPrincipal.add(scrollTabla, BorderLayout.CENTER);

        // Agregar todo al contenido de PanelSeccion
        add(contenedorPrincipal, BorderLayout.CENTER);
    }

    @Override
    public void refrescar() {
        modeloTabla.setRowCount(0);
        try {
            for (Object[] fila : controlador.filasLibros()) {
                String titulo = (String) fila[0];   // Título del libro
                String prestado = (String) fila[4]; // Estado ("Si" o "No")

                if ("Si".equalsIgnoreCase(prestado)) {
                    String quienLoTiene;
                    try {
                        quienLoTiene = controlador.quienTieneElLibro(titulo);
                    } catch (Exception e) {
                        quienLoTiene = "Desconocido";
                    }
                    modeloTabla.addRow(new Object[]{titulo, quienLoTiene, "Prestado"});
                }
            }
        } catch (IllegalStateException ex) {
            modeloTabla.setRowCount(0);
        }
    }
}
