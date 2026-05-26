package gui;

// Importaciones necesarias para construir la UI y acceder a modelos
import java.awt.*;                        // AWT: Color, Dimension, GridBag, Graphics...
import java.util.ResourceBundle;          // ResourceBundle para i18n
import javax.swing.*;                     // Componentes Swing
import javax.swing.border.EmptyBorder;    // Bordes vacíos para padding
import main.MainFrame;                    // Frame principal de la aplicación
import model.BotonRedondeado;             // Botón personalizado
import model.JPanelRedondeado;            // Panel personalizado con bordes redondeados
import model.ProfessorDirectory;          // Directorio de profesores (persistencia/consultas)
import model.ProfessorProfile;            // Modelo que representa un perfil de profesor

// Panel que muestra el detalle de un profesor (foto, datos, valoración, consideraciones)
public class ProfesorDetalle extends JPanel {

    // Referencia al frame principal para navegación y acceso a selección global
    private MainFrame mainFrame;
    // Constantes de color para mantener coherencia visual
    private final Color VERDE_FONDO = new Color(180, 255, 104);
    private final Color GRIS_TARJETA = new Color(220, 220, 220);
    private final Color BLANCO_BOTON = Color.WHITE;
    // Indica si estamos ejecutando dentro de Codespaces (afecta políticas de scroll)
    private final boolean runningInCodespaces;

    // Constructor: monta la interfaz usando el profesor seleccionado en el MainFrame
    public ProfesorDetalle(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.runningInCodespaces = "true".equalsIgnoreCase(System.getenv("CODESPACES")); // comprobar entorno
        ResourceBundle textos = mainFrame.getBundle(); // bundle de textos

        // Obtener el perfil del profesor seleccionado; si no existe, usar perfil por defecto
        ProfessorProfile profile = ProfessorDirectory.get(mainFrame.getSelectedProfessorId());
        if (profile == null) {
            profile = ProfessorDirectory.getDefaultProfile();
        }
        // Nombre seleccionado (puede venir de la navegación); preferir ese valor si existe
        String selectedProfessorName = mainFrame.getSelectedProfessorName();
        String professorName = selectedProfessorName != null ? selectedProfessorName : (profile != null ? profile.getDisplayName() : "");

        // Background y layout principal
        setBackground(VERDE_FONDO);
        setLayout(new BorderLayout());

        // Usamos un panel scrollable para el contenido principal (evita desbordes horizontales)
        ScrollablePanel contentPanel = new ScrollablePanel(new GridBagLayout());
        contentPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // una sola columna
        gbc.fill = GridBagConstraints.HORIZONTAL; // elementos se estiran horizontalmente

        // --- 1. HEADER (foto, nombre y departamento) ---
        JPanel header = new JPanel(new GridBagLayout());
        header.setOpaque(false);
        GridBagConstraints hGbc = new GridBagConstraints();

        // Icono del usuario (si hay recurso, se usa; si no, se muestra emoji)
        ImageIcon userIcon = loadScaledIcon(34, 34, "/resources/prof-user.PNG");
        JLabel userLabel = (userIcon != null) ? new JLabel(userIcon) : new JLabel("👤");
        hGbc.gridx = 0; hGbc.weightx = 0; hGbc.anchor = GridBagConstraints.NORTHWEST;
        hGbc.insets = new Insets(0, 0, 0, 15); // pequeño espacio a la derecha del icono
        header.add(userLabel, hGbc);

        // Panel con nombre y departamento (dos líneas)
        JPanel textHeader = new JPanel(new GridLayout(2, 1));
        textHeader.setOpaque(false);
        JLabel name = new JLabel(professorName);
        name.setFont(new Font("SansSerif", Font.BOLD, 16));
        JLabel dept = new JLabel(profile != null ? profile.getDepartment() : "");
        dept.setFont(new Font("SansSerif", Font.PLAIN, 12));
        textHeader.add(name);
        textHeader.add(dept);

        // Añadir el bloque de texto al header, haciendo que ocupe el espacio restante
        hGbc.gridx = 1; hGbc.weightx = 1.0; hGbc.fill = GridBagConstraints.HORIZONTAL;
        hGbc.insets = new Insets(0, 0, 0, 0);
        header.add(textHeader, hGbc);

        gbc.gridy = 0; // fila 0 del contentPanel
        gbc.insets = new Insets(20, 22, 15, 22); // márgenes alrededor del header
        contentPanel.add(header, gbc);

        // --- 2. VALORACIÓN: tarjeta blanca con nota y estrellas ---
        JPanelRedondeado valCard = new JPanelRedondeado(25); // panel redondeado con radio 25
        valCard.setBackground(BLANCO_BOTON);
        valCard.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 8));

        // Obtener rating (puede venir de ProfessorDirectory o usar valor por defecto)
        double rating = profile != null ? model.ProfessorDirectory.getRating(profile.getId()) : 4.4d;
        String ratingText = String.format(java.util.Locale.US, "%.1f", rating);
        JLabel val = new JLabel("<html>" + textos.getString("profesor.valoracion") + ": <b><font color='#32CD32'>" + ratingText + "</font>/5</b></html>");
        val.setFont(new Font("SansSerif", Font.PLAIN, 13));
        val.setOpaque(false);

        // Representación visual de estrellas (estática aquí)
        JLabel stars = new JLabel("★★★★☆");
        stars.setFont(new Font("SansSerif", Font.PLAIN, 16));
        stars.setForeground(new Color(255, 193, 7)); // color dorado para las estrellas

        valCard.add(val);
        valCard.add(stars);

        gbc.gridy = 1; // fila 1
        gbc.fill = GridBagConstraints.NONE; // que la cápsula no se estire
        gbc.insets = new Insets(0, 0, 15, 0);
        contentPanel.add(valCard, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL; // restaurar para siguientes tarjetas

        // --- 3. DATOS DE CONTACTO: tarjeta gris con filas (despacho, email, teléfono, tutorías)
        JPanelRedondeado datos = new JPanelRedondeado(15);
        datos.setBackground(GRIS_TARJETA);
        datos.setLayout(new GridLayout(4, 1, 0, 12));
        datos.setBorder(new EmptyBorder(18, 20, 18, 20));

        // Agregar filas con icono y texto usando createContactRow
        datos.add(createContactRow("⌂", textos.getString("profesor.despacho") + ": " + (profile != null ? profile.getOffice() : "")));
        datos.add(createContactRow("✉", textos.getString("common.email") + ": " + (profile != null ? profile.getEmail() : "")));
        datos.add(createContactRow("✆", textos.getString("profesor.telefono") + ": " + (profile != null ? profile.getPhone() : "")));
        datos.add(createContactRow("◷", textos.getString("profesor.tutorias") + ": " + ProfessorDirectory.localizeOfficeHours(profile != null ? profile.getOfficeHours() : "", textos)));

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 22, 15, 22);
        contentPanel.add(datos, gbc);

        // --- 4. ASIGNATURAS IMPARTIDAS: tarjeta con lista de asignaturas localizadas ---
        gbc.gridy = 3;
        contentPanel.add(crearTarjetaGrisConTitulo(textos.getString("profesor.asignaturas_impartidas"), 
            profile != null ? profile.getLocalizedSubjectNames(textos) : new String[0]), gbc);

        // --- 5. CONSIDERACIONES: combinar consideraciones del perfil y aspectos guardados (localizados)
        java.util.LinkedHashMap<String, String> allConsiderations = new java.util.LinkedHashMap<>();
        if (profile != null) {
            for (String consideration : profile.getConsiderations()) {
                // Localizar cada consideración (traducción) y usar identity como clave única
                String localized = ProfessorDirectory.localizeConsideration(consideration, textos);
                allConsiderations.putIfAbsent(ProfessorDirectory.considerationIdentity(consideration), localized);
            }
        }

        // Añadir aspectos guardados por usuarios (persistidos) para este profesor
        String currentProfessorId = mainFrame.getSelectedProfessorId();
        if (currentProfessorId != null) {
            java.util.List<String> savedAspects = ProfessorDirectory.getSavedAspects(currentProfessorId);
            for (String aspect : savedAspects) {
                String localized = ProfessorDirectory.localizeConsideration(aspect, textos);
                allConsiderations.putIfAbsent(ProfessorDirectory.considerationIdentity(aspect), localized);
            }
        }

        gbc.gridy = 4;
        contentPanel.add(crearTarjetaGrisConTitulo(textos.getString("profesor.consideraciones"), 
            allConsiderations.values().toArray(new String[0])), gbc);

        // --- 6. BOTÓN PUNTUAR: abre la vista de valoración ---
        BotonRedondeado btnPuntuar = new BotonRedondeado(textos.getString("profesor.puntuar"));
        btnPuntuar.setBackground(BLANCO_BOTON);
        btnPuntuar.setPreferredSize(new Dimension(140, 38));
        btnPuntuar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnPuntuar.addActionListener(e -> mainFrame.showView("VALORACION"));

        gbc.gridy = 5;
        gbc.weighty = 1.0; // empuja el botón hacia abajo si hay espacio
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(10, 0, 30, 0);
        contentPanel.add(btnPuntuar, gbc);

        // --- SCROLL: envolver contentPanel en JScrollPane con estilo "móvil" (scroll vertical oculto por defecto)
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

        // --- 7. NAVEGACIÓN INFERIOR: Home y Back ---
        JPanel footerPanel = new JPanel(new GridLayout(1, 3));
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));

        JPanel leftPanel = new JPanel();
        leftPanel.setOpaque(false);

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        centerPanel.setOpaque(false);
        BotonRedondeado inicio = new BotonRedondeado(textos.getString("common.home"));
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
    // Implementa Scrollable para controlar comportamiento de scroll dentro del JScrollPane
    class ScrollablePanel extends JPanel implements Scrollable {
        public ScrollablePanel(LayoutManager layout) { super(layout); }
        @Override public Dimension getPreferredScrollableViewportSize() { return super.getPreferredSize(); }
        @Override public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) { return 16; }
        @Override public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) { return 16; }
        @Override public boolean getScrollableTracksViewportWidth() { return true; } // ajustar ancho al viewport
        @Override public boolean getScrollableTracksViewportHeight() { return false; }
    }

    // --- MÉTODOS DE UTILIDAD ---

    // Crear una fila de contacto con icono y texto (alineado a la izquierda)
    private JPanel createContactRow(String icon, String text) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);

        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 18)); // icono ligeramente grande
        iconLabel.setPreferredSize(new Dimension(25, 25)); // tamaño fijo para alinear

        String safeText = text == null ? "" : text
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\n", "<br>");
        JLabel textLabel = new JLabel("<html><div style='width: 205px; padding: 0 4px;'>" + safeText + "</div></html>");
        textLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        textLabel.setForeground(Color.DARK_GRAY);

        row.add(iconLabel, BorderLayout.WEST);
        row.add(textLabel, BorderLayout.CENTER);

        return row;
    }

    // Crear una tarjeta gris con un título y una lista vertical de items (Strings)
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

    // Carga la primera imagen disponible de las rutas proporcionadas y la escala
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