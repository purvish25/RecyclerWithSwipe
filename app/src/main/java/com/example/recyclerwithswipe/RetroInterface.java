package com.example.recyclerwithswipe;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by ppurv on 27-12-2017.
 */

public interface RetroInterface {

    @GET("/users")
    Call<List<ModelClass>> getJson();

    @GET("/users")
    Observable<List<ModelClass>> getUsers();
}
