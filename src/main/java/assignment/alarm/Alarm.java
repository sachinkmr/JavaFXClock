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

public class Alarm {
    private JFXTimePicker timePicker;
    private boolean state;

    public boolean setAlarm(boolean alarmState) {
        if (alarmState) {
            state = createAlarm();
        } else {
            closeAlarm();
            state = false;
            timePicker = null;
        }
        return state;
    }

    private void closeAlarm() {
    }

    private boolean createAlarm() {
        timePicker = new JFXTimePicker(LocalTime.now());
        timePicker.setEditable(true);

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Select Alarm Time");
        window.setMinWidth(200);
        window.setMinHeight(100);
        window.setResizable(false);
        window.setOnCloseRequest((event -> closeWindowAndReset(window)));

        Button okButton = new Button("Okay");
        Button cancelButton = new Button("Cancel");

        okButton.setOnAction(event -> {
            window.close();
            setState(true);
        });
        cancelButton.setOnAction((event ->
                closeWindowAndReset(window))
        );

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
        return state;
    }

    private void closeWindowAndReset(Stage window) {
        state = false;
        timePicker = null;
        window.close();
    }

    private void setState(boolean state) {
        this.state = state;
    }

    public boolean isSet() {
        return state;
    }

    public String getAlarmTime() {
        if (state)
            return timePicker.getEditor().getText();
        return "";
    }
}
