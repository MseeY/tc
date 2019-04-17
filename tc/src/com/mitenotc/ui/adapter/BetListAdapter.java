package com.mitenotc.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mitenotc.bean.OrderBean;
import com.mitenotc.bean.TicketBean;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.utils.Getlotterykind;

public class BetListAdapter extends BaseAdapter {
	private  OrderBean orderBean;//订单
	private  TicketBean ticketBean;//单注
    private  LayoutInflater mInflater;

//    private  MyitemOnClickListener mMyItemListener;
//    private  MyitemOnLongClickListener mMyItemlongListener;
    
    
    public BetListAdapter() {
		super();
		orderBean = MyApp.order;
	}

	public BetListAdapter(Context ctx,OrderBean orderBean) {
		super();
	    this.orderBean=orderBean;
		mInflater=LayoutInflater.from(ctx);
	}

	@Override
	public int getCount() {
		return orderBean.getTickets().size();
	}

	@Override
	public Object getItem(int position) {
		return orderBean.getTickets().get(position);
	}
	

	@Override
	public long getItemId(int id) {
		return id;
	}
    
	
	@Override
	public View getView( final int position, View convertView, ViewGroup parent) {
			ViewHolder vh;
    		String textStr="";
            if(convertView == null){
				   vh=new ViewHolder();
					convertView=mInflater.inflate(R.layout.m_lv_indent_item, null);
			
					vh.tv1=(TextView) convertView.findViewById(R.id.lv_bet_kind_item_tv);
					vh.tv2=(TextView) convertView.findViewById(R.id.lv_nu_item_tv);
					convertView.setTag(vh);
				}else{
					vh=(ViewHolder)convertView.getTag();
//					convertView.setOnClickListener(new View.OnClickListener() {
//						
//						@Override
//						public void onClick(View v) {
//							if(mMyItemListener!=null)
//								mMyItemListener.setMyListViewitemOnClickListener(position);
//							
//						}
//					});
//					
//					convertView.setOnLongClickListener(new View.OnLongClickListener() {
//						
//						@Override
//						public boolean onLongClick(View v) {
//							if(mMyItemlongListener!=null){
//								mMyItemlongListener.setMyListViewitemOnLongClickListener( position);
//							}
//							return false;
//						}
//					});
				}
				ticketBean=orderBean.getTickets().get(position);
				String key=ticketBean.getLotteryId()+"-"+ticketBean.getSalesType()+"-"+ticketBean.getChildId();
//				防止异常终止暂时----TODO
				try {
					textStr = Getlotterykind.get(key);
				} catch (Exception e) {
					e.printStackTrace();
				} 
				
	        	vh.tv2.setText(Html.fromHtml(ticketBean.showLotteryNums()));
	        	if(textStr!=null){
	        		if("122".equals(ticketBean.getLotteryId())||"130".equals(ticketBean.getLotteryId())){
	        			vh.tv1.setText(textStr+Html.fromHtml(ticketBean.getNotice1()));
	        		}else{
	        			vh.tv1.setText(textStr+Html.fromHtml(ticketBean.getNotice1()));//销售方式,投注方式
	        		}
	        	}else{
	        		vh.tv1.setText(Html.fromHtml(ticketBean.getNotice1()));//销售方式,投注方式
	        	}
		
		return convertView;
	}
	//先定义 ViewHilder静态类 可以重用  
	public static class ViewHolder{
		public TextView tv1;
		public TextView tv2;
	}
	
//	public interface MyitemOnClickListener {
//		
//		public void setMyListViewitemOnClickListener(int position);
//		
//	}
//	public interface MyitemOnLongClickListener {
//		public void setMyListViewitemOnLongClickListener(int position);
//		
//	}
//
//	public MyitemOnClickListener getmMyItemListener() {
//		return mMyItemListener;
//	}
//
//	public void setmMyItemListener(MyitemOnClickListener mMyItemListener) {
//		this.mMyItemListener = mMyItemListener;
//	}
//
//	public MyitemOnLongClickListener getmMyItemlongListener() {
//		return mMyItemlongListener;
//	}
//
//	public void setmMyItemlongListener(MyitemOnLongClickListener mMyItemlongListener) {
//		this.mMyItemlongListener = mMyItemlongListener;
//	}


	
	
}
