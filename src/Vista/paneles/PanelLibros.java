package vista.paneles;

import controlador.BibliotecaController;
import vista.componentes.Boton;
import vista.componentes.Tarjeta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelLibros extends PanelSeccion {

    private final BibliotecaController controlador;
    private final JTable tablaLibros;
    private final DefaultTableModel modeloTabla;

    public PanelLibros(BibliotecaController controlador) {
        this.controlador = controlador;

        add(encabezado("Libros", "Catálogo de ejemplares y control de disponibilidad"), BorderLayout.NORTH);

        // Configuración de la tabla de libros
        String[] columnas = {"Título", "Edición", "Editorial", "Año", "Prestado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaLibros = new JTable(modeloTabla);
        tablaLibros.setRowHeight(28);
        JScrollPane scrollPane = new JScrollPane(tablaLibros);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        Tarjeta tarjetaTabla = new Tarjeta(Color.WHITE, 30);
        tarjetaTabla.setLayout(new BorderLayout());
        tarjetaTabla.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        tarjetaTabla.add(scrollPane, BorderLayout.CENTER);

        // Panel inferior con botones de acción (Agregar / Quitar)
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        panelBotones.setOpaque(false);

        Boton btnAgregarLibro = new Boton("Agregar Libro");
        Boton btnQuitarLibro = new Boton("Quitar Libro");

        btnAgregarLibro.addActionListener(e -> mostrarDialogoAgregarLibro());
        btnQuitarLibro.addActionListener(e -> mostrarDialogoQuitarLibro());

        panelBotones.add(btnAgregarLibro);
        panelBotones.add(btnQuitarLibro);

        JPanel centro = new JPanel(new BorderLayout(0, 15));
        centro.setOpaque(false);
        centro.add(tarjetaTabla, BorderLayout.CENTER);
        centro.add(panelBotones, BorderLayout.SOUTH);

        add(centro, BorderLayout.CENTER);
        refrescar();
    }

    private void mostrarDialogoAgregarLibro() {
        JTextField txtTitulo = new JTextField(15);
        JTextField txtEdicion = new JTextField(5);
        JTextField txtEditorial = new JTextField(15);
        JTextField txtAnio = new JTextField(5);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.add(new JLabel("Título:"));
        panel.add(txtTitulo);
        panel.add(new JLabel("Edición:"));
        panel.add(txtEdicion);
        panel.add(new JLabel("Editorial:"));
        panel.add(txtEditorial);
        panel.add(new JLabel("Año:"));
        panel.add(txtAnio);

        int resultado = JOptionPane.showConfirmDialog(this, panel, "Agregar Nuevo Libro", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (resultado == JOptionPane.OK_OPTION) {
            try {
                String titulo = txtTitulo.getText().trim();
                int edicion = Integer.parseInt(txtEdicion.getText().trim());
                String editorial = txtEditorial.getText().trim();
                int anio = Integer.parseInt(txtAnio.getText().trim());

                if (titulo.isEmpty() || editorial.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Todos los campos de texto son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean exito = controlador.agregarLibro(titulo, edicion, editorial, anio);
                if (exito) {
                    JOptionPane.showMessageDialog(this, "Libro agregado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    refrescar();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo agregar el libro a la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "La edición y el año deben ser valores numéricos.", "Formato inválido", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void mostrarDialogoQuitarLibro() {
        String titulo = JOptionPane.showInputDialog(this, "Ingrese el título exacto del libro a quitar:", "Quitar Libro", JOptionPane.QUESTION_MESSAGE);
        if (titulo != null && !titulo.trim().isEmpty()) {
            boolean exito = controlador.quitarLibro(titulo.trim());
            if (exito) {
                JOptionPane.showMessageDialog(this, "Libro quitado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                refrescar();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo quitar el libro (verifique que exista y no se encuentre prestado).", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    @Override
    void refrescar() {
        modeloTabla.setRowCount(0);
        List<Object[]> filas = controlador.filasLibros();
        for (Object[] fila : filas) {
            modeloTabla.addRow(fila);
        }
    }
}