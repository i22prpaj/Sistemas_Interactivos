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
        // `getClass().getResource(path)` busca un recurso en el classpath y
        // devuelve una `java.net.URL` apuntando al mismo si lo encuentra.
        // - `getClass()` devuelve el objeto `Class` de la instancia actual;
        //   al llamar `getResource(path)` sobre ese `Class` la búsqueda usa
        //   el paquete de la clase como base cuando `path` no comienza con '/'.
        //   Por ejemplo, `getClass().getResource("ico.png")` busca en el
        //   mismo paquete que esta clase, mientras que `getClass().getResource("/res/ico.png")`
        //   busca desde la raíz del classpath.
        // - Alternativa equivalente y más explícita en contextos estáticos es
        //   usar `MobileListRenderer.class.getResource(path)`.
        // - La `URL` devuelta puede apuntar a recursos dentro de un JAR
        //   (p. ej. protocolo `jar:file:...`) o a archivos en disco según
        //   cómo se empaquete la aplicación; `ImageIcon(URL)` puede leer
        //   ambos casos directamente.
        try {
            java.net.URL url = getClass().getResource(path);
            if (url != null) {
                // Creamos un ImageIcon temporal a partir de la URL y extraemos
                // su `Image` para escalarla con `getScaledInstance(...)`.
                // `Image.SCALE_SMOOTH` solicita un algoritmo de escala de
                // alta calidad (pero más lento). Finalmente envolvemos la
                // imagen escalada en un nuevo `ImageIcon` que es lo que usa
                // Swing para pintar iconos en JLabel, JButton, etc.
                return new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
            }
        } catch (Exception e) {
            // Intencionalmente silencioso: si algo falla al cargar/parsear
            // el recurso devolvemos `null` y el llamador debe proporcionar
            // un fallback (p. ej. un emoji o icono por defecto).
        }

        // Si no encontramos el recurso o ocurre un error devolvemos `null`.
        // Esto permite al renderer decidir un fallback razonable.
        return null;
    }
}