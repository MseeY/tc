package com.mitenotc.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 竞彩对阵pojo父类
 * @author fl
 *
 */
public class JcTeamInfo {
	private String lotteryid="111";	 //string	32	玩法编号
	private String lotteryissue="111"; //string	18	彩票期号
	private String lotttime=String.valueOf(System.currentTimeMillis());;     //string	*	批次时间
	private String lottweek="星期五";     //string	8	批次时间的星期值
	private String starttime=String.valueOf(System.currentTimeMillis());;    //string	*	每批次投注开始时间
	private String endtime=String.valueOf(System.currentTimeMillis());      //string	*	每批次投注截止时间
	private String ballid="11";       //string	8	显示编号
	private String gamename="2014年9月28日星期日10场可投";     //string	5 - 20	赛事名称
	private String gamestarttime=String.valueOf(System.currentTimeMillis());;//string	*	每场比赛开始时间
	private String gameendtime=String.valueOf(System.currentTimeMillis()+2000);;  //string	*	每场比赛投注截止时间
	private String hteam="RB";        //string	5 - 20	主队名称
	private String isconcede="0";    //string	*	是否让球或让分  0：表示不让 -n：表示主让客  n：表示客让主篮彩大小分时，该参数为预设总分
	private String vteam="CN";        //string	5 - 20	客队名称
	private String flag="1";
	private boolean isDan = false;//是否设胆
	private String gameresult="国际A级";	//string	10	赛果
	private String score="1:20";//	string	10	比分

	public String getGameresult() {
		return gameresult;
	}

	public void setGameresult(String gameresult) {
		this.gameresult = gameresult;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}
	/**
	 * 按钮
	 */
	public List<MyBtnPojo> btnPojo;//按钮信息数组

	public List<MyBtnPojo> getBtnPojo() {
		return btnPojo;
	}

	public void setBtnPojo(List<MyBtnPojo> btnPojo) {
		this.btnPojo = btnPojo;
	}

	public JcTeamInfo(){
		btnPojo = new ArrayList<MyBtnPojo>();
	}
	
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
	public boolean isDan() {
		return isDan;
	}

	public void setDan(boolean isDan) {
		this.isDan = isDan;
	}

	/**
	 * 用户选择了几个结果
	 * @return
	 */
	public int itemCheckNum(){
		int num = 0;
		for(int i=0;i<btnPojo.size();i++){
			if(btnPojo.get(i).btnState){
				num++;
			}
		}
		return num;
	}
	public String getCheckStr(){
		String checkStr = "";
		for(int i=0;i<btnPojo.size();i++){
			if(btnPojo.get(i).btnState){
				checkStr += btnPojo.get(i).btnStr;
			}
		}
		return checkStr;
	}
	
	public void initBtnState(){
		for(int i=0;i<btnPojo.size();i++){
			if(btnPojo.get(i).btnState){
				btnPojo.get(i).btnState = false;
			}
		}
	}
	public String getLotteryid() {
		return lotteryid;
	}
	public void setLotteryid(String lotteryid) {
		this.lotteryid = lotteryid;
	}
	public String getLotteryissue() {
		return lotteryissue;
	}
	public void setLotteryissue(String lotteryissue) {
		this.lotteryissue = lotteryissue;
	}
	public String getLotttime() {
		return lotttime;
	}
	public void setLotttime(String lotttime) {
		this.lotttime = lotttime;
	}
	public String getLottweek() {
		return lottweek;
	}
	public void setLottweek(String lottweek) {
		this.lottweek = lottweek;
	}
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public String getBallid() {
		return ballid;
	}
	public void setBallid(String ballid) {
		this.ballid = ballid;
	}
	public String getGamename() {
		return gamename;
	}
	public void setGamename(String gamename) {
		this.gamename = gamename;
	}
	public String getGamestarttime() {
		return gamestarttime;
	}
	public void setGamestarttime(String gamestarttime) {
		this.gamestarttime = gamestarttime;
	}
	public String getGameendtime() {
		return gameendtime;
	}
	public void setGameendtime(String gameendtime) {
		this.gameendtime = gameendtime;
	}
	public String getHteam() {
		return hteam;
	}
	public void setHteam(String hteam) {
		this.hteam = hteam;
	}
	public String getIsconcede() {
		return isconcede;
	}
	public void setIsconcede(String isconcede) {
		this.isconcede = isconcede;
	}
	public String getVteam() {
		return vteam;
	}
	public void setVteam(String vteam) {
		this.vteam = vteam;
	}
	public class MyBtnPojo{
		public boolean btnState = false;
		public String btnStr = "";//按钮代表字面意思
		public String btnCode = "";//按钮代表的投注意思
		public String btnSp = "";//按钮下的赔率
		public MyBtnPojo(String btnStr,String btnCode,String btnSp){
			this.btnStr = btnStr;
			this.btnCode = btnCode;
			this.btnSp = turnSp(btnSp);
		}
		private String turnSp(String btnSp){
			if(btnSp.equals("null")||btnSp.equals("")){
				return "-";
			}else{
				return btnSp;
			}
		}
	}
}
