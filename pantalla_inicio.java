import java.awt.*;
import javax.swing.*;

public class pantalla_inicio {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Pantalla de Inicio");
        JPanel barra_buscador = new JPanel(new FlowLayout());
        JTextField campo_buscador = new JTextField(25);
        campo_buscador.setText("Buscar...");

        ImageIcon lupa = new ImageIcon("images/lupa.png");
        Image iconlupa = lupa.getImage().getScaledInstance(16, 16, Image.SCALE_DEFAULT);
        JButton boton_buscador = new JButton(new ImageIcon(iconlupa));

        barra_buscador.add(campo_buscador);
        barra_buscador.add(boton_buscador);

        JPanel panel_central = new JPanel();
        panel_central.setLayout(new BoxLayout(panel_central, BoxLayout.Y_AXIS));

        JPanel panel_et_destacadas = new JPanel(new FlowLayout());
        JLabel etiqueta_destacadas = new JLabel("Asignaturas Destacadas:");
        etiqueta_destacadas.setFont(new Font("Arial", Font.BOLD, 16));
        etiqueta_destacadas.setBackground(Color.WHITE);
        etiqueta_destacadas.setOpaque(true);
        panel_et_destacadas.add(etiqueta_destacadas);
        panel_central.add(panel_et_destacadas);


        // Asignatura Destacada 1
        JPanel panel_asig_dest_1 = new JPanel(new FlowLayout());
        JPanel asig_dest_1 = new JPanel(new GridLayout(1, 2));      // dividimos el panel en 2 columnas para la imagen y la información de la asignatura
        asig_dest_1.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 5)); // añadimos un margen interno para que el contenido no esté pegado al borde
        ImageIcon icon_asig1 = new ImageIcon("images/image.png");
        Image img_asig1 = icon_asig1.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT);
        JLabel imagen_asig1 = new JLabel(new ImageIcon(img_asig1));
        asig_dest_1.add(imagen_asig1);      // añadimos la imagen a la primera columna del panel

        // creamos un panel para estructurar la info de la asignatura
        JPanel info_asig_1 = new JPanel(new GridLayout(2, 1));
        JButton asig1 = new JButton("Nombre Asignatura");
        asig1.setFont(new Font("Arial", Font.BOLD, 14));
        info_asig_1.add(asig1);

        JPanel prof_asig_dest_1 = new JPanel(new GridLayout(3,1));
        JLabel prof1_1 = new JLabel("Profesor 1");
        JLabel prof2_1 = new JLabel("Profesor 2");
        JLabel prof3_1 = new JLabel("Profesor 3");

        prof_asig_dest_1.add(prof1_1);
        prof_asig_dest_1.add(prof2_1);
        prof_asig_dest_1.add(prof3_1);
        info_asig_1.add(prof_asig_dest_1);
        asig_dest_1.add(info_asig_1);
        panel_asig_dest_1.add(asig_dest_1);

        panel_central.add(panel_asig_dest_1);


        // Asignatura Destacada 2
        JPanel panel_asig_dest_2 = new JPanel(new FlowLayout());
        JPanel asig_dest_2 = new JPanel(new GridLayout(1, 2));      // dividimos el panel en 2 columnas para la imagen y la información de la asignatura
        asig_dest_2.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5)); // añadimos un margen interno para que el contenido no esté pegado al borde
        ImageIcon icon_asig2 = new ImageIcon("images/image.png");
        Image img_asig2 = icon_asig2.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT);
        JLabel imagen_asig2 = new JLabel(new ImageIcon(img_asig2));
        asig_dest_2.add(imagen_asig2);      // añadimos la imagen a la primera columna del panel

        // creamos un panel para estructurar la info de la asignatura
        JPanel info_asig_2 = new JPanel(new GridLayout(2, 1));
        JButton asig2 = new JButton("Nombre Asignatura");
        asig2.setFont(new Font("Arial", Font.BOLD, 14));
        info_asig_2.add(asig2);

        JPanel prof_asig_dest_2 = new JPanel(new GridLayout(3,1));
        JLabel prof1_2 = new JLabel("Profesor 1");
        JLabel prof2_2 = new JLabel("Profesor 2");
        JLabel prof3_2 = new JLabel("Profesor 3");

        prof_asig_dest_2.add(prof1_2);
        prof_asig_dest_2.add(prof2_2);
        prof_asig_dest_2.add(prof3_2);
        info_asig_2.add(prof_asig_dest_2);
        asig_dest_2.add(info_asig_2);
        panel_asig_dest_2.add(asig_dest_2);
        panel_central.add(panel_asig_dest_2);


        // Asignatura Destacada 3
        JPanel panel_asig_dest_3 = new JPanel(new FlowLayout());
        JPanel asig_dest_3 = new JPanel(new GridLayout(1, 2));      // dividimos el panel en 2 columnas para la imagen y la información de la asignatura
        asig_dest_3.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5)); // añadimos un margen interno para que el contenido no esté pegado al borde 
        ImageIcon icon_asig3 = new ImageIcon("images/image.png");
        Image img_asig3 = icon_asig3.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT);
        JLabel imagen_asig3 = new JLabel(new ImageIcon(img_asig3));
        asig_dest_3.add(imagen_asig3);      // añadimos la imagen a la primera columna del panel

        // creamos un panel para estructurar la info de la asignatura
        JPanel info_asig_3 = new JPanel(new GridLayout(2, 1));
        JButton asig3 = new JButton("Nombre Asignatura");
        asig3.setFont(new Font("Arial", Font.BOLD, 14));
        info_asig_3.add(asig3);

        JPanel prof_asig_dest_3 = new JPanel(new GridLayout(3,1));
        JLabel prof1_3 = new JLabel("Profesor 1");
        JLabel prof2_3 = new JLabel("Profesor 2");
        JLabel prof3_3 = new JLabel("Profesor 3");

        prof_asig_dest_3.add(prof1_3);
        prof_asig_dest_3.add(prof2_3);
        prof_asig_dest_3.add(prof3_3);
        info_asig_3.add(prof_asig_dest_3);
        asig_dest_3.add(info_asig_3);
        panel_asig_dest_3.add(asig_dest_3);
        panel_central.add(panel_asig_dest_3);



        frame.add(barra_buscador, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(panel_central);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setSize(600, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
