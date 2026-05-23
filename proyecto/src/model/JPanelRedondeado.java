package model;

import javax.swing.*;
import java.awt.*;

public class JPanelRedondeado extends JPanel {
    private int radius;

    public JPanelRedondeado(int radius) {
        this.radius = radius;
        // El panel no es opaco para que las esquinas redondeadas muestren el fondo
        // subyacente (útil si hay otro componente detrás).
        setOpaque(false); // Importante para que se vea el fondo de atrás en las esquinas
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Dibujar fondo redondeado antes de pintar los hijos.
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(getBackground());
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        // Llamar a super para que los componentes hijos se dibujen encima del fondo.
        super.paintComponent(g);
    }
}