package com.visiabletech.indidmobileapp.ApiInterface;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface
{

    @FormUrlEncoded
    @POST("student_reg")
    Call<String>register(@Field("phone_no") String phone,
                         @Field("name") String name,
                         @Field("class") String cls,
                         @Field("board") String board,
                         @Field("dob") String dob,
                         @Field("pincode") String pin_code,
                         @Field("area") String area,
                         @Field("city") String city,
                         @Field("address") String address,
                         @Field("mode_of_test") String mot,
                         @Field("workshop") String workshop,
                         @Field("user_token")String user_token);


    @FormUrlEncoded
    @POST("executive_reg")
    Call<String>executiveRegister(@Field("phone_no") String phone,
                         @Field("name") String name,
                         @Field("class") String cls,
                         @Field("board") String board,
                         @Field("dob") String dob,
                         @Field("pincode") String pin_code,
                         @Field("area") String area,
                         @Field("city") String city,
                         @Field("address") String address,
                         @Field("mode_of_test") String mot,
                         @Field("workshop") String workshop,
                         @Field("executive_id")String executive_id);




    @FormUrlEncoded
    @POST("student_reg_update")
    Call<String>saveProfile(@Field("student_id") String student_id,
                         @Field("name") String name,
                         @Field("email") String email,
                         @Field("class") String cls,
                         @Field("board") String board,
                         @Field("dob") String dob,
                         @Field("pincode") String pin_code,
                         @Field("area") String area,
                         @Field("city") String city,
                         @Field("address") String address,
                         @Field("mode_of_test") String mot,
                         @Field("workshop") String workshop);




    @FormUrlEncoded
    @POST("profile_img_update")
    Call<String> updateProfileImage(@Field("student_id") String student_id,
                                    @Field("image")String image);



    @FormUrlEncoded
    @POST("otp_send")
    Call<String> sendOTP(@Field("phone_no") String phoneNo);



    @FormUrlEncoded
    @POST("otp_check")
    Call<String> verifyOTP(@Field("phone_no") String phoneNo, @Field("otp") String otp);


}
