package com.veontomo.yoda;

import java.lang.ref.WeakReference;

/**
 * Presenter for the activity
 *
 */
public class Presenter {
    private final WeakReference<View> mView;
    public Presenter(View view) {
        mView = new WeakReference(view);
    }
}
