package com.mitenotc.bean;

import android.graphics.Bitmap;

public class ImageBean {
	private String type;
	private String addr;
	private Bitmap bm;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public Bitmap getBm() {
		return bm;
	}

	public void setBm(Bitmap bm) {
		this.bm = bm;
	}

}
