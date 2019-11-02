package com.polacik.david.cleanerplanner.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.CalendarView;

import com.polacik.david.cleanerplanner.R;
import com.polacik.david.cleanerplanner.constant.IntentConstant;

import java.util.Calendar;

public class CalendarActivity extends Activity {

    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(layoutParams);

        calendarView = findViewById(R.id.calendarView);


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int i, int i1, int i2) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(i, i1, i2);
                int numberWeek = calendar.get(Calendar.WEEK_OF_YEAR);

                String selectDateString = i2 + "." + (i1 + 1) + "." + i;

                Intent sendSelectDate = new Intent();
                sendSelectDate.putExtra(IntentConstant.KEY_CLIENTADDCALENDARDATE, selectDateString);
                sendSelectDate.putExtra(IntentConstant.KEY_CALENDARWEEK, numberWeek);
                sendSelectDate.putExtra(IntentConstant.KEY_CALENDARYEAR, i);
                setResult(Activity.RESULT_OK, sendSelectDate);

                finish();

            }
        });

    }
}
