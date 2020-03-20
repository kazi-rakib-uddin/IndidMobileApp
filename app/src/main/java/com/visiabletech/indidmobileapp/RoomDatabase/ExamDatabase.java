package com.visiabletech.indidmobileapp.RoomDatabase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.visiabletech.indidmobileapp.ApiInterface.ExamSetDeo;

@Database(entities = {ExamTable.class},version = 4)
public abstract class ExamDatabase extends RoomDatabase {

    public abstract ExamSetDeo examSetDeo();
}
