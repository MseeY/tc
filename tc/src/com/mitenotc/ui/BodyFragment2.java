package com.mitenotc.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.provider.Settings;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mitenotc.bean.MessageBean;
import com.mitenotc.dao.Cache_1358;
import com.mitenotc.enums.CustomTagEnum;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.tc.TCActivity;
import com.mitenotc.ui.account.AccountUtils;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.ui.ui_utils.XListView.IXListViewListener;
import com.mitenotc.utils.AppUtil;
import com.mitenotc.utils.FormatUtil;
import com.mitenotc.utils.SPUtil;

/**
 * 主界面购彩大厅界面
 * @author mitenotc
 */
public class BodyFragment2 extends BaseFragment{
 
	private long currentTime;
	protected boolean isArrowVisible;//设置右侧的箭头可见性
	private Map<TextView, CustomTagEnum> mTimerMap;
	private String issue_sample_text = MyApp.res.getString(R.string.item_hall_tv_issue_text);
//	private int[] lotteryIds = {100,102,103,106,112,117,118,119,120};
//	private String lottery_ids_default = "118,106,120,112,119,117,100,102,103,116,123,113,121";//初始化彩种的排列顺序
	private boolean is_lottery_id_prefered;
	private String lottery_ids = "";
	protected List<CustomTagEnum> items;
	private View isWifi;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isFragmentCacheEnable = true;
		mAdapter = null;
//		if(ConstantValue.LOTTERY_TYPE){
//			lottery_ids_default="100,102,103,106";//体彩
//		}else{
//			lottery_ids_default="118,106,120,112,119,117,100,102,103,116,123,113,121";//初始化彩种的排列顺序
//		}
		lottery_ids = SPUtil.getString(R.string.lottery_ids);

		setTitleNav(CustomTagEnum.hall.getId(), getString(R.string.title_nav_hall),0,R.drawable.title_nav_menu);// 定制按钮
		isArrowVisible = true;
		setContentView(R.layout.f_bodyfragment2_layout);
		LinearLayout ll = (LinearLayout) findViewById(R.id.bodyfragment2_ll);
		ll.addView(mListView);
		mListView.setDivider(new ColorDrawable(getResources().getColor(R.color.view_line)));//#cecece
		mListView.setDividerHeight((int) getResources().getDimension(R.dimen.bodyfragment2_lv_divider_height));
		mListView.setSelector(new ColorDrawable(mActivity.getResources().getColor(R.color.login_color_6)));  //背景选择器
		mListView.setBackgroundColor(mActivity.getResources().getColor(R.color.login_color_6));
		mListView.setPullLoadEnable(false);
		mListView.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {
					sendRequest_froce();//强制刷新
					mListView.setRefreshTime(FormatUtil.timeFormat(System.currentTimeMillis()));
			}
			@Override
			public void onLoadMore() {
			}
		});
//		initTimerHelper();
		if(mTimerMap == null){
			mTimerMap = new HashMap<TextView, CustomTagEnum>();
		}
		if(AppUtil.isEmpty(lottery_ids)){
//			lottery_ids = lottery_ids_default;
//			SPUtil.putString(R.string.lottery_ids, lottery_ids);
			is_lottery_id_prefered = true;
			network_notice_required = true;
			handler.sendEmptyMessage(1000);
			mListView.setVisibility(View.GONE);
		}else {
			is_lottery_id_prefered = false;
//			network_notice_required = false;
		   initItems();
		}
	
		initmAdapter();
		sendRequest(0,null);
		
	}
	@Override
	public void onResume() {
		super.onResume();
		CustomTagEnum.startTimer(this, mTimerMap,0);
		setTitleNav(CustomTagEnum.hall.getId(), getString(R.string.title_nav_hall),0,R.drawable.title_nav_menu);// 定制按钮
		isWifi = View.inflate(mActivity, R.layout.no_network_view, null);
		if(!AccountUtils.netConnectivityManager(mActivity)){
			if(mListView.getHeaderViewsCount()<2){
				mListView.addHeaderView(isWifi);
				
			}
			isWifi.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					 Intent intent =  new Intent(Settings.ACTION_SETTINGS);  
			         startActivity(intent);
				}
			});
		}
	}
//	private void initTimerHelper() {
//		if(timerMap == null){
//			timerMap = new HashMap<TextView, CustomTagEnum>();
//		}
//		timerHelper = new TCTimerHelper(new TCTimerHelper.TimerObserver() {
//			@Override
//			public void onUpdate() {
//				update();
//			}
//		});
//	}

	protected void initItems() {
		if(AppUtil.isEmpty(lottery_ids)){
			requestlotteryIds();
		}else{
			String[] lotteryIds = lottery_ids.split(",");
			if(items == null){
				items = new ArrayList<CustomTagEnum>();
			}else {
				items.clear();
			}
//			if(ConstantValue.LOTTERY_TYPE){
//				for (String lotteryId : lotteryIds) {
//					if(lotteryId.equals("100")||lotteryId.equals("102")||lotteryId.equals("103")||lotteryId.equals("106")){
//						CustomTagEnum item = CustomTagEnum.getItemByLotteryId(Integer.parseInt(lotteryId));
//						if(item != null)
//							items.add(item);
//					}
//				}
//			}else{}
				for (String lotteryId : lotteryIds) {
					if(!StringUtils.isBlank(lotteryId)){
						CustomTagEnum item = CustomTagEnum.getItemByLotteryId(Integer.parseInt(lotteryId));
						if(item != null  && isMoNiLottery(lotteryId)){//购彩大厅不显示模拟购彩
							items.add(item);
						}
					}
				}
		}
	}
	/**
	 * 划分彩种支付方式  
	 * 模拟彩种走模拟支付渠道 正式销售彩种走正式的支付渠道
	 * @param getLotteryId
	 * @return
	 */
	private boolean isMoNiLottery(String getLotteryId){
		boolean tag=true;
		String[] miArrayid = MyApp.res.getStringArray(R.array.mi_lottery);
		for (int i = 0; i < miArrayid.length; i++) {
			if(miArrayid[i].equals(getLotteryId)){
				tag=false;
			}
		}
		return tag;
	}
	@Override
	protected void initData() {
	}
	public void sendRequest(int key,Integer lotteryId){
		MessageJson messageJson = new MessageJson();
		messageJson.put("A", lotteryId);
		submitData(key, 1200, messageJson);
	}
	
	public void sendRequest_froce(){
		MessageJson messageJson = new MessageJson();
		messageJson.put("A", null);
		messageJson.put("isforce", 1);
		submitData(0, 1200, messageJson);
	}
	public void requestlotteryIds(){
		MessageJson msg4 = new MessageJson();//同步排序彩种
		msg4.put("A", "6");
		submitData(1302, 1302, msg4);
	}
	public void savelotteryIdsSort(String sortStr){
		MessageJson msg4 = new MessageJson();//同步排序彩种
		msg4.put("A", "6");
		msg4.put("B", sortStr);
		submitData(1303, 1303, msg4);
		System.out.println("id---sub-----1303-6--->"+sortStr);
	}
	 
	 
	@Override
	protected void onMessageReceived(Message msg) { 
		mListView.stopRefresh();
		MessageBean messageBean = (MessageBean) msg.obj;
		switch (msg.arg1) {
		case 0://请求所有的
			mList = messageBean.getLIST();////System.out.println("on case 0 at body2");
			if(mList == null || mList.size() == 0)
				break;
			
			CustomTagEnum.lottery_ids_server = "";//初始化服务端的默认排序为空
			for (MessageBean bean : mList) 
			{
				CustomTagEnum.convertMessage(bean);
			}
//			if(is_lottery_id_prefered || lottery_ids.length() != CustomTagEnum.lottery_ids_server.length()){//如果不是用户自定义的排序就使用服务端的默认排序
//				lottery_ids = CustomTagEnum.lottery_ids_server.substring(1);
				initItems();
//			}
				
				
			initmAdapter();
			break;
		case 1://请求一个彩种的
			if(messageBean.getLIST() != null && messageBean.getLIST().size()>0)
						CustomTagEnum.convertMessage(messageBean.getLIST().get(0));
			break;
		case 1302:// 
			mList = messageBean.getLIST();
			if(mList != null && mList.size() != 0){
				for (MessageBean bean : mList) {
					CustomTagEnum.convertCustomTag(bean);
				}
			}
			String string = messageBean.getC();////System.out.println("messageBean.getC(); = " +messageBean.getC());
			if(!AppUtil.isEmpty(string) && !string.equals(SPUtil.getString(R.string.lottery_ids))){
				SPUtil.putString(R.string.lottery_ids, string);
				lottery_ids =string;//  测试足球
				initItems();
				initmAdapter();
				mListView.setVisibility(View.VISIBLE);
				sendRequest_froce();//强制刷新
				System.out.println("1302--body2---6--c----> " +messageBean.getC());
			}
			break;
		}
	}
	@Override
	protected void nullResult() {
		super.nullResult();
	}
	protected void initmAdapter() {
		if(mAdapter == null){
			mListView.setAdapter(mAdapter = new HallAdapter());
		}else {
			mAdapter.notifyDataSetChanged();
		}
	}
	class HallAdapter extends BaseListAdapter{
		HallAdapter(){
			if(mTimerMap == null){
				mTimerMap = new HashMap<TextView, CustomTagEnum>();
			}else {
				mTimerMap.clear();
			}
		}
		@Override
		public int getCount() {
			if(items == null){
				return 0;
			}
			return items.size();
		}
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final CustomTagEnum item = items.get(position);//获得 相应position 中的item
//			System.out.println("item =========== "+item);
			final ViewHolder holder;
			if(convertView == null){
				convertView = mInflater.inflate( R.layout.f_item_hall, null);
				holder = new ViewHolder();
				holder.item_hall_iv_logo = (ImageView) convertView.findViewById(R.id.item_hall_iv_logo);
				holder.item_hall_iv_arrow = (ImageView) convertView.findViewById(R.id.item_hall_iv_arrow);
				holder.item_hall_awardDetail = (ImageView) convertView.findViewById(R.id.item_hall_awardDetail);
				holder.item_hall_tv_title = (TextView) convertView.findViewById(R.id.item_hall_tv_title);
				holder.item_hall_tv_kaijiang = (TextView) convertView.findViewById(R.id.item_hall_tv_kaijiang);
				holder.item_hall_tv_jiajiang = (TextView) convertView.findViewById(R.id.item_hall_tv_jiajiang);
				holder.item_hall_tv_desc = (TextView) convertView.findViewById(R.id.item_hall_tv_desc);
				holder.item_hall_tv_issue = (TextView) convertView.findViewById(R.id.item_hall_tv_issue);
				holder.item_hint = (TextView) convertView.findViewById(R.id.item_hint);
				holder.item_hint_tag = (TextView) convertView.findViewById(R.id.item_hint_tag);
				
				holder.item_hall_tv_kaijiang.getPaint().setFakeBoldText(true);
				holder.item_hall_tv_jiajiang.getPaint().setFakeBoldText(true);
				
				if(isArrowVisible){
					holder.item_hall_iv_arrow.setVisibility(View.VISIBLE);
				}else {
					holder.item_hall_iv_arrow.setVisibility(View.GONE);
				}
				
				convertView.setTag(holder);
			}else {////System.out.println("convert view reused");
				holder = (ViewHolder) convertView.getTag();
			}
			

			holder.item_hall_tv_desc.setText(Html.fromHtml(item.getDesc()));//desc
			holder.item_hall_iv_logo.setImageResource(item.getIconId());
			
			holder.item_hall_tv_kaijiang.setVisibility(View.GONE);
			holder.item_hall_tv_jiajiang.setVisibility(View.GONE);
			holder.item_hall_tv_issue.setVisibility(View.GONE);
			holder.item_hint_tag.setVisibility(View.GONE);
			if(CustomTagEnum.isAwardDetailItem(item)){
				show(holder.item_hall_awardDetail);
			}else {
				hide(holder.item_hall_awardDetail);
			}
			
			mTimerMap.put(holder.item_hall_tv_issue, item);//以当前的convertview 中的 改变期次信息的 textview 为key 值
			if(item.getEndTime() != null){
				holder.item_hall_tv_issue.setVisibility(View.VISIBLE);
				String lastTime = getLastTime(item.updateRemainedTime()/1000+"");//以毫秒为单位
				holder.item_hall_tv_issue.setText(StringUtils.replaceEach(issue_sample_text, new String[]{"ISSUE","TIME"}, new String[]{item.getIssue(),lastTime}));
			}
			
			if(210==item.getLotteryId()){//-------TODO 新增2014-12-12
				holder.item_hall_tv_title.setText("竞彩足球");//title
				hide(holder.item_hall_tv_issue);
			}else{
				holder.item_hall_tv_title.setText(item.getLotteryName());//title
			}

			
			if(1 == item.getIsAward()){//设置嘉奖图片
				holder.item_hall_tv_jiajiang.setVisibility(View.VISIBLE);
			}
			if(!Arrays.asList(getResources().getStringArray(R.array.codes_frequentry_lottery)).contains(item.getLotteryId()+"")){//彩种不是高频玩法
				String today_str = FormatUtil.dateFormat(System.currentTimeMillis());
				if(item.getEndTime() != null && item.getEndTime().startsWith(today_str)){//今天的日期的格式和开奖的日期格式相同,则显示今日开奖图片
					holder.item_hall_tv_kaijiang.setVisibility(View.VISIBLE);
				}
			}
			
			////System.out.println("s---------"+Cache_1358.getMSG_COUNT() );
			if(1009 == item.getId() && Cache_1358.getMSG_COUNT() >0){// 在线咨询显示 提示标识
//				holder.item_hint.setVisibility(View.VISIBLE);
				holder.item_hint_tag.setVisibility(View.VISIBLE);
				holder.item_hint_tag.setText(String.valueOf(Cache_1358.getMSG_COUNT()));
				////System.out.println("ccccccccccccholder.item_hint_tag.setText ---------->"+String.valueOf(Cache_1358.getMSG_COUNT()));
			}

			onItemClicked(position,convertView,item);
			onItemLongClicked(position, convertView,item);
			setExtras(position,convertView,holder,item);
			return convertView;
		}
	}
	

	/**
	 * 设置 item的点击事件
	 * @param position
	 * @param convertView
	 * @param item 从mList 中拿到的 MessageBean 的item
	 */
	protected void onItemClicked(final int position, View convertView,final CustomTagEnum item) {
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				CustomTagEnum.startActivity(mActivity, null, item);
				
			}
		});
	}
	
	/**
	 * 给 getView 方法设置其他的属性和功能
	 */
	protected void setExtras(final int position, View convertView,ViewHolder holder,final CustomTagEnum item) {
		
	}

	/**
	 * 设置长点击事件
	 * @param position
	 * @param convertView
	 * @param item 从mList 中拿到的 MessageBean 的item
	 */
	protected void onItemLongClicked(final int position, View convertView,final CustomTagEnum item){
		convertView.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View arg0) {
				final TCDialogs dialogs = new TCDialogs(mActivity);
				dialogs.popHallDialog(item.getLotteryName(), new View.OnClickListener() {//置顶
					@Override
					public void onClick(View v) {
						if(position != 0){
							items.remove(item);
							items.add(0,item);
							saveTagIds(items,R.string.lottery_ids);
							savelotteryIdsSort(SPUtil.getString(R.string.lottery_ids));
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
						saveTagIds(items,R.string.lottery_ids);
						savelotteryIdsSort(SPUtil.getString(R.string.lottery_ids));
						mAdapter.notifyDataSetChanged();
						}
						dialogs.dismiss();
					}
				}, new View.OnClickListener() {//下移
					@Override
					public void onClick(View v) {
						if(position != items.size()-1){
							items.remove(item);
							items.add(position+1,item);
							saveTagIds(items,R.string.lottery_ids);
							savelotteryIdsSort(SPUtil.getString(R.string.lottery_ids));
							mAdapter.notifyDataSetChanged();
							}
						dialogs.dismiss();
					}
				},new View.OnClickListener() {//收录到我的定制
					@Override
					public void onClick(View v) {
						BodyFragment1.addTag(mActivity,item.getId());
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

	class ViewHolder{
		ImageView item_hall_iv_logo;
		ImageView item_hall_iv_arrow;
		ImageView item_hall_awardDetail;
		TextView item_hall_tv_title;
		TextView item_hall_tv_kaijiang;
		TextView item_hall_tv_jiajiang;
		TextView item_hall_tv_desc;
		TextView item_hall_tv_issue;
		TextView item_hint;
		TextView item_hint_tag;
	}
	
	@Override
	protected void onRightIconClicked() {
		BodyFragment1.addTag(mActivity,CustomTagEnum.hall.getId());
		setTitleNav(CustomTagEnum.hall.getId(), getString(R.string.title_nav_hall),0,R.drawable.title_nav_menu);
	}

	@Override
	public void onPause() {
		super.onPause();
		CustomTagEnum.stopTimer();
	}
	
	/**
	 * 创建桌面快捷图标
	 * @param tag
	 */
	public void createShortCut(CustomTagEnum tag){
		Intent homeIntent = new Intent(mActivity,TCActivity.class);
		Intent intent = new Intent();
		homeIntent.putExtra("window_id", String.valueOf(tag.getId()));
		intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
//		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, tag.getIconId());
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(MyApp.context,tag.getIconId()));
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, tag.getLotteryName());
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, homeIntent);
		intent.putExtra("duplicate", false);
		mActivity.sendBroadcast(intent);
	}
	
	/**
	 * 从 界面的 CustomTagEnum 的集合中取出 界面id 拼接成字符串 存到sharedpreference 中
	 * @param items
	 */
	public static void saveTagIds(List<CustomTagEnum> items,int keyId){
		if(items == null || items.size()<1){
			SPUtil.putString(keyId, "");
			return;
		}
		String tagIds = "";
		for (CustomTagEnum item : items) {
			tagIds += ","+item.getId();
		}
		System.out.println("id---new-------->"+tagIds);
		SPUtil.putString(keyId, tagIds.substring(1));
	}

	@Override
	public void onNetworkRefresh() {
		super.onNetworkRefresh();
		requestlotteryIds();
	}
	/**
	 * 从 sharedpreference 中取出 界面id 拼接的字符串, 恢复到 customTagEnum 集合中, 并且返回该集合
	 * @return
	 */
	public static List<CustomTagEnum> restoreTags(int keyId){
		String tagsStr = SPUtil.getString(keyId);////System.out.println("tagsStr======"+tagsStr);
		if(AppUtil.isEmpty(tagsStr))
			return null;
		List<CustomTagEnum> tags = new ArrayList<CustomTagEnum>();
		String[] tagsArr = tagsStr.split(",");
		for (String string : tagsArr) {
			if(!AppUtil.isNumeric(string) || CustomTagEnum.getItemById(Integer.parseInt(string)) == null)
				continue;
			tags.add(CustomTagEnum.getItemById(Integer.parseInt(string)));
		}
		return tags;
	}
	
}
