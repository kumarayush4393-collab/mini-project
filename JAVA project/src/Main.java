import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // Create the splash frame
        JFrame splash = new JFrame("Employee Payroll System");
        splash.setSize(500, 300);
        splash.setLocationRelativeTo(null);
        splash.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        splash.setLayout(null);

        // Title label
        JLabel title = new JLabel("Employee Payroll System", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBounds(50, 30, 400, 40);
        splash.add(title);

        // Project info
        JLabel info = new JLabel("<html><center>Project by Yash Hooda and Ayush Kumar Yadav<br>UID: 23BCS13125 and UID:23BCS11594</center></html> ", SwingConstants.CENTER);
        info.setFont(new Font("Arial", Font.PLAIN, 16));
        info.setBounds(50, 90, 400, 60);
        splash.add(info);

        // Go to Login button
        JButton goLogin = new JButton("Go to Login");
        goLogin.setBounds(180, 180, 140, 40);
        splash.add(goLogin);

        goLogin.addActionListener(e -> {
            splash.dispose(); // close splash
            new LoginFrame(); // open login frame
        });

        splash.setVisible(true);
    }
}
