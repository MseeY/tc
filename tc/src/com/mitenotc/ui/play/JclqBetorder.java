package com.mitenotc.ui.play;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.mitenotc.bean.MessageBean;
import com.mitenotc.bean.UserBean;
import com.mitenotc.enums.AccountEnum;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.tc.ThirdActivity;
import com.mitenotc.ui.TCDialogs;
import com.mitenotc.ui.TCDialogs.MyClickedListener;
import com.mitenotc.ui.TCWebFragment;
import com.mitenotc.ui.base.BaseActivity.MyBackPressedListener;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.ui.buy.JcBaseFragment;
import com.mitenotc.ui.pay.Paymain;
import com.mitenotc.ui.ui_utils.MGridView;
import com.mitenotc.ui.ui_utils.MGridView.ActionUpListener;
import com.mitenotc.ui.ui_utils.MyToast;
import com.mitenotc.ui.ui_utils.RefreshYiLouReceiver;
import com.mitenotc.ui.ui_utils.ShakeListener;
import com.mitenotc.ui.ui_utils.ShakeListener.OnShakeListener;
import com.mitenotc.ui.ui_utils.TextViewGroup;
import com.mitenotc.ui.ui_utils.TextViewGroup.OnTextViewGroup;
import com.mitenotc.ui.ui_utils.custom_ui.Custom_jcSession;
import com.mitenotc.utils.DensityUtil;
import com.mitenotc.utils.FormatUtil;
import com.mitenotc.utils.Getlotterykind;

/**
 * 篮彩订单显示页面 (考虑到后期维护暂时将订单列表拆分开) 订单页面 所有彩种通用
 * 
 * @author wanli
 * 
 */
public class JclqBetorder extends BaseFragment implements OnClickListener {
	private JcBaseFragment fragment;// 该彩种的选号界面的fragment//ck 添加
	private Class<? extends BaseFragment> lotteryClazz;// 当前彩种的选号界面的fragment的clazz.
	private Button oneselfchoose_btn;// 编辑/添加赛事
	private Button set_group_btn;// 设置组 默认收起
	private PopupWindow pop_groupSelect_msg;// 下单列表投注方式筛选信息
	private MGridView[] mSetTypeGV = null;
	private LinearLayout group_set_layout;// 投注设置view
	private Button clearchoose_btn;// 清空列表
	private Button bonus_optimize_btn;// 奖金优化
	private Button payment_btn;// 付款

	private String cgText = "串关必选*";// 串关Text
	private MessageBean returnMB;// 服务器参数
	private String childId = "";

	private static long AllPOUR = 0;// 总注数
	/** 震动 **/
	private ShakeListener shakeListener;
	private RefreshYiLouReceiver mReceiver;
	private long lastClickTime = 0; // 防止重复点击

	private TextView no_selection_tv;// 没有选择任何投注号码时候的提示（根据listView 的内容是否为空判断）
	private CheckBox agreeEntrust_checkBox;// 同意委托
	private TCDialogs dialog;// 提示框
	private Dialog mDialog;

	private ListView bet_indent_listview;// 订单列表
	private JCBetListAdapter adapter;// 竞彩投注适配器
	// 两个投注倍数等价
	private EditText multiple_number_ed;// 投注倍数
	private EditText pop_multiple_number_ed;// 投注倍数

	private TextView show_bet_result_tv;// 总共投注 结果与金额
	private List<View> mViews_0 = new ArrayList<View>(); // 串关View_0
	private List<Integer> sessionDataList_0 = new ArrayList<Integer>(); // 串关数据
	private List<View> mViews_1 = new ArrayList<View>(); // 串关View_1
	private List<Integer> sessionDataList_1 = new ArrayList<Integer>(); // 串关数据
	// 此处变量值后期可以抽取到 资源文件中减少java代码量
	String[] simplex_text = new String[] { "1串1", "2串1", "3串1", "4串1", "5串1", "6串1", "7串1", "8串1" };
	String[] simplex_tag = new String[] { "02", "03", "04", "05", "06", "07", "08", "09" };

	String[] unsimplex_text = new String[] { 
			"3串3", "3串4",
			"4串4", "4串5", "4串6", "4串11",
			"5串5", "5串6", "5串10", "5串16", "5串20", "5串26",
			"6串6", "6串7", "6串15", "6串20", "6串22", "6串35", "6串42", "6串50", "6串57",
			"7串7", "7串8", "7串21", "7串35", "7串120",
			"8串8", "8串9", "8串28", "8串56", "8串70", "8串247" };
	String[] unsimplex_tag = new String[] { 
			"31", "17", 
			"32", "19", "33", "20", 
			"34", "22", "35", "23", "36", "24", 
			"37", "26", "38", "39", "27", "40", "28", "41", "30", 
			"42", "43", "44", "45", "46",
			"47", "48", "49", "50", "51", "52" };
	// String[] unsimplex_tag = new String[] { "603", "118", "604", "120",
	// "605", "121", "606", "123", "607", "124", "608", "125", "609", "127",
	// "610", "611", "128", "612", "129", "613", "602", "702",
	// "703", "704", "705", "706", "802", "803", "804", "805", "806", "807" };
	String[] hunguan = MyApp.res.getStringArray(R.array.lottery_hunguan);// 所有竞彩混关
	String[] alllq = MyApp.res.getStringArray(R.array.lottery_alllq);// 所有篮球彩种不包含混关
	/**
	 * 处理清空
	 */
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if (MyApp.order.getTickets().size() > 0) {
					dialog = new TCDialogs(mActivity);
					dialog.popDeleteConfirm(new MyClickedListener() {
						@Override
						public void onClick() {
							dialog.dismiss();
						}
					}, new MyClickedListener() {
						@Override
						public void onClick() {
							toClearListView();
						}
					});
				}

				break;
			case 2:
				adapter = new JCBetListAdapter(mActivity);// 列表适配器
				if (bet_indent_listview != null && adapter != null) {
					bet_indent_listview.setAdapter(adapter);
					adapter.notifyDataSetChanged();
				}
				break;
			}
		}
	};

	@Override
	protected void onMessageReceived(Message msg) {
		returnMB = (MessageBean) msg.obj;
		if ("14004".equals(returnMB.getA())) {
			MyToast.showToast(mActivity, returnMB.getB());
			return;
		}
		switch (msg.arg1) {
		case 4001:
			if (0 == Integer.parseInt(returnMB.getA())) {
				MyToast.showToast(mActivity, returnMB.getB());
				if (!StringUtils.isBlank(returnMB.getD())) {// D 账户可用余额
					UserBean.getInstance().setAvailableMoney(Long.parseLong(returnMB.getD()));
				}
				if (!StringUtils.isBlank(returnMB.getE())) {// E 账户可提现金额
					UserBean.getInstance().setAvailableCash(Long.parseLong(returnMB.getE()));
				}
				if (!StringUtils.isBlank(returnMB.getF())) {// F
															// 账户可用资金(能用来购彩的金额，包括彩金卡)
					UserBean.getInstance().setAvailableBalance(Long.parseLong(returnMB.getF()));
				}
				AccountEnum.convertMessage(returnMB.getLIST());
				MyApp.order.setOrderId(returnMB.getC());// C 订单流水号
				Bundle payBundle = new Bundle();
				payBundle.putString("lotteryId", MyApp.order.getLotteryId());// 竞彩足球使用的是内部id
																				// 210
																				// 编号
				payBundle.putString("orderId", returnMB.getC());// 1107接口需要的 订单号
				payBundle.putString("money", MyApp.order.getAmount() + "");// 1107
																			// 接口需要的第三方支付金额,这里其实是支付的总金额
				payBundle.putString("availableAmount", returnMB.getD());// 用于确定是否进行红包支付
				payBundle.putString("lotteryId", MyApp.order.getLotteryId());// 支付中需要的
																				// 彩票的彩种和期数
				payBundle.putString("issue", MyApp.order.getIssue());// 支付中需要的期数
				payBundle.putInt(MyApp.res.getString(R.string.cmd), 1107);// 支付中需要的CMD
				// 启动支付页面
				start(ThirdActivity.class, Paymain.class, payBundle);
				// 关闭选号页面和下单页面 并重置订单
				fragment.finish();
				setFragmentCacheEnable(false);// fragment 不需要缓存
				MyApp.resetOrderBean();// 重置订单
				finish();
			} else {
				MyToast.showToast(mActivity, returnMB.getB());
				try {
				// 比对销售期号,请求服务器当前销售期数
				if (getSellexpect(returnMB)) {
				} else {
					MyToast.showToast(mActivity, "销售期已成功更新！");
				}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			break;
		}
		super.onMessageReceived(msg);
	}

	@Override
	protected void nullResult() {
		payment_btn.setEnabled(true);// 防止重复点击
		payment_btn.setText("付款");
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
		}
	}

	/**
	 * 期号比对 服务器返回的当前销售期 return true : 一致;false ：相反
	 */
	private boolean getSellexpect(MessageBean msgb) {
		// 比对期号是否一致 140304030
		if (msgb.getLIST().get(0).getD().equals(MyApp.order.getIssue())) {
			return true;
		} else {
			// 更新Order 里面的期号！
			MyApp.order.setIssue(msgb.getLIST().get(0).getD());
			return false;
		}
	}

	/**
	 * 清空所有的票
	 */
	private void toClearListView() {
		cgText = "";
		MyApp.order.getTickets().clear();// 选票
		MyApp.order.setObunch("");// 过关方式
		MyApp.order.getPassTypeList().clear();// 串关
		if (MyApp.order.getTicketsMB() != null) {
			MyApp.order.getTicketsMB().clear();
		}
		adapter.notifyDataSetChanged();
		setNotice();
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jc_show_bet);
		mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		if (getMyBundle() != null) {// 单关or串关
			childId = getMyBundle().getString("childId");
		}
		setTitleNav(MyApp.lotteryMap.get(MyApp.order.getLotteryId()), R.drawable.title_nav_back, 0);
		init();
		setLisnten();

		MyApp.order.setFold(1);
		multiple_number_ed.setText("1");
		if ("2".equals(childId)) {
			pop_multiple_number_ed.setText("1");
		}
		setNotice();// 更新票面提示
	}

	private void initSelect_PoP() {
		if (mSetTypeGV == null)
			mSetTypeGV = new MGridView[2];
		final View v = View.inflate(mActivity, R.layout.set_bet_group, null);
		pop_groupSelect_msg = new PopupWindow(v, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, true);
		pop_groupSelect_msg.setOutsideTouchable(true);// 触摸其它位置 是否 使pop dismiss
		pop_groupSelect_msg.setBackgroundDrawable(new ColorDrawable(-00000)); // 设置其它部分为半透明
																				// 可见
		// pop_groupSelect_msg.setClippingEnabled(false);//保证键盘调用时候(对pop中健在的View做位图裁剪)的精确弹出
		pop_groupSelect_msg.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				group_set_layout.setVisibility(View.VISIBLE);
				multiple_number_ed.setText(String.valueOf(MyApp.order.getFold()));
				multiple_number_ed.invalidate();// View 刷新

				set_group_btn.setText(cgText);
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

	int choose = -1;// 判断是否是mSetType[0]中的多串一被选择了 还是mSetType[1]中的多串多被选择了

	/**
	 * 串关筛选
	 * 
	 * @param v
	 */
	private void showSetPopView(View v) {
		// 实例化
		mViews_0.clear();
		sessionDataList_0.clear();
		mViews_1.clear();
		sessionDataList_1.clear();
		Button set_group_pop = (Button) v.findViewById(R.id.set_group_btn1);
		CheckBox more_chbx = (CheckBox) v.findViewById(R.id.more_chbx_id);
		TextView explain_tv = (TextView) v.findViewById(R.id.explain_id);
		pop_multiple_number_ed = (EditText) v.findViewById(R.id.pop_multiple_number_ed);
		pop_multiple_number_ed.setText(String.valueOf(MyApp.order.getFold()));// 设置默认倍数
		pop_multiple_number_ed.setOnClickListener(this);
		mSetTypeGV[0] = (MGridView) v.findViewById(R.id.bet_group_1);
		mSetTypeGV[1] = (MGridView) v.findViewById(R.id.bet_group_2);
		int n = 0;// 默认值是2串1
		for (int i = 0; i < MyApp.order.getTickets().size(); i++) {
			n += MyApp.order.getTickets().get(i).getSession();// 实际场次选票
		}
		show(mSetTypeGV[0]);// 默认显示
		hide(mSetTypeGV[1]);
		hide(more_chbx);
		show(explain_tv);
		// // 篮彩目前暂时不开放M串N的方式---TODO
		// hide(mSetTypeGV[1]);
		// hide(more_chbx);
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
		case 5:
			initMGV0(4, 11);
			break;
		case 6:
			initMGV0(5, 20);
			break;
		case 7:
			initMGV0(6, 25);
			break;
		default: // >= 8
			initMGV0(simplex_tag.length - 1, unsimplex_tag.length - 1);
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

				Iterator<Integer> iterator = mSetTypeGV[0].getSelectedBalls().iterator();
				while (iterator.hasNext()) {
					Integer next = iterator.next();
					String temp = next.toString().substring(0, 1);
					int size = 0;
					for (int i = 0; i < MyApp.order.getTickets().size(); i++) {
						size += MyApp.order.getTickets().get(i).getSelectMessageBean().size();
					}

					if (!TextUtils.isEmpty(temp)) {
						if (size < Integer.parseInt(temp) - 1)
							if (mSetTypeGV[0].getSelectedBalls().contains(next)) {
								iterator.remove();
							}
					}

				}

				MyApp.order.setObunch(mSetTypeGV[0].getSelectedBallstoString());
				if (mSetTypeGV[0].getSelectedBalls().size() > 0) {
					for (int i = 0; i < mSetTypeGV[0].getSelectedBalls().size(); i++) {
						MyApp.order.getPassTypeList().add(mSetTypeGV[0].getSelectedBalls().get(i));
					}
				}
				cgText = mSetTypeGV[0].getSelectedTextToString();
				if (adapter != null) {
					adapter.clearDanList();
				}
				// System.out.println("321-------->way--->"+cgText);

				choose = 0;
				setNotice();// 更新票面提示
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
				cgText = mSetTypeGV[1].getSelectedTextToString();
				if (adapter != null) {
					adapter.clearDanList();
				}
				// System.out.println("210-------->way--->"+cgText);

				choose = 1;
				setNotice();// 更新票面提示
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
		if (n <= 2) {
			explain_tv.setText("2串1:猜中2场中奖 >>");
		} else {
			explain_tv.setText("2串1,3串1:等什么意思 >>");
		}
	}

	/**
	 * 固定调用方式 int n,int t 分别已经进过测试 对应的值已经和串关 Text tag资源数据一一对应 例如 选票选四场 : n: 指
	 * 2串1 ... 4串1 以及tag t: 指 3串3 ... 4串11 以及tag
	 * 
	 * @param n
	 * @param t
	 */
	private void initMGV0(int n, int t) {
		Custom_jcSession mBox = null;
		for (int i = 1; i <= n; i++) {// 此处的i 与 串关相关(不包括1串1单关,从1开始)
			mBox = new Custom_jcSession(mActivity, simplex_tag[i], simplex_text[i]);
			mViews_0.add(mBox);
			sessionDataList_0.add(Integer.parseInt(simplex_tag[i]));
		}

		for (int i = 0; i <= t; i++) {
			mBox = new Custom_jcSession(mActivity, unsimplex_tag[i], unsimplex_text[i]);
			mViews_1.add(mBox);
			sessionDataList_1.add(Integer.parseInt(unsimplex_tag[i]));
		}

	}

	/**
	 * 初始化准备
	 */
	private void init() {
		// if(MyApp.order.getLotteryId()!=null &&
		// !"".equals(MyApp.order.getLotteryId())){
		// max_expect=CustomTagEnum.getItemByLotteryId(Integer.parseInt(MyApp.order.getLotteryId())).getMaxChasableIssueNum();
		// setTitleText(CustomTagEnum.getItemByLotteryId(Integer.parseInt(MyApp.order.getLotteryId())).getLotteryName());
		// }
		try {
			lotteryClazz = (Class<? extends BaseFragment>) Class.forName("com.mitenotc.ui.play.JCPL" + MyApp.order.getAppId());
			fragment = (JcBaseFragment) getFragment(lotteryClazz);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			MyToast.showToast(mActivity, "该玩法暂不存在");
		}
		bet_indent_listview = (ListView) findViewById(R.id.bet_indent_listview);
		bet_indent_listview.setVerticalScrollBarEnabled(false);// 不显示滚动条
		bet_indent_listview.setCacheColorHint(0);
		bet_indent_listview.setDrawingCacheEnabled(false);

		oneselfchoose_btn = (Button) findViewById(R.id.oneselfchoose_btn);
		set_group_btn = (Button) findViewById(R.id.set_group_btn);
		set_group_btn.setTextColor(MyApp.res.getColor(R.color.red));
		clearchoose_btn = (Button) findViewById(R.id.clearchoose_btn);
		group_set_layout = (LinearLayout) findViewById(R.id.group_set_layout);

		no_selection_tv = (TextView) findViewById(R.id.no_selection_tv);
		agreeEntrust_checkBox = (CheckBox) findViewById(R.id.zwl_agreeEntrust_checkBox);
		multiple_number_ed = (EditText) findViewById(R.id.multiple_number_ed);

		show_bet_result_tv = (TextView) findViewById(R.id.show_bet_result_tv);// TextView
																				// 显示投注总数
																				// 和投注总金额
		payment_btn = (Button) findViewById(R.id.payment_btn);
		// 奖金优化　默认是GONE初始化默认界面数据
		bonus_optimize_btn = (Button) findViewById(R.id.bonus_optimize_btn);
		multiple_number_ed.setText(String.valueOf(MyApp.order.getFold()));// 设置
		mHandler.sendEmptyMessage(2);
	}

	/**
	 * 设置通知消息 选票信息描述
	 * 
	 * @return
	 */
	private void setNotice() {
		AllPOUR = MyApp.order.getAmount();

		if (choose != 1) {
			if (childId.equals("2")) {
				MyApp.order.getPassTypeList().clear();// 重新给竞彩ticket 设置过关方式
														// 串关专用，childId是1的单关就不用了
				if (mSetTypeGV[0] != null) {
					Iterator<Integer> iterator = mSetTypeGV[0].getSelectedBalls().iterator();
					if (iterator == null)
						return;
					while (iterator.hasNext()) {
						Integer next = iterator.next();
						String temp = next.toString().substring(0, 1);

						int size = 0;
						for (int i = 0; i < MyApp.order.getTickets().size(); i++) {
							size += MyApp.order.getTickets().get(i).getSelectMessageBean().size();
						}

						if (!TextUtils.isEmpty(temp)) {
							if (size < Integer.parseInt(temp) - 1)
								if (mSetTypeGV[0].getSelectedBalls().contains(next)) {
									iterator.remove();
								}
						}
					}

					MyApp.order.setObunch(mSetTypeGV[0].getSelectedBallstoString());
					if (mSetTypeGV[0].getSelectedBalls().size() > 0) {
						for (int i = 0; i < mSetTypeGV[0].getSelectedBalls().size(); i++) {
							MyApp.order.getPassTypeList().add(mSetTypeGV[0].getSelectedBalls().get(i));
						}
					}
				}
			}
		}

		if (!StringUtils.isBlank(childId) && "2".equals(childId)) {
			if (MyApp.order.getPassTypeList() != null) {
				cgText = "";
				if (MyApp.order.getPassTypeList().size() <= 2) {
					for (int i = 0; i < MyApp.order.getPassTypeList().size(); i++) {
						if (0 != i) {
							cgText += "," + Getlotterykind.getcgText(MyApp.order.getPassTypeList().get(i));
						} else {
							cgText += Getlotterykind.getcgText(MyApp.order.getPassTypeList().get(i));
						}
					}
				} else if (MyApp.order.getPassTypeList().size() > 2) {
					int n = MyApp.order.getPassTypeList().get(0);
					cgText += Getlotterykind.getcgText(n);
					n = MyApp.order.getPassTypeList().get(1);
					cgText += "," + Getlotterykind.getcgText(n) + "...";
				}
				// System.out.println("210---------ob---> :"+MyApp.order.getObunch()+" |pt : "+MyApp.order.getPassTypeList()+"|"+cgText);
			}
			if (StringUtils.isBlank(cgText)) {
				cgText = "串关必选*";
				show(no_selection_tv);// 无串关提示
			} else {
				hide(no_selection_tv);
			}

			if (choose == 1) {
				if (!set_group_btn.getText().toString().equals("串关必选*")) {
					if ((Integer.parseInt((set_group_btn.getText().toString().substring(0, 1))) > MyApp.order.getTicketsMB().size() || Integer.parseInt((set_group_btn.getText().toString().substring(
							0, 1))) > MyApp.order.getTickets().get(0).getSelectMessageBean().size())) {
						cgText = "串关必选*";
					}
				} else {
					if (!cgText.equals("串关必选*"))
						if (Integer.parseInt(cgText.substring(0, 1)) > MyApp.order.getTicketsMB().size()
								|| Integer.parseInt(cgText.substring(0, 1)) > MyApp.order.getTickets().get(0).getSelectMessageBean().size())
							cgText = "串关必选*";
				}
			}

			set_group_btn.setText(cgText);
			set_group_btn.invalidate();
		}
		if ("串关必选*".equals(set_group_btn.getText().toString()))
			show_bet_result_tv.setText(Html.fromHtml(replaceNoticeText(MyApp.order.getFold(), 0, 0)));// 默认提示
		else
			show_bet_result_tv.setText(Html.fromHtml(replaceNoticeText(MyApp.order.getFold(), MyApp.order.getLotterysCount(), (AllPOUR / 100))));// 默认提示
	}

	/**
	 * 文字提示金额加色效果
	 * 
	 * @param n
	 * @param zhu
	 * @param money
	 * @return
	 */
	private String replaceNoticeText(long n, long zhu, long money) {
		return StringUtils.replaceEach(MyApp.res.getString(R.string.jcbetorder_notice_text),// 投N倍ZHU注<font
																							// color="#ff9500">MONEY元</font>
				new String[] { "N", "ZHU", "MONEY" }, new String[] { String.valueOf(n), String.valueOf(zhu), String.valueOf(money) });
	}

	private void onBack() {
		payment_btn.setEnabled(true);// 防止重复点击
		payment_btn.setText("付款");
		payment_btn.invalidate();
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
		}
		dialog = new TCDialogs(mActivity);
		dialog.popDeleteConfirm(new MyClickedListener() {
			@Override
			public void onClick() {
				dialog.dismiss();
			}
		}, new MyClickedListener() {
			@Override
			public void onClick() {
				MyApp.resetOrderBean();// 清空订单
				fragment.newTicket();// 选号界面 重建选票fragment.finish();
				dialog.dismiss();
				mActivity.finish();// 先跳转后清空
			}
		});
	}

	@Override
	protected void onLeftIconClicked() {// 顶部返回按钮的监听重写
		if (MyApp.order.getTicketsMB() != null && MyApp.order.getTicketsMB().size() > 0) {
			onBack();
		} else {
			MyApp.resetOrderBean();// 清空订单
			fragment.newTicket();// 选号界面 重建选票fragment.finish();
			mActivity.finish();// 先跳转后清空
		}
	}

	private void setLisnten() {
		// 手机返回键
		setMyBackPressedListener(new MyBackPressedListener() {
			@Override
			public void onMyBackPressed() {
				if (MyApp.order.getTickets().size() > 0) {
					onBack();
				} else {
					MyApp.resetOrderBean();// 清空订单
					fragment.newTicket();// 选号界面 重建选票fragment.finish();
					mActivity.finish();// 先跳转后清空
				}
			}
		});
		agreeEntrust_checkBox.setOnClickListener(this);
		oneselfchoose_btn.setOnClickListener(this);
		clearchoose_btn.setOnClickListener(this);
		payment_btn.setOnClickListener(this);
		bonus_optimize_btn.setOnClickListener(this);
		// multiple_number_ed.setOnClickListener(this);

		// 注册传感器 监听
		shakeListener = new ShakeListener(mActivity);
		shakeListener.setOnShakeListener(new OnShakeListener() {
			@Override
			public void onShake() {
			}
		});
		shakeListener.start();
		shakeListener.setEnable(false);// 默认开始不可以摇晃

		// 投注倍数 EditText OnTouchListener监听
		// multiple_number_ed.setOnTouchListener(this);
		// 投注倍数 EditText addTextChangedListener监听
		multiple_number_ed.addTextChangedListener(new TextWatcher() {
			String s;

			@Override
			public void onTextChanged(CharSequence chsq, int start, int before, int count) {
				s = chsq.toString().trim();
				if (StringUtils.isBlank(s)) {// 为空或"0"处理 例如 null, "", "  \n"
					// ...等
					// chsq="2";
					// s=chsq.toString().trim();
					// multiple_number_ed.setText(chsq);
					s = "1".trim();
					// MyToast.showToast(mActivity, "至少投注1倍!");
				}
				// else if(chsq.toString().equals("00")) {
				// chsq="10";
				// s=chsq.toString().trim();
				// multiple_number_ed.setText(chsq);
				// }else
				// if(chsq.toString().equals("000")||chsq.toString().equals("0000"))
				// {
				// chsq="1000";
				// s=chsq.toString().trim();
				// multiple_number_ed.setText(chsq);
				// }
				if (chsq.toString().startsWith("0")) {
					MyToast.showToast(mActivity, "至少输入1");
					s = "1";
					multiple_number_ed.setText("1");
					multiple_number_ed.setSelection(multiple_number_ed.length());
				}
				MyApp.order.setFold(Integer.parseInt(s));
				setNotice();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable e) {
			}
		});

		if (!StringUtils.isBlank(childId) && "1".equals(childId)) {
			// bonus_optimize_btn.setVisibility(View.INVISIBLE);
			hide(no_selection_tv);// 关闭串关选择提示
			set_group_btn.setText("单关");
			MyApp.order.setObunch("02");
			MyApp.order.getPassTypeList().clear();// 重新给竞彩ticket 设置过关方式
			MyApp.order.getPassTypeList().add(2);
			set_group_btn.setCompoundDrawables(null, null, null, null);
		} else {
			initSelect_PoP();
			set_group_btn.setOnClickListener(this);
			pop_multiple_number_ed.addTextChangedListener(new TextWatcher() {
				String s;

				@Override
				public void onTextChanged(CharSequence chsq, int start, int before, int count) {
					s = pop_multiple_number_ed.getText().toString().trim();
					if (StringUtils.isBlank(s)) {// 为空或"0"处理 例如 null, "", "  \n"
						// ...等
						// chsq="2";
						// s=chsq.toString().trim();
						// multiple_number_ed.setText(chsq);
						s = "1".trim();
						// MyToast.showToast(mActivity, "至少投注1倍!");
					}
					// else if(chsq.toString().equals("00")) {
					// chsq="10";
					// s=chsq.toString().trim();
					// multiple_number_ed.setText(chsq);
					// }else
					// if(chsq.toString().equals("000")||chsq.toString().equals("0000"))
					// {
					// chsq="1000";
					// s=chsq.toString().trim();
					// multiple_number_ed.setText(chsq);
					// }
					if (chsq.toString().startsWith("0")) {
						MyToast.showToast(mActivity, "至少输入1");
						s = "1";
						pop_multiple_number_ed.setText("1");
						pop_multiple_number_ed.setSelection(pop_multiple_number_ed.length());
					}
					MyApp.order.setFold(Integer.parseInt(s));
					setNotice();
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				}

				@Override
				public void afterTextChanged(Editable e) {
				}
			});
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pop_multiple_number_ed:
			// pop_multiple_number_ed.setText("");
			break;
		case R.id.multiple_number_ed:
			// multiple_number_ed.setText("");
			break;
		// 编辑 / 添加赛事
		case R.id.oneselfchoose_btn:
			fragment.customTicket();
			finish();
			break;
		// 竞彩设置组 展开按钮
		case R.id.set_group_btn:
			setNotice();
			showpop();
			break;
		// 收起按钮
		case R.id.set_group_btn1:
			if (pop_groupSelect_msg != null && pop_groupSelect_msg.isShowing()) {
				pop_groupSelect_msg.dismiss();
				group_set_layout.setVisibility(View.VISIBLE);
			}
			multiple_number_ed.setText(String.valueOf(MyApp.order.getFold()));
			multiple_number_ed.invalidate();// View 刷新
			set_group_btn.setText(cgText);
			set_group_btn.setTextColor(MyApp.res.getColor(R.color.red));
			setNotice();
			adapter.clearDanList();
			break;
		// 过关方式说明
		case R.id.explain_id:
			Bundle bundle = new Bundle();
			bundle.putString("title", "过关方式说明");
			bundle.putString("url", "file:///android_asset/help/passway.htm");
			start(ThirdActivity.class, TCWebFragment.class, bundle);
			break;
		// 清空列表
		case R.id.clearchoose_btn:
			mHandler.sendEmptyMessage(1);// 刷新适配器
			break;
		// 付款
		case R.id.payment_btn:
			gotopay();
			break;
		// 我同意委托
		case R.id.zwl_agreeEntrust_checkBox:
			Bundle mBundle = new Bundle();
			mBundle.putString("title", "委托投注规则");
			// http://news.mitenotc.com/a/wttzgz.html TCWebFragment
			mBundle.putString("url", "http://news.mitenotc.com/a/wttzgz.html");
			start(ThirdActivity.class, TCWebFragment.class, mBundle);
			break;
		// 奖金优化
		case R.id.bonus_optimize_btn:
			setNotice();
			bonusOptimize();
			break;
		}
	}

	private void showpop() {
		if (pop_groupSelect_msg != null && pop_groupSelect_msg.isShowing()) {// 防止重复弹出
			pop_groupSelect_msg.dismiss();
			group_set_layout.setVisibility(View.VISIBLE);
		}
		showSetPopView(pop_groupSelect_msg.getContentView());
		mSetTypeGV[0].getSelectedBalls().clear();
		mSetTypeGV[1].getSelectedBalls().clear();
		for (int i = 0; i < MyApp.order.getPassTypeList().size(); i++) {
			int s = MyApp.order.getPassTypeList().get(i);
			if (s <= 9) {
//			if (s <= 108) {   <=9是篮彩  <=108是足彩
				mSetTypeGV[0].getSelectedBalls().add(s);
			} else {
				mSetTypeGV[1].getSelectedBalls().add(s);
			}
		}
		// 指定显示位置
		// pop_groupSelect_msg.showAtLocation(payment_btn, Gravity.NO_GRAVITY,
		// pop_groupSelect_msg.getWidth(), -pop_groupSelect_msg.getHeight());
		// 计算x轴方向的偏移量，使得PopupWindow在Title的正下方显示，此处的单位是pixels
		// int xoffInPixels = AppUtil.getDisplayHeight(mActivity)/2 -
		// payment_btn.getHeight()/2;
		// // 将pixels转为dip
		int xoffInDip = DensityUtil.px2dip(mActivity, 74);
		pop_groupSelect_msg.showAsDropDown(payment_btn, 0, xoffInDip);
		group_set_layout.setVisibility(View.INVISIBLE);
		pop_multiple_number_ed.setText(String.valueOf(MyApp.order.getFold()));
		mSetTypeGV[0].notifyDataSetChanged();
		mSetTypeGV[1].notifyDataSetChanged();
		pop_multiple_number_ed.invalidate();// View 刷新
		pop_groupSelect_msg.update();
	}

	/**
	 * 优化奖金
	 */
	private void bonusOptimize() {
		if (isFastClick(mActivity))
			return;// 防止重复点击

		if (MyApp.order.getAmount() <= 0) {
			MyToast.showToast(mActivity, "暂无选票,或串关方式与选票与选票不符!");
			return;
		}
		if (MyApp.order.getPassTypeList() == null) {// 串关检查
			MyToast.showToast(mActivity, "请选择串关方式!");
			showpop();
			return;
		} else {
			// 不支持M串N
			for (int i = 0; i < MyApp.order.getPassTypeList().size(); i++) {
				if (MyApp.order.getPassTypeList().get(i) > 108) {// 非M串1 方式
					MyToast.showToast(mActivity, "不支持M串N,请选择M串1的投注方式!");
					return;
				}
			}
			// 不支持设胆
			for (int i = 0; i < MyApp.order.getTickets().size(); i++) {
				for (int j = 0; j < MyApp.order.getTickets().get(i).getSelectMessageBean().size(); j++) {
					MessageBean mb = MyApp.order.getTickets().get(i).getSelectMessageBean().get(j);

					if (mb.isISD()) {
						MyToast.showToast(mActivity, "不支持胆拖,请付款购买吧!亲");
						return;
					}
				}
			}
		}
		start(ThirdActivity.class, BonusOptimize.class, null);
		finish();
	}

	/**
	 * 下单
	 */
	private void gotopay() {
		if (isFastClick(mActivity))
			return;// 防止重复点击
		if (!UserBean.getInstance().isLogin())// 检测登录
		{
			startLoginForResult();
			return;
		}
		if (MyApp.order.getTicketsMB() != null && MyApp.order.getTicketsMB().size() == 0) {// 判断订单
			MyToast.showToast(mActivity, "您还没有订单！");
			if (mDialog != null && mDialog.isShowing()) {
				mDialog.dismiss();
			}
			payment_btn.setEnabled(true);// 防止重复点击
			payment_btn.setText("付款");
			payment_btn.invalidate();
			return;
		} else {
			for (int i = 0; i < MyApp.order.getTickets().size(); i++) {
				if (MyApp.order.getTickets().get(i).getTicketAmount() > 2000000) {// 单注金额不能大于两万
					MyToast.showToast(mActivity, "单票金额不能超过2万元,请理性购彩!");
					return;
				}
			}
		}
		if (StringUtils.isBlank(MyApp.order.getObunch())) {// 判断串关方式
			MyToast.showToast(mActivity, "请选择串关方式!");
			showpop();
			return;
		}
		if ("串关必选*".equals(set_group_btn.getText().toString())) {
			MyToast.showToast(mActivity, "您需要选择串关方式!");
			return;
		}

		for (int i = 0; i < MyApp.order.getTicketsMB().size(); i++) {
			MessageBean itemMB = MyApp.order.getTicketsMB().get(i);
			if (!FormatUtil.salesThanTimer(itemMB.getJ())) {
				MyToast.showToast(mActivity, "本场比赛投注时间已结束!");
				return;
			}
		}
		submitData(4001, 4001, MyApp.order.getJCOrderJson());

	}

	/**
	 * 登陆返回之后 回调 去付款的方法
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1002 && UserBean.getInstance().isLogin()) {// 登录成功才会再次去请求
			gotopay();
		}
	}

	class JCBetListAdapter extends BaseAdapter {
		MessageBean mB;// 单注
		List<String> tempList;
		LayoutInflater mInflater;
		List<String> danList = null;// 是否可以继续增设胆拖
		Context ctx;

		public JCBetListAdapter(Context ctx) {
			super();
			this.ctx = ctx;
			mInflater = LayoutInflater.from(ctx);
			danList = new LinkedList<String>();
		}

		// 清空胆拖设置
		public void clearDanList() {
			if (danList != null) {
				MyApp.order.clearTicketsDan();
				danList.clear();
				notifyDataSetChanged();
			}
		}

		@Override
		public int getCount() {
			int n = 0;
			for (int i = 0; i < MyApp.order.getTickets().size(); i++) {
				// n +=
				// MyApp.order.getTickets().get(i).getSelectMessageBean().size();
				n += MyApp.order.getTicketsMB().size();
			}
			return n;
		}

		@Override
		public Object getItem(int position) {
			return MyApp.order.getMessageBean(position);
		}

		@Override
		public long getItemId(int id) {
			return id;
		}

		@Override
		@SuppressLint("NewApi")
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder vh = null;
			MessageBean t = null;
			String a = "", b = "", c = "", k = "", m = "";
			mB = MyApp.order.getMessageBean(position);
			tempList = MyApp.order.getTagList(position);
			t = mB.getT();// sp 值"T":{"A":"1.38","B":"4.15","C":"6.30"}
			a = t.getA();
			b = t.getB();
			c = t.getC();
			k = mB.getK();
			m = mB.getM();
			if ("219".contains(MyApp.order.getLotteryId())) {// 篮彩混关
				if (convertView == null) {
					convertView = mInflater.inflate(R.layout.jc_lqbet_item, null);// 此View
																					// 缺少篮彩的订单布局,开发时候需要在xml中添加篮彩布局
					vh = new ViewHolder(convertView);
					convertView.setTag(vh);
				} else if (convertView != null) {
					TextViewGroup lan_item_chgp = (TextViewGroup) convertView.findViewById(R.id.lan_item_chgp);
					lan_item_chgp.setTagList(new ArrayList<String>());
				}
				vh = (ViewHolder) convertView.getTag();
//				if (!StringUtils.isBlank(k) && k.length() > 4) {
//					k = k.substring(0, 4);
//				}
//				if (!StringUtils.isBlank(m) && m.length() > 4) {
//					m = m.substring(0, 4);
//				}
				if (!StringUtils.isBlank(k)) {
					vh.zhu.setText(k);
				}
				if (!StringUtils.isBlank(m)) {
					vh.ke.setText(m);
				}
				String text = "";
				if (tempList != null && tempList.size() > 0) {
					for (int i = 0; i < tempList.size(); i++) {
						if (tempList.get(i).startsWith("214^3")) {
							if (i == 0) {
								text += "让分胜";
							} else {
								text += " ,让分胜";
							}
						} else if (tempList.get(i).startsWith("214^0")) {
							if (i == 0) {
								text += "让分负";
							} else {
								text += " ,让分负";
							}
						} else if (tempList.get(i).startsWith("216^3")) {
							if (i == 0) {
								text += "主胜";
							} else {
								text += " ,主胜";
							}
						} else if (tempList.get(i).startsWith("216^0")) {
							if (i == 0) {
								text += "主负";
							} else {
								text += " ,主负";
							}
						}
					}
				}
				if (!StringUtils.isBlank(text)) {// 这里不能排除 "" ""表示用户没有选择任何结果
					vh.select_text.setText(text);
				}
				show(vh.lan_text_layout);
				show(vh.select_text);
				hide(vh.lan_item_chgp);

				// 215 篮彩_大小分 在下一个判断else if之前截获(原因1 大2 小) tag 不同与其它几个子玩法
			} else if ("215".equals(MyApp.order.getLotteryId())) {
				if (convertView == null) {
					convertView = mInflater.inflate(R.layout.jc_lqbet_item, null);// 此View
																					// 缺少篮彩的订单布局,开发时候需要在xml中添加篮彩布局
					vh = new ViewHolder(convertView);
					convertView.setTag(vh);
				} else if (convertView != null) {
					TextViewGroup lan_item_chgp = (TextViewGroup) convertView.findViewById(R.id.lan_item_chgp);
					lan_item_chgp.setTagList(new ArrayList<String>());
				}
				vh = (ViewHolder) convertView.getTag();
				show(vh.lan_llayout);// 显示篮彩布局
				show(vh.lan_item_chgp);
				show(vh.lan_text_layout);
				hide(vh.select_text);
				vh.zhu.setText(k);
				vh.ke.setText(m);
				if(a!=null){
					vh.vs.setText(a);
					vh.vs.setTextColor(Color.parseColor("#000000"));
					vh.vs.setTextSize(13);
				}else{
					vh.vs.setText("- -");
				}
				vh.lan_cb0.setText(Html.fromHtml(JCPL321.getPKName(false, "aabbcc", "(客)大分", c)));
				vh.lan_cb3.setText(Html.fromHtml(JCPL321.getPKName(false, "aabbcc", "(主)小分", b)));
				vh.lan_cb3.setTag("2");
				vh.lan_cb0.setTag("1");
			} else if ("216".equals(MyApp.order.getLotteryId())) {// 篮彩非混关
				if (convertView == null) {
					convertView = mInflater.inflate(R.layout.jc_lqbet_item, null);// 此View
																					// 缺少篮彩的订单布局,开发时候需要在xml中添加篮彩布局
					vh = new ViewHolder(convertView);
					convertView.setTag(vh);
				} else if (convertView != null) {
					TextViewGroup lan_item_chgp = (TextViewGroup) convertView.findViewById(R.id.lan_item_chgp);
					lan_item_chgp.setTagList(new ArrayList<String>());
				}
				vh = (ViewHolder) convertView.getTag();
				show(vh.lan_llayout);// 显示篮彩布局
				show(vh.lan_item_chgp);
				hide(vh.lan_text_layout);
				hide(vh.select_text);
				vh.lan_cb0.setText(Html.fromHtml(JCPL321.getPKName(false, m, "主负", b)));
				vh.lan_cb3.setText(Html.fromHtml(JCPL321.getPKName(false, k, "主胜", a)));
				vh.lan_cb3.setTag("3");
				vh.lan_cb0.setTag("0");
			}else if ("214".equals(MyApp.order.getLotteryId())) {// 篮彩非混关
				if (convertView == null) {
					convertView = mInflater.inflate(R.layout.jc_lqbet_item, null);// 此View
																					// 缺少篮彩的订单布局,开发时候需要在xml中添加篮彩布局
					vh = new ViewHolder(convertView);
					convertView.setTag(vh);
				} else if (convertView != null) {
					TextViewGroup lan_item_chgp = (TextViewGroup) convertView.findViewById(R.id.lan_item_chgp);
					lan_item_chgp.setTagList(new ArrayList<String>());
				}
				vh = (ViewHolder) convertView.getTag();
				show(vh.lan_llayout);// 显示篮彩布局
				show(vh.lan_item_chgp);
				hide(vh.lan_text_layout);
				hide(vh.select_text);
				if (!a.contains("-")){
					a = StringUtils.replaceEach(MyApp.res.getString(R.string.red_text), new String[] { "MSG" }, new String[] { "(+"+a+")" });
				}else{
					a = StringUtils.replaceEach(MyApp.res.getString(R.string.green_text), new String[] { "MSG" }, new String[] { "("+a+")" });
				}
				vh.lan_cb0.setText(Html.fromHtml(JCPL321.getPKName(false, m, "主负", c)));
				vh.lan_cb3.setText(Html.fromHtml(JCPL321.getPKName(false, k+a, "主胜", b)));
				vh.lan_cb3.setTag("3");
				vh.lan_cb0.setTag("0");
			}
			
			final List<TextView> cbList = vh.lan_item_chgp.getTvList();
			vh.select_text.setOnClickListener(new OnClickListener() {// 混关点击修改
						@Override
						public void onClick(View arg0) {
							if (mDialog != null && mDialog.isShowing()) {
								mDialog.dismiss();
								mDialog = null;
							}
							mDialog = new Dialog(ctx, R.style.dialog_theme);// 加载进度框
							mDialog.setContentView(initDialogview(position));
							mDialog.show();
						}
					});

			/*
			 * //修改 vh.item_chgp.setOnTextViewGroupListener(new
			 * OnTextViewGroup() {
			 * 
			 * @Override public void setOnTextViewGroup(View v, List<String>
			 * tagList,List<String> textList) { if(tagList.size()> 0){
			 * MyApp.order
			 * .updatetkMessageBean(0,position,tagList);//直接在OrderBean中修改ticket
			 * for (int i = 0; i < cbList.size(); i++) { if(-1 !=
			 * tagList.indexOf(cbList.get(i).getTag().toString())){
			 * cbList.get(i)
			 * .setBackgroundDrawable(MyApp.res.getDrawable(R.drawable
			 * .jc_cb_true));
			 * cbList.get(i).setTextColor(MyApp.res.getColor(R.color
			 * .red_chbx_text)); }else{
			 * cbList.get(i).setTextColor(MyApp.res.getColor
			 * (R.color.item_hall_tv_title_text_color));
			 * cbList.get(i).setBackgroundDrawable
			 * (MyApp.res.getDrawable(R.drawable.jc_cb_false)); } } }else{
			 * if(-1!=danList.indexOf(String.valueOf(position))){//若有胆拖设置
			 * 必须移除该设置 否则影响金额计算 danList.remove(String.valueOf(position));
			 * MyApp.order
			 * .setMessageBeanInvariant(mB,false);//直接在OrderBean中修改ticket } }
			 * setNotice();//更新票面提示 notifyDataSetChanged();//不刷新 该场选中效果与最终选定结果相关
			 * shakeListener.vibrate(); } });
			 */

			vh.lan_item_chgp.setOnTextViewGroupListener(new OnTextViewGroup() {
				@Override
				public void setOnTextViewGroup(View v, List<String> tagList, List<String> textList) {
					MyApp.order.updatetkMessageBean(1, position, tagList);// 直接在OrderBean中修改ticket

					if (tagList.size() > 0) {
						for (int i = 0; i < cbList.size(); i++) {
							if (-1 != tagList.indexOf(cbList.get(i).getTag().toString())) {
								cbList.get(i).setBackgroundDrawable(MyApp.res.getDrawable(R.drawable.jc_cb_true));
								cbList.get(i).setTextColor(MyApp.res.getColor(R.color.red_chbx_text));
							} else {
								cbList.get(i).setTextColor(MyApp.res.getColor(R.color.item_hall_tv_title_text_color));
								cbList.get(i).setBackgroundDrawable(MyApp.res.getDrawable(R.drawable.jc_cb_false));
							}
						}
					} else {
						if (-1 != danList.indexOf(String.valueOf(position))) {// 若有胆拖设置
																				// 必须移除该设置
																				// 否则影响金额计算
							danList.remove(String.valueOf(position));
							// MyApp.order.setMessageBeanInvariant(mB,false);//直接在OrderBean中修改ticket
							MyApp.order.setMessageBeanInvariant(position, false);// 直接在OrderBean中修改ticket
						}
					}

					if (danList.size() > 0) {
						String cgTextTemp = Getlotterykind.getcgText(MyApp.order.getPassTypeList().get(MyApp.order.getPassTypeList().size() - 1));
						if (!"串关必选*".equals(set_group_btn.getText().toString()) && !"单关".equals(set_group_btn.getText().toString())) {
							if (MyApp.order.getTickets().get(0).getSelectMessageBean().size() == Integer.parseInt(cgTextTemp.substring(0, 1))) {
								danList.clear();
								MyApp.order.clearTicketsDan();
							}
						}
					}

					setNotice();// 更新票面提示
					notifyDataSetChanged();// 不刷新 该场选中效果与最终选定结果相关
					shakeListener.vibrate();
				}
			});
			// 非混关设置效果
			if (tempList != null && cbList != null) {// 设置选中效果
				// System.out.println("210--------------tempList :"+tempList.toString());
				for (int i = 0; i < cbList.size(); i++) {
					if (-1 != tempList.indexOf(cbList.get(i).getTag().toString())) {
						cbList.get(i).setBackgroundDrawable(MyApp.res.getDrawable(R.drawable.jc_cb_true));
						cbList.get(i).setTextColor(MyApp.res.getColor(R.color.red_chbx_text));
					} else {
						cbList.get(i).setTextColor(MyApp.res.getColor(R.color.item_hall_tv_title_text_color));
						cbList.get(i).setBackgroundDrawable(MyApp.res.getDrawable(R.drawable.jc_cb_false));
					}
				}
				vh.lan_item_chgp.setTagList(tempList);
			}

			vh.delete_btn.setOnClickListener(new OnClickListener() {// item删除
						@Override
						public void onClick(View v) {// 删除
							if (-1 != danList.indexOf(String.valueOf(position))) {
								danList.remove(String.valueOf(position));
							}

							MyApp.order.getTagList(position).clear();
							MyApp.order.removeMessageBean(position); // 直接在OrderBean中修改ticket

							if (danList.size() > 0) {
								String cgTextTemp = Getlotterykind.getcgText(MyApp.order.getPassTypeList().get(MyApp.order.getPassTypeList().size() - 1));
								if (!"串关必选*".equals(set_group_btn.getText().toString()) && !"单关".equals(set_group_btn.getText().toString())) {
									if (MyApp.order.getTickets().get(0).getSelectMessageBean().size() == Integer.parseInt(cgTextTemp.substring(0, 1))) {
										danList.clear();
										MyApp.order.clearTicketsDan();
									}
								}
							}

							setNotice();// 更新票面提示
							shakeListener.vibrate();
							notifyDataSetChanged();
						}
					});
			vh.dele_layout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (-1 != danList.indexOf(String.valueOf(position))) {
						danList.remove(String.valueOf(position));
					}

					MyApp.order.getTagList(position).clear();
					MyApp.order.removeMessageBean(position); // 直接在OrderBean中修改ticket

					if (danList.size() > 0) {
						String cgTextTemp = Getlotterykind.getcgText(MyApp.order.getPassTypeList().get(MyApp.order.getPassTypeList().size() - 1));
						if (!"串关必选*".equals(set_group_btn.getText().toString()) && !"单关".equals(set_group_btn.getText().toString())) {
							if (MyApp.order.getTickets().get(0).getSelectMessageBean().size() == Integer.parseInt(cgTextTemp.substring(0, 1))) {
								danList.clear();
								MyApp.order.clearTicketsDan();
							}
						}
					}

					setNotice();// 更新票面提示
					shakeListener.vibrate();
					notifyDataSetChanged();
				}
			});

			vh.invariant_tv.setOnClickListener(new OnClickListener() {// item胆拖
						@Override
						public void onClick(View arg0) {
							if (-1 == danList.indexOf(String.valueOf(position))) {

								if (!"串关必选*".equals(set_group_btn.getText().toString())) {
									String cgTextTemp = Getlotterykind.getcgText(MyApp.order.getPassTypeList().get(MyApp.order.getPassTypeList().size() - 1));
									if (MyApp.order.getTickets().get(0).getSelectMessageBean().size() == Integer.parseInt(cgTextTemp.substring(0, 1))) {
										MyToast.showToast(mActivity, "您选中的场关数目不够继续设胆!");
										return;
									}
								} else {
									MyToast.showToast(mActivity, "您需要选择串关方式之后才能设胆!");
									return;
								}
								// 不让选胆的时候选择 没有选中的ticket
								int totalMessageBean = 0;
								for (int i = 0; i < MyApp.order.getTickets().size(); i++) {
									totalMessageBean += MyApp.order.getTickets().get(i).getSelectMessageBean().size();
								}
								if (totalMessageBean > 0) {
									boolean flag = false;
									for (int i = 0; i < MyApp.order.getTickets().size(); i++) {
										if (MyApp.order.getTickets().get(i).getSelectMessageBean().contains(MyApp.order.getTicketsMB().get(position)))
											flag = true;
									}
									if (!flag) {
										MyToast.showToast(mActivity, "您不能给没选中的场关设胆!");
										return;
									}
								}

								danList.add(String.valueOf(position));
								// MyApp.order.setMessageBeanInvariant(mB,true);//直接在OrderBean中修改ticket

								// 不让选胆的时候选择 danList的size 超过几串一中的最大数目
								if (!cgText.equals("串关必选*")) {
									int minChuanYi = Integer.parseInt(cgText.trim().substring(0, 1));// 取多串一的第一个
									if (danList.size() > minChuanYi - 1) {
										danList.remove(String.valueOf(position));
										MyToast.showToast(mActivity, "您选择的串关方式不支持继续设胆!");
										return;
									}
								}

								MyApp.order.setMessageBeanInvariant(position, true);// 直接在OrderBean中修改ticket

								if (MyApp.order.getPassTypeList() != null && MyApp.order.getPassTypeList().size() > 0) {// 检测是否可以设置胆拖
									int mcn = MyApp.order.getPassTypeList().get(MyApp.order.getPassTypeList().size() - 1);// 最大M串1
									// 设胆数目必须小于串关数选票场次之差( 例如 danList.size() <=
									// (getCount()-(102-100))) 反之移除该设胆
									if ((((mcn - 1)) < 8) && (mcn - 1) < danList.size()) {// M串N
										if (-1 != danList.indexOf(String.valueOf(position))) {// 是否支持继续设置胆拖
											danList.remove(String.valueOf(position));
										}
										// MyApp.order.setMessageBeanInvariant(mB,false);//直接在OrderBean中修改ticket
										MyApp.order.setMessageBeanInvariant(position, false);// 直接在OrderBean中修改ticket
										MyToast.showToast(mActivity, "您选择的串关方式不支持继续设胆!");
									} else if (Getlotterykind.getCG(mcn).length > 0) {// M串N
										mcn = Getlotterykind.getCG(mcn)[Getlotterykind.getCG(mcn).length - 1];
										if ((mcn - 1) < 8 && (mcn - 1) < danList.size()) {
											if (-1 != danList.indexOf(String.valueOf(position))) {// 是否支持继续设置胆拖
												danList.remove(String.valueOf(position));
											}
											// MyApp.order.setMessageBeanInvariant(mB,false);//直接在OrderBean中修改ticket
											MyApp.order.setMessageBeanInvariant(position, false);// 直接在OrderBean中修改ticket
											MyToast.showToast(mActivity, "您选择的串关方式不支持继续设胆!");
										}
									}
								}
							} else {
								// MyApp.order.setMessageBeanInvariant(mB,false);//直接在OrderBean中修改ticket
								MyApp.order.setMessageBeanInvariant(position, false);// 直接在OrderBean中修改ticket
								danList.remove(String.valueOf(position));
							}
							// System.out.println("210--danList-->"+danList.toString());
							notifyDataSetChanged();
							setNotice();// 更新票面提示
							shakeListener.vibrate();
						}
					});
			// 胆拖 默认状态(非选中 但可选)
			vh.invariant_tv.setBackgroundDrawable(MyApp.res.getDrawable(R.drawable.set_dan_bg_false));// set_dan_bg_untouch
																										// set_dan_bg_false
			vh.invariant_tv.setTextColor(MyApp.res.getColor(R.color.login_color_1));
			vh.invariant_tv.setEnabled(true);
			if (2 >= getCount() || (0 == danList.size() && isAllow())) {// 非选中
																		// 不能继续被选
				vh.invariant_tv.setEnabled(false);
				vh.invariant_tv.setBackgroundDrawable(MyApp.res.getDrawable(R.drawable.set_dan_bg_untouch));// 不可点击
				vh.invariant_tv.getBackground().setAlpha(50);// 0~255透明度值
				vh.invariant_tv.setTextColor(MyApp.res.getColor(R.color.award_info_tv_title_second_color));
			} else if (danList.size() > 0) {
				if (-1 != danList.indexOf(String.valueOf(position))) {// 选中
					vh.invariant_tv.setBackgroundDrawable(MyApp.res.getDrawable(R.drawable.set_dan_bg_true));
					vh.invariant_tv.setTextColor(MyApp.res.getColor(R.color.white));
				} else {// 非选中
					if (MyApp.order.getPassTypeList() != null && MyApp.order.getPassTypeList().size() > 0) {
						int mcn = MyApp.order.getPassTypeList().get(MyApp.order.getPassTypeList().size() - 1);// 最大M串1
						if ((mcn - 2) < 8 && (mcn - 2) == danList.size()) {// 非选中
																			// 不能继续被选
							vh.invariant_tv.setEnabled(false);
							vh.invariant_tv.setBackgroundDrawable(MyApp.res.getDrawable(R.drawable.set_dan_bg_untouch));// 不可点击
							vh.invariant_tv.getBackground().setAlpha(50);// 0~255透明度值
							vh.invariant_tv.setTextColor(MyApp.res.getColor(R.color.award_info_tv_title_second_color));
							if (-1 != danList.indexOf(String.valueOf(position))) {// 移除选中
								danList.remove(String.valueOf(position));
								// MyApp.order.setMessageBeanInvariant(mB,false);//直接在OrderBean中修改ticket
								MyApp.order.setMessageBeanInvariant(position, false);// 直接在OrderBean中修改ticket
							}
						} else if (Getlotterykind.getCG(mcn).length > 0) {
							mcn = Getlotterykind.getCG(mcn)[Getlotterykind.getCG(mcn).length - 1];// 最大M串N
							if ((mcn - 2) < 8 && (mcn - 2) == danList.size()) {
								vh.invariant_tv.setEnabled(false);
								vh.invariant_tv.setBackgroundDrawable(MyApp.res.getDrawable(R.drawable.set_dan_bg_untouch));// 不可点击
								vh.invariant_tv.getBackground().setAlpha(50);// 0~255透明度值
								vh.invariant_tv.setTextColor(MyApp.res.getColor(R.color.award_info_tv_title_second_color));
								if (-1 != danList.indexOf(String.valueOf(position))) {// 移除选中
									danList.remove(String.valueOf(position));
									// MyApp.order.setMessageBeanInvariant(mB,false);//直接在OrderBean中修改ticket
									MyApp.order.setMessageBeanInvariant(position, false);// 直接在OrderBean中修改ticket
								}
							}
						}
					}
				}
			}

			if (choose == 1) { // 这里少一个判断 判断切出来的可以别转换成字符串
				vh.invariant_tv.setEnabled(false);
				vh.invariant_tv.setBackgroundDrawable(MyApp.res.getDrawable(R.drawable.set_dan_bg_untouch));// 不可点击
				vh.invariant_tv.getBackground().setAlpha(50);// 0~255透明度值
				vh.invariant_tv.setTextColor(MyApp.res.getColor(R.color.award_info_tv_title_second_color));
			}
			return convertView;
		}

		/**
		 * 混关 修改dialog view
		 * 
		 * @param position
		 * @return
		 */
		private View initDialogview(final int position) {
			View v = View.inflate(ctx, R.layout.alter_jctc219_item, null);
			MessageBean mb = MyApp.order.getMessageBean(position);
			final List<String> tempList = MyApp.order.getTagList(position);
			MessageBean t = mb.getT();
			String k = mb.getK();
			String m = mb.getM();
			String L = mb.getL();
			if (!StringUtils.isBlank(k)) {// K 主队名称
				if (k.length() > 4) {
					k = k.substring(0, 4);
				}
				((TextView) v.findViewById(R.id.zhu)).setText(k);
			}
			if (!StringUtils.isBlank(m)) {// M 客队名称
				if (m.length() > 4) {
					m = m.substring(0, 4);
				}
				((TextView) v.findViewById(R.id.ke)).setText(m);
			}
			// 胜平负
			// B 非让球胜平负SP值，值之间以 逗号隔开
			String[] tempStr = null;
			if (!StringUtils.isBlank(t.getB().toString()) && t.getB().toString().contains(",")) {
				tempStr = t.getB().split(",");
			}
			if (tempStr != null && tempStr.length == 3) {
				if (!StringUtils.isBlank(tempStr[0])) {// A 胜SP值
					((TextView) v.findViewById(R.id.cb3)).setText(Html.fromHtml(JCPL310.getPKName("主胜", tempStr[0])));
					// ((TextView)
					// v.findViewById(R.id.cb3)).setTag("218^3_"+tempStr[0]);
					((TextView) v.findViewById(R.id.cb3)).setTag("216^3");
				}
				// if(!StringUtils.isBlank(tempStr[1])){//B 平SP值
				// ((TextView)
				// v.findViewById(R.id.cb1)).setText(Html.fromHtml(JCPL310.getPKName("平",tempStr[1])));
				// // ((TextView)
				// v.findViewById(R.id.cb1)).setTag("218^1_"+tempStr[1]);
				// }
				if (!StringUtils.isBlank(tempStr[2])) {// C 负SP值
					((TextView) v.findViewById(R.id.cb0)).setText(Html.fromHtml(JCPL310.getPKName("主负", tempStr[2])));
					// ((TextView)
					// v.findViewById(R.id.cb0)).setTag("218^0_"+tempStr[2]);
					((TextView) v.findViewById(R.id.cb0)).setTag("216^0");
				}
			}
			// 让球胜平负
			JCPL310.setTagTextViewmsg(false, (TextView) v.findViewById(R.id.r_tv), L);// 让球提示
			// A 让球胜平负SP值，值之间以 逗号隔开
			if (!StringUtils.isBlank(t.getA().toString()) && t.getA().toString().contains(",")) {
				tempStr = t.getA().split(",");// [1.37, 3.70, 8.10]
			}
			if (tempStr != null && tempStr.length == 3) {
				if (!StringUtils.isBlank(tempStr[0])) {// A 胜SP值
					((TextView) v.findViewById(R.id.r_ch3)).setText(Html.fromHtml(JCPL310.getPKName("主胜", tempStr[0])));
					// ((TextView)
					// v.findViewById(R.id.r_ch3)).setTag("210^3_"+tempStr[0]);
					((TextView) v.findViewById(R.id.r_ch3)).setTag("214^3");
				}
				// if(!StringUtils.isBlank(tempStr[1])){//B 平SP值
				// ((TextView)
				// v.findViewById(R.id.r_ch1)).setText(Html.fromHtml(JCPL310.getPKName("平",tempStr[1])));
				// }
				if (!StringUtils.isBlank(tempStr[2])) {// C 负SP值
					((TextView) v.findViewById(R.id.r_ch0)).setText(Html.fromHtml(JCPL310.getPKName("主负", tempStr[2])));
					((TextView) v.findViewById(R.id.r_ch0)).setTag("214^0");
				}
			}
			final TextViewGroup dialog_item_chgp = (TextViewGroup) v.findViewById(R.id.dialog_item_chgp);
			if (tempList != null) {
				List<String> tList = new ArrayList<String>();
				for (int i = 0; i < tempList.size(); i++) {
					tList.add(tempList.get(i));
				}
				upGPTVbackground(dialog_item_chgp, tList);
			}
			dialog_item_chgp.setOnTextViewGroupListener(new OnTextViewGroup() {
				@Override
				public void setOnTextViewGroup(View v, List<String> tagList, List<String> textList) {
					// System.out.println("210--d-->"+tagList.toString());
					upGPTVbackground(dialog_item_chgp, tagList);
				}
			});
			// // //取消
			v.findViewById(R.id.cancel_btn).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// List<String> tempList = MyApp.order.getTagList(position);
					// System.out.println("210--0-->"+tempList.toString());
					// upGPTVbackground(dialog_item_chgp,tempList);
					dialog_item_chgp.setTagList(new ArrayList<String>());
					setNotice();// 更新票面提示
					// System.out.println("210-----0------cancel_btn "+MyApp.order.getTagList(position).toString());
					if (mDialog != null && mDialog.isShowing()) {
						mDialog.dismiss();
					}
					notifyDataSetChanged();
				}
			});
			// 确认
			v.findViewById(R.id.affirm_btn).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					List<String> tlist = dialog_item_chgp.getTagList();
					MyApp.order.updatetkMessageBean(1, position, tlist);// 直接在OrderBean中修改ticket
					setNotice();// 更新票面提示
					if (mDialog != null && mDialog.isShowing()) {
						mDialog.dismiss();
					}
					// System.out.println("210-----------affirm_btn "+MyApp.order.getTagList(position).toString());
					notifyDataSetChanged();
					shakeListener.vibrate();
				}
			});
			return v;
		}

		/**
		 * 重载
		 * 
		 * @param item_chgp
		 * @param item_chgp
		 */
		@SuppressWarnings("unused")
		private void upGPTVbackground(TextViewGroup item_chgp, List<String> tempList) {
			List<TextView> cbList = item_chgp.getTvList();
			if (tempList != null && tempList.size() > 0 && cbList != null) {// 设置选中效果
				for (int i = 0; i < cbList.size(); i++) {
					if (-1 != tempList.indexOf(cbList.get(i).getTag().toString())) {
						cbList.get(i).setBackgroundDrawable(MyApp.res.getDrawable(R.drawable.jc_cb_true));
						cbList.get(i).setTextColor(MyApp.res.getColor(R.color.red_chbx_text));
					} else {
						cbList.get(i).setTextColor(MyApp.res.getColor(R.color.item_hall_tv_title_text_color));
						cbList.get(i).setBackgroundDrawable(MyApp.res.getDrawable(R.drawable.jc_cb_false));
					}
				}
				item_chgp.setTagList(tempList);
			} else {
				for (int i = 0; i < cbList.size(); i++) {
					cbList.get(i).setBackgroundDrawable(MyApp.res.getDrawable(R.drawable.jc_cb_false));
					cbList.get(i).setTextColor(MyApp.res.getColor(R.color.item_hall_tv_title_text_color));
				}
				item_chgp.setTagList(new ArrayList<String>());
			}
		}

		/**
		 * false 允许 true 不允许
		 * 
		 * @return
		 */
		private boolean isAllow() {
			boolean b = false;
			if (MyApp.order.getPassTypeList() != null && MyApp.order.getPassTypeList().size() > 0) {
				int mcn = MyApp.order.getPassTypeList().get(MyApp.order.getPassTypeList().size() - 1);// 最大M串1
				if (mcn < 108) { // M串1
					// 设胆数目必须小于串关数选票场次之差( 例如 danList.size() <=
					// (getCount()-(102-100))) 反之移除该设胆
					if ((getCount() - (mcn - 1)) == danList.size()) {
						b = true;
					}
				} else if (Getlotterykind.getCG(mcn).length > 0) {// M串N
					mcn = Getlotterykind.getCG(mcn)[Getlotterykind.getCG(mcn).length - 1];
					if ((getCount() - (mcn - 1)) == danList.size()) {
						b = true;
					}
				}
			}
			return b;
		}

		// 复用
		class ViewHolder {
			TextViewGroup item_chgp, lan_item_chgp;
			LinearLayout dan_layout, dele_layout;
			LinearLayout lan_text_layout, lan_llayout;
			TextView lan_cb0, lan_cb3;
			TextView cb3, cb1, cb0;// 胜平负
			Button invariant_tv;// 胆
			TextView delete_btn;// 删除
			TextView zhu, ke, select_text,vs;// 主队 客队 投注信息

			public ViewHolder(View v) {
				invariant_tv = (Button) v.findViewById(R.id.invariant_tv);// 足彩
				delete_btn = (TextView) v.findViewById(R.id.delete_btn);
				item_chgp = (TextViewGroup) v.findViewById(R.id.item_chgp);
				cb3 = (TextView) v.findViewById(R.id.cb3);
				cb1 = (TextView) v.findViewById(R.id.cb1);
				cb0 = (TextView) v.findViewById(R.id.cb0);
				vs = (TextView) v.findViewById(R.id.tv_jclq_dxf);

				lan_item_chgp = (TextViewGroup) v.findViewById(R.id.lan_item_chgp);// 篮彩
				lan_cb0 = (TextView) v.findViewById(R.id.lan_cb0);
				lan_cb3 = (TextView) v.findViewById(R.id.lan_cb3);

				lan_text_layout = (LinearLayout) v.findViewById(R.id.lan_text_layout);
				lan_llayout = (LinearLayout) v.findViewById(R.id.lan_llayout);
				dele_layout = (LinearLayout) v.findViewById(R.id.dele_layout);
				dan_layout = (LinearLayout) v.findViewById(R.id.dan_layout);
				zhu = (TextView) v.findViewById(R.id.zhu);
				ke = (TextView) v.findViewById(R.id.ke);
				select_text = (TextView) v.findViewById(R.id.select_text);
			}
		}
	}

	/**
	 * 防止点击频繁点击 出现白屏
	 * 
	 * @param mActivity
	 * @return boolean
	 */
	private boolean isFastClick(Context mActivity) {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 1500) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

	@Override
	public void onStop() {
		super.onStop();
		if (mReceiver != null) {
			mActivity.unregisterReceiver(mReceiver);
			mReceiver = null;
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (shakeListener != null) {
			shakeListener.setEnable(false);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		shakeListener.start();
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}

		setPayment(true, mDialog);
	}

	/**
	 * 修改付款按钮状态
	 * 
	 * @param isEnabled
	 * @param mDialog
	 */
	private void setPayment(boolean isEnabled, Dialog mDialog) {
		if (isEnabled && payment_btn != null) {
			payment_btn.setEnabled(true);// 付款时候 用户 按手机的返回键
			payment_btn.setText("付款");
		}
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		setFragmentCacheEnable(false);
	}
}
