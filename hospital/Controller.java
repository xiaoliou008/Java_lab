package hospital;

import java.sql.SQLException;

abstract public class Controller {
    protected Main myApp;
    abstract public void setMyApp(Main myApp) throws SQLException;
}