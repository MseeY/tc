package com.mitenotc.ui.play;

import java.util.ArrayList;
import java.util.List;

import com.mitenotc.tc.R;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

public class ToggleButtonGroup extends LinearLayout {

	private static List<String> checkedList;  //选中的集合
	private Context mContext;

	public List<String> getCheckedList() {
		List<String> listtag = new ArrayList<String>();
		for (int i = 0; i < checkedList.size(); i++) {
			listtag.add(checkedList.get(i));
		}
		return listtag;
	}

	public ToggleButtonGroup(Context context) {
		super(context);
		mContext = context;
		if(checkedList==null){
			checkedList = new ArrayList<String>();
		}
		
	}

	public ToggleButtonGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		if(checkedList==null){
			checkedList = new ArrayList<String>();
		}
	}
	
	@Override
	public void addView(View child, int index,android.view.ViewGroup.LayoutParams params) {
		super.addView(child, index, params);
		
		if((child instanceof ToggleButton)){
			final ToggleButton btn = (ToggleButton)child;
			btn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					try {
					String str = buttonView.getText().subSequence(0, 3).toString();
					if(isChecked){
						btn.setTextOn(buttonView.getText());
						if(!checkedList.contains(str)){
							checkedList.add(str);
						}
					}else{
						btn.setTextOff(buttonView.getText());
						if(checkedList.contains(str)){
							checkedList.remove(checkedList.indexOf(str));
						}
					}
					
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			});
		}
	}
	
	public static void clearList(){
		if(checkedList!=null){
			checkedList.clear();
		}
	}
}
