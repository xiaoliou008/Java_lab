package hospital.department;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KeShi {
    private Connection conn;
    private String sql = "SELECT * FROM T_KSXX";
    private PreparedStatement preparedStatement;

    public KeShi(Connection connection) throws SQLException {
        conn = connection;
        preparedStatement = conn.prepareStatement(sql);
    }

    public List<KeShiTuple> getRelation() throws SQLException {
        List<KeShiTuple> list = new ArrayList<>();
        ResultSet res = preparedStatement.executeQuery();
        while(res.next()){
            list.add(new KeShiTuple(res.getString("KSBH"), res.getString("KSMC"), res.getString("PYZS")));
        }
        return list;
    }
}
