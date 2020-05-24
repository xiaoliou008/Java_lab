package hospital.doctor;

import hospital.department.KeShiTuple;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取医生的列表
 */
public class YiSheng {
    private Connection conn;
    private String sql = "SELECT * FROM T_KSYS";
    private PreparedStatement preparedStatement;

    public YiSheng(Connection connection) throws SQLException {
        conn = connection;
        preparedStatement = conn.prepareStatement(sql);
    }

    public List<YiShengTuple> getRelation() throws SQLException {
        List<YiShengTuple> list = new ArrayList<>();
        ResultSet res = preparedStatement.executeQuery();
        while (res.next()) {
            list.add(new YiShengTuple(
                res.getString("YSBH"),
                res.getString("KSBH"),
                res.getString("YSMC"),
                res.getString("PYZS"),
                res.getString("DLKL"),
                res.getBoolean("SFZJ"),
                res.getDate("DLRQ")));
        }
        return list;
    }
}
