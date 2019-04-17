package com.zbar.lib;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mitenotc.tc.R;
import com.mitenotc.ui.base.BaseFragment;

public class ScanTextFragment extends BaseFragment {
	
	private TextView tv_scan_result_text;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scan_result_text);
		setTitleNav("扫描结果",R.drawable.title_nav_back, 0);
		initView();
	}

	private void initView() {
		tv_scan_result_text = (TextView) findViewById(R.id.tv_scan_result_text);
		Bundle myBundle = getMyBundle();
		String string = myBundle.getString("TEXTRESULT");
		tv_scan_result_text.setText(string);
	}
}
