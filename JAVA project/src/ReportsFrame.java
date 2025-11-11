import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.util.*;

public class ReportsFrame extends JFrame {

    HashMap<String, String> empDept = new HashMap<>();          // emp → dept
    HashMap<String, Double> deptSalary = new HashMap<>();       // dept → total salary
    HashMap<String, Double> yearSalary = new HashMap<>();       // year → total salary
    HashMap<String, Double> empSalary = new HashMap<>();        // emp → total salary

    public ReportsFrame() {
        setTitle("Reports & Analytics");
        setSize(700, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.white);
        setLayout(new BorderLayout());

        loadEmployeeDepartments();
        processPayslips();

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Department Report", departmentPanel());
        tabs.add("Yearly Summary", yearlyPanel());
        tabs.add("Employee Summary", employeePanel());

        add(tabs);
        setVisible(true);
    }

    void loadEmployeeDepartments() {
        try {
            File f = new File(System.getProperty("user.dir") + File.separator + ".." 
                    + File.separator + "xml" + File.separator + "employees.xml");
            if (!f.exists()) return;

            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = db.parse(f);

            NodeList list = doc.getElementsByTagName("employee");
            for (int i = 0; i < list.getLength(); i++) {
                Element e = (Element) list.item(i);
                String id = e.getElementsByTagName("id").item(0).getTextContent();
                String dept = e.getElementsByTagName("department").item(0).getTextContent();
                empDept.put(id, dept);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void processPayslips() {
        try {
            File folder = new File(System.getProperty("user.dir") + File.separator + ".." 
                    + File.separator + "xml" + File.separator + "payroll");
            if (!folder.exists() || !folder.isDirectory()) return;

            File[] files = folder.listFiles(f -> f.getName().endsWith(".xml"));
            if (files == null) return;

            for (File f : files) {
                String filename = f.getName(); // EMP001_2025_11.xml
                String empID = filename.substring(0, filename.indexOf("_"));
                String year = filename.substring(filename.indexOf("_") + 1, filename.lastIndexOf("_"));

                DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document doc = db.parse(f);
                Element root = doc.getDocumentElement();

                // Read net salary
                double net = Double.parseDouble(getTagValue(root, "net"));

                // Department-level
                String dept = empDept.getOrDefault(empID, "Unknown");
                deptSalary.put(dept, deptSalary.getOrDefault(dept, 0.0) + net);

                // Yearly-level
                yearSalary.put(year, yearSalary.getOrDefault(year, 0.0) + net);

                // Employee-level
                empSalary.put(empID, empSalary.getOrDefault(empID, 0.0) + net);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    String getTagValue(Element root, String tag) {
        NodeList list = root.getElementsByTagName(tag);
        if (list.getLength() > 0) {
            return list.item(0).getTextContent();
        }
        return "0";
    }

    JPanel departmentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columns = { "Department", "Total Salary (₹)" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (String dept : deptSalary.keySet()) {
            model.addRow(new Object[]{ dept, deptSalary.get(dept) });
        }

        JTable table = new JTable(model);
        panel.add(new JScrollPane(table));
        return panel;
    }

    JPanel yearlyPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columns = { "Year", "Total Salary (₹)" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (String year : yearSalary.keySet()) {
            model.addRow(new Object[]{ year, yearSalary.get(year) });
        }

        JTable table = new JTable(model);
        panel.add(new JScrollPane(table));
        return panel;
    }

    JPanel employeePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columns = { "Employee ID", "Department", "Total Salary (₹)" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (String emp : empSalary.keySet()) {
            String dept = empDept.getOrDefault(emp, "Unknown");
            model.addRow(new Object[]{ emp, dept, empSalary.get(emp) });
        }

        JTable table = new JTable(model);
        panel.add(new JScrollPane(table));
        return panel;
    }

    public static void main(String[] args) {
        new ReportsFrame();
    }
}
