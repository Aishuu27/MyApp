package com.example.myapp;

import com.google.gson.JsonObject;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.Call;
public interface Postcall {

    @POST("api/v1/create")
    Call<JsonObject>  postcurrentdata(@Body JsonObject body );
}
