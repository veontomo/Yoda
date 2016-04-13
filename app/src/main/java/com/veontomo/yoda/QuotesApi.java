package com.veontomo.yoda;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * API for quote retrieval
 *
 */
public interface QuotesApi {
    @Headers({
            "Accept: application/json",
            "X-Mashape-Key: " + Config.YODA_API_KEY})
    @GET("")
    Call<Quote> getByCategory(@Query("cat") String category);
}
