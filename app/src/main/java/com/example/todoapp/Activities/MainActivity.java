package com.example.todoapp.Activities;

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
import android.widget.TextView;

import com.example.todoapp.Utils.DatabaseHelper;
import com.example.todoapp.R;
import com.example.todoapp.Models.Task;
import com.example.todoapp.Adapters.TaskAdapter;
import com.example.todoapp.Utils.NotificationHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public TextView mainTitle, mainSubtitle, mainEnd;
    public Button btnAddNew;

//    public DatabaseReference reference;
    public RecyclerView mainTask;
    public ArrayList taskArrayList;
    public TaskAdapter taskAdapter;

    public DatabaseHelper databaseHelper;

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
        taskArrayList.clear();
        Cursor data = databaseHelper.GetData("SELECT * FROM TodoList");
        while (data.moveToNext()) {
            int index = data.getInt(0);
            String title = data.getString(1);
            String description = data.getString(2);
            String date = data.getString(3);
            String time = data.getString(4);
            Task task = new Task(index, title, description, date, time);
            taskArrayList.add(task);
        }
        taskAdapter.notifyDataSetChanged();
    }

//    private void _retrieveData() {
//        // get data from firebase
//        reference = FirebaseDatabase.getInstance().getReference().child("TodoApp");
////        reference = FirebaseDatabase.getInstance().getReference("TodoApp");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // set code to retrieve data and replace layout
//                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
//                {
//                    Task task = dataSnapshot1.getValue(Task.class);
//                    taskArrayList.add(task);
//                }
////                taskAdapter = new TaskAdapter(MainActivity.this, taskArrayList);
////                mainTask.setAdapter(taskAdapter);
//                taskAdapter.notifyDataSetChanged();
//                Log.d("debug:main-retrieveData", "Can go here");
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // set code to show an error
//                Log.d("debug:main-retrieveData", "Error happened");
//                Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    private void _bindEvents() {
        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToNewTask = new Intent(MainActivity.this, NewTaskActivity.class);
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
        taskAdapter = new TaskAdapter(MainActivity.this, taskArrayList);
        mainTask.setAdapter(taskAdapter);

        // init database
        databaseHelper = new DatabaseHelper(this, "TodoApp.sqlite", null, 1);
        databaseHelper.AssertTableTodoListExist();

        // Test data
//        databaseHelper.QueryData("INSERT INTO TodoList VALUES (null, 'Task 2', 'Can gi lam nay', datetime('now'))");
    }
}
