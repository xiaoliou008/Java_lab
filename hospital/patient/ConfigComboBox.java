package hospital.patient;

import hospital.shared.PYZSAccessible;
import javafx.beans.binding.ListBinding;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ConfigComboBox<E> {
    public static<E extends PYZSAccessible> void config(ComboBox<E> comboBox, Node nextFocused, List<E> list){     // 要在static后面加<E>，否则编译报错
//        ObservableList<E> originData = comboBox.getItems();

        comboBox.setEditable(true);
        comboBox.setPromptText("无");
        comboBox.setVisibleRowCount(6);

        comboBox.getEditor().setOnMouseMoved(mouseEvent -> {        // 鼠标移动是输入模块上的事件
            if(comboBox.isFocused())
                comboBox.show();
        });
        comboBox.getEditor().setOnMouseClicked(mouseEvent -> {
            comboBox.show();
            comboBox.setVisibleRowCount(6);
        });

        comboBox.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
//                comboBox.hide();
            }
        });
        comboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<E>() {
            @Override
            public void changed(ObservableValue<? extends E> observableValue, E E, E t1) {
//                if(t1 != null) System.out.println("debug#get: " + t1.toString());
//                if(t1 != null) comboBox.getEditor().setText(t1.getName());
            }
        });
        comboBox.setConverter(new StringConverter<E>() {
            @Override
            public String toString(E E) {
                if(E == null) return "";
                return E.toString();
            }

            @Override
            public E fromString(String s) {     // 一定要注意判断null
                if(comboBox.getItems() == null || comboBox.getItems().isEmpty()){
                    return null;
                } else {
                    E r = comboBox.getSelectionModel().getSelectedItem();
                    if(r == null) return comboBox.getItems().get(0);        // 一个都没选就按下回车
                    else return r;
                }
            }
        });

        comboBox.addEventHandler(ComboBoxBase.ON_HIDDEN, new EventHandler<Event>() {
            @Override       // 如果用鼠标点击选项，也能保证只显示名称
            public void handle(Event event) {
//                System.out.println("debug#on hidden");
                if(!comboBox.getSelectionModel().isEmpty()){
                    String name = comboBox.getSelectionModel().getSelectedItem().getName();
                    comboBox.getEditor().setText(name);
                }
            }
        });
        // 注意，当comboBox的ItemList展开时，是不能进行焦点切换的
        comboBox.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            private boolean updateCaret = false;
            private boolean isFocused = false;      // 避免再次选中时 TAB 键直接跳过
            private int caretPos = 0;
            private int currentSel =-1;

            @Override
            public void handle(KeyEvent keyEvent) {
                comboBox.setVisibleRowCount(6);
                String t1 = comboBox.getEditor().getText();
                if(comboBox.isShowing()){
                    isFocused = true;
                }

                switch (keyEvent.getCode()){
                    case UP:
                        comboBox.show();
                        currentSel = Math.max(currentSel-1, 0);
                        comboBox.getSelectionModel().select(currentSel);
                        return;
                    case DOWN:
                        comboBox.show();
                        if(comboBox.getItems() != null) {
                            currentSel = Math.min(currentSel + 1, comboBox.getItems().size() - 1);
                            comboBox.getSelectionModel().select(currentSel);
                        }
                        return;
                    case BACK_SPACE:
                    case DELETE:
                        if(t1 != null){
                            updateCaret = true;
                            caretPos = comboBox.getEditor().getCaretPosition();
                        }
                        break;      // 不加这个就返回了！
                    case ENTER:
                        if(!comboBox.getSelectionModel().isEmpty()){
                            String name = comboBox.getSelectionModel().getSelectedItem().getName();
                            comboBox.getEditor().setText(name);
                            changeCaret(name.length());
                        }
                        return;
                    case END:
                        if(t1 != null){
                            updateCaret = true;
                            caretPos = t1.length();
                        }
                        break;
                    case HOME:
                        if(t1 != null){
                            updateCaret = true;
                            caretPos = 0;
                        }
                        break;
                    case LEFT:
                        if(t1 != null){
                            updateCaret = true;
                            caretPos = Math.max(0, comboBox.getEditor().getCaretPosition() - 1);
                        }
                        break;
                    case RIGHT:
                        if(t1 != null){
                            updateCaret = true;
                            caretPos = Math.min(t1.length(), comboBox.getEditor().getCaretPosition() + 1);
                        }
                        break;
                    case TAB:
                        if(nextFocused != null && isFocused)
                            nextFocused.requestFocus();     // 下一跳的目的地
                    case SHIFT:
                    case CONTROL:
                    case ESCAPE:
                        return;
                    default:
                        break;
                }

                ObservableList originData = null;
                if(list != null)
                    originData = FXCollections.observableArrayList(list);
                if(t1 == null){
                    comboBox.setItems(originData);
                    return;
                }
                FilteredList<E> newList = null;
                if(originData != null)
                    newList = originData.filtered(new Predicate<E>() {
                        @Override
                        public boolean test(E e) {
                            return e.getPYZS().contains(t1);
                        }
                    });

                if(newList == null || newList.isEmpty()){
                    comboBox.setItems(null);
                    comboBox.setPlaceholder(new Label("无"));
                }
                else{
                    comboBox.setItems(newList);
                }
                comboBox.getEditor().setText(t1);     // very important!
                changeCaret(t1.length());
                updateCaret = false;
                caretPos = 0;
                comboBox.show();
            }

            private void changeCaret(int len){
                if(updateCaret){
                    comboBox.getEditor().positionCaret(caretPos);
                } else {
                    comboBox.getEditor().positionCaret(len);
                }
            }
        });

//        comboBox.addEventHandler(EventType.ROOT, event -> {
//            if(comboBox.getItems().size() == list.size() && comboBox.getItems().retainAll(list)) return;
//            ObservableList<E> obList = null;
//            if(list != null) obList = FXCollections.observableArrayList(list);
//            comboBox.setItems(obList);
//        });

    }

    public static<E extends PYZSAccessible> void updateList(ComboBox<E> comboBox, List<E> list){
        if(list == null)
            comboBox.getItems().clear();
        else
            comboBox.setItems(FXCollections.observableArrayList(list));
    }

    public static<E extends PYZSAccessible> void config(ComboBox<E> comboBox, Node nextFocused){     // 要在static后面加<E>，否则编译报错
        ObservableList<E> originData = comboBox.getItems();

        comboBox.setEditable(true);
        comboBox.setPromptText("无");
        comboBox.setVisibleRowCount(6);

        comboBox.getEditor().setOnMouseMoved(mouseEvent -> {        // 鼠标移动是输入模块上的事件
            if(comboBox.isFocused())
                comboBox.show();
        });

        comboBox.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
//                comboBox.hide();
            }
        });
        comboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<E>() {
            @Override
            public void changed(ObservableValue<? extends E> observableValue, E E, E t1) {
//                if(t1 != null) System.out.println("debug#get: " + t1.toString());
//                if(t1 != null) comboBox.getEditor().setText(t1.getName());
            }
        });
        comboBox.setConverter(new StringConverter<E>() {
            @Override
            public String toString(E E) {
                if(E == null) return "";
                return E.toString();
            }

            @Override
            public E fromString(String s) {     // 一定要注意判断null
                if(comboBox.getItems() == null || comboBox.getItems().isEmpty()){
                    return null;
                } else {
                    E r = comboBox.getSelectionModel().getSelectedItem();
                    if(r == null) return comboBox.getItems().get(0);        // 一个都没选就按下回车
                    else return r;
                }
            }
        });

        comboBox.addEventHandler(ComboBoxBase.ON_HIDDEN, new EventHandler<Event>() {
            @Override       // 如果用鼠标点击选项，也能保证只显示名称
            public void handle(Event event) {
//                System.out.println("debug#on hidden");
                if(!comboBox.getSelectionModel().isEmpty()){
                    String name = comboBox.getSelectionModel().getSelectedItem().getName();
                    comboBox.getEditor().setText(name);
                }
            }
        });
        // 注意，当comboBox的ItemList展开时，是不能进行焦点切换的
        comboBox.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            private boolean updateCaret = false;
            private boolean isFocused = false;      // 避免再次选中时 TAB 键直接跳过
            private int caretPos = 0;
            private int currentSel =-1;

            @Override
            public void handle(KeyEvent keyEvent) {
                comboBox.setVisibleRowCount(6);
                String t1 = comboBox.getEditor().getText();
                if(comboBox.isShowing()){
                    isFocused = true;
                }

                switch (keyEvent.getCode()){
                    case UP:
                        comboBox.show();
                        currentSel = Math.max(currentSel-1, 0);
                        comboBox.getSelectionModel().select(currentSel);
                        return;
                    case DOWN:
                        comboBox.show();
                        if(comboBox.getItems() != null) {
                            currentSel = Math.min(currentSel + 1, comboBox.getItems().size() - 1);
                            comboBox.getSelectionModel().select(currentSel);
                        }
                        return;
                    case BACK_SPACE:
                    case DELETE:
                        if(t1 != null){
                            updateCaret = true;
                            caretPos = comboBox.getEditor().getCaretPosition();
                        }
                        break;      // 不加这个就返回了！
                    case ENTER:
                        if(!comboBox.getSelectionModel().isEmpty()){
                            String name = comboBox.getSelectionModel().getSelectedItem().getName();
                            comboBox.getEditor().setText(name);
                            changeCaret(name.length());
                        }
                        return;
                    case END:
                        if(t1 != null){
                            updateCaret = true;
                            caretPos = t1.length();
                        }
                        break;
                    case HOME:
                        if(t1 != null){
                            updateCaret = true;
                            caretPos = 0;
                        }
                        break;
                    case LEFT:
                        if(t1 != null){
                            updateCaret = true;
                            caretPos = Math.max(0, comboBox.getEditor().getCaretPosition() - 1);
                        }
                        break;
                    case RIGHT:
                        if(t1 != null){
                            updateCaret = true;
                            caretPos = Math.min(t1.length(), comboBox.getEditor().getCaretPosition() + 1);
                        }
                        break;
                    case TAB:
                        if(nextFocused != null && isFocused)
                            nextFocused.requestFocus();     // 下一跳的目的地
                    case SHIFT:
                    case CONTROL:
                    case ESCAPE:
                        return;
                    default:
                        break;
                }

                if(t1 == null){
                    comboBox.setItems(originData);
                    return;
                }
                FilteredList<E> newList = originData.filtered(new Predicate<E>() {
                    @Override
                    public boolean test(E e) {
                        return e.getPYZS().contains(t1);
                    }
                });

                if(newList.isEmpty()){
                    comboBox.setItems(null);
                    comboBox.setPlaceholder(new Label("无"));
                }
                else{
                    comboBox.setItems(newList);
                }
                comboBox.getEditor().setText(t1);     // very important!
                changeCaret(t1.length());
                updateCaret = false;
                caretPos = 0;
                comboBox.show();
            }

            private void changeCaret(int len){
                if(updateCaret){
                    comboBox.getEditor().positionCaret(caretPos);
                } else {
                    comboBox.getEditor().positionCaret(len);
                }
            }
        });
    }
}
