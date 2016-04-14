package com.veontomo.yoda;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class MainView extends AppCompatActivity implements MVPView {

    private EditText mInput;
    private TextView mTranslation;
    private TextView mQuote;
    private MVPPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onStart(){
        super.onStart();
        init();
    }

    public void translate(View view){
        if (mPresenter != null){
            mPresenter.onTranslate(mInput.getEditableText().toString());
        }
    }

    /**
     * Initializes the MVP entities
     */
    private void init() {
        mInput = (EditText) findViewById(R.id.editText);
        mTranslation = (TextView) findViewById(R.id.translation);
        mQuote = (TextView) findViewById(R.id.phrase);
        mPresenter = new MVPPresenter(this);
    }

    @Override
    public void loadText(String text) {
        if (mTranslation != null){
            mTranslation.setText(text);
        } else {
            Log.i(Config.appName, "Can not load");
        }
    }

    @Override
    public void loadList(List<String> sayings) {

    }

    @Override
    public void onTranslationFailure() {
        mTranslation.setText(getString(R.string.translation_failed));
    }

    @Override
    public void setQuote(Quote quote) {
        mQuote.setText(quote.quote + "\n" + quote.author);

    }

    public void clean(View view) {
        mInput.setText(null);
    }
}
