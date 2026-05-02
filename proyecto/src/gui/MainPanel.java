package gui;

import main.MainFrame;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;

public class MainPanel extends JPanel {

    private MainFrame mainFrame;

    public MainPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        ResourceBundle textos = mainFrame.getBundle();

        setBackground(new Color(175, 255, 100));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 12, 8, 12);

        // Header: logo + title + settings icon
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/resources/logo-ing-informtica.png"));
            Image img = logoIcon.getImage().getScaledInstance(36, 36, Image.SCALE_SMOOTH);
            JLabel logo = new JLabel(new ImageIcon(img));
            logo.setBorder(new EmptyBorder(6, 6, 6, 6));
            header.add(logo, BorderLayout.WEST);
        } catch (Exception e) {
            header.add(new JLabel("UCO"), BorderLayout.WEST);
        }

        JLabel title = new JLabel(textos.getString("grado.informatica"));
        title.setFont(new Font("Arial", Font.BOLD, 14));
        header.add(title, BorderLayout.CENTER);

        // notification button
        JButton notif = new JButton();
        notif.setFocusPainted(false);
        notif.setContentAreaFilled(false);
        notif.setBorderPainted(false);
        notif.setOpaque(false);
        notif.setCursor(new Cursor(Cursor.HAND_CURSOR));
        try {
            ImageIcon notifIcon = new ImageIcon(getClass().getResource("/resources/notif.PNG"));
            Image scaledNotif = notifIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            notif.setIcon(new ImageIcon(scaledNotif));
        } catch (Exception e) {
            notif.setText("🔔");
        }
        notif.setPreferredSize(new Dimension(34, 34));
        notif.setBorder(new EmptyBorder(6, 6, 6, 6));
        notif.addActionListener(e -> mainFrame.showView("CONFIGURACION"));
        header.add(notif, BorderLayout.EAST);

        gbc.gridy = 0;
        add(header, gbc);

        // Search box with reserved space on the right for settings
        JPanel searchRow = new JPanel(new BorderLayout(10, 0));
        searchRow.setOpaque(false);

        JTextField search = new JTextField();
        search.setPreferredSize(new Dimension(280, 36));
        search.setMaximumSize(new Dimension(280, 36));
        search.setBackground(Color.WHITE);
        search.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            new EmptyBorder(6, 10, 6, 10)));
        search.setText("Buscar");
        searchRow.add(search, BorderLayout.WEST);

        JButton searchSettings = new JButton();
        searchSettings.setFocusPainted(false);
        searchSettings.setContentAreaFilled(false);
        searchSettings.setBorderPainted(false);
        searchSettings.setOpaque(false);
        searchSettings.setCursor(new Cursor(Cursor.HAND_CURSOR));
        try {
            ImageIcon settingsIcon = new ImageIcon(getClass().getResource("/resources/settings.PNG"));
            Image scaledSettings = settingsIcon.getImage().getScaledInstance(22, 25, Image.SCALE_SMOOTH);
            searchSettings.setIcon(new ImageIcon(scaledSettings));
        } catch (Exception e) {
            searchSettings.setText("⚙");
        }
        searchSettings.setPreferredSize(new Dimension(34, 34));
        searchSettings.addActionListener(e -> mainFrame.showView("CONFIGURACION"));
        searchRow.add(searchSettings, BorderLayout.EAST);

        gbc.gridy = 1;
        add(searchRow, gbc);

        // Label: Asignaturas por curso
        gbc.gridy = 2;
        JLabel lbl = new JLabel("Asignaturas por curso:");
        lbl.setFont(new Font("Arial", Font.PLAIN, 12));
        add(lbl, gbc);

        // Tabs for years (simple panel)
        JPanel tabs = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        tabs.setOpaque(false);
        String[] años = {"Todo", "1º", "2º", "3º", "4º"};
        for (String a : años) {
            JButton b = new JButton(a);
            b.setFocusPainted(false);
            b.setBackground(new Color(240, 240, 240));
            b.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
            tabs.add(b);
        }
        gbc.gridy = 3;
        add(tabs, gbc);

        // List of subjects
        String[] subjects = {"Cálculo", "Economía", "Legislación", "Estadística", "POO", "Álgebra Lineal"};
        JList<String> list = new JList<>(subjects);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setVisibleRowCount(6);
        list.setFixedCellHeight(36);
        list.setBackground(new Color(240, 255, 220));
        list.setBorder(new EmptyBorder(4, 8, 4, 8));

        // Double-click opens REPORTE
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    mainFrame.showView("REPORTE");
                }
            }
        });

        JScrollPane scroll = new JScrollPane(list);
        scroll.setPreferredSize(new Dimension(300, 220));
        gbc.gridy = 4;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        add(scroll, gbc);

        // Bottom navigation
        JPanel bottom = new JPanel(new GridBagLayout());
        bottom.setOpaque(false);

        GridBagConstraints footerGbc = new GridBagConstraints();
        footerGbc.gridy = 0;
        footerGbc.fill = GridBagConstraints.HORIZONTAL;
        footerGbc.insets = new Insets(0, 0, 0, 0);

        footerGbc.gridx = 0;
        footerGbc.weightx = 1.0;
        JPanel leftSpacer = new JPanel();
        leftSpacer.setOpaque(false);
        bottom.add(leftSpacer, footerGbc);

        footerGbc.gridx = 1;
        footerGbc.weightx = 0.0;
        BotonRedondeado inicio = new BotonRedondeado("Inicio");
        inicio.setBackground(new Color(255, 255, 255));
        inicio.setForeground(new Color(70, 70, 70));
        inicio.setFocusPainted(false);
        inicio.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        inicio.setMargin(new Insets(0, 0, 0, 0));
        inicio.setPreferredSize(new Dimension(88, 30));
        inicio.addActionListener(e -> mainFrame.showView("MAIN_ESTUDIANTE"));
        bottom.add(inicio, footerGbc);

        footerGbc.gridx = 2;
        footerGbc.weightx = 1.0;
        JPanel rightFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 6));
        rightFooter.setOpaque(false);
        JButton back = new JButton("←");
        back.setBackground(new Color(255, 255, 255));
        back.setFocusPainted(false);
        back.setOpaque(true);
        back.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
            new EmptyBorder(8, 18, 8, 18)));
        back.setPreferredSize(new Dimension(54, 34));
        back.addActionListener(e -> mainFrame.showView("LOGIN"));
        rightFooter.add(back);
        bottom.add(rightFooter, footerGbc);

        gbc.gridy = 5;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(bottom, gbc);
    }
}
