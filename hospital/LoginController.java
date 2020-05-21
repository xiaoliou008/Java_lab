package hospital;

import javafx.event.ActionEvent;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController extends Controller{

    public void setMyApp(Main myApp){
        this.myApp = myApp;
    }

    public void patientLoginBtnHandler(ActionEvent actionEvent) throws IOException, SQLException {
        myApp.changeScene("patient/patientLogin.fxml", "病人登陆");
    }

    public void doctorLoginBtnHandler(ActionEvent actionEvent) throws IOException, SQLException {
        myApp.changeScene("doctor/doctorLogin.fxml", "医生登陆");
    }
}
