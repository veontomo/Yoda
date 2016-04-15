package com.veontomo.yoda;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import rx.Observer;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class MainView extends AppCompatActivity implements MVPView {

    private EditText mInput;
    private TextView mTranslation;
    private TextView mQuoteText;
    private TextView mQuoteAuthor;
    private MVPPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        consequtiveStreams();
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
        mQuoteText = (TextView) findViewById(R.id.phrase);
        mQuoteAuthor = (TextView) findViewById(R.id.author);
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
    public void onTranslationFailure(final String s) {
        mTranslation.setText(getString(R.string.yoda_default_string));
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setQuote(Quote quote) {
        mQuoteText.setText(quote.quote);
        mQuoteAuthor.setText(quote.author);

    }

    public void clean(View view) {
        mInput.setText(null);
    }


    /**
     * A toy example of consecutive streams
     */
    private void consequtiveStreams() {
        PublishSubject<String> subject = PublishSubject.create();
        Observer<String> greeter = new Observer<String>() {

            @Override
            public void onCompleted() {
                Log.i("GDG1", "No more greetings." + " " + System.currentTimeMillis());
            }

            @Override
            public void onError(Throwable e) {
                Log.i("GDG1", "I'd greet somebody more!");
            }

            @Override
            public void onNext(String t) {
                Log.i(Config.appName, t + " " + System.currentTimeMillis());
            }

        };

        subject.subscribeOn(Schedulers.io()).subscribe(greeter);
        subject.onNext("hi!");
        subject.onNext("bye!");



    }
}
