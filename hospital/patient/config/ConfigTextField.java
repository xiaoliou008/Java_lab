package hospital.patient.config;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.util.regex.Pattern;

public class ConfigTextField {
    public static void config(TextField textField){         // 限制只能输入数字和至多一个小数点，最多两位小数
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(!Pattern.matches("[0-9]*(\\.[0-9]{0,2})?", t1)){
                    textField.setText(s);
                }
                System.out.println(t1);
            }
        });
    }
}
