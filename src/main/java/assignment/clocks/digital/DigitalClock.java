package assignment.clocks.digital;

import assignment.clocks.Clock;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import java.time.LocalTime;

public class DigitalClock extends Clock {
    @FXML
    private HBox labels;
    @FXML
    private Label hours;
    @FXML
    private Label minutes;
    @FXML
    private Label seconds;
    @FXML
    private Label colon1;
    @FXML
    private Label colon2;
    @FXML
    private Label am_pm;

    @Override
    protected void startClock() {
        // the digital clock updates once a second.
        final Timeline digitalTime = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        actionEvent -> drawHands()),
                new KeyFrame(Duration.seconds(1))
        );

        digitalTime.setCycleCount(Animation.INDEFINITE);
        digitalTime.play();
    }

    @Override
    public void paintClockFace() {
        bindColorProperties();
    }

    protected void bindColorProperties() {
        labels.backgroundProperty().setValue(new Background(new BackgroundFill(this.bgColorProperty().getValue(), null, null)));
        this.bgColorProperty().addListener((observable, oldValue, newValue) -> labels.setBackground(new Background(new BackgroundFill(this.bgColorProperty().getValue(), null, null))));
        this.faceColorProperty().addListener((observable, oldValue, newValue) -> labels.setStyle("-fx-border-color: #" + Integer.toHexString(this.faceColorProperty().getValue().hashCode())));
        hours.textFillProperty().bind(this.hourColorProperty());
        minutes.textFillProperty().bind(this.minuteColorProperty());
        seconds.textFillProperty().bind(this.secondColorProperty());
        colon1.textFillProperty().bind(this.faceColorProperty());
        colon2.textFillProperty().bind(this.faceColorProperty());
        am_pm.textFillProperty().bind(this.faceColorProperty());
    }

    @Override
    public void drawHands() {
        hours.setText(getHourString());
        minutes.setText(getMinuteString());
        seconds.setText(getSecondString());
        am_pm.setText(Clock.getAM_PM());
        if (LocalTime.now().getSecond() % 2 == 0) {
            colon1.setVisible(true);
            colon2.setVisible(true);
        } else {
            colon1.setVisible(false);
            colon2.setVisible(false);
        }
    }

    protected String getHourString() {
        return pad(2, (int) Clock.getHours() == 0 ? 12 : (int) Clock.getHours());
    }

    protected String getMinuteString() {
        return pad(2, (int) Clock.getMinutes());
    }

    protected String getSecondString() {
        return pad(2, (int) Clock.getSeconds());
    }

    protected String pad(int paddingCount, int s) {
        StringBuilder sb = new StringBuilder();
        String str = Integer.toString(s);
        for (int i = str.length(); i < paddingCount; i++) {
            sb.append('0');
        }
        sb.append(s);
        return sb.toString();
    }
}
