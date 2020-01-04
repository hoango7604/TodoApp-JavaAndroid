package com.example.todoapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.todoapp.R;
import com.example.todoapp.Utils.DatabaseHelper;

public class ViewTaskActivity extends AppCompatActivity {

    public int taskId;

    public TextView viewTaskTitle, taskTitleLabel, taskDescLabel, taskDateLabel, taskTimeLabel;
    public EditText taskTitle, taskDesc, taskDate, taskTime;

//    public DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);

        this._bindElements();
        this._initTextFont();
    }

    private void _initTextFont() {
        // import font
        Typeface MLight = Typeface.createFromAsset(getAssets(), "fonts/ML.ttf");
        Typeface MMedium = Typeface.createFromAsset(getAssets(), "fonts/MM.ttf");

        // customize font
        viewTaskTitle.setTypeface(MMedium);

        taskTitleLabel.setTypeface(MLight);
        taskTitle.setTypeface(MMedium);

        taskDescLabel.setTypeface(MLight);
        taskDesc.setTypeface(MMedium);

        taskDateLabel.setTypeface(MLight);
        taskDate.setTypeface(MMedium);

        taskTimeLabel.setTypeface(MLight);
        taskTime.setTypeface(MMedium);
    }

    private void _bindElements() {
        viewTaskTitle = findViewById(R.id.view_task_title);

        taskTitleLabel = findViewById(R.id.task_title_label);
        taskDescLabel = findViewById(R.id.task_desc_label);
        taskDateLabel = findViewById(R.id.task_date_label);
        taskTimeLabel = findViewById(R.id.task_time_label);

        taskTitle = findViewById(R.id.task_title);
        taskDesc = findViewById(R.id.task_desc);
        taskDate = findViewById(R.id.task_date);
        taskTime = findViewById(R.id.task_time);

        // get values from database
        int id = getIntent().getIntExtra("id", 0);
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String date = getIntent().getStringExtra("date");
        String time = getIntent().getStringExtra("time");

        // fill in fields
        taskId = id;
        taskTitle.setText(title);
        taskDesc.setText(description);
        taskDate.setText(date);
        taskTime.setText(time);

//        // init database
//        databaseHelper = new DatabaseHelper(this, "TodoApp.sqlite", null, 1);
//        databaseHelper.AssertTableTodoListExist();
    }
}
