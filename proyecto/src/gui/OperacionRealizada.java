package gui;

import main.MainFrame;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ResourceBundle;
import model.BotonRedondeado;

public class OperacionRealizada extends JPanel {

    // Panel simple que muestra un mensaje de "operación realizada" con un
    // icono grande y un botón para volver a la vista previa. Se usa tras
    // acciones como enviar una valoración o completar un formulario.

    private MainFrame mainFrame;

    // Constructor: construye la vista y configura la navegación de retorno
    public OperacionRealizada(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        ResourceBundle textos = mainFrame.getBundle();

        // Layout centrado con márgenes amplios para destacar el mensaje
        setLayout(new GridBagLayout());
        setBackground(new Color(175, 255, 100)); // Fondo verde lima

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        // 1. Título: mensaje de confirmación (localizado)
        JLabel lblMensaje = new JLabel(textos.getString("OperacionRealizada.mensaje"), SwingConstants.CENTER);
        lblMensaje.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridy = 0;
        add(lblMensaje, gbc);

        // 2. Icono: intentamos cargar una imagen desde /resources; si no existe,
        // usamos un carácter de check grande como fallback (evita excepción y
        // permite que la UI siga siendo informativa en entornos limitados).
        JLabel lblIcono = new JLabel();
        try {
            // Intenta cargar un icono real si lo tienes en resources
            ImageIcon icon = new ImageIcon(getClass().getResource("/resources/check_verde.png"));
            Image img = icon.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
            lblIcono.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            // Fallback textual: símbolo check grande con color verde
            lblIcono.setText("✔");
            lblIcono.setFont(new Font("Arial", Font.BOLD, 150));
            lblIcono.setForeground(new Color(34, 139, 34)); // Verde bosque
        }
        lblIcono.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        gbc.insets = new Insets(40, 20, 60, 20); // separación vertical amplia
        add(lblIcono, gbc);

        // 3. Botón "volver": utiliza el `MainFrame` para volver a la vista que
        // corresponde según la operación realizada (dinámico).
        BotonRedondeado btnVolver = new BotonRedondeado(textos.getString("common.back"));
        btnVolver.setBackground(new Color(25, 25, 112)); // Azul oscuro
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFont(new Font("Arial", Font.BOLD, 14));
        btnVolver.setPreferredSize(new Dimension(160, 38));

        // Panel envolvente para centrar y limitar el tamaño del botón
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.setOpaque(false);
        panelBoton.add(btnVolver);

        gbc.gridy = 2;
        gbc.insets = new Insets(10, 20, 20, 20);
        add(panelBoton, gbc);

        // Acción del botón: navegar a la vista que MainFrame ha guardado como
        // destino de retorno tras la operación
        btnVolver.addActionListener(e -> mainFrame.showView(mainFrame.getOperationResultReturnView()));
    }
}