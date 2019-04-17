package com.mitenotc.ui.account;
import java.util.regex.Pattern;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;
import com.mitenotc.bean.MessageBean;
import com.mitenotc.bean.UserBean;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.R;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.utils.AppUtil;

/**
 * 实名验证界面
 * 2014-3-31 11:01:22
 * @author ymx
 *
 */

public class Safe1Fragment extends BaseFragment implements OnClickListener {

	private Button saveInfo;
	private TextView safe_spinner;
	private PopupWindow popup;
	private MyAdapter mAdapter;
	private String[] str = new String[] { "身份证", "驾照", "护照"};
	
	private TextView true_name;
	private TextView card_number;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m_acc_me_safe1);
		setTitleNav(R.string.acc_title_safe,R.drawable.title_nav_back, 0);
		initView();
		setSafe1Listener();
	}

	/** 初始化控件*/
	private void initView() {
		saveInfo = (Button) findViewById(R.id.bt_save_true_name_info);
		safe_spinner = (TextView) findViewById(R.id.tv_acc_safe_spinner);
		true_name = (TextView) findViewById(R.id.tv_acc_safe_true_name);
		card_number = (TextView) findViewById(R.id.tv_acc_safe_card_num);
		
	}

	/** 设置监听事件*/
	private void setSafe1Listener() {
		saveInfo.setOnClickListener(this);
		safe_spinner.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_save_true_name_info:
			if (AccountUtils.isFastClick(mActivity)) {
				return;
			}
			sendRequest(1004);
			break;
		case R.id.tv_acc_safe_spinner:
			showTypeDialog();
			break;
		}
	}

	private void showTypeDialog() {
		
		AccountUtils.showArrowDrawable(safe_spinner,R.drawable.arrow_up_black);

		//动态创建ListView列表，并配置ListView
		ListView lv = new ListView(mActivity);
		lv.setBackgroundResource(R.drawable.ed_bg);
		lv.setVerticalScrollBarEnabled(false);
		//初始化适配器
		mAdapter = new MyAdapter();
		//往listView中填充数据
		lv.setAdapter(mAdapter);
		//设置ListView的 item事件
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				safe_spinner.setText(str[position]);
				popup.dismiss();

			}
		});
		
		popup = new PopupWindow(lv, safe_spinner.getWidth(), AppUtil.getDisplayHeight(mActivity)/4);
		
		AccountUtils.configPopupWindow(safe_spinner, popup);

		//对popup窗口进行关闭监听
		popup.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				//当窗口关闭时 箭头向下
				AccountUtils.showArrowDrawable(safe_spinner, R.drawable.arrow_down_black);
			}
		});
	}

	private class MyAdapter extends BaseListAdapter {

		@Override
		public int getCount() {
			return str.length;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(mActivity, R.layout.m_acc_me_safe_item, null);
			TextView tv_text = (TextView) view.findViewById(R.id.tv_listview_item_number);
			tv_text.setText(str[position]);
			return view;
		}
	}
	
	
/*****************************请求网络阶段*****************************************/	
	
	/** 获取网络数据 */
	private void sendRequest(int key) {
		switch (key) {
		case 1004:
			//当输入的参数不为空时 才可以向服务器发送
			if(!TextUtils.isEmpty(true_name.getText())
				&&!TextUtils.isEmpty(safe_spinner.getText())
				&&!TextUtils.isEmpty(card_number.getText())){
				
				//验证护照号校验
				String passport = "^1[45][0-9]{7}|G[0-9]{8}|P[0-9]{7}|S[0-9]{7,8}|D[0-9]+$";
				//身份证号校验
				String idCard = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)";
				//真实姓名校验
				String name = "^[\u4e00-\u9fa5]{0,}$";

				if(!((checkInfo(card_number.getText().toString(),passport)
						||checkInfo(card_number.getText().toString(),idCard)))
						||!checkInfo(true_name.getText().toString(),name)){
					
					Toast.makeText(mActivity, "您输入的信息有误", Toast.LENGTH_SHORT).show();
					return;
				}
				
			MessageJson msg = new MessageJson();
			msg.put("A", true_name.getText().toString().trim());
			if("身份证".equals(safe_spinner.getText().toString().trim())){
				msg.put("E", "1");
			}else if("驾照".equals(safe_spinner.getText().toString().trim())){
				msg.put("E", "2");
			}else if("护照".equals(safe_spinner.getText().toString().trim())){
				msg.put("E", "3");
			}

			msg.put("F", card_number.getText().toString().trim());
			
			submitData(0, key, msg);
			
			}else{
				Toast.makeText(mActivity, "您输入的信息不完整", Toast.LENGTH_SHORT).show();
				return;
			}
			
			break;
		}
	}
	
	private boolean checkInfo(String str,String rex){
		Pattern pattern = Pattern.compile(rex);
		return pattern.matcher(str).matches();
	}
	
	/** 操作网络数据 */
	@Override
	protected void onMessageReceived(Message msg) {
		MessageBean messageBean = (MessageBean) msg.obj;
		switch (msg.arg2) {
		case 1004:
			if("0".equals(messageBean.getA())){
				UserBean.getInstance().setTrueName(true_name.getText().toString().trim());
				UserBean.getInstance().setUserAccountState(messageBean.getC());
				Toast.makeText(mActivity, "保存成功", Toast.LENGTH_SHORT).show();
				finish();
			}
			break;
		}
		
		
	}
}
