import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    //final is constant- means the value cannot be changed
    //private show that only member management class can access it
    // Url store the address of my MySQL
    // user and pass is create by me
    //jdbc:mysql: host/port/ name of database
    //use in DriveManager.getConnection()
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/member_schema";
    private static final String User = "root";
    private static final String Password = "1234";


    //connect java to mysql by using JDBC
    //use public so other function still call it
    //catch - if something got wrong jump here
    //printStackTrace() - print out the error details in the console for easy debug

    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL, User, Password);
        } catch (SQLException error) {
            JOptionPane.showMessageDialog(null, "Database Connection Failed!");
            error.printStackTrace();
            return null;
        }
    }
}
