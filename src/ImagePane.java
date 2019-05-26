import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

public class ImagePane extends JPanel {

    private BufferedImage img;

    public ImagePane(BufferedImage i) {
        setOpaque(false);
        setImg(i);
    }

    public void setImg(BufferedImage value) {
        if (img != value) {
            img = value;
            repaint();
        }
    }

    public BufferedImage getImg() {
        return img;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(120, 120);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        BufferedImage img = getImg();
        if (img != null) {
            g2d.drawImage(img, 0, 0,120,120,this);
        }
        g2d.dispose();
    }



}
