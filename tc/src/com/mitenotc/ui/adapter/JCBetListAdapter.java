package com.mitenotc.ui.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.mitenotc.bean.MessageBean;
import com.mitenotc.bean.TicketBean;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.ui.ui_utils.CheckboxGroup;
import com.mitenotc.ui.ui_utils.CheckboxGroup.OnChbxGpOnCheckedListener;

public class JCBetListAdapter extends BaseAdapter {
//	private  MessageBean tkMessageBean;//单注
//	private   TicketBean tkBean;
    private  LayoutInflater mInflater;
//    
	public JCBetListAdapter(Context ctx) {
		super();
		mInflater=LayoutInflater.from(ctx);
	}

	@Override
	public int getCount() {
		return MyApp.order.getSceneNumber();//所选所有场次数量和
	}

	@Override
	public Object getItem(int position) {
		return MyApp.order.getMessageBean(position);
	}

	@Override
	public long getItemId(int id) {
		return id;
	}
    
	
	@Override
	public View getView( final int position, View convertView, ViewGroup parent) {
////			ViewHolder vh;
////                if(convertView == null){
//				    ViewHolder vh=new ViewHolder();
//					convertView=mInflater.inflate(R.layout.jc_bet_item, null);
//					vh.delete_btn=(Button) convertView.findViewById(R.id.delete_btn);
//					vh.item_chgp=(CheckboxGroup) convertView.findViewById(R.id.item_chgp);
//					vh.cb3=(CheckBox) convertView.findViewById(R.id.cb3);
//					vh.cb1=(CheckBox) convertView.findViewById(R.id.cb1);
//					vh.cb0=(CheckBox) convertView.findViewById(R.id.cb0);
//					vh.invariant_chbox=(CheckBox) convertView.findViewById(R.id.invariant_chbox);
////					convertView.setTag(vh);
////				}
////				vh=(ViewHolder)convertView.getTag();
//				tkMessageBean=MyApp.order.getMessageBean(position);
////				tkBean=MyApp.order.getJCTicketBean(tkMessageBean);
//				final String pc_dz=tkMessageBean.getC()+"-"+tkMessageBean.getG();//C	批次时间-G	对阵编号	(每批次下的场次)	
//			   // 模拟sp 值
//				vh.cb3.setText(tkMessageBean.getK()+"\n主胜0.00");//K   主队名称
//			    vh.cb1.setText("vs\n平0.00");//vs
//			    vh.cb0.setText(tkMessageBean.getM()+"\n客胜0.00");//M   客队名称
//			    
//			    vh.cb3.setTag("3_0.00");
//			    vh.cb1.setTag("1_0.00");
//			    vh.cb0.setTag("0_0.00");
//			    List<String> tempList = MyApp.order.getTagList(pc_dz);
//				if(tempList!=null){
//					 for (int i = 0; i < tempList.size(); i++) {
//						 String s=tempList.get(i);
//						 if(s.contains("_")){
//							 String[] t=s.split("_");
//							 //胜
//							 if("3".equals(t[0])){
//								 vh.cb3.setChecked(true);
//							 }
//							 //平
//							 if("1".equals(t[0])){
//								 vh.cb1.setChecked(true);
//							 }
//							 //负
//							 if("0".equals(t[0])){
//								 vh.cb0.setChecked(true);
//							 }
//						 }
//					}
//					 vh.item_chgp.setTagList(tempList);
//				 }
//				//删除
//				vh.delete_btn.setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View v) {//删除
//						MyApp.order.removeMessageBean(tkMessageBean);//直接在OrderBean中修改ticket
//						notifyDataSetChanged();
//					}
//				});
//				//修改
//				vh.item_chgp.setChbxGpOnCheckedListener(new OnChbxGpOnCheckedListener() {
//					@Override
//					public void setOnChildCheckedListener(View v, List<String> tagList,List<String> textList) {
//							MyApp.order.updatetkMessageBean(tkMessageBean,pc_dz,tagList);//直接在OrderBean中修改ticket
//							MyApp.order.getAmount();
//					}
//				});
//				//胆拖
//				vh.invariant_chbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//					@Override
//					public void onCheckedChanged(CompoundButton chbtn, boolean isChecked) {
//						MyApp.order.setMessageBeanInvariant(tkMessageBean ,isChecked);//直接在OrderBean中修改ticket
//					}
//				});
		return convertView;
	}
//	//静态类   复用
//	public static class ViewHolder{
//		 Button delete_btn;
//		 CheckboxGroup item_chgp;
//		 CheckBox cb3;//胜
//		 CheckBox cb1;//vs
//		 CheckBox cb0;//平
//		 CheckBox invariant_chbox;//胆
//	}
}
