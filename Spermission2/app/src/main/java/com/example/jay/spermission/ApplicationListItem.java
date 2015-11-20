package com.example.jay.spermission;

import android.graphics.drawable.Drawable;

public class ApplicationListItem {
	private long id;
	private Drawable icon;
	private String text;
	
	
	public ApplicationListItem(long id, Drawable icon, String text) {
		this.id = id;
		this.icon = icon;
		this.text = text;
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public Drawable getIcon() {
		return icon;
	}


	public void setIcon(Drawable icon) {
		this.icon = icon;
	}


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}
	
	
}
