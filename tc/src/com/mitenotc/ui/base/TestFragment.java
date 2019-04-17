package com.mitenotc.ui.base;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.mitenotc.tc.R;
import com.mitenotc.ui.BodyFragment2;
import com.mitenotc.ui.register.RegisterNextone;
import com.mitenotc.ui.register.Registermain;

public class TestFragment extends BaseFragment{

	private Button testMyFragment;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_layout);
		testMyFragment = (Button) findViewById(R.id.button1);
		testMyFragment.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(mActivity, "test my fragment", 0).show();
				replaceMiddle(Registermain.class);
				//mActivity.getBottomFragment().mView.setVisibility(View.GONE);
			}
		});
	}
}
