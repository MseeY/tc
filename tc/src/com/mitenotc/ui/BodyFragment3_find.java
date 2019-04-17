package com.mitenotc.ui;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mitenotc.enums.CustomTagEnum;
import com.mitenotc.tc.ConstantValue;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.tc.ThirdActivity;
import com.mitenotc.ui.account.BuyRedPacket;
import com.mitenotc.ui.account.BuyRedPacketValue;
import com.mitenotc.ui.account.ConsultationFragment;
import com.mitenotc.ui.account.MessageCenter;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.ui.play.PL122;
import com.mitenotc.ui.play.PL130;
import com.mitenotc.ui.wheel.WheelFragment;
import com.zbar.lib.CaptureActivity;

/**
 * 发现
 * @author mitenotc
 *
 */
public class BodyFragment3_find extends BaseFragment{
	
	private MyAdapter mAdapter1;
	private TypedArray findLogos;
	private static boolean flag = ConstantValue.LOTTERY_TYPE;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitleNav(CustomTagEnum.find.getId(), getString(R.string.title_nav_find),0,R.drawable.title_nav_menu);
		setContentView(R.layout.find_wap1);
		initView();
		setfindListener();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		setTitleNav(CustomTagEnum.find.getId(), getString(R.string.title_nav_find),0,R.drawable.title_nav_menu);
	}
	
	@Override
	protected void onLeftIconClicked() {
		start(ThirdActivity.class,CaptureActivity.class, null);
	}

	private void initView() {
		if(flag){
			findLogos = MyApp.res.obtainTypedArray(R.array.find_logo1);
		}else{
			findLogos = MyApp.res.obtainTypedArray(R.array.find_logo);
		}
		
		LinearLayout ll_find_layout = (LinearLayout) findViewById(R.id.ll_find_layout);
		mAdapter1 = new MyAdapter(mActivity);
		mListView.setAdapter(mAdapter1);
		ll_find_layout.addView(mListView);
		mListView.disablePullLoad(); 
		mListView.setPullRefreshEnable(false);
		mListView.setDivider(new ColorDrawable(Color.parseColor("#00000000")));
		mListView.setDividerHeight(3);
		mListView.setDrawingCacheEnabled(false);
		
	}
	
	private void setfindListener() {
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				switch (position) {
				case 1://购买红包
					start(ThirdActivity.class, BuyRedPacket.class, null);
					break;
				case 2://赛事推荐
					Bundle mBundle = new Bundle();
					mBundle.putString("MessageCenter", "2");
					start(ThirdActivity.class, MessageCenter.class, mBundle);
					break;
				case 3:
					start(ThirdActivity.class, ConsultationFragment.class, null);
					break;
				case 4:
					start(ThirdActivity.class, MessageCenter.class, null);
					break;
				case 5:
					start(PL130.class);
					break;
				case 6:
					start(PL122.class);
					break;
				case 7:
					Toast.makeText(mActivity, "敬请期待..", Toast.LENGTH_SHORT).show();
////				start(ThirdActivity.class, WheelFragment.class, null);
					break;
				}
				
//				if(flag){
//					switch (position) {
//					case 1:
//						start(PL130.class);
//						break;
//					case 2:
//						Toast.makeText(mActivity, "敬请期待..", Toast.LENGTH_SHORT).show();
////						start(ThirdActivity.class, WheelFragment.class, null);
//						break;
//					case 3:
//						start(ThirdActivity.class, MessageCenter.class, null);
//						break;
//					}
//				}else{
//					switch (position) {
//					case 1: 
//						start(PL122.class);
//						break;
//					case 2:
//						start(PL130.class);
//						break;
//					case 3:
//						Toast.makeText(mActivity, "敬请期待..", Toast.LENGTH_SHORT).show();
////						start(ThirdActivity.class, WheelFragment.class, null);
//						break;
//					case 4:
//						start(ThirdActivity.class, MessageCenter.class, null);
//						break;
//					}
//				}
				
			}
		});
		
//		ll_find_erweima.setOnClickListener(this);
//		ll_code_scan.setOnClickListener(this);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(findLogos!=null){
			findLogos.recycle();
		}
	}
	
	private class MyAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public MyAdapter(Context pContext) {
			mInflater = LayoutInflater.from(pContext);
		}

		@Override
		public int getCount() {
			return findLogos.length();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewFindHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.find_wap_list, null);
				holder = new ViewFindHolder();
				holder.findLogo = (LinearLayout) convertView.findViewById(R.id.iv_find_logos);
				convertView.setTag(holder);
			} else {
				holder = (ViewFindHolder) convertView.getTag();
			}
			
			holder.findLogo.setBackgroundDrawable(findLogos.getDrawable(position));
			return convertView;
			
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
	}
	
	private static class ViewFindHolder {
		LinearLayout findLogo;
	}
	
	@Override
	protected void onRightIconClicked() {
		BodyFragment1.addTag(mActivity, CustomTagEnum.find.getId());
		setTitleNav(CustomTagEnum.find.getId(), getString(R.string.title_nav_find),0,R.drawable.title_nav_menu);
	}
}