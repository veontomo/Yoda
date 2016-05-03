package com.veontomo.yoda;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;

/**
 * A model of the MVP architecture responsible for retrieval of the phrases.
 */
public class MVPRetrieveModel {
    public static final String CATEGORY_MOVIES = "movies";
    public static final String CATEGORY_FAMOUS = "famous";

    private final Subscriber<String> mUserInputReceiver;
    private MVPPresenter mPresenter;

    private final QuotesApi mPhraseRetrievalService;

    private final Callback<Quote> mCallback;

    /**
     * Category of the quote.
     * By default it is set to "movies".
     */
    private String mCategory = CATEGORY_MOVIES;


    public void setPresenter(final MVPPresenter presenter){
        this.mPresenter = presenter;
    }

    /**
     * Constructor.
     *
     */
    public MVPRetrieveModel() {
        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl(Config.QUOTES_SERVICE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mPhraseRetrievalService = retrofit2.create(QuotesApi.class);

        mCallback = new Callback<Quote>() {
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


        mUserInputReceiver = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.i(Config.appName, "is over");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(Config.appName, "an error occurred: " + e.getMessage());

            }

            @Override
            public void onNext(String s) {
                Log.i(Config.appName, "retrieve the quote from category " + mCategory);
                Call<Quote> call2 = mPhraseRetrievalService.getByCategory(mCategory);
                call2.enqueue(mCallback);
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
     * This method gets called once the presenter receives a text to translate.
     *
     * @param text string to translate
     */
    public void onTranslate(String text) {
        Log.i(Config.appName, "text: " + text + " is received.");
        Observable.just(text).subscribe(mUserInputReceiver);

    }


    public void setCategory(String category) {
        this.mCategory = category;
        Log.i(Config.appName, "Set category to " + category);

    }


}
