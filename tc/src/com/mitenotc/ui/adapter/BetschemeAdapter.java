package com.mitenotc.ui.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mitenotc.bean.RATEBean;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.ui.play.IntelligentScheme;
import com.mitenotc.ui.ui_utils.AddSelfEditText;
import com.mitenotc.ui.ui_utils.AddSelfEditText.ActionUpListener;
import com.mitenotc.ui.ui_utils.AddSelfEditText.AddTextChangedListener;
import com.mitenotc.utils.BaseCalc;
/**
 * 智能追号投注列表
 * @author wanli
 *
 */
public class BetschemeAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    //期数对应 倍数 追期一一对应的投注倍数
    public  List<String> ALL_BS_LIST;
	
  //期数            根据修改页决定的最大追期数 决定所有的销售期号
    public  List<String> ALL_QH_LIST;
   //期数对应的后三项计算结果        累计投入 中奖盈利  盈利率
    private Map<String, List<String>> calc_msg_map ;
    private static  boolean IS_OPEN_LISTEN=false;
    
    private IntelligentScheme illScheme=null;
 
	public BetschemeAdapter(Context context,IntelligentScheme scheme) {
		super();
		mInflater=LayoutInflater.from(context);
		illScheme = scheme;
	}
	
	public BetschemeAdapter(Context context) {
		super();
		mInflater=LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		if(RATEBean.getInstance().getCALC_MAP() != null)
		{
			return RATEBean.getInstance().getCALC_MAP().size();//即最大序号
		}else{
			
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		
		return RATEBean.getInstance().getCALC_MAP();//返回期号
	}
	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView( int position, View convertView, ViewGroup viewGroup) {
		    ViewHolder vh;
			List<String> list=null;
			ALL_BS_LIST=RATEBean.getInstance().getALL_BS_LIST();
			ALL_QH_LIST=RATEBean.getInstance().getALL_QH_LIST();
				if(convertView==null){
						vh=new ViewHolder();
						convertView=mInflater.inflate(R.layout.bet_scheme_lv_item, null);
						vh.serial_number_tv=(TextView) convertView.findViewById(R.id.serial_number_tv);
						vh.issue_tv=(TextView) convertView.findViewById(R.id.issue_tv);
						vh.diY_multiple_ed=(AddSelfEditText) convertView.findViewById(R.id.diY_multiple_ed);
						vh.addup_putinto_tv=(TextView) convertView.findViewById(R.id.addup_putinto_tv);
						vh.zjyl_tv=(TextView) convertView.findViewById(R.id.zjyl_tv);
						vh.yll_tv=(TextView) convertView.findViewById(R.id.yll_tv);					     
						convertView.setTag(vh);
					}else{
				        vh=(ViewHolder) convertView.getTag();
					}
					
					vh.serial_number_tv.setText(String.valueOf(position+1));//序号
					if(ALL_QH_LIST.get(position)!=null){
						vh.issue_tv.setText(ALL_QH_LIST.get(position).substring(ALL_QH_LIST.get(position).length()-2));
					}
					
					list=RATEBean.getInstance().getCALC_MAP().get(String.valueOf(position));//根据期号拿出这一期号所对应的计算出来的  累计 盈利..					
					if(list != null && list.size()==4)
					{
						vh.diY_multiple_ed.setText(list.get(3));//初始化默认 为2 后期必须为上一次设置传递的值 
						vh.addup_putinto_tv.setText(list.get(0));
						vh.zjyl_tv.setText(list.get(1));
						vh.yll_tv.setText(list.get(2));//必须为计算后的值
					}else
					{
						vh.diY_multiple_ed.setText("1");
						vh.addup_putinto_tv.setText("--");
						vh.zjyl_tv.setText("--");
						vh.yll_tv.setText("--");//必须为计算后的值
					}
					
					addListen(vh,position);//加减按钮
					
			return convertView;
	}


   private void addListen(final ViewHolder vh,final int position) {
//		添加监听
			vh.diY_multiple_ed.setActionUpListener(new ActionUpListener() 
			{
						@Override
						public void onActionUP() {
							
							String money="";
							String yllStr="";
							 if("".equals(vh.diY_multiple_ed.getText())
									||"0".equals(vh.diY_multiple_ed.getText())){
								RATEBean.getInstance().getALL_BS_LIST().set(position, "1");
							}else 
							{								
								RATEBean.getInstance().getALL_BS_LIST().set(position, vh.diY_multiple_ed.getText().toString());
							}
							ALL_BS_LIST=RATEBean.getInstance().getALL_BS_LIST();
							
							Map<String, String> bsMap=new HashMap<String, String>();
							for (int i = 0; i < ALL_QH_LIST.size(); i++) {
								bsMap.put(String.valueOf(i), ALL_BS_LIST.get(i));
							}
							
							calc_msg_map=BaseCalc.jszqxxxgbs(bsMap,
															RATEBean.getInstance().getLotteryCount()+"",
															RATEBean.getInstance().getBonusScopeList());
							int qhl_size= ALL_QH_LIST.size();//Integer.parseInt(RATEBean.getInstance().getDEFAULT_XH());
							ALL_BS_LIST.clear();
				    		for (int i = 0; i <qhl_size ; i++) 
				    		{
				    			ALL_BS_LIST.add(i, calc_msg_map.get(String.valueOf(i)).get(3));
				    			if(qhl_size == i+1)
				    			{
					    				money =  calc_msg_map.get(String.valueOf(i)).get(0);
					    				yllStr=calc_msg_map.get(String.valueOf(i)).get(2);
					    				if(yllStr.contains("至")){
					    					////System.out.println("111"+yllStr);
					    					yllStr=yllStr.substring(yllStr.indexOf("至")+1, yllStr.length());
					    					////System.out.println("111"+yllStr);
					    				}else if(yllStr.contains("%")){
					    					 ////System.out.println("111"+yllStr);
					    					 yllStr=yllStr.replace("%", "");
					    					 ////System.out.println("111"+yllStr);
					    				}
				    			}
				    		}
				    		
							RATEBean.getInstance().setONE_YLL(yllStr);//盈利率
							
				    		RATEBean.getInstance().setALL_BS_LIST(ALL_BS_LIST);
				    		RATEBean.getInstance().setCALC_MAP(calc_msg_map);
				    		illScheme.setNoticeText(money);
				    		if(position >=7){//超过一屏幕
				    			IntelligentScheme.getBet_scheme_lv().setSelection(position);//刷新数据不发生item 错位现象
				    		}
							notifyDataSetInvalidated();
						}
						
			});
//			文本变化
			vh.diY_multiple_ed.setAddtextChengeListener(new AddTextChangedListener() {
				private int temPosition=position; 
				@Override
				public void textChanged(CharSequence s, int start, int before, int count) {
						String money="";
						String yllStr="";
						////System.out.println("111==="+vh.diY_multiple_ed.getText());
						if("".equals(vh.diY_multiple_ed.getText())
								||"0".equals(vh.diY_multiple_ed.getText())){
							RATEBean.getInstance().getALL_BS_LIST().set(temPosition, "1");
						}else 
						{								
							RATEBean.getInstance().getALL_BS_LIST().set(temPosition, vh.diY_multiple_ed.getText().toString());
						}
						ALL_BS_LIST=RATEBean.getInstance().getALL_BS_LIST();
						
						Map<String, String> bsMap=new HashMap<String, String>();
						for (int i = 0; i < ALL_QH_LIST.size(); i++) {
							bsMap.put(String.valueOf(i), ALL_BS_LIST.get(i));
						}
						
						calc_msg_map=BaseCalc.jszqxxxgbs(bsMap,
														RATEBean.getInstance().getLotteryCount()+"",
														RATEBean.getInstance().getBonusScopeList());
						int qhl_size= ALL_QH_LIST.size();
						ALL_BS_LIST.clear();
			    		for (int i = 0; i <qhl_size ; i++) 
			    		{
			    			ALL_BS_LIST.add(i, calc_msg_map.get(String.valueOf(i)).get(3));
			    			if(qhl_size == i+1)
			    				{
			    				money =  calc_msg_map.get(String.valueOf(i)).get(0);
			    				yllStr=calc_msg_map.get(String.valueOf(i)).get(2);
			    				if(yllStr.contains("至")){
			    					yllStr=yllStr.substring(yllStr.indexOf("至")+1, yllStr.length());
			    				}else if(yllStr.contains("%")){
			    					 yllStr=yllStr.replace("%", "");
			    				    }
			    				}
			    		}
			    		RATEBean.getInstance().setONE_YLL(yllStr);//盈利率	
			    		
			    		RATEBean.getInstance().setDEFAULT_XH(String.valueOf(ALL_QH_LIST.size()));
			    		RATEBean.getInstance().setALL_QH_LIST(ALL_QH_LIST);
			    		RATEBean.getInstance().setALL_BS_LIST(ALL_BS_LIST);
			    		RATEBean.getInstance().setCALC_MAP(calc_msg_map);
			    		
			    		illScheme.setNoticeText(money);
			    		
			    		if(position >=7){//超过一屏幕
			    			IntelligentScheme.getBet_scheme_lv().setSelection(position);//刷新数据不发生item 错位现象
			    		}
			    		notifyDataSetInvalidated();
				}
					
			});
			
			
	}

   class ViewHolder {
		    public TextView serial_number_tv;// 序号
			public TextView issue_tv;//期号
			public AddSelfEditText diY_multiple_ed;//倍数
			public TextView addup_putinto_tv;//累计投入
			public TextView zjyl_tv;//中奖盈利
			public TextView yll_tv;//盈利率
			
	}
   

}
