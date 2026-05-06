package gui;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import main.MainFrame;

public class LoginPanel extends JPanel {

    private static final boolean DEV_MODE = true;
    private MainFrame mainFrame; // Referencia al contenedor principal para cambiar de pantalla

    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        ResourceBundle textos = mainFrame.getBundle();
        
        // 1. Configuración del fondo (Verde lima según tu diseño)
        setBackground(new Color(175, 255, 100)); 
        setLayout(new BorderLayout());
        
        // Panel contenedor con GridBagLayout
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(175, 255, 100));
        contentPanel.setBorder(new EmptyBorder(10, 50, 10, 50)); // Márgenes a los lados
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Márgenes entre elementos
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 2. Logo UCO
        JLabel logoLabel = new JLabel();
        try {
            // Cargar la imagen y redimensionar manteniendo proporción
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/resources/uco_logo.png"));
            Image originalImage = logoIcon.getImage();
            
            // Obtener dimensiones originales
            int originalWidth = originalImage.getWidth(null);
            int originalHeight = originalImage.getHeight(null);
            
            // Escalar manteniendo proporción (más pequeño)
            int maxWidth = 110;
            double ratio = (double) originalHeight / originalWidth;
            int newWidth = maxWidth;
            int newHeight = (int) (maxWidth * ratio);
            
            Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(scaledImage));
            logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        } catch (Exception e) {
            logoLabel.setText("[ Imagen Logo UCO ]");
            logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }
        gbc.gridy = 0;
        contentPanel.add(logoLabel, gbc);

        // 3. Título
        JLabel titulo = new JLabel(textos.getString("login.titulo"), SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridy = 1;
        contentPanel.add(titulo, gbc);

        // 4. Campo de Correo (con "placeholder" simulado)
        String placeholderCorreo = textos.getString("login.correo");
        JTextField txtCorreo = new JTextField();
        txtCorreo.setToolTipText(placeholderCorreo);
        configurarCampoTexto(txtCorreo, placeholderCorreo);
        gbc.gridy = 2;
        contentPanel.add(wrapCentered(txtCorreo, 35), gbc);

        // 5. Campo de Contraseña (JPasswordField para ocultar caracteres)
        String placeholderPass = textos.getString("login.password");
        JPasswordField txtPassword = new JPasswordField(placeholderPass);
        configurarCampoTexto(txtPassword, placeholderPass);        
        gbc.gridy = 3;
        contentPanel.add(wrapCentered(txtPassword, 35), gbc);

        // 6. Sección de "¿Aún no tienes acceso? Registro"
        JLabel lblPregunta = new JLabel(textos.getString("login.pregunta"));
        JButton btnRegistro = new JButton(textos.getString("login.registro"));

        JPanel panelRegistro = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panelRegistro.setOpaque(false); // Para que se vea el fondo verde
        
        lblPregunta.setFont(new Font("Arial", Font.PLAIN, 12));
        
        btnRegistro.setForeground(Color.BLUE);
        btnRegistro.setBorderPainted(false);
        btnRegistro.setContentAreaFilled(false);
        btnRegistro.setFocusPainted(false);
        btnRegistro.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        panelRegistro.add(lblPregunta);
        panelRegistro.add(btnRegistro);
        gbc.gridy = 4;
        contentPanel.add(panelRegistro, gbc);

        // 7. Botón de Iniciar Sesión (Color granate/magenta)
        JButton btnIniciar = new JButton(textos.getString("login.boton"));
        btnIniciar.setBackground(new Color(194, 24, 91)); // Color rosa/granate de tu diseño
        btnIniciar.setForeground(Color.WHITE);
        btnIniciar.setFont(new Font("Arial", Font.BOLD, 14));
        btnIniciar.setFocusPainted(false);
        btnIniciar.setBorder(new EmptyBorder(10, 10, 10, 10)); // Botón más grande
        btnIniciar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 5;
        gbc.insets = new Insets(20, 10, 10, 10); // Margen externo mínimo; el padding lo pone el wrapper
        contentPanel.add(wrapCentered(btnIniciar, 50), gbc);

        if (DEV_MODE) {
            JPanel devPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
            devPanel.setOpaque(false);

            JLabel devLabel = new JLabel("Modo dev:");
            devLabel.setFont(new Font("Arial", Font.PLAIN, 11));

            JButton btnDevPrincipal = new JButton("Principal");
            JButton btnDevPrincipalModerador = new JButton("Moderador");
            
            btnDevPrincipal.addActionListener(e -> mainFrame.showView("MAIN_ESTUDIANTE"));
            // Acceso directo al MainPanel con rol moderador
            btnDevPrincipalModerador.addActionListener(e -> {
                mainFrame.setUserRole("MODERADOR");
                mainFrame.showView("MAIN_ESTUDIANTE");
            });
            
            devPanel.add(devLabel);
            devPanel.add(btnDevPrincipal);
            devPanel.add(btnDevPrincipalModerador);

            gbc.gridy = 6;
            gbc.insets = new Insets(6, 18, 6, 18);
            contentPanel.add(devPanel, gbc);
        }
        
        add(contentPanel, BorderLayout.CENTER);

        // --- ACCIONES Y NAVEGACIÓN ---

        // Navegación 4: Ir a Registro
        btnRegistro.addActionListener(e -> {
            mainFrame.showView("REGISTRO"); // Llamamos a la vista de registro (que crearemos luego)
        });

        // Lógica simulada de Iniciar Sesión (Navegación 1, 2 y 3)
        btnIniciar.addActionListener(e -> {
            String correo = txtCorreo.getText();
            String password = new String(txtPassword.getPassword());

            if (correo.equals("moderador@uco.es") && password.equals("1234")) {
                // Caso 2: Cuenta de moderador
                // Aquí en un futuro le pasaríamos un parámetro al MainFrame indicando el rol
                System.out.println("Acceso concedido: Moderador");
                mainFrame.setUserRole("MODERADOR");
                mainFrame.showView("MAIN_ESTUDIANTE");
            } else if (correo.equals("elena.ruiz@uco.es") && password.equals("1234")) {
                // Caso 1: Cuenta general
                System.out.println("Acceso concedido: Estudiante");
                mainFrame.setUserRole("ESTUDIANTE");
                mainFrame.showView("MAIN_ESTUDIANTE");
            } else {
                // Caso 3: Credenciales inválidas
                System.out.println("Error de credenciales");
                mainFrame.showView("LOGIN_ERROR"); // Vista de error (que crearemos luego)
            }
        });
    }

    // Método auxiliar para simular el comportamiento de "Placeholder" y dar estilo a los campos
    private void configurarCampoTexto(JTextField campo, String textoPorDefecto) {
        campo.setPreferredSize(new Dimension(220, 40));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY), 
                new EmptyBorder(5, 10, 10, 10) // Padding interno
        ));
        
        // Simular Placeholder: Se borra al hacer clic, vuelve si está vacío
        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (campo.getText().equals(textoPorDefecto)) {
                    campo.setText("");
                    if (campo instanceof JPasswordField) {
                        ((JPasswordField) campo).setEchoChar('•'); // Ocultar al escribir
                    }
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (campo.getText().isEmpty()) {
                    campo.setText(textoPorDefecto);
                    if (campo instanceof JPasswordField) {
                        ((JPasswordField) campo).setEchoChar((char) 0); // Mostrar el texto "Contraseña"
                    }
                }
            }
        });
        
        // Si es contraseña, mostramos el texto "Contraseña" al principio sin ocultar
        if (campo instanceof JPasswordField && campo.getText().equals(textoPorDefecto)) {
            ((JPasswordField) campo).setEchoChar((char) 0); 
        }
    }

    private JPanel wrapCentered(JComponent component, int horizontalPadding) {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(0, horizontalPadding, 0, horizontalPadding));
        wrapper.add(component);
        return wrapper;
    }
}