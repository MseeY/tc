package com.mitenotc.ui.play;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode.VmPolicy;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.tc.ThirdActivity;
import com.mitenotc.bean.JcTeamInfo;
import com.mitenotc.bean.MessageBean;
import com.mitenotc.ui.adapter.JCListAdapter;
import com.mitenotc.ui.base.BaseActivity.MyBackPressedListener;
import com.mitenotc.ui.buy.BaseBuyAnnotation;
import com.mitenotc.ui.buy.JcBaseFragment;
import com.mitenotc.ui.ui_utils.CheckboxGroup;
import com.mitenotc.ui.ui_utils.JCListView;
import com.mitenotc.ui.ui_utils.MyToast;
import com.mitenotc.ui.ui_utils.ScreenDialog;
import com.mitenotc.ui.ui_utils.TextViewGroup;
import com.mitenotc.ui.ui_utils.TextViewGroup.OnTextViewGroup;
import com.mitenotc.utils.AppUtil;
import com.mitenotc.utils.FormatUtil;

/**
 * 竞彩篮球 321
 * 
 * @author mitenotc
 * 
 */
@BaseBuyAnnotation(lotteryId = "321", salesType = "216", childId = "2")
public class JCPL321 extends JcBaseFragment {
	private long dialog_time = 0;
	protected List<List<MessageBean>> groupList = new ArrayList<List<MessageBean>>();
	private static ScreenDialog sDialog;// 赛事筛选
	private List<List<MessageBean>> gList;
	private List<MessageBean> tlist = null;
	private List<String> seelectNameList = null;
	private List<Integer> selecid = null;
	private int chid;
	// sp 值 和 name 筛选标志
	private boolean issp, isname;
	private float SP = 0;// 默认值
	public Handler zqHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if (sDialog != null && sDialog.isShowing()) {
					sDialog.dismiss();
				}
				if (sDialog == null) {
					sDialog = new ScreenDialog(mActivity,"");
				}
				SP = sDialog.getSp();
				selecid = sDialog.getSelectId();
				chid = sDialog.getSpCheckid();
				sDialog.addListener(gameNameMap, new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						sDialog.setSelectById(selecid, SP, chid);
						sDialog.dismiss();// 取消
					}
				}, new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						if (sDialog.getSelectListName().size() > 0) {
							SP = sDialog.getSp();
							seelectNameList = sDialog.getSelectListName();
							selectMessBean();
							if (gList.size() > 0) {
								mjcAdapter = new JCPL221ListAdapter(mActivity, gList, home_expandable_Lv);
								home_expandable_Lv.setAdapter(mjcAdapter);// 必须重新设置适配器
																			// 以保证UI有明显的刷新效果
								initTicket();
								setNotice();
								mjcAdapter.notifyList();
							} else {
								MyToast.showToast(mActivity, "没有符合筛选条件的场次,请重新筛选 !");
							}
							sDialog.dismiss();
						} else {
							MyToast.showToast(mActivity, "赛事筛选不能为空,请重新筛选!");
						}
					}
				});
				break;
			}
		};
	};

	private void selectMessBean() {
		gList = new ArrayList<List<MessageBean>>();// 保证在不需要多次网络请求的情况下 在已有数据集合中
													// 可以按照用户的条件筛选
		for (List<MessageBean> list : groupList) {
			if (list.size() > 0) {
				tlist = new ArrayList<MessageBean>();
				for (int i = 0; i < list.size(); i++) {
					MessageBean m = list.get(i).getT();
					issp = false;
					isname = false;
					if ((!StringUtils.isBlank(m.getA()) && !StringUtils.isBlank(m.getB())) && SP > 0) {// SP值筛选
						if ((m.getB().contains(",") && m.getA().contains(",")) && (m.getB().split(",").length > 0 && m.getA().split(",").length > 0)) {
							// 1.5以下
							if ((1.5 == SP) && (Float.parseFloat(m.getB().split(",")[0]) > SP) && (Float.parseFloat(m.getA().split(",")[0]) > SP)) {
								issp = true;
							}
							// 2.0以上
							if ((2.0 == SP) && (Float.parseFloat(m.getB().split(",")[0]) < SP) && (Float.parseFloat(m.getA().split(",")[0]) < SP)) {
								issp = true;
							}
						} else {
							float s = Float.parseFloat(m.getB());
							if ((1.5 == SP) && (s < SP)) {// 1.5以下
								issp = true;
							}
							if ((2.0 == SP) && (s > SP)) {// 2.0以上
								issp = true;
							}
						}
					} else {// 所有赔率
						issp = true;
					}
					if (seelectNameList != null && seelectNameList.size() > 0) {// 赛事名称筛选
						String h = list.get(i).getH();
						if (!StringUtils.isBlank(h)) {
							for (int j = 0; j < seelectNameList.size(); j++) {
								if (h.startsWith(seelectNameList.get(j))) {
									isname = true;
								}
							}
						}
					}
					if (issp && isname) {
						tlist.add(list.get(i));
					}
				}
				if (tlist.size() > 0) {
					gList.add(tlist);
				}
			}
		}
	}

	@Override
	protected View customContent_saleType() {
		return View.inflate(mActivity, R.layout.f_lq_pop_titile_rb, null);
	}

	@Override
	protected View customLotteryView() {
		return null;// View.inflate(mActivity, R.layout.jczq_main, null);
	}

	@Override
	protected void onSaleTypeChanged(String tag) {
		String s = salesType, c = childId;// 默认是竞彩过关 胜平负(默认取注解中的值)
		if (tag.contains(",")) {
			s = tag.split(",")[0];
			c = tag.split(",")[1];
		}
		// System.out.println("221------------s--> "+s+" c--> "+c);
		hide(home_expandable_Lv);
		if (mjcAdapter != null) {
			mjcAdapter.clear();
			// mjcAdapter.getGroupStatusMap().clear(); //清空组栏展开标记
			// mjcAdapter.getlotteryTag().clear(); //清空 所选场次
			// mjcAdapter.notifyList();
		}
		MessageJson msg = new MessageJson();
		mDialog = new Dialog(mActivity, R.style.dialog_theme);// 加载进度框
		mDialog.setContentView(R.layout.m_wait_dialog);
		mDialog.show();
		// sDialog.defaultConfiguration();//恢复默认筛选
		// I SP类型（guding：过关/fudong ：单关）

		switch (Integer.parseInt(s)) {
		case 216:// 胜负
			// if(sort_layout!=null){
			// show(sort_layout);
			// }
			setTitleNav(Integer.parseInt(lotteryId), "胜负", R.drawable.title_nav_back, R.drawable.title_nav_menu);
			// switch (Integer.parseInt(c)) {
			// case 2://串关
			// cg_btn.setChecked(true);
			msg.put("I", "guding");
			// break;
			// default://单关
			// dg_btn.setChecked(true);
			// msg.put("I", "fudong");
			// break;
			// }
			break;
		case 217:// 胜分差
			// if(sort_layout!=null){
			// show(sort_layout);
			// }
			setTitleNav(Integer.parseInt(lotteryId), "胜分差", R.drawable.title_nav_back, R.drawable.title_nav_menu);
			// switch (Integer.parseInt(c)) {
			// case 2://串关
			// cg_btn.setChecked(true);
			msg.put("I", "guding");
			// break;
			// default://单关
			// dg_btn.setChecked(true);
			// msg.put("I", "fudong");
			// break;
			// }
			break;
		case 214:// 让分胜负
			// if(sort_layout!=null){
			// show(sort_layout);
			// }
			setTitleNav(Integer.parseInt(lotteryId), "让分胜负", R.drawable.title_nav_back, R.drawable.title_nav_menu);
			// switch (Integer.parseInt(c)) {
			// case 2://串关
			// cg_btn.setChecked(true);
			msg.put("I", "guding");
			// break;
			// default://单关
			// dg_btn.setChecked(true);
			// msg.put("I", "fudong");
			// break;
			// }
			break;
		case 215:// 篮彩_大小分
			setTitleNav(Integer.parseInt(lotteryId), "大小分", R.drawable.title_nav_back, R.drawable.title_nav_menu);
			// switch (Integer.parseInt(c)) {
			// case 2://串关
			// cg_btn.setChecked(true);
			msg.put("I", "guding");
			// break;
			// default://单关
			// dg_btn.setChecked(true);
			// msg.put("I", "fudong");
			// break;
			// }
			break;
		case 219:// 混合过关
			// if(sort_layout!=null){
			// hide(sort_layout);
			// }
			setTitleNav(Integer.parseInt(lotteryId), "混合过关", R.drawable.title_nav_back, R.drawable.title_nav_menu);
			msg.put("I", "guding");
			break;

		}
		msg.put("A", Integer.parseInt(salesType));// salesType 218
		msg.put("F", "0");// F 售票状态 0：销售中 1：不可销售
		submitData(4002, 4002, msg);
		mActivity.showRightIcon2();
	}

	@Override
	protected void onRightIconClicked2() {
		if ((System.currentTimeMillis() - dialog_time) > 300) {// 重复点击
			zqHandler.sendEmptyMessage(1);
			dialog_time = System.currentTimeMillis();
		}
	}

	@Override
	protected void onMessageReceived(Message msg) {
		super.onMessageReceived(msg);
		MessageBean mBean = (MessageBean) msg.obj;
		switch (msg.arg1) {
		case 4002:// 查询竞彩对阵
			if (mBean.getA().equals("0")) {
				List<MessageBean> mList = mBean.getLIST();
				initJCAdapter(mList);// 初始化适配器 适配数据
				basebuy_tv_notice.setText(Html.fromHtml(ticket.getNotice()));
			}
			break;
		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		sDialog = null;
	}

	@Override
	protected void initJCAdapter(List<MessageBean> mList) {
		if (mList == null || mList.size() == 0) {
			if (mDialog != null && mDialog.isShowing()) {// 提示框
				mDialog.dismiss();
			}
			showNullsaishi();
			return;
		}
		hideNullsaishi();
		groupList.clear();// 清空已有的对阵信息
		List<Integer> pcList = new ArrayList<Integer>();
		for (int i = 0; i < mList.size(); i++) {
			if (!StringUtils.isBlank(mList.get(i).getC()) && -1 == pcList.indexOf(Integer.parseInt(mList.get(i).getC()))) {
				pcList.add(Integer.parseInt(mList.get(i).getC()));
			}
			MessageBean mb = mList.get(i);
			saveGanmeName(mb.getW(), mb.getH());// 赛事名称 用于筛选
		}
		Collections.sort(pcList, new Comparator<Integer>() {
			@Override
			public int compare(Integer a, Integer b) {
				return b.compareTo(a);
			}
		});// 排序
		List<MessageBean> temlist;
		for (int j = 0; j < pcList.size(); j++) {
			temlist = new ArrayList<MessageBean>();
			for (int i = 0; i < mList.size(); i++) {
				String pcStr = String.valueOf(pcList.get(j));
				if (!StringUtils.isBlank(mList.get(i).getC()) && pcStr.equals(mList.get(i).getC())) {
					temlist.add(mList.get(i));
				}
			}
			if (temlist.size() > 0) {
				groupList.add(0, temlist);
			}
		}
//		if (sDialog != null) {   //赛事筛选初始化
//			SP = sDialog.getSp();
//			seelectNameList = sDialog.getSelectListName();
//		}
//		if (seelectNameList != null && seelectNameList.size() > 0) {
//			selectMessBean();
//			if (gList.size() > 0) {
//				mjcAdapter = new JCPL221ListAdapter(mActivity, gList, home_expandable_Lv);
//			}
//		} else {
		mjcAdapter = new JCPL221ListAdapter(mActivity, groupList, home_expandable_Lv);
//		}
		home_expandable_Lv.setAdapter(mjcAdapter);// 必须重新设置适配器 以保证UI有明显的刷新效果
		show(home_expandable_Lv);
		mjcAdapter.notifyList();
	}

	/**
	 * 存储赛事名称
	 * 
	 * @param w
	 *            W 是否是五大联赛（0否1是）
	 * @param h
	 *            H 赛事名称
	 */
	private void saveGanmeName(String w, String h) {
		if (StringUtils.isBlank(w) || StringUtils.isBlank(h)) {
			return;
		}
		if (!gameNameMap.containsKey("0")) {
			gameNameMap.put("0", new ArrayList<String>());
		}
		if (!gameNameMap.containsKey("1")) {
			gameNameMap.put("1", new ArrayList<String>());
		}
//		if (h.length() > 4) {
//			h = h.substring(0, 4);
//		}
		if ("0".equals(w) && -1 == gameNameMap.get("0").indexOf(h)) {
			gameNameMap.get("0").add(h);
		} else if ("1".equals(w) && -1 == gameNameMap.get("1").indexOf(h)) {
			gameNameMap.get("1").add(h);
		}
	}

	class JCPL221ListAdapter extends JCListAdapter {
		Map<String, List<String>> tmap = new HashMap<String, List<String>>();

		public JCPL221ListAdapter(Context context, List<List<MessageBean>> groupAllArray, JCListView listView) {
			super(context, groupAllArray, listView);
		}

		@Override
		@SuppressLint("ResourceAsColor")
		public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
			// J每场比赛投注截止时间; K主队名称; L 是否让球 0：表示不让 -n：表示主让客 n：表示客让主;m 客队名称 ,V
			// 本场比赛SP值
			String a = "", b = "", c = "", k = "", m = "", L = "", G = "", H = "", J = "", v = "", w = "", key = "", xa = "", xb = "", xc = "";
			MessageBean t = null, x = null;
			ChildHolder cholder = null;
			MessageBean itemMB = groupAllArray.get(groupPosition).get(childPosition);
			t = itemMB.getT();// sp 值"T":{"A":"1.38","B":"4.15","C":"6.30"}
			if (t != null) {
				a = t.getA();
				b = t.getB();
				c = t.getC();
			}
			
			L = itemMB.getL();// L 是否让球 0：表示不让 -n：表示主让客 n：表示客让主
			// 公共实例化控件部分
			G = itemMB.getG();// G 对阵编号
			H = itemMB.getH();// H 赛事名称
			k = itemMB.getK();// K 主队名称
			m = itemMB.getM();// M 客队名称
			J = itemMB.getJ();// J 每场比赛投注截止时间"J":"2014-11-09 21:10"
			v = itemMB.getV();// V 本场比赛SP值（依据游戏的不同，输出不同的JSON对象），详见说明
			x = itemMB.getX();// X 投注比例
			w = itemMB.getW();// W 是否是五大联赛（0否1是）
			if (x != null) {// X 投注比例 json对象 (A,B,C，分别为胜平负的对应值）
				xa = x.getA();
				xb = x.getB();
				xc = x.getC();
			}
			if (!StringUtils.isBlank(J) && J.contains(" ")) {
				String[] ts = J.split(" ");
				J = ts[1] + "截止";// 21:10
			}
//			if (!StringUtils.isBlank(H) && H.length() > 4) {
//				H = H.substring(0, 4);
//			}
			switch (Integer.parseInt(salesType)) {// 不同玩法
			// 219 混合过关
			case 219:
				if (convertView == null) {
					convertView = inflater.inflate(R.layout.jc_d219_itemview, parent, false);
					cholder = new ChildHolder(convertView, 219);
					convertView.setTag(cholder);
				} else if (convertView != null) {
					TextViewGroup item_chgp = (TextViewGroup) convertView.findViewById(R.id.item_chgp);
					item_chgp.setTagList(new ArrayList<String>());
				}
				cholder = (ChildHolder) convertView.getTag();
				cholder.init(219);// 默认初始化控件
				if (!StringUtils.isBlank(k)) {// K 主队名称
//					if (k.length() > 4) {
//						k = k.substring(0, 4);
//					}
					cholder.zhu.setText("(主)" + k);
				}
				if (!StringUtils.isBlank(m)) {// M 客队名称
//					if (m.length() > 4) {
//						m = m.substring(0, 4);
//					}
					cholder.ke.setText("(客)" + m);
				}
				String[] tempStr = null;// C 胜负，客胜，主胜
				if (!StringUtils.isBlank(t.getC().toString()) && t.getC().toString().contains(",")) {
					tempStr = t.getC().split(",");
					if (tempStr != null && tempStr.length >= 2) {
						if (!StringUtils.isBlank(tempStr[1])) {// 主胜
							cholder.cb3.setText(Html.fromHtml(getPKName("主胜", tempStr[1])));
							cholder.cb3.setTag("216^3");
						}
						if (!StringUtils.isBlank(tempStr[0])) {// 客胜
							cholder.cb0.setText(Html.fromHtml(getPKName("主负", tempStr[0])));
							cholder.cb0.setTag("216^0");
						}
					}
				}
				// setTagTextViewmsg(false, cholder.r_tv, L);// A 让分胜负: SP值
				// ,客胜，主胜
				if (!StringUtils.isBlank(t.getA().toString()) && t.getA().toString().contains(",")) {
					tempStr = t.getA().split(",");
					if (tempStr != null && tempStr.length >= 3) {
						if (!StringUtils.isBlank(tempStr[2])) {// 主胜
							cholder.r_ch3.setText(Html.fromHtml(getPKName("主胜", tempStr[2])));
							cholder.r_ch3.setTag("214^3");
						}
						if (!StringUtils.isBlank(tempStr[1])) {// 客胜
							cholder.r_ch0.setText(Html.fromHtml(getPKName("主负", tempStr[1])));
							cholder.r_ch0.setTag("214^0");
						}
					}
				}
				break;
			case 217:// 胜分差(后期升级完善)
				break;
			case 215:// 大小分
				if (convertView == null) {
					convertView = inflater.inflate(R.layout.jc_d215_itemview, parent, false);
					cholder = new ChildHolder(convertView, 215);
					convertView.setTag(cholder);
				} else if (convertView != null) {
					TextViewGroup item_chgp = (TextViewGroup) convertView.findViewById(R.id.item_chgp);
					item_chgp.setTagList(new ArrayList<String>());
				}
				cholder = (ChildHolder) convertView.getTag();
				cholder.init(215);// 默认效果

				// "T":{"A":197.5,"B":1.7,"C":1.79}
				if (!StringUtils.isBlank(k)) {// K 主队名称
//					if (k.length() > 4) {
//						k = k.substring(0, 4);
//					}
					cholder.zhu.setText("(主)"+k);
				}
				if (!StringUtils.isBlank(m)) {// M 客队名称
//					if (m.length() > 4) {
//						m = m.substring(0, 4);
//					}
					cholder.ke.setText("(客)"+m);
				}
				cholder.cb3.setTag("2");
				cholder.cb0.setTag("1");
				cholder.cb3.setText(Html.fromHtml(getPKName(false, "小于"+a,"++", c)));// b
																							// 主胜SP值
				cholder.cb0.setText(Html.fromHtml(getPKName(false, "大于"+a,"++", b)));// C
																							// 主负SP值
				break;

			case 216:// 216 胜负
				if (convertView == null) {
					convertView = inflater.inflate(R.layout.jc_d216_itemview, parent, false);
					cholder = new ChildHolder(convertView, 216);
					convertView.setTag(cholder);
				} else if (convertView != null) {
					TextViewGroup item_chgp = (TextViewGroup) convertView.findViewById(R.id.item_chgp);
					item_chgp.setTagList(new ArrayList<String>());
				}
				cholder = (ChildHolder) convertView.getTag();
				cholder.init(216); // 默认效果
				cholder.cb3.setText(Html.fromHtml(getPKName(false, "(主)" + k, "主胜", b)));// B
																							// 主胜SP值
				cholder.cb0.setText(Html.fromHtml(getPKName(false, "(客)" + m, "主负", a)));// A
																							// 主负SP值
				cholder.cb3.setTag("3");
				cholder.cb0.setTag("0");
				break;

			case 214:// 214 让分胜负
				if (convertView == null) {
					convertView = inflater.inflate(R.layout.jc_d214_itemview, parent, false);
					cholder = new ChildHolder(convertView, 214);
					convertView.setTag(cholder);
				} else if (convertView != null) {
					TextViewGroup item_chgp = (TextViewGroup) convertView.findViewById(R.id.item_chgp);
					item_chgp.setTagList(new ArrayList<String>());
				}
				cholder = (ChildHolder) convertView.getTag();
				cholder.init(214);// 默认初始化控件
				if (!a.contains("-"))
					setTagTextViewmsg(true, cholder.r_text, "主<br>+" + a);// 让球提示+
				else
					setTagTextViewmsg(true, cholder.r_text, "主<br>" + a);// 让球提示-

				cholder.cb3.setText(Html.fromHtml(getPKName(false, "(主)" + k, "主胜", c)));// B
																							// 主胜SP值
				cholder.cb0.setText(Html.fromHtml(getPKName(false, "(客)" + m, "主负", b)));// A
																							// 主负SP值
				cholder.cb3.setTag("3");
				cholder.cb0.setTag("0");
				break;
			}
			if (!StringUtils.isBlank(xa)) {
				cholder.xa.setText(xa);
			}// 投注比例 v 胜
			if (!StringUtils.isBlank(xb)) {
				cholder.xb.setText(xb);
			}// 投注比例 x 负
			if (!StringUtils.isBlank(H)) {
				cholder.name.setText(H);
			}// H 赛事名称
			if (!StringUtils.isBlank(G)) {
				cholder.session.setText(G);
			}// G 场次编号
			if (!StringUtils.isBlank(J)) {
				cholder.time.setText(J);
			}// J 截止时间
			key = itemMB.getC() + "-" + itemMB.getG();// 篮彩不涉及批次号的截取
			// 组栏效果(默认)
			List<TextView> cbList = cholder.item_chgp.getTvList();
			if ((ticket.getLotteryTag().get(key) != null && ticket.getLotteryTag().get(key).size() > 0) && cbList != null) {// 设置选中效果
				for (int i = 0; i < cbList.size(); i++) {
					if (!StringUtils.isBlank(cbList.get(i).getTag().toString())) {
						if (-1 != ticket.getLotteryTag().get(key).indexOf(cbList.get(i).getTag().toString())) {
							cbList.get(i).setBackgroundDrawable(MyApp.res.getDrawable(R.drawable.jc_cb_true));
							cbList.get(i).setTextColor(MyApp.res.getColor(R.color.red_chbx_text));
						} else if (!StringUtils.isBlank(cbList.get(i).getTag().toString()) && !"yield".equals(cbList.get(i).getTag().toString())) {

							cbList.get(i).setTextColor(MyApp.res.getColor(R.color.item_hall_tv_title_text_color));
							cbList.get(i).setBackgroundDrawable(MyApp.res.getDrawable(R.drawable.jc_cb_false));
						}
					}
				}
				cholder.item_chgp.setTagList(ticket.getLotteryTag().get(key));
			} else {
				for (int i = 0; i < cbList.size(); i++) {
					if (cbList.get(i) != null) {
						cbList.get(i).setBackgroundDrawable(MyApp.res.getDrawable(R.drawable.jc_cb_false));
						cbList.get(i).setTextColor(MyApp.res.getColor(R.color.item_hall_tv_title_text_color));
					}
				}
				if (ticket.getSelectMessageBean() != null && -1 != ticket.getSelectMessageBean().indexOf(itemMB)) {
					ticket.getSelectMessageBean().remove(itemMB);
				}
			}
			// 子布局 选票场次
			cholder.item_chgp.setOnTextViewGroupListener(new OnTextViewGroup() {
				@Override
				public void setOnTextViewGroup(View v, List<String> tagList, List<String> textList) {
					MessageBean itemMB = groupAllArray.get(groupPosition).get(childPosition);
					// 投注截止时间 比对 已经finally处理不用为空判断
					if (!FormatUtil.salesThanTimer(itemMB.getJ())) {
						MyToast.showToast(mActivity, "本场比赛投注时间已结束!");
						notifyDataSetChanged();
						return;
					}
					// 控制每场投注场次数
					if (15 > ticket.getSelectMessageBean().size()) {// 客户端取消选票控制转由后台控制最多选票数
						String key = itemMB.getC() + "-" + itemMB.getG();// 篮彩不涉及批次号截取
						if (tagList.size() > 0) {
							ticket.getLotteryTag().put(key, tagList);
							// 移除已存在 重新加载
							if (-1 != ticket.getSelectMessageBean().indexOf(itemMB)) {// 移除已经存在的
								ticket.getSelectMessageBean().get(ticket.getSelectMessageBean().indexOf(itemMB)).setCH_NUM(tagList.size());
							} else {
								itemMB.setCH_NUM(tagList.size());
								ticket.getSelectMessageBean().add(itemMB);
							}
						} else {
							if (ticket.getLotteryTag() != null && ticket.getLotteryTag().containsKey(key)) {
								ticket.getLotteryTag().remove(key);
							}
							if (ticket.getSelectMessageBean() != null && -1 != ticket.getSelectMessageBean().indexOf(itemMB)) {
								ticket.getSelectMessageBean().remove(itemMB);
							}
						}
						basebuy_tv_notice.setText(Html.fromHtml(ticket.getNotice()));
						changeBtnbgAndTextColors(ticket.getSession());
					} else {
						MyToast.showToast(mActivity, "每张选票只能筛选15场比赛!");
						basebuy_tv_notice.setText(Html.fromHtml(ticket.getNotice()));
					}
					notifyDataSetChanged();
					shakeListener.vibrate();
				}
			});

			// 是否展开子 item
			cholder.open_show.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					if (lotteryScene.contains(String.valueOf(groupPosition) + "-" + String.valueOf(childPosition))) {
						lotteryScene.remove(String.valueOf(groupPosition) + "-" + String.valueOf(childPosition));
					} else {
						lotteryScene.add(String.valueOf(groupPosition) + "-" + String.valueOf(childPosition));
					}
					notifyDataSetChanged();
				}
			});

			String openStr = String.valueOf(groupPosition) + "-" + String.valueOf(childPosition);
			cholder.item_item_layout.setVisibility(View.GONE);
			Drawable mDrawable = MyApp.res.getDrawable(R.drawable.d1);
			mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(), mDrawable.getMinimumHeight());
			cholder.ratio_show_tv.setCompoundDrawables(null, mDrawable, null, null);
			if (lotteryScene.contains(openStr)) {
				mDrawable = MyApp.res.getDrawable(R.drawable.u1);
				mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(), mDrawable.getMinimumHeight());
				cholder.item_item_layout.setVisibility(View.VISIBLE);
				cholder.ratio_show_tv.setCompoundDrawables(null, mDrawable, null, null);
			}
			if (mDialog != null && mDialog.isShowing()) {// 提示框
				mDialog.dismiss();
			}
			return convertView;
		}

		class ChildHolder {
			TextView title, name, session;
			TextView time, xa, xb;// 投注比例
			TextView zhu, ke;// 混合过关 名称
			RelativeLayout open_show;
			LinearLayout home_layout;
			LinearLayout item_item_layout;
			TextViewGroup item_chgp;
			TextView ratio_show_tv;// 选中效果按钮箭头
			TextView cb3, r_text, cb0;// 胜平负
			// Button r_text;
			TextView r_ch3, r_ch0;// 让球胜平负

			public ChildHolder(View v, int salesType) {
				switch (salesType) {// 不同玩法
				case 216:// 胜负
					cb3 = (TextView) v.findViewById(R.id.cb3);
					cb0 = (TextView) v.findViewById(R.id.cb0);
					break;
				case 214:// 让胜负
					cb3 = (TextView) v.findViewById(R.id.cb3);
					r_text = (TextView) v.findViewById(R.id.r_text);
					cb0 = (TextView) v.findViewById(R.id.cb0);
					break;
				case 215:// 大小分
					zhu = (TextView) v.findViewById(R.id.zhu);
					ke = (TextView) v.findViewById(R.id.ke);

					cb3 = (TextView) v.findViewById(R.id.cb3);
					cb0 = (TextView) v.findViewById(R.id.cb0);
					break;
				case 219:// 混合过关
					// vs name
					zhu = (TextView) v.findViewById(R.id.zhu);
					ke = (TextView) v.findViewById(R.id.ke);
					// 胜平负
					cb3 = (TextView) v.findViewById(R.id.cb3);
					cb0 = (TextView) v.findViewById(R.id.cb0);
					// 让球胜平负
					r_ch3 = (TextView) v.findViewById(R.id.r_ch3);
					r_ch0 = (TextView) v.findViewById(R.id.r_ch0);
					break;
				}
				home_layout = (LinearLayout) v.findViewById(R.id.home_layout);
				// 投注比例
				xa = (TextView) v.findViewById(R.id.xa);
				xb = (TextView) v.findViewById(R.id.xb);
				// 左边场次信息描述
				name = (TextView) v.findViewById(R.id.name);
				session = (TextView) v.findViewById(R.id.session);
				time = (TextView) v.findViewById(R.id.time);
				// 公共组
				ratio_show_tv = (TextView) v.findViewById(R.id.ratio_show_tv);
				open_show = (RelativeLayout) v.findViewById(R.id.jc_c_openbtn);
				item_item_layout = (LinearLayout) v.findViewById(R.id.item_item_layout);
				item_chgp = (TextViewGroup) v.findViewById(R.id.item_chgp);
			}

			// 默认初始化控件
			public void init(int salesType) {
				switch (salesType) {// 不同玩法
				case 216:// 胜负
					cb3.setText("");
					cb0.setText("");
					cb3.setTag("");
					cb0.setTag("");
					break;
				case 215:// 大小分
					cb3.setText("");
					cb0.setText("");
					cb3.setTag("");
					cb0.setTag("");
					break;
				case 214:// 让胜负
					cb3.setText("");
					cb3.setTag("");

					cb0.setText("");
					cb0.setTag("");
					r_text.setText("");
					break;
				case 219:// 混关
					// 默认效果
					cb3.setText("");
					cb3.setTag("");
					cb0.setText("");
					cb0.setTag("");

					r_ch3.setText("");
					r_ch3.setTag("");

					r_ch0.setText("");
					r_ch0.setTag("");
					break;
				}
			}
		}
	}

	/**
	 * 格式化效果对阵名称
	 * 
	 * @param pk
	 *            对阵name
	 * @param spf
	 *            胜平负
	 * @param sp
	 *            sp值
	 * @return
	 */
	public static String getPKName(boolean isChecked, String pk, String spf, String sp) {
		String name = "";
		String s = "--";
		if (StringUtils.isBlank(pk)) {
			pk = "--";
		}
		if (StringUtils.isBlank(spf)) {
			spf = "--";
		}
		
		if("++".equals(spf)){
			spf = "";
		}
		if("aabbcc".equals(pk)){
			pk = "";
		}
		
		if (StringUtils.isBlank(sp)) {
			sp = "-.--";
		}

		name = pk;
			
		if("".equals(pk)){
			if (isChecked) {
				s = StringUtils.replaceEach(MyApp.res.getString(R.string.jc_itemui_true1), new String[] { "NAME", "PK_SP" }, new String[] { name, (spf + sp) });
			} else {
				s = StringUtils.replaceEach(MyApp.res.getString(R.string.jc_itemui_false1), new String[] { "NAME", "PK", "SP" }, new String[] { name, spf, sp });
			}
		}else{
			if (isChecked) {
				s = StringUtils.replaceEach(MyApp.res.getString(R.string.jc_itemui_true), new String[] { "NAME", "PK_SP" }, new String[] { name, (spf + sp) });
			} else {
				s = StringUtils.replaceEach(MyApp.res.getString(R.string.jc_itemui_false), new String[] { "NAME", "PK", "SP" }, new String[] { name, spf, sp });
			}
		}	
			
		
		return s;
	}

	/**
	 * 209 item UI 效果
	 * 
	 * @param isChecked
	 * @param name
	 * @param sp
	 * @return
	 */
	public static String getPKName(String name, String sp) {
		String s = "--";
		if (StringUtils.isBlank(name)) {
			name = "--";
		}
		if (StringUtils.isBlank(sp)) {
			sp = "--";
		}
		s = StringUtils.replaceEach(MyApp.res.getString(R.string.jc_209item_true), new String[] { "NAME", "SP" }, new String[] { name, sp });
		return s;
	}

	/**
	 * 适配器 中用于控制让球tagTextView (让球数,背景区分色)信息
	 * 
	 * @param isAddText
	 *            是否增加内容
	 * @param tv
	 *            显示内容的控件
	 * @param L
	 *            具体内容
	 */

	public static void setTagTextViewmsg(boolean isAddText, TextView tv, String L) {
		if (StringUtils.isBlank(L) || tv == null)
			return;
		if ("0".equals(L)) {// 0：表示不让 @color/btn_gray_u
			L = StringUtils.replaceEach(MyApp.res.getString(R.string.gray_text), new String[] { "MSG" }, new String[] { L });
		} else if (L.contains("-")) {// -n：表示主让客 @color/red
			L = StringUtils.replaceEach(MyApp.res.getString(R.string.green_text), new String[] { "MSG" }, new String[] { L });
		} else {// n：表示客让主 @color/btn_orange_u
			L = StringUtils.replaceEach(MyApp.res.getString(R.string.red_text), new String[] { "MSG" }, new String[] { L });
		}
		tv.setText(Html.fromHtml(L));
		if (!isAddText) {
			tv.setText(L);
		}
		tv.setVisibility(View.VISIBLE);
	}

	private long lastClickTime = 0; // 防止重复点击

	private boolean isFastClick(Context mActivity) {
		long timeD = System.currentTimeMillis() - lastClickTime;
		if (0 < timeD && timeD < 1500) {
			return true;
		}
		lastClickTime = System.currentTimeMillis();
		return false;
	}

	@Override
	protected void ok() {
		if (isFastClick(mActivity))
			return;// 防止重复点击
		// System.out.println("221--->"+ticket.toString());
		if (ticket.isTicketAvailable()) {
			MyApp.order.getTickets().add(0, ticket);// 为了保证顺序
			MyApp.order.clearNullTicket();// 清空 空场选票
			MyApp.order.setLotteryId(salesType);
			MyApp.order.setAppId(lotteryId);
			Bundle mBundle = new Bundle();
			mBundle.putString("childId", childId);
			start(ThirdActivity.class, JclqBetorder.class, mBundle);
		} else {
			if (0 == ticket.getSession()) {
				MyToast.showToast(mActivity, "请至少选择" + childId + "场比赛!");
			} else {
				MyToast.showToast(mActivity, "已选1还差1场比赛!");
			}
		}
	}

	@Override
	protected void clear() {
		initTicket();// 重新创建一个新的ticket
		if (mjcAdapter != null) {
			mjcAdapter.notifyDataSetChanged();
		}
		setNotice();
		changeBtnbgAndTextColors(ticket.getSession());
	}
}
