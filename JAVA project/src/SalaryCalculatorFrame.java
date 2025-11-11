import javax.swing.*;
import java.awt.*;
import java.io.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;

public class SalaryCalculatorFrame extends JFrame {

    JComboBox<String> empList;
    JTextField basicField, hraField, daField, bonusField, overtimeField, pfField, taxField, netField;
    JTextField presentField, absentField, totalOvertimeField;

    int presentDays = 0, absentDays = 0, overtimeHours = 0;

    public SalaryCalculatorFrame() {
        setTitle("Salary Calculator");
        setSize(500, 700);
        setLayout(null);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.white);

        JLabel selectLabel = new JLabel("Select Employee:");
        selectLabel.setBounds(40, 30, 150, 30);
        add(selectLabel);

        empList = new JComboBox<>();
        empList.setBounds(200, 30, 200, 30);
        add(empList);
        loadEmployees();

        JButton loadAttendance = new JButton("Load Attendance");
        loadAttendance.setBounds(150, 80, 200, 30);
        add(loadAttendance);
        loadAttendance.addActionListener(e -> loadAttendanceData());

        JLabel presentLabel = new JLabel("Present Days:");
        presentLabel.setBounds(40, 130, 150, 30);
        add(presentLabel);

        presentField = new JTextField();
        presentField.setBounds(200, 130, 200, 30);
        presentField.setEditable(false);
        add(presentField);

        JLabel absentLabel = new JLabel("Absent Days:");
        absentLabel.setBounds(40, 180, 150, 30);
        add(absentLabel);

        absentField = new JTextField();
        absentField.setBounds(200, 180, 200, 30);
        absentField.setEditable(false);
        add(absentField);

        JLabel totalOTLabel = new JLabel("Total Overtime Hours:");
        totalOTLabel.setBounds(40, 230, 150, 30);
        add(totalOTLabel);

        totalOvertimeField = new JTextField();
        totalOvertimeField.setBounds(200, 230, 200, 30);
        totalOvertimeField.setEditable(false);
        add(totalOvertimeField);

        JLabel basicLabel = new JLabel("Basic Salary:");
        basicLabel.setBounds(40, 280, 150, 30);
        add(basicLabel);

        basicField = new JTextField();
        basicField.setBounds(200, 280, 200, 30);
        add(basicField);

        JLabel hraLabel = new JLabel("HRA (40%):");
        hraLabel.setBounds(40, 330, 150, 30);
        add(hraLabel);
        hraField = new JTextField("40%");
        hraField.setBounds(200, 330, 200, 30);
        hraField.setEditable(false);
        add(hraField);

        JLabel daLabel = new JLabel("DA (20%):");
        daLabel.setBounds(40, 380, 150, 30);
        add(daLabel);
        daField = new JTextField("20%");
        daField.setBounds(200, 380, 200, 30);
        daField.setEditable(false);
        add(daField);

        JLabel bonusLabel = new JLabel("Bonus:");
        bonusLabel.setBounds(40, 430, 150, 30);
        add(bonusLabel);
        bonusField = new JTextField("2000");
        bonusField.setBounds(200, 430, 200, 30);
        add(bonusField);

        JLabel pfLabel = new JLabel("PF:");
        pfLabel.setBounds(40, 480, 150, 30);
        add(pfLabel);
        pfField = new JTextField("1800");
        pfField.setBounds(200, 480, 200, 30);
        add(pfField);

        JLabel taxLabel = new JLabel("Tax %:");
        taxLabel.setBounds(40, 530, 150, 30);
        add(taxLabel);
        taxField = new JTextField("10");
        taxField.setBounds(200, 530, 200, 30);
        add(taxField);

        JButton calcBtn = new JButton("Calculate & Save Salary");
        calcBtn.setBounds(130, 580, 220, 40);
        add(calcBtn);
        calcBtn.addActionListener(e -> calculateAndSaveSalary());

        JLabel netLabel = new JLabel("Net Salary:");
        netLabel.setBounds(40, 630, 150, 30);
        add(netLabel);

        netField = new JTextField();
        netField.setBounds(200, 630, 200, 30);
        netField.setEditable(false);
        add(netField);

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
            e.printStackTrace();
        }
    }

    void loadAttendanceData() {
        presentDays = absentDays = overtimeHours = 0;
        try {
            File f = new File(".."+ File.separator +"xml"+ File.separator +"attendance.xml");
            if (!f.exists()) {
                JOptionPane.showMessageDialog(this, "Attendance file not found!");
                return;
            }
            String empID = empList.getSelectedItem().toString();
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = db.parse(f);
            NodeList list = doc.getElementsByTagName("record");
            for (int i = 0; i < list.getLength(); i++) {
                Element rec = (Element) list.item(i);
                if (rec.getElementsByTagName("empID").item(0).getTextContent().equals(empID)) {
                    String status = rec.getElementsByTagName("status").item(0).getTextContent();
                    int ot = Integer.parseInt(rec.getElementsByTagName("overtime").item(0).getTextContent());
                    if (status.equalsIgnoreCase("Present")) presentDays++;
                    else absentDays++;
                    overtimeHours += ot;
                }
            }
            presentField.setText(String.valueOf(presentDays));
            absentField.setText(String.valueOf(absentDays));
            totalOvertimeField.setText(String.valueOf(overtimeHours));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error reading attendance!");
        }
    }

    void calculateAndSaveSalary() {
        try {
            double basic = Double.parseDouble(basicField.getText());
            double hra = basic * 0.40;
            double da = basic * 0.20;
            double bonus = Double.parseDouble(bonusField.getText());
            double pf = Double.parseDouble(pfField.getText());
            double tax = Double.parseDouble(taxField.getText());

            double perDay = basic / 30;
            double deduction = absentDays * perDay;
            double overtimePay = overtimeHours * 100;

            double gross = basic + hra + da + bonus + overtimePay;
            double taxAmt = (tax / 100.0) * gross;
            double net = gross - pf - taxAmt - deduction;

            netField.setText(String.format("%.2f", net));

            // ✅ Save salary XML
            String empID = empList.getSelectedItem().toString();
            File folder = new File(".."+ File.separator +"xml"+ File.separator +"salary");
            if (!folder.exists()) folder.mkdirs();
            File salaryFile = new File(folder, "salary_" + empID + ".xml");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element root = doc.createElement("salaryData");
            doc.appendChild(root);

            root.appendChild(createElem(doc, "employeeID", empID));
            root.appendChild(createElem(doc, "basic", String.valueOf(basic)));
            root.appendChild(createElem(doc, "hra", String.valueOf(hra)));
            root.appendChild(createElem(doc, "da", String.valueOf(da)));
            root.appendChild(createElem(doc, "bonus", String.valueOf(bonus)));
            root.appendChild(createElem(doc, "pf", String.valueOf(pf)));
            root.appendChild(createElem(doc, "tax", String.valueOf(tax)));
            root.appendChild(createElem(doc, "net", String.valueOf(net)));
            root.appendChild(createElem(doc, "present", String.valueOf(presentDays)));
            root.appendChild(createElem(doc, "absent", String.valueOf(absentDays)));
            root.appendChild(createElem(doc, "overtime", String.valueOf(overtimeHours)));

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(doc), new StreamResult(new FileOutputStream(salaryFile)));

            JOptionPane.showMessageDialog(this, "Salary saved successfully for Employee: " + empID);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error calculating or saving salary!");
        }
    }

    Element createElem(Document doc, String name, String value) {
        Element e = doc.createElement(name);
        e.setTextContent(value);
        return e;
    }

    public static void main(String[] args) {
        new SalaryCalculatorFrame();
    }
}
