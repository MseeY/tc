package com.mitenotc.bean;

import java.util.List;
import java.util.Map;

/**
 * 双色球投注信息
 * @author wanli
 *
 */
public class Sspbean {
	private static Sspbean instance;
	public static Sspbean getInstance(){
		if(instance == null){
			instance=new Sspbean();
		}
		return instance;
		
	}
	//接口调用
	private Map<String ,List<Integer>> sspMap;
	/**
	 * 根据投注方式不同
	 * 普通投注
	 * **/
	private String[] name;//投注方式
	private List<Integer> redList;//红球
	private List<Integer> blueList;//篮球
	/**
	 * 根据投注方式不同
	 * 胆拖投注
	 * **/
	private List<Integer>  redDList;//胆码
	private List<Integer>  redTList;//拖码
	/**
	 * @return the name
	 */
	public String[] getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String[] name) {
		this.name = name;
	}
	/**
	 * @return the redList
	 */
	public List<Integer> getRedList() {
		return redList;
	}
	/**
	 * @param redList the redList to set
	 */
	public void setRedList(List<Integer> redList) {
		this.redList = redList;
	}
	/**
	 * @return the blueList
	 */
	public List<Integer> getBlueList() {
		return blueList;
	}
	/**
	 * @param blueList the blueList to set
	 */
	public void setBlueList(List<Integer> blueList) {
		this.blueList = blueList;
	}
	/**
	 * @return the redDList
	 */
	public List<Integer> getRedDList() {
		return redDList;
	}
	/**
	 * @param redDList the redDList to set
	 */
	public void setRedDList(List<Integer> redDList) {
		this.redDList = redDList;
	}
	/**
	 * @return the redTList
	 */
	public List<Integer> getRedTList() {
		return redTList;
	}
	/**
	 * @param redTList the redTList to set
	 */
	public void setRedTList(List<Integer> redTList) {
		this.redTList = redTList;
	}
	/**
	 * @return the sspMap
	 */
	public Map<String, List<Integer>> getSspMap() {
		return sspMap;
	}
	/**
	 * @param sspMap the sspMap to set
	 */
	public void setSspMap(Map<String, List<Integer>> sspMap) {
		this.sspMap = sspMap;
	}
	
	
	
	
	
}
