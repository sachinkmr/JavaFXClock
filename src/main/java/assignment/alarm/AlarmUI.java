package assignment.alarm;

import assignment.component.ToggleSwitch;
import assignment.utils.HelperUtils;
import com.github.plushaze.traynotification.animations.Animations;
import com.github.plushaze.traynotification.notification.Notification;
import com.github.plushaze.traynotification.notification.Notifications;
import com.github.plushaze.traynotification.notification.TrayNotification;
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
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class AlarmUI {
    private HBox alarmBox;
    private Timeline alarmAnimation;
    private ToggleSwitch alarmToggleButton;
    private ImageView imageView;
    private Label alarmLabel;
    private AlarmScheduler alarmScheduler;

    public AlarmUI(ToggleSwitch alarmToggleButton) {
        this.alarmToggleButton = alarmToggleButton;
        alarmAnimation = new Timeline();
        alarmScheduler = new AlarmScheduler();
    }

    public HBox initAlarmUI() {
        // Creating Alarm UI
        try {
            InputStream input = this.getClass().getClassLoader().getResourceAsStream("images/alarm.png");
            Image image = new Image(input);
            imageView = new ImageView(image);
            imageView.setFitHeight(20);
            imageView.setFitWidth(20);
            setAlarmAnimation();
            input.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        alarmLabel = new Label("");
        alarmLabel.setAlignment(Pos.CENTER);
        alarmLabel.setContentDisplay(ContentDisplay.BOTTOM);
        alarmLabel.setStyle("-fx-font-weight: bold;");

        alarmToggleButton.switchedOnProperty().addListener((ov, oldState, newState) -> this.setAlarm(newState));
//        alarmToggleButton.setDisableVisualFocus(true);

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
            createAlarm();
        } else {
            closeAlarm();

        }
    }

    private void closeAlarm() {
        alarmScheduler.stopAlarm();
        alarmAnimation.stop();
        alarmBox.setVisible(false);
        alarmLabel.setText("");
    }

    private void createAlarm() {
        String alarmTime = TimePicker.getSelected();
        if (alarmTime == null || alarmTime.isEmpty()) {
            alarmToggleButton.switchedOnProperty().setValue(false);
            return;
        }
        alarmScheduler.startAlarm(alarmTime, alarmAnimation);
        alarmLabel.setText(alarmTime);
        alarmBox.setVisible(true);
        long date = alarmScheduler.getAlarmDate().getTime();
        long now = new Date().getTime();
        long millis = date - now;
        String alarmMessage = String.format("Alarm set for: %d hour(s), %d minutes(s) and %d seconds(s).", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1), TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));


        TrayNotification tray = new TrayNotification();
        tray.setTitle("Alarm Status");
        tray.setMessage(alarmMessage);
        tray.setNotification(Notifications.SUCCESS);
        tray.setAnimation(Animations.POPUP);
        tray.showAndDismiss(Duration.seconds(5));
    }
}
