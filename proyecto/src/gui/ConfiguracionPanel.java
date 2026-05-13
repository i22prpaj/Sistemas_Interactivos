package gui;

import gui.ConfiguracionPanel.RoundButton;
import gui.ConfiguracionPanel.TogglePill;
import gui.ConfiguracionPanel.ToggleSwitch;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import main.MainFrame;
import model.CredentialStore;

public class ConfiguracionPanel extends JPanel {

    private final MainFrame mainFrame;
    private Timer cacheMessageTimer;

    public ConfiguracionPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        // Usamos BorderLayout para separar el contenido del footer
        setLayout(new BorderLayout());
        setBackground(new Color(212, 250, 187)); // Color verde más claro como la foto

        ResourceBundle bundle = mainFrame.getBundle();

        // 1. PANEL CENTRAL (Contenido con layout null para posicionamiento absoluto)
        JPanel contentPanel = new JPanel(null);
        contentPanel.setOpaque(false);
        
        // ===== TITULO =====
        JLabel title = new JLabel(bundle.getString("config.title"));
        title.setFont(new Font("Arial", Font.BOLD, 24)); // Reducido un poco para tamaño móvil
        title.setBounds(25, 20, 300, 40);
        contentPanel.add(title);

        ImageIcon rawUserIcon = null;
        try {
            rawUserIcon = new ImageIcon(getClass().getResource("/resources/logo_user.png"));
        } catch (Exception ex) {
            // fallback to text if resource not found
        }
        JLabel user;
        if (rawUserIcon != null) {
            Image img = rawUserIcon.getImage().getScaledInstance(35, 26, Image.SCALE_SMOOTH); // Ligeramente más pequeño
            user = new JLabel(new ImageIcon(img));
        } else {
            user = new JLabel(bundle.getString("config.user"));
            user.setFont(new Font("Serif", Font.PLAIN, 26));
        }
        user.setBounds(310, 25, 40, 40);
        contentPanel.add(user);

        JSeparator sep = new JSeparator();
        sep.setForeground(Color.BLACK);
        sep.setBounds(20, 70, 340, 2);
        contentPanel.add(sep);

        // ===== SECCIONES (Generadas dinámicamente para garantizar equidistancia) =====
        int startX = 25;
        int currentY = 100; // Punto de inicio
        int spacing = 60;   // Distancia EXACTA entre cada elemento

        JLabel accountSettingsLabel = createClickableLabel(bundle.getString("config.account_settings"), startX, currentY);
        accountSettingsLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainFrame.showView("AJUSTES_CUENTA");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                accountSettingsLabel.setForeground(new Color(80, 120, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                accountSettingsLabel.setForeground(new Color(40, 40, 40));
            }
        });
        contentPanel.add(accountSettingsLabel);
        currentY += spacing;
        
        contentPanel.add(createLabel(bundle.getString("config.language"), startX, currentY, true));
        contentPanel.add(new TogglePill(190, currentY - 8)); // Ajustado al centro del texto
        currentY += spacing;

        contentPanel.add(createLabel(bundle.getString("config.notifications"), startX, currentY, true));
        contentPanel.add(new ToggleSwitch(280, currentY - 2)); // Ajustado al centro del texto
        currentY += spacing;

        JLabel changePasswordLabel = createClickableLabel(bundle.getString("config.change_password"), startX, currentY);
        changePasswordLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainFrame.showView("CAMBIAR_CONTRASENA");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                changePasswordLabel.setForeground(new Color(80, 120, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                changePasswordLabel.setForeground(new Color(40, 40, 40));
            }
        });
        contentPanel.add(changePasswordLabel);
        currentY += spacing;

        JLabel clearCacheLabel = createClickableLabel(bundle.getString("config.clear_cache"), startX, currentY);
        clearCacheLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                showCacheMessage();
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                clearCacheLabel.setForeground(new Color(80, 120, 255));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                clearCacheLabel.setForeground(new Color(40, 40, 40));
            }
        });
        contentPanel.add(clearCacheLabel);
        currentY += spacing;

        // ===== PANEL DE MODERACIÓN (solo visible para moderadores) =====
        if (mainFrame.isModerator()) {
            JLabel modPanel = createClickableLabel(bundle.getString("common.moderation_panel"), startX, currentY);

            modPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    mainFrame.showView("MODERATION_PANEL"); // cambia al nombre real de tu vista
                }
            
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    modPanel.setForeground(new Color(70, 90, 200)); // hover azul
                }
            
                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    modPanel.setForeground(Color.BLACK);
                }
            });

            contentPanel.add(modPanel);
            currentY += spacing; // Sumamos espacio si existe
        }

        // ===== LOGOUT (Cerrar Sesión) =====
        JLabel logoutLabel = createLabelCentered(bundle.getString("config.logout"), startX, currentY, new Color(180, 40, 70));
        logoutLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                mainFrame.clearCurrentUserEmail();
                mainFrame.showView("LOGIN");
            }
        });
        contentPanel.add(logoutLabel);
        
        // Sumamos margen final para que no se corte justo al ras del texto
        currentY += 40; 

        // IMPORTANTE: Establecer el tamaño preferido del contenido interno para que el Scroll funcione
        contentPanel.setPreferredSize(new Dimension(350, currentY));

        // ===== SCROLL "INVISIBLE" ESTILO MÓVIL =====
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Quitamos borde oscuro
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        
        boolean isCodespaces = "true".equalsIgnoreCase(System.getenv("CODESPACES"));
        scrollPane.setVerticalScrollBarPolicy(isCodespaces ? JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED : JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Hace que el scroll sea rápido y suave

        add(scrollPane, BorderLayout.CENTER);

        // 2. PANEL FOOTER (Botones abajo) con posicionamiento matemáticamente exacto
        JPanel footerPanel = new JPanel(new GridLayout(1, 3)); // Divide en 3 columnas iguales
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20)); // Margen global del footer

        // Columna 1 (Izquierda): Panel vacío para equilibrar el peso
        JPanel leftPanel = new JPanel();
        leftPanel.setOpaque(false);

        // Columna 2 (Centro): Botón Inicio
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        centerPanel.setOpaque(false);
        RoundButton homeBtn = new RoundButton(bundle.getString("common.home"), 100, 40);
        homeBtn.addActionListener(e -> mainFrame.showView("MAIN_ESTUDIANTE"));
        centerPanel.add(homeBtn);

        // Columna 3 (Derecha): Botón Atrás
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPanel.setOpaque(false);
        RoundButton backBtn = new RoundButton(bundle.getString("config.back"), 60, 40); // Usando flecha unicode
        backBtn.addActionListener(e -> mainFrame.goBack());
        rightPanel.add(backBtn);

        footerPanel.add(leftPanel);
        footerPanel.add(centerPanel);
        footerPanel.add(rightPanel);

        JPanel bottomWrapper = new JPanel(new BorderLayout());
        bottomWrapper.setOpaque(false);

        JPanel messageBar = new JPanel(new BorderLayout());
        messageBar.setOpaque(false);
        messageBar.setBorder(BorderFactory.createEmptyBorder(0, 20, 8, 20));

        JLabel cacheMessageLabel = new JLabel("Se han liberado 2.37MB", SwingConstants.CENTER);
        cacheMessageLabel.setOpaque(true);
        cacheMessageLabel.setBackground(Color.BLACK);
        cacheMessageLabel.setForeground(Color.WHITE);
        cacheMessageLabel.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        cacheMessageLabel.setVisible(false);
        cacheMessageLabel.setFont(new Font("Arial", Font.BOLD, 13));

        messageBar.add(cacheMessageLabel, BorderLayout.CENTER);
        bottomWrapper.add(messageBar, BorderLayout.NORTH);
        bottomWrapper.add(footerPanel, BorderLayout.SOUTH);

        add(bottomWrapper, BorderLayout.SOUTH);
    }

    private void showCacheMessage() {
        Container south = getComponentCount() > 0 ? getComponent(1) instanceof Container ? (Container) getComponent(1) : null : null;
        if (south == null) {
            return;
        }

        JLabel cacheMessageLabel = findCacheMessageLabel(south);
        if (cacheMessageLabel == null) {
            return;
        }

        if (cacheMessageTimer != null && cacheMessageTimer.isRunning()) {
            cacheMessageTimer.stop();
        }

        cacheMessageLabel.setVisible(true);
        cacheMessageLabel.revalidate();
        cacheMessageLabel.repaint();

        System.gc();

        cacheMessageTimer = new Timer(2800, e -> {
            cacheMessageLabel.setVisible(false);
            cacheMessageLabel.revalidate();
            cacheMessageLabel.repaint();
        });
        cacheMessageTimer.setRepeats(false);
        cacheMessageTimer.start();
    }

    private JLabel findCacheMessageLabel(Container container) {
        for (Component child : container.getComponents()) {
            if (child instanceof JLabel label && "Se han liberado 2.37MB".equals(label.getText())) {
                return label;
            }
            if (child instanceof Container nested) {
                JLabel found = findCacheMessageLabel(nested);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    private JLabel createLabel(String text, int x, int y, boolean bold) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", bold ? Font.BOLD : Font.PLAIN, 16)); // Reducido de 18 a 16
        // aumentar ancho para evitar truncamientos
        label.setBounds(x, y, 300, 25);
        return label;
    }

    private JLabel createLabelCentered(String text, int x, int y, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16)); // Reducido de 18 a 16
        label.setForeground(color);
        // ocupar todo el ancho del panel
        label.setBounds(x, y, 300, 25);
        return label;
    }

    private JLabel createClickableLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16)); // Reducido de 18 a 16
        label.setBounds(x, y, 300, 25);

        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.setForeground(new Color(40, 40, 40));

        // Hover effect
        label.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                label.setForeground(new Color(80, 120, 255)); // azulito
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                label.setForeground(new Color(40, 40, 40));
            }
        });

        return label;
    }

    // ================== CLASES INTERNAS (Sin cambios mayores, solo ajustes de tamaño) ==================
    
    class TogglePill extends JComponent {
        boolean left;
        public TogglePill(int x, int y) {
            left = Locale.getDefault().getLanguage().equals("es");
            setBounds(x, y, 160, 45);
            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    left = !left;
                    repaint();

                    Locale selectedLocale = left ? new Locale("es", "ES") : Locale.ENGLISH;
                    Timer timer = new Timer(130, ev -> mainFrame.changeLanguage(selectedLocale, "CONFIGURACION"));
                    timer.setRepeats(false);
                    timer.start();
                }
            });
        }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(110, 125, 95));
            g2.fillRoundRect(0, 0, 150, 40, 40, 40);
            g2.setColor(new Color(135, 150, 120));
            if (left) g2.fillRoundRect(3, 3, 72, 34, 34, 34);
            else g2.fillRoundRect(75, 3, 72, 34, 34, 34);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.PLAIN, 12));
            g2.drawString("Español", 18, 25);
            g2.drawString("English", 90, 25);
        }
    }

    class ToggleSwitch extends JComponent {
        boolean on = true;
        public ToggleSwitch(int x, int y) {
            setBounds(x, y, 55, 30);
            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    on = !on; repaint();
                }
            });
        }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(on ? new Color(45, 35, 99) : Color.GRAY);
            g2.fillRoundRect(0, 0, 50, 28, 28, 28);
            g2.setColor(Color.WHITE);
            int circleX = on ? 24 : 3;
            g2.fillOval(circleX, 3, 22, 22);
        }
    }

    class RoundButton extends JButton {
        public RoundButton(String text, int width, int height) {
            super(text);
            setPreferredSize(new Dimension(width, height));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
        }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(185, 250, 130));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            g2.setColor(Color.BLACK);
            FontMetrics fm = g2.getFontMetrics();
            int tx = (getWidth() - fm.stringWidth(getText())) / 2;
            int ty = (getHeight() + fm.getAscent()) / 2 - 4;
            g2.drawString(getText(), tx, ty);
        }
    }
}