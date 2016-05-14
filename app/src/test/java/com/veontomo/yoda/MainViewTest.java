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
 * <p/>
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

    /*
     * tests that define what should be displayed on the screen
     */

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
    public void firstCheckBoxesShouldBeVisible() {
        CheckBox box = (CheckBox) activity.findViewById(R.id.check_1);
        assertEquals("first check box should be visible", View.VISIBLE, box.getVisibility());
    }

    @Test
    public void secondCheckBoxesShouldBeVisible() {
        CheckBox box = (CheckBox) activity.findViewById(R.id.check_2);
        assertEquals("second check box should be present", View.VISIBLE, box.getVisibility());
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
    public void translationTextFieldShouldBeVisible() {
        TextView tv = (TextView) activity.findViewById(R.id.translation);
        assertEquals("edit field for inserting a phrase should be invisible", View.VISIBLE, tv.getVisibility());
    }

    /*
     * tests that define behaviour after activity recreation (i.e. in case of the device rotation)
     */

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
        assertEquals("translation field should maintain its content", "some text", tv.getText().toString().trim());
    }

    /*
    tests that define behaviour of the elements inside of the switcher
     */

    @Test
    public void userInputFieldBecomesVisibleWhenPhraseIsClicked() {
        TextView phrase = (TextView) activity.findViewById(R.id.phrase);
        phrase.performClick();
        TextView input = (TextView) activity.findViewById(R.id.hidden_edit_view);
        assertEquals("input field should become visible", View.VISIBLE, input.getVisibility());
    }

    @Test
    public void phraseFieldBecomesInvisibleWhenPhraseIsClicked() {
        TextView phrase = (TextView) activity.findViewById(R.id.phrase);
        phrase.performClick();
        assertEquals("input field should become invisible", View.GONE, phrase.getVisibility());
    }

    @Test
    public void userInputFieldGetsFocusWhenPhraseIsClicked() {
        TextView phrase = (TextView) activity.findViewById(R.id.phrase);
        phrase.performClick();
        TextView input = (TextView) activity.findViewById(R.id.hidden_edit_view);
        assertTrue("input field should get focus when the phrase is clicked", input.isFocused());
    }

    @Test
    public void userInputFieldShouldRemainVisibleAfterRecreation(){
        TextView phrase = (TextView) activity.findViewById(R.id.phrase);
        phrase.performClick();
        activity.recreate();
        TextView input = (TextView) activity.findViewById(R.id.hidden_edit_view);
        assertEquals("input field should remain visible after recreation", View.VISIBLE, input.getVisibility());
    }


}