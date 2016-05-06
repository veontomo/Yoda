package com.veontomo.yoda;

import android.app.Activity;
import android.widget.Button;

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
@Config(sdk = 18, constants = BuildConfig.class)
public class MainViewTest {
    @Test
    public void shouldDisplayButton() {
        Activity activity = Robolectric.setupActivity(MainView.class);
        Button btn = (Button) activity.findViewById(R.id.retrieveBtn);
        assertNotNull(btn);
    }

}