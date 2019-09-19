package com.xy.calendarfunction;

import android.graphics.Point;

/**
 * Created by Xia_焱 on  2019/9/17.
 * 邮箱：XiahaotianV@163.com
 */
public interface OnCalendarDateListener {
    void onDateChange(Point nowCalendar, int startDay, int endDay, boolean startBelong, boolean endBelong);
    void onDateItemClick(DateEntity dateEntity);
}
