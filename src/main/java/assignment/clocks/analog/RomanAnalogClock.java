package assignment.clocks.analog;

import assignment.clocks.analog.AnalogClock;
import assignment.utils.RomanUtils;
import javafx.geometry.Point2D;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class RomanAnalogClock extends AnalogClock {
    @Override
    public void paintNumbers() {
        double numberPadding = this.getClockFace().getRadius() * Factor.NUMBER_PADDING.value();
        for (int i = 0; i < 60; i += 5) {
            Point2D location = this.pointFromOrigin(i, this.getClockFace().getRadius() - numberPadding);
            Text text = new Text(RomanUtils.toRoman(i / 5 == 0 ? 12 : i / 5));
            createAndAddTextNode(text, location);
        }
    }
}
