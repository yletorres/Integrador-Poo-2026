package vista.paneles;

import controlador.BibliotecaController;
import vista.Tema;
import vista.componentes.Boton;
import vista.componentes.Tarjeta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Panel encargado de visualizar y gestionar los socios de una biblioteca.
 * Permite registrar estudiantes y docentes, modificar sus datos, eliminar
 * socios y consultar la cantidad de libros que tienen prestados. La
 * comunicación con la lógica del sistema se realiza mediante la interfaz
 * BibliotecaController.
 *
 * @author Azula Iara
 * @version TP7 - 2026
 */
public class PanelSocios extends PanelSeccion {

    /**
     * Controlador encargado de gestionar las operaciones de los socios.
     */
    private final BibliotecaController controlador;

    /**
     * Modelo que administra los datos mostrados en la tabla.
     */
    private final DefaultTableModel modelo;

    /**
     * Tabla que muestra los socios registrados en la biblioteca.
     */
    private final JTable tabla;

    /**
     * Construye el panel de socios, configura la tabla y agrega los botones
     * para registrar y eliminar socios.
     *
     * @param p_controlador controlador encargado de gestionar las operaciones
     *                      relacionadas con los socios
     */
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

        JScrollPane desplazamiento = new JScrollPane(tabla);
        Tema.estilizar(desplazamiento);

        Tarjeta tarjeta = new Tarjeta(Tema.TINTA_TERRACOTA, 20);
        tarjeta.setLayout(new BorderLayout());
        tarjeta.add(desplazamiento, BorderLayout.CENTER);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        acciones.setOpaque(false);

        Boton nuevo = new Boton("+ Nuevo socio");
        Boton eliminar = new Boton("Eliminar", Boton.Estilo.OSCURO);

        nuevo.addActionListener(e -> nuevoSocio());
        eliminar.addActionListener(e -> eliminarSocio());

        acciones.add(nuevo);
        acciones.add(eliminar);

        JPanel inferior = new JPanel(new BorderLayout());
        inferior.setOpaque(false);
        inferior.add(acciones, BorderLayout.EAST);

        add(tarjeta, BorderLayout.CENTER);
        add(inferior, BorderLayout.SOUTH);

        refrescar();
    }

    /**
     * Configura la apariencia visual de la tabla de socios. Establece la
     * altura de las filas, los colores, la tipografía, la selección y las
     * características de la cabecera.
     */
    private void configurarTabla() {
        tabla.setRowHeight(46);
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
     * Solicita los datos necesarios para registrar un nuevo socio. Permite
     * seleccionar si el socio es estudiante o docente y solicita la carrera
     * o el área correspondiente. Valida que los campos estén completos y que
     * el DNI sea un número positivo. Luego solicita al controlador registrar
     * el socio y actualiza la tabla.
     */
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

    /**
     * Elimina el socio seleccionado de la biblioteca. Verifica que exista
     * una fila seleccionada, solicita confirmación y comunica la eliminación
     * al controlador. Si la operación se realiza correctamente, actualiza la
     * tabla.
     */
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

    /**
     * Muestra un mensaje de error mediante una ventana de diálogo.
     *
     * @param mensaje descripción del error ocurrido; si es null, se muestra
     *                un mensaje genérico
     */
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje == null ? "Ocurrió un error." : mensaje,"Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Actualiza la tabla con los socios de la biblioteca seleccionada.
     * Elimina las filas existentes y agrega los datos obtenidos desde el
     * controlador.
     */
    @Override
    public void refrescar() {
        modelo.setRowCount(0);

        for (Object[] fila : controlador.filasSocios()) {
            modelo.addRow(fila);
        }
    }
}