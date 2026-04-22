package gui;

import main.MainFrame;
import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle; // Importación necesaria para los bundles

public class LoginErrorPanel extends JPanel {

    private MainFrame mainFrame;

    public LoginErrorPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        
        // Obtenemos el diccionario de textos actual desde el MainFrame
        ResourceBundle textos = mainFrame.getBundle();
        
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

        // 2. Título (Internacionalizado)
        JLabel lblTitulo = new JLabel(textos.getString("error.titulo"), SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(Color.RED);
        gbc.gridy = 1;
        centerPanel.add(lblTitulo, gbc);

        // 3. Mensaje Explicativo (Internacionalizado)
        // El texto se recupera del bundle, el cual ya contiene el formato HTML para los saltos de línea
        JLabel lblMensaje = new JLabel(textos.getString("error.mensaje"), SwingConstants.CENTER);
        gbc.gridy = 2;
        centerPanel.add(lblMensaje, gbc);

        // 4. Botón "Reportar un problema" (Internacionalizado)
        JButton btnReportar = new JButton(textos.getString("error.reportar"));
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
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        bottomPanel.setOpaque(false);

        // Botón circular de la flecha
        JButton btnVolver = new JButton("⬅"); 
        btnVolver.setFont(new Font("Arial", Font.BOLD, 18));
        btnVolver.setBackground(new Color(160, 255, 90)); 
        btnVolver.setForeground(Color.BLACK);
        btnVolver.setFocusPainted(false);
        btnVolver.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));

        bottomPanel.add(btnVolver);
        add(bottomPanel, BorderLayout.SOUTH);

        // --- LÓGICA DE NAVEGACIÓN ---

        btnVolver.addActionListener(e -> {
            mainFrame.showView("LOGIN");
        });

        btnReportar.addActionListener(e -> {
            // mainFrame.showView("REPORTE");
            System.out.println("Navegando a la pantalla de reporte de errores...");
        });
    }
}