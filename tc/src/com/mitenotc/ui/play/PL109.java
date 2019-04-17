package com.mitenotc.ui.play;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.mitenotc.bean.AnyCHnineBean;
import com.mitenotc.tc.R;
import com.mitenotc.ui.adapter.AnyCHnineAdapter;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.ui.buy.BaseBuyAnnotation;
import com.mitenotc.ui.ui_utils.CustomButton;
/**
 * 109	胜负彩任9场
 * @author wanli
 *
 */

public class PL109 extends BaseFragment implements OnClickListener {
     private Button common_btn;//摇一摇
     private CheckBox show_bet_ratio;//是否显示百分比设置
     private TextView arrange_top_playmethod_tv;//摇一摇右边提示
	 private ListView anyChoose_nine_lv;//胜负彩任9场 选择在列表
	 private AnyCHnineAdapter lv_adapter;
//	 private CustomButton customButton=new CustomButton(mActivity);
		
	  //---TODO 测试
	 private static List<List<String>> VSStringlist; 
	 private static  Map<String,List<String>> ScaleMap;
	 
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.play_any9);
		
		init();
		
	}
	
	private void init() {
		//测试数据
		testmsg();
		
		show_bet_ratio=(CheckBox) findViewById(R.id.show_bet_ratio);
		common_btn=(Button) findViewById(R.id.yaoyiyao_common_btn);
		arrange_top_playmethod_tv=(TextView) findViewById(R.id.arrange_top_playmethod_tv);
		anyChoose_nine_lv=(ListView) findViewById(R.id.anyChoose_nine_lv);
		
		lv_adapter=new AnyCHnineAdapter(mActivity,true);
		setListen();
	}
	
	private void testmsg() {
		VSStringlist=new ArrayList<List<String>>();
		ScaleMap=new HashMap<String, List<String>>();
		List<String> list;
		List<String> list2;
		
		for (int i = 0; i <= 1; i++) {
			list=new ArrayList<String>();
			for (int j = 1; j < 15; j++) {
				list.add("主场"+j);
				
			}
			VSStringlist.add(i,list);
		}
		for (int i = 0; i <= 2 ; i++) {
			list2=new ArrayList<String>();
			for (int j = 1; j < 15; j++) {
				list2.add("99%");
			}
			ScaleMap.put(i+"", list2);
			
		}
       
        
		AnyCHnineBean.getInstance().setScaleMap(ScaleMap);
		AnyCHnineBean.getInstance().setVSStringlist(VSStringlist);
	}

	private void setListen() {
		show_bet_ratio.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
		            lv_adapter=new AnyCHnineAdapter(mActivity,true);
				}else{
		            lv_adapter=new AnyCHnineAdapter(mActivity,false);
				}
			}
		});
		common_btn.setOnClickListener(this);
		anyChoose_nine_lv.setAdapter(lv_adapter);
	}

	@Override
	public void onClick(View v) {
		 switch (v.getId()) {
			case R.id.yaoyiyao_common_btn:
				
				break;

			default:
				break;
		}
	}
}
