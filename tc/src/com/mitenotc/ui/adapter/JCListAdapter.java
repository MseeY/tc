package com.mitenotc.ui.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.download.util.StringUtils;
import com.mitenotc.bean.JcTeamInfo;
import com.mitenotc.bean.MessageBean;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.ui.ui_utils.JCListView;
import com.mitenotc.ui.ui_utils.JCListView.JCHeaderAdapter;
import com.mitenotc.ui.ui_utils.MyToast;
import com.mitenotc.utils.AppUtil;
import com.mitenotc.utils.FormatUtil;

public class JCListAdapter extends BaseExpandableListAdapter implements JCHeaderAdapter {
    private int miniNum = 1;//该选区最少应该选中的个数
    
    protected List<List<MessageBean>> groupAllArray;
    protected  int SELECT_SP=0;
//    protected List<MessageBean> childArray=new ArrayList<MessageBean>();//场次数据
//    protected  Map<String, List<String>> tagMap=new HashMap<String, List<String>>();//筛选标志
    
    protected JCListView listView; 
    protected LayoutInflater inflater;
    protected Context ctx;
    
    //  标识是被点击下拉ChildView 的隐藏View的状态
//  private Map<String,List<String>> mapIsCheckId=new HashMap<String, List<String>>();
//    protected List<String> childisOpenList=new ArrayList<String>(); 
    protected Map<String,List<String>> lotteryTag=new HashMap<String, List<String>>();// 所选场次选票数据
	public Map<String, List<String>> getLotteryTag() {
		return lotteryTag;
	}
	public void setLotteryTag(Map<String, List<String>> lotteryTag) {
		this.lotteryTag = lotteryTag;
	}
	
	
	@SuppressLint("UseValueOf")
	public JCListAdapter(Context context,List<List<MessageBean>> groupArray,JCListView listView ) {
			super();
			this.listView  = listView; 
			this.inflater = LayoutInflater.from(context);
			this.ctx=context;
			if(groupArray!=null){
				for (int i = 0; i < groupArray.size(); i++) {
					Collections.sort(groupArray.get(i),new Comparator<MessageBean>() {//每组按照G	对阵编号 排序处理
						@Override
						public int compare(MessageBean mb0, MessageBean mb1) {
							Integer mInteger0=null, mInteger1=null;
							if(!StringUtils.isBlank(mb0.getG())){
								 mInteger0=new Integer(mb0.getG());
							}
							if(!StringUtils.isBlank(mb1.getG())){
								mInteger1=new Integer(mb1.getG());
							}
							if(mInteger0==null || mInteger1==null)return 0;
							return mInteger0.compareTo(mInteger1);
						}
					});
				}
				this.groupAllArray = groupArray;
			}
	}
	@Override
	public int getJCHeaderState(int groupPosition, int childPosition) {
		final int childCount = getChildrenCount(groupPosition);
		if(childPosition == childCount - 1){  
			return PINNED_HEADER_PUSHED_UP; 
		}
		else if(childPosition == -1 && !listView.isGroupExpanded(groupPosition)){ 
			return PINNED_HEADER_GONE; 
		}
		else{
			return PINNED_HEADER_VISIBLE;
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public void configureJCHeader(View header, int groupPosition,int childPosition, int alpha) {
//		System.out.println("125 --configureJCHeader---->"+getGroupClickStatus(groupPosition) +"--"+groupPosition);
		MessageBean mB=null;
		List<MessageBean> groupData = (List<MessageBean>) getGroup(groupPosition);
		if(groupData.size() >0) mB=groupData.get(0);
        if(getGroupClickStatus(groupPosition)== 0){
        	Drawable mDrawable_off=MyApp.res.getDrawable(R.drawable.drop_down_u);
        	mDrawable_off.setBounds(0, 0, mDrawable_off.getMinimumWidth(), mDrawable_off.getMinimumHeight());
        	((TextView) header.findViewById(R.id.jc_g_iv_img)).setCompoundDrawables(mDrawable_off, null, null, null);
        }else{
        	Drawable mDrawable_off=MyApp.res.getDrawable(R.drawable.drop_down_d);
        	mDrawable_off.setBounds(0, 0, mDrawable_off.getMinimumWidth(), mDrawable_off.getMinimumHeight());
        	((TextView) header.findViewById(R.id.jc_g_iv_img)).setCompoundDrawables(mDrawable_off, null, null, null);
        }
        int n= groupAllArray.get(groupPosition).size();//每组总可投场数
        if(mB!=null && !StringUtils.isBlank(mB.getD())){
            String c =mB.getC();//D	批次20141110
  	      ((TextView) header.findViewById(R.id.jc_g_iv_tv)).setText(FormatUtil.getDay(c)+String.valueOf(n)+"可投");
        }
	}
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return groupAllArray.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}
	@Override
	public View getChildView(final int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
 		  return convertView;
	}
	public View getGroupView(int groupPosition, boolean isExpanded,View convertView, ViewGroup parent) {
		GroupHolder groupHolder=null;
        if(convertView == null){
        	convertView = inflater.inflate(R.layout.jc_g_header_view, parent, false);
        	groupHolder=new GroupHolder(convertView);
            convertView.setTag(groupHolder);
        }
        groupHolder=(GroupHolder) convertView.getTag();
//		{"D":"7","E":"2014-11-09 09:00","F":"2014-11-09 00:40","G":"027","A":"210","B":"20000","C":"20141109","L":"-1","M":"芬洛","N":"1",
//		 "O":"","H":"荷兰乙级联赛","I":"2014-11-09 21:30","J":"2014-11-09 21:10","K":"奈梅亨","U":"0","T":"","Q":"","P":"","S":"","R":""} 
//        此方式已经放弃
//        String t =groupArray.get(groupPosition).get(0).getD();//D	批次时间的星期值  (针对同一批次的时间星期值相同)
//        int n= groupArray.get(groupPosition).size();
//        groupHolder.title.setText(getWeek(t)+String.valueOf(n)+"可投");
        
	      String c =groupAllArray.get(groupPosition).get(0).getC();//D	批次时间的星期值  (针对同一批次的时间星期值相同)
	      int n= groupAllArray.get(groupPosition).size();
	      groupHolder.title.setText(FormatUtil.getDay(c)+String.valueOf(n)+"可投");
       
      //设置展开标识
        Drawable mDrawable_off=MyApp.res.getDrawable(R.drawable.drop_down_u);
        mDrawable_off.setBounds(0, 0, mDrawable_off.getMinimumWidth(), mDrawable_off.getMinimumHeight());
        Drawable mDrawable_on=MyApp.res.getDrawable(R.drawable.drop_down_d);
        mDrawable_on.setBounds(0, 0, mDrawable_on.getMinimumWidth(), mDrawable_on.getMinimumHeight());
        groupHolder.gp_img.setCompoundDrawables(mDrawable_on, null, null, null);
        if(isExpanded){
        	groupHolder.gp_img.setCompoundDrawables(mDrawable_off, null, null, null);
        }
        return convertView;
	}
   /**
    * 识别日期
    * @param w
    * @return
    */
	@Deprecated
	public String getWeek(String w){
		String s="------";
		if(StringUtils.isBlank(w))return  s;
		switch ( Integer.parseInt(s)) {
		case 1:
			s="星期一";
			break;
		case 2:
			s="星期二";
			break;
		case 3:
			s="星期三";
			break;
		case 4:
			s="星期四";
			break;
		case 5:
			s="星期五";
			break;
		case 6:
			s="星期六";
			break;
		case 7:
			s="星期日";
			break;
		}
		return  s;
	}
	
	@Override
	public int getChildrenCount(int groupPosition) {
		return groupAllArray.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groupAllArray.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return groupAllArray.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}
	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
    
	class GroupHolder {
		TextView title;
		TextView gp_img;
        public GroupHolder(View convertView) {
        	 title  = (TextView) convertView.findViewById(R.id.jc_g_iv_tv);//D	批次时间的星期值
             gp_img = (TextView) convertView.findViewById(R.id.jc_g_iv_img);
        }
	}
//	public static class ChildHolder {
//		TextView title;
//		TextView ratio_show_tv;// 选中效果按钮箭头
//		CheckBox  ch3;
//		CheckBox  ch1;
//		CheckBox  ch0;
//		LinearLayout jc_btn;
//		LinearLayout item_item_layout;
//	}


	public HashMap<Integer,Integer> groupStatusMap = new HashMap<Integer, Integer>();
	@Override
	public void setGroupClickStatus(int groupPosition, int status) {
		groupStatusMap.put(groupPosition, status);
	}
	public HashMap<Integer, Integer> getGroupStatusMap() {
		return groupStatusMap;
	}
	@Override
	public int getGroupClickStatus(int groupPosition) {
		if(groupStatusMap.containsKey(groupPosition)){
			return groupStatusMap.get(groupPosition);
		}else{
			return 0;
		}
	}
	public void notifyList(){
		if(listView!=null){
			for(int i = 0; i < getGroupCount(); i++){  
				listView.expandGroup(i);
			} 
		}
	}
	public void clear(){
		if(groupAllArray!=null){
			groupAllArray.clear();
		}
		if(groupStatusMap!=null){
			groupStatusMap.clear();
		}
		notifyDataSetChanged();
	}

}