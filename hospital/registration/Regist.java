package hospital.registration;

import hospital.doctor.YiShengTuple;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Regist {
    private Connection conn;
    private String sql =    "SELECT GHBH, BRMC, RQSJ, SFZJ " +
                            "FROM T_GHXX, T_HZXX, T_BRXX " +
                            "WHERE YSBH=? AND T_GHXX.HZBH=T_HZXX.HZBH AND T_GHXX.BRBH=T_BRXX.BRBH";
    private PreparedStatement preparedStatement;

    public Regist(Connection connection) throws SQLException {
        conn = connection;
        preparedStatement = conn.prepareStatement(sql);
    }

    public List<RegistTuple> getRelation(YiShengTuple doctor) throws SQLException {
        List<RegistTuple> list = new ArrayList<>();
        preparedStatement.setString(1, doctor.getYSBH());
        ResultSet res = preparedStatement.executeQuery();
        while (res.next()) {
            list.add(new RegistTuple(
                    res.getString("GHBH"),
                    res.getString("BRMC"),
                    res.getDate("RQSJ"),
                    res.getBoolean("SFZJ")));
        }
        return list;
    }
}