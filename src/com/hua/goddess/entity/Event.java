/**
 * 
 */
package com.hua.goddess.entity;

import android.R.string;

/**
 * @author Administrator
 * 
 */
public class Event {

	private String ID;
	private String date;
	private String content;
	private String fenlei;

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFenlei() {
		return fenlei;
	}

	public void setFenlei(String fenlei) {
		this.fenlei = fenlei;
	}

}
