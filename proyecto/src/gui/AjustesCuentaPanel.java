package gui;

import java.awt.*;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import main.MainFrame;
import model.BotonRedondeado;

public class AjustesCuentaPanel extends JPanel {

    private final MainFrame mainFrame;

    public AjustesCuentaPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        ResourceBundle textos = mainFrame.getBundle();

        setLayout(new BorderLayout());
        setBackground(new Color(212, 250, 187));

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(18, 18, 18, 18));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);

        JLabel title = new JLabel(textos.getString("config.account_title"), SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        gbc.gridy = 0;
        contentPanel.add(title, gbc);

        JPanel card = new JPanel(new GridLayout(0, 1, 0, 8));
        card.setOpaque(true);
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(16, 16, 16, 16));

        String currentUserEmail = mainFrame.getCurrentUserEmail();
        String roleText;
        if (mainFrame.isModerator()) {
            roleText = textos.getString("config.role_moderator");
        } else if (currentUserEmail != null) {
            roleText = textos.getString("config.role_student");
        } else {
            roleText = textos.getString("config.role_guest");
        }

        card.add(createInfoRow(textos.getString("config.account_email"), currentUserEmail != null && !currentUserEmail.isBlank() ? currentUserEmail : textos.getString("config.account_no_session")));
        card.add(createInfoRow(textos.getString("config.account_role"), roleText));
        card.add(createInfoRow(textos.getString("config.account_session"), currentUserEmail != null && !currentUserEmail.isBlank() ? textos.getString("config.account_session_active") : textos.getString("config.account_no_session")));

        gbc.gridy = 1;
        gbc.insets = new Insets(12, 0, 12, 0);
        contentPanel.add(card, gbc);

        JPanel actions = new JPanel(new GridLayout(0, 1, 0, 12));
        actions.setOpaque(false);

        BotonRedondeado changePasswordBtn = new BotonRedondeado(textos.getString("config.account_change_password"));
        changePasswordBtn.setPreferredSize(new Dimension(180, 38));
        changePasswordBtn.addActionListener(e -> mainFrame.showView("CAMBIAR_CONTRASENA"));

        BotonRedondeado logoutBtn = new BotonRedondeado(textos.getString("config.account_logout"));
        logoutBtn.setPreferredSize(new Dimension(180, 38));
        logoutBtn.addActionListener(e -> {
            mainFrame.clearCurrentUserEmail();
            mainFrame.showView("LOGIN");
        });

        actions.add(changePasswordBtn);
        actions.add(logoutBtn);

        gbc.gridy = 2;
        gbc.insets = new Insets(8, 30, 8, 30);
        contentPanel.add(actions, gbc);

        add(contentPanel, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        footer.setOpaque(false);

        BotonRedondeado backBtn = new BotonRedondeado(textos.getString("config.account_back"));
        backBtn.setPreferredSize(new Dimension(120, 36));
        backBtn.setBackground(new Color(212, 250, 187));
        backBtn.setForeground(Color.BLACK);
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> mainFrame.goBack());
        footer.add(backBtn);

        add(footer, BorderLayout.SOUTH);
    }

    private JPanel createInfoRow(String labelText, String valueText) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 13));

        JLabel value = new JLabel(valueText);
        value.setFont(new Font("Arial", Font.PLAIN, 13));
        value.setForeground(new Color(55, 55, 55));

        row.add(label, BorderLayout.WEST);
        row.add(value, BorderLayout.CENTER);
        return row;
    }
}