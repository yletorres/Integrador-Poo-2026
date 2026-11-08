package biblioteca;

//import com.formdev.flatlaf.FlatDarkLaf;
import controlador.BibliotecaController;
import controlador.BibliotecaControllerImpl;
import controlador.dao.BibliotecaDAO;
import controlador.dao.impl.BibliotecaDAOImpl;
import controlador.dao.impl.LibroDAOImpl;
import controlador.dao.impl.PrestamoDAOImpl;
import controlador.dao.impl.SocioDAOImpl;
import vista.Tema;
import vista.VentanaInicial;
import vista.paneles.VentanaPrincipal;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

public class GestionBiblioteca {

    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        SwingUtilities.invokeLater(() -> {
            // FlatDarkLaf.setup();
            Tema.instalar();

            // muestra el menu inicial con las dos opciones disponibles
            new VentanaInicial(
                    GestionBiblioteca::iniciarSistema,
                    GestionBiblioteca::ejecutarDemostracion).setVisible(true);
        });
    }

    // abre la ventana principal de la biblioteca
    private static void iniciarSistema() {
        try {
            BibliotecaController controlador = new BibliotecaControllerImpl();
            new VentanaPrincipal(controlador).setVisible(true);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "No se pudo conectar con la base de datos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ejecuta la demostracion del practico
    private static void ejecutarDemostracion() {
        JOptionPane.showMessageDialog(
                null,
                "La demostración del práctico todavía no está implementada.",
                "Demostración",
                JOptionPane.INFORMATION_MESSAGE);

        Biblioteca biblioteca = new Biblioteca("Biblioteca Central");

        Libro java1 = new Libro("JAVA. Como Programar", 1, "Pearson", 2018);
        Libro java2 = new Libro("JAVA. Como Programar", 1, "Pearson", 2018); // 2 iguales de orueba
        Libro longman = new Libro("Longman. Diccionario Pocket", 3, "Longman", 2015);
        Libro vivir = new Libro("Vivir para contarla", 1, "Sudamericana", 2002);
        Libro programando = new Libro("Programando con JAVA", 2, "Anaya", 2021);
        Libro orgullo = new Libro("Orgullo y prejuicio", 5, "Alianza", 2010);
        Libro patrones = new Libro("Patrones de Diseño", 1, "Addison-Wesley", 2019);


        biblioteca.agregarLibro(java1);
        biblioteca.agregarLibro(java2);
        biblioteca.agregarLibro(longman);
        biblioteca.agregarLibro(vivir);
        biblioteca.agregarLibro(programando);
        biblioteca.agregarLibro(orgullo);
        biblioteca.agregarLibro(patrones);

        System.out.println("Biblioteca: " + biblioteca.getNombre());
        System.out.println("Libros cargados: " + biblioteca.getLibros().size());

        System.out.println("---CARGAR SOCIOS----");
        biblioteca.nuevoSocioDocente(14524782, "Juan Perez", "Exactas");
        biblioteca.nuevoSocioDocente(10912002, "María Alegre", "Ciencias");
        biblioteca.nuevoSocioEstudiante(28987498, "Francisco Paenza", "Licenciatura en Sistemas");
        biblioteca.nuevoSocioEstudiante(31987123, "Cesar Milstein", "Biología");

        Socio juanPerez = biblioteca.buscarSocio(14524782);
        Socio franciscoPaenza = biblioteca.buscarSocio(28987498);
        Socio mariaAlegre = biblioteca.buscarSocio(10912002);
        System.out.println("buuscar Socio: (99998768999)" + biblioteca.buscarSocio(99999999) + " (no existe)\n");
        System.out.println("Socios cargados: " + biblioteca.getSocios().size() + "\n");

        // Prestaos y devolcion
        System.out.println("Prestamos y dovoluciones");
        Calendar hoy = Calendar.getInstance();
        Calendar paso10 = Calendar.getInstance();
        paso10.add(Calendar.DAY_OF_MONTH, -10);
        Calendar paso25 = Calendar.getInstance();
        paso25.add(Calendar.DAY_OF_MONTH, -25);


        boolean prestamo1 = biblioteca.prestarLibro(hoy, juanPerez, programando);
        System.out.println("Juan Perez pide 'Programando con JAVA': " + (prestamo1 ? "PRESTADO" : "RECHAZADO"));
        boolean prestamo2 = biblioteca.prestarLibro(hoy, juanFernandez, vivir);
        System.out.println("Juan Fernández pide 'Vivir para contarla': " + (prestamo2 ? "PRESTADO" : "RECHAZADO"));
        boolean prestamo3 = biblioteca.prestarLibro(hoy, cesar, orgullo);
        System.out.println("Cesar Milstein pide 'Orgullo y prejuicio': " + (prestamo3 ? "PRESTADO" : "RECHAZADO"));

        // vencidos
        boolean prestamo4  = biblioteca.prestarLibro(paso10, mariaAlegre, longman);
        System.out.println("María pide 'Longman': " + (prestamo4 ? "PRESTADO" : "RECHAZADO"));
        boolean prestamo5 = biblioteca.prestarLibro(paso25, francisco, java1);
        System.out.println("Francisco Paenza pide 'JAVA. Como Programar': " + (prestamo5 ? "PRESTADO" : "RECHAZADO"));


        // socio con un vencido no puede pedir otro
        boolean prestamo6 = biblioteca.prestarLibro(hoy, francisco, patrones);
        System.out.println("Francisco Paenza pide 'Patrones de Diseño': " + (prestamo6 ? "PRESTADO" : "RECHAZADO"));

        // libro ya prestado
        boolean prestamo7 = biblioteca.prestarLibro(hoy, cesar, programando);
        System.out.println("Cesar Milstein pide 'Programando con JAVA': " + (prestamo7 ? "PRESTADO" : "RECHAZADO"));


        // devolucion hecha
        System.out.println("\n-- Devoluciones --");
        try {
            biblioteca.devolverLibro(vivir);
            System.out.println("Devuelto: Vivir para contarla");
        } catch (LibroNoPrestadoException excepcion) {
            System.out.println("Error: " + excepcion.getMessage());
        }

        // LibroNoPrestadoException libro ya devuelto
        try {
            biblioteca.devolverLibro(vivir);
            System.out.println("Devuelto 'Vivir para contarla'");
        } catch (LibroNoPrestadoException excepcion) {
            System.out.println("LibroNoPrestadoException: " + excepcion.getMessage());
        }


        System.out.println("Cantidad de socios de tipo Estudiante: " + biblioteca.cantidadDeSociosPorTipo("Estudiante"));
        System.out.println("Cantidad de socios de tipo Docente: " + biblioteca.cantidadDeSociosPorTipo("Docente"));

        System.out.println("\nLista de docentes que nunca han adeudado ni adeudan: ");
        System.out.println(biblioteca.listaDeDocentesResponsables());

        System.out.println("Préstamos vencidos al día de hoy:");
        ArrayList<Prestamo> vencidos = biblioteca.prestamosVencidos();
        if (vencidos.isEmpty()) {
            System.out.println("(ninguno)");
        }
        for (Prestamo p : vencidos) {
            System.out.println(p.toString());
            System.out.println("---");
        }

        System.out.println("\n" + biblioteca.listaDeTitulos());

        System.out.println("---LISTADOS---");
        System.out.println(biblioteca.listadeLibros());
        System.out.println(biblioteca.listaDeSocios());

        System.out.println("--- SOCIOS CON LIBROS EN PRESTAMO ---");
        try {
            System.out.println("'Programando con JAVA' - SOCIO: " + biblioteca.quienTieneElLibro(programando));
        } catch (LibroNoPrestadoException e) {
            System.out.println("'Programando con JAVA': " + e.getMessage());
        }
        try {
            System.out.println("'Orgullo y prejuicio' - SOCIO: " + biblioteca.quienTieneElLibro(orgullo));
        } catch (LibroNoPrestadoException e) {
            System.out.println("'Orgullo y prejuicio' - " + e.getMessage());
        }
        try {
            System.out.println("'Vivir para contarla' - SOCIO: " + biblioteca.quienTieneElLibro(vivir));
        } catch (LibroNoPrestadoException e) {
            System.out.println("'Vivir para contarla': -" + e.getMessage()); // devolcvio
        }
    }
}
