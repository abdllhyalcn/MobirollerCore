package com.mobiroller.core.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ealtaca on 14.12.2016.
 */

public class RssModel implements Serializable {

    String title;
    String description;
    String link;
    String image;
    String guid;
    String author;
    Date pubDate;

    public RssModel() {
    }

    public RssModel(String title,String image, String description, String link,   String author, Date pubDate) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.image = image;
        this.author = author;
        this.pubDate = pubDate;
    }
    public RssModel(String title, String description) {
        this.title = title;
        this.description = description;
    }

    @Override
    public String toString() {
        return "RssModel{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                ", image='" + image + '\'' +
                ", guid='" + guid + '\'' +
                ", author='" + author + '\'' +
                ", pubDate=" + pubDate +
                '}';
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }
}
