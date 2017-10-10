package com.zige.robot.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.zige.robot.R;


/**一个圆形百分比进度 View
 * 用于展示简易的图标
 * Created by Administrator on 2015/12/16.
 */
public class CirclePercentView extends View {

    private ValueAnimator progressAnimator;
    //要画的弧度
    private float mCurrentAngle;
    //外围弧的宽度
    private int borderWith = 15;

    Paint orangePaint;
    Paint grayPaint;
    //是否在画弧
    boolean isRunning=false;

    public CirclePercentView(Context context) {
        this(context, null);
        initView();
    }

    public CirclePercentView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        initView();
    }

    public CirclePercentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();

    }

    private void initView(){
        orangePaint = new Paint();
        orangePaint.setAntiAlias(true); //打开抗锯齿
        orangePaint.setStyle(Paint.Style.FILL);
        orangePaint.setColor(getResources().getColor(R.color.color_ffab1f)); //内圆

        grayPaint = new Paint();
        grayPaint.setAntiAlias(true);
        grayPaint.setStyle(Paint.Style.STROKE);
        grayPaint.setStrokeWidth(borderWith);
        grayPaint.setColor(getResources().getColor(R.color.tv_777)); //外围圈


    }

    @Override
    protected void onDraw(Canvas canvas) {
        int radius = getWidth() / 2;
        //内层圆
        orangePaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(radius, radius, (float) (radius*0.7), orangePaint );

//        if (isRunning){
//            //外层圆弧
//            RectF rectF = new RectF(borderWith, borderWith, 2*radius - borderWith, 2*radius - borderWith);
//            canvas.drawArc(rectF, 0 ,360, false, orangePaint);
//        }else {
            //外层圆弧
            RectF rectF = new RectF(borderWith, borderWith, 2*radius - borderWith, 2*radius - borderWith);
            canvas.drawArc(rectF, 0 ,360, false, grayPaint);
            //进度条
            orangePaint.setStyle(Paint.Style.STROKE);
            orangePaint.setStrokeWidth(borderWith);
            RectF rectF1 = new RectF(borderWith, borderWith, 2*radius - borderWith, 2*radius - borderWith);
            canvas.drawArc(rectF1, 270 ,mCurrentAngle, false, orangePaint);
            Log.d("mEndAngle:" , mCurrentAngle+"");
//        }


    }

    /**
     * 为进度设置动画
     * @param start
     * @param end
     */
    public void startAnimation(float start, float end, long length) {
        isRunning=true;
        progressAnimator = ValueAnimator.ofFloat(start, end);
        progressAnimator.setDuration(length);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrentAngle = (float) animation.getAnimatedValue();
                if(mCurrentAngle >=360){
                    isRunning=false;
                    if(listener==null)
                        return;
                    listener.onEndAnimation(); //已经达到了360
                }
                invalidate();
            }
        });
        progressAnimator.start();
    }

    /**
     * 圆弧重新归为0
     */
    public void setRestView(){
        if(progressAnimator!=null && progressAnimator.isRunning()){
            progressAnimator.cancel();
        }
        mCurrentAngle =0;
        isRunning=false;
        invalidate();
    }


    /**
     * 设置360度圆动画结束监听器
     * @param listener
     */
    public void setOnEndListener(CircleAnimationEndListener listener){
        this.listener = listener;
    }

    CircleAnimationEndListener listener;
    public interface CircleAnimationEndListener{
            void onEndAnimation();
    }



}
