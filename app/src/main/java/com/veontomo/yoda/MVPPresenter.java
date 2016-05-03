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
    private final MVPRetrieveModel mRetrieveModel;

    private final MVPTranslateModel mTranslateModel;

    /**
     * A private constructor.
     * <p/>
     * Apart from setting the fields {@link #mView}, {@link #mRetrieveModel} and {@link #mTranslateModel},
     * it configures the models by passing the instance of the presenter to the models.
     *
     * @param view  view corresponding to the presenter
     * @param retrieveModel model responsible for quote retrieval.
     * @param translateModel model responsible for quote translation.
     */
    private MVPPresenter(final MVPView view, final MVPRetrieveModel retrieveModel, final MVPTranslateModel translateModel) {
        this.mView = view;
        this.mRetrieveModel = retrieveModel;
        this.mTranslateModel = translateModel;
        this.mRetrieveModel.setPresenter(this);
        this.mTranslateModel.setPresenter(this);
    }

    /**
     * This method is called once the model has translated the phrase.
     *
     * @param s
     */
    public void onTranslated(String s) {
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
        mTranslateModel.translate(quote.quote);
    }

    /**
     * Starts the retrieval of a quote.
     */
    public void retrieveQuote() {
        mView.disableButton(true);
        mRetrieveModel.retrieveQuote();
    }

    /**
     * Sets the category
     *
     * @param category
     */
    public void setCategory(String category) {
        mRetrieveModel.setCategory(category);
    }


    /**
     * Creates a presenter with properly set view and model objects.
     *
     * @param view
     * @return
     */
    public static MVPPresenter create(final MVPView view) {
        MVPPresenter presenter = new MVPPresenter(view, new MVPRetrieveModel(), new MVPTranslateModel());
        return presenter;

    }
}
