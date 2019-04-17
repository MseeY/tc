package com.mitenotc.ui.base;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.GloableParams;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.ui.BodyFragment1;
import com.mitenotc.ui.BodyFragment2;
import com.mitenotc.ui.BodyFragment3;
import com.mitenotc.ui.BodyFragment3_Simulation;
import com.mitenotc.ui.BodyFragment3_find;
import com.mitenotc.ui.BodyFragment4;

public class BottomFragment extends BaseFragment{
	
	private RadioGroup rg;
	private RadioButton rb1;
	private RadioButton rb2;
	private RadioButton rb3;
	private RadioButton rb4;
	

	private MessageJson probeMessage;
/*	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.m_rg_bottom_nav, null);
		LayoutParams layoutParams = new LayoutParams(-1,-2);
		view.setLayoutParams(layoutParams);
		return view;
	}*/
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.f_fragment_bottom_rl_nav);
		initView();
		setListener();
		//rg.check(R.id.rb_ui_bottom1);
		rb1.setChecked(true);
	}
	
//    protected void onSaveInstanceState(Bundle outState) { 
//        // TODO Auto-generated method stub 
//        super.onSaveInstanceState(outState); 
//        mActivity.getSupportFragmentManager().putFragment(outState, "mContent", mContent); 
//    } 
	
//	@Override
//	public void onSaveInstanceState(Bundle outState) {
//		// TODO Auto-generated method stub
//		super.onSaveInstanceState(outState);
////		mActivity.getSupportFragmentManager().putFragment(outState, "mActivity", mActivity); 
//	}
	
	private void initView() {
		rg = (RadioGroup) findViewById(R.id.rg_ui_bottom);
		rb1 = (RadioButton) findViewById(R.id.rb_ui_bottom1);
		rb2 = (RadioButton) findViewById(R.id.rb_ui_bottom2);
		rb3 = (RadioButton) findViewById(R.id.rb_ui_bottom3);
		rb4 = (RadioButton) findViewById(R.id.rb_ui_bottom4);
	}
	
	private void setListener() {
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {////System.out.println("onchecked changed");
				switch (checkedId) {
				case R.id.rb_ui_bottom1:
					//System.out.println("1302---3--customWWW1--:");
					replaceMiddle(BodyFragment1.class);
//					MyApp.saveProbeMsg("点击-我的定制", "点击切换底部导航", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
//							new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),"0" , "");
					break;
				case R.id.rb_ui_bottom2:
					replaceMiddle(BodyFragment2.class);
//					MyApp.saveProbeMsg("点击-购彩大厅", "点击切换底部导航", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
//							new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),"0" , "");
					break;
				case R.id.rb_ui_bottom3:
//					replaceMiddle(BodyFragment3_Simulation.class);
					replaceMiddle(BodyFragment3_find.class);
//					MyApp.saveProbeMsg("点击-发现", "点击切换底部导航", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
//							new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),"0" , "");
					break;
				case R.id.rb_ui_bottom4:
					replaceMiddle(BodyFragment4.class);
//					MyApp.saveProbeMsg("点击-我", "点击切换底部导航", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
//							new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),"0" , "");
					break;
				}
			}
		});
	}
	
	public void check(String className){
		if(BodyFragment1.class.getName().equals(className)){
			rb1.setChecked(true);//这种方式 oncheckedchanged 只会触发一次
			rg.check(R.id.rb_ui_bottom1);//使用这种方式 oncheckedchanged 会触发三次
		}else if(BodyFragment2.class.getName().equals(className)){
			rb2.setChecked(true);
			rg.check(R.id.rb_ui_bottom2);
		}
//		else if(BodyFragment3_Simulation.class.getName().equals(className)){
//			rb3.setChecked(true);
//			rg.check(R.id.rb_ui_bottom3);
//		}
		else if(BodyFragment3_find.class.getName().equals(className)){
			rb3.setChecked(true);
			rg.check(R.id.rb_ui_bottom3);
		}
		else if(BodyFragment4.class.getName().equals(className)){
			rb4.setChecked(true);
			rg.check(R.id.rb_ui_bottom4);
		}
	}
}
