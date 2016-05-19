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
    public static final boolean IS_PRODUCTION = true;

    /**
     * api key for the quote retrieval service
     */
    @SuppressWarnings({"SpellCheckingInspection"})
    public static final String API_KEY_1 = "cdY74PzM7lmshAFnL81d4g8Nmkmjp19ctRPjsns3cfVRoORLhD";

    /**
     * api key for the Yoda translation service
     */
    @SuppressWarnings("SpellCheckingInspection")
    /**
     * url of the Yoda translation API
     */
    public static final String YODA_SERVICE_URL = "https://yoda.p.mashape.com/";

    /**
     * url of the quote retrieval API
     */
    @SuppressWarnings("SpellCheckingInspection")
    public static final String QUOTES_SERVICE_URL = "https://andruxnet-random-famous-quotes.p.mashape.com";
}
