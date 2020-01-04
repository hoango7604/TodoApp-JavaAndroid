package com.example.todoapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.todoapp.Activities.EditTaskActivity;
import com.example.todoapp.Models.Task;
import com.example.todoapp.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {
    private Context _context;
    private ArrayList<Task> _taskArrayList;

    public TaskAdapter(Context context, ArrayList<Task> taskArrayList) {
        this._context = context;
        this._taskArrayList = taskArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new MyViewHolder(LayoutInflater.from(_context).inflate(R.layout.item_task, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        final int id = _taskArrayList.get(position).getId();
        final String title = _taskArrayList.get(position).getTitle();
        final String description = _taskArrayList.get(position).getDesc();
        final String date = _taskArrayList.get(position).getDate();
        final String time = _taskArrayList.get(position).getTime();

        myViewHolder.itemTitle.setText(title);
        myViewHolder.itemDesc.setText(description);
        myViewHolder.itemDate.setText(date);
        myViewHolder.itemTime.setText(time);

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToEditTask = new Intent(_context, EditTaskActivity.class);
                goToEditTask.putExtra("id", id);
                goToEditTask.putExtra("title", title);
                goToEditTask.putExtra("description", description);
                goToEditTask.putExtra("date", date);
                goToEditTask.putExtra("time", time);
                _context.startActivity(goToEditTask);
            }
        });
    }

    @Override
    public int getItemCount() {
        return _taskArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView itemTitle, itemDesc, itemDate, itemTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.item_title);
            itemDesc = itemView.findViewById(R.id.item_desc);
            itemDate = itemView.findViewById(R.id.item_date);
            itemTime = itemView.findViewById(R.id.item_time);
        }
    }
}
