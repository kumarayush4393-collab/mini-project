import javax.swing.*;
import java.awt.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

public class AttendanceFrame extends JFrame {

    JComboBox<String> empList;
    JTextField dateField, overtimeField;
    JRadioButton presentBtn, absentBtn;
    ButtonGroup group;

    public AttendanceFrame() {
        setTitle("Attendance Management");
        setSize(450, 420);
        setLayout(null);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.white);

        JLabel empLabel = new JLabel("Select Employee:");
        empLabel.setBounds(40, 40, 150, 30);
        add(empLabel);

        empList = new JComboBox<>();
        empList.setBounds(200, 40, 180, 30);
        add(empList);
        loadEmployees();

        JLabel dateLabel = new JLabel("Date (DD-MM-YYYY):");
        dateLabel.setBounds(40, 90, 150, 30);
        add(dateLabel);

        dateField = new JTextField();
        dateField.setBounds(200, 90, 180, 30);
        add(dateField);

        presentBtn = new JRadioButton("Present");
        absentBtn = new JRadioButton("Absent");
        presentBtn.setBackground(Color.white);
        absentBtn.setBackground(Color.white);
        presentBtn.setBounds(200, 140, 100, 30);
        absentBtn.setBounds(300, 140, 100, 30);

        add(presentBtn);
        add(absentBtn);

        group = new ButtonGroup();
        group.add(presentBtn);
        group.add(absentBtn);

        JLabel overtimeLabel = new JLabel("Overtime (hrs):");
        overtimeLabel.setBounds(40, 190, 150, 30);
        add(overtimeLabel);

        overtimeField = new JTextField("0");
        overtimeField.setBounds(200, 190, 180, 30);
        add(overtimeField);

        JButton saveBtn = new JButton("Save Attendance");
        saveBtn.setBounds(140, 260, 180, 40);
        add(saveBtn);

        saveBtn.addActionListener(e -> saveAttendance());

        setVisible(true);
    }

    void loadEmployees() {
        try {
            // ✅ Load from xml folder in project root (not inside src)
            File f = new File("../xml/employees.xml");
            if (!f.exists()) return;

            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = db.parse(f);
            NodeList list = doc.getElementsByTagName("employee");

            for (int i = 0; i < list.getLength(); i++) {
                Element emp = (Element) list.item(i);
                empList.addItem(emp.getElementsByTagName("id").item(0).getTextContent());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading employees: " + e.getMessage());
        }
    }

    void saveAttendance() {
        try {
            // ✅ Save attendance in xml folder (outside src)
            File file = new File("../xml/attendance.xml");

            // Create xml folder if it doesn’t exist
            file.getParentFile().mkdirs();

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc;

            if (!file.exists()) {
                doc = db.newDocument();
                Element root = doc.createElement("attendance");
                doc.appendChild(root);
            } else {
                doc = db.parse(file);
            }

            Element root = doc.getDocumentElement();
            Element rec = doc.createElement("record");

            rec.appendChild(createNode(doc, "empID", empList.getSelectedItem().toString()));
            rec.appendChild(createNode(doc, "date", dateField.getText()));
            rec.appendChild(createNode(doc, "status", presentBtn.isSelected() ? "Present" : "Absent"));
            rec.appendChild(createNode(doc, "overtime", overtimeField.getText()));

            root.appendChild(rec);

            Transformer tf = TransformerFactory.newInstance().newTransformer();
            tf.transform(new DOMSource(doc), new StreamResult(file));

            JOptionPane.showMessageDialog(this, "Attendance Saved Successfully!");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    Node createNode(Document doc, String tag, String val) {
        Element e = doc.createElement(tag);
        e.appendChild(doc.createTextNode(val));
        return e;
    }
}
