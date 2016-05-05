package com.veontomo.yoda;

import android.app.Activity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Instrumental tests for the main activity.
 *
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class MainViewTest {
    @Test
    public void shouldDisplayButton() {
        Activity activity = Robolectric.setupActivity(MainView.class);
        assertNotNull(activity.findViewById(R.id.retrieveBtn));
//        assertTrue(true);

    }

}