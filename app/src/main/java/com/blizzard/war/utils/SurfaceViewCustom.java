package com.blizzard.war.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.SurfaceView;

public class SurfaceViewCustom extends SurfaceView {

    public float width1;
    public float height1;

    public SurfaceViewCustom(Context context) {
        super(context);
    }

    public void setMeasure(float width, float height) {
        this.width1 = width;
        this.height1 = height;
    }

    public SurfaceViewCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SurfaceViewCustom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SurfaceViewCustom(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (this.width1 > 0) {
            width = (int) width1;
        }
        if (this.height1 > 0) {
            height = (int) height1;
        }
        setMeasuredDimension(width, height);
    }
}