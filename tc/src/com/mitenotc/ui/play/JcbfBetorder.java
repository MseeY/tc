package com.mitenotc.ui.play;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.mitenotc.bean.JcBfBean;
import com.mitenotc.bean.MessageBean;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.ui.buy.JcBaseFragment;
import com.mitenotc.ui.ui_utils.MGridView;
import com.mitenotc.ui.ui_utils.MyToast;
import com.mitenotc.ui.ui_utils.MGridView.ActionUpListener;
import com.mitenotc.ui.ui_utils.custom_ui.Custom_jcSession;
import com.mitenotc.utils.DensityUtil;

public class JcbfBetorder extends BaseFragment implements OnClickListener{
	
	private JcBaseFragment fragment;// 该彩种的选号界面的fragment//ck 添加
	private Class<? extends BaseFragment> lotteryClazz;// 当前彩种的选号界面的fragment的clazz.
	private ListView bet_indent_listview;// 订单列表
	private Button oneselfchoose_btn;// 编辑/添加赛事
	private String childId;
	private JCBetListAdapter adapter;// 竞彩投注适配器
	private TextView show_bet_result_tv;// 总共投注 结果与金额
	private JcBfBean bfbean;
	private List<Entry<String, List<String>>> scoreList;
	private List<Entry<String, MessageBean>> piaoList;
	private Button set_group_btn;
	private MGridView[] mSetTypeGV = null;
	private PopupWindow pop_groupSelect_msg;// 下单列表投注方式筛选信息
	private LinearLayout group_set_layout;// 投注设置view
	private EditText multiple_number_ed;// 投注倍数
	private EditText pop_multiple_number_ed;// 投注倍数
	private Button payment_btn;
	private Button set_group_btn1;
	private List<View> mViews_0 = new ArrayList<View>(); // 串关View_0
	private List<Integer> sessionDataList_0 = new ArrayList<Integer>(); // 串关数据
	private List<View> mViews_1 = new ArrayList<View>(); // 串关View_1
	private List<Integer> sessionDataList_1 = new ArrayList<Integer>(); // 串关数据
	
	String[] simplex_text = new String[] { "1串1", "2串1", "3串1", "4串1", "5串1", "6串1", "7串1", "8串1" };
	String[] simplex_tag = new String[] { "101", "102", "103", "104", "105", "106", "107", "108" };

	String[] unsimplex_text = new String[] { "3串3", "3串4",// 1
			"4串4", "4串5", "4串6", "4串11",// 5
			"5串5", "5串6", "5串10", "5串16", "5串20", "5串26",// 11
			"6串6", "6串7", "6串15", "6串20", "6串22", "6串35", "6串42", "6串50", "6串57",// 20
			"7串7", "7串8", "7串21", "7串35", "7串120",// 25
			"8串8", "8串9", "8串28", "8串56", "8串70", "8串247" };
	
	String[] unsimplex_tag = new String[] { "603", "118", "604", "120", "605", "121", "606", "123", "607", "124", "608", "125", "609", "127", "610", "611", "128", "612", "129", "613", "602", "702",
			"703", "704", "705", "706", "802", "803", "804", "805", "806", "807" };

	
	/**
	 * 处理清空
	 */
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 2:
//				Toast.makeText(mActivity, "被调用了", 0).show();
				adapter = new JCBetListAdapter();// 列表适配器
				if (bet_indent_listview != null && adapter != null) {
					bet_indent_listview.setAdapter(adapter);
					adapter.notifyDataSetChanged();
				}
				break;
			}
		}
	};

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jc_show_bet);
		if (getMyBundle() != null) {// 单关or串关
			childId = getMyBundle().getString("childId");
		}
		mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		setTitleNav(MyApp.lotteryMap.get(MyApp.order.getLotteryId()), R.drawable.title_nav_back, 0);
		initView();
		setNotice();
	}

	private void setNotice() {
	}

	private void initView() {
		try {
			lotteryClazz = (Class<? extends BaseFragment>) Class.forName("com.mitenotc.ui.play.JCPL" + MyApp.order.getAppId());
			fragment = (JcBaseFragment) getFragment(lotteryClazz);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			MyToast.showToast(mActivity, "该玩法暂不存在");
		}
		bfbean = new JcBfBean();
		scoreList = bfbean.getScoreList();
		piaoList  = bfbean.getPiaoList();
		bet_indent_listview = (ListView) findViewById(R.id.bet_indent_listview);
		bet_indent_listview.setVerticalScrollBarEnabled(false);// 不显示滚动条
		bet_indent_listview.setCacheColorHint(0);
		bet_indent_listview.setDrawingCacheEnabled(false);
		oneselfchoose_btn = (Button) findViewById(R.id.oneselfchoose_btn);
		oneselfchoose_btn.setOnClickListener(this);
		mHandler.sendEmptyMessage(2);
		set_group_btn = (Button) findViewById(R.id.set_group_btn);
		set_group_btn.setOnClickListener(this);
		show_bet_result_tv = (TextView) findViewById(R.id.show_bet_result_tv);// TextView
		show_bet_result_tv.setText(bfbean.getNotice());
		set_group_btn.setTextColor(MyApp.res.getColor(R.color.red));
		group_set_layout = (LinearLayout) findViewById(R.id.group_set_layout);
		multiple_number_ed = (EditText) findViewById(R.id.multiple_number_ed);
		show_bet_result_tv = (TextView) findViewById(R.id.show_bet_result_tv);// TextView
		payment_btn = (Button) findViewById(R.id.payment_btn);
		initSelect_PoP();
	}
	
	class JCBetListAdapter extends BaseAdapter {
		public JCBetListAdapter() {
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ChildOtherHolder holder;
			Entry<String, List<String>> score = scoreList.get(position);
			Entry<String, MessageBean> piao = piaoList.get(position);
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.jc_zqbfbet_item,null);
				holder = new ChildOtherHolder(convertView, 211);
				convertView.setTag(holder);
			} else {
				holder = (ChildOtherHolder) convertView.getTag();
			}
			holder.init(211);
			holder.select_text.setText(score.getValue().toString());
			holder.zhu.setText(piao.getValue().getK());
			holder.ke.setText(piao.getValue().getM());
			return convertView;
		}

		@Override
		public int getCount() {
			return scoreList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

	}
	
	class ChildOtherHolder {
		private TextView select_text;
		private TextView ke;
		private TextView zhu;
		public ChildOtherHolder(View v, int salesType) {
			switch (salesType) {
			case 211:
				select_text = (TextView) v.findViewById(R.id.select_text);
				ke = (TextView) v.findViewById(R.id.ke);
				zhu = (TextView) v.findViewById(R.id.zhu);
				break;
			}
		}

		public void init(int salesType) {
			switch (salesType) {
			case 211:
				select_text.setText("");
				ke.setText("");
				zhu.setText("");
				break;
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//编辑赛事
		case R.id.oneselfchoose_btn:
			finish();
			fragment.reflash();
			break;
		case R.id.set_group_btn:
			showpop();
			break;
		case R.id.set_group_btn1:
			closepop();
			break;
		}
		
	}
	
	private void closepop() {
		if (pop_groupSelect_msg != null && pop_groupSelect_msg.isShowing()) {
			pop_groupSelect_msg.dismiss();
			group_set_layout.setVisibility(View.VISIBLE);
		}
		multiple_number_ed.setText(String.valueOf(MyApp.order.getFold()));
		multiple_number_ed.invalidate();// View 刷新
		set_group_btn.setText("");
		set_group_btn.setTextColor(MyApp.res.getColor(R.color.red));
		
	}

	private void showpop() {
		if (pop_groupSelect_msg != null && pop_groupSelect_msg.isShowing()) {// 防止重复弹出
			pop_groupSelect_msg.dismiss();
			group_set_layout.setVisibility(View.VISIBLE);
		}
		showSetPopView(pop_groupSelect_msg.getContentView());
		mSetTypeGV[0].getSelectedBalls().clear();
		mSetTypeGV[1].getSelectedBalls().clear();
		
		List<String> passtypeList = bfbean.getPasstypeList();
		for (int i = 0; i < passtypeList.size(); i++) {
			int parseInt = Integer.parseInt(passtypeList.get(i));
			mSetTypeGV[0].getSelectedBalls().add(parseInt);
		}
//		for (int i = 0; i < MyApp.order.getPassTypeList().size(); i++) {
//			int s = MyApp.order.getPassTypeList().get(i);
//			if (s <= 108) {
//				mSetTypeGV[0].getSelectedBalls().add(s);
//			} else {
//				mSetTypeGV[1].getSelectedBalls().add(s);
//			}
//		}

		int xoffInDip = DensityUtil.px2dip(mActivity, 74);
		pop_groupSelect_msg.showAsDropDown(payment_btn, 0, xoffInDip);
		group_set_layout.setVisibility(View.INVISIBLE);
		pop_multiple_number_ed.setText(String.valueOf(MyApp.order.getFold()));
		mSetTypeGV[0].notifyDataSetChanged();
		mSetTypeGV[1].notifyDataSetChanged();
		pop_multiple_number_ed.invalidate();// View 刷新
		pop_groupSelect_msg.update();
	}
	
	private void initSelect_PoP() {
		if (mSetTypeGV == null)
			mSetTypeGV = new MGridView[2];
		final View v = View.inflate(mActivity, R.layout.set_bet_group, null);
		pop_groupSelect_msg = new PopupWindow(v, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, true);
		pop_groupSelect_msg.setOutsideTouchable(true);// 触摸其它位置 是否 使pop dismiss
		pop_groupSelect_msg.setBackgroundDrawable(new ColorDrawable(-00000)); // 设置其它部分为半透明
		// pop_groupSelect_msg.setClippingEnabled(false);//保证键盘调用时候(对pop中健在的View做位图裁剪)的精确弹出
		pop_groupSelect_msg.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				group_set_layout.setVisibility(View.VISIBLE);
				multiple_number_ed.setText(String.valueOf(MyApp.order.getFold()));
				multiple_number_ed.invalidate();// View 刷新

				set_group_btn.setText("");
				set_group_btn.setTextColor(MyApp.res.getColor(R.color.red));
			}
		});
		pop_multiple_number_ed = (EditText) v.findViewById(R.id.pop_multiple_number_ed);
		pop_multiple_number_ed.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				RelativeLayout rlayout = (RelativeLayout) v.findViewById(R.id.layout_id);
				rlayout.setPadding(0, 0, 0, 0);
				// pop_groupSelect_msg.showAtLocation(payment_btn,
				// Gravity.LEFT|Gravity.TOP, pop_groupSelect_msg.getWidth(),
				// -(pop_groupSelect_msg.getHeight()-74));
			}
		});
	}
	
	private void showSetPopView(View v) {
		
		mViews_0.clear();
		sessionDataList_0.clear();
		mViews_1.clear();
		sessionDataList_1.clear();

		Button set_group_pop = (Button) v.findViewById(R.id.set_group_btn1);
		CheckBox more_chbx = (CheckBox) v.findViewById(R.id.more_chbx_id);
		TextView explain_tv = (TextView) v.findViewById(R.id.explain_id);
		pop_multiple_number_ed = (EditText) v.findViewById(R.id.pop_multiple_number_ed);
		pop_multiple_number_ed.setText("1");// 设置默认倍数
		mSetTypeGV[0] = (MGridView) v.findViewById(R.id.bet_group_1);
		mSetTypeGV[1] = (MGridView) v.findViewById(R.id.bet_group_2);
		show(mSetTypeGV[0]);// 默认显示
		show(mSetTypeGV[1]);
		show(more_chbx);
		show(explain_tv);
		int n = scoreList.size();
		switch (n) {
		case 0:// 单关处理 101
			hide(more_chbx);
			hide(explain_tv);
			hide(mSetTypeGV[0]);
			hide(mSetTypeGV[1]);
			break;
		case 1:// 单关处理 101
			break;
		case 2:// 过关处理 默认是2串1
			Custom_jcSession mBox = new Custom_jcSession(mActivity, simplex_tag[1], simplex_text[1]);
			mViews_0.add(mBox);
			sessionDataList_0.add(Integer.parseInt(simplex_tag[1]));
			mSetTypeGV[0].simpleInit(false, sessionDataList_0, 1, mViews_0);
			mSetTypeGV[0].notifyDataSetChanged();
			hide(mSetTypeGV[1]);
			hide(more_chbx);
			break;
		case 3:
			initMGV0(2, 1);
			break;
		case 4:
			initMGV0(3, 5);
			break;
		default: // >= 8
			initMGV0(3, 5);
			break;
		}
		
		mSetTypeGV[0].simpleInit(false, sessionDataList_0, 1, mViews_0);// M串1
		mSetTypeGV[1].simpleInit(false, sessionDataList_1, 1, mViews_1);// M串N
		mSetTypeGV[0].setMaxNum(0);// 0多选/不选
		mSetTypeGV[0].setMiniNum(1);
		mSetTypeGV[1].setMaxNum(1);
		mSetTypeGV[1].setMiniNum(1);
		mSetTypeGV[1].setSelfMutual(true);// M串N自身互斥
		mSetTypeGV[0].setActionUpListener(new ActionUpListener() {
			@Override
			public void onActionUp() {// 串关容器互斥
				
				mSetTypeGV[1].clear();
				MyApp.order.getPassTypeList().clear();// 重新给竞彩ticket 设置过关方式

//				Iterator<Integer> iterator = mSetTypeGV[0].getSelectedBalls().iterator();
//				
//				while (iterator.hasNext()) {
//					Integer next = iterator.next();
//					String temp = next.toString().substring(2, 3);
//
//					int size = 0;
//					for (int i = 0; i < MyApp.order.getTickets().size(); i++) {
//						size += MyApp.order.getTickets().get(i).getSelectMessageBean().size();
//					}
//
//					if (!TextUtils.isEmpty(temp)) {
//						if (size < Integer.parseInt(temp))
//							if (mSetTypeGV[0].getSelectedBalls().contains(next)) {
//								iterator.remove();
//							}
//					}
//
//				}
				
				bfbean.getPasstypeList().clear();
				List<String> selectedText = mSetTypeGV[0].getSelectedText();
				
				for (int i = 0; i < selectedText.size(); i++) {
					String str = selectedText.get(i).substring(0, 1);
					bfbean.getPasstypeList().add("10"+str);
				}
				show_bet_result_tv.setText(bfbean.getNotice());
//				System.out.println(";;;;;;;;;;;;;;;;;"+bfbean.getPasstypeList().toString());

//				MyApp.order.setObunch(mSetTypeGV[0].getSelectedBallstoString());
//				if (mSetTypeGV[0].getSelectedBalls().size() > 0) {
//					for (int i = 0; i < mSetTypeGV[0].getSelectedBalls().size(); i++) {
//						MyApp.order.getPassTypeList().add(mSetTypeGV[0].getSelectedBalls().get(i));
//					}
//				}
//				cgText = mSetTypeGV[0].getSelectedTextToString();
//				if (adapter != null) {
//					adapter.clearDanList();
//				}
				// //System.out.println("210-------->way--->"+cgText);
				// System.out.println("Size:" +
				// MyApp.order.getPassTypeList().size());

//				setNotice();// 更新票面提示
			}
		});
		mSetTypeGV[1].setActionUpListener(new ActionUpListener() {
			@Override
			public void onActionUp() {// 串关容器互斥
				mSetTypeGV[0].clear();
				MyApp.order.getPassTypeList().clear();// 重新给竞彩ticket 设置过关方式
				MyApp.order.setObunch(mSetTypeGV[1].getSelectedBallstoString());
				if (mSetTypeGV[1].getSelectedBalls().size() > 0) {
					for (int i = 0; i < mSetTypeGV[1].getSelectedBalls().size(); i++) {
						MyApp.order.getPassTypeList().add(mSetTypeGV[1].getSelectedBalls().get(i));
					}
				}
			}
		});
		more_chbx.setOnCheckedChangeListener(new OnCheckedChangeListener() {// 收起
																			// 或展开Checkbox
					@Override
					public void onCheckedChanged(CompoundButton btn, boolean isChecked) {
						switch (btn.getId()) {
						case R.id.more_chbx_id:
							if (isChecked) {
								btn.setText("更多玩法");
								hide(mSetTypeGV[1]);
							} else {
								btn.setText("收起更多");
								show(mSetTypeGV[1]);
							}
							if (pop_groupSelect_msg != null && pop_groupSelect_msg.isShowing()) {
								pop_groupSelect_msg.update();
							}
							break;
						}
					}
				});
		set_group_pop.setOnClickListener(this);
		explain_tv.setOnClickListener(this);
	}
	
	private void initMGV0(int n, int t) {
		Custom_jcSession mBox = null;
		for (int i = 1; i <= n; i++) {// 此处的i 与 串关相关(不包括1串1单关,从1开始)
			mBox = new Custom_jcSession(mActivity, simplex_tag[i], simplex_text[i]);
			mViews_0.add(mBox);
			sessionDataList_0.add(Integer.parseInt(simplex_tag[i]));
		}
//		for (int i = 0; i <= t; i++) {
//			mBox = new Custom_jcSession(mActivity, unsimplex_tag[i], unsimplex_text[i]);
//			mViews_1.add(mBox);
//			sessionDataList_1.add(Integer.parseInt(unsimplex_tag[i]));
//		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(bfbean.getScoreList()!=null||bfbean.getPiaoList()!=null){
		bfbean.getScoreList().clear();
		bfbean.getPiaoList().clear();
		}
	}
}
