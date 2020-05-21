package hospital.mysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Consult {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/第3章习题";
        Connection conn = ConnectionFactory.create(url,"root","root");
        if(conn != null){
            try{
                Statement sta = conn.createStatement();
                ResultSet rs = sta.executeQuery("SELECT * FROM SPJ WHERE SNO = 'S1'");
                while(rs.next()){
                    System.out.print(rs.getString("SNO") + '\t');
                    System.out.print(rs.getString("PNO") + '\t');
                    System.out.print(rs.getString("JNO") + '\t');
                    System.out.println(rs.getInt("QTY"));
                }
                rs.close();
                sta.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
