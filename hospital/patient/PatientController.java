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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.WindowEvent;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
    @FXML public Button OKButton;
    @FXML public Text textPatientName;
    @FXML public CheckBox checkBoxYuE;

    private double pay = 0.0;
    private UpdateGHXX updateGHXX;
    private PatientTuple patient;
    private List<KeShiTuple> ksmcList;
    private List<YiShengTuple> ysxmList;
    private List<HaoZhongType> hzlbList =
            new ArrayList<HaoZhongType>(Arrays.asList(new HaoZhongType("普通"), new HaoZhongType("专家")));
    private List<HaoZhongTuple> hzmcList;

    public void setPatient(PatientTuple patient) {
        this.patient = patient;
    }

    public Text getTextPatientName() {
        return textPatientName;
    }

    @Override
    public void setMyApp(Main myApp) throws SQLException {
        this.myApp = myApp;
        updateGHXX = new UpdateGHXX(myApp.conn);
        ksmcList = new KeShi(myApp.conn).getRelation();
        ysxmList = new YiSheng(myApp.conn).getRelation();
        hzmcList = new HaoZhong(myApp.conn).getRelation();
        configComboBox();
    }

    public void patientExit(ActionEvent e) throws SQLException {
        // 注意：WindowEvent不能转换成Event类型
        Alert alert = new Alert(Alert.AlertType.WARNING, "确定退出挂号系统？");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                WindowEvent.fireEvent(myApp.getStage(), new WindowEvent(myApp.getStage(), WINDOW_CLOSE_REQUEST));
                exit(0);
            }
        });
    }

    private void configComboBox() throws SQLException {
        // 保证在没有选择科室之前，不能选择后面的内容
        comboBoxYSXM.setDisable(true);
        comboBoxHZLB.setDisable(true);
        comboBoxHZMC.setDisable(true);
        List<KeShiTuple> list1 = new ArrayList<>(ksmcList);
        List<YiShengTuple> list2 = new ArrayList<>(ysxmList);
        List<HaoZhongType> list3 = new ArrayList<>(hzlbList);
        List<HaoZhongTuple> list4 = new ArrayList<>(hzmcList);
        comboBoxKSMC.getItems().addAll(ksmcList);
        ConfigComboBox.config(comboBoxKSMC, comboBoxYSXM, list1);
        comboBoxYSXM.getItems().addAll(ysxmList);
        ConfigComboBox.config(comboBoxYSXM, comboBoxHZLB, list2);
        comboBoxHZLB.getItems().addAll(hzlbList);
        ConfigComboBox.config(comboBoxHZLB, comboBoxHZMC, list3);
        comboBoxHZMC.getItems().addAll(hzmcList);
        ConfigComboBox.config(comboBoxHZMC, textFieldJKJE, list4);

        // 下面配置这四个选项框之间的依赖关系
        // 当科室内容变动的时候，清空后续选择框的内容
        comboBoxKSMC.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                comboBoxYSXM.getSelectionModel().clearSelection();      // 清空之前选择的内容
                comboBoxYSXM.getEditor().clear();
                list2.clear();
                if(!comboBoxKSMC.getSelectionModel().isEmpty()) {
                    String ksbh = comboBoxKSMC.getSelectionModel().getSelectedItem().getKSBH();
                    // 修改医生选择框的内容
                    for (YiShengTuple t : ysxmList) {
                        if(t.getKSBH().equals(ksbh))     // String不能用==比较！
                            list2.add(t);
                    }
                    ConfigComboBox.updateList(comboBoxYSXM, list2);
                    comboBoxYSXM.setDisable(false);
                }
                else        // 如果科室没有选择，则医生不能选择
                    comboBoxYSXM.setDisable(true);
            }
        });
        // 当医生选择框变动时
        comboBoxYSXM.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                comboBoxHZLB.getSelectionModel().clearSelection();
                comboBoxHZLB.getEditor().clear();
                comboBoxHZMC.getEditor().clear();
                list3.clear();
                list4.clear();
                if(!comboBoxYSXM.getSelectionModel().isEmpty()) {
                    boolean sfzj = comboBoxYSXM.getSelectionModel().getSelectedItem().getSFZJ();
                    // 修改号种选择框的内容
                    list3.add(new HaoZhongType("普通"));
                    if(sfzj) {       // 医生是专家
                        list3.add(new HaoZhongType("专家"));
                    }
                    ConfigComboBox.updateList(comboBoxHZLB, list3);
                    comboBoxHZLB.setDisable(false);
                }
                else        // 如果医生没有选择，则号种不能选择
                    comboBoxHZLB.setDisable(true);
            }
        });
        // 当号种选择框变动时
        comboBoxHZLB.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                comboBoxHZMC.getEditor().clear();
                list4.clear();
                if(!comboBoxHZLB.getSelectionModel().isEmpty()) {
                    boolean sfzj = comboBoxHZLB.getSelectionModel().getSelectedItem().getSFZJ();
                    String ksbh = comboBoxKSMC.getSelectionModel().getSelectedItem().getKSBH();
                    for(HaoZhongTuple t : hzmcList){
                        if(t.getKSBH().equals(ksbh) && t.isSFZJ() == sfzj){
                            list4.add(t);
                        }
                    }
                    ConfigComboBox.updateList(comboBoxHZMC, list4);
                    comboBoxHZMC.setDisable(false);
                }
                else        // 如果医生没有选择，则号种不能选择
                    comboBoxHZMC.setDisable(true);
            }
        });

        comboBoxHZMC.addEventHandler(EventType.ROOT, event -> {
//            System.out.println("debug#HZMC: " + event.getEventType());
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
        comboBoxKSMC.getSelectionModel().clearSelection();
        comboBoxKSMC.getEditor().clear();
        // 其他的选项框自动清除
        textFieldJKJE.clear();
        textFieldYJJE.clear();
        textFieldZLJE.clear();
        textFieldGHHM.clear();
        comboBoxKSMC.requestFocus();
        OKButton.setDisable(false);
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
            Alert alert = new Alert(Alert.AlertType.ERROR, "请填写完所有信息后挂号");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    alert.close();
                }
            });
        }
        else {
            if(checkBoxYuE.isSelected()){       // 使用余额缴费
                double ye = patient.getYE(myApp.conn);
                double pay = Double.parseDouble(textFieldYJJE.getText());
                if(ye < pay){
                    String prompt = "病人账户余额不足\n" + "余额：" + ye + "\n应缴金额：" + pay;
                    Alert alert = new Alert(Alert.AlertType.ERROR, prompt);
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            alert.close();
                        }
                    });
                    return;
                }
                // 余额充足，扣款
                patient.setYE(myApp.conn, ye - pay);
            } else {        // 使用现金缴费
                if(Double.parseDouble(textFieldZLJE.getText()) < 0){
                    String prompt = "病人交易余额不足\n" +
                            "不足金额数：" + (-Double.parseDouble(textFieldZLJE.getText()));
                    Alert alert = new Alert(Alert.AlertType.ERROR, prompt);
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            alert.close();
                        }
                    });
                    return;
                }
            }
            System.out.println("挂号成功");
            String GHHM = updateGHXX.update(comboBoxHZMC.getSelectionModel().getSelectedItem().getHZBH(),
                            comboBoxYSXM.getSelectionModel().getSelectedItem().getYSBH(),
                            patient.getBRBH(), 10.0, new Date());
            textFieldGHHM.setText(GHHM);
            OKButton.setDisable(true);      // 避免重复挂号
        }
        // debug
//        updateGHXX.update("000001", "000001", "000001", 10.0, new Date());
    }

    public void checkBoxHandler(ActionEvent actionEvent) {
        if(checkBoxYuE.isSelected()){
            textFieldJKJE.setDisable(true);
            textFieldZLJE.setDisable(true);
        } else {
            textFieldJKJE.setDisable(false);
            textFieldZLJE.setDisable(false);
        }
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