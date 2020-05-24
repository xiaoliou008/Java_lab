package hospital.registration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HaoZhong {
    private Connection conn;
    private String sql = "SELECT * FROM T_HZXX";
    private PreparedStatement preparedStatement;

    public HaoZhong(Connection connection) throws SQLException {
        conn = connection;
        preparedStatement = conn.prepareStatement(sql);
    }

    /**
     * 获取所有的号种
     * @return
     * @throws SQLException
     */
    public List<HaoZhongTuple> getRelation() throws SQLException {
        List<HaoZhongTuple> list = new ArrayList<>();
        ResultSet res = preparedStatement.executeQuery();
        while (res.next()) {
            list.add(new HaoZhongTuple(
                    res.getString("HZBH"),
                    res.getString("HZMC"),
                    res.getString("PYZS"),
                    res.getString("KSBH"),
                    res.getBoolean("SFZJ"),
                    res.getInt("GHRS"),
                    res.getDouble("GHFY")));
        }
        return list;
    }
}
