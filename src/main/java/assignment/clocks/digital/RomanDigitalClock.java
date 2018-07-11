package assignment.clocks.digital;

import assignment.clocks.AbstractClock;
import assignment.utils.RomanUtils;

public class RomanDigitalClock extends DigitalClock {
    @Override
    public String getHourString() {
        return pad(4, (int) AbstractClock.getHours() == 0 ? 12 : (int) AbstractClock.getHours());
    }

    @Override
    public String getMinuteString() {
        return pad(7, (int) AbstractClock.getMinutes());
    }

    @Override
    public String getSecondString() {
        return pad(7, (int) AbstractClock.getSeconds());
    }


    @Override
    public String pad(int paddingCount, int number) {
        StringBuilder sb = new StringBuilder();
        String str = number == 0 ? "0" : RomanUtils.toRoman(number);
        for (int i = str.length(); i < 7; i++) {
            sb.append(' ');
        }
        sb.append(str);
        return sb.toString();
    }
}
