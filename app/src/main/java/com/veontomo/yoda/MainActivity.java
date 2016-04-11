package com.veontomo.yoda;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MVPView {

    private TextView mTextView;
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
            mPresenter.onTranslate(mTextView.getEditableText().toString());
        }
    }

    /**
     * Initializes the MVP entities
     */
    private void init() {
        mTextView = (TextView) findViewById(R.id.editText);
        mPresenter = new MVPPresenter(this, new MVPModel());
    }

    @Override
    public void loadText(String text) {
        if (mTextView != null){
            mTextView.setText(text);
        } else {
            Log.i(Config.appName, "Can not load");
        }
    }

    @Override
    public void loadList(List<String> sayings) {

    }
}
