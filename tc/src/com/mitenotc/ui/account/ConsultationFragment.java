package com.mitenotc.ui.account;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ArrowKeyMovementMethod;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.mitenotc.bean.MessageBean;
import com.mitenotc.dao.Cache_1353;
import com.mitenotc.dao.Cache_1358;
import com.mitenotc.enums.CustomTagEnum;
import com.mitenotc.net.MessageJson;
import com.mitenotc.service.TCService;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.tc.TCTimerHelper;
import com.mitenotc.ui.BodyFragment1;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.utils.AppUtil;
import com.mitenotc.utils.SPUtil;

/**
 * 在线咨询：2014-3-1 14:23:40
 * 
 * @author ymx
 * 
 */
@SuppressLint("Recycle")
public class ConsultationFragment extends BaseFragment implements
		OnClickListener {

	private ListView listView;
	private Button consultation_send;
	private EditText consultation_input;
	private MyAdapter mAdapter;
	private Cache_1353 cache;
	private String content;
	private TypedArray userPhotos;

	private long preTextChangeTime = 0;
	private String ACTION="com.mitenotc.ui.account.ConsultationFragment";
	
	/** 轮询 */
	private TCTimerHelper timer;
	private TCService mService;
	private Intent serviceIntent;
    private  int temp=0;
    Handler mHandler=new Handler(){
    	@Override
    	public void handleMessage(Message msg) {
    		super.handleMessage(msg);
    		////System.out.println("c-----------mHandler");
    		if(temp == msg.what){
    			mAdapter.notifyDataSetChanged();
    			listView.setSelection(listView.getCount() - 1);
    		}
    	}
    };
    @Override
    public void onStart() {
    	super.onStart();
    	TCService.TIME_INTERVAL = 5;
//        mActivity.startService(new Intent("com.mitenotc.intent.TCService"));
        Intent tc_service=new Intent(mActivity,TCService.class);
		tc_service.setAction("com.mitenotc.intent.TCService");
		mActivity.startService(tc_service);
    };
    
    private Animation shake;
   
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		shake = AnimationUtils.loadAnimation(mActivity, R.anim.clause_shake);
		//因为ThirtyActivity做了调整  所以在这里需要添加下行代码
		mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		TCService.TIME_INTERVAL = 5;
		IntentFilter mFilter=new IntentFilter();
		mFilter.addAction(ACTION);
		mActivity.registerReceiver(mReceiver, mFilter);
		
		setContentView(R.layout.m_acc_consultation_item);
		setTitleNav(CustomTagEnum.consultation.getId(),
				R.string.acc_title_online_consultation,
				R.drawable.title_nav_back, R.drawable.title_nav_menu);
		cache = new Cache_1353();
		userPhotos = MyApp.res.obtainTypedArray(R.array.acc_user_photos);
		initView();
		setConLinstener();
	}

	private void initView() {
		listView = (ListView) findViewById(R.id.lv_acc_consultation_item);
		mAdapter = new MyAdapter();
		listView.setAdapter(mAdapter);
		AccountUtils.configListView(listView);
		consultation_send = (Button) findViewById(R.id.bt_acc_consultation_send);
		consultation_input = (EditText) findViewById(R.id.et_acc_consultation_input);
	}

	private void setConLinstener() {
		consultation_send.setOnClickListener(this);
		consultation_input.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				AccountUtils.changedEdittext(consultation_send, s, count);
				
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
	
	private final String reg ="^([a-z]|[A-Z]|[0-9]|[\u4E00-\u9FA5]|[,.，。！^\\+\\-\\/=_!~·？?;\\[\\]:@#￥%……&*（）\\$《》<>{}()]){0,}$";

	private Pattern pattern = Pattern.compile(reg);
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 发送按钮
		case R.id.bt_acc_consultation_send:

			if (!"".equals(consultation_input.getText().toString().trim())) {
				content = consultation_input.getText().toString();
				Matcher matcher = pattern.matcher(consultation_input.getText().toString().trim());
				if(!matcher.matches()){
					Toast.makeText(mActivity, "您输入的字符无效", Toast.LENGTH_SHORT).show();
					return;
				}
				sendRequest(1353, content);
				consultation_input.setText("");
				preTextChangeTime = System.currentTimeMillis();
				
//				serviceIntent.putExtra("content", content);
//				serviceIntent.putExtra("TIME_INTERVAL", 5000);
//				mActivity.startService(serviceIntent);
//				consultation_input.setText("");
			} else {
				consultation_input.startAnimation(shake);
//				Toast.makeText(mActivity, "请输入咨询内容", Toast.LENGTH_SHORT).show();
			}
			break;
//		case R.id.ll_consult_cefu_phoneNumber:
//			AccountUtils.boda(mActivity, "4000328666");
//			break;
		}
	}

	/** 获取网络数据 */
	private void sendRequest(int key, String content1) {
		MessageJson msg = new MessageJson();
		msg.put("B", content1);
		submitData(0, key, msg);

	}

	/** 操作网络数据 */
	@Override
	protected void onMessageReceived(Message msg) {
		mAdapter.notifyDataSetChanged();
		listView.setSelection(listView.getCount() - 1);
	}

	private class MyAdapter extends BaseAdapter {
		private List<MessageBean> msgList = cache.getMsgList().getLIST();

		@Override
		public void notifyDataSetChanged() {
			// TODO Auto-generated method stub
			msgList = cache.getMsgList().getLIST();
			super.notifyDataSetChanged();
		}

		@Override
		public int getCount() {

			return msgList.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MessageBean messageBean = msgList
					.get(msgList.size() - position - 1);

			if ("2".equals(messageBean.getA())) {
				View view = View.inflate(mActivity,R.layout.m_acc_consultation_item_left, null);
				TextView tv = (TextView) view.findViewById(R.id.tv_acc_consultation_content_left);
				tv.setText(messageBean.getB());
				tv.setAutoLinkMask(Linkify.ALL);
				tv.setMovementMethod(LinkMovementMethod.getInstance()); 
//				tv.setFocusableInTouchMode(true);
//				tv.setFocusable(true);
//				tv.setClickable(true);
//				tv.setLongClickable(true);
//				tv.setMovementMethod( ArrowKeyMovementMethod.getInstance());
//				tv.setText(tv.getText(),BufferType.SPANNABLE );
				checkText(tv);
				return view;
			} else {
				View view = View.inflate(mActivity,R.layout.m_acc_consultation_item_right, null);
				TextView tv = (TextView) view.findViewById(R.id.tv_acc_consultation_content_right);
				ImageView iv = (ImageView) view.findViewById(R.id.iv_acc_consultation_content_right);
				tv.setText(messageBean.getB());
				tv.setAutoLinkMask(Linkify.ALL);
				tv.setMovementMethod(LinkMovementMethod.getInstance());
//				tv.setFocusableInTouchMode(true);
//				tv.setFocusable(true);
//				tv.setClickable(true);
//				tv.setLongClickable(true);
//				tv.setMovementMethod( ArrowKeyMovementMethod.getInstance());
//				tv.setText(tv.getText(),BufferType.SPANNABLE );

				checkText(tv);
				
				try {
				if(!"".equals(SPUtil.getString(R.string.USERPHOTO))){
					iv.setImageDrawable(userPhotos.getDrawable(Integer.parseInt(SPUtil.getString(R.string.USERPHOTO))));
				}else if(MyApp.header_photo !=null){
					Bitmap header_photo = MyApp.header_photo;
					Drawable drawale = new BitmapDrawable(header_photo);
					iv.setImageDrawable(drawale);
				}else{
					iv.setImageResource(R.drawable.acc_me_header_logo);
				}
				
				} catch (Exception e) {
					Toast.makeText(mActivity, "重进一下吧", Toast.LENGTH_SHORT).show();
				}

				return view;
			}
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
	}

	/** 控制条目的宽度 */
	private void checkText(TextView tv) {
		if (tv.getText().toString().getBytes().length > 39) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.width = AppUtil.getDisplayWidth(mActivity) * 320 / 540;
			tv.setLayoutParams(params);
		}
	}

	@Override
	public void onResume() {
//		timer.start();
		super.onResume();
		TCService.TIME_INTERVAL = 5;
	}

	@Override
	public void onPause() {
//		timer.stop();
		super.onPause();	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		TCService.TIME_INTERVAL = 60;//  恢复默认 
		if(mReceiver!=null){
			mActivity.unregisterReceiver(mReceiver);
			mReceiver=null;
		}
	};
	 
	BroadcastReceiver mReceiver=new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context ctx, Intent intent) {
			
			 if(ACTION.equals(intent.getAction()) && intent!=null){  
				 temp=Cache_1358.getMSG_COUNT();
				 if(temp > 0){
					 mHandler.sendEmptyMessage(temp);
				 }
			   }  
		}
	};
	@Override
	protected void onRightIconClicked() {
		BodyFragment1.addTag(mActivity, CustomTagEnum.consultation.getId());
		setTitleNav(CustomTagEnum.consultation.getId(),R.string.acc_title_online_consultation,R.drawable.title_nav_back, R.drawable.title_nav_menu);
	}
}
