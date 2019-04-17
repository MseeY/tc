package com.mitenotc.ui.account;

import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mitenotc.bean.MessageBean;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.tc.ThirdActivity;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.ui.pay.PayMoreway;

/**
 * 2014-3-5 01:25:14 
 * 购买红包的面值
 * @author ymx
 * 
 */
public class BuyRedPacketValue extends BaseFragment implements OnClickListener {

	private Button pac_value_add;
	private Button pac_value_subtract;
	private EditText pac_value_number;
	private Button pac_value_pay;
	private TextView pac_change_number;
	private TextView befor_value;
	private static int editValue = 1; // 红包数量初始化
	private int changeValue = 0;  //红包应付金额
	private Bundle mBundle;
	private TextView now_value;
	private TextView red_pac_number;
	


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m_acc_fund_red_packet_value);
		setTitleNav(R.string.acc_title_buy_read_pac_buy, R.drawable.title_nav_back,0);
		mBundle = getMyBundle();
		// 当页面初始化的时候，重新加载数据
		editValue = 1;
		changeValue = Integer.parseInt(mBundle.getString("Price"));
		initView();
		setValueListener();
	}

	private void initView() {
		befor_value = (TextView) findViewById(R.id.tv_red_packet_befor_value);
		befor_value.setText(AccountUtils.fenToyuan(mBundle.getString("Value"))+"元");
		now_value = (TextView) findViewById(R.id.tv_red_packet_now_value);
		now_value.setText(AccountUtils.fenToyuan(mBundle.getString("Price"))+"元");
		red_pac_number = (TextView) findViewById(R.id.tv_acc_red_pac_value_number);
		red_pac_number.setText(mBundle.getString("Number"));
		pac_value_add = (Button) findViewById(R.id.bt_acc_red_pac_value_add);
		pac_value_subtract = (Button) findViewById(R.id.bt_acc_red_pac_value_subtract);
		pac_value_pay = (Button) findViewById(R.id.bt_acc_red_pac_value_pay);
		pac_value_number = (EditText) findViewById(R.id.et_acc_red_pac_value_number);
		pac_change_number = (TextView) findViewById(R.id.tv_acc_red_pac_change_number);
		pac_change_number.setText(AccountUtils.fenToyuan(mBundle.getString("Price")));
	}

	private void setValueListener() {

		pac_value_add.setOnClickListener(this);
		pac_value_subtract.setOnClickListener(this);
		pac_value_pay.setOnClickListener(this);
		pac_value_number.setCursorVisible(false);
		
		pac_value_number.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pac_value_number.setSelection(pac_value_number.getText().length());
				pac_value_number.setCursorVisible(true);
			}
		});
		
		// 监听输入的文本
		pac_value_number.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				
				pac_value_number.setSelection(pac_value_number.getText().length());
				
				if (TextUtils.isEmpty(s)) {
					pac_change_number.setText("0");
					return;
				} else {
					editValue = Integer.parseInt(String.valueOf(s));
					if (editValue < 0) {
						pac_value_number.setText("0");
						editValue = 0;
					}
				}

				pac_change_number.setText(AccountUtils.fenToyuan(String.valueOf(changeValue
						* editValue)));

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				////System.out.println("改变前的" + s);
			}

			@Override
			public void afterTextChanged(Editable s) {
				////System.out.println("改变后的" + s);

			}
		});
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.bt_acc_red_pac_value_add:
			pac_value_number.setText(String.valueOf(++editValue));
			break;
		case R.id.bt_acc_red_pac_value_subtract:
			pac_value_number.setText(String.valueOf(--editValue));
			break;
		case R.id.bt_acc_red_pac_value_pay:
			//防止2次点击出现白屏
			if(AccountUtils.isFastClick(mActivity)){
				return;
			}
			sendRequest(1105);
			break;
		}
	}
	
/*********************************** 请求网络阶段 ******************************/

	/** 获取网络数据 */
	private void sendRequest(int key) {
		////System.out.println("pac_value_number.getText().toString()"+pac_value_number.getText().toString());
		////System.out.println("pac_change_number.getText().toString()"+AccountUtils.yuanTofen(pac_change_number.getText().toString()));
		////System.out.println("mBundle"+mBundle.get("TCnumber"));
		if(TextUtils.isEmpty(pac_value_number.getText().toString())
				||Integer.parseInt(pac_value_number.getText().toString())==0||pac_value_number.getText().toString().startsWith("0")){
			Toast.makeText(mActivity, "请您输入正确的红包数量", Toast.LENGTH_SHORT).show();
			return;
		}
		
		MessageJson msg = new MessageJson();
		msg.put("A", mBundle.get("TCnumber"));
		msg.put("B", pac_value_number.getText().toString());
		msg.put("C", AccountUtils.yuanTofen(pac_change_number.getText().toString()));
		submitData(0, key, msg);
	}

	/** 操作网络数据 */
	@Override
	protected void onMessageReceived(Message msg) {
		MessageBean messageBean = (MessageBean) msg.obj;
		if("0".equals(messageBean.getA())){
			Bundle bundle = new Bundle();
			bundle.putInt(MyApp.res.getString(R.string.cmd),1105);
			bundle.putString("orderId",messageBean.getC());////System.out.println("pac_change_number.getText().toString() = "+pac_change_number.getText().toString());
			bundle.putString("money",AccountUtils.yuanTofen(pac_change_number.getText().toString()));
			bundle.putString("payDesc","购买红包");//支付项目
			start(ThirdActivity.class, PayMoreway.class,bundle);
		}
	}
}
