package com.veontomo.yoda;

/**
 * Presenter for the activity according to MVP approach
 *
 */
public class MVPPresenter {
    /**
     * A reference to the view
     */
    private final MVPView mView;

    /**
     * A reference to the model
     */
    private final MVPModel mModel;

    /**
     * Constructor.
     *
     * Converts hard reference into weak ones.
     * @param view
     * @param model
     */
    public MVPPresenter(MVPView view, MVPModel model) {
        mView = view;
        mModel = model;
    }


    /**
     * This method is called once the translation button is clicked.
     * @param text string contained in the edit text
     */
    public void onTranslate(final String text) {
        mModel.onTranslate(text, this);
    }

    /**
     * This method is called once the model has translated the phrase.
     * @param s
     */
    public void onTraslated(String s) {
        mView.loadText(s);

    }
}
