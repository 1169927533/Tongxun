package com.example.a11699.graduatemanager.zidingyiView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.a11699.graduatemanager.R;

public class CustomLineChart extends View {
    private int width; // 宽
    private int height; // 高
    private int pointNum; // 折点个数
    private int lineColor; // 折线颜色
    private int shadowColor; // 阴影颜色
    private float[] percents; // 百分比，可以理解为任务完成百分比
    private int itemWidth;
    private Path path; // 避免在 onDraw 方法内创建多个对象，所以在这里声明一个成员变量

    public CustomLineChart(Context context) {
        super(context);
    }

    public CustomLineChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomLineChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //首先通过obtainStyledAttributes方法获取typedarray
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.CustomLineChart);
        //这个是
        //typedarray给自定义的view添加属性
        //使用typedarray获取对应的属性
       // int paintColor = typedArray.getColor(R.styleable.ViewWithAttrs_paint_color, Color.BLACK);
        //最后的Color.BLACK是没获取到时的默认值
        lineColor=typedArray.getColor(R.styleable.CustomLineChart_lineColor, Color.BLACK);
        shadowColor = typedArray.getColor(R.styleable.CustomLineChart_shadowColor, Color.BLACK);
        pointNum = typedArray.getInteger(R.styleable.CustomLineChart_pointNum, 0);

        typedArray.recycle();//回收
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (pointNum == 0) {
            return;
        }
        if (percents == null || percents.length != pointNum) {
            return;
        }
        if (width == 0) {
            width = getMeasuredWidth();
        }
        if (height == 0) {
            height = getMeasuredHeight();
        }
        if (path == null) {
            path = new Path();
        }
        itemWidth = width/(pointNum-1);
        Paint paint = new Paint();
        paint.setStrokeWidth(5);
        // 阴影部分使用 Path 连接成一个封闭图形
        path.reset();
        path.moveTo(0, height);
        path.lineTo(0, height*(1-percents[0]));
        for (int i = 1; i < percents.length; i++) {
            // 连接阴影
            path.lineTo(itemWidth*i, height*(1-percents[i]));
        }
        path.lineTo(width, height);
        path.lineTo(0, height);
        paint.setColor(shadowColor);
        // 画阴影
        canvas.drawPath(path, paint);
        path.close();

        paint.setColor(lineColor);
        canvas.drawCircle(0, height*(1-percents[0]), 10, paint);
        for (int i = 1; i < percents.length; i++) {
            // 画线
            canvas.drawLine(itemWidth*(i-1), height*(1-percents[i-1]), itemWidth*i, height*(1-percents[i]), paint);
            // 画点
            canvas.drawCircle(itemWidth*i, height*(1-percents[i]), 10, paint);
        }

        canvas.save();

    }
    public void setPercents(float[] percents) {
        this.percents = percents;
        invalidate();
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public void setShadowColor(int shadowColor) {
        this.shadowColor = shadowColor;
    }

    public void setPointNum(int pointNum) {
        this.pointNum = pointNum;
    }

}















