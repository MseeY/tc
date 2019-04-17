package com.mitenotc.ui.account;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.mitenotc.tc.ConstantValue;
import com.mitenotc.tc.R;
import com.mitenotc.tc.ThirdActivity;
import com.mitenotc.ui.TCWebFragment;
import com.mitenotc.ui.base.BaseFragment;

public class HelpSenter extends BaseFragment {
	
	private ListView listView;
	private List<Bundle> bundles;
	private String[] urls;
	private String[] titles;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m_acc_help_center);
		setTitleNav("帮助中心", R.drawable.title_nav_back, 0);
		initHelpListView();
		
	}

	private void initHelpListView() {
		listView = (ListView) findViewById(R.id.lv_acc_help_center);
		AccountUtils.configListView(listView);
		bundles = new ArrayList<Bundle>();
		if(ConstantValue.LOTTERY_TYPE){
			urls = getResources().getStringArray(R.array.acc_setting_help_url_ticai);
			titles = getResources().getStringArray(R.array.acc_setting_help_title_ticai);
		}else{
			urls = getResources().getStringArray(R.array.acc_setting_help_url);
			titles = getResources().getStringArray(R.array.acc_setting_help_title);
		}
		for (int i = 0; i < urls.length; i++) {
			Bundle bundle = new Bundle();
			bundle.putString("url", urls[i]);
			bundle.putString("title", titles[i]);
			bundles.add(bundle);
		}
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				start(ThirdActivity.class, TCWebFragment.class, bundles.get(position));
			}
		});
		listView.setAdapter(new MyAdapter());
	}
	
	private class MyAdapter extends BaseListAdapter{
		
		@Override
		public int getCount() {
			return titles.length;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(mActivity, R.layout.m_acc_help_item, null);
			TextView acc_help = (TextView) view.findViewById(R.id.tv_acc_help);
			acc_help.setText(titles[position]);
			return view;
		}
	}
}
