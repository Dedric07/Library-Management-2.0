
import java.sql.*;
import java.sql.SQLException;

public class LoginInterface {
    public static String[] LoginUser(String name, String password) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/member_schema", "root", "1234")) {
            String sql = "SELECT 'admin' as user_type, admin_name, admin_password FROM ADMIN WHERE admin_name = ? AND admin_password = ? " +
                    "UNION ALL " +
                    "SELECT 'member' as user_type, member_name, member_password FROM member WHERE member_name = ? AND member_password = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, password);
            statement.setString(3, name);
            statement.setString(4, password);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new String[]{rs.getString("user_type"), rs.getString("admin_name")};
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    public static void main(String[] args) {
        MyFrame frame = new MyFrame();
    }
}

