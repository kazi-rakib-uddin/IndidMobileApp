package com.visiabletech.indidmobileapp.Pogo;

import android.content.Context;
import android.content.SharedPreferences;

public class User {



    private String mobile,pass,username,id,role;

    private Context context;

    public SharedPreferences sharedPreferences;


    public User(Context context) {

        this.context=context;

        sharedPreferences=context.getSharedPreferences("login_details", Context.MODE_PRIVATE);

    }

    public String getMobile() {

        mobile=sharedPreferences.getString("mobile","");
        return mobile;
    }

    public void setMobile(String mobile) {

        sharedPreferences.edit().putString("mobile",mobile).commit();

        this.mobile = mobile;
    }

    public String getPass() {

        pass=sharedPreferences.getString("pass","");
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;

        sharedPreferences.edit().putString("pass",pass).commit();

    }

    public String getUsername() {

        username=sharedPreferences.getString("username","");
        return username;
    }

    public void setUsername(String username) {
        this.username = username;

        sharedPreferences.edit().putString("username",username).commit();

    }

    public String getId() {

        id=sharedPreferences.getString("id","");
        return id;
    }

    public void setId(String id) {
        this.id = id;

        sharedPreferences.edit().putString("id",id).commit();

    }


    public String getRole() {

        role=sharedPreferences.getString("role","");
        return role;
    }


    public void setRole(String role) {
        this.role = role;

        sharedPreferences.edit().putString("role",role).commit();

    }



    public  void  remove(){


        sharedPreferences.edit().clear().commit();

    }

}
