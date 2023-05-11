package com.cumn.blueaccount;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CurrencyApi {
    @GET("latest/{base}")
    Call<CurrencyResponse> getCurrencyRates(@Path("base") String base);
}
