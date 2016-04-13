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
    private TextView mText;
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
        mText = (TextView) findViewById(R.id.text);
        mPresenter = new MVPPresenter(this);
    }

    @Override
    public void loadText(String text) {
        if (mText != null){
            mText.setText(text);
        } else {
            Log.i(Config.appName, "Can not load");
        }
    }

    @Override
    public void loadList(List<String> sayings) {

    }

    @Override
    public void onTranslationFailure() {
        mText.setText(getString(R.string.translation_failed));
    }
}
