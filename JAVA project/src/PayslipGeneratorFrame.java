import javax.swing.*;
import java.awt.*;
import java.io.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PayslipGeneratorFrame extends JFrame {

    JComboBox<String> empList;
    JTextField basicField, hraField, daField, bonusField, pfField, taxField, netField;
    JTextField presentField, absentField, overtimeField;
    JButton saveBtn, loadBtn;

    public PayslipGeneratorFrame() {
        setTitle("Payslip Generator");
        setSize(550, 750);
        setLayout(null);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.white);

        JLabel lbl = new JLabel("Generate Payslip (Admin Only)");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lbl.setBounds(140, 20, 300, 30);
        add(lbl);

        JLabel empLabel = new JLabel("Select Employee:");
        empLabel.setBounds(60, 80, 150, 30);
        add(empLabel);

        empList = new JComboBox<>();
        empList.setBounds(220, 80, 200, 30);
        add(empList);
        loadEmployees();

        loadBtn = new JButton("Load Salary Data");
        loadBtn.setBounds(160, 130, 200, 35);
        add(loadBtn);
        loadBtn.addActionListener(e -> loadSalaryData());

        JLabel p1 = new JLabel("Present Days:");
        p1.setBounds(60, 180, 150, 30);
        add(p1);
        presentField = new JTextField();
        presentField.setBounds(220, 180, 200, 30);
        presentField.setEditable(false);
        add(presentField);

        JLabel p2 = new JLabel("Absent Days:");
        p2.setBounds(60, 230, 150, 30);
        add(p2);
        absentField = new JTextField();
        absentField.setBounds(220, 230, 200, 30);
        absentField.setEditable(false);
        add(absentField);

        JLabel p3 = new JLabel("Overtime Hours:");
        p3.setBounds(60, 280, 150, 30);
        add(p3);
        overtimeField = new JTextField();
        overtimeField.setBounds(220, 280, 200, 30);
        overtimeField.setEditable(false);
        add(overtimeField);

        JLabel basicLabel = new JLabel("Basic Salary:");
        basicLabel.setBounds(60, 330, 150, 30);
        add(basicLabel);
        basicField = new JTextField();
        basicField.setBounds(220, 330, 200, 30);
        add(basicField);

        JLabel hraLabel = new JLabel("HRA (40%):");
        hraLabel.setBounds(60, 380, 150, 30);
        add(hraLabel);
        hraField = new JTextField();
        hraField.setBounds(220, 380, 200, 30);
        hraField.setEditable(false);
        add(hraField);

        JLabel daLabel = new JLabel("DA (20%):");
        daLabel.setBounds(60, 430, 150, 30);
        add(daLabel);
        daField = new JTextField();
        daField.setBounds(220, 430, 200, 30);
        daField.setEditable(false);
        add(daField);

        JLabel bonusLabel = new JLabel("Bonus:");
        bonusLabel.setBounds(60, 480, 150, 30);
        add(bonusLabel);
        bonusField = new JTextField();
        bonusField.setBounds(220, 480, 200, 30);
        add(bonusField);

        JLabel pfLabel = new JLabel("PF:");
        pfLabel.setBounds(60, 530, 150, 30);
        add(pfLabel);
        pfField = new JTextField();
        pfField.setBounds(220, 530, 200, 30);
        add(pfField);

        JLabel taxLabel = new JLabel("Tax %:");
        taxLabel.setBounds(60, 580, 150, 30);
        add(taxLabel);
        taxField = new JTextField();
        taxField.setBounds(220, 580, 200, 30);
        add(taxField);

        JLabel netLabel = new JLabel("Net Salary:");
        netLabel.setBounds(60, 630, 150, 30);
        add(netLabel);
        netField = new JTextField();
        netField.setBounds(220, 630, 200, 30);
        netField.setEditable(false);
        add(netField);

        saveBtn = new JButton("Generate Payslip XML");
        saveBtn.setBounds(150, 680, 220, 40);
        add(saveBtn);
        saveBtn.addActionListener(e -> calculateAndGeneratePayslip());

        setVisible(true);
    }

    void loadEmployees() {
        try {
            File f = new File(".."+ File.separator +"xml"+ File.separator +"employees.xml");
            if (!f.exists()) return;
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = db.parse(f);
            NodeList list = doc.getElementsByTagName("employee");
            for (int i = 0; i < list.getLength(); i++) {
                Element emp = (Element) list.item(i);
                String id = emp.getElementsByTagName("id").item(0).getTextContent();
                empList.addItem(id);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading employees!");
        }
    }

    void loadSalaryData() {
        try {
            String empID = empList.getSelectedItem().toString();
            File f = new File(".."+ File.separator +"xml"+ File.separator +"salary"+ File.separator +"salary_" + empID + ".xml");
            if (!f.exists()) {
                JOptionPane.showMessageDialog(this, "No salary data found! Use Salary Calculator first.");
                return;
            }
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = db.parse(f);
            basicField.setText(getTagValue(doc, "basic"));
            hraField.setText(getTagValue(doc, "hra"));
            daField.setText(getTagValue(doc, "da"));
            bonusField.setText(getTagValue(doc, "bonus"));
            pfField.setText(getTagValue(doc, "pf"));
            taxField.setText(getTagValue(doc, "tax"));
            presentField.setText(getTagValue(doc, "present"));
            absentField.setText(getTagValue(doc, "absent"));
            overtimeField.setText(getTagValue(doc, "overtime"));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading salary data!");
        }
    }

    String getTagValue(Document doc, String tag) {
        return doc.getElementsByTagName(tag).item(0).getTextContent();
    }

    // ✅ New unified method: calculates net salary AND generates XML
    void calculateAndGeneratePayslip() {
        try {
            double basic = Double.parseDouble(basicField.getText());
            double hra = basic * 0.40;
            double da = basic * 0.20;
            double bonus = Double.parseDouble(bonusField.getText());
            double pf = Double.parseDouble(pfField.getText());
            double tax = Double.parseDouble(taxField.getText());
            int absent = Integer.parseInt(absentField.getText());
            int overtime = Integer.parseInt(overtimeField.getText());

            double perDay = basic / 30;
            double deduction = absent * perDay;
            double overtimePay = overtime * 100;
            double gross = basic + hra + da + bonus + overtimePay;
            double taxAmt = (tax / 100.0) * gross;
            double net = gross - pf - taxAmt - deduction;

            netField.setText(String.format("%.2f", net));

            // Save payslip XML
            String empID = empList.getSelectedItem().toString();
            File folder = new File(".."+ File.separator +"xml"+ File.separator +"payroll");
            if (!folder.exists()) folder.mkdirs();

            String monthYear = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy_MM"));
            File payslipFile = new File(folder, empID + "_" + monthYear + ".xml");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element root = doc.createElement("payslip");
            doc.appendChild(root);

            root.appendChild(createElem(doc, "employeeID", empID));
            root.appendChild(createElem(doc, "basic", String.valueOf(basic)));
            root.appendChild(createElem(doc, "hra", String.valueOf(hra)));
            root.appendChild(createElem(doc, "da", String.valueOf(da)));
            root.appendChild(createElem(doc, "bonus", String.valueOf(bonus)));
            root.appendChild(createElem(doc, "pf", String.valueOf(pf)));
            root.appendChild(createElem(doc, "tax", String.valueOf(tax)));
            root.appendChild(createElem(doc, "net", String.valueOf(net)));
            root.appendChild(createElem(doc, "present", String.valueOf(presentField.getText())));
            root.appendChild(createElem(doc, "absent", String.valueOf(absentField.getText())));
            root.appendChild(createElem(doc, "overtime", String.valueOf(overtimeField.getText())));

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(doc), new StreamResult(new FileOutputStream(payslipFile)));

            JOptionPane.showMessageDialog(this, "Payslip generated successfully: " + payslipFile.getName());

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error generating payslip!");
        }
    }

    Element createElem(Document doc, String name, String value) {
        Element e = doc.createElement(name);
        e.setTextContent(value);
        return e;
    }

    public static void main(String[] args) {
        new PayslipGeneratorFrame();
    }
}
