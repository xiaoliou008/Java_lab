package hospital.doctor;

import hospital.Controller;
import hospital.Main;
import hospital.doctor.income.Income;
import hospital.doctor.income.IncomeTuple;
import hospital.mysql.ConnectionFactory;
import hospital.registration.Regist;
import hospital.registration.RegistTuple;
import hospital.shared.ExitAlert;
import hospital.shared.HelpAlert;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.WindowEvent;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static java.lang.System.exit;
import static javafx.stage.WindowEvent.WINDOW_CLOSE_REQUEST;

public class DoctorController extends Controller {
    @FXML public Pane paneIncome;
    @FXML public TableView tableIncome;
    @FXML public Pane panePatient;
    @FXML public TableView tablePatient;
    @FXML public DatePicker DatePickerBegin;
    @FXML public DatePicker DatePickerEnd;
    @FXML public Text textTitle;
    private Main myApp;
    private YiShengTuple doctor;
    private Regist register;
    private List<RegistTuple> listRegistTuple;
    private Income incomeGetter;
    private List<IncomeTuple> incomeTupleList;

    public void setDoctor(YiShengTuple doctor) {
        this.doctor = doctor;
    }

    @Override
    public void setMyApp(Main myApp) throws SQLException {
        this.myApp = myApp;
        register = new Regist(myApp.conn);
        incomeGetter = new Income(myApp.conn);
        DatePickerBegin.setValue(LocalDate.now());
        DatePickerEnd.setValue(LocalDate.now());
    }

    /**
     * 配置表格
     * @throws SQLException
     */
    public void configTable() throws SQLException {
        paneIncome.setVisible(false);
        panePatient.setVisible(true);
        RegistTuple.configTable(tablePatient);
        IncomeTuple.configTable(tableIncome);
        String today = ConnectionFactory.getSQLTime(myApp.conn).split(" ")[0];
        listRegistTuple = register.getRelation(doctor, today, today);
        tablePatient.getItems().setAll(listRegistTuple);
        // debug
//        tableIncome.getItems().add(new IncomeTuple("nk", "bq", "bq", true, 10, 5.5));
        incomeTupleList = incomeGetter.getRelation(doctor);
        tableIncome.getItems().setAll(incomeTupleList);
    }

    /**
     * 选择病人
     * @param actionEvent
     */
    public void btnPatientHandler(ActionEvent actionEvent) {
        textTitle.setText("病人列表");
        panePatient.setVisible(true);
        paneIncome.setVisible(false);
    }

    /**
     * 选择收入
     * @param actionEvent
     */
    public void btnIncomeHandler(ActionEvent actionEvent) {
        textTitle.setText("收入列表");
        panePatient.setVisible(false);
        paneIncome.setVisible(true);
    }

    /**
     * 退出按钮
     * @param actionEvent
     */
    public void btnExitHandler(ActionEvent actionEvent) {
        ExitAlert.show(myApp);
    }

    /**
     * 选择起始日期
     * @param actionEvent
     * @throws SQLException
     */
    public void datePicketBeginHandler(ActionEvent actionEvent) throws SQLException {
        System.out.println(DatePickerBegin.getValue());
        listRegistTuple = register.getRelation(
                doctor, DatePickerBegin.getValue().toString(), DatePickerEnd.getValue().toString());
        tablePatient.getItems().setAll(listRegistTuple);
        incomeTupleList = incomeGetter.getRelation(
                doctor, DatePickerBegin.getValue().toString(), DatePickerEnd.getValue().toString());
        tableIncome.getItems().setAll(incomeTupleList);
    }

    /**
     * 选择结束日期
     * @param actionEvent
     * @throws SQLException
     */
    public void datePicketEndHandler(ActionEvent actionEvent) throws SQLException {
        System.out.println(DatePickerEnd.getValue());
        datePicketBeginHandler(actionEvent);
    }

    /**
     * 菜单栏初始化选项
     * @param actionEvent
     * @throws SQLException
     */
    public void menuInitHandler(ActionEvent actionEvent) throws SQLException {
        DatePickerBegin.setValue(LocalDate.now());
        DatePickerEnd.setValue(LocalDate.now());
        configTable();
    }

    /**
     * 菜单栏帮助
     * @param actionEvent
     */
    public void helperHandler(ActionEvent actionEvent) {
        HelpAlert.show(myApp);
    }
}
