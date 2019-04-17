package com.mitenotc.ui.account;

import org.apache.commons.codec.digest.DigestUtils;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.mitenotc.bean.MessageBean;
import com.mitenotc.bean.UserBean;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.tc.SecondActivity;
import com.mitenotc.tc.ThirdActivity;
import com.mitenotc.ui.TCDialogs;
import com.mitenotc.ui.TCDialogs.MyClickedListener;
import com.mitenotc.ui.TCDialogs.OkClickedListener;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.utils.AppUtil;
import com.mitenotc.utils.SPUtil;

/**
 * @author ymx 账户中心：安全与隐私逻辑界面 2014-3-31 11:01:34
 */

public class SafeFragment extends BaseFragment implements OnClickListener {

	private LinearLayout middle_gone;
	private TextView tv_to_perfect;
	private TextView phone_number;
	private Button to_check_phone_number;
	private TextView had_check;
	private LinearLayout ll_to_perfect;
	private TextView ture_name;
	private TextView card_type;
	private TextView card_number;

	private TextView cb_need_paypwd;// 支付时需要密码
	private Button update_pwd;
	private TextView tv_acc_safe_online_ask;
	private TextView tv_btn_tikuan_shuoming;
	private ScrollView sv_safe;
	private LinearLayout ll_acc_fund_extraction_text;
	private Dialog mDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mDialog=new Dialog(mActivity,R.style.dialog_theme);
		mDialog.setContentView(R.layout.m_wait_dialog);
		mDialog.show();
		setContentView(R.layout.m_acc_me_safe2);
		setTitleNav(R.string.acc_title_safe, R.drawable.title_nav_back, 0);
		initView();
		setSafeListener();

	}

	private void setSafeListener() {
		tv_to_perfect.setOnClickListener(this);
		to_check_phone_number.setOnClickListener(this);
		update_pwd.setOnClickListener(this);
		tv_acc_safe_online_ask.setOnClickListener(this);
		tv_btn_tikuan_shuoming.setOnClickListener(this);
	}

	private void initView() {
		ll_acc_fund_extraction_text = (LinearLayout) findViewById(R.id.ll_acc_fund_extraction_text);
		sv_safe = (ScrollView) findViewById(R.id.sv_safe);
		tv_btn_tikuan_shuoming = (TextView) findViewById(R.id.tv_btn_tikuan_shuoming);
		cb_need_paypwd = (TextView) findViewById(R.id.tv_setting_need_paypwd);
		update_pwd = (Button) findViewById(R.id.btn_acc_setting_update_pwd);
		middle_gone = (LinearLayout) findViewById(R.id.ll_acc_middle_gone);
		tv_to_perfect = (TextView) findViewById(R.id.tv_to_perfect);
		phone_number = (TextView) findViewById(R.id.tv_safe_phone_number);
		phone_number.setText(AccountUtils.phoneNumber(UserBean.getInstance()
				.getPhoneNum()));

		// 插入网络请求的数据
		ture_name = (TextView) findViewById(R.id.tv_acc_safe_ture_name);
		card_type = (TextView) findViewById(R.id.tv_acc_safe_card_type);
		card_number = (TextView) findViewById(R.id.tv_acc_safe_card_number);

		// 以下是对页面的初始化操作的控件 (控件的显隐)
		to_check_phone_number = (Button) findViewById(R.id.bt_acc_safe_to_check);
		had_check = (TextView) findViewById(R.id.tv_acc_safe_is_check);
		ll_to_perfect = (LinearLayout) findViewById(R.id.ll_to_perfect);
		tv_acc_safe_online_ask = (TextView) findViewById(R.id.tv_acc_safe_online_ask);
		
		if (!"".equals(SPUtil.getBoolean(MyApp.res.getString(R.string.PAYPWD1),
				true))) {

			if (SPUtil.getBoolean(MyApp.res.getString(R.string.PAYPWD1), true)) {
				AccountUtils.showArrowDrawable(cb_need_paypwd,R.drawable.checkbox_set_select);
			} else {
				AccountUtils.showArrowDrawable(cb_need_paypwd,R.drawable.checkbox_set_unselect);
			}
		} else {
			AccountUtils.showArrowDrawable(cb_need_paypwd,R.drawable.checkbox_set_select);
		}

		String i = UserBean.getInstance().getUserAccountState();
		int code = 0;
		if (!TextUtils.isEmpty(i) || i != null) {
			code = Integer.parseInt(i);
			switch (code) {
			case 0:
				to_check_phone_number.setVisibility(View.VISIBLE); // 立即验证按钮
				had_check.setVisibility(View.GONE); // 手机已验证
				middle_gone.setVisibility(View.GONE); // 如果没有完善那么就gone

				ll_to_perfect.setVisibility(View.VISIBLE);
				// 控件的点击事件
				to_check_phone_number.setOnClickListener(this);
				tv_to_perfect.setOnClickListener(this);

				break;

			case 1:
				to_check_phone_number.setVisibility(View.GONE);
				had_check.setVisibility(View.VISIBLE);

				ll_to_perfect.setVisibility(View.VISIBLE);// 未实名就显示立即完善
				middle_gone.setVisibility(View.GONE);

				break;
			case 2:
				ll_to_perfect.setVisibility(View.GONE);
				middle_gone.setVisibility(View.VISIBLE);

				to_check_phone_number.setVisibility(View.VISIBLE);
				had_check.setVisibility(View.GONE);

				break;
			case 3:
				to_check_phone_number.setVisibility(View.GONE);
				had_check.setVisibility(View.VISIBLE);
				ll_to_perfect.setVisibility(View.GONE);
				middle_gone.setVisibility(View.VISIBLE);

				break;
			case 4:
				break;
			}

			cb_need_paypwd.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Drawable[] compoundDrawables = cb_need_paypwd
							.getCompoundDrawables();
					Drawable d1 = compoundDrawables[2];
					Drawable d2 = MyApp.res
							.getDrawable(R.drawable.checkbox_set_select);

					if (d1.getConstantState().equals(d2.getConstantState())) {
						showPwdDialog(3);
					} else {
						showPwdDialog(4);
					}
				}
			});
		}
	}

	private void showPwdDialog(int what) {
		switch (what) {
		case 3:
			final TCDialogs t3 = new TCDialogs(mActivity);
			t3.popSettingPayPwd(new OkClickedListener() {

				@Override
				public void okClicked() {
					EditText dialog_et_pwd = t3.getDialog_et_pwd();
					if (!AppUtil.isEmpty(dialog_et_pwd.getText().toString())) {
						MessageJson msg = new MessageJson();
						msg.put("A", "5");
						msg.put("B", "0");
						msg.put("C",
								DigestUtils.md5Hex(dialog_et_pwd.getText()
										.toString().trim()));
						submitData(3, 1305, msg);
					} else {
						Toast.makeText(mActivity, "请输入账户密码", Toast.LENGTH_SHORT)
								.show();
					}
				}
			}, new MyClickedListener() {

				@Override
				public void onClick() {
				}
			});
			break;
		case 4:
			final TCDialogs t4 = new TCDialogs(mActivity);
			t4.popSettingPayPwd(new OkClickedListener() {

				@Override
				public void okClicked() {
					EditText dialog_et_pwd = t4.getDialog_et_pwd();
					if (!AppUtil.isEmpty(dialog_et_pwd.getText().toString())) {
						MessageJson msg = new MessageJson();
						msg.put("A", "5");
						msg.put("B", "1");
						msg.put("C",
								DigestUtils.md5Hex(dialog_et_pwd.getText()
										.toString().trim()));
						submitData(4, 1305, msg);
					} else {
						Toast.makeText(mActivity, "请输入账户密码", Toast.LENGTH_SHORT)
								.show();
					}
				}
			}, new MyClickedListener() {

				@Override
				public void onClick() {
				}
			});
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_acc_safe_to_check:
			Bundle mbBundle = new Bundle();
			mbBundle.putInt(MyApp.res.getString(R.string.cmd), 1001);
			start(SecondActivity.class, PhoneVerify.class, mbBundle);
			break;

		case R.id.tv_to_perfect:
			start(SecondActivity.class, Safe1Fragment.class, null);
			break;
		case R.id.btn_acc_setting_update_pwd:
			if (AccountUtils.isFastClick(mActivity)) {
				return;
			}
			Bundle bundle = new Bundle();
			bundle.putInt(MyApp.res.getString(R.string.cmd), 1007);
			start(ThirdActivity.class, ChangePwd.class, bundle);
			break;
			
		case R.id.tv_acc_safe_online_ask:
			if (AccountUtils.isFastClick(mActivity)) {
				return;
			}
			
			start(ThirdActivity.class, ConsultationFragment.class, null);
			break;
		case R.id.tv_btn_tikuan_shuoming:
			switch (ll_acc_fund_extraction_text.getVisibility()) {
			case View.VISIBLE:
				ll_acc_fund_extraction_text.setVisibility(View.GONE);
				tv_btn_tikuan_shuoming.setText("操作说明");
				handler.post(new Runnable() {
				    @Override
				    public void run() {
				    	sv_safe.fullScroll(ScrollView.FOCUS_UP);
				    }
				});
				break;
			case View.GONE:
				ll_acc_fund_extraction_text.setVisibility(View.VISIBLE);
				tv_btn_tikuan_shuoming.setText("收起说明");
				handler.post(new Runnable() {
				    @Override
				    public void run() {
				    	sv_safe.fullScroll(ScrollView.FOCUS_DOWN);
				    }
				});
				break;
			}
			break;
		}
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
	public void onStart() {
		super.onStart();
		sendRequest(1008);
		initView();
		setSafeListener();
	}

	/***************************** 请求网络阶段 *****************************************/

	/** 获取网络数据 */
	private void sendRequest(int key) {
		MessageJson msg = new MessageJson();
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
		case 1008:
			if (messageBean == null) {
				return;
			}
			if (!AppUtil.isEmpty(messageBean.getC())) {
				ture_name.setText(AccountUtils.TureName(messageBean.getC())); // 真实姓名
			}
			card_type.setText(messageBean.getG()); // 证件类型
			if (!"".equals(messageBean.getH())) {
				card_number.setText(messageBean.getH()); // 证件号码
			}
			break;

		case 1305:
			MessageBean msgbean1 = (MessageBean) msg.obj;
			switch (msg.arg1) {
			case 3:
				if ("0".equals(msgbean1.getA())) {
					SPUtil.putBoolean(MyApp.res.getString(R.string.PAYPWD1),
							false);
					AccountUtils.showArrowDrawable(cb_need_paypwd,
							R.drawable.checkbox_set_unselect);
//					SPUtil.putString(MyApp.res.getString(R.string.PAY_PWD_FLAG), "0");
//					SPUtil.putString(MyApp.res.getString(R.string.PAY_PWD_BUY),"");
				}
				break;
			case 4:
				if ("0".equals(msgbean1.getA())) {
					SPUtil.putBoolean(MyApp.res.getString(R.string.PAYPWD1),
							true);
					AccountUtils.showArrowDrawable(cb_need_paypwd,
							R.drawable.checkbox_set_select);
//					SPUtil.putString(MyApp.res.getString(R.string.PAY_PWD_FLAG), "0");
//					SPUtil.putString(MyApp.res.getString(R.string.PAY_PWD_BUY),"");
				}
				break;
			}
			break;
		}
	}
}
