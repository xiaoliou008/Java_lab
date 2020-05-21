package hospital.patient;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class FxUtilTest {

    public interface AutoCompleteComparator<T> {
        boolean matches(String typedText, T objectToCompare);
    }

    public static<T> void autoCompleteComboBoxPlus(ComboBox<T> comboBox, AutoCompleteComparator<T> comparatorMethod) {
        ObservableList<T> data = comboBox.getItems();

        comboBox.setEditable(true);
        comboBox.getEditor().focusedProperty().addListener(observable -> {      // 如果没有选择任何一个项，则显示的文字为null
            if (comboBox.getSelectionModel().getSelectedIndex() < 0) {
                comboBox.getEditor().setText(null);
            }
        });
        comboBox.addEventHandler(KeyEvent.KEY_PRESSED, t -> comboBox.hide());   // 按下键时隐藏
        comboBox.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {      // 释放键时

            private boolean moveCaretToPos = false;
            private int caretPos;

            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.UP) {        // ↑
                    caretPos = -1;
                    if (comboBox.getEditor().getText() != null) {
                        moveCaret(comboBox.getEditor().getText().length());     // 移动到输入框的最右端
                    }
                    return;
                } else if (event.getCode() == KeyCode.DOWN) {       // ↓
                    if (!comboBox.isShowing()) {
                        comboBox.show();
                    }
                    caretPos = -1;
                    if (comboBox.getEditor().getText() != null) {
                        moveCaret(comboBox.getEditor().getText().length());     // 移动到输入框的最右端
                    }
                    return;
                } else if (event.getCode() == KeyCode.BACK_SPACE) {
                    if (comboBox.getEditor().getText() != null) {
                        moveCaretToPos = true;
                        caretPos = comboBox.getEditor().getCaretPosition();
                    }
                } else if (event.getCode() == KeyCode.DELETE) {
                    if (comboBox.getEditor().getText() != null) {
                        moveCaretToPos = true;
                        caretPos = comboBox.getEditor().getCaretPosition();
                    }
                } else if (event.getCode() == KeyCode.ENTER) {
                    return;
                }

                if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.LEFT || event.getCode().equals(KeyCode.SHIFT) || event.getCode().equals(KeyCode.CONTROL)
                        || event.isControlDown() || event.getCode() == KeyCode.HOME
                        || event.getCode() == KeyCode.END || event.getCode() == KeyCode.TAB) {
                    return;
                }

                ObservableList<T> list = FXCollections.observableArrayList();
                for (T aData : data) {
                    if (aData != null && comboBox.getEditor().getText() != null && comparatorMethod.matches(comboBox.getEditor().getText(), aData)) {
                        list.add(aData);
                    }
                }
                String t = "";
                if (comboBox.getEditor().getText() != null) {
                    t = comboBox.getEditor().getText();
                }

                comboBox.setItems(list);        // 执行完这个之后，很可能修改输入框的内容！所以下面紧接着执行setText
                comboBox.getEditor().setText(t);    // 执行了这个之后，会丢失光标的位置（光标位置清0），所以下面恢复
                if (!moveCaretToPos) {
                    caretPos = -1;
                }
                moveCaret(t.length());
                if (!list.isEmpty()) {
                    comboBox.show();
                }
            }

            private void moveCaret(int textLength) {        // 改变输入框光标的位置
                if (caretPos == -1) {       // -1表示移动到输入框的最右端
                    comboBox.getEditor().positionCaret(textLength);
                } else {                    // 否则移动到caretPos的位置
                    comboBox.getEditor().positionCaret(caretPos);
                }
                moveCaretToPos = false;
            }
        });
    }

    public static<T> T getComboBoxValue(ComboBox<T> comboBox){
        if (comboBox.getSelectionModel().getSelectedIndex() < 0) {
            return null;
        } else {
            return comboBox.getItems().get(comboBox.getSelectionModel().getSelectedIndex());
        }
    }

}