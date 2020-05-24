package hospital.doctor;

import java.sql.*;

public class DoctorLogin {
    private Connection conn;
    private String sql = "SELECT * FROM T_KSYS WHERE YSBH=? AND DLKL=?";
    private PreparedStatement preparedStatement;

    public DoctorLogin(Connection connection) throws SQLException {
        conn = connection;
        preparedStatement = conn.prepareStatement(sql);
    }

    /**
     * 检查编号和密码是否正确
     * @param ID
     * @param passwd
     * @return
     * @throws SQLException
     */
    public YiShengTuple login(String ID, String passwd) throws SQLException {
        if (ID != null && passwd != null) {
            preparedStatement.setString(1, ID);
            preparedStatement.setString(2, passwd);
            ResultSet res = preparedStatement.executeQuery();
            if (res.next()) {
                return new YiShengTuple(
                        res.getString("YSBH"),
                        res.getString("KSBH"),
                        res.getString("YSMC"),
                        res.getString("PYZS"),
                        res.getString("DLKL"),
                        res.getBoolean("SFZJ"),
                        res.getDate("DLRQ")
                );
            } else return null;
        }
        return null;
    }

    public void close() throws SQLException {
        preparedStatement.close();
//        conn.close();
    }
}
