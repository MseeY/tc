package com.mitenotc.ui.account;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.mitenotc.bean.MessageBean;
import com.mitenotc.enums.CustomTagEnum;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.R;
import com.mitenotc.tc.ThirdActivity;
import com.mitenotc.ui.BodyFragment1;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.utils.SingleLayoutListView;
import com.mitenotc.utils.SingleLayoutListView.OnLoadMoreListener;
import com.mitenotc.utils.SingleLayoutListView.OnRefreshListener;

/**
 * 购买红包 2014-3-5 01:24:26
 * 
 * @author ymx
 * 
 */
public class BuyRedPacket extends BaseFragment{

	private ListView listView;
	private Dialog mDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m_acc_fund_buy_red_packet);
		mDialog=new Dialog(mActivity,R.style.dialog_theme);
		mDialog.setContentView(R.layout.m_wait_dialog);
		mDialog.show();
//		setTitleNav(R.string.acc_title_buy_read_pac, R.drawable.title_nav_back,
//				0);
		setTitleNav(CustomTagEnum.list_redPacket.getId(),R.string.acc_title_buy_read_pac,R.drawable.title_nav_back, R.drawable.title_nav_menu);
		network_notice_required = true;
		initView();
		sendRequest(1104); // 查询红包信息
	}
	private LinearLayout inflate;
	private SingleLayoutListView mListView1;
	private void initView() {
		mListView1 = new SingleLayoutListView(mActivity);
		configListViewPushListener1();
		FrameLayout fl_color_money_card1 = (FrameLayout) findViewById(R.id.fl_color_money_card1);
		inflate = (LinearLayout) View.inflate(mActivity, R.layout.m_acc_fund_manager_null, null);
		TextView text1 = (TextView) inflate.findViewById(R.id.tv_acc_fund_null);
		TextView text2 = (TextView) inflate.findViewById(R.id.tv_acc_fund_buy_lottery);
		text1.setText("红包已抢光，期待下一轮发放");
		text2.setVisibility(View.GONE);
		AccountUtils.configListView(mListView1);
		mListView1.setDividerHeight(0);
		mListView1.setDivider(null);
		fl_color_money_card1.addView(mListView1);
		fl_color_money_card1.addView(inflate);
	}
	
	private void configListViewPushListener1() {
		mListView1.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				sendRequest(1104);
			}
		});
	}
	
	@Override
	protected void onReLogin() {
		super.onReLogin();
		sendRequest(1104);
	}
	
	@Override
	public void onNetworkRefresh() {
		super.onNetworkRefresh();
		sendRequest(1104);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		setTitleNav(CustomTagEnum.list_redPacket.getId(),R.string.acc_title_buy_read_pac,R.drawable.title_nav_back, R.drawable.title_nav_menu);
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
		if(mDialog!=null && mDialog.isShowing()){
			mDialog.dismiss();
		}
		MessageBean messageBean = (MessageBean) msg.obj;
		mList = messageBean.getLIST();
		mListView1.onRefreshComplete();
		if (mList != null) {
			mListView1.setAdapter(new MyAdapter());
			inflate.setVisibility(View.GONE);
		}else{
			inflate.setVisibility(View.VISIBLE);
		}
	}

	class MyAdapter extends BaseListAdapter {

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewRedHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mActivity,R.layout.m_acc_fund_red_packet_item, null);
				holder = new ViewRedHolder();
				holder.red_time = (TextView) convertView.findViewById(R.id.tv_acc_red_time);
				holder.original_price = (TextView) convertView.findViewById(R.id.tv_acc_red_original_price);
				holder.now_price = (TextView) convertView.findViewById(R.id.tv_acc_red_now_price);
				holder.red_pac_name = (TextView) convertView.findViewById(R.id.tv_fund_red_pac_name);
				
				convertView.setTag(holder);
			} else {
				holder = (ViewRedHolder) convertView.getTag();
			}
			
			holder.red_time.setText("");
			holder.original_price.setText("");
			holder.now_price.setText("");
			holder.red_pac_name.setText("");

			MessageBean messageBean = mList.get(position);
			holder.red_time.setText(messageBean.getG());
			holder.original_price.setText("¥"+AccountUtils.fenToyuan(messageBean.getC()));
			holder.now_price.setText("¥"+AccountUtils.fenToyuan(messageBean.getD()));
			holder.red_pac_name.setText(messageBean.getB());
			onItemClick(convertView, messageBean);
			return convertView;
		}
	}
	
	private void onItemClick(View convertView, final MessageBean messageBean) {
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//防止2次点击出现白屏
				if(AccountUtils.isFastClick(mActivity)){
					return;
				}
				Bundle bundle = new Bundle();
				bundle.putString("TCnumber", messageBean.getA());
				bundle.putString("Value", messageBean.getC());
				bundle.putString("Price", messageBean.getD());
				bundle.putString("Number", messageBean.getE());
				start(ThirdActivity.class,BuyRedPacketValue.class, bundle);
			}
		});
	}
	
	class ViewRedHolder {
		TextView red_time;
		TextView original_price;
		TextView now_price;
		TextView red_pac_name;
	}
	
	@Override
	protected void onRightIconClicked() {
		BodyFragment1.addTag(mActivity, CustomTagEnum.list_redPacket.getId());
		setTitleNav(CustomTagEnum.list_redPacket.getId(),R.string.acc_title_buy_read_pac,R.drawable.title_nav_back, R.drawable.title_nav_menu);
	}
}


