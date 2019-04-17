package com.mitenotc.ui.ui_utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.ui.ui_utils.MGridView.CustomItem;
import com.mitenotc.utils.DensityUtil;
import com.mitenotc.utils.FormatUtil;

public class CustomPK extends LinearLayout implements CustomItem{
    
//	private RelativeLayout pk_one_rl;
//	private RelativeLayout pk_two_rl;
//	private RelativeLayout pk_three_rl;
	
	private RelativeLayout select_pk_rl;
	private RelativeLayout unselect_pk_rl;   //豹子    默认选号
	private RelativeLayout unselect_pk_rl2;  //顺子    特殊选号
	private RelativeLayout unselect_pk_rl3;  //包选    特殊选号
	
	private TextView pk_one_textStr;
	private TextView pk_two_textStr;
	private TextView pk_three_textStr;
	
	private TextView pk_one_textStr2;
	private TextView pk_two_textStr2;
	private TextView pk_three_textStr2;
	
	private TextView pk_bx_textStr;
	private TextView pk_bx_textStrBonus;
	private TextView ctm_pk_yiloutv;
	private TextView sl_tv_img;
	private View view;
	private LayoutParams ctmPkWidgetParam;//PK扑克牌的参数 （宽高信息）  默认扑克牌是（41 * 53）  任选玩法时候是（54 * 66）  选中
	
	
	private String OneTextStr="";
	private String TwoTextStr="";
	private String ThreeTextStr="";
	
	
	public CustomPK(Context context) {
		super(context);
		initView(context);
	}

	public CustomPK(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}
	
	 /**
	  * 初始化
	  * @param context
	  */
	private void initView(Context context) {
		view= View.inflate(context,R.layout.custom_pk_layout, this);
		
		select_pk_rl=(RelativeLayout) view.findViewById(R.id.select_pk_rl);
		unselect_pk_rl=(RelativeLayout) view.findViewById(R.id.unselect_pk_rl);
		unselect_pk_rl2=(RelativeLayout) view.findViewById(R.id.unselect_pk_rl2);
		unselect_pk_rl3=(RelativeLayout) view.findViewById(R.id.unselect_pk_rl3);
		ctm_pk_yiloutv=(TextView) findViewById(R.id.ctm_pk_yiloutv);
		
//		pk_one_rl=(RelativeLayout)view. findViewById(R.id.pk_one_rl);//pk 扑克牌  默认是（ 豹子状态）
//		pk_two_rl=(RelativeLayout) view.findViewById(R.id.pk_two_rl);
//		pk_three_rl=(RelativeLayout) view.findViewById(R.id.pk_three_rl);
		
		pk_one_textStr=(TextView)view. findViewById(R.id.pk_one_textStr); // 扑克牌 文字
		pk_two_textStr=(TextView) view.findViewById(R.id.pk_two_textStr);
		pk_three_textStr=(TextView)view. findViewById(R.id.pk_three_textStr);
		
		pk_one_textStr2=(TextView)view. findViewById(R.id.pk_one_textStr2); // 扑克牌 文字
		pk_two_textStr2=(TextView) view.findViewById(R.id.pk_two_textStr2);
		pk_three_textStr2=(TextView)view. findViewById(R.id.pk_three_textStr2);
		
		pk_bx_textStr=(TextView)view. findViewById(R.id.pk_bx_textStr);
		pk_bx_textStrBonus=(TextView)view. findViewById(R.id.pk_bx_textStrBonus);
		sl_tv_img=(TextView)view. findViewById(R.id.sl_tv_img);
		
		select_pk_rl.setVisibility(View.INVISIBLE);//默认不显示
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
			   FormatUtil.setTypeface(pk_one_textStr, 1);
			   FormatUtil.setTypeface(pk_two_textStr, 1);
			   FormatUtil.setTypeface(pk_three_textStr, 1);
			   FormatUtil.setTypeface(pk_one_textStr2, 1);
			   FormatUtil.setTypeface(pk_two_textStr2, 1);
			   FormatUtil.setTypeface(pk_three_textStr2, 1);
//				pk_one_textStr.setTypeface(fontFace);
//				pk_two_textStr.setTypeface(fontFace);
//				pk_three_textStr.setTypeface(fontFace);
//				
//				pk_one_textStr2.setTypeface(fontFace);
//				pk_two_textStr2.setTypeface(fontFace);
//				pk_three_textStr2.setTypeface(fontFace);
				
			}
		}).start();
	}
	/**
	 * 初始化
	 * 猜顺子 特殊的 布局(特殊布局 PK )
	 */
	public void init_csz_specialPkPadding(){
		unselect_pk_rl2.setVisibility(View.VISIBLE);
		unselect_pk_rl.setVisibility(View.GONE);
		unselect_pk_rl3.setVisibility(View.GONE);
	}
	/**
	 * 初始化
	 * 默认布局   PK
	 * 
	 */
	public void init_csz_commonPkPadding(){
		if(unselect_pk_rl!=null)
		unselect_pk_rl.setVisibility(View.VISIBLE);
		if(unselect_pk_rl2!=null)
		unselect_pk_rl2.setVisibility(View.GONE);
		unselect_pk_rl2.destroyDrawingCache();
		if(unselect_pk_rl3!=null)
		unselect_pk_rl3.setVisibility(View.GONE);
		unselect_pk_rl3.destroyDrawingCache();
	}
	/**
	 * 包选  PK
	 * 
	 */
	public void init_bx_specialPkPadding(){
		if(unselect_pk_rl3 != null)
		unselect_pk_rl3.setVisibility(View.VISIBLE);
		if(unselect_pk_rl2 != null)
		unselect_pk_rl2.destroyDrawingCache();
		unselect_pk_rl2.setVisibility(View.GONE);
		if(unselect_pk_rl != null)
        unselect_pk_rl.destroyDrawingCache();
		unselect_pk_rl.setVisibility(View.GONE);
	}
	
	
	 /**
	  * 扑克 数量
	  * @param num
	  * pk_two_textStr： ctmPkWidgetParam 用于包选玩法时候设置扑克的 宽高
	  * unselect_pk_rl： ctmPkWidgetParam 用于包选玩法时候  外围选中框的宽高设置
	  */
	 public void setShowPKNum(int num,android.widget.RelativeLayout.LayoutParams ctmPkWidgetParam,android.widget.RelativeLayout.LayoutParams ctmPkWidgetParam2){
		 switch (num) {
			case 1:
				init_csz_commonPkPadding();
				/*pk_one_rl.setVisibility(View.VISIBLE);
				pk_two_rl.setVisibility(View.GONE);
				pk_three_rl.setVisibility(View.GONE);*/
				
				pk_one_textStr.setVisibility(View.VISIBLE);
				pk_two_textStr.setVisibility(View.GONE);
				pk_three_textStr.setVisibility(View.GONE);
				
//				ctm_pk_yiloutv.setPadding(15, 0, 0, 0);
//				调整 pk 大小
//				ctmPkWidgetParam=new LayoutParams(41, 53);//单张pk
//				select_pk_rl.setLayoutParams(ctmPkWidgetParam);
//				unselect_pk_rl.setLayoutParams(ctmPkWidgetParam2);
				break;
			case 2:
				init_csz_commonPkPadding();
				/*pk_one_rl.setVisibility(View.VISIBLE);
				pk_two_rl.setVisibility(View.VISIBLE);
				pk_three_rl.setVisibility(View.GONE);*/
				
//				pk_one_textStr.setVisibility(View.VISIBLE);
//				pk_two_textStr.setVisibility(View.VISIBLE);
//				pk_three_textStr.setVisibility(View.GONE);
//				ctm_pk_yiloutv.setPadding(12, 0, 0, 0);
				
//				调整 pk 大小
//				ctmPkWidgetParam=new LayoutParams(52, 58);//两张pk
//				select_pk_rl.setLayoutParams(ctmPkWidgetParam);
				
				break;
			case 3://豹子  
				init_csz_commonPkPadding();
				/*pk_one_rl.setVisibility(View.VISIBLE);
				pk_two_rl.setVisibility(View.VISIBLE);
				pk_three_rl.setVisibility(View.VISIBLE);*/
				
				pk_one_textStr.setVisibility(View.VISIBLE);
				pk_two_textStr.setVisibility(View.VISIBLE);
				pk_three_textStr.setVisibility(View.VISIBLE);
//				ctm_pk_yiloutv.setPadding(30, 0, 0, 0);
//				调整 pk 大小
//				ctmPkWidgetParam=new LayoutParams(62, 58);//三张pk
//				select_pk_rl.setLayoutParams(ctmPkWidgetParam);
				
				break;
			case 4://（顺子）
				
				init_csz_specialPkPadding();
				/*pk_one_rl.setVisibility(View.VISIBLE);
				pk_two_rl.setVisibility(View.GONE);
				pk_three_rl.setVisibility(View.GONE);*/
				
				pk_one_textStr.setVisibility(View.VISIBLE);
				pk_two_textStr.setVisibility(View.GONE);
				pk_three_textStr.setVisibility(View.GONE);
//				ctm_pk_yiloutv.setPadding(55, 0, 0, 0);
//				调整 pk 大小
				
//				ctmPkWidgetParam=new LayoutParams(93, 58);//三张pk
//				select_pk_rl.setLayoutParams(ctmPkWidgetParam);
				break;
			case 5://包选
				init_bx_specialPkPadding();
				select_pk_rl.setLayoutParams(ctmPkWidgetParam);
				break;
			case 6://其它 自定义  单张包选
				init_csz_commonPkPadding();
			/*	pk_one_rl.setVisibility(View.VISIBLE);
				pk_two_rl.setVisibility(View.GONE);
				pk_three_rl.setVisibility(View.GONE);*/
				pk_one_textStr.setVisibility(View.VISIBLE);
				pk_two_textStr.setVisibility(View.GONE);
				pk_three_textStr.setVisibility(View.GONE);
//				调整 pk 外布局大小
//				pk_one_rl.setPadding(15, 15, 0, 15);
//				ctm_pk_yiloutv.setPadding(50, 0, 0, 0);
//				ctm_pk_yiloutv.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
				ctm_pk_yiloutv.setGravity(Gravity.CENTER);
				
//				pk_one_rl.setLayoutParams(ctmPkWidgetParam);
				
				unselect_pk_rl.setLayoutParams(ctmPkWidgetParam);
				select_pk_rl.setLayoutParams(ctmPkWidgetParam2);
				break;
		
			default:
				break;
			}
	 };
	 
   /**
   * 
   * 重置 扑克 参数
   * @param ctmPkWidgetParam
   * @param OneTextStr
   * @param TwoTextStr
   * @param ThreeTextStr
   */
	private void updatePKLayoutParamsAndText(android.widget.RelativeLayout.LayoutParams ctmPkWidgetParam,String OneTextStr,String TwoTextStr,String ThreeTextStr){
		
//		pk_one_rl.setLayoutParams(ctmPkWidgetParam);
//		pk_two_rl.setLayoutParams(ctmPkWidgetParam);
//		pk_three_rl.setLayoutParams(ctmPkWidgetParam);
		
		pk_one_textStr.setText(OneTextStr);
		pk_two_textStr.setText(TwoTextStr);
		pk_three_textStr.setText(ThreeTextStr);
	}

	/**
	 * 包选  pk 设置信息
	 * @param textStr
	 * @param textBonusStr
	 * @param resid
	 */
	public void setBx_itemMsg(String textStr,String bonustextStr,int resid){
		pk_bx_textStr.setText(textStr);
		pk_bx_textStrBonus.setText(bonustextStr);
		
		Drawable btnImgRes=null;
		btnImgRes=MyApp.res.getDrawable(resid);
		
//		setCompoundDrawables 画的drawable的宽高是按drawable.setBound()设置的宽高，
//		API:  The Drawables must already have had setBounds(Rect) called.意思是说使用之前必须使用Drawable.setBounds设置Drawable的长宽。
//		 而setCompoundDrawablesWithIntrinsicBounds是画的drawable的宽高是按drawable固定的宽高，
//		即通过getIntrinsicWidth()与getIntrinsicHeight()获得，API：The Drawables' bounds will be set to their intrinsic bounds.
		
//		btnImgRes.setBounds(btnImgRes.getMinimumWidth(),  btnImgRes.getMinimumHeight(),0,0);
//		sl_btn_img.setCompoundDrawables(null, null, btnImgRes,null);
		
		sl_tv_img.setCompoundDrawablesWithIntrinsicBounds(null, null, btnImgRes,null);
	}
	
	public void setPK_yilouText(String textStr) {
		if(ctm_pk_yiloutv!=null){
			ctm_pk_yiloutv.setText(textStr);
		}
	}
	
	public String getOneTextStr() {
		return OneTextStr;
	}

	public void setOneTextStr(String oneTextStr) {
		OneTextStr = oneTextStr;
		if(pk_one_textStr != null){
			pk_one_textStr.setText(oneTextStr);
		}
	}
	public TextView getOneTextVew(){
		return pk_one_textStr;
	}
	
	public TextView getYiLouTextVew(){
		return ctm_pk_yiloutv;
	}
	

	public String getTwoTextStr() {
		return TwoTextStr;
	}

	public void setTwoTextStr(String twoTextStr) {
		TwoTextStr = twoTextStr;
		if(pk_two_textStr!=null){
			pk_two_textStr.setText(twoTextStr);
		}
	}

	public String getThreeTextStr() {
		return ThreeTextStr;
	}

	public void setThreeTextStr(String threeTextStr) {
		ThreeTextStr = threeTextStr;
		if(pk_three_textStr!=null){
			pk_three_textStr.setText(threeTextStr);
		}
	}
	public String getOneTextStr2() {
		return OneTextStr;
	}
	
	public void setOneTextStr2(String oneTextStr) {
		OneTextStr = oneTextStr;
		if(pk_one_textStr2 != null){
			pk_one_textStr2.setText(oneTextStr);
		}
	}
	
	public String getTwoTextStr2() {
		return TwoTextStr;
	}
	
	public void setTwoTextStr2(String twoTextStr) {
		TwoTextStr = twoTextStr;
		if(pk_two_textStr2!=null){
			pk_two_textStr2.setText(twoTextStr);
		}
	}
	
	public String getThreeTextStr2() {
		return ThreeTextStr;
	}
	
	public void setThreeTextStr2(String threeTextStr) {
		ThreeTextStr = threeTextStr;
		if(pk_three_textStr2!=null){
			pk_three_textStr2.setText(threeTextStr);
		}
	}
	
	/**
	 * 设置背景色
	 * @param num
	 * @param resid
	 */
	public void setBackG(int num ,int resid){
		switch (num) {
		case 1:
//			if(pk_one_rl!=null && pk_one_textStr != null){
//				pk_one_rl.setBackgroundResource(resid);
//				pk_one_textStr.setText("");
//			}
			if(pk_one_textStr!=null){
				pk_one_textStr.setBackgroundResource(resid);
				pk_one_textStr.setText("");
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	public void selected() {
		select_pk_rl.setVisibility(View.VISIBLE);//与之相反
	}
	
	@Override
	public void unselected() {
		select_pk_rl.setVisibility(View.INVISIBLE);//不显示（没选中情况下）
	}

	@Override
	public String getText() {//此处不需要实现
		return null;
	}
	
}
