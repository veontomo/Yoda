package com.veontomo.yoda;

import android.util.Log;

/**
 * Presenter for the activity according to MVP approach
 */
public class MVPPresenter {

    public static final short CATEGORY_1 = 1;
    public static final short CATEGORY_2 = 2;
    private static final String TAG = Config.appName;

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
     * @param view           view corresponding to the presenter
     * @param retrieveModel  model responsible for quote retrieval.
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
        mView.loadTranslation(s);
        mView.disableButton(false);
        mView.stopBladeAnimation();

    }

    /**
     * This method is called if the translation was not successful.
     */
    public void onTranslationFailure(final String s) {
        mView.onTranslationFailure(s);
        mView.disableButton(false);
        mView.stopBladeAnimation();
    }

    /**
     * This method is called when the quote us received
     */
    public void onQuoteReceived(final Quote quote) {
        mView.setQuote(quote);
        translate(quote.quote);
    }

    /**
     * Passes given string to the translation service.
     * @param str
     */
    public void translate(final String str){
        mTranslateModel.translate(str);
    }

    /**
     * Starts the retrieval of a quote.
     */
    public void retrieveQuote() {
        mView.disableButton(true);
        mRetrieveModel.retrieveQuote();
        mView.startBladeAnimation();
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

    /**
     * It is called when the response of the retrieval of a phrase fails.
     */
    public void onRetrieveResponseFailure(final String s) {
        mView.retrieveResponseFailure(s);

    }

    /**
     *
     */
    public void stop() {
        Log.i(TAG, "stop: presenter");

    }

    /**
     * Passes the request to toggle the status of given category to {@link MVPRetrieveModel}.
     *
     * @param category
     */
    public void setCategoryStatus(short category, boolean status) {
        switch (category) {
            case CATEGORY_1:
                mRetrieveModel.setCategoryStatus(0, status);
                break;
            case CATEGORY_2:
                mRetrieveModel.setCategoryStatus(1, status);
                break;
            default:
                Log.i(TAG, "toggleCategoryStatus: unknown category \"" + category + "\" is passed");
        }
    }
}
