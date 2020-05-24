package hospital.shared;

import hospital.Main;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.WindowEvent;

import static java.lang.System.exit;
import static javafx.stage.WindowEvent.WINDOW_CLOSE_REQUEST;

/**
 * 生成退出提示对话框
 */
public class ExitAlert {
    public static void show(Main myApp){
        Alert alert = new Alert(Alert.AlertType.WARNING, "确定退出挂号系统？");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                WindowEvent.fireEvent(myApp.getStage(), new WindowEvent(myApp.getStage(), WINDOW_CLOSE_REQUEST));
                exit(0);
            }
        });
    }
}
