package com.veontomo.yoda;

import java.util.List;

/**
 * View interface according to MVP approach
 *
 */
public interface MVPView {
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

    void showTranslationProblem(Quote quote, String translation);
}
