import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;



public class Transaction {

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


    /*
    public static void viewTransaction() {
        try {
            Connection connectdb = Database.connect();

            Statement state = connectdb.createStatement();
            ResultSet result = state.executeQuery("SELECT * FROM transaction");

            //string builder
            StringBuilder stringb = new StringBuilder("====Transaction list====\n");
            while (result.next()) {
                stringb.append("Transaction ID: ").append(result.getInt("transaction_id")).append("\n");
                stringb.append("Member ID: ").append(result.getInt("member_id")).append("\n");
                stringb.append("Book Title: ").append(result.getString("book_title")).append("\n");
                stringb.append("Borrow Quantity: ").append(result.getInt("borrow_quantity")).append("\n");
                stringb.append("Borrow Date: ").append(result.getString("borrow_date")).append("\n");
                stringb.append("Return Date: ").append(result.getString("return_date")).append("\n");
                stringb.append("Status: ").append(result.getString("status")).append("\n\n");
            }

            JOptionPane.showMessageDialog(null, stringb.toString());
            connectdb.close();  //remember to put outside the while loop, so that it can load all the transaction in database

        } catch (Exception error) {
            JOptionPane.showMessageDialog(null, "Error viewing transaction !!! " + error.getMessage());
        }
    }
    */

    public static void viewTransaction() {
        try {
            Connection connectdb = Database.connect();

            Statement state = connectdb.createStatement();
            ResultSet result = state.executeQuery("SELECT * FROM transaction");

            // column for jtable
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

    //use private to prevent this method misused by other classes
    private void loadTransactionData(DefaultTableModel model) {
        try {
            Connection connectdb = Database.connect();
            Statement state = connectdb.createStatement();
            ResultSet result = state.executeQuery("SELECT * FROM transaction");

            model.setRowCount(0); // Clear existing rows

            while (result.next()) {
                Object[] row = new Object[7];
                row[0] = result.getInt("transaction_id");
                row[1] = result.getInt("member_id");
                row[2] = result.getString("book_title");
                row[3] = result.getInt("borrow_quantity");
                row[4] = result.getString("borrow_date");
                row[5] = result.getString("return_date");
                row[6] = result.getString("status");
                model.addRow(row);
            }

            connectdb.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error loading transactions: " + e.getMessage());
        }
    }



    public void showPage() {
        JFrame frame = new JFrame("Transaction Management");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLayout(new BorderLayout());

        // Top panel with buttons
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton borrowBtn = new JButton("Borrow Book");
        JButton returnBtn = new JButton("Return Book");
        JButton viewBtn = new JButton("View Transactions");
        JButton deleteBtn = new JButton("Delete Transaction");


        topPanel.add(borrowBtn);
        topPanel.add(returnBtn);
        topPanel.add(viewBtn);
        topPanel.add(deleteBtn);


        frame.add(topPanel, BorderLayout.NORTH);

        // Table panel
        String[] columnNames = {"Transaction ID", "Member ID", "Book Title",
                "Borrow Quantity", "Borrow Date", "Return Date", "Status"};

        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Load table data initially
        loadTransactionData(model);

        // Button actions
        borrowBtn.addActionListener(e -> {
            borrowBook();
            loadTransactionData(model);  // refresh
        });

        returnBtn.addActionListener(e -> {
            returnBook();
            loadTransactionData(model);  // refresh
        });

        deleteBtn.addActionListener(e -> {
            deleteTransaction();
            loadTransactionData(model);  // refresh
        });

        viewBtn.addActionListener(e -> {
            loadTransactionData(model);  // refresh manually
        });


        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


/*
    public void showPage() {
        JFrame frame = new JFrame("Transaction Management System");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(null);

        // Buttons
        JButton borrowBtn = new JButton("Borrow Book");
        JButton returnBtn = new JButton("Return Book");
        JButton deleteBtn = new JButton("Delete Transaction");
        JButton viewBtn = new JButton("View Transaction");


        // Set positions
        borrowBtn.setBounds(100, 30, 200, 30);
        deleteBtn.setBounds(100, 70, 200, 30);
        returnBtn.setBounds(100, 110, 200, 30);
        viewBtn.setBounds(100, 150, 200, 30);

        // Add to frame
        frame.add(borrowBtn);
        frame.add(deleteBtn);
        frame.add(returnBtn);
        frame.add(viewBtn);
        frame.add(viewBtn);

        borrowBtn.addActionListener(e -> borrowBook());
        viewBtn.addActionListener(e-> viewTransaction());
        returnBtn.addActionListener(e-> returnBook());
        deleteBtn.addActionListener(e-> deleteTransaction());



        frame.setVisible(true);
    }
    */



}