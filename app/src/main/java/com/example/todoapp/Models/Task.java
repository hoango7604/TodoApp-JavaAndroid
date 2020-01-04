package com.example.todoapp.Models;

public class Task {
    private int _id;
    private String _title;
    private String _desc;
    private String _date;
    private String _time;

    public Task() {}

    public Task(int id, String title, String desc, String date, String time) {
        this._id = id;
        this._title = title;
        this._desc = desc;
        this._date = date;
        this._time = time;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public String getTitle() {
        return _title;
    }

    public void setTitle(String title) {
        this._title = title;
    }

    public String getDate() {
        return _date;
    }

    public void setDate(String date) {
        this._date = date;
    }

    public String getDesc() {
        return _desc;
    }

    public void setDesc(String desc) {
        this._desc = desc;
    }

    public String getTime() {
        return _time;
    }

    public void setTime(String time) {
        this._time = time;
    }
}
