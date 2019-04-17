package com.mitenotc.ui;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.mitenotc.bean.RATEBean;
import com.mitenotc.enums.CustomTagEnum;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.ui.play.IntelligentScheme;
import com.mitenotc.ui.ui_utils.AddSelfEditText;
import com.mitenotc.ui.ui_utils.AddSelfEditText.AddTextChangedListener;
import com.mitenotc.ui.ui_utils.MyToast;
import com.mitenotc.ui.ui_utils.AddSelfEditText.ActionUpListener;
import com.mitenotc.utils.AppUtil;
import com.mitenotc.utils.DensityUtil;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class FADialog extends Dialog {
    private AddSelfEditText expect_asEdtext;//连续追号期数
    private AddSelfEditText double_asEdtext;//起始倍数

	private Button create_scheme_btn;//方案
	private Button abolish_btn;//取消
	private RadioButton anticipate_one_RB;//设置预期盈利方案一
	private RadioButton anticipate_two_RB;//设置预期盈利方案二
	private RadioButton anticipate_three_RB;//设置预期盈利方案三
	
	private EditText FangAn_one_ed;//全程最低盈利率
	private EditText FangAn_two_ed1;//前 n 期
	private EditText FangAn_two_ed2;//盈利  %
	private EditText FangAn_two_ed3;//之后  %
	private EditText FangAn_three_ed;//全程最低盈利多少元
	
	private CheckBox FangAn_stop_cb;//停止追期cb
	private IntelligentScheme illScheme;
	private boolean IS_CREATE_SCHEME_TAG=false; //默认是FALSE 
	
	
	
	/** 用于记录和判断EditText 编辑内容值得的大小**/
	
	private int bfLen;//记录字符串被删除字符之前  即初始值（hint 所指定的值）
	private int location=0;//记录光标的位置 
	private Context ctx;

	public FADialog(Context context,IntelligentScheme obj) {
		super(context, R.style.dialog_theme);
		illScheme = obj;
		this.ctx=context;
		init();
		setCanceledOnTouchOutside(false);//触动了窗外的界限不能取消界面
	}

	public FADialog(Context context) {
		super(context, R.style.dialog_theme);
		this.ctx=context;
		init();
		setCanceledOnTouchOutside(false);//触动了窗外的界限不能取消界面
	}

	public FADialog(Context context, int theme) {
		super(context, theme);
		init();
		setCanceledOnTouchOutside(false);//触动了窗外的界限不能取消界面
	}

	private void init() {
		setContentView(R.layout.alter_scheme);

		LayoutParams lp = getWindow().getAttributes();
		lp.x = 0;
		lp.y = 10;
		
		initView();
		
	}

	private void initView() {
		FangAn_stop_cb=(CheckBox)findViewById(R.id.FangAn_stop_cb);//停止追期cb
		FangAn_one_ed=(EditText)findViewById(R.id.FangAn_one_ed);//全程最低盈利率
		FangAn_two_ed1=(EditText)findViewById(R.id.FangAn_two_ed1);//前 n 期
		FangAn_two_ed2=(EditText)findViewById(R.id.FangAn_two_ed2);//盈利  %
		FangAn_two_ed3=(EditText)findViewById(R.id.FangAn_two_ed3);//之后  %
		FangAn_three_ed=(EditText)findViewById(R.id.FangAn_three_ed);//全程最低盈利多少元
		expect_asEdtext=(AddSelfEditText) findViewById(R.id.expect_asEdtext);
		
		double_asEdtext=(AddSelfEditText) findViewById(R.id.double_asEdtext);
		
		anticipate_one_RB=(RadioButton) findViewById(R.id.anticipate_one_RB);//设置预期盈利方案一
		anticipate_two_RB=(RadioButton) findViewById(R.id.anticipate_two_RB);//设置预期盈利方案二
		anticipate_three_RB=(RadioButton) findViewById(R.id.anticipate_three_RB);//设置预期盈利方案三
		
		create_scheme_btn=(Button) findViewById(R.id.create_scheme_btn);//方案
		abolish_btn=(Button) findViewById(R.id.abolish_btn);//取消
		
//		初始化数据
		expect_asEdtext.setHint(R.string.zn_hint_10_ed);
		double_asEdtext.setHint(R.string.zn_hint_2_ed);
		expect_asEdtext.setTextMaxLength(3);
		double_asEdtext.setTextMaxLength(4);
//		追期数
		if(StringUtils.isBlank(RATEBean.getInstance().getDEFAULT_XH())){
			expect_asEdtext.setText("10");
		}else {
			expect_asEdtext.setText(RATEBean.getInstance().getDEFAULT_XH());
		}
//		倍数
		if(StringUtils.isBlank(RATEBean.getInstance().getBS())){
			double_asEdtext.setText("1");
		}else {
			double_asEdtext.setText(RATEBean.getInstance().getBS());
		}
	
//		确定用户设置的方案  默认为方案一  RadioButton
	    switch (RATEBean.getInstance().getFANG_AN()) {
//	    
			case 1:
				anticipate_two_RB.setChecked(false);//设置预期盈利方案二
				anticipate_three_RB.setChecked(false);//设置预期盈利方案三
				anticipate_one_RB.setChecked(true);//设置预期盈利方案一
				ableFangAnOne_edset();
				FangAn_one_ed.setText(RATEBean.getInstance().getONE_YLL().replace("%", ""));//方案一盈利率
				break;
			case 2:
				anticipate_one_RB.setChecked(false);//设置预期盈利方案一
				anticipate_three_RB.setChecked(false);//设置预期盈利方案三
				
				anticipate_two_RB.setChecked(true);//设置预期盈利方案二
				ableFangAnTwo_edset();
				
				FangAn_two_ed1.setText(RATEBean.getInstance().getTWO_QH());//前 n 期
				FangAn_two_ed2.setText(RATEBean.getInstance().getTWO_QYLL());//盈利  %
				FangAn_two_ed3.setText(RATEBean.getInstance().getTWO_HYLL());//之后  %
				break;
			case 3:
				anticipate_one_RB.setChecked(false);//设置预期盈利方案一
				anticipate_two_RB.setChecked(false);//设置预期盈利方案二
				
				anticipate_three_RB.setChecked(true);//设置预期盈利方案三
				ableFangAnThree_edset();
				
				FangAn_three_ed.setText(RATEBean.getInstance().getTHREE_RMB());//盈利  元
				break;
		    }
	    amendPadding();
		
		setListener();
	}

	
	public void addListener(View.OnClickListener create_scheme_btn_Listener,
			View.OnClickListener abolish_btn_Listener){
//		生成方案
		if(create_scheme_btn_Listener!=null){
		
			create_scheme_btn.setOnClickListener(create_scheme_btn_Listener);
//		取消
		}
		if(abolish_btn_Listener!=null){
			abolish_btn.setOnClickListener(abolish_btn_Listener);
		}
	   show();
	}
	private void setListener() {
		findViewById(R.id.explain_text).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				View mView=View.inflate(ctx, R.layout.help_view, null);
				final Dialog alertDialog = new Dialog(ctx, R.style.dialog_theme);
				             alertDialog.setContentView(mView);
				             alertDialog.setCancelable(true);
				             alertDialog.getWindow().setLayout(DensityUtil.dip2px(ctx, 300), DensityUtil.dip2px(ctx,200));
				             alertDialog.show(); 
				    mView.findViewById(R.id.help_btn).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						if(alertDialog!=null && alertDialog.isShowing()){
							alertDialog.cancel();
						}
					 }
				    });
			               
			}

		});//停止追期cb
//		停止追期
		FangAn_stop_cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					IntelligentScheme.setIS_STOP("1");//中奖后是否停止追号(0不停止；1停止)
//					MyToast.showToast(ctx, "确定停止！");// 8-19 取消提示
				}else{
					IntelligentScheme.setIS_STOP("0");
//					MyToast.showToast(ctx, "取消停止！");// 8-19 取消提示
				}
			}
		});
		
//		默认预期盈利方案为一 
		anticipate_one_RB.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton cpbButton, boolean ischange) {
				amendPadding();
//				最终为选中的情况下
				if(cpbButton.isChecked()){
					RATEBean.getInstance().setFANG_AN(1);//方案为1
					ableFangAnOne_edset();
				}
			}

	
		});
		//设置预期盈利方案二
		anticipate_two_RB.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton cpbButton, boolean ischange) {
				amendPadding();
				if(cpbButton.isChecked()){
					RATEBean.getInstance().setFANG_AN(2);//方案为2
					ableFangAnTwo_edset();
				}
			}

		});
		//设置预期盈利方案三
		anticipate_three_RB.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton cpbButton, boolean ischange) {
				amendPadding();
				if(cpbButton.isChecked()){
					RATEBean.getInstance().setFANG_AN(3);//方案为3
					ableFangAnThree_edset();
				}
			}

			
	
		});
//		连续追期
		expect_asEdtext.setAddtextChengeListener(new AddTextChangedListener() {
			
			@Override
			public void textChanged(CharSequence cs, int start, int before, int count) {
				if(StringUtils.isBlank(expect_asEdtext.getText().toString())){
					MyToast.showToast(ctx, "默认连续追期数为10");
					RATEBean.getInstance().setDEFAULT_XH("10");
					return;
				}
				if(StringUtils.isBlank(RATEBean.getInstance().getDEFAULT_XH())){
				     return;	
				}else{
					String strText=expect_asEdtext.getText();
					int temp=Integer.parseInt(strText);
					if(StringUtils.isBlank(MyApp.order.getLotteryId())){//为空处理
						return;
					}
					int temp1=CustomTagEnum.getItemById(Integer.parseInt(MyApp.order.getLotteryId())).getMaxChasableIssueNum();
					////System.out.println("tempN"+temp1);
					if(temp >= temp1){
						MyToast.showToast(ctx, "最大追期:"+temp1+"期");
						RATEBean.getInstance().setDEFAULT_XH(String.valueOf(temp1));
					}else{
						RATEBean.getInstance().setDEFAULT_XH(strText);
					}
				}
					
//				comparisonValuation(1,expect_asEdtext.getHint(),expect_asEdtext.getText().toString());
			}
		});
//		 起始倍数
		double_asEdtext.setAddtextChengeListener(new AddTextChangedListener() {
			@Override
			public void textChanged(CharSequence cs, int start, int before, int count) {
				if(StringUtils.isBlank(double_asEdtext.getText().toString())){
					MyToast.showToast(ctx, "默认倍数为1");
					RATEBean.getInstance().setBS("1");
					return;
			     }
				
				if(StringUtils.isBlank(RATEBean.getInstance().getBS())){
				     return;	
				}else{
					String strText=double_asEdtext.getText();
					int temp=Integer.parseInt(strText);
					if(1 > temp){
						MyToast.showToast(ctx, "默认倍数为1");
						RATEBean.getInstance().setBS("1");
					}else{
						RATEBean.getInstance().setBS(strText);
					}
				}
				
//				comparisonValuation(2,double_asEdtext.getHint(),strText);
			}
		});
		
		//全程最低盈利率   FangAn_one_ed   addTextChangedListener
		FangAn_one_ed.addTextChangedListener(new TextWatcher() {
		
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence csq, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable edtb) {
				
				if(StringUtils.isBlank(FangAn_one_ed.getText().toString())){
					MyToast.showToast(ctx, "默认全程最低盈利率为30%！");
					RATEBean.getInstance().setONE_YLL("30");
					return;
				}
			}			
//				comparisonValuation(3,FangAn_one_ed.getHint().toString()
//								,FangAn_one_ed.getText().toString());
		});
		//前 n 期
		FangAn_two_ed1.addTextChangedListener(new TextWatcher() {
		
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence csq, int start, int count,
					int after) {
				
				
			}
			
			@Override
			public void afterTextChanged(Editable edtb) {
				
				String tempNStr=FangAn_two_ed1.getText().toString();
				if(StringUtils.isBlank(tempNStr)){
					MyToast.showToast(ctx, "默认为前5期！");
					RATEBean.getInstance().setTWO_QH("5");
					return;
				}else{
					if(StringUtils.isBlank(expect_asEdtext.getText().toString())){//追期数为空
						MyToast.showToast(ctx, "前"+tempNStr+"期不能小于连续追号期数！");
					}
					RATEBean.getInstance().setTWO_QH(tempNStr);
				}
//						 comparisonValuation(4,FangAn_two_ed1.getHint().toString()
//								   ,FangAn_two_ed1.getText().toString());
				}
		});
		//盈利  %
		FangAn_two_ed2.addTextChangedListener(new TextWatcher() {
		
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence csq, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable edtb) {
				String tempNStr=FangAn_two_ed2.getText().toString();
				if(StringUtils.isBlank(tempNStr)){
					MyToast.showToast(ctx, "默认为前5期盈利为50%！");
					RATEBean.getInstance().setTWO_QYLL("50");
					return;
				}
//						comparisonValuation(5,FangAn_two_ed2.getHint().toString()
//								   ,FangAn_two_ed2.getText().toString());
			}
		});
		//之后  %
		FangAn_two_ed3.addTextChangedListener(new TextWatcher() {
		
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence csq, int start, int count,
					int after) {
				
				
			}
			
			@Override
			public void afterTextChanged(Editable edtb) {
				String tempNStr=FangAn_two_ed3.getText().toString();
				if(StringUtils.isBlank(tempNStr)){
					MyToast.showToast(ctx, "默认为前5期之后盈利为20%！");
					RATEBean.getInstance().setTWO_QYLL("20");
					return;
				}
					
//				 comparisonValuation(6,FangAn_two_ed3.getHint().toString()  ,FangAn_two_ed3.getText().toString());
			}
		});
		
		//全程最低盈利多少元
		FangAn_three_ed.addTextChangedListener(new TextWatcher() {
		
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence csq, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable edtb) {
				String tempNStr=FangAn_three_ed.getText().toString();
					if(StringUtils.isBlank(tempNStr)){
						MyToast.showToast(ctx, "默认全程最低盈利30元！");
						RATEBean.getInstance().setTHREE_RMB("30");
						return;
					}
					
//						comparisonValuation(7,FangAn_three_ed.getHint().toString(),
//					             FangAn_three_ed.getText().toString());
				}
		});
	}
	/**
	 *   与方案一  相关的隐藏 与显示
	 */
	private  void ableFangAnOne_edset() {
		FangAn_one_ed.setEnabled(true);
		FangAn_one_ed.setBackgroundResource(R.drawable.ed_bg);
		
		FangAn_two_ed1.setEnabled(false);
		FangAn_two_ed2.setEnabled(false);
		FangAn_two_ed3.setEnabled(false);
		FangAn_three_ed.setEnabled(false);
		
		FangAn_two_ed1.setBackgroundResource(R.color.btn_gray_u);
		FangAn_two_ed2.setBackgroundResource(R.color.btn_gray_u);
		FangAn_two_ed3.setBackgroundResource(R.color.btn_gray_u);
		FangAn_three_ed.setBackgroundResource(R.color.btn_gray_u);
	}
	
	/**
	 *   与方案二  相关的隐藏 与显示
	 */
	private void ableFangAnTwo_edset() {
		FangAn_one_ed.setEnabled(false);
		
		FangAn_two_ed1.setEnabled(true);
		FangAn_two_ed2.setEnabled(true);
		FangAn_two_ed3.setEnabled(true);
		
		FangAn_two_ed1.setBackgroundResource(R.drawable.ed_bg);
		FangAn_two_ed2.setBackgroundResource(R.drawable.ed_bg);
		FangAn_two_ed3.setBackgroundResource(R.drawable.ed_bg);
		
		FangAn_three_ed.setEnabled(false);
		FangAn_one_ed.setBackgroundResource(R.color.btn_gray_u);
		FangAn_three_ed.setBackgroundResource(R.color.btn_gray_u);
	}
	/**
	 *   与方案三  相关的隐藏 与显示
	 */
	
	private void ableFangAnThree_edset() {
		FangAn_three_ed.setEnabled(true);
		FangAn_three_ed.setBackgroundResource(R.drawable.ed_bg);
		
		FangAn_two_ed1.setEnabled(false);
		FangAn_two_ed2.setEnabled(false);
		FangAn_two_ed3.setEnabled(false);
		FangAn_one_ed.setEnabled(false);
		
		FangAn_two_ed1.setBackgroundResource(R.color.btn_gray_u);
		FangAn_two_ed2.setBackgroundResource(R.color.btn_gray_u);
		FangAn_two_ed3.setBackgroundResource(R.color.btn_gray_u);
		FangAn_one_ed.setBackgroundResource(R.color.btn_gray_u);
	}
	/**
	 * hint 与  text 对比斌赋值
	 * @param sign 标志
	 * @param hintStr  hint 
	 * @param textStr  text
	 *  1 连续追号
	 *  2 起始倍数 
	 *  3 全程最低盈利率 
	 *  4 前 n 期 
	 *  5 前 n 期 盈利率
	 *  6 前 n 期之后的 盈利率
	 *  7 全程最低盈利率
	 * 
	 */
	
	private String comparisonValuation(int sign ,String hintStr,String textStr) {
		
		   int h=Integer.parseInt(hintStr);
		   int t=Integer.parseInt(textStr);
		   if(t<h){
			   switch (sign) {
					case 1:
							RATEBean.getInstance().setMAX_XH(hintStr);
							MyToast.showToast(ctx, R.string.comparison_toast_text);
							  
						break;
					case 2:
						     RATEBean.getInstance().setBS(hintStr);
						     MyToast.showToast(ctx, R.string.comparison_toast_text1);
						
						break;
					case 3:
						     RATEBean.getInstance().setONE_YLL(hintStr);
						     MyToast.showToast(ctx, R.string.comparison_toast_text2);
						break;
					case 4:
					      	RATEBean.getInstance().setTWO_QH(hintStr);
					      	 MyToast.showToast(ctx, R.string.comparison_toast_text3);
						break;
					case 5:
				        	 RATEBean.getInstance().setTWO_QYLL(hintStr);
				        	 MyToast.showToast(ctx, R.string.comparison_toast_text4);
				    	break;
					
					case 6:
				      	     RATEBean.getInstance().setTWO_HYLL(hintStr);
				      	   MyToast.showToast(ctx, R.string.comparison_toast_text5);
					    break;
					case 7:
				      	     RATEBean.getInstance().setTHREE_RMB(hintStr);
				      	    MyToast.showToast(ctx, R.string.comparison_toast_text6);
				      	
					    break;
					default:
						break;
					}
			   return hintStr;
		   }else if(t>h){
			   switch (sign) {
					case 1:
						        RATEBean.getInstance().setBS(textStr);
						break;
					case 2:
						        RATEBean.getInstance().setBS(textStr);
						
						break;
					case 3:
						         RATEBean.getInstance().setONE_YLL(textStr);
						break;
					case 4:
						         RATEBean.getInstance().setTWO_QH(textStr);
						break;
					case 5:
						         RATEBean.getInstance().setTWO_QYLL(textStr);
				          break;
					case 6:
				                  RATEBean.getInstance().setTWO_HYLL(textStr);
				          break;
					case 7:
				                  RATEBean.getInstance().setTHREE_RMB(textStr);
				          break;
						
					default:
						break;
					}
		   }
		   return  textStr;
		

	}
//  为了界面 设计效果  让文字居中 Edittext 有背景效果
	private void amendPadding(){
		FangAn_one_ed.setPadding(0, 6, 0, 3);//全程最低盈利率
		FangAn_two_ed1.setPadding(0, 6, 0, 3);//前 n 期
		FangAn_two_ed2.setPadding(0, 6, 0, 3);//盈利  %
		FangAn_two_ed3.setPadding(0, 6, 0, 3);//之后  %
		FangAn_three_ed.setPadding(0, 6, 0, 3);//全程最低盈利多少元
	}


	/**
	 * 生成方案
	 */
	public boolean toCreateScheme() {
//   	 *  1 连续追号
		String exptStr=expect_asEdtext.getText();//comparisonValuation(1, expect_asEdtext.getHint(), expect_asEdtext.getText()) ;
		String lotterStr=MyApp.order.getLotteryId();
		int tempN=CustomTagEnum.getItemById(Integer.parseInt(MyApp.order.getLotteryId())).getMaxChasableIssueNum();
		
		if(StringUtils.isBlank(expect_asEdtext.getText())){
				MyToast.showToastLong(ctx, "连续追期不能为空！");
				RATEBean.getInstance().setFANG_AN(0);//取消任何一种方案
				IS_CREATE_SCHEME_TAG=false;
				return IS_CREATE_SCHEME_TAG;
				
		}
		else if( "120".equals(lotterStr)||"122".equals(lotterStr))
		{
			
				if(Integer.parseInt(exptStr) > tempN ){
					MyToast.showToastLong(ctx, "连续追期不能大于"+tempN+"期");
					IS_CREATE_SCHEME_TAG=false;
					return IS_CREATE_SCHEME_TAG;
				}else {
					RATEBean.getInstance().setDEFAULT_XH(exptStr);
					IS_CREATE_SCHEME_TAG=true;
				}
				
	    }else if( "119".equals(lotterStr)|| "112".equals(lotterStr))
	    {
				if(Integer.parseInt(exptStr) > tempN){
					MyToast.showToastLong(ctx, "连续追期不能大于"+tempN+"期");
					IS_CREATE_SCHEME_TAG=false;
					return IS_CREATE_SCHEME_TAG;
				}else {
					RATEBean.getInstance().setDEFAULT_XH(exptStr);
					IS_CREATE_SCHEME_TAG=true;
				}
				
	    }
//		else{
//				RATEBean.getInstance().setDEFAULT_XH(exptStr);
//				IS_CREATE_SCHEME_TAG=true;
//		}
			
		
		
		if(StringUtils.isBlank(double_asEdtext.getText()) && IS_CREATE_SCHEME_TAG==true){
			MyToast.showToastLong(ctx, "投注倍数不能为空,默认为1");
			IS_CREATE_SCHEME_TAG=false;
			RATEBean.getInstance().setFANG_AN(0);
			return IS_CREATE_SCHEME_TAG;
		}else{
//   	 *  2 起始倍数 	
			String  dbeStr=double_asEdtext.getText();//comparisonValuation(2, double_asEdtext.getHint(), double_asEdtext.getText()) ;
			RATEBean.getInstance().setBS(dbeStr);
			IS_CREATE_SCHEME_TAG=true;
		}

//   	 *  3 全程最低盈利率 
//   	 *  4 前 n 期 
//   	 *  5 前 n 期 盈利率
//   	 *  6 前 n 期之后的 盈利率
//   	 *  7 全程最低盈利率
		switch (RATEBean.getInstance().getFANG_AN()) {

		    case 1:
					String oneStr = FangAn_one_ed.getText().toString();
					
					if(StringUtils.isBlank(oneStr) && IS_CREATE_SCHEME_TAG==true)
				     {
						    MyToast.showToastLong(ctx, "全程最低盈利率 不能为空！");
							IS_CREATE_SCHEME_TAG=false;
							RATEBean.getInstance().setFANG_AN(0);
							return IS_CREATE_SCHEME_TAG;
				    	}else  if(AppUtil.isNumeric(oneStr) == false && IS_CREATE_SCHEME_TAG==true){
				    		MyToast.showToastLong(ctx, "全程最低盈利率 不能为负值！");
				    		IS_CREATE_SCHEME_TAG=false;
							RATEBean.getInstance().setFANG_AN(0);
				    	}else {
		//		    		String oedStr=comparisonValuation(3, FangAn_one_ed.getHint().toString(), oneStr) ;
				    		
				    		RATEBean.getInstance().setONE_YLL(oneStr);
				    		IS_CREATE_SCHEME_TAG=true;
				    		RATEBean.getInstance().setFANG_AN(1);
				    	}
				
		    	break;
			case 2:
						String twoStr2 = FangAn_two_ed2.getText().toString();
						String twoStr = FangAn_two_ed3.getText().toString();
					    String twoStr1 = FangAn_two_ed1.getText().toString();
					
					    if(StringUtils.isBlank(twoStr1)){
					    	MyToast.showToastLong(ctx,"指定前期不能为空！");
							IS_CREATE_SCHEME_TAG=false;
							RATEBean.getInstance().setFANG_AN(0);
					    	return IS_CREATE_SCHEME_TAG;
						}else {
							//前 n 期
		//					ted_Str1=comparisonValuation(4, FangAn_two_ed1.getHint().toString(),twoStr1) ;
							
							int temp1=Integer.parseInt(twoStr1);
							int temp2=Integer.parseInt(expect_asEdtext.getText());//不用为空判断之前已经做了为空判断
							
							if(temp1 > temp2){//前 n 期  大于 追期数
								IS_CREATE_SCHEME_TAG=false;
								return IS_CREATE_SCHEME_TAG;
							}else{
								RATEBean.getInstance().setTWO_QH(twoStr1);
								IS_CREATE_SCHEME_TAG=true;
								RATEBean.getInstance().setFANG_AN(2);
							}
						}
					
					    if(StringUtils.isBlank(twoStr2) && IS_CREATE_SCHEME_TAG==true){
					    	MyToast.showToastLong(ctx,"前"+twoStr1+"期的盈利率不能为空！");
					    	IS_CREATE_SCHEME_TAG=false;
					    	RATEBean.getInstance().setFANG_AN(0);
							
						}else {
							//盈利  %
		//					ted_Str2=comparisonValuation(5, FangAn_two_ed2.getHint().toString(), 
		//							twoStr2) ;
							RATEBean.getInstance().setTWO_QYLL(twoStr2);	
							IS_CREATE_SCHEME_TAG=true;
							RATEBean.getInstance().setFANG_AN(2);
						}
						
					
					    if(StringUtils.isBlank(twoStr) && IS_CREATE_SCHEME_TAG==true){
					    	MyToast.showToastLong(ctx,"前"+twoStr1+"期之后的盈利率不能为空！");
					    	IS_CREATE_SCHEME_TAG=false;
					    	RATEBean.getInstance().setFANG_AN(0);
						}else {
							//之后  %
		//					ted_Str3=comparisonValuation(6, FangAn_two_ed3.getHint().toString(), 
		//							twoStr) ;
							RATEBean.getInstance().setTWO_HYLL(twoStr);
							IS_CREATE_SCHEME_TAG=true;
							RATEBean.getInstance().setFANG_AN(2);
						}
				
				break;
			case 3:
					String tStr=FangAn_three_ed.getText().toString();
	//				String hStr=FangAn_three_ed.getHint().toString();
			            //全程最低盈利多少元
					if(StringUtils.isBlank(tStr) && IS_CREATE_SCHEME_TAG==true){
						MyToast.showToastLong(ctx, "盈利金额不能为空！");
						IS_CREATE_SCHEME_TAG=false;
						RATEBean.getInstance().setFANG_AN(0);
					}else {
	//					String treed_Str=comparisonValuation(7,hStr , tStr) ;
						RATEBean.getInstance().setTHREE_RMB(tStr);
						IS_CREATE_SCHEME_TAG=true;
						RATEBean.getInstance().setFANG_AN(3);
					}
				break;
	
			default:
				break;
		}
		return IS_CREATE_SCHEME_TAG;
		
	}

	public Button getCreate_scheme_btn() {
		return create_scheme_btn;
	}

	public Button getAbolish_btn() {
		return abolish_btn;
	}



}
