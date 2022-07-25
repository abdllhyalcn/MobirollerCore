package com.mobiroller.core.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by ealtaca on 07.01.2017.
 */

public class NoteModel implements Serializable {

    private String color;
    private String title;
    private String description;
    private ArrayList<String> imagePaths;
    private Long created_at;
    private Long updated_at;
    private int position;
    private String uniqueId;

    public NoteModel() {
        this.color = "";
        this.title = "";
        this.description = "";
        this.imagePaths = null;
        this.updated_at = null;
        this.created_at = new Date().getTime();
        this.uniqueId = UUID.randomUUID().toString();

    }

    public NoteModel(String description, String title, String color) {
        this.description = description;
        this.title = title;
        this.color = color;
        this.updated_at =null;
        this.created_at = new Date().getTime();
    }

    public NoteModel(String description, String title, String color, ArrayList<String> imagePaths) {
        this.description = description;
        this.title = title;
        this.color = color;
        this.imagePaths = imagePaths;
        this.updated_at =null;
        this.created_at = new Date().getTime();
    }



    public String getTitle() {
        if(title == null)
            return "";
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        if(description == null)
            return "";
        return description;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getImagePaths() {
        if(imagePaths==null)
            imagePaths = new ArrayList<>();
        return imagePaths;
    }

    public void setImagePaths(ArrayList<String> imagePaths) {
        this.imagePaths = imagePaths;
    }

    public Long getCreated_at() {
        return created_at;
    }

    public Long getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at() {
        this.updated_at = new Date().getTime();
    }

    public String getFirstImagePath() {
        if (imagePaths != null && imagePaths.size() != 0)
            return imagePaths.get(0);
        else
            return "";
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


    @Override
    public String toString() {
        return "NoteModel{" +
                "color='" + color + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", imagePaths=" + imagePaths +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                ", position=" + position +
                '}';
    }

    public boolean isUpdated()
    {
        return updated_at != null;
    }
}
