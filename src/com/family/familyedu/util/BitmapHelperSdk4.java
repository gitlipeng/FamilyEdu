package com.family.familyedu.util;

import java.io.InputStream;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.TypedValue;

@TargetApi(4)
final class BitmapHelperSdk4
  implements BitmapHelper.IHelper
{
  public final float convertUnitToPixel(Context paramContext, int paramInt, float paramFloat)
  {
    return TypedValue.applyDimension(paramInt, paramFloat, paramContext.getResources().getDisplayMetrics());
  }

  public final Bitmap decodeFile(String paramString, float paramFloat)
  {
    BitmapFactory.Options localOptions = new BitmapFactory.Options();
    float f = 160.0F * paramFloat;
    localOptions.inDensity = (int)f;
    Bitmap localBitmap = BitmapFactory.decodeFile(paramString, localOptions);
    if (localBitmap != null)
      localBitmap.setDensity((int)f);
    return localBitmap;
  }

  public final Bitmap decodeStream(InputStream paramInputStream)
  {
    BitmapFactory.Options localOptions = new BitmapFactory.Options();
    localOptions.inDensity = 160;
    localOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
    return BitmapFactory.decodeStream(paramInputStream, null, localOptions);
  }

  public final Bitmap decodeStream(InputStream paramInputStream, float paramFloat)
  {
    BitmapFactory.Options localOptions = new BitmapFactory.Options();
    localOptions.inDensity = (int)(160.0F * paramFloat);
    localOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
    return BitmapFactory.decodeStream(paramInputStream, null, localOptions);
  }

  public final float dip2px(Context paramContext, float paramFloat)
  {
    return convertUnitToPixel(paramContext, 1, paramFloat);
  }

  public final float px2dip(Context paramContext, float paramFloat)
  {
    return paramFloat / paramContext.getResources().getDisplayMetrics().density;
  }
}