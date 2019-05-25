import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePane extends JPanel {

    private BufferedImage img;
    private String name;

    public ImagePane() {
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
        return new Dimension(288, 144);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        BufferedImage img = getImg();
        if (img != null) {
            g2d.drawImage(img, 10, 10,120,120, this);
            g2d.setFont(new Font("Roboto Condensed Bold", Font.BOLD, 20));
            name = name.trim();
            String[] nameArr = name.split("\\s+");

            int pixHeight = (int) g2d.getFont().getStringBounds(name,g2d.getFontRenderContext()).getHeight();
            g2d.drawString(name,150,10+pixHeight);
        }
        g2d.dispose();
    }

    public void setText(String name) {
        this.name = name;
    }
}
