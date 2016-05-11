package com.veontomo.yoda;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
    private static final String PHRASE_TOKEN = "phrase";

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
     * Restores the activity state from the bundle
     *
     * @param savedInstanceState
     */
    private void restoreState(@NonNull Bundle savedInstanceState) {
        Log.i(TAG, "restoreState: view");
        final Quote q = new Quote();
        q.quote = savedInstanceState.getString(PHRASE_TOKEN);
        q.author = savedInstanceState.getString(AUTHOR_TOKEN);
        setQuote(q);
        loadTranslation(savedInstanceState.getString(TRANSLATION_TOKEN));
        setSwitcher(savedInstanceState.getShort(SWITCHER_TOKEN));
        mPresenter.setCategoryStatus(MVPPresenter.CATEGORY_1, savedInstanceState.getBoolean(CHECK_TOKEN_1));
        mPresenter.setCategoryStatus(MVPPresenter.CATEGORY_2, savedInstanceState.getBoolean(CHECK_TOKEN_2));
        mPresenter.loadCacheBundle(savedInstanceState.getBundle(CACHE_TOKEN));
        mCheck1.setChecked(savedInstanceState.getBoolean(CHECK_TOKEN_1));
        mCheck2.setChecked(savedInstanceState.getBoolean(CHECK_TOKEN_2));


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
    }

    @Override
    public void loadTranslation(String text) {
        if (mTranslation != null) {
            mTranslation.setText(text);
            mTranslation.setCompoundDrawablesWithIntrinsicBounds(R.drawable.yoda_square, 0, 0, 0);
        } else {
            Log.i(Config.appName, "Can not load since the translation text view is null.");
        }
    }


    @Override
    public void onTranslationFailure(final String s) {
        mTranslation.setText(getString(R.string.yoda_default_string));
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
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

    @Override
    public void showTranslationProblem(Quote quote, String translation) {
        Log.i(TAG, "showTranslationProblem: a problem with translation of the quote " + quote.toString() + ", received: " + translation);
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


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.i(TAG, "onSaveInstanceState: ");
        savedInstanceState.putCharSequence(PHRASE_TOKEN, mQuoteText.getText());
        savedInstanceState.putCharSequence(AUTHOR_TOKEN, mQuoteAuthor.getText());
        savedInstanceState.putCharSequence(TRANSLATION_TOKEN, mTranslation.getText());
        savedInstanceState.putBoolean(CHECK_TOKEN_1, mCheck1.isChecked());
        savedInstanceState.putBoolean(CHECK_TOKEN_2, mCheck2.isChecked());
        savedInstanceState.putShort(SWITCHER_TOKEN, getSwitcherStatus());
        savedInstanceState.putBundle(CACHE_TOKEN, mPresenter.getCacheParcelable());
        super.onSaveInstanceState(savedInstanceState);
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
