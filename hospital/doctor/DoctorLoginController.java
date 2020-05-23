package hospital.doctor;

import hospital.Controller;
import hospital.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.SQLException;

public class DoctorLoginController extends Controller {
    @FXML public TextField textDoctorID;
    @FXML public PasswordField passwordDoctor;
    @FXML public Text textDoctorPrompt;
    private DoctorLogin doctorLogin;

    @Override
    public void setMyApp(Main myApp) throws SQLException {
        this.myApp = myApp;
        textDoctorPrompt.setText("\n请输入编号和密码");
        doctorLogin = new DoctorLogin(myApp.conn);
    }

    public void doctorLoginBtnOn(ActionEvent actionEvent) throws SQLException, IOException {
        YiShengTuple doctor =
                doctorLogin.login(textDoctorID.getCharacters().toString(), passwordDoctor.getCharacters().toString());
        if(doctor != null){
            doctor.updateLogin(myApp.conn);     // 更新登陆日期
            doctorLogin.close();
            myApp.changeScene(doctor);
        } else {
            textDoctorPrompt.setText("\n编号或密码错误！");        // 不加\n的话，显示会有问题
        }
    }
}
