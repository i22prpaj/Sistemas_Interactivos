package gui;

// Importaciones necesarias para la interfaz y eventos
import java.awt.*;                        // Clases AWT (Color, Dimension, Cursor...)
import java.awt.event.FocusAdapter;       // Adaptador para eventos de foco
import java.awt.event.FocusEvent;         // Evento de foco
import java.util.*;                        // ResourceBundle y utilidades varias
import javax.swing.*;                      // Componentes Swing (JPanel, JLabel, JButton...)
import javax.swing.border.EmptyBorder;    // Borde vacío para márgenes internos
import main.MainFrame;                    // Frame principal que controla la navegación
import model.BotonRedondeado;             // Botón personalizado con estilo redondeado
import model.CredentialStore;             // Gestor simple de credenciales (autenticación)

// Panel de login: maneja la interfaz y la lógica básica de autenticación (simulada)
public class LoginPanel extends JPanel {

    // Bandera de desarrollo para mostrar botones auxiliares; mantener false en producción
    private static final boolean DEV_MODE = false;
    // Referencia al MainFrame para cambiar vistas y ajustar estado global (rol, email)
    private MainFrame mainFrame;

    // Constructor: inicializa la interfaz y enlaza acciones de botones
    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame; // guardar referencia al frame principal
        ResourceBundle textos = mainFrame.getBundle(); // obtener bundle de textos para i18n
        
        // 1. Configuración del fondo y layout principal
        setBackground(new Color(175, 255, 100)); // color de fondo (verde lima)
        setLayout(new BorderLayout()); // layout principal
        
        // Panel central que contendrá logo, campos y botones
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(175, 255, 100));
        contentPanel.setBorder(new EmptyBorder(10, 50, 10, 50)); // padding lateral
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // separación entre elementos
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL; // los componentes se estiran horizontalmente

        // 2. Logo UCO (intento de cargar recurso, si falla se muestra texto alternativo)
        JLabel logoLabel = new JLabel();
        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/resources/uco_logo.png"));
            Image originalImage = logoIcon.getImage();
            // Calcular proporciones para escalar a un ancho máximo
            int originalWidth = originalImage.getWidth(null);
            int originalHeight = originalImage.getHeight(null);
            int maxWidth = 110;
            double ratio = (double) originalHeight / originalWidth;
            int newWidth = maxWidth;
            int newHeight = (int) (maxWidth * ratio);
            Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(scaledImage));
            logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        } catch (Exception e) {
            // Si no se carga la imagen, mostrar un placeholder textual para el logo
            logoLabel.setText("[ Imagen Logo UCO ]");
            logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }
        gbc.gridy = 0; // fila 0
        contentPanel.add(logoLabel, gbc); // añadir logo al panel central

        // 3. Título localizado
        JLabel titulo = new JLabel(textos.getString("login.titulo"), SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridy = 1; // fila 1
        contentPanel.add(titulo, gbc);

        // 4. Campo de correo con placeholder simulado (se configura con configurarCampoTexto)
        String placeholderCorreo = textos.getString("login.correo");
        JTextField txtCorreo = new JTextField();
        txtCorreo.setToolTipText(placeholderCorreo); // tooltip con el texto por defecto
        configurarCampoTexto(txtCorreo, placeholderCorreo);
        gbc.gridy = 2; // fila 2
        contentPanel.add(wrapCentered(txtCorreo, 35), gbc);

        // 5. Campo de contraseña (JPasswordField) con placeholder
        String placeholderPass = textos.getString("login.password");
        JPasswordField txtPassword = new JPasswordField(placeholderPass);
        configurarCampoTexto(txtPassword, placeholderPass);        
        gbc.gridy = 3; // fila 3
        contentPanel.add(wrapCentered(txtPassword, 35), gbc);

        // 6. Sección de registro (texto y botón enlazable)
        JLabel lblPregunta = new JLabel(textos.getString("login.pregunta"));
        JButton btnRegistro = new JButton(textos.getString("login.registro"));

        JPanel panelRegistro = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panelRegistro.setOpaque(false); // fondo transparente para respetar el color del panel padre
        
        lblPregunta.setFont(new Font("Arial", Font.PLAIN, 12));
        
        // Estilizar el botón de registro para que parezca enlace
        btnRegistro.setForeground(Color.BLUE);
        btnRegistro.setBorderPainted(false);
        btnRegistro.setContentAreaFilled(false);
        btnRegistro.setFocusPainted(false);
        btnRegistro.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        panelRegistro.add(lblPregunta); // texto "¿Aún no tienes acceso?"
        panelRegistro.add(btnRegistro); // botón que actúa como enlace al registro
        gbc.gridy = 4; // fila 4
        contentPanel.add(panelRegistro, gbc);

        // 7. Botón de iniciar sesión principal (estilizado con BotonRedondeado)
        BotonRedondeado btnIniciar = new BotonRedondeado(textos.getString("login.boton"));
        btnIniciar.setBackground(new Color(194, 24, 91)); // color principal del botón
        btnIniciar.setForeground(Color.WHITE);
        btnIniciar.setFont(new Font("Arial", Font.BOLD, 14));
        btnIniciar.setPreferredSize(new Dimension(150, 35));
        gbc.gridy = 5; // fila 5
        gbc.insets = new Insets(20, 10, 10, 10); // margen alrededor del botón
        contentPanel.add(wrapCentered(btnIniciar, 50), gbc);

        // Modo de desarrollo: botones extra para navegar rápido (solo si DEV_MODE == true)
        if (DEV_MODE) {
            JPanel devPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
            devPanel.setOpaque(false);

            JLabel devLabel = new JLabel("Modo dev:");
            devLabel.setFont(new Font("Arial", Font.PLAIN, 11));

            JButton btnDevPrincipal = new JButton("Principal");
            JButton btnDevPrincipalModerador = new JButton("Moderador");
            
            // Atajos para pruebas: establecer rol y email y navegar al main
            btnDevPrincipal.addActionListener(e -> {
                mainFrame.setUserRole("ESTUDIANTE");
                mainFrame.setCurrentUserEmail("elena.ruiz@uco.es");
                mainFrame.showView("MAIN_ESTUDIANTE");
            });
            // Acceso directo como moderador
            btnDevPrincipalModerador.addActionListener(e -> {
                mainFrame.setUserRole("MODERADOR");
                mainFrame.setCurrentUserEmail("moderador@uco.es");
                mainFrame.showView("MAIN_ESTUDIANTE");
            });
            
            devPanel.add(devLabel);
            devPanel.add(btnDevPrincipal);
            devPanel.add(btnDevPrincipalModerador);

            gbc.gridy = 6;
            gbc.insets = new Insets(6, 18, 6, 18);
            contentPanel.add(devPanel, gbc);
        }
        
        // Añadir el panel central al centro del panel principal
        add(contentPanel, BorderLayout.CENTER);

        // --- ACCIONES Y NAVEGACIÓN ---

        // Botón de registro: cambiar a la vista de registro
        btnRegistro.addActionListener(e -> {
            mainFrame.showView("REGISTRO");
        });

        // Botón iniciar: lógica de autenticación (simulada con CredentialStore)
        btnIniciar.addActionListener(e -> {
            String correo = txtCorreo.getText();
            String password = new String(txtPassword.getPassword());

            // Autenticación y diferenciación de roles por email (ejemplo simplificado)
            if (CredentialStore.authenticate(correo, password) && correo.equalsIgnoreCase("moderador@uco.es")) {
                // Cuenta de moderador
                System.out.println("Acceso concedido: Moderador");
                mainFrame.setUserRole("MODERADOR");
                mainFrame.setCurrentUserEmail(correo);
                mainFrame.showView("MAIN_ESTUDIANTE");
            } else if (CredentialStore.authenticate(correo, password) && correo.equalsIgnoreCase("elena.ruiz@uco.es")) {
                // Cuenta de estudiante
                System.out.println("Acceso concedido: Estudiante");
                mainFrame.setUserRole("ESTUDIANTE");
                mainFrame.setCurrentUserEmail(correo);
                mainFrame.showView("MAIN_ESTUDIANTE");
            } else {
                // Credenciales inválidas: mostrar vista de error
                System.out.println("Error de credenciales");
                mainFrame.showView("LOGIN_ERROR");
            }
        });
    }

    // Método auxiliar: aplica estilo y comportamiento de "placeholder" a un campo de texto
    private void configurarCampoTexto(JTextField campo, String textoPorDefecto) {
        campo.setPreferredSize(new Dimension(220, 40));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY), 
                new EmptyBorder(5, 10, 10, 10) // padding interno
        ));
        
        // Simular placeholder: borrar al entrar y restaurar al perder foco si está vacío
        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (campo.getText().equals(textoPorDefecto)) {
                    campo.setText("");
                    if (campo instanceof JPasswordField) {
                        ((JPasswordField) campo).setEchoChar('•'); // ocultar caracteres
                    }
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (campo.getText().isEmpty()) {
                    campo.setText(textoPorDefecto);
                    if (campo instanceof JPasswordField) {
                        ((JPasswordField) campo).setEchoChar((char) 0); // mostrar placeholder
                    }
                }
            }
        });
        
        // Si es JPasswordField y contiene aún el placeholder, desactivar el echo char (mostrar texto)
        if (campo instanceof JPasswordField && campo.getText().equals(textoPorDefecto)) {
            ((JPasswordField) campo).setEchoChar((char) 0); 
        }
    }

    // Wrapper reutilizable para centrar componentes y añadir padding horizontal
    private JPanel wrapCentered(JComponent component, int horizontalPadding) {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(0, horizontalPadding, 0, horizontalPadding));
        wrapper.add(component);
        return wrapper;
    }
}