package com.mitenotc.utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

/**
 * 奖金优化 计算
 * @author mitenotc
 *
 */
public class ParseLottery 
{
	String A[] = null;
	String B[] = null;
	int Value = 0;
	int lotterycount =0;
	Map<String,Integer> vmap;
	public ParseLottery() {
		// TODO Auto-generated constructor stub
	}
	public ParseLottery(Map<String,Integer> map,int m)
	{
		vmap = map;
		Set<String> set = map.keySet();
        Iterator<String> it = set.iterator();
        int i = 0;
        A = new String[set.size()];
        while(it.hasNext()){
        	A[i++] = it.next().toString();
        }		
		B = new String[m];
	}
	public  void   Com(int n,   int m)
	  {
	      int   i;
	      for   (i   =   n;   i   >=   m;   --i){   //抽屉原理
	    	   B[m]=A[i];
	          if   (m   >   0)
	              Com(i-1,m-1);
	          else
	        	  {
	        	   //Value = "";
	        	    lotterycount = 1;
	        	  	for(int x=0;x<B.length;x++)	        		  
	        	  	{
	        	  		lotterycount *= vmap.get(B[x]);
	        	  	}	        	  	
	        	  	Value = Value+ lotterycount;
	        	  }
	      }
	  }
  public static int Fn_ParseLotteryCode(Map<String,Integer> map,int n,int m)
  {
      int vn = n;
      int vm = m;

      ParseLottery v = new ParseLottery(map,vm);
 		  v.Com(vn-1,vm-1);
      return v.Value;
  }
    public static int  flag = 1;
    
    /**
     * @param tag 奖金优化分类
     * @param bonuslist 单注奖金分布集合
     * @param imoney 总金额
     * @return
     */
//     result [-1918350069, 1800524800, 109687480, 7461739, 636395, 37107, 2420, 197, 11, 0]

	public static List<Integer> calBonus(int tag,List<Float> bonuslist,int imoney) throws ArithmeticException
	{ 
		System.out.println("210------->tag"+tag+"bonuslist"+bonuslist.toString()+"imoney"+imoney);
		List<Integer> s = new ArrayList<Integer>();
		
		float maxvalue = 0;
		float minvalue = 99999;
		float squrevale = 0;
		int curvalue = 0;
		int maxindex = 0;
		int minindex = 0;
		int summoney = 0;
		Map<Integer,Float> tmpmap = new TreeMap<Integer,Float>();  
		for(int i=0;i<bonuslist.size();i++)
		{
			maxindex = maxvalue > bonuslist.get(i) ? maxindex : i;
			maxvalue = maxvalue > bonuslist.get(i) ? maxvalue:bonuslist.get(i);
			minindex = minvalue < bonuslist.get(i) ? minindex : i ;
			minvalue = minvalue < bonuslist.get(i) ? minvalue:bonuslist.get(i);
			squrevale += bonuslist.get(i);
			tmpmap.put(i, bonuslist.get(i));
		}		
		
		//平均奖金
		squrevale = (( squrevale / bonuslist.size() ) * (imoney/2)) / bonuslist.size();
		switch (tag) {
		case 1://平均优化
//			for(int i = 0 ; i < bonuslist.size();i++)//		  bonuslist->[3.98, 4.4, 4.78, 4.28, 4.64, 5.14]
//			{
//				curvalue = (int)(squrevale / bonuslist.get(i));
//				s.add(curvalue);
//				summoney += curvalue * 2 ;
//			}
//			if(summoney < imoney )
//			{
//				s.set(minindex, s.get(minindex) + (imoney - summoney)/2);				
//			}
			int n=bonuslist.size();
			int dbs=imoney/(n*2);//均分倍数
			for(int i = 0 ; i < n ;i++)//		  bonuslist->[3.98, 4.4, 4.78, 4.28, 4.64, 5.14]
			{
				curvalue = (int)(squrevale / bonuslist.get(i));//(4.5366664/3.98)=1.34
				curvalue *= dbs;
				if(1 >= curvalue){
					curvalue=dbs;
				}
				if(curvalue == dbs){
					s.add(curvalue);
				}else{
					curvalue /= dbs;
					s.add(curvalue);
				}
				summoney += curvalue * 2 ;
			}
			System.out.println("summoney-->"+summoney+"imoney-->"+imoney);
			if(summoney < imoney  )
			{
					try {
						s.set(minindex, s.get(minindex) + (imoney - summoney)/(2*dbs));
					} catch (Exception e) {
						e.printStackTrace();
					}finally{
						s.set(minindex, 1);
					}
					try {
						s.set(maxindex, s.get(maxindex) - (imoney - summoney)/(2*dbs)+(imoney-summoney)/2);	
					} catch (Exception e) {
						e.printStackTrace();
					}finally{
						s.set(maxindex, 1);
					}
			}
			break;
		case 2://风险最小
			tmpmap = sortMapByValue(tmpmap);
			
			for (Map.Entry<Integer,Float> entry : tmpmap.entrySet()) 
			{
				curvalue  = (int)Math.round((imoney/entry.getValue()));
			    curvalue = curvalue <= 0 ? 1 : curvalue;
			    entry.setValue((float) curvalue);
			    summoney += curvalue * 2 ;			    
			}
			for(int i = 0 ; i < bonuslist.size();i++)
			{
				s.add(tmpmap.get(i).intValue());
			}
			if(summoney < imoney )
			{
				s.set(minindex, s.get(minindex) + (imoney - summoney)/2);				
			}
			break;
		case 3://奖金最高
			ParseLottery.flag = 0;
			tmpmap = sortMapByValue(tmpmap);
			for (Map.Entry<Integer,Float> entry : tmpmap.entrySet()) 
			{
				curvalue  = (int)Math.round((imoney / entry.getValue()));
				
			    curvalue = curvalue <= 0 ? 1 : curvalue;
			    entry.setValue((float) curvalue);
			    summoney += curvalue * 2 ;	
			     
			}
			for(int i = 0 ; i < bonuslist.size();i++)
			{
				s.add(tmpmap.get(i).intValue());
			}
			if(summoney < imoney )
			{
				s.set(maxindex, s.get(maxindex) + (imoney - summoney)/2);				
			}			
			break;
	}
//		//squrevale
//		if(tag ==1 ) //平均优化
//		{
//			for(int i = 0 ; i < bonuslist.size();i++)
//			{
//				curvalue = (int)(squrevale / bonuslist.get(i));
//				s.add(curvalue);
//				summoney += curvalue * 2 ;
//			}
//			if(summoney < imoney )
//			{
//				s.set(minindex, s.get(minindex) + (imoney - summoney)/2);				
//			}
//			return s;
//		}
//		
//		
//		
//		if(tag==2)  //风险最小
//		{
//			tmpmap = sortMapByValue(tmpmap);
//			
//			for (Map.Entry<Integer,Float> entry : tmpmap.entrySet()) 
//			{
//				curvalue  = (int)Math.round((imoney/entry.getValue()));
//			    curvalue = curvalue <= 0 ? 1 : curvalue;
//			    entry.setValue((float) curvalue);
//			    summoney += curvalue * 2 ;			    
//			}
//			for(int i = 0 ; i < bonuslist.size();i++)
//			{
//				s.add(tmpmap.get(i).intValue());
//			}
//			if(summoney < imoney )
//			{
//				s.set(minindex, s.get(minindex) + (imoney - summoney)/2);				
//			}
//			return s;
//		}
//		if(tag==3) //奖金最高
//		{
//			ParseLottery.flag = 0;
//			tmpmap = sortMapByValue(tmpmap);
//			for (Map.Entry<Integer,Float> entry : tmpmap.entrySet()) 
//			{
//				curvalue  = (int)Math.round((imoney / entry.getValue()));
//				
//			    curvalue = curvalue <= 0 ? 1 : curvalue;
//			    entry.setValue((float) curvalue);
//			    summoney += curvalue * 2 ;	
//			     
//			}
//			for(int i = 0 ; i < bonuslist.size();i++)
//			{
//				s.add(tmpmap.get(i).intValue());
//			}
//			if(summoney < imoney )
//			{
//				s.set(maxindex, s.get(maxindex) + (imoney - summoney)/2);				
//			}			
//		}
		
		return s;
		
	}
	public static Map<Integer,Float> sortMapByValue(Map<Integer,Float> map) 
	{  
	        if (map == null || map.isEmpty()) {  
	            return null;  
	        }  
	        Map<Integer,Float> sortedMap = new LinkedHashMap<Integer,Float>();  
	        List<Map.Entry<Integer,Float>> entryList = new ArrayList<Map.Entry<Integer, Float>>(map.entrySet());  
	        Collections.sort(entryList, new Comparator<Map.Entry<Integer,Float>>(){
	        	 public int compare(Entry<Integer,Float> me1, Entry<Integer,Float> me2) 
	     	    {  
	        		 if(ParseLottery.flag >0)
	        		 {
	        			 return me1.getValue().compareTo(me2.getValue()) > 0 ? 0 : 1;
	        		 }
	        		 else
	        			 return me1.getValue().compareTo(me2.getValue());
	     	    }  
	          });
	        Iterator<Map.Entry<Integer,Float>> iter = entryList.iterator();  
	        Map.Entry<Integer,Float> tmpEntry = null;  
	        while (iter.hasNext()) {  
	            tmpEntry = iter.next();  
	            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());  
	        }  
	        return sortedMap;
	  }  
	


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		 * Map<String,Integer> map  = new HashMap<String,Integer>();
		map.put("001", 3);
		map.put("002", 3);
		map.put("003", 1);
		
		System.out.print(Fn_ParseLotteryCode(map,3,2));*/
		ArrayList<Float> bs = new ArrayList<Float>();
		bs.add(Float.valueOf((float) 10.4));
		bs.add(Float.valueOf((float) 5.1));
		//bs.add(Float.valueOf((float) 4.3));
		//bs.add(Float.valueOf((float) 2.02));
		//bs.add(Float.valueOf((float) 2.9));
		
		ArrayList<Integer> st = (ArrayList<Integer>) calBonus(2,bs,20);
		//System.out.print(st.toString());
		int sum = 0;
		for(int i=0;i<bs.size();i++)
		{
			
			sum += st.get(i);
			System.out.print("sum"+ sum +"  ----sp:"+ bs.get(i)+"---"+st.get(i)+"---" + bs.get(i)*st.get(i)+"\n");
		}
	}

}
