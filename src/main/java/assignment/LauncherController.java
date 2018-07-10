package assignment;

import assignment.alarm.Alarm;
import assignment.clocks.Clock;
import assignment.utils.HelperUtils;
import com.jfoenix.controls.JFXToggleButton;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class LauncherController implements Initializable {
    private Map<String, Clock> clocks;
    private Alarm alarm;
    private Timeline playAlarm;

    @FXML
    private HBox alarmBox;
    @FXML
    private StackPane clocksPane;
    @FXML
    private Button resetAllColorsButton;
    @FXML
    private ComboBox<String> switchClockButton;
    @FXML
    private ColorPicker faceColorButton;
    @FXML
    private ColorPicker hourColorButton;
    @FXML
    private ColorPicker minuteColorButton;
    @FXML
    private ColorPicker secondColorButton;
    @FXML
    private ColorPicker bgColorButton;
    @FXML
    private JFXToggleButton alarmButton;

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTooltips();
        initColorButtonStyle();
        initAlarmUI();

        clocks = new HashMap<>();
        alarm = new Alarm();

        resetAllColorsButton.setOnAction((event) -> resetAllColors());
        switchClockButton.valueProperty().addListener((ov, oldClockName, newClockName) -> {
            clocks.values().forEach(clock -> clock.hideClock());
            Clock newClock = clocks.get(newClockName);
            newClock.getClockPane().toFront();
            newClock.showClock();
        });


    }

    private void initAlarmUI() {
        // Creating Alarm UI
        try {
            FileInputStream input = new FileInputStream(HelperUtils.getResourceLocation("images/alarm.png").getFile());
            Image image = new Image(input);
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(20);
            imageView.setFitWidth(20);
            playAlarm = new Timeline();
            playAlarm.setCycleCount(Animation.INDEFINITE);
            playAlarm.getKeyFrames().addAll(
                    new KeyFrame(Duration.millis(0), new KeyValue(imageView.translateXProperty(), 0)),
                    new KeyFrame(Duration.millis(250), new KeyValue(imageView.translateXProperty(), 2)),
                    new KeyFrame(Duration.millis(500), new KeyValue(imageView.translateXProperty(), 0)),
                    new KeyFrame(Duration.millis(750), new KeyValue(imageView.translateXProperty(), -2)),
                    new KeyFrame(Duration.millis(1000), new KeyValue(imageView.translateXProperty(), 0))
            );

            Label label = new Label("");
            label.setAlignment(Pos.CENTER);
            label.setContentDisplay(ContentDisplay.CENTER);

            alarmBox.setVisible(false);
            alarmBox.setSpacing(5);
            alarmBox.setAlignment(Pos.TOP_LEFT);

            alarmBox.getChildren().addAll(imageView, label);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        alarmButton.setDisableVisualFocus(true);
        alarmButton.selectedProperty().addListener((ov, oldState, newState) -> {
            if (alarmButton.isSelected()) {
                String alarmTime = setAlarm(true);
                if (!alarmTime.isEmpty()) {
                    ((Label) alarmBox.getChildren().get(1)).setText(alarmTime);
                    ImageView img = ((ImageView)alarmBox.getChildren().get(0));

                    playAlarm.playFromStart();
                    alarmBox.setVisible(true);
                } else {
                    ((Label) alarmBox.getChildren().get(1)).setText("");
                    playAlarm.stop();
                    alarmBox.setVisible(false);
                    alarmButton.setSelected(false);
                }
            } else {
                setAlarm(false);
                alarmButton.setSelected(false);
                alarmBox.setVisible(false);
            }
        });
        alarmBox.toFront();
    }

    private void bindColorsProperties(Clock clock) {
        hourColorButton.valueProperty().bindBidirectional(clock.hourColorProperty());
        minuteColorButton.valueProperty().bindBidirectional(clock.minuteColorProperty());
        secondColorButton.valueProperty().bindBidirectional(clock.secondColorProperty());
        faceColorButton.valueProperty().bindBidirectional(clock.faceColorProperty());
        bgColorButton.valueProperty().bindBidirectional(clock.bgColorProperty());
    }

    private void resetAllColors() {
        Clock clock = getCurrentClock();
        clock.resetColors();
    }

    private Clock getCurrentClock() {
        for (Clock clock : clocks.values()) {
            if (clock.getClockPane().isVisible()) return clock;
        }
        throw new RuntimeException("No Visible clock found");
    }

    private void initColorButtonStyle() {
        hourColorButton.setStyle("-fx-color-label-visible: false ;");
        minuteColorButton.setStyle("-fx-color-label-visible: false ;");
        secondColorButton.setStyle("-fx-color-label-visible: false ;");
        faceColorButton.setStyle("-fx-color-label-visible: false ;");
        bgColorButton.setStyle("-fx-color-label-visible: false ;");
    }

    private void initTooltips() {
        this.switchClockButton.setTooltip(new Tooltip("Switch between clock types"));
        this.bgColorButton.setTooltip(new Tooltip("Change clock background color"));
        this.resetAllColorsButton.setTooltip(new Tooltip("Reset all colors to default"));
        this.hourColorButton.setTooltip(new Tooltip("Change hour color"));
        this.minuteColorButton.setTooltip(new Tooltip("Change minute color"));
        this.secondColorButton.setTooltip(new Tooltip("Change second's color"));
        this.faceColorButton.setTooltip(new Tooltip("Change clock face color"));
    }

    private String setAlarm(boolean alarmState) {
        alarm.setAlarm(alarmState);
        return alarm.getAlarmTime();
    }

    public Clock registerClock(FXMLLoader loader, String name) {
        try {
            Pane clockPane = loader.load();
            clockPane.setVisible(false);
            clocksPane.getChildren().add(clockPane);
            Clock clock = loader.getController();
            clock.setName(name);

            if (!switchClockButton.getItems().contains(name)) {
                switchClockButton.getItems().add(name);
            }
            bindColorsProperties(clock);
            clocks.put(name, clock);

            return clock;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to create clock: " + name);
        }
    }

    public void unRegisterClock(Clock clock) {
        if (clocks.containsKey(clock.getName())) {
            clocksPane.getChildren().remove(clock.getClockPane());
            switchClockButton.getItems().remove(clock.getName());
            clocks.remove(clock.getName());
        }
    }
}
