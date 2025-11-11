import javax.swing.*;
import java.awt.*;
import java.io.File;

public class SalaryHistoryFrame extends JFrame {

    private JList<String> payslipList;
    private DefaultListModel<String> listModel;

    public SalaryHistoryFrame(String empID) {
        setTitle("Salary History - " + empID);
        setSize(400, 500);
        setLayout(null);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.white);

        JLabel title = new JLabel("Payslips for Employee: " + empID);
        title.setBounds(40, 20, 300, 30);
        add(title);

        listModel = new DefaultListModel<>();
        payslipList = new JList<>(listModel);
        JScrollPane pane = new JScrollPane(payslipList);
        pane.setBounds(40, 70, 300, 300);
        add(pane);

        loadPayslips(empID);

        JButton openBtn = new JButton("Open Payslip");
        openBtn.setBounds(120, 390, 150, 40);
        add(openBtn);

        openBtn.addActionListener(e -> openSelectedPayslip());

        setVisible(true);
    }

    private void loadPayslips(String empID) {
        File folder = new File(System.getProperty("user.dir") + File.separator + ".." 
                + File.separator + "xml" + File.separator + "payroll");

        if (!folder.exists()) {
            JOptionPane.showMessageDialog(this, "❌ Payroll folder not found.");
            return;
        }

        File[] files = folder.listFiles((dir, name) -> name.startsWith(empID + "_") && name.endsWith(".xml"));
        if (files == null || files.length == 0) {
            listModel.addElement("No payslips found for " + empID);
            return;
        }

        for (File f : files) {
            listModel.addElement(f.getName());
        }
    }

    private void openSelectedPayslip() {
        String selected = payslipList.getSelectedValue();
        if (selected == null || selected.startsWith("No payslips")) {
            JOptionPane.showMessageDialog(this, "Select a valid payslip first!");
            return;
        }

        File file = new File(System.getProperty("user.dir") + File.separator + ".." 
                + File.separator + "xml" + File.separator + "payroll" + File.separator + selected);

        new PayslipViewerFrame(file);
    }
}
