package com.mitenotc.ui.account;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import com.mitenotc.bean.MessageBean;
import com.mitenotc.bean.UserBean;
import com.mitenotc.enums.AccountEnum;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.R;
import com.mitenotc.tc.SecondActivity;
import com.mitenotc.tc.ThirdActivity;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.utils.AppUtil;
import com.mitenotc.utils.FormatUtil;

/**
 * 用户提现 2014-2-14 08:47:38
 * 修改 2014-10-26 18:31:24
 * @author ymx
 * 
 */

public class ExtractionCash extends BaseFragment implements OnClickListener {

	private TextView phone_number;
	private TextView true_name;
	private TextView no_bind_bank_card;
	private LinearLayout show_bank_info;
	private TextView no_bind_true_name;
	private TextView draw_money;
	private TextView select_card;
	private PopupWindow popup;
	// private List<BankBean> bankInfos;
	// private BankBean bankBean;
	private Button extraction_submit; // 提交
	private String bNumber;
	private String bName;
	private EditText input_money; // 用户输入的可提金额
	private EditText input_password;
	private Long server_draw_money; // 服务器返回的可提金额
	private TextView online_ask;
	private TextView tv_bank_card_manager;
	private TextView rl_btn_tikuan_shuoming;
	private LinearLayout ll_acc_fund_extraction_text;
	private ScrollView sv_acc_extraction;
	private Dialog mDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitleNav(R.string.acc_title_extraction_cash,
				R.drawable.title_nav_back, 0);
		setContentView(R.layout.m_acc_fund_extraction_cash1);
		initView();
		setCashListener();

	}
	private Animation shake;
	private void initView() {
		shake = AnimationUtils.loadAnimation(mActivity, R.anim.clause_shake);
		rl_btn_tikuan_shuoming = (TextView) findViewById(R.id.rl_btn_tikuan_shuoming);
		ll_acc_fund_extraction_text = (LinearLayout) findViewById(R.id.ll_acc_fund_extraction_text);
		sv_acc_extraction = (ScrollView) findViewById(R.id.sv_fund_extra_cash);
		phone_number = (TextView) findViewById(R.id.extra_cash_content1);
		true_name = (TextView) findViewById(R.id.extra_cash_content2);
		if ("".equals(UserBean.getInstance().getPhoneNum())
				|| UserBean.getInstance().getPhoneNum() == null) {
			phone_number.setText(UserBean.getInstance().getPhoneNum());
		} else {
			phone_number.setText(AccountUtils.phoneNumber(UserBean
					.getInstance().getPhoneNum()));
		}
		
//		acc_title_text1 = (TextView) findViewById(R.id.acc_title_text1);
//		acc_title_text1.setText("账户：");
//		acc_title_text2 = (TextView) findViewById(R.id.acc_title_text2);
//		acc_title_text2.setText("真实姓名：");
		
		select_card = (TextView) findViewById(R.id.tv_acc_extraction_select_card);

		no_bind_bank_card = (TextView) findViewById(R.id.tv_no_bind_bank_card);
		show_bank_info = (LinearLayout) findViewById(R.id.ll_show_bank_info);
		no_bind_true_name = (TextView) findViewById(R.id.acc_title_content3);
		//本次最多转出多少元
//		draw_money = (TextView) findViewById(R.id.tv_fund_extraction_draw_money);
		server_draw_money = UserBean.getInstance().getAvailableCash();
//		draw_money.setText(FormatUtil.moneyFormat2String(Double
//				.parseDouble(AccountUtils.fenToyuan(server_draw_money + "")))
//				+ "元");

		extraction_submit = (Button) findViewById(R.id.bt_acc_fund_extraction_submit);
		input_money = (EditText) findViewById(R.id.tv_acc_extraction_input_money);
		input_password = (EditText) findViewById(R.id.tv_acc_extraction_input_password);
		input_money.setHint("本次最多转出"+FormatUtil.moneyFormat2String(Double
				.parseDouble(AccountUtils.fenToyuan(server_draw_money + "")))
				+ "元");
//		if ("".equals(UserBean.getInstance().getTrueName())
//				|| UserBean.getInstance().getTrueName() == null) {
//			no_bind_true_name.setVisibility(View.VISIBLE);
//			true_name.setVisibility(View.GONE);
//		} else {
//			no_bind_true_name.setVisibility(View.GONE);
//			true_name.setVisibility(View.VISIBLE);
//			true_name.setText(AccountUtils.TureName(UserBean.getInstance()
//					.getTrueName()));
//		}
		
		online_ask = (TextView) findViewById(R.id.tv_acc_out_online_ask);
//		tv_bank_card_manager = (TextView) findViewById(R.id.tv_bank_card_manager);

	}

	private void setCashListener() {
		rl_btn_tikuan_shuoming.setOnClickListener(this);
		no_bind_bank_card.setOnClickListener(this);
		no_bind_true_name.setOnClickListener(this);
		select_card.setOnClickListener(this);
		extraction_submit.setOnClickListener(this);
		online_ask.setOnClickListener(this);
		input_money.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
				AccountUtils.changedEdittext(extraction_submit, s, count);
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.rl_btn_tikuan_shuoming:
			switch (ll_acc_fund_extraction_text.getVisibility()) {
			case View.VISIBLE:
				ll_acc_fund_extraction_text.setVisibility(View.GONE);
				rl_btn_tikuan_shuoming.setText("操作说明");
				handler.post(new Runnable() {
				    @Override
				    public void run() {
				    	sv_acc_extraction.fullScroll(ScrollView.FOCUS_UP);
				    }
				});
				break;
			case View.GONE:
				ll_acc_fund_extraction_text.setVisibility(View.VISIBLE);
				rl_btn_tikuan_shuoming.setText("收起说明");
				handler.post(new Runnable() {
				    @Override
				    public void run() {
				    	sv_acc_extraction.fullScroll(ScrollView.FOCUS_DOWN);
				    }
				});
				break;
			}
			break;
			
			
//		case R.id.tv_bank_card_manager:
//			if (AccountUtils.isFastClick(mActivity)) {
//				return;
//			}
//			//没有实名制的时候    主动跳转到实名制页面
//			if (AppUtil.isEmpty(UserBean.getInstance().getTrueName())){
//				start(ThirdActivity.class, Safe1Fragment.class, null);
//				return;
//			}
//			start(ThirdActivity.class, BankCardManager.class, null);
//			break;
		case R.id.tv_acc_out_online_ask:
			if (AccountUtils.isFastClick(mActivity)) {
				return;
			}
			start(ThirdActivity.class, ConsultationFragment.class, null);
			break;
		case R.id.acc_title_content3:
			if (AccountUtils.isFastClick(mActivity)) {
				return;
			}
			start(ThirdActivity.class, Safe1Fragment.class, null);
			break;
		case R.id.tv_no_bind_bank_card:
			if (AccountUtils.isFastClick(mActivity)) {
				return;
			}
			try {
				start(ThirdActivity.class, BankCardAdd.class, null);
			} catch (Exception e) {
				return;
			}
			
			break;
		case R.id.tv_acc_extraction_select_card:
			if (AccountUtils.isFastClick(mActivity)) {
				return;
			}
			try {
				showBankDialog();
			} catch (Exception e) {
				return;
			}
			
			break;
		case R.id.bt_acc_fund_extraction_submit:
			if (AccountUtils.isFastClick(mActivity)) {
				return;
			}
			if ("".equals(UserBean.getInstance().getTrueName())
					|| UserBean.getInstance().getTrueName() == null) {
				Toast.makeText(mActivity, "您还未进行实名设置", Toast.LENGTH_SHORT)
						.show();
			}

			String bankCard = select_card.getText().toString().trim();
			String money = input_money.getText().toString().trim();
			String password = input_password.getText().toString().trim();
			
			if (TextUtils.isEmpty(bankCard) || bankCard == null) {
				Toast.makeText(mActivity, "请选择您的银行卡", Toast.LENGTH_SHORT).show();
				select_card.startAnimation(shake);
				return;
			}

			long code = 0;
//			long AvailableCashStart = 0;
			if ((TextUtils.isEmpty(money) || money == null)
					&& (TextUtils.isEmpty(password) || password == null)) {
//				Toast.makeText(mActivity, "请输入提现金额和密码", Toast.LENGTH_SHORT)
//						.show();
				input_money.startAnimation(shake);
				input_password.startAnimation(shake);
			} else if (TextUtils.isEmpty(money) && !TextUtils.isEmpty(password)) {
//				Toast.makeText(mActivity, "请输入您要提现的金额", Toast.LENGTH_SHORT)
//						.show();
				input_money.startAnimation(shake);
			} else if (TextUtils.isEmpty(password) && !TextUtils.isEmpty(money)) {
//				Toast.makeText(mActivity, "请输入账户密码", Toast.LENGTH_SHORT).show();
				input_password.startAnimation(shake);
			} else {
				code = Long.parseLong(AccountUtils.yuanTofen(money));
//				AvailableCashStart = UserBean.getInstance().getAvailableCashStart();
				if (server_draw_money < code) {
					Toast.makeText(mActivity, "提现金额不能大于可提现金额",Toast.LENGTH_SHORT).show();
				} else if (money.startsWith("0")) {
					Toast.makeText(mActivity,"提现金额不能为0元；",Toast.LENGTH_SHORT).show();
					input_money.setText("");
				} else {
					if (password.length() > 5 && password.length() < 17) {
						mDialog=new Dialog(mActivity,R.style.dialog_theme);
						mDialog.setContentView(R.layout.m_wait_dialog);
						mDialog.show();
						sendRequest(1106, bName, bNumber,
								AccountUtils.yuanTofen(input_money.getText()
										.toString().trim()),
								DigestUtils.md5Hex(StringUtils
										.trimToNull(input_password.getText()
												.toString().trim())));
					} else {
						Toast.makeText(mActivity, "您输入的密码不正确",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
			
			break;
		}
	}

	/** 银行卡的 pop窗体 */
	private void showBankDialog() {

		AccountUtils.showArrowDrawable(select_card, R.drawable.arrow_up_black);
		View headerView = View.inflate(mActivity, R.layout.m_acc_extra_cash_item,null);
		// 动态创建ListView列表，并配置ListView
		ListView lv = new ListView(mActivity);
		lv.setBackgroundResource(R.drawable.ed_bg);
		lv.setVerticalScrollBarEnabled(true);
		lv.addHeaderView(headerView);
		if (mList != null) {
			no_bind_bank_card.setVisibility(View.GONE);
			show_bank_info.setVisibility(View.VISIBLE);
			// 初始化适配器
			mAdapter = new MyCardAdapter();
			// 往listView中填充数据
			lv.setAdapter(mAdapter);

			// 设置ListView的 item事件
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					if(position==0){
//						start(ThirdActivity.class, BankCardAdd.class, null);
//						没有实名制的时候    主动跳转到实名制页面
						if (AppUtil.isEmpty(UserBean.getInstance().getTrueName())){
							start(ThirdActivity.class, Safe1Fragment.class, null);
							return;
						}
						start(ThirdActivity.class, BankCardManager.class, null);
						popup.dismiss();
						return;
					}
					
					MessageBean messageBean = mList.get(position-1);
					bNumber = messageBean.getB();
					bName = messageBean.getA();
					if (bNumber != null || bNumber.length() > 4) {
						select_card.setText(bName
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
			popup = new PopupWindow(lv, select_card.getWidth(),
					AppUtil.getDisplayHeight(mActivity) / 3);
			AccountUtils.configPopupWindow(select_card, popup);

			// 对popup窗口进行关闭监听
			popup.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss() {
					// 当窗口关闭时 箭头向下
					AccountUtils.showArrowDrawable(select_card,
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

	/** 获取网络数据 */
	private void sendRequest(int key, String bName, String bNumber,
			String drawMoney, String pwd) {
		MessageJson msg = new MessageJson();
		msg.put("A", bName);
		msg.put("B", bNumber);
		msg.put("C", drawMoney);
		msg.put("D", pwd);
		submitData(0, key, msg);
	}

	/** 操作网络数据 */
	@Override
	protected void onMessageReceived(Message msg) {
		MessageBean messageBean = (MessageBean) msg.obj;
		if(mDialog!=null && mDialog.isShowing()){
			mDialog.dismiss();
		}
		switch (msg.arg2) {
		case 1106:
			if ("0".equals(messageBean.getA())) {
				UserBean.getInstance().setAvailableMoney(Long.parseLong(messageBean.getC()));
				UserBean.getInstance().setAvailableCash(Long.parseLong(messageBean.getD()));
				UserBean.getInstance().setAvailableBalance(Long.parseLong(messageBean.getE()));
				AccountEnum.convertMessage(messageBean.getLIST());
				start(SecondActivity.class, ExtractionSuccess.class, null);
			}
			break;
		case 1008:
			if ("0".equals(messageBean.getA())) {
				mList = messageBean.getLIST();

				if (mList == null || mList.size() == 0) {
					no_bind_bank_card.setVisibility(View.VISIBLE);
					show_bank_info.setVisibility(View.GONE);
				}else{
					no_bind_bank_card.setVisibility(View.GONE);
					show_bank_info.setVisibility(View.VISIBLE);
				}
				
				if(!AppUtil.isEmpty(messageBean.getC())){
					no_bind_true_name.setVisibility(View.GONE);
					true_name.setVisibility(View.VISIBLE);
					true_name.setText(messageBean.getC());
					UserBean.getInstance().setTrueName(messageBean.getC());
				} else {
					no_bind_true_name.setVisibility(View.VISIBLE);
					true_name.setVisibility(View.GONE);
				}
			}
			break;
		}

	}

	@Override
	public void onStart() {
		super.onStart();
		mDialog=new Dialog(mActivity,R.style.dialog_theme);
		mDialog.setContentView(R.layout.m_wait_dialog);
		mDialog.show();
		MessageJson msg = new MessageJson();
		submitData(0, 1008, msg);
		initView();
		select_card.setText("");
		input_money.setText("");
		input_password.setText("");
	}
}
