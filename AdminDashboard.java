
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;




public class AdminDashboard extends JFrame {
    public AdminDashboard(String username) {
        setTitle("Admin Dashboard");
        setSize(1000, 700);
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // NORTH: Header
        JPanel header = new JPanel();
        header.setBackground(new Color(90, 93, 231));
        header.add(new JLabel("📕 LIBRARIAN DASHBOARD "));
        add(header, BorderLayout.NORTH);

        JButton memberBtn = new JButton("Manage Members");
        JButton bookBtn = new JButton("Manage Books");
        JButton transBtn = new JButton("Manage Transactions");
        JButton logoutBtn = new JButton("Log Out");

        // WEST: SideBar
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBackground(new Color(90, 93, 231));
        navPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        Dimension btnSize = new Dimension(180, 40);
        memberBtn.setMaximumSize(btnSize);
        bookBtn.setMaximumSize(btnSize);
        transBtn.setMaximumSize(btnSize);
        logoutBtn.setMaximumSize(btnSize);

        navPanel.add(Box.createVerticalStrut(20));
        navPanel.add(memberBtn);
        navPanel.add(Box.createVerticalStrut(10));
        navPanel.add(bookBtn);
        navPanel.add(Box.createVerticalStrut(10));
        navPanel.add(transBtn);

        navPanel.add(Box.createVerticalStrut(500));
        navPanel.add(logoutBtn);

        // CENTER: Main Content
        JPanel mainPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 240, 240));

        mainPanel.add(createStatusBox("Total Members", "1,245", new Color(70, 130, 180)));
        mainPanel.add(createStatusBox("Total Books", "5,678", new Color(60, 179, 113)));
        mainPanel.add(createStatusBox("Books Borrowed", "342", new Color(255, 165, 0)));
        mainPanel.add(createStatusBox("Books Returned", "298", new Color(186, 85, 211)));


        add(mainPanel, BorderLayout.CENTER);
        add(navPanel, BorderLayout.WEST);

        // Connect to your real functions
        memberBtn.addActionListener(e -> new Member().showPage());
        bookBtn.addActionListener(e -> new Book().showPage());
        transBtn.addActionListener(e -> new Transaction().showPage());
        logoutBtn.addActionListener(e -> {
            dispose(); // close the dashboard
            new MyFrame(); // reopen the login interface
        } );

        setVisible(true);
    }


    private JPanel createStatusBox(String title, String value, Color bgColor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgColor);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 36));
        valueLabel.setForeground(Color.WHITE);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);

        return panel;
    }
}
