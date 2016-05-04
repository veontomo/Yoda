package com.veontomo.yoda;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class MainView extends AppCompatActivity implements MVPView {
    private static final String TAG = Config.appName;
    private TextView mTranslation;
    private TextView mQuoteText;
    private TextView mQuoteAuthor;
    private ViewSwitcher switcher;
    private MVPPresenter mPresenter;
    private ImageView mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public void onStart() {
        super.onStart();
        init();


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
    public void setQuote(Quote quote) {
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


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_movie:
                if (checked)
                    mPresenter.setCategory(MVPRetrieveModel.CATEGORY_MOVIES);
                break;
            case R.id.radio_famous:
                if (checked)
                    mPresenter.setCategory(MVPRetrieveModel.CATEGORY_FAMOUS);
                break;
        }
    }


}
