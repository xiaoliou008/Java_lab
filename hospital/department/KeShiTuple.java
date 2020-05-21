package hospital.department;

import hospital.shared.PYZSAccessible;

public class KeShiTuple implements PYZSAccessible {
    private String KSBH;
    private String KSMC;
    private String PYZS;

    public String getKSBH() {
        return KSBH;
    }

    public KeShiTuple(String id, String name, String py){
        KSBH = id;
        KSMC = name;
        PYZS = py;
    }

    @Override
    public String getPYZS() {
        return PYZS;
    }

    @Override
    public String getName() {
        return KSMC;
    }

    @Override
    public String toString() {
        return KSBH + "-" + KSMC + "-" + PYZS;
    }
}
