package com.mitenotc.ui.pay;

import android.os.Bundle;
import android.widget.TextView;

import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.utils.AppUtil;

public class PayResultFragment extends BaseFragment{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView tv = new TextView(mActivity);
		setContentView(tv);
		Bundle bundle = getMyBundle();
		String respStr = bundle.getString("responseDesc");
		if(AppUtil.isEmpty(respStr)){
			respStr = "返回数据为空";
		}
		tv.setText(respStr);
	}
}
