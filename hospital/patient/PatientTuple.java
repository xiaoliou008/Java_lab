package hospital.patient;

import com.mysql.cj.protocol.Resultset;
import hospital.mysql.ConnectionFactory;

import java.sql.*;

public class PatientTuple {
    private String BRBH;
    private String BRMC;
    private String DLKL;
    private double YCJE;
    private Date DLRQ;

    public PatientTuple(String BRBH, String BRMC, String DLKL, double YCJE, Date DLRQ){
        this.BRBH = BRBH;
        this.BRMC = BRMC;
        this.DLKL = DLKL;
        this.YCJE = YCJE;
        this.DLRQ = DLRQ;
    }

    public String getBRBH() {
        return BRBH;
    }

    public String getBRMC() {
        return BRMC;
    }

    public double getYCJE() {
        return YCJE;
    }

    public Date getDLRQ() {
        return DLRQ;
    }

    /**
     * 获取病人的余额
     * @return
     */
    public double getYE(Connection conn) throws SQLException {
        String sql = "SELECT YCJE FROM T_BRXX WHERE BRBH=" + BRBH;
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        ResultSet res = preparedStatement.executeQuery();
        if(res.next()){
            return res.getDouble("YCJE");
        } else return 0.0;
    }

    /**
     * 设置病人的余额
     * @param conn
     * @param setYCJE
     * @throws SQLException
     */
    public void setYE(Connection conn, double setYCJE) throws SQLException {
        String sql = "UPDATE T_BRXX SET YCJE=" + setYCJE + " WHERE BRBH=" + BRBH;
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.executeUpdate();
    }

    /**
     * 更新病人的登陆时间
     * @param conn
     */
    public void updateLogin(Connection conn) throws SQLException {
        String now = ConnectionFactory.getSQLTime(conn);
        String sql = "UPDATE T_BRXX\n" +
                    "SET DLRQ = '" + now +
                    "'\nWHERE BRBH = " + BRBH;
        System.out.println(sql);
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.executeUpdate();
    }
}
