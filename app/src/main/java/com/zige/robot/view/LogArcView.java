package com.zige.robot.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

import com.zige.robot.R;

/**
 * Created by lidingwei on 2017/5/1 0001.
 */
public class LogArcView extends View {


    private float borderWidth = 35f;
    private int[] colorList = {R.color.color_ffa000, R.color.color_0ac1c5, R.color.color_fe5365, R.color.color_5039fb, R.color.color_22b4ff};
    private ValueAnimator mValueAnimator;

    public LogArcView(Context context) {
        super(context);
        intView();
    }

    public LogArcView(Context context, AttributeSet attrs) {
        super(context, attrs);
        intView();
    }

    public LogArcView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        intView();
    }

    Paint paint;
    private void intView(){
        paint = new Paint();
        paint.setAntiAlias(true); //打开抗锯齿
        paint.setStyle(Paint.Style.STROKE); //描边
        paint.setStrokeCap(Paint.Cap.ROUND); //画笔的样式，描边型
        paint.setStrokeJoin(Paint.Join.ROUND); //结合处为圆形
        paint.setStrokeWidth(borderWidth);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth() / 2;
        if(mData ==null){
            return;
        }
        if(mData.size()>5){
            return;
        }
        RectF rectF =null;
        for (int pos =0; pos<mData.size(); pos++){
            rectF = new RectF(borderWidth * (pos+1) +10*pos , borderWidth *(pos+1) + 10*pos , 2*width - borderWidth*(pos+1) - 10*pos, 2*width - borderWidth*(pos+1) -10*pos);
            float angle = Float.parseFloat(mData.get(pos)) * 360;
            drawArcItem(pos, canvas, rectF, colorList[pos], 0,angle);
        }

    }

    private void drawArcItem(int pos,Canvas canvas,RectF rectF,int color, float startAngle,float sweepAngle){
        paint.setColor(getResources().getColor(color));
        if(pos == colorList.length - 1){
            paint.setStyle(Paint.Style.FILL);
            canvas.drawArc(rectF, -90, sweepAngle * currentParams, true, paint);
        }else {
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawArc(rectF, -90 + 40*pos, sweepAngle* currentParams, false, paint);
        }

    }

    List<String> mData = null;
    public void setArcList(List<String> data){
        this.mData = data;
        startAnimation(); //一定时间内完成图形绘制动画
//        invalidate();
    }

    float currentParams;
    private void startAnimation(){
        mValueAnimator = ValueAnimator.ofFloat(0, 1);
        mValueAnimator.setDuration(1 * 1000);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentParams = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        mValueAnimator.start();
    }

}