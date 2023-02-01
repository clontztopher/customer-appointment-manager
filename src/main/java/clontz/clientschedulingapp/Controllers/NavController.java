package clontz.clientschedulingapp.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class NavController implements Initializable {

    public static enum View {
        REPORTS, CUSTOMERS, APPOINTMENTS

    }
    private static View activeView;
    @FXML
    public Button reportsBtn;

    @FXML
    public Button customersBtn;

    @FXML
    public Button appointmentsBtn;

    static public void setActiveView(View nextActiveView) {
        activeView = nextActiveView;
    }

    public void handleNavClick(ActionEvent actionEvent) {
        String resourceLocation;
        String sceneTitle;
        Button clicked = (Button) actionEvent.getSource();

        if (clicked == reportsBtn) {
            resourceLocation = "reports-view.fxml";
            sceneTitle = "Reports";
            setActiveView(View.REPORTS);
        } else if (clicked == customersBtn) {
            resourceLocation = "manage-customer-view.fxml";
            sceneTitle = "Manage Customers";
            setActiveView(View.CUSTOMERS);
        } else {
            resourceLocation = "manage-appointment-view.fxml";
            sceneTitle = "Manage Appointments";
            setActiveView(View.APPOINTMENTS);
        }

        try {
            Parent root = FXMLLoader.load(getClass().getResource(resourceLocation));
            Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle(sceneTitle);
            stage.setScene(scene);
            stage.show();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String activeBtnStyle = "-fx-background-color: darkslategrey; -fx-text-fill: white;";
        String inactiveBtnStyle = "-fx-background-color: lightgrey; -fx-text-fill: black;";

        if (activeView.equals(View.REPORTS)) {
            reportsBtn.setStyle(activeBtnStyle);
            appointmentsBtn.setStyle(inactiveBtnStyle);
            customersBtn.setStyle(inactiveBtnStyle);
        } else if (activeView.equals(View.CUSTOMERS)) {
            customersBtn.setStyle(activeBtnStyle);
            reportsBtn.setStyle(inactiveBtnStyle);
            appointmentsBtn.setStyle(inactiveBtnStyle);
        } else {
            appointmentsBtn.setStyle(activeBtnStyle);
            reportsBtn.setStyle(inactiveBtnStyle);
            customersBtn.setStyle(inactiveBtnStyle);
        }
    }
}
