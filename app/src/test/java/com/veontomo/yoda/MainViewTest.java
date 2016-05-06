package com.veontomo.yoda;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Instrumental tests for the main activity.
 *
 * Useful article: https://github.com/codepath/android_guides/wiki/Unit-Testing-with-Robolectric
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(sdk = Build.VERSION_CODES.JELLY_BEAN, constants = BuildConfig.class)
public class MainViewTest {

    private Activity activity;

    @Before
    public void init() {
        final ActivityController<MainView> controller = Robolectric.buildActivity(MainView.class);
        // although it is possible to create an activity using
        // Robolectric.setupActivity(MainView.class), let us
        // create it using the controller in order to have more
        // control over the activity lifecycle
        activity = controller
                .create()
                .start()
                .resume()
                .visible()
                .get();
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
        CheckBox box = (CheckBox) activity.findViewById(R.id.check_1);
        assertTrue("first check box should be checked", box.isChecked());
    }

    @Test
    public void secondCheckBoxesShouldBeNotChecked() {
        CheckBox box = (CheckBox) activity.findViewById(R.id.check_2);
        assertFalse("first check box should be not checked", box.isChecked());
    }

    @Test
    public void phraseTextFieldShouldBeVisible() {
        TextView tv = (TextView) activity.findViewById(R.id.phrase);
        assertEquals("text field for displaying a phrase should be visible", View.VISIBLE, tv.getVisibility());
    }

    @Test
    public void inputTextFieldShouldBeInvisible() {
        EditText tv = (EditText) activity.findViewById(R.id.hidden_edit_view);
        assertEquals("edit field for inserting a phrase should be invisible", View.GONE, tv.getVisibility());
    }

    @Test
    public void firstCheckBoxRemainsCheckedAfterRecreation() {
        CheckBox box = (CheckBox) activity.findViewById(R.id.check_1);
        box.setChecked(true);
        activity.recreate();
        box = (CheckBox) activity.findViewById(R.id.check_1);
        assertTrue("the first checkbox must remain checked after screen rotation", box.isChecked());
    }

    @Test
    public void firstCheckBoxRemainsUncheckedAfterRecreation() {
        CheckBox box = (CheckBox) activity.findViewById(R.id.check_1);
        box.setChecked(false);
        activity.recreate();
        box = (CheckBox) activity.findViewById(R.id.check_1);
        assertFalse("the first checkbox must remain unchecked after screen rotation", box.isChecked());
    }


    @Test
    public void secondCheckBoxRemainsCheckedAfterRecreation() {
        CheckBox box = (CheckBox) activity.findViewById(R.id.check_2);
        box.setChecked(true);
        activity.recreate();
        box = (CheckBox) activity.findViewById(R.id.check_2);
        assertTrue("the second checkbox must remain checked after screen rotation", box.isChecked());
    }

    @Test
    public void secondCheckBoxRemainsUncheckedAfterRecreation() {
        CheckBox box = (CheckBox) activity.findViewById(R.id.check_2);
        box.setChecked(false);
        activity.recreate();
        box = (CheckBox) activity.findViewById(R.id.check_2);
        assertFalse("the second  checkbox must remain unchecked after screen rotation", box.isChecked());
    }

    @Test
    public void translationFieldShouldDisplayTextAfterRecreation() {
        TextView tv = (TextView) activity.findViewById(R.id.translation);
        tv.setText("some text");
        activity.recreate();
        tv = (TextView) activity.findViewById(R.id.translation);
        assertEquals("translation field should maintain its content", "some text", tv.getText());
    }

}