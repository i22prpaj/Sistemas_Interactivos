package gui;

import java.awt.*;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import main.MainFrame;
import model.BotonRedondeado;
import model.MobileListRenderer;

public class MainPanel extends JPanel {

    private static final class SubjectOption {
        private final String key;
        private final String label;

        private SubjectOption(String key, String label) {
            this.key = key;
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    private static final class ProfessorOption {
        private final String name;

        private ProfessorOption(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private MainFrame mainFrame;
    private final Color VERDE_FONDO = new Color(180,255,104);
    private final Color VERDE_BOTON = new Color(212,255,189); 
    private final Color GRIS_BUSCADOR = new Color(235, 230, 240);
    private final Color BLANCO_TRANSLUCIDO = new Color(255, 255, 255, 150);

    public MainPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        ResourceBundle textos = mainFrame.getBundle();

        setBackground(VERDE_FONDO);
        // Usamos BorderLayout para anclar el botón Atrás al fondo y el Scroll al centro
        setLayout(new BorderLayout());

        // --- CONTENEDOR PRINCIPAL (El que se va a deslizar) ---
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(5, 15, 5, 15);

        // --- 1. HEADER ---
        JPanel header = new JPanel(new BorderLayout(8, 0));
        header.setOpaque(false);

        ImageIcon logoIcon = loadScaledIcon(30, 30, "/resources/logo-ing-informtica.png");
        header.add(new JLabel(logoIcon != null ? logoIcon : new ImageIcon()), BorderLayout.WEST);

        JLabel title = new JLabel(textos.getString("grado.informatica"));
        title.setFont(new Font("SansSerif", Font.BOLD, 13)); 
        header.add(title, BorderLayout.CENTER);

        // Notificaciones -> Sin funcionalidad por ahora
        JButton notif = crearBotonIcono("/resources/notif.png", "/resources/notif.PNG", "🔔", 22);
        header.add(notif, BorderLayout.EAST);

        gbc.gridy = 0;
        contentPanel.add(header, gbc);

        // --- 2. BUSCADOR Y SETTINGS ---
        JPanel searchRow = new JPanel(new BorderLayout(10, 0)); 
        searchRow.setOpaque(false);

        JTextField search = new JTextField(textos.getString("principal.buscar")) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(GRIS_BUSCADOR);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        search.setOpaque(false);
        search.setBorder(new EmptyBorder(0, 15, 0, 5));
        search.setPreferredSize(new Dimension(150, 35)); 
        searchRow.add(search, BorderLayout.CENTER);

        // Settings -> Acción: CONFIGURACION
        JButton settings = crearBotonIcono("/resources/settings.png", "/resources/settings.PNG", "⚙", 24);
        settings.addActionListener(e -> mainFrame.showView("CONFIGURACION"));
        searchRow.add(settings, BorderLayout.EAST);

        gbc.gridy = 1;
        gbc.insets = new Insets(10, 15, 10, 15);
        contentPanel.add(searchRow, gbc);

        // --- 3. ETIQUETA ASIGNATURAS ---
        JLabel lbl = new JLabel(textos.getString("subjects.label"));
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 20, 5, 15); 
        contentPanel.add(lbl, gbc);

        // --- 4. TABS ---
        JPanel tabsContainer = new JPanel(new GridLayout(1, 5)) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(BLANCO_TRANSLUCIDO);
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g);
            }
        };
        tabsContainer.setOpaque(false);
        tabsContainer.setPreferredSize(new Dimension(280, 40));

        String[] años = {"Todo", "1º", "2º", "3º", "4º"};
        for (int i = 0; i < años.length; i++) {
            JButton b = new JButton(años[i]);
            b.setFocusPainted(false);
            b.setContentAreaFilled(false);
            b.setBorderPainted(false);
            b.setFont(new Font("SansSerif", i == 0 ? Font.BOLD : Font.PLAIN, 12));
            b.setForeground(i == 0 ? new Color(100, 100, 255) : Color.DARK_GRAY);
            
            if (i == 0) {
                b.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(100, 100, 255)));
            }
            tabsContainer.add(b);
        }
        gbc.gridy = 3;
        gbc.insets = new Insets(5, 15, 5, 15);
        contentPanel.add(tabsContainer, gbc);

        // --- 5. LISTA ---
        SubjectOption[] subjects = {
            new SubjectOption("subjects.intro_programacion", textos.getString("subjects.intro_programacion")),
            new SubjectOption("common.calculus", textos.getString("common.calculus")),
            new SubjectOption("subjects.estadistica", textos.getString("subjects.estadistica")),
            new SubjectOption("subjects.fisica", textos.getString("subjects.fisica")),
            new SubjectOption("subjects.economia", textos.getString("subjects.economia")),
            new SubjectOption("subjects.metodologia_programacion", textos.getString("subjects.metodologia_programacion")),
            new SubjectOption("subjects.fundamentos_computadores", textos.getString("subjects.fundamentos_computadores")),
            new SubjectOption("subjects.circuitos", textos.getString("subjects.circuitos")),
            new SubjectOption("subjects.matematica_discreta", textos.getString("subjects.matematica_discreta")),
            new SubjectOption("common.linear_algebra", textos.getString("common.linear_algebra")),
            new SubjectOption("subjects.poo", textos.getString("subjects.poo")),
            new SubjectOption("subjects.bases_datos", textos.getString("subjects.bases_datos")),
            new SubjectOption("subjects.sistemas_operativos", textos.getString("subjects.sistemas_operativos")),
            new SubjectOption("subjects.ingenieria_software", textos.getString("subjects.ingenieria_software")),
            new SubjectOption("subjects.arquitectura_computadores", textos.getString("subjects.arquitectura_computadores")),
            new SubjectOption("subjects.programacion_administracion", textos.getString("subjects.programacion_administracion")),
            new SubjectOption("subjects.estructuras_datos", textos.getString("subjects.estructuras_datos")),
            new SubjectOption("subjects.sistemas_informacion", textos.getString("subjects.sistemas_informacion")),
            new SubjectOption("subjects.sistemas_inteligentes", textos.getString("subjects.sistemas_inteligentes")),
            new SubjectOption("subjects.arquitectura_redes", textos.getString("subjects.arquitectura_redes")),
            new SubjectOption("subjects.programacion_web", textos.getString("subjects.programacion_web")),
            new SubjectOption("subjects.redes", textos.getString("subjects.redes")),
            new SubjectOption("subjects.legislacion", textos.getString("subjects.legislacion"))
        };
        JList<SubjectOption> list = new JList<>(subjects);
        list.setCellRenderer(new MobileListRenderer());
        list.setBackground(VERDE_FONDO);
        list.setFixedCellHeight(50);
        list.setVisibleRowCount(subjects.length); // Muestra todos los elementos

        list.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
                SubjectOption seleccionado = list.getSelectedValue();
                if (seleccionado != null) {
                    mainFrame.setSelectedSubjectKey(seleccionado.key);
                    mainFrame.setSelectedProfessorId(null);
                    mainFrame.setSelectedProfessorKey(null);
                    mainFrame.setSelectedProfessorName(null);
                    mainFrame.showView("ASIGNATURAS"); 
                }
            }
        });

        gbc.gridy = 4;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        // Añadimos la lista DIRECTAMENTE al panel (el scroll lo hará el JScrollPane global)
        contentPanel.add(list, gbc);

        // --- SCROLL GLOBAL TILO MÓVIL ---
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        
        boolean isCodespaces = "true".equalsIgnoreCase(System.getenv("CODESPACES"));
        scrollPane.setVerticalScrollBarPolicy(isCodespaces ? JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED : JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Prohibido barra horizontal
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Scroll rápido

        add(scrollPane, BorderLayout.CENTER);

        // --- 6. NAVEGACIÓN INFERIOR ---
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottom.setOpaque(false);

        // Atrás -> Acción: goBack (Se eliminó el botón de Inicio)
        BotonRedondeado back = new BotonRedondeado(" ← ");
        back.setBackground(VERDE_BOTON);
        back.setPreferredSize(new Dimension(65, 36));
        back.addActionListener(e -> mainFrame.goBack());

        bottom.add(back);

        add(bottom, BorderLayout.SOUTH);
    }

    private JButton crearBotonIcono(String p1, String p2, String bck, int s) {
        JButton btn = new JButton();
        ImageIcon icon = loadScaledIcon(s, s, p1, p2);
        if (icon != null) btn.setIcon(icon); else btn.setText(bck);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private ImageIcon loadScaledIcon(int w, int h, String... paths) {
        for (String p : paths) {
            try {
                java.net.URL url = getClass().getResource(p);
                if (url != null) return new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
            } catch (Exception ignored) {}
        }
        return null;
    }
}