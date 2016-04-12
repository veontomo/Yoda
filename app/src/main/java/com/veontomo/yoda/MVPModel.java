package com.veontomo.yoda;

import android.util.Log;

/**
 * Model class of the activity according to MVP approach
 */
public class MVPModel {

    /**
     * This method gets called once the presenter receives a text to translate.
     *
     * @param text string to translate
     */
    public void onTranslate(String text, final MVPPresenter presenter) {
        Log.i(Config.appName, "text: " + text + " is received.");
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
