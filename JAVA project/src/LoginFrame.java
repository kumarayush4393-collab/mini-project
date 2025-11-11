import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {

    JTextField userField;
    JPasswordField passField;
    JButton loginBtn;

    public LoginFrame() {
        setTitle("Payroll Login");
        setSize(350, 250);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.white);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(30, 40, 100, 30);
        add(userLabel);

        userField = new JTextField();
        userField.setBounds(130, 40, 150, 30);
        add(userField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(30, 90, 100, 30);
        add(passLabel);

        passField = new JPasswordField();
        passField.setBounds(130, 90, 150, 30);
        add(passField);

        loginBtn = new JButton("Login");
        loginBtn.setBounds(110, 150, 100, 40);
        add(loginBtn);

        loginBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginAction();
            }
        });

        setVisible(true);
    }

    void loginAction() {
        String user = userField.getText();
        String pass = String.valueOf(passField.getPassword());

        // Hardcoded users for now
        if(user.equals("admin") && pass.equals("admin123")) {
            dispose();
            new AdminDashboard();
        }
        else if(user.equals("employee") && pass.equals("emp123")) {
            dispose();
            new EmployeeDashboard("EMP001"); // Pass employee ID
        }
        else {
            JOptionPane.showMessageDialog(this, "Invalid Credentials");
        }
    }

    public static void main(String[] args) {
        new LoginFrame();
    }
}
