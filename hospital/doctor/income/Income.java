package hospital.doctor.income;

import hospital.doctor.YiSheng;
import hospital.doctor.YiShengTuple;
import hospital.mysql.ConnectionFactory;
import hospital.registration.RegistTuple;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Income {
    private Connection conn;
    private String sql = "SELECT KSMC, T_GHXX.YSBH, YSMC, T_HZXX.SFZJ , SUM(GHRC) AS GHRC , SUM(T_GHXX.GHFY) AS SRHJ\n" +
            "FROM T_GHXX, T_KSXX, T_KSYS, T_HZXX\n" +
            "WHERE T_GHXX.HZBH = T_HZXX.HZBH AND\n" +
            "T_HZXX.KSBH = T_KSXX.KSBH AND\n" +
            "T_GHXX.YSBH = T_KSYS.YSBH AND\n" +
            "T_GHXX.THBZ = 0 AND\n" +
            "T_GHXX.RQSJ BETWEEN ? AND ?" +
            "GROUP BY KSMC, T_GHXX.YSBH, YSMC, T_HZXX.SFZJ";

    private PreparedStatement preparedStatement;

    public Income(Connection connection) throws SQLException {
        conn = connection;
        preparedStatement = conn.prepareStatement(sql);
    }

    public List<IncomeTuple> getRelation(YiShengTuple doctor) throws SQLException {
        List<IncomeTuple> list = new ArrayList<>();
        String today = ConnectionFactory.getSQLTime(conn).split(" ")[0];    // 取日期部分
        preparedStatement.setString(1, today + " 00:00:00");       // 不需要额外加单引号
        preparedStatement.setString(2, today + " 23:59:59");
        ResultSet res = preparedStatement.executeQuery();
        while (res.next()) {
            list.add(new IncomeTuple(
                    res.getString("KSMC"),
                    res.getString("YSBH"),
                    res.getString("YSMC"),
                    res.getBoolean("SFZJ"),
                    res.getInt("GHRC"),
                    res.getDouble("SRHJ")
            ));
        }
        return list;
    }

    public List<IncomeTuple> getRelation(YiShengTuple doctor, String begin, String end) throws SQLException {
        List<IncomeTuple> list = new ArrayList<>();
        for(IncomeTuple t : getRelation(doctor)){

        }
        return list;
    }
}