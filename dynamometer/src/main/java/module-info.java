module com.artimesia.dynamometer {
    requires javafx.controls;
    requires javafx.fxml;
    requires jssc;
    requires java.sql;

    opens com.artimesia.dynamometer to javafx.fxml;
    exports com.artimesia.dynamometer;
}