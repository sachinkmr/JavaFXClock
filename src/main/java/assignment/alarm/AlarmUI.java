package assignment.alarm;

import assignment.utils.HelperUtils;
import com.jfoenix.controls.JFXTimePicker;
import com.jfoenix.controls.JFXToggleButton;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class AlarmUI {
    private JFXTimePicker timePicker;
    private HBox alarmBox;
    private boolean enabled;
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
        alarmBox.setPrefSize(71, 21);
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
    }

    private void setAlarmAnimation() {
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
            enabled = true;
        } else {
            closeAlarm();
            enabled = false;
        }
    }

    private void closeAlarm() {
        enabled = false;
        timePicker = null;
    }

    private boolean createAlarm() {

        return enabled;
    }
    public boolean isEnabled() {
        return enabled;
    }

    private String getAlarmTime() {
        if (enabled)
            return timePicker.getEditor().getText();
        return "";
    }
}
