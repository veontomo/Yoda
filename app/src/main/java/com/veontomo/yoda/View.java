package com.veontomo.yoda;

import java.util.List;

/**
 * View interface
 *
 */
public interface View {
    void loadText(String text);

    void loadList(List<String> sayings);
}
