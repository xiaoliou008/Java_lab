package hospital.doctor;

import hospital.shared.PYZSAccessible;

import java.sql.Date;

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
}
