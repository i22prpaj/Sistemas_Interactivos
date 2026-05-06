package gui;

import main.MainFrame;
import model.BotonRedondeado;
import model.JPanelRedondeado;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

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

    public ModerationPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setBackground(BG);
        setLayout(new BorderLayout());

        // Usamos un JScrollPane invisible para permitir scroll si hay muchos reportes
        JPanel container = new JPanel();
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
        
        // 5. Botones inferiores fijos
        add(container, BorderLayout.CENTER);
        add(createBottomNav(), BorderLayout.SOUTH);
    }

    private JPanel createTopBar() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(500, 40));

        JLabel title = new JLabel("Panel de Moderación");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(new Color(20, 20, 20));

        p.add(title, BorderLayout.WEST);
        p.add(new NotificationIcon(3), BorderLayout.EAST);
        return p;
    }

    private JPanel createStatsRow() {
        JPanel row = new JPanel(new GridLayout(1, 3, 15, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(500, 80));

        row.add(new StatBubble("3", "Pendientes"));
        row.add(new StatBubble("5", "Hoy"));
        row.add(new StatBubble("2", "Urgentes"));
        return row;
    }

    private JPanel createTabSelector() {
        JPanelRedondeado wrapper = new JPanelRedondeado(40);
        wrapper.setBackground(new Color(235, 250, 210)); // Sutil contraste
        wrapper.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        wrapper.setMaximumSize(new Dimension(380, 50));

        wrapper.add(createTabButton("🟡 Pendientes", true));
        wrapper.add(createTabButton("✅ Aprobados", false));
        wrapper.add(createTabButton("🗑 Eliminados", false));

        return wrapper;
    }

    private JButton createTabButton(String text, boolean active) {
        JButton b = new JButton(text);
        b.setFont(new Font("SansSerif", active ? Font.BOLD : Font.PLAIN, 12));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        
        if (active) {
            // Efecto de píldora blanca interna para el activo
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
        list.add(new ReportCard(
            "Lenguaje ofensivo", 
            "\"Este profesor es un [***] y no sabe explicar nada. Todos los que...\"",
            "Antonio López Jiménez", "Hace 2h", true, RED_ACCENT
        ));
        
        list.add(Box.createVerticalStrut(20));

        // Tarjeta 2
        list.add(new ReportCard(
            "Difamación", 
            "\"Suspende a todos sus alumnos por capricho. El año pasado reprobó...\"",
            "María García Ruiz", "Hace 5h", false, new Color(250, 190, 100)
        ));

        return list;
    }

    private JPanel createBottomNav() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 20));
        p.setOpaque(false);

        BotonRedondeado inicio = new BotonRedondeado("Inicio");
        inicio.setPreferredSize(new Dimension(100, 40));
        inicio.setBackground(new Color(225, 255, 190));

        BotonRedondeado back = new BotonRedondeado(" ← ");
        back.setPreferredSize(new Dimension(60, 40));
        back.setBackground(new Color(225, 255, 190));

        p.add(inicio);
        p.add(back);
        return p;
    }

    // --- SUBCOMPONENTES CLAVADOS ---

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
                BorderFactory.createMatteBorder(0, 8, 0, 0, tagColor), // El borde de color lateral
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
                JLabel lblWarn = new JLabel("<html>⚠️ <b>Filtro automático:</b> detectadas 3 palabras prohibidas</html>");
                lblWarn.setOpaque(true);
                lblWarn.setBackground(WARNING_YELLOW);
                lblWarn.setForeground(WARNING_TEXT);
                lblWarn.setBorder(new EmptyBorder(8, 10, 8, 10));
                info.add(Box.createVerticalStrut(12));
                info.add(lblWarn);
            }

            // Botones Acción
            JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
            actions.setOpaque(false);

            JButton btnVer = new JButton("Ver detalles ↗");
            btnVer.setForeground(PURPLE_TEXT);
            btnVer.setFont(new Font("SansSerif", Font.BOLD, 13));
            btnVer.setBorder(BorderFactory.createLineBorder(PURPLE_TEXT, 1));
            btnVer.setContentAreaFilled(false);
            btnVer.setPreferredSize(new Dimension(150, 35));
            // Navegar a la vista de detalle del reporte
            btnVer.addActionListener(e -> {
                if (mainFrame != null) {
                    mainFrame.showView("REPORT_DETAIL");
                }
            });

            JButton btnCheck = createIconButton("✓", GREEN_ACCENT);
            JButton btnCross = createIconButton("✕", RED_ACCENT);

            actions.add(btnVer);
            actions.add(btnCheck);
            actions.add(btnCross);

            add(info, BorderLayout.CENTER);
            add(actions, BorderLayout.SOUTH);
        }

        private JButton createIconButton(String icon, Color bg) {
            JButton b = new JButton(icon);
            b.setPreferredSize(new Dimension(45, 35));
            b.setBackground(bg);
            b.setForeground(Color.WHITE);
            b.setFont(new Font("SansSerif", Font.BOLD, 16));
            b.setBorderPainted(false);
            b.setFocusPainted(false);
            return b;
        }
    }

    class NotificationIcon extends JComponent {
        int val;
        NotificationIcon(int v) { this.val = v; setPreferredSize(new Dimension(40, 40)); }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 20));
            g2.drawString("🔔", 5, 25);
            g2.setColor(RED_ACCENT);
            g2.fillOval(20, 5, 18, 18);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 10));
            g2.drawString(String.valueOf(val), 26, 18);
        }
    }
}