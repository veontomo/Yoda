package com.veontomo.yoda;

import android.os.Bundle;
import android.util.Log;

/**
 * Presenter for the activity according to MVP approach
 */
public class MVPPresenter {

    private final static String CACHE_TOKEN = "cache";
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

    private final QuoteCache mCache;

    private Quote mCurrentQuote;

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
        this.mCache = new QuoteCache(10);
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
     * Show the translation and in case of success, stores it in the cache.
     *
     * @param in  string to be tranlated
     * @param out translation of the above string
     */
    public void onTranslationReceived(final String in, final String out) {
        mView.onTranslationReady(out);
        mView.disableButton(false);
        if (mCurrentQuote == null) {
            Log.i(TAG, "onTranslationReady: current quote is not set");
            return;
        }
        if (mCurrentQuote.quote != null && mCurrentQuote.quote.equals(in)) {
            mCache.put(mCurrentQuote, out);
        } else {
            Log.i(TAG, "onTranslationReady: stored quote content \"" + mCurrentQuote.quote + "\" does not coincides with received string \"" + in + "\".");
        }
    }

    /**
     * This method is called if the translation of given text is not successful.
     *
     * @param txt text to be translated
     * @param msg a message describing the failure
     */
    public void onTranslationFailure(final String txt, final String msg) {
        mView.onTranslationFailure(msg);
        mView.disableButton(false);
    }

    /**
     * Displays a message that the response for the translation has been received, but it has not been successful.
     *
     * @param str         a text to translate
     * @param translation
     */
    public void onTranslationProblem(final String str, final String translation) {
        if (this.mCurrentQuote != null && this.mCurrentQuote.quote != null && this.mCurrentQuote.quote.equals(str)) {
            mView.showTranslationProblem(this.mCurrentQuote, translation);
        } else {
            Log.i(TAG, "onTranslationReady: stored quote content does not coincides with received string " + str);
        }
    }

    /**
     * Passes the received quote to the view and then translate it.
     * <p/>
     * Before sending the quote to the translation service, control first if the quote translation
     * is present in the cache.
     */
    public void onQuoteReceived(final Quote quote) {
        mView.setQuote(quote);
        if (mCache.contains(quote)) {
            final String translation = mCache.get(quote);
            onTranslationReceived(quote.quote, translation);
        } else {
            translate(quote);
        }
    }

    /**
     * Picks up a random item from the cache and passes it to the translation service.
     * <p/>
     * In case the case is empty, passes the given string to the {@link MVPView#retrieveResponseFailure(String)} method.
     */
    public void onQuoteFailure(final String s) {
        if (mCache.getSize() > 0) {
            final Quote q = mCache.getRandom();
            onQuoteReceived(q);
        } else {
            mView.retrieveResponseFailure(s);
            mView.disableButton(false);
        }
    }

    /**
     * Displays a message that a quote has been retrieved, but the response from the corresponding service is not successful.
     *
     * @param quote
     */
    public void onQuoteProblem(final Quote quote) {
        Log.i(TAG, "onQuoteProblem: ");
        mView.onQuoteProblem(quote);
        mView.disableButton(false);
    }

    /**
     * Stores the given quote in {@link #mCurrentQuote} and passes the quote content to the translation service.
     *
     * @param quote
     */
    public void translate(final Quote quote) {
        this.mCurrentQuote = quote;
        mTranslateModel.translate(quote.quote);
    }

    public void translate(String userInput) {
        final Quote q = new Quote();
        q.quote = userInput;
        translate(q);

    }

    /**
     * Starts the retrieval of a quote.
     */
    public void retrieveQuote() {
        mView.disableButton(true);
        mRetrieveModel.retrieveQuote();
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


    /**
     * Restores the cache from given bundle.
     * <p/>
     * It is supposed that the cache has previously been saved into a bundle.
     *
     * @param b a bundle to restore the cache from
     */
    public void loadCacheAsBundle(Bundle b) {
        Log.i(TAG, "loadCacheAsBundle: loading cache parcelable.");
        Log.i(TAG, "loadCacheAsBundle: current cache size " + mCache.getSize());
        if (b != null) {
            Log.i(TAG, "loadCacheAsBundle: loading...");
            mCache.loadBundle(b.getParcelable(CACHE_TOKEN));
        }
        Log.i(TAG, "loadCacheAsBundle: cache size " + mCache.getSize());
    }

    /**
     * Returns the cache put into a bundle
     *
     * @return a bundle into with the cache inside
     */
    public Bundle getCacheAsBundle() {
        Log.i(TAG, "getCacheAsBundle: the presenter starts returning a parcelable");
        Bundle b = new Bundle();
        b.putParcelable(CACHE_TOKEN, mCache);
        return b;
    }

    /**
     * {@link #mCurrentQuote} setter
     *
     * @param q
     */
    public void setCurrentQuote(final Quote q) {
        this.mCurrentQuote = q;
    }

    /**
     * {@link #mCurrentQuote} getter
     *
     * @return
     */
    public Quote getCurrentQuote() {
        return mCurrentQuote;
    }
}
