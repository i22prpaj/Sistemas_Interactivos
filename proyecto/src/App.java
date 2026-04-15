import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class App {
    private static final Color APP_BG = new Color(0xD9D9D9);
    private static final Color CARD_BG = new Color(0xA8F45A);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(App::createAndShowUI);
    }

    private static void createAndShowUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        JFrame frame = new JFrame("Inicio Sesion");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(360, 740);
        frame.setMinimumSize(new Dimension(340, 650));
        frame.setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(APP_BG);

        root.add(createTopBar(), BorderLayout.NORTH);
        root.add(createLoginCard(), BorderLayout.CENTER);

        frame.setContentPane(root);
        frame.setVisible(true);
    }

    private static JPanel createTopBar() {
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        topBar.setBackground(APP_BG);
        topBar.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));

        JLabel title = new JLabel("Inicio Sesion");
        title.setForeground(new Color(0x7F7F7F));
        title.setFont(new Font("Arial", Font.PLAIN, 28));
        topBar.add(title);

        return topBar;
    }

    private static JPanel createLoginCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createEmptyBorder(26, 26, 26, 26));

        card.add(Box.createVerticalStrut(40));
        card.add(centered(new UcoLogo()));
        card.add(Box.createVerticalStrut(22));

        JLabel subtitle = new JLabel("Introduzca credenciales de acceso");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 27));
        subtitle.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        card.add(subtitle);

        card.add(Box.createVerticalStrut(24));
        card.add(centered(createInputField("elena.ruiz@uco.es")));
        card.add(Box.createVerticalStrut(16));
        card.add(centered(createPasswordField("..............")));
        card.add(Box.createVerticalStrut(24));

        JPanel registerRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        registerRow.setOpaque(false);
        JLabel question = new JLabel("Aun no tienes acceso?");
        question.setFont(new Font("Arial", Font.PLAIN, 26));
        JLabel registerLink = new JLabel("Registro");
        registerLink.setFont(new Font("Arial", Font.PLAIN, 26));
        registerLink.setForeground(new Color(0x0059FF));
        registerRow.add(question);
        registerRow.add(registerLink);
        card.add(registerRow);

        card.add(Box.createVerticalStrut(28));
        card.add(centered(new RoundedButton("Iniciar Sesion")));

        card.add(Box.createVerticalGlue());
        return card;
    }

    private static JComponent centered(JComponent component) {
        component.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        return component;
    }

    private static JTextField createInputField(String text) {
        JTextField field = new JTextField(text);
        field.setPreferredSize(new Dimension(205, 46));
        field.setMaximumSize(new Dimension(205, 46));
        field.setMinimumSize(new Dimension(205, 46));
        field.setHorizontalAlignment(SwingConstants.LEFT);
        field.setFont(new Font("Arial", Font.PLAIN, 30));
        field.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 14));
        return field;
    }

    private static JPasswordField createPasswordField(String text) {
        JPasswordField field = new JPasswordField(text);
        field.setEchoChar('*');
        field.setPreferredSize(new Dimension(205, 46));
        field.setMaximumSize(new Dimension(205, 46));
        field.setMinimumSize(new Dimension(205, 46));
        field.setFont(new Font("Arial", Font.PLAIN, 30));
        field.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 14));
        return field;
    }

    private static class RoundedButton extends JButton {
        RoundedButton(String text) {
            super(text);
            setPreferredSize(new Dimension(145, 44));
            setMaximumSize(new Dimension(145, 44));
            setMinimumSize(new Dimension(145, 44));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setForeground(Color.WHITE);
            setFont(new Font("Arial", Font.PLAIN, 25));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(0xB6004A));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 28, 28);

            g2.setColor(new Color(0xD21B66));
            g2.setStroke(new BasicStroke(2f));
            g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, 28, 28));
            g2.dispose();

            super.paintComponent(g);
        }
    }

    private static class UcoLogo extends JComponent {
        UcoLogo() {
            setPreferredSize(new Dimension(150, 205));
            setMaximumSize(new Dimension(150, 205));
            setMinimumSize(new Dimension(150, 205));
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int cx = getWidth() / 2;
            int top = 8;
            int w = 112;
            int h = 138;

            Polygon shield = new Polygon();
            shield.addPoint(cx, top);
            shield.addPoint(cx - w / 2, top + 35);
            shield.addPoint(cx - w / 2, top + h - 35);
            shield.addPoint(cx, top + h);
            shield.addPoint(cx + w / 2, top + h - 35);
            shield.addPoint(cx + w / 2, top + 35);

            g2.setColor(new Color(0x292066));
            g2.fillPolygon(shield);

            int fanX = cx - 43;
            int fanY = top + 24;
            int fanW = 86;
            int fanH = 86;
            g2.setColor(new Color(0xDFFB88));
            g2.fill(new Arc2D.Double(fanX, fanY, fanW, fanH, 0, 180, Arc2D.PIE));

            for (int i = 0; i < 9; i++) {
                g2.setColor(new Color(0xB70B48));
                int x1 = cx;
                int y1 = top + 66;
                int x2 = cx - 39 + i * 10;
                int y2 = top + 28;
                int x3 = cx - 34 + i * 10;
                int y3 = top + 28;
                Polygon ray = new Polygon(new int[] {x1, x2, x3}, new int[] {y1, y2, y3}, 3);
                g2.fill(ray);
            }

            g2.setColor(new Color(0xF8B019));
            g2.setStroke(new BasicStroke(8f));
            g2.draw(new Arc2D.Double(cx - 29, top + 50, 58, 58, 0, 180, Arc2D.OPEN));

            g2.setColor(new Color(0x292066));
            g2.fillOval(cx - 25, top + 63, 50, 40);

            Polygon leftWing = new Polygon(new int[] {cx - 54, cx - 8, cx - 8, cx - 54},
                    new int[] {top + 97, top + 118, top + 150, top + 130}, 4);
            Polygon rightWing = new Polygon(new int[] {cx + 54, cx + 8, cx + 8, cx + 54},
                    new int[] {top + 97, top + 118, top + 150, top + 130}, 4);
            g2.setColor(new Color(0xF2F2F2));
            g2.fillPolygon(leftWing);
            g2.fillPolygon(rightWing);

            g2.setColor(new Color(0x292066));
            g2.setStroke(new BasicStroke(6f));
            g2.drawLine(cx, top + 116, cx, top + 152);

            g2.dispose();
        }
    }
}
