package hospital.update;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateGHXX {
    private Connection conn;
    private String sql_max = "SELECT MAX(GHBH) FROM T_GHXX";
    private String sql_insert = "INSERT INTO T_GHXX VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
    private PreparedStatement getMax;
    private PreparedStatement insert;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");

    public UpdateGHXX(Connection connection) throws SQLException {
        conn = connection;
        getMax = conn.prepareStatement(sql_max);
        insert = conn.prepareStatement(sql_insert);
    }

    public String update(String HZBH, String YSBH, String BRBH, double GHFY, Date RQSJ) throws SQLException {
        ResultSet resMax = getMax.executeQuery();
        int maxID = 0;
        if (resMax.next() && resMax.getString(1) != null) {
            maxID = Integer.parseInt(resMax.getString(1));
            System.out.println(resMax.getString(1));        // 编号从1开始
        } else maxID = 0;
        String nextID = String.format("%06d", maxID + 1);
        insert.setString(1, nextID);
        insert.setString(2, HZBH);
        insert.setString(3, YSBH);
        insert.setString(4, BRBH);
        insert.setInt(5, 1);
        insert.setBoolean(6, false);
        insert.setDouble(7, GHFY);
        insert.setString(8, sdf.format(RQSJ));
        insert.execute();
        System.out.println(sdf.format(RQSJ));
        return nextID;
    }
}
