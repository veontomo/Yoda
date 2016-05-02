package com.veontomo.yoda;

/**
 * Presenter for the activity according to MVP approach
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
     * @param view view corresponding to the presenter
     * @param model model corresponding to the presenter. In its turn, it is bound to the presenter.
     */
    private MVPPresenter(final MVPView view, final MVPModel model) {
        mView = view;
        mModel = model;
        mModel.setPresenter(this);
    }

    /**
     * This method is called once the model has translated the phrase.
     *
     * @param s
     */
    public void onTraslated(String s) {
        mView.loadText(s);
        mView.disableButton(false);

    }

    /**
     * This method is called if the translation was not successful.
     */
    public void onTranslationFailure(final String s) {
        mView.onTranslationFailure(s);
        mView.disableButton(false);
    }

    /**
     * This method is called when the quote us received
     */
    public void onQuoteReceived(final Quote quote) {
        mView.setQuote(quote);


    }

    /**
     * Starts the retrieval of a quote and its then its translation
     */
    public void retrievePhrase() {
        mModel.onTranslate("");
        mView.disableButton(true);

    }

    /**
     * Sets the category
     *
     * @param category
     */
    public void setCategory(String category) {
        mModel.setCategory(category);
    }


    /**
     * Creates a presenter with properly set view and model objects.
     *
     * @param view
     * @return
     */
    public static MVPPresenter create(final MVPView view) {
        MVPPresenter presenter = new MVPPresenter(view, new MVPModel());
        return presenter;

    }
}
