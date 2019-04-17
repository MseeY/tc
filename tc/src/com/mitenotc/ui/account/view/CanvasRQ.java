package com.mitenotc.ui.account.view;

import java.util.Hashtable;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.utils.AppUtil;
import com.mitenotc.utils.SPUtil;
import com.zbar.lib.Untilly;

public class CanvasRQ extends View {

	// private Bitmap mBG;
	private Bitmap mOverBp;
	private TypedArray userPhotos;
	private String qr = "http://www.baidu.com"; // 不能为中文
	private Paint paint;
	private int QR_WIDTH;
	private int QR_HEIGHT;
	// 前景色
	private int FOREGROUND_COLOR = 0xff000000;
	// 背景色
	private int BACKGROUND_COLOR = 0xffffffff;
	public String getQr() {
		return qr;
	}

	public void setQr(String qr) {
		this.qr = qr;
	}

	public Paint getPaint() {
		return paint;
	}

	public void setPaint(Paint paint) {
		this.paint = paint;
	}

	public int getFOREGROUND_COLOR() {
		return FOREGROUND_COLOR;
	}

	public void setFOREGROUND_COLOR(int fOREGROUND_COLOR) {
		FOREGROUND_COLOR = fOREGROUND_COLOR;
	}

	public int getBACKGROUND_COLOR() {
		return BACKGROUND_COLOR;
	}

	public void setBACKGROUND_COLOR(int bACKGROUND_COLOR) {
		BACKGROUND_COLOR = bACKGROUND_COLOR;
	}

	// 图片宽度的一般
	private int IMAGE_HALFWIDTH = 50;

	// 插入到二维码里面的图片对象
	private Bitmap mBitmap;
	// 需要插图图片的大小 这里设定为40*40

	public CanvasRQ(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public CanvasRQ(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public CanvasRQ(Context context) {
		super(context);
		initView();
	}

	private void initView() {
		 setClickable(true);
		 int displayHeight = AppUtil.getDisplayHeight(MyApp.context);
		 if(displayHeight>960){
			 QR_WIDTH = 500;
			 QR_HEIGHT = 500;
			 IMAGE_HALFWIDTH = 50;
		 }else{
			 QR_WIDTH = 270;
			 QR_HEIGHT = 270;
			 IMAGE_HALFWIDTH = 25;
		 }
		paint = new Paint();
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
			cretaeBitmap(qr, mOverBp);
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		  int measureWidth = measureWidth(widthMeasureSpec);
//	      int measureHeight = measureHeight(heightMeasureSpec);
	      setMeasuredDimension(510, 510);
	}
	
	  private int measureWidth(int pWidthMeasureSpec) {
	    	int result = 0;
	    	
	        int widthMode = MeasureSpec.getMode(pWidthMeasureSpec);
	        int widthSize = MeasureSpec.getSize(pWidthMeasureSpec);
	        
	        switch (widthMode) {
	        case MeasureSpec.AT_MOST:
	        	break;
	        case MeasureSpec.EXACTLY:
	        	result = widthSize;
	            break;
	        }
	        return result;
	    }
	    
	    
	    private int measureHeight(int pHeightMeasureSpec) {
	    	int result = 0;
	    	
	        int heightMode = MeasureSpec.getMode(pHeightMeasureSpec);
	        int heightSize = MeasureSpec.getSize(pHeightMeasureSpec);
	        
	        switch (heightMode) {
	        case MeasureSpec.AT_MOST:
	        	break;
	        case MeasureSpec.EXACTLY:
	        	result = heightSize;
	            break;
	        }
	        
	        return result;
	    }

	@Override
	protected void onDraw(Canvas canvas) {
		// 定义圆角矩形对象
		int left = (getWidth() - mBitmap.getWidth()) / 2;
		int top = (getHeight() - mBitmap.getHeight()) / 2;
		RectF rectF1 = new RectF(left - 10, top - 10, left + mBitmap.getWidth()+ 10, top + mBitmap.getHeight() + 10);
		paint.setAntiAlias(true);
		paint.setColor(MyApp.res.getColor(R.color.gray));
		canvas.drawRoundRect(rectF1, 20, 20, paint);
		canvas.drawBitmap(mBitmap, left, top, null);
	}

	public Bitmap cretaeBitmap(String str, Bitmap icon) throws WriterException {
		// TODO Auto-generated method stub
		icon = Untilly.zoomBitmap(icon, IMAGE_HALFWIDTH);
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
//		hints.put(EncodeHintType.MARGIN, 1);
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
}
