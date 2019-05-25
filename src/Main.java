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

public class Main extends JFrame implements Printable {

    private int dpi = 203;
    private int width = (int) (203*3);
    private int height = (int) (203*1.5);


    public static void main(String[] args) {
        Color lightGray = new Color (188,192,190);
        Color darkGray = new Color (88,89,91);
        Color blue = new Color(0,146,200);
        Color green = new Color(0,167,109);
        Font font = new Font("Roboto Condensed Bold", Font.PLAIN,  21);
        JFrame frame = new JFrame("CebuCON Registration");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);

        JPanel panel = new JPanel();

        JPanel header = new JPanel();
        header.setOpaque(true);
        header.setBackground(new Color(188,190,192));
        header.setLayout(new FlowLayout(FlowLayout.LEFT));


        JButton qrRead = new JButton();
        JButton preview = new JButton();
        final String[] result = {""};

        panel.setLayout(new FlowLayout());
        qrRead.setText("QR Reader");
        preview.setText("Print Preview");
        panel.add(qrRead);
        panel.add(preview);
        frame.add(panel);
        frame.setSize(200,300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


        qrRead.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    final Thread thread = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try (QrCapture qr = new QrCapture()) {
                                result[0] = qr.getResult();
                                if(result[0] != null) {
                                    String qrText = generateQrText(result[0]);
                                    System.out.println(qrText);
                                    String filePath = "C:\\Users\\ljda0\\Desktop\\qrcode-reg-temp.png";
                                    String fileType = "png";
                                    File qrFile = new File(filePath);
                                    int size = (int) (203 * 1.4);
                                    QRCodeGenerator qrCodeGenerator = new QRCodeGenerator();
                                    try {
                                        qrCodeGenerator.createQRImage(qrFile, qrText, size, fileType);
                                    } catch (WriterException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                    thread.setDaemon(true);
                    thread.start();
            }
        });

        preview.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ImagePane pane = new ImagePane();
                        try{
                            pane.setImg(ImageIO.read(new File("C:\\Users\\ljda0\\Desktop\\qrcode-reg-temp.png")));
                            pane.setText("Jerome Martinez");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        JFrame frame2 = new JFrame("Test");
                        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        frame2.setLayout(new BorderLayout());
                        frame2.add(pane);
                        frame2.pack();
                        frame2.setLocationRelativeTo(null);
                        frame2.setVisible(true);

                    }
                });
                thread.setDaemon(true);
                thread.start();
            }
        });






    }
    public void printPreview(Graphics g){
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (UnsupportedLookAndFeelException e) {
                    e.printStackTrace();
                }
                ImagePane pane = new ImagePane();
                try{
                    pane.setImg(ImageIO.read(new File("C:\\Users\\ljda0\\Desktop\\qrcode-reg-temp.png")));
                } catch (IOException e) {
                    e.printStackTrace();
                }


                JFrame frame = new JFrame("Test");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.add(pane);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

            }
        });
    }
    @Override
    public int print(Graphics g, PageFormat pf, int page)
            throws PrinterException {


        // We have only one page, and 'page'
        // is zero-based
        if (page > 0) {
            return NO_SUCH_PAGE;
        }

        // User (0,0) is typically outside the
        // imageable area, so we must translate
        // by the X and Y values in the PageFormat
        // to avoid clipping.
        Graphics2D g2d = (Graphics2D)g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());

        // Now we perform our rendering
        Image img = null;
        try {
            img = ImageIO.read(new File("C:\\Users\\ljda0\\Desktop\\qrcode-reg-temp.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        g2d.drawImage(img,10,10,120,120,null);
        g.drawString("Hello world!", 100, 100);

        // tell the caller that this page is part
        // of the printed document
        return PAGE_EXISTS;
    }

    public static String generateQrText(String result){
        String qrString ="";
        qrString = result;
        String[] splitQrString = qrString.split(",");

        SelectFromDB query = new SelectFromDB();
        List<String> queryResult = query.selectNameProfession(splitQrString[1],splitQrString[0]);
        String name = queryResult.get(0);
        String mobile = queryResult.get(1);

        name = "CB-" + name;
        mobile = "+63" + mobile;

        String qrText = "BEGIN:VCARD\n";
        qrText += "FN:" + name;
        qrText += "\nTEL;TYPE=mobile;VALUE=uri:"+mobile+"\n";
        qrText += "END:VCARD";

        return qrText;
    }
}
