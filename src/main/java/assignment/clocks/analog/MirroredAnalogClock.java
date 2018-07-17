package assignment.clocks.analog;

import assignment.clocks.Clock;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class MirroredAnalogClock extends AnalogClock {

    @Override
    public void paintNumbers() {
        double numberPadding = this.getClockFace().getRadius() * Factor.NUMBER_PADDING.value();
        for (int i = 0, x = 60; i < 60; i += 5, x -= 5) {
            Point2D location = this.pointFromOrigin(i, this.getClockFace().getRadius() - numberPadding);
            Text text = new Text(Integer.toString(x / 5 == 0 ? 12 : x / 5));
            createAndAddTextNode(text, location);
        }
    }

    @Override
    public void startClock(Timeline timeLine) {
        getOrigin().toFront();

        timeLine.setCycleCount(Animation.INDEFINITE);
        timeLine.getKeyFrames().add(new KeyFrame(Duration.millis(10), (event -> {
            hourProperty().setValue(-Clock.getHours());
            minuteProperty().setValue(-Clock.getMinutes());
            secondProperty().setValue(-Clock.getSeconds());
        })));

        timeLine.play();
    }
}
