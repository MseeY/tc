package com.mitenotc.ui.account;

import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;

import com.mitenotc.bean.MessageBean;
import com.mitenotc.bean.UserBean;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.R;
import com.mitenotc.tc.ThirdActivity;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.utils.AppUtil;

public class PerfectBankInfo extends BaseFragment implements OnClickListener{
	
	private TextView tv_acc_bank_select_card;
	private EditText et_acc_input_bank_num;
	private PopupWindow popup;
	private String bNumber;
	private String bName;
	private String bAdress;
	private TextView tv_no_bind_bank_info;
	private Dialog mDialog;
	private Button bt_save_perfect_bankinfo;
	private Animation shake;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m_acc_bank_manager_perfect);
		setTitleNav("完善银行卡信息", R.drawable.title_nav_back, 0);
		shake = AnimationUtils.loadAnimation(mActivity, R.anim.clause_shake);
		tv_acc_bank_select_card = (TextView) findViewById(R.id.tv_acc_bank_select_card);
		tv_acc_bank_select_card.setOnClickListener(this);
		et_acc_input_bank_num = (EditText) findViewById(R.id.et_acc_input_bank_num);
		tv_no_bind_bank_info = (TextView) findViewById(R.id.tv_no_bind_bank_info);
		tv_no_bind_bank_info.setOnClickListener(this);
		bt_save_perfect_bankinfo = (Button) findViewById(R.id.bt_save_perfect_bankinfo);
		bt_save_perfect_bankinfo.setOnClickListener(this);
	}

	private void sendRequest(int cmd) {
		MessageJson msj = new MessageJson();
		submitData(0, cmd, msj);
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
//		mDialog=new Dialog(mActivity,R.style.dialog_theme);
//		mDialog.setContentView(R.layout.m_wait_dialog);
//		mDialog.show();
		sendRequest(1008);
		tv_acc_bank_select_card.setText("");
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_acc_bank_select_card:
			if (AccountUtils.isFastClick(mActivity)) {
				return;
			}
			try {
				showBankDialog();
			} catch (Exception e) {
				return;
			}
			break;
		case R.id.tv_no_bind_bank_info:
			if (AccountUtils.isFastClick(mActivity)) {
				return;
			}
			start(ThirdActivity.class, BankCardAdd.class, null);
			break;
		case R.id.bt_save_perfect_bankinfo:
			if (AccountUtils.isFastClick(mActivity)) {
				return;
			}
			
			String bankInfoName = et_acc_input_bank_num.getText().toString().trim();
			if(AppUtil.isEmpty(bankInfoName)){
				et_acc_input_bank_num.startAnimation(shake);
				return;
			}
			
			MessageJson msj = new MessageJson();
			msj.put("A", bNumber);
			msj.put("B", bankInfoName);
			submitData(1, 1019, msj);
			mDialog=new Dialog(mActivity,R.style.dialog_theme);
			mDialog.setContentView(R.layout.m_wait_dialog);
			mDialog.show();
			
			break;
		default:
			break;
		}
		
	}
	
	@Override
	protected void onMessageReceived(Message msg) {
		super.onMessageReceived(msg);
		MessageBean messageBean = (MessageBean) msg.obj;
		switch (msg.arg1) {
		case 0:
			if("0".equals(messageBean.getA())){
				
				if (messageBean.getLIST() == null || messageBean.getLIST().size() == 0) {
					tv_no_bind_bank_info.setVisibility(View.VISIBLE);
					tv_acc_bank_select_card.setVisibility(View.GONE);
				}else{
					tv_no_bind_bank_info.setVisibility(View.GONE);
					tv_acc_bank_select_card.setVisibility(View.VISIBLE);
					mList = messageBean.getLIST();
				}
			}
			break;
		case 1:
			if("0".equals(messageBean.getA())){
				Toast.makeText(mActivity, messageBean.getB(), 0).show();
				if(mDialog!=null && mDialog.isShowing()){
					mDialog.dismiss();
				}
				finish();
			}
			break;
		default:
			break;
		}
	}
	
	/** 银行卡的 pop窗体 */
	private void showBankDialog() {

		AccountUtils.showArrowDrawable(tv_acc_bank_select_card, R.drawable.arrow_up_black);
//		View headerView = View.inflate(mActivity, R.layout.m_acc_extra_cash_item,null);
		// 动态创建ListView列表，并配置ListView
		ListView lv = new ListView(mActivity);
		lv.setBackgroundResource(R.drawable.ed_bg);
		lv.setVerticalScrollBarEnabled(true);
//		lv.addHeaderView(headerView);
		if (mList != null) {
//			no_bind_bank_card.setVisibility(View.GONE);
//			show_bank_info.setVisibility(View.VISIBLE);
			// 初始化适配器
			mAdapter = new MyCardAdapter();
			// 往listView中填充数据
			lv.setAdapter(mAdapter);

			// 设置ListView的 item事件
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					
					MessageBean messageBean = mList.get(position);
					bNumber = messageBean.getB();
					bName = messageBean.getA();
					bAdress = messageBean.getE();
					et_acc_input_bank_num.setText(bAdress);
					et_acc_input_bank_num.setSelection(et_acc_input_bank_num.length());
					if (!AppUtil.isEmpty(bNumber) || bNumber.length() > 4) {
						tv_acc_bank_select_card.setText(bName
								+ "             "
								+ "卡尾号："
								+ bNumber.substring(bNumber.length() - 4,
										bNumber.length()));
					} else {
						Toast.makeText(mActivity, "银行卡号有误", Toast.LENGTH_SHORT)
								.show();
					}

					popup.dismiss();

				}
			});
			popup = new PopupWindow(lv, tv_acc_bank_select_card.getWidth(),
					AppUtil.getDisplayHeight(mActivity) / 3);
			AccountUtils.configPopupWindow(tv_acc_bank_select_card, popup);

			// 对popup窗口进行关闭监听
			popup.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss() {
					// 当窗口关闭时 箭头向下
					AccountUtils.showArrowDrawable(tv_acc_bank_select_card,
							R.drawable.arrow_down_black);
				}
			});
		} else {
			AccountUtils.showToast(mActivity, "您的info为空");
		}

	}

	/** 银行卡的适配器 */
	private class MyCardAdapter extends BaseListAdapter {

		@Override
		public int getCount() {
			return mList.size();
		}

		List<Map<String, Drawable>> logoList = UserBean.getInstance()
				.getLogoList();
		Map<String, Drawable> map = logoList.get(0);
		Set<String> keySet = map.keySet();

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewBankHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mActivity,R.layout.m_acc_fund_bank_item, null);
				holder = new ViewBankHolder();
				holder.tv_textCardName = (TextView) convertView.findViewById(R.id.tv_acc_fund_extraction_bankname);
				holder.tv_textCardNumber = (TextView) convertView.findViewById(R.id.tv_acc_fund_bank_number);
				holder.tv_textCardLogo = (ImageView) convertView.findViewById(R.id.iv_acc_fund_extraction_logo);
				convertView.setTag(holder);
			} else {
				holder = (ViewBankHolder) convertView.getTag();
			}

			MessageBean messageBean = mList.get(position);

			if (keySet.contains(messageBean.getA())) {
				holder.tv_textCardName.setText(messageBean.getA());
				holder.tv_textCardLogo.setBackgroundDrawable(logoList.get(0).get(messageBean.getA()));
			}
			String b = messageBean.getB();

			if (!TextUtils.isEmpty(b)) {
				holder.tv_textCardNumber.setText(b.substring(b.length() - 4,
						b.length()));
			} else {
				holder.tv_textCardNumber.setText(b);
			}

			return convertView;
		}
	}

	class ViewBankHolder {
		TextView tv_textCardName;
		TextView tv_textCardNumber;
		ImageView tv_textCardLogo;
	}

}
