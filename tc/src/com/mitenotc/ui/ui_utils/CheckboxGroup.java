package com.mitenotc.ui.ui_utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
/**
 * 自定义 CheckboxGroup
 * @author mitenotc
 *
 */
public class CheckboxGroup extends LinearLayout {
	
	private List<String> tagList;//  获取CheckBox tag
	private List<String> textList;//  获取CheckBox text
	private List<CheckBox> cbList=new ArrayList<CheckBox>();
	public CheckboxGroup(Context context) {
		super(context);
	}
	public CheckboxGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public interface OnChbxGpOnCheckedListener{
		void setOnChildCheckedListener(View v,List<String> tagList ,List<String> textList);
	}
	public OnChbxGpOnCheckedListener mGpCheckedListener;
    public void setChbxGpOnCheckedListener(OnChbxGpOnCheckedListener mGpCheckedListener) {
		this.mGpCheckedListener = mGpCheckedListener;
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
	   System.out.println("125----addView---textList ---> "+textList.toString()+"  tagList----> "+tagList.toString());
		 if(child instanceof CheckBox){
			 final CheckBox button = (CheckBox) child;
			 if(-1==cbList.indexOf(button)){
				 cbList.add(button);
			 }
			 button.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton chBtn, boolean isCheck) {
					if(isCheck){
						    if(-1 == tagList.indexOf((String)chBtn.getTag())){
						    	tagList.add(0,(String)chBtn.getTag());
						    }else{
						    	tagList.remove(tagList.indexOf((String)chBtn.getTag()));
						    }
						    if(-1 == textList.indexOf(chBtn.getText().toString().trim())){
						    	textList.add(0,chBtn.getText().toString().trim());
						    }
						    System.out.println("125-----isChecked true"+(String)chBtn.getTag());
					}else if(chBtn.isPressed()){
						System.out.println("125-----isChecked false"+(String)chBtn.getTag());
						if(-1!=tagList.indexOf((String)chBtn.getTag())){
							tagList.remove(tagList.indexOf((String)chBtn.getTag()));
						}
						
						if(-1 != textList.indexOf(chBtn.getText().toString().trim())){
					    	textList.remove(textList.indexOf(chBtn.getText().toString().trim()));
					    }
					}
					if(mGpCheckedListener!=null && chBtn.isPressed()){
						System.out.println("125----1------tagList :"+tagList.toString());
						mGpCheckedListener.setOnChildCheckedListener(chBtn, tagList, textList);
					}
				}
			});
		 }else if(child instanceof LinearLayout){
		     	int childCount = ((LinearLayout) child).getChildCount();
		     	for(int i = 0; i < childCount; i++){
		    		View view = ((LinearLayout) child).getChildAt(i);
		     		if (view instanceof CheckBox) {
		                 final CheckBox button = (CheckBox) view;
		                 if(-1==cbList.indexOf(button)){
		    				 cbList.add(button);
		    			 }
		                 button.setOnCheckedChangeListener(new OnCheckedChangeListener() {
							@Override
							public void onCheckedChanged(CompoundButton chBtn, boolean isCheck) {
								tagList.clear();
								textList.clear();
								if(isCheck){
									 if(-1 == tagList.indexOf((String)chBtn.getTag())){
									    	tagList.add(0,(String)chBtn.getTag());
									    }
									  if(-1 == textList.indexOf(chBtn.getText().toString().trim())){
									    	textList.remove(textList.indexOf(chBtn.getText().toString().trim()));
									    }
								}else {
									 if(-1 != tagList.indexOf((String)chBtn.getTag())){
										 tagList.remove(tagList.indexOf((String)chBtn.getTag()));
									   }
									if(-1 != textList.indexOf(chBtn.getText().toString().trim())){
							    	   textList.remove(textList.indexOf(chBtn.getText().toString().trim()));
							         }
								}
								
								if(mGpCheckedListener!=null && button.isPressed() && child.isFocusable()){
									System.out.println("125----1------textList :"+textList.toString());
									mGpCheckedListener.setOnChildCheckedListener(chBtn, tagList, textList);
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
	
	public List<CheckBox> getCbList() {
		return cbList;
	}
	public void setCbList(List<CheckBox> cbList) {
		this.cbList = cbList;
	}
	public void setTagList(List<String> tagList) {
		this.tagList = tagList;
	}
	
	
}
