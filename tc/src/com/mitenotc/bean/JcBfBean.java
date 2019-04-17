package com.mitenotc.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.widget.TextView;

import com.mitenotc.utils.JCBaseCalc;

public class JcBfBean {
	// 倍数
	private int fold = 1;
	// 胆的个数
	private int danNum = 0;
	
	private int lotteryCount = 0;
	
	// 过关方式 2串1 就为2 3串1 就为3
	private List<String> passtypeList = new LinkedList<String>();


	public List<String> getPasstypeList() {
		return passtypeList;
	}

	public void setPasstypeList(List<String> passtypeList) {
		this.passtypeList = passtypeList;
	}

	// 是否是胆拖的集合
	private static List<Boolean> danList = new ArrayList<Boolean>();
	// 每场选了几个比分的集合
	private static List<Integer> integerList = new ArrayList<Integer>();
	
	private Map<String, List<String>> mapData = new HashMap<String, List<String>>();
	private Map<String, MessageBean> piao = new HashMap<String, MessageBean>();
	// 排好顺序的集合
	
	private static List<Map.Entry<String, List<String>>> scoreList;
	private static List<Map.Entry<String, MessageBean>> piaoList;
	
	public Map<String, MessageBean> getPiao() {
		return piao;
	}

	public List<Map.Entry<String, List<String>>> getScoreList() {
		return scoreList;
	}

	public void setPiao(Map<String, MessageBean> piao) {
		this.piao = piao;
	}
	
	public Map<String, List<String>> getMapData() {
		return mapData;
	}

	public void setMapData(Map<String, List<String>> mapData) {
		this.mapData = mapData;
	}
	
	public int getFold() {
		return fold;
	}
	
	public List<Map.Entry<String, MessageBean>> getPiaoList() {
		return piaoList;
	}

	//对集合进行排序
	public void initSort() {
		piaoList = new ArrayList<Map.Entry<String, MessageBean>>(piao.entrySet());
		scoreList = new ArrayList<Map.Entry<String, List<String>>>(mapData.entrySet());
		
		Collections.sort(piaoList, new Comparator<Map.Entry<String, MessageBean>>() {
			public int compare(Map.Entry<String, MessageBean> o1,
					Map.Entry<String, MessageBean> o2) {

				if (o1.getKey().split("_")[0].equals(o2.getKey().split("_")[0])) {
					return Integer.parseInt(o1.getKey().split("_")[1])- Integer.parseInt(o2.getKey().split("_")[1]);
				} else {
					return Integer.parseInt(o1.getKey().split("_")[0])- Integer.parseInt(o2.getKey().split("_")[0]);
				}
			}
		});
		Collections.sort(scoreList, new Comparator<Map.Entry<String, List<String>>>() {
			public int compare(Map.Entry<String, List<String>> o1,
					Map.Entry<String, List<String>> o2) {
				if (o1.getKey().split("_")[0].equals(o2.getKey().split("_")[0])) {
					return Integer.parseInt(o1.getKey().split("_")[1])- Integer.parseInt(o2.getKey().split("_")[1]);
				} else {
					return Integer.parseInt(o1.getKey().split("_")[0])- Integer.parseInt(o2.getKey().split("_")[0]);
				}
			}
		});
		
		if(integerList!=null){
			integerList.clear();
		}
		//初始化胆信息
		if(danList!=null){
			danList.clear();
		}
		for (int i = 0; i < scoreList.size(); i++) {
			danList.add(false);
		}
	}
	
	//显示的金额是以“元”为单位
	public String getNotice(){
		lotteryCount = 0;
		fold = 1;
		if(integerList!=null){
			integerList.clear();
		}
		
		for (int i = 0; i < scoreList.size(); i++) {
			integerList.add(scoreList.get(i).getValue().size());
			if(danList.get(i)==true){
				danNum = danNum+1;
			}
		}
		
		for (int i = 0; i < passtypeList.size(); i++) {
			String str = passtypeList.get(i);
			int parseInt = Integer.parseInt(str.substring(str.length()-1, str.length()));
			lotteryCount = lotteryCount+JCBaseCalc.getAllLC(integerList, parseInt, danList, danNum);
		}
		
		if(passtypeList.size()==0){
			return "投1倍0注0元";
		}
		
		return "投"+fold+"倍"+lotteryCount+"注"+lotteryCount*2*fold+"元";
	}
}
