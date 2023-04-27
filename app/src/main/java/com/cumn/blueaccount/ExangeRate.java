package com.cumn.blueaccount;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ExangeRate {
    @GET("latest")
    Call<ExangerateResponse> getLatestExangeRates(@Query("base") String base,@Query("cambio") String cambio);
}
