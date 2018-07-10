package assignment.alarm;

import java.time.LocalTime;
import java.util.Timer;

public class Alarm {
    private LocalTime alarmTime;
    private Timer timer ;

    public Alarm() {
        timer = new Timer();
    }

    public void startAlarm(LocalTime alarmTime) {
        this.alarmTime = alarmTime;
        timer.cancel();
        timer.purge();
        timer.
    }

    public void stopAlarm() {
    }
}
