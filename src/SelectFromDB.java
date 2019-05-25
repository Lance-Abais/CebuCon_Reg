import javax.sound.midi.SysexMessage;
import javax.xml.transform.Result;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sqlitetutorial.net
 */
public class SelectFromDB {

    public List<String> selectName(String name) {

        String url = "jdbc:sqlite:C://testDB.sqlite3";
        Connection conn = null;
        List<String> result = new ArrayList<String>();
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
        String sql = "SELECT c_name, c_mobile FROM persons WHERE c_name='" + name +"';";
        System.out.println(sql);
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            result.add(rs.getString("c_name"));
            result.add(String.valueOf(rs.getInt("c_mobile")));
            for(int i=0; i<result.size(); i++) System.out.println(result.get(i));
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

    public List<String> selectNameProfession(String name, String profession) {

        String url = "jdbc:sqlite:C://testDB.sqlite3";
        Connection conn = null;
        List<String> result = new ArrayList<String>();
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
        String sql = "SELECT c_name, c_mobile FROM persons WHERE c_name='" + name +"' AND c_profession='"+profession+"';";
        System.out.println(sql);
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            result.add(rs.getString("c_name"));
            result.add(String.valueOf(rs.getInt("c_mobile")));
            for(int i=0; i<result.size(); i++) System.out.println(result.get(i));
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

    public void addPerson (String profession,String name, String company, String address, String email, String mobile, String[] purpose, String[] how){

    }

}