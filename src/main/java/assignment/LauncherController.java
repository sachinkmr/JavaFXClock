package assignment;

import assignment.alarm.AlarmUI;
import assignment.clocks.AbstractClock;
import assignment.utils.HelperUtils;
import com.jfoenix.controls.JFXToggleButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class LauncherController implements Initializable {
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
    private Pane buttonPanel;
    @FXML
    private Button closeButton;
    private Map<String, AbstractClock> clocks;

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTooltips();
        initColorButtonStyle();

        clocks = new HashMap<>();
        AlarmUI alarmUI = new AlarmUI(alarmButton);
        buttonPanel.getChildren().add(alarmUI.initAlarmUI());

        bindColorButtonsToClock();
        resetAllColorsButton.setOnAction((event) -> resetAllColors());
        closeButton.setOnAction(event -> ((Stage)clocksPane.getScene().getWindow()).close());

        switchClockButton.valueProperty().addListener((ov, oldClockName, newClockName) -> {
            clocks.values().forEach(clock -> clock.hideClock());
            AbstractClock newClock = clocks.get(newClockName);
            newClock.getClockPane().toFront();
            changeColorPickerButtons(newClock);
            newClock.showClock();
        });
    }

    public void resetAllColors() {
        AbstractClock clock = getCurrentClock();
        clock.resetColors();
        changeColorPickerButtons(clock);
    }

    private void changeColorPickerButtons(AbstractClock clock) {
        hourColorButton.valueProperty().setValue(clock.hourColorProperty().getValue());
        minuteColorButton.valueProperty().setValue(clock.minuteColorProperty().getValue());
        secondColorButton.valueProperty().setValue(clock.secondColorProperty().getValue());
        faceColorButton.valueProperty().setValue(clock.faceColorProperty().getValue());
        bgColorButton.valueProperty().setValue(clock.bgColorProperty().getValue());
    }

    private AbstractClock getCurrentClock() {
        for (AbstractClock clock : clocks.values()) {
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

    public AbstractClock registerClock(String fxmlPath, String name) {
        try {
            FXMLLoader loader = new FXMLLoader(HelperUtils.getResourceLocation(fxmlPath));
            Pane clockPane = loader.load();
            clockPane.setVisible(false);
            clocksPane.getChildren().add(clockPane);
            AbstractClock clock = loader.getController();
            clock.setName(name);

            if (!switchClockButton.getItems().contains(name)) {
                switchClockButton.getItems().add(name);
            }
            clocks.put(name, clock);
            return clock;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to create clock: " + name);
        }
    }

    private void bindColorButtonsToClock() {
        hourColorButton.setOnAction((event -> getCurrentClock().hourColorProperty().setValue(hourColorButton.getValue())));
        minuteColorButton.setOnAction((event -> getCurrentClock().minuteColorProperty().setValue(minuteColorButton.getValue())));
        secondColorButton.setOnAction((event -> getCurrentClock().secondColorProperty().setValue(secondColorButton.getValue())));
        faceColorButton.setOnAction((event -> getCurrentClock().faceColorProperty().setValue(faceColorButton.getValue())));
        bgColorButton.setOnAction((event -> getCurrentClock().bgColorProperty().setValue(bgColorButton.getValue())));

    }
}
