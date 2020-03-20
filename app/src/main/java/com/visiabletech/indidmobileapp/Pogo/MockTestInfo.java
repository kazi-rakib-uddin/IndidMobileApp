package com.visiabletech.indidmobileapp.Pogo;

import android.content.Context;

public class MockTestInfo {

    private Context context;
    private String mockTest;

    public MockTestInfo(Context context, String mockTest) {
        this.context = context;
        this.mockTest = mockTest;
    }


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getMockTest() {
        return mockTest;
    }

    public void setMockTest(String mockTest) {
        this.mockTest = mockTest;
    }
}
