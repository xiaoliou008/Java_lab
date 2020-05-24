package hospital.registration;

import hospital.shared.PYZSAccessible;

/**
 * 号种类型类
 */
public class HaoZhongType implements PYZSAccessible {

    private boolean isExpert = false;

    public HaoZhongType(String s){
        if(s.equals("专家")) isExpert = true;
    }

    @Override
    public String getPYZS() {
        return isExpert ? "zj" : "pt";
    }

    @Override
    public String getName() {
        return isExpert ? "专家" : "普通";
    }

    public boolean getSFZJ(){
        return isExpert;
    }

    @Override
    public String toString() {
        return getName();
    }
}
