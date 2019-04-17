package com.mitenotc.ui.ui_utils;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.utils.DensityUtil;

public class MyWM  extends LinearLayout  {
    private WindowManager wm;
    private WindowManager.LayoutParams wmParams;
    
    private float mTouchStartX;
    private float mTouchStartY;
    
    private float X=0;
    private float Y=70;
    
    private  TextView leftbtn_yiloutv;
	private  TextView leftbtn_yiloutv2;
    
	private View mview;

    
	public MyWM(Context context) {
		super(context);
		initView(context);
	}

	public MyWM(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}
	
	private void initView(Context context) {
//		 wm = MyApp.getMywm();
//		 wmParams= MyApp.getMywmParams();
	     wmParams.type=2002;   //设置window type
         wmParams.format=PixelFormat.RGBA_8888;   //设置图片格式，效果为背景透明
         Y=DensityUtil.dip2px(context, 70);
         //以屏幕左上角为原点，设置x、y初始值
         wmParams.x=(int) X;
         wmParams.y=(int) Y;
       //设置悬浮窗口长宽数据
         wmParams.width=DensityUtil.dip2px(context, 200);
         wmParams.height=DensityUtil.dip2px(context, 48);
        //设置Window flag
         wmParams.flags=0x00000008|0x00000020;//LayoutParams.FLAG_NOT_TOUCH_MODAL
//		                               | LayoutParams.FLAG_NOT_FOCUSABLE;
         mview=View.inflate(context, R.layout.wm_yilou, this);
		 leftbtn_yiloutv = (TextView) mview.findViewById(R.id.leftbtn_yiloutv);
		 leftbtn_yiloutv2 = (TextView) mview.findViewById(R.id.leftbtn_yiloutv2);

     	leftbtn_yiloutv.setOnClickListener(new View.OnClickListener() {	
 			public void onClick(View arg0) {
 		        leftbtn_yiloutv.setVisibility(View.GONE);
 		        leftbtn_yiloutv2.setVisibility(View.VISIBLE);
 			}
 		});
     	
     	leftbtn_yiloutv2.setOnClickListener(new OnClickListener() {
 			@Override
 			public void onClick(View arg0) {
// 				todestroyWM();
 			   String temStr=leftbtn_yiloutv2.getText().toString().trim();
 			   if("显示遗漏".equals(temStr)){
 				   leftbtn_yiloutv2.setText("隐藏遗漏");
 				   if(isShowyilouView != null){
 					   isShowyilouView.changeViewItemyilouShow(true);
 				   }
 			   }else{
 				   leftbtn_yiloutv2.setText("显示遗漏");
 				  if(isShowyilouView != null){
 					  isShowyilouView.changeViewItemyilouShow(false);
 				   }
 			   }
 				
 		        leftbtn_yiloutv.setVisibility(View.VISIBLE);
 		        leftbtn_yiloutv2.setVisibility(View.GONE);
 			}
 		});
     	//调整悬浮窗口
         wmParams.gravity=Gravity.LEFT|Gravity.BOTTOM;
         //显示myFloatView图像
         wm.addView(mview, wmParams);
	}
	public interface ChangeyilouShowView{
	 public void  changeViewItemyilouShow(boolean isChangeView);
	}
	private  ChangeyilouShowView isShowyilouView;
	
    public  void setisShowyilouViewLinstener(ChangeyilouShowView mShowView) {
		this.isShowyilouView = mShowView;
	}

	/**
     * 关闭 WindowManager
     */
	public void DestroyWM(){
		if(mview!=null && mview.getParent() != null){
			wm.removeView(mview);
		}
	
	}
	/**
	 * 关闭 WindowManager
	 */
	public void hintWM(){
		if(leftbtn_yiloutv!=null && leftbtn_yiloutv.getVisibility()==View.VISIBLE){
			leftbtn_yiloutv.setVisibility(View.GONE);
		}else if(leftbtn_yiloutv2!=null && leftbtn_yiloutv2.getVisibility()==View.VISIBLE){
			leftbtn_yiloutv2.setVisibility(View.GONE);
		}
	}
	
	public void showDefaultyiLouTV(){
		if(mview!=null && mview.getParent()==null){
			wm.addView(mview, wmParams);
		}
		if(leftbtn_yiloutv!=null && leftbtn_yiloutv.getVisibility()==View.GONE){
			leftbtn_yiloutv.setVisibility(View.VISIBLE);
		}
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//获取相对屏幕的坐标，即以屏幕左上角为原点
		 
        switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:    //捕获手指触摸按下动作
                        //获取相对View的坐标，即以此View左上角为原点
                    	 Y = event.getRawY()-70;//25是系统状态栏的高度
//                    	////System.out.println("Y------DOWN------ = "+Y );
                        break;

                    case MotionEvent.ACTION_MOVE:  //捕获手指触摸移动动作
                    	mTouchStartY =  event.getY();
                    	
                        updateViewPosition();
                        break;

                    case MotionEvent.ACTION_UP:    //捕获手指触摸离开动作
                    	mTouchStartY =  event.getY();
                    	
                        updateViewPosition();
                        break;
                    }
                    return true;
      }
	/**
	 * 更新位置
	 */
	private void updateViewPosition(){
	     //更新浮动窗口位置参数 
		wmParams.x=(int) X;   //x轴不移动
		if((int) mTouchStartY >= 100 || (int) mTouchStartY <= 180  ){  // 为了控制在顶部title 一下 底部清空按钮以上
			Y=(Y - mTouchStartY);
		}
		 wmParams.y=(int) Y;
		           
//		////System.out.println("Y---------UP------- = "+Y );
//		////System.out.println("Y----mTouchStartY-- = "+mTouchStartY );
//		////System.out.println("Y----(Y - mTouchStartY)--= "+(Y - mTouchStartY) );
	    wm.updateViewLayout(this, wmParams);  //刷新显示
   }
	

}
