package com.veontomo.yoda;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Wrapper for the all tests.
 *
 * @see <a href="http://developer.android.com/training/testing/unit-testing/instrumented-unit-tests.html">Building Instrumented Unit Tests</a>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({MVPRetrieveModelTest.class, QuoteCacheTest.class, MainViewTest.class})
public class AllTestsSuite {
}
