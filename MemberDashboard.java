import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MemberDashboard extends JFrame {

    public static void borrowBook() {
        try {
            int memid = Integer.parseInt(JOptionPane.showInputDialog("Enter your Member ID: "));
            String btitle = JOptionPane.showInputDialog("Enter book title you want to borrow: ");
            int bquantity = Integer.parseInt(JOptionPane.showInputDialog("Enter quantity: "));
            String borrowdate = JOptionPane.showInputDialog("Enter borrow date (DD-MM-YYYY): ");

            Connection connectdb = Database.connect();

            //find book and check quantity
            String checkbook = "SELECT book_quantity FROM book WHERE book_title = ?";
            PreparedStatement state = connectdb.prepareStatement(checkbook);
            state.setString(1, btitle);
            ResultSet result = state.executeQuery();

            //get
            if (result.next()) {
                int bkquantity = result.getInt("book_quantity");

                if (bkquantity > 0) {
                    // minus quantity from the table
                    String update = ("UPDATE book SET book_quantity = book_quantity - ?  WHERE book_title = ? ");
                    PreparedStatement updatestate = connectdb.prepareStatement(update);
                    updatestate.setInt(1, bquantity);
                    updatestate.setString(2, btitle);
                    updatestate.executeUpdate();

                    //insert into transaction table
                    String insert = "INSERT INTO transaction(member_id, book_title,borrow_quantity, borrow_date,status) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement insert1 = connectdb.prepareStatement(insert);
                    insert1.setInt(1, memid);
                    insert1.setString(2, btitle);
                    insert1.setInt(3, bquantity);
                    insert1.setString(4, borrowdate);
                    insert1.setString(5, "Borrowed");
                    insert1.executeUpdate();


                    JOptionPane.showMessageDialog(null, "Book borrowed successfully on " + borrowdate);
                } else {
                    JOptionPane.showMessageDialog(null, "Sorry, no stock available for this book.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Book title cannot be found.");
            }

            connectdb.close();
        } catch (Exception error) {
            JOptionPane.showMessageDialog(null, "Error borrowing book: " + error.getMessage());
        }
    }


    public static void returnBook() {
        try {
            int tranid = Integer.parseInt(JOptionPane.showInputDialog("Enter your transaction ID: "));
            String btitle = JOptionPane.showInputDialog("Enter book title you want to return: ");
            int bquantity = Integer.parseInt(JOptionPane.showInputDialog("Enter quantity to return: "));
            String returndate = JOptionPane.showInputDialog("Enter return date (DD-MM-YYYY): ");

            Connection connectdb = Database.connect();

            // Check borrowed record
            String checkquantity = "SELECT * FROM transaction WHERE transaction_id = ? AND book_title = ? AND status = 'Borrowed'";
            PreparedStatement state = connectdb.prepareStatement(checkquantity);
            state.setInt(1, tranid);
            state.setString(2, btitle);
            ResultSet result = state.executeQuery();

            if (result.next()) {
                int borrowquantity = result.getInt("borrow_quantity");

                if (bquantity > borrowquantity) {
                    JOptionPane.showMessageDialog(null, "You are trying to return more than borrowed.");
                } else {
                    // Add quantity back to book table
                    String updatequan = "UPDATE book SET book_quantity = book_quantity + ? WHERE book_title = ?";
                    PreparedStatement upstate = connectdb.prepareStatement(updatequan);
                    upstate.setInt(1, bquantity);
                    upstate.setString(2, btitle);
                    upstate.executeUpdate();

                    // Update transaction: set status and return date
                    String updatetran = "UPDATE transaction SET status = 'Returned', return_date = ? WHERE transaction_id = ? AND book_title = ? AND status = 'Borrowed'";
                    PreparedStatement uptran = connectdb.prepareStatement(updatetran);
                    uptran.setString(1, returndate);
                    uptran.setInt(2, tranid);
                    uptran.setString(3, btitle);
                    uptran.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Book returned successfully on " + returndate);
                }
            } else {
                JOptionPane.showMessageDialog(null, "No borrowed record found for book: " + btitle);
            }

            connectdb.close();
        } catch (Exception error) {
            JOptionPane.showMessageDialog(null, "Error returning book: " + error.getMessage());
        }
    }


    public static void viewTransaction() {
        try {
            Connection connectdb = Database.connect();

            Statement state = connectdb.createStatement();
            ResultSet result = state.executeQuery("SELECT * FROM transaction");

            // column for Jtable
            String[] columnTrans = {
                    "Transaction ID", "Member ID", "Book Title",
                    "Borrow Quantity", "Borrow Date", "Return Date", "Status"
            };


            List<String[]> transList = new ArrayList<>();

            while (result.next()) {
                String[] row = new String[7];
                row[0] = String.valueOf(result.getInt("transaction_id"));
                row[1] = String.valueOf(result.getInt("member_id"));
                row[2] = result.getString("book_title");
                row[3] = String.valueOf(result.getInt("borrow_quantity"));
                row[4] = result.getString("borrow_date");
                row[5] = result.getString("return_date");
                row[6] = result.getString("status");
                transList.add(row);
            }

            connectdb.close();

            // Convert List to 2D array
            String[][] tableData = new String[transList.size()][7];
            for (int i = 0; i < transList.size(); i++) {
                tableData[i] = transList.get(i);
            }

            // Create JTable and scroll pane
            JTable table = new JTable(tableData, columnTrans);
            JScrollPane scrollPane = new JScrollPane(table);

            // Show in JFrame
            JFrame frame = new JFrame("Transaction List");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(800, 400);
            frame.add(scrollPane);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

        } catch (Exception error) {
            JOptionPane.showMessageDialog(null, "Error viewing transaction !!! " + error.getMessage());
        }
    }

    public static JPanel viewBook() {
        JPanel bookPanel = new JPanel(new BorderLayout());
        bookPanel.setBorder(BorderFactory.createTitledBorder("Book List"));

        try {
            Connection connectdb = Database.connect();
            Statement state = connectdb.createStatement();
            ResultSet result = state.executeQuery("SELECT * FROM BOOK");

            String[] columnBook = {"Book ID", "Title", "Author", "Year", "Quantity", "Genre"};
            List<String[]> bookInfo = new ArrayList<>();

            while (result.next()) {
                String[] row = new String[6];
                row[0] = String.valueOf(result.getInt("book_id"));
                row[1] = result.getString("book_title");
                row[2] = result.getString("book_author");
                row[3] = String.valueOf(result.getInt("publish_year"));
                row[4] = String.valueOf(result.getInt("book_quantity"));
                row[5] = result.getString("book_genre");
                bookInfo.add(row);
            }

            connectdb.close();

            String[][] tableData = new String[bookInfo.size()][6];
            for (int i = 0; i < bookInfo.size(); i++) {
                tableData[i] = bookInfo.get(i);
            }

            JTable table = new JTable(tableData, columnBook);
            JScrollPane scrollPane = new JScrollPane(table);
            bookPanel.add(scrollPane, BorderLayout.CENTER);

        } catch (Exception error) {
            JOptionPane.showMessageDialog(null, "Error loading books: " + error.getMessage());
            bookPanel.add(new JLabel("Error loading book data"), BorderLayout.CENTER);
        }

        return bookPanel;
    }

    public MemberDashboard(String username) {
        setTitle("Member Dashboard");
        setSize(1000, 700);
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(viewBook(), BorderLayout.CENTER);

        JButton borrowBtn = new JButton("Borrow Book");
        JButton returnBtn = new JButton("Return Book");
        JButton viewBtn = new JButton("View Transaction");

        // WEST: SideBar
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBackground(new Color(0, 102, 102));
        navPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        Dimension btnSize = new Dimension(180, 40);
        borrowBtn.setMaximumSize(btnSize);
        returnBtn.setMaximumSize(btnSize);
        viewBtn.setMaximumSize(btnSize);

        navPanel.add(Box.createVerticalStrut(20));
        navPanel.add(borrowBtn);
        navPanel.add(Box.createVerticalStrut(10));
        navPanel.add(returnBtn);
        navPanel.add(Box.createVerticalStrut(10));
        navPanel.add(viewBtn);

        add(navPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        borrowBtn.addActionListener(e ->borrowBook());
        returnBtn.addActionListener(e -> returnBook());
        viewBtn.addActionListener(e -> viewTransaction());

        setVisible(true);
    }
}

