package assignment.alarm;

import assignment.utils.HelperUtils;
import com.jfoenix.controls.JFXTimePicker;
import com.jfoenix.controls.JFXToggleButton;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalTime;

public class AlarmUI {
    private JFXTimePicker timePicker;
    private HBox alarmBox;
    private boolean state;
    private Timeline playAlarm;
    private JFXToggleButton alarmToggleButton;
    private ImageView imageView;
    private Label alarmLabel;

    public AlarmUI(JFXToggleButton alarmToggleButton) {
        this.alarmToggleButton = alarmToggleButton;
        alarmToggleButton.setDisableVisualFocus(true);

        alarmBox = new HBox(5);
        alarmBox.setLayoutX(243.0);
        alarmBox.setLayoutY(16.0);
        alarmBox.setPrefSize(71,21);
        alarmBox.setVisible(false);
        alarmBox.setAlignment(Pos.TOP_LEFT);
    }

    public void initAlarmUI() {
        // Creating Alarm UI
        alarmToggleButton.selectedProperty().addListener((ov, oldState, newState) -> this.setAlarm(newState));
        try {
            FileInputStream input = new FileInputStream(HelperUtils.getResourceLocation("images/alarm.png").getFile());
            Image image = new Image(input);
            imageView = new ImageView(image);
            imageView.setFitHeight(20);
            imageView.setFitWidth(20);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        alarmLabel = new Label("");
        alarmLabel.setAlignment(Pos.CENTER);
        alarmLabel.setContentDisplay(ContentDisplay.CENTER);
        alarmBox.getChildren().addAll(imageView, alarmLabel);

        setAlarmAnimation();
    }

    private void setAlarmAnimation(){
        playAlarm = new Timeline();
        playAlarm.setCycleCount(Animation.INDEFINITE);
        playAlarm.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(0), new KeyValue(imageView.translateXProperty(), 0)),
                new KeyFrame(Duration.millis(250), new KeyValue(imageView.translateXProperty(), 2)),
                new KeyFrame(Duration.millis(500), new KeyValue(imageView.translateXProperty(), 0)),
                new KeyFrame(Duration.millis(750), new KeyValue(imageView.translateXProperty(), -2)),
                new KeyFrame(Duration.millis(1000), new KeyValue(imageView.translateXProperty(), 0))
        );
    }

    public void setAlarm(boolean alarmState) {
        if (alarmState && createAlarm()) {
            state = true;
        } else {
            closeAlarm();
            state = false;
        }
    }

    private void closeAlarm() {
        state = false;
        timePicker = null;
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

    private String getAlarmTime() {
        if (state)
            return timePicker.getEditor().getText();
        return "";
    }
}
