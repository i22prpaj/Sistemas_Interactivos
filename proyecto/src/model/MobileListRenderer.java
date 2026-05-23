package model;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MobileListRenderer extends DefaultListCellRenderer {
    // Renderer ligero pensado para listas estilo móvil: icono a la izquierda,
    // texto central y una flecha a la derecha. Usa un panel transparente
    // para permitir fondos personalizados en la lista.
    private ImageIcon carpetaIcon;

    public MobileListRenderer() {
        // Intenta cargar un icono de carpeta desde recursos; si no existe,
        // se usa un emoji como fallback. El tamaño buscado es 24x20.
        carpetaIcon = loadIcon("/resources/folder.PNG", 24, 20); 
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        // Construimos un panel por celda con layout simple: icono | texto | flecha
        JPanel p = new JPanel(new BorderLayout(15, 0));
        p.setOpaque(false);
        // Margen interior para separar del borde del scroll
        p.setBorder(new EmptyBorder(5, 15, 5, 15)); 

        // Icono (Carpeta) o fallback
        JLabel iconLabel = new JLabel(carpetaIcon != null ? carpetaIcon : new JLabel("📁").getIcon());
        
        // Texto del elemento: estilo y color. Si está seleccionado se muestra en negrita.
        JLabel text = new JLabel(value.toString());
        text.setFont(new Font("SansSerif", isSelected ? Font.BOLD : Font.PLAIN, 15));
        text.setForeground(new Color(50, 50, 50));
        
        text.setPreferredSize(new Dimension(170, 30));

        JLabel arrow = new JLabel("›");
        arrow.setForeground(new Color(100, 100, 100));
        arrow.setFont(new Font("Monospaced", Font.BOLD, 22));

        p.add(iconLabel, BorderLayout.WEST);
        p.add(text, BorderLayout.CENTER);
        p.add(arrow, BorderLayout.EAST);

        // Si la celda está seleccionada, coloreamos el fondo con una capa translúcida
        // para mantener el aspecto 'tarjeta' sin perder la sensación de profundidad.
        if (isSelected) {
            p.setOpaque(true);
            p.setBackground(new Color(255, 255, 255, 120)); // Fondo blanco translúcido
        }
        return p;
    }

    private ImageIcon loadIcon(String path, int w, int h) {
        try {
            java.net.URL url = getClass().getResource(path);
            if (url != null) {
                return new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
            }
        } catch (Exception e) {}
        return null;
    }
}