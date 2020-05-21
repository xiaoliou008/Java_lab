package hospital.doctor;

import hospital.Controller;
import hospital.Main;
import hospital.doctor.income.Income;
import hospital.doctor.income.IncomeTuple;
import hospital.registration.Regist;
import hospital.registration.RegistTuple;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.stage.WindowEvent;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import static java.lang.System.exit;
import static javafx.stage.WindowEvent.WINDOW_CLOSE_REQUEST;

public class DoctorController extends Controller {
    @FXML public Pane paneIncome;
    @FXML public TableView tableIncome;
    @FXML public Pane panePatient;
    @FXML public TableView tablePatient;
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
    }

    public void configTable() throws SQLException {
        paneIncome.setVisible(false);
        panePatient.setVisible(true);
        RegistTuple.configTable(tablePatient);
        IncomeTuple.configTable(tableIncome);
        listRegistTuple = register.getRelation(doctor);
        tablePatient.getItems().setAll(listRegistTuple);
        // debug
//        tableIncome.getItems().add(new IncomeTuple("nk", "bq", "bq", true, 10, 5.5));
        incomeTupleList = incomeGetter.getRelation(doctor);
        tableIncome.getItems().setAll(incomeTupleList);
    }

    public void bntPatientHandler(ActionEvent actionEvent) {
        panePatient.setVisible(true);
        paneIncome.setVisible(false);
    }

    public void btnIncomeHandler(ActionEvent actionEvent) {
        panePatient.setVisible(false);
        paneIncome.setVisible(true);
    }

    public void btnExitHandler(ActionEvent actionEvent) {
        WindowEvent.fireEvent(myApp.getStage(), new WindowEvent(myApp.getStage(), WINDOW_CLOSE_REQUEST));
        exit(0);
    }
}
