package com.veontomo.yoda;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * API of Yoda translation service
 *
 */
public interface YodaApi {
    @Headers("X-Mashape-Key: " + Config.YODA_API_KEY)
    @GET("yoda")
    Call<YodaMessage> translate(@Query("sentence") String phrase);
}


