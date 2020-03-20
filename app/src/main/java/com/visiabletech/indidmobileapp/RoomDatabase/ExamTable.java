package com.visiabletech.indidmobileapp.RoomDatabase;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "examTable", primaryKeys = {"exam_id", "question_id"})
public class ExamTable {


    @ColumnInfo(name = "exam_id")
    @NonNull
    private String exam_id;


    @ColumnInfo(name = "student_id")
    @NonNull
    private String student_id;


    @ColumnInfo(name = "question_id")
    @NonNull
    private String question_id;


    @ColumnInfo(name = "question")
    private String question;

    @ColumnInfo(name = "answer")
    private String answer;









    @NonNull
    public String getExam_id() {
        return exam_id;
    }

    public void setExam_id(@NonNull String exam_id) {
        this.exam_id = exam_id;
    }

    @NonNull
    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(@NonNull String student_id) {
        this.student_id = student_id;
    }

    @NonNull
    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(@NonNull String question_id) {
        this.question_id = question_id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }


}
