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
    public static final String CATEGORY_MOVIES = "movies";
    public static final String CATEGORY_FAMOUS = "famous";
    private static final String TAG = Config.appName;

    private MVPPresenter mPresenter;

    private final QuotesApi mPhraseRetrievalService;

    private final Callback<Quote> callback;

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
        mPhraseRetrievalService = retrofit2.create(QuotesApi.class);

        callback = new Callback<Quote>() {
            @Override
            public void onResponse(Call<Quote> call, Response<Quote> response) {
                Log.i(Config.appName, "quote on response");

                if (response.isSuccessful()) {
                    Log.i(Config.appName, "response is successful");
                    onQuoteReceived(response.body());

                } else {
                    Log.i(Config.appName, "response is not successful");
                }
            }

            @Override
            public void onFailure(Call<Quote> call, Throwable t) {
                Log.i(Config.appName, "Quote response fail: " + t.getMessage() + "\n" + call.request().toString());
            }
        };

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
        Call<Quote> call = mPhraseRetrievalService.getByCategory(mCategory);
        call.enqueue(callback);
    }

    /**
     * {@link #mCategory} setter
     * @param category name of category
     */
    public void setCategory(String category) {
        this.mCategory = category;
    }





}
