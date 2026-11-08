package vista.formularios;

import controlador.BibliotecaController;
import vista.Tema;
import vista.componentes.Boton;

import javax.swing.*;
import java.awt.*;

/**
 * Formulario para el registro de préstamos de libros dentro del sistema de biblioteca.
 * Permite ingresar el DNI del socio y el título del libro a prestar, validando
 * los campos e invocando la lógica de negocio a través del controlador.
 *
 * @author Josefina Vallejos Romero
 */
    public class FormularioPrestamo extends JPanel {

    /** Controlador de la biblioteca utilizado para ejecutar la lógica de negocio. */
        private final BibliotecaController controlador;
    /** Campo de texto para ingresar el DNI del socio. */
        private final JTextField txtDni;
    /** Campo de texto para ingresar el título del libro a prestar. */
        private final JTextField txtTitulo;
    /** Acción callback a ejecutar tras registrar exitosamente un préstamo. */
        private final Runnable alRegistrarSuccess;

    /**
     * Construye e inicializa la interfaz visual del formulario de registro de préstamos.
     *
     * @param controlador instancia del controlador de la biblioteca
     * @param alRegistrarSuccess acción o callback que se ejecuta cuando el préstamo se registra con éxito
     */
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

    /**
     * Valida los datos ingresados en los campos de texto e intenta registrar
     * el préstamo invocando al controlador. Maneja excepciones de formato numérico
     * y de tiempo de ejecución mostrando mensajes informativos al usuario.
     */
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