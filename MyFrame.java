
import javax.swing.*;
import java.awt.*;


public class MyFrame extends JFrame {
    JButton button;
    JLabel Login, Name, Pass;
    JPanel left, right;
    JTextField name;
    JPasswordField pass;


    private void handleLogin(){
        String username = name.getText().trim();
        String password = new String(pass.getPassword());

        if(username.isEmpty()||password.isEmpty()){
            JOptionPane.showMessageDialog(this, "Please enter both username and passowrd!", "Input error!", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try{
            String[] loginResult = LoginInterface.LoginUser(username, password);

            if (loginResult != null) {
                JOptionPane.showMessageDialog(this,
                        "Welcome, " + loginResult[1] + "!",
                        "Login Success",
                        JOptionPane.INFORMATION_MESSAGE);

                this.dispose();
                if ("admin".equals(loginResult[0])) {
                    new AdminDashboard(username).setVisible(true);
                } else {
                    new MemberDashboard(username).setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid username or password",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    public MyFrame() {
        left = new JPanel();
        left.setBounds(0, 0, 400, 500);
        left.setBackground(new Color(0, 102, 102));
        left.setLayout(null);

        // 1. Load the image
        ImageIcon icon = new ImageIcon("firstLabel.png");

        // 2. Scale the image (adjust numbers as needed)
        Image img = icon.getImage();
        Image scaledImg = img.getScaledInstance(374, 666, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImg);

        // 3. Create label with image
        JLabel imageLabel = new JLabel(scaledIcon);
        imageLabel.setBounds(50, 100, 400, 680);

        right = new JPanel();
        right.setBounds(400, 0, 800, 500);
        right.setLayout(null);

        Login = new JLabel("NoBook Library");
        Login.setBounds(569, 50, 200, 50);
        Login.setFont(new Font("", Font.BOLD, 23));
        Login.setForeground(new Color(0, 102, 102));

        Name = new JLabel("Name");
        Name.setBounds(512, 100, 200, 50);
        Name.setFont(new Font("", Font.PLAIN, 16));
        Name.setForeground(Color.black);

        name = new JTextField();
        name.setBounds(512, 140, 285, 25);


        Pass = new JLabel("Password");
        Pass.setBounds(512, 200, 200, 50);
        Pass.setFont(new Font("", Font.PLAIN, 16));
        Pass.setForeground(Color.black);

        pass = new JPasswordField();
        pass.setBounds(512, 240, 285, 25);

        button = new JButton("Login");
        button.setBounds(512, 300, 100, 20);
        button.setFocusable(false);
        button.addActionListener(e -> handleLogin());









        this.setTitle("Library of Liberty");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(900, 500);
        this.setVisible(true);
        this.setResizable(false);

        left.add(imageLabel);

        right.add(Login);
        // Name->Label name->Txt
        right.add(Name);
        right.add(name);

        // Pass->Label pass->passField
        right.add(Pass);
        right.add(pass);

        right.add(button);

        this.add(left);
        this.add(right);
    }
}




/*
public class MyFrame extends JFrame {
    JButton button;
    JLabel Login, Name, Pass;
    JPanel left, right;
    JTextField name;
    JPasswordField pass;

    private void handleLogin() {
        String username = name.getText().trim();
        String password = new String(pass.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password!", "Input error!", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String[] loginResult = MyJDBC.LoginUser(username, password);

            if (loginResult != null) {
                JOptionPane.showMessageDialog(this, "Welcome, " + loginResult[1] + "!", "Login Success", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();

                String role = loginResult[0];
                if ("admin".equalsIgnoreCase(role)) {
                    new AdminDashboard(username).setVisible(true);
                } else if ("member".equalsIgnoreCase(role)) {
                    new MemberDashboard(username).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Unknown user role: " + role);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public MyFrame() {
        left = new JPanel();
        left.setBounds(0, 0, 400, 500);
        left.setBackground(new Color(0, 102, 102));
        left.setLayout(null);

        // Optional image
        ImageIcon icon = new ImageIcon("firstLabel.png");
        Image img = icon.getImage();
        Image scaledImg = img.getScaledInstance(374, 666, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImg);
        JLabel imageLabel = new JLabel(scaledIcon);
        imageLabel.setBounds(50, 100, 400, 680);

        right = new JPanel();
        right.setBounds(400, 0, 800, 500);
        right.setLayout(null);

        Login = new JLabel("Good Day");
        Login.setBounds(569, 50, 200, 50);
        Login.setFont(new Font("", Font.BOLD, 36));
        Login.setForeground(new Color(0, 102, 102));

        Name = new JLabel("Name");
        Name.setBounds(512, 100, 200, 50);
        Name.setFont(new Font("", Font.PLAIN, 16));

        name = new JTextField();
        name.setBounds(512, 140, 285, 25);

        Pass = new JLabel("Password");
        Pass.setBounds(512, 200, 200, 50);
        Pass.setFont(new Font("", Font.PLAIN, 16));

        pass = new JPasswordField();
        pass.setBounds(512, 240, 285, 25);

        button = new JButton("Login");
        button.setBounds(512, 300, 100, 20);
        button.setFocusable(false);
        button.addActionListener(e -> handleLogin());

        this.setTitle("Library of Liberty");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(900, 500);
        this.setResizable(false);
        this.setLayout(null);

        left.add(imageLabel);

        right.add(Login);
        right.add(Name);
        right.add(name);
        right.add(Pass);
        right.add(pass);
        right.add(button);

        this.add(left);
        this.add(right);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new MyFrame();
    }
}
*/