package com.mitenotc.ui.ui_utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class GridRadioGroup extends RadioGroup{
	
	private OnCheckedChangeListener mOnCheckedChangeListener;
	private int currentCheckedId;
	private String currentCheckedTag;
	
	public GridRadioGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public GridRadioGroup(Context context) {
		super(context);
		init();
	}
	
    private void init() {
	}
    
    @Override
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
    	this.mOnCheckedChangeListener = listener;
    }

	@Override
    public void addView(final View child, int index, ViewGroup.LayoutParams params) {
     if (child instanceof RadioButton) {
     	
     	((RadioButton) child).setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_UP:
						((RadioButton) child).setChecked(true);
						checkRadioButton((RadioButton) child);
						if(mOnCheckedChangeListener != null){
							mOnCheckedChangeListener.onCheckedChanged(GridRadioGroup.this, child.getId());
						}
						break;
					default:
						break;
					}
					return true;
				}
			});
     	
     } else if(child instanceof LinearLayout){
     	int childCount = ((LinearLayout) child).getChildCount();
     	for(int i = 0; i < childCount; i++){
     		View view = ((LinearLayout) child).getChildAt(i);
     		if (view instanceof RadioButton) {
                 final RadioButton button = (RadioButton) view;

             	
             	((RadioButton) button).setOnTouchListener(new OnTouchListener() {
     				
     				@Override
     				public boolean onTouch(View v, MotionEvent event) {
     					switch (event.getAction()) {
    					case MotionEvent.ACTION_UP:
    						((RadioButton) button).setChecked(true);
         					checkRadioButton((RadioButton) button);
         					if(mOnCheckedChangeListener != null){
         						mOnCheckedChangeListener.onCheckedChanged(GridRadioGroup.this, button.getId());
         					}
							break;
						default:
							break;
						}
     					/*((RadioButton) button).setChecked(true);
     					checkRadioButton((RadioButton) button);
     					if(mOnCheckedChangeListener != null){
     						mOnCheckedChangeListener.onCheckedChanged(GridRadioGroup.this, button.getId());
     					}*/
     					return true;
     				}
     			});
                 
             }
     	}
     }

     super.addView(child, index, params);
 }
 
 private void checkRadioButton(RadioButton radioButton){
 	View child;
 	int radioCount = getChildCount();
 	for(int i = 0; i < radioCount; i++){
 		child = getChildAt(i);
 		if (child instanceof RadioButton) {
 			if(child == radioButton){
 				// do nothing
 			} else {
 				((RadioButton) child).setChecked(false);
 			}
 		} else if(child instanceof LinearLayout){
 			int childCount = ((LinearLayout) child).getChildCount();
 			for(int j = 0; j < childCount; j++){
 				View view = ((LinearLayout) child).getChildAt(j);
 				if (view instanceof RadioButton) {
 					final RadioButton button = (RadioButton) view;
 	    			if(button == radioButton){
 	    				// do nothing
 	    			} else {
 	    				((RadioButton) button).setChecked(false);
 	    			}
 				}
 			}
 		}
 	}
 }

public int getCurrentCheckedId() {
	return currentCheckedId;
}

public void setCurrentCheckedId(int currentCheckedId) {
	this.currentCheckedId = currentCheckedId;
}

public String getCurrentCheckedTag() {
	return currentCheckedTag;
}

public void setCurrentCheckedTag(String currentCheckedTag) {
	this.currentCheckedTag = currentCheckedTag;
}

	/**
	 * 用于替代 check(id) 方法. 
	 * check 方法 会导致在linearlayout 下的 radiobutton 在checked 为false 时 背景颜色依然是checked 状态
	 * 该方法能很好的解决该问题
	 * @param id
	 */
	public void checkRadioButtonById(int id){
		RadioButton rb = (RadioButton) findViewById(id);
		rb.setChecked(true);
		checkRadioButton(rb);
		setCurrentCheckedId(id);
	}
	
	/**
	 * 代替 getCheckedRadioButtonId() 方法. 用于弥补在 linearlayout 里面的 radiobutton 为checked的时 返回的 checkedid 为1 的问题
	 * @return
	 */
	public int getCheckedId(){
		View child;
		int radioCount = getChildCount();
	 	for(int i = 0; i < radioCount; i++){
	 		
	 		child = getChildAt(i);
	 		if (child instanceof RadioButton) {
	 			if(((RadioButton) child).isChecked()){
	 				return child.getId();
	 			}
	 		} else if(child instanceof LinearLayout){
	 			int childCount = ((LinearLayout) child).getChildCount();
	 			for(int j = 0; j < childCount; j++){
	 				View view = ((LinearLayout) child).getChildAt(j);
	 				if (view instanceof RadioButton) {
	 					if(((RadioButton) view).isChecked()){
	 		 				return view.getId();
	 		 			}
	 				}
	 			}
	 		}
	 	}
	 	return -1;
	}
}
