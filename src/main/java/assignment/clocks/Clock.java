package assignment.clocks;

import javafx.animation.Animation;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.ResourceBundle;

public abstract class Clock implements Initializable {
    private Timeline timeLine;
    @FXML
    private AnchorPane clockPane;
    private String name;
    private ObjectProperty<Color> hourColor;
    private ObjectProperty<Color> minuteColor;
    private ObjectProperty<Color> secondColor;
    private ObjectProperty<Color> faceColor;
    private ObjectProperty<Color> bgColor;
    private SimpleDoubleProperty hour;
    private SimpleDoubleProperty minute;
    private SimpleDoubleProperty second;


    protected Clock() {
        timeLine = new Timeline();
        timeLine.setCycleCount(Animation.INDEFINITE);

        // Setting clock color
        hourColor = new SimpleObjectProperty<>(Color.valueOf("#6b6969"));
        minuteColor = new SimpleObjectProperty<>(Color.valueOf("#6b6969"));
        secondColor = new SimpleObjectProperty<>(Color.valueOf("red"));
        faceColor = new SimpleObjectProperty<>(Color.valueOf("#6b6969"));
        bgColor = new SimpleObjectProperty<>(Color.valueOf("white"));

        // Setting Clock time
        hour = new SimpleDoubleProperty(Clock.getHours());
        minute = new SimpleDoubleProperty(Clock.getMinutes());
        second = new SimpleDoubleProperty(Clock.getSeconds());
    }

    /**
     * Calculate local system clock hours.
     *
     * @return precise hours up to 6 decimal points
     */
    public static double getHours() {
        double hours = LocalTime.now(ZoneId.systemDefault()).getHour() + getMinutes() / 60;
        return hours >= 12 ? hours - 12 : hours;
    }

    /**
     * Calculate local system clock minutes.
     *
     * @return precise minutes up to 6 decimal points
     */
    public static double getMinutes() {
        return LocalTime.now(ZoneId.systemDefault()).getMinute() + getSeconds() / 60;
    }

    /**
     * Calculate local system clock seconds.
     *
     * @return precise seconds up to 6 decimal points
     */
    public static double getSeconds() {
        LocalTime time = LocalTime.now(ZoneId.systemDefault());
        return time.getSecond() + (double) time.getNano() / 1000000000;
    }

    /**
     * Calculate local system clock AM or PM.
     *
     * @return AM or PM for current time
     */
    public static String getAM_PM() {
        return LocalTime.now(ZoneId.systemDefault()).getHour() < 12 ? "AM" : "PM";
    }

    /**
     * Use to create clock UI from fxml file.
     *
     * @param location
     * @param resources
     */
    @Override
    public final void initialize(URL location, ResourceBundle resources) {
        initClockUI();
        resetColors();
        startClock(timeLine);
    }

    /**
     * Implement this Method to draw/paint clock UI.
     */
    protected abstract void initClockUI();

    /**
     * Implement animation logic here with pre initialized timeline object
     */
    protected abstract void startClock(Timeline timeLine);

    protected SimpleDoubleProperty hourProperty() {
        return hour;
    }

    protected SimpleDoubleProperty minuteProperty() {
        return minute;
    }

    protected SimpleDoubleProperty secondProperty() {
        return second;
    }

    public void hideClock() {
        clockPane.setVisible(false);
        timeLine.pause();
    }

    public void showClock() {
        timeLine.playFromStart();
        clockPane.setVisible(true);
    }

    public void resetColors() {
        this.hourColor.setValue(Color.valueOf("#6b6969"));
        this.minuteColor.setValue(Color.valueOf("#6b6969"));
        this.secondColor.setValue(Color.valueOf("red"));
        this.faceColor.setValue(Color.valueOf("#6b6969"));
        this.bgColor.setValue(Color.valueOf("white"));
    }

    public void setName(String name) {
        this.name = name;
    }

    public AnchorPane getClockPane() {
        return this.clockPane;
    }

    public ObjectProperty<Color> hourColorProperty() {
        return this.hourColor;
    }

    public ObjectProperty<Color> minuteColorProperty() {
        return this.minuteColor;
    }

    public ObjectProperty<Color> secondColorProperty() {
        return this.secondColor;
    }

    public ObjectProperty<Color> faceColorProperty() {
        return this.faceColor;
    }

    public ObjectProperty<Color> bgColorProperty() {
        return this.bgColor;
    }

    public Timeline getTimeLine() {
        return timeLine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Clock clock = (Clock) o;
        return Objects.equals(this.name, clock.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }
}
