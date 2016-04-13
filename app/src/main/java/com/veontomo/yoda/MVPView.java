package com.veontomo.yoda;

import java.util.List;

/**
 * View interface according to MVP approach
 *
 */
public interface MVPView {
    void loadText(String text);

    void loadList(List<String> sayings);

    void onTranslationFailure();
}
