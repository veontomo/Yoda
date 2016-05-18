package com.veontomo.yoda;

/**
 * View interface according to MVP approach
 *
 */
interface MVPView {
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

    void onTranslationProblem();

    /**
     * Sets the statuses of the view checkboxes
     * @param statuses
     */
    void setCategories(final boolean[] statuses);

    void enableUserInput(boolean status);
}
