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
     * NB: Creates a new instance of a model using operator "new" explicitly.
     *
     * @param view
     */
    public MVPPresenter(final MVPView view) {
        mView = view;
        // TODO: avoid creating a new instance here
        mModel = new MVPModel(this);
    }


    /**
     * This method is called once the translation button is clicked.
     * @param text string contained in the edit text
     */
    public void onTranslate(final String text) {
        mModel.onTranslate(text);
    }

    /**
     * This method is called once the model has translated the phrase.
     * @param s
     */
    public void onTraslated(String s) {
        mView.loadText(s);

    }

    /**
     * This method is called if the translation was not successful.
     */
    public void onTranslationFailure() {
        mView.onTranslationFailure();



    }

    /**
     * This method is called when the quote us received
     */
    public void onQuoteReceived(final Quote quote) {
        mView.setQuote(quote);


    }
}
