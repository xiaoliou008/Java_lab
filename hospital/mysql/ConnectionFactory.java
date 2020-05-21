package hospital.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    public static Connection create(String url, String root, String pwd){
        Connection conn = null;
        System.out.println("正在连接数据库...");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url,"root","root");
            if(conn != null) {
                System.out.println("成功连接到数据库。");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("数据库驱动加载错误，" + e.toString());
        } catch (SQLException e) {
            System.out.println("数据库连接错误，" + e.toString());
        }
        return conn;
    }

}
