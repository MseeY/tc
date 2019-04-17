package com.mitenotc.bean;

import android.widget.ImageView;

/**
 * @author mitenotc
 *
 */
public class HallItem_Bean {

	private int iconId;
	private String title;
	private int[] awardIconIds = new int[3]; //0代表隐藏,1代表显示
	private String desc;
	private String issue;
	private String time;
	public HallItem_Bean(int iconId, String title, int[] awardIconIds,
			String desc, String issue, String time) {
		super();
		this.iconId = iconId;
		this.title = title;
		this.awardIconIds = awardIconIds;
		this.desc = desc;
		this.issue = issue;
		this.time = time;
		
	}
	public int getIconId() {
		return iconId;
	}
	public void setIconId(int iconId) {
		this.iconId = iconId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int[] getAwardIconIds() {
		return awardIconIds;
	}
	public void setAwardIconIds(int[] awardIconIds) {
		this.awardIconIds = awardIconIds;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getIssue() {
		return issue;
	}
	public void setIssue(String issue) {
		this.issue = issue;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
}
