package hospital.patient;

import hospital.Controller;
import hospital.Main;
import hospital.department.KeShi;
import hospital.department.KeShiTuple;
import hospital.doctor.YiSheng;
import hospital.doctor.YiShengTuple;
import hospital.patient.config.ConfigComboBox;
import hospital.patient.config.ConfigTextField;
import hospital.registration.HaoZhong;
import hospital.registration.HaoZhongTuple;
import hospital.registration.HaoZhongType;
import hospital.patient.update.UpdateGHXX;
import hospital.shared.ExitAlert;
import hospital.shared.HelpAlert;
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
import static java.lang.System.in;
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
    @FXML public MenuItem menuOK;

    private double pay = 0.0;
    private UpdateGHXX updateGHXX;
    private PatientTuple patient;
    private List<KeShiTuple> ksmcList;
    private List<YiShengTuple> ysxmList;
    private List<HaoZhongType> hzlbList =
            new ArrayList<HaoZhongType>(Arrays.asList(new HaoZhongType("普通"), new HaoZhongType("专家")));
    private List<HaoZhongTuple> hzmcList;

    /**
     * 设置病人
     * @param patient
     */
    public void setPatient(PatientTuple patient) {
        this.patient = patient;
    }

    /**
     * 获取病人姓名
     * @return
     */
    public Text getTextPatientName() {
        return textPatientName;
    }

    /**
     * 设置myapp
     * @param myApp
     * @throws SQLException
     */
    @Override
    public void setMyApp(Main myApp) throws SQLException {
        this.myApp = myApp;
        updateGHXX = new UpdateGHXX(myApp.conn);
        ksmcList = new KeShi(myApp.conn).getRelation();
        ysxmList = new YiSheng(myApp.conn).getRelation();
        hzmcList = new HaoZhong(myApp.conn).getRelation();
        configComboBox();
    }

    /**
     * 点击退出按钮
     * @param e
     * @throws SQLException
     */
    public void patientExit(ActionEvent e) throws SQLException {
        // 注意：WindowEvent不能转换成Event类型
        ExitAlert.show(myApp);
    }

    /**
     * 配置combobox
     * @throws SQLException
     */
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

    /**
     * 点击清空按钮
     * @param actionEvent
     */
    public void clearHandler(ActionEvent actionEvent) {
        disableAllField(false);
        comboBoxKSMC.getSelectionModel().clearSelection();
        comboBoxKSMC.getEditor().clear();
        comboBoxHZLB.getSelectionModel().clearSelection();
        comboBoxHZLB.getEditor().clear();
        comboBoxYSXM.getSelectionModel().clearSelection();
        comboBoxYSXM.getEditor().clear();
        comboBoxHZLB.setDisable(true);
        comboBoxHZMC.setDisable(true);
        // 其他的选项框自动清除
        textFieldJKJE.clear();
        textFieldYJJE.clear();
        textFieldZLJE.clear();
        textFieldGHHM.clear();
        comboBoxKSMC.requestFocus();
        OKButton.setDisable(false);
        menuOK.setDisable(false);
    }

    /**
     * 点击确定按钮
     * @param actionEvent
     * @throws SQLException
     */
    public void OKHandler(ActionEvent actionEvent) throws SQLException {
        if(     comboBoxHZLB.getSelectionModel().isEmpty() ||
                comboBoxKSMC.getSelectionModel().isEmpty() ||
                comboBoxYSXM.getSelectionModel().isEmpty() ||
                comboBoxHZMC.getSelectionModel().isEmpty() ||
                (!textFieldZLJE.isDisable() && textFieldZLJE.getText().isEmpty()) ||
                textFieldYJJE.getText().isEmpty() ||
                (!textFieldJKJE.isDisable() && textFieldJKJE.getText().isEmpty())) {
            System.out.println("挂号失败");
            Alert alert = new Alert(Alert.AlertType.ERROR, "请填写完所有信息后挂号");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    alert.close();
                }
            });
        }
        else {
            double pay = Double.parseDouble(textFieldYJJE.getText());
            String GHHM = null;
            if(checkBoxYuE.isSelected()){       // 使用余额缴费
                double ye = patient.getYE(myApp.conn);
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
                GHHM = updateGHXX.update(comboBoxHZMC.getSelectionModel().getSelectedItem().getHZBH(),
                        comboBoxYSXM.getSelectionModel().getSelectedItem().getYSBH(),
                        patient.getBRBH(), pay/*, new Date()*/);    // 不使用系统时间，而是与数据库同步
                if(!GHHM.equals("full")) {
                    // 余额充足，扣款
                    patient.setYE(myApp.conn, ye - pay);
                    String prompt = "使用账户余额挂号成功\n" + "挂号后余额：" + (ye - pay);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, prompt);
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            alert.close();
                        }
                    });
                }
            } else {        // 使用现金缴费
                double rest = Double.parseDouble(textFieldZLJE.getText());
                if(rest < 0){
                    String prompt = "病人交易余额不足\n应缴金额：" + pay +
                            "\n不足金额数：" + (-Double.parseDouble(textFieldZLJE.getText()));
                    Alert alert = new Alert(Alert.AlertType.ERROR, prompt);
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            alert.close();
                        }
                    });
                    return;
                }
                GHHM = updateGHXX.update(comboBoxHZMC.getSelectionModel().getSelectedItem().getHZBH(),
                        comboBoxYSXM.getSelectionModel().getSelectedItem().getYSBH(),
                        patient.getBRBH(), pay/*, new Date()*/);    // 不使用系统时间，而是与数据库同步
                if(!GHHM.equals("full")) {
                    String prompt = "使用现金挂号成功\n" + "挂号后找零：" + rest +
                            "\n是否选择存入账户余额？";
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, prompt);
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            System.out.println("存款");
                            try {
                                patient.setYE(myApp.conn, patient.getYE(myApp.conn) + rest);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            alert.close();
                        }
                    });
                }
            }
            if(GHHM.equals("full")){
                String prompt = "该号种人数已满\n挂号失败";
                Alert alert = new Alert(Alert.AlertType.ERROR, prompt);
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        alert.close();
                    }
                });
                return;
            } else {        // 挂号成功
                System.out.println("挂号成功");
                textFieldGHHM.setText(GHHM);
                OKButton.setDisable(true);      // 避免重复挂号
                menuOK.setDisable(true);
                disableAllField(true);
            }
        }
        // debug
//        updateGHXX.update("000001", "000001", "000001", 10.0, new Date());
    }

    /**
     * 点击checkBox
     * @param actionEvent
     */
    public void checkBoxHandler(ActionEvent actionEvent) {
        if(checkBoxYuE.isSelected()){
            textFieldJKJE.setDisable(true);
            textFieldZLJE.setDisable(true);
        } else {
            textFieldJKJE.setDisable(false);
            textFieldZLJE.setDisable(false);
        }
    }

    /**
     * 配置可编辑性
     * @param disable
     */
    private void disableAllField(boolean disable){
        comboBoxKSMC.setDisable(disable);
        comboBoxHZMC.setDisable(disable);
        comboBoxYSXM.setDisable(disable);
        comboBoxHZLB.setDisable(disable);
        textFieldJKJE.setDisable(disable);
//        textFieldZLJE.setDisable(disable);
//        textFieldGHHM.setDisable(disable);
//        textFieldYJJE.setDisable(disable);
    }

    /**
     * 菜单退号
     * @param actionEvent
     */
    public void menuTHHandler(ActionEvent actionEvent) {
        String ghbh = textFieldGHHM.getText();
        if(comboBoxKSMC.isDisable() && ghbh != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "确定退号？");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    System.out.println("退号");
                    try {
                        updateGHXX.cancel(ghbh, patient);
                        HelpAlert.show(myApp, "退号成功！");
                        alert.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "尚未完成挂号！");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    alert.close();
                }
            });
        }
    }

    /**
     * 菜单帮助关于
     * @param actionEvent
     */
    public void helperHandler(ActionEvent actionEvent) {
        HelpAlert.show(myApp);
    }

    /**
     * 查询余额
     * @param actionEvent
     * @throws SQLException
     */
    public void askMoneyHandler(ActionEvent actionEvent) throws SQLException {
        double money = patient.getYE(myApp.conn);
        HelpAlert.show(myApp, "当前余额：" + money);
    }

    /**
     * 菜单存款
     * @param actionEvent
     */
    public void depositHandler(ActionEvent actionEvent) {
        TextInputDialog input = new TextInputDialog("请输入存款金额");
        input.showAndWait().ifPresent(response -> {
            System.out.println("存款：" + response);
            try {
                patient.addYE(myApp.conn, Double.parseDouble(response));
                HelpAlert.show(myApp, "存款成功！");
                input.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (NumberFormatException e){
                HelpAlert.show(myApp, "存款失败！\n请输入正确的金额");
                input.close();
            }
        });
    }
}
