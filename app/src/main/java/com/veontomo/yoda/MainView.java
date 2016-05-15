package com.veontomo.yoda;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class MainView extends AppCompatActivity implements MVPView {
    private static final String TAG = Config.appName;
    /**
     * a string key under which the content of the phrase field is to be saved
     * when saving the activity state for further recreating
     */
    private static final String QUOTE_TOKEN = "phrase";

    /**
     * a string key under which the content of the translation field is to be saved
     * when saving the activity state for further recreating
     */
    private static final String TRANSLATION_TOKEN = "translation";

    /**
     * a string key under which the statuses of the check boxes corresponding to categories
     * are to be saved when saving the activity state for further recreating
     */
    private static final String CHECK_TOKEN = "categories";

    /**
     * a string key under which the status of the mSwitcher is to be saved
     * when saving the activity state for further recreating
     */
    private static final String SWITCHER_TOKEN = "switcher";

    /**
     * the mSwitcher status if it displays the first view
     */
    private static final short SWITCHER_STATUS_1 = 1;

    /**
     * the mSwitcher status if it displays the second view
     */
    private static final short SWITCHER_STATUS_2 = 2;

    /**
     * the mSwitcher status in case of some error
     */
    private static final short SWITCHER_STATUS_ERROR = -1;

    /**
     * a string key under which the cache is to be saved
     * when saving the activity state for further recreating
     */
    private static final String CACHE_TOKEN = "cache";

    /**
     * a string key under which the translation status is to be saved
     * when saving the activity state for further recreating
     */
    private static final String TRANSLATION_STATUS_TOKEN = "translation_status";

    private TextView mTranslation;
    private TextView mQuoteText;
    private TextView mQuoteAuthor;
    private ViewSwitcher mSwitcher;
    private EditText mUserInput;
    private MVPPresenter mPresenter;
    private Button mButton;
    private CheckBox mCheck1;
    private CheckBox mCheck2;
    private Bundle mState = null;
    private ImageSpan mSpanSuccess;
    private ImageSpan mSpanFailure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Config.IS_PRODUCTION) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }

        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            mState = savedInstanceState;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        init();
        if (mState != null) {
            restoreState(mState);
            mState = null;
        }
    }

    @Override
    public void onPause() {
        mPresenter.stop();
        super.onPause();
    }

    /**
     * Restores the activity state from a bundle.
     * <p/>
     * It is supposed that the bundle has previously been created by {@link #onSaveInstanceState(Bundle)}.
     *
     * @param savedInstanceState a bundle containing the view's state
     */
    private void restoreState(@NonNull Bundle savedInstanceState) {
        final Quote q = savedInstanceState.getParcelable(QUOTE_TOKEN);
        mPresenter.setCurrentQuote(q);
        mPresenter.loadCacheAsBundle(savedInstanceState.getBundle(CACHE_TOKEN));
        mPresenter.setCategoryStatuses(savedInstanceState.getBooleanArray(CHECK_TOKEN));
        mPresenter.setTranslationStatus(savedInstanceState.getShort(TRANSLATION_STATUS_TOKEN));
        onTranslationReady(savedInstanceState.getString(TRANSLATION_TOKEN));
        setSwitcher(savedInstanceState.getShort(SWITCHER_TOKEN));
        setQuote(q);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(TRANSLATION_TOKEN, mTranslation.getText().toString().trim());
        savedInstanceState.putBooleanArray(CHECK_TOKEN, new boolean[]{mCheck1.isChecked(), mCheck2.isChecked()});
        savedInstanceState.putShort(SWITCHER_TOKEN, getSwitcherStatus());
        savedInstanceState.putBundle(CACHE_TOKEN, mPresenter.getCacheAsBundle());
        savedInstanceState.putShort(TRANSLATION_STATUS_TOKEN, mPresenter.getTranslationStatus());
        savedInstanceState.putParcelable(QUOTE_TOKEN, mPresenter.getCurrentQuote());
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * A callback associated with the button that elaborates the user input.
     * <p/>
     * If the edit text field corresponding to the user input is not empty,
     * then a translation of that string is initiated.
     * Otherwise, a quote retrieval is initiated.
     *
     * @param view view a click on which triggers execution of this method
     */
    public void elaborate(View view) {
        if (mPresenter != null) {
            final String userInput = mUserInput.getEditableText().toString();
            if (userInput.isEmpty()) {
                mPresenter.retrieveQuote();
            } else {
                mPresenter.translate(userInput);
                mUserInput.setText(null);
                mSwitcher.showPrevious();
            }
        }
    }

    /**
     * Create references to required views and initialize the presenter.
     */
    private void init() {
        mTranslation = (TextView) findViewById(R.id.translation);
        mQuoteText = (TextView) findViewById(R.id.phrase);
        mQuoteAuthor = (TextView) findViewById(R.id.author);
        mSwitcher = (ViewSwitcher) findViewById(R.id.my_switcher);
        mUserInput = (EditText) findViewById(R.id.hidden_edit_view);
        mButton = (Button) findViewById(R.id.retrieveBtn);
        mCheck1 = (CheckBox) findViewById(R.id.check_1);
        mCheck2 = (CheckBox) findViewById(R.id.check_2);
        mPresenter = MVPPresenter.create(this);

        mSpanSuccess = createImageSpan(R.drawable.yoda_square);
        mSpanFailure = createImageSpan(R.drawable.panda_square);
    }

    /**
     * Creates an image span from a drawable with the given id.
     *
     * @param id an id of a drawable
     * @return ImageSpan or null
     */
    @Nullable
    private ImageSpan createImageSpan(int id) {
        final Drawable drawable;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            drawable = getResources().getDrawable(id, null);
        } else {
            drawable = getResources().getDrawable(id);
        }
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            return new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        }
        return null;
    }

    /**
     * Prepends the success image to the given text and then displays it in the translation field.
     * After that, enables the button.
     *
     * @param s a string to be display in the translation field
     */
    @Override
    public void onTranslationReady(final String s) {
        showTranslation(s);
        disableButton(false);
    }


    /**
     * Prepends the failure image to the given text and then displays it in the translation field.
     * After that, enables the button.
     *
     * @param s a string to be display in the translation field
     */
    @Override
    public void onTranslationFailure(final String s) {
        showTranslation(s);
        disableButton(false);
    }

    /**
     * Prepends the failure image to the Yoda default text and then displays it in the translation field.
     * After that, enables the button.
     *
     * @param quote   a quote translation of which has caused the problem
     * @param message string received as a translation of the quote
     */
    @Override
    public void showTranslationProblem(final Quote quote, final String message) {
        showTranslation(getResources().getString(R.string.yoda_default_string));
        disableButton(false);
    }

    /**
     * Sets the statuses of the view checkboxes.
     * <p/>
     * The array must contain exactly two elements.
     * Otherwise, the method does nothing.
     *
     * @param statuses array containing statuses of two checkboxes.
     */
    @Override
    public void setCategories(final boolean[] statuses) {
        Log.i(TAG, "setCategories: setting the statuses");
        if (statuses != null && statuses.length == 2) {
            Log.i(TAG, "setCategories: status array is valid");
            mCheck1.setChecked(statuses[0]);
            mCheck2.setChecked(statuses[1]);
        }
    }

    /**
     * Prepends an image span to a given string and displays the obtained object in the
     * translation field.
     * <p/>
     * What image span should be used is decided based on the value of {@link MVPPresenter#mTranslationStatus}.
     *
     * @param txt a string that should be displayed in the translation field
     */
    private void showTranslation(final String txt) {
        if (mTranslation == null) {
            Log.i(Config.appName, "Can not load since the translation text view is null.");
            return;
        }
        final SpannableString ss = new SpannableString("  " + txt);
        final short status = mPresenter.getTranslationStatus();
        final ImageSpan image;
        if (status == MVPPresenter.TRANSLATION_OK) {
            image = mSpanSuccess;
        } else if (status == MVPPresenter.TRANSLATION_FAILURE || status == MVPPresenter.TRANSLATION_PROBLEM) {
            image = mSpanFailure;
        } else {
            image = null;
        }
        if (image != null) {
            ss.setSpan(image, 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            mTranslation.setText(ss);
        } else {
            mTranslation.setText(txt);
        }
    }

    @Override
    public void setQuote(final Quote quote) {
        setSwitcher(SWITCHER_STATUS_1);
        if (quote != null) {
            mQuoteText.setText(quote.quote);
            mQuoteAuthor.setText(quote.author);
            mButton.setText(getText(R.string.i_feel_lucky));
        }
        setCheckboxVisibility(View.VISIBLE);
    }


    /**
     * Disables the button that activates  phrase retrieval
     *
     * @param b
     */
    @Override
    public void disableButton(boolean b) {
        mButton.setEnabled(!b);
    }

    @Override
    public void retrieveResponseFailure(final String s) {
        Toast.makeText(MainView.this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onQuoteProblem(final Quote quote) {
        Log.i(TAG, "onQuoteProblem: some problem with " + quote.toString());
    }


    public void textViewClicked(View v) {
        mSwitcher.showNext();
        mButton.setText(getText(R.string.translate));
        setCheckboxVisibility(View.INVISIBLE);
        mUserInput.requestFocus();
    }

    private void setCheckboxVisibility(int visibility) {
        mCheck1.setVisibility(visibility);
        mCheck2.setVisibility(visibility);
    }


    /**
     * Passes the statuses of the checkboxes to the presenter.
     *
     * @param view a click on this view triggers the execution of this method
     */
    public void onCategoryStatusChange(View view) {
        mPresenter.setCategoryStatuses(new boolean[]{mCheck1.isChecked(), mCheck2.isChecked()});
    }

    /**
     * Returns
     * <ul>
     * <li> {@link #SWITCHER_STATUS_1} if the mSwitcher displays {@link #mQuoteText},</li>
     * <li> {@link #SWITCHER_STATUS_2} if it displays {@link #mUserInput},</li>
     * <li> {@link #SWITCHER_STATUS_ERROR}, if either mSwitcher is not defined or for any other reason</li>
     * </ul>
     *
     * @return {@link #SWITCHER_STATUS_1} or {@link #SWITCHER_STATUS_2} or {@link #SWITCHER_STATUS_ERROR}
     */
    private short getSwitcherStatus() {
        if (mSwitcher == null) {
            return SWITCHER_STATUS_ERROR;
        }
        final int id = mSwitcher.getCurrentView().getId();
        if (id == mQuoteText.getId()) {
            return SWITCHER_STATUS_1;
        }
        if (id == mUserInput.getId()) {
            return SWITCHER_STATUS_2;
        }
        return SWITCHER_STATUS_ERROR;
    }


    /**
     * Makes the mSwitcher  display one of its child views:
     * <ul>
     * <li> if the status is {@link #SWITCHER_STATUS_1}, the first child is displayed </li>
     * <li> if the status is {@link #SWITCHER_STATUS_2}, the second child is displayed </li>
     * <li> otherwise nothing is done.</li>
     * </ul>
     *
     * @param status either {@link #SWITCHER_STATUS_1} or {@link #SWITCHER_STATUS_2}
     */
    private void setSwitcher(short status) {
        if (mSwitcher == null) {
            return;
        }
        if (status == SWITCHER_STATUS_1) {
            mSwitcher.setDisplayedChild(0);
        } else if (status == SWITCHER_STATUS_2) {
            mSwitcher.setDisplayedChild(1);
        }
    }


}
