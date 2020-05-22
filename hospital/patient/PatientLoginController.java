package hospital.patient;

import hospital.Controller;
import hospital.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.SQLException;

public class PatientLoginController extends Controller {
    @FXML private TextField textPatientID;
    @FXML private PasswordField passwordPatient;
    @FXML private Text textPatientPrompt;
    private PatientLogin patientLogin;

    @Override
    public void setMyApp(Main myApp) throws SQLException {
        this.myApp = myApp;
        textPatientPrompt.setText("\n请输入编号和密码");
        patientLogin = new PatientLogin(myApp.conn);
    }


    public void patientLoginBtnOn(ActionEvent e) throws IOException, SQLException {
        PatientTuple patient =
                patientLogin.login(textPatientID.getCharacters().toString(), passwordPatient.getCharacters().toString());
        if(patient != null){
            patient.updateLogin(myApp.conn);
            patientLogin.close();
            myApp.changeScene(patient);
        } else {
            textPatientPrompt.setText("\n编号或密码错误！");        // 不加\n的话，显示会有问题
        }
    }
}
