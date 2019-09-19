package com.xy.calendarfunction;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;

/**
 * Created by Xia_焱 on  2019/9/17.
 * 邮箱：XiahaotianV@163.com
 */
public class CalendarRecycleView <T extends BaseDateEntity> extends RecyclerView {

    private CalendarRecycleViewAdapter mAdapter;
    private Context                    mContext;
    private CalendarTool               mCalendarTool;
    private OnCalendarDateListener     mDateListener;
    private float                      motion_x;

    public CalendarRecycleView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public CalendarRecycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public CalendarRecycleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }


    private void init() {
        setLayoutManager(new StaggeredGridLayoutManager(7, StaggeredGridLayoutManager.VERTICAL));
        mCalendarTool = new CalendarTool();
        mAdapter = new CalendarRecycleViewAdapter(mContext, mCalendarTool.initDateList());

        setAdapter(mAdapter);
        mAdapter.setOnItemListener(new CalendarRecycleViewAdapter.OnItemListener() {
            @Override
            public void onItemClick(DateEntity dateEntity) {
                if (mDateListener != null) {
                    mDateListener.onDateItemClick(dateEntity);
                }
            }
        });
    }

    public void initRecordList(ArrayList<T> list) {
        mCalendarTool.initRecordList(list);
        mCalendarTool.initDateList();
        mAdapter.notifyDataSetChanged();
    }

    public void setOnCalendarDateListener(OnCalendarDateListener listener) {
        this.mDateListener = listener;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                motion_x = event.getX();
                Log.i("onTouchEvent", "ACTION_DOWN: " + event.getX() + "  " + motion_x);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i("onTouchEvent", "ACTION_MOVE: " + event.getX() + "  " + motion_x);
                float x = event.getX() - motion_x;
                if (Math.abs(x) > CalendarTool.FLING_MIN_DISTANCE && motion_x != 0) {
                    mCalendarTool.flushDate(x);
                    mAdapter.notifyDataSetChanged();
                    motion_x = 0;
                    if (mDateListener != null) {
                        mDateListener.onDateChange(mCalendarTool.getNowCalendar(),
                                mCalendarTool.getStartDay(),
                                mCalendarTool.getEndDay(),
                                mCalendarTool.isStartBelong(),
                                mCalendarTool.isEndBelong());
                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;

        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                motion_x = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(motion_x - event.getX()) >= mCalendarTool.FLING_MIN_DISTANCE) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(event);
    }


}
