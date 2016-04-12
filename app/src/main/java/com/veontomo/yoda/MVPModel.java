package com.veontomo.yoda;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
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
    private final Retrofit retrofit2;
    private final YodaApi yodaService;

    public MVPModel(final MVPPresenter presenter) {
        this.retrofit2 = new Retrofit.Builder()
                .baseUrl(Config.YODA_SERVICE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        yodaService = retrofit2.create(YodaApi.class);


        this.mPresenter = presenter;

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
                Log.i(Config.appName, "enqueued for translation: " + s);
                Call<YodaMessage> call2 = yodaService.translate(s);
                call2.enqueue(new Callback<YodaMessage>() {
                                  @Override
                                  public void onResponse(Call<YodaMessage> call, Response<YodaMessage> response) {
                                      if (response.isSuccessful()) {
                                          Log.i("RT", "OK");
                                          YodaMessage message = response.body();
                                          onTranslated("translated what Yoda said" + message.message);


                                      } else {
                                          onTranslated("Yoda response failed");
                                      }
                                  }

                                  @Override
                                  public void onFailure(Call<YodaMessage> call, Throwable t) {
                                      onTranslated("Yoda failured: " + t.getMessage());
                                  }
                              }
                );

            }
        };
    }

    /**
     * This method gets called once the presenter receives a text to translate.
     *
     * @param text string to translate
     */
    public void onTranslate(String text, final MVPPresenter presenter) {
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
