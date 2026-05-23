package gui;

import java.awt.*;
import java.awt.event.FocusAdapter; // escucha de foco (ganar/perder foco)
import java.awt.event.FocusEvent;   // evento de foco
import java.util.ResourceBundle;    // para internacionalización de textos
import javax.swing.*;               // componentes Swing principales
import javax.swing.border.EmptyBorder; // borde vacío para márgenes internos
import main.MainFrame;              // clase principal del GUI (controlador de navegación)
import model.BotonRedondeado;       // botón personalizado con esquinas redondeadas
import model.CredentialStore;       // gestión simple de credenciales (cambio de contraseña)

// Panel que permite al usuario cambiar su contraseña.
public class CambiarContrasenaPanel extends JPanel {

    // Referencia a la ventana principal para navegación y acceso a recursos.
    private final MainFrame mainFrame;
    // Temporizador que oculta el mensaje de estado y navega atrás cuando la operación fue exitosa.
    private Timer statusTimer;
    // Etiqueta donde se muestran mensajes de estado al usuario (error/éxito).
    private final JLabel statusLabel;

    // Constructor: crea y compone todos los componentes UI del panel.
    public CambiarContrasenaPanel(MainFrame mainFrame) {
        // Guardar referencia a la ventana principal recibida como parámetro.
        this.mainFrame = mainFrame;
        // Obtener ResourceBundle (texto localizado) desde la ventana principal.
        ResourceBundle textos = mainFrame.getBundle();

        // Establecer el gestor de layout principal y fondo del panel.
        setLayout(new BorderLayout()); // layout de 5 regiones
        setBackground(new Color(175, 255, 100)); // color de fondo del panel

        // Panel central con GridBagLayout para apilar campos y etiquetas.
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false); // fondo transparente (hereda del padre)
        centerPanel.setBorder(new EmptyBorder(18, 24, 18, 24)); // margen interior

        // Configuración inicial de las restricciones de GridBag (columnas, relleno, márgenes).
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // una sola columna
        gbc.fill = GridBagConstraints.HORIZONTAL; // expandir horizontalmente
        gbc.insets = new Insets(8, 0, 8, 0); // separaciones entre filas

        // Título del panel (cadena localizada en el bundle).
        JLabel title = new JLabel(textos.getString("config.change_password_title"), SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22)); // tamaño y estilo del texto
        gbc.gridy = 0; // fila 0
        centerPanel.add(title, gbc); // añadir al panel central

        // Obtener el email del usuario actualmente logueado desde MainFrame.
        String currentUserEmail = mainFrame.getCurrentUserEmail();
        // Etiqueta que muestra el email (o vacía si es null).
        JLabel userLabel = new JLabel(currentUserEmail != null ? currentUserEmail : "", SwingConstants.CENTER);
        userLabel.setFont(new Font("Arial", Font.PLAIN, 12)); // fuente más pequeña
        userLabel.setForeground(new Color(80, 80, 80)); // color gris oscuro
        gbc.gridy = 1; // fila 1
        gbc.insets = new Insets(0, 0, 12, 0); // ajustar margen inferior
        centerPanel.add(userLabel, gbc); // añadir etiqueta de usuario

        // Campo para la contraseña actual (placeholder localizado). Se marca como campo de tipo password.
        JTextField currentPassword = createField(textos.getString("config.change_password_current"), true);
        gbc.gridy = 2; // fila 2
        gbc.insets = new Insets(8, 0, 8, 0); // restaurar márgenes
        centerPanel.add(currentPassword, gbc); // añadir campo actual

        // Campo para la nueva contraseña.
        JTextField newPassword = createField(textos.getString("config.change_password_new"), true);
        gbc.gridy = 3; // fila 3
        centerPanel.add(newPassword, gbc); // añadir campo nueva contraseña

        // Campo para confirmar la nueva contraseña.
        JTextField confirmPassword = createField(textos.getString("config.change_password_confirm"), true);
        gbc.gridy = 4; // fila 4
        centerPanel.add(confirmPassword, gbc); // añadir campo de confirmación

        // Etiqueta de estado/feedback (inicialmente vacía, centrada).
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12)); // fuente para mensajes
        statusLabel.setForeground(new Color(170, 40, 40)); // color por defecto (error)
        statusLabel.setOpaque(false); // inicialmente transparente
        statusLabel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10)); // padding
        gbc.gridy = 5; // fila 5
        gbc.insets = new Insets(6, 0, 4, 0); // márgenes para la etiqueta
        centerPanel.add(statusLabel, gbc); // añadir etiqueta de estado

        // Botón de guardar/cambiar contraseña, usando botón redondeado personalizado.
        BotonRedondeado saveBtn = new BotonRedondeado(textos.getString("common.send"));
        saveBtn.setPreferredSize(new Dimension(120, 38)); // tamaño preferido del botón
        saveBtn.setBackground(Color.WHITE); // fondo blanco del botón
        saveBtn.setForeground(Color.BLACK); // texto en negro
        // Acción al pulsar el botón: validar campos y llamar a CredentialStore.changePassword
        saveBtn.addActionListener(e -> {
            // Obtener email del usuario actual nuevamente
            String email = mainFrame.getCurrentUserEmail();
            // Si no hay email (no logueado), mostrar mensaje y salir
            if (email == null || email.isBlank()) {
                showStatus(textos.getString("config.change_password_login_required"), false);
                return;
            }

            // Leer las contraseñas desde los JPasswordField (convertir a String)
            String current = new String(((JPasswordField) currentPassword).getPassword());
            String fresh = new String(((JPasswordField) newPassword).getPassword());
            String confirm = new String(((JPasswordField) confirmPassword).getPassword());

            // Validación: el valor no debe ser igual al placeholder inicial.
            if (current.equals(textos.getString("config.change_password_current"))) {
                showStatus(textos.getString("config.change_password_wrong"), false);
                return;
            }

            // Validación: nueva contraseña no puede ser el placeholder ni estar vacía.
            if (fresh.equals(textos.getString("config.change_password_new")) || fresh.isBlank()) {
                showStatus(textos.getString("config.change_password_empty"), false);
                return;
            }

            // Validación: la confirmación debe coincidir con la nueva contraseña.
            if (!fresh.equals(confirm)) {
                showStatus(textos.getString("config.change_password_mismatch"), false);
                return;
            }

            // Intentar cambiar la contraseña en el store; si falla, mostrar error.
            if (!CredentialStore.changePassword(email, current, fresh)) {
                showStatus(textos.getString("config.change_password_wrong"), false);
                return;
            }

            // Si todo va bien, resetear los placeholders de los campos y quitar el echo char.
            currentPassword.setText(textos.getString("config.change_password_current"));
            ((JPasswordField) currentPassword).setEchoChar((char) 0); // mostrar placeholder
            newPassword.setText(textos.getString("config.change_password_new"));
            ((JPasswordField) newPassword).setEchoChar((char) 0);
            confirmPassword.setText(textos.getString("config.change_password_confirm"));
            ((JPasswordField) confirmPassword).setEchoChar((char) 0);
            // Mostrar mensaje de éxito y programar volver atrás.
            showStatus(textos.getString("config.change_password_success"), true);
        });

        // Wrapper para centrar el botón en el layout.
        JPanel buttonWrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        buttonWrap.setOpaque(false); // fondo transparente
        buttonWrap.add(saveBtn); // añadir botón al wrapper
        gbc.gridy = 6; // fila 6
        gbc.insets = new Insets(16, 0, 6, 0); // margen superior para el botón
        centerPanel.add(buttonWrap, gbc); // añadir wrapper al panel central

        // Añadir el panel central al centro del BorderLayout del panel principal.
        add(centerPanel, BorderLayout.CENTER);

        // Panel inferior (footer) con botón de volver.
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 14));
        footerPanel.setOpaque(false);

        // Botón de volver (navegación hacia atrás), también redondeado.
        BotonRedondeado backBtn = new BotonRedondeado(textos.getString("config.back"));
        backBtn.setPreferredSize(new Dimension(120, 36)); // tamaño del botón de volver
        backBtn.setForeground(Color.BLACK); // texto negro
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // cursor de mano
        // Acción al pulsar volver: detener temporizador si existe y navegar atrás.
        backBtn.addActionListener(e -> {
            if (statusTimer != null && statusTimer.isRunning()) {
                statusTimer.stop();
                statusTimer = null; // limpiar referencia
            }
            mainFrame.goBack(); // pedir al MainFrame que vuelva a la vista anterior
        });
        footerPanel.add(backBtn); // añadir botón de volver al footer

        // Añadir el footer al sur del BorderLayout del panel principal.
        add(footerPanel, BorderLayout.SOUTH);
    }

    // Método que muestra un mensaje de estado y, si es éxito, vuelve atrás tras un retardo.
    private void showStatus(String message, boolean success) {
        // Si hay un temporizador activo, detenerlo para reiniciar el tiempo.
        if (statusTimer != null && statusTimer.isRunning()) {
            statusTimer.stop();
        }

        // Actualizar texto y colores de la etiqueta de estado según éxito o error.
        statusLabel.setText(message);
        statusLabel.setForeground(success ? Color.WHITE : new Color(170, 40, 40));
        statusLabel.setBackground(success ? Color.BLACK : new Color(175, 255, 100));
        statusLabel.setOpaque(success); // si es éxito, hacer fondo visible
        statusLabel.revalidate(); // solicitar re-layout
        statusLabel.repaint(); // repintar la etiqueta

        // Si la operación fue un éxito, iniciar un Timer que navega atrás después de 2800ms.
        if (success) {
            statusTimer = new Timer(2800, e -> {
                statusTimer = null; // limpiar referencia al temporizador
                mainFrame.goBack(); // navegar atrás automáticamente
            });
            statusTimer.setRepeats(false); // solo una ejecución
            statusTimer.start(); // iniciar el temporizador
        }
    }

    // Método auxiliar para crear campos de texto o password con placeholder y comportamiento de foco.
    private JTextField createField(String placeholder, boolean password) {
        // Crear JPasswordField si password==true, en caso contrario JTextField normal.
        JTextField field = password ? new JPasswordField(placeholder) : new JTextField(placeholder);
        field.setPreferredSize(new Dimension(250, 40)); // tamaño preferido del campo
        // Bordes compuestos: línea exterior blanca + padding interno
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE),
            new EmptyBorder(5, 10, 5, 10)
        ));
        field.setFont(new Font("Arial", Font.PLAIN, 13)); // fuente del texto
        field.setBackground(Color.WHITE); // fondo blanco del campo

        // Si es campo de password, ocultar el contenido inicialmente (echo char 0 para el placeholder).
        if (password) {
            ((JPasswordField) field).setEchoChar((char) 0);
        }

        // Añadir listener de foco para comportarse como campo con placeholder.
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                // Al ganar foco, si el texto es el placeholder, limpiarlo y activar el echo char.
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    if (password) {
                        ((JPasswordField) field).setEchoChar('•'); // sustituto gráfico para ocultar texto
                    }
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                // Al perder foco, si el campo quedó vacío, restaurar el placeholder y desactivar echo char.
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    if (password) {
                        ((JPasswordField) field).setEchoChar((char) 0);
                    }
                }
            }
        });

        // Devolver el campo preparado.
        return field;
    }
}