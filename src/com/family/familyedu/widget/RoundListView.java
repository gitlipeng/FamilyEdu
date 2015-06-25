package com.family.familyedu.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.widget.ListView;

public class RoundListView extends ListView
{
  public final float RADIUS = 18.0F;
  public int SeletedColor = 1140850943;
  private int _h;
  private int _w;
  public int background = -5588020;
  private Drawable bg;
  private Path mClip;
  public int stroke = -1;

  public RoundListView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    init();
  }

  private void init()
  {
    GradientDrawable localGradientDrawable = new GradientDrawable();
    localGradientDrawable.setStroke(1, this.stroke);
    localGradientDrawable.setCornerRadius(18.0F);
    localGradientDrawable.setColor(this.background);
    setBackgroundDrawable(localGradientDrawable);
    setCacheColorHint(0);
    setVerticalFadingEdgeEnabled(false);
    StateListDrawable localStateListDrawable = new StateListDrawable();
    int[] arrayOfInt1 = { 16842919, 16842909 };
    GradientDrawable.Orientation localOrientation = GradientDrawable.Orientation.LEFT_RIGHT;
    int[] arrayOfInt2 = new int[2];
    arrayOfInt2[0] = this.SeletedColor;
    arrayOfInt2[1] = this.SeletedColor;
    localStateListDrawable.addState(arrayOfInt1, new GradientDrawable(localOrientation, arrayOfInt2));
    setSelector(localStateListDrawable);
  }

  protected void dispatchDraw(Canvas paramCanvas)
  {
    paramCanvas.save();
    paramCanvas.clipPath(this.mClip);
    super.dispatchDraw(paramCanvas);
    paramCanvas.restore();
  }

  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    this._w = paramInt1;
    this._h = paramInt2;
    this.mClip = new Path();
    RectF localRectF = new RectF(0.0F, 0.0F, paramInt1, paramInt2);
    this.mClip.addRoundRect(localRectF, 18.0F, 18.0F, Path.Direction.CW);
  }
}