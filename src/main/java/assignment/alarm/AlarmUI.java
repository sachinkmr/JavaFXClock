package assignment.alarm;

import assignment.utils.HelperUtils;
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

public class AlarmUI {
    private HBox alarmBox;
    private boolean enabled;
    private Timeline alarmAnimation;
    private JFXToggleButton alarmToggleButton;
    private ImageView imageView;
    private Label alarmLabel;
    private AlarmScheduler alarmScheduler;

    public AlarmUI(JFXToggleButton alarmToggleButton) {
        this.alarmToggleButton = alarmToggleButton;
        alarmAnimation = new Timeline();
        alarmScheduler = new AlarmScheduler();
    }

    public HBox initAlarmUI() {
        // Creating Alarm UI
        Image image = new Image(HelperUtils.getResourceLocation("images/alarm.png").toExternalForm());
        imageView = new ImageView(image);
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        setAlarmAnimation();


        alarmLabel = new Label("");
        alarmLabel.setAlignment(Pos.CENTER);
        alarmLabel.setContentDisplay(ContentDisplay.BOTTOM);
        alarmLabel.setStyle("-fx-font-weight: bold;");

        alarmToggleButton.selectedProperty().addListener((ov, oldState, newState) -> this.setAlarm(newState));
        alarmToggleButton.setDisableVisualFocus(true);

        alarmBox = new HBox(1);
        alarmBox.setLayoutX(240.0);
        alarmBox.setLayoutY(17.0);
        alarmBox.setPrefSize(77, 21);
        alarmBox.setVisible(false);
        alarmBox.setAlignment(Pos.TOP_LEFT);
        alarmBox.getChildren().addAll(imageView, alarmLabel);
        return alarmBox;
    }

    private void setAlarmAnimation() {
        alarmAnimation.setCycleCount(Animation.INDEFINITE);
        alarmAnimation.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(0), new KeyValue(imageView.translateXProperty(), 0)),
                new KeyFrame(Duration.millis(250), new KeyValue(imageView.translateXProperty(), 3)),
                new KeyFrame(Duration.millis(500), new KeyValue(imageView.translateXProperty(), 0)),
                new KeyFrame(Duration.millis(750), new KeyValue(imageView.translateXProperty(), -3)),
                new KeyFrame(Duration.millis(1000), new KeyValue(imageView.translateXProperty(), 0))
        );
    }

    private void setAlarm(boolean alarmState) {
        if (alarmState) {
            enabled = createAlarm();
        } else {
            enabled = closeAlarm();
        }
    }

    private boolean closeAlarm() {
        alarmScheduler.stopAlarm();
        alarmAnimation.stop();
        alarmBox.setVisible(false);
        alarmLabel.setText("");
        return false;
    }

    private boolean createAlarm() {
        String alarmTime = TimePicker.getSelecetdTime();
        if (alarmTime == null || alarmTime.isEmpty()) {
            alarmToggleButton.setSelected(false);
            return false;
        }
        alarmScheduler.startAlarm(alarmTime, alarmAnimation);
        alarmLabel.setText(alarmTime);
        alarmBox.setVisible(true);
        //TODO: new Alert(Alert.AlertType.ERROR, alarmScheduler.getAlarmDate().toString()).showAndWait();
        return enabled;
    }
}
