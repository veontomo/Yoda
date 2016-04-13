package com.veontomo.yoda;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * API of Yoda translation service
 */
public interface YodaApi {
    @Headers({
            "Accept: text/plain",
            "X-Mashape-Key: " + Config.YODA_API_KEY})
    @GET("yoda")
    Call<String> translate(@Query("sentence") String phrase);
}


