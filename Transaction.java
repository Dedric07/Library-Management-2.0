
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;



public class Transaction {

    public static void borrowBook() {//borrow book
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

    //display transaction in a JTable
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

    public static void deleteTransaction() {
        try {
            int tranid = Integer.parseInt(JOptionPane.showInputDialog("Enter Transaction ID you want to delete:"));

            Connection connectdb = Database.connect();

            // Check if transaction exists
            String check = "SELECT * FROM transaction WHERE transaction_id = ?";
            PreparedStatement checkState = connectdb.prepareStatement(check);
            checkState.setInt(1, tranid);
            ResultSet result = checkState.executeQuery();

            if (result.next()) {
                int confirm = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to delete Transaction ID: " + tranid + "?",
                        "Confirm Delete", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    String deleteSql = "DELETE FROM transaction WHERE transaction_id = ?";
                    PreparedStatement delState = connectdb.prepareStatement(deleteSql);
                    delState.setInt(1, tranid);
                    delState.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Transaction deleted successfully!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Transaction ID " + tranid + " not found.");
            }

            connectdb.close();
        } catch (Exception error) {
            JOptionPane.showMessageDialog(null, "Error deleting transaction: " + error.getMessage());
        }
    }




    public static JPanel viewTransaction() {
        JPanel transactionPanel = new JPanel(new BorderLayout());
        transactionPanel.setBorder(BorderFactory.createTitledBorder("Transaction List"));

        try {
            Connection connectdb = Database.connect();
            Statement state = connectdb.createStatement();
            ResultSet result = state.executeQuery("SELECT * FROM TRANSACTION");

            String[] columnTransaction = {"Transaction ID", "Member ID", "Book Title", "Borrow Date", "Status", "Borrow Quantity", "Return Date"};
            List<String[]> transactionInfo = new ArrayList<>();

            while (result.next()) {
                String[] row = new String[7];
                row[0] = String.valueOf(result.getInt("transaction_id"));
                row[1] = String.valueOf(result.getInt("member_id"));
                row[2] = result.getString("book_title");
                row[3] = result.getString("borrow_date");
                row[4] = result.getString("status");
                row[5] = String.valueOf(result.getInt("borrow_quantity"));
                row[6] = result.getString("return_date");
                transactionInfo.add(row);
            }

            connectdb.close();

            String[][] tableData = new String[transactionInfo.size()][7];
            for (int i = 0; i < transactionInfo.size(); i++) {
                tableData[i] = transactionInfo.get(i);
            }

            JTable table = new JTable(tableData, columnTransaction);
            JScrollPane scrollPane = new JScrollPane(table);
            transactionPanel.add(scrollPane, BorderLayout.CENTER);

        } catch (Exception error) {
            JOptionPane.showMessageDialog(null, "Error loading transaction: " + error.getMessage());
            transactionPanel.add(new JLabel("Error loading transaction data"), BorderLayout.CENTER);
        }

        return transactionPanel;
    }

    
    public void showPage() {
        JFrame frame = new JFrame("Transaction Management System");
        frame.setSize(1000, 700);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(viewTransaction(), BorderLayout.CENTER);

        JButton deleteBtn = new JButton("Delete Transaction");

        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBackground(new Color(90, 93, 231));
        navPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        Dimension btnSize = new Dimension(180, 40);


        deleteBtn.setMaximumSize(btnSize);



        navPanel.add(deleteBtn);

        // Listeners
        deleteBtn.addActionListener(e-> deleteTransaction());

        frame.add(navPanel, BorderLayout.WEST);
        frame.add(contentPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}



