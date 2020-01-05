package com.example.todoapp.Utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.todoapp.Activities.ViewTaskActivity;
import com.example.todoapp.Models.Task;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationHelper {
    public static String CHANNEL_ID = "TodoApp_Notification";
    public static String CHANNEL_NAME = "TodoApp";
    public static String CHANNEL_DESCRIPTION = "TodoApp Notification";

    public static void scheduleBroadcast(Context context, AlarmManager alarmManager, Task task, long timeInMillis) {
        Intent intent = new Intent(context, AlertReceiver.class);
        intent.putExtra("id", task.getId());
        intent.putExtra("title", task.getTitle());
        intent.putExtra("description", task.getDesc());
        intent.putExtra("date", task.getDate());
        intent.putExtra("time", task.getTime());
        intent.putExtra("alarm_time", task.getAlarmTime());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                task.getId(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
    }

    public static void cancelBroadcast(Context context, AlarmManager alarmManager, Task task) {
        Intent intent = new Intent(context, AlertReceiver.class);
        intent.putExtra("id", task.getId());
        intent.putExtra("title", task.getTitle());
        intent.putExtra("description", task.getDesc());
        intent.putExtra("date", task.getDate());
        intent.putExtra("time", task.getTime());
        intent.putExtra("alarm_time", task.getAlarmTime());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                task.getId(),
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
        );

        alarmManager.cancel(pendingIntent);
    }

    public static void displayNotification(Context context, Task task) {
        Intent intent = new Intent(context, ViewTaskActivity.class);
        intent.putExtra("id", task.getId());
        intent.putExtra("title", task.getTitle());
        intent.putExtra("description", task.getDesc());
        intent.putExtra("date", task.getDate());
        intent.putExtra("time", task.getTime());
        intent.putExtra("alarm_time", task.getAlarmTime());
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                task.getId(),
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_popup_reminder)
                .setContentTitle(task.getTitle())
                .setContentText(task.getDesc())
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(task.getId(), builder.build());
    }
}
