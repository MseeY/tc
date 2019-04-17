package com.mitenotc.ui.account;

import java.util.List;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;
import com.mitenotc.bean.MessageBean;
import com.mitenotc.bean.UserBean;
import com.mitenotc.dao.impl.AddressDaoImpl;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.tc.ThirdActivity;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.utils.AppUtil;

/**
 * 涉及到省市联动，利用数据库的原理，根据用户点击查询出所有的省份 在根据省份的名字查询出所有的市
 * 
 * @author ymx
 * 
 */
@SuppressLint("Recycle")
public class BankCardAdd extends BaseFragment implements OnClickListener {

	private TextView bankCard; // 选择的银行卡类型
	private TextView province; // 省份
	private TextView city; // 城市
	private EditText bankNumber; // 输入的银行卡号
	private Button saveInfo;
	private PopupWindow popup;
	private AddressDaoImpl dao = new AddressDaoImpl();
	private List<String> provinceList = dao.findProvince();
	private List<String> cityList;
	private String[] bankCards;
	private TypedArray bankLogos;

	/** 此标记如果为fase，那么第二个城市列表不弹出pop。如果为true，反之 */
	private boolean flag;
	private TextView keep_card_person;
	private TextView no_true_name;
	private EditText et_adress_bank;
	private Dialog mDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m_acc_bank_manager_input);
		setTitleNav("添加银行卡", R.drawable.title_nav_back, 0);
		flag = false;
		initView();
		setBankListener();
	}

	private void initView() {
		keep_card_person = (TextView) findViewById(R.id.tv_acc_bank_keep_card);
		bankNumber = (EditText) findViewById(R.id.et_acc_input_bank_num);
		bankCard = (TextView) findViewById(R.id.tv_acc_bank_select_card);
		province = (TextView) findViewById(R.id.tv_acc_bank_select_province);
		city = (TextView) findViewById(R.id.tv_acc_bank_select_city);
		saveInfo = (Button) findViewById(R.id.bt_save_true_name_info);
		no_true_name = (TextView) findViewById(R.id.tv_bank_no_true_name);
		et_adress_bank = (EditText) findViewById(R.id.et_adress_bank);
		
		String i = UserBean.getInstance().getUserAccountState();
		int code = 0;
		if (!TextUtils.isEmpty(i) && i != null) {
			code = Integer.parseInt(i);
			switch (code) {
			case 0:
				no_true_name.setVisibility(View.VISIBLE);
				keep_card_person.setVisibility(View.GONE);
				break;
			case 1:
				no_true_name.setVisibility(View.VISIBLE);
				keep_card_person.setVisibility(View.GONE);
				break;
			default:
				no_true_name.setVisibility(View.GONE);
				keep_card_person.setVisibility(View.VISIBLE);
				if(!AppUtil.isEmpty(UserBean.getInstance().getTrueName())){
					keep_card_person.setText(AccountUtils.TureName(UserBean.getInstance().getTrueName()));
				}
				break;
			}
		}
	}
	
	@Override
	public void onStart() {
		super.onStart();
		MessageJson msg = new MessageJson();
		submitData(0, 1008, msg);
		initView();
	}

	private void setBankListener() {
		no_true_name.setOnClickListener(this);
		bankCard.setOnClickListener(this);
		province.setOnClickListener(this);
		city.setOnClickListener(this);
		saveInfo.setOnClickListener(this);

		province.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				city.setText("");
				flag = true;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				flag = false;

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_acc_bank_select_card:
			showCardDialog();
			break;
		case R.id.tv_acc_bank_select_province:
			showProvinceDialog();
			break;
		case R.id.tv_acc_bank_select_city:
			if (flag) {
				cityList = dao.findCityByName(province.getText().toString());
				showCityDialog();
			}
			break;
		case R.id.bt_save_true_name_info:
			if(AccountUtils.isFastClick(mActivity)){
				return;
			}
			
			if("".equals(UserBean.getInstance().getTrueName())){
				Toast.makeText(mActivity, "您还未进行实名设置", Toast.LENGTH_SHORT).show();
				return;
			}
			if(AppUtil.isEmpty(bankCard.getText().toString())){
				Toast.makeText(mActivity, "银行卡号不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			if(AppUtil.isEmpty(province.getText().toString())){
				Toast.makeText(mActivity, "省份不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			
			if(AppUtil.isEmpty(city.getText().toString())){
				Toast.makeText(mActivity, "城市不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			
			if(AppUtil.isEmpty(bankNumber.getText().toString().trim())){
				Toast.makeText(mActivity, "银行卡号不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			
			if(AppUtil.isEmpty(et_adress_bank.getText().toString().trim())){
				Toast.makeText(mActivity, "开户行不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			sendRequest(1018, bankCard.getText().toString(), 
								  bankNumber.getText().toString().trim(), 
						          province.getText().toString(), 
						          city.getText().toString(),
						          et_adress_bank.getText().toString().trim());
			break;
			
		//未实名需要点击的按钮	
		case R.id.tv_bank_no_true_name:
			if(AccountUtils.isFastClick(mActivity)){
				return;
			}
			start(ThirdActivity.class,Safe1Fragment.class, null);
			break;
		}
	}

	/** 银行卡的 pop窗体 */
	private void showCardDialog() {

		AccountUtils.showArrowDrawable(bankCard, R.drawable.arrow_up_black);

		// 动态创建ListView列表，并配置ListView
		ListView lv = new ListView(mActivity);
		lv.setBackgroundResource(R.drawable.ed_bg);
		lv.setVerticalScrollBarEnabled(true);
		// 初始化数据一定要放到适配器
		bankCards = MyApp.res.getStringArray(R.array.acc_bank_card);
		bankLogos = MyApp.res.obtainTypedArray(R.array.acc_bank_logo);
		// 初始化适配器
		mAdapter = new MyCardAdapter();
		// 往listView中填充数据
		lv.setAdapter(mAdapter);

		// 设置ListView的 item事件
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				bankCard.setText(bankCards[position]);
				popup.dismiss();

			}
		});
		popup = new PopupWindow(lv, bankCard.getWidth(),
				AppUtil.getDisplayHeight(mActivity) / 3);
		AccountUtils.configPopupWindow(bankCard, popup);

		// 对popup窗口进行关闭监听
		popup.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				// 当窗口关闭时 箭头向下
				AccountUtils.showArrowDrawable(bankCard,
						R.drawable.arrow_down_black);
			}
		});

	}

	/** 弹出省份的popup窗口 */
	private void showProvinceDialog() {

		AccountUtils.showArrowDrawable(province, R.drawable.arrow_up_black);

		// 动态创建ListView列表，并配置ListView
		ListView lv = new ListView(mActivity);
		lv.setBackgroundResource(R.drawable.ed_bg);
		lv.setVerticalScrollBarEnabled(true);

		// 初始化适配器
		mAdapter = new MyProvinceAdapter();
		// 往listView中填充数据
		lv.setAdapter(mAdapter);
		// 设置ListView的 item事件
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				province.setText(provinceList.get(position));
				popup.dismiss();

			}
		});
		popup = new PopupWindow(lv, province.getWidth(),
				AppUtil.getDisplayHeight(mActivity) / 3);
		AccountUtils.configPopupWindow(province, popup);

		// 对popup窗口进行关闭监听
		popup.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				// 当窗口关闭时 箭头向下
				AccountUtils.showArrowDrawable(province,
						R.drawable.arrow_down_black);
			}
		});

	}

	/** 弹出城市的popup窗口 */
	private void showCityDialog() {

		AccountUtils.showArrowDrawable(city, R.drawable.arrow_up_black);

		// 动态创建ListView列表，并配置ListView
		ListView lv = new ListView(mActivity);
		lv.setBackgroundResource(R.drawable.ed_bg);
		lv.setVerticalScrollBarEnabled(true);
		// 初始化适配器
		mAdapter = new MyCityAdapter();
		// 往listView中填充数据
		lv.setAdapter(mAdapter);
		// 设置ListView的 item事件
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				city.setText(cityList.get(position));
				popup.dismiss();

			}
		});
		popup = new PopupWindow(lv, city.getWidth(),
				AppUtil.getDisplayHeight(mActivity) / 3);
		AccountUtils.configPopupWindow(city, popup);

		// 对popup窗口进行关闭监听
		popup.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				// 当窗口关闭时 箭头向下
				AccountUtils.showArrowDrawable(city,
						R.drawable.arrow_down_black);
			}
		});
	}

	/** 银行卡的适配器 */
	private class MyCardAdapter extends BaseListAdapter {

		@Override
		public int getCount() {
			return bankCards.length;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewBankHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mActivity,
						R.layout.m_acc_me_safe_item, null);
				holder = new ViewBankHolder();
				holder.tv_textCard = (TextView) convertView
						.findViewById(R.id.tv_listview_item_number);
				convertView.setTag(holder);
			} else {
				holder = (ViewBankHolder) convertView.getTag();
			}
			
			holder.tv_textCard.setText(bankCards[position]);
			Drawable d = bankLogos.getDrawable(position);
			d.setBounds(0, 0, d.getMinimumWidth(), d.getMinimumHeight());
			holder.tv_textCard.setCompoundDrawables(d, null, null, null);
			return convertView;
		}
	}

	private class MyProvinceAdapter extends BaseListAdapter {

		@Override
		public int getCount() {

			return provinceList.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewBankHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mActivity,
						R.layout.m_acc_me_safe_item, null);
				holder = new ViewBankHolder();
				holder.tv_textPro = (TextView) convertView
						.findViewById(R.id.tv_listview_item_number);
				convertView.setTag(holder);
			} else {
				holder = (ViewBankHolder) convertView.getTag();
			}

			holder.tv_textPro.setText(provinceList.get(position));
			return convertView;
		}
	}

	private class MyCityAdapter extends BaseListAdapter {

		@Override
		public int getCount() {

			return cityList.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewBankHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mActivity,
						R.layout.m_acc_me_safe_item, null);
				holder = new ViewBankHolder();
				holder.tv_textCity = (TextView) convertView.findViewById(R.id.tv_listview_item_number);
				convertView.setTag(holder);
			} else {
				holder = (ViewBankHolder) convertView.getTag();
			}

			holder.tv_textCity.setText(cityList.get(position));
			return convertView;
		}
	}

	class ViewBankHolder {
		TextView tv_textPro;
		TextView tv_textCity;
		TextView tv_textCard;
	}

	/*********************************** 请求网络阶段 ******************************/

	/** 获取网络数据 */
	private void sendRequest(int key,String bc,String bn,String p,String c,String ee) {
		mDialog=new Dialog(mActivity,R.style.dialog_theme);
		mDialog.setContentView(R.layout.m_wait_dialog);
		mDialog.show();
		MessageJson msg1 = new MessageJson();
		switch (key) {
		case 1018:
			JSONArray jsonArray = new JSONArray();
			JSONObject bankJson = new JSONObject();
			try {
				bankJson.put("A", bc);
				bankJson.put("B", bn);
				bankJson.put("C", p);
				bankJson.put("D", c);
				bankJson.put("E", ee);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			jsonArray.put(bankJson);
			msg1.put("LIST", jsonArray);
			submitData(0, key, msg1);
			break;
		}
	}

	/** 操作网络数据 */
	@Override
	protected void onMessageReceived(Message msg) {
		MessageBean messageBean = (MessageBean) msg.obj;
		switch (msg.arg2) {
		case 1018:
			if ("0".equals(messageBean.getA())) {
				if(mDialog!=null && mDialog.isShowing()){
					mDialog.dismiss();
				}
				finish();
				Toast.makeText(mActivity, "添加银行卡成功", Toast.LENGTH_SHORT).show();
			}
			break;
		case 1008:
			if ("0".equals(messageBean.getA())) {
				if(!AppUtil.isEmpty(messageBean.getC())){
					keep_card_person.setText(messageBean.getC());
				}
			}
			break;
		}
	}
}
