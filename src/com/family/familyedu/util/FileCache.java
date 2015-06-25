package com.family.familyedu.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;

/**
 * 文件缓存类，将图片缓存到sd卡中
 * <p>
 * Copyright: Copyright (c) 2014-1-9 下午5:49:54
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * 
 * @author lipeng@c-platform.com
 * @version 1.0.0
 */
public class FileCache {

	/**
	 * TAG
	 */
	private static final String TAG = "FileCache";

	/**
	 * 单例
	 */
	private static FileCache diskCache;

	/**
	 * 上下文
	 */
	private Context context;

	/**
	 * 单例实例化
	 * 
	 * @param ctx
	 * @return
	 */
	public synchronized static FileCache newInstance(Context ctx) {
		if (diskCache == null) {
			diskCache = new FileCache(ctx);
		}
		return diskCache;
	}

	private FileCache(Context context) {
		this.context = context;
	}

	/**
	 * 根据图片名称和所在目录获取图片
	 * 
	 * @param fileName
	 *            图片名称包含具体地址
	 * @return
	 */
	public Bitmap getBitmap(String fileName) {
		if (fileName == null) {
			return null;
		}
		File file = new File(fileName);
		if (file.exists()) {
//			Log.i(TAG, "取自sd卡文件：" + file.getAbsolutePath());
			// TODO 第二个参数的意义以及如果图片过大还要加其他处理优化
			Bitmap bitmap = BitmapHelper.decodeFile(file.getAbsolutePath(), 1);
			return bitmap;
		} else {
			return null;
		}
	}

	/**
	 * 放入sd卡缓存中
	 * 
	 * @param cacheName
	 *            图片名称
	 * @param bitmap
	 *            图片
	 * @return
	 */
	public boolean putBitmap(String fileName, Bitmap bitmap) {
		if (fileName == null || bitmap == null) {
			return false;
		}
		// 判断文件夹以及文件是否存在，不存在则创建
		File file = new File(fileName);

		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}

		if (!file.exists()) {
			try {
				file.createNewFile();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
			boolean rel = bitmap.compress(CompressFormat.JPEG, 100, bos);
			bos.close();
			return rel;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void clear() {
		// 具体根据文件夹来删除缓存
		// String cacheDir = CacheInFileUtils.getCachePath(context,
		// Fields.CACHE_TUAN_MY);
		// File file = new File(cacheDir);
		// if (file != null) {
		// File[] files = file.listFiles();
		// if (files != null) {
		// for (File f : files) {
		// f.delete();
		// }
		// file.delete();
		// }
		// }
	}
	
	/**
	 * 保存json文件
	 * @param name
	 * @param outputStream
	 */
	public void putTxt(String fileName,String content){
		String path = context.getFilesDir().getPath();
		Log.e("path", path);
		FileOutputStream outputStream;
		try {
			File fileTemp = new File(path,fileName);
			Log.e("file", fileTemp.getPath());
			if(fileTemp.exists()){
				fileTemp.delete();
			}
			outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			outputStream.write(content.getBytes("UTF-8"));
			outputStream.close();
        }
        catch (Exception e) {
	        e.printStackTrace();
	        Log.e(TAG, "putTxt", e);
        }

	}
	
	/**
	 * 拷贝文件到file目录下
	 * @param assetsSrc
	 * @param path
	 * @param fileName
	 * @return
	 */
	public boolean copyAssetsToFile(String assetsSrc,String path, String fileName){  
        InputStream istream = null;  
        FileOutputStream outputStream = null;
        try{  
			File fileTemp = new File(path,fileName);
			Log.e("file", fileTemp.getPath());
			if(fileTemp.exists()){
				fileTemp.delete();
			}
			
            AssetManager am = context.getAssets();  
            istream = am.open(assetsSrc);  
            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            byte[] buffer = new byte[1024];  
            int length;  
            while ((length = istream.read(buffer))>0){  
            	outputStream.write(buffer, 0, length);  
            }  
            istream.close();  
            outputStream.close();  
        }  
        catch(Exception e){  
        	Log.e(TAG, "copyAssetsToFile", e);
            try{  
				if (istream != null) {
					istream.close();
				}
				if (outputStream != null) {
					outputStream.close();
				}  
            }  
            catch(Exception ee){  
            	Log.e(TAG, "copyAssetsToFile", e);
            }  
            return false;  
        }  
        return true;  
    }
	
	/**
	 * 拷贝文件到databases目录下
	 * @param assetsSrc
	 * @param des
	 * @return
	 */
	public boolean copyAssetsToDatabases(String assetsSrc,String path, String fileName){  
        InputStream istream = null;  
        OutputStream ostream = null;  
        try{  
            AssetManager am = context.getAssets();  
            istream = am.open(assetsSrc);  
            File dir = new File(path);
            if(!dir.exists()){
            	dir.mkdir();
            }
            ostream = new FileOutputStream(path+"/"+fileName);  
            byte[] buffer = new byte[1024];  
            int length;  
            while ((length = istream.read(buffer))>0){  
                ostream.write(buffer, 0, length);  
            }  
            istream.close();  
            ostream.close();  
        }  
        catch(Exception e){  
        	Log.e(TAG, "copyAssetsToDatabases", e);
            try{  
                if(istream!=null)  
                    istream.close();  
                if(ostream!=null)  
                    ostream.close();  
            }  
            catch(Exception ee){  
            	Log.e(TAG, "copyAssetsToDatabases", e); 
            }  
            return false;  
        }  
        return true;  
    }  
	
}
