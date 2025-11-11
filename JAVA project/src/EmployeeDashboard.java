import javax.swing.*;
import java.io.File;

public class EmployeeDashboard extends JFrame {

    private String empID; // Employee ID from login

    public EmployeeDashboard(String empID) {
        this.empID = empID; // assign logged-in employee ID

        setTitle("Employee Dashboard - " + empID);
        setSize(500, 300);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ✅ Background image setup
        JLabel background = new JLabel(new ImageIcon("background.jpg"));
        background.setBounds(0, 0, 500, 300);
        background.setLayout(null); // so we can add buttons on top
        setContentPane(background);

        // ✅ Buttons
        JButton viewPayslip = new JButton("View Latest Payslip");
        viewPayslip.setBounds(150, 80, 200, 40);
        background.add(viewPayslip);

        JButton viewHistory = new JButton("View Salary History");
        viewHistory.setBounds(150, 140, 200, 40);
        background.add(viewHistory);

        // ✅ Action: Open latest payslip
        viewPayslip.addActionListener(e -> openLatestPayslip());

        // ✅ Action: Open salary history frame
        viewHistory.addActionListener(e -> new SalaryHistoryFrame(empID));

        setVisible(true);
    }

    private void openLatestPayslip() {
        try {
            File folder = new File(System.getProperty("user.dir") + File.separator + ".." 
                    + File.separator + "xml" + File.separator + "payroll");

            if (!folder.exists() || !folder.isDirectory()) {
                JOptionPane.showMessageDialog(this, "Payroll folder not found!");
                return;
            }

            File[] files = folder.listFiles((dir, name) -> name.startsWith(empID + "_") && name.endsWith(".xml"));
            if (files == null || files.length == 0) {
                JOptionPane.showMessageDialog(this, "No payslip found for " + empID);
                return;
            }

            // Get the latest file by lastModified
            File latest = files[0];
            for (File f : files) {
                if (f.lastModified() > latest.lastModified()) {
                    latest = f;
                }
            }

            new PayslipViewerFrame(latest);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error opening latest payslip: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // ✅ TEST MAIN
    public static void main(String[] args) {
        new EmployeeDashboard("EMP001"); // replace with dynamic login empID
    }
}
