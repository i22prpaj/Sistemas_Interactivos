package gui;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import main.MainFrame;
import model.BotonRedondeado;

public class RegistroPanel extends JPanel {

    private MainFrame mainFrame;
    private JLabel registroMessageLabel;
    private Timer registroMessageTimer;

    public RegistroPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        ResourceBundle textos = mainFrame.getBundle();

        setLayout(new BorderLayout());
        setBackground(new Color(175, 255, 100)); // Fondo verde lima

        // --- PANEL CENTRAL (Formulario) ---
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 20, 8, 20);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 1. Texto Bienvenida
        JLabel lblBienvenida = new JLabel(textos.getString("registro.bienvenida"), SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 15));
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 20, 20);
        centerPanel.add(lblBienvenida, gbc);
        gbc.insets = new Insets(8, 20, 8, 20); // Reset insets

        // 2. Campos de texto con Placeholders
        JTextField txtCorreo = crearCampo(textos.getString("registro.correo"), false);
        gbc.gridy = 1; centerPanel.add(wrapCentered(txtCorreo, 35), gbc);

        JPasswordField txtPass = (JPasswordField) crearCampo(textos.getString("registro.pass"), true);
        gbc.gridy = 2; centerPanel.add(wrapCentered(txtPass, 35), gbc);

        JPasswordField txtConfirmPass = (JPasswordField) crearCampo(textos.getString("registro.confirmPass"), true);
        gbc.gridy = 3; centerPanel.add(wrapCentered(txtConfirmPass, 35), gbc);

        JTextField txtNombre = crearCampo(textos.getString("registro.nombre"), false);
        gbc.gridy = 4; centerPanel.add(wrapCentered(txtNombre, 35), gbc);

        JTextField txtApellidos = crearCampo(textos.getString("registro.apellidos"), false);
        gbc.gridy = 5; centerPanel.add(wrapCentered(txtApellidos, 35), gbc);

        // 3. Desplegable (JComboBox)
        String[] grados = {
            textos.getString("registro.gradoPrompt"),
            textos.getString("grado.informatica"),
            textos.getString("grado.derecho"),
            textos.getString("grado.medicina"),
            textos.getString("grado.veterinaria")
        };
        JComboBox<String> comboGrado = new JComboBox<>(grados);
        comboGrado.setPreferredSize(new Dimension(220, 20));
        comboGrado.setBackground(Color.WHITE);
        gbc.gridy = 6;
        centerPanel.add(wrapCentered(comboGrado, 35), gbc);

        // 4. Checkbox Términos
        JCheckBox checkTerminos = new JCheckBox(textos.getString("registro.terminos"));
        checkTerminos.setText("<html><span style='width:280px;'>" + textos.getString("registro.terminos") + "</span></html>");
        checkTerminos.setOpaque(false);
        checkTerminos.setFont(new Font("Arial", Font.BOLD, 11));
        gbc.gridy = 7;
        centerPanel.add(checkTerminos, gbc);

        // 5. Botón Registrar
        BotonRedondeado btnRegistrar = new BotonRedondeado(textos.getString("registro.boton"));
        btnRegistrar.setBackground(Color.WHITE);
        btnRegistrar.setForeground(Color.BLACK);
        btnRegistrar.setFont(new Font("Arial", Font.BOLD, 14));
        btnRegistrar.setPreferredSize(new Dimension(150, 35));
        gbc.gridy = 8;
        gbc.insets = new Insets(10, 20, 20, 20);
        centerPanel.add(wrapCentered(btnRegistrar, 35), gbc);

        add(centerPanel, BorderLayout.CENTER);

        // --- PANEL INFERIOR (Mensaje + Botón Atrás) ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        bottomPanel.setOpaque(false);
        BotonRedondeado btnAtras = new BotonRedondeado("⬅");
        btnAtras.setFont(new Font("Arial", Font.BOLD, 18));
        btnAtras.setPreferredSize(new Dimension(48, 36));
        btnAtras.setForeground(Color.BLACK);
        btnAtras.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bottomPanel.add(btnAtras);

        JPanel bottomWrapper = new JPanel(new BorderLayout());
        bottomWrapper.setOpaque(false);

        JPanel messageBar = new JPanel(new BorderLayout());
        messageBar.setOpaque(false);
        messageBar.setBorder(BorderFactory.createEmptyBorder(0, 20, 8, 20));

        registroMessageLabel = new JLabel(textos.getString("registro.terms_required"), SwingConstants.CENTER);
        registroMessageLabel.setOpaque(true);
        registroMessageLabel.setBackground(Color.BLACK);
        registroMessageLabel.setForeground(Color.WHITE);
        registroMessageLabel.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        registroMessageLabel.setVisible(false);
        registroMessageLabel.setFont(new Font("Arial", Font.BOLD, 13));

        messageBar.add(registroMessageLabel, BorderLayout.CENTER);
        bottomWrapper.add(messageBar, BorderLayout.NORTH);
        bottomWrapper.add(bottomPanel, BorderLayout.SOUTH);

        add(bottomWrapper, BorderLayout.SOUTH);

        // --- LÓGICA ---
        btnAtras.addActionListener(e -> mainFrame.showView("LOGIN"));
        
        btnRegistrar.addActionListener(e -> {
            if(checkTerminos.isSelected()) {
                hideRegistroMessage();
                System.out.println("Registro completado. Navegando a pantalla principal...");
                mainFrame.showView("MAIN_ESTUDIANTE"); 
            } else {
                showRegistroMessage();
            }
        });
    }

    private void showRegistroMessage() {
        if (registroMessageLabel == null) {
            return;
        }

        if (registroMessageTimer != null && registroMessageTimer.isRunning()) {
            registroMessageTimer.stop();
        }

        registroMessageLabel.setVisible(true);
        registroMessageLabel.revalidate();
        registroMessageLabel.repaint();

        registroMessageTimer = new Timer(2800, e -> hideRegistroMessage());
        registroMessageTimer.setRepeats(false);
        registroMessageTimer.start();
    }

    private void hideRegistroMessage() {
        if (registroMessageLabel == null) {
            return;
        }
        registroMessageLabel.setVisible(false);
        registroMessageLabel.revalidate();
        registroMessageLabel.repaint();
    }

    // Método auxiliar para crear campos con placeholder (reutilizando tu lógica)
    private JTextField crearCampo(String placeholder, boolean esPassword) {
        JTextField campo = esPassword ? new JPasswordField(placeholder) : new JTextField(placeholder);
        campo.setPreferredSize(new Dimension(220, 40));
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE), 
            new EmptyBorder(7, 10, 9, 10)
        ));

        if (esPassword) ((JPasswordField) campo).setEchoChar((char) 0);

        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (campo.getText().equals(placeholder)) {
                    campo.setText("");
                    if (esPassword) ((JPasswordField) campo).setEchoChar('•');
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (campo.getText().isEmpty()) {
                    campo.setText(placeholder);
                    if (esPassword) ((JPasswordField) campo).setEchoChar((char) 0);
                }
            }
        });
        return campo;
    }

    private JPanel wrapCentered(JComponent component, int horizontalPadding) {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(0, horizontalPadding, 0, horizontalPadding));
        wrapper.add(component);
        return wrapper;
    }
}