package com.mobiroller.core.models.events;

import com.mobiroller.core.jcplayer.JcAudio;

/**
 * Created by ealtaca on 10/31/17.
 */

public class MP3PositionEvent {

    private JcAudio jcAudio;
    private String screenId;
    private boolean isPlaying;

    public MP3PositionEvent(JcAudio jcAudio,String screenId) {
        this.jcAudio = jcAudio;
        this.screenId = screenId;
    }

    public MP3PositionEvent(JcAudio jcAudio,String screenId,boolean isPlaying) {
        this.jcAudio = jcAudio;
        this.screenId = screenId;
        this.isPlaying = isPlaying;
    }

    public JcAudio getJcAudio() {
        return jcAudio;
    }

    public void setJcAudio(JcAudio jcAudio) {
        this.jcAudio = jcAudio;
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
}
