package com.visiabletech.indidmobileapp.Pogo;

import android.os.Parcel;
import android.os.Parcelable;

public class SwitchChildInfo implements Parcelable {
    public String studentCode;
    public String fName;
    public String lName;
    public String studentId;

    public SwitchChildInfo(String fName, String lName, String studentId){

        this.fName = fName;
        this.lName = lName;
        this.studentId = studentId;
    }

    public SwitchChildInfo(Parcel in){

        this.fName = in.readString();
        this.lName = in.readString();
        this.studentId = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fName);
        dest.writeString(lName);
        dest.writeString(studentId);
    }

    public static final Creator<SwitchChildInfo> CREATOR = new Creator<SwitchChildInfo>() {
        public SwitchChildInfo createFromParcel(Parcel in) {
            return new SwitchChildInfo(in);
        }

        public SwitchChildInfo[] newArray(int size) {
            return new SwitchChildInfo[size];

        }
    };
}


