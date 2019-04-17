package com.mitenotc.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.Toast;

import com.ab.view.listener.AbOnItemClickListener;
import com.ab.view.sliding.AbSlidingPlayView;
import com.mitenotc.bean.ImageBean;
import com.mitenotc.bean.MessageBean;
import com.mitenotc.bean.UserBean;
import com.mitenotc.dao.Cache_1308;
import com.mitenotc.dao.Cache_1358;
import com.mitenotc.engine.BaseEngine;
import com.mitenotc.enums.CustomTagEnum;
import com.mitenotc.exception.MessageException;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.tc.ThirdActivity;
import com.mitenotc.ui.TCDialogs.MyClickedListener;
import com.mitenotc.ui.TCDialogs.OkClickedListener;
import com.mitenotc.ui.account.AccountUtils;
import com.mitenotc.ui.account.DownLoadUtil;
import com.mitenotc.ui.base.BaseActivity;
import com.mitenotc.ui.ui_utils.MemoryManager;
import com.mitenotc.utils.AppUtil;
import com.mitenotc.utils.SPUtil;
/**
 * 我的定制 界面
 * @author mitenotc
 */
public class BodyFragment1 extends BodyFragment2{
	private static BodyFragment1 body1;
//	private List<Integer> customCodes;//用户自定义的界面的 id的集合
//	private int[] defaultCustCodes = {118,1002,1003};//默认的 界面 id;//118双色球//1001购彩大厅,1002,开奖信息,1003,用户中心,1004消息中心
	private String custom_code_oncreate;
	private List<CustomTagEnum> requiredTags;//不可删除的界面id
	private static boolean isCustomTagChanged;
	private int temp=0;
	private  MSGMyBroadcastReceiver mReceiver;
	private String ACTION    ="com.mitenotc.ui.account.ConsultationFragment";
	private AbSlidingPlayView mSlidingPlayView = null;
	
	private ImageView mPlayImage;
	
	Handler mHandler=new Handler(){
    	@Override
    	public void handleMessage(Message msg) {
    		super.handleMessage(msg);
    		if(temp == msg.what){
    			items = restoreTags();
    			mAdapter.notifyDataSetChanged();
    		}
    	}
    };
    // 8-19 增加
    class MSGMyBroadcastReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			if(ACTION.equals(intent.getAction()) && intent!=null){  
				 temp=Cache_1358.getMSG_COUNT();
				 if(temp > 0){
					 mHandler.sendEmptyMessage(temp);
				 }
		    }
		}
    	
    }
    
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		if(body1 == null)
		{
			body1 = this;
		}
		// requiredTags定制的东西不能被删除
		requiredTags = new ArrayList<CustomTagEnum>();
		requiredTags.add(CustomTagEnum.awardInfo);//开奖信息
		requiredTags.add(CustomTagEnum.recharge);//账户充值 
//		requiredTags.add(CustomTagEnum.message_center);//消息中心 可以删除
		requiredTags.add(CustomTagEnum.consultation);//在线咨询
		requiredTags.add(CustomTagEnum.perfectBankInfo);//完善银行卡信息
		items = requiredTags;
		custom_code_oncreate = SPUtil.getString(R.string.custom_codes);
		
		if(AppUtil.isEmpty(custom_code_oncreate)){
//			items.add(0,CustomTagEnum.lottery_ssq);//双色球 118,120,112,1006
//			items.add(1,CustomTagEnum.lottery_k3);//快三 120
//			items.add(2,CustomTagEnum.lottery_11x5);//十一选五 112
			items.add(CustomTagEnum.perfectBankInfo);//银行卡信息
			items.add(0,CustomTagEnum.lottery_dlt);//大乐透
			items.add(1,CustomTagEnum.lottery_pk3);//快乐扑克
			items.add(2,CustomTagEnum.lottery_11x5);//十一选五 112
			items.add(CustomTagEnum.message_center);//消息中心 可以删除
			
			saveTagIds(requiredTags);
          //sendRequest(3);
		}else{
			items = restoreTags();
			items.add(CustomTagEnum.perfectBankInfo);//完善银行卡信息
			items.add(CustomTagEnum.awardInfo);//开奖信息
//			items.add(CustomTagEnum.recharge);//账户充值
//			items.add(CustomTagEnum.message_center);//消息中心
			items.add(CustomTagEnum.consultation);//在线咨询
		}
		super.onCreate(savedInstanceState);
		network_notice_required = false;
		handler.sendEmptyMessage(1000);
		mListView.setVisibility(View.VISIBLE);
		// 注册广播
		mReceiver=new MSGMyBroadcastReceiver();// 实例化广播
		IntentFilter mFilter=new IntentFilter();
		mFilter.addAction("com.mitenotc.ui.account.ConsultationFragment");
		mActivity.registerReceiver(mReceiver, mFilter);
		
		setTitleNav(R.string.title_nav_custom, 0, 0);
		isArrowVisible = false;
		mListView.disablePullLoad(); 
		mListView.setPullRefreshEnable(false);////System.out.println("------------------------isLogin()--------- = "+UserBean.getInstance().isLogin());
		sendRequest_awardInfo();
		loadImagePlay();
	}

	private void loadImagePlay() {
		
		mSlidingPlayView = (AbSlidingPlayView) this.findViewById(R.id.mAbSlidingPlayView);
	//	mSlidingPlayView.setPageLineHorizontalGravity(Gravity.RIGHT);
		
		// 设置高度
		mSlidingPlayView.setOnItemClickListener(new AbOnItemClickListener() {
			@Override
			public void onClick(int position) {
				ImageBean bean = MyApp.listPic1308.get(position);
				String aString = bean.getType();
				String dString = bean.getAddr();
				if("1".equals(aString)){//启动彩种界面
					try {
						int dInt = 0;
						if(!AppUtil.isEmpty(dString)){
							dInt = Integer.parseInt(dString);
						}
						CustomTagEnum.startActivityWithId(mActivity, null,dInt);
					} catch (Exception e) {
						return;
					}
					
				}else if("2".equals(aString)){//启动WebView
					try {
						Bundle mBundle = new Bundle();
						mBundle.putString("url", dString);
						mBundle.putString("title", mActivity.getResources().getString(R.string.app_name));
						start(ThirdActivity.class, TCWebFragment.class,mBundle);
					} catch (Exception e) {
						return;
					}
					
				}else if("3".equals(aString)){//启动手机默认浏览器
					try {
						Intent intent = new Intent();
						intent.setAction("android.intent.action.VIEW");    
						Uri content_url = Uri.parse(dString);   
						intent.setData(content_url);  
						startActivity(intent);
					} catch (Exception e) {
						// TODO: handle exception
						return;
					}

				}
			}
		});		
		
		if(MyApp.listPic1308!= null && MyApp.listPic1308.size()>0)
		{
				for (int i = 0; i < MyApp.listPic1308.size(); i++) {
					ImageBean bm = MyApp.listPic1308.get(i);
					
					Bitmap bitmap = bm.getBm();
					View mPlayView = View.inflate(mActivity, R.layout.play_view_item,null);
					mPlayImage = (ImageView) mPlayView.findViewById(R.id.mPlayImage);
					mPlayImage.setBackgroundResource(R.color.gray_white);
					mPlayImage.setImageBitmap(bitmap);
					mSlidingPlayView.addView(mPlayView);
				}
				mSlidingPlayView.startPlay();	
				mSlidingPlayView.setVisibility(View.VISIBLE);

		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		items = restoreTags();
		initmAdapter();
		setTitleNav(R.string.title_nav_custom, 0, 0);
		checkVerson();
//		sendRequest(3);//System.out.println("1302----------------");
	}
	@Override
	public void onPause() {
		if(isCustomTagChanged){//如果customtags 需要发送到服务端保存
			sendRequest(4);//提交 我的定制的 数据
			saveTagIds(items);
		}
		super.onPause();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(mReceiver!=null){
			mActivity.unregisterReceiver(mReceiver);
			mReceiver=null;
		}
	}
	
	@Override
	protected void initItems() {//这里的items 的处理与 购彩大厅的不同
	}
	
//	@Override
//	protected void initItems() {////System.out.println("-------");
//		refresh();
//	}
 
//	/**
//	 * 刷新该界面的数据
//	 */
//	private void refresh() {
//		refreshCustomCodes();
//		reSetItems();
//		initmAdapter();
//	}
//	/**
//	 * 从sp 中从新加载 customCodes数据
//	 */
//	private void refreshCustomCodes() {
//		customCodes = getCustomCodes();
//		if(customCodes.size() == 0){//如果用户还未自定义,则用默认的设置
//			for (int i = 0; i < defaultCustCodes.length; i++) {
//				customCodes.add(defaultCustCodes[i]);
//			}
//			saveCustomCodes(customCodes);//保存默认的设置
//		}
//	}
//	/**
//	 * 从新加载item 中的数据
//	 */
//	private void reSetItems() {
//		if(items == null){
//			items = new ArrayList<CustomTagEnum>();
//		}else {
//			items.clear(); 
//		}
////		for (int lotteryId : customCodes) {
////			CustomTagEnum item = CustomTagEnum.getItemById(lotteryId);
////			if(item != null)
////				items.add(item);
////		}
//	}
 

	public void sendRequest_awardInfo(){//开奖公告查询
		MessageJson msg = new MessageJson();
		msg.put("C", "1");
		submitData(1201, 1201, msg);
	}
	public void sendRequest(int key){
		if(!UserBean.getInstance().isLogin())
			return;
		int cmd = 1302;
		MessageJson msg = new MessageJson();
		msg.put("A", "3");
		if(key == 4){//设置 我的定制. 如果是3, 就是查询我的定制, 没有 b标签
			msg.put("B", SPUtil.getString(R.string.custom_codes));
			cmd = 1303;
		}
		submitData(key, cmd, msg);
	} 
	@Override
	protected void onMessageReceived(Message msg) {////System.out.println("---------------------onmessage received-------------------"+msg.arg1);
		super.onMessageReceived(msg);
		MessageBean messageBean = (MessageBean) msg.obj;
		////System.out.println("arg1 =========="+msg.arg1);
		switch (msg.arg1) {
		case 3://从新加载 我的定制的列表 数据//改成 用户登录时获取数据
			/*mList = messageBean.getLIST();////System.out.println("mlist =================== "+mList);
			if(mList != null && mList.size() != 0){
				for (MessageBean bean : mList) {////System.out.println("items = "+bean.getA());
					CustomTagEnum.convertCustomTag(bean);
				}
			}
			String string = messageBean.getC();////System.out.println("messageBean.getC(); = " +messageBean.getC());
			if(!AppUtil.isEmpty(string) && !string.equals(SPUtil.getString(R.string.custom_codes))){
				SPUtil.putString(R.string.custom_codes, string);
//				////System.out.println("messageBean.getC(); = " +messageBean.getC());
			}
			items = restoreTags();
//			if(AppUtil.isEmpty(string)){
//				items = requiredTags;
//			}
			initmAdapter();*/
			break;
		case 4://提交我的定制参数串 到服务器
			isCustomTagChanged = false;//保存到服务端成功, 无需重新发送
			break;
		case 1201://开奖详情
			mList = messageBean.getLIST();////System.out.println("mlist =================== "+mList);
			if(mList != null && mList.size() != 0){
				for (MessageBean bean : mList) {////System.out.println("items = "+bean.getA());
					CustomTagEnum.convertCustomTag_awardInfo(bean);
				}
			}
			initmAdapter();
			break;
		case 1302://开奖详情
			mList = messageBean.getLIST();////System.out.println("mlist =================== "+mList);
			if(mList != null && mList.size() != 0){
				for (MessageBean bean : mList) {////System.out.println("items = "+bean.getA());
					CustomTagEnum.convertCustomTag_awardInfo(bean);
				}
			}
			initmAdapter();
			break;
		}
	}
	 
	
	@Override
	protected void onItemLongClicked(final int position, View convertView,final CustomTagEnum item) {
		convertView.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				final TCDialogs dialogs = new TCDialogs(mActivity);
				dialogs.popCustomDialog(item.getLotteryName(), new View.OnClickListener() {//置顶
					@Override
					public void onClick(View v) {
						if(position != 0){
							items.remove(item);
							items.add(0,item);
							saveTagIds(items);
							mAdapter.notifyDataSetChanged();
						}
						
						dialogs.dismiss();
					}
				}, new View.OnClickListener() {//上移
					@Override
					public void onClick(View v) {
						if(position != 0){
						items.remove(item);
						items.add(position-1,item);
						saveTagIds(items);
						mAdapter.notifyDataSetChanged();
						}
						dialogs.dismiss();
					}
				}, new View.OnClickListener() {//下移
					@Override
					public void onClick(View v) {// TODO 下移bug 闪退
						if(position != items.size()-1){
							items.remove(item);
							items.add(position+1,item);
							saveTagIds(items);
							mAdapter.notifyDataSetChanged();
							}
						dialogs.dismiss();
					}
				}, new View.OnClickListener() {//移除应用
					@Override
					public void onClick(View v) {
						dialogs.dismiss();
						System.out.println("d----------requiredTags :"+requiredTags.toString());
						System.out.println("d----------item :"+item.getId());
						if(requiredTags.contains(item)){
							Toast.makeText(mActivity, "该应用不可移除", 0).show();
							return;
						}
						items.remove(item);
						deleteTag(item);
						mAdapter.notifyDataSetChanged();
						dialogs.dismiss();
					}
				}, new View.OnClickListener() {//发送到桌面
					@Override
					public void onClick(View v) {
						createShortCut(item);
						dialogs.dismiss();
					}
				});
				return true;
			}
		});
	}
	
	
	@Override
	protected void onRightIconClicked() {
	}
	
	@Override
	protected void setExtras(int position, View convertView,ViewHolder holder, CustomTagEnum item) {
//		holder.item_hall_tv_issue.setVisibility(View.GONE);
	}
	
//	public static void saveCustomCodes(List<Integer> codes){//各个界面的id
//		String custom_codes = getStringCustomCodes(codes);
//		SPUtil.putString(R.string.custom_codes, custom_codes);
//	}
//
//	/**
//	 * 把 customCodes 保存到sharedpreference 中
//	 * @param codes
//	 * @return
//	 */
//	private static String getStringCustomCodes(List<Integer> codes) {
//		String custom_codes = "";
//		for (Integer code : codes) {
//			custom_codes += ","+code;
//		}
//		custom_codes = custom_codes.substring(1);
//		return custom_codes;
//	}
//	/**
//	 * 从sharedPreference 中获取 customCodes
//	 * @return
//	 */
//	public static List<Integer> getCustomCodes(){
//		String custom_codes = SPUtil.getString(R.string.custom_codes);
//		List<Integer> customCodes = new ArrayList<Integer>();
//		String[] codes = custom_codes.split(",");
//		for (int i = 0; i < codes.length; i++) {
//			if(!AppUtil.isEmpty(codes[i]))
//				customCodes.add(Integer.parseInt(codes[i]));
//		}
//		return customCodes;
//	} 
//	/**
//	 * 添加一个自定义界面,默认加到顶部
//	 * @param code
//	 */
//	public static void addCustomCode(int code){//
//		List<Integer> customCodes = getCustomCodes();
//		customCodes.remove((Object) code);
//		customCodes.add(0,code);
//		saveCustomCodes(customCodes);
//	}
	
	/**
	 * 从 界面的 CustomTagEnum 的集合中取出 界面id 拼接成字符串 存到sharedpreference 中
	 * @param items
	 */
	public static void saveTagIds(List<CustomTagEnum> items){
		if(items == null || items.size()<1){
			SPUtil.putString(R.string.custom_codes, "");
			return;
		}
		String tagIds = "";
		String tempIds = SPUtil.getString(R.string.custom_codes);
		for (CustomTagEnum item : items) {
//			if(!"1009".equals(String.valueOf(item.getId()))){// 不包含
				tagIds += ","+item.getId();
//			}
		}
		if(tagIds.length() > 0){
			SPUtil.putString(R.string.custom_codes, tagIds.substring(1));
		}
		isCustomTagChanged = true;
		if(body1 != null){
			body1.sendRequest(4);
		}
	}
	
	private static String filterTheRepeat(String orin,String regex){
		String dest = "";
		String[] splits = orin.split(regex);
		for (int i = 0; i < splits.length; i++) {
			if(!dest.contains(splits[i]))
				dest += splits[i]+",";
		}
		return dest.substring(0,dest.length()-1);
	}
	
	/**
	 * 从 sharedpreference 中取出 界面id 拼接的字符串, 恢复到 customTagEnum 集合中, 并且返回该集合
	 * @return
	 */
	public static List<CustomTagEnum> restoreTags(){
		String tagsStr = SPUtil.getString(R.string.custom_codes);////System.out.println("tagsStr======"+tagsStr);
		tagsStr= "2000,"+tagsStr ;//添加完善银行卡信息
		System.out.println("tagsStr="+tagsStr);
		if(AppUtil.isEmpty(tagsStr)){
			tagsStr ="2000,112,1006,1009,1002";//默认有的但是可以删除
			SPUtil.putString(R.string.custom_codes, tagsStr);
		}
		
		tagsStr = filterTheRepeat(tagsStr, ",");
		
		String tagsStr2 = SPUtil.getString(R.string.lottery_ids);//服务器端支持彩种
		if(AppUtil.isEmpty(tagsStr))
			return null;
		List<CustomTagEnum> tags = new ArrayList<CustomTagEnum>();
		String[] tagsArr = tagsStr.split(",");
//		String[] tagsArr2 = tagsStr2.split(",");
		
		if(Cache_1358.getMSG_COUNT()>0)
	     	tags.add(CustomTagEnum.getItemById(Integer.parseInt("1009")));//  TODO 8-12 咨询中心有新消息置顶
//		if(tagsArr2.length >= tagsArr.length){
//			for (int i = 0; i < tagsArr.length; i++) {
//				for (int j = 0; j < tagsArr2.length; j++) {// 筛选服务器端支持的彩种显示,服务器端不销售的彩种 即使用户定制了也不显示
//					if(tagsArr[i].equals(tagsArr2[j])){
//						if(!AppUtil.isNumeric(tagsArr[i]) || CustomTagEnum.getItemById(Integer.parseInt(tagsArr[i])) == null)
//							continue;
//						
//						if(Cache_1358.getMSG_COUNT()>0)
//						{
//							if(!"1009".equals(tagsArr[i]) )
//							{
//								tags.add(CustomTagEnum.getItemById(Integer.parseInt(tagsArr[i])));
//							}
//						}else
//						{
//							tags.add(CustomTagEnum.getItemById(Integer.parseInt(tagsArr[i])));
//						}
//					}
//				}
//			}
//		}else{
			for (String string : tagsArr) {
				if(!AppUtil.isNumeric(string) || CustomTagEnum.getItemById(Integer.parseInt(string)) == null)
					continue;
				if(Cache_1358.getMSG_COUNT()>0)
				{
					if(!"1009".equals(string) )
					{
						tags.add(CustomTagEnum.getItemById(Integer.parseInt(string)));
					}
				}else
				{
					tags.add(CustomTagEnum.getItemById(Integer.parseInt(string)));
				}
			}
//		}
		return tags;
	}
	/**
	 * 添加一个 tag 到 sharedpreference 中
	 * @param id
	 */
	public static void addTag(BaseActivity activity,int id){
		String tags = SPUtil.getString(R.string.custom_codes);
		if(!AppUtil.isEmpty(tags) && (","+tags+",").contains(","+id+",")){
			Toast.makeText(activity, "该模块已经定制", 0).show();
			return;
		}else{
			Toast.makeText(activity, "定制成功", 0).show();
			tags = id + "," +tags;
			SPUtil.putString(R.string.custom_codes, tags);////System.out.println("tag =++++++++++++++++++++ "+tags);
			isCustomTagChanged = true;//需要发送新的 customtags 到服务端保存
			body1.sendRequest(4);
		}
	}
	public static boolean isTagSaved(int id){
		String tags = SPUtil.getString(R.string.custom_codes);////System.out.println("1111 tag =++++++++++++++++++++ "+tags);
		if(!AppUtil.isEmpty(tags) && (","+tags+",").contains(","+id+",")){
			return true;
		}
		return false;
	}

	/**
	 * 移除并保存新的 items 的tagids 到 sharedpreferenced 中
	 * 特定的移除方法,仅限于该类使用
	 * @param tag
	 */
	public void deleteTag(CustomTagEnum tag){
		if(requiredTags.contains(tag)){
			Toast.makeText(mActivity, "该模块不可移除", 0).show();
			return;
		}
		items.remove(tag);
		saveTagIds(items);
	}
	private void checkVerson() 
	{
		
		if(MyApp.updateBean == null)
			return;
		try{
					if ("0".equals(MyApp.updateBean.getA())) 
					{
						TCDialogs t = new TCDialogs(mActivity);
						t.setCancelable(false);
						t.popUpgrade(new OkClickedListener() {
							
							@Override
							public void okClicked() {
								if(!MemoryManager.externalMemoryAvailable()||MemoryManager.getAvailableExternalMemorySize() < 5.5 * 1024 * 1024){
									Toast.makeText(mActivity, "无SD卡或SD卡容量不足！", Toast.LENGTH_SHORT).show();
									return;
								}
								//这里不能用当前的context。因为创建dialog需要依附context
								DownLoadUtil dlu = new DownLoadUtil(mActivity);
								dlu.createDialog(MyApp.updateBean.getD());
								dlu.createNotification();
								MyApp.updateBean = null;
								
							}
						}, MyApp.updateBean.getE(), new MyClickedListener() {
							
							@Override
							public void onClick() {
								MyApp.updateBean = null;
							}
						});
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
	}
	
} 
