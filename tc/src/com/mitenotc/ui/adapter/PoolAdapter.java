package com.mitenotc.ui.adapter;

import java.text.DecimalFormat;
import java.util.List;

import com.mitenotc.tc.R;
import com.mitenotc.tc.R.color;


import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PoolAdapter extends BaseAdapter {
	
	private Context context;

	private int startNum;// 开始号码——福彩3D

	private int endNum;// 结束号码
	private List<Integer> selectNums;// 玩家已选号码
	
	private int resId;//背景图片



	public PoolAdapter(Context context, int endNum, List<Integer> selectNums,
			int resId) {
		super();
		this.context = context;
		this.endNum = endNum;
		this.selectNums = selectNums;
		this.resId = resId;
	}

	@Override
	public int getCount() {
		return endNum;
	}

	@Override
	public Object getItem(int position) {
		return position+1;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView ball=new TextView(context);
		ball.setTextSize(15);
		TextPaint paint=ball.getPaint();
		paint.setFakeBoldText(true);
		if(getCount() == 33){
			//item为33设置红球布局中每一个球的字体颜色
			ball.setTextColor(Color.RED);	
			
		}else if(getCount()== 16){
			ball.setTextColor(Color.BLUE);
		}
		
		ball.setGravity(Gravity.CENTER);
		
		DecimalFormat format=new DecimalFormat("00");
		
		ball.setText(format.format(position + 1));
		
		//判断当前的这个号码是不是包含在已选集合中
		if(selectNums.contains(getItem(position))){
			ball.setBackgroundColor(resId);
			
		}else {
			ball.setBackgroundResource(R.drawable.shape_ball_white);
			ball.setTextColor(Color.WHITE);
			
		}

		
		return ball;
	}

}
