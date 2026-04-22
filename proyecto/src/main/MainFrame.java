package main;

import gui.LoginErrorPanel; // Importamos tu panel de login
import gui.LoginPanel; // Importamos tu panel de error
import java.awt.*; // Importamos tu panel de error
import javax.swing.*; // Importamos tu panel de error
import java.util.Locale;           
import java.util.ResourceBundle;   

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private ResourceBundle bundle;

    public MainFrame() {
        // 1. Establecer el idioma por defecto (Español de España)
        Locale.setDefault(new Locale("es", "ES"));
        
        // 2. Cargar el Bundle. Busca "Textos_es.properties" en la carpeta "resources"
        bundle = ResourceBundle.getBundle("resources.Textos", Locale.getDefault());

        setTitle("UCO-Reviews - Interfaz");
        setSize(350, 650); // Tamaño vertical tipo móvil
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar en la pantalla
        setResizable(false); // Evitar que se deforme

        // Usamos CardLayout para apilar las pantallas y mostrar solo una
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Añadimos las pantallas al contenedor. 
        // Le damos un "nombre clave" (ej. "LOGIN") para llamarla luego
        mainPanel.add(new LoginPanel(this), "LOGIN");
        
        // Aquí iremos añadiendo las demás según las vayamos creando:
        // mainPanel.add(new RegistroPanel(this), "REGISTRO");
        // mainPanel.add(new ErrorPanel(this), "ERROR");
        mainPanel.add(new LoginErrorPanel(this), "LOGIN_ERROR");

        add(mainPanel);
    }

    // Método que usan los paneles para pedirle al MainFrame que cambie de vista
    public void showView(String viewName) {
        cardLayout.show(mainPanel, viewName);
    }

    public static void main(String[] args) {
        // Evita crear ventanas Swing en entornos sin servidor gráfico.
        if (GraphicsEnvironment.isHeadless()) {
            System.err.println("Entorno sin interfaz grafica (headless): no se puede abrir la UI Swing.");
            System.err.println("Ejecuta esta aplicacion en tu equipo local con escritorio o con X11/Xvfb.");
            return;
        }

        // Iniciar la aplicación
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }

    // Método para que los paneles accedan al diccionario de textos
    public ResourceBundle getBundle() {
        return bundle;
    }
}