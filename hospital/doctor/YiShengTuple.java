package hospital.doctor;

import hospital.mysql.ConnectionFactory;
import hospital.shared.PYZSAccessible;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 医生
 */
public class YiShengTuple implements PYZSAccessible {

    private String YSBH;
    private String KSBH;
    private String YSMC;
    private String PYZS;
    private String DLKL;
    private Boolean SFZJ;
    private Date DLRQ;

    public String getYSBH() {
        return YSBH;
    }

    public String getKSBH() {
        return KSBH;
    }

    public String getYSMC() {
        return YSMC;
    }

    public String getDLKL() {
        return DLKL;
    }

    public Boolean getSFZJ() {
        return SFZJ;
    }

    public Date getDLRQ() {
        return DLRQ;
    }

    public YiShengTuple(String YSBH, String KSBH, String YSMC, String PYZS, String DLKL, Boolean SFZJ, Date DLRQ) {
        this.YSBH = YSBH;
        this.KSBH = KSBH;
        this.YSMC = YSMC;
        this.PYZS = PYZS;
        this.DLKL = DLKL;
        this.SFZJ = SFZJ;
        this.DLRQ = DLRQ;
    }

    @Override
    public String getPYZS() {
        return PYZS;
    }

    @Override
    public String getName() {
        return YSMC;
    }

    @Override
    public String toString() {
        return YSMC + "-" + PYZS + "-" + (SFZJ ? "专家" : "普通");
    }

    /**
     * 更新医生的登陆时间
     * @param conn
     */
    public void updateLogin(Connection conn) throws SQLException {
        String now = ConnectionFactory.getSQLTime(conn);
        String sql = "UPDATE T_KSYS\n" +
                "SET DLRQ = '" + now +
                "'\nWHERE YSBH = " + YSBH;
        System.out.println(sql);
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.executeUpdate();
    }
}
