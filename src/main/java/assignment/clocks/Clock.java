package assignment.clocks;

import javafx.beans.property.ObjectProperty;
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

    private ObjectProperty<Color> hourColor;
    private ObjectProperty<Color> minuteColor;
    private ObjectProperty<Color> secondColor;
    private ObjectProperty<Color> faceColor;
    private ObjectProperty<Color> bgColor;

    @FXML
    protected AnchorPane clockPane;
    protected String name;

    public Clock() {
        hourColor = new SimpleObjectProperty<>(Color.valueOf("#6b6969"));
        minuteColor = new SimpleObjectProperty<>(Color.valueOf("#6b6969"));
        secondColor = new SimpleObjectProperty<>(Color.valueOf("red"));
        faceColor = new SimpleObjectProperty<>(Color.valueOf("#6b6969"));
        bgColor = new SimpleObjectProperty<>(Color.valueOf("white"));
    }

    @Override
    public final void initialize(URL location, ResourceBundle resources) {
        paintClockFace();
        drawHands();
        resetColors();
        startClock();
    }

    protected abstract void paintClockFace();

    protected abstract void drawHands();

    protected abstract void startClock();

    public void hideClock() {
        clockPane.setVisible(false);
    }

    public void showClock() {
        clockPane.setVisible(true);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void resetColors() {
        this.hourColor.setValue(Color.valueOf("#6b6969"));
        this.minuteColor.setValue(Color.valueOf("#6b6969"));
        this.secondColor.setValue(Color.valueOf("red"));
        this.faceColor.setValue(Color.valueOf("#6b6969"));
        this.bgColor.setValue(Color.valueOf("white"));
    }

    public String getName() {
        return name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Clock clock = (Clock) o;
        return Objects.equals(this.getName(), clock.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getName());
    }

    public static double getHours() {
        double hours = LocalTime.now(ZoneId.systemDefault()).getHour()+getMinutes()/60;
        return hours >= 12 ? hours - 12 : hours;
    }

    public static double getMinutes() {
        return LocalTime.now(ZoneId.systemDefault()).getMinute()+getSeconds()/60;
    }

    public static double getSeconds() {
        LocalTime time = LocalTime.now(ZoneId.systemDefault());
        return time.getSecond() + (double) time.getNano()/1000000000;
    }

    public static String getAM_PM() {
        return LocalTime.now(ZoneId.systemDefault()).getHour() < 12 ? "AM" : "PM";
    }
}
