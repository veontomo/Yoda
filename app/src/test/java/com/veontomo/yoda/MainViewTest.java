package com.veontomo.yoda;

import android.app.Activity;
import android.widget.Button;
import android.widget.CheckBox;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Instrumental tests for the main activity.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(sdk = 18, constants = BuildConfig.class)
public class MainViewTest {

    private Activity activity;

    @Before
    public void init() {
        activity = Robolectric.setupActivity(MainView.class);
    }

    @Test
    public void buttonShouldBeDisplayed() {
        Button btn = (Button) activity.findViewById(R.id.retrieveBtn);
        assertNotNull(btn);
    }

    @Test
    public void buttonShouldHaveText() {
        Button btn = (Button) activity.findViewById(R.id.retrieveBtn);
        assertEquals("I feel lucky", btn.getText());
    }

    @Test
    public void firstCheckBoxesShouldBeDisplayed() {
        CheckBox box = (CheckBox) activity.findViewById(R.id.check_1);
        assertNotNull("first check box should be present", box);
    }

    @Test
    public void secondCheckBoxesShouldBeDisplayed() {
        CheckBox box = (CheckBox) activity.findViewById(R.id.check_2);
        assertNotNull("second check box should be present", box);
    }


    @Test
    public void firstCheckBoxesShouldBeChecked() {
        CheckBox box1 = (CheckBox) activity.findViewById(R.id.check_1);
        assertTrue("first check box should be checked", box1.isChecked());
    }

    @Test
    public void secondCheckBoxesShouldBeNotChecked() {
        CheckBox box = (CheckBox) activity.findViewById(R.id.check_2);
        assertFalse("first check box should be not checked", box.isChecked());
    }

}