package hospital;

import javafx.event.ActionEvent;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController extends Controller{

    /**
     * 设置myApp
     * @param myApp
     */
    public void setMyApp(Main myApp){
        this.myApp = myApp;
    }

    /**
     * 病人登陆
     * @param actionEvent
     * @throws IOException
     * @throws SQLException
     */
    public void patientLoginBtnHandler(ActionEvent actionEvent) throws IOException, SQLException {
        myApp.changeScene("patient/patientLogin.fxml", "病人登陆");
    }

    /**
     * 医生登陆
     * @param actionEvent
     * @throws IOException
     * @throws SQLException
     */
    public void doctorLoginBtnHandler(ActionEvent actionEvent) throws IOException, SQLException {
        myApp.changeScene("doctor/doctorLogin.fxml", "医生登陆");
    }
}
