package com.example.todoapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.todoapp.Utils.DatabaseHelper;
import com.example.todoapp.R;
import com.example.todoapp.Models.Task;
import com.example.todoapp.Adapters.TaskAdapter;
import com.example.todoapp.Utils.NotificationHelper;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public CalendarView calendarView;
    public TextView mainTitle, mainSubtitle, mainTaskCount;
    public Button btnViewTaskList;

    public DatabaseHelper databaseHelper;

    public Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this._bindElements();
        this._initTextFont();
        this._bindEvents();
        this._retrieveData();
        this._createNotificationChannel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this._retrieveData();
    }

    private void _createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            NotificationChannel channel = new NotificationChannel(NotificationHelper.CHANNEL_ID, NotificationHelper.CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(NotificationHelper.CHANNEL_DESCRIPTION);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void _retrieveData() {
        Cursor data = databaseHelper.GetData("SELECT COUNT(*) FROM TodoList WHERE date = '" + _getDateFormat(calendar.getTime()) + "'");
        if (data.moveToNext()) {
            int taskCount = data.getInt(0);
            mainTaskCount.setText("You have " + taskCount + " task(s) to do");
        }
    }

    private void _bindEvents() {
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                Log.d("debug:main", "date: " + DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime()));
                _retrieveData();
            }
        });

        btnViewTaskList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToTaskList = new Intent(MainActivity.this,  TaskListActivity.class);
                goToTaskList.putExtra("date", _getDateFormat(calendar.getTime()));
                startActivity(goToTaskList);
            }
        });
    }

    private void _initTextFont() {
        // import font
        Typeface MLight = Typeface.createFromAsset(getAssets(), "fonts/ML.ttf");
        Typeface MMedium = Typeface.createFromAsset(getAssets(), "fonts/MM.ttf");

        // customize font
        mainTitle.setTypeface(MMedium);
        mainSubtitle.setTypeface(MLight);
        mainTaskCount.setTypeface(MLight);
        btnViewTaskList.setTypeface(MLight);
    }

    private void _bindElements() {
        // bind elements
        mainTitle = findViewById(R.id.main_title);
        mainSubtitle = findViewById(R.id.main_subtitle);
        mainTaskCount = findViewById(R.id.main_task_count);
        btnViewTaskList = findViewById(R.id.btn_view_task_list);

        calendarView = findViewById(R.id.calendar_view);

        // init database
        databaseHelper = new DatabaseHelper(this, "TodoApp.sqlite", null, 1);
        databaseHelper.AssertTableTodoListExist();

        // Test data
//        databaseHelper.QueryData("INSERT INTO TodoList VALUES (null, 'Task 2', 'Can gi lam nay', datetime('now'))");

        // Set current date to calendar
        calendar = Calendar.getInstance();
//        Log.d("debug:main", "date: " + DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime()));
    }

    private String _getDateFormat(Date date) {
        return DateFormat.getDateInstance(DateFormat.SHORT).format(date.getTime());
    }
}
