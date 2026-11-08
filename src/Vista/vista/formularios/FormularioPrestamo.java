package vista.formularios;

import controlador.BibliotecaController;
import vista.Tema;
import vista.componentes.Boton;

import javax.swing.*;
import java.awt.*;

    public class FormularioPrestamo extends JPanel {

        private final BibliotecaController controlador;
        private final JTextField txtDni;
        private final JTextField txtTitulo;
        private final Runnable alRegistrarSuccess;

        public FormularioPrestamo(BibliotecaController controlador, Runnable alRegistrarSuccess) {
            this.controlador = controlador;
            this.alRegistrarSuccess = alRegistrarSuccess;

            setOpaque(false);
            setLayout(new GridBagLayout());
            setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.GRAY),
                    " Registrar Préstamo ",
                    0, 0,
                    Tema.sans(Font.BOLD, 14),
                    Color.WHITE
            ));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 8, 8, 8);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // DNI Socio
            gbc.gridx = 0; gbc.gridy = 0;
            JLabel lblDni = new JLabel("DNI del Socio:");
            lblDni.setForeground(Color.WHITE);
            add(lblDni, gbc);

            gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
            txtDni = new JTextField(12);
            add(txtDni, gbc);

            // Título Libro
            gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
            JLabel lblTitulo = new JLabel("Título del Libro:");
            lblTitulo.setForeground(Color.WHITE);
            add(lblTitulo, gbc);

            gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
            txtTitulo = new JTextField(15);
            add(txtTitulo, gbc);

            // Botón Confirmar
            gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.CENTER;
            Boton btnPrestar = new Boton("Registrar Préstamo");
            btnPrestar.addActionListener(e -> registrarPrestamo());
            add(btnPrestar, gbc);
        }

        private void registrarPrestamo() {
            String dniTexto = txtDni.getText().trim();
            String titulo = txtTitulo.getText().trim();

            if (dniTexto.isEmpty() || titulo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, completá todos los campos.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int dni = Integer.parseInt(dniTexto);
                boolean exito = controlador.prestarLibro(dni, titulo);

                if (exito) {
                    JOptionPane.showMessageDialog(this, "¡Préstamo registrado con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    txtDni.setText("");
                    txtTitulo.setText("");
                    if (alRegistrarSuccess != null) {
                        alRegistrarSuccess.run();
                    }
                } else {
                    JOptionPane.showMessageDialog(this,
                            "No se pudo registrar el préstamo. Verificá que el socio exista, el libro esté disponible y el socio no tenga deudas o límites excedidos.",
                            "Error al prestar",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El DNI debe ser un número entero válido.", "DNI inválido", JOptionPane.ERROR_MESSAGE);
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(this, "No se pudo registrar el préstamo: " + ex.getMessage(), "Error al prestar", JOptionPane.ERROR_MESSAGE);
            }
        }
    }