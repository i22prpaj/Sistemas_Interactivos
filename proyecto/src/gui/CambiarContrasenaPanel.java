package gui;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import main.MainFrame;
import model.BotonRedondeado;
import model.CredentialStore;

public class CambiarContrasenaPanel extends JPanel {

    private final MainFrame mainFrame;
    private Timer statusTimer;
    private final JLabel statusLabel;

    public CambiarContrasenaPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        ResourceBundle textos = mainFrame.getBundle();

        setLayout(new BorderLayout());
        setBackground(new Color(175, 255, 100));

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(18, 24, 18, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);

        JLabel title = new JLabel(textos.getString("config.change_password_title"), SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        gbc.gridy = 0;
        centerPanel.add(title, gbc);

        String currentUserEmail = mainFrame.getCurrentUserEmail();
        JLabel userLabel = new JLabel(currentUserEmail != null ? currentUserEmail : "", SwingConstants.CENTER);
        userLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        userLabel.setForeground(new Color(80, 80, 80));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 12, 0);
        centerPanel.add(userLabel, gbc);

        JTextField currentPassword = createField(textos.getString("config.change_password_current"), true);
        gbc.gridy = 2;
        gbc.insets = new Insets(8, 0, 8, 0);
        centerPanel.add(currentPassword, gbc);

        JTextField newPassword = createField(textos.getString("config.change_password_new"), true);
        gbc.gridy = 3;
        centerPanel.add(newPassword, gbc);

        JTextField confirmPassword = createField(textos.getString("config.change_password_confirm"), true);
        gbc.gridy = 4;
        centerPanel.add(confirmPassword, gbc);

        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(170, 40, 40));
        statusLabel.setOpaque(false);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        gbc.gridy = 5;
        gbc.insets = new Insets(6, 0, 4, 0);
        centerPanel.add(statusLabel, gbc);

        BotonRedondeado saveBtn = new BotonRedondeado(textos.getString("common.send"));
        saveBtn.setPreferredSize(new Dimension(120, 38));
        saveBtn.setBackground(Color.WHITE);
        saveBtn.setForeground(Color.BLACK);
        saveBtn.addActionListener(e -> {
            String email = mainFrame.getCurrentUserEmail();
            if (email == null || email.isBlank()) {
                showStatus(textos.getString("config.change_password_login_required"), false);
                return;
            }

            String current = new String(((JPasswordField) currentPassword).getPassword());
            String fresh = new String(((JPasswordField) newPassword).getPassword());
            String confirm = new String(((JPasswordField) confirmPassword).getPassword());

            if (current.equals(textos.getString("config.change_password_current"))) {
                showStatus(textos.getString("config.change_password_wrong"), false);
                return;
            }

            if (fresh.equals(textos.getString("config.change_password_new")) || fresh.isBlank()) {
                showStatus(textos.getString("config.change_password_empty"), false);
                return;
            }

            if (!fresh.equals(confirm)) {
                showStatus(textos.getString("config.change_password_mismatch"), false);
                return;
            }

            if (!CredentialStore.changePassword(email, current, fresh)) {
                showStatus(textos.getString("config.change_password_wrong"), false);
                return;
            }

            currentPassword.setText(textos.getString("config.change_password_current"));
            ((JPasswordField) currentPassword).setEchoChar((char) 0);
            newPassword.setText(textos.getString("config.change_password_new"));
            ((JPasswordField) newPassword).setEchoChar((char) 0);
            confirmPassword.setText(textos.getString("config.change_password_confirm"));
            ((JPasswordField) confirmPassword).setEchoChar((char) 0);
            showStatus(textos.getString("config.change_password_success"), true);
        });

        JPanel buttonWrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        buttonWrap.setOpaque(false);
        buttonWrap.add(saveBtn);
        gbc.gridy = 6;
        gbc.insets = new Insets(16, 0, 6, 0);
        centerPanel.add(buttonWrap, gbc);

        add(centerPanel, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 14));
        footerPanel.setOpaque(false);

        BotonRedondeado backBtn = new BotonRedondeado(textos.getString("config.back"));
        backBtn.setPreferredSize(new Dimension(120, 36));
        backBtn.setForeground(Color.BLACK);
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> {
            if (statusTimer != null && statusTimer.isRunning()) {
                statusTimer.stop();
                statusTimer = null;
            }
            mainFrame.goBack();
        });
        footerPanel.add(backBtn);

        add(footerPanel, BorderLayout.SOUTH);
    }

    private void showStatus(String message, boolean success) {
        if (statusTimer != null && statusTimer.isRunning()) {
            statusTimer.stop();
        }

        statusLabel.setText(message);
        statusLabel.setForeground(success ? Color.WHITE : new Color(170, 40, 40));
        statusLabel.setBackground(success ? Color.BLACK : new Color(175, 255, 100));
        statusLabel.setOpaque(success);
        statusLabel.revalidate();
        statusLabel.repaint();

        if (success) {
            statusTimer = new Timer(2800, e -> {
                statusTimer = null;
                mainFrame.goBack();
            });
            statusTimer.setRepeats(false);
            statusTimer.start();
        }
    }

    private JTextField createField(String placeholder, boolean password) {
        JTextField field = password ? new JPasswordField(placeholder) : new JTextField(placeholder);
        field.setPreferredSize(new Dimension(250, 40));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE),
            new EmptyBorder(5, 10, 5, 10)
        ));
        field.setFont(new Font("Arial", Font.PLAIN, 13));
        field.setBackground(Color.WHITE);

        if (password) {
            ((JPasswordField) field).setEchoChar((char) 0);
        }

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    if (password) {
                        ((JPasswordField) field).setEchoChar('•');
                    }
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    if (password) {
                        ((JPasswordField) field).setEchoChar((char) 0);
                    }
                }
            }
        });

        return field;
    }
}