package com.mitenotc.ui.ui_utils;

import com.mitenotc.tc.R;

import android.app.Dialog;
import android.content.Context;
/**
 * 
 * 智能追号 初始化方案时候使用
 * @author admin
 *
 */
public class WaitDialogs extends Dialog {

	public WaitDialogs(Context context) {
		super(context, R.style.dialog_theme);
		init();
		setCanceledOnTouchOutside(false);//点击不能取消
	}
	private void init() {
		setContentView(R.layout.m_wait_dialog);
	}
}
