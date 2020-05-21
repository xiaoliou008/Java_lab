package hospital.registration;

import hospital.shared.PYZSAccessible;

public class HaoZhongTuple implements PYZSAccessible {

    private String HZBH;
    private String HZMC;
    private String PYZS;
    private String KSBH;
    private boolean SFZJ;
    private int GHRS;
    private double GHFY;

    public String getHZBH() {
        return HZBH;
    }

    public int getGHRS() {
        return GHRS;
    }

    public double getGHFY() {
        return GHFY;
    }

    public HaoZhongTuple(String a, String b, String c, String d, boolean e, int f, double g) {
        HZBH = a;
        HZMC = b;
        PYZS = c;
        KSBH = d;
        SFZJ = e;
        GHRS = f;
        GHFY = g;
    }

    @Override
    public String getPYZS() {
        return PYZS;
    }

    @Override
    public String getName() {
        return HZMC;
    }

    public String getKSBH() {
        return KSBH;
    }

    public boolean isSFZJ() {
        return SFZJ;
    }

    @Override
    public String toString() {
        return HZMC + "-" + (SFZJ ? "专家" : "普通");
    }
}
