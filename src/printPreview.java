import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Flow;

public class printPreview extends JPanel {
    private JTextArea textArea1;
    private JTextArea textArea2;
    private JPanel picPrint;
    public JPanel previewPane;
    private JPanel namePrint;
    private String name;
    private String company;
    private BufferedImage img;

    public printPreview(String name, String company, BufferedImage img) {
        this.name = name;
        this.company = company;
        this.img = img;
        createUIComponents();
    }

    private void createUIComponents() {
        textArea1 = new JTextArea();
        textArea2 = new JTextArea();
        previewPane = new JPanel();
        previewPane.setSize(new Dimension(288, 144));
        textArea1.setOpaque(true);
        textArea2.setOpaque(true);
        textArea1.setLineWrap(true);
        textArea2.setLineWrap(true);
        textArea1.setWrapStyleWord(true);
        textArea2.setWrapStyleWord(true);
        textArea1.setEditable(false);
        textArea2.setEditable(false);
        textArea1.setSize(new Dimension(144,96));
        textArea2.setSize(new Dimension(144,38));

        textArea1.setFont(new Font("Roboto Condensed Bold", Font.PLAIN, 24));
        textArea2.setFont(new Font("Roboto Condensed Bold", Font.PLAIN, 14));
        textArea1.setForeground(Color.BLACK);
        textArea2.setForeground(Color.BLACK);

        picPrint = new ImagePane(img);
        textArea1.setText(name);
        textArea2.setText(company);


        previewPane.setSize(new Dimension(288, 144));
        previewPane.setMaximumSize(new Dimension(288,144));
        previewPane.setLayout(new GridLayout(1,2));
        previewPane.setBackground(new Color(-1));
        previewPane.add(picPrint,0);
        picPrint.setAlignmentX(0);
        picPrint.setAlignmentY(0);
        namePrint = new JPanel();
        namePrint.setLayout(new GridLayout(2,1));
        textArea1.setBackground(new Color(-1));
        namePrint.add(textArea1);
        textArea2.setBackground(new Color(-1));
        namePrint.add(textArea2);
        previewPane.add(namePrint,1);
    }
}
