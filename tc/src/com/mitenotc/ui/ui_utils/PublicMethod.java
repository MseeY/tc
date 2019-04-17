package com.mitenotc.ui.ui_utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.Inflater;

import android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TextView.BufferType;

/**
 * 共用方法类
 */

public class PublicMethod {
	public final static int MAXICON = 8;
	public final static int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
	public final static int FP = ViewGroup.LayoutParams.FILL_PARENT;

	/**
	 * 从数组sourceInt中取出num个组合：规定前num-1为，最后一位去子组合的分组和，然后将固定值和子组合合并 如：{1,2,3}取2位组合：
	 * 第1步：固定第1为1，取子组合{2，3}的2-1位
	 * 第2步：2-1=1位，无需固定为，则划分子组合为一位子组合{{2}{3}}；否则继续固定第n为，去子组合m-n为；
	 * 第3步：将一位子组合于前一位固定值合并，为{2,1}{3,1}；若还有固定值，将合并组合和前固定值组合，至组合完毕
	 * 
	 * @param sourceInt
	 *            源数组
	 * @param num
	 *            子组合大小
	 * @return 子组合数据
	 */

	/**
	 * 求a取b的组合数
	 */
	private static int zuHe(int a, int b) {
		int up = 1;
		for (int up_i = 0; up_i < b; up_i++) {
			up = up * a;
			a--;
		}

		int down = jieCheng(b);

		return up / down;
	}

	/**
	 * 求b的阶乘
	 */
	private static int jieCheng(int b) {
		int result = 0;

		if (b == 1 || b == 0) {
			result = b;
		} else {
			result = b * jieCheng(b - 1);
		}

		return result;
	}

	/**
	 * 截取数组的子数组，从start位至末尾
	 * 
	 * @param sourceInt
	 *            源数组
	 * @param start
	 *            起始位
	 * @return
	 */
	private static int[] subIntByIndex(int[] sourceInt, int start) {
		int[] resultInt = new int[sourceInt.length - start - 1];

		for (int i = start + 1, j = 0; i < sourceInt.length; i++, j++) {
			resultInt[j] = sourceInt[i];
		}

		return resultInt;
	}

	/**
	 * 将sourceInt数组，添加到destinationInt数组中，从第一位开始填充
	 * 
	 * @param resultInt
	 *            结果数组
	 * @param sourceInt
	 *            源数组
	 * @return 结果数组
	 */
	private static int[] addInt(int[] resultInt, int[] sourceInt) {
		for (int source_i = 0; source_i < sourceInt.length; source_i++) {
			resultInt[source_i] = sourceInt[source_i];
		}

		return resultInt;
	}

	/**
	 * 获取数组的大小
	 * 
	 * @param resultInts
	 *            数组对象
	 * @return 数组大小
	 */
	private static int getIntsSize(int[][] resultInts) {
		int size = 0;

		for (int i = 0; i < resultInts.length; i++) {
			if (resultInts[i] != null) {
				size++;
			}
		}
		return size;
	}

	/**
	 * 获取 单个随机数
	 */
	static Random random = new Random();

	public static int getRandomByRange(int aFrom, int aTo) {
		return (random.nextInt() >>> 1) % (aTo - aFrom + 1) + aFrom;
	}

	/**
	 * 检查数组碰撞
	 * 
	 * @param aNums
	 * @param aTo
	 * @param aCheckNum
	 * @return
	 */
	public static boolean checkCollision(int[] aNums, int aTo, int aCheckNum) {
		boolean returnValue = false;
		for (int i = 0; i < aTo; i++) {
			if (aNums[i] == aCheckNum) {
				returnValue = true;
			}
		}
		return returnValue;
	}

	/**
	 * 获取 多个随机数
	 * 
	 * @param aNum
	 * @param aFrom
	 * @param aTo
	 * @return
	 */
	public static int[] getRandomsWithoutCollision(int aNum, int aFrom, int aTo) {
		int[] iReturnNums = new int[aNum];
		for (int i = 0; i < aNum; i++) {
			int iCurrentNum = getRandomByRange(aFrom, aTo);
			while (checkCollision(iReturnNums, i, iCurrentNum)) {
				iCurrentNum = getRandomByRange(aFrom, aTo);
			}
			iReturnNums[i] = iCurrentNum;
		}
		return iReturnNums;
	}

	/**
	 * 获取当前页面的屏幕高度
	 * 
	 * @param cx
	 * @return
	 */
	public static int getDisplayHeight(Context cx) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = cx.getApplicationContext().getResources().getDisplayMetrics();
		int screenHeight = dm.heightPixels;
		return screenHeight;
	}

	/**
	 * 获取当前页面的屏幕宽度
	 * 
	 * @param cx
	 * @return
	 */
	public static int getDisplayWidth(Context cx) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = cx.getApplicationContext().getResources().getDisplayMetrics();
		int screenWidth = dm.widthPixels;

		return screenWidth;
	}

	/**
	 * 修改余额格式
	 * 
	 * @param str
	 * @return
	 */
	public static String changeMoney(String str) {
		if (str.length() > 2) {
			if (str.substring(str.length() - 2, str.length()).equals("00")) {
				str = str.substring(0, str.length() - 2);
			} else {
				str = str.substring(0, str.length() - 2) + "."
						+ str.substring(str.length() - 2, str.length());
			}
		} else if (str.length() == 2) {
			str = "0" + "." + str;
		} else if (str.length() == 1) {
			str = "0.0" + str;
		}
		return str;
	}

	/**
	 * 发短信
	 * 
	 * @param phoneNumber
	 * @param message
	 * @return
	 */
	public static boolean sendSMS(String phoneNumber, String message) {
		try {
			SmsManager sms = SmsManager.getDefault();
			List<String> iContents = sms.divideMessage(message);
			for (int i = 0; i < iContents.size(); i++)
				sms.sendTextMessage(phoneNumber, null, iContents.get(i), null,
						null);

		} catch (IllegalArgumentException e) {
			return false;
		}
		return true;
	}

	/**
	 * 输出信息
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void myOutLog(String tag, String msg) {
		// Log.e(tag, msg);
	}

	/**
	 * 设置listView的间距
	 * 
	 * @param listview
	 */
	public static void setmydividerHeight(ListView listview) {
		listview.setDivider(new ColorDrawable(Color.GRAY));
		listview.setDividerHeight(1);

	}

	/**
	 * 获得当前期和截止时间
	 * 
	 * @param string
	 * @param context
	 * @return
	 */
	public static String[] getLotno(String string, Context context) {

		return null;

	}

	/**
	 * 排序
	 * 
	 * @param nums
	 * @param str
	 * @return
	 */
	public static int[] orderby(int[] nums, String str) {
		// 从大到小排
		if (str.equalsIgnoreCase("cba")) {
			for (int i = 0; i < nums.length; i++) {
				for (int j = i + 1; j < nums.length; j++) {
					if (nums[i] < nums[j]) {
						int tem = nums[i];
						nums[i] = nums[j];
						nums[j] = tem;
					}
				}
			}
		}
		// 从小到大排
		else if (str.equalsIgnoreCase("abc")) {
			for (int i = 0; i < nums.length; i++) {
				for (int j = i + 1; j < nums.length; j++) {
					if (nums[i] > nums[j]) {
						int tem = nums[i];
						nums[i] = nums[j];
						nums[j] = tem;
					}
				}
			}
		}
		return nums;
	}

	/**
	 * 将注码1转换成字符01的方法
	 * 
	 * @param num
	 * @return
	 */
	public static String getZhuMa(int num) {
		String str = "";

		if (num < 10) {
			str = "0" + num;
		} else {
			str = "" + num;
		}
		return str;

	}

	/**
	 * 数组排序
	 * 
	 * @param t
	 * @return
	 */
	public static int[] sort(int t[]) {
		int t_s[] = t;
		int temp;
		for (int i = 0; i < t_s.length; i++) {
			for (int j = i + 1; j < t_s.length; j++) {
				if (t_s[i] > t_s[j]) {
					temp = t_s[i];
					t_s[i] = t_s[j];
					t_s[j] = temp;
				}
			}
		}
		return t_s;
	}

	/**
	 * 机选提示框1 用来提醒选球规则
	 * 
	 * @param string
	 *            显示框信息
	 * @return
	 */

	public static void alertJiXuan(String string, Context context) {
		Builder dialog = new AlertDialog.Builder(context).setTitle("请选择号码")
				.setMessage(string)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}

				});
		dialog.show();

	}


	/**
	 * 得到小球的显示的数值
	 * 
	 * @param isTen
	 * @param aBallViewText
	 * @param iBallViewNo
	 * @return
	 */
	private static String getBallStr(boolean isTen, int aBallViewText,
			int iBallViewNo) {
		String iStrTemp = "";
		if (aBallViewText == 0) {
			iStrTemp = "" + (iBallViewNo);// 小球从0开始
		} else if (aBallViewText == 1) {
			iStrTemp = "" + (iBallViewNo + 1);// 小球从1开始
		} else if (aBallViewText == 3) {
			iStrTemp = "" + (iBallViewNo + 3);// 小球从3开始
		}
		if (isTen) {
			iStrTemp = isTen(iStrTemp);
		}
		return iStrTemp;
	}

	/**
	 * 得到大小单双的显示
	 * 
	 * @param isTen
	 * @param aBallViewText
	 * @param iBallViewNo
	 * @return
	 */
	private static String getDXBallStr(int iBallViewNo) {
		String iStrTemp = "";
		if (iBallViewNo == 0) {
			iStrTemp = "大";
		} else if (iBallViewNo == 1) {
			iStrTemp = "小";
		} else if (iBallViewNo == 2) {
			iStrTemp = "单";
		} else if (iBallViewNo == 3) {
			iStrTemp = "双";
		}
		return iStrTemp;
	}

	/**
	 * 计算选区高度
	 * 
	 * @param aFieldWidth屏幕宽度
	 * @param aBallNum选区总球数
	 * @param viewNumPerLine每行个数
	 * @return 选区高度
	 */
	public static int areaHeight(int aFieldWidth, int iBallNum,
			int viewNumPerLine, boolean isMiss) {
		// 定义没行小球的个数为7
		int iFieldWidth = aFieldWidth;
		int scrollBarWidth = 6;
		int iBallViewWidth = (iFieldWidth - scrollBarWidth) / viewNumPerLine
				- 2;
		int lineNum = iBallNum / viewNumPerLine;
		int lastLineViewNum = iBallNum % viewNumPerLine;
		if (lastLineViewNum > 0) {
			lineNum++;
		}
		if (isMiss) {
			lineNum *= 2;
		}
		return (lineNum + 1) * iBallViewWidth + 10;// +1代表标题
	}

	/**
	 * 遗漏值赋值
	 */
	public static void setMissText(List<TextView> textList,
			List<String> missValues) {
		int[] rankInt = null;
		if (missValues != null) {
			rankInt = rankList(missValues);
		}
		for (int i = 0; i < textList.size(); i++) {
			String missValue = missValues.get(i);
			textList.get(i).setText(missValue);
			if (rankInt[0] == Integer.parseInt(missValue)
					|| rankInt[1] == Integer.parseInt(missValue)) {
				textList.get(i).setTextColor(Color.RED);
			}
		}
	}

	/**
	 * 冒泡排序
	 * 
	 * @param myArray
	 * @return
	 */
	public static int[] rankList(List<String> myArray) {
		int[] rankInt = new int[myArray.size()];
		for (int n = 0; n < myArray.size(); n++) {
			rankInt[n] = Integer.parseInt(myArray.get(n));
		}
		// 取长度最长的词组 -- 冒泡法
		for (int j = 1; j < rankInt.length; j++) {
			for (int i = 0; i < rankInt.length - 1; i++) {
				// 如果 myArray[i] > myArray[i+1] ，则 myArray[i] 上浮一位
				if (rankInt[i] < rankInt[i + 1]) {
					int temp = rankInt[i];
					rankInt[i] = rankInt[i + 1];
					rankInt[i + 1] = temp;
				}
			}
		}
		return rankInt;
	}

	/**
	 * 将数组转换成注码串
	 * 
	 * @param balls
	 * @return
	 */
	public static String getStrZhuMa(int balls[]) {
		String str = "";
		for (int i = 0; i < balls.length; i++) {
			str += isTen(balls[i]);
			if (i != (balls.length - 1))
				str += ",";
		}
		return str;

	}

//	public static String toLotnohemai(String type) {}
//
//	public static boolean isJC(String type) {}
//	public static boolean isJCZ(String type) {}
//	public static boolean isJCL(String type) {}
//
//	public static Class<?> toClass(String type) {}
//
//	/**
//	 * 根据彩种名称获取玩法界面
//	 * 
//	 * @param name
//	 * @return Class<?>
//	 */
//	public static Class<?> getClass(Activity activity, String name) {}
//
//	public static int toIconView(String type) {}

	/**
	 * 转换成分
	 * 
	 */
	public static double toFen(double amt) {

		return amt * 100;

	}
	/**
	 * 转换成分
	 * 
	 */
	public static int toIntFen(int amt) {

		return amt * 100;

	}

	/**
	 * 带小数的金额转换成分
	 * 
	 */
	public static String doubleToFen(String amt) {
		BigDecimal amt3 = new BigDecimal(amt);
		BigDecimal shu = new BigDecimal("100");
		// double amt2 = Double.parseDouble(amt) * 100;
		return amt3.multiply(shu).toString();
		// return String.valueOf(Integer.parseInt(amt)*100);
	}

	/**
	 * 带小数的金额转换成分
	 * 
	 */
	public static String intToFen(String amt) {
		BigDecimal amt3 = new BigDecimal(amt);
		BigDecimal shu = new BigDecimal("100");
		// double amt2 = Double.parseDouble(amt) * 100;
		return amt3.multiply(shu).toBigInteger().toString();
		// return String.valueOf(Integer.parseInt(amt)*100);

	}

	/**
	 * 转换成元
	 * 
	 */
	public static String toIntYuan(String amt) {
		String money = "";
		try {
			money = Long.toString(Long.parseLong(amt) / 100);
		} catch (Exception e) {

		}
		return money;

	}

	/**
	 * 转换成百分比
	 * 
	 * @param allAmt
	 * @param buyAmt
	 * @return
	 */
	public static String getProgress(double allAmt, double buyAmt) {
		if (buyAmt != 0 && allAmt != 0) {
			double progress = buyAmt / allAmt * 100;
			int progressInt = (int) (progress + 0.5);
			return progressInt + "%";
		} else {
			return "0%";
		}

	}

	/**
	 * 转换成元
	 * 
	 */
	public static String toYuan(String amt) {
		double target = Double.parseDouble(amt) / 100;
		String result = formatStringToTwoPoint(target);
		return result;

	}

	public static String formatStringToTwoPoint(double num) {
		DecimalFormat df1 = new DecimalFormat("###0.00");
		String result = df1.format(num);
		return result;
	}

	/**
	 * 将1转换成01
	 * 
	 * @param time
	 * @return
	 */
	public static String isTen(int time) {
		String timeStr = "";
		if (time < 10) {
			timeStr += "0" + time;
		} else {
			timeStr += time;
		}
		return timeStr;
	}

	/**
	 * 将1转换成01
	 * 
	 * @param time
	 * @return
	 */
	public static String isTen(String time) {
		String timeStr = "";
		if (!time.equals("") && time != null) {
			int num = Integer.parseInt(time);
			if (num < 10) {
				timeStr += "0" + time;
			} else {
				timeStr += time;
			}
		}
		return timeStr;
	}

	public static int toInt(String string) {
		int num = 0;
		if (string != null) {
			if (!string.equals("")) {
				num = Integer.parseInt(string);
			}
		}
		return num;
	}

	/**
	 * 把金额格式化成"*元"
	 * 
	 * @param money
	 * @return
	 */
	public static String formatMoney(String money) {
		StringBuffer formatMoney = new StringBuffer();
		formatMoney.append(PublicMethod.toYuan(money));
		formatMoney.append("元");
		return formatMoney.toString();
	}

	/**
	 * 判断是否是模拟器
	 * 
	 * @return
	 */
	public static boolean isEmulator() {
		Runtime runtime = Runtime.getRuntime();
		Process process = null;
		try {
			process = runtime.exec("/system/bin/cat /proc/cpuinfo");
			process.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}

		InputStream in = process.getInputStream();
		BufferedReader boy = new BufferedReader(new InputStreamReader(in));
		String mystring = null;
		try {
			mystring = boy.readLine();
			while (mystring != null) {
				mystring = mystring.trim().toLowerCase();
				if ((mystring.startsWith("hardware"))
						&& mystring.endsWith("goldfish")) {
					return true;
				}
				mystring = boy.readLine();
			}

		} catch (IOException e) {
			return false;
		}

		return false;
	}

	public static String getzhumainfo(String lotno, int beishu, String bet_code) {
		String zhuma = "";
		String beishuzhuma = "";
		if (beishu < 10) {
			beishuzhuma = "0" + beishu;
		} else {
			beishuzhuma = beishu + "";
		}
		if (lotno.equals("F47104")) {
			zhuma = "00" + beishuzhuma + bet_code;
		} else if (lotno.equals("F47102")) {
			zhuma = "00" + beishuzhuma + bet_code;
		} else if (lotno.equals("F47103")) {
			zhuma = "20" + beishuzhuma + bet_code;
		} else {
			zhuma = bet_code;
		}
		return zhuma;
	}

	/**
	 * gzip
	 * 
	 * @param data
	 * @return byte[]
	 */
	public static byte[] decompress2(byte[] data) {
		byte[] output = new byte[0];

		Inflater decompresser = new Inflater();
		decompresser.reset();
		decompresser.setInput(data);

		ByteArrayOutputStream o = new ByteArrayOutputStream(data.length);
		try {
			byte[] buf = new byte[1024];
			while (!decompresser.finished()) {
				int i = decompresser.inflate(buf);
				o.write(buf, 0, i);
			}
			output = o.toByteArray();
		} catch (Exception e) {
			output = data;
			e.printStackTrace();
		} finally {
			try {
				o.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		decompresser.end();
		return output;
	}

	// 检查是否11位电话号码
	public static boolean isphonenum(String phonenum) {
		Pattern p = Pattern.compile("^\\d{11}");
		Matcher m = p.matcher(phonenum);
		return m.matches();
	}

	// 检查是否数字
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * 返回总注数竞彩多串过关投注计算注数
	 * 
	 * @param teamNum
	 *            多串过关3*3 teamNum = 3
	 * @param select
	 *            2*1 select=2
	 * @return 将几场比赛分成几组
	 */
	public static int getDouZhushu(int teamNum, List<String> betcodes,
			int select, List<Boolean> isDanList, int isDanNum) {
		// 初始化原始数据
		int[] a = new int[betcodes.size()];
		for (int i = 0; i < a.length; i++) {
			a[i] = i;
		}
		// 接收数据
		int[] b = new int[teamNum];

		List<int[]> list = new ArrayList<int[]>();

		// 进行组合
		combine(a, a.length, teamNum, b, teamNum, list);
		int resultInt = 0;
		for (int[] result : list) {
			List<String> betcode = new ArrayList<String>();
			int danNum = 0;
			for (int p : result) {
				betcode.add(betcodes.get(p));
				if (isDanNum > 0 && isDanList.get(p)) {
					danNum++;
				}
			}
			if (isDanNum == 0 || danNum == isDanNum) {
				resultInt += getAllAmt(betcode, select, isDanList, 0);
			}
		}

		return resultInt;
	}

	/**
	 * 返回总注数竞彩自由过关投注计算注数
	 * 
	 * @param betcodes
	 * @param select
	 * @return
	 */
	public static int getAllAmt(List<String> betcodes, int select,
			List<Boolean> isDanList, int isDanNum) {
		// 初始化原始数据
		int[] a = new int[betcodes.size()];
		for (int i = 0; i < a.length; i++) {
			a[i] = i;
		}
		// 接收数据
		int[] b = new int[select];

		List<int[]> list = new ArrayList<int[]>();

		// 进行组合
		combine(a, a.length, select, b, select, list);

		// 返回数据对象
		int resultInt = 0;
		for (int[] result : list) {
			int itemNum = 1;
			int danNum = 0;
			for (int p : result) {
				itemNum *= Integer.parseInt(betcodes.get(p));
				if (isDanNum > 0 && isDanList.get(p)) {
					danNum++;
				}
			}
			if (isDanNum == 0 || danNum == isDanNum) {
				resultInt += itemNum;
			}
		}

		return resultInt;
	}

	public static int getDanAAmt(List<String> betcodes) {
		int zhushu = 0;
		for (int i = 0; i < betcodes.size(); i++) {
			zhushu += Integer.valueOf(betcodes.get(i));
		}

		return zhushu;
	}

	/**
	 * 组合的递归算法
	 * 
	 * @param a
	 *            原始数据
	 * @param n
	 *            原始数据个数
	 * @param m
	 *            选择数据个数
	 * @param b
	 *            存放被选择的数据
	 * @param M
	 *            常量，选择数据个数
	 * @param list
	 *            存放计算结果
	 */
	public static void combine(int a[], int n, int m, int b[], final int M,
			List<int[]> list) {
		for (int i = n; i >= m; i--) {
			b[m - 1] = i - 1;
			if (m > 1)
				combine(a, i - 1, m - 1, b, M, list);
			else {
				int[] result = new int[M];
				for (int j = M - 1; j >= 0; j--) {
					result[j] = a[b[j]];
				}
				list.add(result);
			}
		}
	}

	/**
	 * 提现时金额输入框保留两位小数
	 */
	public static TextWatcher twoDigitsDecimal = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable edt) {
			// TODO Auto-generated method stub
			String temp = edt.toString();
			int posDot = temp.indexOf(".");
			if (posDot <= 0)
				return;
			if (temp.length() - posDot - 1 > 2) {
				edt.delete(posDot + 3, posDot + 4);
			}
		}
	};

	// /**
	// * 将dip转换成px
	// *
	// * @param dip
	// * @return
	// */
	// public static int getPxInt2(Context context,float dip) {
	// return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
	// dip, context.getResources().getDisplayMetrics());
	// }
	/**
	 * 将px转换成dip
	 * 
	 * @param dip
	 * @return
	 */
	public static int getPxInt(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 将字符串中的“\n”转化为“<br>
	 * ”用于字符串的Html的换行操作
	 * 
	 * @param str
	 * @return
	 */
	public static String repleaceNtoBR(String str) {
		String brString = "";
		brString = str.replaceAll("\n", "<br>");
		return brString;
	}

	/**
	 * 打开网址
	 * 
	 * @param cx
	 *            本地的context
	 * @param a
	 *            网址字符串
	 */
	public static void openUrlByString(Context cx, String a) {
		Uri myUri = Uri.parse(a);
		Intent returnIt = new Intent(Intent.ACTION_VIEW, myUri);
		cx.startActivity(returnIt);
	}

	/**
	 * 得到一个没有0.0的都变了数组
	 * 
	 * @param array
	 * @return
	 */
	public static double[] getDoubleArrayNoZero(double[] array) {
		Arrays.sort(array);
		double firstNzero = 0.0;
		for (double aa : array) {
			if (aa != 0.0) {
				firstNzero = aa;
				break;
			}
		}

		int index = Arrays.binarySearch(array, firstNzero);
		double[] result = new double[array.length - index];
		for (int i = 0; i < array.length - index; i++) {
			result[i] = array[index + i];
		}
		return result;
	}

	public static void setTextColor(TextView text, String content, int start,
			int end, int color) {
		SpannableStringBuilder builder = new SpannableStringBuilder();
		builder.append(content);
		builder.setSpan(new ForegroundColorSpan(color), start, end,
				Spanned.SPAN_COMPOSING);
		text.setText(builder, BufferType.EDITABLE);
	}

	public static void setEditOnclick(final EditText mTextBeishu,
			final int minInt, final int maxInt) {
		mTextBeishu.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable edit) {
				String text = edit.toString();
				int mTextNum = 1;
				if (text != null && !text.equals("")) {
					mTextNum = Integer.parseInt(text);
					if (mTextNum < minInt) {
						setValueThread(mTextBeishu, minInt);
					} else if (mTextNum > maxInt) {
						mTextBeishu.setText("" + maxInt);
					}
				} else {
					setValueThread(mTextBeishu, minInt);
				}
				mTextBeishu.setSelection(mTextBeishu.length());
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});
	}

	public static void setValueThread(final EditText mTextBeishu,
			final int minInt) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mTextBeishu.post(new Runnable() {
					public void run() {
						String text = mTextBeishu.getText().toString();
						if (text.equals("")) {
							mTextBeishu.setText("" + minInt);
						} else if (Integer.parseInt(text) < minInt) {
							mTextBeishu.setText("" + minInt);
						}
					}
				});
			}
		}).start();
	}

	/**
	 * 格式化开奖号码
	 */
	public static String formatNum(String iNumbers, int num) {
		String iShowNumber = "";
		int length = iNumbers.length() / num;
		for (int i = 0; i < length; i++) {
			iShowNumber += iNumbers.substring(i * num, i * num + num);
			if (i != length - 1) {
				iShowNumber += ",";
			}
		}
		return iShowNumber;

	}

	/**
	 * 格式化时时彩开奖号码
	 * 
	 * @param iNumbers
	 *            开奖号码字符串
	 * @param num
	 *            号码位数
	 * @return 格式化后号码
	 */
	public static String formatSSCNum(String iNumbers, int num) {
		String iShowNumber = "";
		String singleOrSmall = "";
		int length = iNumbers.length() / num;
		for (int i = 0; i < length; i++) {
			String number = iNumbers.substring(i * num, i * num + num);
			iShowNumber += number;
			if (i == length - 1 || i == length - 2) {
				singleOrSmall += judgeBigSmallOrSigleDouble(Integer
						.valueOf(number));
				if (i == length - 2) {
					singleOrSmall += ",";
				}
			}
			if (i != length - 1) {
				iShowNumber += ",";
			}
		}
		return iShowNumber + "  " + singleOrSmall;
	}

	/**
	 * 判断该数字的大小单双
	 * 
	 * @param num
	 *            被判断的数组
	 * @return 大小单双字符串结果
	 */
	private static String judgeBigSmallOrSigleDouble(Integer num) {
		String result = "";

		if (num >= 5) {
			result += "大";
		} else {
			result += "小";
		}

		if (num % 2 == 0) {
			result += "双";
		} else {
			result += "单";
		}

		return result;
	}

	public static String getNewString(int length, String str) {
		String returnStr = "";
		if (str.length() > length) {
			returnStr = str.substring(0, length) + "***";
		} else {
			returnStr = str;
		}
		return returnStr;

	}

	public static void setTextColor(TextView text, int startInt, int endInt,
			String textStr, int color) {
		SpannableStringBuilder builder = new SpannableStringBuilder();
		builder.append(textStr);
		builder.setSpan(new ForegroundColorSpan(color), startInt, endInt,
				Spanned.SPAN_COMPOSING);
		text.setText(builder, BufferType.EDITABLE);
	}

	public static int toYuan(int fen) {
		if (fen != 0) {
			return fen / 100;
		} else {
			return 0;
		}
	}

	public static double doubleToYuan(double fen) {
		if (fen != 0) {
			return fen / 100;
		} else {
			return 0;
		}
	}

	public static String getNewTime() {
		final Calendar c = Calendar.getInstance();
		return "" + c.get(Calendar.YEAR) + c.get(Calendar.MONTH)
				+ c.get(Calendar.DAY_OF_MONTH);// 获取当前月份的日期号码
	}

	public static void setTextTTF(Context context, TextView text, String ttfName) {
		Typeface fontFace = Typeface.createFromAsset(context.getAssets(),
				"fonts/" + ttfName + ".ttf");
		text.setTypeface(fontFace);
	}

	public static Typeface getTypeface(Context context, String ttfName) {
		Typeface fontFace = Typeface.createFromAsset(context.getAssets(),
				"fonts/" + ttfName + ".ttf");
		return fontFace;
	}

	/**
	 * 实现震动的方法
	 * 
	 * @作者：
	 * @日期：
	 * @参数：
	 * @返回值：
	 * @修改人：
	 * @修改内容：
	 * @修改日期：
	 * @版本：
	 */
	public static void onVibrator(Context context, long num) {
		// 读取是否震动参数
		Vibrator vibrator = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
		if (vibrator == null) {
			Vibrator localVibrator = (Vibrator) context.getApplicationContext()
					.getSystemService("vibrator");
			vibrator = localVibrator;
		}
		vibrator.vibrate(num);
	}

	/**
	 * 读取配置文件里的渠道名
	 * 
	 * @param context
	 * @return
	 */
	public static String getMainfestInfo(Activity context) {
		String channel = "";
		try {
			// 设置PackageManager.GET_META_DATA标识位是必须的
			ApplicationInfo info = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			channel = info.metaData.getString("UMENG_CHANNEL");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return channel;

	}
	public static int strToInt(String numStr) {
		int num = 1;
		if (numStr != null && !numStr.equals("")) {
			num = Integer.parseInt(numStr);
		}
		return num;
	}
	public static String formatWeek(String weekId){
		String weekStr = "";
		int id = 0;
		if(!weekId.equals("")&&!weekId.equals("null"))
		id = Integer.parseInt(weekId);
		switch(id){
		case 0:
			weekStr = "";
			break;
		case 1:
			weekStr = "星期一";
			break;
		case 2:
			weekStr = "星期二";
			break;
		case 3:
			weekStr = "星期三";
			break;
		case 4:
			weekStr = "星期四";
			break;
		case 5:
			weekStr = "星期五";
			break;
		case 6:
			weekStr = "星期六";
			break;
		case 7:
			weekStr = "星期日";
			break;
		}
		return weekStr;
	}
	public static String formatDateDT(String dateStr) {
		String year = dateStr.substring(0, 4);
		String moon = dateStr.substring(4, 6);
		String day = dateStr.substring(6, dateStr.length());
 		return year+"-"+moon+"-"+day;
	}
	public static String maxStrLength(String name,int maxL){
		if(name.length()>maxL){
			return name.substring(0, maxL);
		}else{
			return name;
		}
	}


}
