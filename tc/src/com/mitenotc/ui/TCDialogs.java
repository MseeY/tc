package com.mitenotc.ui;

import java.util.ArrayList;
import java.util.List;

import com.mitenotc.tc.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TCDialogs extends Dialog {

	private TextView dialog_title_tv;
	private TextView dialog_tv_content;
	private TextView dialog_tv_confirm;
	private ImageView dialog_title_iv_left;
	private ImageView dialog_title_iv_right;
	private Button dialog_btn_first;
	private Button dialog_btn_left;
	private Button dialog_btn_right;
	private Button dialog_btn_third_line;
	private Button dialog_btn_last;
	private EditText dialog_et_pwd;
	private RelativeLayout dialog_show_amount;
	private TextView show_money_amount;
	private TextView show_redpac_amount;
	private LinearLayout show_dialog_title_ll;
	private ListView dialog_listview;
	
	public ListView getDialog_listview() {
		return dialog_listview;
	}

	public TextView getShow_money_amount() {
		return show_money_amount;
	}

	public TextView getShow_redpac_amount() {
		return show_redpac_amount;
	}

	public EditText getDialog_et_pwd() {
		return dialog_et_pwd;
	}
	
	public ImageView getDialog_title_right(){
		return dialog_title_iv_right;
	}

	private List<View> views;
	private RelativeLayout dialog_bg_color;

	public TCDialogs(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		init();
	}

	private void init() {
		setContentView(R.layout.f_dialog_layout);

		initView();
		setListener();
	}

	public TCDialogs(Context context) {
		super(context, R.style.dialog_theme);
		init();
		setCanceledOnTouchOutside(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	private void initView() {
		dialog_title_tv = (TextView) this.findViewById(R.id.dialog_title_tv);
		dialog_tv_content = (TextView) this.findViewById(R.id.dialog_tv_content);
		dialog_tv_confirm = (TextView) this.findViewById(R.id.dialog_tv_confirm);
		dialog_title_iv_left = (ImageView) this.findViewById(R.id.dialog_title_iv_left);
		dialog_title_iv_right = (ImageView) findViewById(R.id.dialog_title_iv_right);
		dialog_btn_first = (Button) findViewById(R.id.dialog_btn_first);
		dialog_btn_left = (Button) findViewById(R.id.dialog_btn_left);
		dialog_btn_right = (Button) findViewById(R.id.dialog_btn_right);
		dialog_btn_third_line = (Button) findViewById(R.id.dialog_btn_third_line);
		dialog_btn_last = (Button) findViewById(R.id.dialog_btn_last);
		dialog_et_pwd = (EditText) findViewById(R.id.dialog_acc_setting_pwd);
		dialog_show_amount = (RelativeLayout) findViewById(R.id.dialog_ll_show_amount);
		show_money_amount = (TextView) findViewById(R.id.payorder_show_amount);
		show_redpac_amount = (TextView) findViewById(R.id.payorder_show_redpac_amount);
		show_dialog_title_ll = (LinearLayout) findViewById(R.id.dialog_title_ll);
		dialog_listview = (ListView) findViewById(R.id.payorder_other_pay_methods);
		dialog_bg_color = (RelativeLayout) findViewById(R.id.dialog_bg_color);

		views = new ArrayList<View>();
		views.add(dialog_title_iv_left);
		views.add(dialog_tv_content);
		views.add(dialog_tv_confirm);
		views.add(dialog_btn_first);
		views.add(dialog_btn_left);
		views.add(dialog_btn_right);
		views.add(dialog_btn_third_line);
		views.add(dialog_btn_last);
		views.add(dialog_et_pwd);
		views.add(dialog_show_amount);
		views.add(dialog_listview);
	}

	private void setListener() {
		dialog_title_iv_right.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}

	public void setListeners(View.OnClickListener btn_first_Listener,
			View.OnClickListener btn_left_Listener,
			View.OnClickListener btn_right_Listener,
			View.OnClickListener btn_third_line_Listener,
			View.OnClickListener btn_last_Listener) {
		setListener(dialog_btn_first, btn_first_Listener);
		setListener(dialog_btn_left, btn_left_Listener);
		setListener(dialog_btn_right, btn_right_Listener);
		setListener(dialog_btn_third_line, btn_third_line_Listener);
		setListener(dialog_btn_last, btn_last_Listener);
	}

	/**
	 * 判断 listener 是否为空,如果不为空则给 btn 设置监听
	 * 
	 * @param btn
	 * @param listener
	 */
	public void setListener(Button btn, View.OnClickListener listener) {
		if (listener != null) {
			btn.setOnClickListener(listener);
		}
	}

	/**
	 * @param title
	 *            dialog的标题
	 * @param btn_first_Listener
	 *            第一个按钮的点击事件 // 添加到我的定制
	 * @param btn_last_Listener
	 *            最后一个按钮的点击事件 // 发送到桌面
	 */
	public void popHallDialog(String title,
			View.OnClickListener btn_first_Listener,
			View.OnClickListener btn_left_Listener,
			View.OnClickListener btn_right_Listener,
			View.OnClickListener btn_third_line_Listener,
			View.OnClickListener btn_last_Listener) {
		refreshWithTitle(title);
		dialog_btn_first.setText("置顶 - " + title);
		dialog_btn_left.setText("上移");
		dialog_btn_left.setBackgroundResource(R.drawable.btn_bg_white_selector);
		dialog_btn_left.setTextColor(Color.parseColor("#000000"));
		dialog_btn_right.setText("下移");
		dialog_btn_right.setBackgroundResource(R.drawable.btn_bg_white_selector);
		dialog_btn_right.setTextColor(Color.parseColor("#000000"));
		dialog_btn_third_line.setText("发送 " + title + "到我的定制");
		dialog_btn_last.setText("添加 " + title + "到我的桌面");
		makeVisible(dialog_btn_first, dialog_btn_last, dialog_btn_left,
				dialog_btn_right, dialog_btn_third_line, dialog_btn_last);
		setListeners(btn_first_Listener, btn_left_Listener, btn_right_Listener,
				btn_third_line_Listener, btn_last_Listener);
		show();
		// 添加到我的定制
//		setListener(dialog_btn_first, btn_first_Listener);
		// 发送到桌面
//		setListener(dialog_btn_last, btn_last_Listener);
	}

	/**
	 * 我的自定的 dialog
	 * 
	 * @param title
	 */
	public void popCustomDialog(String title,
			View.OnClickListener btn_first_Listener,
			View.OnClickListener btn_left_Listener,
			View.OnClickListener btn_right_Listener,
			View.OnClickListener btn_third_line_Listener,
			View.OnClickListener btn_last_Listener) {
		refreshWithTitle(title);
		dialog_btn_first.setText("置顶 - " + title);
		dialog_btn_left.setText("上移");
		dialog_btn_left.setBackgroundResource(R.drawable.btn_bg_white_selector);
		dialog_btn_left.setTextColor(Color.parseColor("#000000"));
		dialog_btn_right.setText("下移");
		dialog_btn_right.setBackgroundResource(R.drawable.btn_bg_white_selector);
		dialog_btn_right.setTextColor(Color.parseColor("#000000"));
		dialog_btn_last.setText("发送 " + title + "到我的桌面");
		makeVisible(dialog_btn_first, dialog_btn_last, dialog_btn_left,
				dialog_btn_right, dialog_btn_third_line, dialog_btn_last);
		setListeners(btn_first_Listener, btn_left_Listener, btn_right_Listener,
				btn_third_line_Listener, btn_last_Listener);
		show();
	}

	private void refreshWithTitle(String title) {
		refresh();
		dialog_title_tv.setText(title);
	}

	private void refreshWithTitle(int title) {
		refresh();
		dialog_title_tv.setText(title);
	}

	public void popExitDialog(View.OnClickListener btn_left_Listener) {
		refreshWithTitle(R.string.dialog_tv_exit_title_text);
		makeVisible(dialog_tv_confirm, dialog_btn_left, dialog_btn_right);
		setListener(dialog_btn_left, btn_left_Listener);
		onCancelClicked();
		show();
	}
	
	
	/**
	 * 右边取消
	 */
	private void onCancelClicked() {
		dialog_btn_right.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				onCancelClick();
			}
		});
	}
	public void  onCancelClick()
	{
		
	}
	/**
	 * 弹出 绑定手机 提示 对话框
	 * 
	 * @param okClickedListener
	 */
	public void popBindNumber(OkClickedListener okClickedListener,
			MyClickedListener cancelListener) {
		refreshWithTitle(R.string.dialog_title_tc);// 把所有的控件设为不可见
		dialog_btn_left.setText(R.string.dialog_to_check);
		dialog_btn_right.setText(R.string.dialog_to_jump);
		makeVisible(dialog_tv_content, dialog_btn_left, dialog_btn_right);// 把要用到的按钮显示出来
		setOkClickListener(okClickedListener);// 设置点击监听
		setMyClickedListener(dialog_btn_right, cancelListener);
		show();// 显示
	}

	public interface MyClickedListener {
		public void onClick();
	}

	/**
	 * Dialog 上的cancel 按钮点击事件触发后自动关闭dialog
	 * 
	 * @param view
	 * @param listener
	 */
	public void setMyClickedListener(View view, final MyClickedListener listener) {
		if (listener != null) {
			view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dismiss();
					listener.onClick();
				}
			});
		}
	}

	public interface OkClickedListener {
		public void okClicked();
	}

	public void setOkClickListener(final OkClickedListener okClickedListener) {
		dialog_btn_left.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TCDialogs.this.dismiss();
				if (okClickedListener != null) {
					okClickedListener.okClicked();
				}
			}
		});
	}

	/**
	 * 设置不可见的控件
	 * 
	 * @param views
	 */
	public void makeVisible(View... views) {
		for (View view : views) {
			view.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 设置不可见的 控件
	 * 
	 * @param views
	 */
	public void makeVisible_Gone(View... views) {
		for (View view : views) {
			view.setVisibility(View.GONE);
		}
	}
	/**
	 * 左 右 两个button 
	 */
	public void phoneVSuccess(String titleStr,String contentStr,
			           String leftbtnStr,String rightbtnStr,
			           MyClickedListener leftListener,MyClickedListener rightListener) {
		refreshWithTitle(titleStr);
		makeVisible(dialog_tv_content,dialog_btn_left,dialog_btn_right);
		setMyClickedListener(dialog_btn_left,leftListener);
		setMyClickedListener(dialog_btn_right,rightListener);
		dialog_tv_content.setText(contentStr);
		dialog_btn_left.setText(leftbtnStr);
		dialog_btn_right.setText(rightbtnStr);
		show();
	}
	
   /**
    * 只有一个 button          --TODO  有改动-万利
    * @param String titleStr,
    * @param String contentStr,
    * @param String buttonStr,
    */
	public void unableChasesDialog(String titleStr,String contentStr,String buttonStr,MyClickedListener  myClickedListener,boolean isv) {
		refreshWithTitle(titleStr);
		makeVisible(dialog_tv_content,dialog_btn_last);
		setMyClickedListener(dialog_btn_last,myClickedListener);
		dialog_btn_last.setText(buttonStr);
		dialog_tv_content.setText(contentStr);
		dialog_title_tv.setGravity(Gravity.CENTER);//内容居中
		dialog_title_tv.setPadding(0, 0, 40, 0);
		dialog_tv_content.setGravity(Gravity.CENTER);//内容居中
		
		if(isv){
			dialog_title_iv_right.setVisibility(View.VISIBLE);	
		}else{
			
			dialog_title_iv_right.setVisibility(View.GONE);
		}
		show();
	}
	/**
	 * 初始化控件为 View.Gone
	 */
	public void refresh() {
		for (View view : views) {
			view.setVisibility(View.GONE);
		}
		dialog_title_iv_left.setVisibility(View.INVISIBLE);
	}

	/***************************** 账户中心所有的弹出框 **********************************/

	/**
	 * 手机号码验证：手机号码验证成功！您的账户将会更加安全！ Verification the phone number.
	 */
	public void popVerPhoneNumber(OkClickedListener okClickedListener,
			MyClickedListener cancelListener) {
		refreshWithTitle(R.string.dialog_title_ver_phonenumber);
		dialog_tv_content.setText(R.string.dialog_tv_ver_phonenumber_text);
		dialog_btn_left.setText(R.string.dialog_btn_start);
		makeVisible(dialog_tv_content, dialog_btn_left, dialog_btn_right);
		setOkClickListener(okClickedListener);
		setMyClickedListener(dialog_btn_right, cancelListener);
		show();
	}

	/**
	 * 手机号码验证：您的账户密码已成功找回！
	 */
	public void popSuccessReturn() {
		refreshWithTitle(R.string.dialog_title_ver_phonenumber);
		dialog_tv_content.setText(R.string.dialog_tv_ver_success_return_text);
		dialog_btn_right.setText(R.string.dialog_btn_close);
		makeVisible(dialog_tv_content, dialog_btn_right);
		show();
	}
	
	/**
	 * 退出登陆
	 */
	public void popLoginOut(OkClickedListener okClickedListener) {
		refreshWithTitle(R.string.dialog_title_loginout);
		dialog_tv_confirm.setText(R.string.dialog_tv_logonout_text);
		makeVisible(dialog_tv_confirm, dialog_btn_left,dialog_btn_right);
		setOkClickListener(okClickedListener);
		onCancelClicked();
		show();
	}

	/**
	 * 手机号码验证：您的账户已修改成功！
	 */
	public void popUpdateSuccess() {
		refreshWithTitle(R.string.dialog_title_ver_phonenumber);
		dialog_tv_content.setText(R.string.dialog_tv_ver_update_success_text);
		dialog_btn_right.setText(R.string.dialog_btn_close);
		makeVisible(dialog_tv_content, dialog_btn_right);
		show();
	}

	

	/**
	 * 销代申请：代销申请需要您进一步完善个人相关信息！ Sell for：Improve relevant information
	 */
	public void popSellApply(OkClickedListener okClickedListener,
			MyClickedListener cancelListener) {
		refreshWithTitle(R.string.dialog_title_sell_apply);
		dialog_tv_content.setText(R.string.dialog_tv_sell_apply_text);
		dialog_btn_left.setText(R.string.dialog_btn_start);
		makeVisible(dialog_tv_content, dialog_btn_left, dialog_btn_right);
		setOkClickListener(okClickedListener);
		setMyClickedListener(dialog_btn_right, cancelListener);
		show();
	}

	/**
	 * 销代申请：销代审核通过，我们会尽快与您开通。 Success
	 */
	public void popApplySuccess(OkClickedListener okClickedListener,
			MyClickedListener cancelListener) {
		refreshWithTitle(R.string.dialog_title_sell_apply);
		dialog_tv_content.setText(R.string.dialog_tv_apply_success_text);
		makeVisible(dialog_tv_content, dialog_btn_left, dialog_btn_right);
		setOkClickListener(okClickedListener);
		setMyClickedListener(dialog_btn_right, cancelListener);
		show();
	}

	/**
	 * 太彩彩票：确认要删除此张银行卡吗？ delete the bank card
	 */
	public void popDelBankCard(OkClickedListener okClickedListener) {
		refreshWithTitle(R.string.dialog_title_tc);
		dialog_tv_confirm.setText(R.string.dialog_tv_del_bankcard_text);
		makeVisible(dialog_tv_confirm, dialog_btn_left, dialog_btn_right);
		setOkClickListener(okClickedListener);
		onCancelClicked();
		show();
	}
	
	/**
	 * 修改昵称
	 * @param okClickedListener
	 * @param cancelListener
	 */
	public void popUpdateName(OkClickedListener okClickedListener,
			MyClickedListener cancelListener){
		refreshWithTitle(R.string.dialog_btn_name_baocun);
		dialog_btn_left.setText(R.string.dialog_btn_ok);
		makeVisible(dialog_et_pwd,dialog_btn_left,dialog_btn_right);
		setOkClickListener(okClickedListener);
		setMyClickedListener(dialog_btn_right, cancelListener);
		show();
	}

	/**
	 * 检测新版本：当前已是最新版本啦！ To check the version
	 */

	public void popCheckVersion() {
		refreshWithTitle(R.string.dialog_title_check_newversion);
		dialog_tv_confirm.setText(R.string.dialog_tv_check_version_text);
		// 根据需求，版本更新完点击“确定”按钮以后还是跳到当前界面，所以把"取消"按钮的text设置成确定。
		dialog_btn_right.setText(R.string.dialog_btn_ok);
		makeVisible(dialog_tv_confirm, dialog_btn_right);
		onCancelClicked();
		show();
	}
	
	/**
	 * 检测到新版本     To check the version
	 */

	public void popHadNewVersion(OkClickedListener okClickedListener,String description){
		refreshWithTitle(R.string.dialog_title_find_newversion);
		dialog_tv_content.setText(description);
		dialog_btn_left.setText(R.string.dialog_btn_download);
		makeVisible(dialog_tv_content,dialog_btn_left,dialog_btn_right);
		setOkClickListener(okClickedListener);
		onCancelClicked();
		show();
	}
	
	public void popUpgrade(OkClickedListener okClickedListener,String description,
			MyClickedListener cancelListener){
		refreshWithTitle(R.string.dialog_title_find_newversion);
		dialog_tv_content.setText(description);
		makeVisible(dialog_tv_content,dialog_btn_left,dialog_btn_right);
		setOkClickListener(okClickedListener);
		setMyClickedListener(dialog_btn_right, cancelListener);
		show();
	}

	/**
	 * 购彩界面,左边的按钮为 取消,右边为确定
	 * @param btn_left_listener  
	 * 
	 * @param btn_left_listener
	 * @param btn_right_listener
	 */
	public void popDeleteConfirm(MyClickedListener btn_left_listener,
			MyClickedListener btn_right_listener) {
		makeVisible(dialog_tv_confirm, dialog_btn_left, dialog_btn_right);
		dialog_btn_left.setText(R.string.dialog_btn_cancel);
		dialog_btn_right.setText(R.string.dialog_btn_ok);
		popDeleteConfirm("提示","清除列表?", btn_left_listener, btn_right_listener);
		show();
	}

	/**
	 * * 左边按钮为取消,右边按钮为确定,中间的内容自定义 
	 * @param title      
	 * @param content    
	 * @param btn_left_listener
	 * @param btn_right_listener
	 */
	public void popDeleteConfirm(String title,String content,
			MyClickedListener btn_left_listener,
			MyClickedListener btn_right_listener) {
		refreshWithTitle(title);
		dialog_title_iv_right.setVisibility(View.INVISIBLE);
		makeVisible(dialog_tv_confirm, dialog_btn_left, dialog_btn_right);
		dialog_tv_confirm.setText(content);
		dialog_btn_left.setText(R.string.dialog_btn_ok);
		dialog_btn_right.setText(R.string.dialog_btn_cancel);
		setMyClickedListener(dialog_btn_right, btn_left_listener);
		setMyClickedListener(dialog_btn_left, btn_right_listener);
		show();
	}
	/**
	 * * 左边按钮为取消,右边按钮为确定,中间的内容自定义 
	 * @param title      
	 * @param content    
	 * @param btn_left_listener
	 * @param btn_right_listener
	 */
	public void popDeleteConfirm(String title,Spannable content,
			MyClickedListener btn_left_listener,
			MyClickedListener btn_right_listener) {
		refreshWithTitle(title);
		dialog_title_iv_right.setVisibility(View.INVISIBLE);
		makeVisible(dialog_tv_confirm, dialog_btn_left, dialog_btn_right);
		dialog_tv_confirm.setText(content);
		dialog_btn_left.setText(R.string.dialog_btn_ok);
		dialog_btn_right.setText(R.string.dialog_btn_cancel);
		setMyClickedListener(dialog_btn_right, btn_left_listener);
		setMyClickedListener(dialog_btn_left, btn_right_listener);
		show();
	}
	/**
	 * 太彩彩票：确认要删除此张银行卡吗？ delete the bank card
	 */
	public void popPushMsg(String leftbtnText,CharSequence msg,OkClickedListener okClickedListener) 
	{
		refreshWithTitle("系统消息");
		
		dialog_tv_confirm.setText(msg);
//		dialog_btn_left.setText("立即查看");
		dialog_btn_left.setText(leftbtnText);
		dialog_btn_right.setText("一会再说");		
		makeVisible(dialog_tv_confirm, dialog_btn_left, dialog_btn_right);
		setOkClickListener(okClickedListener);
		onCancelClicked();
		show();
	}
	
	/**
	 * 支付密码
	 */

	public void popSettingPayPwd(OkClickedListener okClickedListener,
			MyClickedListener cancelListener){
		refreshWithTitle(R.string.app_name);
		dialog_btn_left.setText(R.string.dialog_btn_ok);
		makeVisible(dialog_et_pwd,dialog_btn_left,dialog_btn_right);
		setOkClickListener(okClickedListener);
		setMyClickedListener(dialog_btn_right, cancelListener);
		show();
	}
	/**
	 * 确认 取消订单
	 * @param okClickedListener
	 */
	public void popCancelOrder(OkClickedListener okClickedListener) {
		refreshWithTitle("取消订单");
		dialog_tv_confirm.setText("您确定要取消订单吗");
		makeVisible(dialog_tv_confirm, dialog_btn_left,dialog_btn_right);
		setOkClickListener(okClickedListener);
		onCancelClicked();
		show();
	}
	
	/**
	 * show amount
	 * @param okClickedListener
	 */
	public void popAmount() {
		refreshWithTitle("余额查询");
		makeVisible(dialog_show_amount);
		onCancelClicked();
		show();
	}
	
	public void popPayMethods() {
		refreshWithTitle("请选择其他支付方式");
		dialog_bg_color.setBackgroundColor(Color.WHITE);
		makeVisible_Gone(dialog_title_iv_right,dialog_title_iv_left);
		makeVisible(dialog_listview);
		onCancelClicked();
		show();
	}
}
