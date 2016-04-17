package com.veontomo.yoda;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.util.Date;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        consequtiveStreams();
        calllog();


    }

    private void calllog() {
        Log.i(Config.appName, "last call: " + CallLog.Calls.getLastOutgoingCall(this));
        StringBuffer stringBuffer = new StringBuffer();
        //if (checkPermission())
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.i(Config.appName, "no permissions");
            return;
        }
        Cursor cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI,
                null, null, null, CallLog.Calls.DATE + " DESC");
        int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = cursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = cursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);
        while (cursor.moveToNext()) {
            String phNumber = cursor.getString(number);
            String callType = cursor.getString(type);
            String callDate = cursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = cursor.getString(duration);
            String dir = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
            }
            stringBuffer.append("\nPhone Number:--- " + phNumber + " \nCall Type:--- "
                    + dir + " \nCall Date:--- " + callDayTime
                    + " \nCall duration in sec :--- " + callDuration);
            stringBuffer.append("\n----------------------------------");
        }
        cursor.close();
    }


    @Override
    public void onStart(){
        super.onStart();
        init();
    }

    public void start(View view){
        //mQuoteText.setText("aaaaaaaaaaaaaaaa");
        // switcher.showPrevious();
        if (mPresenter != null){
            mPresenter.onStart();
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



    public void textViewClicked(View v) {
        switcher.showNext();
    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
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


    /**
     * A toy example of consecutive streams
     */
    private void consequtiveStreams() {
        PublishSubject<String> subject = PublishSubject.create();
        Observer<String> greeter = new Observer<String>() {

            @Override
            public void onCompleted() {
                Log.i(Config.appName, "No more greetings." + " " + System.currentTimeMillis());
            }

            @Override
            public void onError(Throwable e) {
                Log.i(Config.appName, "I'd greet somebody more!");
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
