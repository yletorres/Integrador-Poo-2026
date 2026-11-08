package vista.componentes;

import vista.Tema;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;

public class Icono implements Icon {

    public enum Tipo { INICIO, LIBROS, SOCIOS, PRESTAMOS, LISTADOS, INFORMES, SALIR, LIBRO_VACIO }

    private final Tipo tipo;
    private final int tamanio;

    public Icono(Tipo tipo, int tamanio) {
        this.tipo = tipo;
        this.tamanio = tamanio;
    }

    public static Icono para(String seccion, int tamanio) {
        switch (seccion) {
            case "Libros": return new Icono(Tipo.LIBROS, tamanio);
            case "Socios": return new Icono(Tipo.SOCIOS, tamanio);
            case "Préstamos": return new Icono(Tipo.PRESTAMOS, tamanio);
            case "Listados": return new Icono(Tipo.LISTADOS, tamanio);
            case "Informes": return new Icono(Tipo.INFORMES, tamanio);
            default: return new Icono(Tipo.INICIO, tamanio);
        }
    }

    @Override
    public int getIconWidth() {
        return tamanio;
    }

    @Override
    public int getIconHeight() {
        return tamanio;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = Tema.suavizar(g);
        dibujar(g2, tipo, x, y, tamanio, c == null ? Tema.TEXTO_CLARO : c.getForeground());
        g2.dispose();
    }

    public static void dibujar(Graphics2D g, Tipo tipo, double x, double y, double tamanio, Color color) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.translate(x, y);
        double s = tamanio / 20.0;
        g2.scale(s, s);
        g2.setColor(color);
        g2.setStroke(new BasicStroke(tipo == Tipo.LIBRO_VACIO ? 1.2f : 1.7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        Path2D.Double p = new Path2D.Double();
        switch (tipo) {
            case INICIO:
                p.moveTo(3, 9.5); p.lineTo(10, 3.5); p.lineTo(17, 9.5);
                p.moveTo(5, 8); p.lineTo(5, 16.5); p.lineTo(15, 16.5); p.lineTo(15, 8);
                p.moveTo(8.5, 16.5); p.lineTo(8.5, 12); p.lineTo(11.5, 12); p.lineTo(11.5, 16.5);
                g2.draw(p);
                break;
            case LIBROS:
                g2.draw(new RoundRectangle2D.Double(4.5, 3, 11, 14, 2.5, 2.5));
                p.moveTo(7.5, 3); p.lineTo(7.5, 17);
                p.moveTo(10, 7); p.lineTo(13, 7);
                g2.draw(p);
                break;
            case SOCIOS:
                g2.draw(new Ellipse2D.Double(5, 4.2, 5.4, 5.4));
                p.moveTo(2.5, 16.5); p.curveTo(2.5, 13.2, 4.8, 11.6, 7.7, 11.6); p.curveTo(10.6, 11.6, 12.9, 13.2, 12.9, 16.5);
                p.moveTo(12.6, 4.6); p.curveTo(15.4, 4.6, 15.4, 9.2, 12.6, 9.2);
                p.moveTo(14.4, 11.9); p.curveTo(16.3, 12.4, 17.5, 13.9, 17.5, 16.5);
                g2.draw(p);
                break;
            case PRESTAMOS:
                p.moveTo(3.5, 7); p.lineTo(15.5, 7);
                p.moveTo(12.5, 4); p.lineTo(15.5, 7); p.lineTo(12.5, 10);
                p.moveTo(16.5, 13); p.lineTo(4.5, 13);
                p.moveTo(7.5, 10); p.lineTo(4.5, 13); p.lineTo(7.5, 16);
                g2.draw(p);
                break;
            case LISTADOS:
                for (double fy : new double[]{5.5, 10, 14.5}) {
                    g2.fill(new Ellipse2D.Double(3.4, fy - 1.1, 2.2, 2.2));
                    p.moveTo(8, fy); p.lineTo(16.5, fy);
                }
                g2.draw(p);
                break;
            case INFORMES:
                p.moveTo(3, 16.5); p.lineTo(17, 16.5);
                g2.draw(p);
                g2.draw(new RoundRectangle2D.Double(4.8, 10, 2.8, 4.5, 1, 1));
                g2.draw(new RoundRectangle2D.Double(8.6, 5, 2.8, 9.5, 1, 1));
                g2.draw(new RoundRectangle2D.Double(12.4, 8, 2.8, 6.5, 1, 1));
                break;
            case SALIR:
                p.moveTo(11, 3.5); p.lineTo(5, 3.5); p.lineTo(5, 16.5); p.lineTo(11, 16.5);
                p.moveTo(9, 10); p.lineTo(17, 10);
                p.moveTo(14, 7); p.lineTo(17, 10); p.lineTo(14, 13);
                g2.draw(p);
                break;
            case LIBRO_VACIO:
                for (int lado = -1; lado <= 1; lado += 2) {
                    Path2D.Double pagina = new Path2D.Double();
                    pagina.moveTo(10 + lado * 0.6, 5.2);
                    pagina.curveTo(10 + lado * 2.8, 3.6, 10 + lado * 5.6, 3.6, 10 + lado * 7.4, 4.6);
                    pagina.lineTo(10 + lado * 7.4, 14.8);
                    pagina.curveTo(10 + lado * 5.6, 13.8, 10 + lado * 2.8, 13.8, 10 + lado * 0.6, 15.4);
                    pagina.closePath();
                    g2.draw(pagina);
                }
                break;
            default:
                break;
        }
        g2.dispose();
    }
}
