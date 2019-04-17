package com.mitenotc.ui.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mitenotc.bean.MessageBean;
import com.mitenotc.tc.R;
import com.mitenotc.utils.LogUtil;

import android.content.Context;
import android.text.Html;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 *  历史奖池信息
 * @author wanli
 *
 */
public class HistoryPondAdapter extends BaseAdapter {
	private List<MessageBean> msgBL;//请求成功的list
	private MessageBean msgB;
 	
	private String className="com.mitenotc.ui.play.PL119";
    private LayoutInflater mInflater;
    
	
	
	public HistoryPondAdapter(Context ctx,List<MessageBean> msgBL,String calssName) {
		super();
		this.msgBL=msgBL;
		this.className=calssName;
		mInflater=LayoutInflater.from(ctx);
	}

	@Override
	public int getCount() {
		return msgBL.size();
	}

	@Override
	public Object getItem(int position) {
		return msgBL.get(position);
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Viewholder vh = null;
		ViewTwoholder v2h = null;
		String H;//红球
		String L;//篮球
		Spannable textsp;
//		  if("com.mitenotc.ui.play.PL119".endsWith(className)){
//			  if(convertView == null ){
//					vh=new Viewholder();
//					convertView=mInflater.inflate(R.layout.m_lv_history_msg_item, null);
//					vh.tv1=(TextView) convertView.findViewById(R.id.it_periods_tv);
//					vh.tv2=(TextView) convertView.findViewById(R.id.it_history_number_tv);
//					convertView.setTag(vh);
//					
//				}else{
//					vh=(Viewholder) convertView.getTag();
//				}
//			  
//			  
//		  }else {
			  if(convertView == null ){
					vh=new Viewholder();
					convertView=mInflater.inflate(R.layout.m_lv_history_msg_item, null);
					vh.tv1=(TextView) convertView.findViewById(R.id.it_periods_tv);
					vh.tv2=(TextView) convertView.findViewById(R.id.it_history_number_tv);
					convertView.setTag(vh);
					
				}else{
					vh=(Viewholder) convertView.getTag();
				}
				msgB=msgBL.get(position);
				if(msgB!=null){
					vh.tv1.setText(msgB.getC());//历史期号
					H=msgB.getE().substring(0,msgB.getE().length()-2).replace(",", " ").replace("+", " ");
					L=msgB.getE().substring(msgB.getE().length()-2,msgB.getE().length());
					textsp=(Spannable) Html.fromHtml("<font color=#ff0000>"+H+"</font>"+
			                "<font color=#007aff>"+L+"</font>");
					vh.tv2.setText(textsp);//历史期号对应的历史投注号码
				} 
//		  }
		
			
			
		return convertView;
	}
	
	
   static class Viewholder{
	   
	   TextView tv1;//历史期数
	   TextView tv2;//历史号码
	   
	   
   }
   
//   时时彩  历史背景中号码展示的item 
   static class  ViewTwoholder{
	   
   }
}
