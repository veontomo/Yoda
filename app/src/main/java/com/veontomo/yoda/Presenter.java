package com.veontomo.yoda;

/**
 * Presenter for the activity
 *
 */
public class Presenter {
    /**
     * A reference to the view
     */
    private final View mView;

    /**
     * A reference to the model
     */
    private final Model mModel;

    /**
     * Constructor.
     *
     * Converts hard reference into weak ones.
     * @param view
     * @param model
     */
    public Presenter(View view, Model model) {
        mView = view;
        mModel = model;
    }


    /**
     * This method is called once the translation button is clicked.
     * @param text string contained in the edit text
     */
    public void onTranslate(final String text) {
        mModel.onTranslate(text);

    }
}
