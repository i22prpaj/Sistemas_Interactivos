package gui;

// Importaciones necesarias para construir la interfaz y manejar eventos.
import java.awt.*;                        // Clases AWT (Color, Dimension, Cursor...)
import java.awt.event.FocusAdapter;       // Adaptador para escuchar focus (ganar/perder foco)
import java.awt.event.FocusEvent;         // Evento de foco
import java.util.ResourceBundle;          // Soporte para textos localizados
import javax.swing.*;                      // Componentes Swing (JPanel, JLabel, JButton...)
import javax.swing.border.EmptyBorder;    // Borde vacío para márgenes internos
import main.MainFrame;                    // Clase principal que controla la navegación
import model.BotonRedondeado;             // Botón personalizado con esquinas redondeadas

// Panel de registro de usuario. Contiene el formulario y la lógica de validación básica.
public class RegistroPanel extends JPanel {

    // Referencia a la ventana principal (se usa para cambiar de vista y obtener textos).
    private MainFrame mainFrame;
    // Etiqueta que muestra mensajes temporales relacionados con el registro.
    private JLabel registroMessageLabel;
    // Temporizador que oculta el mensaje después de unos segundos.
    private Timer registroMessageTimer;

    // Constructor: crea la interfaz y enlaza la lógica de los botones.
    public RegistroPanel(MainFrame mainFrame) {
        // Guardar referencia al frame principal recibido como argumento.
        this.mainFrame = mainFrame;
        // Obtener el bundle de textos para internacionalización.
        ResourceBundle textos = mainFrame.getBundle();

        // Layout principal del panel y color de fondo.
        setLayout(new BorderLayout());
        setBackground(new Color(175, 255, 100)); // color de fondo (verde lima)

        // --- PANEL CENTRAL (Formulario) ---
        JPanel centerPanel = new JPanel(new GridBagLayout()); // panel con GridBag para formulario
        centerPanel.setOpaque(false); // transparente para heredar fondo
        GridBagConstraints gbc = new GridBagConstraints(); // restricciones para GridBag
        gbc.insets = new Insets(8, 20, 8, 20); // márgenes por defecto entre componentes
        gbc.gridx = 0; // usamos una sola columna
        gbc.fill = GridBagConstraints.HORIZONTAL; // que los componentes se estiren horizontalmente

        // 1. Texto de bienvenida (localizado)
        JLabel lblBienvenida = new JLabel(textos.getString("registro.bienvenida"), SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 15)); // fuente y estilo
        gbc.gridy = 0; // primera fila
        gbc.insets = new Insets(20, 20, 20, 20); // mayor espacio alrededor del título
        centerPanel.add(lblBienvenida, gbc); // añadir etiqueta al panel central
        gbc.insets = new Insets(8, 20, 8, 20); // restaurar insets para los siguientes campos

        // 2. Campos de texto con placeholders (usar crearCampo)
        JTextField txtCorreo = crearCampo(textos.getString("registro.correo"), false);
        gbc.gridy = 1; centerPanel.add(wrapCentered(txtCorreo, 35), gbc); // añadir correo

        JPasswordField txtPass = (JPasswordField) crearCampo(textos.getString("registro.pass"), true);
        gbc.gridy = 2; centerPanel.add(wrapCentered(txtPass, 35), gbc); // añadir contraseña

        JPasswordField txtConfirmPass = (JPasswordField) crearCampo(textos.getString("registro.confirmPass"), true);
        gbc.gridy = 3; centerPanel.add(wrapCentered(txtConfirmPass, 35), gbc); // añadir confirmación

        JTextField txtNombre = crearCampo(textos.getString("registro.nombre"), false);
        gbc.gridy = 4; centerPanel.add(wrapCentered(txtNombre, 35), gbc); // añadir nombre

        JTextField txtApellidos = crearCampo(textos.getString("registro.apellidos"), false);
        gbc.gridy = 5; centerPanel.add(wrapCentered(txtApellidos, 35), gbc); // añadir apellidos

        // 3. Desplegable para seleccionar grado (opciones localizadas)
        String[] grados = {
            textos.getString("registro.gradoPrompt"),
            textos.getString("grado.informatica"),
            textos.getString("grado.derecho"),
            textos.getString("grado.medicina"),
            textos.getString("grado.veterinaria")
        };
        JComboBox<String> comboGrado = new JComboBox<>(grados); // combo con las opciones
        comboGrado.setPreferredSize(new Dimension(220, 20));
        comboGrado.setBackground(Color.WHITE);
        gbc.gridy = 6; // fila para el combo
        centerPanel.add(wrapCentered(comboGrado, 35), gbc);

        // 4. Checkbox para aceptar términos (texto enriquecido con HTML para ajustar ancho)
        JCheckBox checkTerminos = new JCheckBox(textos.getString("registro.terminos"));
        checkTerminos.setText("<html><span style='width:280px;'>" + textos.getString("registro.terminos") + "</span></html>");
        checkTerminos.setOpaque(false); // fondo transparente
        checkTerminos.setFont(new Font("Arial", Font.BOLD, 11));
        gbc.gridy = 7; // fila del checkbox
        centerPanel.add(checkTerminos, gbc);

        // 5. Botón Registrar (BotonRedondeado personalizado)
        BotonRedondeado btnRegistrar = new BotonRedondeado(textos.getString("registro.boton"));
        btnRegistrar.setBackground(Color.WHITE);
        btnRegistrar.setForeground(Color.BLACK);
        btnRegistrar.setFont(new Font("Arial", Font.BOLD, 14));
        btnRegistrar.setPreferredSize(new Dimension(150, 35));
        gbc.gridy = 8; // fila del botón
        gbc.insets = new Insets(10, 20, 20, 20); // margen alrededor del botón
        centerPanel.add(wrapCentered(btnRegistrar, 35), gbc);

        // Añadir el panel central al centro del layout principal
        add(centerPanel, BorderLayout.CENTER);

        // --- PANEL INFERIOR (Mensaje + Botón Atrás) ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        bottomPanel.setOpaque(false);
        BotonRedondeado btnAtras = new BotonRedondeado("⬅"); // botón para volver
        btnAtras.setFont(new Font("Arial", Font.BOLD, 18));
        btnAtras.setPreferredSize(new Dimension(48, 36));
        btnAtras.setForeground(Color.BLACK);
        btnAtras.setCursor(new Cursor(Cursor.HAND_CURSOR)); // cursor mano para indicar interactividad
        bottomPanel.add(btnAtras); // añadir botón atrás al panel inferior

        // Wrapper que coloca el messageBar encima y el bottomPanel en la parte inferior
        JPanel bottomWrapper = new JPanel(new BorderLayout());
        bottomWrapper.setOpaque(false);

        JPanel messageBar = new JPanel(new BorderLayout());
        messageBar.setOpaque(false);
        messageBar.setBorder(BorderFactory.createEmptyBorder(0, 20, 8, 20)); // padding para la barra de mensajes

        // Etiqueta que muestra el mensaje sobre aceptar términos (invisible por defecto)
        registroMessageLabel = new JLabel(textos.getString("registro.terms_required"), SwingConstants.CENTER);
        registroMessageLabel.setOpaque(true); // se pintará con fondo cuando se muestre
        registroMessageLabel.setBackground(Color.BLACK);
        registroMessageLabel.setForeground(Color.WHITE);
        registroMessageLabel.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        registroMessageLabel.setVisible(false); // oculto inicialmente
        registroMessageLabel.setFont(new Font("Arial", Font.BOLD, 13));

        messageBar.add(registroMessageLabel, BorderLayout.CENTER); // centrar la etiqueta en la barra
        bottomWrapper.add(messageBar, BorderLayout.NORTH); // barra arriba del wrapper
        bottomWrapper.add(bottomPanel, BorderLayout.SOUTH); // panel con botón abajo del wrapper

        add(bottomWrapper, BorderLayout.SOUTH); // añadir todo al sur del panel principal

        // --- LÓGICA DE BOTONES ---
        // Acción del botón atrás: pedir a MainFrame que muestre la vista de login
        btnAtras.addActionListener(e -> mainFrame.showView("LOGIN"));
        
        // Acción del botón registrar: si acepta términos, navegar a la pantalla principal; si no, mostrar mensaje
        btnRegistrar.addActionListener(e -> {
            if(checkTerminos.isSelected()) {
                hideRegistroMessage(); // ocultar mensaje si estaba visible
                System.out.println("Registro completado. Navegando a pantalla principal...");
                mainFrame.showView("MAIN_ESTUDIANTE"); 
            } else {
                showRegistroMessage(); // mostrar mensaje temporal indicando que acepte términos
            }
        });
    }

    // Muestra la etiqueta de mensaje temporalmente y la oculta tras 2.8s.
    private void showRegistroMessage() {
        if (registroMessageLabel == null) {
            return; // seguridad: nada que mostrar
        }

        // Si ya hay un temporizador corriendo, pararlo para reiniciar el tiempo.
        if (registroMessageTimer != null && registroMessageTimer.isRunning()) {
            registroMessageTimer.stop();
        }

        registroMessageLabel.setVisible(true); // hacer visible la etiqueta
        registroMessageLabel.revalidate(); // forzar recálculo del layout
        registroMessageLabel.repaint(); // repintar componente

        // Programar timer que ocultará la etiqueta después de 2800 ms
        registroMessageTimer = new Timer(2800, e -> hideRegistroMessage());
        registroMessageTimer.setRepeats(false);
        registroMessageTimer.start();
    }

    // Oculta la etiqueta de mensaje si existe.
    private void hideRegistroMessage() {
        if (registroMessageLabel == null) {
            return;
        }
        registroMessageLabel.setVisible(false);
        registroMessageLabel.revalidate();
        registroMessageLabel.repaint();
    }

    // Método auxiliar para crear campos con placeholder (se explica la primera vez que aparece en otro archivo).
    private JTextField crearCampo(String placeholder, boolean esPassword) {
        JTextField campo = esPassword ? new JPasswordField(placeholder) : new JTextField(placeholder);
        campo.setPreferredSize(new Dimension(220, 40));
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE), 
            new EmptyBorder(7, 10, 9, 10)
        ));

        // Si es un campo de contraseña, desactivar el eco char para mostrar el placeholder inicialmente
        if (esPassword) ((JPasswordField) campo).setEchoChar((char) 0);

        // Listener de foco: al ganar foco, si el contenido es el placeholder, limpiar el campo y ocultar el texto
        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (campo.getText().equals(placeholder)) {
                    campo.setText("");
                    if (esPassword) ((JPasswordField) campo).setEchoChar('•'); // caracter de ocultación
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                // Si al perder foco el campo quedó vacío, restaurar el placeholder
                if (campo.getText().isEmpty()) {
                    campo.setText(placeholder);
                    if (esPassword) ((JPasswordField) campo).setEchoChar((char) 0); // mostrar placeholder
                }
            }
        });
        return campo;
    }

    // Wrapper que centra un componente y añade padding horizontal; reutilizado en varios formularios.
    private JPanel wrapCentered(JComponent component, int horizontalPadding) {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(0, horizontalPadding, 0, horizontalPadding));
        wrapper.add(component);
        return wrapper;
    }
}