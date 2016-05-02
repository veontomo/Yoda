package com.veontomo.yoda;

import java.util.List;

/**
 * View interface according to MVP approach
 *
 */
public interface MVPView {
    void loadText(String text);

    void loadList(List<String> sayings);

    void onTranslationFailure(final String s);

    void setQuote(Quote quote);

    /**
     * Disables the button that activates  phrase retrieval
     * @param b
     */
    void disableButton(boolean b);
}
