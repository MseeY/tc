package com.mitenotc.tc;

import android.view.View;

/**
 * 常量
 * @author mitenotc
 */
public interface ConstantValue {

	/**
	 * 字符集
	 */
	String CHARSET = "UTF-8";
	/**
	 * 服务器地址
	 */
	String URI = "http://api.mitenotc.com/index.php";// 10.0.2.2模拟器如果需要跟PC机通信127.0.0.1 
//	String URI = "http://118.144.88.41/index.php";// 10.0.2.2模拟器如果需要跟PC机通信127.0.0.1 
	String URI_test = "http://test.mitenotc.com/index.php";// 测试地址
	
	String AgentID = "10000001";
	
	/**
	 * 福彩快三 江苏快三 模拟快三 推荐比对值
	 */
   double RECOMMEND_COMPARISON=1.5;
   /**
    * 彩种类型  能够制定模拟投注的彩种  默认定制中的彩种  帮助中心
    * true 体彩  
    * false  泰彩(全彩种)
    */
   boolean LOTTERY_TYPE= false;
   /**
    * 联通话费支付
    * true View.VISIBLE
    * false View.GONE
    */
   boolean UnicomPhone= true;
   /**
    * 大额转账
    * true View.VISIBLE
    * false View.GONE
    */
   boolean WholesalePay= true;
   /**
    * 客服电话 
    */
   String serviceTel="4000328666";//4000328666
   String serviceTel_format="400-0328-666";//400-0328-666
   String serviceTel_format1="400&#8211;0328&#8211;666";//400&#8211;0328&#8211;666
   /**
    * 官网
    */
   String officialWebsite="http://m.mitenotc.com";//http://m.mitenotc.com
   /**
    * 支付项目  不能为空
    */
   String payProject="泰彩彩票支付";//山东体彩支付
   /**
    * 委托协议  协议不可更换(内容有版本区分须后台修改)
    */
   String entrust="http://news.mitenotc.com/a/wttzgz.html";
   /**
    * 版权
    */
   String copyright="梅泰诺泰彩科技(北京)有限公司";//
}
