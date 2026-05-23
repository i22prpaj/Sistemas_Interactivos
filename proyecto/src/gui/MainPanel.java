package gui;

// Importaciones: AWT para gráficos y eventos, Swing para componentes UI, utilidades, y clases de nuestro modelo
import java.awt.*;                        // Clases AWT (Color, Dimension, Cursor, Graphics...)
import java.util.ResourceBundle;          // Para localizar textos
import javax.swing.*;                     // Componentes Swing
import javax.swing.border.EmptyBorder;    // Borde vacío para márgenes
import javax.swing.event.DocumentEvent;   // Eventos de documento para el buscador
import javax.swing.event.DocumentListener;// Listener para cambios en el Document
import java.awt.event.FocusAdapter;       // Adaptador para foco
import java.awt.event.FocusEvent;         // Evento de foco
import main.MainFrame;                    // Frame principal para navegación y estado global
import model.BotonRedondeado;             // Botón personalizado con bordes redondeados
import model.MobileListRenderer;          // Renderer para las celdas de la lista (estilo móvil)

// Panel principal de la aplicación: lista asignaturas, buscador, filtros por curso y navegación.
public class MainPanel extends JPanel {

    // Clase auxiliar que representa una opción de asignatura con clave, etiqueta localizada y año.
    private static final class SubjectOption {
        private final String key;   // clave interna para localizar perfiles/asignaturas
        private final String label; // texto visible (localizado)
        private final int year;     // año: 0 = Todo, 1-4 = curso concreto

        private SubjectOption(String key, String label, int year) {
            this.key = key;
            this.label = label;
            this.year = year;
        }

        @Override
        public String toString() {
            return label; // JList usará toString() para mostrar la etiqueta
        }
    }

    // Clase auxiliar simplificada para representar una opción de profesor en listas (solo nombre visible).
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

    // Referencia al MainFrame para cambiar vistas y almacenar selección global
    private MainFrame mainFrame;
    // Colores usados en la UI como constantes para mantener coherencia visual
    private final Color VERDE_FONDO = new Color(180,255,104);
    private final Color VERDE_BOTON = new Color(212,255,189);
    private final Color GRIS_BUSCADOR = new Color(235, 230, 240);
    private final Color BLANCO_TRANSLUCIDO = new Color(255, 255, 255, 150);

    // Constructor: monta la interfaz principal con buscador, lista y navegación
    public MainPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame; // guardar referencia al frame principal
        ResourceBundle textos = mainFrame.getBundle(); // cargar bundle de textos

        setBackground(VERDE_FONDO); // color de fondo del panel
        // Usamos BorderLayout para anclar el scroll al centro y la navegación al sur
        setLayout(new BorderLayout());

        // --- CONTENEDOR PRINCIPAL (contenido que irá dentro del JScrollPane) ---
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL; // los elementos se estiran horizontalmente
        gbc.weightx = 1.0;
        gbc.insets = new Insets(5, 15, 5, 15); // separaciones generales

        // --- 1. HEADER: logo + título + notificaciones ---
        JPanel header = new JPanel(new BorderLayout(8, 0));
        header.setOpaque(false);

        ImageIcon logoIcon = loadScaledIcon(30, 30, "/resources/logo-ing-informtica.png");
        header.add(new JLabel(logoIcon != null ? logoIcon : new ImageIcon()), BorderLayout.WEST); // logo (si existe)

        JLabel title = new JLabel(textos.getString("grado.informatica"));
        title.setFont(new Font("SansSerif", Font.BOLD, 13));
        header.add(title, BorderLayout.CENTER); // título centrado

        // Botón de notificaciones (actualmente sin funcionalidad más allá del icono)
        JButton notif = crearBotonIcono("/resources/notif.png", "/resources/notif.PNG", "🔔", 22);
        header.add(notif, BorderLayout.EAST);

        gbc.gridy = 0;
        contentPanel.add(header, gbc);

        // --- 2. FILA DE BÚSQUEDA Y SETTINGS ---
        JPanel searchRow = new JPanel(new BorderLayout(10, 0));
        searchRow.setOpaque(false);

        // Campo de búsqueda personalizado: dibuja un fondo redondeado antes de pintar el texto
        JTextField search = new JTextField(textos.getString("principal.buscar")) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(GRIS_BUSCADOR);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30); // fondo redondeado
                super.paintComponent(g);
                g2.dispose();
            }
        };
        search.setOpaque(false);
        search.setBorder(new EmptyBorder(0, 15, 0, 5));
        search.setPreferredSize(new Dimension(150, 35));

        // Placeholder dinámico: desaparece al ganar foco y reaparece si está vacío
        String placeholderText = textos.getString("principal.buscar");
        search.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (search.getText().equals(placeholderText)) {
                    search.setText("");
                    search.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (search.getText().isEmpty()) {
                    search.setText(placeholderText);
                    search.setForeground(new Color(150, 150, 150));
                }
            }
        });

        searchRow.add(search, BorderLayout.CENTER); // añadir buscador a la fila

        // Botón de configuración que lleva a la vista de configuración
        JButton settings = crearBotonIcono("/resources/settings.png", "/resources/settings.PNG", "⚙", 24);
        settings.addActionListener(e -> mainFrame.showView("CONFIGURACION"));
        searchRow.add(settings, BorderLayout.EAST);

        gbc.gridy = 1;
        gbc.insets = new Insets(10, 15, 10, 15);
        contentPanel.add(searchRow, gbc);

        // --- 3. ETIQUETA 'ASIGNATURAS' ---
        JLabel lbl = new JLabel(textos.getString("subjects.label"));
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 20, 5, 15);
        contentPanel.add(lbl, gbc);

        // --- PREPARAR LISTA DE ASIGNATURAS ---
        SubjectOption[] allSubjects = {
            // 1º curso
            new SubjectOption("subjects.intro_programacion", textos.getString("subjects.intro_programacion"), 1),
            new SubjectOption("subjects.estadistica", textos.getString("subjects.estadistica"), 1),
            new SubjectOption("subjects.fisica", textos.getString("subjects.fisica"), 1),
            new SubjectOption("subjects.economia", textos.getString("subjects.economia"), 1),
            new SubjectOption("common.calculus", textos.getString("common.calculus"), 1),
            new SubjectOption("subjects.metodologia_programacion", textos.getString("subjects.metodologia_programacion"), 1),
            new SubjectOption("subjects.fundamentos_computadores", textos.getString("subjects.fundamentos_computadores"), 1),
            new SubjectOption("subjects.circuitos", textos.getString("subjects.circuitos"), 1),
            new SubjectOption("subjects.matematica_discreta", textos.getString("subjects.matematica_discreta"), 1),
            new SubjectOption("common.linear_algebra", textos.getString("common.linear_algebra"), 1),
            // 2º curso
            new SubjectOption("subjects.poo", textos.getString("subjects.poo"), 2),
            new SubjectOption("subjects.bases_datos", textos.getString("subjects.bases_datos"), 2),
            new SubjectOption("subjects.sistemas_operativos", textos.getString("subjects.sistemas_operativos"), 2),
            new SubjectOption("subjects.ingenieria_software", textos.getString("subjects.ingenieria_software"), 2),
            new SubjectOption("subjects.arquitectura_computadores", textos.getString("subjects.arquitectura_computadores"), 2),
            new SubjectOption("subjects.programacion_administracion", textos.getString("subjects.programacion_administracion"), 2),
            new SubjectOption("subjects.estructuras_datos", textos.getString("subjects.estructuras_datos"), 2),
            new SubjectOption("subjects.sistemas_informacion", textos.getString("subjects.sistemas_informacion"), 2),
            new SubjectOption("subjects.sistemas_inteligentes", textos.getString("subjects.sistemas_inteligentes"), 2),
            new SubjectOption("subjects.arquitectura_redes", textos.getString("subjects.arquitectura_redes"), 2),
            // 3º curso
            new SubjectOption("subjects.programacion_web", textos.getString("subjects.programacion_web"), 3),
            new SubjectOption("subjects.redes", textos.getString("subjects.redes"), 3),
            new SubjectOption("subjects.legislacion", textos.getString("subjects.legislacion"), 3),
            // 4º curso
            new SubjectOption("subjects.proyectos", textos.getString("subjects.proyectos"), 4),
        };

        DefaultListModel<SubjectOption> listModel = new DefaultListModel<>();
        for (SubjectOption s : allSubjects) {
            listModel.addElement(s);
        }

        // --- FILTRADO: funciones auxiliares para filtrar por año y texto de búsqueda ---
        final int[] selectedYear = {0}; // 0 = Todo (valor compartido que puede mutar desde lambdas)
        java.util.function.Supplier<String> effectiveSearchText = () -> {
            String currentText = search.getText();
            return placeholderText.equals(currentText) ? "" : currentText; // ignorar placeholder
        };
        java.util.function.BiConsumer<Integer, String> updateListFilter = (yearIndex, searchText) -> {
            listModel.clear();
            String query = searchText.toLowerCase().trim();

            for (SubjectOption s : allSubjects) {
                boolean matchesYear = (yearIndex == 0 || s.year == yearIndex);
                boolean matchesSearch = query.isEmpty() || s.label.toLowerCase().contains(query);

                if (matchesYear && matchesSearch) {
                    listModel.addElement(s);
                }
            }
        };

        // --- 4. TABS (filtros por curso) ---
        JPanel tabsContainer = new JPanel(new GridLayout(1, 5)) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(BLANCO_TRANSLUCIDO);
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15); // fondo blanco translúcido redondeado
                super.paintComponent(g);
            }
        };
        tabsContainer.setOpaque(false);
        tabsContainer.setPreferredSize(new Dimension(280, 40));

        String[] años = {"Todo", "1º", "2º", "3º", "4º"};
        JButton[] yearButtons = new JButton[años.length];

        for (int i = 0; i < años.length; i++) {
            final int yearIndex = i; // capturar índice para el listener
            JButton b = new JButton(años[i]);
            yearButtons[i] = b;
            b.setFocusPainted(false);
            b.setContentAreaFilled(false);
            b.setBorderPainted(false);
            b.setFont(new Font("SansSerif", i == 0 ? Font.BOLD : Font.PLAIN, 12));
            b.setForeground(i == 0 ? new Color(100, 100, 255) : Color.DARK_GRAY);

            if (i == 0) {
                // Marcar la pestaña 'Todo' visualmente con una línea inferior
                b.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(100, 100, 255)));
            }

            // Listener: al pulsar actualiza el filtro y el estilo de los botones
            b.addActionListener(e -> {
                selectedYear[0] = yearIndex;
                updateListFilter.accept(yearIndex, effectiveSearchText.get());

                for (int j = 0; j < yearButtons.length; j++) {
                    if (j == yearIndex) {
                        yearButtons[j].setFont(new Font("SansSerif", Font.BOLD, 12));
                        yearButtons[j].setForeground(new Color(100, 100, 255));
                        yearButtons[j].setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(100, 100, 255)));
                    } else {
                        yearButtons[j].setFont(new Font("SansSerif", Font.PLAIN, 12));
                        yearButtons[j].setForeground(Color.DARK_GRAY);
                        yearButtons[j].setBorder(null);
                    }
                }
            });

            tabsContainer.add(b);
        }
        gbc.gridy = 3;
        gbc.insets = new Insets(5, 15, 5, 15);
        contentPanel.add(tabsContainer, gbc);

        // Añadir escucha de cambios en el campo de búsqueda para actualizar el filtro en tiempo real
        search.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { updateSearch(); }
            @Override
            public void removeUpdate(DocumentEvent e) { updateSearch(); }
            @Override
            public void changedUpdate(DocumentEvent e) { updateSearch(); }

            private void updateSearch() {
                updateListFilter.accept(selectedYear[0], effectiveSearchText.get());
            }
        });

        // Estado inicial: mostrar todas las asignaturas
        updateListFilter.accept(0, "");

        // --- 5. LISTA DE ASIGNATURAS ---
        JList<SubjectOption> list = new JList<>(listModel);
        list.setCellRenderer(new MobileListRenderer()); // render personalizado
        list.setBackground(VERDE_FONDO);
        list.setFixedCellHeight(50);
        list.setVisibleRowCount(allSubjects.length); // intento de mostrar todos los elementos

        // Al hacer click en un elemento de la lista, guardamos la selección y vamos a la vista de asignaturas
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
        gbc.fill = GridBagConstraints.BOTH; // la lista ocupará espacio vertical
        // Añadimos la lista directamente; el JScrollPane externo manejará el scroll
        contentPanel.add(list, gbc);

        // --- SCROLL GLOBAL: envolver el contentPanel en un JScrollPane ---
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        boolean isCodespaces = "true".equalsIgnoreCase(System.getenv("CODESPACES"));
        // En entornos Codespaces permitir scroll vertical, en local lo deshabilitamos para UI estilo móvil
        scrollPane.setVerticalScrollBarPolicy(isCodespaces ? JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED : JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // velocidad de scroll

        add(scrollPane, BorderLayout.CENTER); // añadir scroll al centro del panel principal

        // --- 6. NAVEGACIÓN INFERIOR: botón Atrás centrado ---
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottom.setOpaque(false);

        BotonRedondeado back = new BotonRedondeado(" ← ");
        back.setBackground(VERDE_BOTON);
        back.setPreferredSize(new Dimension(65, 36));
        back.addActionListener(e -> mainFrame.goBack()); // volver a la vista anterior

        bottom.add(back);
        add(bottom, BorderLayout.SOUTH);
    }

    // Crea un botón que intenta cargar un icono redimensionado; si no, usa un texto alternativo.
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

    // Intenta cargar la primera ruta disponible y escalar la imagen al tamaño pedido.
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