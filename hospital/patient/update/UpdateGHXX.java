package hospital.patient.update;

import hospital.mysql.ConnectionFactory;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateGHXX {
    private Connection conn;
    private String sql_max = "SELECT MAX(GHBH) FROM T_GHXX";
    private String sql_insert = "INSERT INTO T_GHXX VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
    private String sql_getNum = "SELECT COUNT(*) FROM T_GHXX WHERE HZBH=? AND THBZ=0";
    private String sql_cancel = "UPDATE T_GHXX SET THBZ = 1 WHERE GHBH = ?";
    private PreparedStatement getMax;
    private PreparedStatement insert;
    private PreparedStatement count;
    private PreparedStatement cancelStat;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");

    public UpdateGHXX(Connection connection) throws SQLException {
        conn = connection;
        getMax = conn.prepareStatement(sql_max);
        insert = conn.prepareStatement(sql_insert);
        count = conn.prepareStatement(sql_getNum);
        cancelStat = conn.prepareStatement(sql_cancel);
    }

    /**
     * 插入挂号信息（和数据库同步日期）
     * 避免使用锁，而为了保证编号的连续
     * 使用了数据库的完整性约束（码不能重复）
     * 当重复插入时，必然会引发异常
     * 捕获异常后重新插入，即可避免冲突
     * @param HZBH
     * @param YSBH
     * @param BRBH
     * @param GHFY
     * @return
     * @throws SQLException
     */
    public String update(String HZBH, String YSBH, String BRBH, double GHFY) throws SQLException {
        String nextID = null;
        String RQSJ = ConnectionFactory.getSQLTime(conn);       // 与数据库同步时间
        while(true) {       // 如果插入不成功，就反复尝试插入
            try {
                ResultSet resMax = getMax.executeQuery();       // 每次都先查询得到编号最大的值
                int maxID = 0;
                if (resMax.next() && resMax.getString(1) != null) {
                    maxID = Integer.parseInt(resMax.getString(1));
                    System.out.println(resMax.getString(1));        // 编号从1开始
                } else maxID = 0;
                nextID = String.format("%06d", maxID + 1);
                // 获取挂号人次
                int num = getNum(HZBH);
                if(num < 0) return "full";  // 返回full表示人数已满
                insert.setString(1, nextID);
                insert.setString(2, HZBH);
                insert.setString(3, YSBH);
                insert.setString(4, BRBH);
                insert.setInt(5, num);
                insert.setBoolean(6, false);
                insert.setDouble(7, GHFY);
                insert.setString(8, RQSJ);
                insert.execute();
                break;  //执行成功就跳出循环
            } catch (SQLException e) {
                System.out.println("更新挂号表异常");
                continue;   // 插入失败就继续循环
            }
        }
        return nextID;
    }

    /**
     * 插入挂号信息（给出日期）
     * 避免使用锁，而为了保证编号的连续
     * 使用了数据库的完整性约束（码不能重复）
     * 当重复插入时，必然会引发异常
     * 捕获异常后重新插入，即可避免冲突
     * @param HZBH
     * @param YSBH
     * @param BRBH
     * @param GHFY
     * @param RQSJ
     * @return
     * @throws SQLException
     */
    public String update(String HZBH, String YSBH, String BRBH, double GHFY, Date RQSJ) throws SQLException {
        String nextID = null;
        while(true) {       // 如果插入不成功，就反复尝试插入
            try {
                ResultSet resMax = getMax.executeQuery();       // 每次都先查询得到编号最大的值
                int maxID = 0;
                if (resMax.next() && resMax.getString(1) != null) {
                    maxID = Integer.parseInt(resMax.getString(1));
                    System.out.println(resMax.getString(1));        // 编号从1开始
                } else maxID = 0;
                nextID = String.format("%06d", maxID + 1);
                // 获取挂号人次
                int num = getNum(HZBH);
                if(num < 0) return "full";  // 返回full表示人数已满
                insert.setString(1, nextID);
                insert.setString(2, HZBH);
                insert.setString(3, YSBH);
                insert.setString(4, BRBH);
                insert.setInt(5, num);
                insert.setBoolean(6, false);
                insert.setDouble(7, GHFY);
                insert.setString(8, sdf.format(RQSJ));
                insert.execute();
                break;  //执行成功就跳出循环
            } catch (SQLException e) {
                System.out.println("更新挂号表异常");
                continue;   // 插入失败就继续循环
            }
        }
        System.out.println("挂号时间：" + sdf.format(RQSJ));
        return nextID;
    }

    /**
     * 在挂号信息表中查找某一个号种编号的个数
     * @param hzbh
     * @return
     */
    private int getNum(String hzbh) throws SQLException {
        count.setString(1, hzbh);
        ResultSet res = count.executeQuery();
        int num = Integer.MAX_VALUE;
        if(res.next()){     // 返回个数
            num = res.getInt(1);
        }
        String sql_rs = "SELECT GHRS\n" +
                        "FROM T_HZXX\n" +
                        "WHERE HZBH=" + hzbh;     // 退号的不算
        res = conn.prepareStatement(sql_rs).executeQuery();
        int rs = 0;
        if(res.next()){     // 这个不能少，next将游标下移一行
            rs = res.getInt(1);
        }
        if(num >= rs)
            return -1;      // -1表示人数已满
        else
            return num + 1; // +1表示当前病人的人次
    }

    public void cancel(String ghbh) throws SQLException {
        cancelStat.setString(1, ghbh);
        cancelStat.executeUpdate();
    }
}
