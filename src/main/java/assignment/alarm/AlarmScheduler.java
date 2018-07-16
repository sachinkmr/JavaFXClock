package assignment.alarm;

import assignment.utils.HelperUtils;
import javafx.animation.Timeline;

import javax.sound.sampled.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

class AlarmScheduler {
    private Timer timer;
    private Date alarmDate;
    private AlarmTimerTask timerTask;

    public AlarmScheduler() {
        timer = new Timer(true);
    }

    public void startAlarm(String alarmText, Timeline alarmAnimation) {
        alarmDate = setAlarmDate(alarmText);
        timer.purge();
        timerTask = new AlarmTimerTask(alarmAnimation);
        timer.schedule(timerTask, alarmDate);
    }

    public void stopAlarm() {
        if (timerTask != null) timerTask.cancel();
        timer.purge();
        alarmDate = null;
    }

    private Date setAlarmDate(String alarmText) {
        LocalTime alarmTime = LocalTime.parse(alarmText, DateTimeFormatter.ofPattern("h:mm a"));
        LocalTime currentTime = LocalTime.now();
        long minutes = alarmTime.isBefore(currentTime) ? 1440 + ChronoUnit.MINUTES.between(currentTime, alarmTime) :
                ChronoUnit.MINUTES.between(currentTime, alarmTime) + 1;
        LocalDateTime dt = LocalDateTime.now(ZoneId.systemDefault());
        LocalDateTime dateTime = dt.plus(java.time.Duration.of(minutes, ChronoUnit.MINUTES)).minusSeconds(dt.getSecond());
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public Date getAlarmDate() {
        if (alarmDate != null) return alarmDate;
        throw new IllegalStateException("Initialize alarm first");
    }

    private static class AlarmTimerTask extends TimerTask {
        private static final long ALARM_PLAY_TIME = 10 * 1000;
        private Timeline alarmAnimation;
        private Clip clip;

        public AlarmTimerTask(Timeline alarmAnimation) {
            this.alarmAnimation = alarmAnimation;
        }

        @Override
        public boolean cancel() {
            boolean isCancelled = super.cancel();
            if (clip != null) {
                clip.stop();
                clip.close();
            }
            return isCancelled;
        }

        @Override
        public void run() {
            setupAudio();
            long currTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - currTime < ALARM_PLAY_TIME) {
                clip.start();
                alarmAnimation.play();
            }
        }

        private void setupAudio() {
            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(HelperUtils.getResourceLocation("alarm.wav"));
                clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}