import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author sqlitetutorial.net
 */
public class SelectFromDB {

    public List<String> selectName(String name) {

        String url = "jdbc:sqlite:C://testDB.sqlite3";
        Connection conn = null;
        ResultSet rs = null;
        List<String> result = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
        String sql = "SELECT c_name, c_mobile FROM persons WHERE c_name='" + name +"';";

        try {


            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                result.add(rs.getString("c_name"));
                result.add(rs.getString("c_mobile"));
            }
        } catch (SQLException f) {
            System.out.println(f.getMessage());
        }

        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }


}