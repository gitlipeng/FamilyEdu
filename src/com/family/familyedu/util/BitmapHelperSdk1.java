package com.family.familyedu.util;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.InputStream;

final class BitmapHelperSdk1
  implements BitmapHelper.IHelper
{
  public final float convertUnitToPixel(Context paramContext, int paramInt, float paramFloat)
  {
    return paramFloat;
  }

  public final Bitmap decodeFile(String paramString, float paramFloat)
  {
    return BitmapFactory.decodeFile(paramString);
  }

  public final Bitmap decodeStream(InputStream paramInputStream)
  {
    return BitmapFactory.decodeStream(paramInputStream);
  }

  public final Bitmap decodeStream(InputStream paramInputStream, float paramFloat)
  {
    return decodeStream(paramInputStream);
  }

  public final float dip2px(Context paramContext, float paramFloat)
  {
    return paramFloat;
  }

  public final float px2dip(Context paramContext, float paramFloat)
  {
    return paramFloat;
  }
}