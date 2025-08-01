import javax.swing.*;

public class MemberDashboard extends JFrame {
    public MemberDashboard(String username) {
        setTitle("Member Dashboard - Welcome " + username);
        setSize(400, 200);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton transBtn = new JButton("Transaction Management");
        transBtn.setBounds(100, 60, 200, 30);

        add(transBtn);

        // Only allow viewing and managing personal transactions
        transBtn.addActionListener(e -> new UserTransaction().showPage());

        setVisible(true);
    }
}
