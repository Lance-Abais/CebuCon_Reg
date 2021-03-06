import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Exchanger;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.*;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;


public class QrCapture extends JFrame implements Closeable {

    private static final long serialVersionUID = 1L;

    private Webcam webcam = null;
    private BufferedImage image = null;
    private Result result = null;
    private Exchanger<String> exchanger = new Exchanger<String>();

    public QrCapture() {

        super();

        setLayout(new FlowLayout());
        setTitle("QR Code Reader");
        try {
            setIconImage(ImageIO.read(new File("C:\\Temp\\favicon.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                close();
            }
        });

        List<Webcam> webcamList = Webcam.getWebcams();
        webcam = webcamList.get(0);
        webcam.setViewSize(WebcamResolution.QVGA.getSize());
        webcam.open();

        add(new WebcamPanel(webcam));

        pack();
        setVisible(true);

        final Thread daemon = new Thread(new Runnable() {

            @Override
            public void run() {
                while (isVisible()) {
                    read();
                }
            }
        });
        daemon.setDaemon(true);
        daemon.start();
    }

    private static BinaryBitmap toBinaryBitmap(BufferedImage image) {
        return new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
    }

    private void read() {

        if (!webcam.isOpen()) {
            return;
        }
        if ((image = webcam.getImage()) == null) {
            return;
        }

        try {
            result = new QRCodeReader().decode(toBinaryBitmap(image));
        } catch (NotFoundException e) {
            return;
        } catch (ChecksumException e) {
            return;
        } catch (FormatException e) {
            return;
        }

        if (result != null) {
            try {
                exchanger.exchange(result.getText());
            } catch (InterruptedException e) {
                return;
            } finally {
                dispose();
            }
        }
    }

    public String getResult() throws InterruptedException {
        return exchanger.exchange(null);
    }

    @Override
    public void close() {
        webcam.close();
    }
}