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
     * a string key under which the content of the quote author field is to be saved
     * when saving the activity state for further recreating
     */
    private static final String AUTHOR_TOKEN = "author";

    /**
     * a string key under which the status of the check button corresponding to "movie" is to be saved
     * when saving the activity state for further recreating
     */
    private static final String CHECK_TOKEN_1 = "radio_movie";

    /**
     * a string key under which the status of the check button corresponding to "famous" is to be saved
     * when saving the activity state for further recreating
     */
    private static final String CHECK_TOKEN_2 = "check_famous";

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
     * a string key under which the previously requested phrases are to be saved
     * when saving the activity state for further recreating
     */
    private static final String CACHE_PHRASES_TOKEN = "cache_phrases";

    /**
     * a string key under which the previously retrieved translations are to be saved
     * when saving the activity state for further recreating
     */
    private static final String CACHE_TRANSLATIONS_TOKEN = "cache_translations";

    /**
     * a string key under which the cache is to be saved
     * when saving the activity state for further recreating
     */
    private static final String CACHE_TOKEN = "cache";

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
        Log.i(TAG, "restoreState: view");
        final Quote q = savedInstanceState.getParcelable(QUOTE_TOKEN);
        setQuote(q);
        onTranslationReady(savedInstanceState.getString(TRANSLATION_TOKEN));
        setSwitcher(savedInstanceState.getShort(SWITCHER_TOKEN));
        mPresenter.setCurrentQuote(q);
        mPresenter.setCategoryStatus(MVPPresenter.CATEGORY_1, savedInstanceState.getBoolean(CHECK_TOKEN_1));
        mPresenter.setCategoryStatus(MVPPresenter.CATEGORY_2, savedInstanceState.getBoolean(CHECK_TOKEN_2));
        mPresenter.loadCacheAsBundle(savedInstanceState.getBundle(CACHE_TOKEN));
        mCheck1.setChecked(savedInstanceState.getBoolean(CHECK_TOKEN_1));
        mCheck2.setChecked(savedInstanceState.getBoolean(CHECK_TOKEN_2));
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.i(TAG, "onSaveInstanceState: ");
        savedInstanceState.putCharSequence(TRANSLATION_TOKEN, mTranslation.getText());
        savedInstanceState.putBoolean(CHECK_TOKEN_1, mCheck1.isChecked());
        savedInstanceState.putBoolean(CHECK_TOKEN_2, mCheck2.isChecked());
        savedInstanceState.putShort(SWITCHER_TOKEN, getSwitcherStatus());
        savedInstanceState.putBundle(CACHE_TOKEN, mPresenter.getCacheAsBundle());
        savedInstanceState.putParcelable(QUOTE_TOKEN, mPresenter.getCurrentQuote());
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * A callback associated with the button elaborates the user input.
     * <p/>
     * If the edit text field corresponding to the user input is not empty,
     * then a translation of that string is initiated.
     * Otherwise, a quote retrieval is initiated.
     *
     * @param view
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
     * @param s
     */
    @Override
    public void onTranslationReady(final String s) {
        showTranslation(s, mSpanFailure);
        disableButton(false);
    }


    /**
     * Prepends the failure image to the given text and then displays it in the translation field.
     * After that, enables the button.
     *
     * @param s
     */
    @Override
    public void onTranslationFailure(final String s) {
        showTranslation(s, mSpanFailure);
        disableButton(false);
    }

    /**
     * Prepends the failure image to the Yoda default text and then displays it in the translation field.
     * After that, enables the button.
     *
     * @param quote
     * @param translation
     */
    @Override
    public void showTranslationProblem(final Quote quote, final String translation) {
        showTranslation(getResources().getString(R.string.yoda_default_string), mSpanFailure);
        disableButton(false);
    }

    private void showTranslation(final String txt, final ImageSpan image) {
        if (mTranslation == null) {
            Log.i(Config.appName, "Can not load since the translation text view is null.");
            return;
        }
        final SpannableString ss = new SpannableString("  " + txt);
        if (mSpanSuccess != null) {
            ss.setSpan(image, 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            mTranslation.setText(ss);
        } else {
            mTranslation.setText(txt);
        }


    }

    @Override
    public void setQuote(final Quote quote) {
        setSwitcher(SWITCHER_STATUS_1);
        mQuoteText.setText(quote.quote);
        mQuoteAuthor.setText(quote.author);
        mButton.setText(getText(R.string.i_feel_lucky));
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
    public void showQuoteProblem(final Quote quote) {
        Log.i(TAG, "showQuoteProblem: some problem with " + quote.toString());
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


    public void onCategory1(View view) {
        final CheckBox b = (CheckBox) view;
        mPresenter.setCategoryStatus(MVPPresenter.CATEGORY_1, b != null && b.isChecked());
    }

    public void onCategory2(View view) {
        final CheckBox b = (CheckBox) view;
        mPresenter.setCategoryStatus(MVPPresenter.CATEGORY_2, b != null && b.isChecked());
    }


    /**
     * Returns
     * <ul>
     * <li> {@link #SWITCHER_STATUS_1} if the mSwitcher displays {@link #mQuoteText},</li>
     * <li> {@link #SWITCHER_STATUS_2} if it displays {@link #mUserInput},</li>
     * <li> {@link #SWITCHER_STATUS_ERROR}, if either mSwitcher is not defined or for any other reason</li>
     * </ul>
     *
     * @return
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
