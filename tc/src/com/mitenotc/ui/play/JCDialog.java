package com.mitenotc.ui.play;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.mitenotc.bean.MessageBean;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.utils.AppUtil;

import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.ToggleButton;

public class JCDialog extends Dialog{

	private ToggleButtonGroup linearLayout1;
	private ToggleButtonGroup linearLayout2;
	private ToggleButtonGroup linearLayout3;
	private ToggleButtonGroup linearLayout4;
	private ToggleButtonGroup linearLayout5;
	private String[] win = {"1:0","2:0","2:1","3:0","3:1","3:2","4:0","4:1","4:2","5:0","5:1","5:2","胜其他"};
	private String[] equal = {"0:0","1:1","2:2","3:3","平其他"};
	private String[] lose = {"0:1","0:2","1:2","0:3","1:3","2:3","0:4","1:4","2:4","0:5","1:5","2:5","负其他"};
	private List<ToggleButton> winList = new ArrayList<ToggleButton>();
	private List<ToggleButton> equList = new ArrayList<ToggleButton>();
	private List<ToggleButton> losList = new ArrayList<ToggleButton>();
	private List<ToggleButton> allList = new ArrayList<ToggleButton>();
	private TextView T0;
	private TextView T2;
	private Context mContext;
	
	public JCDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		mContext = context;
		init();
	}

	public JCDialog(Context context, int theme) {
		super(context, R.style.dialog_theme);
		mContext = context;
		init();
	}

	public JCDialog(Context context) {
		super(context,R.style.dialog_theme);
		mContext = context;
		init();
	}
	
	private void init() {
		setContentView(R.layout.d52dialog_layout);
		linearLayout1 = (ToggleButtonGroup) findViewById(R.id.linearLayout1);
		linearLayout2 = (ToggleButtonGroup) findViewById(R.id.linearLayout2);
		linearLayout3 = (ToggleButtonGroup) findViewById(R.id.linearLayout3);
		linearLayout4 = (ToggleButtonGroup) findViewById(R.id.linearLayout4);
		linearLayout5 = (ToggleButtonGroup) findViewById(R.id.linearLayout5);
		T0 = (TextView) findViewById(R.id.T0);//主队
		T2 = (TextView) findViewById(R.id.T2);//客队
//		addToggleButton(linearLayout1, 1, 0, 1);
//		addToggleButton(linearLayout1, 1, 2, 3);
//		addToggleButton(linearLayout1, 1, 4, 5);
//		addToggleButton(linearLayout1, 1, 6, 7);
//		addToggleButton(linearLayout1, 1, 8, 9);
//		addToggleButton(linearLayout1, 1, 10, 11);
//		addToggleButton(linearLayout1, 1, 12, 13);
//		
//		addToggleButton(linearLayout2, 1, 0, 1);
//		addToggleButton(linearLayout2, 1, 2, 3);
//		addToggleButton(linearLayout2, 1, , 1);
//		addToggleButton(linearLayout2, 1, 0, 1);
//		addToggleButton(linearLayout2, 1, 0, 1);
//		addToggleButton(linearLayout2, 1, 0, 1);
//		addToggleButton(linearLayout2, 2.01f, 10, 11);
//		addToggleButton(linearLayout4, 1, 10, 11);
//		addToggleButton(linearLayout4, 1, 12, 13);
//		addToggleButton(linearLayout5, 2.01f, 10, 11);
		
		
		for (int i = 0; i < linearLayout1.getChildCount(); i++) {
			View childAt = linearLayout1.getChildAt(i);
			if(childAt instanceof ToggleButton){
				ToggleButton btn = (ToggleButton)childAt;
				winList.add(btn);
				btn.setTag("0");
			}
			
			View childAt1 = linearLayout4.getChildAt(i);
			if(childAt instanceof ToggleButton){
				ToggleButton btn = (ToggleButton)childAt1;
				losList.add(btn);
				btn.setTag("2");
			}
		}
		
		for (int i = 0; i < linearLayout2.getChildCount(); i++) {
			View childAt = linearLayout2.getChildAt(i);
			if(childAt instanceof ToggleButton){
				ToggleButton btn = (ToggleButton)childAt;
				winList.add(btn);
				btn.setTag("0");
			}
			
			View childAt1 = linearLayout5.getChildAt(i);
			if(childAt instanceof ToggleButton){
				ToggleButton btn = (ToggleButton)childAt1;
				losList.add(btn);
				btn.setTag("2");
			}
		}
		
		for (int i = 0; i < linearLayout3.getChildCount(); i++) {
			View childAt = linearLayout3.getChildAt(i);
			if(childAt instanceof ToggleButton){
				ToggleButton btn = (ToggleButton)childAt;
				equList.add(btn);
				btn.setTag("1");
			}
		}
	}
	
	public void addToggleButton(ToggleButtonGroup toggle,float weight,int tb,int v){
		ToggleButton tbg = new ToggleButton(mContext);
		tbg.setTextSize(12);
		tbg.setBackgroundResource(R.drawable.lq_toggle_bg);
		tbg.setPadding(0, 0, 0, 0);
		LayoutParams par1 = new LayoutParams(0, LayoutParams.MATCH_PARENT);
		par1.weight= weight;
		tbg.setLayoutParams(par1);
		toggle.addView(tbg, tb);
		
		View view = new View(mContext);
		view.setBackgroundResource(R.color.tc_gray);
		par1 = new LayoutParams(1, LayoutParams.MATCH_PARENT);
		view.setLayoutParams(par1);
		toggle.addView(view, v);
	}
	
	public void initDialogData(String[] win1,String[] equ1,String[] los1,String k,String m){
		
		if(!AppUtil.isEmpty(k)){
			T0.setText(k);
		}else{
			T0.setText("---");
		}
		
		if(!AppUtil.isEmpty(m)){
			T2.setText(m);
		}else{
			T2.setText("---");
		}
		
		for (int i = 0; i < winList.size(); i++) {
			String winStr = StringUtils.replaceEach(MyApp.res.getString(R.string.jc_211dialog_sp), new String[] { "NAME", "SP" }, new String[] { win[i], win1[i]});
			String losStr = StringUtils.replaceEach(MyApp.res.getString(R.string.jc_211dialog_sp), new String[] { "NAME", "SP" }, new String[] { lose[i], los1[i]});
			winList.get(i).setText(Html.fromHtml(winStr));
			losList.get(i).setText(Html.fromHtml(losStr));
			allList.add(winList.get(i));
			allList.add(losList.get(i));
		}
		
		for (int i = 0; i < equList.size(); i++) {
			String equStr = StringUtils.replaceEach(MyApp.res.getString(R.string.jc_211dialog_sp), new String[] { "NAME", "SP" }, new String[] { equal[i], equ1[i]});
			equList.get(i).setText(Html.fromHtml(equStr));
			allList.add(equList.get(i));
		}
	}
	
	//给选中的Togglebutton着色
	public void setInitList(List<String> list){
		if(list!=null){
		for (int i = 0; i < allList.size(); i++) {
			for (int j = 0; j < list.size(); j++) {
				if(list.get(j).toString().equals(allList.get(i).getText().subSequence(0, 3).toString())){
					allList.get(i).setChecked(true);
				}
			}
		}
	}
	}
	
	
	public void addListenerJCDialog(View.OnClickListener cancel_btn_Listener,
			View.OnClickListener affirm_btn_Listener){
		if(cancel_btn_Listener!=null){
			findViewById(R.id.cancel_btn).setOnClickListener(cancel_btn_Listener);//取消
		}
		if(affirm_btn_Listener!=null){
			findViewById(R.id.affirm_btn).setOnClickListener(affirm_btn_Listener);//确认
		}
	}

}
