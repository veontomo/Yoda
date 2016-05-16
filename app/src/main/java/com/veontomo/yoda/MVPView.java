package com.veontomo.yoda;

import java.util.List;

/**
 * View interface according to MVP approach
 *
 */
public interface MVPView {
    void showMessage(final String msg);

    void onTranslationReady(String text);

    void onTranslationFailure(final String s);

    void setQuote(Quote quote);

    /**
     * Disables the button that activates  phrase retrieval
     * @param b
     */
    void disableButton(boolean b);

    void retrieveResponseFailure(final String s);

    void onQuoteProblem(final Quote body);

    void onTranslationProblem(Quote quote, String translation);

    /**
     * Sets the statuses of the view checkboxes
     * @param statuses
     */
    void setCategories(final boolean[] statuses);

    void enableUserInput(boolean status);
}
