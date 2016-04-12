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

    private void onTranslated(String s, final MVPPresenter presenter) {
        presenter.onTraslated(s);


    }
}
