module clontz.clientschedulingapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens clontz.clientschedulingapp to javafx.fxml;
    exports clontz.clientschedulingapp;
    opens clontz.clientschedulingapp.Controllers to javafx.fxml;
    exports clontz.clientschedulingapp.Controllers;
    opens clontz.clientschedulingapp.Models to javafx.fxml;
    exports clontz.clientschedulingapp.Models;
    opens clontz.clientschedulingapp.Helpers to javafx.fxml;
    exports clontz.clientschedulingapp.Helpers;
}