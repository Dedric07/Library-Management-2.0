
import javax.swing.*; //for (GUI)
import java.awt.*;
import java.sql.*; //for database connection and sql queries
import java.util.List;
import java.util.ArrayList;



 public class Member {
    private int member_id;
    private String member_name;
    private String contact_details;


     public Member() {
         this.member_id = 0;
         this.member_name = "";
         this.contact_details = "";
     }

    //constructor
    public Member(int memberID, String name, String contactDetails) {
        this.member_id = memberID;
        this.member_name = name;
        this.contact_details = contactDetails;
    }

    //getter
    public int getMemberID()
    {
        return member_id;
    }

    public String getName()
    {
        return member_name;
    }

    public String getContactDetails()
    {
        return contact_details;
    }


     //use try catch instead of using if else because in this code got we use database, which something went wrong is out of our control,
     //add member function
     public static void addMember() {
         try {
             int id = Integer.parseInt(JOptionPane.showInputDialog("Enter Member ID:"));
             String name = JOptionPane.showInputDialog("Enter Member name:");
             String contact = JOptionPane.showInputDialog("Enter Contact details:");
             String pass = JOptionPane.showInputDialog("Enter Password:");

             Connection connectdb = Database.connect();

             //mysql command, but use ? instead of straight forward put values
             // ? -easier to insert user input and reusable
             //? =placeholder for real data
             //use prepared statement to insert values into ? safely

             String mysql = "INSERT INTO MEMBER (member_id, member_name, contact_details, member_password) VALUES (?, ?, ?, ?)";
             //prepare statement let us bind values safely into SQL using ?
             PreparedStatement state = connectdb.prepareStatement(mysql);
             state.setInt(1, id);
             state.setString(2, name);
             state.setString(3, contact);
             state.setString(4, pass);

             state.executeUpdate();
             connectdb.close();

             JOptionPane.showMessageDialog(null, "Member added successfully !!!");

         } catch (Exception error) {
             JOptionPane.showMessageDialog(null, "Error adding member !!! " + error.getMessage());
         }
     }

   
     public static JPanel viewMember() {
         JPanel memberPanel = new JPanel(new BorderLayout());
         memberPanel.setBorder(BorderFactory.createTitledBorder("Member List"));

         try {
             Connection connectdb = Database.connect();
             Statement state = connectdb.createStatement();
             ResultSet result = state.executeQuery("SELECT * FROM MEMBER");

             String[] columnMember = {"Member ID", "Member Name", "Contact Details", "Password"};
             List<String[]> memberInfo = new ArrayList<>();

             while (result.next()) {
                 String[] row = new String[4];
                 row[0] = String.valueOf(result.getInt("member_id"));
                 row[1] = result.getString("member_name");
                 row[2] = result.getString("contact_details");
                 row[3] = result.getString("member_password");
                 memberInfo.add(row);
             }

             connectdb.close();

             String[][] tableData = new String[memberInfo.size()][4];
             for (int i = 0; i < memberInfo.size(); i++) {
                 tableData[i] = memberInfo.get(i);
             }

             JTable table = new JTable(tableData, columnMember);
             JScrollPane scrollPane = new JScrollPane(table);
             memberPanel.add(scrollPane, BorderLayout.CENTER);

         } catch (Exception error) {
             JOptionPane.showMessageDialog(null, "Error loading members: " + error.getMessage());
             memberPanel.add(new JLabel("Error loading member data"), BorderLayout.CENTER);
         }

         return memberPanel;
     }



     public static void searchMember() {
         try {
             int id = Integer.parseInt(JOptionPane.showInputDialog("Enter Member ID you want to search:"));
             Connection connectdb = Database.connect();
             String mysql = "SELECT * FROM MEMBER WHERE member_id = ?";
             PreparedStatement state = connectdb.prepareStatement(mysql);
             state.setInt(1, id);
             ResultSet result = state.executeQuery();

             if (result.next()) {
                 String info = "Member Found:\n\n" +
                         "Member ID: " + result.getInt("member_id") + "\n" +
                         "Member name: " + result.getString("member_name") + "\n" +
                         "Contact details: " + result.getString("contact_details") + "\n" +
                         "Password is: " + result.getString("member_password");
                 JOptionPane.showMessageDialog(null, info);
             } else {
                 JOptionPane.showMessageDialog(null, "Member with ID " + id + " not found !!!");
             }

             connectdb.close();
         } catch (Exception error) {
             JOptionPane.showMessageDialog(null, "Error searching member !!! " + error.getMessage());
         }
     }


     public static void deleteMember() {
         try {
             int id = Integer.parseInt(JOptionPane.showInputDialog("Enter Member ID that you want to delete:"));
             Connection connectdb = Database.connect();

             String checkSql = "SELECT * FROM MEMBER WHERE member_id = ?";
             PreparedStatement checkstate = connectdb.prepareStatement(checkSql);
             checkstate.setInt(1, id);
             ResultSet result = checkstate.executeQuery();

             if (result.next()) {
                 int confirm = JOptionPane.showConfirmDialog(null,
                         "Are you sure you want to delete member with ID:" + id + "?",
                         "Confirmation", JOptionPane.YES_NO_OPTION);

                 if (confirm == JOptionPane.YES_OPTION) {
                     String deleteSql = "DELETE FROM MEMBER WHERE member_id = ?";
                     PreparedStatement deletestate = connectdb.prepareStatement(deleteSql);
                     deletestate.setInt(1, id);
                     deletestate.executeUpdate();
                     JOptionPane.showMessageDialog(null, "Member deleted successfully !!!");
                 }
             } else {
                 JOptionPane.showMessageDialog(null, "Member with ID " + id + " not found.");
             }

             connectdb.close();
         } catch (Exception error) {
             JOptionPane.showMessageDialog(null, "Error deleting member !!! "+ error.getMessage());
         }
     }


     public static void updateMember() {
         try {
             int id = Integer.parseInt(JOptionPane.showInputDialog("Enter Member ID to update:"));

             String nwname = JOptionPane.showInputDialog("Enter new name:");
             String nwcontact = JOptionPane.showInputDialog("Enter new contact details:");
             String nwpass = JOptionPane.showInputDialog("Enter new password:");

             // Connect to DB
             Connection connectdb = Database.connect();

             String mysql = "UPDATE MEMBER SET member_name = ?, contact_details = ?, member_password = ? WHERE member_id = ?";
             PreparedStatement stmt = connectdb.prepareStatement(mysql);
             stmt.setString(1, nwname);
             stmt.setString(2, nwcontact);
             stmt.setString(3, nwpass);
             stmt.setInt(4, id);


             int updaterow = stmt.executeUpdate(); // Return how many row update

             if (updaterow > 0) {
                 JOptionPane.showMessageDialog(null, "Member updated successfully !!!");
             } else {
                 JOptionPane.showMessageDialog(null, "Member ID which is " + id + "cannot be found !!!");
             }

             connectdb.close();
         } catch (Exception e) {
             JOptionPane.showMessageDialog(null, "Error updating member information: " + e.getMessage());
         }
     }


     public void showPage() {
         JFrame frame = new JFrame("Member Management System");
         frame.setSize(1000, 700);
         frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

         JPanel contentPanel = new JPanel(new BorderLayout());
         contentPanel.add(viewMember(), BorderLayout.CENTER);

         JButton addBtn = new JButton("Add Member");
         JButton deleteBtn = new JButton("Delete Member");
         JButton updateBtn = new JButton("Update Member");
         JButton searchBtn = new JButton("Search Member");

         JPanel navPanel = new JPanel();
         navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
         navPanel.setBackground(new Color(90, 93, 231));
         navPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
         Dimension btnSize = new Dimension(180, 40);

         addBtn.setMaximumSize(btnSize);
         deleteBtn.setMaximumSize(btnSize);
         updateBtn.setMaximumSize(btnSize);
         searchBtn.setMaximumSize(btnSize);

         navPanel.add(Box.createVerticalStrut(20));
         navPanel.add(addBtn);
         navPanel.add(Box.createVerticalStrut(10));
         navPanel.add(deleteBtn);
         navPanel.add(Box.createVerticalStrut(10));
         navPanel.add(updateBtn);
         navPanel.add(Box.createVerticalStrut(10));
         navPanel.add(searchBtn);


         // Listeners
         addBtn.addActionListener(e -> addMember());
         searchBtn.addActionListener(e -> searchMember());
         deleteBtn.addActionListener(e -> deleteMember());
         updateBtn.addActionListener(e -> updateMember());

         frame.add(navPanel, BorderLayout.WEST);
         frame.add(contentPanel, BorderLayout.CENTER);
         frame.setVisible(true);
     }

 }


