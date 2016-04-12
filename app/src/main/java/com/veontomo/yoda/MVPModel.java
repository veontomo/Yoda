package com.veontomo.yoda;

import android.util.Log;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Model class of the activity according to MVP approach
 */
public class MVPModel {
    private final Subscriber<String> mUserInputReceiver;
    private final MVPPresenter mPresenter;

    public MVPModel(final MVPPresenter presenter){
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
                Log.i(Config.appName, "enqueued for translation: "  + s);
                onTranslated("translated what Yoda said" + s);
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
     * @param s
     */
    private void onTranslated(final String s) {
        mPresenter.onTraslated(s);


    }
}
