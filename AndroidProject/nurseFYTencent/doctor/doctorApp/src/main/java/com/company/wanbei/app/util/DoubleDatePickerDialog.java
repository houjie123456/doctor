package com.company.wanbei.app.util;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import androidx.appcompat.app.AlertDialog;

import com.company.wanbei.app.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class DoubleDatePickerDialog extends AlertDialog implements DialogInterface.OnClickListener, DatePicker.OnDateChangedListener {

    private static final String START_YEAR = "start_year";
    private static final String END_YEAR = "end_year";
    private static final String START_MONTH = "start_month";
    private static final String END_MONTH = "end_month";
    private static final String START_DAY = "start_day";
    private static final String END_DAY = "end_day";

    private final DatePicker mDatePicker_start;
    private final DatePicker mDatePicker_end;
    private final OnDateSetListener mCallBack;

    /**
     * The callback used to indicate the user is done filling in the date.
     */
    public interface OnDateSetListener {

        void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth,
                       DatePicker endDatePicker, int endYear, int endMonthOfYear, int endDayOfMonth);
    }

    /**
     * @param context
     *      The context the dialog is to run in.
     * @param callBack
     *      How the parent is notified that the date is set.
     * @param year
     *      The initial year of the dialog.
     * @param monthOfYear
     *      The initial month of the dialog.
     * @param dayOfMonth
     *      The initial day of the dialog.
     */
    public DoubleDatePickerDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth,int syear, int smonthOfYear,
                                  int sdayOfMonth) {
        this(context, 0, callBack, year, monthOfYear, dayOfMonth, syear, smonthOfYear, sdayOfMonth);
    }

    public DoubleDatePickerDialog(Context context, int theme, OnDateSetListener callBack, int year, int monthOfYear,
                                  int dayOfMonth,int syear, int smonthOfYear,
                                  int sdayOfMonth) {
        this(context, 0, callBack, year, monthOfYear, dayOfMonth, syear, smonthOfYear, sdayOfMonth, true);
    }

    /**
     * @param context
     *      The context the dialog is to run in.
     * @param theme
     *      the theme to apply to this dialog
     * @param callBack
     *      How the parent is notified that the date is set.
     * @param syear
     *      The initial year of the dialog.
     * @param smonthOfYear
     *      The initial month of the dialog.
     * @param sdayOfMonth
     *      The initial day of the dialog.
     */
    public DoubleDatePickerDialog(Context context, int theme, OnDateSetListener callBack, int syear, int smonthOfYear,
                                  int sdayOfMonth, int eyear, int emonthOfYear,
                                  int edayOfMonth,boolean isDayVisible) {
        super(context, theme);

        mCallBack = callBack;

        Context themeContext = getContext();
        setButton(BUTTON_POSITIVE, "??? ???", this);
        setButton(BUTTON_NEGATIVE, "??? ???", this);
        // setButton(BUTTON_POSITIVE,
        // themeContext.getText(android.R.string.date_time_done), this);
        setIcon(0);

        LayoutInflater inflater = (LayoutInflater) themeContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.date_pick_dialog, null);
        setView(view);
        mDatePicker_start = (DatePicker) view.findViewById(R.id.datePickerStart);
        mDatePicker_end = (DatePicker) view.findViewById(R.id.datePickerEnd);

        resizePicker(mDatePicker_start);
        resizePicker(mDatePicker_end);

        mDatePicker_start.init(syear, smonthOfYear, sdayOfMonth, this);
        mDatePicker_end.init(eyear, emonthOfYear, edayOfMonth, this);
        // updateTitle(year, monthOfYear, dayOfMonth);

        // ??????????????????????????????????????????????????????
        if (!isDayVisible) {
            hidDay(mDatePicker_start);
            hidDay(mDatePicker_end);
        }


    }
    /**
     * ??????FrameLayout?????????
     * */
    private void resizePicker(FrameLayout tp){        //DatePicker???TimePicker?????????FrameLayout
        List<NumberPicker> npList = findNumberPicker(tp);  //???????????????NumberPicker
        for(NumberPicker np:npList){
            resizeNumberPicker(np);      //????????????NumberPicker?????????
        }
    }
    /**
     * ??????viewGroup ?????????numberpicker??????
     * */
    private List<NumberPicker> findNumberPicker(ViewGroup viewGroup) {
        List<NumberPicker> npList = new ArrayList<NumberPicker>();
        View child = null;
        if(null != viewGroup){
            for(int i=0;i<viewGroup.getChildCount();i++){
                child = viewGroup.getChildAt(i);
                if(child instanceof NumberPicker){
                    npList.add((NumberPicker)child);
                }else if(child instanceof LinearLayout){
                    List<NumberPicker> result = findNumberPicker((ViewGroup) child);
                    if(result.size()>0){
                        return result;
                    }
                }
            }
        }
        return npList;
    }

    /**
     * ??????numberpicker??????
     * */
    private void resizeNumberPicker(NumberPicker np){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(10,5,10,5);
        np.setLayoutParams(params);
    }


    /**
     * ??????DatePicker??????????????????
     *
     * @param mDatePicker
     */
    private void hidDay(DatePicker mDatePicker) {
        Field[] datePickerfFields = mDatePicker.getClass().getDeclaredFields();
        for (Field datePickerField : datePickerfFields) {
            if ("mDaySpinner".equals(datePickerField.getName())) {
                datePickerField.setAccessible(true);
                Object dayPicker = new Object();
                try {
                    dayPicker = datePickerField.get(mDatePicker);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                // datePicker.getCalendarView().setVisibility(View.GONE);
                ((View) dayPicker).setVisibility(View.GONE);
            }
        }
    }

    public void onClick(DialogInterface dialog, int which) {
        // Log.d(this.getClass().getSimpleName(), String.format("which:%d",
        // which));
        // ??????????????? ?????????????????????????????????????????? ??????????????????????????????
        if (which == BUTTON_POSITIVE)
            tryNotifyDateSet();
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int month, int day) {
        if (view.getId() == R.id.datePickerStart)
            mDatePicker_start.init(year, month, day, this);
        if (view.getId() == R.id.datePickerEnd)
            mDatePicker_end.init(year, month, day, this);
        // updateTitle(year, month, day);
    }

    /**
     * ?????????????????????DatePicker
     *
     * @return The calendar view.
     */
    public DatePicker getDatePickerStart() {
        return mDatePicker_start;
    }

    /**
     * ?????????????????????DatePicker
     *
     * @return The calendar view.
     */
    public DatePicker getDatePickerEnd() {
        return mDatePicker_end;
    }

    /**
     * Sets the start date.
     *
     * @param year
     *      The date year.
     * @param monthOfYear
     *      The date month.
     * @param dayOfMonth
     *      The date day of month.
     */
    public void updateStartDate(int year, int monthOfYear, int dayOfMonth) {
        mDatePicker_start.updateDate(year, monthOfYear, dayOfMonth);
    }

    /**
     * Sets the end date.
     *
     * @param year
     *      The date year.
     * @param monthOfYear
     *      The date month.
     * @param dayOfMonth
     *      The date day of month.
     */
    public void updateEndDate(int year, int monthOfYear, int dayOfMonth) {
        mDatePicker_end.updateDate(year, monthOfYear, dayOfMonth);
    }

    private void tryNotifyDateSet() {
        if (mCallBack != null) {
            mDatePicker_start.clearFocus();
            mDatePicker_end.clearFocus();
            mCallBack.onDateSet(mDatePicker_start, mDatePicker_start.getYear(), mDatePicker_start.getMonth(),
                    mDatePicker_start.getDayOfMonth(), mDatePicker_end, mDatePicker_end.getYear(),
                    mDatePicker_end.getMonth(), mDatePicker_end.getDayOfMonth());
        }
    }

    @Override
    protected void onStop() {
        // tryNotifyDateSet();
        super.onStop();
    }

    @Override
    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        state.putInt(START_YEAR, mDatePicker_start.getYear());
        state.putInt(START_MONTH, mDatePicker_start.getMonth());
        state.putInt(START_DAY, mDatePicker_start.getDayOfMonth());
        state.putInt(END_YEAR, mDatePicker_end.getYear());
        state.putInt(END_MONTH, mDatePicker_end.getMonth());
        state.putInt(END_DAY, mDatePicker_end.getDayOfMonth());
        return state;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int start_year = savedInstanceState.getInt(START_YEAR);
        int start_month = savedInstanceState.getInt(START_MONTH);
        int start_day = savedInstanceState.getInt(START_DAY);
        mDatePicker_start.init(start_year, start_month, start_day, this);

        int end_year = savedInstanceState.getInt(END_YEAR);
        int end_month = savedInstanceState.getInt(END_MONTH);
        int end_day = savedInstanceState.getInt(END_DAY);
        mDatePicker_end.init(end_year, end_month, end_day, this);

    }
}
