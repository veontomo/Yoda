package com.veontomo.yoda;

/**
 * Wrapper for the all tests
 */

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({MVPRetrieveModelTest.class, QuoteCacheTest.class, MainViewTest.class})
public class AllTestsSuite {
}
