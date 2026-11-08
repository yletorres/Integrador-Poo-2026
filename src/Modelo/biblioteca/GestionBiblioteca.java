package biblioteca;

//import com.formdev.flatlaf.FlatDarkLaf;
import biblioteca.excepciones.LibroNoPrestadoException;
import biblioteca.libro.Libro;
import biblioteca.prestamo.Prestamo;
import biblioteca.socio.Socio;
import controlador.BibliotecaController;
import controlador.BibliotecaControllerImpl;
import vista.Tema;
import vista.VentanaInicial;
import vista.VentanaSalida;
import vista.paneles.VentanaPrincipal;

import javax.swing.*;
import java.sql.SQLException;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

    // ejecuta la demostracion del practico (solo en memoria, no toca la base de datos)
    private static void ejecutarDemostracion() {
        List<VentanaSalida.Seccion> secciones = new ArrayList<>();
        try {
            demostracion(secciones);
        } catch (RuntimeException e) {
            e.printStackTrace();
            secciones.add(new VentanaSalida.Seccion("Error", "Error en la demostración", "-", e.toString()));
        } finally {
            if (original != null) {
                System.setOut(original);
                original = null;
            }
        }
        VentanaSalida.mostrar("Demostración del práctico", secciones);
    }

    private static PrintStream original;
    private static ByteArrayOutputStream buffer;

    // desde aca lo que se imprime se guarda en un buffer en vez de ir a la consola
    private static void iniciarCaptura() {
        original = System.out;
        buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
    }

    // termina la captura y devuelve lo que se imprimio
    private static String finCaptura() {
        System.setOut(original);
        original = null;
        return buffer.toString(StandardCharsets.UTF_8).stripTrailing();
    }

    private static void demostracion(List<VentanaSalida.Seccion> secciones) {

        iniciarCaptura();
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
        Socio cesar = biblioteca.buscarSocio(31987123);
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
        boolean prestamo2 = biblioteca.prestarLibro(hoy, juanPerez, vivir);
        System.out.println("Juan Perez pide 'Vivir para contarla': " + (prestamo2 ? "PRESTADO" : "RECHAZADO"));
        boolean prestamo3 = biblioteca.prestarLibro(hoy, cesar, orgullo);
        System.out.println("Cesar Milstein pide 'Orgullo y prejuicio': " + (prestamo3 ? "PRESTADO" : "RECHAZADO"));

        // vencidos
        boolean prestamo4  = biblioteca.prestarLibro(paso10, mariaAlegre, longman);
        System.out.println("María pide 'Longman': " + (prestamo4 ? "PRESTADO" : "RECHAZADO"));
        boolean prestamo5 = biblioteca.prestarLibro(paso25, franciscoPaenza, java1);
        System.out.println("Francisco Paenza pide 'JAVA. Como Programar': " + (prestamo5 ? "PRESTADO" : "RECHAZADO"));


        // socio con un vencido no puede pedir otro
        boolean prestamo6 = biblioteca.prestarLibro(hoy, franciscoPaenza, patrones);
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


        secciones.add(new VentanaSalida.Seccion("Preparación",
                "Datos de prueba: se instancian los objetos del diseño (libros, socios, préstamos y devoluciones)",
                "biblioteca.agregarLibro(libro)\n"
              + "biblioteca.nuevoSocioDocente(dni, nombre, area)\n"
              + "biblioteca.nuevoSocioEstudiante(dni, nombre, carrera)\n"
              + "biblioteca.buscarSocio(dni)\n"
              + "biblioteca.prestarLibro(fecha, socio, libro)\n"
              + "biblioteca.devolverLibro(libro)   // puede lanzar LibroNoPrestadoException",
                finCaptura()));

        // --- consigna 1 ---
        iniciarCaptura();
        System.out.println("Estudiantes: " + biblioteca.cantidadDeSociosPorTipo("Estudiante"));
        secciones.add(new VentanaSalida.Seccion("Consigna 1",
                "¿Qué cantidad de socios de tipo Estudiante hay?",
                "biblioteca.cantidadDeSociosPorTipo(\"Estudiante\")",
                finCaptura()));

        // --- consigna 2 ---
        iniciarCaptura();
        System.out.println(biblioteca.listaDeDocentesResponsables());
        secciones.add(new VentanaSalida.Seccion("Consigna 2",
                "¿Cuál es la lista de docentes que nunca han adeudado ni adeudan libros?",
                "biblioteca.listaDeDocentesResponsables()",
                finCaptura()));

        // --- consigna 3 ---
        iniciarCaptura();
        System.out.println(biblioteca.listaDeLibros());
        secciones.add(new VentanaSalida.Seccion("Consigna 3",
                "¿Cuál es la lista de libros?",
                "biblioteca.listaDeLibros()",
                finCaptura()));

        iniciarCaptura();
        System.out.println(biblioteca.listaDeSocios());
        secciones.add(new VentanaSalida.Seccion("Consigna 3",
                "¿Cuál es la lista de socios?",
                "biblioteca.listaDeSocios()",
                finCaptura()));

        // --- consigna 4 ---
        iniciarCaptura();
        try {
            System.out.println("'Programando con JAVA' - SOCIO: " + biblioteca.quienTieneElLibro(programando));
        } catch (LibroNoPrestadoException e) {
            System.out.println("'Programando con JAVA': " + e.getMessage());
        }
        secciones.add(new VentanaSalida.Seccion("Consigna 4",
                "¿Qué socio tiene prestado el libro \"Programando con JAVA\"?",
                "biblioteca.quienTieneElLibro(programando)   // lanza LibroNoPrestadoException si no está prestado",
                finCaptura()));

        // --- complementarios ---
        iniciarCaptura();
        System.out.println("Cantidad de socios de tipo Docente: " + biblioteca.cantidadDeSociosPorTipo("Docente"));
        System.out.println("\nPréstamos vencidos al día de hoy:");
        ArrayList<Prestamo> vencidos = biblioteca.prestamosVencidos();
        if (vencidos.isEmpty()) {
            System.out.println("(ninguno)");
        }
        for (Prestamo p : vencidos) {
            System.out.println(p.toString());
            System.out.println("---");
        }
        System.out.println("\n" + biblioteca.listaDeTitulos());
        secciones.add(new VentanaSalida.Seccion("Complementario",
                "Otras consultas de apoyo: docentes, préstamos vencidos y títulos",
                "biblioteca.cantidadDeSociosPorTipo(\"Docente\")\n"
              + "biblioteca.prestamosVencidos()\n"
              + "biblioteca.listaDeTitulos()",
                finCaptura()));

        iniciarCaptura();
        try {
            System.out.println("'Orgullo y prejuicio' - SOCIO: " + biblioteca.quienTieneElLibro(orgullo));
        } catch (LibroNoPrestadoException e) {
            System.out.println("'Orgullo y prejuicio' - " + e.getMessage());
        }
        try {
            System.out.println("'Vivir para contarla' - SOCIO: " + biblioteca.quienTieneElLibro(vivir));
        } catch (LibroNoPrestadoException e) {
            System.out.println("'Vivir para contarla': " + e.getMessage()); // ya devuelto
        }
        secciones.add(new VentanaSalida.Seccion("Complementario",
                "Otros libros: uno prestado y otro ya devuelto (LibroNoPrestadoException)",
                "biblioteca.quienTieneElLibro(orgullo)\nbiblioteca.quienTieneElLibro(vivir)",
                finCaptura()));
    }
}
