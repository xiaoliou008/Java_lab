package hospital.patient;

import java.sql.Date;

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
}
