package com.mitenotc.ui.account;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.graphics.drawable.Drawable;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mitenotc.bean.BankBean;
import com.mitenotc.bean.MessageBean;
import com.mitenotc.bean.UserBean;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.R;
import com.mitenotc.tc.ThirdActivity;
import com.mitenotc.ui.TCDialogs;
import com.mitenotc.ui.TCDialogs.OkClickedListener;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.utils.AppUtil;

/**
 * 2014-3-24 10:34:18
 * 银行卡管理
 * @author ymx
 *
 */
public class BankCardManager extends BaseFragment{
	
	private ListView listView;
	private BaseAdapter mAdapter;
	private TextView tv_bank_header; //当银行卡为空的时候显示的文字
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m_acc_bank_manager);
		setTitleNav1("银行卡管理", R.drawable.title_nav_back,R.drawable.title_nav_add,R.drawable.selector_title_iv_right_bg);
		network_notice_required = true;
//		sendRequest(1008);
		initBankListView();
	}
	
	@Override
	protected void onReLogin() {
		super.onReLogin();
		sendRequest(1008);
	}
	
	@Override
	public void onNetworkRefresh() {
		super.onNetworkRefresh();
		sendRequest(1008);
	}
	
	@Override
	protected void initData() {
	}
	
	@Override    //右标题点击事件
	protected void onRightIconClicked() {
		super.onRightIconClicked();
		start(ThirdActivity.class,BankCardAdd.class,null);
	}
	
	private void initBankListView() {
		listView = (ListView) findViewById(R.id.lv_acc_bank_manager);
		tv_bank_header = (TextView) findViewById(R.id.tv_acc_bank_header);
//		View header = View.inflate(mActivity, R.layout.m_acc_records_header,null);
//		View footer = View.inflate(mActivity, R.layout.m_acc_records_footer,null);
//		listView.addHeaderView(header);
//		listView.addFooterView(footer);
		AccountUtils.configListView(listView);
	}
	
/*********************************** 请求网络阶段 ******************************/		
	
	/** 获取网络数据 */
	private void sendRequest(int key) {
		MessageJson msg = new MessageJson();
	    submitData(0, key, msg);
	}
	
	/** 操作网络数据 */
	@Override
	protected void onMessageReceived(Message msg) {
		MessageBean messageBean = (MessageBean) msg.obj;
		switch (msg.arg2) {
		case 1008:
			mList = messageBean.getLIST();
			//如果不进行为空判断  那么程序会崩溃
			if(mList==null||mList.size()==0){
				tv_bank_header.setVisibility(View.VISIBLE);
				listView.setVisibility(View.GONE);
				return;
			}
				
			tv_bank_header.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
			mAdapter = new MyAdapter();
			listView.setAdapter(mAdapter);
		
			List<BankBean> bankInfos = new ArrayList<BankBean>();
			BankBean bank = null;
			if(UserBean.getInstance().getLogoList().size()==0){
				return;
			}
			List<Map<String, Drawable>> logoList = UserBean.getInstance().getLogoList();
			Map<String, Drawable> map = logoList.get(0);
			Set<String> keySet = map.keySet();
			
			for (int i = 0; i < mList.size(); i++) {
				MessageBean bean = mList.get(i);
				bank = new BankBean();
				if(keySet.contains(bean.getA())){
					bank.setBankName(bean.getA());
					bank.setBankLogo(logoList.get(0).get(bean.getA()));
				}
				bank.setBankNumber(bean.getB());
				bank.setBankProvince(bean.getC());
				bank.setBankCity(bean.getD());
				bankInfos.add(bank);
			}
			// 保存银行卡信息列表
			UserBean.getInstance().setBankInfos(bankInfos);
			
			break;
		case 1010:
				mAdapter.notifyDataSetChanged();
				AccountUtils.showToast(mActivity, "删除成功！");
			break;
		}
	}
	
	private class MyAdapter extends BaseListAdapter{

		@Override
		public int getCount() {
			return mList.size();
		}
		
		List<Map<String, Drawable>> logoList = UserBean.getInstance().getLogoList();
		Map<String, Drawable> map = logoList.get(0);
		Set<String> keySet = map.keySet();
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewBankHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mActivity,R.layout.m_acc_bank_manager_item, null);
				holder = new ViewBankHolder();
				holder.delete_bank_card = (ImageView)convertView.findViewById(R.id.iv_acc_bank_delete);
				holder.itemType = (TextView) convertView.findViewById(R.id.tv_acc_bank_item_type);
				holder.itemNumber = (TextView) convertView.findViewById(R.id.tv_acc_bank_item_number);
				holder.itemProvince = (TextView) convertView.findViewById(R.id.tv_acc_bank_item_province);
				holder.itemCity = (TextView) convertView.findViewById(R.id.tv_acc_bank_item_city);
				holder.itemAdress = (TextView) convertView.findViewById(R.id.tv_bank_adress);
				convertView.setTag(holder);
			}else{
				
				holder = (ViewBankHolder) convertView.getTag();
			}
			
			holder.itemType.setText("");
			holder.itemNumber.setText("");
			holder.itemProvince.setText("");
			holder.itemCity.setText("");
			holder.itemAdress.setText("（未完善）");
			
			MessageBean messageBean = mList.get(position);
			if(keySet.contains(messageBean.getA())){
				holder.itemType.setText(messageBean.getA());
				Drawable d = logoList.get(0).get(messageBean.getA());
				d.setBounds(0, 0, d.getMinimumWidth(), d.getMinimumHeight());
				holder.itemType.setCompoundDrawables(d, null, null, null);
			}
			
			holder.itemNumber.setText(messageBean.getB());
			
			holder.itemProvince.setText(messageBean.getC());
			holder.itemCity.setText(messageBean.getD());
			if(!AppUtil.isEmpty(messageBean.getE())){
				holder.itemAdress.setText(messageBean.getE());
			}
				
			holder.delete_bank_card.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					TCDialogs d = new TCDialogs(mActivity);
					d.popDelBankCard(new OkClickedListener() {
						@Override
						public void okClicked() {
							MessageBean msgBean = mList.get(position);
							MessageJson msg = new MessageJson();
							msg.put("A", msgBean.getA());
							msg.put("B", msgBean.getB());
							submitData(0, 1010, msg);
							mList.remove(position);
						}
					});
				}
			});
			
			return convertView;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}
	}
	
	class ViewBankHolder {
		ImageView delete_bank_card;
		TextView itemType;
		TextView itemNumber;
		TextView itemProvince;
		TextView itemCity;
		TextView itemAdress;
	}

	@Override
	public void onStart() {
		super.onStart();
		setTitleNav("银行卡管理", R.drawable.title_nav_back,R.drawable.title_nav_add);
		sendRequest(1008);
	}
}