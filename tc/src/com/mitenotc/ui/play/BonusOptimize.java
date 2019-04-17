package com.mitenotc.ui.play;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.mitenotc.bean.MessageBean;
import com.mitenotc.bean.RATEBean;
import com.mitenotc.bean.TicketBean;
import com.mitenotc.bean.UserBean;
import com.mitenotc.enums.AccountEnum;
import com.mitenotc.enums.CustomTagEnum;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.tc.SecondActivity;
import com.mitenotc.tc.ThirdActivity;
import com.mitenotc.ui.TCDialogs;
import com.mitenotc.ui.TCWebFragment;
import com.mitenotc.ui.TCDialogs.MyClickedListener;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.ui.base.BaseActivity.MyBackPressedListener;
import com.mitenotc.ui.buy.JcBaseFragment;
import com.mitenotc.ui.pay.Paymain;
import com.mitenotc.ui.ui_utils.AddSelfEditText;
import com.mitenotc.ui.ui_utils.GridRadioGroup;
import com.mitenotc.ui.ui_utils.MyToast;
import com.mitenotc.ui.ui_utils.AddSelfEditText.ActionUpListener;
import com.mitenotc.ui.ui_utils.AddSelfEditText.AddTextChangedListener;
import com.mitenotc.utils.FormatUtil;
import com.mitenotc.utils.Getlotterykind;
import com.mitenotc.utils.ParseLottery;
/**
 * 奖金优化
 * @author mitenotc
 *
 */
public class BonusOptimize extends BaseFragment implements OnClickListener {
	 private JcBaseFragment fragment;//该彩种的选号界面的fragment//ck 添加
	 private Class<? extends BaseFragment> lotteryClazz;//当前彩种的选号界面的fragment的clazz.
	
	 private BOAdapter mboAdapter=null;
	 private AddSelfEditText plan_buy_edit;//计划购买金额
	 private CheckBox explain_chbox;
	 private GridRadioGroup condition_rgp;
	 private RadioButton rdBtn0;
	 private RadioButton rdBtn1;
	 private RadioButton rdBtn2;
	 
	 
	 private EditText allnumber_ed;//总订单投注倍数
	 private Button payment_btn;//付款下单
	 private TextView show_text;//提示  0注0元
	 private static int  OTAG=1;//默认初始化 为平均优化
	 private  List<Float> bonuslist =new LinkedList<Float>();
	 private List<Integer> result;//返回计算结果
     private long lastClickTime=0; //防止重复点击
     private int ALLMoney=0;
     private int lotterycount=0;
     private int bo_pour=1;
     private  TCDialogs yloudialog;//提示框
     private List<Integer> isChList=new LinkedList<Integer>();//是否选择下单
	 
////	 mean optimize 平均优化
//	 private static int  MEAN_OPTIMIZE=1;
////	 risk min  风险最小
//	 private static int  RISK_MIN=2;
////	 bonus max 奖金最高
//	 private static int  BONUS_MAX=3;
     private ArrayList<Object[]> allAList=new ArrayList<Object[]>(); 
     private 	ListView boListView;
     private int OrdlerAmount=0;
	 private Handler boHandler=new Handler(){
		 public void handleMessage(Message msg) {
			 switch (msg.what) {
			case 0:
				try {
					if(mboAdapter==null){//初始化
						OrdlerAmount=(int)(MyApp.order.getAmount()/100);
					    calcBonus();
						mboAdapter=new BOAdapter();
						boListView.setAdapter(mboAdapter);
					}else{
						OrdlerAmount= Integer.parseInt(plan_buy_edit.getText());
					}
					setNoticetext();
					setChecked();
					mboAdapter.notifyDataSetChanged();
				} catch (ArithmeticException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					MyToast.showToast(mActivity, "无法进行奖金优化!");
				}
				break;
			}
		 }

	 };
		private void setChecked() {
			// TODO Auto-generated method stub
			switch (OTAG) {
			case 1:
				rdBtn0.setChecked(true);
				break;
			case 2:
				rdBtn1.setChecked(true);
				break;
			case 3:
				rdBtn2.setChecked(true);
				break;
			}
			
		}
     /**
      * 通知消息
      */
		private void setCheckedNoticetext() {
			List<Float> tem=null;
			OrdlerAmount=0;
			if(result!=null){
			 if(isChList.size() > 0){
				 for (int i = 0; i < isChList.size(); i++) {//isChList  要选择下单的单票
						 OrdlerAmount += result.get(isChList.get(i));//计算总倍数
						 System.out.println("210---> result "+result.get(isChList.get(i))+"|result.to : "+result.toString()+"|--isChList "+isChList.get(i));
				 }
			 }
			 OrdlerAmount *= 2;//每注两元  总金额
			 plan_buy_edit.setText(String.valueOf(OrdlerAmount));//总金额此处顺序不能修改
			 OrdlerAmount *= bo_pour;// 投注倍数
			 tem=new ArrayList<Float>();
			 for (int i = 0; i < isChList.size(); i++) {
				 if(bonuslist.size() > i && result.size() > i){
						tem.add((float) (result.get(isChList.get(i)) * bonuslist.get(isChList.get(i))));
					}
			 }
		        Collections.sort(tem);//排序
//			     String jj="奖金范围值"+String.valueOf(tem.get(0))+"~"+String.valueOf(tem.get(tem.size()-1));
				if(tem.size() > 0){
					show_text.setText(Html.fromHtml(replaceNoticeString(String.valueOf(OrdlerAmount), FormatUtil.getFloatStr(tem.get(0) * bo_pour), FormatUtil.getFloatStr(tem.get(tem.size()-1) * bo_pour))));
				}else{
					show_text.setText(Html.fromHtml(replaceNoticeString("0", "0", "0")));
				}
			}else{
				show_text.setText(Html.fromHtml(replaceNoticeString("0", "0", "0")));
			}
		}
     /**
      * 通知消息
      */
		private void setNoticetext() {
			List<Float> tem=null;
			OrdlerAmount=0;
			if(result!=null){
			 if(isChList.size() > 0){
//				 calcBonus();
				 for (int i = 0; i < isChList.size(); i++) {//isChList  要选择下单的单票
						 OrdlerAmount += result.get(isChList.get(i));//计算总倍数
						 System.out.println("210---> result "+result.get(isChList.get(i))+"|result.to : "+result.toString()+"|--isChList "+isChList.get(i));
				 }
			 }
			 OrdlerAmount *= 2;
			 plan_buy_edit.setText(String.valueOf(OrdlerAmount));//总金额
			 OrdlerAmount *= bo_pour;//每注两元  总金额
			 tem=new ArrayList<Float>();
			 for (int i = 0; i < isChList.size(); i++) {
				 if(bonuslist.size() > i && result.size() > i){
						tem.add((float) (result.get(isChList.get(i)) * bonuslist.get(isChList.get(i))));
				}
			 }
		        Collections.sort(tem);//排序
//			     String jj="奖金范围值"+String.valueOf(tem.get(0))+"~"+String.valueOf(tem.get(tem.size()-1));
				if(tem.size() > 0){
					show_text.setText(Html.fromHtml(replaceNoticeString(String.valueOf(OrdlerAmount), FormatUtil.getFloatStr(tem.get(0)), FormatUtil.getFloatStr(tem.get(tem.size()-1)))));
				}else{
					show_text.setText(Html.fromHtml(replaceNoticeString("0", "0", "0")));
				}
			}else{
				show_text.setText(Html.fromHtml(replaceNoticeString("0", "0", "0")));
			}
		}
		public String replaceNoticeString(String zje,String min,String max){
			String s="";
			if(StringUtils.isBlank(zje)){
				zje="0";
			}
			if(StringUtils.isBlank(min)){
				min="0";
			}
			if(StringUtils.isBlank(max)){
				max="0";
			}
//			 <string name="jc_bo_showtext"><![CDATA[总金额:<font color="#ff9500">NUM</font>元<small><br>奖金范围值:MIM ~ MAX<small><br></font>]]></string>
			return  StringUtils.replaceEach(MyApp.res.getString(R.string.jc_bo_showtext), new String[]{"NUM","MIM","MAX"}, new String[]{zje,min,max});
		}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitleNav("奖金优化", R.drawable.title_nav_back, 0);
		setContentView(R.layout.bonusoptimize_layout);
		
		LinearLayout content_layout=(LinearLayout) findViewById(R.id.bop_content_layout);
	    boListView = new ListView(mActivity);
		boListView.setDivider(new ColorDrawable(getResources().getColor(R.color.view_line)));//#cecece
		boListView.setDividerHeight((int) getResources().getDimension(R.dimen.bodyfragment2_lv_divider_height));
		boListView.setSelector(new ColorDrawable(mActivity.getResources().getColor(R.color.login_color_6)));  //背景选择器
		boListView.setBackgroundColor(mActivity.getResources().getColor(R.color.login_color_6));
//		boListView.setPullRefreshEnable(false);//不允许刷新
		content_layout.addView(boListView);
		initLV();
		setLisntener();
		boHandler.sendEmptyMessage(0);
		initJCBasseFragment();
	}
	@Override
	protected void onLeftIconClicked() {//返回按钮的监听重写
		onBack();
	}
	private void onBack() {
		if(yloudialog==null){
			yloudialog=new TCDialogs(mActivity);//提示
		}
		yloudialog.popDeleteConfirm("放弃奖金优化方案", "当前所有方案信息将不会保存！", new MyClickedListener() {
			@Override
			public void onClick() {
				yloudialog.dismiss();
			}
		}, new  MyClickedListener() {
			@Override
			public void onClick() {
				start(SecondActivity.class,JczqBetorder.class,null);
				yloudialog.dismiss();
				finish();
			}
		});
	}
	/**
	 * 初始化当前彩种的选号界面的fragmen
	 */
	@SuppressWarnings("unchecked")
	private void initJCBasseFragment(){
		  try{
			  lotteryClazz = (Class<? extends BaseFragment>) Class.forName("com.mitenotc.ui.play.JCPL"+MyApp.order.getAppId());
			  fragment = (JcBaseFragment) getFragment(lotteryClazz);
			}catch (ClassNotFoundException e) {
				e.printStackTrace();
				MyToast.showToast(mActivity, "该玩法暂不存在");
			}
	 }
	private void initLV() {
//		View headView=View.inflate(mActivity, R.layout.bonus_optimize, null);
//		plan_buy_edit=(AddSelfEditText) headView.findViewById(R.id.plan_buy_edit);//计划购彩总金额
//		explain_chbox=(CheckBox) headView.findViewById(R.id.explain_chbox);//说明Checkbox
//		condition_rgp=(GridRadioGroup) headView.findViewById(R.id.condition_rgp);//条件筛选
//		boListView.addHeaderView(headView);
		yloudialog=new TCDialogs(mActivity);//提示
		
		plan_buy_edit=(AddSelfEditText) findViewById(R.id.plan_buy_edit);//计划购彩总金额
		plan_buy_edit.setText(String.valueOf(MyApp.order.getAmount()/100));
		explain_chbox=(CheckBox) findViewById(R.id.explain_chbox);//说明Checkbox
		condition_rgp=(GridRadioGroup)findViewById(R.id.condition_rgp);//条件筛选
		
		allnumber_ed=(EditText) findViewById(R.id.multiple_number_ed);
		payment_btn=(Button) findViewById(R.id.payment_btn);
		show_text=(TextView) findViewById(R.id.show_bet_result_tv);
		
	    rdBtn0=(RadioButton) findViewById(R.id.rb0);
		rdBtn1=(RadioButton) findViewById(R.id.rb1);
		rdBtn2=(RadioButton) findViewById(R.id.rb2);
		
	}
	public  void setLisntener() {
		plan_buy_edit.setAddtextChengeListener(new AddTextChangedListener() {
			@Override
			public void textChanged(CharSequence cs, int start, int before, int count) {
				 if(StringUtils.isBlank(cs.toString())){
					 plan_buy_edit.setHint(String.valueOf(MyApp.order.getAmount()/100));
					 return;
				 }else{		
//					int money = Integer.parseInt(plan_buy_edit.getText());
//					result=ParseLottery.calBonus(OTAG, bonuslist, money);
				   
//					OrdlerAmount= Integer.parseInt(plan_buy_edit.getText());
//					System.out.println("210--------------->"+OrdlerAmount);
					 plan_buy_edit.setText(cs);
//					boHandler.sendEmptyMessage(0);
				 }
				 condition_rgp.clearCheck();
			}
		});
		plan_buy_edit.setActionUpListener(new ActionUpListener() {
			@Override
			public void onActionUP() {
				if(StringUtils.isBlank(plan_buy_edit.getText())){
					plan_buy_edit.setText("1");
					 return;
				 }
				 condition_rgp.clearCheck();
//				else{		
//					int money = Integer.parseInt(plan_buy_edit.getText());
//					result=ParseLottery.calBonus(OTAG, bonuslist, money);
//				    OrdlerAmount= Integer.parseInt(plan_buy_edit.getText());
//					boHandler.sendEmptyMessage(0);
//				 }
			}
		});
		condition_rgp.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				boolean b=false;
				int tm=0;
				if(!StringUtils.isBlank(plan_buy_edit.getText())){
					tm=Integer.parseInt(plan_buy_edit.getText());
				}
				if(0==tm){
					tm=(int)(MyApp.order.getAmount()/100);
				}
				switch (checkedId) {
					case R.id.rb0://平均优化
						group.check(checkedId);
						OTAG=1;
						
						System.out.println("125---平均"+OTAG);
						break;
					case R.id.rb1://风险最小
						if(MyApp.order.getTickets().size()==1 && MyApp.order.getTickets().get(0).getSession()==2){
							b=true;
						}
						if(b){
							group.check(checkedId);
							OTAG=2;
						}else{
							group.clearCheck();
							MyToast.showToast(mActivity, "奖金优化不支持多场多串关方式!");
						}
						System.out.println("125---风小"+OTAG);
						break;
					case R.id.rb2://奖金最高
						if(MyApp.order.getTickets().size()==1 && MyApp.order.getTickets().get(0).getSession()==2){
							b=true;
						}
						if(b){
							group.check(checkedId);
							OTAG=3;
						}else{
							group.clearCheck();
							MyToast.showToast(mActivity, "奖金优化不支持多场多串关方式!");
						}
						System.out.println("125---奖高"+OTAG);
						break;
				}
				result=ParseLottery.calBonus(OTAG, bonuslist, tm);
				if(mboAdapter!=null){
					mboAdapter.notifyDataSetChanged();
				}
				setCheckedNoticetext();
				setChecked();
			}
		});
		allnumber_ed.addTextChangedListener(new  TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				String ed=allnumber_ed.getText().toString().trim();
				if(StringUtils.isBlank(ed) || "0".equals(ed)){
					allnumber_ed.setHint("1");
					bo_pour=1;
				}else{
					bo_pour=Integer.parseInt(ed);
				}
				setNoticetext();
			}
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {
			}
			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});
		//手机返回按钮
		setMyBackPressedListener(new MyBackPressedListener() {
			@Override
			public void onMyBackPressed() {
				onBack();
			}
		});
		explain_chbox.setOnClickListener(this);
		payment_btn.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.payment_btn://付款下单
			gotopay();
			break;
		case R.id.explain_chbox://优化说明
//			210_optimization.html
			Bundle	bundle=new Bundle();
			bundle.putString("title", "奖金优化");
			bundle.putString("url", "file:///android_asset/help/210_optimization.html");
			start(ThirdActivity.class,TCWebFragment.class,bundle);
			break;
		}
	}

	/**
	 * 登陆返回之后   回调 去付款的方法
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 1002 && UserBean.getInstance().isLogin()){//登录成功才会再次去请求
			gotopay();
		}
	}
	private void gotopay() {
		 if(isFastClick(mActivity))return;//防止重复点击
		 if(!UserBean.getInstance().isLogin())//检测登录
		 {
	   		 startLoginForResult();
	   		 return;
   	     }
		//单注金额不能大于两万
		 for (TicketBean ticket : MyApp.order.getTickets()) {
			 if(ticket.getTicketAmount() > 2000000){
				 MyToast.showToast(mActivity, "单票金额不能超过2万元,请理性购彩!");
				 return; 
			 }
		 }
		 JSONArray g =null;
		 String b="";
		 MessageJson msg=null ,tmsg = null;
		 if(isChList.size() > 0){
			 OrdlerAmount=0;
			 g = new JSONArray();
			 for (int i = 0; i < isChList.size(); i++) {//isChList  要选择下单的单票
					 Object[] o =allAList.get(isChList.get(i));//拿出对应的组合票
					 if(o !=null && o.length > 0){
						 msg =new MessageJson();
						 for (int j = 0; j < o.length; j++) {
							 tmsg=(MessageJson) o[j];
							 if(!tmsg.isNull("cg") && j==0){//只拿一个就行
								 msg.put("A", tmsg.get("cg"));//A	过关方式
							 }
							 b += tmsg.get("sb_json");
							 if(j!=o.length-1)b+=";";
						 }
					 }
					 
//					 msg.put("D", String.valueOf(bo_pour));//D	注数 
					 msg.put("D", "1");//D	注数  2015-1-9  确认奖金优化后规定所有单票都是一注 
					 msg.put("C", String.valueOf((bo_pour * result.get(isChList.get(i)))));//C	倍数
					 if(!StringUtils.isBlank(b)){
						 msg.put("B", b);//B	投注代码，注与注之间用^分开
					 }
					 b="";
					 g.put(msg);
					 OrdlerAmount += result.get(isChList.get(i));//计算总倍数
			 }
			 OrdlerAmount *= (200 * bo_pour);//每注两元
			 System.out.println("210---g: "+g.toString()+"calcAmount"+OrdlerAmount);
			 submitData(4001, 4001, MyApp.order.boJCOrderJson(g,OrdlerAmount));
			 
		 }else{
			 MyToast.showTestToast(mActivity, "没有选票!");
		 }
//				for (int i1 = 0; i1 < result.size(); i1++) {
//					Object[] o =allAList.get(i1);
////					 for (Object[] o : allAList) {
//					msg =new MessageJson();
//					if(o !=null && o.length > 0){
//						for (int j = 0; j < o.length; j++) {
//							tmsg=(MessageJson) o[j];
//							if(!tmsg.isNull("cg") && j==0){//只拿一个就行
//								msg.put("A", tmsg.get("cg"));//A	过关方式
//							}
//							b += tmsg.get("sb_json");
//							if(j!=o.length-1)b+=";";
//						}
//						
//						msg.put("D", String.valueOf(MyApp.order.getBo_pour()));//D	注数
//						msg.put("C", String.valueOf(result.get(i1)));//C	倍数
//						if(!StringUtils.isBlank(b)){//-----TODO------投注代码 可能需要根据注数重新拼接字符串
//							msg.put("B", b);//B	投注代码，注与注之间用^分开
//						}
////					 }
//						b="";
//						g.put(msg);
//						calcAmount += result.get(i);
//					}
//					System.out.println("125---"+i+"----"+result.get(i));
//				}	
				
//			}
//			 calcAmount*=200;//每注两元
//			 System.out.println("125---g: "+g.toString()+"calcAmount"+calcAmount);
//			 submitData(4001, 4001, MyApp.order.boJCOrderJson(g,calcAmount));
		
	}
@Override
protected void onMessageReceived(Message msg) {
	MessageBean returnmb=(MessageBean) msg.obj;
    switch (msg.arg1){
    case 4001:
	    if(0==Integer.parseInt(returnmb.getA()))
	    {
//	    	MyToast.showToast(mActivity, returnmb.getB());
	    	if(!StringUtils.isBlank(returnmb.getD())){// D	账户可用余额
	    		UserBean.getInstance().setAvailableMoney(Long.parseLong(returnmb.getD()));
	    	}
	    	if(!StringUtils.isBlank(returnmb.getE())){//E	账户可提现金额
	    		UserBean.getInstance().setAvailableCash(Long.parseLong(returnmb.getE()));
	    	}
	    	if(!StringUtils.isBlank(returnmb.getF())){// F	账户可用资金(能用来购彩的金额，包括彩金卡)
	    		UserBean.getInstance().setAvailableBalance(Long.parseLong(returnmb.getF()));
	    	}
	    	AccountEnum.convertMessage(returnmb.getLIST());
	    	MyApp.order.setOrderId(returnmb.getC());//C	订单流水号
	    	Bundle payBundle = new Bundle();
	    	payBundle.putString("lotteryId", MyApp.order.getLotteryId());//竞彩足球使用的是内部id 125 编号
	    	payBundle.putString("orderId", returnmb.getC());//1107接口需要的 订单号
	    	payBundle.putString("money", String.valueOf(OrdlerAmount));//1107 接口需要的第三方支付金额,这里其实是支付的总金额
	    	payBundle.putString("availableAmount", returnmb.getD());//用于确定是否进行红包支付
	    	payBundle.putString("issue", MyApp.order.getIssue());//支付中需要的期数
	    	payBundle.putInt(MyApp.res.getString(R.string.cmd), 1107);//支付中需要的CMD
	    	
//	    	//启动支付页面
	    	start(ThirdActivity.class,Paymain.class,payBundle);
	    	//关闭选号页面和下单页面 并重置订单
	    	MyApp.resetOrderBean();//重置订单
	    	setFragmentCacheEnable(false);//fragment 不需要缓存
	    	fragment.finish();
	    	finish();
	    }else{
	    	MyToast.showToast(mActivity, returnmb.getB());//提示
	    }
	    break;
    }
	super.onMessageReceived(msg);
}
	/**
	 * 优化
	 */
	private void calcBonus() {
		float temp=1;
		allAList.clear();
		bonuslist.clear();
//		isChList.clear();
		List<TicketBean> tL=MyApp.order.getTickets();
		for (int j = 0; j < tL.size(); j++) {
			LinkedList<Object[]> tempArray=tL.get(j).createChildTicket();
			for (int i = 0; i < tempArray.size(); i++) {
				temp=1;
				if(-1==allAList.indexOf(tempArray.get(i))){
					allAList.add(tempArray.get(i));
				}
				for (int k = 0; k < tempArray.get(i).length; k++) {
					MessageJson tempMj=(MessageJson) tempArray.get(i)[k];
					if(!tempMj.isNull("sp") && !StringUtils.isBlank(tempMj.getString("sp")) ){
						temp *=Float.parseFloat(tempMj.getString("sp"));
					}
				}
				bonuslist.add(FormatUtil.getFloat(temp));
//				isChList.add(i);//串关是否选中
			}
		}
		int tm=0;
		if(!StringUtils.isBlank(plan_buy_edit.getText())){
			tm=Integer.parseInt(plan_buy_edit.getText());
		}
		if(0==tm || 1==tm){//  1 是plan_buy_edit.getText();默认值
			tm=(int)(MyApp.order.getAmount()/100);
		}
		System.out.println("210--OTAG "+OTAG+ " bonuslist "+bonuslist.toString()+"|size "+bonuslist.size()
				           +" n :"+MyApp.order.getAmount()/100+"|tm"+tm);
		result=ParseLottery.calBonus(OTAG, bonuslist,tm);
	};

	class BOAdapter extends BaseAdapter{
		 List<String> tagList=new ArrayList<String>();//查看投注比例
		 public BOAdapter() {
			isChList.clear();
			List<TicketBean> tL=MyApp.order.getTickets();
			for (int j = 0; j < tL.size(); j++) {
				LinkedList<Object[]> tempArray=tL.get(j).createChildTicket();
				for (int i = 0; i < tempArray.size(); i++) {
					isChList.add(i);//拆分选票默认都是选中的
				}
			}
		}

		@Override
		public int getCount() {
			return allAList.size();//彩票ticket数
		}
		@Override
		public Object getItem(int position) {
			return allAList.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView( final int position, View convertView, ViewGroup viewGroup) {
			ViewHolder mHolder=null;
			int zs=1;
			float jj=1;
			Object[] tempo=null;
			if(convertView==null){
			    convertView=View.inflate(mActivity, R.layout.item_bonusoptimize, null);
			    mHolder=new ViewHolder(convertView);
			    convertView.setTag(mHolder);
			}
			mHolder=(ViewHolder) convertView.getTag();
			tempo=allAList.get(position);
			if(result!=null && (result.size() > position)){
				zs=result.get(position);//[39808, 3828, 527, 62, 10, 1]
				if(bonuslist.size() > position){
					jj= zs * bonuslist.get(position);//[7.38, 76.73, 557.34, 4709.5, 27795.49, 231258.44]
				}
			}
			mHolder.fund_tv.setText(FormatUtil.getFloatStr(jj)+"元");
			mHolder.edit.setText(String.valueOf(zs));
			if(tempo!=null){
				mHolder.bunch_type.setText(Getlotterykind.getCGString(tempo.length));
			}
		    mHolder.lose_tv.setText(Html.fromHtml(getVsName(allAList.get(position))));
//			mHolder.bunch_type.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//				@Override
//				public void onCheckedChanged(CompoundButton cbtn, boolean isChecked) {
//					if(isChecked){
//						if(-1 == isChList.indexOf(isChList.indexOf(position))){
//							isChList.add(position);
//						}
//					}else {
//						if(-1 != isChList.indexOf(isChList.indexOf(position))){
//							isChList.remove(isChList.indexOf(position));
//						}
//					}
//					notifyDataSetChanged();
//				}
//			});
		    mHolder.bunch_type.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if(isChList.contains(position)){
						System.out.println("----------0-----------");
						isChList.remove(position);
					}else{
						System.out.println("----------1-----------");
						isChList.add(position);
					}
					setNoticetext();
					setChecked();
					notifyDataSetChanged();
					System.out.println("isChList---------->"+isChList.toString());
//					boHandler.sendEmptyMessage(0);
//					setNoticetext();
				}
			});
			mHolder.lose_tv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					System.out.println("220--position--->"+position+"tagList --->"+tagList.toString());
					if(tagList.contains(String.valueOf(position))){
						tagList.remove(String.valueOf(position));
					}else{
						tagList.add(String.valueOf(position));
					}
					notifyDataSetChanged();
				}
			});
			Drawable  mDrawable=MyApp.res.getDrawable(R.drawable.checkbox_red_unselect);
			mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(), mDrawable.getMinimumHeight());
			mHolder.bunch_type.setCompoundDrawables(mDrawable, null, null, null);
			if(-1 != isChList.indexOf(position)){
//				mHolder.bunch_type.setChecked(true);
				mDrawable=MyApp.res.getDrawable(R.drawable.checkbox_red_select);
				mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(), mDrawable.getMinimumHeight());
				mHolder.bunch_type.setCompoundDrawables(mDrawable, null, null, null);
			}
//			System.out.println("125---->isChList  "+isChList.toString());
//			else{
//				mHolder.bunch_type.setChecked(false);
//			}
		  //  组栏效果(默认)
		    mDrawable=MyApp.res.getDrawable(R.drawable.d1);
			mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(), mDrawable.getMinimumHeight());
			mHolder.lose_tv.setCompoundDrawables(null, null, null, mDrawable);
//			hide(mHolder.item_item_layout);//默认
			mHolder.item_item_layout.setVisibility(View.GONE);
			if(tagList.contains(String.valueOf(position))){
				mHolder.item_item_layout.setVisibility(View.VISIBLE);
//				show(mHolder.item_item_layout);
				mDrawable=MyApp.res.getDrawable(R.drawable.u1);
				mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(), mDrawable.getMinimumHeight());
				mHolder.lose_tv.setCompoundDrawables(null, null, null, mDrawable);
			}
			addListener(mHolder.edit,position);
			return convertView;
		}
		/**
		 * 修改计划购买注数(实际意义上的倍数)
		 * @param edit
		 */
		private void addListener(final AddSelfEditText edit,final int position) {
			
			edit.setAddtextChengeListener(new AddTextChangedListener() {
				@Override
				public void textChanged(CharSequence cs, int start, int before, int count) {
					 if(StringUtils.isBlank(cs.toString())){
						 return;
					 }else{		
						int money = Integer.parseInt(edit.getText());
						if(0==money){
							money+=1;
						}
						result.set(position, money);
						if(result!=null){
							int n=0;
							for (int i = 0; i < result.size(); i++) {
								n += result.get(i);
								System.out.println("125--->"+i+result.get(i));
							}
							n *= 2 ;
							plan_buy_edit.setText(String.valueOf(n));//总金额
							
							show_text.setText("总金额"+String.valueOf(n * bo_pour)+"元");
						}
						setUpdateNotice();
						notifyDataSetChanged();
					 }
				}

			});
			edit.setActionUpListener(new ActionUpListener() {
				@Override
				public void onActionUP() {
					if(StringUtils.isBlank(edit.getText())){
						 return;
					 }else{		
						int money = Integer.parseInt(edit.getText());
						result.set(position, money);
						if(result!=null){
							int n=0;
							for (int i = 0; i < result.size(); i++) {
								n += result.get(i);
								System.out.println("125--->"+i+result.get(i));
							}
							n *= 2;
							plan_buy_edit.setText(String.valueOf(n));//总金额
							show_text.setText("总金额"+String.valueOf(n * bo_pour)+"元");
						}
						setUpdateNotice();
						notifyDataSetChanged();
					 }
				}
			});
			
		}
		private void setUpdateNotice() {
			List<Float> tem=null;
			int n=0;
			if(result!=null){
				tem=new ArrayList<Float>();
				for (int i = 0; i < result.size(); i++) {
					n += result.get(i);
					if(bonuslist.size() > i){
						tem.add((float) (result.get(i) * bonuslist.get(i)));
					}
				}
				n *= 2 ;
				plan_buy_edit.setText(String.valueOf(n));//总金额
				Collections.sort(tem);//排序
//						String jj="奖金范围值"+String.valueOf(tem.get(0))+"~"+String.valueOf(tem.get(tem.size()-1));
				show_text.setText(Html.fromHtml(replaceNoticeString(String.valueOf(n * bo_pour), FormatUtil.getFloatStr(tem.get(0)), FormatUtil.getFloatStr(tem.get(tem.size()-1)))));
			}
		}

		class ViewHolder{
			TextView bunch_type	;//checkbox_red_select 和 切换
			LinearLayout without_layout;
		     LinearLayout item_item_layout;
		     AddSelfEditText edit;
		     TextView fund_tv;//预测奖金
		     TextView lose_tv;//提示信息
		     TextView win_tv;
			public ViewHolder(View v) {
				if(v==null){return ;}
				//init 控件 此处不对convertView 进行循环使用 (会导致CheckBox 刷新焦点初始化为默认值)
			    bunch_type = (TextView) v.findViewById(R.id.bunch_type);//串关
//				without_layout=(LinearLayout) v.findViewById(R.id.without_layout);//单注组合最外部Layout
				item_item_layout=(LinearLayout) v.findViewById(R.id.item_item_layout);//单注组合最外部Layout
//				final CheckBox chbox_tag=(CheckBox) convertView.findViewById(R.id.chbox_tag);
				edit=(AddSelfEditText) v.findViewById(R.id.edit);//注数分布编辑框
//				win_tv=(TextView) v.findViewById(R.id.win_tv);
				lose_tv=(TextView) v.findViewById(R.id.lose_tv);
			    fund_tv=(TextView) v.findViewById(R.id.fund_tv);
			}
		     
		}
	};
	/**
	 *  防止点击频繁点击  出现白屏 
	 * @param mActivity
	 * @return boolean
	 */
	private   boolean isFastClick(Context mActivity) {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 1500 ) {
			return true;
		}
		lastClickTime = time;
		return false;
	}
	/**
	 * //<![CDATA[ZHU|WIN<small><br>KE|WIN</br></small>]]>
	 * @param childTK
	 * @return
	 */
	public static  String getVsName(Object[] childTK){
		String vs1="",vs2 ="";
		MessageJson mj=null;
		if(childTK==null )return "";
		if(1==childTK.length){//单关
			mj=(MessageJson) childTK[0];
			if(!mj.isNull("ms")){
				vs1=mj.getString("ms");
			}
			return vs1;
		}else if(childTK.length >= 2){
			mj=(MessageJson) childTK[0];
			if(!mj.isNull("ms")){
				vs1=mj.getString("ms");
			}
			mj=(MessageJson) childTK[1];
			if(!mj.isNull("ms")){
				vs2=mj.getString("ms");
			}
		}
//		else if(childTK.length==1){
//			System.out.println("125---vs-->"+((CTicketBean) childTK[0]).getVsStr());
//		}
		return StringUtils.replaceEach(MyApp.res.getString(R.string.jc_bovsText), new String[]{"V1","V2"}, new String[]{ vs1 ,vs2});
		
	}
}
