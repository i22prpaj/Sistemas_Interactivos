package gui;

import main.MainFrame;
import model.BotonRedondeado;
import model.JPanelRedondeado;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ReportDetailPanel extends JPanel {

    // Colores extraídos de la imagen
    private static final Color BG = new Color(199, 255, 126); 
    private static final Color CARD_WHITE = Color.WHITE;
    private static final Color TEXT_GRAY = new Color(120, 120, 120);
    private static final Color TEXT_DARK = new Color(40, 40, 40);
    
    private static final Color RED_ACCENT = new Color(235, 77, 75);
    private static final Color GREEN_ACTION = new Color(18, 184, 134);
    private static final Color RED_ACTION = new Color(245, 75, 75);
    
    private static final Color WARNING_BG = new Color(255, 243, 205);
    private static final Color WARNING_TEXT = new Color(145, 100, 10);

    private final MainFrame mainFrame;
    private final java.util.ResourceBundle bundle;

    public ReportDetailPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.bundle = mainFrame != null ? mainFrame.getBundle() : java.util.ResourceBundle.getBundle("bundle.Bundle", java.util.Locale.getDefault());

        setLayout(new BorderLayout());
        setBackground(BG);

        // Panel desplazable que no permite crecer hacia la derecha
        ScrollablePanel root = new ScrollablePanel();
        root.setOpaque(false);
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(new EmptyBorder(30, 25, 30, 25));

        // Ensamblado de componentes
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

    private JComponent createTagBadge() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        p.setOpaque(false);

        JLabel tag = new JLabel(" 🚫 " + bundle.getString("moderation.pendientes") + " ");
        tag.setOpaque(true);
        tag.setBackground(new Color(255, 230, 230));
        tag.setForeground(RED_ACCENT);
        tag.setFont(new Font("SansSerif", Font.BOLD, 13));
        tag.setBorder(new EmptyBorder(8, 12, 8, 12));
        
        p.add(tag);
        return p;
    }

    private JComponent createProfessorCard() {
        JPanelRedondeado card = buildBaseCard(25);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        card.add(createMutedLabel(bundle.getString("reportdetail.professor_reported")));
        card.add(Box.createVerticalStrut(5));
        card.add(createBoldLabel(bundle.getString("reportdetail.professor_name"), 18));
        card.add(createMutedLabel(bundle.getString("profesor.asig_algebra") + " • 2º Grado Informática"));

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
        JLabel warn = new JLabel(bundle.getString("reportdetail.warn.filter_detected"));
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
        JPanelRedondeado card = buildBaseCard(25);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        card.add(createRichLabel(java.text.MessageFormat.format(bundle.getString("reportdetail.reported_by"), "1 usuario"), ""));
        card.add(Box.createVerticalStrut(4));
        card.add(createRichLabel(java.text.MessageFormat.format(bundle.getString("reportdetail.published"), "Hace 2 horas"), ""));
        card.add(Box.createVerticalStrut(4));
        card.add(createRichLabel(bundle.getString("reportdetail.user_author"), "fran.perez (sin historial previo)"));
        return card;
    }

    private JComponent createReasonCard() {
        JPanelRedondeado card = buildBaseCard(25);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        card.add(createMutedLabel(bundle.getString("reportdetail.reason_title")));
        card.add(Box.createVerticalStrut(8));
        
        JLabel item = new JLabel(bundle.getString("reportdetail.reason_item"));
        item.setFont(new Font("SansSerif", Font.BOLD, 14));
        card.add(item);

        return card;
    }

    private JComponent createActionButtons() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 0));
        p.setOpaque(false);

        // Botones tipo píldora (muy redondeados)
        BotonRedondeado btnOk = new BotonRedondeado(bundle.getString("reportdetail.btn_approve"));
        btnOk.setBackground(GREEN_ACTION);
        btnOk.setForeground(Color.WHITE);
        btnOk.setPreferredSize(new Dimension(160, 60));
        btnOk.setFont(new Font("SansSerif", Font.BOLD, 14));

        BotonRedondeado btnNo = new BotonRedondeado(bundle.getString("reportdetail.btn_reject"));
        btnNo.setBackground(RED_ACTION);
        btnNo.setForeground(Color.WHITE);
        btnNo.setPreferredSize(new Dimension(160, 60));
        btnNo.setFont(new Font("SansSerif", Font.BOLD, 14));

        p.add(btnOk);
        p.add(btnNo);
        return p;
    }

    // --- Helpers Estéticos ---

    private JPanelRedondeado buildBaseCard(int radius) {
        JPanelRedondeado card = new JPanelRedondeado(radius);
        card.setBackground(CARD_WHITE);
        card.setBorder(new EmptyBorder(15, 20, 15, 20));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        return card;
    }

    private JLabel createMutedLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(TEXT_GRAY);
        l.setFont(new Font("SansSerif", Font.BOLD, 13));
        return l;
    }

    private JLabel createBoldLabel(String text, int size) {
        JLabel l = new JLabel(text);
        l.setForeground(TEXT_DARK);
        l.setFont(new Font("SansSerif", Font.BOLD, size));
        return l;
    }

    private JLabel createRichLabel(String gray, String bold) {
        JLabel l = new JLabel("<html><font color='#787878'><b>" + gray + "</b></font> " +
                             "<font color='#282828'><b>" + bold + "</b></font></html>");
        l.setFont(new Font("SansSerif", Font.PLAIN, 14));
        return l;
    }
}