package hospital;

import hospital.doctor.DoctorController;
import hospital.doctor.YiShengTuple;
import hospital.patient.PatientController;
import hospital.patient.PatientTuple;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

import hospital.mysql.*;
import javafx.util.Duration;

public class Main extends Application {
    Stage stage;
    Controller controller;
    public Connection conn;

    /**
     * 客户端的入口
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Pane root = loader.load();

        String url = "jdbc:mysql://localhost:3306/hospital";
        // "jdbc:mysql://localhost:3306/javacourse?serverTimezone=GMT" 可以避免时区问题？
        conn = ConnectionFactory.create(url, "root", "root");

        controller = loader.getController();
        controller.setMyApp(this);

        setTimeTitle(primaryStage);

        stage.setScene(new Scene(root));
        stage.setTitle("登陆");
        stage.setOnCloseRequest(new WindowCloseHandler());
        stage.show();

        // debug patient         运行后直接进入病人界面
//        changeScene(new PatientTuple(
//                "000003", "张三", "zs", 10.0, new Date(new java.util.Date().getTime())));

        // debug doctor         运行后直接进入医生界面
//        changeScene(new YiShengTuple(
//                "000001", "000001", "扁鹊", "bq", "bq", true,
//                new Date(new java.util.Date().getTime())));
    }

    /**
     * 切换场景，例如医生登陆、病人登陆后的场景切换
     * @param targetFXML
     * @param title
     * @throws IOException
     * @throws SQLException
     */
    public void changeScene(String targetFXML, String title) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(targetFXML));
        Pane root = loader.load();

        controller = loader.getController();
        controller.setMyApp(this);

        stage.setScene(new Scene(root));
        stage.setTitle(title);
        stage.show();
    }

    /**
     * 切换场景，用于病人
     * @param patient
     * @throws IOException
     * @throws SQLException
     */
    public void changeScene(PatientTuple patient) throws IOException, SQLException {
        changeScene("patient/patient.fxml", "病人挂号");
        ((PatientController) controller).setPatient(patient);
        ((PatientController) controller).getTextPatientName().setText(patient.getBRMC());
    }

    /**
     * 切换场景，用于医生
     * @param doctor
     * @throws IOException
     * @throws SQLException
     */
    public void changeScene(YiShengTuple doctor) throws IOException, SQLException {
        changeScene("doctor/doctor.fxml", "医生：" + doctor.getName());
        ((DoctorController) controller).setDoctor(doctor);
        ((DoctorController) controller).configTable();
//        ((DoctorController)controller).getTextFieldPatientName().setText(doctor.getBRMC());
    }

    /**
     * 捕获窗口关闭的消息，窗口关闭时把数据库连接也关闭
     */
    class WindowCloseHandler implements EventHandler<WindowEvent> {

        @Override
        public void handle(WindowEvent windowEvent) {
            try {
                conn.close();
                System.out.println("关闭数据库连接");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Stage getStage() {
        return stage;
    }

    /**
     * 设置标题中的时间（数据库时间同步，相当于心跳检测）
     * @param primaryStage
     */
    private void setTimeTitle(Stage primaryStage) {
        EventHandler<ActionEvent> timeEventHandler = e -> {
            try {
                String origin = primaryStage.getTitle().split(" ")[0];
                primaryStage.setTitle(origin + "   " + ConnectionFactory.getSQLTime(conn));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        };
        Timeline animation = new Timeline(new KeyFrame(Duration.millis(1000), timeEventHandler));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }

    /**
     * 整个程序的入口
     * @param args
     */
    static public void main(String[] args) {
        launch(args);
    }
}
