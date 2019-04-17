package com.zbar.lib;

import java.io.ByteArrayOutputStream;
import java.util.Hashtable;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.ui.account.view.ActionSheetDialog;
import com.mitenotc.ui.account.view.ActionSheetDialog.OnSheetItemClickListener;
import com.mitenotc.ui.account.view.ActionSheetDialog.SheetItemColor;
import com.mitenotc.ui.base.BaseFragment;
import com.mitenotc.utils.AppUtil;
import com.mitenotc.utils.SPUtil;
/**
 * 2015-2-2 13:30:47
 * @author ymx
 *
 */

public class MyOrCodeFragment extends BaseFragment implements OnClickListener{
	
	private ImageButton iv_or_code;
	private String mPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/taicai/";
	private String mFileName = "myqr.jpg";
	// private Bitmap mBG;
	private Bitmap mOverBp;
	private TypedArray userPhotos;
	private String qr = "http://m.mitenotc.com"; // 不能为中文
	private int QR_WIDTH;
	private int QR_HEIGHT;
	// 前景色
	private int FOREGROUND_COLOR;
	// 背景色
	private int BACKGROUND_COLOR;
	
	// 图片宽度的一般
	private int IMAGE_HALFWIDTH = 20;
	
	private Bitmap mBitmap;
	//制作后的bitmap
	private Bitmap resultBitmap;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scan_my_orcode);
		setTitleNav("我的二维码", R.drawable.title_nav_back, 0);
		iv_or_code = (ImageButton) findViewById(R.id.iv_or_code);
		iv_or_code.setOnClickListener(this);
		//屏幕的适配
		int displayHeight = AppUtil.getDisplayHeight(MyApp.context);
		 if(displayHeight>960){
			 QR_WIDTH = 450;
			 QR_HEIGHT = 450;
			 IMAGE_HALFWIDTH = 50;
		 }else{
			 QR_WIDTH = 300;
			 QR_HEIGHT = 300;
			 IMAGE_HALFWIDTH = 25;
		 }
		//数据的初始化
		 int foreground = SPUtil.getInt(MyApp.res.getString(R.string.FOREGROUNDCOLOR), 0);
		 int background = SPUtil.getInt(MyApp.res.getString(R.string.BACKGROUNDCOLOR), 0);
		 if(foreground != 0 && background != 0){
			 FOREGROUND_COLOR = foreground;
			 BACKGROUND_COLOR = background;
		 }else{
			 FOREGROUND_COLOR = 0xff000000;
			 BACKGROUND_COLOR = 0xffffffff;
		 }
		 
		 userPhotos = MyApp.res.obtainTypedArray(R.array.acc_user_photos);
			if(!"".equals(SPUtil.getString(R.string.USERPHOTO))){
				Drawable drawable = userPhotos.getDrawable(Integer.parseInt(SPUtil.getString(R.string.USERPHOTO)));
				BitmapDrawable bd = (BitmapDrawable)drawable;
				mOverBp = bd.getBitmap();
			}else if(MyApp.header_photo !=null){
				mOverBp = MyApp.header_photo;
			}else{
				mOverBp = BitmapFactory.decodeResource(MyApp.context.getResources(),
						R.drawable.acc_me_header_logo);
			}
			
			if(userPhotos!=null){
				userPhotos.recycle();
			}

			try {
				resultBitmap = cretaeBitmap(qr, mOverBp);
				iv_or_code.setImageBitmap(resultBitmap);
			} catch (WriterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public Bitmap cretaeBitmap(String str, Bitmap icon) throws WriterException {
		// TODO Auto-generated method stub
		icon = Untilly.zoomBitmap(icon, IMAGE_HALFWIDTH);
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hints.put(EncodeHintType.MARGIN, 1);
		// 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
		BitMatrix matrix = new MultiFormatWriter().encode(str,BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
		
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		// 二维矩阵转为一维像素数组,也就是一直横着排了
		int halfW = width / 2;
		int halfH = height / 2;
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH&& y > halfH - IMAGE_HALFWIDTH&& y < halfH + IMAGE_HALFWIDTH) {
					pixels[y * width + x] = icon.getPixel(x - halfW+ IMAGE_HALFWIDTH, y - halfH + IMAGE_HALFWIDTH);
				} else {
					if (matrix.get(x, y)) {
						pixels[y * width + x] = FOREGROUND_COLOR;
					} else { // 无信息设置像素点为白色
						pixels[y * width + x] = BACKGROUND_COLOR;
					}
				}

			}
		}
		 mBitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		// 通过像素数组生成bitmap
		 mBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return mBitmap;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_or_code:
			showOperation();
			break;
		}
	}
	
	private void showOperation() {
		// TODO Auto-generated method stub
		new ActionSheetDialog(mActivity)
		.builder()
		.setCancelable(true)
		.setCanceledOnTouchOutside(true)
		.addSheetItem("分享", SheetItemColor.Blue,
				new OnSheetItemClickListener() {
					@Override
					public void onClick(int which) {
						showShare();
					}
				})
		.addSheetItem("换个样式", SheetItemColor.Blue,
				new OnSheetItemClickListener() {
					@Override
					public void onClick(int which) {
						changeStyle();
					}
				})
		.addSheetItem("保存到手机", SheetItemColor.Blue,
				new OnSheetItemClickListener() {
					@Override
					public void onClick(int which) {
						Boolean state = writeBitmap(resultBitmap, mPath, mFileName);
						if(state){//如果保存成功  刷新相册
//							mActivity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));  
							Toast.makeText(mActivity, "已保存到："+mPath+mFileName, Toast.LENGTH_LONG).show();
//							MediaScannerConnection.scanFile(mActivity, new String[]{Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath() + "/" + fileName}, null, null);
						}else{
							Toast.makeText(mActivity, "保存失败，请检查SD卡！", Toast.LENGTH_SHORT).show();
						}
					}
				}).show();
		
	}

	private void showShare() {
		 ShareSDK.initSDK(mActivity);
		 OnekeyShare oks = new OnekeyShare();
			oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
			oks.setAddress("");
			oks.setTitle("泰彩彩票，扫一扫有惊喜！");//标题
			oks.setText("让您的朋友扫一扫，您可获得意外的惊喜，赶快行动吧");//内容
			oks.setTitleUrl("http://bug.mitenotc.com/");
			oks.setUrl("http://bug.mitenotc.com/");
			oks.setImageUrl("http://img01.appcms.cc/20140609/t017/t0179714ce391ce3cda.png");
			oks.setComment("泰彩彩票");
			oks.setSite("泰彩彩票");
			oks.setSiteUrl("http://bug.mitenotc.com/");
//			oks.disableSSOWhenAuthorize();
			oks.setTheme(OnekeyShareTheme.CLASSIC);
			oks.show(mActivity);
	}
	
	private void changeStyle() {
		new ActionSheetDialog(mActivity)
		.builder()
		.setCancelable(true)
		.setCanceledOnTouchOutside(true)
		.addSheetItem("绿叶配红花", SheetItemColor.Blue,
				new OnSheetItemClickListener() {
					@Override
					public void onClick(int which) {
						FOREGROUND_COLOR = Color.RED;
						BACKGROUND_COLOR = Color.GREEN;
						try {
							resultBitmap = cretaeBitmap(qr, mOverBp);
							iv_or_code.setImageBitmap(resultBitmap);
							SPUtil.putInt(MyApp.res.getString(R.string.FOREGROUNDCOLOR), Color.RED);
							SPUtil.putInt(MyApp.res.getString(R.string.BACKGROUNDCOLOR), Color.GREEN);
						} catch (WriterException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				})
		.addSheetItem("蓝天配白云", SheetItemColor.Blue,
				new OnSheetItemClickListener() {
					@Override
					public void onClick(int which) {
						FOREGROUND_COLOR = Color.BLUE;
						BACKGROUND_COLOR = Color.WHITE;
						try {
							resultBitmap = cretaeBitmap(qr, mOverBp);
							iv_or_code.setImageBitmap(resultBitmap);
							SPUtil.putInt(MyApp.res.getString(R.string.FOREGROUNDCOLOR), Color.BLUE);
							SPUtil.putInt(MyApp.res.getString(R.string.BACKGROUNDCOLOR), Color.WHITE);
						} catch (WriterException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				})
		
		.addSheetItem("默认样式", SheetItemColor.Blue,
				new OnSheetItemClickListener() {
					@Override
					public void onClick(int which) {
						FOREGROUND_COLOR = 0xff000000;
						BACKGROUND_COLOR = 0xffffffff;
						try {
							resultBitmap = cretaeBitmap(qr, mOverBp);
							iv_or_code.setImageBitmap(resultBitmap);
							SPUtil.putInt(MyApp.res.getString(R.string.FOREGROUNDCOLOR), 0xff000000);
							SPUtil.putInt(MyApp.res.getString(R.string.BACKGROUNDCOLOR), 0xffffffff);
						} catch (WriterException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				})
		.addSheetItem("返回", SheetItemColor.Blue,
				new OnSheetItemClickListener() {
					@Override
					public void onClick(int which) {
						showOperation();
					}
				}).show();
	}
	
	/**
	 * 保存二维码
	 * 
	 * @param b
	 * @return
	 */
	public static boolean writeBitmap(Bitmap b,String mPath,String mFileName) {
		ByteArrayOutputStream by = new ByteArrayOutputStream();
		b.compress(Bitmap.CompressFormat.JPEG, 100, by);
		byte[] stream = by.toByteArray();
		return Untilly.writeToSdcard(stream, mPath, mFileName);
	}
}
