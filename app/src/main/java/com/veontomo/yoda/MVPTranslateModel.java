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
    private final YodaApi yodaService;
    private final Callback<String> translateExec;
    private MVPPresenter mPresenter;
    /**
     * a phrase to translate
     */
    private String mPhrase;


    public MVPTranslateModel() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.YODA_SERVICE_URL)
                .addConverterFactory(new ToStringConverterFactory())
                .build();
        yodaService = retrofit.create(YodaApi.class);

        translateExec = new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    mPresenter.showTranslation(mPhrase, response.body());
                } else {
                    mPresenter.showTranslationProblem(mPhrase, response.body());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mPresenter.onTranslationFailure(mPhrase, t.getMessage());
            }
        };
    }


    public void setPresenter(final MVPPresenter presenter) {
        this.mPresenter = presenter;
    }


    /**
     * Request for the service to translate a given quote.
     *
     * @param txt text to translate
     */
    public void translate(final String txt) {
        this.mPhrase = txt;
        Call<String> call = yodaService.translate(txt);
        call.enqueue(translateExec);
    }


}
