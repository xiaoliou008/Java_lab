package hospital.patient;

import java.sql.*;

public class PatientLogin {
    private Connection conn;
    private String sql = "SELECT * FROM T_BRXX WHERE BRBH=? AND DLKL=?";
    private PreparedStatement preparedStatement;

    public PatientLogin(Connection connection) throws SQLException {
        conn = connection;
        preparedStatement = conn.prepareStatement(sql);
    }

    public PatientTuple login(String ID, String passwd) throws SQLException {
        if (ID != null && passwd != null) {
            preparedStatement.setString(1, ID);
            preparedStatement.setString(2, passwd);
            ResultSet res = preparedStatement.executeQuery();
            if (res.next()) {       // 登陆成功
                return new PatientTuple(
                        res.getString("BRBH"),
                        res.getString("BRMC"),
                        res.getString("DLKL"),
                        res.getDouble("YCJE"),
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
