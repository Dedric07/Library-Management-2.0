import javax.swing.*;

public class MemberDashboard extends JFrame {
    public MemberDashboard(String username) {
        setTitle("Member Dashboard - Welcome " + username);
        setSize(400, 300);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton transBtn = new JButton("Transaction System");
        JButton logoutBtn = new JButton("Log Out");

        transBtn.setBounds(100, 50, 200, 30);
        logoutBtn.setBounds(100, 100, 200,30);

        add(transBtn);
        add(logoutBtn);



        transBtn.addActionListener(e -> new UserTransaction().showPage());
        logoutBtn.addActionListener(e->{
            dispose();
            new MyFrame();
        });

        setVisible(true);
    }
}
