package com.zige.robot.view.calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zige.robot.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

/**
 * Created by cracker on 2017/8/3.
 */

public class CalendarView extends View {
    public static final int TYPE_START = 1; //选择开始日期
    public static final int TYPE_STARTEND = 2;//开始结束日期都选择好
    private CustomBeam beam;
    private float itemHeight;
    private int itemSelectedColor, itemSelectedTextColor, defaultItemSize = 30;
    private int itemWidth;
    private CalendarSelectListenner listener;
    public static ArrayList<SelectedDate> clickedList = new ArrayList<>();
    private int curYear, curMonth, year, month;
    private int startI = -1, endI = -1;
    public static boolean isSelectBefore = false;

    public CalendarView(Context context) {
        super(context);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.customGridViewAttrs);
        itemHeight = ta.getDimension(R.styleable.customGridViewAttrs_itemHeight, 154);
        itemSelectedColor = ta.getColor(R.styleable.customGridViewAttrs_itemSelectedColor, Color.parseColor("#feba48"));
        itemSelectedTextColor = ta.getColor(R.styleable.customGridViewAttrs_itemSelectedTextColor, Color.WHITE);
        ta.recycle();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        itemWidth = getWidth() / 7;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        paint.setTextSize(defaultItemSize);
        startI = -1;
        endI = -1;
        Paint.FontMetricsInt itemMetrics = paint.getFontMetricsInt();
        //绘制点击按钮
        for (int i = 0; i < clickedList.size(); i++) {
            if (year == clickedList.get(i).getYear() && month == clickedList.get(i).getMonth()) {
                paint.setColor(itemSelectedColor);
                int xPos = clickedList.get(i).getX();
                int yPos = clickedList.get(i).getY();
                int iValue = (yPos - 1) * 7 + xPos - beam.spaceCount;
                if (i == 0) {
                    startI = iValue;
                } else {
                    endI = iValue;
                }
                RectF rectF = new RectF(xPos * itemWidth - itemWidth, yPos * itemHeight - itemHeight, xPos * itemWidth, yPos * itemHeight);
                canvas.drawRoundRect(rectF, 10, 10, paint);
            }
        }

        //绘制月份
        int offsetY = (int) itemHeight;
        int offsetX = itemWidth / 2 + beam.spaceCount * itemWidth;
        for (int i = 1; i <= beam.monthCount; i++) {
            paint.setColor(itemSelectedTextColor);
            if (endI == i) {
                paint.setTextSize(30);
                int textWidth = getTextWidth(paint, "结束");
                canvas.drawText("结束", offsetX - textWidth / 2, offsetY - 22 - (-itemMetrics.top + itemMetrics.bottom) / 2, paint);
                int textWidth1 = getTextWidth(paint, i + "");
                canvas.drawText("" + i, offsetX - textWidth1 / 2, offsetY - 22 - (-itemMetrics.top + itemMetrics.bottom) - 36, paint);
            } else if (startI == i) {
                paint.setTextSize(30);
                int textWidth = getTextWidth(paint, "开始");
                canvas.drawText("开始", offsetX - textWidth / 2, offsetY - 22 - (-itemMetrics.top + itemMetrics.bottom) / 2, paint);
                int textWidth1 = getTextWidth(paint, i + "");
                canvas.drawText("" + i, offsetX - textWidth1 / 2, offsetY - 22 - (-itemMetrics.top + itemMetrics.bottom) - 36, paint);
            } else {
                paint.setTextSize(42);
                int textWidth = getTextWidth(paint, i + "");
                if (beam.nextCount < i) {
                    paint.setColor(getResources().getColor(R.color.txtv_item_next));
                } else {
                    paint.setColor(getResources().getColor(R.color.txtv_item_black));
                }
                canvas.drawText("" + i, offsetX - textWidth / 2, offsetY - itemHeight / 2 + (-itemMetrics.top + itemMetrics.bottom) / 2, paint);
            }
            offsetX += itemWidth;
            //换行
            if ((beam.spaceCount + i) % 7 == 0) {
                offsetY += itemHeight;
                offsetX = itemWidth / 2;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        double d = (beam.spaceCount + beam.monthCount) / 7.0;
        double itemCount = Math.ceil(d);
        setMeasuredDimension(width, (int) (itemHeight * itemCount));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            float x = event.getX();
            float y = event.getY();
            if (!isSelectBefore) {
                if ((year == curYear && month >= curMonth) || year > curYear) {
                    drawBG(x, y);
                }
            } else {
                drawBG(x, y);
            }
        }
        return true;
    }

    /**
     * 绘制点击时间
     */
    private void drawBG(float x, float y) {
        float xF = x / itemWidth;
        float yF = y / itemHeight;
        int xPos = (int) Math.ceil(xF);
        int yPos = (int) Math.ceil(yF);
        int iValue = (yPos - 1) * 7 + xPos - beam.spaceCount;
        if (iValue <= 0 || iValue > beam.monthCount) {
            return;
        }
        if (!isSelectBefore) {
            if (beam.nextCount >= iValue) {
                return;
            }
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, iValue);
        clickedList.add(new SelectedDate(year, month, iValue, xPos, yPos, calendar));
        if (clickedList.size() > 2) {
            clickedList.clear();
            clickedList.add(new SelectedDate(year, month, iValue, xPos, yPos, calendar));
            if (null != listener) {
                String[] result = new String[1];
                SelectedDate startDate = clickedList.get(0);
                result[0] = startDate.getYear() + "-" + (startDate.getMonth() + 1) + "-" + startDate.getDay();
                listener.onSelected(TYPE_START, result);
            }
        } else if (clickedList.size() == 2) {
            Collections.sort(clickedList);
            if (null != listener) {
                String[] result = new String[2];
                SelectedDate startDate = clickedList.get(0);
                result[0] = startDate.getYear() + "-" + (startDate.getMonth() + 1) + "-" + startDate.getDay();
                SelectedDate endDate = clickedList.get(1);
                result[1] = endDate.getYear() + "-" + (endDate.getMonth() + 1) + "-" + endDate.getDay();
                listener.onSelected(TYPE_STARTEND, result);
            }
        } else {
            if (null != listener) {
                String[] result = new String[1];
                SelectedDate startDate = clickedList.get(0);
                result[0] = startDate.getYear() + "-" + (startDate.getMonth() + 1) + "-" + startDate.getDay();
                listener.onSelected(TYPE_START, result);
            }
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 设置选择监听
     *
     * @param listenner 监听回调
     */
    public void setSelectListenner(CalendarSelectListenner listenner) {
        this.listener = listenner;
    }

    CalendarRvAdapter adapter;

    public void setData(CustomBeam beam, CalendarRvAdapter adapter) {
        this.beam = beam;
        this.adapter = adapter;
        String[] curYearMonth = beam.curYearMonth.split(",");
        curYear = Integer.parseInt(curYearMonth[0]);
        curMonth = Integer.parseInt(curYearMonth[1]);
        String[] yearMonth = beam.yearMonth.split(",");
        year = Integer.parseInt(yearMonth[0]);
        month = Integer.parseInt(yearMonth[1]);
        invalidate();
        forceLayout();
    }

    public static int getTextWidth(Paint paint, String str) {
        int w = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                w += (int) Math.ceil(widths[j]);
            }
        }
        return w;
    }

//    /**
//     * 设置是否能点击之前的日期
//     */
//    public static void setSelectBefore(boolean isSelectBefore) {
//        isSelectBefore = isSelectBefore;
//    }
}
