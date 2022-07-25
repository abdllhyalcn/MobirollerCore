package com.mobiroller.core.models.events;

import com.mobiroller.core.jcplayer.JcAudio;

/**
 * Created by ealtaca on 10/30/17.
 */

public class MP3Event {


    private boolean isPlaying;
    private int duration;
    private int currentTime;
    private JcAudio currentJcAudio;
    private String screenId;

    public MP3Event(String screenId,boolean isPlaying, int duration, int currentTime, JcAudio currentJcAudio) {
        this.isPlaying = isPlaying;
        this.duration = duration;
        this.currentTime = currentTime;
        this.currentJcAudio = currentJcAudio;
        this.screenId = screenId;
    }

    public String getScreenId() {
        return screenId;
    }

    public void setScreenId(String screenId) {
        this.screenId = screenId;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    public JcAudio getCurrentJcAudio() {
        return currentJcAudio;
    }

    public void setCurrentJcAudio(JcAudio currentJcAudio) {
        this.currentJcAudio = currentJcAudio;
    }

    @Override
    public String toString() {
        return "MP3Event{" +
                "isPlaying=" + isPlaying +
                ", duration=" + duration +
                ", currentTime=" + currentTime +
                ", currentJcAudio=" + currentJcAudio +
                ", screenId='" + screenId + '\'' +
                '}';
    }
}
