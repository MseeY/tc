package com.mitenotc.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class BaseCalc {
	
	  public static void main(String[] args) {
//		 System.out.println("calc : "+ calc(15,2));
//		  List<Integer> jjList=new ArrayList<Integer>();
//		  jjList.add(11);
//		  Map<String,String> ylxxMap=new HashMap<String, String>();
//		  ylxxMap.put("1","30");
		  Map<String,Integer> map=new HashMap<String, Integer>();
		  long g=JCCalc(map,2,3);
		  System.out.println("g------->"+g);
		  
	}
	  static Map<String,Integer> vmap;
	  static String A[] = null;
	  static String B[] = null;
	  static long Value = 0;
	  static long lotterycount =0;
  /**
   * 竞彩计算
   * @param map 每场筛选赛果    数
   * @param n   串关 102 : 2
   * @param m   总场次数 (单关为1 ,过关为 m>2)
   * @return
   */
	public   static long  JCCalc(Map<String,Integer> map,int n,int m){
		vmap = map;
		Set<String> set = map.keySet();
        Iterator<String> it = set.iterator();
        int i = 0;
        A = new String[set.size()];
        while(it.hasNext()){
        	A[i++] = it.next().toString();
        }		
		B = new String[m];
		return Com(n-1,m-1);
//		return Value;
	}
	public static  long   Com(int n,   int m) {
	      for(int i  =  n;   i  >=  m;   --i){   //抽屉原理
	    	   B[m]=A[i];
	          if (m   >   0){
	        	  Com(i-1,m-1);
	          }else{
	        	   //Value = "";
	        	  lotterycount = 1;
	        	  	for(int x=0;x<B.length;x++)	        		  
	        	  	{
	        	  		lotterycount *= vmap.get(B[x]);
	        	  	}
	        	  	Value = Value+ lotterycount;
	        	  }
	      }
	      return Value;
	 }
	public static long calc(long m,long n)
	{
		long result =1;
		for(int i=1;i<=n;i++)
		{
	 		 result=result * (m-n+i)/i;
		}
		return result;
	}
//	--------------------------整合----------------------------------
	/**
	 * 
	 * @param bs          投注倍数              (默认投注倍数为1)
	 * @param zs          一票投注注数      (投注号码计算的单式投注注数)
	 * @param jjList      奖金集合              (奖金个数为1,按照方普通计算方式走;奖金个数为2的时候,计算盈利和盈利率范围返回结果为区间值)
	 * @param ylxxMap     最低盈利信息      (Key:1,value:方案一最低盈利率;key:2,value:方案二指定前N期;key:3,
	 *                    方案二前期指定最低盈利率;key：4,value:方案二余期最低盈利;key:5,value:方案三最低盈利金额)
	 * @param zqs         追期数                  
	 * @param qsqh        起始期号              (根据追期数自己累加)
	 * @return            期信息Map   (key:期号  value：当期信息List(累计投入,盈利,盈利率,投注倍数))
	 */
	public static Map<String, List<String>>  jsqxx(String bs,String zs,List<Integer> jjList,Map<String,String> ylxxMap,String zqs,String qsqh)
	{
//		System.out.println("------------------------------");
//		System.out.println("bs:"+bs);
//		System.out.println("zs:"+zs);
//		System.out.println("jjList:"+jjList.toString());
//		System.out.println("ylxxMap:"+ylxxMap.toString());
//		System.out.println("zqs:"+zqs);
//		System.out.println("qsqh:"+qsqh);
//		System.out.println("------------------------------");
		
		if ("".equals(bs) || bs == null || !isNumeric(bs))
		{
			bs = "1";
		}
		
		if ("".equals(zs) || zs == null || !isNumeric(zs))
		{
			return null;
		}
		
		if (jjList == null || jjList.size() == 0 || jjList.size() >2)
		{
			
			return null;
		}
		
		if (jjList.size() == 2) //如果奖金两个值一样按照一个计算
		{
			if (jjList.get(0).equals(jjList.get(1))) 
			{
				jjList.remove(0);
			}
		}
		
		if (ylxxMap == null || ylxxMap.size() == 0)
		{
			return null;
		}
		
		if ("".equals(zqs) || zqs == null || !isNumeric(zqs))
		{
			return null;
		}
		
		if ("".equals(qsqh) || qsqh == null || !isNumeric(qsqh))
		{
			return null;
		}

		Map<String, List<String>> zqxx = new HashMap<String, List<String>>();// 追期信息
		
		String  ylxxMap1 = null;
		if (ylxxMap.get("1") != null && isNumeric(ylxxMap.get("1"))) 
		{
			ylxxMap1 = ylxxMap.get("1");
		}
		
		String ylxxMap2 = null;
		if (ylxxMap.get("2") != null && isNumeric(ylxxMap.get("2"))) 
		{
			ylxxMap1 = ylxxMap.get("2");
		}
		
		String ylxxMap3 = null;
		if (ylxxMap.get("3") != null && isNumeric(ylxxMap.get("3"))) 
		{
			ylxxMap1 = ylxxMap.get("3");
		}
		
		String ylxxMap4 = null;
		if (ylxxMap.get("4") != null && isNumeric(ylxxMap.get("4"))) 
		{
			ylxxMap1 = ylxxMap.get("4");
		}
		
		String ylxxMap5 = null;
		if (ylxxMap.get("5") != null && isNumeric(ylxxMap.get("5"))) 
		{
			ylxxMap1 = ylxxMap.get("5");
		}
		double ljtr = 0.0;// 累计投入
		int dqje = 0;// 当期金额
		int zxyl = 0;// 最小奖金盈利
		int zxyll = 0; // 最小奖金盈利率
		int zdyl = 0;// 最大奖金盈利
		int zdyll = 0; // 最大奖金盈利率
		boolean jsf1 = true;// 标识符
		boolean sflj = true;// 是否累计标识符
		int bs1 = Integer.parseInt(bs);// 起始倍数
		int ksqh = Integer.parseInt(qsqh);//开始期号
		int trzs = Integer.parseInt(zs);//一票投入注数
		//计算每一期的投注信息
		for (int i = 0; i < Integer.parseInt(zqs); i++) 
		{
			List<String> qixinxi = new ArrayList<String>();// 期信息
			int qh = ksqh+i;
			
			while (jsf1) 
			{
				if (bs1 > 9999) 
				{
					jsf1 = false;//终止内部while循环
					sflj = false;//不累计
					i = Integer.parseInt(zqs);//让外层不在循环
				}
				dqje = 2 * trzs * bs1;// 当期金额
				ljtr += dqje;// 累计投入
				
				//如果传入中奖金额是两个 计算第二盈利和盈利率
				if(jjList.size() == 2)
				{
//					zdyl = jjList.get(1) * bs1 * trzs - (int) ljtr;
					zdyl = jjList.get(1) * bs1 - (int) ljtr;
				}
				else
				{
//					zdyl = jjList.get(0) * bs1 * trzs - (int) ljtr;
					zdyl = jjList.get(0) * bs1  - (int) ljtr;
				}
				zdyll = (int) (zdyl / ljtr * 100);
			
				if (ylxxMap1 != null) 
				{
					if (ksqh == qh && zdyll < Integer.parseInt(ylxxMap1)) 
					{
						return null;
					}
					// 如果盈利率大于最小盈利率进行下一期计算
					if (zdyll >= Integer.parseInt(ylxxMap1)) 
					{
						jsf1 = false;
					} 
					else 
					{
						ljtr -= dqje;
						bs1++;
					}
				}
				else if (ylxxMap2 != null) 
				{
					if (ylxxMap3 != null && ylxxMap4 != null) 
					{
						if (i < Integer.parseInt(ylxxMap.get("2"))) 
						{//前多少期
							// 如果盈利率大于最小盈利率进行下一期计算
							if (zdyll >= Integer.parseInt(ylxxMap3)) 
							{
								jsf1 = false;
							} 
							else 
							{
								ljtr -= dqje;
								bs1++;
							}
						}
						else
						{
							// 如果盈利率大于最小盈利率进行下一期计算
							if (zdyll >= Integer.parseInt(ylxxMap4)) 
							{
								jsf1 = false;
							} 
							else 
							{
								ljtr -= dqje;
								bs1++;
							}
						}
					}
					else
					{
						return null;
					}
				}
				else if (ylxxMap5 != null) 
				{
					// 如果盈利大于最小盈利进行下一期计算
					if (zdyl > Integer.parseInt(ylxxMap5)) 
					{
						jsf1 = false;
					} 
					else 
					{
						ljtr -= dqje;
						bs1++;
					}
				}
				else
				{
					return null;
				}
			}
			
			jsf1 = true;
			
			if(jjList.size() == 2)
			{
//				zxyl = jjList.get(0) * bs1 * trzs - (int) ljtr;// 最小奖金盈利
				zxyl = jjList.get(0) * bs1 - (int) ljtr;// 最小奖金盈利
				zxyll = (int) (zxyl / ljtr * 100);// 最小奖金盈利率
			}
			
			qixinxi.add(0, (int)ljtr + "");// 累计投入

			if(jjList.size() == 2)
			{
			    qixinxi.add(1, zxyl + "\n至\n" +zdyl);//盈利
			    qixinxi.add(2, zxyll + "%\n至\n" + zdyll+"%");//盈利率
			}
			else
			{
				qixinxi.add(1, zdyl + "");//盈利
				qixinxi.add(2, zdyll + "%");//盈利率
			}
			
			qixinxi.add(3, bs1 + "");//投注倍数
			
			if (sflj)
			{
				zqxx.put(qh + "", qixinxi);//key:期号  value：当期信息List
			}
		}
        return zqxx;
	}
	/**
	 * 使用正则判断是否为整数
	 * @param str
	 * @return  true：为整数;false：不为整数;
	 */
	public static boolean isNumeric(String str){ 
	    Pattern pattern = Pattern.compile("[0-9]*"); 
	    return pattern.matcher(str).matches();    
	 } 
	
	

	/**
	 *  修改 某一期的
	 * @param bs          投注倍数
	 * @param zs          一票投注注数
	 * @param jjList      奖金集合
	 * @param ylxxMap     最低盈利信息      (Key:1,value:方案一最低盈利率;key:2,value:方案二指定前N期;key:3,
	 *                    方案二前期指定最低盈利率;key：4,value:方案二余期最低盈利;key:5,value:方案三最低盈利金额)
	 * @return            期信息Map   (key:期号  value：当期信息List(累计投入,盈利,盈利率,投注倍数))
	 */
	public static Map<String, List<String>> jszqxxxgbs(Map<String, String> bs,String zs,List<Integer> jjList) 
	{
		
		
		////System.out.println("修改==");
		////System.out.println("bs :"+bs);
		////System.out.println("zs :"+zs);
		////System.out.println("jjList :"+jjList.toString());
		////System.out.println("修改==");
		
		
		if (bs == null) 
		{
			return null;
		}
		
		if ("".equals(zs) || zs == null || !isNumeric(zs)) 
		{
			return null;
		}
		
		if (jjList == null || jjList.size() == 0 || jjList.size() >2)
		{
			return null;
		}
		
		
		//如果奖金两个值一样按照一个计算
		if (jjList.size() == 2) 
		{
			if (jjList.get(0).equals(jjList.get(1))) 
			{
				jjList.remove(0);
			}
		}
		
		double ljtr = 0.0;// 累计投入
		int dqje = 0;// 当前期投入
		int zxyl = 0;// 最小奖金盈利
		int zxyll = 0; // 最小奖金盈利率
		int zdyl = 0;// 最大奖金盈利
		int zdyll = 0; // 最大奖金盈利率
		int bs1 = 0;// 起始倍数
		
		Map<String, List<String>> zqxx = new HashMap<String, List<String>>();// 追期信息
		
		List<String> qhList = new ArrayList<String>();
		for (String qh : bs.keySet()) {
			if (qh != null && !qh.equals("")) 
			{
				qhList.add(qh);
			}
			else 
			{
				return null;
			}
		}

		for (int g = 0; g < qhList.size(); g++) 
		{
			for (int i = 0; i < qhList.size()-1; i++) 
			{
				int j = 0;
				int j1 = 0;
				j = Integer.parseInt(qhList.get(i));
				j1 = Integer.parseInt(qhList.get(i + 1)); 
				if(j > j1)
				{
					qhList.remove(i);
					qhList.add(i,j1 + "");
					qhList.remove(i + 1);
					qhList.add(i + 1,j + "");
				}
			}
		}
		
		for (int i = 0; i < qhList.size(); i++) 
		{
			List<String> qixinxi = new ArrayList<String>();// 期信息
			bs1 = Integer.parseInt(bs.get(qhList.get(i)));
			
			dqje = 2 * Integer.parseInt(zs) * bs1;// 当期金额
			ljtr += dqje;// 累计投入
			
			//如果传入中奖金额是两个 计算第二盈利和盈利率
			if(jjList.size() == 2)
			{
				zdyl = jjList.get(1) * bs1 * Integer.parseInt(zs) - (int) ljtr;
			}
			else
			{
				zdyl = jjList.get(0) * bs1 * Integer.parseInt(zs) - (int) ljtr;
			}
			zdyll = (int) (zdyl / ljtr * 100);
			
			
			//如果传入中奖金额是两个 计算第二盈利和盈利率
			if(jjList.size() == 2)
			{
				zdyl = jjList.get(1) * bs1 * Integer.parseInt(zs) - (int) ljtr;// 最大奖金盈利
				zxyl = jjList.get(0) * bs1 * Integer.parseInt(zs) - (int) ljtr;// 最小奖金盈利
				zxyll = (int) (zxyl / ljtr * 100);// 最小奖金盈利率
			}
			else
			{
				zdyl = jjList.get(0) * bs1 * Integer.parseInt(zs) - (int) ljtr;
			}
			zdyll = (int) (zdyl / ljtr * 100);
			
			qixinxi.add(0, (int)ljtr + "");// 累计投入

			if(jjList.size() == 2)
			{
			    qixinxi.add(1, zxyl + "\n至\n" +zdyl);//盈利
			    qixinxi.add(2, zxyll + "%\n至\n" + zdyll+"%");//盈利率
			}
			else
			{
				qixinxi.add(1, zdyl + "");//盈利
				qixinxi.add(2, zdyll + "%");//盈利率
			}
			
			qixinxi.add(3, bs1 + "");//投注倍数
			zqxx.put(qhList.get(i) + "", qixinxi);//key:期号  value：当期信息List
		}
		return zqxx;
	}
	 /**
	    * 对象Array随机无重复组合 M串N
	    * @param source
	    * @param n
	    * @return
	    */
	    public   static LinkedList<Object[]> cmn(Object[] source,int n)
	    {
	    	
	    	LinkedList<Object[]> result=new LinkedList<Object[]>();
	        if(n==1)
	        {
	            for(int i=0;i<source.length;i++)
	            {
	                result.add(new Object[]{source[i]});

	            }
	        }
	        else if(source.length==n)
	        {
	            result.add(source);
	        }
	        else
	        {
	            Object[] psource=new Object[source.length-1];
	            for(int i=0;i<psource.length;i++)
	            {
	                psource[i]=source[i];
	            }
	            result=cmn(psource,n);
	            LinkedList<Object[]> tmp=cmn(psource,n-1);
	            for(int i=0;i<tmp.size();i++)
	            {
	                Object[] rs=new Object[n];
	                for(int j=0;j<n-1;j++)
	                {
	                    rs[j]=tmp.get(i)[j];
	                }
	                rs[n-1]=source[source.length-1];
	                result.add(rs);
	            }
	        }
	        return result;
	    }
	    /**
	     * 奖金优化 bonusOptimize
	     * @param zje 总金额
	     * @param sp sp值
	     * @param zs 注数分布
	     * @return
	     */
	    public List<Integer>  bo(int zje,List<Float> sp,int zs){
	    	Map<String,Float[]> returnMap=new HashMap<String, Float[]>();
	    	int zzs=0 ,a=1;
	    	
	    	if(zje == 0||sp == null||zs == 0)return null;
	    	zzs=(zje/2);//总注数
	    	
//	    	Collections.sort(willzhi);//排序
//	    	if(willzhi.size() > 0){
//	    		FormatUtil.getFloatStr(willzhi.get(0));
//	    	}
//	    	float he=0;
//	    	for (int i = 0; i < willzhi.size(); i++) {
//	    		he +=willzhi.get(i);
//			}
	    	
	    	
			return null;
	    }
}

