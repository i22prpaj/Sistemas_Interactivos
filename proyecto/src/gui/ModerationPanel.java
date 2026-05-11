package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import main.MainFrame;
import model.BotonRedondeado;
import model.JPanelRedondeado;

public class ModerationPanel extends JPanel {

    // Colores exactos extraídos de image_758859.png
    private static final Color BG = new Color(199, 255, 126); 
    private static final Color PILL_BG = new Color(223, 246, 187);
    private static final Color CARD_WHITE = Color.WHITE;
    
    private static final Color RED_ACCENT = new Color(235, 77, 75);
    private static final Color GREEN_ACCENT = new Color(38, 194, 129);
    private static final Color PURPLE_TEXT = new Color(123, 74, 255);
    private static final Color WARNING_YELLOW = new Color(255, 243, 205);
    private static final Color WARNING_TEXT = new Color(133, 100, 4);

    private final MainFrame mainFrame;
    private final java.util.ResourceBundle bundle;
    private final boolean runningInCodespaces;

    public ModerationPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.bundle = mainFrame != null ? mainFrame.getBundle() : java.util.ResourceBundle.getBundle("bundle.Bundle", java.util.Locale.getDefault());
        this.runningInCodespaces = "true".equalsIgnoreCase(System.getenv("CODESPACES"));
        setBackground(BG);
        setLayout(new BorderLayout());

        // USAMOS NUESTRO PANEL ESPECIAL QUE NO SE DESBORDA HORIZONTALMENTE
        ScrollablePanel container = new ScrollablePanel();
        container.setOpaque(false);
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBorder(new EmptyBorder(40, 25, 20, 25));

        // 1. Título y Campana
        container.add(createTopBar());
        container.add(Box.createVerticalStrut(25));

        // 2. Burbujas de Estadísticas
        container.add(createStatsRow());
        container.add(Box.createVerticalStrut(30));

        // 3. Selector de pestañas (Pills)
        container.add(createTabSelector());
        container.add(Box.createVerticalStrut(25));

        // 4. Lista de tarjetas
        container.add(createCardList());
        
        // --- SCROLL INVISIBLE ESTILO MÓVIL ---
        JScrollPane scrollPane = new JScrollPane(container);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(
            runningInCodespaces ? JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED : JScrollPane.VERTICAL_SCROLLBAR_NEVER
        );
        scrollPane.setWheelScrollingEnabled(true);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Scroll suave y rápido
        if (runningInCodespaces) {
            scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        }

        // Añadimos el Scroll al centro y la navegación fija al sur
        add(scrollPane, BorderLayout.CENTER);
        add(createBottomNav(), BorderLayout.SOUTH);
    }

    // --- EL TRUCO PARA EVITAR EL DESBORDAMIENTO HORIZONTAL ---
    class ScrollablePanel extends JPanel implements Scrollable {
        @Override
        public Dimension getPreferredScrollableViewportSize() { return super.getPreferredSize(); }
        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) { return 16; }
        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) { return 16; }
        @Override
        public boolean getScrollableTracksViewportWidth() { return true; } // ¡ESTO EVITA QUE CRESCA HACIA LA DERECHA!
        @Override
        public boolean getScrollableTracksViewportHeight() { return false; }
    }

    private JPanel createTopBar() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(500, 40));

        JLabel title = new JLabel(bundle.getString("moderation.title"));
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(new Color(20, 20, 20));

        p.add(title, BorderLayout.WEST);
        p.add(new NotificationIcon(3), BorderLayout.EAST);
        return p;
    }

    private JPanel createStatsRow() {
        JPanel row = new JPanel(new GridLayout(1, 3, 15, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(500, 80));

        row.add(new StatBubble("3", bundle.getString("moderation.pendientes")));
        row.add(new StatBubble("5", bundle.getString("moderation.hoy")));
        row.add(new StatBubble("2", bundle.getString("moderation.urgentes")));
        return row;
    }

    private JPanel createTabSelector() {
        JPanelRedondeado wrapper = new JPanelRedondeado(40);
        wrapper.setBackground(new Color(235, 250, 210)); // Sutil contraste
        wrapper.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapper.setMaximumSize(new Dimension(380, 50));

        wrapper.add(createTabButton(bundle.getString("moderation.tab.pending"), true));
        wrapper.add(createTabButton(bundle.getString("moderation.tab.approved"), false));
        wrapper.add(createTabButton(bundle.getString("moderation.tab.deleted"), false));

        return wrapper;
    }

    private JButton createTabButton(String text, boolean active) {
        JButton b = new JButton(text);
        b.setFont(new Font("SansSerif", active ? Font.BOLD : Font.PLAIN, 12));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        
        if (active) {
            b.setOpaque(true);
            b.setBackground(Color.WHITE);
            b.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        }
        return b;
    }

    private JPanel createCardList() {
        JPanel list = new JPanel();
        list.setOpaque(false);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));

        // Tarjeta 1
        list.add(createReportCard("moderation.card1", true, RED_ACCENT));
        
        list.add(Box.createVerticalStrut(20));

        // Tarjeta 2
        list.add(createReportCard("moderation.card2", false, new Color(250, 190, 100)));

        return list;
    }

    private JComponent createReportCard(String keyPrefix, boolean warn, Color tagColor) {
        return new ReportCard(
            bundle.getString(keyPrefix + ".tag"),
            bundle.getString(keyPrefix + ".body"),
            bundle.getString(keyPrefix + ".prof"),
            bundle.getString(keyPrefix + ".time"),
            warn,
            tagColor
        );
    }

    private JPanel createBottomNav() {
        // Usamos el Grid exacto de 3 columnas para centrado perfecto como arreglamos en Configuración
        JPanel footerPanel = new JPanel(new GridLayout(1, 3)); 
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));

        // Columna 1 (Izquierda): Vacía
        JPanel leftPanel = new JPanel();
        leftPanel.setOpaque(false);

        // Columna 2 (Centro): Botón Inicio
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        centerPanel.setOpaque(false);
        BotonRedondeado inicio = new BotonRedondeado(bundle.getString("moderation.btn_home"));
        inicio.setPreferredSize(new Dimension(100, 40)); 
        inicio.setBackground(new Color(225, 255, 190));
        inicio.addActionListener(e -> {
            if (mainFrame != null) mainFrame.showView("MAIN_ESTUDIANTE");
        });
        centerPanel.add(inicio);

        // Columna 3 (Derecha): Botón Atrás
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPanel.setOpaque(false);
        BotonRedondeado back = new BotonRedondeado(" ← ");
        back.setPreferredSize(new Dimension(60, 40));
        back.setBackground(new Color(225, 255, 190));
        back.addActionListener(e -> {
            if (mainFrame != null) mainFrame.goBack();
        });
        rightPanel.add(back);

        footerPanel.add(leftPanel);
        footerPanel.add(centerPanel);
        footerPanel.add(rightPanel);

        return footerPanel;
    }

    // --- SUBCOMPONENTES ---

    class StatBubble extends JPanelRedondeado {
        StatBubble(String num, String text) {
            super(35); 
            setBackground(PILL_BG);
            setLayout(new GridLayout(2, 1));
            setBorder(new EmptyBorder(10, 5, 10, 5));
            
            JLabel lNum = new JLabel(num, SwingConstants.CENTER);
            lNum.setFont(new Font("SansSerif", Font.BOLD, 18)); 
            
            JLabel lText = new JLabel(text, SwingConstants.CENTER);
            lText.setFont(new Font("SansSerif", Font.PLAIN, 12));
            
            add(lNum);
            add(lText);
        }
    }

    class ReportCard extends JPanelRedondeado {
        ReportCard(String tag, String body, String prof, String time, boolean warn, Color tagColor) {
            super(25);
            setBackground(CARD_WHITE);
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 8, 0, 0, tagColor),
                new EmptyBorder(15, 15, 15, 15) 
            ));

            // Contenido Texto
            JPanel info = new JPanel();
            info.setOpaque(false);
            info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

            JLabel lblTag = new JLabel(" 🚫 " + tag + " ");
            lblTag.setFont(new Font("SansSerif", Font.BOLD, 11));
            lblTag.setForeground(tagColor);
            lblTag.setOpaque(true);
            lblTag.setBackground(new Color(tagColor.getRed(), tagColor.getGreen(), tagColor.getBlue(), 30));

            JLabel lblBody = new JLabel("<html><b>" + body + "</b></html>");
            lblBody.setFont(new Font("SansSerif", Font.PLAIN, 14)); 

            JLabel lblMeta = new JLabel("📚 " + prof + " • " + time);
            lblMeta.setForeground(Color.GRAY);
            lblMeta.setFont(new Font("SansSerif", Font.PLAIN, 11));

            info.add(lblTag);
            info.add(Box.createVerticalStrut(10));
            info.add(lblBody);
            info.add(Box.createVerticalStrut(10));
            info.add(lblMeta);

            if(warn) {
                JLabel lblWarn = new JLabel(bundle.getString("moderation.warn.auto_filter"));
                lblWarn.setOpaque(true);
                lblWarn.setBackground(WARNING_YELLOW);
                lblWarn.setForeground(WARNING_TEXT);
                lblWarn.setBorder(new EmptyBorder(8, 10, 8, 10));
                info.add(Box.createVerticalStrut(12));
                info.add(lblWarn);
            }

            // Botones Acción
            JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 10)); 
            actions.setOpaque(false);

            JButton btnVer = new JButton(bundle.getString("moderation.view_details"));
            btnVer.setForeground(PURPLE_TEXT);
            btnVer.setFont(new Font("SansSerif", Font.BOLD, 13));
            btnVer.setBorder(BorderFactory.createLineBorder(PURPLE_TEXT, 1));
            btnVer.setContentAreaFilled(false);
            btnVer.setPreferredSize(new Dimension(150, 35)); 
            
            btnVer.addActionListener(e -> {
                if (mainFrame != null) {
                    mainFrame.showView("REPORT_DETAIL");
                }
            });

            // Creamos los botones
            JButton btnCheck = createIconButton("✓", GREEN_ACCENT);
            JButton btnCross = createIconButton("✕", RED_ACCENT);

            actions.add(btnVer);
            actions.add(btnCheck);
            actions.add(btnCross);

            add(info, BorderLayout.CENTER);
            add(actions, BorderLayout.SOUTH);
        }

        private JButton createIconButton(String icon, Color bg) {
            JButton b = new JButton(icon) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    // Forzamos a dibujar nuestro propio color de fondo
                    g2.setColor(bg);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    
                    super.paintComponent(g);
                    g2.dispose();
                }
            };
            
            b.setPreferredSize(new Dimension(38, 32)); 
            b.setForeground(Color.WHITE);
            b.setFont(new Font("SansSerif", Font.BOLD, 15));
            
            // LA CLAVE MAGICA: Quitamos los márgenes internos para que el símbolo quepa perfectamente
            b.setMargin(new Insets(0, 0, 0, 0));
            
            b.setContentAreaFilled(false); 
            b.setBorderPainted(false);
            b.setFocusPainted(false);
            
            return b;
        }
    }

    class NotificationIcon extends JComponent {
        int val;
        private ImageIcon notifIcon;
        
        NotificationIcon(int v) { 
            this.val = v; 
            setPreferredSize(new Dimension(40, 40));
            try {
                notifIcon = new ImageIcon(getClass().getResource("/resources/notif.PNG"));
            } catch (Exception ex) {
                notifIcon = null;
            }
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Dibujar la imagen si está disponible
            if (notifIcon != null) {
                Image img = notifIcon.getImage();
                g2.drawImage(img, 2, 2, 28, 28, this);
            } else {
                // Fallback: dibujar emoji si no encuentra la imagen
                g2.setFont(new Font("SansSerif", Font.PLAIN, 20));
                g2.drawString("🔔", 5, 25);
            }
            
            // Badge con el número
            g2.setColor(RED_ACCENT);
            g2.fillOval(20, 5, 18, 18);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 10));
            g2.drawString(String.valueOf(val), 26, 18);
        }
    }
}