package assignment.clocks.analog;

import assignment.clocks.AbstractClock;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.util.Duration;

public class FuturisticClock extends AnalogClock {

    @FXML
    private Label f_time;
    private Arc hourHand;
    private Arc minuteHand;
    private Arc secondHand;
    private Group ticks;
    private double radius = 180;

    @Override
    public void paintClockFace() {
        ticks = new Group();
        paintDots();
        f_time.setMaxWidth(Double.MAX_VALUE);
        AnchorPane.setLeftAnchor(f_time, 0.0);
        AnchorPane.setRightAnchor(f_time, 0.0);
        f_time.setAlignment(Pos.CENTER);

        this.getClockFace().setStroke(Color.TRANSPARENT);
        this.getClockFace().setFill(this.bgColorProperty().getValue());
    }

    @Override
    protected void paintDots() {
        double dotsPadding = radius * Factor.DOTS_PADDING.value() - 2;

        for (int i = 1; i <= 60; i++) {
            double tickLength = i % 5 == 0 ? 20 : 8;

            Point2D point1 = this.pointFromOrigin(i, radius - tickLength - dotsPadding);
            Point2D point2 = this.pointFromOrigin(i, radius - dotsPadding);
            Line tick = new Line(point1.getX(), point1.getY(), point2.getX(), point2.getY());
            tick.setStrokeWidth(4);
            tick.setStrokeLineCap(StrokeLineCap.ROUND);
            ticks.getChildren().add(tick);
        }
        this.getClockPane().getChildren().add(ticks);
    }

    @Override
    public void resetColors() {
        this.hourColorProperty().setValue(Color.DARKCYAN);
        this.minuteColorProperty().setValue(Color.CORAL);
        this.secondColorProperty().setValue(Color.ORANGE);
        this.faceColorProperty().setValue(Color.MEDIUMSLATEBLUE);
        this.bgColorProperty().setValue(Color.TRANSPARENT);
    }


    @Override
    public void drawHands() {
        double secondHandRadius = radius;
        double minuteHandRadius = secondHandRadius * 0.78;
        double hourHandRadius = minuteHandRadius * 0.5;

        hourHand = drawArc(hourHandRadius, 9);
        minuteHand = drawArc(minuteHandRadius, 9);
        secondHand = drawArc(secondHandRadius, 4);

        this.getClockPane().getChildren().addAll(hourHand, minuteHand, secondHand);
        secondHand.toFront();

        bindColorProperties();
    }

    private Arc drawArc(double arcRadius, int strokeWidth) {
        double center = 190.0;
        Arc arc = new Arc(center, center, arcRadius, arcRadius, 90, 360);
        arc.setFill(Color.TRANSPARENT);
        arc.setStrokeWidth(strokeWidth);
        arc.setStrokeLineCap(StrokeLineCap.ROUND);
        return arc;
    }

    @Override
    public void bindColorProperties() {
        this.bgColorProperty().addListener((observable, oldValue, newValue) -> this.getClockFace().setFill(this.bgColorProperty().getValue()));
        this.hourColorProperty().addListener((observable, oldValue, newValue) -> {
            hourHand.setStroke(this.hourColorProperty().getValue());
            hourHand.setEffect(new DropShadow(10, this.hourColorProperty().getValue()));
        });
        this.minuteColorProperty().addListener((observable, oldValue, newValue) -> {
            minuteHand.setStroke(this.minuteColorProperty().getValue());
            minuteHand.setEffect(new DropShadow(10, this.minuteColorProperty().getValue()));
        });
        this.secondColorProperty().addListener((observable, oldValue, newValue) -> {
            secondHand.setStroke(this.secondColorProperty().getValue());
            secondHand.setEffect(new DropShadow(10, this.secondColorProperty().getValue()));
        });
        this.faceColorProperty().addListener((observable, oldValue, newValue) -> {
            f_time.setTextFill(this.faceColorProperty().getValue());
            f_time.setEffect(new DropShadow(10, this.faceColorProperty().getValue()));
            ticks.getChildren().forEach(tick -> {
                Line line = (Line) tick;
                line.setEffect(new DropShadow(10, this.faceColorProperty().getValue()));
                line.setStroke(this.faceColorProperty().getValue());
            });
        });
    }


    @Override
    public void startClock() {
        timeLine = new Timeline(
                new KeyFrame(Duration.millis(10), t -> {
                    secondHand.setLength(-getSecondAngle());
                    minuteHand.setLength(-getMinuteAngle());
                    hourHand.setLength(-getHourAngle());
                }));

        timeLine.setCycleCount(Animation.INDEFINITE);
        timeLine.play();
    }

    private double getSecondAngle() {
        return AbstractClock.getSeconds() * (360 / 60);
    }

    private double getMinuteAngle() {
        return AbstractClock.getMinutes() * (360 / 60);
    }

    private double getHourAngle() {
        return AbstractClock.getHours() * (360 / 12);
    }
}
