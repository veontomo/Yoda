package com.veontomo.yoda;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.squareup.leakcanary.LeakCanary;

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
     * a string key under which the status of the radio button corresponding to "movie" is to be saved
     * when saving the activity state for further recreating
     */
    private static final String MOVIE_BUTTON_TOKEN = "radio_movie";

    private TextView mTranslation;
    private TextView mQuoteText;
    private TextView mQuoteAuthor;
    private ViewSwitcher switcher;
    private MVPPresenter mPresenter;
    private ImageView mButton;
    private RadioButton mRadio;
    private RadioButton mFamous;
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
            LeakCanary.install(getApplication());
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
        mPresenter = null;
        super.onPause();
    }

    /**
     * Restores the activity state from the bundle
     *
     * @param savedInstanceState
     */
    private void restoreState(@NonNull Bundle savedInstanceState) {
        final Quote q = new Quote();
        q.quote = savedInstanceState.getString(PHRASE_TOKEN);
        q.author = savedInstanceState.getString(AUTHOR_TOKEN);
        setQuote(q);
        loadText(savedInstanceState.getString(TRANSLATION_TOKEN));
        if (savedInstanceState.getBoolean(MOVIE_BUTTON_TOKEN)) {
            onMovieClicked(null);
        } else {
            onFamousClicked(null);
        }
    }

    /**
     * A callback associated with the button that activates the quote retrieval.
     *
     * @param view
     */
    public void retrieveQuote(View view) {
        if (mPresenter != null) {
            mPresenter.retrieveQuote();
        }
    }

    /**
     * Create references to required views and initialize the presenter.
     */
    private void init() {
        mTranslation = (TextView) findViewById(R.id.translation);
        mQuoteText = (TextView) findViewById(R.id.phrase);
        mQuoteAuthor = (TextView) findViewById(R.id.author);
        switcher = (ViewSwitcher) findViewById(R.id.my_switcher);
        mButton = (ImageView) findViewById(R.id.retrieveBtn);
        mRadio = (RadioButton) findViewById(R.id.radio_movie);
        mFamous = (RadioButton) findViewById(R.id.radio_famous);
        mPresenter = MVPPresenter.create(this);
    }

    @Override
    public void loadText(String text) {
        if (mTranslation != null) {
            mTranslation.setText(text);
            mTranslation.setCompoundDrawablesWithIntrinsicBounds(R.drawable.yoda_square, 0, 0, 0);
        } else {
            Log.i(Config.appName, "Can not load");
        }
    }


    @Override
    public void onTranslationFailure(final String s) {
        mTranslation.setText(getString(R.string.yoda_default_string));
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setQuote(final Quote quote) {
        mQuoteText.setText(quote.quote);
        mQuoteAuthor.setText(quote.author);

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
    public void startBladeAnimation() {
        Log.i(TAG, "startBladeAnimation: animation start");
    }

    @Override
    public void stopBladeAnimation() {
        Log.i(TAG, "stopBladeAnimation: animation stop");
    }

    @Override
    public void retrieveResponseFailure(final String s) {
        Toast.makeText(MainView.this, s, Toast.LENGTH_SHORT).show();
    }


    public void textViewClicked(View v) {
        switcher.showNext();
    }


    public void onMovieClicked(View view) {
        mPresenter.setCategory(MVPPresenter.CATEGORY_MOVIES);
        mRadio.setEnabled(true);
    }

    public void onFamousClicked(View view) {
        mPresenter.setCategory(MVPPresenter.CATEGORY_FAMOUS);
        mFamous.setEnabled(true);
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putCharSequence(PHRASE_TOKEN, mQuoteText.getText());
        savedInstanceState.putCharSequence(TRANSLATION_TOKEN, mTranslation.getText());
        savedInstanceState.putCharSequence(AUTHOR_TOKEN, mQuoteAuthor.getText());
        savedInstanceState.putBoolean(MOVIE_BUTTON_TOKEN, mRadio.isChecked());

        super.onSaveInstanceState(savedInstanceState);
    }


}
