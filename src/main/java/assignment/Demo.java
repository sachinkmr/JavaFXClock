package assignment;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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

        root.getChildren().addAll(timeTextField, hrLabel, minLabel);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
