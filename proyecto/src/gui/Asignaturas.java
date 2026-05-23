package gui;

import main.MainFrame;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ResourceBundle;
import model.BotonRedondeado;
import model.ProfessorDirectory;
import model.ProfessorProfile;
import model.ProfesorListRenderer;

public class Asignaturas extends JPanel {

    private MainFrame mainFrame;
    private final Color VERDE_FONDO = new Color(172, 255, 100);
    private final Color VERDE_BOTON = new Color(220, 255, 180);

    // Constructor: muestra la lista de profesores para la asignatura seleccionada
    public Asignaturas(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        ResourceBundle textos = mainFrame.getBundle();
        String selectedSubjectKey = mainFrame.getSelectedSubjectKey();
        // Obtener lista de profesores filtrada por asignatura desde ProfessorDirectory
        java.util.List<ProfessorProfile> professors = ProfessorDirectory.getBySubject(selectedSubjectKey);

        setBackground(VERDE_FONDO);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 5, 10, 25);

        // --- 1. TÍTULO DE LA ASIGNATURA ---
        // Buscamos la clave de la asignatura en el bundle para mostrar su nombre localizado;
        // si no existe, usamos una etiqueta genérica.
        String titleText = textos.containsKey(selectedSubjectKey) ? textos.getString(selectedSubjectKey) : textos.getString("subjects.label");
        JLabel title = new JLabel(titleText, SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        gbc.gridy = 0;
        gbc.insets = new Insets(40, 20, 20, 20); // Espacio superior para simular la "isla"
        add(title, gbc);

        // --- 2. LISTA DE PROFESORES ---
        // Convertimos la lista a array para JList y usamos un renderer personalizado
        JList<ProfessorProfile> list = new JList<>(professors.toArray(new ProfessorProfile[0]));
        // Usamos un renderizador similar al de asignaturas pero con icono de persona
        list.setCellRenderer(new ProfesorListRenderer());
        list.setBackground(VERDE_FONDO);
        list.setFixedCellHeight(55);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Al hacer click en un profesor seleccionamos su id y navegamos al detalle
        list.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                ProfessorProfile seleccionado = list.getSelectedValue();
                if (seleccionado != null) {
                    mainFrame.setSelectedProfessorId(seleccionado.getId());
                    mainFrame.setSelectedProfessorKey(seleccionado.getId());
                    mainFrame.setSelectedProfessorName(seleccionado.getDisplayName());
                    mainFrame.showView("PROFESOR_DETALLE");
                }
            }
        });

        // Envolver la lista en un JScrollPane con comportamiento móvil (scroll opcional)
        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        
        boolean isCodespaces = "true".equalsIgnoreCase(System.getenv("CODESPACES"));
        scroll.setVerticalScrollBarPolicy(isCodespaces ? JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED : JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));

        // Panel izquierdo que contiene la etiqueta "Profesores" y la lista
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);
        leftPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

        JLabel lblProf = new JLabel(textos.getString("asignaturas.profesores"));
        lblProf.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblProf.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblProf.setBorder(new EmptyBorder(0, 2, 8, 0));
        leftPanel.add(lblProf);

        // Ajustar tamaño del scroll y añadir al panel
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        scroll.setPreferredSize(new Dimension(260, 180));
        leftPanel.add(scroll);

        gbc.gridy = 1;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 0); // Sin insets para pegar al borde del área
        gbc.anchor = GridBagConstraints.NORTHWEST;
        add(leftPanel, gbc);

        // --- 4. NAVEGACIÓN INFERIOR: botones Home y Back ---
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 20));
        bottom.setOpaque(false);

        BotonRedondeado inicio = new BotonRedondeado(textos.getString("common.home"));
        inicio.setBackground(VERDE_BOTON);
        inicio.setPreferredSize(new Dimension(100, 38));
        inicio.addActionListener(e -> mainFrame.showView("MAIN_ESTUDIANTE"));

        // Botón atrás con forma circular
        BotonRedondeado back = new BotonRedondeado(" ← ");
        back.setBackground(VERDE_BOTON);
        back.setPreferredSize(new Dimension(55, 38));
        back.addActionListener(e -> mainFrame.goBack());

        bottom.add(inicio);
        bottom.add(back);

        gbc.gridy = 3;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        // Reducimos el margen inferior para acercar los botones al borde
        gbc.insets = new Insets(0, 5, 6, 15);
        gbc.anchor = GridBagConstraints.PAGE_END;
        add(bottom, gbc);
    }
}