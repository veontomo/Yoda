package com.veontomo.yoda;

/**
 * Configuration constants
 *
 */
public abstract class Config {
    public static final String appName = "Yoda";

    public static final boolean IS_PRODUCTION = false;

    /**
     *
     */
    public static final String API_KEY_1 = IS_PRODUCTION ? "cdY74PzM7lmshAFnL81d4g8Nmkmjp19ctRPjsns3cfVRoORLhD" : "BcsP5qtoB9mshvuD0VHmv78FP75Tp1uqnt1jsnv9YEFqi9oygG";
    public static final String YODA_SERVICE_URL = "https://yoda.p.mashape.com/";
    public static final String QUOTES_SERVICE_URL = "https://andruxnet-random-famous-quotes.p.mashape.com";
}
