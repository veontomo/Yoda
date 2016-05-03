package com.veontomo.yoda;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A model of the MVP architecture responsible for translations.
 */
public class MVPTranslateModel {
    private MVPPresenter mPresenter;
    private final YodaApi yodaService;
    private final Callback<String> translateExec;

    public void setPresenter(final MVPPresenter presenter) {
        this.mPresenter = presenter;
    }

    public MVPTranslateModel() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.YODA_SERVICE_URL)
                .addConverterFactory(new ToStringConverterFactory())
                .build();
        yodaService = retrofit.create(YodaApi.class);

        translateExec = new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i(Config.appName, response.body());
                if (response.isSuccessful()) {
                    onTranslated(response.body());
                } else {
                    onTranslationFailure(response.body());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i(Config.appName, "MVPTranslateModel failure: " + t.getMessage());
                onTranslationFailure(t.getMessage() + " " + call.request().toString());
            }
        };

//        Call<String> call2 = yodaService.translate(response.body().quote);
        Call<String> call2 = yodaService.translate("a sample to translate");
        call2.enqueue(translateExec);
    }


    /**
     * This method gets called once the phrase has been translated.
     *
     * @param s
     */
    private void onTranslated(final String s) {
        mPresenter.onTraslated(s);
    }

    /**
     * This method is called if the response from the Yoda API is not successful.
     */
    private void onTranslationFailure(final String s) {
        mPresenter.onTranslationFailure(s);
    }

    /**
     * Request for the service to translate the quote.
     *
     * @param quote a quote to translate
     */
    public void translate(Quote quote) {


    }
}
