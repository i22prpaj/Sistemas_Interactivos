package gui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;

public class ChangeLanguageAction implements ActionListener {
  private Locale[] locales;
  private int currentLocaleIndex;
  private JFrame jf;
  private JLabel ej_label;
  private JLabel saludo_label;
  private JButton changeLanguageButton;

  public ChangeLanguageAction(Locale[] locales, int currentLocaleIndex, JFrame jf, JLabel ej_label, JLabel saludo_label, JButton changeLanguageButton) {
    this.locales = locales;
    this.currentLocaleIndex = currentLocaleIndex;
    this.jf = jf;
    this.ej_label = ej_label;
    this.saludo_label = saludo_label;
    this.changeLanguageButton = changeLanguageButton;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    // Cambiar al siguiente idioma de forma cíclica
    currentLocaleIndex = (currentLocaleIndex + 1) % locales.length;
    Locale nextLocale = locales[currentLocaleIndex];
    ResourceBundle bundle_text = ResourceBundle.getBundle("bundle.Bundle", nextLocale);

    // Actualizar textos de la interfaz
    jf.setTitle(bundle_text.getString("Titulo"));
    ej_label.setText(bundle_text.getString("Cabecera"));
    saludo_label.setText(bundle_text.getString("Saludo"));
    changeLanguageButton.setText(bundle_text.getString("Cambiar_idioma"));
    SwingUtilities.updateComponentTreeUI(jf);
  }
}