package com.mitenotc.ui.play;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mitenotc.bean.JcBfBean;
import com.mitenotc.bean.MessageBean;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.tc.ThirdActivity;
import com.mitenotc.ui.adapter.JCListAdapter;
import com.mitenotc.ui.buy.BaseBuyAnnotation;
import com.mitenotc.ui.buy.JcBaseFragment;
import com.mitenotc.ui.ui_utils.JCListView;
import com.mitenotc.ui.ui_utils.MyToast;
import com.mitenotc.ui.ui_utils.ScreenDialog;
import com.mitenotc.ui.ui_utils.TextViewGroup;
import com.mitenotc.ui.ui_utils.TextViewGroup.OnTextViewGroup;
import com.mitenotc.utils.FormatUtil;

/**
 * 竞彩足球 310 让球胜平负 218 胜平负 209 混合过关
 * 
 * @author mitenotc salesType : 玩法 childId : 过关/单关
 */
@BaseBuyAnnotation(lotteryId = "310", salesType = "218", childId = "2")
public class JCPL310 extends JcBaseFragment {
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
					sDialog = new ScreenDialog(mActivity);
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
								mjcAdapter = new JCPL210ListAdapter(mActivity,
										gList, home_expandable_Lv);
								home_expandable_Lv.setAdapter(mjcAdapter);// 必须重新设置适配器
																			// 以保证UI有明显的刷新效果
								initTicket();
								setNotice();
								mjcAdapter.notifyList();
							} else {
								MyToast.showToast(mActivity,
										"没有符合筛选条件的场次,请重新筛选 !");
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
		gList = new ArrayList<List<MessageBean>>();
		// 保证在不需要多次网络请求的情况下 在已有数据集合中 可以按照用户的条件筛选
		for (List<MessageBean> list : groupList) {
			if (list.size() > 0) {
				tlist = new ArrayList<MessageBean>();
				for (int i = 0; i < list.size(); i++) {
					MessageBean m = list.get(i).getT();
					issp = false;
					isname = false;
					// 210 218 :"T":{"A":"1.50"} 209 :
					// "T":{"A":"2.76,3.50,2.08","B":"6.90,4.50,1.32"}
					if ((!StringUtils.isBlank(m.getA()) && !StringUtils
							.isBlank(m.getB())) && SP > 0) {// SP值筛选
						if ((m.getA().contains(",") && m.getB().contains(","))
								&& (m.getA().split(",").length > 0 && m.getB()
										.split(",").length > 0)) {
							// 1.5以下
							if ((1.5 == SP)
									&& (Float
											.parseFloat(m.getA().split(",")[0]) > SP)
									&& (Float
											.parseFloat(m.getB().split(",")[0]) > SP)) {
								issp = true;
							}
							// 2.0以上
							if ((2.0 == SP)
									&& (Float
											.parseFloat(m.getA().split(",")[0]) < SP)
									&& (Float
											.parseFloat(m.getB().split(",")[0]) < SP)) {
								issp = true;
							}
						} else {
							float s = Float.parseFloat(m.getA());
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
		return View.inflate(mActivity, R.layout.f_zq_pop_titile_rb, null);
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
		case 218:// 胜平负 JC_D218Adapter
			setTitleNav(Integer.parseInt(lotteryId), "胜平负",
					R.drawable.title_nav_back, R.drawable.title_nav_menu);
			sendRequest(msg, 0);
			break;
		case 210:// 让球胜平负 JC_D210Adapter
			setTitleNav(Integer.parseInt(lotteryId), "让球胜平负",
					R.drawable.title_nav_back, R.drawable.title_nav_menu);
			sendRequest(msg, 0);
			break;
		case 209:// 混合过关 JC_D209Adapter
			setTitleNav(Integer.parseInt(lotteryId), "混合过关",
					R.drawable.title_nav_back, R.drawable.title_nav_menu);
			sendRequest(msg, 0);
			break;
		case 211:// 比分
			setTitleNav(Integer.parseInt(lotteryId), "比分",
					R.drawable.title_nav_back, R.drawable.title_nav_menu);
			sendRequest(msg, 1);
			break;
		// case 213:// 半全场
		// setTitleNav(Integer.parseInt(lotteryId), "半全场",
		// R.drawable.title_nav_back, R.drawable.title_nav_menu);
		// msg.put("I", "guding");
		// break;
		// case 212:// 总进球
		// setTitleNav(Integer.parseInt(lotteryId), "总进球",
		// R.drawable.title_nav_back, R.drawable.title_nav_menu);
		// msg.put("I", "guding");
		// break;
		}

	}

	private void sendRequest(MessageJson msg, int what) {
		msg.put("A", Integer.parseInt(salesType));// salesType 218
		msg.put("F", "0");// F 售票状态 0：销售中 1：不可销售
		msg.put("I", "guding");
		submitData(what, 4002, msg);
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
		case 0:// 查询竞彩对阵
			if (mBean.getA().equals("0")) {
				List<MessageBean> mList = mBean.getLIST();
				initJCAdapter(mList);// 初始化适配器 适配数据
				basebuy_tv_notice.setText(Html.fromHtml(ticket.getNotice()));
			}
			break;
		case 1:// 袁梦希修改
			if (mBean.getA().equals("0")) {
				List<MessageBean> mList = mBean.getLIST();
				initOtherAdapter(mList);// 初始化适配器 适配数据
				// basebuy_tv_notice.setText(Html.fromHtml(ticket.getNotice()));
			}
			break;
		}
	}

	@Override
	protected void errorResult(Message msg) {
		super.errorResult(msg);
		// mhandler.sendEmptyMessage(MSG_ERROR);
	}

	@Override
	protected void nullResult() {
		super.nullResult();
		// mhandler.sendEmptyMessage(MSG_ERROR);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		sDialog = null;
	}

	private void initOtherAdapter(List<MessageBean> mList) {
		if (mList == null || mList.size() == 0) {
			if (mDialog != null && mDialog.isShowing()) {// 提示框
				mDialog.dismiss();
			}
			showNullsaishi();
			return;
		}
		hideNullsaishi();
		groupList.clear();// 清空已有的对阵信息
		if(bfbean.getMapData()!=null){
			bfbean.getMapData().clear();
		}
		if(bfbean.getPiao()!=null){
			bfbean.getPiao().clear();
		}
		List<Integer> pcList = new ArrayList<Integer>();// 日期的集合
		for (int i = 0; i < mList.size(); i++) {
			if (!StringUtils.isBlank(mList.get(i).getC())
					&& -1 == pcList.indexOf(Integer.parseInt(mList.get(i)
							.getC()))) {
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
				if (!StringUtils.isBlank(mList.get(i).getC())
						&& pcStr.equals(mList.get(i).getC())) {
					temlist.add(mList.get(i));
				}
			}
			if (temlist.size() > 0) {
				groupList.add(0, temlist);
			}
		}
		ToggleButtonGroup.clearList();
		mjcAdapter = new JCOtherListAdapter(mActivity, groupList,
				home_expandable_Lv);
		home_expandable_Lv.setAdapter(mjcAdapter);// 必须重新设置适配器 以保证UI有明显的刷新效果
		show(home_expandable_Lv);
		mjcAdapter.notifyList();
	}

	class JCOtherListAdapter extends JCListAdapter {
		
		boolean isClick = false;//防止重复点击

		public JCOtherListAdapter(Context context,
				List<List<MessageBean>> groupAllArray, JCListView listView) {
			super(context, groupAllArray, listView);
		}

		@Override
		@SuppressLint("ResourceAsColor")
		public View getChildView(final int groupPosition,final int childPosition, boolean isLastChild, View convertView,ViewGroup parent) {
			ChildOtherHolder holder;
			final MessageBean itemMB = groupAllArray.get(groupPosition).get(childPosition);
			switch (Integer.parseInt(salesType)) {
			case 211:
				if (convertView == null) {
					convertView = mInflater.inflate(R.layout.jc_d211_itemview,null);
					holder = new ChildOtherHolder(convertView, 211);
					convertView.setTag(holder);
				} else {
					holder = (ChildOtherHolder) convertView.getTag();
				}
				holder.init(211);
				final String k = itemMB.getK();
				holder.zhu.setText(k);
				final String m = itemMB.getM();
				holder.ke.setText(m);

				String pos = String.valueOf(groupPosition)+"_"+String.valueOf(childPosition);
				if (bfbean.getMapData().containsKey(pos)) {
					List<String> list = bfbean.getMapData().get(pos);
					if (list.size() > 0) {
						holder.btn_bf.setText(list.toString());
						holder.btn_bf.setTextColor(Color.WHITE);
						holder.btn_bf.setBackgroundResource(R.drawable.btn_bg_orange_selector);
					} else {
						bfbean.getMapData().remove(pos);// 在集合中移除空的元素
						holder.btn_bf.setText("点击选择比分");
						holder.btn_bf.setTextColor(Color.BLACK);
						holder.btn_bf.setBackgroundResource(R.drawable.btn_bg_white_selector);
					}
				}

				MessageBean t = itemMB.getT();
				final String[] win = t.getA().split(",");
				final String[] equ = t.getB().split(",");
				final String[] los = t.getC().split(",");
				String time = itemMB.getJ();
				if (!StringUtils.isBlank(time) && time.contains(" ")) {
					String[] ts = time.split(" ");
					time = ts[1] + "截止";// 21:10
				}

				holder.time.setText(time);
				String h = itemMB.getH();
				if (!StringUtils.isBlank(h) && h.length() > 4) {
					h = h.substring(0, 4);
				}
				holder.name.setText(h);
				holder.changci.setText(itemMB.getG());

				final String text = holder.btn_bf.getText().toString();
				holder.btn_bf.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (!isClick) {
							isClick = true;
						} else {
							return;
						}

						if (bfbean.getMapData().size() > 14 && "点击选择比分".equals(text)) {
							Toast.makeText(mActivity, "每张选票只能筛选15场比赛!",Toast.LENGTH_SHORT).show();
							mjcAdapter.notifyDataSetChanged();
							isClick = false;
							return;
						} else {
							
							final JCDialog dialog = new JCDialog(mActivity);
							dialog.initDialogData(win, equ, los, k, m);
							if (bfbean.getMapData().containsKey(String.valueOf(groupPosition)+"_"+ String.valueOf(childPosition))) {
								List<String> list = bfbean.getMapData().get(String.valueOf(groupPosition)+"_"+ String.valueOf(childPosition));
								dialog.setInitList(list);
							}
							dialog.addListenerJCDialog(new OnClickListener() {
			
								@Override
								public void onClick(View v) {// 取消
									ToggleButtonGroup.clearList();
									dialog.dismiss();
			
								}
							}, new OnClickListener() {
			
								@Override
								public void onClick(View v) {// 确认
									ToggleButtonGroup tbg = new ToggleButtonGroup(mActivity);
									List<String> checkedList = tbg.getCheckedList();
									String str = String.valueOf(groupPosition)+"_"+String.valueOf(childPosition);
									if (checkedList.size() > 0) {
										bfbean.getMapData().put(str, checkedList);
										System.out.println(";;;;;;;;;;"+itemMB.getK());
										bfbean.getPiao().put(str, itemMB);
									} else {
										bfbean.getMapData().remove(str);
										bfbean.getPiao().remove(str);
									}
									ticket.setSession(bfbean.getMapData().size());
									basebuy_tv_notice.setText(Html.fromHtml(ticket.getNotice1()));
									changeBtnbgAndTextColors(ticket.getSession1());
									mjcAdapter.notifyDataSetChanged();
									ToggleButtonGroup.clearList();
									dialog.dismiss();
								}
							});
							dialog.show();
							
							dialog.setOnDismissListener(new OnDismissListener() {
								
								@Override
								public void onDismiss(DialogInterface dialog) {
									// TODO Auto-generated method stub
									isClick=false;
								}
							});
						}

					}

				});
				
				break;
			}

			if (mDialog != null && mDialog.isShowing()) {// 提示框
				mDialog.dismiss();
			}
			return convertView;
		}
	}

	class ChildOtherHolder {
		private TextView zhu, ke, name, changci, time;
		private Button btn_bf;

		public ChildOtherHolder(View v, int salesType) {
			switch (salesType) {
			case 211:
				zhu = (TextView) v.findViewById(R.id.zhu);
				ke = (TextView) v.findViewById(R.id.ke);
				name = (TextView) v.findViewById(R.id.name);
				changci = (TextView) v.findViewById(R.id.session);
				time = (TextView) v.findViewById(R.id.time);
				btn_bf = (Button) v.findViewById(R.id.btn_jc_d211);
				break;
			}
		}

		public void init(int salesType) {
			switch (salesType) {
			case 211:
				zhu.setText("");
				ke.setText("");
				name.setText("");
				changci.setText("");
				time.setText("");
				btn_bf.setText("点击选择比分");
				btn_bf.setTextColor(Color.BLACK);
				btn_bf.setBackgroundResource(R.drawable.btn_bg_white_selector);
				break;
			}
		}
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
		List<Integer> pcList = new ArrayList<Integer>();// 日期的集合
		for (int i = 0; i < mList.size(); i++) {
			if (!StringUtils.isBlank(mList.get(i).getC())
					&& -1 == pcList.indexOf(Integer.parseInt(mList.get(i)
							.getC()))) {
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
				if (!StringUtils.isBlank(mList.get(i).getC())
						&& pcStr.equals(mList.get(i).getC())) {
					temlist.add(mList.get(i));
				}
			}
			if (temlist.size() > 0) {
				groupList.add(0, temlist);
			}
		}
		// 方式二 -----由于手机系统时间会导致数据不能正常显示因此弃用------TODO
		// 批次筛选对照组 客户端支持由当前天数递增共5天(即未来5天内赛事对阵信息): 例如 :[20141112, 20141113,
		// 20141114, 20141115, 20141116];
		/*
		 * String[] t=FormatUtil.getTimes(); Map<String, List<MessageBean>>
		 * map=new HashMap<String, List<MessageBean>>(); for (int i = 0; i <
		 * t.length; i++) { map.put(String.valueOf(i), new
		 * ArrayList<MessageBean>()); } for (int i = 0; i < mList.size(); i++) {
		 * MessageBean mb = mList.get(i); saveGanmeName(mb.getW(),mb.getH());
		 * if(t[0].equals(mb.getC())){//今天 map.get("0").add(mb); }else
		 * if(t[1].equals(mb.getC())){//今天 +1 map.get("1").add(mb); }else
		 * if(t[2].equals(mb.getC())){//今天 +2 map.get("2").add(mb); }else
		 * if(t[3].equals(mb.getC())){//今天 +3 map.get("3").add(mb); }else
		 * if(t[4].equals(mb.getC())){//今天 +4 map.get("4").add(mb); } } // 筛选过滤
		 * for (int i = 0; i < map.size(); i++) {
		 * if(map.get(String.valueOf(i)).size() > 0){
		 * groupList.add(map.get(String.valueOf(i))); } }
		 */
		// if (sDialog != null) {
		// SP = sDialog.getSp();
		// seelectNameList = sDialog.getSelectListName();
		// }
		// if (seelectNameList != null && seelectNameList.size() > 0) {
		// selectMessBean();
		// if (gList.size() > 0) {
		// mjcAdapter = new JCPL210ListAdapter(mActivity, gList,
		// home_expandable_Lv);
		// }
		// } else {
		mjcAdapter = new JCPL210ListAdapter(mActivity, groupList,
				home_expandable_Lv);
		// }
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
		if (h.length() > 4) {
			h = h.substring(0, 4);
		}
		if ("0".equals(w) && -1 == gameNameMap.get("0").indexOf(h)) {
			gameNameMap.get("0").add(h);
		} else if ("1".equals(w) && -1 == gameNameMap.get("1").indexOf(h)) {
			gameNameMap.get("1").add(h);
		}
	}

	class JCPL210ListAdapter extends JCListAdapter {

		public JCPL210ListAdapter(Context context,
				List<List<MessageBean>> groupAllArray, JCListView listView) {
			super(context, groupAllArray, listView);
		}

		@Override
		@SuppressLint("ResourceAsColor")
		public View getChildView(final int groupPosition,
				final int childPosition, boolean isLastChild, View convertView,
				ViewGroup parent) {
			// J每场比赛投注截止时间; K主队名称; L 是否让球 0：表示不让 -n：表示主让客 n：表示客让主;m 客队名称 ,V
			// 本场比赛SP值
			String a = "", b = "", c = "", k = "", m = "", L = "", G = "", H = "", J = "", v = "", w = "", key = "", xa = "", xb = "", xc = "";
			MessageBean t = null, x = null;
			ChildHolder cholder = null;
			MessageBean itemMB = groupAllArray.get(groupPosition).get(
					childPosition);
			t = itemMB.getT();// sp 值"T":{"A":"1.38","B":"4.15","C":"6.30"}
			if (t != null) {
				a = t.getA();
				b = t.getB();
				c = t.getC();
			}
			k = itemMB.getK();// K 主队名称
			m = itemMB.getM();// M 客队名称
			L = itemMB.getL();// L 是否让球 0：表示不让 -n：表示主让客 n：表示客让主
			// 公共实例化控件部分
			G = itemMB.getG();// G 对阵编号
			H = itemMB.getH();// H 赛事名称
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
			if (!StringUtils.isBlank(H) && H.length() > 4) {
				H = H.substring(0, 4);
			}
			switch (Integer.parseInt(salesType)) {// 不同玩法
			// 209 混合过关
			case 209:
				if (convertView == null) {
					convertView = inflater.inflate(R.layout.jc_d209_itemview,
							parent, false);
					cholder = new ChildHolder(convertView, 209);
					convertView.setTag(cholder);
				} else if (convertView != null) {
					TextViewGroup item_chgp = (TextViewGroup) convertView
							.findViewById(R.id.item_chgp);
					item_chgp.setTagList(new ArrayList<String>());
				}
				cholder = (ChildHolder) convertView.getTag();
				cholder.init(209);// 默认初始化控件
				if (!StringUtils.isBlank(k)) {// K 主队名称
					if (k.length() > 4) {
						k = k.substring(0, 4);
					}
					cholder.zhu.setText(k);
				}
				if (!StringUtils.isBlank(m)) {// M 客队名称
					if (m.length() > 4) {
						m = m.substring(0, 4);
					}
					cholder.ke.setText(m);
				}
				String[] tempStr = null;// B 非让球胜平负SP值，值之间以 逗号隔开
				if (!StringUtils.isBlank(t.getB().toString())
						&& t.getB().toString().contains(",")) {
					tempStr = t.getB().split(",");
					if (tempStr != null && tempStr.length == 3) {
						if (!StringUtils.isBlank(tempStr[0])) {// A 胜SP值
							cholder.cb3.setText(Html.fromHtml(getPKName("主胜",
									tempStr[0])));
							cholder.cb3.setTag("218^3_" + tempStr[0]);
						}
						if (!StringUtils.isBlank(tempStr[1])) {// B 平SP值
							cholder.cb1.setText(Html.fromHtml(getPKName("平",
									tempStr[1])));
							cholder.cb1.setTag("218^1_" + tempStr[1]);
						}
						if (!StringUtils.isBlank(tempStr[2])) {// C 负SP值
							cholder.cb0.setText(Html.fromHtml(getPKName("客胜",
									tempStr[2])));
							cholder.cb0.setTag("218^0_" + tempStr[2]);
						}
					}
				}
				setTagTextViewmsg(false, cholder.r_tv, L);// 让球提示
				// A 让球胜平负SP值，值之间以 逗号隔开
				if (!StringUtils.isBlank(t.getA().toString())
						&& t.getA().toString().contains(",")) {
					tempStr = t.getA().split(",");
					if (tempStr != null && tempStr.length == 3) {
						if (!StringUtils.isBlank(tempStr[0])) {// A 胜SP值
							cholder.r_ch3.setText(Html.fromHtml(getPKName("主胜",
									tempStr[0])));
							cholder.r_ch3.setTag("210^3_" + tempStr[0]);
						}
						if (!StringUtils.isBlank(tempStr[1])) {// B 平SP值
							cholder.r_ch1.setText(Html.fromHtml(getPKName("平",
									tempStr[1])));
							cholder.r_ch1.setTag("210^1_" + tempStr[1]);
						}
						if (!StringUtils.isBlank(tempStr[2])) {// C 负SP值
							cholder.r_ch0.setText(Html.fromHtml(getPKName("客胜",
									tempStr[2])));
							cholder.r_ch0.setTag("210^0_" + tempStr[2]);
						}
					}
				}
				break;
			// 218 胜平负
			case 218:
				if (convertView == null) {
					convertView = inflater.inflate(R.layout.jc_d218_itemview,
							parent, false);
					cholder = new ChildHolder(convertView, 218);
					convertView.setTag(cholder);
				} else if (convertView != null) {
					TextViewGroup item_chgp = (TextViewGroup) convertView
							.findViewById(R.id.item_chgp);
					item_chgp.setTagList(new ArrayList<String>());
				}
				cholder = (ChildHolder) convertView.getTag();
				// 默认效果
				cholder.init(218);
				if (!StringUtils.isBlank(a)) {// A 胜SP值
					cholder.cb3.setText(Html.fromHtml(getPKName(false, k, "主胜",
							a)));
					cholder.cb3.setTag("3_" + a);
				}
				if (!StringUtils.isBlank(b)) {// B 平SP值
					cholder.cb1.setText(Html.fromHtml(getPKName(false, "VS",
							"平", b)));
					cholder.cb1.setTag("1_" + b);
				}
				if (!StringUtils.isBlank(c)) {// C 负SP值
					cholder.cb0.setText(Html.fromHtml(getPKName(false, m, "客胜",
							c)));
					cholder.cb0.setTag("0_" + c);
				}
				break;
			// 210 让球胜平负
			case 210:
				if (convertView == null) {
					convertView = inflater.inflate(R.layout.jc_d210_itemview,
							parent, false);
					cholder = new ChildHolder(convertView, 210);
					convertView.setTag(cholder);
				} else if (convertView != null) {
					TextViewGroup item_chgp = (TextViewGroup) convertView
							.findViewById(R.id.item_chgp);
					item_chgp.setTagList(new ArrayList<String>());
				}
				cholder = (ChildHolder) convertView.getTag();
				// 默认初始化控件
				cholder.init(210);
				setTagTextViewmsg(true, cholder.r_tv, L);// 让球提示
				if (!StringUtils.isBlank(a)) {// A 胜SP值
					cholder.r_ch3.setText(Html.fromHtml(getPKName(false, k,
							"主胜", a)));
					cholder.r_ch3.setTag("3_" + a);
				}
				if (!StringUtils.isBlank(b)) {// B 平SP值
					cholder.r_ch1.setText(Html.fromHtml(getPKName(false, "VS",
							"平", b)));
					cholder.r_ch1.setTag("1_" + b);
				}
				if (!StringUtils.isBlank(c)) {// C 负SP值
					cholder.r_ch0.setText(Html.fromHtml(getPKName(false, m,
							"客胜", c)));
					cholder.r_ch0.setTag("0_" + c);
				}
				break;
			}
			if (!StringUtils.isBlank(xa)) {
				cholder.xa.setText(xa);
			}// 投注比例 v 胜
			if (!StringUtils.isBlank(xb)) {
				cholder.xb.setText(xb);
			}// 投注比例 x 平
			if (!StringUtils.isBlank(xc)) {
				cholder.xc.setText(xc);
			}// 投注比例 w 负
			if (!StringUtils.isBlank(H)) {
				cholder.name.setText(H);
			}// H 赛事名称
			if (!StringUtils.isBlank(G)) {
				cholder.session.setText(G);
			}// G 场次编号
			if (!StringUtils.isBlank(J)) {
				cholder.time.setText(J);
				if (!FormatUtil.salesThanTimer(J)) {
					show(cholder.not_optional_view);// 停止销售的需要覆盖不可点击蒙版
				} else {
					hide(cholder.not_optional_view);
				}
			}// J 截止时间

			if (itemMB.getC().length() > 6) {// 20141122为了字符串截取安全
				key = itemMB.getC().subSequence(itemMB.getC().length() - 6,
						itemMB.getC().length())
						+ "-" + itemMB.getG();
			}
			// 组栏效果(默认)
			List<TextView> cbList = cholder.item_chgp.getTvList();
			if ((ticket.getLotteryTag().get(key) != null && ticket
					.getLotteryTag().get(key).size() > 0)
					&& cbList != null) {// 设置选中效果
				for (int i = 0; i < cbList.size(); i++) {
					if (-1 != ticket.getLotteryTag().get(key)
							.indexOf(cbList.get(i).getTag().toString())) {
						cbList.get(i).setBackgroundDrawable(
								MyApp.res.getDrawable(R.drawable.jc_cb_true));
						cbList.get(i).setTextColor(
								MyApp.res.getColor(R.color.red_chbx_text));
					} else {
						cbList.get(i)
								.setTextColor(
										MyApp.res
												.getColor(R.color.item_hall_tv_title_text_color));
						cbList.get(i).setBackgroundDrawable(
								MyApp.res.getDrawable(R.drawable.jc_cb_false));
					}
				}
				cholder.item_chgp.setTagList(ticket.getLotteryTag().get(key));
			} else {
				for (int i = 0; i < cbList.size(); i++) {
					cbList.get(i).setBackgroundDrawable(
							MyApp.res.getDrawable(R.drawable.jc_cb_false));
					cbList.get(i)
							.setTextColor(
									MyApp.res
											.getColor(R.color.item_hall_tv_title_text_color));
				}
				if (ticket.getSelectMessageBean() != null
						&& -1 != ticket.getSelectMessageBean().indexOf(itemMB)) {
					ticket.getSelectMessageBean().remove(itemMB);
				}
			}
			// 子布局 选票场次
			cholder.item_chgp.setOnTextViewGroupListener(new OnTextViewGroup() {
				@Override
				public void setOnTextViewGroup(View v, List<String> tagList,
						List<String> textList) {
					MessageBean itemMB = groupAllArray.get(groupPosition).get(
							childPosition);
					// 投注截止时间 比对 已经finally处理不用为空判断
					if (!FormatUtil.salesThanTimer(itemMB.getJ())) {
						MyToast.showToast(mActivity, "本场比赛投注时间已结束!");
						notifyDataSetChanged();
						return;
					}
					// 控制每场投注场次数
					if (15 > ticket.getSelectMessageBean().size()) {// 客户端取消选票控制转由后台控制最多选票数

						String key = "";
						if (itemMB.getC().length() > 6) {// 为了字符串截取安全
							key = itemMB.getC().subSequence(
									itemMB.getC().length() - 6,
									itemMB.getC().length())
									+ "-" + itemMB.getG();
						}
						if (tagList.size() > 0) {
							ticket.getLotteryTag().put(key, tagList);
							// 移除已存在 重新加载
							if (-1 != ticket.getSelectMessageBean().indexOf(
									itemMB)) {// 移除已经存在的
								ticket.getSelectMessageBean()
										.get(ticket.getSelectMessageBean()
												.indexOf(itemMB))
										.setCH_NUM(tagList.size());
							} else {
								itemMB.setCH_NUM(tagList.size());
								ticket.getSelectMessageBean().add(itemMB);
							}
						} else {
							if (ticket.getLotteryTag() != null
									&& ticket.getLotteryTag().containsKey(key)) {
								ticket.getLotteryTag().remove(key);
							}
							if (ticket.getSelectMessageBean() != null
									&& -1 != ticket.getSelectMessageBean()
											.indexOf(itemMB)) {
								ticket.getSelectMessageBean().remove(itemMB);
							}
						}

						basebuy_tv_notice.setText(Html.fromHtml(ticket
								.getNotice()));
						changeBtnbgAndTextColors(ticket.getSession());
					} else {
						MyToast.showToast(mActivity, "每张选票只能筛选15场比赛!");
						basebuy_tv_notice.setText(Html.fromHtml(ticket
								.getNotice()));
					}
					notifyDataSetChanged();
					shakeListener.vibrate();
				}
			});

			// 是否展开子 item
			cholder.open_show.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					if (lotteryScene.contains(String.valueOf(groupPosition)+ "-" + String.valueOf(childPosition))) {
						lotteryScene.remove(String.valueOf(groupPosition) + "-"+ String.valueOf(childPosition));
					} else {
						lotteryScene.add(String.valueOf(groupPosition) + "-"+ String.valueOf(childPosition));
					}
					notifyDataSetChanged();
				}
			});

			String openStr = String.valueOf(groupPosition) + "-"
					+ String.valueOf(childPosition);
			cholder.item_item_layout.setVisibility(View.GONE);
			Drawable mDrawable = MyApp.res.getDrawable(R.drawable.d1);
			mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(),
					mDrawable.getMinimumHeight());
			cholder.ratio_show_tv.setCompoundDrawables(null, mDrawable, null,
					null);
			if (lotteryScene.contains(openStr)) {
				mDrawable = MyApp.res.getDrawable(R.drawable.u1);
				mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(),
						mDrawable.getMinimumHeight());
				cholder.item_item_layout.setVisibility(View.VISIBLE);
				cholder.ratio_show_tv.setCompoundDrawables(null, mDrawable,
						null, null);
			}
			if (mDialog != null && mDialog.isShowing()) {// 提示框
				mDialog.dismiss();
			}
			return convertView;
		}

		class ChildHolder {
			TextView title, name, session;
			TextView time, xa, xb, xc, r_tv;// 投注比例
			TextView zhu, ke;// 混合过关 名称
			RelativeLayout open_show;
			LinearLayout item_item_layout;
			TextViewGroup item_chgp;
			TextView ratio_show_tv;// 选中效果按钮箭头
			TextView cb3, cb1, cb0;// 胜平负
			TextView r_ch3, r_ch1, r_ch0;// 让球胜平负
			View not_optional_view;

			public ChildHolder(View v, int salesType) {
				switch (salesType) {// 不同玩法
				case 210:// 让球胜平负
					r_tv = (TextView) v.findViewById(R.id.r_tv);
					r_ch3 = (TextView) v.findViewById(R.id.r_ch3);
					r_ch1 = (TextView) v.findViewById(R.id.r_ch1);
					r_ch0 = (TextView) v.findViewById(R.id.r_ch0);
					break;
				case 218:// 胜平负
					cb3 = (TextView) v.findViewById(R.id.cb3);
					cb1 = (TextView) v.findViewById(R.id.cb1);
					cb0 = (TextView) v.findViewById(R.id.cb0);
					break;
				case 209:// 混合过关
					// vs name
					zhu = (TextView) v.findViewById(R.id.zhu);
					ke = (TextView) v.findViewById(R.id.ke);
					// 胜平负
					cb3 = (TextView) v.findViewById(R.id.cb3);
					cb1 = (TextView) v.findViewById(R.id.cb1);
					cb0 = (TextView) v.findViewById(R.id.cb0);
					// 让球胜平负
					r_tv = (TextView) v.findViewById(R.id.r_tv);
					r_ch3 = (TextView) v.findViewById(R.id.r_ch3);
					r_ch1 = (TextView) v.findViewById(R.id.r_ch1);
					r_ch0 = (TextView) v.findViewById(R.id.r_ch0);
					break;
				}
				not_optional_view = v.findViewById(R.id.not_optional_view);
				// 投注比例
				xa = (TextView) v.findViewById(R.id.xa);
				xb = (TextView) v.findViewById(R.id.xb);
				xc = (TextView) v.findViewById(R.id.xc);
				// 左边场次信息描述
				name = (TextView) v.findViewById(R.id.name);
				session = (TextView) v.findViewById(R.id.session);
				time = (TextView) v.findViewById(R.id.time);
				// 公共组
				ratio_show_tv = (TextView) v.findViewById(R.id.ratio_show_tv);
				open_show = (RelativeLayout) v.findViewById(R.id.jc_c_openbtn);
				item_item_layout = (LinearLayout) v
						.findViewById(R.id.item_item_layout);
				item_chgp = (TextViewGroup) v.findViewById(R.id.item_chgp);
			}

			// 默认初始化控件
			public void init(int salesType) {
				switch (salesType) {// 不同玩法
				case 210:// 让球胜平负
					// 默认效果
					r_ch3.setText("");
					r_ch3.setTag("");
					r_ch1.setText("");
					r_ch1.setTag("");
					r_ch0.setText("");
					r_ch0.setTag("");
					r_tv.setText("");
					r_tv.setVisibility(View.INVISIBLE);
					break;
				case 218:// 胜平负
					cb3.setText("");
					cb1.setText("");
					cb0.setText("");
					cb3.setTag("");
					cb1.setTag("");
					cb0.setTag("");
					break;
				case 209:// 混关
					// 默认效果
					cb3.setText("");
					cb3.setTag("");
					cb1.setText("");
					cb1.setTag("");
					cb0.setText("");
					cb0.setTag("");

					r_ch3.setText("");
					r_ch3.setTag("");
					r_ch1.setText("");
					r_ch1.setTag("");
					r_ch0.setText("");
					r_ch0.setTag("");
					r_tv.setText("");
					r_tv.setVisibility(View.INVISIBLE);
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
	public static String getPKName(boolean isChecked, String pk, String spf,
			String sp) {
		String name = "";
		String s = "--";
		if (StringUtils.isBlank(pk)) {
			pk = "--";
		}
		if (StringUtils.isBlank(spf)) {
			spf = "--";
		}
		if (StringUtils.isBlank(sp)) {
			sp = "0.00";
		}
		if (pk.length() > 4) {
			name = pk.substring(0, 4);
		} else {
			name = pk;
		}
		if (isChecked) {
			// <string
			// name="jc_itemui_false"><![CDATA[NAME<br><small>PK_SP</small></br>]]></string>
			s = StringUtils.replaceEach(
					MyApp.res.getString(R.string.jc_itemui_true), new String[] {
							"NAME", "PK_SP" },
					new String[] { name, (spf + sp) });
		} else {
			// <string name="jc_itemui_true"><![CDATA[NAME<br><small>PK<font
			// color="#ef2d33">SP</font></small></br>]]></string>
			s = StringUtils.replaceEach(
					MyApp.res.getString(R.string.jc_itemui_false),
					new String[] { "NAME", "PK", "SP" }, new String[] { name,
							spf, sp });
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
		s = StringUtils.replaceEach(
				MyApp.res.getString(R.string.jc_209item_true), new String[] {
						"NAME", "SP" }, new String[] { name, sp });
		return s;
	}

	/**
	 * 适配器 中用于控制让球tagTextView (让球数,背景区分色)信息----TODO 此处可以抽取为公用工具
	 * 
	 * @param tv
	 * @param L
	 */
	public static void setTagTextViewmsg(boolean isAddText, TextView tv,
			String L) {
		if (StringUtils.isBlank(L) || tv == null)
			return;
		if ("0".equals(L)) {// 0：表示不让 @color/btn_gray_u
			// tv.setBackgroundColor(MyApp.res.getColor(R.color.r_orange));
			tv.setBackgroundDrawable(MyApp.res
					.getDrawable(R.drawable.jc_r_gray));// 灰色
		} else if (L.contains("-")) {// -n：表示主让客 @color/red
			// tv.setBackgroundColor(MyApp.res.getColor(R.color.r_green));
			tv.setBackgroundDrawable(MyApp.res
					.getDrawable(R.drawable.jc_r_green));// 红色
		} else {// n：表示客让主 @color/btn_orange_u
			// tv.setBackgroundColor(MyApp.res.getColor(R.color.r_red));
			tv.setBackgroundDrawable(MyApp.res.getDrawable(R.drawable.jc_r_red));// 红色
		}
		tv.setText("让球\n" + L);
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

		if ("211".equals(salesType)) {
			int session = ticket.getSession1();
			if (0 == session) {
				MyToast.showToast(mActivity, "请至少选择" + childId + "场比赛!");
			} else if ((1 == session)) {
				MyToast.showToast(mActivity, "已选1还差1场比赛!");
			} else {
				bfbean.initSort();
				start(ThirdActivity.class, JcbfBetorder.class, null);
			}
		} else {
			if (ticket.isTicketAvailable()) {
				MyApp.order.getTickets().add(0, ticket);// 为了保证顺序
				MyApp.order.clearNullTicket();// 清空 空场选票
				MyApp.order.setLotteryId(salesType);
				MyApp.order.setAppId(lotteryId);
				Bundle mBundle = new Bundle();
				mBundle.putString("childId", childId);
				start(ThirdActivity.class, JczqBetorder.class, mBundle);
			} else {
				if (0 == ticket.getSession()) {
					MyToast.showToast(mActivity, "请至少选择" + childId + "场比赛!");
				} else {
					MyToast.showToast(mActivity, "已选1还差1场比赛!");
				}
			}
		}
	}

	@Override
	protected void clear() {

		if("211".equals(salesType)) {
			if(bfbean.getPiao()!=null){
				bfbean.getPiao().clear();
			}
			if(bfbean.getMapData()!=null){
				bfbean.getMapData().clear();
				ticket.setSession(bfbean.getMapData().size());
				changeBtnbgAndTextColors(ticket.getSession1());
				basebuy_tv_notice.setText(Html.fromHtml(ticket.getNotice()));
			}
			
			if (mjcAdapter != null) {
				mjcAdapter.notifyDataSetChanged();
			}
		}else{
			initTicket();// 重新创建一个新的ticket
			if (mjcAdapter != null) {
				mjcAdapter.notifyDataSetChanged();
			}
			setNotice();
			changeBtnbgAndTextColors(ticket.getSession());
		}
	}

}
