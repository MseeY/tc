package com.mitenotc.bean;

import com.mitenotc.dao.DBHelper;
import com.mitenotc.dao.annotation.Column;
import com.mitenotc.dao.annotation.TableName;

@TableName(DBHelper.HALL_ITEM_TABLE_NAME)
public class HallItemBean {

	@Column(DBHelper.HALL_ITEM_ICONPATH)
	private String iconPath;
	@Column(DBHelper.HALL_ITEM_TITLE)
	private String title;
	@Column(DBHelper.HALL_ITEM_DESC)
	private String desc;
	@Column(DBHelper.HALL_ITEM_ISSUE)
	private String issue;
	@Column(DBHelper.HALL_ITEM_PRIZEICONS)
	private String prizeIcons;
	
	public String getIconPath() {
		return iconPath;
	}
	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
	public String getPrizeIcons() {
		return prizeIcons;
	}
	public void setPrizeIcons(String prizeIcons) {
		this.prizeIcons = prizeIcons;
	}
}
