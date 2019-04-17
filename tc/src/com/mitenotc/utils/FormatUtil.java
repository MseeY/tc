package com.mitenotc.utils;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

import com.mitenotc.tc.MyApp;

public class FormatUtil {
    /** 
     * 时间比对 朝7晚0
	 * @param date 
     */  
	public static  boolean formatSheduleTimer(String date){  
		int hour=0;
		boolean tag=true;
	           String[] str=date.split(":");  
	           if(str==null||str.length==0){  
                return tag;  
	           }else{  
	               try{  
	                    hour=Integer.valueOf(str[0]);  //str[1] 分 str[2] 秒
	                    System.out.println("1358---->"+hour);
	                    if(0 <= hour && hour <= 7){//朝7晚0
	                    	tag=false;
	                    }else{
	                    	tag=true;
	                    }
//	                    System.out.println("t----------- = "+hour+" tag "+tag);
	                }catch(NumberFormatException e){ 
	                	tag=true;
	               }  
	            }  
	          return tag;  
	   }
	/**
	 * 给TextView 设置字体样式库文件
	 *在assets底下创建一fonts文件夹 并放入要使用的字体文件(.ttf)
                 并提供相对路径给creatFromAsset()来创建Typeface对象 字体文件必须是true type font的格式(ttf)；
		 当使用外部字体却又发现字体没有变化的时候(以 DroidSans-Bold.ttf代替)，通常是因为
		 这个字体android没有支持,而非你的程序发生了错误
		 final Typeface fontFace = Typeface.createFromAsset(context.getAssets(),"fonts/460-cai978.ttf");
	 */
	static Typeface fontFace0 = Typeface.createFromAsset(MyApp.context.getAssets(),"fonts/721-CAI978.ttf");
	static Typeface fontFace1 = Typeface.createFromAsset(MyApp.context.getAssets(),"fonts/460-cai978.ttf");
	static Typeface fontFace2 = Typeface.createFromAsset(MyApp.context.getAssets(),"fonts/821-CAI978.ttf");
	public static  void setTypeface(TextView tv,int i){
		 try {
			 switch (i) {
					case 0:
						tv.setTypeface(fontFace0);
						break;
					case 1:
						tv.setTypeface(fontFace1);
						break;
					case 2:
						tv.setTypeface(fontFace2);
						break;
					default:
						break;
			 }
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("文件加载失败:*.ttf "+ e.getMessage());
			}
	}
	/**
	 * 获取时间 对照批次
	 * 例如 :[20141112, 20141113, 20141114, 20141115, 20141116];
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String[] getTimes(){
		//获取当前日期 以及未来 4天期次 
		String[] t=new String[7];
		 try {
			 Date date = new Date();  
			 SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");  
			 String nowDate = sf.format(date); 
			for (int i = 0; i < 5; i++) {
				Calendar cal = Calendar.getInstance();
				//注：在使用set方法之前，必须先clear一下，否则很多信息会继承自系统当前时间  
				cal.clear();
				cal.setTime(sf.parse(nowDate));  
				cal.add(Calendar.DAY_OF_YEAR, +i);
				t[i]= sf.format(cal.getTime());  
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}	           
		return t;
	}
	/** 
	 * 竞彩时间比对
	 * @param date 
	 */  
	@SuppressWarnings("finally")
	@SuppressLint("SimpleDateFormat")
	public static  boolean salesThanTimer(String s1){ 
		boolean tag=false;
		try{  
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Calendar c1=Calendar.getInstance();
			Calendar c2=Calendar.getInstance();
			c1.setTime(df.parse(s1));//销售截止时间
			c2.setTime(df.parse(df.format(new Date())));//当前系统时间
//			System.out.println("220---c1--->"+c1+"c2----->"+c2);
		    if(c1.compareTo(c2) >=  0){
				tag=true;
			}
		}catch(Exception e){
        }finally{
        	return tag;  
        }
	}
	/**
	 * 金钱 的 格式化. 未特殊说明的情况下，所有金额型字段都用分表示
	 * @param money
	 * @return
	 */
	public static double moneyFormat2Double(double money){
		return Double.parseDouble(new DecimalFormat("0.00").format(money));
	}
	public static float getFloat(float money){
		return Float.parseFloat(new DecimalFormat("0.00").format(money));
	}
	public static String getFloatStr(float money){
		return String.valueOf(new DecimalFormat("0.00").format(money));
	}
	/**
	 * 把 货币 格式到阅读的 字符串 例如 123,456.78
	 * @param money
	 * @return
	 */
	public static String moneyFormat2String(double money){
		String str = new DecimalFormat("###,###,###,###,###,###.00").format(money);
		if(money<1){
			str = "0"+str;
		}
		return str;
	}
	/**
	 * 把 货币 格式到阅读的 字符串 例如 123,456.78
	 * @param money
	 * @return
	 */
	public static String moneyFormat2StringInt(double money){
		String str = new DecimalFormat("#").format(money);
		if(money<1){
			str = "0"+str;
		}
		return str;
	}
	/**
	 * 格式化为 :yyyy年MM月dd 日星期A
	 * 
	 * @param s 长度如果大于或者小于 8 则返回原字符串
	 * @return
	 */
	public static String getDay(String s){
		String ss=s;
		if(s.length() <8 ||s.length() >8  )return s;//返回元字符
		try {
			//先格式化
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date d = sdf.parse(s); //字符串转时间
			long millis=d.getTime();//获得毫秒数
			Calendar cal = Calendar.getInstance(); 
			//注：在使用set方法之前，必须先clear一下，否则很多信息会继承自系统当前时间 
			cal.clear();
			cal.setTimeInMillis(millis);                   
			Formatter ft=new Formatter(Locale.CHINA);  //通过中国时区获取时间                  
			ss = ft.format("%1$tY年%1$tm月%1$td日%1$tA", cal).toString();// 格式化为 :yyyy年MM月dd 日星期A
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return ss;
	}
	/**
	 * 对金额进行转换
	 * 主要用于类似于 开奖详情中的 本期销量: xx.xx 亿 / xx.xx 万
	 * @param money 传进去时单位是元
	 */
	public static String moneyFormat(String money){
		long money_long = 0;
		if(AppUtil.isNumeric(money)){
			money_long = Long.parseLong(money);
		}
		
		if(money_long / 100000000 >= 1){//亿
			money = money_long / 100000000 + "." +money_long % 100000000;
			return moneyFormat2String(Double.parseDouble(money)) + "亿";
		}
		
		if(money_long / 10000 >= 1){//万
			money = money_long / 10000 + "." +money_long % 10000;
			return moneyFormat2String(Double.parseDouble(money)) + "万";
		}
		
		return money+"元";//元
	}
	/**
	 * 把货币转换成 有 逗号隔开的形式,精确到个位
	 * @param money
	 * @return
	 */
	public static String moneyFormat2String_int(double money){
		String str = new DecimalFormat("####,####,####,####").format(money);
		if(money<1){
			str = "0"+str;
		}
		return str;
	}
	
	/**
	 * 时间的格式化, long 类型 转换成 24小时制的格式：yyyy-mm-dd hh:nn:ss
	 * @param time
	 * @return
	 */
	public static String timeFormat(long time){
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
	}
	/**
	 * 日期格式化, long 类型 转换成：yyyy-mm-dd
	 * @param time
	 * @return
	 */
	public static String dateFormat(long time){
		return new SimpleDateFormat("yyyy-MM-dd").format(time);
	}
	/**
	 * 解析时间到 long 类型. 把String 类型的 时间字符串 转换成 long 类型.
	 * @param time
	 * @return
	 */
	public static long timeFormat(String time){
		try {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			return -1;
		}
	}
	/**
	 * 解析日期到 long 类型. 把String 类型的 时间字符串 转换成 long 类型.
	 * @param time
	 * @return
	 */
	public static long dateFormat(String time){
		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse(time).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			return -1;
		}
	}
	/**
	 * 球号格式化,   十位数字 添加0
	 * @param num
	 * @return
	 */
	public static String ballNumberFormat(int num){
		return new DecimalFormat("00").format(num);
	}
	public static String ballNumberFormat(String num){
		return ballNumberFormat(Integer.parseInt(num));
	}
}
