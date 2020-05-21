package hospital.registration;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Date;

public class RegistTuple {
    private String ID;
    private String patientName;
    private Date date;
    private boolean isExpert;

    public RegistTuple(String ID, String patientName, Date date, boolean isExpert) {
        this.ID = ID;
        this.patientName = patientName;
        this.date = date;
        this.isExpert = isExpert;
    }

    public static void configTable(TableView<RegistTuple> tableView){
        TableColumn<RegistTuple, String> tc_id = new TableColumn<>("挂号编号");
        tc_id.setCellValueFactory(new PropertyValueFactory<RegistTuple, String>("ID"));
        TableColumn<RegistTuple, String> tc_name = new TableColumn<>("病人名称");
        tc_name.setCellValueFactory(new PropertyValueFactory<RegistTuple, String>("patientName"));
        TableColumn<RegistTuple, Date> tc_date = new TableColumn<>("挂号日期时间");
        tc_date.setCellValueFactory(new PropertyValueFactory<RegistTuple, Date>("date"));
        TableColumn<RegistTuple, String> tc_expert = new TableColumn<>("号种类别");
        tc_expert.setCellValueFactory(new PropertyValueFactory<RegistTuple, String>("isExpert"));
        tableView.getColumns().setAll(tc_id, tc_name, tc_date, tc_expert);
    }

    public String getID() {
        return ID;
    }

    public String getPatientName() {
        return patientName;
    }

    public Date getDate() {
        return date;
    }

    public boolean isExpert() {
        return isExpert;
    }

    public String getIsExpert() {
        return isExpert ? "专家" : "普通";
    }       // PropertyValueFactory使用反射机制，调用对应属性的get方法
    // 对于boolean类型，自动创建的get方式命名为isXXX，不符合格式要求，所以手动创建一个get方法
}
