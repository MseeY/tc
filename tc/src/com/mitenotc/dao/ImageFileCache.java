package com.mitenotc.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.codec.digest.DigestUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

public class ImageFileCache {

	/*
	 * public Bitmap getImageFromFile(String url) { // TODO Auto-generated
	 * method stub return null; }
	 * 
	 * public void saveBitmapToFile(Bitmap bmp, String url) { // TODO
	 * Auto-generated method stub
	 * 
	 * }
	 */

	private static final String CACHDIR = "taicai/img";
	private static final String WHOLESALE_CONV = ".cach";

	private static final int MB = 1024 * 1024;
	private static final int CACHE_SIZE = 10;
	private static final int FREE_SD_SPACE_NEEDED_TO_CACHE = 10;

	public ImageFileCache() {
		// 清理文件缓存
		// /removeCache(getDirectory());
	}

	/** 从缓存中获取图片 **/
	public Bitmap getImageFromFile(String filename) {
		String path = getDirectory() + "/" + filename + WHOLESALE_CONV;
		File file = new File(path);
		if (file.exists()) {
			BitmapFactory.Options opts = new BitmapFactory.Options();
		//	opts.inJustDecodeBounds = true;

			//BitmapFactory.decodeFile(path, opts);

			opts.inSampleSize = 2;//computeSampleSize(opts, -1, 128 * 128);
			// 这里一定要将其设置回false，因为之前我们将其设置成了true
			opts.inJustDecodeBounds = false;
			Bitmap bmp = BitmapFactory.decodeFile(path, opts);
			if (bmp == null) {
				file.delete();
			} else {
				
				return bmp;
			}
		}
		return null;
	}
	
	/** 从缓存中获取图片 **/
	public Bitmap getImageFromFile2(String path) {
		File file = new File(path);
		if (file.exists()) {
		    BitmapFactory.Options bfOptions=new BitmapFactory.Options();
		    bfOptions.inDither=false; //不进行图片抖动处理                 
		    bfOptions.inPurgeable=true; //如果被设置为true,那么由此产生的位图将分配像素,这样他们可以被GC系统在需要时回收                
		    bfOptions.inInputShareable=true;//表示只获得宽高 不返回Bitmap
		    bfOptions.inTempStorage=new byte[32 * 1024]; 

		    FileInputStream fs=null;
		    try {
		        fs = new FileInputStream(file);
		    } catch (FileNotFoundException e) {
		        e.printStackTrace();
		    }

		    try {
		        if(fs!=null) {
				Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fs.getFD(), null, bfOptions);
					if(bitmap!=null){
						return bitmap; 
					}else{
						return null;
					}
				}
		    } catch (IOException e) {
		        e.printStackTrace();
		    } finally{ 
		        if(fs!=null) {
		            try {
		                fs.close();
		            } catch (IOException e) {
		                e.printStackTrace();
		            }
		        }
		    }
		}
		return null;
	}
	
	/** 从缓存中获取图片 **/
	public Bitmap getImageFromFile1(String filename) {
		String path = getDirectory() + "/" + filename + WHOLESALE_CONV;
		File file = new File(path);
		if (file.exists()) {
		    Log.i("showImage","loading:"+filename);
		    BitmapFactory.Options bfOptions=new BitmapFactory.Options();
		    bfOptions.inDither=false; //不进行图片抖动处理                 
		    bfOptions.inPurgeable=true; //如果被设置为true,那么由此产生的位图将分配像素,这样他们可以被GC系统在需要时回收                
		    bfOptions.inInputShareable=true;//表示只获得宽高 不返回Bitmap
		    bfOptions.inTempStorage=new byte[32 * 1024]; 

		    FileInputStream fs=null;
		    try {
		        fs = new FileInputStream(file);
		    } catch (FileNotFoundException e) {
		        e.printStackTrace();
		    }

		    try {
		        if(fs!=null) {
				Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fs.getFD(), null, bfOptions);
					if(bitmap!=null){
						return bitmap; 
					}else{
						return null;
					}
				}
		    } catch (IOException e) {
		        e.printStackTrace();
		    } finally{ 
		        if(fs!=null) {
		            try {
		                fs.close();
		            } catch (IOException e) {
		                e.printStackTrace();
		            }
		        }
		    }
		}
		return null;
	}

	/** 计算sdcard上的剩余空间 **/
	private int freeSpaceOnSd() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());
		double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat
				.getBlockSize()) / MB;
		return (int) sdFreeMB;
	}

	/** 将图片存入文件缓存 **/
	public void saveBitmapToFile(Bitmap bm, String filename) {
		if (bm == null) {
			return;
		}
		// 判断sdcard上的空间
		if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
			// SD空间不足
			return;
		}

		String dir = getDirectory();
		File dirFile = new File(dir);

		if (!dirFile.exists())
			dirFile.mkdirs();

		File file = new File(dir + "/" + filename + WHOLESALE_CONV);
		try {
			file.createNewFile();
			OutputStream outStream = new FileOutputStream(file);
			bm.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
			outStream.flush();
			outStream.close();
		} catch (FileNotFoundException e) {
			Log.w("ImageFileCache", "FileNotFoundException");
		} catch (IOException e) {
			Log.w("ImageFileCache", "IOException");
		}
	}

	/**
	 * 计算存储目录下的文件大小，
	 * 当文件总大小大于规定的CACHE_SIZE或者sdcard剩余空间小于FREE_SD_SPACE_NEEDED_TO_CACHE的规定
	 * 那么删除40%最近没有被使用的文件
	 */
	public boolean removeCache(String filename) {
		String path = getDirectory() + "/" + filename + WHOLESALE_CONV;
		File file = new File(path);
		if (file.exists())
			file.delete();
		return true;
	}

	/** 获得缓存目录 **/
	private String getDirectory() {
		String dir = getSDPath() + "/" + CACHDIR;
		return dir;
	}

	/** 取SD卡路径 **/
	private String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory(); // 获取根目录
		}
		if (sdDir != null) {
			return sdDir.toString();
		} else {
			return "";
		}
	}

	public boolean fileExist(String filename) {
		String path = getDirectory() + "/" + filename + WHOLESALE_CONV;
		System.out.println("path== " + path);
		File file = new File(path);
		return file.exists();
	}

	public boolean downloadImage(String path) {
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			int code = conn.getResponseCode();//
			if (code == 200) {
				InputStream is = conn.getInputStream();
				Bitmap bitmap = BitmapFactory.decodeStream(is);
				saveBitmapToFile(bitmap, DigestUtils.md5Hex(path));
				bitmap = null;
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}
}
