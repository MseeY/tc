package com.mitenotc.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.download.util.StringUtils;
import com.mitenotc.bean.MessageBean;
import com.mitenotc.engine.EngineProxy;
import com.mitenotc.enums.CustomTagEnum;
import com.mitenotc.exception.MessageException;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.tc.ThirdActivity;
import com.mitenotc.test.FakeDatas;
import com.mitenotc.ui.BodyFragment2.ViewHolder;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.ui.lotteryinfo.AwardInfoDetail;
import com.mitenotc.ui.lotteryinfo.JCAwardInfoDetail;
import com.mitenotc.ui.lotteryinfo.JCAwardInfoDetail.JCAwardInfoAdapter;
import com.mitenotc.ui.ui_utils.AwardDetailCust;
import com.mitenotc.ui.ui_utils.MyToast;
import com.mitenotc.ui.ui_utils.XListView.IXListViewListener;
import com.mitenotc.utils.AppUtil;
import com.mitenotc.utils.FormatUtil;
import com.mitenotc.utils.SPUtil;

/**
 * award info,开奖信息
 * 界面有点卡,有待解决
 * @author mitenotc
 */ 
public class BodyFragment3 extends BaseFragment{
//	@Override
//	protected BaseAdapter initAdapter() {
//		return new AwardInfoAdapter();
//	}
//	/**
//	 * 子线程中获取数据,主要这里不可以进行有关界面的操作, MessageException 异常可以直接往上抛出 
//	 * @return
//	 * @throws MessageException
//	 */
//	@Override
//	protected MessageBean getMessageBean() throws MessageException {
//		MessageBean bean = EngineProxy.proxy.getCMD_1201(null, null, "1", "100");
//		
//		mList = bean.getLIST(); 
////		mList = FakeDatas.getAwardInfos();
//		return new MessageBean();
//	} 
	private TypedArray dice_array;
	private Dialog mDialog;
	private MessageBean jcBean;
	protected boolean isaddjc=true;
	protected int[] poker_drawables = {R.drawable.award_info_bg_pk1,R.drawable.award_info_bg_pk2,R.drawable.award_info_bg_pk3,R.drawable.award_info_bg_pk4};//扑克的花色的图片
	protected String[] poker_val = {"A","2","3","4","5","6","7","8","9","10","J","Q","K"};//扑克的值
	protected List<MessageBean> jcList = new ArrayList<MessageBean>(); 
	@Override
	protected void initData() {
	};
	public void sendRequest(int key){
		MessageJson jsonMsg = new MessageJson();
		jsonMsg.put("C", "1");
		jsonMsg.put("D", "100");
		if(key ==1){
			jsonMsg.put("isforce", "1");
		}
		submitData(key, 1201, jsonMsg);
		
	}
	@Override
	protected void onMessageReceived(Message msg) {
		if(mDialog!=null && mDialog.isShowing()){
			mDialog.dismiss();
		}
		mListView.setRefreshTime(FormatUtil.timeFormat(System.currentTimeMillis()));
		mListView.stopRefresh();
		MessageBean messageBean = (MessageBean) msg.obj;
		List<MessageBean> listTemp=null;
		String 	lottery_ids = SPUtil.getString(R.string.lottery_ids);// 购彩大厅中有的彩种才显示开奖信息
		String[] split = lottery_ids.split(",");
		listTemp = messageBean.getLIST();
		switch (msg.arg1) {
			case 0:
			case 1:
				mList.clear();
				for (MessageBean bean : listTemp) {
					for (int i = 0; i < split.length; i++) {
						if(CustomTagEnum.getItemByLotteryId(Integer.parseInt(bean.getA())) != null && split[i].equals(bean.getA()) ){
							mList.add(bean);
						}
					}
				}
				//竞彩只是单独固定添加一个条目 -----TODO 等待后期的增加
				if(isaddjc){
					jcBean=new MessageBean();
					jcBean.setD(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
					jcBean.setE("");
					jcBean.setG("0");
					jcBean.setH("0");
					jcBean.setA("209");
					jcBean.setB("竞彩足球");
					mList.add(jcBean);
					
//					2015-3-10 等待加载竞彩篮球的html5的开奖地址
//					jcBean=new MessageBean();
//					jcBean.setD(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
//					jcBean.setE("");
//					jcBean.setG("0");
//					jcBean.setH("0");
//					jcBean.setA("219");
//					jcBean.setB("竞彩篮球");
//					mList.add(jcBean);
				}else if (-1!=mList.indexOf(jcBean)){
					mList.remove(jcBean);
				}
				break;
		}
		if(mList.size() > 0){
			mActivity.hideNetworkRefresh();//关闭网络异常按钮baseFragment
		}
		if(mAdapter==null){
			mAdapter = new AwardInfoAdapter();
			mListView.setAdapter(mAdapter);
		}else{
			mAdapter.notifyDataSetChanged();
		}
		
//		if(msg.arg1 == 0 || msg.arg1 ==1){
//			List<MessageBean> listTemp = messageBean.getLIST();
//			mList = new ArrayList<MessageBean>();//清除不存在的彩种
//		   String 	lottery_ids = SPUtil.getString(R.string.lottery_ids);// 购彩大厅中有的彩种才显示开奖信息
//		   String[] split = lottery_ids.split(",");
//			for (MessageBean bean : listTemp) {
//				for (int i = 0; i < split.length; i++) {
//					if(CustomTagEnum.getItemByLotteryId(Integer.parseInt(bean.getA())) != null && split[i].equals(bean.getA()) ){
//						mList.add(bean);
//					}
//				}
//			}
//			mAdapter = new AwardInfoAdapter();
//			mListView.setAdapter(mAdapter);
//		}
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		network_notice_required = true;
		setContentView(R.layout.f_bodyfragment3_layout);
		setTitleNav(CustomTagEnum.awardInfo.getId(),R.string.title_nav_award, R.drawable.title_nav_back, R.drawable.title_nav_menu);
		LinearLayout ll = (LinearLayout) findViewById(R.id.bodyfragment3_ll);
		mListView.setDividerHeight(0);
		/**特别注释  android:versionCode="16" android:versionName="2.1.1" 开始增加 因为布局中添加了新的控件 所以在addView 其它时候必须清空处理**/
		ll.removeAllViews();
		ll.addView(mListView);
		mListView.setAdapter(new AwardInfoAdapter());
		//从网络端获取数据
		mListView.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {
				onXListViewRefresh();
			}
			@Override
			public void onLoadMore() {	
				onXListViewLoad();
			}
		});
		mDialog=new Dialog(mActivity,R.style.dialog_theme);
		mDialog.setContentView(R.layout.m_wait_dialog);
		mDialog.show();
		sendRequest(0);
	}
	
	@Override
		public void onDestroy() {
			super.onDestroy();
			if(dice_array!=null){
				dice_array.recycle();
			}
		}

	/**
	 * xlistview 上拉加载
	 */
	protected void onXListViewLoad() {
	}
	/**
	 * xlistview 下拉刷新
	 */
	protected void onXListViewRefresh() {
		sendRequest(1);//强制刷新
	}
	@Override
	public void onNetworkRefresh() {
		sendRequest(1);
	}
		public class AwardInfoAdapter extends BaseListAdapter{
			@SuppressLint({ "ResourceAsColor", "SimpleDateFormat" })
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				MessageBean item = mList.get(position);
				ViewHolder holder = null;
				if(convertView!=null){
					holder = (ViewHolder) convertView.getTag();
					holder.award_info_content.removeAllViews();
//					holder.award_info_content.setBackgroundResource(R.drawable.award_info_item_content_dice_bg);
				}else {
					convertView = View.inflate(mActivity, R.layout.f_item_award_info_ll, null);
					holder = new ViewHolder();
					holder.award_info_tv_title = (TextView) convertView.findViewById(R.id.tv1);
					holder.tv1 = (TextView) convertView.findViewById(R.id.tv2);
					holder.tv2 = (TextView) convertView.findViewById(R.id.tv3);
//					holder.tv2 = (TextView) convertView.findViewById(R.id.award_info_tv_title);
					holder.award_info_tv_issue = (TextView) convertView.findViewById(R.id.award_info_tv_issue);
					holder.award_info_tv_time = (TextView) convertView.findViewById(R.id.award_info_tv_time);
					holder.award_info_content = (LinearLayout) convertView.findViewById(R.id.award_info_content);
					holder.iv_arrow = (ImageView) convertView.findViewById(R.id.award_info_iv_arrow);
					holder.award_info_detail_contianer = (LinearLayout) convertView.findViewById(R.id.award_info_detail_contianer);
					AwardDetailCust custView = new AwardDetailCust(mActivity);
					holder.award_info_detail_contianer.addView(custView);
					holder.award_info_detail_contianer.setTag(custView);
					
//					holder.award_info_tv_title.getPaint().setFakeBoldText(true);//设置中文加粗
					convertView.setTag(holder);
				}
				
				holder.tv1.setVisibility(View.VISIBLE);
				holder.tv2.setVisibility(View.VISIBLE);
				holder.award_info_tv_issue.setText("");
				holder.award_info_tv_time.setText("");
				holder.award_info_tv_title.setText("");
				
				String[] codes_ball_lottery = getResources().getStringArray(R.array.codes_ball_lottery);
				String[] codes_dice_lottery = getResources().getStringArray(R.array.codes_dice_lottery);
				String[] codes_shengfu_lottery = getResources().getStringArray(R.array.codes_shengfu_lottery);//足球彩种
				String[] codes_poker_lottery = getResources().getStringArray(R.array.codes_poker_lottery);//扑克类彩票
				String[] codes_jczq = getResources().getStringArray(R.array.jczq_lottery);//竞彩足球
	        		
	        		if("模拟十一运夺金".equals(item.getB())){
	        			holder.award_info_tv_title.setText("模拟11运夺金");//玩法彩种
	        		}else{
	        			holder.award_info_tv_title.setText(item.getB());
	        		}
					holder.award_info_tv_issue.setText(item.getC());//玩法期号
					String time = item.getD();
					time = time.split(" ")[0].replace("-", ".");
					holder.award_info_tv_time.setText(time);
					
					String[] normalNums = item.getE().split("\\+")[0].split(",");
					String[] specNums = null;
					if(item.getE().split("\\+").length>1){
						specNums = item.getE().split("\\+")[1].split(",");
					}
					
				if(Arrays.asList(codes_ball_lottery).contains(item.getA())){//球类彩票
					//协议改了之后蓝球需要根据不同的球进行各自处理
//					holder.award_info_content.setBackgroundResource(R.drawable.award_info_item_content_ball_bg);//换背景
					addBall_bg(holder, normalNums,R.drawable.shape_ball_red,item.getA());//添加红球
					addBall_bg(holder, specNums,R.drawable.shape_ball_blue,item.getA());//添加篮球
				}else if(Arrays.asList(codes_dice_lottery).contains(item.getA())){//筛子类彩票
//					////System.out.println("normalNums.length = "+normalNums.length);
					for (int i = 0; i < normalNums.length; i++) {
						View view = View.inflate(mActivity, R.layout.f_award_info_item_dice, null);
						ImageView iv = (ImageView) view.findViewById(R.id.award_info_dice_iv);
						dice_array = getResources().obtainTypedArray(R.array.dice_drawables);
						Drawable drawable = dice_array.getDrawable(Integer.parseInt(normalNums[i])-1);//获取筛子图片
						iv.setImageDrawable(drawable);
						holder.award_info_content.addView(view);
					}
				}else if(Arrays.asList(codes_shengfu_lottery).contains(item.getA())){//足球类彩票
						View view = View.inflate(mActivity, R.layout.f_award_info_item_shengfu_bg, null);
						LinearLayout ll_bg = (LinearLayout) view.findViewById(R.id.award_info_shengfu_bg);
						holder.award_info_content.addView(view);
						for (int i = 0; i < normalNums.length; i++) {
							View shengfu_item = view.inflate(mActivity, R.layout.f_award_info_item_shengfu, null);
							TextView tv_shengfu = (TextView) shengfu_item.findViewById(R.id.award_info_shengfu_tv);
							tv_shengfu.setText(normalNums[i]);
							ll_bg.addView(shengfu_item);
						}
			    }else if (Arrays.asList(codes_poker_lottery).contains(item.getA())) {//扑克类彩票
						
						for (int i = 0; i < normalNums.length; i++) {
							if(AppUtil.isNumeric(normalNums[i])){
								int num = Integer.parseInt(normalNums[i]);
								int color = num/100;//花色 1, 2, 3, 4
								int val = num%100;//扑克的值, A, 2, 3, 4, 5, 6, 7, 8, 9, 10, J, Q, K;
								View view = View.inflate(mActivity, R.layout.f_award_info_item_poker, null);
								LinearLayout award_info_item_pk_ll_bg = (LinearLayout) view.findViewById(R.id.award_info_item_pk_ll_bg);
								award_info_item_pk_ll_bg.setBackgroundResource(poker_drawables[color - 1]);
								
								TextView award_info_item_pk_tv = (TextView) view.findViewById(R.id.award_info_item_pk_tv);
								award_info_item_pk_tv.setText(poker_val[val - 1]);
								holder.award_info_content.addView(view);
							}
						}
				}else if(Arrays.asList(codes_jczq).contains(item.getA())){//竞彩玩法代码
					System.out.println("b3--------is zq--"+item.getA());
					View view = View.inflate(mActivity, R.layout.f_award_info_item_zq, null);
//					String k="",m="",t1="",g="",o="",I="",h="",w="";
//					h=item.getH();//H	赛事名称
//					g=item.getG();//G	对阵编号
//					k=item.getK();//主
//					m=item.getM();//客
//					I=item.getI();//I	每场比赛开始时间
//					w=item.getW();// W	彩果 T	赛果
//					o=item.getO();//O	总比分
//					//字符长度处理
//					if(!StringUtils.isBlank(h)&& h.length() > 4){
//						h=h.substring(0, 4);
//					}
//					if(!StringUtils.isBlank(k)&& k.length() > 4){
//						k=k.substring(0, 4);
//					}
//					if(StringUtils.isBlank(m) && m.length() > 4){
//						m=m.substring(0, 4);
//					}
//					if(!StringUtils.isBlank(k)){
//						((TextView) view.findViewById(R.id.tv3)).setText(k);//K	主队名称
//					}
//					if(!StringUtils.isBlank(item.getO())){
//						((TextView) view.findViewById(R.id.tv1)).setText(o);//vs O	总比分
//					}else{
//						((TextView) view.findViewById(R.id.tv1)).setText("-:-");//vs O	总比分
//					}
//					if(!StringUtils.isBlank(item.getM())){
//						((TextView) view.findViewById(R.id.tv0)).setText(m);//M	客队名称
//					}
//					((TextView) view.findViewById(R.id.msg)).setText(getVS(w));//R	赛果
//					if(!StringUtils.isBlank(item.getJ())){
//						holder.award_info_tv_time.setText(item.getJ().split(" ")[0].replace("-", "."));
//					}
					
					holder.award_info_content.addView(view);
//					holder.award_info_content.setBackgroundColor(MyApp.res.getColor(R.color.white));
					holder.award_info_tv_title.setText("竞彩足球");//玩法彩种
					holder.award_info_tv_issue.setText("");//玩法期号
					holder.tv1.setVisibility(View.GONE);
					holder.tv2.setVisibility(View.GONE);
					String s=new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
					if(!StringUtils.isBlank(s)){
						holder.award_info_tv_time.setText(s.split(" ")[0].replace("-", "."));
					}
				}
					onItemClicked(position,convertView,item,holder);
//				setExtras(holder);
				return convertView;
			}
            /**
             * 根据彩果获得文字描述
             * @param w
             * @return
             */
			private String getVS(String w) {
				if(StringUtils.isBlank(w))return "(--)";
				switch(Integer.parseInt(w)){
				case 3:
					w="(主胜)";
					break;
				case 1:
					w="(平)";
					break;
				case 0:
					w="(主负)";
					break;
				}
				return w;
			}

			private void addBall_bg(ViewHolder holder, String[] ballNums,int resId, String lotteryId) {
				if(ballNums!=null){
					for (int j = 0; j < ballNums.length; j++) {
						View view = View.inflate(mActivity, R.layout.f_award_info_item_ball, null);
						TextView tv_ball = (TextView) view.findViewById(R.id.award_info_item_ball_tv_bg);
						if(!AppUtil.isNumeric(ballNums[j])){
							return;
						}
						String numStr = ballNums[j];
						//排列三,排列五,七星彩 不需要格式
						if("100".equals(lotteryId) || "102".equals(lotteryId) || "103".equals(lotteryId) || "119".equals(lotteryId)){
						}else {
							numStr = FormatUtil.ballNumberFormat(ballNums[j]);
						}
						tv_ball.setText(numStr);
						tv_ball.setBackgroundResource(resId);
						holder.award_info_content.addView(view);
					}
				}
			}
		}
		/**
		 * 设置 item的点击事件
		 * @param position
		 * @param convertView
		 * @param item 从mList 中拿到的 MessageBean 的item
		 */
		protected void onItemClicked(final int position, View convertView,final MessageBean item,ViewHolder holder) {
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
//					Toast.makeText(mActivity, "跳转到 "+item.getB(), 0).show();
					Bundle myBundle = new Bundle();
					myBundle.clear();
					if(Arrays.asList(getResources().getStringArray(R.array.jczq_playid)).contains(item.getA())){//足球
//						1 加载服务端 html5 页面
						Bundle	bundle=new Bundle();
						bundle.putString("title", "竞彩足球-开奖结果");
						bundle.putString("url", "http://m.mitenotc.com/jczq/kj/");//测试环境  http://bug.mitenotc.com/jczq/kj/
						start(ThirdActivity.class,TCWebFragment.class,bundle);
						//2 废弃
//						start(JCAwardInfoDetail.class,myBundle);
						MyToast.showToast(mActivity, "加载竞彩足球开奖中请等待....");
					}else if(Arrays.asList(MyApp.res.getStringArray(R.array.jclq_playid)).contains(item.getA())){//篮球
//						2 加载服务端 html5 页面
//						Bundle	bundle=new Bundle();
//						bundle.putString("title", "竞彩篮球-开奖结果");
//						bundle.putString("url", "http://bug.mitenotc.com/jczq/kj/");
//						start(ThirdActivity.class,TCWebFragment.class,bundle);
						MyToast.showToast(mActivity, "加载竞彩篮球开奖中请等待....          等待地址中.....!");
					}else{
						myBundle.putString("lotteryId", item.getA());
						myBundle.putString("lotteryName", item.getB());
						myBundle.putString("issue", item.getC());
						start(AwardInfoDetail.class,myBundle);
					}
				}
			});
		}
		
		protected void setExtras(ViewHolder holder) {
			holder.award_info_detail_contianer.setVisibility(View.GONE);
		}
		/**
		 * 判断是否为足彩
		 * @param s 为null或"" 返回为false 反之返回具体的判断结果
		 * @return
		 */
		public boolean isZQ(String s){
			boolean b=false;
			if(StringUtils.isBlank(s)) return b;
			String[] jczq_split=MyApp.res.getStringArray(R.array.jczq_lottery);
			for (int i = 0; i < jczq_split.length; i++) {
				if(jczq_split[i].equals(s)){
					b=true;
				}
			}
			return b;
		}
	protected class ViewHolder{
		public TextView award_info_tv_title;
		TextView award_info_tv_issue;
		TextView tv1;//"第"
		TextView tv2;//"期"
		TextView award_info_tv_time;
		LinearLayout award_info_content;
		public LinearLayout award_info_detail_contianer;// = (LinearLayout) convertView.findViewById(R.id.award_info_detail_contianer);
		public ImageView iv_arrow;// = (ImageView) convertView.findViewById(R.id.award_info_iv_arrow);
	}
	
	@Override
	protected void onRightIconClicked() {
		BodyFragment1.addTag(mActivity,CustomTagEnum.awardInfo.getId());
		setTitleNav(CustomTagEnum.awardInfo.getId(),R.string.title_nav_award, 0, R.drawable.title_nav_menu);
	}
}



/*holder.award_info_ball_content.setVisibility(View.VISIBLE);
for (int i = 0; i < normalNums.length; i++) {
	View view = View.inflate(mActivity, R.layout.f_award_info_item_ball, null);
	TextView tv = (TextView) view.findViewById(R.id.award_info_item_ball_tv_bg);
	if(i == normalNums.length-1 && specNums == null){
		tv.setVisibility(View.GONE);
		tv = (TextView) view.findViewById(R.id.award_info_item_ball_tv_bg);
		tv.setVisibility(View.VISIBLE);
	}
	tv.setBackgroundResource(R.drawable.award_info_item_ball_red);
	tv.setText(FormatUtil.ballNumberFormat(normalNums[i]));
	holder.award_info_ball_content.addView(view);
}

if(specNums != null){
	for (int i = 0; i < specNums.length; i++) {
		View view = View.inflate(mActivity, R.layout.f_award_info_item_ball, null);
		TextView tv = (TextView) view.findViewById(R.id.award_info_item_ball_tv_bg);
		if(i == specNums.length-1){
			tv.setVisibility(View.GONE);
			tv = (TextView) view.findViewById(R.id.award_info_item_ball_tv_bg);
			tv.setVisibility(View.VISIBLE);
		}
		tv.setBackgroundResource(R.drawable.award_info_item_ball_blue);
		tv.setText(FormatUtil.ballNumberFormat(normalNums[i]));
		holder.award_info_ball_content.addView(view);
	}
}*/

/*					if("106".equals(item.getA())){//大乐透
specNums = new String[2];
specNums[0] = normalNums[5];
specNums[1] = normalNums[6];
String[] normalNums_copy = normalNums;
normalNums = new String[5];
for (int i = 0; i < 5; i++) {
	normalNums[i] = normalNums_copy[i];
}
}else if("118".equals(item.getA())) {//双色球
specNums = new String[1];
specNums[0] = normalNums[6];
String[] normalNums_copy = normalNums;
normalNums = new String[6];
for (int i = 0; i < 6; i++) {
	normalNums[i] = normalNums_copy[i];
}
}
*/			