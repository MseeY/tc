package com.mitenotc.ui.adapter;

import com.mitenotc.bean.AnyCHnineBean;
import com.mitenotc.tc.R;
import com.mitenotc.ui.ui_utils.CustomButton;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * 胜负彩任9场
 * 
 * 适配器
 * @author wanli
 *
 */
public class AnyCHnineAdapter extends BaseAdapter {
    private Context context;
    private boolean isShow ;
    private LayoutInflater mInflater;
	
	public AnyCHnineAdapter(Context context,boolean isShow) {
		super();
		this.context = context;
		this.isShow=isShow;
		mInflater=LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		
		return AnyCHnineBean.getInstance().getScaleMap().size();
	}

	@Override
	public Object getItem(int position) {
		return AnyCHnineBean.getInstance();
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
			     VHolder vh;
			     AnyCHnineBean anychBean=AnyCHnineBean.getInstance();
			 
				 if(convertView == null){
					 vh=new VHolder();
					 convertView=mInflater.inflate(R.layout.m_lv_any9_item, null);
					 vh.vs_left_tv=(TextView) convertView.findViewById(R.id.vs_left_tv);
					 vh.vs_right_tv=(TextView) convertView.findViewById(R.id.vs_right_tv);
					 vh.cb_three=(CustomButton) convertView.findViewById(R.id.cb_three);
					 vh.cb_one=(CustomButton) convertView.findViewById(R.id.cb_one);
					 vh.cb_zero=(CustomButton) convertView.findViewById(R.id.cb_zero);
					 convertView.setTag(vh);
				 }else{
					 vh = (VHolder) convertView.getTag();
				 }
				 if(anychBean!=null){
					    String leftname=anychBean.getVSStringlist().get(0).get(position);
						String reightname=anychBean.getVSStringlist().get(1).get(position); 
						String str3=anychBean.getScaleMap().get("2").get(position);
						String str1=anychBean.getScaleMap().get("1").get(position);
						String str0=anychBean.getScaleMap().get("0").get(position);
						
						// ----- TODO 等待后期抽取
						vh.cb_three.setTV1ViewResource("3", str3);
						vh.cb_one.setTV1ViewResource("1", str1);
						vh.cb_zero.setTV1ViewResource("0", str0);
						//是否显示百分比
						if(isShow){
							vh.cb_three.setIsshowratioTV(true);
							vh.cb_one.setIsshowratioTV(true);
							vh.cb_zero.setIsshowratioTV(true);
						}else{
							vh.cb_three.setIsshowratioTV(false);
							vh.cb_one.setIsshowratioTV(false);
							vh.cb_zero.setIsshowratioTV(false);
						}
						vh.vs_left_tv.setText(leftname);
						vh.vs_right_tv.setText(reightname); 
				 }
				
		 
		 
		 
		return convertView;
	}
	//先定义 ViewHilder静态类 可以重用  
	public static class VHolder{
		public TextView vs_left_tv;// vs左边tv
		public TextView vs_right_tv;// vs右边tv
		public CustomButton cb_three;// 3 
		public CustomButton cb_one;// 1 
		public CustomButton cb_zero;// 0 
	}

}
