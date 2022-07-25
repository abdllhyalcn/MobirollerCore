package com.mobiroller.core.models;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by ealtaca on 30.12.2016.
 */

public class TodoModel implements Serializable{
    public enum Color {
        RED, YELLOW, GREEN
    }

    private String text;
    private Color color;
    private boolean checked;
    private String uniqueId;

    public TodoModel(){
        this.text = "";
        this.color = Color.YELLOW;
        this.checked = false;
    }

    public TodoModel(TodoModel ti){
        this.text = ti.getText();
        this.color = ti.getColor();
        this.checked = ti.getChecked();
    }

    static public TodoModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, TodoModel.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean getChecked(){
        return checked;
    }

    public void setChecked(boolean c){
        checked = c;
    }

    public void toggleChecked(){
        checked = !checked;
    }

    @Override
    public String toString() {
        return "[ text: " + text + ",  color: " + color + ", checked: " + checked + "]";
    }
}
