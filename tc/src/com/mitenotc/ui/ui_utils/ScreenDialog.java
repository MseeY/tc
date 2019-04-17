package com.mitenotc.ui.ui_utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mitenotc.tc.R;
import com.mitenotc.ui.ui_utils.custom_ui.Custom_jcSession;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
/**
 * 竞彩五大赛事筛选 
 * @author mitenotc
 */
public class ScreenDialog extends Dialog  {
    private Context ctx;
    private GridRadioGroup sprg;//sp 筛选
    private RadioButton sp0;
    private RadioButton sp1;
    private RadioButton sp2;
    private RadioButton r1;
    private RadioButton r2;
    private RadioButton r3;
    private GridRadioGroup grp;
    private MGridView game_mv;
    private Map<String,List<String>> gameNameMap;
	private  List<Integer> lsList;
	private  List<Integer> FXList;//反选

	private float sp=0;//默认是所有

	public ScreenDialog(Context context) {
		super(context, R.style.dialog_theme);
		ctx=context;
		init();
	}
	
	public ScreenDialog(Context context,String str) {
		super(context, R.style.dialog_theme);
		ctx=context;
		init();
		r3.setVisibility(View.GONE);
	}
	
	public ScreenDialog(Context context, int theme) {
		super(context, theme);
		ctx=context;
		setCanceledOnTouchOutside(false);//触动了窗外的界限不能取消界面
		init();
	}
	private void init() {
		setContentView(R.layout.screendialog_layout);
		sprg = (GridRadioGroup) findViewById(R.id.sprg);
		sp0 = (RadioButton) findViewById(R.id.sp0);
		sp1 = (RadioButton) findViewById(R.id.sp1);
		sp2 = (RadioButton) findViewById(R.id.sp2);
		r1 = (RadioButton) findViewById(R.id.r1);
		r2 = (RadioButton) findViewById(R.id.r2);
		r3 = (RadioButton) findViewById(R.id.r3);
		grp = (GridRadioGroup) findViewById(R.id.grp);
		game_mv = (MGridView) findViewById(R.id.game_mv);//显示4002返回赛事名称
		sprg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.sp0:
					sp=(float) 0;
					break;
				case R.id.sp1:
					sp=(float) 1.5;
					break;
				case R.id.sp2:
					sp=(float) 2.0;
					break;
				}
				game_mv.notifyDataSetChanged();
			}
		});
//		game_mv.setActionUpListener(new ActionUpListener() {
//			@Override
//			public void onActionUp() {
//				List<Integer>  list=game_mv.getSelectedBalls();
//				game_mv.getSelectedText();
//				String s=game_mv.getSelectedTextToString();
//				System.out.println("210---game_mv -->s "+s+" list ["+list.toString()+"]");
//			}
//		});
		grp.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.r1://全部
						game_mv.getSelectedBalls().clear();
						if(r1.isChecked()){
							for (int i : idList) {
								game_mv.getSelectedBalls().add(i);
								System.out.println("210------>"+i);
							}
						}
					break;
				case R.id.r2://反选
						List<Integer> tList=game_mv.getSelectedBalls();
						List<Integer> list=new ArrayList<Integer>();
						if(tList!=null  && tList.size() >0){
							for (int j = 0; j < idList.size(); j++) {
								if(-1==tList.indexOf(idList.get(j))){
										list.add(idList.get(j));
								}
							}
							game_mv.getSelectedBalls().clear();
							if(list.size() > 0){
								for (int  i : list) {
									game_mv.getSelectedBalls().add(i);
								}
							}
						}else {
							game_mv.getSelectedBalls().clear();
							for (int i : idList) {
								game_mv.getSelectedBalls().add(i);
							}
						}
//						if(tList!=null  && tList.size() >0){
//							for (int j = 0; j < tList.size(); j++) {
//								if(idList.contains(tList.get(j))){
//									list.add(tList.get(j));
//								}
////								for (int i = 0; i < idList.size(); i++) {
////									if(tList.get(j) != idList.get(i)){
////										list.add(idList.get(i));
////									}
////								}
//							}
//							game_mv.getSelectedBalls().clear();
//							if(list.size() > 0){
//								for (int  i : list) {
//									game_mv.getSelectedBalls().add(i);
//								}
//							}
//						}else {
//							game_mv.getSelectedBalls().clear();
//							for (int i : idList) {
//								game_mv.getSelectedBalls().add(i);
//							}
//						}
					break;
				case R.id.r3://五大联赛
					    game_mv.getSelectedBalls().clear();
						for (int i : lsList) {
							game_mv.getSelectedBalls().add(i);
						}
					break;
				}
				String s=game_mv.getSelectedTextToString();
				System.out.println("210---end---> s "+s+ "list "+game_mv.getSelectedText().toString());
				for (int j = 0; j < game_mv.getSelectedBalls().size(); j++) {
					System.out.println("s======id=====>"+game_mv.getSelectedBalls().get(j));
					if(textList.size() > game_mv.getSelectedBalls().get(j)){
						System.out.println("s=====text======>"+textList.get(game_mv.getSelectedBalls().get(j)));
					}
				}
				game_mv.notifyDataSetChanged();
			}
		});
	}
	/**
	 * 提供外部设置监听
	 * @param cancel_btn_Listener
	 * @param affirm_btn_Listener
	 */
	public void addListener(Map<String,List<String>>  map,View.OnClickListener cancel_btn_Listener,
			View.OnClickListener affirm_btn_Listener){
		if(cancel_btn_Listener!=null){
			findViewById(R.id.cancel_btn).setOnClickListener(cancel_btn_Listener);//取消
		}
		if(affirm_btn_Listener!=null){
			findViewById(R.id.affirm_btn).setOnClickListener(affirm_btn_Listener);//确认
		}
		setGameNameMap(map);
		show();
	}
//	private void clearRbtn(){
//		r1.setChecked(false);
//		r2.setChecked(false);
//		r3.setChecked(false);
//		sp0.setChecked(false);
//		sp1.setChecked(false);
//		sp2.setChecked(false);
//	}
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.cancel_btn:// 取消
//			cancel();
//			break;
//		case R.id.affirm_btn:// 确认
//			JCPL210.setIS_AFFIRM(true);
//			JCPL210.setSlist(game_mv.getSelectedText());
//			cancel();
//			break;
//		}
//		
//	}
	public List<String> getSelectListName(){//获得筛选
		List<String> rt=new ArrayList<String>();
		if(textList!=null){
			for (int j = 0; j < game_mv.getSelectedBalls().size(); j++) {
				if(textList.size() > game_mv.getSelectedBalls().get(j)){
					rt.add(textList.get(game_mv.getSelectedBalls().get(j)));
				}
			}
		}
		return rt;
	}
	//获得选中id
	public List<Integer> getSelectId(){//获得筛选
		List<Integer> selectid=new ArrayList<Integer>();
		if(game_mv.getSelectedBalls()!=null ){
			for (int j = 0; j < game_mv.getSelectedBalls().size(); j++) {
				selectid.add(game_mv.getSelectedBalls().get(j));
			}
		}
		return selectid;
	}
	/**
	 * 取消
	 * @param list
	 * @param sp
	 * @param chid 
	 */
	public void setSelectById(List<Integer> list,float sp, int chid){
		game_mv.getSelectedBalls().clear();
		if(list!=null){
			for (int i : list) {
				game_mv.getSelectedBalls().add(i);
			}
		}else if(idList!=null){
			  for (int i : idList) {//全选
					game_mv.getSelectedBalls().add(i);
			  }
		}
		this.sp=sp;
		sprg.setCurrentCheckedId(chid);
	}
	/**
	 *  SP筛选结果 选中id
	 * @return
	 */
	public int getSpCheckid(){
		return sprg.getCheckedId();
	}
	public float getSp() {
		return sp;
	}
	private  List<Integer> idList; 
	private  List<String> textList;
	private  List<View> cvList;
	private  Custom_jcSession ctv;
	public void setGameNameMap(Map<String, List<String>> map) {
		this.gameNameMap = map;
		if(gameNameMap!=null){
			System.out.println("210------>gameNameMap"+gameNameMap.toString());
			if(textList==null){
				textList=new ArrayList<String>(); 
			}else{
				textList.clear();
			}
			if(cvList==null){
				cvList=new ArrayList<View>();
			}else{
				cvList.clear();
			}
			if(idList==null){
				idList=new ArrayList<Integer>();
			}else{
				idList.clear();
			}
			if(lsList==null){
				lsList=new ArrayList<Integer>();//五大联赛下标
			}else{
				lsList.clear();
			}
//			W	是否是五大联赛（0否1是）gameNameMap 不能采用循环
			if(gameNameMap.containsKey("1")&& gameNameMap.get("1").size() > 0){
				for (int j = 0; j < gameNameMap.get("1").size(); j++) {
					textList.add(gameNameMap.get("1").get(j));
					lsList.add(j);//标记五大联赛下标
				}
			}
			if(gameNameMap.containsKey("0")&& gameNameMap.get("0").size() > 0){
				for (int j = 0; j < gameNameMap.get("0").size(); j++) {
					textList.add(gameNameMap.get("0").get(j));
				}
			}
			if(textList.size() > 0 && game_mv!=null){
				for (int i = 0; i < textList.size(); i++) {
					ctv=new Custom_jcSession(ctx,String.valueOf(i),textList.get(i));
					cvList.add(ctv);
					idList.add(i);
				}
				game_mv.simpleInit(false, idList, idList.size(), cvList);
				game_mv.setMaxNum(0);
			}
			if(game_mv.getSelectedBalls()==null || game_mv.getSelectedBalls().size()== 0){
			  defaultConfiguration();//默认配置
			}
		}
	}
	/**
	 * 默认配置
	 */
	public void defaultConfiguration(){
		sp0.setChecked(true);//所有赔率
		r1.setChecked(true);//全选
		if(idList!=null){
		  for (int i : idList) {//全选
				game_mv.getSelectedBalls().add(i);
		  }
		}
		game_mv.notifyDataSetChanged();
	}
}
