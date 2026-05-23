package gui;

import main.MainFrame;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ResourceBundle;
import model.BotonRedondeado;

public class ReportePanel extends JPanel {

    private MainFrame mainFrame;

    // Constructor: monta la UI para que el usuario describa y envíe un reporte
    public ReportePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        ResourceBundle textos = mainFrame.getBundle();

        // Layout y color de fondo (verde lima para destacar el formulario)
        setLayout(new GridBagLayout());
        setBackground(new Color(175, 255, 100));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 20, 15, 20); // espacio alrededor de componentes
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 1. TÍTULO: texto centrado y grande
        JLabel lblTitulo = new JLabel(textos.getString("common.report_problem"), SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        gbc.gridy = 0;
        add(lblTitulo, gbc);

        // 2. DESPLEGABLE: elegir tipo de reporte (primer elemento es prompt)
        String[] tiposError = {
            textos.getString("reporte.tipoPrompt"),
            textos.getString("reporte.tipo1"),
            textos.getString("reporte.tipo2"),
            textos.getString("reporte.tipo3")
        };
        JComboBox<String> comboErrores = new JComboBox<>(tiposError);
        comboErrores.setPreferredSize(new Dimension(280, 40));
        comboErrores.setBackground(new Color(230, 230, 230)); // Gris claro para contraste
        gbc.gridy = 1;
        add(comboErrores, gbc);

        // 3. ÁREA DE TEXTO: descripción del problema con placeholder
        String placeholder = textos.getString("reporte.descPrompt");
        JTextArea txtDescripcion = new JTextArea(placeholder);
        txtDescripcion.setPreferredSize(new Dimension(280, 150));
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setBackground(new Color(230, 230, 230)); // Gris claro
        txtDescripcion.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(10, 10, 10, 10)
        ));

        // Placeholder manual: al ganar foco borramos el texto si coincide con el placeholder,
        // y al perder foco lo restauramos si el usuario no escribió nada.
        txtDescripcion.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtDescripcion.getText().equals(placeholder)) {
                    txtDescripcion.setText("");
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (txtDescripcion.getText().isEmpty()) {
                    txtDescripcion.setText(placeholder);
                }
            }
        });
        gbc.gridy = 2;
        add(txtDescripcion, gbc);

        // 4. BOTONES: Cancelar (volver) y Enviar (procesar)
        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 20, 0));
        panelBotones.setOpaque(false);

        BotonRedondeado btnCancelar = new BotonRedondeado(textos.getString("reporte.cancelar"));
        btnCancelar.setBackground(Color.WHITE);
        btnCancelar.setForeground(Color.BLACK);
        btnCancelar.setPreferredSize(new Dimension(130, 38));
        btnCancelar.setFocusPainted(false);

        BotonRedondeado btnEnviar = new BotonRedondeado(textos.getString("common.send"));
        btnEnviar.setBackground(new Color(25, 25, 112)); // Azul oscuro tipo Midnight Blue
        btnEnviar.setForeground(Color.WHITE);
        btnEnviar.setPreferredSize(new Dimension(130, 38));
        btnEnviar.setFocusPainted(false);

        panelBotones.add(btnCancelar);
        panelBotones.add(btnEnviar);

        gbc.gridy = 3;
        gbc.insets = new Insets(30, 20, 10, 20); // más espacio encima de los botones
        add(panelBotones, gbc);

        // --- LÓGICA: navegación y acciones de botones ---
        // Cancelar: regresamos a la pantalla de error/login previa
        btnCancelar.addActionListener(e -> mainFrame.showView("LOGIN_ERROR"));

        // Enviar: en la versión demo no hay backend, así que mostramos la pantalla
        // de operación realizada; en producción aquí se podría enviar a un servicio.
        btnEnviar.addActionListener(e -> {
            // Ejemplo: construir objeto reporte y enviarlo a servidor / guardar
            mainFrame.showOperacionRealizada("LOGIN");
        });
    }
}