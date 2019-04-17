package com.mitenotc.ui.account;

import java.util.List;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mitenotc.bean.MessageBean;
import com.mitenotc.bean.UserBean;
import com.mitenotc.enums.AccountEnum;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.R;
import com.mitenotc.tc.ThirdActivity;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.ui.ui_utils.XListView.IXListViewListener;
import com.mitenotc.utils.FormatUtil;

/**
 * 我的红包
 * 2014-4-12 16:29:33
 * @author ymx
 *
 */
public class MyColorMoneyCard extends BaseFragment{
	
	private TextView money;
	private Button buy_color_card;
	private FrameLayout fund_manager;
	private LinearLayout inflate;
	private TextView color_number;
	private Dialog mDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m_acc_color_money_card1);
		setTitleNav("我的红包", R.drawable.title_nav_back, 0);
		mDialog=new Dialog(mActivity,R.style.dialog_theme);
		mDialog.setContentView(R.layout.m_wait_dialog);
		mDialog.show();
		initView();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		MessageJson msg1 = new MessageJson();
		submitData(0, 1111, msg1);
		MessageJson msg2 = new MessageJson();
		submitData(0, 1100, msg2);
		
	}
	
	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime("刚刚");
	}
	
	private void initView() {
		mListView.setPullLoadEnable(false);
		fund_manager = (FrameLayout) findViewById(R.id.fl_color_money_card);
		AccountUtils.configListView(mListView);
		fund_manager.addView(mListView);
		inflate = (LinearLayout) View.inflate(mActivity, R.layout.m_acc_fund_manager_null, null);
		TextView null_text = (TextView) inflate.findViewById(R.id.tv_acc_fund_null);
		TextView to_buy = (TextView) inflate.findViewById(R.id.tv_acc_fund_buy_lottery);
		null_text.setText("您还没有红包，");
		to_buy.setText("立即购买");
		to_buy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				start(ThirdActivity.class, BuyRedPacket.class, null);
			}
		});
		
		fund_manager.addView(inflate);
		mListView.setXListViewListener(new IXListViewListener() {
			
			@Override
			public void onRefresh() {
				MessageJson msg1 = new MessageJson();
				submitData(0, 1111, msg1);
				onLoad();
			}
			
			@Override
			public void onLoadMore() {
			}
		});
		
		money = (TextView) findViewById(R.id.tv_acc_color_money);
		money.setText("¥"+FormatUtil.moneyFormat2String(Double.parseDouble(AccountUtils.fenToyuan(AccountEnum.redpacket_account.getMoney()+""))));
		buy_color_card = (Button) findViewById(R.id.bt_acc_buy_color_card);
		buy_color_card.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				start(ThirdActivity.class, BuyRedPacket.class, null);
			}
		});
		
		color_number = (TextView) findViewById(R.id.tv_acc_color_money_number);

	}

	/** 操作网络数据 */
	@Override
	protected void onMessageReceived(Message msg) {
		if(mDialog!=null && mDialog.isShowing()){
			mDialog.dismiss();
		}
		MessageBean messageBean = (MessageBean) msg.obj;
		switch (msg.arg2) {
		case 1111:
			mList = messageBean.getLIST();
			if(mList==null){
				color_number.setText("0");
			}else{
				color_number.setText(mList.size()+"");
			}
			
			if(messageBean.getLIST() == null || messageBean.getLIST().size()==0){
				inflate.setVisibility(View.VISIBLE);
				onLoad();
				return;
			}
			
			if(mAdapter == null){
				mListView.setAdapter(mAdapter = new MyAdapter());
			}else {
				mAdapter.notifyDataSetChanged();
			}
			break;
		case 1100:
			UserBean.getInstance().setAvailableMoney(Long.parseLong(messageBean.getC()));
			UserBean.getInstance().setAvailableCash(Long.parseLong(messageBean.getD()));
			UserBean.getInstance().setAvailableBalance(Long.parseLong(messageBean.getE()));
			UserBean.getInstance().setAvailableCashStart(Long.parseLong(messageBean.getF()));
			AccountEnum.convertMessage(messageBean.getLIST());
			money.setText("¥"+FormatUtil.moneyFormat2String(Double.parseDouble(AccountUtils.fenToyuan(AccountEnum.redpacket_account.getMoney()+""))));
			break;
		}
	}
	
	private class MyAdapter extends BaseListAdapter {

		@Override
		public int getCount() {
			
			if(mList.size()==0)
				return 0;
			return mList.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			//复用，节省资源
			ViewColorHolder holder;
			if(convertView == null){
				convertView = View.inflate(mActivity, R.layout.m_acc_color_card_item, null);
				holder = new ViewColorHolder();
//				holder.acc_me_bg = (LinearLayout) convertView.findViewById(R.id.ll_acc_color_bg);
//				holder.item_gone = (LinearLayout) convertView.findViewById(R.id.ll_acc_color_item_gone);
				
				holder.colorName = (TextView) convertView.findViewById(R.id.tv_acc_color_name);
				holder.colorTime = (TextView) convertView.findViewById(R.id.tv_acc_color_time);
				holder.colorMoney = (TextView) convertView.findViewById(R.id.tv_acc_color_money);
				holder.colorMoneyValue = (TextView) convertView.findViewById(R.id.tv_acc_color_money_value);
				holder.colorPlay = (TextView) convertView.findViewById(R.id.tv_acc_color_play);
				
				convertView.setTag(holder);
			}else{
				holder = (ViewColorHolder) convertView.getTag();
			}
			
			//以下三行语句是复用之后的初始化，如果不初始化条目容易错位。
//			holder.item_gone.setVisibility(View.VISIBLE);
//			holder.acc_me_bg.setBackgroundResource(R.drawable.acc_msg_title_bg);
			holder.colorName.setText("");
			holder.colorTime.setText("");
			holder.colorMoney.setText("");
			holder.colorMoneyValue.setText("");
			holder.colorPlay.setText("");
			
			//塞入数据
			MessageBean messageBean = mList.get(position);
			holder.colorName.setText(messageBean.getB());
			holder.colorTime.setText(messageBean.getF());
			holder.colorMoney.setText("¥"+AccountUtils.fenToyuan(messageBean.getD()));
			holder.colorMoneyValue.setText("¥"+AccountUtils.fenToyuan(messageBean.getC()));
			holder.colorPlay.setText(messageBean.getE());
	
//			//给最后一个条目换个背景
//			if (position == getCount() - 1) {
//				holder.item_gone.setVisibility(View.GONE);
//				holder.acc_me_bg.setBackgroundResource(R.drawable.acc_me_white_bg);
//			}
			
			return convertView;
		}
	}
	
	class ViewColorHolder{
//		LinearLayout acc_me_bg;
//		LinearLayout item_gone;
		
		TextView colorName;
		TextView colorTime;
		TextView colorMoney;
		TextView colorMoneyValue;
		TextView colorPlay;
		
	}
}
