import java.awt.*;
import javax.swing.*;

public class perfil_profesor{
    public static void main(String[] args) {
        UIManager.put("Label.font", new Font("Arial", Font.PLAIN, 15));

        JFrame frame = new JFrame("Nombre del Profesor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 450);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        JLabel nameLabel = new JLabel("Nombre del profesor");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 22));
        JLabel emailLabel = new JLabel("Correo Electrónico");
        JLabel departmentLabel = new JLabel("Departamento");
        JLabel despacho = new JLabel("Ubicación del despacho");
        JLabel tutorias = new JLabel("Horario de tutorías");
        JLabel telefono = new JLabel("Número de teléfono");

        JLabel texto= new JLabel("Asignaturas impartidas:");
        texto.setBorder(BorderFactory.createEmptyBorder(15,0,10,0));
        JPanel asignaturasPanel = new JPanel();
        asignaturasPanel.setLayout(new BoxLayout(asignaturasPanel, BoxLayout.Y_AXIS));
        JLabel asignatura1 = new JLabel("Asignatura 1, Curso, Grado");
        asignatura1.setBorder(BorderFactory.createEmptyBorder(0,10,5,0));
        JLabel asignatura2 = new JLabel("Asignatura 2, Curso, Grado");
        asignatura2.setBorder(BorderFactory.createEmptyBorder(0,10,5,0));
        JLabel asignatura3 = new JLabel("Asignatura 3, Curso, Grado");
        asignatura3.setBorder(BorderFactory.createEmptyBorder(0,10,5,0));

        JPanel valoraciones_dest = new JPanel();
        JLabel valoraciones = new JLabel("Valoraciones de los estudiantes:");
        valoraciones.setBorder(BorderFactory.createEmptyBorder(15,0,10,0));
        valoraciones_dest.setLayout(new BoxLayout(valoraciones_dest, BoxLayout.Y_AXIS));
        valoraciones_dest.add(valoraciones);
        JLabel valoracion1 = new JLabel("Valoración 1");
        valoracion1.setBorder(BorderFactory.createEmptyBorder(0,10,5,0));
        JLabel valoracion2 = new JLabel("Valoración 2");
        valoracion2.setBorder(BorderFactory.createEmptyBorder(0,10,5,0));
        JLabel valoracion3 = new JLabel("Valoración 3");
        valoracion3.setBorder(BorderFactory.createEmptyBorder(0,10,5,0));
        valoraciones_dest.add(valoracion1);
        valoraciones_dest.add(valoracion2);
        valoraciones_dest.add(valoracion3);
        
        panel.add(nameLabel);
        panel.add(emailLabel);
        panel.add(departmentLabel);
        panel.add(despacho);
        panel.add(tutorias);
        panel.add(telefono);
        panel.add(texto);
        asignaturasPanel.add(asignatura1);
        asignaturasPanel.add(asignatura2);
        asignaturasPanel.add(asignatura3);
        panel.add(asignaturasPanel);
        panel.add(valoraciones_dest);

        frame.add(panel);
        frame.setVisible(true);
    }
}