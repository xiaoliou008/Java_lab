package hospital.doctor.income;

import hospital.doctor.YiSheng;
import hospital.doctor.YiShengTuple;
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
            "T_GHXX.YSBH = T_KSYS.YSBH\n" +
            "GROUP BY KSMC, T_GHXX.YSBH, YSMC, T_HZXX.SFZJ";

    private PreparedStatement preparedStatement;

    public Income(Connection connection) throws SQLException {
        conn = connection;
        preparedStatement = conn.prepareStatement(sql);
    }

    public List<IncomeTuple> getRelation(YiShengTuple doctor) throws SQLException {
        List<IncomeTuple> list = new ArrayList<>();
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
}