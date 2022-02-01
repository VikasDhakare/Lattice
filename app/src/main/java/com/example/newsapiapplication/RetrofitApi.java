package com.example.newsapiapplication;

import com.example.newsapiapplication.Models.NewsModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface RetrofitApi {
    @GET("top-headlines")
    Call<NewsModel> getNews(
            @Query("country") String country ,
            @Query("category") String category ,
            @Query("q") String query ,
            @Query("apiKey") String api_key
    );
}
