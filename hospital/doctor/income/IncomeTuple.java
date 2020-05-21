package hospital.doctor.income;

import hospital.registration.RegistTuple;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Date;

public class IncomeTuple {
    private String KSMC;
    private String YSBH;
    private String YSMC;
    private boolean SFZJ;
    private int GHRC;
    private double income;

    public IncomeTuple(String KSMC, String YSBH, String YSMC, boolean SFZJ, int GHRC, double income) {
        this.KSMC = KSMC;
        this.YSBH = YSBH;
        this.YSMC = YSMC;
        this.SFZJ = SFZJ;
        this.GHRC = GHRC;
        this.income = income;
    }

    public static void configTable(TableView<IncomeTuple> tableView){
        TableColumn<IncomeTuple, String> tc_KSMC = new TableColumn<>("科室名称");
        tc_KSMC.setCellValueFactory(new PropertyValueFactory<IncomeTuple, String>("KSMC"));
        TableColumn<IncomeTuple, String> tc_YSBH = new TableColumn<>("医生编号");
        tc_YSBH.setCellValueFactory(new PropertyValueFactory<IncomeTuple, String>("YSBH"));
        TableColumn<IncomeTuple, String> tc_YSMC = new TableColumn<>("医生名称");
        tc_YSMC.setCellValueFactory(new PropertyValueFactory<IncomeTuple, String>("YSMC"));
        TableColumn<IncomeTuple, String> tc_SFZJ = new TableColumn<>("号种类别");
        tc_SFZJ.setCellValueFactory(new PropertyValueFactory<IncomeTuple, String>("SFZJ"));
        TableColumn<IncomeTuple, Integer> tc_GHRC = new TableColumn<>("挂号人次");
        tc_GHRC.setCellValueFactory(new PropertyValueFactory<IncomeTuple, Integer>("GHRC"));
        TableColumn<IncomeTuple, Double> tc_income = new TableColumn<>("收入合计");
        tc_income.setCellValueFactory(new PropertyValueFactory<IncomeTuple, Double>("income"));
        tableView.getColumns().setAll(tc_KSMC, tc_YSBH, tc_YSMC, tc_SFZJ, tc_GHRC, tc_income);
    }

    public String getKSMC() {
        return KSMC;
    }

    public String getYSBH() {
        return YSBH;
    }

    public String getYSMC() {
        return YSMC;
    }

    public boolean isSFZJ() {
        return SFZJ;
    }

    public String getSFZJ() {
        return (SFZJ ? "专家" : "普通");
    }

    public int getGHRC() {
        return GHRC;
    }

    public double getIncome() {
        return income;
    }
}
