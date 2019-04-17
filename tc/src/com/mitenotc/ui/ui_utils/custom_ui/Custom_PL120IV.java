package com.mitenotc.ui.ui_utils.custom_ui;

import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.ui.ui_utils.MGridView.CustomItem;

import android.content.Context;
import android.text.Spannable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Custom_PL120IV extends LinearLayout implements CustomItem {
    private TextView tv_number;// 默认 选号    
    private TextView tv_dqyl;// 默认 gone   -->当前
    private TextView tv_scyl;// 默认 gone   -->上次
    private TextView tv_tzjz;// 默认 投资价值  -->最大
    private TextView tv_bonustext;
    private LinearLayout rl_number;
    
    private int sd_id;//selectedDrawable_id 选中背景
    private int usd_id;//unselectedDrawable_id 未选中背景
    
//    private Spannable textSp;
    private String textSp;
    private String bonustext="";
    
	public Custom_PL120IV(Context context) {
		super(context);
		init(context);
	}

	public Custom_PL120IV(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		View mView=View.inflate(context, R.layout.custom_pl120iv, this);
		rl_number=(LinearLayout) mView. findViewById(R.id.rl_number);
		tv_number=(TextView) mView.findViewById(R.id.tv_number);
		
		tv_dqyl=(TextView) mView.findViewById(R.id.tv_dqyl);
		tv_scyl=(TextView) mView.findViewById(R.id.tv_scyl);
		tv_tzjz=(TextView) mView.findViewById(R.id.tv_tzjz);
		
		tv_bonustext=(TextView) mView. findViewById(R.id.tv_bonustext);
	}
       /**
        *  初始化界面  默认选号界面(号)
        * @param sd_id
        * @param usd_id
        * @param textSp
        * @param bonustext
        * @param tzjztext
        */
	public void initView(int sd_id,int usd_id,String textSp,String bonustext,String tzjztext){
		this.sd_id=sd_id;
		this.usd_id=usd_id;
		this.textSp=textSp;
		this.bonustext=bonustext;
		if(rl_number!=null){
			rl_number.setBackgroundDrawable(MyApp.res.getDrawable(usd_id));
		}
		if(tv_number != null){
			tv_number.setText(textSp);
		}
		if(tv_tzjz != null){
			tv_tzjz.setText(tzjztext);
		}
		if(tv_bonustext != null){
			tv_bonustext.setText(bonustext);
		}
	}
	public void initView(boolean isShowbonus,int sd_id,int usd_id,String textSp,String bonustext,String tzjztext){
		this.sd_id=sd_id;
		this.usd_id=usd_id;
		this.textSp=textSp;
		this.bonustext=bonustext;
		if(rl_number!=null){
			rl_number.setBackgroundDrawable(MyApp.res.getDrawable(usd_id));
		}
		if(tv_number != null){
			tv_number.setText(textSp);
		}
		if(tv_tzjz != null){
			if(tzjztext==null){
				tv_tzjz.setVisibility(View.GONE);
			}else{
				tv_tzjz.setVisibility(View.VISIBLE);
				tv_tzjz.setText(tzjztext);
			}
		}
		if(tv_bonustext != null){
			if(isShowbonus){
				if(bonustext ==null){
					tv_bonustext.setVisibility(View.GONE);
				}else{
					tv_bonustext.setText(bonustext);
					tv_bonustext.setVisibility(View.VISIBLE);
				}
			}else{
				tv_bonustext.setVisibility(View.GONE);
				if(tv_scyl!=null){
					tv_scyl.setVisibility(View.GONE);
				}
			}
		}
		
	}
	/**
	 *  遗漏切换界面(漏)
	 * @param dqStr
	 * @param scStr
	 * @param zdStr
	 */
	public void initYLView(int sd_id,int usd_id,String dqStr,String scStr,String zdStr){
		this.sd_id=sd_id;
		this.usd_id=usd_id;
		if(tv_number!=null){
			tv_number.setVisibility(View.GONE);
		}
		if(tv_bonustext!=null){
			tv_bonustext.setVisibility(View.GONE);
		}
		if(tv_dqyl!=null){
			tv_dqyl.setVisibility(View.VISIBLE);
			tv_dqyl.setText(dqStr);//  当前遗漏
		}
		if(tv_scyl!=null){
			tv_scyl.setVisibility(View.VISIBLE);
			tv_scyl.setText(scStr);//  上次遗漏
		}
		if(tv_tzjz!=null){
			tv_tzjz.setVisibility(View.VISIBLE);
			tv_tzjz.setText(zdStr);//  投资价值 --> 最大遗漏遗漏
		}
	}
	
	
	@Override
	public void selected() {
		rl_number.setBackgroundDrawable(MyApp.res.getDrawable(sd_id));
		tv_number.setTextColor(MyApp.res.getColor(R.color.zwl_register_white_color));
		tv_tzjz.setTextColor(MyApp.res.getColor(R.color.zwl_register_white_color));
		tv_scyl.setTextColor(MyApp.res.getColor(R.color.zwl_register_white_color));
		tv_dqyl.setTextColor(MyApp.res.getColor(R.color.zwl_register_white_color));
	}
	@Override
	public void unselected() {
		rl_number.setBackgroundDrawable(MyApp.res.getDrawable(usd_id));
		tv_number.setTextColor(MyApp.res.getColor(R.color.fast3_text_color2));
		tv_tzjz.setTextColor(MyApp.res.getColor(R.color.fast3_text_color2));
		tv_scyl.setTextColor(MyApp.res.getColor(R.color.fast3_text_color2));
		tv_dqyl.setTextColor(MyApp.res.getColor(R.color.fast3_text_color2));
	}

	@Override
	public String getText() {//此处不需要实现
		// TODO Auto-generated method stub
		return null;
	}

}
