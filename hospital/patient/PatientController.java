package hospital.patient;

import hospital.Controller;
import hospital.Main;
import hospital.department.KeShi;
import hospital.department.KeShiTuple;
import hospital.doctor.YiSheng;
import hospital.doctor.YiShengTuple;
import hospital.registration.HaoZhong;
import hospital.registration.HaoZhongTuple;
import hospital.registration.HaoZhongType;
import hospital.update.UpdateGHXX;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.WindowEvent;

import java.sql.SQLException;
import java.util.Date;

import static java.lang.System.exit;
import static javafx.stage.WindowEvent.WINDOW_CLOSE_REQUEST;

public class PatientController extends Controller {
    @FXML public ComboBox<KeShiTuple> comboBoxKSMC;
    @FXML public ComboBox<YiShengTuple> comboBoxYSXM;
    @FXML public ComboBox<HaoZhongType> comboBoxHZLB;
    @FXML public ComboBox<HaoZhongTuple> comboBoxHZMC;
    @FXML public TextField textFieldJKJE;
    @FXML public TextField textFieldYJJE;
    @FXML public TextField textFieldZLJE;
    @FXML public TextField textFieldGHHM;
    @FXML public TextField textFieldPatientName;
    private double pay = 0.0;
    private UpdateGHXX updateGHXX;
    private PatientTuple patient;

    public void setPatient(PatientTuple patient) {
        this.patient = patient;
    }

    public TextField getTextFieldPatientName() {
        return textFieldPatientName;
    }

    @Override
    public void setMyApp(Main myApp) throws SQLException {
        this.myApp = myApp;
        configComboBox();
        updateGHXX = new UpdateGHXX(myApp.conn);
    }

    public void patientExit(ActionEvent e) throws SQLException {
        // 注意：WindowEvent不能转换成Event类型
        WindowEvent.fireEvent(myApp.getStage(), new WindowEvent(myApp.getStage(), WINDOW_CLOSE_REQUEST));
        exit(0);
    }

    private void configComboBox() throws SQLException {
        comboBoxKSMC.getItems().addAll(new KeShi(myApp.conn).getRelation());
        ConfigComboBox.config(comboBoxKSMC, comboBoxYSXM);
        comboBoxYSXM.getItems().addAll(new YiSheng(myApp.conn).getRelation());
        ConfigComboBox.config(comboBoxYSXM, comboBoxHZLB);
        comboBoxHZLB.getItems().addAll(new HaoZhongType("普通"), new HaoZhongType("专家"));
        ConfigComboBox.config(comboBoxHZLB, comboBoxHZMC);
        comboBoxHZMC.getItems().addAll(new HaoZhong(myApp.conn).getRelation());
        ConfigComboBox.config(comboBoxHZMC, textFieldJKJE);

        comboBoxHZMC.addEventHandler(EventType.ROOT, event -> {
            System.out.println("debug#HZMC: " + event.getEventType());
            if(!comboBoxHZMC.getSelectionModel().isEmpty()){
                pay = comboBoxHZMC.getSelectionModel().getSelectedItem().getGHFY();
                textFieldYJJE.setText(
                        Double.toString(pay)
                );
                if(textFieldJKJE.getText() != null && !textFieldJKJE.getText().isEmpty()){
                    textFieldZLJE.setText(
                            Double.toString(        // 保证结果只有两位小数
                                    Math.round((Double.parseDouble(textFieldJKJE.getText()) - pay) * 100) / 100.0
                            )
                    );
                } else {
                    textFieldJKJE.setText("");
                }
            }
        });

        textFieldJKJE.addEventHandler(KeyEvent.KEY_RELEASED, keyEvent -> {
            if(textFieldJKJE.getText() != null && !textFieldJKJE.getText().isEmpty()){
                textFieldZLJE.setText(
                        Double.toString(        // 保证结果只有两位小数
                                Math.round((Double.parseDouble(textFieldJKJE.getText()) - pay) * 100) / 100.0
                        )
                );
            } else {
                textFieldJKJE.setText("");
            }
        });
        ConfigTextField.config(textFieldJKJE);

        textFieldYJJE.setPromptText("0.0");


//        comboBoxKSMC.addEventHandler(EventType.ROOT, new EventHandler<Event>() {
//            @Override
//            public void handle(Event event) {
//                System.out.println(event.getEventType());
//                ActionEvent e = new ActionEvent();
//            }
//        });
    }

    public void clearHandler(ActionEvent actionEvent) {
        comboBoxKSMC.getEditor().clear();
        comboBoxHZMC.getEditor().clear();
        comboBoxYSXM.getEditor().clear();
        comboBoxHZLB.getEditor().clear();
        textFieldJKJE.clear();
        textFieldYJJE.clear();
        textFieldZLJE.clear();
        textFieldGHHM.clear();
        comboBoxKSMC.requestFocus();
    }

    public void OKHandler(ActionEvent actionEvent) throws SQLException {
        if(     comboBoxHZLB.getSelectionModel().isEmpty() ||
                comboBoxKSMC.getSelectionModel().isEmpty() ||
                comboBoxYSXM.getSelectionModel().isEmpty() ||
                comboBoxHZMC.getSelectionModel().isEmpty() ||
                textFieldZLJE.getText().isEmpty() ||
                textFieldYJJE.getText().isEmpty() ||
                textFieldJKJE.getText().isEmpty()) {
            System.out.println("挂号失败");
        }
        else {
            System.out.println("挂号成功");
            String GHHM = updateGHXX.update(comboBoxHZMC.getSelectionModel().getSelectedItem().getHZBH(),
                            comboBoxYSXM.getSelectionModel().getSelectedItem().getYSBH(),
                            patient.getBRBH(), 10.0, new Date());
            textFieldGHHM.setText(GHHM);
        }
        // debug
//        updateGHXX.update("000001", "000001", "000001", 10.0, new Date());
    }
}


//class TestA{
//    String text;
//    public TestA(){text = "";}
//    public TestA(String s){if(s != null) text = s; else text = "";}
//    @Override
//    public String toString() {
//        return text;
//    }
//}


//                if(t1 == null || t1.length() < 1){
////                    comboBoxKSMC.setItems(ksmcItems);
//                    comboBoxKSMC.setValue(s);
//                    return;
//                }
//                if(s == null || s.length() < 1){
//                    comboBoxKSMC.setValue(t1);
//                    return;
//                }
//                if(t1.length() == s.length() || Math.abs(t1.length() - s.length()) > 1) return;
////                if(t1.length() < s.length()) return;
//                System.out.println("old = " + s);
//                System.out.println("new = " + t1);
//                FilteredList<String> newList = ksmcItems.filtered(new Predicate<String>() {
//                    @Override
//                    public boolean test(String s) {
//                        return s.contains(t1);
//                    }
//                });
//                if(newList.isEmpty()){
//                    comboBoxKSMC.setItems(null);
//                    comboBoxKSMC.setPlaceholder(new Label("无"));
//                    return;
//                }
//                else comboBoxKSMC.setItems(newList);
////                else comboBoxKSMC.getItems().setAll(newList);
////                comboBoxKSMC.show();
//                comboBoxKSMC.hide();
////                comboBoxKSMC.setValue(t1);