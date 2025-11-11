import javax.swing.*;

public class AdminDashboard extends JFrame {

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(500, 450);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ✅ Background image
        JLabel background = new JLabel(new ImageIcon("background.jpg"));
        background.setBounds(0, 0, 500, 450);
        background.setLayout(null); // so we can position buttons on top
        setContentPane(background);

        JButton addEmp = new JButton("Add Employee");
        addEmp.setBounds(150, 40, 200, 40);
        background.add(addEmp); // ✅ add to background

        JButton salaryCalc = new JButton("Calculate Salary");
        salaryCalc.setBounds(150, 100, 200, 40);
        background.add(salaryCalc);

        JButton generatePayslip = new JButton("Generate Payslip");
        generatePayslip.setBounds(150, 160, 200, 40);
        background.add(generatePayslip);

        JButton reports = new JButton("Reports");
        reports.setBounds(150, 220, 200, 40);
        background.add(reports);

        JButton attendance = new JButton("Attendance");
        attendance.setBounds(150, 280, 200, 40);
        background.add(attendance);

        // ✅ Button Actions
        addEmp.addActionListener(e -> new EmployeeFormFrame());
        salaryCalc.addActionListener(e -> new SalaryCalculatorFrame());
        generatePayslip.addActionListener(e -> new PayslipGeneratorFrame());
        reports.addActionListener(e -> new ReportsFrame());
        attendance.addActionListener(e -> new AttendanceFrame());

        setVisible(true);
    }
}
