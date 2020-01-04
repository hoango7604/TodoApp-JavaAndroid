package com.example.todoapp.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.todoapp.Models.Task;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra("id", 0);
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        String date = intent.getStringExtra("date");
        String time = intent.getStringExtra("time");

        NotificationHelper.displayNotification(context, new Task(id, title, description, date, time));
    }
}
