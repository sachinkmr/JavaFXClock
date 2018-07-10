package assignment.clocks.analog;

import assignment.clocks.Clock;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class AnalogClock extends Clock {
    @FXML
    private Circle clockFace;
    @FXML
    private Circle origin;

    private Line hourHand;
    private Line minuteHand;
    private Line secondHand;
    protected SimpleDoubleProperty hour;
    protected SimpleDoubleProperty minute;
    protected SimpleDoubleProperty second;

    public AnalogClock() {
        hour = new SimpleDoubleProperty(Clock.getHours());
        minute = new SimpleDoubleProperty(Clock.getMinutes());
        second = new SimpleDoubleProperty(Clock.getSeconds());
    }

    private Rotate getRotate() {
        Rotate rotate = new Rotate();
        rotate.setPivotX(clockFace.getLayoutX());
        rotate.setPivotY(clockFace.getLayoutY());
        return rotate;
    }

    @Override
    public void startClock() {
        origin.toFront();

        Timeline tl = new Timeline();
        tl.setCycleCount(Animation.INDEFINITE);
        tl.getKeyFrames().add(new KeyFrame(Duration.millis(10), (event -> {
            hour.setValue(Clock.getHours());
            minute.setValue(Clock.getMinutes());
            second.setValue(Clock.getSeconds());
        })));
        tl.play();
    }

    @Override
    public void paintClockFace() {
        paintDots();
        paintNumbers();
    }

    @Override
    public void drawHands() {
        // Drawing second hand
        Point2D endPoint = pointFromOrigin(0, clockFace.getRadius() * Factor.SECOND_HAND.value());
        secondHand = createHand(second.multiply(360 / 60), endPoint, 2);
        secondHand.setStartY(clockFace.getLayoutY() + clockFace.getRadius() * Factor.SECOND_HAND_BACK_LENGTH.value());

        // Drawing minute hand
        endPoint = pointFromOrigin(0, clockFace.getRadius() * Factor.MINUTE_HAND.value());
        minuteHand = createHand(minute.multiply(360 / 60), endPoint, 3);

        // Drawing hour hand
        endPoint = pointFromOrigin(0, clockFace.getRadius() * Factor.HOUR_HAND.value());
        hourHand = createHand(hour.multiply(360 / 12), endPoint, 3);

        clockPane.getChildren().addAll(hourHand, minuteHand, secondHand);
        bindColorProperties();
    }

    protected void bindColorProperties() {
        hourHand.strokeProperty().bind(this.hourColorProperty());
        minuteHand.strokeProperty().bind(this.minuteColorProperty());
        secondHand.strokeProperty().bind(this.secondColorProperty());
        origin.fillProperty().bind(this.faceColorProperty());
        clockFace.strokeProperty().bind(this.faceColorProperty());
        clockFace.fillProperty().bind(this.bgColorProperty());
    }

    protected Line createHand(DoubleBinding timeAngleProperty, Point2D endPoint, double strokeWidth) {
        Line line = new Line();
        line.setStartX(clockFace.getLayoutX());
        line.setStartY(clockFace.getLayoutY());
        line.setEndX(endPoint.getX());
        line.setEndY(endPoint.getY());
        line.setStrokeWidth(strokeWidth);
        line.toFront();

        // adding and binding rotation
        Rotate rotate = getRotate();
        rotate.angleProperty().bind(timeAngleProperty);
        line.getTransforms().add(rotate);
        return line;
    }

    protected void paintDots() {
        double dotsPadding = clockFace.getRadius() * Factor.DOTS_PADDING.value();
        for (int i = 1; i <= 60; i++) {
            Point2D location = this.pointFromOrigin(i, clockFace.getRadius() - dotsPadding);
            double dotRadius = i % 5 == 0 ? 3.5 : 1.5;
            Circle dot = new Circle(location.getX(), location.getY(), dotRadius);
            dot.fillProperty().bind(this.faceColorProperty());
            clockPane.getChildren().add(dot);
        }
    }

    protected void paintNumbers() {
        double numberPadding = clockFace.getRadius() * Factor.NUMBER_PADDING.value();
        for (int i = 0; i < 60; i += 5) {
            Point2D location = this.pointFromOrigin(i, clockFace.getRadius() - numberPadding);
            Text text = new Text(Integer.toString(i / 5 == 0 ? 12 : i / 5));
            createAndAddTextNode(text, location);
        }
    }

    protected void createAndAddTextNode(Text text, Point2D location) {
        text.setX(location.getX());
        text.setY(location.getY());
        text.setLayoutX(-5);
        text.setLayoutY(5);
        text.fillProperty().bind(this.faceColorProperty());
        text.setFont(Font.font(text.getFont().getFamily(), FontWeight.BOLD, text.getFont().getSize() + 1));
        clockPane.getChildren().add(text);
    }

    protected Point2D pointFromOrigin(double angle, double distanceFromOrigin) {
        double t = 2 * Math.PI * (angle - 15) / 60;
        double x = clockPane.getPrefWidth() / 2 + distanceFromOrigin * Math.cos(t);
        double y = clockPane.getPrefHeight() / 2 + distanceFromOrigin * Math.sin(t);
        return new Point2D(x, y);
    }

    protected enum Factor {
        DOTS_PADDING(0.055),
        NUMBER_PADDING(0.125),
        SECOND_HAND_BACK_LENGTH(.1315),
        HOUR_HAND(0.6),
        MINUTE_HAND(0.9),
        SECOND_HAND(0.95);

        private final double factor;

        Factor(double factor) {
            this.factor = factor;
        }

        public double value() {
            return this.factor;
        }
    }

    public Circle getClockFace() {
        return clockFace;
    }

    protected Circle getOrigin() {
        return origin;
    }
}
