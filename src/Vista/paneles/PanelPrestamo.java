package vista.paneles;

import controlador.BibliotecaController;
import biblioteca.excepciones.LibroNoPrestadoException;
import vista.componentes.Boton;
import vista.componentes.Tarjeta;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;

public class PanelPrestamo extends PanelSeccion {

    public PanelPrestamo(BibliotecaController controlador) {
        add(encabezado("Gestión de Préstamos", "Registro de retiros y devoluciones de ejemplares"), BorderLayout.NORTH);

        JPanel contenedor = new JPanel(new GridLayout(1, 2, 20, 0));
        contenedor.setOpaque(false);

        // --- TARJETA DE PRÉSTAMO ---
        Tarjeta tarjetaPrestamo = new Tarjeta(Color.WHITE, 30);
        tarjetaPrestamo.setLayout(new GridBagLayout());
        tarjetaPrestamo.setBorder(BorderFactory.createEmptyBorder(24, 30, 24, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtDni = new JTextField(15);
        JTextField txtLibroPrestamo = new JTextField(15);
        Boton btnPrestamo = new Boton("Registrar Préstamo");

        gbc.gridx = 0; gbc.gridy = 0; tarjetaPrestamo.add(new JLabel("D.N.I. del Socio:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; tarjetaPrestamo.add(txtDni, gbc);
        gbc.gridx = 0; gbc.gridy = 1; tarjetaPrestamo.add(new JLabel("Título del Libro:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; tarjetaPrestamo.add(txtLibroPrestamo, gbc);
        gbc.gridx = 1; gbc.gridy = 2; tarjetaPrestamo.add(btnPrestamo, gbc);

        btnPrestamo.addActionListener(e -> {
            try {
                int dni = Integer.parseInt(txtDni.getText().trim());
                String titulo = txtLibroPrestamo.getText().trim();
                boolean exito = controlador.realizarPrestamo(dni, titulo, Calendar.getInstance());
                if (exito) {
                    JOptionPane.showMessageDialog(this, "¡Préstamo registrado con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    txtDni.setText("");
                    txtLibroPrestamo.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo realizar el préstamo (verifique datos, stock o estado).", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El DNI debe ser numérico.", "Formato inválido", JOptionPane.ERROR_MESSAGE);
            }
        });

        // --- TARJETA DE DEVOLUCIÓN ---
        Tarjeta tarjetaDevolucion = new Tarjeta(Color.WHITE, 30);
        tarjetaDevolucion.setLayout(new GridBagLayout());
        tarjetaDevolucion.setBorder(BorderFactory.createEmptyBorder(24, 30, 24, 30));

        JTextField txtLibroDevolucion = new JTextField(15);
        Boton btnDevolucion = new Boton("Registrar Devolución");

        gbc.gridx = 0; gbc.gridy = 0; tarjetaDevolucion.add(new JLabel("Título del Libro:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; tarjetaDevolucion.add(txtLibroDevolucion, gbc);
        gbc.gridx = 1; gbc.gridy = 1; tarjetaDevolucion.add(btnDevolucion, gbc);

        btnDevolucion.addActionListener(e -> {
            String titulo = txtLibroDevolucion.getText().trim();
            try {
                controlador.devolverLibro(titulo);
                JOptionPane.showMessageDialog(this, "¡Devolución registrada con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                txtLibroDevolucion.setText("");
            } catch (LibroNoPrestadoException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        contenedor.add(tarjetaPrestamo);
        contenedor.add(tarjetaDevolucion);
        add(contenedor, BorderLayout.CENTER);
    }

    @Override
    void refrescar() {
        // Actualización al mostrar el panel
    }
}