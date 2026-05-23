package gui;

import main.MainFrame;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ResourceBundle;
import model.BotonRedondeado;
import model.JPanelRedondeado;
import model.ProfessorDirectory;
import model.ProfessorProfile;

public class ValoracionPanel extends JPanel {

    // Clase auxiliar simple que representa una opción de aspecto
    // key: identificador persistente que se guarda en ProfessorDirectory
    // label: texto mostrado al usuario (localizado vía ResourceBundle)
    private static final class AspectOption {
        private final String key;
        private final String label;

        private AspectOption(String key, String label) {
            this.key = key;
            this.label = label;
        }
    }

    private MainFrame mainFrame;
    // Colores usados en la UI (constantes para coherencia)
    private final Color VERDE_FONDO = new Color(180, 255, 104);
    private final Color GRIS_CLARITO = new Color(220, 220, 220);
    private final Color AZUL_OSCURO = new Color(30, 30, 80);
    private final Color AMARILLO_ESTRELLA = new Color(255, 200, 0);
    private final Color GRIS_ESTRELLA = new Color(190, 190, 190);

    // Constructor: monta la interfaz para valorar al profesor seleccionado
    public ValoracionPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        ResourceBundle textos = mainFrame.getBundle();
        ProfessorProfile profile = ProfessorDirectory.get(mainFrame.getSelectedProfessorId());
        if (profile == null) {
            profile = ProfessorDirectory.getDefaultProfile();
        }
        String professorName = mainFrame.getSelectedProfessorName();
        if (professorName == null && profile != null) {
            professorName = profile.getDisplayName();
        }

        setBackground(VERDE_FONDO);
        setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 16, 5, 16);

        // --- 1. TÍTULO ---
        JLabel title = new JLabel(textos.getString("valoracion.titulo") + (professorName != null ? " " + professorName : ""), SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 17));
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 12, 5, 12); // Margen para la "isla"
        contentPanel.add(title, gbc);

        // --- 2. SELECTOR DE ASIGNATURA ---
        String[] subjectLabels = profile != null ? profile.getLocalizedSubjectNames(textos) : new String[0];
        String[] asigs = new String[subjectLabels.length + 1];
        asigs[0] = textos.getString("valoracion.asignatura_placeholder");
        System.arraycopy(subjectLabels, 0, asigs, 1, subjectLabels.length);
        JComboBox<String> combo = new JComboBox<>(asigs);
        combo.setPreferredSize(new Dimension(160, 35));
        combo.setBackground(GRIS_CLARITO);
        // Quitamos el borde por defecto para que parezca más moderno
        combo.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 14, 5, 14);
        contentPanel.add(combo, gbc);

        // --- 3. ESTRELLAS ---
        // Creamos 5 botones con aspecto de estrella (JToggleButton). Cada uno escribe
        // su valor en el array `rating` cuando se pulsa. Usamos un array de 1
        // elemento como contenedor mutable accesible desde el ActionListener.
        JLabel lblPregunta = new JLabel(textos.getString("valoracion.pregunta"), SwingConstants.CENTER);
        lblPregunta.setFont(new Font("SansSerif", Font.BOLD, 13));
        
        gbc.gridy = 2;
        contentPanel.add(lblPregunta, gbc);

        JPanel estrellasIconos = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        estrellasIconos.setOpaque(false);
        ButtonGroup starGroup = new ButtonGroup();
        final int[] rating = {0};
        for(int i=0; i<5; i++) {
            final int starValue = i + 1;
            JToggleButton estrella = new JToggleButton("☆");
            estrella.setFont(new Font("Serif", Font.PLAIN, 30));
            estrella.setFocusPainted(false);
            estrella.setBorderPainted(false);
            estrella.setContentAreaFilled(false);
            estrella.setOpaque(false);
            estrella.setForeground(GRIS_ESTRELLA);
            estrella.addActionListener(e -> {
                rating[0] = starValue;
                updateStars(starGroup, estrellasIconos, rating[0]);
            });
            starGroup.add(estrella);
            estrellasIconos.add(estrella);
        }
        gbc.gridy = 3;
        contentPanel.add(estrellasIconos, gbc);

        // --- 4. ASPECTOS DESTACADOS (Cuadro Blanco) ---
        // Lista de aspectos que el usuario puede marcar; cada checkbox lleva
        // asociado en sus propiedades el `aspectKey` que se usará para persistir.
        JLabel lblAspectos = new JLabel(textos.getString("valoracion.aspectos"));
        lblAspectos.setFont(new Font("SansSerif", Font.BOLD, 13));
        gbc.gridy = 4;
        gbc.insets = new Insets(5, 12, 5, 12);
        contentPanel.add(lblAspectos, gbc);

        JPanelRedondeado cuadroBlanco = new JPanelRedondeado(20);
        cuadroBlanco.setBackground(Color.WHITE);
        cuadroBlanco.setLayout(new GridLayout(0, 1, 0, 5));
        cuadroBlanco.setBorder(new EmptyBorder(15, 14, 15, 14));

        AspectOption[] aspectos = {
            new AspectOption("profesor.considera_pasa_lista", textos.getString("profesor.considera_pasa_lista")),
            new AspectOption("valoracion.aspecto_lee_pdf", textos.getString("valoracion.aspecto_lee_pdf")),
            new AspectOption("valoracion.aspecto_examen_dificil", textos.getString("valoracion.aspecto_examen_dificil")),
            new AspectOption("valoracion.aspecto_asistencia_obligatoria", textos.getString("valoracion.aspecto_asistencia_obligatoria")),
            new AspectOption("valoracion.aspecto_examen_test", textos.getString("valoracion.aspecto_examen_test")),
            new AspectOption("valoracion.aspecto_muy_practico", textos.getString("valoracion.aspecto_muy_practico")),
            new AspectOption("valoracion.aspecto_buen_material", textos.getString("valoracion.aspecto_buen_material"))
        };
        
        // Guardamos las referencias a los checkboxes para leerlos al enviar
        java.util.List<JCheckBox> checkboxes = new java.util.ArrayList<>();
        for (AspectOption asp : aspectos) {
            JCheckBox cb = new JCheckBox(asp.label);
            cb.setOpaque(false);
            cb.putClientProperty("aspectKey", asp.key);
            cuadroBlanco.add(cb);
            checkboxes.add(cb);
        }

        gbc.gridy = 5;
        gbc.weighty = 0.2;
        gbc.fill = GridBagConstraints.BOTH;
        contentPanel.add(cuadroBlanco, gbc);

        // --- 5. COMENTARIOS (Cuadro Gris) ---
        // Texto multi-línea con placeholder; al ganar foco borramos el placeholder,
        // y al perder foco lo restauramos si el usuario no escribe nada.
        String comentarioPlaceholder = textos.getString("valoracion.comentario_placeholder");
        JTextArea txtComentario = new JTextArea(comentarioPlaceholder);
        txtComentario.setFont(new Font("SansSerif", Font.ITALIC, 12));
        txtComentario.setForeground(Color.GRAY);
        txtComentario.setBackground(GRIS_CLARITO);
        txtComentario.setBorder(new EmptyBorder(10, 10, 5, 10));
        txtComentario.setLineWrap(true);
        txtComentario.setWrapStyleWord(true);
        txtComentario.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtComentario.getText().equals(comentarioPlaceholder)) {
                    txtComentario.setText("");
                    txtComentario.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtComentario.getText().trim().isEmpty()) {
                    txtComentario.setText(comentarioPlaceholder);
                    txtComentario.setForeground(Color.GRAY);
                }
            }
        });

        JPanelRedondeado wrapComentario = new JPanelRedondeado(20);
        wrapComentario.setBackground(GRIS_CLARITO);
        wrapComentario.setLayout(new BorderLayout());
        wrapComentario.add(txtComentario);

        gbc.gridy = 6;
        gbc.insets = new Insets(5, 12, 5, 12);
        contentPanel.add(wrapComentario, gbc);

        // --- 6. BOTÓN ENVIAR ---
        // Al pulsar, si hay rating, se persiste la puntuación y los aspectos marcados.
        // Luego se muestra la pantalla de operación realizada.
        JLabel lblAnonimo = new JLabel(textos.getString("valoracion.anonimo"), SwingConstants.CENTER);
        lblAnonimo.setFont(new Font("SansSerif", Font.BOLD, 12));
        gbc.gridy = 7;
        gbc.weighty = 0;
        contentPanel.add(lblAnonimo, gbc);

        BotonRedondeado btnEnviar = new BotonRedondeado(textos.getString("common.send"));
        btnEnviar.setBackground(AZUL_OSCURO);
        btnEnviar.setForeground(Color.WHITE);
        btnEnviar.setPreferredSize(new Dimension(105, 38));
        
        // Al pulsar enviar: guardar el rating y mostrar pantalla de confirmación
        btnEnviar.addActionListener(e -> {
            String professorId = mainFrame.getSelectedProfessorId();
            if (professorId != null && rating[0] > 0) {
                // Persistir rating (se suma/acomula en ProfessorDirectory)
                ProfessorDirectory.addRating(professorId, (double) rating[0]);

                // Recolectar las claves de aspectos seleccionados y guardarlas
                java.util.List<String> marcados = new java.util.ArrayList<>();
                for (JCheckBox cb : checkboxes) {
                    if (cb.isSelected()) {
                        Object aspectKey = cb.getClientProperty("aspectKey");
                        if (aspectKey instanceof String key) {
                            marcados.add(key);
                        }
                    }
                }
                ProfessorDirectory.setSavedAspects(professorId, marcados);
            }
            mainFrame.showOperacionRealizada("MAIN_ESTUDIANTE");
        });
        
        JPanel btnWrap = new JPanel();
        btnWrap.setOpaque(false);
        btnWrap.add(btnEnviar);
        
        gbc.gridy = 8;
        contentPanel.add(btnWrap, gbc);

        // --- 7. NAVEGACIÓN INFERIOR ---
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        bottom.setOpaque(false);
        
        BotonRedondeado inicio = new BotonRedondeado(textos.getString("common.home"));
        inicio.setBackground(new Color(230, 255, 210)); // Verde clarito
        inicio.setPreferredSize(new Dimension(100, 36));
        inicio.addActionListener(e -> mainFrame.showView("MAIN_ESTUDIANTE"));

        
        BotonRedondeado back = new BotonRedondeado(" ← ");
        back.setBackground(new Color(230, 255, 210)); // Verde clarito
        back.setPreferredSize(new Dimension(65, 36));
        back.addActionListener(e -> mainFrame.goBack());

        bottom.add(inicio);
        bottom.add(back);

        gbc.gridy = 9;
        gbc.insets = new Insets(10, 0, 8, 0);
        contentPanel.add(bottom, gbc);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        
        boolean isCodespaces = "true".equalsIgnoreCase(System.getenv("CODESPACES"));
        scrollPane.setVerticalScrollBarPolicy(isCodespaces ? ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED : ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        add(scrollPane, BorderLayout.CENTER);
    }

    private void updateStars(ButtonGroup starGroup, JPanel estrellasIconos, int rating) {
        int index = 1;
        for (java.util.Enumeration<AbstractButton> buttons = starGroup.getElements(); buttons.hasMoreElements(); index++) {
            AbstractButton button = buttons.nextElement();
            if (index <= rating) {
                button.setText("★");
                button.setForeground(AMARILLO_ESTRELLA);
                button.setSelected(true);
            } else {
                button.setText("☆");
                button.setForeground(GRIS_ESTRELLA);
                button.setSelected(false);
            }
        }
        estrellasIconos.revalidate();
        estrellasIconos.repaint();
    }
}