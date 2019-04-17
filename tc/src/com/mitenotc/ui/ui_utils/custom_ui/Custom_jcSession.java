package com.mitenotc.ui.ui_utils.custom_ui;

import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.ui.ui_utils.MGridView.CustomItem;
import com.mitenotc.utils.AppUtil;

import android.content.Context;
import android.text.Spannable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 竞彩串关
 * @author mitenotc
 *
 */
public class Custom_jcSession extends LinearLayout implements CustomItem {
    private TextView cb;
    
    private String tag="";
    private String text="";
    
	public Custom_jcSession(Context context) {
		super(context);
		init(context);
	}
	public Custom_jcSession(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	public Custom_jcSession(Context context, String tag, String text) {
		super(context);
		this.tag = tag;
		this.text = text;
		 init(context);
	}

	private void init(Context context) {
		View mView=View.inflate(context, R.layout.jcsession_view, this);
		cb= (TextView) mView. findViewById(R.id.cb1);
		if(!AppUtil.isEmpty(tag)){
			cb.setTag(tag);
		}
		if(!AppUtil.isEmpty(text)){
			cb.setText(text);
		}
	}
	
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public void setText(String text) {
		this.text = text;
	}
	@Override
	public void selected() {
		if(cb!=null){
			cb.setBackgroundDrawable(MyApp.res.getDrawable(R.drawable.jc_tv_true));
			cb.setTextColor(MyApp.res.getColor(R.color.white));
		}
	}
	@Override
	public void unselected() {
		if(cb!=null){
			cb.setBackgroundDrawable(MyApp.res.getDrawable(R.drawable.jc_cb_false));
			cb.setTextColor(MyApp.res.getColor(R.color.award_info_tv_title_color));
		}
	}
	@Override
	public String getText() {
		String s="";
		if(cb!=null){
			s=cb.getText().toString();
		}
		return s;
	}

}
