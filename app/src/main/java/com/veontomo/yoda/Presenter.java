package com.veontomo.yoda;

import java.lang.ref.WeakReference;

/**
 * Presenter for the activity
 *
 */
public class Presenter {
    /**
     * A reference (weak) to the view
     */
    private final WeakReference mView;

    /**
     * A reference (weak) to the model
     */
    private final WeakReference mModel;

    /**
     * Constructor.
     *
     * Converts hard reference into weak ones.
     * @param view
     * @param model
     */
    public Presenter(View view, Model model) {
        mView = new WeakReference(view);
        mModel = new WeakReference(model);
    }
}
