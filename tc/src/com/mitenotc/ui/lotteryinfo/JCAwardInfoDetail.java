package com.mitenotc.ui.lotteryinfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioGroup.OnCheckedChangeListener;

import org.apache.commons.lang3.StringUtils;
import com.mitenotc.bean.MessageBean;
import com.mitenotc.enums.CustomTagEnum;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.ui.BodyFragment1;
import com.mitenotc.ui.BodyFragment3;
import com.mitenotc.ui.ui_utils.GridRadioGroup;
import com.mitenotc.ui.ui_utils.MyToast;
import com.mitenotc.ui.ui_utils.XListView.IXListViewListener;
import com.mitenotc.utils.FormatUtil;
/**
 * 此处只处理竞彩足球的开奖信息
 * @author mitenotc
 *
 */
public class JCAwardInfoDetail extends BodyFragment3{
//	protected JCListView jc_Lottery_Lv;
//	protected JCListAdapter mjcAdapter;
//  protected List<List<MessageBean>> groupList=new ArrayList<List<MessageBean>>();
	private String lotteryId="218";//默认是
	private String issue="";//默认是昨天
	
	private  PopupWindow pop_lotteryType;
	private  GridRadioGroup saleType_grg;
	private long last_pop_time;//用于记录popwindow最后一次dismiss的时间
	private Handler jcaHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {//把 原来在 GridRadioGroup 的onCheckedChanged 中的操作放到handler中,主要考虑小米系统的兼容
			if(pop_lotteryType.isShowing())
				pop_lotteryType.dismiss();
			resetTitleState();
			RadioGroup group = (RadioGroup) msg.obj;
			int checkedId = msg.arg1;
			if(saleType_grg.getCurrentCheckedId() == checkedId){
				////System.out.println("curentId not changed");
				return;
			}
			 saleType_grg.setCurrentCheckedId(checkedId);
			RadioButton rb = (RadioButton) group.findViewById(checkedId);
			if(rb.getTag() == null)//安全性考虑,rb不会是空的,同时 tag也不能为空
				return;
			setTitleText("竞彩-"+rb.getText().toString());//改变不同的销售方式的时候更改头部导航的标题文字
			String tag = rb.getTag().toString();
			System.out.println("125------------->tag"+tag);
			if(tag.contains(",")){
				String[] tags = tag.split(",");
				lotteryId = tags[0];
			}else if (!StringUtils.isBlank(tag)){
				lotteryId=tag;
			}
			issue=getS();
			sendRequest(lotteryId, issue);
		}
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitleNav("竞彩-胜平负",R.drawable.title_nav_back,R.drawable.icon_date);
		mListView.setDivider(new ColorDrawable(getResources().getColor(R.color.view_line1)));//#cecece
		mListView.setDividerHeight(1);
		mListView.setPullLoadEnable(true);
		mListView.removemFooterView();//移出页脚
		mListView.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {// 下拉加载
				 issue=getS();//测试
				 sendRequest(lotteryId,issue);;//强制刷新
			}
			@Override
			public void onLoadMore() {// 上拉加载	
			}
		});
		initLotteryType();
		resetTitleState();
		setTitleCheckedListener();
//		issue=getS();
//		sendRequest(lotteryId,issue);
		jcaHandler.sendEmptyMessage(1);
	}
	/**
	 * 竞彩按照具体玩法显示开奖信息
	 */
	private void setTitleCheckedListener() {
		setTitleCheckedChangedListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					if(System.currentTimeMillis() - last_pop_time > 300)
						pop_lotteryType.showAsDropDown(buttonView, 0, 0);
					else
						resetTitleState();
				}else {
					if(pop_lotteryType.isShowing())
						pop_lotteryType.dismiss();
				}
			}
		});
		
	}
	/**
	 * 初始化 popwindow 和 popwindow 的contentview
	 */
	private void initLotteryType() {
		View cview = View.inflate(mActivity, R.layout.f_zq_pop_titile_rb, null);
		cview.findViewById(R.id.arrange3_rbtn2).setVisibility(View.GONE);//关闭混关按钮
		if(cview != null){
			saleType_grg  = (GridRadioGroup) cview.findViewById(R.id.saleType_grg);
			pop_lotteryType = new PopupWindow(cview, -1, -2);
			pop_lotteryType.setBackgroundDrawable(new BitmapDrawable());
			pop_lotteryType.setOutsideTouchable(true);
			pop_lotteryType.setOnDismissListener(new OnDismissListener() {//在pop dismiss的时候把标题的checked状态改回到false
				@Override
				public void onDismiss() {
					last_pop_time = System.currentTimeMillis();
					resetTitleState();
				}
			});
			saleType_grg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					Message msg = Message.obtain();
					msg.arg1 = checkedId;
					msg.obj = group;
					jcaHandler.sendMessage(msg);//上面的3行代码 由于考虑到系统 原因,放到handler中执行. 如果没有放在handler中,小米手机会出现异常
				}
			});
		}
	}
	@Override
	protected void onRightIconClicked() {//定制按钮  在此处不再是响应定制事件  而是修改为时间选择器
		System.out.println("125----------选择时间>");
//		setTitleNav(CustomTagEnum.awardInfo.getId(),R.string.title_nav_award, 0, R.drawable.title_nav_menu);
//		BodyFragment1.addTag(mActivity,CustomTagEnum.awardInfo.getId());
	
	}
	@Override
	protected void onRightIconClicked2() {
		System.out.println("125-------onRightIconClicked2");
//		setTitleNav(CustomTagEnum.award_detail_zc.getLotteryName(), R.drawable.title_nav_back, R.drawable.icon_date);//
	}
	/**
	 * 竞彩请求开奖信息
	 * @param lotteryId
	 * @param issue
	 */
	public void sendRequest(String lotteryId,String issue){
		if(StringUtils.isBlank(lotteryId) && StringUtils.isBlank(issue))
		    return;
		System.out.println("125----> 开奖 : lotteryId "+lotteryId+" issue "+issue);
		MessageJson mB=new MessageJson();
//		A	a:可以是一个玩法（若是混关玩法，则需要对阵编号）
//		    b:也可以是多个玩法拼的字符串（不含混关）
//		    c:还可以为空（不含混关），查询所有对阵的开奖信息
		mB.put("A", lotteryId);
		mB.put("G", issue);//G	批次时间
		mB.put("K", "1");//K	查询类型 1：竞彩足球 2：竞彩篮球
		submitData(4004, 4004, mB);
	}
	//获取当前时间的批次时间 例如 141202
	@SuppressLint("SimpleDateFormat")
	public static String getS(){
		String s="";
		 Date date = new Date();  
		try {
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");  
			 String nowDate = sf.format(date); //当前时间
				Calendar cal = Calendar.getInstance();
				//注：在使用set方法之前，必须先clear一下，否则很多信息会继承自系统当前时间  
				cal.clear();
				cal.setTime(sf.parse(nowDate));  
				cal.add(Calendar.DAY_OF_YEAR, -1);
				s= sf.format(cal.getTime());
				s=s.substring(s.length()-6, s.length());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return s;  
	}
    @Override
	protected void onMessageReceived(Message msg) {
		mListView.setRefreshTime(FormatUtil.timeFormat(System.currentTimeMillis()));
		mListView.stopRefresh();
		MessageBean messageBean = (MessageBean) msg.obj;
		List<MessageBean> listTemp=null;
		switch (msg.arg1) {
			case 4004:
				if(messageBean.getA().equals("0")){
					listTemp = messageBean.getLIST();
					if(listTemp!=null && listTemp.size() > 0){
						jcList.clear();
						for (MessageBean messageB : listTemp) {
							jcList.add(messageB);//购彩大厅中有的彩种才显示
							mAdapter = new JCAwardInfoAdapter();
							mListView.setAdapter(mAdapter);
						}
					}
				}else{
					MyToast.showToast(mActivity, messageBean.getB());//Toast B 错误描述信息
				}
				break;
		  }
		System.out.println("125---->"+mList.size()+" mList "+mList.toString());
		if(mList.size() > 0){
			mActivity.hideNetworkRefresh();//关闭网络异常按钮baseFragment
		}
	
	}
//    private String[] codes_jczq = getResources().getStringArray(R.array.jczq_lottery);//竞彩足球
	public class JCAwardInfoAdapter extends BaseListAdapter{
		@Override
		public int getCount() {////System.out.println("mlist = "+mList);
			if(jcList == null){
				return 0;
			}
			return jcList.size();
		}
		@Override
		public Object getItem(int position) {
			return jcList.get(position);
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MessageBean item = jcList.get(position);
			ViewHolder holder = null;
			String k="",m="",t1="",g="",o="",I="",h="",w="";
			if(convertView==null){
				holder = new ViewHolder();
				convertView = View.inflate(mActivity, R.layout.f_item_award_jc_info, null);
				holder.tv_msg=(TextView) convertView.findViewById(R.id.tv_msg);
				holder.zhu=(TextView) convertView.findViewById(R.id.zhu);
				holder.vs=(TextView) convertView.findViewById(R.id.vs);
				holder.ke=(TextView) convertView.findViewById(R.id.ke);
				convertView.setTag(holder);
			}
			holder=(ViewHolder) convertView.getTag();
//			{"D":"4","E":"2014-12-04 09:00","F":"2014-12-04 22:40","G":"002","A":"210","B":"20000","C":"20141204","L":"  -1.0",
//				"M":"摩德纳","N":"0","O":"","H":"意大利杯","T1":"","I":"2014-12-05 01:00","J":"2014-12-04 22:40","K":"卡利亚里","U":""
//				,"W":"","V":"","Q":"","P":"","S":"","R":"","X":""},
			//H	赛事名称+G	对阵编号+I	每场比赛开始时间
			h=item.getH();//H	赛事名称
			g=item.getG();//G	对阵编号
			k=item.getK();//主
			m=item.getM();//客
			I=item.getI();//I	每场比赛开始时间
			w=item.getW();// W	彩果 T	赛果
			o=item.getO();//O	总比分
			//字符长度处理
			if(h.length() > 4){
				h=h.substring(0, 4);
			}
			if(k.length() > 4){
				k=k.substring(0, 4);
			}
			if(m.length() > 4){
				m=m.substring(0, 4);
			}
			holder.tv_msg.setText(Html.fromHtml(formatText(0,"",h, g, I)));
			holder.vs.setText(Html.fromHtml(formatText(1,w,w, o,"")));
			
		    if(!StringUtils.isBlank(k)){
				holder.zhu.setText(k);//K	主队名称
			}
			if(!StringUtils.isBlank(m)){
				holder.ke.setText(m);//M	客队名称
			}
			return convertView;
		}
		
	 class ViewHolder {
		 TextView tv_msg;
		 TextView zhu;
		 TextView vs;
		 TextView ke;
	 }
	}
	/**
	 * 格式化text效果
	 * @param style 
	 * 0:<![CDATA[H<br>G</br>&#160;<small><br>I</br></small>]]>
	 * 1:
	 *       胜<string name="jc_awardinfo_vs3"><![CDATA[<font color="#ff0000">SPF<br>SP</br></font>]]></string>
		         平<string name="jc_awardinfo_vs1"><![CDATA[<font color="#007AFF">SPF<br>SP</br></font>]]></string>
		         负<string name="jc_awardinfo_vs0"><![CDATA[<font color="#ff6e940e">SPF<br>SP</br></font>]]></string>
	 * 2:
	 * @param sfp  3,1 0(彩果胜平负)
	 * @param ts1
	 * @param ts2
	 * @param ts2
	 * @return
	 */
	public static  String formatText(int style,String sfp,String ts0,String ts1,String ts2){
		String s="";
		if(StringUtils.isBlank(ts0)){ts0="--";}
		if(StringUtils.isBlank(ts1)){ts1="--";}
		if(StringUtils.isBlank(ts2)){
			ts2="--";
		}else if(ts2.contains(" ")){
			ts2=ts2.split(" ")[1];
		}
		switch (style) {
		 case 0:
			s=StringUtils.replaceEach(MyApp.res.getString(R.string.jc_awardinfo_msg), new String[]{"H","G","I"}, new String[]{ ts0,ts1," "+ts2});
			break;
		 case 1:
			 if(StringUtils.isBlank(sfp)){
				 s="--";
			 }else{
				 switch (Integer.parseInt(sfp)) {
					case 3:
						s=StringUtils.replaceEach(MyApp.res.getString(R.string.jc_awardinfo_vs3), new String[]{"SPF","SP"}, new String[]{ ts0,ts1});
						break;
					case 1:
						s=StringUtils.replaceEach(MyApp.res.getString(R.string.jc_awardinfo_vs1), new String[]{"SPF","SP"}, new String[]{ ts0,ts1});
						break;
					case 0:
						s=StringUtils.replaceEach(MyApp.res.getString(R.string.jc_awardinfo_vs0), new String[]{"SPF","SP"}, new String[]{ ts0,ts1});
						break;
				  }
			 }
			 break;
		}
		return s;
	}
//	@SuppressLint("SimpleDateFormat")
//	private void initJCListViewAdapter() {
//		try {
//			MessageBean  mB=null;
//			for (int i = 0; i < 2; i++) {
//				for (int j = 0; j < 8; j++) {
//					mB=new MessageBean();
//					Date date = new Date();  
//				    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");  
//					String nowDate = sf.format(date);  
//					Calendar cal = Calendar.getInstance();  
//					cal.setTime(sf.parse(nowDate));  
//					cal.add(Calendar.DAY_OF_YEAR, +i);
//					String D = sf.format(cal.getTime());
////					A	玩法
//					mB.setA("218");
////					B	期号
//					mB.setB(D);
////					C	批次时间
//					mB.setC(D);
//					if(0==i){
////					D	批次时间的星期值
//						mB.setD("2014年11月13 星期四");
//					}else{
//						mB.setD("2014年11月14 星期五");
//					}
////					E	每批次投注开始时间
//					mB.setE("00:00");
////					F	每批次投注截止时间
//					mB.setF("23:00");
////					G	对阵编号
//					mB.setG("00"+String.valueOf(j));
////					H	赛事名称
//					mB.setH("赛事名称XX");
////					I	每场比赛开始时间
//					mB.setI("00:00");
////					J	每场比赛投注截止时间
//					mB.setJ("01:30");
////					K	主队名称
//					mB.setK("主队名称XX");
////					L	是否让球 
//					mB.setL("0");
////					0：表示不让 -n：表示主让客  n：表示客让主
////					M	客队名称
//					mB.setM("客队名称XX");
////					N	销售状态
//					mB.setN("0");
////					-1:审核未通过  0：销售中  1：停止销售
////					2：已开奖，奖金处理中  3：奖金处理完毕  4：取消
////					O	比分
////					P	上半场比分
////					Q	下半场比分
////					T	赛果
////					U	奖金
////					V	本场比赛SP值
////					W	是否是五大联赛（0否1是）
//					mList.add(mB);
//				}
//			}
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}  
//		String[] t=JCPL125.getTimes();//时间对照组 由当前天数递增共5天: 例如 :[20141112, 20141113, 20141114, 20141115, 20141116];
//		Map<String, List<MessageBean>> map=new HashMap<String, List<MessageBean>>();
//		for (int i = 0; i < t.length; i++) {
//			map.put(String.valueOf(i), new ArrayList<MessageBean>());
//		}
//		if(mList==null) return;
//		for (int i = 0; i < mList.size(); i++) {
//			MessageBean messageBean = mList.get(i);
//			if(t[0].equals(messageBean.getC())){//今天
//				map.get("0").add(messageBean);
//			}else if(t[1].equals(messageBean.getC())){//今天 +1
//				map.get("1").add(messageBean);
//			}else if(t[2].equals(messageBean.getC())){//今天 +2
//				map.get("2").add(messageBean);
//			}else if(t[3].equals(messageBean.getC())){//今天 +3
//				map.get("3").add(messageBean);
//			}else if(t[4].equals(messageBean.getC())){//今天 +4
//				map.get("4").add(messageBean);
//			}
//		}
////		筛选过滤
//		for (int i = 0; i < map.size(); i++) {
//			if(map.get(String.valueOf(i)).size() > 0){
//				groupList.add(map.get(String.valueOf(i)));
//			}
//		}
////		mjcAdapter=new JCPL125ListAdapter(mActivity, groupList,jc_Lottery_Lv);
//		jc_Lottery_Lv.setAdapter(mjcAdapter);
//		mjcAdapter.getGroupStatusMap().clear();  //清空组栏展开标记
//		mjcAdapter.getLotteryTag().clear();  //清空 所选场次
//		mjcAdapter.notifyList();
//	}
	
//	public class JCPL125ListAdapter  extends JCListAdapter{//----TODO 等待调试  开奖信息的每个item  等待替换
//	    protected List<String> childisOpenList=new ArrayList<String>(); 
//		public JCPL125ListAdapter(Context context,
//				List<List<MessageBean>> groupArray, JCListView listView) {
//			super(context, groupArray, listView);
//		}
////		{"D":"7","E":"2014-11-09 09:00","F":"2014-11-09 00:40","G":"027","A":"210","B":"20000","C":"20141109","L":"-1","M":"芬洛","N":"1",
////		 "O":"","H":"荷兰乙级联赛","I":"2014-11-09 21:30","J":"2014-11-09 21:10","K":"奈梅亨","U":"0","T":"","Q":"","P":"","S":"","R":""} 
//		@Override
//		public View getChildView(final int groupPosition, final int childPosition,boolean isLastChild, View convertView, ViewGroup parent) {
//			//J每场比赛投注截止时间; K主队名称; L 是否让球 0：表示不让 -n：表示主让客  n：表示客让主;m 客队名称 ,V	本场比赛SP值
//			String  k="",m="",L="",G="",H="",J="" ,v="";
//			String salesType=groupList.get(groupPosition).get(childPosition).getA();//对应的竞彩玩法
//			if(AppUtil.isEmpty(salesType)){
//		    	 return super.getChildView(groupPosition, childPosition, isLastChild,
//						convertView, parent);
//		     }
//			switch (Integer.parseInt(salesType)) {// 不同玩法
//				case 209:
//					//防止 CheckBox focusable 丢失 导致checked 属性回复默认初始值
////					if(convertView==null){
//						convertView =inflater.inflate(R.layout.jc_d209_itemview, parent, false);
////					}
//						k=groupList.get(groupPosition).get(childPosition).getK();//K   主队名称
//					    m=groupList.get(groupPosition).get(childPosition).getM();//M	客队名称
//					    v=groupList.get(groupPosition).get(childPosition).getV();//sp	值
//					    L=groupList.get(groupPosition).get(childPosition).getL();//是否让球
//					    if(!AppUtil.isEmpty(k)){
//							((TextView) convertView.findViewById(R.id.zhu_name)).setText(k);
//						}
//					    if(!AppUtil.isEmpty(m)){
//					    	((TextView) convertView.findViewById(R.id.ke_tv)).setText(m);
//					    }
//					    //非让球区
//					    if(!AppUtil.isEmpty(v)){
//							((CheckBox) convertView.findViewById(R.id.h_ch3)).setText("主胜\n"+v);
//						}
//						if(!AppUtil.isEmpty(v)){
//							((CheckBox) convertView.findViewById(R.id.h_ch1)).setText("平\n"+v);
//						}
//						if(!AppUtil.isEmpty(v)){
//							((CheckBox) convertView.findViewById(R.id.h_ch0)).setText("客胜\n"+v);
//						}
//						//让球区
//						setTagTextViewmsg(((TextView) convertView.findViewById(R.id.vs_tag_tv0)),L);//让球 tagTextView标识
//					   if(!AppUtil.isEmpty(v)){
//							((CheckBox) convertView.findViewById(R.id.R_ch3)).setText("主胜\n"+v);
//						}
//						if(!AppUtil.isEmpty(v)){
//							((CheckBox) convertView.findViewById(R.id.R_ch1)).setText("平\n"+v);
//						}
//						if(!AppUtil.isEmpty(v)){
//							((CheckBox) convertView.findViewById(R.id.R_ch0)).setText("客胜\n"+v);
//						}
//					break;
//				case 218:
////					if(convertView==null){
//						convertView =inflater.inflate(R.layout.jc_d218_itemview, parent, false);
////					}
//						if(!AppUtil.isEmpty(groupList.get(groupPosition).get(childPosition).getK())){//K   主队名称
//							((CheckBox) convertView.findViewById(R.id.cb0)).setText(groupList.get(groupPosition).get(childPosition).getK());
//						}
//						if(!AppUtil.isEmpty(groupList.get(groupPosition).get(childPosition).getM())){//M	客队名称
//							((CheckBox) convertView.findViewById(R.id.cb2)).setText(groupList.get(groupPosition).get(childPosition).getM());
//						}
//					break;
//				case 210:
////					if(convertView==null){
//						convertView =inflater.inflate(R.layout.jc_d210_itemview, parent, false);
////					}
//						 k=groupList.get(groupPosition).get(childPosition).getK();//K   主队名称
//						 m=groupList.get(groupPosition).get(childPosition).getM();//M	客队名称
//						 L=groupList.get(groupPosition).get(childPosition).getL();//L	是否让球 0：表示不让 -n：表示主让客  n：表示客让主
//						if(!AppUtil.isEmpty(k)){
//							((CheckBox) convertView.findViewById(R.id.cb0)).setText(k);
//						}
//						if(!AppUtil.isEmpty(m)){
//							((CheckBox) convertView.findViewById(R.id.cb2)).setText(m);
//						}
//						setTagTextViewmsg(((TextView)convertView.findViewById(R.id.vs_tag_tv0)),L);
//					break;
//				default:
//					super.getChildView(groupPosition, childPosition, isLastChild,
//							convertView, parent);
//					break;
//	           }
//			 // 公共实例化控件部分
//				G=groupList.get(groupPosition).get(childPosition).getG();//G	对阵编号
//				H=groupList.get(groupPosition).get(childPosition).getH();//H	赛事名称
//				J=groupList.get(groupPosition).get(childPosition).getJ();//J	每场比赛投注截止时间"J":"2014-11-09 21:10"
//				if(J.contains(" ")){
//					String[] t=J.split(" ");
//					J=t[1];//21:10
//				}
//				
//				if(!AppUtil.isEmpty(H)){
//					((TextView) convertView.findViewById(R.id.name)).setText(H);
//				}
//				if(!AppUtil.isEmpty(G)){
//					((TextView) convertView.findViewById(R.id.session)).setText(G);
//				}
//				if(!AppUtil.isEmpty(J)){
//					((TextView) convertView.findViewById(R.id.time)).setText(J);
//				}
//				convertView.findViewById(R.id.jc_c_openbtn).setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View view) {
//						if(childisOpenList.contains(String.valueOf(groupPosition)+"-"+String.valueOf(childPosition))){
//							childisOpenList.remove(String.valueOf(groupPosition)+"-"+String.valueOf(childPosition));
//						}else{
//							childisOpenList.add(String.valueOf(groupPosition)+"-"+String.valueOf(childPosition));
//						}
//						notifyDataSetChanged();
//					}
//				});
////				List<CheckBox> cbList = ((CheckboxGroup) convertView.findViewById(R.id.item_chgp)).getCbList();
////				String s=String.valueOf(groupPosition)+"-"+String.valueOf(childPosition);
////				if(lotteryTag.containsKey(s)){// 设置选中效果
////					for (int i = 0; i < cbList.size(); i++) {
////						for (int j = 0; j < lotteryTag.get(s).size(); j++) {
////							if(cbList.get(i).getTag().equals(lotteryTag.get(s).get(j))){
////								cbList.get(i).setChecked(true);
////							}
////						}
////				     }
////					((CheckboxGroup) convertView.findViewById(R.id.item_chgp)).setTagList(lotteryTag.get(s));
////				}else{// 恢复默认效果
////					for (int i = 0; i < cbList.size(); i++) {
////						cbList.get(i).setChecked(false);
////				     }
////				}
//				   //  子布局 选票场次
//				((CheckboxGroup) convertView.findViewById(R.id.item_chgp)).setChbxGpOnCheckedListener(new OnChbxGpOnCheckedListener() {
//					@Override
//					public void setOnChildCheckedListener(View v, List<String> tagList,
//							List<String> textList) {
//						if(tagList.size() >0){
//							lotteryTag.put(String.valueOf(groupPosition)+"-"+String.valueOf(childPosition), tagList);
//						}else{
//							lotteryTag.remove(String.valueOf(groupPosition)+"-"+String.valueOf(childPosition));
//						}
//						String batch=groupList.get(groupPosition).get(childPosition).getC();//C	批次时间
//					    notifyDataSetChanged();
//					}
//				});
//				//  组栏效果(默认)
//				convertView.findViewById(R.id.item_item_layout).setVisibility(View.GONE);
//				Drawable  mDrawable=MyApp.res.getDrawable(R.drawable.d1);
//				mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(), mDrawable.getMinimumHeight());
//				((TextView) convertView.findViewById(R.id.ratio_show_tv)).setCompoundDrawables(null, mDrawable, null, null);
//				if(childisOpenList.contains(String.valueOf(groupPosition)+"-"+String.valueOf(childPosition))){
//					convertView.findViewById(R.id.item_item_layout).setVisibility(View.VISIBLE);
//					mDrawable=MyApp.res.getDrawable(R.drawable.u1);
//					mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(), mDrawable.getMinimumHeight());
//					((TextView) convertView.findViewById(R.id.ratio_show_tv)).setCompoundDrawables(null, mDrawable, null, null);
//				}
//				
//			return convertView;
//		}
//		
//	}
	/**
	 * 设置背景 和字体色彩  
	 * 例如:开奖信息中胜平负对应的信息有个字的背景色 和字体颜色
	 * @param textView
	 * @param L 
	 */
	public static void setTagTextViewmsg(TextView textView, String l) {
		
	}
}
