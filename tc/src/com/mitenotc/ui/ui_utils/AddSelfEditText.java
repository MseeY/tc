package com.mitenotc.ui.ui_utils;

import org.apache.commons.lang3.StringUtils;

import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.ui.ui_utils.MGridView.ActionUpListener;
import com.mitenotc.utils.SPUtil;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
/**
 * 自定义编辑框
 * @author wanli
 *
 */
public class AddSelfEditText extends LinearLayout {
	  private  EditText mEditText;
	  private  Button subtract_btn;//减
	  private  Button add_btn;//加
	  private  Context mContext=null;
	  private  String text;//EditText对应的内容text
	  private  int hint_resid=R.string.zn_hint_1_ed;//EditText对应的内容hint  资源id
	  
	  private boolean tag_open_listener=false;
	  
	  private boolean HINT_OR_TEXT=false;
	 
	  
	  /**
	   * 三种up事件的区分
	   * 0  ：没有变化 （初始化值）
	   * 1  ：减
	   * 2  ：编辑完成
	   * 3  ：加 
	   */
     public   int ADD_SUBTRACT=0;
	  //监听事件
	  public ActionUpListener actionUpListener;
	  
	  public interface ActionUpListener{
		  void onActionUP();
	  }
	  //监听事件
	  public AddTextChangedListener addtextChengeListener;
	  
	  public interface AddTextChangedListener{
		  void textChanged(CharSequence s, int start, int before, int count);
	  }
	  
	 public AddSelfEditText(Context context) {
		super(context);
		this.mContext=context;
		init();
	
	 }

	public AddSelfEditText(final Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		Resources.Theme theme = context.getTheme();
		inflaterLayout(context);//导入布局
		
		init();
		addListener();  
	}
   
	private void inflaterLayout(Context context) {
		LayoutInflater.from(context).inflate(R.layout.custom_edittext, this);
	}

	private void init() {
		mEditText=(EditText) findViewById(R.id.mEdit);
		subtract_btn=(Button) findViewById(R.id.subtract_btn);
		add_btn= (Button) findViewById(R.id.add_btn);
		
				
	}

	private void addListener() {

//		加 Touch
		if(add_btn!=null)
		add_btn.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(mEditText==null)return false;
				if(event.getAction()==MotionEvent.ACTION_UP
						&&
						mEditText.getText()!=null
						&&!"".equals(mEditText.getText().toString())){
					
					int num=Integer.valueOf(mEditText.getText().toString());
					num++;
					
					mEditText.setText(Integer.toString(num));
					
     				if (actionUpListener != null) {
     					
     					setADD_SUBTRACT(3);
     					actionUpListener.onActionUP();
     					
     				}
				}
				return false;
			}
		});
//		减Touch
		if(subtract_btn!=null)
		subtract_btn.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
                 if(event.getAction()==MotionEvent.ACTION_UP
                		 &&
 						mEditText.getText()!=null
 						 &&
 						!"".equals(mEditText.getText().toString())){
                	 
                		 int num = Integer.valueOf(mEditText.getText().toString());  
    				     num--;
    				     if(num >= 1){
    				    	 
    				    	 mEditText.setText(Integer.toString(num)); 
    				     }else{
    				    	 mEditText.setHint("1"); 
    				    	 
    				     }
    				     
         				if (actionUpListener != null) {
         					setADD_SUBTRACT(1);
         					actionUpListener.onActionUP();
         				}
                	 }
                 return false;	
			}
		});
		
//		mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
//			
//			@Override
//			public void onFocusChange(View v, boolean hasFocus) {
//				if(hasFocus)
//				{
//					if(StringUtils.isBlank(mEditText.getText().toString())){
//					  return;
//				    }else if("0".equals(mEditText.getText().toString())){
//					  return;
//				    }
//					// 编辑EditText  text改变事件
//	 				if (addtextChengeListener != null)
//	 				{
//	 					addtextChengeListener.textChanged();
//	 				}
//				}
//				
//			}
//		});
		if(mEditText!=null){
		mEditText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				tag_open_listener=true;
				mEditText.setFocusable(true);
				mEditText.setCursorVisible(true);
			}
		});
//		涉及到初始化  不能使用观察着模式
		mEditText.addTextChangedListener(new TextWatcher() {
			String str="";
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(tag_open_listener){
					////System.out.println("tag_open_listener="+tag_open_listener);
						if(StringUtils.isBlank(mEditText.getText().toString())){
							return;
						}else if("0".equals(mEditText.getText().toString())){
							return;
						}
						// 编辑EditText  text改变事件
		 				if (addtextChengeListener != null)
		 				{
		 					addtextChengeListener.textChanged(s, start, before, count);
		 				}
				}
				str=s.toString();
			 }
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				if(mEditText!=null){
					mEditText.setFocusable(true);
					mEditText.setCursorVisible(true);
				}
			}
			
			@Override
			public void afterTextChanged(Editable edtb) {
			}
		});
		}
		
		
	}

	
	public  String getHint(){
		return mEditText.getHint().toString();
	}
	/**
	 * @return the text
	 */
	public String getText() {
		if(!"".equals(mEditText.getText().toString())||mEditText.getText()!=null){
			text=mEditText.getText().toString();
		}
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		    if(mEditText !=null){
		    	mEditText.setText(text);
		    	mEditText.setHint(text);
		    }
		this.text = text;
	}
	public void setText(CharSequence cs) {
		if(mEditText !=null){
			mEditText.setText(cs);
			mEditText.setHint(cs);
		}
		this.text = cs.toString();
	}

	/**
	 * @return the actionUpListener
	 */
	public ActionUpListener getActionUpListener() {
		return actionUpListener;
	}

	/**
	 * @param actionUpListener the actionUpListener to set
	 */
	public void setActionUpListener(ActionUpListener actionUpListener) {
		this.actionUpListener = actionUpListener;
	}

	/**
	 * @return the aDD_SUBTRACT
	 */
	public int getADD_SUBTRACT() {
		return ADD_SUBTRACT;
	}

	/**
	 * @param aDD_SUBTRACT the aDD_SUBTRACT to set
	 */
	public void setADD_SUBTRACT(int aDD_SUBTRACT) {
		ADD_SUBTRACT = aDD_SUBTRACT;
	}

	/**
	 * @return the hint_resid
	 */
	public int getHint_resid() {
		return hint_resid;
	}

	/**
	 * @param hint_resid the hint_resid to set
	 */
	public void setHint(int hint_resid) {
		if(mEditText!=null){
			mEditText.setHint(hint_resid);
		}
		this.hint_resid = hint_resid;
	}
	/**
	 * @param hint_resid the hint_resid to set
	 */
	public void setHint(String hint_resStr) {
		if(mEditText!=null){
			mEditText.setHint(hint_resStr);
		}
	}
	/**
	 * @param hint_resid the hint_resid to set
	 */
	public void setTextMaxLength(int len) {
		if(mEditText!=null){
			InputFilter[] filters= {new InputFilter.LengthFilter(len)};  
			mEditText.setFilters(filters); 
		}
	}

	public AddTextChangedListener getAddtextChengeListener() {
		return addtextChengeListener;
	}

	public void setAddtextChengeListener(
			AddTextChangedListener addtextChengeListener) {
		this.addtextChengeListener = addtextChengeListener;
	}

	


}
