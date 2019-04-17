package com.mitenotc.ui.account;

import java.io.File;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.time.DateFormatUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.mitenotc.tc.R;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.utils.AppUtil;

/**
 * 账户中心的工具类
 * 
 * @author ymx
 */

public class AccountUtils extends BaseFragment {

	/** Toast测试阶段为 true，上线之后改为false */
	public static final boolean toast = true;

	private static long lastClickTime = 0;

	/** 配置ListView */
	public static void configListView(ListView listView) {
		listView.setSelector(new ColorDrawable(0x00000000)); // 设置item的点击事件的背景
		listView.setCacheColorHint(0);
		listView.setVerticalScrollBarEnabled(false);// 不显示滚动条
		listView.setDrawingCacheEnabled(false);

	}

	/** 防止点击过快 出现白屏 */
	public static boolean isFastClick(Context mActivity) {

		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 1000) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

	/** 显示短时间的土司 */
	public static void showToast(Context mActivity, String sting) {
		if (toast)
			Toast.makeText(mActivity, sting, Toast.LENGTH_SHORT).show();
	}

	/** 设置DrawableRight的右朝向 */
	public static void showArrowDrawable(TextView view, int sourceId) {
		Drawable d1 = view.getResources().getDrawable(sourceId);
		d1.setBounds(0, 0, d1.getMinimumWidth(), d1.getMinimumHeight());
		view.setCompoundDrawables(null, null, d1, null);
	}

	/** 设置DrawableRight的左朝向 */
	public static void showArrowLeftDrawable(TextView view, int sourceId) {
		Drawable d1 = view.getResources().getDrawable(sourceId);
		d1.setBounds(0, 0, d1.getMinimumWidth(), d1.getMinimumHeight());
		view.setCompoundDrawables(d1, null, null, null);
	}

	/** 配置popwindow属性 */
	public static void configPopupWindow(View view, PopupWindow popup) {
		popup.setOutsideTouchable(true);
		popup.setBackgroundDrawable(new BitmapDrawable());
		popup.setFocusable(true);
		popup.showAsDropDown(view, 0, -1);
	}

	/** 金钱 "分" 转换成 "元" (保留两位小数) */
	public static String fenToyuan(String str) {
		
		if(str.endsWith("10")){
			String substr = str.substring(0, str.length()-2);
			return substr+".10";
		}

		String dst = "";
		long tmp = Long.parseLong(str);

		long rs = tmp / 100;
		if (rs == 0) {
			dst = "0";
		} else {
			dst = +rs + "";
		}

		rs = tmp % 100;
		if (rs != 0) {
			if (rs > 10) {
				dst += "." + rs;
			} else {
				dst += ".0" + rs;
			}
		}
		return dst;
	}

	/** 金钱 "元" 转换成 "分" */
	public static String yuanTofen(String str) {

		float dst = Float.parseFloat(str);
		return (int) (dst * 100 + 0.009f) + "";
	}

	/** 银行卡号的截取截取字符串后四位 */
	public static String interceptString(String number) {
		number = number.substring(0, number.length() - 9) + "****"
				+ number.substring(number.length() - 4, number.length());
		return number;
	}

	/** 手机号的替换 */
	public static String phoneNumber(String phoneNumber) {
		if (AppUtil.isEmpty(phoneNumber) || phoneNumber.getBytes().length < 8) {
			return phoneNumber;
		}
		phoneNumber = phoneNumber.replaceAll("(\\d{3})\\d{4}(\\d{4})",
				"$1****$2");
		return phoneNumber;
	}

	/** 身份证 后四位变成**** */
	public static String personCard(String personCard) {
		personCard = personCard.substring(0, personCard.length() - 4) + "****";
		return personCard;
	}

	/** 真实名字 **** */
	public static String TureName(String name) {
		name = "*" + name.substring(1, name.length());
		return name;
	}

	/** 是否得到root权限 **/

	public static boolean isRoot() {
		boolean bool = false;
		try {
			if ((!new File("/system/bin/su").exists())
					&& (!new File("/system/xbin/su").exists())) {
				bool = false;
			} else {
				bool = true;
			}
		} catch (Exception e) {

		}
		return bool;
	}

	/**
	 * 与下载有关 返回文件名字
	 * 
	 * @param path
	 * @return
	 */
	public static String getFileName(String path) {
		int start = path.lastIndexOf("/") + 1;
		return path.substring(start, path.length());
	}

	/**
	 * 是否是联通的手机号码
	 * 
	 * @param number
	 * @return
	 */
	public static boolean isUnicomNumber(String number) {
		String str = "^1(3[0-2]|5[56]|8[56])\\d{8}$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(number);
		return m.matches();
	}

	public static void boda(Context mActivity, String phoneNumber) {
		// 判断手机是否有sim卡
		TelephonyManager manager = (TelephonyManager) mActivity
				.getSystemService(Context.TELEPHONY_SERVICE);
		int simState = manager.getSimState();
		if (simState == 1) {
			Toast.makeText(mActivity, "您还没有插入sim卡！", Toast.LENGTH_SHORT).show();
			return;
		}

		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
				+ phoneNumber));
		mActivity.startActivity(intent);
	}

	/**
	 * 获取屏幕尺寸与密度.
	 * 
	 * @param context
	 *            the context
	 * @return mDisplayMetrics
	 */
	public static AbDisplayMetrics getDisplayMetrics(Context context) {
		AbDisplayMetrics mDisplayMetrics = new AbDisplayMetrics();
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display d = windowManager.getDefaultDisplay();
		mDisplayMetrics.displayWidth = d.getWidth();
		mDisplayMetrics.displayHeight = d.getHeight();
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		mDisplayMetrics.density = dm.density;
		mDisplayMetrics.widthPixels = dm.widthPixels;
		mDisplayMetrics.heightPixels = dm.heightPixels;
		mDisplayMetrics.scaledDensity = dm.scaledDensity;

		return mDisplayMetrics;
	}

	/**
	 * 描述：根据屏幕大小缩放.
	 * 
	 * @param screenWidth
	 * @param screenHeight
	 * @param size
	 * @return
	 */
	public static int resize(Context context, float pxValue) {
		AbDisplayMetrics mDisplayMetrics = AccountUtils
				.getDisplayMetrics(context);
		return resize(mDisplayMetrics.displayWidth,
				mDisplayMetrics.displayHeight, pxValue);
	}

	/**
	 * 描述：根据屏幕大小缩放.
	 * 
	 * @param screenWidth
	 * @param screenHeight
	 * @param size
	 * @return
	 */
	public static int resize(int displayWidth, int displayHeight, float pxValue) {
		float scale = 1;
		try {
			float scaleWidth = (float) displayWidth / 480;
			float scaleHeight = (float) displayHeight / 800;
			scale = Math.min(scaleWidth, scaleHeight);
		} catch (Exception e) {
		}
		return Math.round(pxValue * scale);
	}

	public static void setPadding(Context context, View view, int left,
			int top, int right, int bottom) {
		int paramLeft = resize(context, left);
		int paramTop = resize(context, top);
		int paramRight = resize(context, right);
		int paramBottom = resize(context, bottom);
		view.setPadding(paramLeft, paramTop, paramRight, paramBottom);
	}

	public static void setMargin(Context context, View view, int left, int top,
			int right, int bottom) {
		int paramLeft = resize(context, left);
		int paramTop = resize(context, top);
		int paramRight = resize(context, right);
		int paramBottom = resize(context, bottom);
		ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view
				.getLayoutParams();
		if (left != Integer.MAX_VALUE && left != Integer.MIN_VALUE) {
			params.leftMargin = paramLeft;
		}
		if (right != Integer.MAX_VALUE && left != Integer.MIN_VALUE) {
			params.rightMargin = paramRight;
		}
		if (top != Integer.MAX_VALUE && left != Integer.MIN_VALUE) {
			params.topMargin = paramTop;
		}
		if (bottom != Integer.MAX_VALUE && left != Integer.MIN_VALUE) {
			params.bottomMargin = paramBottom;
		}
		view.setLayoutParams(params);
	}

	/**
	 * 得到本地或者网络上的bitmap url - 网络或者本地图片的绝对路径,比如:
	 * 
	 * A.网络路径: url="http://blog.foreverlove.us/girl2.png" ;
	 * 
	 * B.本地路径:url="file://mnt/sdcard/photo/image.png";
	 * 
	 * C.支持的图片格式 ,png, jpg,bmp,gif等等
	 * 
	 * @param url
	 * @return
	 */
	
	public static String getStringDateShort() {
//		  Date currentTime = new Date();
//		  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//		  String dateString = formatter.format(currentTime);
//		  return dateString;
		return DateFormatUtils.format(new Date(), "yyyy-MM-dd");
	}
	
	public static void changedEdittext(Button v,CharSequence s,int count){
		
		if(s.length() == 0){
			v.setBackgroundResource(R.drawable.shape_acc_graynull_btn);
			v.setTextColor(Color.parseColor("#CCCCCC"));
		}
		
		if(count>0){
			v.setBackgroundResource(R.drawable.btn_bg_red_selector);
			v.setTextColor(Color.parseColor("#FFFFFF"));
		}
	}
	
	public static boolean iswificonnect(Context mActivity) {
		ConnectivityManager connMgr = (ConnectivityManager)mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		
		return networkInfo.isConnected();
	}

	 public static boolean netConnectivityManager(Context ctx) {
		 ConnectivityManager connManager = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
	  // 获取代表联网状态的NetWorkInfo对象
	  NetworkInfo wifiNet = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	  NetworkInfo gNet = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	  return  wifiNet.isConnected() || gNet.isConnected();
	 }

}
