package com.mitenotc.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mitenotc.tc.MyApp;
import com.mitenotc.utils.BaseCalc;
/**
 * 
 * 智能投注方案
 * @author wanli
 *
 */
public class RATEBean {
	
	private static  RATEBean instance;
	public static RATEBean getInstance(){
		if(instance==null){
			instance=new RATEBean();
		}
		return instance;
	}
//	彩种
	private String lottery="";
	
	//当前销售时间
	private  String NOW_TIME;
	//当前销售期号 有服务器返回的当前销售期决定
	private  String NOW_QH="";
    //最大序号  默认10  等同于修改的连续追号
    private  String DEFAULT_XH="10";
//	最大追期号
    private  String MAX_XH="10";
    //倍数默认为 1
    private String BS="1";
    
//	注数
	private int lotteryCount = 10;
    
    //彩种奖金指默认一注时候的中奖金额
    
    private List<Integer> bonusScopeList=new ArrayList<Integer>();//  一票奖金范围

    /**
     * 预期盈利方案中 类  
     *  1  只有全程最低盈利率
     *  
     *  2 前 n 期 最低为 m %盈利率
     *    
     *  3 全程最低盈利  y  元
     */
  //默认方案为1 只有全程最低盈利率为30%
    private  int FANG_AN=1;
    
  //全局默認為方案一  ：
  //  方案为 1 时候 盈利率  默认30 %  
    private  String ONE_YLL="30"; 
    
   //方案为 2 时候 前n期  默认为前 5 期
    private  String TWO_QH="5"; 
   //方案为 2 时候 前n期 盈利率  默认为50  %
    private  String TWO_QYLL="50"; 
   //方案为 2 时候 前n期 之后盈利率  默认为20  %
    private  String TWO_HYLL="20"; 
    //  算法所需要的三个参数信息         
    private  Map<String, String> TWO_FAN_MAP;
   
    //方案为 3 时候 最低盈利金额 默认为30 元
    private  String THREE_RMB="30";
  //期数对应 倍数 追期一一对应的投注倍数
    private List<String> ALL_BS_LIST;
  //期数            根据修改页决定的最大追期数 决定所有的销售期号
    private  List<String> ALL_QH_LIST;

    
    
//    计算结果map
    private Map<String, List<String>> CALC_MAP;

    
    
	/**
	 * @return the cALC_MAP
	 */
	public Map<String, List<String>> getCALC_MAP() {
		return CALC_MAP;
	}
	/**
	 * @param cALC_MAP the cALC_MAP to set
	 */
	public void setCALC_MAP(Map<String, List<String>> cALC_MAP) {
		CALC_MAP = cALC_MAP;
	}
	/**
	 * @return the aLL_BS_LIST
	 */
	public List<String> getALL_BS_LIST() {
		return ALL_BS_LIST;
	}
	/**
	 * @return the aLL_QH_LIST
	 */
	public List<String> getALL_QH_LIST() {
		return ALL_QH_LIST;
	}
	/**
	 * @return the nOW_TIME
	 */
	public String getNOW_TIME() {
		return NOW_TIME;
	}
	/**
	 * @param nOW_TIME the nOW_TIME to set
	 */
	public void setNOW_TIME(String nOW_TIME) {
		NOW_TIME = nOW_TIME;
	}
	/**
	 * @return the nOW_QH
	 */
	public String getNOW_QH() {
		return NOW_QH;
	}
	/**
	 * @param nOW_QH the nOW_QH to set
	 */
	public void setNOW_QH(String nOW_QH) {
		NOW_QH = nOW_QH;
	}
	/**
	 * @return the mAX_XH
	 */
	public String getMAX_XH() {
		return MAX_XH;
	}
	/**
	 * @param mAX_XH the mAX_XH to set
	 */
	public  void setMAX_XH(String mAX_XH) {
		MAX_XH = mAX_XH;
	}
	/**
	 * @return the bS
	 */
	public  String getBS() {
		return BS;
	}
	/**
	 * @param bS the bS to set
	 */
	public  void setBS(String bS) {
		this.BS = bS;
	}
	/**
	 * @return the fANG_AN
	 */
	public  int getFANG_AN() {
		return FANG_AN;
	}
	/**
	 * @param fANG_AN the fANG_AN to set
	 */
	public  void setFANG_AN(int fANG_AN) {
		FANG_AN = fANG_AN;
	}



	public String getONE_YLL() {
		return ONE_YLL;
	}
	public void setONE_YLL(String oNE_YLL) {
		ONE_YLL = oNE_YLL;
	}
	
	
	public String getTWO_QH() {
		return TWO_QH;
	}
	public void setTWO_QH(String tWO_QH) {
		TWO_QH = tWO_QH;
	}
	public String getTWO_QYLL() {
		return TWO_QYLL;
	}
	public void setTWO_QYLL(String tWO_QYLL) {
		TWO_QYLL = tWO_QYLL;
	}
	public String getTWO_HYLL() {
		return TWO_HYLL;
	}
	public void setTWO_HYLL(String tWO_HYLL) {
		TWO_HYLL = tWO_HYLL;
	}

	public String getTHREE_RMB() {
		return THREE_RMB;
	}
	public void setTHREE_RMB(String tHREE_RMB) {
		THREE_RMB = tHREE_RMB;
	}
	/**
	 * @param aLL_BS_LIST the aLL_BS_LIST to set
	 */
	public void setALL_BS_LIST(List<String> aLL_BS_LIST) {
		ALL_BS_LIST = aLL_BS_LIST;
	}

	/**
	 * @param aLL_QH_LIST the aLL_QH_LIST to set
	 */
	public void setALL_QH_LIST(List<String> aLL_QH_LIST) {
		ALL_QH_LIST = aLL_QH_LIST;
	}

	public Map<String, String> getTWO_FAN_MAP() {
		return TWO_FAN_MAP;
	}
	public void setTWO_FAN_MAP(Map<String, String> tWO_FAN_MAP) {
		TWO_FAN_MAP = tWO_FAN_MAP;
	}
	public String getLottery() {
		return lottery;
	}
	public void setLottery(String lottery) {
		this.lottery = lottery;
	}
	public List<Integer> getBonusScopeList() {
		return bonusScopeList;
	}
	public void setBonusScopeList(List<Integer> bonusScopeList) {
		this.bonusScopeList = bonusScopeList;
	}
	public int getLotteryCount() {
		return lotteryCount;
	}
	public void setLotteryCount(int lotteryCount) {
		this.lotteryCount = lotteryCount;
	}
	public String getDEFAULT_XH() {
		return DEFAULT_XH;
	}
	public void setDEFAULT_XH(String dEFAULT_XH) {
		DEFAULT_XH = dEFAULT_XH;
	}
   
     
}
