
package vista.paneles;

import controlador.BibliotecaController;
import vista.Tema;
import vista.componentes.Tarjeta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelSocios extends PanelSeccion {

    private final BibliotecaController controlador;
    private final DefaultTableModel modelo;
    private final JTable tabla;

    public PanelSocios(BibliotecaController p_controlador) {
        this.controlador = p_controlador;

        setLayout(new BorderLayout(0, 20));

        add(encabezado("Socios","Administrá los docentes y estudiantes registrados."), BorderLayout.NORTH);

        modelo = new DefaultTableModel(new String[]{"D.N.I.", "Nombre", "Tipo", "Libros prestados"},0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        configurarTabla();

        Tarjeta tarjeta = new Tarjeta(Color.WHITE, 20);
        tarjeta.setLayout(new BorderLayout());
        tarjeta.add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        acciones.setOpaque(false);

        JButton nuevo = new JButton(" + Nuevo socio ");
        JButton modificar = new JButton("Modificar");
        JButton eliminar = new JButton("Eliminar");

        nuevo.addActionListener(e -> nuevoSocio());
        modificar.addActionListener(e -> modificarSocio());
        eliminar.addActionListener(e -> eliminarSocio());

        acciones.add(nuevo);
        acciones.add(modificar);
        acciones.add(eliminar);

        JPanel inferior = new JPanel(new BorderLayout());
        inferior.setOpaque(false);
        inferior.add(acciones, BorderLayout.EAST);

        add(tarjeta, BorderLayout.CENTER);
        add(inferior, BorderLayout.SOUTH);

        refrescar();
    }

    private void configurarTabla() {
        tabla.setRowHeight(46);
        tabla.setFont(Tema.sans(Font.PLAIN, 14));
        tabla.setForeground(Tema.TEXTO);
        tabla.setBackground(Color.WHITE);
        tabla.setSelectionBackground(Tema.FONDO);
        tabla.setSelectionForeground(Tema.TEXTO);
        tabla.setShowVerticalLines(false);
        tabla.setGridColor(Tema.FONDO);
        tabla.setFillsViewportHeight(true);

        tabla.getTableHeader().setFont(Tema.sans(Font.BOLD, 13));
        tabla.getTableHeader().setBackground(Tema.FONDO);
        tabla.getTableHeader().setForeground(Tema.TEXTO);
        tabla.getTableHeader().setReorderingAllowed(false);
    }

    private void nuevoSocio() {
        JTextField dni = new JTextField();
        JTextField nombre = new JTextField();

        JComboBox<String> tipo = new JComboBox<>(new String[]{"Estudiante", "Docente"});

        JLabel etiquetaDato = new JLabel("Carrera:");
        JTextField dato = new JTextField();

        tipo.addActionListener(e -> {
            etiquetaDato.setText(tipo.getSelectedItem().equals("Estudiante") ? "Carrera:" : "Área:");
        });

        JPanel formulario = new JPanel(new GridLayout(0, 1, 5, 5));
        formulario.add(new JLabel("DNI:"));
        formulario.add(dni);
        formulario.add(new JLabel("Nombre:"));
        formulario.add(nombre);
        formulario.add(new JLabel("Tipo:"));
        formulario.add(tipo);
        formulario.add(etiquetaDato);
        formulario.add(dato);

        int resultado = JOptionPane.showConfirmDialog(this, formulario, "Nuevo socio", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (resultado != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            if (dni.getText().trim().isEmpty()
                    || nombre.getText().trim().isEmpty()
                    || dato.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("Completá todos los campos.");
            }

            int numeroDni = Integer.parseInt(dni.getText().trim());

            if (numeroDni <= 0) {
                throw new IllegalArgumentException("El DNI debe ser mayor que cero.");
            }

            if (tipo.getSelectedItem().equals("Estudiante")) {
                controlador.nuevoSocioEstudiante(
                        numeroDni, nombre.getText().trim(), dato.getText().trim());
            } else {
                controlador.nuevoSocioDocente(
                        numeroDni, nombre.getText().trim(), dato.getText().trim());
            }

            refrescar();

            JOptionPane.showMessageDialog(this, "Socio registrado correctamente.");

        } catch (NumberFormatException ex) {
            mostrarError("El DNI debe ser numérico.");
        } catch (Exception ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void modificarSocio() {
        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            mostrarError("Seleccioná un socio de la tabla.");
            return;
        }

        try {
            int filaModelo = tabla.convertRowIndexToModel(fila);
            int dni = ((Number) modelo.getValueAt(filaModelo, 0)).intValue();

            String nombreActual = modelo.getValueAt(filaModelo, 1).toString();
            String tipoActual = modelo.getValueAt(filaModelo, 2).toString();

            JTextField nombre = new JTextField(nombreActual);
            JTextField dato = new JTextField(controlador.datoEspecificoSocio(dni));

            String etiqueta = tipoActual.equals("Estudiante") ? "Carrera:" : "Área:";

            JPanel formulario = new JPanel(new GridLayout(0, 1, 5, 5));
            formulario.add(new JLabel("Nombre:"));
            formulario.add(nombre);
            formulario.add(new JLabel(etiqueta));
            formulario.add(dato);

            int resultado = JOptionPane.showConfirmDialog(
                    this,
                    formulario,
                    "Modificar socio",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (resultado != JOptionPane.OK_OPTION) {
                return;
            }

            if (nombre.getText().trim().isEmpty() || dato.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("Completá todos los campos.");
            }

            controlador.modificarSocio(dni, nombre.getText().trim(), dato.getText().trim());

            refrescar();

            JOptionPane.showMessageDialog(this, "Socio modificado correctamente.");

        } catch (Exception ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void eliminarSocio() {
        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            mostrarError("Seleccioná un socio de la tabla.");
            return;
        }

        int dni = ((Number) modelo.getValueAt(fila, 0)).intValue();
        String nombre = modelo.getValueAt(fila, 1).toString();

        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Querés eliminar al socio " +
                nombre + "?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            controlador.eliminarSocio(dni);
            refrescar();

            JOptionPane.showMessageDialog(this,"Socio eliminado correctamente.");

        } catch (Exception ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje == null ? "Ocurrió un error." : mensaje,"Error", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void refrescar() {
        modelo.setRowCount(0);

        for (Object[] fila : controlador.filasSocios()) {
            modelo.addRow(fila);
        }
    }
}
