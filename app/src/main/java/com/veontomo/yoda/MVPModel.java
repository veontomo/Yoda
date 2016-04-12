package com.veontomo.yoda;

import android.util.Log;

import rx.Observable;
import rx.Subscriber;

/**
 * Model class of the activity according to MVP approach
 */
public class MVPModel {
    private final Subscriber<String> mUserInputReceiver;

    public MVPModel(){

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
        Observable.just(text).subscribe(mUserInputReceiver);
        onTranslated("translated what Yoda said" + text, presenter);
    }

    /**
     * This method gets called once the phrase has been translated.
     * @param s
     * @param presenter
     */
    private void onTranslated(final String s, final MVPPresenter presenter) {
        presenter.onTraslated(s);


    }
}
