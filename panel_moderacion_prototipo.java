import java.awt.*;
import javax.swing.*;

public class panel_moderacion {
    public static void main(String[] args) {
        UIManager.put("Label.font", new Font("Arial", Font.PLAIN, 15));

        JFrame frame = new JFrame("Panel de Moderación");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel cabecera = new JPanel();
        cabecera.setLayout(new BoxLayout(cabecera, BoxLayout.Y_AXIS));
        JLabel titulo = new JLabel("Panel de Moderación");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        JLabel subtitulo = new JLabel("Comentarios y reseñas pendientes de revisión");
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        cabecera.add(titulo);
        cabecera.add(Box.createVerticalStrut(5));
        cabecera.add(subtitulo);

        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel filtroEtiqueta = new JLabel("Filtrar por:");
        String[] opcionesFiltro = {"Pendientes", "Con palabras marcadas", "Reportados", "Todos"};
        JComboBox<String> filtro = new JComboBox<>(opcionesFiltro);
        JButton actualizar = new JButton("Actualizar");
        filtros.add(filtroEtiqueta);
        filtros.add(filtro);
        filtros.add(actualizar);

        JPanel listaPanel = new JPanel();
        listaPanel.setLayout(new BoxLayout(listaPanel, BoxLayout.Y_AXIS));

        listaPanel.add(crearTarjetaComentario(
                "Reseña 1",
                "Asignatura: Programación Web",
                "Motivo: lenguaje ofensivo detectado",
                "Comentario: 'El profesor no explica nada y...'",
                new String[] {"Eliminar", "Aprobar", "Ver contexto"}
        ));

        listaPanel.add(Box.createVerticalStrut(10));

        listaPanel.add(crearTarjetaComentario(
                "Reseña 2",
                "Asignatura: Bases de Datos",
                "Motivo: varios reportes de usuarios",
                "Comentario: 'La dificultad está mal valorada y...'",
                new String[] {"Eliminar", "Aprobar", "Ver contexto"}
        ));

        listaPanel.add(Box.createVerticalStrut(10));

        listaPanel.add(crearTarjetaComentario(
                "Reseña 3",
                "Asignatura: Interacción Persona-Ordenador",
                "Motivo: posible información personal",
                "Comentario: 'Se llama Juan y vive en...'",
                new String[] {"Eliminar", "Aprobar", "Marcar"}
        ));

        JScrollPane scrollPane = new JScrollPane(listaPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Elementos en revisión"));

        JPanel accionesGlobales = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton revisarSiguiente = new JButton("Siguiente pendiente");
        JButton verEstadisticas = new JButton("Ver estadísticas");
        accionesGlobales.add(verEstadisticas);
        accionesGlobales.add(revisarSiguiente);

        JPanel parteSuperior = new JPanel();
        parteSuperior.setLayout(new BoxLayout(parteSuperior, BoxLayout.Y_AXIS));
        parteSuperior.add(cabecera);
        parteSuperior.add(Box.createVerticalStrut(8));
        parteSuperior.add(filtros);

        panelPrincipal.add(parteSuperior, BorderLayout.NORTH);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);
        panelPrincipal.add(accionesGlobales, BorderLayout.SOUTH);

        frame.add(panelPrincipal);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static JPanel crearTarjetaComentario(String titulo, String asignatura, String motivo, String comentario, String[] acciones) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BorderLayout(10, 10));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        tarjeta.setBackground(Color.WHITE);

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setOpaque(false);

        JLabel tituloLabel = new JLabel(titulo);
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 18));
        JLabel asignaturaLabel = new JLabel(asignatura);
        JLabel motivoLabel = new JLabel(motivo);
        JLabel comentarioLabel = new JLabel("<html><body style='width:420px'>" + comentario + "</body></html>");

        info.add(tituloLabel);
        info.add(Box.createVerticalStrut(4));
        info.add(asignaturaLabel);
        info.add(motivoLabel);
        info.add(Box.createVerticalStrut(6));
        info.add(comentarioLabel);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botones.setOpaque(false);
        for (String accion : acciones) {
            botones.add(new JButton(accion));
        }

        tarjeta.add(info, BorderLayout.CENTER);
        tarjeta.add(botones, BorderLayout.SOUTH);
        return tarjeta;
    }
}