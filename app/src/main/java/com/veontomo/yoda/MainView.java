package com.veontomo.yoda;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
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
        mState = savedInstanceState;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
        restoreState(mState);
        mState = null;
    }

    /**
     * Restores the activity state from a bundle.
     * <p/>
     * It is supposed that the bundle has previously been created by {@link #onSaveInstanceState(Bundle)}.
     *
     * @param bundle a bundle containing the view's state
     */
    private void restoreState(Bundle bundle) {
        if (bundle == null) {
            return;
        }
        final Quote q = bundle.getParcelable(QUOTE_TOKEN);
        mPresenter.setCurrentQuote(q);
        mPresenter.loadCacheAsBundle(bundle.getBundle(CACHE_TOKEN));
        mPresenter.setCategoryStatuses(bundle.getBooleanArray(CHECK_TOKEN));
        mPresenter.setTranslationStatus(bundle.getShort(TRANSLATION_STATUS_TOKEN));
        mPresenter.enableUserInput(bundle.getBoolean(SWITCHER_TOKEN));
        onTranslationReady(bundle.getString(TRANSLATION_TOKEN));
        setQuote(q);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(TRANSLATION_TOKEN, mTranslation.getText().toString().trim());
        savedInstanceState.putBooleanArray(CHECK_TOKEN, new boolean[]{mCheck1.isChecked(), mCheck2.isChecked()});
        savedInstanceState.putBoolean(SWITCHER_TOKEN, mPresenter.isUserInputActive());
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
     * @param view view a click on which triggers execution of this method. It is not used in the method.
     */
    public void elaborate(@SuppressWarnings("UnusedParameters") View view) {
        if (mPresenter == null) {
            showMessage(getResources().getString(R.string.no_presenter_error));
        }
        if (mPresenter.isUserInputActive()) {
            mPresenter.enableUserInput(false);
            final String userInput = mUserInput.getEditableText().toString();
            if (userInput.isEmpty()) {
                mPresenter.retrieveQuote();
            } else {
                mUserInput.setText(null);
                mPresenter.translate(userInput);
            }
        } else {
            mPresenter.retrieveQuote();
        }
    }

    private void showMessage(final String txt) {
        Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_SHORT).show();
    }

    /**
     * Create references to required views and initialize the presenter.
     */
    private void init() {
        mTranslation = (TextView) findViewById(R.id.translation);
        mQuoteText = (TextView) findViewById(R.id.phrase);
        mQuoteAuthor = (TextView) findViewById(R.id.author);
        mSwitcher = (ViewSwitcher) findViewById(R.id.my_switcher);
        mUserInput = (EditText) findViewById(R.id.input_view);
        mButton = (Button) findViewById(R.id.retrieveBtn);
        mCheck1 = (CheckBox) findViewById(R.id.check_1);
        mCheck2 = (CheckBox) findViewById(R.id.check_2);
        mPresenter = MVPPresenter.create(this);

        mSpanSuccess = createImageSpan(R.drawable.yoda);
        mSpanFailure = createImageSpan(R.drawable.panda);
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
        showTranslation(getResources().getString(R.string.yoda_translate_failure_string));
        showMessage(s);
        disableButton(false);
    }

    /**
     * Prepends the failure image to the Yoda default text and then displays it in the translation field.
     * After that, enables the button.
     *
     */
    @Override
    public void onTranslationProblem() {
        showTranslation(getResources().getString(R.string.yoda_translate_problem_string));
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
        if (statuses != null && statuses.length == 2) {
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
        if (quote != null) {
            mQuoteText.setText(quote.quote);
            mQuoteAuthor.setText(quote.author);

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

    /**
     * Enables the input field that is initially hidden in the switcher view.
     *
     * This method is executed once the corresponding edit text view is clicked.
     * For this reason the argument is required, but the method does not use it.
     * @param v a click on this view triggers the execution of this method
     */
    public void textViewClicked(@SuppressWarnings("UnusedParameters") View v) {
        mSwitcher.showNext();
        mPresenter.enableUserInput(true);
        setCheckboxVisibility(View.INVISIBLE);
        final String txt = mQuoteText.getText().toString();
        if (!"".equals(txt)) {
            mUserInput.setText(txt);
        }
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
    public void onCategoryStatusChange(@SuppressWarnings("UnusedParameters") View view) {
        mPresenter.setCategoryStatuses(new boolean[]{mCheck1.isChecked(), mCheck2.isChecked()});
    }

    /**
     * Enable/disable the user input in the switcher and adjust the
     * button text.
     *
     * @param status true to enable, false to disable
     */
    public void enableUserInput(boolean status) {
        if (status) {
            mSwitcher.setDisplayedChild(1);
            mButton.setText(getText(R.string.translate));
        } else {
            mSwitcher.setDisplayedChild(0);
            mButton.setText(getText(R.string.i_feel_lucky));
        }
    }


}
