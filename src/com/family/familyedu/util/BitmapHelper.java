package com.family.familyedu.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;

public class BitmapHelper
{ static Context context;
  static final BitmapHelper.IHelper IMPL;
  public BitmapHelper(Context context) {
	  this.context = context;
  }
  abstract interface IHelper
  {
    public abstract float convertUnitToPixel(Context paramContext, int paramInt, float paramFloat);

    public abstract Bitmap decodeFile(String paramString, float paramFloat);

    public abstract Bitmap decodeStream(InputStream paramInputStream);

    public abstract Bitmap decodeStream(InputStream paramInputStream, float paramFloat);

    public abstract float dip2px(Context paramContext, float paramFloat);

    public abstract float px2dip(Context paramContext, float paramFloat);
  }
  static
  {
    if (Build.VERSION.SDK_INT >= 4)
    {
    	IMPL = new BitmapHelperSdk4();
    }else{
    	IMPL = new BitmapHelperSdk1();
    }
   
  }

  public static Bitmap bitmapAdder(int paramInt1, int paramInt2)
  {
    return bitmapAdder(paramInt1, paramInt2, 0.0F);
  }

  public static Bitmap bitmapAdder(int paramInt1, int paramInt2, float paramFloat)
  {
    Bitmap localBitmap1 = BitmapFactory.decodeResource(context.getResources(), paramInt1).copy(Bitmap.Config.ARGB_8888, true);
    Bitmap localBitmap2 = BitmapFactory.decodeResource(context.getResources(), paramInt2);
    new Canvas(localBitmap1).drawBitmap(localBitmap2, localBitmap1.getWidth() - localBitmap2.getWidth() - paramFloat, paramFloat, null);
    return localBitmap1;
  }

  public static Bitmap decodeFile(String paramString, float paramFloat)
  {
    return IMPL.decodeFile(paramString, paramFloat);
  }

  public static Bitmap decodeStream(InputStream paramInputStream)
  {
    return IMPL.decodeStream(paramInputStream);
  }

  public static Bitmap decodeStream(InputStream paramInputStream, float paramFloat)
  {
    return IMPL.decodeStream(paramInputStream, paramFloat);
  }

  public static Bitmap decodeURL(String paramString)
  {
    try
    {
      HttpURLConnection localHttpURLConnection = (HttpURLConnection)new URL(paramString).openConnection();
      localHttpURLConnection.setDoInput(true);
      localHttpURLConnection.connect();
      Bitmap localBitmap = decodeStream(localHttpURLConnection.getInputStream());
      return localBitmap;
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    return null;
  }

  public static float dip2px(Context paramContext, float paramFloat)
  {
    return IMPL.dip2px(paramContext, paramFloat);
  }

  public static Bitmap drawableToBitmap(Drawable paramDrawable)
  {
    int i = paramDrawable.getIntrinsicWidth();
    int j = paramDrawable.getIntrinsicHeight();
    if (paramDrawable.getOpacity() != -1);
    for (Bitmap.Config localConfig = Bitmap.Config.ARGB_8888; ; localConfig = Bitmap.Config.RGB_565)
    {
      Bitmap localBitmap = Bitmap.createBitmap(i, j, localConfig);
      Canvas localCanvas = new Canvas(localBitmap);
      paramDrawable.setBounds(0, 0, i, j);
      paramDrawable.draw(localCanvas);
      return localBitmap;
    }
  }

  public static int iPXToPX(Context paramContext, float paramFloat)
  {
    return (int)(0.5F + paramFloat * (paramContext.getResources().getDisplayMetrics().widthPixels / 640.0F));
  }

  public static float iPXToPXF(Context paramContext, float paramFloat)
  {
    return paramFloat * (paramContext.getResources().getDisplayMetrics().widthPixels / 640.0F);
  }

  public static int px(float paramFloat)
  {
    return (int)(0.5F + IMPL.dip2px(context, paramFloat));
  }

  public static float px2dip(Context paramContext, float paramFloat)
  {
    return IMPL.px2dip(paramContext, paramFloat);
  }
}