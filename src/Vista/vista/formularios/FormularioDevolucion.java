package vista.formularios;
import biblioteca.excepciones.LibroNoPrestadoException;
import controlador.BibliotecaController;
import vista.Tema;
import vista.componentes.Boton;

import javax.swing.*;
import java.awt.*;

    public class FormularioDevolucion extends JPanel {

        private final BibliotecaController controlador;
        private final JTextField txtTitulo;
        private final Runnable alDevolverSuccess;

        public FormularioDevolucion(BibliotecaController controlador, Runnable alDevolverSuccess) {
            this.controlador = controlador;
            this.alDevolverSuccess = alDevolverSuccess;

            setOpaque(false);
            setLayout(new GridBagLayout());
            setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.GRAY),
                    " Registrar Devolución ",
                    0, 0,
                    Tema.sans(Font.BOLD, 14),
                    Color.WHITE
            ));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 8, 8, 8);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // Título del libro a devolver
            gbc.gridx = 0; gbc.gridy = 0;
            JLabel lblTitulo = new JLabel("Título del Libro:");
            lblTitulo.setForeground(Color.WHITE);
            add(lblTitulo, gbc);

            gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
            txtTitulo = new JTextField(15);
            add(txtTitulo, gbc);

            // Botón Confirmar Devolución
            gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.CENTER;
            Boton btnDevolver = new Boton("Registrar Devolución", Boton.Estilo.OSCURO);
            btnDevolver.addActionListener(e -> registrarDevolucion());
            add(btnDevolver, gbc);
        }

        private void registrarDevolucion() {
            String titulo = txtTitulo.getText().trim();

            if (titulo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingresá el título del libro a devolver.", "Campo vacío", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                controlador.devolverLibro(titulo);
                JOptionPane.showMessageDialog(this, "¡Devolución registrada correctamente!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                txtTitulo.setText("");
                if (alDevolverSuccess != null) {
                    alDevolverSuccess.run();
                }
            } catch (LibroNoPrestadoException ex) {
                // Cumpliendo consigna: muestra ÚNICAMENTE ex.getMessage() sin alterarlo
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Atención", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}
