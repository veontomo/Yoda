package com.veontomo.yoda;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
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
     * a string key under which the status of the check button corresponding to "movie" is to be saved
     * when saving the activity state for further recreating
     */
    private static final String MOVIE_CHECK_TOKEN = "radio_movie";

    /**
     * a string key under which the status of the check button corresponding to "famous" is to be saved
     * when saving the activity state for further recreating
     */
    private static final String FAMOUS_CHECK_TOKEN = "check_famous";

    private TextView mTranslation;
    private TextView mQuoteText;
    private TextView mQuoteAuthor;
    private ViewSwitcher switcher;
    private MVPPresenter mPresenter;
    private ImageView mButton;
    private CheckBox mMovie;
    private CheckBox mFamous;
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
        loadTranslation(savedInstanceState.getString(TRANSLATION_TOKEN));
        if (savedInstanceState.getBoolean(MOVIE_CHECK_TOKEN)) {
//            onMovieClicked(null);
        }
        if (savedInstanceState.getBoolean(FAMOUS_CHECK_TOKEN)) {
//            onFamousClicked(null);
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
        mMovie = (CheckBox) findViewById(R.id.check_movie);
        mFamous = (CheckBox) findViewById(R.id.check_famous);
        mPresenter = MVPPresenter.create(this);
    }

    @Override
    public void loadTranslation(String text) {
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


    public void onCategoryClicked(View view) {
        if (view == null) {
            return;
        }
        final int id = view.getId();
        switch (id) {
            case R.id.check_famous:
                mPresenter.toggleCategoryStatus(MVPPresenter.CATEGORY_MOVIES);
                break;
            case R.id.check_movie:
                mPresenter.toggleCategoryStatus(MVPPresenter.CATEGORY_MOVIES);
                break;
            default:
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putCharSequence(PHRASE_TOKEN, mQuoteText.getText());
        savedInstanceState.putCharSequence(AUTHOR_TOKEN, mQuoteAuthor.getText());
        savedInstanceState.putCharSequence(TRANSLATION_TOKEN, mTranslation.getText());
        savedInstanceState.putBoolean(MOVIE_CHECK_TOKEN, mMovie.isChecked());
        savedInstanceState.putBoolean(FAMOUS_CHECK_TOKEN, mFamous.isChecked());
        super.onSaveInstanceState(savedInstanceState);
    }


}
