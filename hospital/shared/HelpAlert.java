package hospital.shared;

import hospital.Main;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.WindowEvent;

import static java.lang.System.exit;
import static javafx.stage.WindowEvent.WINDOW_CLOSE_REQUEST;

public class HelpAlert {
    public static void show(Main myApp){
        Alert alert = new Alert(
                Alert.AlertType.INFORMATION, "制作人：刘静平\n班级：ACM1701\n学号：U201714624");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                alert.close();
            }
        });
    }

    public static void show(Main myApp, String s){
        Alert alert = new Alert(
                Alert.AlertType.INFORMATION, s);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                alert.close();
            }
        });
    }
}
