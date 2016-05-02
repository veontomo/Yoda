package com.veontomo.yoda;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.util.List;

import rx.Observer;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class MainView extends AppCompatActivity implements MVPView {
    private TextView mTranslation;
    private TextView mQuoteText;
    private TextView mQuoteAuthor;
    private ViewSwitcher switcher;
    private MVPPresenter mPresenter;
    private Button mButton;

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

    public void retrievePhrase(View view) {
        if (mPresenter != null) {
            mPresenter.retrievePhrase();
        }
    }

    /**
     * Initializes the MVP entities
     */
    private void init() {
        mTranslation = (TextView) findViewById(R.id.translation);
        mQuoteText = (TextView) findViewById(R.id.phrase);
        mQuoteAuthor = (TextView) findViewById(R.id.author);
        switcher = (ViewSwitcher) findViewById(R.id.my_switcher);
        mButton = (Button) findViewById(R.id.retrieveBtn);
        mPresenter = MVPPresenter.create(this);
    }

    @Override
    public void loadText(String text) {
        if (mTranslation != null) {
            mTranslation.setText(text);
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
                    mPresenter.setCategory(MVPModel.CATEGORY_MOVIES);
                break;
            case R.id.radio_famous:
                if (checked)
                    mPresenter.setCategory(MVPModel.CATEGORY_FAMOUS);
                break;
        }
    }



}
