package com.mitenotc.tc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONArray;
import org.json.JSONException;

import android.R.anim;
import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.os.Message;
import android.os.Process;
import android.view.WindowManager;
import cn.jpush.android.api.JPushInterface;

import com.mitenotc.bean.ImageBean;
import com.mitenotc.bean.MessageBean;
import com.mitenotc.bean.OrderBean;
import com.mitenotc.dao.CacheDBHelper;
import com.mitenotc.dao.Cache_1308;
import com.mitenotc.engine.BaseEngine;
import com.mitenotc.exception.MessageException;
import com.mitenotc.net.MessageJson;
import com.mitenotc.net.Protocol;
import com.mitenotc.service.TCService;
import com.mitenotc.ui.base.BaseActivity;
import com.mitenotc.utils.AppUtil;
import com.mitenotc.utils.FormatUtil;
import com.mitenotc.utils.JPTagAliasCallback;
import com.mitenotc.utils.SPUtil;

public class MyApp extends Application{
 
	public static long appTestTime;
	public static Resources res;
	public static Context context;
//	public static Map<String, BaseFragment> fragmentCaches = new HashMap<String, BaseFragment>();
	public static OrderBean order = new OrderBean();
	public static Map<String, String> lotteryMap;
//	public static MessageJson message;
//	public static MessageBean messageBean;
	public static TCActivity tcActivity;
	public static  CacheDBHelper datacache;
	public static List<BaseActivity> activityList;
	public static int screenWidth;
	public static List<String> loadimageList = new ArrayList<String>();
	public static List<ImageBean> listPic1308 = null;
	public static Bitmap splashbitmap = null;
	public static Bitmap header_photo;
//	public static MessageJson probeMsg;//探针记录
	public static MessageBean updateBean = null;
	private static SQLiteDatabase db;
//	public static MessageJson probeMessage;
	public static int temp_id=1;
	public static String TEMP_PROBE_ID;// 全局探针ID每次启动直到退出都是唯一的
	
//	private static WindowManager.LayoutParams wmParams;
//  public static WindowManager wm=null;
    
	public static String TCCONSULT_SERVICE="com.mitenotc.intent.TCService";
	@Override
	public void onCreate() {
		super.onCreate();
//		Thread.currentThread().setUncaughtExceptionHandler(new TCException());// ---TODO 打包时候需要打开此全局异常捕获
		
		//sendError();
//		System.out.println("XXXMyApp:===== "+System.currentTimeMillis());
		activityList = new ArrayList<BaseActivity>();
//	    probeMsg = new MessageJson();
		res = getResources();
		context = getApplicationContext();
//		fragmentCaches = new HashMap<String, BaseFragment>();
		initProtocol();
		
//		JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
        datacache = new CacheDBHelper(this);
        initLotteryMap_AndProbe();
        System.out.println(";;;;;;别名="+Protocol.getInstance().getC());
		JPushInterface.setAliasAndTags(getApplicationContext(),Protocol.getInstance().getC(),null, new JPTagAliasCallback());  
		
		// 启动服务
		Intent tc_service=new Intent(context,TCService.class);
		tc_service.setAction(TCCONSULT_SERVICE);
		context.startService(tc_service);
	}
	
//    @Override
//    public void onLowMemory() {
//    	super.onLowMemory();
//    	saveProbeMsg("jvm---Application---> onLowMemory()","系统调用了 onLowMemory",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
//		,"1",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),"onLowMemory 内存不足:虚拟机调用GC!",false);
//    }

	 
	private void initLotteryMap_AndProbe() {
		lotteryMap = new HashMap<String, String>();
		lotteryMap.put("100", "排列三");
		lotteryMap.put("102", "排列五");
		lotteryMap.put("103", "七星彩");
		lotteryMap.put("106", "大乐透");
		lotteryMap.put("112", "11运夺金"); 
		lotteryMap.put("113", "多乐彩"); 
		lotteryMap.put("117", "七乐彩");
		lotteryMap.put("118", "双色球");
		lotteryMap.put("119", "江西时时彩");
		lotteryMap.put("120", "福彩快三");
		lotteryMap.put("122", "模拟快三");
		lotteryMap.put("123", "江苏快三");
		lotteryMap.put("121", "快乐扑克3");
		lotteryMap.put("116", "福彩3D");
		lotteryMap.put("130", "模拟11运夺金");
		lotteryMap.put("125", "竞彩足球");

		lotteryMap.put("212", "总进球");//足球玩法
		lotteryMap.put("210", "让球胜平负");
		lotteryMap.put("218", "胜平负");
		lotteryMap.put("213", "半全场");
		lotteryMap.put("211", "比分");
		lotteryMap.put("209", "混合过关");
		
		lotteryMap.put("216", "胜负");//篮球玩法
		lotteryMap.put("214", "让分胜负");
		lotteryMap.put("215", "大小分");
		lotteryMap.put("217", "胜分差");
		lotteryMap.put("219", "混合过关");
//		探针信息
//	    temp_id=SPUtil.getInt(getString(R.string.probe_id), temp_id);
//		if(99 <= temp_id ){
//			temp_id=1;
//		}else{
//			temp_id +=1;// id 在1-99 内循环自增
//		}
//		SPUtil.putInt(getString(R.string.probe_id), temp_id);// 存储id 
//		TEMP_PROBE_ID=new SimpleDateFormat("yyMMdd").format(new Date());
//		TEMP_PROBE_ID +=FormatUtil.ballNumberFormat(temp_id);////System.out.println("1307==tempDataID  :"+TEMP_PROBE_ID);
//		saveProbeMsg("start ok!","TC_Application onCreate!",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
//				,"0",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),"");

	}

	
	public static String getLotteryName(){
		return MyApp.lotteryMap.get(MyApp.order.getLotteryId());
	}
	public static OrderBean resetOrderBean(){
		       order=null;//释放引用
		return order = new OrderBean();
	}
	
	public static void setOrderBean(OrderBean orderBean){
		order = orderBean;
	}
	
//	private void initGlobalParams() {
//		GloableParams.login_required_cmds = res.getIntArray(R.array.login_required_cmds);
//	}

	private void initProtocol() {
		screenWidth = AppUtil.getDisplayWidth(this);
		//		Protocol.getInstance().setUSR(sp.getString("USR", ""));//手机号
		//		Protocol.getInstance().setUSR(sp.getString("SND", ""));//sessionId
				Protocol.getInstance().setVER(AppUtil.getVersion(this));
				Protocol.getInstance().setA(AppUtil.getDisplayHeight(this)+"*"+AppUtil.getDisplayWidth(this));
				Protocol.getInstance().setB(Build.MODEL);
				Protocol.getInstance().setC(getProtocol_C());
				Protocol.getInstance().setD("ANDROID " + Build.VERSION.RELEASE);
				Protocol.getInstance().setE("");//ip地址
	}
	/**
	 * 删除掉其他的Activity,只剩下TCActivity
	 */
	public static void backToTCActivity(){
		for (int i = 1; i < activityList.size(); i++) {
			activityList.get(i).finish();
		}
//	  	saveProbeMsg("backToTCActivity 回到主页","Activity,只剩下TCActivity--- 用户处于主页!",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
//	  			,"",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),"");

	}
	
	/**
	 * 获取协议中的规定的 设备序列号
	 * @return
	 */
	public String getProtocol_C(){
		if(AppUtil.isEmpty(AppUtil.getIEMI(this)) && AppUtil.isEmpty(AppUtil.getSimSerialNumber(this))){
			return null;
		}else {
			return DigestUtils.md5Hex(AppUtil.getIEMI(this)+AppUtil.getSimSerialNumber(this));
		}
	}
	/**
	 *	A	会话标示(打开软件以后生成的ID，退出前唯一)
	 * @param Str_B B	类别
	 * @param Str_C C	模块/按钮/操作
	 * @param Str_D D	开始时间
	 * @param Str_F E	结束时间
	 * @param Str_E F	状态(0 正常；其他 异常)
	 * @param Str_G G	失败原因
	 */

//	public static  void saveProbeMsg(String Str_B,String Str_C,String Str_D,String Str_E ,String Str_F,String Str_G) {
//		MessageJson probeMessage=new MessageJson();
//		probeMessage.put("A", TEMP_PROBE_ID);
//		probeMessage.put("B", Str_B);
//		probeMessage.put("C", Str_C);
//		probeMessage.put("D", Str_D);
//		probeMessage.put("E", Str_E);
//		probeMessage.put("F", Str_F);
//		probeMessage.put("G", Str_G);
////		saveProbeMsg(probeMessage,false);
//		
//	}
	/**
	 *	A	会话标示(打开软件以后生成的ID，退出前唯一)
	 * @param Str_B B	类别
	 * @param Str_C C	模块/按钮/操作
	 * @param Str_D D	开始时间
	 * @param Str_F E	结束时间
	 * @param Str_E F	状态(0 正常；其他 异常)
	 * @param Str_G G	失败原因
	 * @param isSend  是否发送
	 */
	
//	public static  void saveProbeMsg(String Str_B,String Str_C,String Str_D,String Str_E ,String Str_F,String Str_G,boolean isSend) {
//		MessageJson probeMessage=new MessageJson();
//		probeMessage.put("A", TEMP_PROBE_ID);
//		probeMessage.put("B", Str_B);
//		probeMessage.put("C", Str_C);
//		probeMessage.put("D", Str_D);
//		probeMessage.put("E", Str_E);
//		probeMessage.put("F", Str_F);
//		probeMessage.put("G", Str_G);
////		saveProbeMsg(probeMessage,isSend);
//		
//	}
	/**
	 * 全局提供探针信息存出 
	 * @param mProbeJson
	 */
//	public static   void saveProbeMsg(MessageJson mProbeJson){
//   	 saveProbeMsg(mProbeJson, false);
//    }
	/**
	 * * 判断条数 先存储 后请求 
	 * 防止请求失败 丢下已经记录的数据
	 * @param mProbeJson
	 * @param isForthwithSend 是否立刻发送
	 */
//    public static  void saveProbeMsg(MessageJson mProbeJson ,boolean isForthwithSend){
////    	////System.out.println("1307==size:"+probeMsgList.size() );
//		if(probeMsg!=null || isForthwithSend ){
////			readTosendProbeMsg();
//	    		String msgtext=null;
//	    		 int  tem_id = 0;
//	    		if(db==null){
//	    			db= MyApp.datacache.getWritableDatabase();
//	    		}
//	//    		先插入数据 insert 
//	    		ContentValues cv = new ContentValues();
//	    		cv.put("msg", mProbeJson.toString());
//	    		long tempLong = db.insert("probemsg", null, cv);
//	    		////System.out.println("1307==="+tempLong);
//	//    		后查询数据 query
//	    		String[] outcloums={"_id","msg"};
//	    		Cursor result= db.query("probemsg",outcloums,null,null,null,null,null);
//	    		try {
//	    			////System.out.println("1307==result.getCount()  :"+result.getCount());
//					if(result.getCount() >= 20 ){
//						
//						List<MessageJson> probemsgList=new ArrayList<MessageJson>();
//						
//						/* 废弃以下方式
//						 * 在没有网络的前提下数据量会达到上百条数据 容易引起OOM
//						 * while (result.moveToNext()) {
//							msgtext=result.getString(result.getColumnIndex("msg"));
//							if(AppUtil.isEmpty(msgtext)){
//								return;
//							}
//							MessageJson mJson=new MessageJson(msgtext);
//							probemsgList.add(0,mJson);
//						}*/
//						for (int i = 0; i < 20; i++) {
//							if(result.moveToNext()){
//								msgtext=result.getString(result.getColumnIndex("msg"));
//								
//								if(AppUtil.isEmpty(msgtext)){
//									return;
//								}
//								MessageJson mJson=new MessageJson(msgtext);
//								probemsgList.add(0,mJson);
//								////System.out.println("1307==probemsgList.size() : "+probemsgList.size());
//								
//								if(19 == i){
//									tem_id=result.getInt(result.getColumnIndex("_id"));
//									////System.out.println("1307===_id=="+tem_id);
//								}
//							}
//						}
//						////System.out.println("1307==probemsgList.toString()"+probemsgList.toString());
//						
//						MessageJson jsonMsg = new MessageJson();
//						jsonMsg.put("LIST",new JSONArray(probemsgList.toString()));
//						
//					   tosaveProbeMsg(db,jsonMsg,tem_id);
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//		}
//    }
    
   
	class TCException implements UncaughtExceptionHandler{
		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			final StringBuilder sb = new StringBuilder();
			final File file = new File(Environment.getExternalStorageDirectory(), "error.txt");
			try {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				ex.printStackTrace();
				ex.printStackTrace(pw);
				Throwable cause = ex.getCause();
				while (cause != null) {
					cause.printStackTrace(pw);
					cause = cause.getCause();
				}
				
				sb.append("\n--------------------exception start------------------- \n"+sw.toString()+"\n--------------------exception end------------------- \n");
				Field[] fields = Build.class.getFields();
				sb.append("\n--------------------device info start------------------- \n");
				for (Field field : fields) {
					sb.append(field.getName()+" = "+field.get(null)+" | ");
				}
				sb.append("\n--------------------device info end------------------- \n");
				
				PrintWriter printWriter = new PrintWriter(new FileOutputStream(file, true));
				printWriter.write(sb.toString());
				pw.close();
				printWriter.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			sendError(sb, file,true);
		}
	}
	/**
	 * 发送错误日志到服务端,内部未封装文件的读取
	 * @param sb
	 * @param file
	 * @param isCrashed
	 */
	private void sendError(final StringBuilder sb, final File file,final boolean isCrashed) {
		//把错误信息发送到后台,并把保存的文件删除
		new Thread(){
			@Override
			public void run() {
				super.run();
				try {
					MessageJson jsonMsg = new MessageJson();
					jsonMsg.put("A", sb.toString());
					MessageBean msg = BaseEngine.getCMD(1306, jsonMsg);//把异常信息发送到服务端
					file.delete();
				} catch (MessageException e) {
					e.printStackTrace();
				}finally{
					if(isCrashed){
						android.os.Process.killProcess(android.os.Process.myPid());
						 int   temp_id=SPUtil.getInt(getString(R.string.probe_id), 1);
							if(99 <= temp_id ){
								temp_id=1;
							}else{
								temp_id +=1;// id 在1-99 内循环自增
							}
							SPUtil.putInt(getString(R.string.probe_id), temp_id);// 存储id 
							TEMP_PROBE_ID=new SimpleDateFormat("yyMMdd").format(new Date());
							TEMP_PROBE_ID +=FormatUtil.ballNumberFormat(temp_id);
							////System.out.println("1307==tempDataID    已经修改为:"+MyApp.TEMP_PROBE_ID);
							
//							saveProbeMsg("android.os.Process.killProcess(android.os.Process.myPid())","异常退出泰彩-TEMP_PROBE_ID-已经更新",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
//									,"0",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),"");
							
					}
				}
			}
		}.start();
	}
	/**
	 * 发送探针信息到服务端     此方式会引起 OOM   1.1.3 以后的版本已经废弃使用
	 * @param sb
	 * @param file
	 * @param probeMsgList2 
	 * @param isCrashed
	 */
	@Deprecated
	private static  void readTosendProbeMsg() {
		//把错误信息发送到后台,并把保存的文件删除
		new Thread(){
			@Override
			public void run() {
				super.run();
				try {
//					saveAndSendProbeMsg();
				} catch (Exception e) {
					e.printStackTrace();
//					sendProbeMsg();//  写入失败 直接发送
				}
			}

//			private synchronized void saveAndSendProbeMsg() throws Exception {
//					if(Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
////	 先存储
//						File file = new File(Environment.getExternalStorageDirectory().getPath()+File.separator + "probeMsg.txt");///storage/sdcard0/probeMsg.txt
//							if(!file.exists()){
//								file.createNewFile();
//							}
//							PrintWriter mPrintWriter=new  PrintWriter(new FileOutputStream(file, true));
//							for (int i = 0; i < probeMsg.size(); i++) {
//								mPrintWriter.write(probeMsg.get(i).toString()+",");
//								////System.out.println("1307==mPrintWriter.write :"+probeMsgList.get(i).toString()+"\n");
//							}
//							mPrintWriter.close();
//// 读取 并发送
//						final StringBuilder sb = new StringBuilder();
//						if(file.exists()){
//							BufferedReader br = new BufferedReader(new FileReader(file));
//							String line = null;
//							while((line = br.readLine()) != null){
//								sb.append(line);
//							  }
//							br.close();
//					 	}
//						
//						MessageJson jsonMsg=null;
//						MessageBean msg=null;
//						String tempStr=sb.toString().trim();
//						if(AppUtil.isEmpty(tempStr)==false){//  不为null
//							jsonMsg=new MessageJson();
//							JSONArray ja=new JSONArray(tempStr);
//							if(ja!=null){
//							  jsonMsg.put("LIST",ja);
//							////System.out.println("1307==read jsonMsg  : "+jsonMsg);
//								msg = BaseEngine.getCMD(1307, jsonMsg);//把探针信息发送到服务端
//								if(msg!=null && "0".equals(msg.getA())){// 发送成功
//									probeMsg.clear();//发送成功 清空
//									////System.out.println("1307==ok! file.delete()");
//									file.delete();
//								}
//							}
//						
//						// 防止文件读取之后由于格式等问题 导致不能正真请求1307 把探针信息提交给服务器 --比较稳妥的方式
//						}else if(1 <= probeMsg.size()){
//							jsonMsg = new MessageJson();
//							jsonMsg.put("LIST",new JSONArray(probeMsg.toString()));
//							////System.out.println("1307== send probeMsgList  : "+jsonMsg.toString());
//							msg= BaseEngine.getCMD(1307, jsonMsg);//把探针信息发送到服务端
//							if("0".equals(msg.getA())){// 发送成功
//								probeMsg.clear();//发送成功 清空
//								////System.out.println("1307==ok! file.delete()");
//								file.delete();
//						     }
//						}
//					}else{
//						   sendProbeMsg();//  存储在本地失败 直接发送
//						   return;
//					}
//						
//			}
		}.start();
	}
	private static void tosaveProbeMsg(final SQLiteDatabase db,final MessageJson jsonMsg, final int tem_id) {
		new Thread(){
    	  @Override
    	public void run() {
    		super.run();
    		try {
				MessageBean msg = BaseEngine.getCMD(1307, jsonMsg);//把探针信息发送到服务端
				if("0".equals(msg.getA())){// 发送成功
					db.delete("probemsg", "_id <?", new String[]{String.valueOf(tem_id)});
//					db.delete("probemsg", null, null);
					////System.out.println("1307== msg.getA() :"+msg.getA());
	    			db.execSQL("delete from 'probemsg';");
	    			db.execSQL("update sqlite_sequence set seq=0 where name='probemsg';");

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	
		  }
      }.start();
	}
	/**
	 * 直接发送  没有内存卡的时候
	 * 发送探针信息到服务端
	 * @param sb
	 * @param file
	 * @param isCrashed
	 */
	@Deprecated
//	private static void sendProbeMsg() {
//		//把错误信息发送到后台,并把保存的文件删除
//		new Thread(){
//			@Override
//			public void run() {
//				super.run();
//				try {
//					if(0 == probeMsg.size()){
//						return;
//					}
//					MessageJson jsonMsg = new MessageJson();
//					jsonMsg.put("LIST", probeMsg);
//					MessageBean msg = BaseEngine.getCMD(1307, jsonMsg);//把探针信息发送到服务端
//					if("0".equals(msg.getA())){// 发送成功
//						probeMsg.clear();//发送成功 清空
//						////System.out.println("1307==probeMsgList.clear() 没有发现sd 直接发送!");
//						saveProbeMsg("android.os.Process.killProcess(android.os.Process.myPid())","没有发现sd 直接发送!-请求1307",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
//								,"0",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),"");
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}.start();
//	}
	
	/**
	 * 发送错误日志到服务端
	 * @param isCrashed 如果是异常引起的,就关闭当前的进程
	 */
	private void sendError() {//发送错误日志到服务端
		try {
			final File file = new File(Environment.getExternalStorageDirectory(),"error.txt");
			final StringBuilder sb = new StringBuilder();
			if(file.exists()){
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line = null;
				while((line = br.readLine()) != null){
					sb.append(line);
				}
				br.close();
				sendError(sb,file,false);
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//   public static WindowManager.LayoutParams   getMywmParams(){
//	   
//	   if(wmParams==null){
//		   wmParams=new WindowManager.LayoutParams();
//	   }
//	   return wmParams;
//   }
//   public static WindowManager getMywm(){
//	   if(wm==null){
//		   wm=(WindowManager)context.getApplicationContext().getSystemService(("window"));
//	   }
//	  return wm;
//   }
}
