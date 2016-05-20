package com.veontomo.yoda;

/**
 * Configuration constants
 */
abstract class Config {
    public static final String appName = "Yoda";

    /**
     * whether the app is in production mode or in develop one.
     *
     * TODO: Use gradle flavours instead
     *
     * true - production mode, false - develop mode.
     */
    public static final boolean IS_PRODUCTION = false;

    /**
     * api key api key to access the online services
     */
    @SuppressWarnings({"SpellCheckingInspection"})
    public static final String API_KEY =  "BcsP5qtoB9mshvuD0VHmv78FP75Tp1uqnt1jsnv9YEFqi9oygG";

    /**
     * url of the Yoda translation API
     */
    @SuppressWarnings("SpellCheckingInspection")
    public static final String YODA_SERVICE_URL = "https://yoda.p.mashape.com/";

    /**
     * url of the quote retrieval API
     */
    @SuppressWarnings("SpellCheckingInspection")
    public static final String QUOTES_SERVICE_URL = "https://andruxnet-random-famous-quotes.p.mashape.com";
}
