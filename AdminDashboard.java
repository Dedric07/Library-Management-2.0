import javax.swing.*;
import java.awt.event.*;



public class AdminDashboard extends JFrame {
    public AdminDashboard(String username) {
        setTitle("Admin Dashboard - Welcome " + username);
        setSize(400, 300);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton memberBtn = new JButton("Manage Members");
        JButton bookBtn = new JButton("Manage Books");
        JButton transBtn = new JButton("Manage Transactions");
        JButton logoutBtn = new JButton("Log Out");

        memberBtn.setBounds(100, 50, 200, 30);
        bookBtn.setBounds(100, 100, 200, 30);
        transBtn.setBounds(100, 150, 200, 30);
        logoutBtn.setBounds( 100, 200,200,30 );

        add(memberBtn);
        add(bookBtn);
        add(transBtn);
        add(logoutBtn);

        // Connect to your real functions
        memberBtn.addActionListener(e -> new Member().showPage());
        bookBtn.addActionListener(e -> new Book().showPage());
        transBtn.addActionListener(e -> new Transaction().showPage());
        logoutBtn.addActionListener(e -> {
            dispose(); // close the dashboard
            new MyFrame(); // reopen the login interface
        });


        setVisible(true);
    }
}
