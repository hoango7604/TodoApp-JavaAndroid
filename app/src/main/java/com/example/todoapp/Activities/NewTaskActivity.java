package com.example.todoapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.todoapp.Fragments.DatePickerFragment;
import com.example.todoapp.Fragments.TimePickerFragment;
import com.example.todoapp.Models.Task;
import com.example.todoapp.Utils.DatabaseHelper;
import com.example.todoapp.R;
import com.example.todoapp.Utils.NotificationHelper;

import java.text.DateFormat;
import java.util.Calendar;

public class NewTaskActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    public TextView newTaskTitle, taskTitleLabel, taskDescLabel, taskDateLabel, taskTimeLabel;
    public EditText taskTitle, taskDesc, taskDate, taskTime;
    public Button btnCreate, btnCancel;

    public DatabaseHelper databaseHelper;

    public Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        
        this._bindElements();
        this._initTextFont();
        this._bindEvents();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        
        _updateDateText();
    }

    private void _updateDateText() {
        String dateText = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime());
        taskDate.setText(dateText);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        _updateTimeText();
    }

    private void _updateTimeText() {
        String timeText = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
        taskTime.setText(timeText);
    }

    private void _bindEvents() {
        taskDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DialogFragment datePicker = new DatePickerFragment();
                    datePicker.show(getSupportFragmentManager(), DatePickerFragment.DATE_PICKER_TAG);
                }
            }
        });

        taskDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), DatePickerFragment.DATE_PICKER_TAG);
            }
        });

        taskTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DialogFragment timePicker = new TimePickerFragment();
                    timePicker.show(getSupportFragmentManager(), TimePickerFragment.TIME_PICKER_TAG);
                }
            }
        });

        taskTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), TimePickerFragment.TIME_PICKER_TAG);
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = taskTitle.getText().toString().trim();
                String description = taskDesc.getText().toString().trim();
                String date = taskDate.getText().toString().trim();
                String time = taskTime.getText().toString().trim();
                if (title.isEmpty() || date.isEmpty() || time.isEmpty()) {
                    Toast.makeText(NewTaskActivity.this, "Please fill in missing fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                databaseHelper.QueryData("INSERT INTO TodoList VALUES (null, '"+ title +"', '" + description + "', '" + date + "', '" + time + "')");
                Cursor data = databaseHelper.GetData("SELECT last_insert_rowid()");
                if (data.moveToNext()) {
                    int id = data.getInt(0);
                    Task task = new Task(id, title, description, date, time);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    NotificationHelper.scheduleBroadcast(NewTaskActivity.this, alarmManager, task, calendar.getTimeInMillis());
                }
                _backToMainActivity();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _backToMainActivity();
            }
        });
    }

    private void _backToMainActivity() {
        finish();
    }

    private void _initTextFont() {
        // import font
        Typeface MLight = Typeface.createFromAsset(getAssets(), "fonts/ML.ttf");
        Typeface MMedium = Typeface.createFromAsset(getAssets(), "fonts/MM.ttf");

        // customize font
        newTaskTitle.setTypeface(MMedium);

        taskTitleLabel.setTypeface(MLight);
        taskTitle.setTypeface(MMedium);

        taskDescLabel.setTypeface(MLight);
        taskDesc.setTypeface(MMedium);

        taskDateLabel.setTypeface(MLight);
        taskDate.setTypeface(MMedium);

        taskTimeLabel.setTypeface(MLight);
        taskTime.setTypeface(MMedium);

        btnCreate.setTypeface(MMedium);
        btnCancel.setTypeface(MLight);
    }

    private void _bindElements() {
        newTaskTitle = findViewById(R.id.new_task_title);

        taskTitleLabel = findViewById(R.id.task_title_label);
        taskDescLabel = findViewById(R.id.task_desc_label);
        taskDateLabel = findViewById(R.id.task_date_label);
        taskTimeLabel = findViewById(R.id.task_time_label);

        taskTitle = findViewById(R.id.task_title);
        taskDesc = findViewById(R.id.task_desc);
        taskDate = findViewById(R.id.task_date);
        taskTime = findViewById(R.id.task_time);

        btnCreate = findViewById(R.id.btn_create);
        btnCancel = findViewById(R.id.btn_cancel);

        // init database
        databaseHelper = new DatabaseHelper(this, "TodoApp.sqlite", null, 1);
        databaseHelper.AssertTableTodoListExist();

        // init date
        calendar = Calendar.getInstance();
    }
}
