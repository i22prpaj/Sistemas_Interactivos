package gui;

import main.MainFrame;
import javax.swing.*;
import java.awt.*;

public class LoginErrorPanel extends JPanel {

    private MainFrame mainFrame;

    public LoginErrorPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        
        // Usamos BorderLayout para poder enviar el botón de volver al "Sur" (abajo)
        setLayout(new BorderLayout());
        setBackground(new Color(175, 255, 100)); // Fondo verde lima

        // --- PANEL CENTRAL (Icono, Título, Mensaje, Reportar) ---
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false); // Transparente para que se vea el fondo verde del padre
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 1. Icono de Alerta
        JLabel iconLabel = new JLabel();
        try {
            ImageIcon alertIcon = new ImageIcon(getClass().getResource("/resources/acceso_denegado.png"));
            Image img = alertIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            iconLabel.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            // Fallback por si la imagen no carga: un emoji grande de alerta
            iconLabel.setText("⚠️");
            iconLabel.setFont(new Font("Arial", Font.PLAIN, 80));
        }
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        centerPanel.add(iconLabel, gbc);

        // 2. Título "¡Acceso Denegado!"
        JLabel lblTitulo = new JLabel("¡Acceso Denegado!", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(Color.RED);
        gbc.gridy = 1;
        centerPanel.add(lblTitulo, gbc);

        // 3. Mensaje Explicativo
        // Usamos HTML dentro del JLabel para poder hacer los saltos de línea (br) y centrar el texto
        String mensajeHtml = "<html><div style='text-align: center; font-family: Arial; font-size: 14px;'>"
                + "El correo electrónico o la<br>contraseña introducidos no son<br>"
                + "correctos. Por favor, inténtelo<br>de nuevo o contacte con el<br>"
                + "administrador si el problema<br>persiste.</div></html>";
        JLabel lblMensaje = new JLabel(mensajeHtml, SwingConstants.CENTER);
        gbc.gridy = 2;
        centerPanel.add(lblMensaje, gbc);

        // 4. Botón "Reportar un problema"
        JButton btnReportar = new JButton("Reportar un problema");
        btnReportar.setForeground(new Color(0, 150, 255)); // Azul claro
        btnReportar.setFont(new Font("Arial", Font.PLAIN, 14));
        btnReportar.setBorderPainted(false); // Quitar borde
        btnReportar.setContentAreaFilled(false); // Quitar fondo
        btnReportar.setFocusPainted(false);
        btnReportar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 3;
        gbc.insets = new Insets(50, 10, 10, 10); // Empujarlo hacia abajo
        centerPanel.add(btnReportar, gbc);

        // Añadimos el bloque central al panel principal
        add(centerPanel, BorderLayout.CENTER);


        // --- PANEL INFERIOR (Botón de retroceso) ---
        // Usamos FlowLayout alineado a la derecha
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        bottomPanel.setOpaque(false);

        // Creamos el botón circular de la flecha
        JButton btnVolver = new JButton("⬅"); // Usamos carácter Unicode de flecha
        btnVolver.setFont(new Font("Arial", Font.BOLD, 18));
        btnVolver.setBackground(new Color(160, 255, 90)); // Un verde un pelín más oscuro para resaltar
        btnVolver.setForeground(Color.BLACK);
        btnVolver.setFocusPainted(false);
        btnVolver.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));

        bottomPanel.add(btnVolver);
        add(bottomPanel, BorderLayout.SOUTH);

        // --- LÓGICA DE NAVEGACIÓN ---

        btnVolver.addActionListener(e -> {
            // Volver a la pantalla de login
            mainFrame.showView("LOGIN");
        });

        btnReportar.addActionListener(e -> {
            // mainFrame.showView("REPORTE"); // Lo descomentaremos cuando se cree el panel de reporte
            System.out.println("Navegando a la pantalla de reporte de errores...");
        });
    }
}