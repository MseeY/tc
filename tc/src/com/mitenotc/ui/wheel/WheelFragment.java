package com.mitenotc.ui.wheel;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LayoutAnimationController;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mitenotc.tc.R;
import com.mitenotc.ui.account.AccountUtils;
import com.mitenotc.ui.account.view.MyAutoCycleRollingListView;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.utils.AppUtil;

@SuppressLint("NewApi")
public class WheelFragment extends BaseFragment implements OnClickListener {

	private ImageView pointer;
	private ImageView btn_lucky;
	private ImageView iv_luckanim;
	private ImageView plate;
	private AnimationDrawable animationDrawable;
	private ImageView iv_luck_mid;
	private RelativeLayout rl_wheel_text;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {
			initView();
		} catch (Exception e) {
			Toast.makeText(mActivity, "重进一次吧", Toast.LENGTH_SHORT).show();
		}
	}

	private void initView() {
		setContentView(R.layout.luck_demo_page1);
		iv_luck_mid = (ImageView) findViewById(R.id.iv_luck_mid);
		rl_wheel_text = (RelativeLayout) findViewById(R.id.rl_wheel_text);
		 int displayHeight = AppUtil.getDisplayHeight(mActivity);
		 LayoutParams layoutParams = rl_wheel_text.getLayoutParams();
		 LayoutParams para = iv_luck_mid.getLayoutParams();
		 para.width = LayoutParams.MATCH_PARENT;
		 layoutParams.width = LayoutParams.MATCH_PARENT;
		 if(displayHeight>960){
			 para.height = AccountUtils.resize(mActivity, 80);
			 layoutParams.height = AccountUtils.resize(mActivity, 240);
		 }else{
			 para.height = AccountUtils.resize(mActivity, 80);
			 layoutParams.height = AccountUtils.resize(mActivity, 150);
		 }
		 iv_luck_mid.setLayoutParams(para);
		 rl_wheel_text.setLayoutParams(layoutParams);
		 
//		scrollView = (AutoScrollView) findViewById(R.id.auto_scrollview);
		setTitleNav("夺宝摩天轮", R.drawable.title_nav_back, 0);
//		rl_find_luck_bg = (RelativeLayout) findViewById(R.id.rl_find_luck_bg);
		pointer = (ImageView) findViewById(R.id.iv_pointer);
		btn_lucky = (ImageView) findViewById(R.id.iv_btn_lucky);
		iv_luckanim = (ImageView) findViewById(R.id.iv_luckanim);
		iv_luckanim.setImageResource(R.drawable.luckanim);
		animationDrawable = (AnimationDrawable) iv_luckanim.getDrawable();
		animationDrawable.start();
//		mThread = new MThread();
		plate = (ImageView) findViewById(R.id.plate);
		plate.setOnClickListener(this);
//		mThread.start();
		btn_lucky.setOnClickListener(this);
		initWheelDate();
		
	}
	
	
	private MyAutoCycleRollingListView scroll_text;

	private void initWheelDate() {
		scroll_text = (MyAutoCycleRollingListView) findViewById(R.id.luck_auto_scroll_text);
//		 String[] adapterData = new String[] { "Afghanistan", "Albania", "Algeria", 
//                "American Samoa", "Andorra", "Angola", "Anguilla", 
//            "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia", 
//                "Aruba", "Australia", "Austria", "Azerbaijan", "Bahrain", 
//                "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", 
//                "Benin", "Bermuda", "Bhutan", "Bolivia", 
//                "Bosnia and Herzegovina", "Botswana", "Bouvet Island" }; 
//ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>( 
//        mActivity, android.R.layout.simple_list_item_1,adapterData); 
		scroll_text.setAdapter(new MyAdapter());
		scroll_text.startRoll();
}

	RotateAnimation rotateAnimation;
	RotateAnimation rotateAnimation1;
	DecelerateInterpolator inter;
	int[] comm = { 30, 60, 90, 120, 150, 180, 210, 240, 270, 300, 330, 360 };
	int index;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_btn_lucky:
			rotateAnimation = new RotateAnimation(0, +1080,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			rotateAnimation.setFillAfter(true);
			rotateAnimation.setDuration(3000);
			AccelerateInterpolator adi = new AccelerateInterpolator();
		       LayoutAnimationController lac=new LayoutAnimationController(rotateAnimation);

		       //设置控件显示的顺序；

		       lac.setOrder(LayoutAnimationController.ORDER_REVERSE);

		       //设置控件显示间隔时间；

		       lac.setDelay(1);

			rotateAnimation.setInterpolator(adi);

			rotateAnimation1 = new RotateAnimation(0, +1080,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			rotateAnimation1.setFillAfter(true);
			rotateAnimation1.setDuration(3000);
			AccelerateInterpolator adi1 = new AccelerateInterpolator();
			rotateAnimation1.setInterpolator(adi1);

		       LayoutAnimationController lac1=new LayoutAnimationController(rotateAnimation1);

		       //设置控件显示的顺序；

		       lac1.setOrder(LayoutAnimationController.ORDER_REVERSE);

		       //设置控件显示间隔时间；

		       lac.setDelay(1);
			
			pointer.startAnimation(rotateAnimation);
			iv_luckanim.startAnimation(rotateAnimation1);
			rotateAnimation.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {

				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
//					int rotation = (int) plate.getRotation();
					// Toast.makeText(getApplicationContext(), rotation+"",
					// 0).show();
					Random random = new Random();//
					index = random.nextInt(comm.length);
					rotateAnimation = new RotateAnimation(+1080 % 360, /*rotation*/
							+ 360 + comm[index], Animation.RELATIVE_TO_SELF,
							0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
					rotateAnimation.setFillAfter(true);
					rotateAnimation.setDuration(5000);
					inter = new DecelerateInterpolator();
					rotateAnimation.setInterpolator(inter);
					pointer.startAnimation(rotateAnimation);
//					animationDrawable.start();

					/*
					 * 15-45 七星彩 45-75
					 */
					rotateAnimation
							.setAnimationListener(new AnimationListener() {

								@Override
								public void onAnimationStart(Animation animation) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onAnimationRepeat(
										Animation animation) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onAnimationEnd(Animation animation) {
									switch (comm[index]) {
									case 30:
										showDialog("七星彩");
										break;
									case 60:
										showDialog("排列三");
										break;
									case 90:
										showDialog("快乐扑克");
										break;
									case 120:
										showDialog("福彩快三");
										break;
									case 150:
										showDialog("20积分");
										break;
									case 180:
										showDialog("20积分");
										break;
									case 210:
										showDialog("11运夺金");
										break;
									case 240:
										showDialog("7星彩");
										break;
									case 270:
										showDialog("双色球");
										break;
									case 300:
										showDialog("11运夺金");
										break;
									case 330:
										showDialog("排列5");
										break;
									case 360:
										showDialog("福彩快三");
										break;
									}
								}
							});
				}
			});

			break;
		default:
			break;
		}
	}

	public void showDialog(String str) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
		builder.setMessage("您中了:" + str);
		builder.setTitle("中奖提示");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
//				count = 0;
				pointer.clearAnimation();
//				mThread = new MThread();
//				mThread.start();
			}
		});
		
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
//				count = 0;
				pointer.clearAnimation();
//				mThread = new MThread();
//				mThread.start();
			}
		});

		builder.create().show();
	}
	
    class MyAdapter extends BaseAdapter{
    	
    	@Override
		public View getView(int position, View convertView, ViewGroup parent) {
    		ViewScrollHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.luck_demo_atuo_scroll_item, null);
				holder = new ViewScrollHolder();
				holder.text = (TextView) convertView.findViewById(R.id.tv_atuo_scroll_text);
				holder.time = (TextView) convertView.findViewById(R.id.tv_atuo_scroll_time);
				convertView.setTag(holder);
			} else {
				holder = (ViewScrollHolder) convertView.getTag();
			}
			
			Random dom = new Random();
			int nextInt = dom.nextInt(6);
			holder.text.setText("恭喜用户"+nextInt);
			holder.time.setText("2012-1-4");
			return convertView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 50;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
    	
    }
    
    private static class ViewScrollHolder {
		TextView text; // 用户名
		TextView time; // 时间
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		scroll_text.pauseRoll();
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		scroll_text.restartRoll();
		super.onResume();
	}
}
