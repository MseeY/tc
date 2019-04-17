package com.mitenotc.tc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.mitenotc.bean.MessageBean;
import com.mitenotc.bean.UserBean;
import com.mitenotc.dao.Cache_1308;
import com.mitenotc.dao.Cache_1309;
import com.mitenotc.dao.ImageFileCache;
import com.mitenotc.engine.BaseEngine;
import com.mitenotc.enums.CustomTagEnum;
import com.mitenotc.exception.MessageException;
import com.mitenotc.net.MessageJson;
import com.mitenotc.service.TCService;
import com.mitenotc.ui.TCDialogs;
import com.mitenotc.ui.TCDialogs.OkClickedListener;
import com.mitenotc.ui.account.DownLoadUtil;
import com.mitenotc.ui.account.SplashFragment;
import com.mitenotc.ui.base.BaseActivity;
import com.mitenotc.ui.base.BottomFragment;
import com.mitenotc.ui.ui_utils.MemoryManager;
import com.mitenotc.utils.AppUtil;
import com.mitenotc.utils.SPUtil;
/**
 * 主界面的Activity 属于第一层
 * @author mitenotc
 */
public class TCActivity extends BaseActivity{
	
	private BottomFragment bottomFragment;
	@Override
	protected void onCreate(Bundle arg0) 
	{
		super.onCreate(arg0);
		try {
//			2015-1-27 服务耗电量超过27%  设置消息轮询间隔时间 
		sendBroadcast(new Intent("android.intent.action.Start_TCService"));
			
		if(bottomFragment == null)
		{
			bottomFragment = new BottomFragment();
		}
		replaceBottom(bottomFragment);
		MyApp.splashbitmap = new Cache_1309().getPicture();
		MyApp.listPic1308 =  new Cache_1308().getListPic1308();
		
		//头像的缓存加载
		ImageFileCache cache = new ImageFileCache();
		File myCaptureFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/taicai/header.png");
		if(myCaptureFile.exists()){
			MyApp.header_photo = cache.getImageFromFile2(Environment.getExternalStorageDirectory().getAbsolutePath() + "/taicai/header.png");
		}
		
		copyAssetDBFile("city.db");
		copyAssetDBFile("orcodetc.png");
		//支付密码的标记为1时，那么置位0，同时密码也清空
//		String payPwdFlag = SPUtil.getString(MyApp.res.getString(R.string.PAY_PWD_FLAG));
//		if("1".equals(payPwdFlag)||AppUtil.isEmpty(payPwdFlag)){
//			SPUtil.putString(MyApp.res.getString(R.string.PAY_PWD_FLAG), "0");
//			SPUtil.putString(MyApp.res.getString(R.string.PAY_PWD_BUY),"");
//		}
		
		intBankInfo();
		
		String customStr=SPUtil.getString(R.string.custom_codes);
		if(AppUtil.isEmpty(customStr)){
			if(ConstantValue.LOTTERY_TYPE){
				SPUtil.putString(R.string.custom_codes, "106,112,121,1002,1009,1006");//默认我的定制
			}else{
				SPUtil.putString(R.string.custom_codes, "118,120,112,1002,1009,1006");//默认我的定制  体彩
			}
		}
		
		if("".equals(SPUtil.getString(MyApp.res.getString(R.string.SPLASH))))
		{
			start(ThirdActivity.class,SplashFragment.class);
		}
		else
		{
			if(MyApp.splashbitmap!= null){
				start(ThirdActivity.class,SplashFragment.class);
			}
		}		

		if(fm == null){ return;}
		targetCls = SecondActivity.class;
		} catch (Exception e) {
			Toast.makeText(this, "重进一次吧", Toast.LENGTH_SHORT).show();
		}
		new Thread(){
			public void run() {
				try {
					MessageJson msg = new MessageJson();
					BaseEngine.getCMD(1308, msg);
					BaseEngine.getCMD(1309, msg);
					MyApp.updateBean = BaseEngine.getCMD(1355,msg);
                    //购彩大厅
					msg.put("A", "6");	
					MessageBean messageBean = BaseEngine.getCMD(1302, msg);
					if("0".equals(messageBean.getA())){
						String string = messageBean.getC();
						if(!AppUtil.isEmpty(string) && !string.equals(SPUtil.getString(R.string.lottery_ids))){
							SPUtil.putString(R.string.lottery_ids, string);
							System.out.println("1302----splash---6--->"+string);
						}
					}
					//我的定制
					msg.put("A", "3");	
					messageBean = BaseEngine.getCMD(1302, msg);
					if("0".equals(messageBean.getA())){
						List<MessageBean> mList = messageBean.getLIST();
						if(mList != null && mList.size() != 0){
							for (MessageBean bean : mList) {
								CustomTagEnum.convertCustomTag(bean);
							}
						}
						String string = messageBean.getC();
						if(!AppUtil.isEmpty(string) && !string.equals(SPUtil.getString(R.string.custom_codes))){
							if(AppUtil.isEmpty(string)){
								if(ConstantValue.LOTTERY_TYPE){//体彩
									SPUtil.putString(R.string.custom_codes, "112,106,121,1002,1009,1006");
								}else{
								    SPUtil.putString(R.string.custom_codes, "118,120,112,1002,1009,1006");
							    }
							}else{
								SPUtil.putString(R.string.custom_codes, string);
							}
							System.out.println("1302----splash---3--->"+string);
						}
					}
				} catch (MessageException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};
		}.start();
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		sendBroadcast(new Intent("android.intent.action.Stop_TCService"));
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		bottomFragment=null;
		MyApp.listPic1308 = null;
		MyApp.splashbitmap = null;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		JPushInterface.onPause(this);
	}

	@Override
	protected void onResume() 
	{
		// TODO Auto-generated method stub
		super.onResume();
		JPushInterface.onResume(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		
		Bundle data = getMyBundle();
		String idStr = getIntent().getStringExtra("window_id");
		if(!AppUtil.isEmpty(idStr)){
			int id = Integer.parseInt(idStr);
			if(id != 0)
			{
				getIntent().putExtra("window_id", "0");
				quickStart(id, data);
			}
		}
		
		showBottomNav();
		
	} 

	public void quickStart(int id, Bundle data){//快捷启动
		CustomTagEnum.startActivityWithId(this, data, id);
	}
	private void copyAssetDBFile(final String filename) {
		// 检查数据库是否存在 如果不存在 拷贝
		File file = new File(this.getFilesDir(), filename);
		if (file.exists() && file.length() > 0) {
		} else {
					try {
						// 获取到资产管理器
						AssetManager am = this.getAssets();
						InputStream is = am.open(filename);
						// data/data/包名/files/
						file = new File(this.getFilesDir(), filename);
						FileOutputStream fos = new FileOutputStream(file);
						int len = 0;
						byte[] buffer = new byte[1024];
						while ((len = is.read(buffer)) != -1) {
							fos.write(buffer, 0, len);
						}
						fos.close();
						is.close();

					} catch (Exception e) {
						e.printStackTrace();
					}
					}
	}
	
	@Override
	public void onBackPressed() { 
		final TCDialogs dialogs = new TCDialogs(this);
		dialogs.popExitDialog(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				MyApp.saveProbeMsg("Exit TC!","退出泰彩提示-选择了确认",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
//						,"0",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),"",true);// 退出之前把探针信息发送一次 不管信息是多少条目(至少大于一条)
//				探针信息
//			    int   temp_id=SPUtil.getInt(getString(R.string.probe_id), 1);
//				if(99 <= temp_id ){
//					temp_id=1;
//				}else{
//					temp_id +=1;// id 在1-99 内循环自增
//				}
//				SPUtil.putInt(getString(R.string.probe_id), temp_id);// 存储id 
//				MyApp.TEMP_PROBE_ID=new SimpleDateFormat("yyMMdd").format(new Date());
//				MyApp.TEMP_PROBE_ID +=FormatUtil.ballNumberFormat(temp_id);
				////System.out.println("1307==tempDataID    已经修改为:"+MyApp.TEMP_PROBE_ID);
				dialogs.dismiss();//Activity 关闭前要先关闭 dialog,不然会出现 leaked window 异常
				finish();
				
			}
		});
		
	}
	
	/** 初始化银行卡信息  **/
	private void intBankInfo() {
		List<Map<String, Drawable>> list = new ArrayList<Map<String,Drawable>>();
		Map<String, Drawable> mapLogos = new HashMap<String, Drawable>();
		String[] bankNames = MyApp.res.getStringArray(R.array.acc_bank_card);
		TypedArray bankLogos = MyApp.res.obtainTypedArray(R.array.acc_bank_logo);
		for (int i = 0; i < bankNames.length; i++) {
			mapLogos.put(bankNames[i], bankLogos.getDrawable(i));
		}
		list.add(mapLogos);
		UserBean.getInstance().setLogoList(list);
		if(bankLogos!=null){
			bankLogos.recycle();
		}
	}

	/*private LinearLayout ll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		startActivity(BaseActivity.class);
		setContentView(R.layout.f_item_award_info_ll);
		TCDialogs dialog = new TCDialogs(this, R.style.dialog_theme);
//		dialog.popHallDialog("ssq");
//		dialog.popCustomDialog("ssq");
//		dialog.popExitDialog();
//		dialog.popBindNumber();
		testPop_menu();
	}

	private void testPop_menu() {
		LinearLayout ll = (LinearLayout) findViewById(R.id.award_info_shengfu_content);
		ll.setVisibility(View.VISIBLE);
		LinearLayout ll2 = (LinearLayout) ll.findViewById(R.id.award_info_shengfu_second_content);
		for (int i = 0; i < 5; i++) {
			
			View tv =  View.inflate(this, R.layout.f_award_info_item_shengfu, null);
				
			ll2.addView(tv);
		}
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		
//		TCPop.showPopMenu(this, R.array.pop_menu_ivs_hall, R.array.pop_menu_tvs_hall, findViewById(R.id.title_nav_iv_right));
	}
	
	private void startActivity(Class<? extends Activity> cls) {
		startActivity(new Intent(this,cls));
		ImageView iv = new ImageView(this);
	}*/
}
