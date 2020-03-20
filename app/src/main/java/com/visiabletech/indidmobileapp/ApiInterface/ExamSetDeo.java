package com.visiabletech.indidmobileapp.ApiInterface;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import com.visiabletech.indidmobileapp.RoomDatabase.ExamTable;

import java.util.List;

@Dao
public interface ExamSetDeo {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void addExamQuestion(List<ExamTable> examTable);



    @Query("select* from examTable")
    public List<ExamTable> retriveExamListSet();

    @Update
    public void updateExamList(ExamTable examTable);

    @Query("DELETE FROM examTable")
    public void nukeTable();


}
