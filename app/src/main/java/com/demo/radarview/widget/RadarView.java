package com.demo.radarview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Author: ZhengHuaizhi
 * Date: 2019/12/23
 * Description: 雷达图
 */
public class RadarView extends View {

    // 雷达图最小半径和最大半径
    private float mMinRadius, mMaxRadius;
    // 雷达图中心点
    private float mCenterX, mCenterY;
    // 雷达图层级数
    private int mLevel;
    // 多边形边数
    private int mSideCount;
    // 多边形外角
    private double mOuterCorner;
    // 绘制线路
    private Path mPath;
    // 多边形画笔，各属性值点画笔，属性覆盖区域画笔，属性名称画笔
    private Paint mLinePaint, mPointPaint, mAreaPaint, mPropertyNamePaint;

    private LegendProperty mData;

    private float mPropertyValueMax;

    private ArrayList<Point> mPointList;


    public RadarView(Context context) {
        this(context, null);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initVariables();
        initPaints();
    }

    private void initVariables() {
        mLevel = 5;
        mSideCount = 6;
        mOuterCorner = 360.0 / mSideCount;
        mPropertyValueMax = 1000;

        mPath = new Path();
        mData = new LegendProperty();
        mPointList = new ArrayList<>();
    }

    private void initPaints() {
        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(0xffbdbdbd);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(3);

        mPointPaint = new Paint();
        mPointPaint.setAntiAlias(true);
        mPointPaint.setColor(Color.TRANSPARENT);

        mAreaPaint = new Paint();
        mAreaPaint.setAntiAlias(true);
        mAreaPaint.setColor(0xff304ffe);
        mAreaPaint.setStyle(Paint.Style.FILL);

        mPropertyNamePaint = new Paint();
        mPropertyNamePaint.setAntiAlias(true);
        mPropertyNamePaint.setColor(Color.BLACK);
        mPropertyNamePaint.setTextSize(sp2px(getContext(), 14));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mMaxRadius = Math.min(w, h) * 0.8f / 2;
        mMinRadius = mMaxRadius / mLevel;
        mCenterX = w / 2;
        mCenterY = h / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.translate(mCenterX, mCenterY);

        drawPolygons(canvas);

        drawLines(canvas);

        drawProperties(canvas);

        drawArea(canvas);
    }

    /**
     * 绘制多边形
     */
    private void drawPolygons(Canvas canvas) {
        for (int level = 1; level <= mLevel; level++) {
            mPath.reset();
            for (int i = 0; i < mSideCount + 1; i++) {
                drawPoints(canvas, level, i);
            }
        }
    }

    /**
     * 绘制多边形顶角点,连接顶角
     */
    private void drawPoints(Canvas canvas, int level, int i) {
        float px =
                (float) (mMinRadius * level * Math.cos(Math.toRadians(270.0 - i * mOuterCorner)));
        float py =
                (float) (mMinRadius * level * Math.sin(Math.toRadians(270.0 - i * mOuterCorner)));
        if (i == 0) {
            mPath.moveTo(px, py);
        } else {
            mPath.lineTo(px, py);
        }
        canvas.drawPath(mPath, mLinePaint);
    }

    /**
     * 绘制多边形内径
     */
    private void drawLines(Canvas canvas) {
        for (int i = 0; i < mOuterCorner; i++) {
            mPath.reset();
            mPath.moveTo(0, 0);
            float px = (float) (mMaxRadius * Math.cos(Math.toRadians(270.0 - i * mOuterCorner)));
            float py = (float) (mMaxRadius * Math.sin(Math.toRadians(270.0 - i * mOuterCorner)));
            mPath.lineTo(px, py);
            canvas.drawPath(mPath, mLinePaint);
        }
    }

    /**
     * 绘制属性值点和名称
     */
    private void drawProperties(Canvas canvas) {
        int index = 0;
        HashMap<String, Float> hashMap = mData.getPropertyMap();
        Set<Map.Entry<String, Float>> set = hashMap.entrySet();

        for (Map.Entry<String, Float> entry : set) {
            // 属性值和名称
            float value = entry.getValue();
            String propertyName = entry.getKey();
            // 属性值占该属性最大值的百分百
            float percent = value / mPropertyValueMax;
            // 属性最大值x、y坐标
            double angdeg = 270.0 - index * mOuterCorner;
            float pxMax = (float) (mMaxRadius * Math.cos(Math.toRadians(angdeg)));
            float pyMax = (float) (mMaxRadius * Math.sin(Math.toRadians(angdeg)));
            // 当前该属性值x、y坐标
            float px = percent * pxMax;
            float py = percent * pyMax;
            // 描点、记录点
            canvas.drawCircle(px, py, 4, mPointPaint);
            mPointList.add(index, new Point(px, py));

            // 绘制属性名称
            float distanceX = mPropertyNamePaint.measureText(propertyName);
            float distanceY = mPropertyNamePaint.descent() - mPropertyNamePaint.ascent();
            if (angdeg <= 0.0) {
                canvas.drawText(propertyName, pxMax, pyMax, mPropertyNamePaint);
            } else if (angdeg <= 90.0) {
                canvas.drawText(propertyName, pxMax, pyMax + distanceY, mPropertyNamePaint);
            } else if (angdeg <= 180.0) {
                canvas.drawText(propertyName, pxMax - distanceX, pyMax + distanceY, mPropertyNamePaint);
            } else if (angdeg <= 270.0){
                canvas.drawText(propertyName, pxMax - distanceX, pyMax, mPropertyNamePaint);
            }
            index++;
        }
    }

    /**
     * 绘制属性区域
     */
    private void drawArea(Canvas canvas) {
        mPath.reset();
        for (Point point : mPointList) {
            int index = mPointList.indexOf(point);
            if (index == 0) {
                mPath.moveTo(point.px, point.py);
            } else {
                mPath.lineTo(point.px, point.py);
            }
        }
        // 闭合path
        mPath.close();
        canvas.drawPath(mPath, mAreaPaint);
    }

    public void setData(LegendProperty legendProperty) {
        mData = legendProperty;
        invalidate();
    }

    private class Point {
        float px;
        float py;

        public Point(float px, float py) {
            this.px = px;
            this.py = py;
        }
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
