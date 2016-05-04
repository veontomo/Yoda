package com.veontomo.yoda;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A model of the MVP architecture responsible for retrieval of the phrases.
 */
public class MVPRetrieveModel {
    private static final String TAG = Config.appName;
    public static final String CATEGORY_MOVIES = "movies";
    public static final String CATEGORY_FAMOUS = "famous";

    private MVPPresenter mPresenter;

    private final QuotesApi mQuoteRetrievalService;

    private final Callback<Quote> mQuoteCallback;

    private final List<Quote> cache;

    /**
     * max number of the elements that the cache might contain
     */
    private final int maxCacheSize = 10;

    /**
     * Category of the quote.
     * By default it is set to "movies".
     */
    private String mCategory = CATEGORY_MOVIES;


    public void setPresenter(final MVPPresenter presenter) {
        this.mPresenter = presenter;
    }

    /**
     * Constructor.
     */
    public MVPRetrieveModel() {

        cache = new ArrayList<>();

        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl(Config.QUOTES_SERVICE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mQuoteRetrievalService = retrofit2.create(QuotesApi.class);

        mQuoteCallback = new Callback<Quote>() {
            @Override
            public void onResponse(Call<Quote> call, Response<Quote> response) {
                if (response.isSuccessful()) {
                    onQuoteReceived(response.body());
                    store(response.body());
                } else {
                    if (!cache.isEmpty()) {
                        onQuoteReceived(getRandomFromCache());
                    } else {
                        onRetrieveFailure("Something wrong I have retrieved.");
                    }
                }
            }

            @Override
            public void onFailure(Call<Quote> call, Throwable t) {
                if (!cache.isEmpty()) {
                    onQuoteReceived(getRandomFromCache());
                } else {
                    onRetrieveFailure(t.getMessage());
                }
            }
        };

    }

    /**
     * Returns a random quote from the cache.
     *
     * @return
     */
    private Quote getRandomFromCache() {
        Random generator = new Random();
        int pos = generator.nextInt(cache.size());
        Log.i(TAG, "getRandomFromCache: returning quote n." + pos + " from cache of size " + cache.size() );
        return cache.get(pos);
    }

    /**
     * Stores the quote in the cache.
     * <p/>
     * If the cache becomes bigger than specified limit, the first element of the cache is removed.
     *
     * @param quote quote to put in the cache
     */
    private void store(Quote quote) {
        Log.i(TAG, "store: add quote to the cache");
        if (cache.size() >= maxCacheSize) {
            cache.remove(0);
        }
        cache.add(quote);
    }

    private void onRetrieveFailure(final String s) {
        mPresenter.onRetrieveResponseFailure(s);
    }

    /**
     * This method is called once a quote is received from the service
     *
     * @param quote
     */
    private void onQuoteReceived(final Quote quote) {
        mPresenter.onQuoteReceived(quote);
    }

    /**
     * Activates the retrieval of a quote.
     */
    public void retrieveQuote() {
        Log.i(TAG, "retrieveQuote: start");
        Call<Quote> call = mQuoteRetrievalService.getByCategory(mCategory);
        call.enqueue(mQuoteCallback);
    }

    /**
     * {@link #mCategory} setter
     *
     * @param category name of category
     */
    public void setCategory(String category) {
        this.mCategory = category;
    }


}
