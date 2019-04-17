package com.mitenotc.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.ui.base.BaseActivity;
import com.mitenotc.ui.base.BottomFragment;
import com.mitenotc.utils.AppUtil;
/**
 * popwindow 工具类
 * @author mitenotc
 */
public class TCPop {

	public static PopupWindow pop;
	private static LinearLayout ll;
	private static int width;
	static{
		LinearLayout view = new LinearLayout(MyApp.context);
		ll = new LinearLayout(MyApp.context);
		view.addView(ll);
		ll.setBackgroundResource(R.drawable.pop_bg);
		ll.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2);
		ll.setLayoutParams(params);
		params.rightMargin = 2;
		pop = new PopupWindow(view, -2, -2);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setOutsideTouchable(true);
		pop.setFocusable(true);
	}
	/**
	 * 被 showPopMenu(Context context,int iconsArrayId, int textsArrayId, View anchor) 取代
	 * @param context
	 * @param iconIds
	 * @param texts
	 * @param anchor
	 */
	@Deprecated
	public static void showPopMenu(Context context,int [] iconIds, String [] texts, View anchor){
		LinearLayout ll = new LinearLayout(context);
		ll.setBackgroundResource(R.drawable.pop_bg);
		ll.setOrientation(LinearLayout.VERTICAL);
		for (int i = 0; i < iconIds.length; i++) {
			View view = View.inflate(context, R.layout.f_item_pop_menu, null);
			ll.addView(view);
			ImageView iv = (ImageView) view.findViewById(R.id.pop_menu_iv);
			iv.setImageResource(iconIds[i]);
			TextView tv = (TextView) view.findViewById(R.id.pop_menu_tv);
			tv.setText(texts[i]);
		}
		PopupWindow pop = new PopupWindow(ll, -2, -2);
		pop.showAsDropDown(anchor);
	}
	/**
	 * 使用 TypedArray 和 资源文件的方式 以数组的形式来组合 popwindow的 icon 和 text, 该函数 没有处理 点击事件,被 showPopMenu1取代
	 * @param context
	 * @param iconsArrayId 资源文件中的 array型的资源id
	 * @param textsArrayId  资源文件中的 array型的资源id
	 * @param anchor popwindon 要显示的 ancher view
	 */
	@Deprecated
	public static void showPopMenu(Context context,int iconsArrayId, int textsArrayId, View anchor){
		LinearLayout ll = new LinearLayout(context);
		ll.setBackgroundResource(R.drawable.pop_bg);
		ll.setOrientation(LinearLayout.VERTICAL);
		TypedArray icons = context.getResources().obtainTypedArray(iconsArrayId);
		TypedArray texts = context.getResources().obtainTypedArray(textsArrayId);
		
		for (int i = 0; i < icons.length(); i++) {
			View view = View.inflate(context, R.layout.f_item_pop_menu, null);
			ll.addView(view);
			ImageView iv = (ImageView) view.findViewById(R.id.pop_menu_iv);
			iv.setImageDrawable(icons.getDrawable(i));
			TextView tv = (TextView) view.findViewById(R.id.pop_menu_tv);
			tv.setText(texts.getString(i));
		}
		
		pop = new PopupWindow(ll, -2, -2);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setOutsideTouchable(true);
		pop.showAsDropDown(anchor,0,4);
	}
	/**
	 * 显示 popwindow, 同时在popwindow 上加上了点击事件
	 * @param context
	 * @param iconsArrayId
	 * @param textsArrayId
	 * @param funcsId
	 * @param anchor
	 */
	public static void showPopMenu1(final BaseActivity context,int iconsArrayId, int textsArrayId, int funcsId,View anchor){
		
		ll.removeAllViews();
		
		TypedArray icons = context.getResources().obtainTypedArray(iconsArrayId);
		TypedArray texts = context.getResources().obtainTypedArray(textsArrayId);
		final String[] funcs = context.getResources().getStringArray(funcsId);
		
		for (int i = 0; i < icons.length(); i++) {
			final View view = View.inflate(context, R.layout.f_item_pop_menu, null);
			ll.addView(view);
			ImageView iv = (ImageView) view.findViewById(R.id.pop_menu_iv);
			iv.setImageDrawable(icons.getDrawable(i));
			TextView tv = (TextView) view.findViewById(R.id.pop_menu_tv);
			final String func = funcs[i];
			tv.setText(texts.getString(i));
			if(i == icons.length()-1){
				view.findViewById(R.id.pop_menu_divider).setVisibility(View.GONE);
			}
			
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(func.startsWith("0")){
						Toast.makeText(context, "收录到我的定制", 0).show();
						if(!AppUtil.isEmpty(func.substring(1))){
//							BodyFragment1.addCustomCode(Integer.parseInt(func.substring(1)));
						}
					}else if(func.startsWith("1")){
//						Toast.makeText(context, "跳转到"+func.substring(1), 0).show();
						BottomFragment bottomFragment = (BottomFragment) context.getBottomFragment();
						pop.dismiss();
						bottomFragment.check(func.substring(1));
					}else if(func.startsWith("2")){
						Toast.makeText(context, "跳转到"+func.substring(1), 0).show();
					}
				}
			});
		}
		
		pop.showAsDropDown(anchor,0,0);
	}
	/**
	 * 传入 模块的 array Id值和 所在的Activity, 简化了 showPopMenu1 的参数
	 * @param context
	 * @param modeId
	 */
	public static void showPopMenu(BaseActivity context,int modeId){
		TypedArray mode = context.getResources().obtainTypedArray(modeId);
		int iconsArrayId = mode.getResourceId(0, 0);
		int textsArrayId = mode.getResourceId(1, 0);
		int funcsId = mode .getResourceId(2, 0);
		View anchor = context.findViewById(R.id.title_nav_iv_right);
		showPopMenu1(context, iconsArrayId, textsArrayId, funcsId, anchor);
	}
}
