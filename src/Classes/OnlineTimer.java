package Classes;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class OnlineTimer {

    private Label timeLabel;
    private double millis;
    private double playMillis;
    private double pauseMillis;
    private Timeline timeline;

    public OnlineTimer(Label timeLabel) {
        this.timeLabel = timeLabel;
        playMillis = System.currentTimeMillis();
        millis = 0;
    }

    public String getTimeInFormat(long millis) {
        Date d = new Date(millis);
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss"); // HH for 0-23
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df.format(d);
    }

    public void run() {
        timeline = new Timeline(new KeyFrame(
            Duration.millis(1000),
            event -> {
                millis = (System.currentTimeMillis() - playMillis);
                String time = getTimeInFormat((long) millis);
                timeLabel.setText(String.valueOf(time));
            }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void Play() {
        if (timeline.getStatus() == Animation.Status.PAUSED || timeline.getStatus() == Animation.Status.STOPPED) {
            playMillis = System.currentTimeMillis() - (pauseMillis - playMillis);
            timeline.play();
            System.out.println(timeline.getStatus());
        }
    }

    public void Pause() {
        if (timeline.getStatus() == Animation.Status.RUNNING) {
            pauseMillis = System.currentTimeMillis();
            timeline.pause();
            System.out.println(timeline.getStatus());
        }
    }

    public void Stop() {
        if (timeline.getStatus() == Animation.Status.RUNNING || timeline.getStatus() == Animation.Status.PAUSED) {
            playMillis = 0;
            pauseMillis = 0;
            timeline.stop();
            timeLabel.setText(String.valueOf(getTimeInFormat(0)));
            System.out.println(timeline.getStatus());
        }
    }

    public double getMillis() {
        return millis;
    }
}