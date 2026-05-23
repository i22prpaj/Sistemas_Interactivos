package model;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JButton;
import javax.swing.Icon;

public class BotonRedondeado extends JButton {
    // Botón personalizado con fondo y esquinas redondeadas.
    // Se adapta a texto o icono y gestiona efectos de rollover/pressed.
    public BotonRedondeado(String label) {
        super(label);
        // Eliminamos el estilo por defecto para dibujar nosotros el fondo.
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setMargin(new Insets(8, 18, 8, 18));

        // Fuente más moderna y puntero tipo mano para interacción táctil/rápida.
        setFont(new Font("SansSerif", Font.BOLD, 13));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Creamos un Graphics2D independiente para no alterar el original.
        Graphics2D g2 = (Graphics2D) g.create();

        // Activar antialiasing para bordes suaves.
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Determinar color de fondo según estado (pressed/rollover)
        Color fillColor = getBackground();
        if (getModel().isPressed()) {
            fillColor = fillColor.darker();
        } else if (getModel().isRollover()) {
            fillColor = fillColor.brighter();
        }

        // Dibujar un rectángulo redondeado que cubra todo el botón.
        int arc = getHeight(); // arco igual a la altura para esquinas completamente redondeadas
        g2.setColor(fillColor);
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), arc, arc));

        // Preparar para dibujar texto o icono centrado.
        String text = getText();
        Icon icon = getIcon();
        g2.setFont(getFont());
        g2.setColor(getForeground());
        FontMetrics fm = g2.getFontMetrics();

        if (icon != null) {
            // Si existe un icono, delegar a la implementación padre (maneja icono+texto correctamente).
            super.paintComponent(g);
        } else if (text != null && !text.isEmpty()) {
            // Calcular posición X/Y para centrar el texto vertical y horizontalmente.
            int textWidth = fm.stringWidth(text);
            int textAscent = fm.getAscent();
            int x = (getWidth() - textWidth) / 2;
            int y = (getHeight() + textAscent) / 2 - fm.getDescent();
            g2.drawString(text, x, y);
        }

        // Liberar recursos del Graphics2D clonado.
        g2.dispose();
    }
}