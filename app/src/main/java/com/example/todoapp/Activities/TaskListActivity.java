package com.example.todoapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.todoapp.Adapters.TaskAdapter;
import com.example.todoapp.Models.Task;
import com.example.todoapp.R;
import com.example.todoapp.Utils.DatabaseHelper;

import java.util.ArrayList;

public class TaskListActivity extends AppCompatActivity {

    public TextView mainTitle, mainSubtitle, mainEnd;
    public Button btnAddNew;

    public RecyclerView mainTask;
    public ArrayList taskArrayList;
    public TaskAdapter taskAdapter;

    public DatabaseHelper databaseHelper;

    public String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        this._bindElements();
        this._initTextFont();
        this._bindEvents();
        this._retrieveData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this._retrieveData();
    }

    private void _retrieveData() {
        taskArrayList.clear();
        Cursor data = databaseHelper.GetData("SELECT * FROM TodoList WHERE date = '" + selectedDate + "'");
        while (data.moveToNext()) {
            int index = data.getInt(0);
            String title = data.getString(1);
            String description = data.getString(2);
            String date = data.getString(3);
            String time = data.getString(4);
            int alarmTime = data.getInt(5);
            Task task = new Task(index, title, description, date, time, alarmTime);
            taskArrayList.add(task);
        }
        taskAdapter.notifyDataSetChanged();
    }

    private void _bindEvents() {
        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToNewTask = new Intent(TaskListActivity.this, NewTaskActivity.class);
                goToNewTask.putExtra("date", selectedDate);
                startActivity(goToNewTask);
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
        mainEnd.setTypeface(MLight);
        btnAddNew.setTypeface(MLight);
    }

    private void _bindElements() {
        // bind elements
        mainTitle = findViewById(R.id.main_title);
        mainSubtitle = findViewById(R.id.main_subtitle);
        mainEnd = findViewById(R.id.main_end);
        btnAddNew = findViewById(R.id.btn_add_new);

        // working with data
        mainTask = findViewById(R.id.main_task);
        mainTask.setLayoutManager(new LinearLayoutManager(this));
        taskArrayList = new ArrayList();
        taskAdapter = new TaskAdapter(TaskListActivity.this, taskArrayList);
        mainTask.setAdapter(taskAdapter);

        // init database
        databaseHelper = new DatabaseHelper(this, "TodoApp.sqlite", null, 1);
        databaseHelper.AssertTableTodoListExist();

        // get date from intend
        Intent intent = getIntent();
        selectedDate = intent.getStringExtra("date");
//        Log.d("debug:task-list", "date: " + date);
    }
}
