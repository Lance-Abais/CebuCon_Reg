import com.google.zxing.WriterException;

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
        JFrame frame = new JFrame("CebuCON Registration");
        JPanel panel = new JPanel();
        JButton qrRead = new JButton();
        final String[] result = {""};

        panel.setLayout(new FlowLayout());
        qrRead.setText("QR Reader");
        panel.add(qrRead);
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
                                String qrText = generateQrText(result[0]);
                                String filePath = "%temp%\\qrcode-reg-temp.png";
                                String fileType = "png";
                                File qrFile = new File(filePath);
                                int size = (int) (203*1.4);
                                QRCodeGenerator qrCodeGenerator = new QRCodeGenerator();
                                try {
                                    qrCodeGenerator.createQRImage(qrFile,qrText,size,fileType);
                                } catch (WriterException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
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
        List<String> queryResult = query.selectName(splitQrString[1]);
        String name = queryResult.get(0);
        String mobile = queryResult.get(1);

        name = "CB-" + name;
        mobile = "+63" + mobile;

        String qrText = "BEGIN:VCARD\n";
        qrText += "FN:" + name;
        qrText += "TEL;TYPE=mobile;VALUE=uri:mobile";
        qrText += "END:VCARD";

        return qrText;
    }
}
