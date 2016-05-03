package com.veontomo.yoda;

import android.util.Log;

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
                } else {
                    onRetrieveFailure("Something wrong I have retrieved.");
                }
            }

            @Override
            public void onFailure(Call<Quote> call, Throwable t) {
                onRetrieveFailure(t.getMessage());
            }
        };

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
