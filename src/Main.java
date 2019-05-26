import com.google.zxing.WriterException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Main extends JFrame {

    public static void main(String args[]) {
        JFrame frame = new JFrame("CebuCON Registration");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);

        frame.setContentPane(new MainPage().$$$getRootComponent$$$());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        Image favicon = null;
        try {
            favicon = ImageIO.read(new File("C:\\Temp\\favicon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        frame.setIconImage(favicon);

    }
}
