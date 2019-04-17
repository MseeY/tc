package com.mitenotc.utils;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class JCBaseCalc {
	

    /**
     * 进行组合的方法
     * @param teamNum
     * @param listSize
     * @return
     */
	private static List<int[]> toCombine(int teamNum, int listSize) {
		// 初始化原始数据
		int[] a = new int[listSize];
		for (int i = 0; i < a.length; i++) {
			a[i] = i;
		}
		// 接收数据
		int[] b = new int[teamNum];

		List<int[]> list = new ArrayList<int[]>();

		// 进行组合
		combine(a, a.length, teamNum, b, teamNum, list);
		return list;
	}
	/**
	 * 组合的递归算法
	 * 
	 * @param a 原始数据
	 * @param n 原始数据个数
	 * @param m  选择数据个数
	 * @param b 存放被选择的数据
	 * @param M 常量，选择数据个数
	 * @param list 结果
	 */
	public static void combine(int a[], int n, int m, int b[], final int M, List<int[]> list) {
		for (int i = n; i >= m; i--) {
			b[m - 1] = i - 1;
			if (m > 1){
				combine(a, i - 1, m - 1, b, M, list);
			}else {
				int[] result = new int[M];
				for (int j = M - 1; j >= 0; j--) {
					result[j] = a[b[j]];
				}
				list.add(result);
			}
		}
	}


	/**
	 * // 单一串关 最多108 (8串1) 小于108
	 * @param betcodes 每场筛选的 胜平负个数
	 * @param select 串关方式 例如102 : 2
	 * @param isDanList 是否设置为胆拖 boolean
	 * @param isDanNum 胆拖个数
	 * @return
	 */
	public static int getAllLC(List<Integer> betcodes, int cg, List<Boolean> isDanL, int isDanN) {
		List<int[]> list = toCombine(cg, betcodes.size());
		// 返回数据对象
		int resultInt = 0;
		for (int[] result : list) {
			int itemNum = 1;
			int danNum = 0;
			for (int p : result) {
				itemNum *= betcodes.get(p);
				if (isDanN > 0 && isDanL.get(p)) {
					danNum++;
				}
			}
			if (isDanN == 0 || danNum == isDanN) {
				resultInt += itemNum;
			}
		}
		return resultInt;
	}
	/**
	 * 两分组是否相同
	 * @param result
	 * @param resultItem
	 * @return
	 */
	private static boolean isDiffResult(int[] result, int[] resultItem) {
		int diffNum = 0;
		 for(int n=0;n<result.length;n++){
			 int nNum = result[n];
			 for(int m=0;m<resultItem.length;m++){
				 int mNum = resultItem[m];
				 if(nNum==mNum){
					 diffNum++;
				 }
			 }
		 }
		 if(diffNum == result.length){
			 return true;
		 }else{
			 return false;
		 }
	}
    /**
     * 单关投注计算注数
     * @param betcodes
     * @return
     */
	public static int getDanAAmt(List<Integer> betcodes) {
		int zhushu = 0;
		for (int i = 0; i < betcodes.size(); i++) {
			zhushu += betcodes.get(i);
		}
		return zhushu;
	}
	/**
	 * 返回总注数竞彩多串过关投注计算注数
	 * 
	 * @param teamNum 多串过关3*3 teamNum = 3
	 * @param select 例如 102 :2
	 * @return 将比赛分组
	 */
	@Deprecated
	public static int getDouZhushu(int teamNum, List<Integer> betcodes, int select, List<Boolean> isDanList, int isDanNum) {
		List<int[]> list = toCombine(teamNum, betcodes.size());
		List<int[]> listAll = new ArrayList<int[]>();
		for (int[] resultA : list) {
			// 接收数据
			int[] b = new int[select];
			// 进行组合
			combine(resultA, resultA.length, select, b, select, listAll);
		}
		List<int[]> listNoDiff = new ArrayList<int[]>();
		for(int i=0;i<listAll.size();i++){
			int[] resultA = listAll.get(i);
			if(listNoDiff.size()==0){
				listNoDiff.add(resultA);
			}else{
				int sizeNum = listNoDiff.size();
				for(int j=0;j<sizeNum;j++){
					int[] resultItem = listNoDiff.get(j);
					if(isDiffResult(resultA, resultItem)){
						break;
					}
					if(j==sizeNum-1){
						listNoDiff.add(resultA);
					}
			    }
			}
		}
		int resultInt = 0;
		resultInt = getAllAmtDuo(betcodes,listNoDiff, select, isDanList, 0);

		return resultInt;
	}
	/**
	 * 返回总注数竞彩自由过关投注计算注数
	 * 
	 * @param betcodes
	 * @param select
	 * @return
	 */
	@Deprecated
	public static int getAllAmtDuo(List<Integer> betcodes,List<int[]> list, int select, List<Boolean> isDanList, int isDanNum) {
		// 返回数据对象
		int resultInt = 0;
		for (int[] result : list) {
			int itemNum = 1;
			int danNum = 0;
			for (int p : result) {
				itemNum *= betcodes.get(p);
				if (isDanNum > 0 && isDanList.get(p)) {
					danNum++;
				}
			}
			if (isDanNum == 0 || danNum == isDanNum) {
				resultInt += itemNum;
			}
		}
		return resultInt;
	}
}
