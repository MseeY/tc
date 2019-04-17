package com.mitenotc.ui.account;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.mitenotc.bean.MessageBean;
import com.mitenotc.bean.UserBean;
import com.mitenotc.enums.AccountEnum;
import com.mitenotc.enums.CustomTagEnum;
import com.mitenotc.net.MessageJson;
import com.mitenotc.net.Protocol;
import com.mitenotc.tc.ConstantValue;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.tc.ThirdActivity;
import com.mitenotc.ui.BodyFragment1;
import com.mitenotc.ui.TCDialogs;
import com.mitenotc.ui.TCDialogs.MyClickedListener;
import com.mitenotc.ui.TCDialogs.OkClickedListener;
import com.mitenotc.ui.TCWebFragment;
import com.mitenotc.ui.account.view.ActionSheetDialog;
import com.mitenotc.ui.account.view.ActionSheetDialog.OnSheetItemClickListener;
import com.mitenotc.ui.account.view.ActionSheetDialog.SheetItemColor;
import com.mitenotc.ui.account.view.RoundImageView;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.ui.register.Registermain;
import com.mitenotc.utils.AppUtil;
import com.mitenotc.utils.FormatUtil;
import com.mitenotc.utils.SPUtil;

/**
 * 账户中心 ---我
 * 2014-3-17 14:41:22
 * @author ymx v 2.0 
 * 修改 2014-10-24 15:59:58
 * 
 */

public class MeFragment extends BaseFragment implements OnClickListener{
	
	/** 控制登陆的组件 */
	private Button main_login;
	private LinearLayout foget_pwd;
	private LinearLayout free_regist;
	
	/** 控制两个界面的显示与隐藏*/
	private LinearLayout visibOrGone;
	private ListView listView;

	/** 控制账户中心的组件*/
	private TextView lotteryName;
	private TextView redPackage;
	private TextView phoneNumber;
	private Button bt_loginout;
	boolean update_mode = false;  //控制ListView的header是否可点击
	/**
	 *  用户等级
	 *   L1 对应用户等级(0-20)
	 *   L2对应用户等级(21-40)
	 *   L3 对应用户等级(41-60)
	 *   L4 对应用户等级(61-80)
	 *   L5 对应用户等级(81-100)
	 */
	private TextView DJ_0;
	private TextView DJ_1;
	private TextView DJ_2;
	private TextView DJ_3;
	private TextView DJ_4;
	
	private TextView userRating;
	private TextView userIntegral;
	
	/** 控制用户头像的组件*/
	private RoundImageView photo;   //用户头像
	private TypedArray userPhotos;
	private TypedArray meLogos;
	private String[] titles;
	private LinearLayout integral;

	private TextView cash_account;
	private LinearLayout change_integral;
	private ImageView find_integral_close;
	private TextView find_integral_balance;
	private EditText find_integral_input;
	private Button find_Ok;
	private Button find_cancel;
	private Animation shake;
	private Dialog mDialog;
	private LinearLayout btn_name;
	private View header_line;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m_acc_me_list);
		visibOrGone = (LinearLayout) findViewById(R.id.ll_acc_main_login);
		listView = (ListView) findViewById(R.id.lv_acc_me_list);
		titles = MyApp.res.getStringArray(R.array.acc_me_title);
		meLogos = MyApp.res.obtainTypedArray(R.array.acc_me_title_img);
		userPhotos = MyApp.res.obtainTypedArray(R.array.acc_user_photos);
		main_login = (Button) findViewById(R.id.bt_acc_main_login);
		foget_pwd = (LinearLayout) findViewById(R.id.ll_acc_foget_pwd);
		free_regist = (LinearLayout) findViewById(R.id.ll_acc_free_regist);
		main_login.setOnClickListener(this);
		foget_pwd.setOnClickListener(this);
		free_regist.setOnClickListener(this);
		
		if (UserBean.getInstance().isLogin()) {
			initCreate();
		}else{
			startLoginForResult();
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(userPhotos!=null){
			userPhotos.recycle();
		}
		if(meLogos!=null){
			meLogos.recycle();
		}
	}
	
	@Override
	public void onStart() {
		super.onStart();
		shake = AnimationUtils.loadAnimation(mActivity, R.anim.clause_shake);
		setTitleNav(CustomTagEnum.user.getId(),R.string.title_nav_user, 0, R.drawable.title_nav_menu);
		if(UserBean.getInstance().isLogin() && !AppUtil.isEmpty(Protocol.getInstance().getUSR())){
			sendRequest(1008);
			sendRequest(1015);
		}
	}
	
	@Override
	protected void onReLogin() {
		initCreate();
	}
	
	private IntegralDialogs dialog;
	private void showIntegralDialog() {
		dialog = new IntegralDialogs(mActivity);
		dialog.setContentView(integral);
		dialog.show();
	}

	private void showPhotoDialog() {
		
		final PhotoDialogs dialog = new PhotoDialogs(mActivity);
		GridView grid_photo = (GridView) View.inflate(mActivity, R.layout.m_acc_me_photo_grid, null);
		mAdapter = new MyPhotoAdapter();
		grid_photo.setAdapter(mAdapter);
		grid_photo.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				photo.setImageDrawable(userPhotos.getDrawable(position));
				SPUtil.putString(getString(R.string.USERPHOTO),position+"");
				File myCaptureFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/taicai/header.png");
				if(myCaptureFile.exists()){
					MyApp.header_photo = null;
					myCaptureFile.delete();
				}
				dialog.dismiss();
			}
		});
		dialog.setContentView(grid_photo);
		dialog.show();
	}
	
	/** 银行卡的适配器 */
	private class MyPhotoAdapter extends BaseListAdapter {

		@Override
		public int getCount() {
			return userPhotos.length();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewPhotoHolder holder = null;
			if (convertView == null) {
				convertView = View.inflate(mActivity,R.layout.m_acc_me_photo_item, null);
				holder = new ViewPhotoHolder();
				holder.iv_photo = (ImageView) convertView.findViewById(R.id.iv_acc_me_photo_item);
				convertView.setTag(holder);
			} else {
				holder = (ViewPhotoHolder) convertView.getTag();
			}
			
			holder.iv_photo.setImageDrawable(userPhotos.getDrawable(position));
			return convertView;
		}
	}
	
	class ViewPhotoHolder{
		ImageView iv_photo;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (UserBean.getInstance().isLogin()) {
			sendRequest(1100); //数据的实时，在登陆以后保存金额之类的数据
//			lotteryName.setText(UserBean.getInstance().getLotteryName());
			String uname = SPUtil.getString(MyApp.res.getString(R.string.uname));
			if(!AppUtil.isEmpty(uname)){
				lotteryName.setText(uname);
			}
			phoneNumber.setText(AccountUtils.phoneNumber(UserBean.getInstance().getPhoneNum()));
			visibOrGone.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
			listView.setSelection(0);
		}else{
			setTitleNav(R.string.user_login, 0, 0);
			visibOrGone.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
		}
	}
	
	
	
	@Override
	protected void initData() {
	}
	
	public void initCreate() {////System.out.println("UserBean.getInstance().isLogin() ===== "+UserBean.getInstance().isLogin());
		setTitleNav(CustomTagEnum.user.getId(),R.string.title_nav_user, 0, R.drawable.title_nav_menu);
//		setTitleNav("我",0,0);
		initListView();// 初始化ListView
		initView();// 初始化字体（这里需要注意，初始化内容的时候需要写在initListView的下面）
		setMeListener();
		setListViewItemListener();// 给listView条目(item)设置监听事件
		if(UserBean.getInstance().isLogin() && !AppUtil.isEmpty(Protocol.getInstance().getUSR())){
			sendRequest(1008);
			sendRequest(1015);
		}
			
	}

	private void setListViewItemListener() {
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				// 注意：这里的case不能从0开始，ListView的header也算一个item。
				case 1:
					start(ThirdActivity.class, RechargeFragment.class, null);
					break;
				case 2:
//					start(ThirdActivity.class, ExtractionCash.class,null);
					start(ThirdActivity.class, ExtractionFragment.class, null);
					break;
				case 3:
//					start(ThirdActivity.class, MyColorMoneyCard.class,null);
					start(ThirdActivity.class, BettingRecords.class, null);
					break;
				case 4:
//					start(ThirdActivity.class, BankCardManager.class, null);
					start(ThirdActivity.class, FundManager.class, null);
					break;
//				case 5:
//					start(ThirdActivity.class, MessageCenter.class, null);
//					break;
				case 5:
					mDialog=new Dialog(mActivity,R.style.dialog_theme);
					mDialog.setContentView(R.layout.m_wait_dialog);
					mDialog.show();
					sendRequest(1359);
					break;
//				case 7:
//					start(ThirdActivity.class, ConsultationFragment.class,null);
//					break;
				case 6:
					start(ThirdActivity.class, SafeFragment.class, null);
					break;
				case 7:
					start(ThirdActivity.class, SettingFragment.class, null);
					break;
				}
			}
		});
	}

	private void initView() {
		mDialog=new Dialog(mActivity,R.style.dialog_theme);
		mDialog.setContentView(R.layout.m_wait_dialog);
		photo = (RoundImageView) findViewById(R.id.iv_acc_me_photo);
		photo.setOnClickListener(this);
		
		try {
	
		if(!"".equals(SPUtil.getString(R.string.USERPHOTO))){
			photo.setImageDrawable(userPhotos.getDrawable(Integer.parseInt(SPUtil.getString(R.string.USERPHOTO))));
		}else if(MyApp.header_photo !=null){
			Bitmap header_photo = MyApp.header_photo;
			Drawable drawale = new BitmapDrawable(header_photo);
			photo.setImageDrawable(drawale);
		}else{
			photo.setImageResource(R.drawable.acc_me_header_logo);
		}
		
		} catch (Exception e) {
			Toast.makeText(mActivity, "重进一下吧", Toast.LENGTH_SHORT).show();
		}
		
		userRating = (TextView) findViewById(R.id.tv_acc_me_rating);
		userRating.setText(UserBean.getInstance().getUserRating());
		lotteryName = (TextView) findViewById(R.id.tv_acc_me_lottery_name);
		header_line = findViewById(R.id.header_line);
		btn_name = (LinearLayout) findViewById(R.id.ll_acc_me_name_btn);
		lotteryName.setText(UserBean.getInstance().getLotteryName());
//		update_name = (EditText) findViewById(R.id.et_acc_me_update_name);
//		accountBalance = (TextView) findViewById(R.id.tv_acc_me_account);
		redPackage = (TextView) findViewById(R.id.tv_acc_me_red_package);
		
		change_integral = (LinearLayout) findViewById(R.id.ll_acc_change_integral);
		change_integral.setOnClickListener(this);
		integral = (LinearLayout) View.inflate(mActivity, R.layout.m_acc_me_change_integral, null);
		find_integral_close = (ImageView) integral.findViewById(R.id.iv_integral_close);
		find_integral_close.setOnClickListener(this);
		find_integral_balance = (TextView) integral.findViewById(R.id.tv_acc_integral_balance);
		find_integral_balance.setText(AccountUtils.fenToyuan(AccountEnum.point_account.getMoney()+""));
		find_integral_input = (EditText) integral.findViewById(R.id.et_acc_integral_input);
		find_Ok = (Button) integral.findViewById(R.id.bt_acc_integral_Ok);
		find_Ok.setOnClickListener(this);
		find_cancel = (Button) integral.findViewById(R.id.bt_acc_integral_cancel);
		find_cancel.setOnClickListener(this);
		
		
		phoneNumber = (TextView) findViewById(R.id.tv_acc_me_phone);
		phoneNumber.setText(AccountUtils.phoneNumber(UserBean.getInstance().getPhoneNum()));
		
		userIntegral = (TextView) findViewById(R.id.tv_acc_me_integral);
		redPackage.setText("¥"+FormatUtil.moneyFormat2String(Double.parseDouble(AccountUtils.fenToyuan(AccountEnum.redpacket_account.getMoney()+""))));
		userIntegral.setText(AccountUtils.fenToyuan(AccountEnum.point_account.getMoney()+""));
//		accountBalance.setText("¥"+FormatUtil.moneyFormat2String(Double.parseDouble(AccountUtils.fenToyuan(UserBean.getInstance().getAvailableBalance()+""))));
		cash_account = (TextView) findViewById(R.id.tv_cash_account);
		cash_account.setText("¥"+FormatUtil.moneyFormat2String(Double.parseDouble(AccountUtils.fenToyuan(UserBean.getInstance().getAvailableMoney()+""))));
		
	
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_acc_main_login:
			start(ThirdActivity.class, LoginFragment.class ,null);
			break;
		case R.id.ll_acc_foget_pwd:
			Bundle bundle=new Bundle();
			bundle.putInt(MyApp.res.getString(R.string.cmd), 1009);
			start(ThirdActivity.class, ForgetPwd.class, bundle);
			break;
			//免费注册
		case R.id.ll_acc_free_regist:
			start(ThirdActivity.class, Registermain.class, null);
			break;
			//用户头像
		case R.id.iv_acc_me_photo:
			new ActionSheetDialog(mActivity)
			.builder()
			.setCancelable(true)
			.setCanceledOnTouchOutside(true)
			.addSheetItem("内置头像", SheetItemColor.Blue,
					new OnSheetItemClickListener() {
						@Override
						public void onClick(int which) {
							showPhotoDialog();
						}
					})
			.addSheetItem("拍照", SheetItemColor.Blue,
					new OnSheetItemClickListener() {
						@Override
						public void onClick(int which) {
							Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
							//下面这句指定调用相机拍照后的照片存储的路径
							intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"/taicai/header.jpg")));
							startActivityForResult(intent, 2);
						}
					})
			.addSheetItem("从相册选择", SheetItemColor.Blue,
					new OnSheetItemClickListener() {
						@Override
						public void onClick(int which) {
							Intent intent = new Intent(Intent.ACTION_PICK, null);
							intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
							intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"/taicai/header.jpg")));
							startActivityForResult(intent, 1);
						}
					}).show();
			break;
		case R.id.ll_acc_change_integral:
			showIntegralDialog();
			break;
		case R.id.iv_integral_close:
			dialog.dismiss();
			break;
		case R.id.bt_acc_integral_cancel:
			dialog.dismiss();
			break;
		case R.id.bt_acc_integral_Ok:
			String integralValue = find_integral_input.getText().toString().trim();
			if(AppUtil.isEmpty(integralValue)){
				find_integral_input.startAnimation(shake);
				return;
			}
			if(integralValue.startsWith("0")){
				Toast.makeText(mActivity, "请输入正确的金额", Toast.LENGTH_SHORT).show();
				return;
			}
			
			MessageJson msg = new MessageJson();
			msg.put("A", integralValue+"00000");
			submitData(0, 1102, msg);
			break;
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		// 如果是直接从相册获取
		case 1:
			try {
				if(data==null){
					return;
				}
				startPhotoZoom(data.getData());
			} catch (Exception e) {
				return;
			}
			
			break;
		// 如果是调用相机拍照时
		case 2:
			try {
				File temp = new File(Environment.getExternalStorageDirectory()
						+ "/taicai/header.jpg");
				if(!temp.exists()){
					return;
				}
				startPhotoZoom(Uri.fromFile(temp));
			} catch (Exception e) {
				return;
			}
			
			break;
		// 取得裁剪后的图片
		case 3:
			try {
				if(data != null){
					setPicToView(data);
				}
				
			} catch (Exception e) {
				return;
			}
			break;
		}
	}
	
	/**
	 * 裁剪图片方法实现
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}
	
	/**
	 * 保存裁剪之后的图片数据
	 * @param picdata
	 */
	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			Bitmap photo1 = extras.getParcelable("data");
			Drawable drawable = new BitmapDrawable(photo1);
			photo.setImageDrawable(drawable);
			SPUtil.putString(R.string.USERPHOTO, "");//清空photo
			MyApp.header_photo = photo1;
			savePhotoToSD(photo1);
			File temp = new File(Environment.getExternalStorageDirectory()+ "/taicai/header.jpg");
			if(temp.exists()){
				temp.delete();
			}
		}
	}
	
	private void savePhotoToSD(Bitmap photo1){
		File myCaptureFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/taicai/header.png");
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        photo1.compress(Bitmap.CompressFormat.PNG, 80, bos);
        try {
			bos.flush();
			bos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public boolean nameTag = false;
	
	private String updateNewName;

	private void setMeListener() {
		
//		click_Recharge.setOnClickListener(this);
//		click_Redpacket.setOnClickListener(this);
		
//		update_name.addTextChangedListener(new TextWatcher() {
//			
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before, int count) {
//				if(s.length()>7){
//					nameTag = true;
//					Toast.makeText(mActivity, "您的昵称过长，请重新输入。", Toast.LENGTH_SHORT).show();
//					return;
//				}
//				
//				if(s.length()<7){
//					nameTag = false;
//				}
//			}
//			
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void afterTextChanged(Editable s) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		
		btn_name.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
//				photo.setClickable(false);
				
				TCDialogs dialog = new TCDialogs(mActivity);
				final EditText dialog_name = dialog.getDialog_et_pwd();
				dialog_name.setHint("请您写一段话,为自己喝彩！");
				dialog_name.setInputType(InputType.TYPE_CLASS_TEXT);
				
				dialog.popUpdateName(new OkClickedListener() {
					
					@Override
					public void okClicked() {
						updateNewName = dialog_name.getText().toString().trim();
						if(updateNewName.getBytes().length > 15*3){
							Toast.makeText(mActivity, "您的个性签名太长!",Toast.LENGTH_SHORT).show();
							return;
						}
						
						if(AppUtil.isEmpty(updateNewName)){
							Toast.makeText(mActivity, "签名不能为空!",Toast.LENGTH_SHORT).show();
							return;
						}
						//往服务端发送请求   判断彩名是否可用
						MessageJson msg = new MessageJson();
						msg.put("A",updateNewName);
						submitData(0, 1006, msg);//查询彩名是否有重复
					}
				}, new MyClickedListener() {
					
					@Override
					public void onClick() {
						// TODO Auto-generated method stub
						
					}
				});
			}
		});

	}
	
	TextView sales_agent_title;
	RelativeLayout sales_agent_btn;
	private void initListView() {
		
		View headerView = View.inflate(mActivity, R.layout.m_acc_me_header1,null);
		View footerView = View.inflate(mActivity, R.layout.m_acc_me_footer,null);
		sales_agent_title = (TextView) headerView.findViewById(R.id.tv_sales_agent_title);
		sales_agent_btn = (RelativeLayout)headerView.findViewById(R.id.rl_sales_agent_btn);
		
		DJ_0= (TextView) headerView.findViewById(R.id.dj_0);
	    DJ_1 = (TextView) headerView.findViewById(R.id.dj_1);
		DJ_2 = (TextView) headerView.findViewById(R.id.dj_2);
		DJ_3 = (TextView) headerView.findViewById(R.id.dj_3);
		DJ_4 = (TextView) headerView.findViewById(R.id.dj_4);
		
		String strDJ=UserBean.getInstance().getUserRating();
//		String strDJ="3";//测试
		//System.out.println("strDJ--------->"+strDJ);// 等级字符串
		formatLevel(strDJ);
		
//		 wanli 添加 点击进入我的红包
		headerView.findViewById(R.id.redPacket_llt).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				start(ThirdActivity.class, MyColorMoneyCard.class,null);
			}
		});
		bt_loginout = (Button) footerView.findViewById(R.id.bt_acc_me_footer);
		bt_loginout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(AccountUtils.isFastClick(mActivity)){
					return;
				}
				TCDialogs t = new TCDialogs(mActivity);
				t.popLoginOut(new OkClickedListener() {
					@Override
					public void okClicked() {
						mDialog.show();
						sendRequest(1003);
					}
				});
			}
		});
		
		if(listView.getHeaderViewsCount()<=0){
			listView.addHeaderView(headerView, null, false); // 这种方式加载让header失去点击事件
			listView.addFooterView(footerView);
		}
		
//		listView.setSelector(new ColorDrawable(0x00000000)); // 设置item的点击事件的背景
//		listView.setCacheColorHint(0);
//		listView.setVerticalScrollBarEnabled(false);// 不显示滚动条
//		listView.setDrawingCacheEnabled(false);
		
		listView.setAdapter(new MyAdapter());
		listView.setSelector(new ColorDrawable(mActivity.getResources().getColor(R.color.login_color_6))); // 设置item的点击事件的背景
		listView.setBackgroundColor(mActivity.getResources().getColor(R.color.login_color_6));
		listView.setDivider(null);
		listView.setDividerHeight(0);
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
		final MessageBean messageBean = (MessageBean) msg.obj;
		switch (msg.arg2) {
		case 1005:
			if("0".equals(messageBean.getA())){
				MessageJson msg1 = new MessageJson();
				msg1.put("A",updateNewName);
				submitData(0, 1006, msg1);//修改彩名
			}
			break;
		case 1003:
			if("0".equals(messageBean.getA())){
				UserBean.getInstance().setLogin(false);
				SPUtil.putString(R.string.SND, "");
				setTitleNav(R.string.user_login, 0, 0);
				visibOrGone.setVisibility(View.VISIBLE);
				listView.setVisibility(View.GONE);
				redPackage.setText("正在加载..");
				userIntegral.setText("正在加载..");
				cash_account.setText("正在加载..");
				lotteryName.setText("正在加载..");
				if(mDialog!=null&&mDialog.isShowing()){
					mDialog.dismiss();
				}
				Toast.makeText(mActivity, messageBean.getB(), Toast.LENGTH_SHORT).show();
				
			}
			break;
		case 1006:
			if("0".equals(messageBean.getA())){
				lotteryName.setText(updateNewName);
//				UserBean.getInstance().setLotteryName(updateNewName);
//				SPUtil.putString(MyApp.res.getString(R.string.LOTTERYNAME), updateNewName);
				SPUtil.putString(MyApp.res.getString(R.string.uname),updateNewName);
				Toast.makeText(mActivity, "保存成功！", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(mActivity, "用户名已存在或不可用！", Toast.LENGTH_SHORT).show();
			}
			break;
		case 1008:
			if("0".equals(messageBean.getA())){
				UserBean.getInstance().setTrueName(messageBean.getC());
				UserBean.getInstance().setUserAccountState(messageBean.getI());
			}
			break;
		case 1015:
			if("0".equals(messageBean.getA())){
				if(!"0".equals(messageBean.getC())){
					header_line.setVisibility(View.GONE);
					sales_agent_btn.setVisibility(View.VISIBLE);
					sales_agent_title.setText(messageBean.getD());
					sales_agent_btn.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Bundle bundle = new Bundle();
							bundle.putString("url", messageBean.getE());
							start(ThirdActivity.class, TCWebFragment.class, bundle);
//							if("1".equals(messageBean.getC())){
//								bundle.putString("url", messageBean.getE());
//							}else if("2".equals(messageBean.getC())){
//								bundle.putString("url", messageBean.getE());
//							}else if("3".equals(messageBean.getC())){
//								bundle.putString("url", messageBean.getE());
//							}
						}
					});
				}else{
					header_line.setVisibility(View.VISIBLE);
					sales_agent_btn.setVisibility(View.GONE);
				}
			}
			break;
		case 1100:
			if("0".equals(messageBean.getA())){
			UserBean.getInstance().setAvailableMoney(Long.parseLong(messageBean.getC()));
			UserBean.getInstance().setAvailableCash(Long.parseLong(messageBean.getD()));
			UserBean.getInstance().setAvailableBalance(Long.parseLong(messageBean.getE()));
			AccountEnum.convertMessage(messageBean.getLIST());
			//数据的实时
			redPackage.setText("¥"+FormatUtil.moneyFormat2String(Double.parseDouble(AccountUtils.fenToyuan(AccountEnum.redpacket_account.getMoney()+""))));
			userIntegral.setText(AccountUtils.fenToyuan(AccountEnum.point_account.getMoney()+""));
//			accountBalance.setText("¥"+FormatUtil.moneyFormat2String(Double.parseDouble(AccountUtils.fenToyuan(UserBean.getInstance().getAvailableBalance()+""))));
			cash_account.setText("¥"+FormatUtil.moneyFormat2String(Double.parseDouble(AccountUtils.fenToyuan(UserBean.getInstance().getAvailableMoney()+""))));
			}
			break;
		case 1359:
			if(mDialog!=null&&mDialog.isShowing()){
				mDialog.dismiss();
			}
			if("0".equals(messageBean.getA())){
					 ShareSDK.initSDK(mActivity);
					 OnekeyShare oks = new OnekeyShare();
						oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
						oks.setAddress("");
						if(ConstantValue.LOTTERY_TYPE){
							oks.setTitle("山东体彩，扫一扫轻松购彩!");//标题
							oks.setText(messageBean.getD());//内容
							oks.setTitleUrl("http://m.mitenotc.com/");
							oks.setUrl("http://m.mitenotc.com/");
							oks.setImagePath("/data/data/com.mitenotc.tc/files/orcodetc.png");
							oks.setComment("山东体彩");
							oks.setSite("山东体彩");
							oks.addHiddenPlatform(QQ.NAME);//如果是体彩隐藏这三项
							oks.addHiddenPlatform(Wechat.NAME);
							oks.addHiddenPlatform(WechatMoments.NAME);
							oks.setSiteUrl("http://m.mitenotc.com/");
							oks.setTheme(OnekeyShareTheme.CLASSIC);
							oks.show(mActivity);
						}else{
							oks.setTitle("泰彩彩票，扫一扫轻松购彩!");//标题
							oks.setText(messageBean.getD());//内容
							oks.setTitleUrl("http://m.mitenotc.com/");
							oks.setUrl("http://m.mitenotc.com/");
							oks.setImagePath("/data/data/com.mitenotc.tc/files/orcodetc.png");
							oks.setComment("泰彩彩票");
							oks.setSite("泰彩彩票");
							oks.setSiteUrl("http://m.mitenotc.com/");
							oks.setTheme(OnekeyShareTheme.CLASSIC);
							oks.show(mActivity);
						}
			}
			break;
			
		case 1102:
			if("0".equals(messageBean.getA())){
				dialog.dismiss();
				Toast.makeText(mActivity, "兑换成功！", Toast.LENGTH_SHORT).show();
				AccountEnum.convertMessage(messageBean.getLIST());
				redPackage.setText("¥"+FormatUtil.moneyFormat2String(Double.parseDouble(AccountUtils.fenToyuan(AccountEnum.redpacket_account.getMoney()+""))));
				userIntegral.setText(AccountUtils.fenToyuan(AccountEnum.point_account.getMoney()+""));
//				accountBalance.setText("¥"+FormatUtil.moneyFormat2String(Double.parseDouble(AccountUtils.fenToyuan(UserBean.getInstance().getAvailableBalance()+""))));
				cash_account.setText("¥"+FormatUtil.moneyFormat2String(Double.parseDouble(AccountUtils.fenToyuan(UserBean.getInstance().getAvailableMoney()+""))));
			}
			break;
		}
	}

	private class MyAdapter extends BaseListAdapter {
	
		@Override
		public int getCount() {
			return titles.length;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(mActivity, R.layout.m_acc_me_item, null);
			ImageView me_image = (ImageView) view.findViewById(R.id.iv_acc_me_logos);
			TextView me_title = (TextView) view.findViewById(R.id.tv_acc_me_title);
			View topline = view.findViewById(R.id.acc_me_item_oneline);
			View bottomline = view.findViewById(R.id.acc_me_item_longline);
			View juli = view.findViewById(R.id.acc_me_item_juli);
			me_image.setBackgroundDrawable(meLogos.getDrawable(position));
			me_title.setText(titles[position]);

			switch (position) {
			case 0:
				bottomline.setVisibility(View.GONE);
				break;
			case 1:
				topline.setVisibility(View.GONE);
				bottomline.setVisibility(View.VISIBLE);
				juli.setVisibility(View.VISIBLE);
				break;
			case 2:
				bottomline.setVisibility(View.GONE);
				topline.setVisibility(View.VISIBLE);
				break;
			case 3:
				topline.setVisibility(View.GONE);
				bottomline.setVisibility(View.VISIBLE);
				juli.setVisibility(View.VISIBLE);
				break;
			case 4:
				bottomline.setVisibility(View.GONE);
				topline.setVisibility(View.VISIBLE);
				break;
			case 5:
				bottomline.setVisibility(View.GONE);
				topline.setVisibility(View.GONE);
				juli.setVisibility(View.GONE);
				break;
			case 6:
				topline.setVisibility(View.GONE);
				bottomline.setVisibility(View.VISIBLE);
				break;
			case 7:
				topline.setVisibility(View.GONE);
				bottomline.setVisibility(View.GONE);
				break;
//			case 8:
//				topline.setVisibility(View.GONE);
//				bottomline.setVisibility(View.VISIBLE);
//				break;
			}
			return view;
		}
	}
	
	@Override
	protected void onRightIconClicked() {
		BodyFragment1.addTag(mActivity,CustomTagEnum.user.getId());
		setTitleNav(CustomTagEnum.user.getId(),R.string.title_nav_user, 0, R.drawable.title_nav_menu);
	}
	
	public class PhotoDialogs extends Dialog {
		private void init() {
			setContentView(R.layout.m_acc_me_photo_grid);
			initView();
		}

		public PhotoDialogs(Context context) {
			super(context, R.style.dialog_theme);
			init();
			setCanceledOnTouchOutside(true);
		}
	}
	
	public class IntegralDialogs extends Dialog {
		private void init() {
			setContentView(R.layout.m_acc_me_change_integral);
			initView();
		}

		public IntegralDialogs(Context context) {
			super(context, R.style.dialog_theme);
			init();
			setCanceledOnTouchOutside(true);
		}
	}
	
	/**
	 * 划分等级
	 * @param tempStr
	 * @return
	 */
	public  void formatLevel(String tempStr){
		if(tempStr==null){
			return ;
		}
		int[] imgId=new int[tempStr.length()];
		for(int i = 0; i < tempStr.length(); i++){
			char temC=tempStr.charAt(i);
			String temp=String.valueOf(temC);
//			//System.out.println(temp);
			switch (Integer.valueOf(temp)) {
				case 0:
					imgId[i]=0;
					break;
				case 1:
					imgId[i]=1;
					
					break;
				case 2:
					imgId[i]=2;
					
					break;
				case 3:
					imgId[i]=3;
					
					break;
				case 4:
					imgId[i]=4;
					
					break;
				default:
					imgId[i]=4;
					break;
			       }
		  }
		for (int i = 0; i < imgId.length; i++) {
			switch (i) {
			case 0:
				setView(DJ_0,imgId[i]);
				break;
			case 1:
				setView(DJ_1,imgId[i]);
				break;
			case 2:
				setView(DJ_2,imgId[i]);
				break;
			case 3:
				setView(DJ_3,imgId[i]);
				break;
			case 4:
				setView(DJ_4,imgId[i]);
				break;

			}
		}
		
	}
	//设置等级显示背景
	private void setView(TextView dj_tv, int i) {
		switch (i) {
		case 0:
			dj_tv.setVisibility(View.VISIBLE);
			dj_tv.setBackgroundDrawable(MyApp.res.getDrawable(R.drawable.rank0));
			break;

		case 1:
			dj_tv.setVisibility(View.VISIBLE);
			dj_tv.setBackgroundDrawable(MyApp.res.getDrawable(R.drawable.rank1));
			break;
			
		case 2:
			dj_tv.setVisibility(View.VISIBLE);
			dj_tv.setBackgroundDrawable(MyApp.res.getDrawable(R.drawable.rank2));
			break;
			
		case 3:
			dj_tv.setVisibility(View.VISIBLE);
			dj_tv.setBackgroundDrawable(MyApp.res.getDrawable(R.drawable.rank3));
			break;
		case 4:
			dj_tv.setVisibility(View.VISIBLE);
			dj_tv.setBackgroundDrawable(MyApp.res.getDrawable(R.drawable.rank4));
			break;
		default:
			dj_tv.setVisibility(View.GONE);
			break;
		}
	};
}

