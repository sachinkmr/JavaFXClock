package assignment;

import assignment.alarm.Alarm;
import assignment.alarm.AlarmUI;
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

        clocks = new HashMap<>();
        AlarmUI alarmUI = new AlarmUI(alarmButton);
        alarmUI.initAlarmUI();


        resetAllColorsButton.setOnAction((event) -> resetAllColors());
        switchClockButton.valueProperty().addListener((ov, oldClockName, newClockName) -> {
            clocks.values().forEach(clock -> clock.hideClock());
            Clock newClock = clocks.get(newClockName);
            newClock.getClockPane().toFront();
            newClock.showClock();
        });
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
