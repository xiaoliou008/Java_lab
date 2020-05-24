package hospital;

import java.sql.SQLException;

/**
 * 控制器的抽象父类
 */
abstract public class Controller {
    protected Main myApp;
    abstract public void setMyApp(Main myApp) throws SQLException;
}