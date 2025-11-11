import javax.swing.*;
import java.awt.*;
import java.io.File;
import javax.xml.parsers.*;
import org.w3c.dom.*;

public class PayslipViewerFrame extends JFrame {

    private JTextArea payslipArea;

    public PayslipViewerFrame(File payslipFile) {
        setTitle("View Payslip");
        setSize(550, 500);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.white);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel heading = new JLabel("Payslip Viewer", SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 20));
        heading.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(heading, BorderLayout.NORTH);

        payslipArea = new JTextArea();
        payslipArea.setEditable(false);
        payslipArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        payslipArea.setMargin(new Insets(10, 10, 10, 10));
        add(new JScrollPane(payslipArea), BorderLayout.CENTER);

        loadPayslip(payslipFile);

        setVisible(true);
    }

    private void loadPayslip(File file) {
        try {
            if (!file.exists()) {
                payslipArea.setText("❌ Payslip file not found: " + file.getName());
                return;
            }

            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();

            Element root = doc.getDocumentElement();

            StringBuilder sb = new StringBuilder();
            sb.append("Payslip File: ").append(file.getName()).append("\n");
            sb.append("====================================\n");
            sb.append("Employee ID: ").append(root.getElementsByTagName("employeeID").item(0).getTextContent()).append("\n\n");

            sb.append("Attendance:\n");
            sb.append("  Present: ").append(root.getElementsByTagName("present").item(0).getTextContent()).append("\n");
            sb.append("  Absent:  ").append(root.getElementsByTagName("absent").item(0).getTextContent()).append("\n");
            sb.append("  Overtime: ").append(root.getElementsByTagName("overtime").item(0).getTextContent()).append("\n\n");

            sb.append("Salary Details:\n");
            sb.append("  Basic:   ").append(root.getElementsByTagName("basic").item(0).getTextContent()).append("\n");
            sb.append("  HRA:     ").append(root.getElementsByTagName("hra").item(0).getTextContent()).append("\n");
            sb.append("  DA:      ").append(root.getElementsByTagName("da").item(0).getTextContent()).append("\n");
            sb.append("  Bonus:   ").append(root.getElementsByTagName("bonus").item(0).getTextContent()).append("\n");
            sb.append("  PF:      ").append(root.getElementsByTagName("pf").item(0).getTextContent()).append("\n");
            sb.append("  Tax(%):  ").append(root.getElementsByTagName("tax").item(0).getTextContent()).append("\n\n");

            sb.append("Net Salary: ₹").append(root.getElementsByTagName("net").item(0).getTextContent()).append("\n");
            sb.append("====================================");

            payslipArea.setText(sb.toString());

        } catch (Exception ex) {
            payslipArea.setText("Error loading payslip: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
