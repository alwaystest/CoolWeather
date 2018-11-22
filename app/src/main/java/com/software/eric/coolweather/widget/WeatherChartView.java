package com.software.eric.coolweather.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import com.software.eric.coolweather.R;
import com.software.eric.coolweather.util.LogUtil;

import java.util.ArrayList;

/**
 * Created by Mzz on 2016/2/24.
 */
public class WeatherChartView extends View {

    private static final String TAG = "WeatherChartView";

    private static final int verMargin = 200;
    private static final int horMargin = 10;
    private static final int tempTextOffset = 20;

    private ArrayList<Integer> maxTemperature = new ArrayList<>();
    private ArrayList<Integer> minTemperature = new ArrayList<>();
    private int dailyMinTemp;
    private int dailyMaxTemp;
    private ArrayList<String> xLabels = new ArrayList<>();
    Paint textPaint;
    Paint max;
    Paint min;
    Paint line;

    public WeatherChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        textPaint = new Paint();
        max = new Paint();
        min = new Paint();
        line = new Paint();
        line.setColor(Color.WHITE);
        line.setStrokeWidth(5);
        line.setStyle(Paint.Style.STROKE);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(40);
        textPaint.setTextAlign(Paint.Align.CENTER);
        xLabels.add(getContext().getString(R.string.no_data));
        max.setColor(Color.WHITE);
        min.setColor(Color.WHITE);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        // TODO: 2016/2/24 optimise
        super.onDraw(canvas);
        int height = getHeight();
        int width = getWidth();
        int textWidth = (width - (2 * horMargin)) / 7;
        int pointOffset = textWidth / 2;
        int graphHeight = height - 2 * verMargin;
        int tempRange = dailyMaxTemp - dailyMinTemp;
        int everageHeight = graphHeight / (tempRange == 0 ? 1 : tempRange);
        LogUtil.d(TAG, dailyMaxTemp + "dailyMax");
        LogUtil.d(TAG, dailyMinTemp + "dailyMin");
        int textStart = horMargin + (textWidth / 2);

        for (int i = 0; i < xLabels.size() && i < minTemperature.size(); i++) {
            String s = xLabels.get(i);
            canvas.drawText(s, textStart, height - verMargin / 2, textPaint);
            canvas.drawCircle(textStart, height - verMargin - ((minTemperature.get(i) - dailyMinTemp) * everageHeight), 10, min);
            canvas.drawCircle(textStart, height - verMargin - ((maxTemperature.get(i) - dailyMinTemp) * everageHeight), 10, max);
            if (i > 0) {
                canvas.drawLine(textStart - textWidth,
                        height - verMargin - ((minTemperature.get(i - 1) - dailyMinTemp) * everageHeight),
                        textStart,
                        height - verMargin - ((minTemperature.get(i) - dailyMinTemp) * everageHeight),
                        line);
                LogUtil.d(TAG, "min");
                canvas.drawLine(textStart - textWidth,
                        height - verMargin - ((maxTemperature.get(i - 1) - dailyMinTemp) * everageHeight),
                        textStart,
                        height - verMargin - ((maxTemperature.get(i) - dailyMinTemp) * everageHeight),
                        line);
                LogUtil.d(TAG, "max");
            }
            canvas.drawText(minTemperature.get(i) + getResources().getString(R.string.degree),
                    textStart,
                    height - verMargin - ((minTemperature.get(i) - dailyMinTemp) * everageHeight) + 2 * tempTextOffset,
                    textPaint);
            canvas.drawText(maxTemperature.get(i) + getResources().getString(R.string.degree),
                    textStart,
                    height - verMargin - ((maxTemperature.get(i) - dailyMinTemp) * everageHeight) - tempTextOffset,
                    textPaint);
            textStart += textWidth;
        }
        LogUtil.d(TAG, "-----------------------");
    }

/*    public void setData(WeatherInfoBean weatherInfoBean) {
        if (weatherInfoBean == null) {
            return;
        }
        xLabels.clear();
        maxTemperature.clear();
        minTemperature.clear();
        dailyMaxTemp = weatherInfoBean.getDaily_forecast()[0].getTmp().getMax();
        dailyMinTemp = weatherInfoBean.getDaily_forecast()[0].getTmp().getMin();
        for (DailyForecast d : weatherInfoBean.getDaily_forecast()) {
            String date = d.getDate();
            xLabels.add(date.substring(date.length() - 5));
            int min = d.getTmp().getMin();
            int max = d.getTmp().getMax();
            minTemperature.add(min);
            maxTemperature.add(max);
            dailyMinTemp = dailyMinTemp < min ? dailyMinTemp : min;
            dailyMaxTemp = dailyMaxTemp > max ? dailyMaxTemp : max;
        }
    }*/
}
