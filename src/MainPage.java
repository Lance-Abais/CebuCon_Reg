import com.google.zxing.WriterException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class MainPage {
    private JPanel contentPane;
    private JRadioButton architectRadioButton;
    private JRadioButton civilEngineerRadioButton;
    private JRadioButton contractorRadioButton;
    private JRadioButton developerRadioButton;
    private JRadioButton interiorDesignerRadioButton;
    private JRadioButton electricalEngineerRadioButton;
    private JRadioButton mechanicalEngineerRadioButton;
    private JRadioButton tradeBuyerRadioButton;
    private JRadioButton othersRadioButton;
    private JPanel header;
    private JPanel profession;
    private JPanel contactDetails;
    private JPanel body;
    private JPanel purpose;
    private JPanel how;
    private JPanel buttonsPane;
    private JPanel rightBody;
    private JTextField fName;
    private JTextField designation;
    private JTextField company;
    private JTextField email;
    private JTextField mobile;
    private JTextArea address;
    private JCheckBox seeNewProductsCheckBox;
    private JCheckBox evaluateProductForPurchaseCheckBox;
    private JCheckBox consultAConstructionProblemCheckBox;
    private JCheckBox seekDealershipDistributorshipRightsCheckBox;
    private JCheckBox seekAlternativeSupplierCheckBox;
    private JCheckBox attendTechnicalSeminarCheckBox;
    private JCheckBox newspaperCheckBox;
    private JCheckBox CDNDigitalCheckBox;
    private JCheckBox facebookAdsCheckBox;
    private JCheckBox bannersStreamersCheckBox;
    private JCheckBox cebuCONMailerCheckBox;
    private JCheckBox wordOfMouthCheckBox;
    private JButton QRCODEButton;
    private JButton PRINTButton;
    private JLabel logoHolder;
    private ButtonGroup purposeGroup;
    private ButtonGroup professionGroup;
    private String nameS;
    private String companyS;
    private String professionS;
    private String designationS;
    private String emailS;
    private String mobileS;
    private String addressS;
    private ArrayList<String> purposeS = new ArrayList<>();
    private ArrayList<String> howS = new ArrayList<>();
    private ArrayList<JCheckBox> purposeArr = new ArrayList<>();
    private ArrayList<JCheckBox> howArr = new ArrayList<>();

    String filePath = "C:\\Temp\\qrcode-reg-temp.png";
    String fileType = "png";

    public MainPage() {
        final String[] result = {null};

        purposeArr.add(seeNewProductsCheckBox);
        purposeArr.add(evaluateProductForPurchaseCheckBox);
        purposeArr.add(consultAConstructionProblemCheckBox);
        purposeArr.add(seekAlternativeSupplierCheckBox);
        purposeArr.add(seekDealershipDistributorshipRightsCheckBox);
        purposeArr.add(attendTechnicalSeminarCheckBox);

        howArr.add(newspaperCheckBox);
        howArr.add(CDNDigitalCheckBox);
        howArr.add(facebookAdsCheckBox);
        howArr.add(bannersStreamersCheckBox);
        howArr.add(cebuCONMailerCheckBox);
        howArr.add(wordOfMouthCheckBox);

        QRCODEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try (QrCapture qr = new QrCapture()) {
                            result[0] = qr.getResult();
                            if (result[0] != null) {
                                String qrText = generateQrTextDB(result[0]);
                                System.out.println(qrText);
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

                        preview();
                    }
                });
                thread.setDaemon(true);
                thread.start();
            }

            private String generateQrTextDB(String s) {

                String qrString = "";
                qrString = s;
                String[] splitQrString = qrString.split(",");

                SelectFromDB query = new SelectFromDB();
                List<String> queryResult = query.selectNameProfession(splitQrString[1], splitQrString[0]);
                String namer = queryResult.get(0);
                companyS = queryResult.get(2);
                String mobile = queryResult.get(1);
                nameS = queryResult.get(0);

                namer = "CB-" + namer;
                mobile = "+63" + mobile;

                String qrText = "BEGIN:VCARD\n";
                qrText += "FN:" + namer;
                qrText += "\nTEL;TYPE=mobile;VALUE=uri:" + mobile + "\n";
                qrText += "END:VCARD";

                return qrText;

            }

        });
        PRINTButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nameS = fName.getText();
                companyS = company.getText();
                designationS = designation.getText();
                emailS = email.getText();
                mobileS = mobile.getText();
                addressS = address.getText();
                professionS = getSelectedButton();
                getSelectedPurpose();
                getSelectedHow();
                new SelectFromDB().addPerson(professionS, nameS, designationS, companyS, addressS, emailS, mobileS, purposeS, howS);
                String qrText = "BEGIN:VCARD\n";
                qrText += "FN:CB-" + nameS;
                qrText += "\nTEL;TYPE=mobile;VALUE=uri:" + mobileS + "\n";
                qrText += "END:VCARD";

                System.out.println(qrText);
                File qrFile = new File(filePath);
                int size = (int) (203 * 1.4);
                QRCodeGenerator qrCodeGenerator = new QRCodeGenerator();
                try {
                    qrCodeGenerator.createQRImage(qrFile, qrText, size, fileType);
                } catch (WriterException f) {
                    f.printStackTrace();
                } catch (IOException f) {
                    f.printStackTrace();
                }
                preview();

            }
        });
    }

    private void preview() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {


                JFrame frame2 = new JFrame("Preview");
                printPreview prev = null;
                try {
                    prev = new printPreview(nameS, companyS, ImageIO.read(new File(filePath)));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    frame2.setIconImage(ImageIO.read(new File("C:\\Temp\\favicon.png")));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                frame2.setSize(new Dimension(288, 160));
                frame2.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
                frame2.setContentPane(prev.previewPane);
                frame2.setLocationRelativeTo(null);
                frame2.setVisible(true);
                frame2.setBackground(Color.WHITE);

                PrinterJob pjob = PrinterJob.getPrinterJob();
                PageFormat preformat = pjob.defaultPage();
                Paper paper = preformat.getPaper();
                paper.setImageableArea(0, 0, 288, 144);
                System.out.println(paper.getImageableHeight());
                System.out.println(paper.getImageableWidth());
                preformat.setPaper(paper);
                preformat.setOrientation(PageFormat.PORTRAIT);
                PageFormat postformat = pjob.pageDialog(preformat);
                if (preformat != postformat) {
                    pjob.setPrintable(new Printer(frame2), postformat);
                    if (pjob.printDialog()) {
                        try {
                            pjob.print();
                            frame2.dispose();
                            resetForm();
                        } catch (PrinterException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private String getSelectedButton() {
        for (Enumeration<AbstractButton> buttons = professionGroup.getElements(); buttons.hasMoreElements(); ) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                return button.getText();
            }
        }
        return null;
    }

    private void getSelectedPurpose() {
        if (seeNewProductsCheckBox.isSelected()) purposeS.add(seeNewProductsCheckBox.getText());
        if (evaluateProductForPurchaseCheckBox.isSelected()) purposeS.add(evaluateProductForPurchaseCheckBox.getText());
        if (consultAConstructionProblemCheckBox.isSelected())
            purposeS.add(consultAConstructionProblemCheckBox.getText());
        if (seekDealershipDistributorshipRightsCheckBox.isSelected())
            purposeS.add(seekDealershipDistributorshipRightsCheckBox.getText());
        if (seekAlternativeSupplierCheckBox.isSelected()) purposeS.add(seekAlternativeSupplierCheckBox.getText());
        if (attendTechnicalSeminarCheckBox.isSelected()) purposeS.add(attendTechnicalSeminarCheckBox.getText());

    }

    private void getSelectedHow() {
        if (newspaperCheckBox.isSelected()) howS.add(newspaperCheckBox.getText());
        if (CDNDigitalCheckBox.isSelected()) howS.add(CDNDigitalCheckBox.getText());
        if (facebookAdsCheckBox.isSelected()) howS.add(facebookAdsCheckBox.getText());
        if (bannersStreamersCheckBox.isSelected()) howS.add(bannersStreamersCheckBox.getText());
        if (cebuCONMailerCheckBox.isSelected()) howS.add(cebuCONMailerCheckBox.getText());
        if (wordOfMouthCheckBox.isSelected()) howS.add(wordOfMouthCheckBox.getText());
    }

    public void resetForm() {
        fName.setText("");
        designation.setText("");
        company.setText("");
        address.setText("");
        email.setText("");
        mobile.setText("");

        ButtonGroup resetPurpose = new ButtonGroup();
        ButtonGroup resetHow = new ButtonGroup();

        for (int i = 0; i < purposeArr.size(); i++) resetPurpose.add(purposeArr.get(i));
        for (int i = 0; i < howArr.size(); i++) resetHow.add(howArr.get(i));
        professionGroup.clearSelection();
        resetPurpose.clearSelection();
        resetHow.clearSelection();

        for (int i = 0; i < purposeArr.size(); i++) resetPurpose.remove(purposeArr.get(i));
        for (int i = 0; i < howArr.size(); i++) resetHow.remove(howArr.get(i));

    }


    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridBagLayout());
        contentPane.setBackground(new Color(-10987173));
        contentPane.putClientProperty("html.disable", Boolean.TRUE);
        header = new JPanel();
        header.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        header.setBackground(new Color(-16777216));
        header.setName("header");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(header, gbc);
        logoHolder = new JLabel();
        logoHolder.setEnabled(true);
        logoHolder.setIcon(new ImageIcon(getClass().getResource("/favicon (1).png")));
        logoHolder.setText("");
        header.add(logoHolder);
        final JLabel label1 = new JLabel();
        label1.setEnabled(true);
        Font label1Font = this.$$$getFont$$$("Noto Sans", Font.BOLD, 18, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setForeground(new Color(-1));
        label1.setText("CebuCON: Construction Show Cebu");
        header.add(label1);
        profession = new JPanel();
        profession.setLayout(new GridBagLayout());
        profession.setBackground(new Color(-4407616));
        profession.setDoubleBuffered(false);
        profession.setFocusCycleRoot(true);
        profession.setOpaque(true);
        profession.putClientProperty("html.disable", Boolean.FALSE);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(profession, gbc);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 5));
        panel1.setOpaque(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        profession.add(panel1, gbc);
        final JLabel label2 = new JLabel();
        label2.setEnabled(true);
        Font label2Font = this.$$$getFont$$$("Noto Sans", Font.BOLD, 24, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setForeground(new Color(-16777216));
        label2.setText("Profession");
        panel1.add(label2);
        architectRadioButton = new JRadioButton();
        Font architectRadioButtonFont = this.$$$getFont$$$("Noto Sans", Font.BOLD, 24, architectRadioButton.getFont());
        if (architectRadioButtonFont != null) architectRadioButton.setFont(architectRadioButtonFont);
        architectRadioButton.setForeground(new Color(-16777216));
        architectRadioButton.setOpaque(false);
        architectRadioButton.setText("Architect");
        panel1.add(architectRadioButton);
        civilEngineerRadioButton = new JRadioButton();
        Font civilEngineerRadioButtonFont = this.$$$getFont$$$("Noto Sans", Font.BOLD, 24, civilEngineerRadioButton.getFont());
        if (civilEngineerRadioButtonFont != null) civilEngineerRadioButton.setFont(civilEngineerRadioButtonFont);
        civilEngineerRadioButton.setForeground(new Color(-16777216));
        civilEngineerRadioButton.setOpaque(false);
        civilEngineerRadioButton.setText("Civil Engineer");
        panel1.add(civilEngineerRadioButton);
        contractorRadioButton = new JRadioButton();
        Font contractorRadioButtonFont = this.$$$getFont$$$("Noto Sans", Font.BOLD, 24, contractorRadioButton.getFont());
        if (contractorRadioButtonFont != null) contractorRadioButton.setFont(contractorRadioButtonFont);
        contractorRadioButton.setForeground(new Color(-16777216));
        contractorRadioButton.setOpaque(false);
        contractorRadioButton.setText("Contractor");
        panel1.add(contractorRadioButton);
        developerRadioButton = new JRadioButton();
        Font developerRadioButtonFont = this.$$$getFont$$$("Noto Sans", Font.BOLD, 24, developerRadioButton.getFont());
        if (developerRadioButtonFont != null) developerRadioButton.setFont(developerRadioButtonFont);
        developerRadioButton.setForeground(new Color(-16777216));
        developerRadioButton.setOpaque(false);
        developerRadioButton.setText("Developer");
        panel1.add(developerRadioButton);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel2.setDoubleBuffered(true);
        panel2.setEnabled(true);
        panel2.setOpaque(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        profession.add(panel2, gbc);
        interiorDesignerRadioButton = new JRadioButton();
        Font interiorDesignerRadioButtonFont = this.$$$getFont$$$("Noto Sans", Font.BOLD, 24, interiorDesignerRadioButton.getFont());
        if (interiorDesignerRadioButtonFont != null)
            interiorDesignerRadioButton.setFont(interiorDesignerRadioButtonFont);
        interiorDesignerRadioButton.setForeground(new Color(-16777216));
        interiorDesignerRadioButton.setHorizontalAlignment(0);
        interiorDesignerRadioButton.setOpaque(false);
        interiorDesignerRadioButton.setText("Interior Designer");
        panel2.add(interiorDesignerRadioButton);
        electricalEngineerRadioButton = new JRadioButton();
        Font electricalEngineerRadioButtonFont = this.$$$getFont$$$("Noto Sans", Font.BOLD, 24, electricalEngineerRadioButton.getFont());
        if (electricalEngineerRadioButtonFont != null)
            electricalEngineerRadioButton.setFont(electricalEngineerRadioButtonFont);
        electricalEngineerRadioButton.setForeground(new Color(-16777216));
        electricalEngineerRadioButton.setHorizontalAlignment(0);
        electricalEngineerRadioButton.setOpaque(false);
        electricalEngineerRadioButton.setText("Electrical Engineer");
        panel2.add(electricalEngineerRadioButton);
        mechanicalEngineerRadioButton = new JRadioButton();
        Font mechanicalEngineerRadioButtonFont = this.$$$getFont$$$("Noto Sans", Font.BOLD, 24, mechanicalEngineerRadioButton.getFont());
        if (mechanicalEngineerRadioButtonFont != null)
            mechanicalEngineerRadioButton.setFont(mechanicalEngineerRadioButtonFont);
        mechanicalEngineerRadioButton.setForeground(new Color(-16777216));
        mechanicalEngineerRadioButton.setHorizontalAlignment(0);
        mechanicalEngineerRadioButton.setOpaque(false);
        mechanicalEngineerRadioButton.setText("Mechanical Engineer");
        panel2.add(mechanicalEngineerRadioButton);
        tradeBuyerRadioButton = new JRadioButton();
        Font tradeBuyerRadioButtonFont = this.$$$getFont$$$("Noto Sans", Font.BOLD, 24, tradeBuyerRadioButton.getFont());
        if (tradeBuyerRadioButtonFont != null) tradeBuyerRadioButton.setFont(tradeBuyerRadioButtonFont);
        tradeBuyerRadioButton.setForeground(new Color(-16777216));
        tradeBuyerRadioButton.setHorizontalAlignment(0);
        tradeBuyerRadioButton.setOpaque(false);
        tradeBuyerRadioButton.setText("Trade Buyer");
        panel2.add(tradeBuyerRadioButton);
        othersRadioButton = new JRadioButton();
        Font othersRadioButtonFont = this.$$$getFont$$$("Noto Sans", Font.BOLD, 24, othersRadioButton.getFont());
        if (othersRadioButtonFont != null) othersRadioButton.setFont(othersRadioButtonFont);
        othersRadioButton.setForeground(new Color(-16777216));
        othersRadioButton.setHorizontalAlignment(0);
        othersRadioButton.setOpaque(false);
        othersRadioButton.setText("Others");
        panel2.add(othersRadioButton);
        body = new JPanel();
        body.setLayout(new GridBagLayout());
        body.setOpaque(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        contentPane.add(body, gbc);
        rightBody = new JPanel();
        rightBody.setLayout(new GridBagLayout());
        rightBody.setOpaque(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 10, 10, 10);
        body.add(rightBody, gbc);
        purpose = new JPanel();
        purpose.setLayout(new GridBagLayout());
        purpose.setOpaque(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        rightBody.add(purpose, gbc);
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$("Noto Sans", Font.BOLD, 24, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setForeground(new Color(-4407616));
        label3.setText("PURPOSE OF VISIT");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        purpose.add(label3, gbc);
        evaluateProductForPurchaseCheckBox = new JCheckBox();
        Font evaluateProductForPurchaseCheckBoxFont = this.$$$getFont$$$("Noto Sans", Font.BOLD, 24, evaluateProductForPurchaseCheckBox.getFont());
        if (evaluateProductForPurchaseCheckBoxFont != null)
            evaluateProductForPurchaseCheckBox.setFont(evaluateProductForPurchaseCheckBoxFont);
        evaluateProductForPurchaseCheckBox.setForeground(new Color(-4407616));
        evaluateProductForPurchaseCheckBox.setOpaque(false);
        evaluateProductForPurchaseCheckBox.setText("Evaluate product for purchase");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        purpose.add(evaluateProductForPurchaseCheckBox, gbc);
        consultAConstructionProblemCheckBox = new JCheckBox();
        Font consultAConstructionProblemCheckBoxFont = this.$$$getFont$$$("Noto Sans", Font.BOLD, 24, consultAConstructionProblemCheckBox.getFont());
        if (consultAConstructionProblemCheckBoxFont != null)
            consultAConstructionProblemCheckBox.setFont(consultAConstructionProblemCheckBoxFont);
        consultAConstructionProblemCheckBox.setForeground(new Color(-4407616));
        consultAConstructionProblemCheckBox.setOpaque(false);
        consultAConstructionProblemCheckBox.setText("Consult a Construction Problem");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        purpose.add(consultAConstructionProblemCheckBox, gbc);
        seekDealershipDistributorshipRightsCheckBox = new JCheckBox();
        Font seekDealershipDistributorshipRightsCheckBoxFont = this.$$$getFont$$$("Noto Sans", Font.BOLD, 24, seekDealershipDistributorshipRightsCheckBox.getFont());
        if (seekDealershipDistributorshipRightsCheckBoxFont != null)
            seekDealershipDistributorshipRightsCheckBox.setFont(seekDealershipDistributorshipRightsCheckBoxFont);
        seekDealershipDistributorshipRightsCheckBox.setForeground(new Color(-4407616));
        seekDealershipDistributorshipRightsCheckBox.setOpaque(false);
        seekDealershipDistributorshipRightsCheckBox.setText("Seek Dealership or Distributorship rights");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        purpose.add(seekDealershipDistributorshipRightsCheckBox, gbc);
        seekAlternativeSupplierCheckBox = new JCheckBox();
        Font seekAlternativeSupplierCheckBoxFont = this.$$$getFont$$$("Noto Sans", Font.BOLD, 24, seekAlternativeSupplierCheckBox.getFont());
        if (seekAlternativeSupplierCheckBoxFont != null)
            seekAlternativeSupplierCheckBox.setFont(seekAlternativeSupplierCheckBoxFont);
        seekAlternativeSupplierCheckBox.setForeground(new Color(-4407616));
        seekAlternativeSupplierCheckBox.setOpaque(false);
        seekAlternativeSupplierCheckBox.setText("Seek alternative supplier");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        purpose.add(seekAlternativeSupplierCheckBox, gbc);
        attendTechnicalSeminarCheckBox = new JCheckBox();
        Font attendTechnicalSeminarCheckBoxFont = this.$$$getFont$$$("Noto Sans", Font.BOLD, 24, attendTechnicalSeminarCheckBox.getFont());
        if (attendTechnicalSeminarCheckBoxFont != null)
            attendTechnicalSeminarCheckBox.setFont(attendTechnicalSeminarCheckBoxFont);
        attendTechnicalSeminarCheckBox.setForeground(new Color(-4407616));
        attendTechnicalSeminarCheckBox.setOpaque(false);
        attendTechnicalSeminarCheckBox.setText("Attend Technical Seminar");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        purpose.add(attendTechnicalSeminarCheckBox, gbc);
        seeNewProductsCheckBox = new JCheckBox();
        Font seeNewProductsCheckBoxFont = this.$$$getFont$$$("Noto Sans", Font.BOLD, 24, seeNewProductsCheckBox.getFont());
        if (seeNewProductsCheckBoxFont != null) seeNewProductsCheckBox.setFont(seeNewProductsCheckBoxFont);
        seeNewProductsCheckBox.setForeground(new Color(-4407616));
        seeNewProductsCheckBox.setOpaque(false);
        seeNewProductsCheckBox.setSelected(false);
        seeNewProductsCheckBox.setText("See new products");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        purpose.add(seeNewProductsCheckBox, gbc);
        buttonsPane = new JPanel();
        buttonsPane.setLayout(new GridBagLayout());
        buttonsPane.setOpaque(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        rightBody.add(buttonsPane, gbc);
        PRINTButton = new JButton();
        PRINTButton.setBackground(new Color(-16734355));
        PRINTButton.setBorderPainted(true);
        PRINTButton.setContentAreaFilled(true);
        PRINTButton.setFocusable(true);
        Font PRINTButtonFont = this.$$$getFont$$$("Noto Sans", Font.BOLD, 20, PRINTButton.getFont());
        if (PRINTButtonFont != null) PRINTButton.setFont(PRINTButtonFont);
        PRINTButton.setForeground(new Color(-16777216));
        PRINTButton.setMultiClickThreshhold(1L);
        PRINTButton.setName("printButton");
        PRINTButton.setOpaque(true);
        PRINTButton.setText("PRINT");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        buttonsPane.add(PRINTButton, gbc);
        QRCODEButton = new JButton();
        QRCODEButton.setBackground(new Color(-16739640));
        QRCODEButton.setFocusable(true);
        Font QRCODEButtonFont = this.$$$getFont$$$("Noto Sans", Font.BOLD, 20, QRCODEButton.getFont());
        if (QRCODEButtonFont != null) QRCODEButton.setFont(QRCODEButtonFont);
        QRCODEButton.setForeground(new Color(-16777216));
        QRCODEButton.setLabel("QR CODE");
        QRCODEButton.setMultiClickThreshhold(1L);
        QRCODEButton.setName("qrRead");
        QRCODEButton.setOpaque(true);
        QRCODEButton.setText("QR CODE");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        buttonsPane.add(QRCODEButton, gbc);
        how = new JPanel();
        how.setLayout(new GridBagLayout());
        Font howFont = this.$$$getFont$$$("Noto Sans", Font.BOLD, 24, how.getFont());
        if (howFont != null) how.setFont(howFont);
        how.setOpaque(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        rightBody.add(how, gbc);
        final JLabel label4 = new JLabel();
        Font label4Font = this.$$$getFont$$$("Noto Sans", Font.BOLD, 24, label4.getFont());
        if (label4Font != null) label4.setFont(label4Font);
        label4.setForeground(new Color(-4407616));
        label4.setText("HOW DID YOU KNOW ABOUT CEBUCON?");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        how.add(label4, gbc);
        newspaperCheckBox = new JCheckBox();
        Font newspaperCheckBoxFont = this.$$$getFont$$$("Noto Sans", Font.BOLD, 24, newspaperCheckBox.getFont());
        if (newspaperCheckBoxFont != null) newspaperCheckBox.setFont(newspaperCheckBoxFont);
        newspaperCheckBox.setForeground(new Color(-4407616));
        newspaperCheckBox.setOpaque(false);
        newspaperCheckBox.setText("Newspaper");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        how.add(newspaperCheckBox, gbc);
        CDNDigitalCheckBox = new JCheckBox();
        Font CDNDigitalCheckBoxFont = this.$$$getFont$$$("Noto Sans", Font.BOLD, 24, CDNDigitalCheckBox.getFont());
        if (CDNDigitalCheckBoxFont != null) CDNDigitalCheckBox.setFont(CDNDigitalCheckBoxFont);
        CDNDigitalCheckBox.setForeground(new Color(-4407616));
        CDNDigitalCheckBox.setOpaque(false);
        CDNDigitalCheckBox.setText("CDN Digital");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        how.add(CDNDigitalCheckBox, gbc);
        facebookAdsCheckBox = new JCheckBox();
        Font facebookAdsCheckBoxFont = this.$$$getFont$$$("Noto Sans", Font.BOLD, 24, facebookAdsCheckBox.getFont());
        if (facebookAdsCheckBoxFont != null) facebookAdsCheckBox.setFont(facebookAdsCheckBoxFont);
        facebookAdsCheckBox.setForeground(new Color(-4407616));
        facebookAdsCheckBox.setOpaque(false);
        facebookAdsCheckBox.setText("Facebook Ads");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        how.add(facebookAdsCheckBox, gbc);
        bannersStreamersCheckBox = new JCheckBox();
        Font bannersStreamersCheckBoxFont = this.$$$getFont$$$("Noto Sans", Font.BOLD, 24, bannersStreamersCheckBox.getFont());
        if (bannersStreamersCheckBoxFont != null) bannersStreamersCheckBox.setFont(bannersStreamersCheckBoxFont);
        bannersStreamersCheckBox.setForeground(new Color(-4407616));
        bannersStreamersCheckBox.setOpaque(false);
        bannersStreamersCheckBox.setText("Banners or Streamers  ");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        how.add(bannersStreamersCheckBox, gbc);
        cebuCONMailerCheckBox = new JCheckBox();
        Font cebuCONMailerCheckBoxFont = this.$$$getFont$$$("Noto Sans", Font.BOLD, 24, cebuCONMailerCheckBox.getFont());
        if (cebuCONMailerCheckBoxFont != null) cebuCONMailerCheckBox.setFont(cebuCONMailerCheckBoxFont);
        cebuCONMailerCheckBox.setForeground(new Color(-4407616));
        cebuCONMailerCheckBox.setOpaque(false);
        cebuCONMailerCheckBox.setText("CebuCON Mailer");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        how.add(cebuCONMailerCheckBox, gbc);
        wordOfMouthCheckBox = new JCheckBox();
        Font wordOfMouthCheckBoxFont = this.$$$getFont$$$("Noto Sans", Font.BOLD, 24, wordOfMouthCheckBox.getFont());
        if (wordOfMouthCheckBoxFont != null) wordOfMouthCheckBox.setFont(wordOfMouthCheckBoxFont);
        wordOfMouthCheckBox.setForeground(new Color(-4407616));
        wordOfMouthCheckBox.setOpaque(false);
        wordOfMouthCheckBox.setText("Word of Mouth");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        how.add(wordOfMouthCheckBox, gbc);
        contactDetails = new JPanel();
        contactDetails.setLayout(new GridBagLayout());
        contactDetails.setOpaque(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.5;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 10, 10, 0);
        body.add(contactDetails, gbc);
        final JLabel label5 = new JLabel();
        Font label5Font = this.$$$getFont$$$("Noto Sans", Font.BOLD, 22, label5.getFont());
        if (label5Font != null) label5.setFont(label5Font);
        label5.setForeground(new Color(-4407616));
        label5.setText("FULL NAME");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        contactDetails.add(label5, gbc);
        fName = new JTextField();
        fName.setBackground(new Color(-4407616));
        fName.setFocusable(true);
        Font fNameFont = this.$$$getFont$$$("Roboto Condensed", Font.BOLD, 20, fName.getFont());
        if (fNameFont != null) fName.setFont(fNameFont);
        fName.setForeground(new Color(-16777216));
        fName.setOpaque(true);
        fName.setToolTipText("e.g. Juan Dela Cruz");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contactDetails.add(fName, gbc);
        final JLabel label6 = new JLabel();
        Font label6Font = this.$$$getFont$$$("Noto Sans", Font.BOLD, 22, label6.getFont());
        if (label6Font != null) label6.setFont(label6Font);
        label6.setForeground(new Color(-4407616));
        label6.setText("DESIGNATION");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        contactDetails.add(label6, gbc);
        designation = new JTextField();
        designation.setBackground(new Color(-4407616));
        designation.setFocusable(true);
        Font designationFont = this.$$$getFont$$$("Roboto Condensed", Font.BOLD, 20, designation.getFont());
        if (designationFont != null) designation.setFont(designationFont);
        designation.setForeground(new Color(-16777216));
        designation.setOpaque(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contactDetails.add(designation, gbc);
        final JLabel label7 = new JLabel();
        Font label7Font = this.$$$getFont$$$("Noto Sans", Font.BOLD, 22, label7.getFont());
        if (label7Font != null) label7.setFont(label7Font);
        label7.setForeground(new Color(-4407616));
        label7.setText("COMPANY / INSTITUTION");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        contactDetails.add(label7, gbc);
        company = new JTextField();
        company.setBackground(new Color(-4407616));
        company.setFocusable(true);
        Font companyFont = this.$$$getFont$$$("Roboto Condensed", Font.BOLD, 20, company.getFont());
        if (companyFont != null) company.setFont(companyFont);
        company.setForeground(new Color(-16777216));
        company.setOpaque(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contactDetails.add(company, gbc);
        final JLabel label8 = new JLabel();
        Font label8Font = this.$$$getFont$$$("Noto Sans", Font.BOLD, 22, label8.getFont());
        if (label8Font != null) label8.setFont(label8Font);
        label8.setForeground(new Color(-4407616));
        label8.setText("EMAIL");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        contactDetails.add(label8, gbc);
        email = new JTextField();
        email.setBackground(new Color(-4407616));
        email.setFocusable(true);
        Font emailFont = this.$$$getFont$$$("Roboto Condensed", Font.BOLD, 20, email.getFont());
        if (emailFont != null) email.setFont(emailFont);
        email.setForeground(new Color(-16777216));
        email.setOpaque(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contactDetails.add(email, gbc);
        final JLabel label9 = new JLabel();
        Font label9Font = this.$$$getFont$$$("Noto Sans", Font.BOLD, 22, label9.getFont());
        if (label9Font != null) label9.setFont(label9Font);
        label9.setForeground(new Color(-4407616));
        label9.setText("MOBILE");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        contactDetails.add(label9, gbc);
        mobile = new JTextField();
        mobile.setBackground(new Color(-4407616));
        mobile.setFocusable(true);
        Font mobileFont = this.$$$getFont$$$("Roboto Condensed", Font.BOLD, 20, mobile.getFont());
        if (mobileFont != null) mobile.setFont(mobileFont);
        mobile.setForeground(new Color(-16777216));
        mobile.setOpaque(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contactDetails.add(mobile, gbc);
        final JLabel label10 = new JLabel();
        Font label10Font = this.$$$getFont$$$("Noto Sans", Font.BOLD, 22, label10.getFont());
        if (label10Font != null) label10.setFont(label10Font);
        label10.setForeground(new Color(-4407616));
        label10.setText("MAILING ADDRESS");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        contactDetails.add(label10, gbc);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        panel3.setOpaque(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contactDetails.add(panel3, gbc);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridBagLayout());
        panel4.setBackground(new Color(-4407616));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(panel4, gbc);
        final JLabel label11 = new JLabel();
        label11.setBackground(new Color(-4407616));
        Font label11Font = this.$$$getFont$$$("Noto Sans", Font.BOLD, 24, label11.getFont());
        if (label11Font != null) label11.setFont(label11Font);
        label11.setForeground(new Color(-16777216));
        label11.setOpaque(false);
        label11.setText("CONTACT DETAILS");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        panel4.add(label11, gbc);
        final JScrollPane scrollPane1 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.weighty = 50.0;
        gbc.fill = GridBagConstraints.BOTH;
        contactDetails.add(scrollPane1, gbc);
        address = new JTextArea();
        address.setAutoscrolls(false);
        address.setBackground(new Color(-4407616));
        address.setFocusable(true);
        Font addressFont = this.$$$getFont$$$("Roboto Condensed", Font.BOLD, 20, address.getFont());
        if (addressFont != null) address.setFont(addressFont);
        address.setForeground(new Color(-16777216));
        address.setLineWrap(true);
        address.setOpaque(true);
        address.setRows(3);
        address.setWrapStyleWord(true);
        scrollPane1.setViewportView(address);
        professionGroup = new ButtonGroup();
        professionGroup.add(interiorDesignerRadioButton);
        professionGroup.add(interiorDesignerRadioButton);
        professionGroup.add(electricalEngineerRadioButton);
        professionGroup.add(mechanicalEngineerRadioButton);
        professionGroup.add(tradeBuyerRadioButton);
        professionGroup.add(othersRadioButton);
        professionGroup.add(architectRadioButton);
        professionGroup.add(civilEngineerRadioButton);
        professionGroup.add(contractorRadioButton);
        professionGroup.add(developerRadioButton);
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(seeNewProductsCheckBox);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(newspaperCheckBox);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}



