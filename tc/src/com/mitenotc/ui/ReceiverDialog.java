package com.mitenotc.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.mitenotc.enums.CustomTagEnum;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.MyReceiver;
import com.mitenotc.tc.R;
import com.mitenotc.tc.TCActivity;
import com.mitenotc.ui.TCDialogs.MyClickedListener;
import com.mitenotc.ui.base.BaseActivity;
import com.mitenotc.ui.play.IntelligentScheme;
import com.mitenotc.utils.AppUtil;
import com.mitenotc.utils.DensityUtil;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ReceiverDialog extends Dialog implements android.view.View.OnClickListener {
	private Context ctx;
//	private TextView title_view;
	private TextView text_view;
	private Button left_btn;
	private Button reight_btn;
	private ImageView close_btn;
	
	private List<JSONObject> jsonInfo=new ArrayList<JSONObject>();
	Intent newintent =null;
	final Bundle newbundle = new Bundle(); 
	final String window_idstr="1000";
	String leftbtnStr="立即查看";
	String rightbtnStr="关   闭";
	String title="系统消息";
	CharSequence msg=null;
//	private int temp = 0;  // 初始化 0 开始拿info
	private static SQLiteDatabase db;
	
	public ReceiverDialog(Context context,IntelligentScheme obj) {
		super(context, R.style.dialog_theme);
		setCanceledOnTouchOutside(true);//触动了窗外的界限能取消界面
		init(context);
	}
	public ReceiverDialog(Context context) {
		super(context, R.style.dialog_theme);
		init(context);
		setCanceledOnTouchOutside(true);//触动了窗外的界限能取消界面
	}
	public ReceiverDialog(Context context, int theme) {
		super(context, theme);
		init(context);
		setCanceledOnTouchOutside(true);//触动了窗外的界限能取消界面
	}
	private void init(Context context) {
		this.ctx=context;
		newintent= new Intent(ctx, TCActivity.class); 
		if(db==null){
			db= MyApp.datacache.getWritableDatabase();
		}
		setContentView(R.layout.receiver_dialog);
		initView();
		initViewText();
		toCreateShow();
	}
	
	private void initView() {
//		title_view = (TextView) findViewById(R.id.dg_title);
		text_view=(TextView) findViewById(R.id.dg_text);
		left_btn=(Button) findViewById(R.id.lift_btn);
		reight_btn=(Button) findViewById(R.id.reight_btn);
		close_btn=(ImageView) findViewById(R.id.close_btn);
		
		left_btn.setOnClickListener(this);
		close_btn.setOnClickListener(this);
		reight_btn.setOnClickListener(this);
		
	}
	private void initViewText() {
		left_btn.setText(leftbtnStr);
		reight_btn.setText(rightbtnStr);
		if(AppUtil.isEmpty(left_btn.getText().toString().trim())){
			leftbtnStr="立即查看";
			left_btn.setText(leftbtnStr);
		}
	}
	private JSONObject getinfoJson() throws JSONException{
		JSONObject J=null;
		if(jsonInfo.size() >=1){
			if(1==jsonInfo.size()){
				rightbtnStr="取   消";
			}else {
				rightbtnStr="下一条";	
			}
			J=jsonInfo.get(0);
		}
		return J;
	}

	public void showReceiverDialog() {
		try {
				JSONObject info=getinfoJson();
				if(info==null){return;}
//				String t="";
				if("00".equals(info.getString("A"))){
					if(!info.getJSONObject("B").isNull("B") ){//00 :E	信息连接地址
						text_view.setText(info.getJSONObject("B").getString("B"));
					}
//					title_view.setText("系统消息");
					newbundle.putString("url", info.getJSONObject("B").getString("E"));
					newintent.putExtra("window_id","10000");
					newintent.putExtras(newbundle);
				}else if("01".equals(info.getString("A"))){
					if(!info.getJSONObject("B").isNull("A") ){//01 :B_B	信息摘要
						msg=Html.fromHtml(info.getJSONObject("B").getString("A"));
						text_view.setText(msg);
					}
//					title_view.setText(msg);
//					title_view.setText("系统消息");
					newbundle.putString("Notification", info.getJSONObject("B").toString());
					newintent.putExtra("window_id",info.getJSONObject("B").getString("C"));
					newintent.putExtras(newbundle);
				}else if("02".equals(info.getString("A"))){
					if(!info.getJSONObject("B").isNull("A") ){//02 :B_B	信息摘要
						msg= Html.fromHtml(info.getJSONObject("B").getString("A"));
						text_view.setText(msg);
					}
//					title_view.setText(msg);
//					title_view.setText("系统消息");
//					if(!info.getJSONObject("B").isNull("B") ){//02 :B_B	信息摘要
//						msg= Html.fromHtml(info.getJSONObject("B").getString("B"));
//					}
//					text_view.setText(msg);
					newbundle.putString("Notification", info.getJSONObject("B").toString());
					newintent.putExtra("window_id","0");
					newintent.putExtras(newbundle);
				}else if("03".equals(info.getString("A"))){
					if(!info.getJSONObject("B").isNull("C") ){//03 :B_C	信息摘要
						msg= Html.fromHtml(info.getJSONObject("B").getString("C"));
						text_view.setText(msg);
					}
//					title_view.setText(msg);
//					title_view.setText("系统消息");
				    int msgtype = info.getJSONObject("B").getInt("A");
					if(msgtype == 3 ||  msgtype == 4)
					{
					     newbundle.putString("orderId", info.getJSONObject("B").getJSONObject("B").getString("A"));
						if("1".equals(info.getJSONObject("B").getJSONObject("B").getString("F"))){
							
							newintent.putExtra("window_id","1013"); //不追号普通玩法
						}else if("2".equals(info.getJSONObject("B").getJSONObject("B").getString("F"))){
							newintent.putExtra("window_id","1014"); //追号普通玩法
						}else{
							newintent.putExtra("window_id","0");
						}
					}else{
//						title_view.setText("系统消息");
						if(!info.getJSONObject("B").isNull("C") ){//03 :B_B	信息摘要
							msg= Html.fromHtml(info.getJSONObject("B").getString("C"));
							text_view.setText(msg);
						}
						newintent.putExtra("window_id","1003");			
					}
					newintent.putExtras(newbundle);
				}
				if(!info.getJSONObject("B").isNull("Z") ){
					leftbtnStr=info.getJSONObject("B").getString("Z");
				}
		
			    initViewText();
			    show();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private void toCreateShow() {
		try {
            String[] cloumsStr={"B","info"}; 
            Cursor result=db.query("receiver_info", cloumsStr, null,null,null,null,null);
            System.out.println("info------Count :"+result.getColumnCount());
            jsonInfo.clear();
            while (result.moveToNext()){
				int tempInt=result.getInt(result.getColumnIndex(cloumsStr[0]));
				String infoStr=result.getString(result.getColumnIndex(cloumsStr[1]));
				if(0==tempInt){
					JSONObject info=new JSONObject(infoStr);
					jsonInfo.add(0, info);
				}
				db.execSQL("delete from receiver_info  where info='"+infoStr+"';");
			}
            
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.lift_btn:
			 try {
				 removeAndDBdelete();
				 String window_idstr = newintent.getStringExtra("window_id");
				 if(!AppUtil.isEmpty(window_idstr)){
					 int window_id = Integer.parseInt(window_idstr);						 
					 if(window_id==0) return ;
					 MyApp.backToTCActivity();
					 newintent.putExtra("window_id", "0");
					 CustomTagEnum.startActivityWithId((BaseActivity)MyApp.activityList.get(0),newbundle,window_id);
				 }
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case R.id.dialog_title_iv_right:
			dismiss();
			break;
		case R.id.reight_btn:
			try {
				removeAndDBdelete();
				if("取    消".equals(reight_btn.getText())){
					dismiss();
				}else{
					if(jsonInfo.size() >= 1){
						showReceiverDialog();
					}else{
						dismiss();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		 }
	}
	private void removeAndDBdelete() throws JSONException{
		 if(jsonInfo.size() >= 1){
			 jsonInfo.remove(0);
		 }
	}
	

}
