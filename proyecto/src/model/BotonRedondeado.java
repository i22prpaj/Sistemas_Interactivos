package model;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JButton;
import javax.swing.Icon;

public class BotonRedondeado extends JButton {

    public BotonRedondeado(String label) {
        super(label);
        // Quitamos todo el estilo por defecto de Java (el look gris metálico)
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setMargin(new Insets(8, 18, 8, 18));
        
        // Fuente más moderna y parecida a la imagen
        setFont(new Font("SansSerif", Font.BOLD, 13));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        // Suavizado de bordes
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Lógica de color (Efecto de clic/rollover)
        Color fillColor = getBackground();
        if (getModel().isPressed()) {
            fillColor = fillColor.darker();
        } else if (getModel().isRollover()) {
            fillColor = fillColor.brighter();
        }

        // Dibujar fondo redondeado
        int arc = getHeight();
        g2.setColor(fillColor);
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), arc, arc));

        // Dibujar texto centrado manualmente para evitar el truncamiento por layout
        String text = getText();
        Icon icon = getIcon();
        g2.setFont(getFont());
        g2.setColor(getForeground());
        FontMetrics fm = g2.getFontMetrics();

        if (icon != null) {
            // Si hay icono, dejar que la implementación por defecto lo gestione
            super.paintComponent(g);
        } else if (text != null && !text.isEmpty()) {
            int textWidth = fm.stringWidth(text);
            int textAscent = fm.getAscent();
            int x = (getWidth() - textWidth) / 2;
            int y = (getHeight() + textAscent) / 2 - fm.getDescent();
            g2.drawString(text, x, y);
        }

        g2.dispose();
    }
}