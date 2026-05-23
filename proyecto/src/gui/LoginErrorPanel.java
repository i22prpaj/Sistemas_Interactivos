package gui;

import java.awt.*;
import java.util.ResourceBundle;
import javax.swing.*;
import main.MainFrame;
import model.BotonRedondeado;

public class LoginErrorPanel extends JPanel {

    // Panel mostrado cuando el login falla. Muestra un icono de alerta,
    // un título en rojo y opciones para reportar el problema o volver.

    private MainFrame mainFrame;

    public LoginErrorPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        // Recuperar el ResourceBundle del MainFrame para textos localizados
        ResourceBundle textos = mainFrame.getBundle();

        setLayout(new BorderLayout());
        setBackground(new Color(175, 255, 100)); // Fondo verde lima

        // --- PANEL CENTRAL: icono, título, mensaje y enlace para reportar ---
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 1. Icono de alerta: intentamos cargar imagen; si falla usamos emoji
        JLabel iconLabel = new JLabel();
        try {
            ImageIcon alertIcon = new ImageIcon(getClass().getResource("/resources/acceso_denegado.png"));
            Image img = alertIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            iconLabel.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            // Fallback: texto emoji para entornos sin recursos
            iconLabel.setText("⚠️");
            iconLabel.setFont(new Font("Arial", Font.PLAIN, 80));
        }
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        centerPanel.add(iconLabel, gbc);

        // 2. Título: clave `LoginError.titulo` localizada
        JLabel lblTitulo = new JLabel(textos.getString("LoginError.titulo"), SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(Color.RED);
        gbc.gridy = 1;
        centerPanel.add(lblTitulo, gbc);

        // 3. Mensaje explicativo: clave `LoginError.mensaje`
        JLabel lblMensaje = new JLabel(textos.getString("LoginError.mensaje"), SwingConstants.CENTER);
        gbc.gridy = 2;
        centerPanel.add(lblMensaje, gbc);

        // 4. Botón "Reportar" (renderizado como enlace): al pulsar va a la vista
        // de reporte para que el usuario describa el problema.
        JButton btnReportar = new JButton(textos.getString("common.report_problem"));
        btnReportar.setForeground(Color.BLUE);
        btnReportar.setFont(new Font("Arial", Font.PLAIN, 14));
        btnReportar.setBorderPainted(false);
        btnReportar.setContentAreaFilled(false);
        btnReportar.setFocusPainted(false);
        btnReportar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 3;
        gbc.insets = new Insets(50, 10, 10, 10);
        centerPanel.add(btnReportar, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // --- PANEL INFERIOR: botón volver en la esquina inferior derecha ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        bottomPanel.setOpaque(false);

        BotonRedondeado btnVolver = new BotonRedondeado("⬅");
        btnVolver.setFont(new Font("Arial", Font.BOLD, 18));
        btnVolver.setForeground(Color.BLACK);
        btnVolver.setFocusPainted(false);
        btnVolver.setPreferredSize(new Dimension(48, 36));
        btnVolver.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));

        bottomPanel.add(btnVolver);
        add(bottomPanel, BorderLayout.SOUTH);

        // Lógica de navegación
        btnVolver.addActionListener(e -> {
            mainFrame.showView("LOGIN");
        });

        btnReportar.addActionListener(e -> {
            mainFrame.showView("REPORTE");
        });
    }
}