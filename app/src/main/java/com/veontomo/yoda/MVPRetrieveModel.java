package com.veontomo.yoda;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A model of the MVP architecture responsible for retrieval of the phrases.
 */
public class MVPRetrieveModel {
    private static final String TAG = Config.appName;

    public static final String CATEGORY_1 = "movie";
    public static final String CATEGORY_2 = "famous";

    private MVPPresenter mPresenter;

    private final QuotesApi mQuoteRetrievalService;

    private final Callback<Quote> mQuoteCallback;

    private final QuoteCache cache;

    /**
     * Array of category statuses.
     * If a category is marked as "true", it means that a phrase of that category can be retrieved.
     */
    private final boolean[] mCategoryStatuses = new boolean[]{true, false};

    public void setPresenter(final MVPPresenter presenter) {
        this.mPresenter = presenter;
    }

    /**
     * Constructor.
     */
    public MVPRetrieveModel() {

        cache = new QuoteCache(10);

        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl(Config.QUOTES_SERVICE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mQuoteRetrievalService = retrofit2.create(QuotesApi.class);

        mQuoteCallback = new Callback<Quote>() {
            @Override
            public void onResponse(Call<Quote> call, Response<Quote> response) {
                if (response.isSuccessful()) {
                    onQuoteReceived(response.body());
                    cache.put(response.body());
                } else {
                    final Quote quote = cache.getRandom();
                    if (quote != null) {
                        onQuoteReceived(quote);
                    } else {
                        onRetrieveFailure("Something wrong I have retrieved.");
                    }
                }
            }

            @Override
            public void onFailure(Call<Quote> call, Throwable t) {
                final Quote quote = cache.getRandom();
                if (quote != null) {
                    onQuoteReceived(quote);
                } else {
                    onRetrieveFailure(t.getMessage());
                }
            }
        };

    }


    private void onRetrieveFailure(final String s) {
        mPresenter.onRetrieveResponseFailure(s);
    }

    /**
     * This method is called once a quote is received from the service
     *
     * @param quote
     */
    private void onQuoteReceived(final Quote quote) {
        mPresenter.onQuoteReceived(quote);
    }

    /**
     * Activates the retrieval of a quote.
     */
    public void retrieveQuote() {
        Call<Quote> call = mQuoteRetrievalService.getByCategory("");
        call.enqueue(mQuoteCallback);
    }

    /**
     * Sets the status of a specific category.
     *
     * @param pos    index of the category to be set
     * @param status value of the status
     */
    public void setCategoryStatus(int pos, boolean status) {
        if (pos < mCategoryStatuses.length) {
            mCategoryStatuses[pos] = status;
        }
    }

    /**
     * Gets the status of a category by category's index.
     *
     * @param pos index of the category to be set
     */
    public boolean getCategoryStatus(int pos) {
        return (pos < mCategoryStatuses.length) && mCategoryStatuses[pos];
    }


    /**
     * Changes the status of a category.
     * If the status of of a category is marked as "true", then a phrase of this category can be retrieved.
     *
     * @param pos index of the category
     */
    public void toggleCategoryStatus(int pos) {
        if (pos < mCategoryStatuses.length) {
            mCategoryStatuses[pos] = !mCategoryStatuses[pos];
        }
    }

    /**
     * Returns a category of a phrase to retrieve.
     *
     * Only categories whose statuses are "true" can be retrieved. In case when multiple categories
     * have status "true", a random one is chosen.
     *
     * @return
     */
    public String getCategoryToRetrieve() {
        if (mCategoryStatuses[0] && !mCategoryStatuses[1]){
            return CATEGORY_1;
        }
        if (mCategoryStatuses[1] && !mCategoryStatuses[0]){
            return CATEGORY_2;
        }
        return getRandomCategory();
    }

    /**
     * Returns a random category: either {@link #CATEGORY_1} or {@link #CATEGORY_2}.
     * @return
     */
    public String getRandomCategory() {
        Random generator = new Random();
        return (generator.nextInt(2) == 0) ? CATEGORY_1 : CATEGORY_2;

    }
}
