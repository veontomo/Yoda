package com.veontomo.yoda;

import android.util.Log;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A model of the MVP architecture responsible for translations.
 */
public class MVPTranslateModel {
    private final YodaApi yodaService;
    private final Callback<String> translateExec;
    private MVPPresenter mPresenter;
    /**
     * a phrase to translate
     */
    private Quote mQuote;

    /**
     * a cache storing the translations
     */
    private HashMap<Quote, String> cache;

    private final int maxCacheSize = 10;

    public MVPTranslateModel() {
        cache = new HashMap<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.YODA_SERVICE_URL)
                .addConverterFactory(new ToStringConverterFactory())
                .build();
        yodaService = retrofit.create(YodaApi.class);

        translateExec = new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    onTranslated(response.body());
                    store(mQuote, response.body());
                } else {
                    onTranslationFailure(response.body());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i(Config.appName, "MVPTranslateModel failure: " + t.getMessage());
                onTranslationFailure(t.getMessage());
            }
        };
    }

    /**
     * Putting the key-value pair into the cache.
     *
     * @param key
     * @param value
     */
    private void store(Quote key, String value) {
        if (cache.size() >= maxCacheSize){
            Quote aKey = cache.entrySet().iterator().next().getKey();
            cache.remove(aKey);
        }
        cache.put(key, value);

    }

    public void setPresenter(final MVPPresenter presenter) {
        this.mPresenter = presenter;
    }

    /**
     * This method gets called once the phrase has been translated.
     *
     * @param s
     */
    private void onTranslated(final String s) {
        mPresenter.onTranslated(s);

    }

    /**
     * This method is called if the response from the Yoda API is not successful.
     */
    private void onTranslationFailure(final String s) {
        mPresenter.onTranslationFailure(s);
    }

    /**
     * Request for the service to translate a given quote.
     *
     * @param quote a quote to translate
     */
    public void translate(final Quote quote) {
        this.mQuote = quote;
        if (cache.containsKey(quote)) {
            onTranslated(cache.get(quote));
        } else {
            Call<String> call = yodaService.translate(quote.quote);
            call.enqueue(translateExec);
        }
    }


}
