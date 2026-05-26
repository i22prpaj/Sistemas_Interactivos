package main;

import gui.Asignaturas;
import gui.ConfiguracionPanel;
import gui.AjustesCuentaPanel;
import gui.LoginErrorPanel;
import gui.LoginPanel;
import gui.MainPanel;
import gui.ModerationPanel;
import gui.ProfesorDetalle;
import gui.RegistroPanel;
import gui.CambiarContrasenaPanel;
import gui.ReportDetailPanel;
import gui.ReportePanel;
import gui.ValoracionPanel;
import gui.OperacionRealizada;
import java.awt.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;   

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private ResourceBundle bundle;
    private Deque<String> history = new ArrayDeque<>();
    private String currentView = null;
    private String userRole = "GUEST"; // Roles: GUEST, ESTUDIANTE, MODERADOR
    private String currentUserEmail = null;
    private String selectedSubjectKey = "subjects.intro_programacion";
    private String selectedProfessorId = null;
    private String selectedProfessorName = null;
    private String operationResultReturnView = "MAIN_ESTUDIANTE";
    private final boolean runningInCodespaces;

    public MainFrame() {
        // 1. Establecer el idioma por defecto (Español de España)
        Locale.setDefault(new Locale("es", "ES"));

        runningInCodespaces = "true".equalsIgnoreCase(System.getenv("CODESPACES"));
        
        // 2. Cargar el Bundle. Busca "Bundle_es.properties" en la carpeta "bundle"
        bundle = ResourceBundle.getBundle("bundle.Bundle", Locale.getDefault());

        setTitle("UCO-Reviews - Interfaz");
        setSize(runningInCodespaces ? 350 : 450, 650); // Más ancho en local
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar en la pantalla
        setResizable(false); // Evitar que se deforme

        // Usamos CardLayout para apilar las pantallas y mostrar solo una
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        buildViews();
        showView("LOGIN", false); // Muestra el login SOLO la primera vez que se abre la app

        add(mainPanel);
    }

    private void buildViews() {
        mainPanel.removeAll();

        // Añadimos las pantallas al contenedor. 
        // Le damos un "nombre clave" (ej. "INICIO") para llamarla luego
        mainPanel.add(new LoginPanel(this), "LOGIN");
        mainPanel.add(new LoginErrorPanel(this), "LOGIN_ERROR");
        mainPanel.add(new RegistroPanel(this), "REGISTRO");
        mainPanel.add(new ReportePanel(this), "REPORTE");
        mainPanel.add(new MainPanel(this), "MAIN_ESTUDIANTE");
        mainPanel.add(new ConfiguracionPanel(this), "CONFIGURACION");
        mainPanel.add(new AjustesCuentaPanel(this), "AJUSTES_CUENTA");
        mainPanel.add(new CambiarContrasenaPanel(this), "CAMBIAR_CONTRASENA");
        mainPanel.add(new Asignaturas(this), "ASIGNATURAS");
        mainPanel.add(new ProfesorDetalle(this), "PROFESOR_DETALLE");
        mainPanel.add(new ValoracionPanel(this), "VALORACION");
        mainPanel.add(new OperacionRealizada(this), "OPERACION_REALIZADA");
        mainPanel.add(new ModerationPanel(this), "MODERATION_PANEL");
        mainPanel.add(new ReportDetailPanel(this), "REPORT_DETAIL");
        
        mainPanel.revalidate();
        mainPanel.repaint();

    }

    // Método que usan los paneles para pedirle al MainFrame que cambie de vista
    public void showView(String viewName) {
        showView(viewName, true);
    }

    public void showView(String viewName, boolean addToHistory) {
        // Si `addToHistory` es true guardamos la vista actual en la pila de
        // historial para poder volver atrás (LIFO). Evitamos guardar si
        // `currentView` es null o coincide con la vista destino.
        if (addToHistory && currentView != null && !currentView.equals(viewName)) {
            history.push(currentView);
        }

        // Algunas vistas deben reconstruirse (recrear componentes) cuando se
        // muestran porque su contenido depende del estado actual (p.ej. lista
        // de asignaturas, detalle de profesor, etc.). `buildViews()` recrea
        // todas las pantallas y asegura que las vistas dependientes estén actualizadas.
        if ("ASIGNATURAS".equals(viewName) || "PROFESOR_DETALLE".equals(viewName) || "VALORACION".equals(viewName) || "CAMBIAR_CONTRASENA".equals(viewName) || "AJUSTES_CUENTA".equals(viewName)) {
            buildViews();
        }
        cardLayout.show(mainPanel, viewName);
        currentView = viewName; //registrar la vista actual para futuras navegaciones.
    }

    public void showOperacionRealizada(String returnView) {
        // Guardamos la vista a la que debemos volver después de mostrar la
        // pantalla de "operación realizada", y mostramos dicha vista sin
        // añadir la propia pantalla de resultado al historial.
        setOperationResultReturnView(returnView);
        showView("OPERACION_REALIZADA");
    }

    public void goBack() {
        // Volver a la vista anterior usando la pila de historial (LIFO).
        // Si el historial está vacío no hacemos nada.
        if (history.isEmpty()) return;
        String previous = history.pop();
        // Al mostrar la vista anterior no queremos volver a apilar la vista
        // actual en el historial, por eso pasamos `false` en addToHistory.
        showView(previous, false);
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

    public void setSelectedSubjectKey(String selectedSubjectKey) {
        // Establece la clave de la asignatura seleccionada.
        // Si se pasa `null` aplicamos una asignatura por defecto para evitar
        // valores nulos en el flujo de la UI.
        this.selectedSubjectKey = selectedSubjectKey == null ? "subjects.intro_programacion" : selectedSubjectKey;
    }

    public String getSelectedSubjectKey() {
        // Devuelve la clave (resource key) de la asignatura actualmente seleccionada.
        // Esta clave se usa para recuperar el texto localizado en el bundle.
        return selectedSubjectKey;
    }

    public void setSelectedProfessorKey(String selectedProfessorKey) {
        // Setter que acepta una "clave" de profesor (compatibilidad con llamadas
        // previas en el código). Internamente se guarda en el campo `selectedProfessorId`.
        this.selectedProfessorId = selectedProfessorKey;
    }

    public String getSelectedProfessorKey() {
        // Devuelve el identificador interno del profesor seleccionado.
        // Nótese que el nombre del método mantiene la terminología "Key" por
        // compatibilidad histórica; el valor devuelto es el mismo que `getSelectedProfessorId()`.
        return selectedProfessorId;
    }

    public void setSelectedProfessorId(String selectedProfessorId) {
        this.selectedProfessorId = selectedProfessorId;
    }

    public String getSelectedProfessorId() {
        return selectedProfessorId;
    }

    public void setSelectedProfessorName(String selectedProfessorName) {
        // Guarda el nombre para mostrar del profesor seleccionado. Este campo
        // se usa para mostrar títulos o encabezados en las vistas sin necesitar
        // acceder al `ProfessorDirectory` cada vez.
        this.selectedProfessorName = selectedProfessorName;
    }

    public String getSelectedProfessorName() {
        return selectedProfessorName;
    }

    // User role helpers
    public void setUserRole(String role) {
        this.userRole = role == null ? "GUEST" : role;
        // Recreate all views so role-dependent panels are built with the new state
        buildViews();
    }

    public void setCurrentUserEmail(String currentUserEmail) {
        this.currentUserEmail = currentUserEmail;
    }

    public String getCurrentUserEmail() {
        return currentUserEmail;
    }

    public void clearCurrentUserEmail() {
        this.currentUserEmail = null;
    }

    public String getOperationResultReturnView() {
        return operationResultReturnView;
    }

    public void setOperationResultReturnView(String operationResultReturnView) {
        // Establece la vista a la que debe volver la aplicación después de
        // mostrar la pantalla de "operación realizada". Validamos que la
        // cadena no sea nula ni vacía para evitar sobrescribir el valor por
        // defecto con un valor inválido.
        if (operationResultReturnView != null && !operationResultReturnView.isBlank()) {
            this.operationResultReturnView = operationResultReturnView;
        }
    }

    public boolean isModerator() {
        return "MODERADOR".equalsIgnoreCase(this.userRole);
    }

    public void changeLanguage(Locale locale, String viewToShow) {
        Locale.setDefault(locale);
        bundle = ResourceBundle.getBundle("bundle.Bundle", locale);
        buildViews();
        // Mostrar la vista solicitada sin añadir al historial (cambio de idioma no debe crear entrada)
        showView(viewToShow, false);
    }
}