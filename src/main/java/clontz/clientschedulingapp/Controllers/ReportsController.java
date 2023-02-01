package clontz.clientschedulingapp.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class ReportsController implements Initializable {

    @FXML
    public Pane navMenu;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        NavController.setActiveView(NavController.View.REPORTS);
        System.out.println(url);
        System.out.println("Opened Reports");
    }


}
