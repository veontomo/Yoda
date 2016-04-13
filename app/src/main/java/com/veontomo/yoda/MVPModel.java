package com.veontomo.yoda;

import android.util.Log;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.ScalarsConverterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Model class of the activity according to MVP approach
 */
public class MVPModel {
    private final Subscriber<String> mUserInputReceiver;
    private final MVPPresenter mPresenter;
    private final YodaApi yodaService;
    private final QuotesApi quoteService;
    private final Callback<String> callback;
    private final Callback<Quote> callback2;

    /**
     * Constructor.
     *
     * @param presenter
     */
    public MVPModel(final MVPPresenter presenter) {
        this.mPresenter = presenter;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.YODA_SERVICE_URL)
                .addConverterFactory(new ToStringConverterFactory())
//                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        yodaService = retrofit.create(YodaApi.class);

        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl(Config.QUOTES_SERVICE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        quoteService = retrofit2.create(QuotesApi.class);

        callback = new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i(Config.appName, response.body());
                if (response.isSuccessful()) {
                    onTranslated(response.body());
                } else {
                    onTranslationFailure();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                onTranslated("Yoda fail: " + t.getMessage() + call.request().toString());
            }
        };

        callback2 = new Callback<Quote>() {
            @Override
            public void onResponse(Call<Quote> call, Response<Quote> response) {
                Log.i(Config.appName, "quote on response");
                Log.i(Config.appName, response.body().quote + " by " + response.body().author);
                if (response.isSuccessful()) {
                    Log.i(Config.appName, "response is successful");
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
//                Log.i(Config.appName, "enqueued for translation: " + s);
//                Call<String> call = yodaService.translate(s);
//                call.enqueue(callback);

                Log.i(Config.appName, "retrieve the quote");
                Call<Quote> call2 = quoteService.getByCategory("movie");
                call2.enqueue(callback2);
            }
        };


    }

    /**
     * This method is called if the response from the Yoda API is not successful.
     */
    private void onTranslationFailure() {
        mPresenter.onTranslationFailure();
    }

    /**
     * This method gets called once the presenter receives a text to translate.
     *
     * @param text string to translate
     */
    public void onTranslate(String text) {
        Log.i(Config.appName, "text: " + text + " is received.");
        Observable.just(text).map(new Func1<String, String>() {
            @Override
            public String call(String s) {
                return s.trim();
            }
        }).subscribe(mUserInputReceiver);

    }

    /**
     * This method gets called once the phrase has been translated.
     *
     * @param s
     */
    private void onTranslated(final String s) {
        mPresenter.onTraslated(s);


    }
}
