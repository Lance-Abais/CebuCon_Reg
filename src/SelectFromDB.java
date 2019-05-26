import javax.sound.midi.SysexMessage;
import javax.swing.plaf.nimbus.State;
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

        String url = "jdbc:sqlite:C://Temp//DB.sqlite3";
        Connection conn = null;
        List<String> result = new ArrayList<String>();
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
        String sql = "SELECT c_name, c_mobile, c_company FROM persons WHERE c_name='" + name +"';";
        System.out.println(sql);
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            result.add(rs.getString("c_name"));
            result.add(String.valueOf(rs.getInt("c_mobile")));
            result.add(rs.getString("c_company"));
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

        String url = "jdbc:sqlite:C://Temp//DB.sqlite3";
        Connection conn = null;
        List<String> result = new ArrayList<String>();
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
        String sql = "SELECT c_name, c_mobile, c_company FROM persons WHERE c_name='" + name +"' AND c_profession='"+profession+"';";
        System.out.println(sql);
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            result.add(rs.getString("c_name"));
            result.add(String.valueOf(rs.getInt("c_mobile")));
            result.add(rs.getString("c_company"));
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

    public void addPerson (String profession,String name, String designation, String company, String address, String email, String mobile, ArrayList<String> purpose, ArrayList<String> how){

        String url = "jdbc:sqlite:C://Temp//onsite.sqlite3";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
        String sql = "INSERT INTO people (c_profession, c_name, c_company, c_designation, c_email, c_mobile, c_address, c_purpose, c_how) VALUES ('";

        sql+=profession + "', '";
        sql+=name + "', '";
        sql+=company + "', '";
        sql+=designation + "', '";
        sql+=email + "', '";
        sql+=mobile + "', '";
        sql+=address + "', '";
        String purposeString = "";
        String howString = "";
        for(int i = 0; i <  purpose.size(); i++) purposeString+=purpose.get(i) + ",";
        purposeString = purposeString.substring(0, purposeString.length()-1);
        for(int i= 0; i < how.size(); i++) howString +=how.get(i) + ",";
        howString = howString.substring(0, howString.length()-1);
        sql+=purposeString + "', '";
        sql+=howString + "');";
        System.out.println(sql);

        Statement statement = null;
        try {
            statement = conn.createStatement();
            statement.execute(sql);
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}