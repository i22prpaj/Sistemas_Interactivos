package gui;

import java.awt.*;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import main.MainFrame;
import model.BotonRedondeado;
import model.JPanelRedondeado;

public class ProfesorDetalle extends JPanel {

    private MainFrame mainFrame;
    private final Color VERDE_FONDO = new Color(180, 255, 104);
    private final Color GRIS_TARJETA = new Color(220, 220, 220);
    private final Color BLANCO_BOTON = Color.WHITE;
    private final boolean runningInCodespaces;

    public ProfesorDetalle(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.runningInCodespaces = "true".equalsIgnoreCase(System.getenv("CODESPACES"));
        ResourceBundle textos = mainFrame.getBundle();

        setBackground(VERDE_FONDO);
        setLayout(new BorderLayout());

        // USAMOS NUESTRO PANEL ESPECIAL PARA EVITAR DESBORDAMIENTOS HORIZONTALES
        ScrollablePanel contentPanel = new ScrollablePanel(new GridBagLayout());
        contentPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- 1. HEADER (Foto + Nombre + Departamento) ---
        JPanel header = new JPanel(new GridBagLayout());
        header.setOpaque(false);
        GridBagConstraints hGbc = new GridBagConstraints();

        // Icono de Usuario (grande)
        ImageIcon userIcon = loadScaledIcon(34, 34, "/resources/prof-user.PNG");
        JLabel userLabel = (userIcon != null) ? new JLabel(userIcon) : new JLabel("👤");
        hGbc.gridx = 0; hGbc.weightx = 0; hGbc.anchor = GridBagConstraints.NORTHWEST;
        hGbc.insets = new Insets(0, 0, 0, 15);
        header.add(userLabel, hGbc);

        // Nombre y Departamento
        JPanel textHeader = new JPanel(new GridLayout(2, 1));
        textHeader.setOpaque(false);
        JLabel name = new JLabel(textos.getString("profesor.nombre"));
        name.setFont(new Font("SansSerif", Font.BOLD, 16));
        JLabel dept = new JLabel(textos.getString("profesor.departamento"));
        dept.setFont(new Font("SansSerif", Font.PLAIN, 12));
        textHeader.add(name);
        textHeader.add(dept);

        hGbc.gridx = 1; hGbc.weightx = 1.0; hGbc.fill = GridBagConstraints.HORIZONTAL;
        hGbc.insets = new Insets(0, 0, 0, 0);
        header.add(textHeader, hGbc);

        gbc.gridy = 0;
        // Márgenes optimizados para alineación perfecta de la columna
        gbc.insets = new Insets(20, 15, 15, 15);
        contentPanel.add(header, gbc);

        // --- 2. VALORACIÓN (Cápsula Blanca con Estrellas) ---
        JPanelRedondeado valCard = new JPanelRedondeado(25);
        valCard.setBackground(BLANCO_BOTON);
        valCard.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 8));

        JLabel val = new JLabel("<html>" + textos.getString("profesor.valoracion") + ": <b><font color='#32CD32'>4.4</font>/5</b></html>");
        val.setFont(new Font("SansSerif", Font.PLAIN, 13));
        val.setOpaque(false);
        
        JLabel stars = new JLabel("★★★★☆"); 
        stars.setFont(new Font("SansSerif", Font.PLAIN, 16));
        stars.setForeground(new Color(255, 193, 7)); // Color dorado

        valCard.add(val);
        valCard.add(stars); 
        
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE; // Evita que la cápsula se estire
        gbc.insets = new Insets(0, 0, 15, 0);
        contentPanel.add(valCard, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL; // Restauramos para las siguientes tarjetas

        // --- 3. DATOS DE CONTACTO (Tarjeta Gris con Iconos) ---
        JPanelRedondeado datos = new JPanelRedondeado(15);
        datos.setBackground(GRIS_TARJETA);
        // ¡LA MAGIA AQUÍ! Un GridLayout garantiza matemáticamente el mismo espacio entre todas las filas
        datos.setLayout(new GridLayout(4, 1, 0, 12));
        datos.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Símbolos tipográficos clásicos que no se renderizan como "emojis gigantes"
        datos.add(createContactRow("⌂", textos.getString("profesor.despacho") + ": " + textos.getString("profesor.despacho_valor")));
        datos.add(createContactRow("✉", textos.getString("profesor.correo") + ": " + textos.getString("profesor.correo_valor")));
        datos.add(createContactRow("✆", textos.getString("profesor.telefono") + ": " + textos.getString("profesor.telefono_valor")));
        datos.add(createContactRow("◷", textos.getString("profesor.tutorias") + ": " + textos.getString("profesor.tutorias_valor")));

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 15, 15, 15);
        contentPanel.add(datos, gbc);

        // --- 4. ASIGNATURAS IMPARTIDAS (Tarjeta Gris con Título) ---
        gbc.gridy = 3;
        contentPanel.add(crearTarjetaGrisConTitulo(textos.getString("profesor.asignaturas_impartidas"), 
            new String[]{textos.getString("profesor.asig_algebra"), textos.getString("profesor.asig_calculo"), textos.getString("profesor.asig_fundamentos")}), gbc);

        // --- 5. CONSIDERACIONES (Tarjeta Gris con Título e Iconos) ---
        gbc.gridy = 4;
        contentPanel.add(crearTarjetaGrisConsideraciones(textos.getString("profesor.consideraciones"), textos), gbc);

        // --- 6. BOTÓN PUNTUAR (Cápsula Blanca con Sombra) ---
        BotonRedondeado btnPuntuar = new BotonRedondeado(textos.getString("profesor.puntuar"));
        btnPuntuar.setBackground(BLANCO_BOTON);
        btnPuntuar.setPreferredSize(new Dimension(140, 38));
        btnPuntuar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnPuntuar.addActionListener(e -> mainFrame.showView("VALORACION"));

        gbc.gridy = 5;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(10, 0, 30, 0); 
        contentPanel.add(btnPuntuar, gbc);

        // --- SCROLL INVISIBLE ESTILO MÓVIL ---
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        
        boolean isCodespaces = "true".equalsIgnoreCase(System.getenv("CODESPACES"));
        scrollPane.setVerticalScrollBarPolicy(isCodespaces ? JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED : JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); 
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        add(scrollPane, BorderLayout.CENTER);

        // --- 7. NAVEGACIÓN INFERIOR ---
        JPanel footerPanel = new JPanel(new GridLayout(1, 3)); 
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));

        JPanel leftPanel = new JPanel();
        leftPanel.setOpaque(false);

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        centerPanel.setOpaque(false);
        BotonRedondeado inicio = new BotonRedondeado(textos.getString("config.home"));
        inicio.setBackground(new Color(230, 255, 210)); 
        inicio.setPreferredSize(new Dimension(100, 36));
        inicio.addActionListener(e -> mainFrame.showView("MAIN_ESTUDIANTE"));
        centerPanel.add(inicio);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPanel.setOpaque(false);
        BotonRedondeado back = new BotonRedondeado(" ← ");
        back.setBackground(new Color(230, 255, 210));
        back.setPreferredSize(new Dimension(65, 36));
        back.addActionListener(e -> mainFrame.goBack());
        rightPanel.add(back);

        footerPanel.add(leftPanel);
        footerPanel.add(centerPanel);
        footerPanel.add(rightPanel);

        add(footerPanel, BorderLayout.SOUTH);
    }

    // --- CLASE AUXILIAR PARA EL SCROLL MÓVIL ---
    class ScrollablePanel extends JPanel implements Scrollable {
        public ScrollablePanel(LayoutManager layout) { super(layout); }
        @Override public Dimension getPreferredScrollableViewportSize() { return super.getPreferredSize(); }
        @Override public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) { return 16; }
        @Override public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) { return 16; }
        @Override public boolean getScrollableTracksViewportWidth() { return true; } 
        @Override public boolean getScrollableTracksViewportHeight() { return false; }
    }

    // --- MÉTODOS DE UTILIDAD ---

    private JPanel createContactRow(String icon, String text) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);
        
        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 18)); // Ligeramente más grande para que destaque
        iconLabel.setPreferredSize(new Dimension(25, 25)); // Caja fija para alineación perfecta
        
        JLabel textLabel = new JLabel("<html>" + text + "</html>"); 
        textLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        textLabel.setForeground(Color.DARK_GRAY);
        
        row.add(iconLabel, BorderLayout.WEST);
        row.add(textLabel, BorderLayout.CENTER);
        
        return row;
    }

    private JPanel crearTarjetaGrisConTitulo(String titulo, String[] items) {
        JPanelRedondeado tarjeta = new JPanelRedondeado(15);
        tarjeta.setBackground(GRIS_TARJETA);
        tarjeta.setLayout(new BorderLayout());
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTitulo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK), 
            new EmptyBorder(10, 15, 10, 15)
        ));
        tarjeta.add(lblTitulo, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(10, 15, 15, 15));

        for (String item : items) {
            JLabel lblItem = new JLabel(item);
            lblItem.setFont(new Font("SansSerif", Font.PLAIN, 13));
            lblItem.setBorder(new EmptyBorder(5, 0, 5, 0));
            content.add(lblItem);
        }
        tarjeta.add(content, BorderLayout.CENTER);
        
        return tarjeta;
    }

    private JPanel crearTarjetaGrisConsideraciones(String titulo, ResourceBundle textos) {
        JPanelRedondeado tarjeta = new JPanelRedondeado(15);
        tarjeta.setBackground(GRIS_TARJETA);
        tarjeta.setLayout(new BorderLayout());
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTitulo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK),
            new EmptyBorder(10, 15, 10, 15)
        ));
        tarjeta.add(lblTitulo, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(10, 15, 15, 15));

        String[] items = {
            textos.getString("profesor.considera_pasa_lista"),
            textos.getString("profesor.considera_explica_bien"),
            textos.getString("profesor.considera_revisa_practicas"),
            textos.getString("profesor.considera_hace_parciales")
        };
        ImageIcon check = loadScaledIcon(18, 18, "/resources/check.png"); 

        for (String item : items) {
            JPanel row = new JPanel(new BorderLayout());
            row.setOpaque(false);
            row.setBorder(new EmptyBorder(4, 0, 4, 0));

            JLabel lblItem = new JLabel(item);
            lblItem.setFont(new Font("SansSerif", Font.PLAIN, 12));
            
            JLabel lblCheck = (check != null) ? new JLabel(check) : new JLabel("✓");

            row.add(lblItem, BorderLayout.WEST);
            row.add(lblCheck, BorderLayout.EAST);
            content.add(row);
        }
        tarjeta.add(content, BorderLayout.CENTER);
        
        return tarjeta;
    }

    private ImageIcon loadScaledIcon(int width, int height, String... paths) {
        for (String path : paths) {
            try {
                java.net.URL url = getClass().getResource(path);
                if (url != null) {
                    return new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
                }
            } catch (Exception ignored) {}
        }
        return null;
    }
}