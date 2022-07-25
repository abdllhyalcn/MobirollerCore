package com.mobiroller.core.jcplayer;


import java.io.Serializable;

/**
 * Created by jean on 27/06/16.
 */

public class JcAudio implements Serializable {
    private long id;
    private String title;
    private int position;
    private String path;
    private Origin origin;


    public JcAudio(String title, String path, Origin origin) {
        // It looks bad
        //int randomNumber = path.length() + title.length();

        // We init id  -1 and position with -1. And let JcPlayerView define it.
        // We need to do this because there is a possibility that the user reload previous playlist
        // from persistence storage like sharedPreference or SQLite.
        this.id = -1;
        this.position = -1;
        this.title = title;
        this.path = path;
        this.origin = origin;
    }

    public JcAudio(String path) {
        // It looks bad
        //int randomNumber = path.length() + title.length();

        // We init id  -1 and position with -1. And let JcPlayerView define it.
        // We need to do this because there is a possibility that the user reload previous playlist
        // from persistence storage like sharedPreference or SQLite.
        this.path = path;
    }

    public JcAudio(String title, String path, long id, int position, Origin origin) {
        this.id = id;
        this.position = position;
        this.title = title;
        this.path = path;
        this.origin = origin;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Origin getOrigin() {
        return origin;
    }


    public static JcAudio createFromURL(String title, String url) {
        return new JcAudio(title, url, Origin.URL);
    }
}