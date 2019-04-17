package com.mitenotc.utils;

public class MathUtil {

	/**
	 * 计算一个数的阶乘
	 */
	public static long factorial(int num) {
		// n!=n*(n-1)*...*1
		// num=1or0
		// num<0
		if (num > 1) {
			return num * factorial(num - 1);
		} else if (num == 1 || num == 0) {
			return 1;
		} else {
			throw new IllegalArgumentException("num >=0");
		}
	}
	/**
	 * 计算排序
	 * @param n 底数
	 * @param m
	 * @return
	 */
	public static long A(int n, int m){
		////System.out.println("factorial(n) = "+factorial(n));
		////System.out.println("factorial(m) = "+factorial(m));
		return factorial(n)/factorial(n-m);
	}
	/**
	 * 计算组合
	 * @param n
	 * @param m
	 * @return
	 */
	public static long C(int n, int m){
		return A(n,m)/factorial(m);
	}
}
