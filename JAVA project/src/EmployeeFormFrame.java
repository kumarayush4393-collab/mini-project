import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class EmployeeFormFrame extends JFrame {

    JTextField idField, nameField, deptField, desgField, joinDateField, basicSalaryField, taxField;

    public EmployeeFormFrame() {
        setTitle("Add Employee");
        setSize(450, 450);
        setLayout(null);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.white);

        JLabel idLabel = new JLabel("Employee ID:");
        idLabel.setBounds(40, 40, 150, 30);
        add(idLabel);

        idField = new JTextField();
        idField.setBounds(200, 40, 180, 30);
        add(idField);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(40, 90, 150, 30);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(200, 90, 180, 30);
        add(nameField);

        JLabel deptLabel = new JLabel("Department:");
        deptLabel.setBounds(40, 140, 150, 30);
        add(deptLabel);

        deptField = new JTextField();
        deptField.setBounds(200, 140, 180, 30);
        add(deptField);

        JLabel desgLabel = new JLabel("Designation:");
        desgLabel.setBounds(40, 190, 150, 30);
        add(desgLabel);

        desgField = new JTextField();
        desgField.setBounds(200, 190, 180, 30);
        add(desgField);

        JLabel joinLabel = new JLabel("Joining Date:");
        joinLabel.setBounds(40, 240, 150, 30);
        add(joinLabel);

        joinDateField = new JTextField("DD-MM-YYYY");
        joinDateField.setBounds(200, 240, 180, 30);
        add(joinDateField);

        JLabel salaryLabel = new JLabel("Basic Salary:");
        salaryLabel.setBounds(40, 290, 150, 30);
        add(salaryLabel);

        basicSalaryField = new JTextField();
        basicSalaryField.setBounds(200, 290, 180, 30);
        add(basicSalaryField);

        JLabel taxLabel = new JLabel("Tax %:");
        taxLabel.setBounds(40, 340, 150, 30);
        add(taxLabel);

        taxField = new JTextField();
        taxField.setBounds(200, 340, 180, 30);
        add(taxField);

        JButton saveBtn = new JButton("Save Employee");
        saveBtn.setBounds(140, 380, 150, 30);
        add(saveBtn);

        saveBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveEmployee();
            }
        });

        setVisible(true);
    }

    void saveEmployee() {
        try {
            // ✅ Ensure the xml folder exists
            File dir = new File("xml");
            if (!dir.exists()) dir.mkdirs();

            // ✅ Save employees.xml inside xml folder
            File file = new File("../xml/employees.xml");

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc;

            if (!file.exists()) {
                doc = db.newDocument();
                Element root = doc.createElement("employees");
                doc.appendChild(root);
            } else {
                doc = db.parse(file);
            }

            Element root = doc.getDocumentElement();
            Element emp = doc.createElement("employee");

            emp.appendChild(createNode(doc, "id", idField.getText()));
            emp.appendChild(createNode(doc, "name", nameField.getText()));
            emp.appendChild(createNode(doc, "department", deptField.getText()));
            emp.appendChild(createNode(doc, "designation", desgField.getText()));
            emp.appendChild(createNode(doc, "joiningDate", joinDateField.getText()));
            emp.appendChild(createNode(doc, "basicSalary", basicSalaryField.getText()));
            emp.appendChild(createNode(doc, "tax", taxField.getText()));

            root.appendChild(emp);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(new DOMSource(doc), new StreamResult(file));

            JOptionPane.showMessageDialog(this, "Employee Saved Successfully!");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    Node createNode(Document doc, String tag, String value) {
        Element node = doc.createElement(tag);
        node.appendChild(doc.createTextNode(value));
        return node;
    }
}
