package com.xy.calendarfunction;

import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * @author zed
 * @version 获取日历数据工具类
 */
public class CalendarTool<T extends BaseDateEntity> {

	private final String TAG = CalendarTool.class.getSimpleName();

	public static int FLING_MIN_DISTANCE = 100;

	private final int[] weekDayRow = {0, 1, 2, 3, 4, 5, 6};

	private ArrayList<DateEntity> mDataList = new ArrayList<>();//日期数组
	private ArrayList<T> mRecordList;//事件记录数组
	private DateEntity   mDateEntity;
	private int          mYear;
	private int          mMonth;

	private boolean mEndBelong;
	private boolean mStartBelong;
	private int     mStartDay;
	private int     mEndDay;

	/**
	 * 当前年月日
	 */
	private int mCurrenYear;
	private int mCurrenMonth;
	private int mCurrenDay;

	/**
	 * 平年月天数数组
	 */
	int commonYearMonthDay[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	/**
	 * 闰年月天数数组
	 */
	int leapYearMonthDay[]   = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

	public CalendarTool() {
		/** 初始化当前系统的日期 */
		Calendar calendar = Calendar.getInstance();
		mCurrenYear = calendar.get(Calendar.YEAR);
		mCurrenMonth = calendar.get(Calendar.MONTH) + 1;
		mCurrenDay = calendar.get(Calendar.DAY_OF_MONTH);
		this.mYear = mCurrenYear;
		this.mMonth = mCurrenMonth;
	}

	/**
	 * 获取当前日历的年月 x为年，y为月
	 */
	public Point getNowCalendar() {
		Point p = new Point(mYear, mMonth);
		return p;
	}

	/**
	 * 判断第一天属不属于本月
	 */
	public boolean isStartBelong() {
		return mStartBelong;
	}

	/**
	 * 判断最后一天属不属于本月
	 */
	public boolean isEndBelong() {
		return mEndBelong;
	}

	/**
	 * 获取日历第一天的日期
	 */
	public int getStartDay() {
		return mStartDay;
	}

	/**
	 * 获取日历最后一天的日期
	 */
	public int getEndDay() {
		return mEndDay;
	}

	public ArrayList<DateEntity> initDateList() {
		return initDateList(mYear, mMonth);
	}

	public void initRecordList(ArrayList<T> recordList) {
		mRecordList = recordList;
	}

	/**
	 * 通过年月获取当前页面的日期集合
	 */
	private ArrayList<DateEntity> initDateList(int year, int month) {

		Log.i(TAG, "initDateList: year = " + year + " month = " + month);

		mDataList.clear();

		/** 修改部分 */
		int endDate = 0;// 得到上一个月的天数，作为上一个月在本日历的结束日期
		if ((year - 1) == this.mYear || month == 1) {// 说明向前翻了一年，那么上个月的天数就应该是上一年的12月的天数，或者到翻到一月份的时候，那么上一个月的天数也是上一年的12月份的天数
			endDate = this.getDays(year - 1, 12);
		} else {// 得到上一个月的天数，作为上一个月在本日历的结束日期
			endDate = this.getDays(year, month - 1);
		}
		/** 修改部分结束 */

		this.mYear = year;// 当前日历上显示的年
		this.mMonth = month;// 当前日历上显示的月

		int days = this.getDays(year, month);// 得到本月的总共天数
		int dayOfWeek = this.getWeekDay(year, month);//得到当前年月的第一天为星期几
		int selfDaysEndWeek = 0;// 本月的最后一天是星期几

		mStartBelong = true;

		/** 先添加前面不属于本月的 */
		if (dayOfWeek != 0) {
			int startDate = endDate - dayOfWeek + 1;// 当前月的上一个月在本日历的开始日期
			for (int i = startDate, j = 0; i <= endDate; i++, j++) {

				mDateEntity = new DateEntity(year, month - 1, i);
				mDateEntity.date = mDateEntity.year * 10000 + mDateEntity.month * 100 + i;
				if (startDate == i) {
					mStartBelong = false;
					mStartDay = mDateEntity.date;
				}

				mDateEntity.isSelfMonthDate = false;
				mDateEntity.weekDay = weekDayRow[j];
				mDateEntity.hasRecord = hasRecord(mDateEntity.date);
				mDataList.add(mDateEntity);
			}
		}

		/** 添加本月的 */
		for (int i = 1, j = dayOfWeek; i <= days; i++, j++) {

			mDateEntity = new DateEntity(year, month, i);
			mDateEntity.date = mDateEntity.year * 10000 + mDateEntity.month * 100 + i;
			if (mStartBelong && i == 1) {
				mStartDay = mDateEntity.date;
			}
			if (i == days) {
				mEndDay = mDateEntity.date;
			}
			mDateEntity.isSelfMonthDate = true;
			if (j >= 7) {
				j = 0;
			}
			selfDaysEndWeek = j;
			mDateEntity.weekDay = weekDayRow[j];
			if (year == mCurrenYear && month == mCurrenMonth && i == mCurrenDay) {
				mDateEntity.isNowDate = true;
			}
			mDateEntity.hasRecord = hasRecord(mDateEntity.date);
			mDataList.add(mDateEntity);
		}

		mEndBelong = true;

		/*** 添加后面下一个月的 */
		for (int i = 1, j = selfDaysEndWeek + 1; i < 7; i++, j++) {

			if (j >= 7) {
				break;
			}
			mEndBelong = false;

			mDateEntity = new DateEntity(year, month + 1, i);

			if (mDateEntity.month > 12) {
				mDateEntity.year = year + 1;
				mDateEntity.month = 1;
			}
			mDateEntity.date = mDateEntity.year * 10000 + mDateEntity.month * 100 + i;
			mDateEntity.isSelfMonthDate = false;
			mDateEntity.weekDay = weekDayRow[j];
			mDateEntity.hasRecord = hasRecord(mDateEntity.date);
			mDataList.add(mDateEntity);

			mEndDay = mDateEntity.year * 10000 + mDateEntity.month * 100 + i;
		}
		return mDataList;
	}

	/**
	 * 通过年月，获取这个月一共有多少天
	 */
	private int getDays(int year, int month) {
		int days = 0;

		if ((year % 4 == 0 && (year % 100 != 0)) || (year % 400 == 0)) {
			if (month > 0 && month <= 12) {
				days = leapYearMonthDay[month - 1];
			}
		} else {
			if (month > 0 && month <= 12) {
				days = commonYearMonthDay[month - 1];
			}
		}
		return days;
	}

	private boolean hasRecord(int date) {
		if (mRecordList != null) {
			for (T baseDateEntity : mRecordList) {
				if (baseDateEntity.year * 10000 + baseDateEntity.month * 100 + baseDateEntity.day == date) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 通过年，月获取当前月的第一天为星期几 ，返回0是星期天，1是星期一，依次类推
	 */
	private int getWeekDay(int year, int month) {
		int dayOfWeek;
		int goneYearDays = 0;
		int thisYearDays = 0;
		boolean isLeapYear = false;//闰年
		int commonYearMonthDay[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
		int leapYearMonthDay[] = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
		for (int i = 1900; i < year; i++) {// 从1900年开始算起，1900年1月1日为星期一
			if ((i % 4 == 0 && (i % 100 != 0)) || (i % 400 == 0)) {
				goneYearDays = goneYearDays + 366;
			} else {
				goneYearDays = goneYearDays + 365;
			}
		}
		if ((year % 4 == 0 && (year % 100 != 0)) || (year % 400 == 0)) {
			isLeapYear = true;
			for (int i = 0; i < month - 1; i++) {
				thisYearDays = thisYearDays + leapYearMonthDay[i];
			}
		} else {
			isLeapYear = false;
			for (int i = 0; i < month - 1; i++) {
				thisYearDays = thisYearDays + commonYearMonthDay[i];
			}
		}
		dayOfWeek = (goneYearDays + thisYearDays + 1) % 7;

		Log.d(this.getClass().getName(), "从1990到现在有" + (goneYearDays + thisYearDays + 1) + "天");
		Log.d(this.getClass().getName(), year + "年" + month + "月" + 1 + "日是星期" + dayOfWeek);
		return dayOfWeek;
	}

	public void flushDate(float distance_x) {
		if (distance_x < 0) {// Fling right
			if (mMonth + 1 > 12) {
				mDataList = initDateList(mYear + 1, 1);
			} else {
				mDataList = initDateList(mYear, mMonth + 1);
			}
		} else {// Fling left
			if (mMonth - 1 <= 0) {
				mDataList = initDateList(mYear - 1, 12);
			} else {
				mDataList = initDateList(mYear, mMonth - 1);
			}
		}
	}
}
