package com.mitenotc.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnyCHnineBean {
	

	//--------TODO 测试数据  
	
	private static AnyCHnineBean instance;
	
	public static AnyCHnineBean getInstance(){
		if(instance == null){
			instance=new AnyCHnineBean();
		}
		return instance;
	}
	

	
	
	
	
	
	 /**
	  * 对手列表 VSStringlist.get(0) 主场name
	  *       VSStringlist.get(1) 客场name 
	  */
	   
  private  List<List<String>> VSStringlist; 
    /**
	  * *  预测胜负百分比
	  * strkey ：3, 1,0
	  * ThreeScalelist.get(strkey) **%
	  */
  private  Map<String,List<String>> ScaleMap;
  
  
  
	/**
	 * @return the vSStringlist
	 */
	public List<List<String>> getVSStringlist() {
		return VSStringlist;
	}
	/**
	 * @param vSStringlist the vSStringlist to set
	 */
	public void setVSStringlist(List<List<String>> vSStringlist) {
		VSStringlist = vSStringlist;
	}
	/**
	 * @return the scaleMap
	 */
	public Map<String, List<String>> getScaleMap() {
		return ScaleMap;
	}
	/**
	 * @param scaleMap the scaleMap to set
	 */
	public void setScaleMap(Map<String, List<String>> scaleMap) {
		ScaleMap = scaleMap;
	}
  
  
  
}
