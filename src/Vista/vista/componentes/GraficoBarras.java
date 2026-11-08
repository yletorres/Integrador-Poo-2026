package vista.componentes;

import vista.Tema;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// grafico de barras con una grilla suave y una leyenda numerada a la derecha
public class GraficoBarras extends JComponent {

    private List<String> nombres = new ArrayList<>();
    private List<Integer> valores = new ArrayList<>();
    private String vacio = "Todavía no hay datos.";
    private String ayuda = "";

    public GraficoBarras() {
        setPreferredSize(new Dimension(520, 260));
    }

    public void setAyuda(String ayuda) {
        this.ayuda = ayuda;
        repaint();
    }

    public void setDatos(List<String> nombres, List<Integer> valores, String mensajeVacio) {
        this.nombres = nombres;
        this.valores = valores;
        this.vacio = mensajeVacio;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = Tema.suavizar(g);
        int w = getWidth(), h = getHeight();

        int max = 0;
        for (int v : valores) max = Math.max(max, v);
        
        // muestra un mensaje si todavia no hay valores para graficar
        if (valores.isEmpty() || max == 0) {
            int icono = 72;
            int bloque = icono + 20 + 22 + (ayuda.isEmpty() ? 0 : 26);
            int y0 = (h - bloque) / 2;
            Icono.dibujar(g2, Icono.Tipo.LIBRO_VACIO, (w - icono) / 2.0, y0, icono, Tema.conAlfa(Tema.CARAMELO, 150));
            g2.setFont(Tema.sans(Font.BOLD, 16));
            g2.setColor(Tema.TEXTO_CLARO);
            FontMetrics fm = g2.getFontMetrics();
            int yTexto = y0 + icono + 20 + fm.getAscent();
            g2.drawString(vacio, (w - fm.stringWidth(vacio)) / 2, yTexto);
            if (!ayuda.isEmpty()) {
                g2.setFont(Tema.sans(Font.PLAIN, 14));
                g2.setColor(Tema.conAlfa(Tema.TEXTO_CLARO, 180));
                FontMetrics fa = g2.getFontMetrics();
                g2.drawString(ayuda, (w - fa.stringWidth(ayuda)) / 2, yTexto + 26);
            }
            g2.dispose();
            return;
        }

        int leyenda = 190, izq = 34, abajo = 30, arriba = 18;
        int pw = w - leyenda - izq - 12, ph = h - abajo - arriba;
        int paso = max <= 5 ? 1 : max <= 10 ? 2 : (int) Math.ceil(max / 5.0);
        int techo = (int) Math.ceil(max / (double) paso) * paso;

        // calcula la escala y dibuja las lineas de referencia
        // grilla y escala
        g2.setFont(Tema.sans(Font.PLAIN, 11));
        FontMetrics fm = g2.getFontMetrics();
        for (int v = 0; v <= techo; v += paso) {
            int y = arriba + ph - (int) Math.round(ph * v / (double) techo);
            g2.setColor(Tema.conAlfa(Tema.TEXTO_CLARO, 45));
            g2.setStroke(new BasicStroke(1f));
            g2.drawLine(izq, y, izq + pw, y);
            g2.setColor(Tema.conAlfa(Tema.TEXTO_CLARO, 180));
            String t = String.valueOf(v);
            g2.drawString(t, izq - 8 - fm.stringWidth(t), y + fm.getAscent() / 2 - 1);
        }

        // barras y valor
        int n = valores.size();
        double ranura = pw / (double) n;
        int ancho = (int) Math.min(36, ranura * 0.5);
        for (int i = 0; i < n; i++) {
            int v = valores.get(i);
            int x = (int) (izq + i * ranura + (ranura - ancho) / 2);
            int alto = (int) Math.round(ph * v / (double) techo);
            g2.setColor(i % 2 == 0 ? Tema.CARAMELO : Tema.TEXTO_CLARO);
            if (alto > 0) {
                g2.fill(Tema.forma(x, arriba + ph - alto, ancho, alto, 7, 7, 0, 0));
            }
            g2.setColor(Tema.TEXTO_CLARO);
            g2.setFont(Tema.sans(Font.BOLD, 12));
            String num = String.valueOf(v);
            g2.drawString(num, x + (ancho - g2.getFontMetrics().stringWidth(num)) / 2, arriba + ph - alto - 6);
            g2.setColor(Tema.conAlfa(Tema.TEXTO_CLARO, 180));
            g2.setFont(Tema.sans(Font.PLAIN, 12));
            String eje = String.valueOf(i + 1);
            g2.drawString(eje, x + (ancho - g2.getFontMetrics().stringWidth(eje)) / 2, arriba + ph + 20);
        }

        // relaciona cada numero del eje con su nombre correspondiente
        int x0 = w - leyenda + 6;
        g2.setFont(Tema.sans(Font.PLAIN, 13));
        FontMetrics fl = g2.getFontMetrics();
        for (int i = 0; i < n; i++) {
            int y = arriba + 6 + i * 26;
            g2.setColor(i % 2 == 0 ? Tema.CARAMELO : Tema.TEXTO_CLARO);
            g2.fillRoundRect(x0, y, 12, 12, 4, 4);
            g2.setColor(Tema.TEXTO_CLARO);
            g2.drawString((i + 1) + "  " + recortar(nombres.get(i), fl, leyenda - 36), x0 + 22, y + 11);
        }
        g2.dispose();
    }

    private static String recortar(String texto, FontMetrics fm, int ancho) {
        if (fm.stringWidth(texto) <= ancho) return texto;
        String s = texto;
        while (s.length() > 1 && fm.stringWidth(s + "…") > ancho) s = s.substring(0, s.length() - 1);
        return s + "…";
    }
}
