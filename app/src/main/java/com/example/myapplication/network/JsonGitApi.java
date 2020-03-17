package com.example.myapplication.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JsonGitApi {
    @GET("/search/users?")
    Call<UserList> getUsers(@Query("q") String username,
                            @Query("page") Integer pageNum,
                            @Query("per_page") Integer perPage
    );

    @GET("/users/{username}/repos?sort=update")
    Call<List<Repository>> getRepository(@Path("username") String username);
}
