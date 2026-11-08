package vista.paneles;

import controlador.BibliotecaController;
import vista.componentes.Boton;
import vista.componentes.Tarjeta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelSocios extends PanelSeccion {

    private final BibliotecaController controlador;
    private final JTable tablaSocios;
    private final DefaultTableModel modeloTabla;

    public PanelSocios(BibliotecaController controlador) {
        this.controlador = controlador;

        add(encabezado("Socios", "Docentes y estudiantes registrados en la biblioteca"), BorderLayout.NORTH);

        // Configuración de la tabla de socios
        String[] columnas = {"D.N.I.", "Nombre", "Tipo", "Libros Prestados"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No editables directamente en celdas
            }
        };
        tablaSocios = new JTable(modeloTabla);
        tablaSocios.setRowHeight(28);
        JScrollPane scrollPane = new JScrollPane(tablaSocios);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        Tarjeta tarjetaTabla = new Tarjeta(Color.WHITE, 30);
        tarjetaTabla.setLayout(new BorderLayout());
        tarjetaTabla.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        tarjetaTabla.add(scrollPane, BorderLayout.CENTER);

        // Panel inferior con botones de acción (Agregar Estudiante / Docente)
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        panelBotones.setOpaque(false);

        Boton btnEstudiante = new Boton("Nuevo Estudiante");
        Boton btnDocente = new Boton("Nuevo Docente");

        btnEstudiante.addActionListener(e -> mostrarDialogoEstudiante());
        btnDocente.addActionListener(e -> mostrarDialogoDocente());

        panelBotones.add(btnEstudiante);
        panelBotones.add(btnDocente);

        JPanel centro = new JPanel(new BorderLayout(0, 15));
        centro.setOpaque(false);
        centro.add(tarjetaTabla, BorderLayout.CENTER);
        centro.add(panelBotones, BorderLayout.SOUTH);

        add(centro, BorderLayout.CENTER);
        refrescar();
    }

    private void mostrarDialogoEstudiante() {
        JTextField txtDni = new JTextField(10);
        JTextField txtNombre = new JTextField(15);
        JTextField txtCarrera = new JTextField(15);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.add(new JLabel("D.N.I.:"));
        panel.add(txtDni);
        panel.add(new JLabel("Nombre y Apellido:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Carrera:"));
        panel.add(txtCarrera);

        int resultado = JOptionPane.showConfirmDialog(this, panel, "Registrar Nuevo Estudiante", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (resultado == JOptionPane.OK_OPTION) {
            try {
                int dni = Integer.parseInt(txtDni.getText().trim());
                String nombre = txtNombre.getText().trim();
                String carrera = txtCarrera.getText().trim();

                if (nombre.isEmpty() || carrera.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean exito = controlador.agregarSocioEstudiante(dni, nombre, carrera);
                if (exito) {
                    JOptionPane.showMessageDialog(this, "Estudiante agregado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    refrescar();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo agregar el estudiante (verifique si el DNI ya existe).", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El DNI debe ser un valor numérico.", "Formato inválido", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void mostrarDialogoDocente() {
        JTextField txtDni = new JTextField(10);
        JTextField txtNombre = new JTextField(15);
        JTextField txtArea = new JTextField(15);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.add(new JLabel("D.N.I.:"));
        panel.add(txtDni);
        panel.add(new JLabel("Nombre y Apellido:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Área:"));
        panel.add(txtArea);

        int resultado = JOptionPane.showConfirmDialog(this, panel, "Registrar Nuevo Docente", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (resultado == JOptionPane.OK_OPTION) {
            try {
                int dni = Integer.parseInt(txtDni.getText().trim());
                String nombre = txtNombre.getText().trim();
                String area = txtArea.getText().trim();

                if (nombre.isEmpty() || area.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean exito = controlador.agregarSocioDocente(dni, nombre, area);
                if (exito) {
                    JOptionPane.showMessageDialog(this, "Docente agregado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    refrescar();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo agregar el docente (verifique si el DNI ya existe).", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El DNI debe ser un valor numérico.", "Formato inválido", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    void refrescar() {
        modeloTabla.setRowCount(0);
        List<Object[]> filas = controlador.filasSocios();
        for (Object[] fila : filas) {
            modeloTabla.addRow(fila);
        }
    }
}