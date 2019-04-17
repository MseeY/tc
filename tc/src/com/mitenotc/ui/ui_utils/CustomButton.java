package com.mitenotc.ui.ui_utils;

import com.mitenotc.tc.R;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomButton extends LinearLayout {
	private static final String TAG = CustomButton.class.getSimpleName();
	private TextView tv1;
	private TextView tv2;//右下角百分比
	
	public boolean isshowratioTV=true;
	private Context mContext=null;
	
	public CustomButton(Context context) {
		super(context);
		this.mContext=context;
	}
	
    public CustomButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		Resources.Theme theme = context.getTheme();
		inflaterLayout(context);//导入布局
		
		tv1=(TextView) findViewById(R.id.cstom_btn_tv1);
		tv2=(TextView) findViewById(R.id.cstom_btn_tv2);
		tv1.setClickable(false);//tv1不能获取焦点
		tv2.setClickable(false);//tv2不能获取焦点
		
		//isshowratioTV ： true 显示比例  false 则反
		if(isshowratioTV){
			tv1.setGravity(Gravity.RIGHT);
			tv2.setVisibility(View.VISIBLE);
		}else{
			tv1.setGravity(Gravity.CENTER);
			tv2.setVisibility(View.GONE);
		}
		
	}
   /**
    * 设置 TextView  资源
    * @param context
    */
    
    public void setTV1ViewResource(int redId1,int redId2){
    	tv1.setText(redId1);
    	tv2.setText(redId2);
    }
    
    public void setTV1ViewResource(String str1 ,String str2){
    	tv1.setText(str1);
    	tv2.setText(str2);
    }
    

	protected void inflaterLayout(Context context){
    	LayoutInflater.from(context).inflate(R.layout.custom_btn, this ,true);
    }
	/**
	 * @return the isshowratioTV
	 */
	public boolean isIsshowratioTV() {
		return isshowratioTV;
	}


	/**
	 * @param isshowratioTV the isshowratioTV to set
	 */
	public void setIsshowratioTV(boolean isshowratioTV) {
		this.isshowratioTV = isshowratioTV;
	}

}
