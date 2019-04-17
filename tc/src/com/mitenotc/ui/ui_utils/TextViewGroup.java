package com.mitenotc.ui.ui_utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.utils.AppUtil;

import android.R.integer;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 自定义 CheckboxGroup
 * @author mitenotc
 *
 */
public class TextViewGroup extends LinearLayout {
	
	private List<String> tagList;//  获取CheckBox tag
	private List<String> textList;//  获取CheckBox text
	private List<TextView> tvList=new LinkedList<TextView>();
	public TextViewGroup(Context context) {
		super(context);
	}
	public TextViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public interface OnTextViewGroup{
		void setOnTextViewGroup(View v,List<String> tagList ,List<String> textList);
	}
	public OnTextViewGroup onTextViewGroup;
   
	public void setOnTextViewGroupListener(OnTextViewGroup onTextViewGroup) {
		this.onTextViewGroup = onTextViewGroup;
	}
	@Override
	public void addView(final View child, int index,android.view.ViewGroup.LayoutParams params) {
		super.addView(child, index, params);
		if(tagList==null){
			tagList=new ArrayList<String>();
		}else{
			tagList.clear();
		}
	   if(textList==null){
		   textList=new ArrayList<String>();
	   }else {
		   textList.clear();  
	   }
		 if(child instanceof TextView){
			 final TextView button = (TextView) child;
			 if(-1==tvList.indexOf(button)){
				 tvList.add(button);
			 }
			 button.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(tagList==null){tagList=new ArrayList<String>();}
					if(StringUtils.isBlank(button.getTag().toString())){
						return ;
					}
					 if(-1 == tagList.indexOf(button.getTag().toString())){
					    	tagList.add(0,button.getTag().toString());
					    	button.setBackgroundDrawable(MyApp.res.getDrawable(R.drawable.jc_cb_true));
					    }else{
					    	button.setBackgroundDrawable(MyApp.res.getDrawable(R.drawable.jc_cb_false));
					    	tagList.remove(tagList.indexOf(button.getTag().toString()));
					    }
//					 
//					 if(-1 == textList.indexOf(button.getText().toString().trim())){
//						 textList.add(0,button.getText().toString().trim());
//					 }else{
//						 textList.remove(textList.indexOf(button.getText().toString().trim()));
//					 }
					 if(onTextViewGroup!=null && button.isPressed()){
						onTextViewGroup.setOnTextViewGroup(button, tagList, textList);
					}
				}
			});
	
		 }else if(child instanceof LinearLayout){
		     	int childCount = ((LinearLayout) child).getChildCount();
		     	for(int i = 0; i < childCount; i++){
		    		View view = ((LinearLayout) child).getChildAt(i);
		     		if (view instanceof TextView) {
		                 final TextView button = (TextView) view;
		                 if(-1==tvList.indexOf(button)){
		    				 tvList.add(button);
		    			 }
		            	 button.setOnClickListener(new OnClickListener() {
		     				@Override
		     				public void onClick(View v) {
		     					if(tagList==null){tagList=new ArrayList<String>();}
		     					if(StringUtils.isBlank(button.getTag().toString())){
		    						return ;
		    					}
		     					 if(-1 == tagList.indexOf(button.getTag().toString())){
		     					    	tagList.add(0,button.getTag().toString());
		     					    	button.setBackgroundDrawable(MyApp.res.getDrawable(R.drawable.jc_cb_true));
		     					    }else{
		     					    	button.setBackgroundDrawable(MyApp.res.getDrawable(R.drawable.jc_cb_false));
		     					    	tagList.remove(tagList.indexOf(button.getTag().toString()));
		     					    }
		     					 if(onTextViewGroup!=null && button.isPressed()){
		 							onTextViewGroup.setOnTextViewGroup(button, tagList, textList);
		 					   }
		     				}
		     			});
		             }
		     	}
		 }
	}
	public List<String> getTagList() {
		return tagList;
	}
	public List<String> getTextList() {
		return textList;
	}
	
	public List<TextView> getTvList() {
		return tvList;
	}
	public void setTvList(List<TextView> tvList) {
		this.tvList = tvList;
	}
	public void setTagList(List<String> tagList) {
		this.tagList = tagList;
	}
}
