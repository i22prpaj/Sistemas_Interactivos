package gui;

import main.MainFrame;
import model.BotonRedondeado;
import model.JPanelRedondeado;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ReportDetailPanel extends JPanel {

    // Panel que muestra el detalle de un reporte de moderación.
    // Se organiza como varias "tarjetas" (cards) apiladas verticalmente,
    // cada una construida por un helper `createXxx()` para mantener el código claro.

    // -------------------- Constantes visuales --------------------
    // Colores para mantener consistencia con el diseño original
    private static final Color BG = new Color(199, 255, 126);
    private static final Color CARD_WHITE = Color.WHITE;
    private static final Color TEXT_GRAY = new Color(120, 120, 120);
    private static final Color TEXT_DARK = new Color(40, 40, 40);

    private static final Color RED_ACCENT = new Color(235, 77, 75);     // para etiquetas de peligro
    private static final Color GREEN_ACTION = new Color(18, 184, 134);  // botón aprobar
    private static final Color RED_ACTION = new Color(245, 75, 75);     // botón eliminar

    private static final Color WARNING_BG = new Color(255, 243, 205);   // fondo para advertencias
    private static final Color WARNING_TEXT = new Color(145, 100, 10);

    private final MainFrame mainFrame;
    private final java.util.ResourceBundle bundle;

    // -------------------- Constructor --------------------
    // Monta toda la estructura: header, badge, tarjetas con info y botones de acción.
    public ReportDetailPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        // Obtener el bundle desde el mainFrame si existe, sino usar el por defecto
        this.bundle = mainFrame != null ? mainFrame.getBundle() : java.util.ResourceBundle.getBundle("bundle.Bundle", java.util.Locale.getDefault());

        setLayout(new BorderLayout());
        setBackground(BG);

        // Root vertical scrollable: empaqueta todas las tarjetas en una columna
        ScrollablePanel root = new ScrollablePanel();
        root.setOpaque(false);
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(new EmptyBorder(30, 25, 30, 25));

        // Ensamblado de las secciones en orden visual
        root.add(createHeader());
        root.add(Box.createVerticalStrut(20));
        root.add(createTagBadge());
        root.add(Box.createVerticalStrut(20));
        root.add(createProfessorCard());
        root.add(Box.createVerticalStrut(15));
        root.add(createCommentCard());
        root.add(Box.createVerticalStrut(15));
        root.add(createInfoCard());
        root.add(Box.createVerticalStrut(15));
        root.add(createReasonCard());
        root.add(Box.createVerticalStrut(30));
        root.add(createActionButtons());

        // JScrollPane que envuelve el root. En Codespaces o entornos no móviles se
        // permite scroll vertical según el entorno; por defecto no hay scroll horizontal.
        JScrollPane scroll = new JScrollPane(root);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);

        boolean isCodespaces = "true".equalsIgnoreCase(System.getenv("CODESPACES"));
        scroll.setVerticalScrollBarPolicy(isCodespaces ? JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED : JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));

        add(scroll, BorderLayout.CENTER);
    }

    // Panel especializado que implementa la interfaz Scrollable para comportarse
    // como una columna apilable que no crece horizontalmente (evita desbordes).
    // - `getScrollableTracksViewportWidth() == true` fuerza que el ancho del panel
    //   siga siempre el ancho del viewport, evitando scroll horizontal.
    class ScrollablePanel extends JPanel implements Scrollable {
        @Override
        public Dimension getPreferredScrollableViewportSize() { return super.getPreferredSize(); }
        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) { return 16; }
        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) { return 16; }
        @Override
        public boolean getScrollableTracksViewportWidth() { return true; }
        @Override
        public boolean getScrollableTracksViewportHeight() { return false; }
    }

    // Cabecera del panel: botón "Atrás" y título.
    // El botón no pinta borde ni fondo y usa un cursor de mano para indicar que es interactivo.
    private JComponent createHeader() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        p.setOpaque(false);

        JButton back = new JButton(bundle.getString("config.back"));
        back.setFont(new Font("SansSerif", Font.BOLD, 20));
        back.setBorderPainted(false);
        back.setContentAreaFilled(false);
        back.setCursor(new Cursor(Cursor.HAND_CURSOR));
        back.addActionListener(e -> mainFrame.goBack());

        JLabel title = new JLabel(bundle.getString("reportdetail.title"));
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(TEXT_DARK);

        p.add(back);
        p.add(title);
        return p;
    }

    // Badge / etiqueta que muestra la categoría del reporte (p.ej. "Lenguaje ofensivo").
    // Tiene fondo suavemente coloreado y un icono a la izquierda para reforzar el significado.
    private JComponent createTagBadge() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        p.setOpaque(false);

        JLabel tag = new JLabel(" " + bundle.getString("moderation.card1.tag") + " ", loadScaledIcon("/resources/lenguaje_ofensivo.PNG", 22, 22), SwingConstants.LEFT);
        tag.setHorizontalTextPosition(SwingConstants.RIGHT);
        tag.setIconTextGap(6);
        tag.setOpaque(true);
        tag.setBackground(new Color(255, 230, 230));
        tag.setForeground(RED_ACCENT);
        tag.setFont(new Font("SansSerif", Font.BOLD, 13));
        tag.setBorder(new EmptyBorder(8, 12, 8, 12));
        
        p.add(tag);
        return p;
    }

    // Tarjeta que muestra la información del profesor involucrado en el reporte.
    // Usa `buildBaseCard` para mantener el mismo estilo visual de las demás tarjetas.
    private JComponent createProfessorCard() {
        JPanelRedondeado card = buildBaseCard(25);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        card.add(createMutedLabel(bundle.getString("reportdetail.professor_reported")));
        card.add(Box.createVerticalStrut(5));
        card.add(createBoldLabel(bundle.getString("profesor.nombre"), 18));
        card.add(createMutedLabel(bundle.getString("common.linear_algebra")));

        return card;
    }

    private JComponent createCommentCard() {
        JPanelRedondeado card = buildBaseCard(25);
        card.setLayout(new BorderLayout());
        
        // El borde rojo lateral izquierdo
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 5, 0, 0, RED_ACCENT),
            new EmptyBorder(15, 20, 15, 20)
        ));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        content.add(createMutedLabel(bundle.getString("reportdetail.comment_full")));
        content.add(Box.createVerticalStrut(10));

        // Texto con el resaltado de palabras prohibidas [***]
        String msg = "<html><body style='width: 250px; font-family: SansSerif; font-size: 11pt;'>" +
                bundle.getString("reportdetail.comment_text") +
                "</body></html>";
        
        JLabel bodyText = new JLabel(msg);
        content.add(bodyText);
        content.add(Box.createVerticalStrut(15));

        // Warning Badge
        JLabel warn = new JLabel(bundle.getString("common.auto_filter_warning"), loadScaledIcon("/resources/warning.PNG", 18, 18), SwingConstants.LEFT);
        warn.setHorizontalTextPosition(SwingConstants.RIGHT);
        warn.setIconTextGap(6);
        warn.setOpaque(true);
        warn.setBackground(WARNING_BG);
        warn.setForeground(WARNING_TEXT);
        warn.setBorder(new EmptyBorder(10, 12, 10, 12));
        warn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        
        content.add(warn);
        card.add(content, BorderLayout.CENTER);
        return card;
    }

    private JComponent createInfoCard() {
        // Construye una tarjeta sencilla con información adicional sobre el reporte.
        // Se usan etiquetas enriquecidas (`createRichLabel`) para mostrar pares
        // de texto gris + negrita (p.ej. "Reportado por:" + "1 usuario").
        JPanelRedondeado card = buildBaseCard(25);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        // "Reportado por:" — el valor mostrado también sale del bundle para
        // no dejar textos fijos en español dentro del panel.
        card.add(createRichLabel(java.text.MessageFormat.format(bundle.getString("reportdetail.reported_by"), bundle.getString("reportdetail.reported_by_value")), ""));
        card.add(Box.createVerticalStrut(4));

        card.add(createRichLabel(java.text.MessageFormat.format(bundle.getString("reportdetail.published"), bundle.getString("reportdetail.published_value")), ""));
        card.add(Box.createVerticalStrut(4));

        // autor del comentario y nota sobre su historial.
        card.add(createRichLabel(bundle.getString("reportdetail.user_author"), bundle.getString("reportdetail.user_author_value")));
        return card;
    }

    private JComponent createReasonCard() {
        // Tarjeta que muestra el motivo del reporte.
        // Presenta un título atenuado y a continuación el motivo destacado en negrita.
        JPanelRedondeado card = buildBaseCard(25);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        // Título (p.ej. "Motivo") con estilo atenuado.
        card.add(createMutedLabel(bundle.getString("reportdetail.reason_title")));
        card.add(Box.createVerticalStrut(8));
        
        // Elemento que describe el motivo concreto. Se fuerza una fuente en negrita
        // para darle más peso visual dentro de la tarjeta.
        JLabel item = new JLabel(bundle.getString("reportdetail.reason_item"));
        item.setFont(new Font("SansSerif", Font.BOLD, 14));
        card.add(item);

        return card;
    }

    private JComponent createActionButtons() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(320, 70));
        p.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Botones tipo píldora (muy redondeados)
        BotonRedondeado btnOk = new BotonRedondeado(bundle.getString("reportdetail.btn_approve"));
        btnOk.setBackground(GREEN_ACTION);
        btnOk.setForeground(Color.WHITE);
        btnOk.setPreferredSize(new Dimension(130, 50));
        btnOk.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnOk.addActionListener(e -> {
            if (mainFrame != null) mainFrame.showOperacionRealizada("MAIN_ESTUDIANTE");
            });

        BotonRedondeado btnNo = new BotonRedondeado(bundle.getString("common.delete"));
        btnNo.setBackground(RED_ACTION);
        btnNo.setForeground(Color.WHITE);
        btnNo.setPreferredSize(new Dimension(130, 50));
        btnNo.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnNo.addActionListener(e -> {
            if (mainFrame != null) mainFrame.showOperacionRealizada("MAIN_ESTUDIANTE");
            });

        p.add(btnOk);
        p.add(btnNo);
        return p;
    }

    // --- Helpers Estéticos ---

    private JPanelRedondeado buildBaseCard(int radius) {
        // Crea una tarjeta base con esquinas redondeadas y padding uniforme.
        // Este helper centraliza el estilo usado por todas las "cards" del panel,
        // de modo que cambiar el aspecto global sea sencillo.
        JPanelRedondeado card = new JPanelRedondeado(radius);
        card.setBackground(CARD_WHITE);
        card.setBorder(new EmptyBorder(15, 20, 15, 20));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        return card;
    }

    private JLabel createMutedLabel(String text) {
        // Etiqueta con estilo 'muted' para subtítulos o textos menos destacados.
        // Usa un gris suave y una fuente en negrita para mantener legibilidad.
        JLabel l = new JLabel(text);
        l.setForeground(TEXT_GRAY);
        l.setFont(new Font("SansSerif", Font.BOLD, 13));
        return l;
    }

    private JLabel createBoldLabel(String text, int size) {
        // Etiqueta prominente en color oscuro y tamaño personalizado.
        // Se usa para títulos o nombres donde queremos máxima prioridad visual.
        JLabel l = new JLabel(text);
        l.setForeground(TEXT_DARK);
        l.setFont(new Font("SansSerif", Font.BOLD, size));
        return l;
    }

    private JLabel createRichLabel(String gray, String bold) {
        // Etiqueta compuesta (texto gris + texto en negrita) usando HTML simple.
        // Ideal para pares de información del tipo "etiqueta: valor" con jerarquía.
        JLabel l = new JLabel("<html><font color='#787878'><b>" + gray + "</b></font> " +
                             "<font color='#282828'><b>" + bold + "</b></font></html>");
        l.setFont(new Font("SansSerif", Font.PLAIN, 14));
        return l;
    }

    private ImageIcon loadScaledIcon(String resourcePath, int width, int height) {
        // Carga un recurso de imagen embebido y lo escala suavemente al tamaño
        // solicitado. Devuelve `null` si el recurso no existe — los llamadores
        // deben tolerar iconos nulos para evitar excepciones en tiempo de ejecución.
        java.net.URL url = getClass().getResource(resourcePath);
        if (url == null) {
            return null;
        }

        ImageIcon icon = new ImageIcon(url);
        Image scaled = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }
}