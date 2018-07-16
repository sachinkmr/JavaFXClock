package assignment;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Demo extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(5);
        root.setPadding(new javafx.geometry.Insets(5));
        Label hrLabel = new Label();
        Label minLabel = new Label();
        TimePicker2 timeTextField = new TimePicker2();
        hrLabel.textProperty().bind(Bindings.format("Hours: %d", timeTextField.hoursProperty()));
        minLabel.textProperty().bind(Bindings.format("Minutes: %d", timeTextField.minutesProperty()));
        LocalTime now = LocalTime.now(ZoneId.systemDefault());
        LocalTime nextDay = now.plusMinutes(1440);
        TimeSpinner spinner = new TimeSpinner();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
        spinner.valueProperty().addListener((obs, oldTime, newTime) ->
                System.out.println(formatter.format(newTime)));


        root.getChildren().addAll(timeTextField, hrLabel, minLabel, spinner);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
