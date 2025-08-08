import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;


public class Book {

    private int book_id;
    private String book_title;
    private String book_author;
    private int publish_year;
    private int book_quantity;
    private String book_genre;

    public Book() {
        this.book_id = 0;
        this.book_title = "";
        this.book_author = "";
        this.publish_year = 0;
        this.book_quantity = 0;
        this.book_genre = "";
    }

    public Book(int id, String title, String author, int year, int quantity, String genre){
        this.book_id=id;
        this.book_title=title;
        this.book_author = author;
        this.publish_year = year;
        this.book_quantity=quantity;
        this.book_genre=genre;

    }

    //getter
    public int getbookID(){
        return book_id;

    }
    public String getTitle(){
        return book_title;
    }

    public String getAuthor() {
        return book_author;
    }

    public int getYear() {
        return publish_year;
    }

    public int getQuantity() {
        return book_quantity;
    }

    public String getGenre() {
        return book_genre;
    }


    public static void addBook(){
        try{
           String title = JOptionPane.showInputDialog("Enter Book title: ");
           String author = JOptionPane.showInputDialog("Enter Book author: ");
           int year = Integer.parseInt(JOptionPane.showInputDialog("Enter Publish Year: "));
           int quantity = Integer.parseInt(JOptionPane.showInputDialog("Enter Quantity: "));
           String genre= JOptionPane.showInputDialog("Enter Genre: ");

           Connection connectdb= Database.connect();
           String mysql = "INSERT INTO BOOK (book_title, book_author, publish_year, book_quantity,book_genre) " +
                   "VALUES (?,?,?,?,?)";
           PreparedStatement state = connectdb.prepareStatement(mysql);
           state.setString(1,title);
           state.setString(2,author);
           state.setInt(3,year);
           state.setInt(4, quantity);
           state.setString(5,genre);
           state.executeUpdate();
           connectdb.close();

           JOptionPane.showMessageDialog(null, "Book added successfully !!!");

        }catch (Exception error) {
            JOptionPane.showMessageDialog(null, "Error adding book !!! " + error.getMessage());
        }
    }

    /*
    public static void viewBook(){
        try{
            Connection connectdb = Database.connect();

            Statement state = connectdb.createStatement();
            ResultSet  result= state.executeQuery("SELECT * FROM BOOK");

            //string builder
            StringBuilder stringb = new StringBuilder("====Student list====\n");
            while(result.next()){
                stringb.append("Book ID: ").append(result.getInt("book_id")).append("\n");
                stringb.append("Book Title: ").append(result.getString("book_title")).append("\n");
                stringb.append("Book Author: ").append(result.getString("book_author")).append("\n");
                stringb.append("Book Year: ").append(result.getInt("publish_year")).append("\n");
                stringb.append("Book Quantity: ").append(result.getInt("book_quantity")).append("\n");
                stringb.append("Book Genre: ").append(result.getString("book_genre")).append("\n\n");

            }
            JOptionPane.showMessageDialog(null, stringb.toString());
            connectdb.close();  //remember to put outside the while loop, so that it can load all the book in database


        }catch(Exception error){
            JOptionPane.showMessageDialog(null, "Error viewing book !!! " + error.getMessage());
        }

    }
*/
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



    public static void searchBook(){
        try{
            String btitle = JOptionPane.showInputDialog("Enter book id you want to search: ");
            Connection connectdb = Database.connect();

            String mysql = "SELECT * FROM BOOK WHERE book_id = ? ";
            PreparedStatement state = connectdb.prepareStatement(mysql);
            state.setString(1, btitle);
            ResultSet result = state.executeQuery();

            if (result.next()) {
                String info2 = "Book Found:\n\n" +
                        "Book ID: " + result.getInt("book_id") + "\n" +
                        "Book Title: " + result.getString("book_title") + "\n" +
                        "Book Author: " + result.getString("book_author") + "\n" +
                        "Book Year: " + result.getInt("publish_year") + "\n" +
                        "Book Quantity: " + result.getInt("book_quantity") + "\n" +
                        "Book Genre: " + result.getString("book_genre") + "\n";

                JOptionPane.showMessageDialog(null, info2);
            } else {
                JOptionPane.showMessageDialog(null, "Book with ID " + btitle + " not found !!!");
            }

            connectdb.close();



        }catch (Exception error) {
            JOptionPane.showMessageDialog(null, "Error viewing books !!! " + error.getMessage());
        }
    }

    public static void deleteBook() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Enter Book ID that you want to delete:"));
            Connection connectdb = Database.connect();

            String checkSql = "SELECT * FROM BOOK WHERE book_id = ?";
            PreparedStatement checkstate = connectdb.prepareStatement(checkSql);
            checkstate.setInt(1, id);
            ResultSet result = checkstate.executeQuery();

            if (result.next()) {
                int confirm = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to delete book ID:" + id + " ?",
                        "Confirmation", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    String deleteSql = "DELETE FROM BOOK WHERE book_id = ?";
                    PreparedStatement deletestate = connectdb.prepareStatement(deleteSql);
                    deletestate.setInt(1, id);
                    deletestate.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Book deleted successfully !!!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Book with ID " + id + " not found.");
            }

            connectdb.close();

        } catch (Exception error) {
            JOptionPane.showMessageDialog(null, "Error deleting book !!! "+ error.getMessage());
        }
    }

    public static void updateBook() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Enter Book ID to update:"));

            String nwtitle = JOptionPane.showInputDialog("Enter new title:");
            String nwauthor = JOptionPane.showInputDialog("Enter new author:");
            int nwyear = Integer.parseInt(JOptionPane.showInputDialog("Enter new year:"));
            int nwquantity = Integer.parseInt(JOptionPane.showInputDialog("Enter new quantity"));
            String nwgenre = JOptionPane.showInputDialog("Enter new genre:");

            // Connect to DB
            Connection connectdb = Database.connect();

            String mysql = "UPDATE BOOK SET book_title = ?, book_author = ?, publish_year =?, " +
                    "book_quantity =? ,book_genre = ? WHERE book_id = ?";
            PreparedStatement state = connectdb.prepareStatement(mysql);
            state.setString(1, nwtitle);
            state.setString(2, nwauthor);
            state.setInt(3, nwyear);
            state.setInt(4, nwquantity);
            state.setString(5, nwgenre);
            state.setInt(6, id);


            int updaterow = state.executeUpdate(); // Return how many row update

            if (updaterow > 0) {
                JOptionPane.showMessageDialog(null, "Book updated successfully !!!");
            } else {
                JOptionPane.showMessageDialog(null, "Book ID which is " + id + "cannot be found !!!");
            }

            connectdb.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error updating book information: " + e.getMessage());
        }
    }


    public void showPage() {
        JFrame frame = new JFrame("Book Management System");
        frame.setSize(1000, 700);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout((new BorderLayout(10,10)));

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(viewBook(), BorderLayout.CENTER);

        // Buttons
        JButton addBtn = new JButton("Add Book");
        JButton deleteBtn = new JButton("Delete Book");
        JButton updateBtn = new JButton("Update Book");
        JButton searchBtn = new JButton("Search Book");

        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBackground(new Color(90, 93, 231));
        navPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        Dimension btnSize = new Dimension(180, 40);

        addBtn.setMaximumSize(btnSize);
        deleteBtn.setMaximumSize(btnSize);
        updateBtn.setMaximumSize(btnSize);
        searchBtn.setMaximumSize(btnSize);

        // Add to frame
        navPanel.add(Box.createVerticalStrut(20));
        navPanel.add(addBtn);
        navPanel.add(Box.createVerticalStrut(10));
        navPanel.add(deleteBtn);
        navPanel.add(Box.createVerticalStrut(10));
        navPanel.add(updateBtn);
        navPanel.add(Box.createVerticalStrut(10));
        navPanel.add(searchBtn);

        addBtn.addActionListener(e -> addBook());
        searchBtn.addActionListener(e-> searchBook());
        deleteBtn.addActionListener(e-> deleteBook());
        updateBtn.addActionListener(e-> updateBook());

        frame.add(navPanel, BorderLayout.WEST);
        frame.add(contentPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

}