package assignment.alarm;

import com.jfoenix.controls.JFXTimePicker;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalTime;

public class TimePicker {
    public static String getSelecetdTime() {
        JFXTimePicker timePicker = new JFXTimePicker(LocalTime.now());
        timePicker.setEditable(false);

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Select Alarm Time");
        window.setMinWidth(200);
        window.setMinHeight(100);
        window.setResizable(false);


        Button okButton = new Button("Okay");
        Button cancelButton = new Button("Cancel");

        okButton.setOnAction(event -> window.close());
        cancelButton.setOnAction(event -> closeTimePicker(window, timePicker));
        window.setOnCloseRequest(event -> closeTimePicker(window, timePicker));

        HBox hbox = new HBox();
        hbox.setSpacing(25);
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(okButton, cancelButton);

        VBox pane = new VBox();
        pane.setPadding(new Insets(10));
        pane.setSpacing(25);
        pane.setAlignment(Pos.CENTER);
        pane.getChildren().addAll(timePicker, hbox);

        Scene scene = new Scene(pane, 200, 100);
        window.setScene(scene);
        window.showAndWait();
        return timePicker.getEditor().getText();
    }

    private static void closeTimePicker(Stage window, JFXTimePicker timePicker) {
        timePicker.getEditor().setText("");
        window.close();
    }
}
