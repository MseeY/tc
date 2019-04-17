package com.mitenotc.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mitenotc.tc.R;
import com.mitenotc.ui.adapter.JCListAdapter;
import com.mitenotc.ui.ui_utils.JCListView;
import com.mitenotc.ui.ui_utils.PublicMethod;

import android.app.Activity;



/**
 * 对阵列表父类
 * @author fl
 *
 */
public  class BaseTeamList {
	private final int TEAM_MAX = 3;
    protected Activity baseActivity;
    public List<JcTeamInfo> listInfos;//总对阵列表
    protected List<JcTeamInfo> listInfosCheck;//筛选后对阵列表
    public JCListView exList;
    public JCListAdapter adapter;
    public BaseTeamList(Activity baseActivity,JCListView exList){
    	this.baseActivity = baseActivity;
    	this.exList = exList;
    }
    public void initListInfo(List<JcTeamInfo> listInfos){
    	this.listInfos = listInfos;
    	this.listInfosCheck = null;
    	this.listInfos = listInfos;
    	this.listInfosCheck = listInfos;
    }
	private List<List<JcTeamInfo>> turnListInfo(List<JcTeamInfo> listInfos){
		List<List<JcTeamInfo>> listInfoList = new ArrayList<List<JcTeamInfo>>();
		String lotttime ="";
		List listInfo = null;
		for(int i=0;i<listInfos.size();i++){
			String lotttimeNew = listInfos.get(i).getLotttime();
			if(!lotttime.equals(lotttimeNew)){
				lotttime = lotttimeNew;
				if(listInfo!=null){
					listInfoList.add(listInfo);
				}
				listInfo = new ArrayList<JcTeamInfo>();
				listInfo.add(listInfos.get(i));
			}else{
				listInfo.add(listInfos.get(i));
			}
			if(i==listInfos.size()-1){
				listInfoList.add(listInfo);
			}
		}
		return listInfoList;
	}

	public void initListView() {
		List<List<JcTeamInfo>> listInfos = turnListInfo(this.listInfosCheck);
        List<Map<String, String>> groups = new ArrayList<Map<String, String>>();
        List<List<Map<String, String>>> childs = new ArrayList<List<Map<String, String>>>();   
        for (int i = 0; i < listInfos.size(); i++) {
            Map<String, String> group = new HashMap<String, String>();
            String timeDate = PublicMethod.formatDateDT(listInfos.get(i).get(0).getLotttime());
            String weekStr = PublicMethod.formatWeek(listInfos.get(i).get(0).getLottweek());
            int size = listInfos.get(i).size();
            group.put("g",timeDate + "  "+weekStr+"  共"+size+"场比赛");  
            groups.add(group);  			
            List<Map<String, String>> child = new ArrayList<Map<String, String>>();  
            for (int j = 0; j < listInfos.get(i).size(); j++) { 
                Map<String, String> childdata = new HashMap<String, String>();
                String tameName = PublicMethod.maxStrLength(listInfos.get(i).get(j).getGamename(), TEAM_MAX);
                String time = (j+1)+"-"+formatGameTime(listInfos.get(i).get(j).getGameendtime());
                childdata.put("c",tameName+"\n"+time);
                child.add(childdata);
			}
            childs.add(child);
        }
        exList.clearChoices();
    	exList.setHeaderView(baseActivity.getLayoutInflater().inflate(R.layout.group_header,null));
    	adapter = initAdapter(listInfos, groups, childs);   
        exList.setAdapter(adapter);
        for(int i = 0; i < adapter.getGroupCount(); i++){  
        	exList.expandGroup(i);  
        } 
	}
	public  JCListAdapter initAdapter(List<List<JcTeamInfo>> listInfos,
			List<Map<String, String>> groups,
			List<List<Map<String, String>>> childs){
//		JCListAdapter adapter = new JCListAdapter(   
//				baseActivity,exList, groups, R.layout.group, new String[] { "g" },   
//                new int[] { R.id.groupto }, childs, R.layout.child,   
//                new String[] { "c" }, new int[] { R.id.childto },listInfos);
		
		return adapter;
		
	}
	
	/**
	 * 清空选中状态
	 */
	public void clearList(){
		if(listInfos!=null){
			for(int i=0;i<listInfos.size();i++){
				listInfos.get(i).initBtnState();
			}
			adapter.notifyDataSetChanged();
		}
	}
	/**
	 * 有几个队被选中
	 */
	public int checkTeamNum(){
		int teamNum = 0;
		if(listInfos!=null){
			for(int i=0;i<listInfos.size();i++){
				int num = listInfos.get(i).itemCheckNum();
				if(num>0){
					teamNum++;
				}
			}
		}
		return teamNum;
	}
	public String formatGameTime(String gameendtime){
		String formatStr = "";
		try{
			 String newTime[] = gameendtime.split(" ");
			 int length0 = newTime[0].length();
			 formatStr = newTime[1];
		}catch (Exception e) {
			// TODO: handle exception
			formatStr = gameendtime;
		}
		return formatStr;
		
	}
	
}
