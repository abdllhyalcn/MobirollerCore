package com.mobiroller.core.views;

import android.graphics.drawable.Drawable;

public class NavDrawerItem {
	
	private String title;
	private Drawable icon;
	private String count = "0";
	private String imageUrl;
	private int drawable;
	public int textColor;
	// boolean to set visiblity of the counter
	private boolean isCounterVisible = false;
	
	public NavDrawerItem(){}

	public NavDrawerItem(String title, Drawable icon){
		this.title = title;
		this.icon = icon;
	}
	public NavDrawerItem(String title, String imageUrl){
		this.title = title;
		this.imageUrl = imageUrl;
	}

	public NavDrawerItem(String title, String imageUrl, int textColor) {
		this.title = title;
		this.imageUrl = imageUrl;
		this.textColor = textColor;
	}

	public NavDrawerItem(String title, int drawable){
		this.title = title;
		this.drawable = drawable;
	}
	
	public NavDrawerItem(String title, Drawable icon, boolean isCounterVisible, String count){
		this.title = title;
		this.icon = icon;
		this.isCounterVisible = isCounterVisible;
		this.count = count;
	}

	public int getDrawable() {
		return drawable;
	}

	public void setDrawable(int drawable) {
		this.drawable = drawable;
	}

	public boolean isCounterVisible() {
		return isCounterVisible;
	}

	public void setCounterVisible(boolean counterVisible) {
		isCounterVisible = counterVisible;
	}

	public String getTitle(){
		return this.title;
	}
	
	public Drawable getIcon(){
		return this.icon;
	}
	
	public String getCount(){
		return this.count;
	}
	
	public boolean getCounterVisibility(){
		return this.isCounterVisible;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public void setIcon(Drawable icon){
		this.icon = icon;
	}
	
	public void setCount(String count){
		this.count = count;
	}
	
	public void setCounterVisibility(boolean isCounterVisible){
		this.isCounterVisible = isCounterVisible;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}
