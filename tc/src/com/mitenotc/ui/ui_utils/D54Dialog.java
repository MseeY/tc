package com.mitenotc.ui.ui_utils;

import java.util.List;

import com.mitenotc.tc.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

public class D54Dialog extends Dialog implements android.view.View.OnClickListener {
    private Context ctx;
    private CheckboxGroup cb_gp;
	public D54Dialog(Context context) {
		super(context, R.style.dialog_theme);
		ctx=context;
		init();
	}
	public D54Dialog(Context context, int theme) {
		super(context, theme);
		ctx=context;
		setCanceledOnTouchOutside(false);//触动了窗外的界限不能取消界面
		init();
	}
	private void init() {
		setContentView(R.layout.d54dialog_layout);
		initView();
	}
	private void initView() {
		cb_gp = (CheckboxGroup) findViewById(R.id.chbox_gp);
		findViewById(R.id.cancel_btn).setOnClickListener(this);
		findViewById(R.id.affirm_btn).setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancel_btn:// 取消
			cancel();
			break;
			
		case R.id.affirm_btn:// 确认
			List<String> s=cb_gp.getTagList();
			List<String> s2=cb_gp.getTextList();
			
			System.out.println("124----d54-----tag--->"+s.toString()+"-----Text---->"+s2.toString());
			cancel();
			break;
		}
		
	}
	

}
