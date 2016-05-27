package com.veontomo.yoda

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.StrictMode
import android.support.v7.app.AppCompatActivity
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.ViewSwitcher
import kotlinx.android.synthetic.main.activity_main.*


class MainView : AppCompatActivity(), MVPView {

    private var mTranslation: TextView? = null
    private var mQuoteText: TextView? = null
    private var mQuoteAuthor: TextView? = null
    private var mSwitcher: ViewSwitcher? = null
    private var mUserInput: EditText? = null
    private var mPresenter: MVPPresenter? = null
    private var mButton: Button? = null
    private var mCheck1: CheckBox? = null
    private var mCheck2: CheckBox? = null
    private var mState: Bundle? = null
    private var mSpanSuccess: ImageSpan? = null
    private var mSpanFailure: ImageSpan? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //noinspection PointlessBooleanExpression
        if (!Config.IS_PRODUCTION) {
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog().build())
            StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build())
        }

        setContentView(R.layout.activity_main)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);
        mState = savedInstanceState;

    }

    override fun onResume() {
        super.onResume()
        init()
        restoreState(mState)
        mState = null
    }

    /**
     * Restores the activity state from a bundle.
     *
     *
     * It is supposed that the bundle has previously been created by [.onSaveInstanceState].

     * @param bundle a bundle containing the view's state
     */
    private fun restoreState(bundle: Bundle?) {
        if (bundle == null) {
            return
        }
        val q = bundle.getParcelable<Quote>(QUOTE_TOKEN)
        mPresenter!!.currentQuote = q
        mPresenter!!.loadCacheAsBundle(bundle.getBundle(CACHE_TOKEN))
        mPresenter!!.setCategoryStatuses(bundle.getBooleanArray(CHECK_TOKEN))
        mPresenter!!.translationStatus = bundle.getShort(TRANSLATION_STATUS_TOKEN)
        mPresenter!!.enableUserInput(bundle.getBoolean(SWITCHER_TOKEN))
        onTranslationReady(bundle.getString(TRANSLATION_TOKEN))
        setQuote(q)
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putString(TRANSLATION_TOKEN, mTranslation!!.text.toString().trim { it <= ' ' })
        savedInstanceState.putBooleanArray(CHECK_TOKEN, booleanArrayOf(mCheck1!!.isChecked, mCheck2!!.isChecked))
        savedInstanceState.putBoolean(SWITCHER_TOKEN, mPresenter!!.isUserInputActive)
        savedInstanceState.putBundle(CACHE_TOKEN, mPresenter!!.cacheAsBundle)
        savedInstanceState.putShort(TRANSLATION_STATUS_TOKEN, mPresenter!!.translationStatus)
        savedInstanceState.putParcelable(QUOTE_TOKEN, mPresenter!!.currentQuote)
        super.onSaveInstanceState(savedInstanceState)
    }

    /**
     * A callback associated with the button that elaborates the user input.
     *
     *
     * If the edit text field corresponding to the user input is not empty,
     * then a translation of that string is initiated.
     * Otherwise, a quote retrieval is initiated.

     * @param view view a click on which triggers execution of this method. It is not used in the method.
     */
    fun elaborate(@SuppressWarnings("UnusedParameters") view: View) {
        if (mPresenter == null) {
            showMessage(getResources().getString(R.string.no_presenter_error))
        }
        if (mPresenter!!.isUserInputActive) {
            mPresenter!!.enableUserInput(false)
            val userInput = mUserInput!!.editableText.toString()
            if (userInput.isEmpty()) {
                mPresenter!!.retrieveQuote()
            } else {
                mUserInput!!.setText(null)
                mPresenter!!.translate(userInput)
            }
        } else {
            mPresenter!!.retrieveQuote()
        }
    }

    private fun showMessage(txt: String) {
        Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_SHORT).show()
    }

    /**
     * Create references to required views and initialize the presenter.
     */
    private fun init() {
        mTranslation = this.translation
        mQuoteText = this.phrase
        mQuoteAuthor = this.author
        mSwitcher = this.my_switcher
        mUserInput = this.input_view
        mButton = this.retrieveBtn
        mCheck1 = this.check_1
        mCheck2 = this.check_2
        mPresenter = MVPPresenter.create(this)

        mSpanSuccess = createImageSpan(R.drawable.success)
        mSpanFailure = createImageSpan(R.drawable.failure)
    }

    /**
     * Creates an image span from a drawable with the given id.

     * @param id an id of a drawable
     * *
     * @return ImageSpan or null
     */
    private fun createImageSpan(id: Int): ImageSpan? {
        val drawable: Drawable?
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            drawable = getResources().getDrawable(id, null)
        } else {
            drawable = getResources().getDrawable(id)
        }
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            return ImageSpan(drawable, ImageSpan.ALIGN_BASELINE)
        }
        return null
    }

    /**
     * Prepends the success image to the given text and then displays it in the translation field.
     * After that, enables the button.

     * @param s a string to be display in the translation field
     */
    override fun onTranslationReady(s: String) {
        showTranslation(s)
        disableButton(false)
    }


    /**
     * Prepends the failure image to the given text and then displays it in the translation field.
     * After that, enables the button.

     * @param s a string to be display in the translation field
     */
    override fun onTranslationFailure(s: String) {
        showTranslation(getResources().getString(R.string.yoda_translate_failure_string))
        showMessage(s)
        disableButton(false)
    }

    /**
     * Prepends the failure image to the Yoda default text and then displays it in the translation field.
     * After that, enables the button.
     */
    override fun onTranslationProblem() {
        showTranslation(getResources().getString(R.string.yoda_translate_problem_string))
        disableButton(false)
    }

    /**
     * Sets the statuses of the view checkboxes.
     *
     *
     * The array must contain exactly two elements.
     * Otherwise, the method does nothing.

     * @param statuses array containing statuses of two checkboxes.
     */
    override fun setCategories(statuses: BooleanArray?) {
        if (statuses != null && statuses.size == 2) {
            mCheck1!!.isChecked = statuses[0]
            mCheck2!!.isChecked = statuses[1]
        }
    }

    /**
     * Prepends an image span to a given string and displays the obtained object in the
     * translation field.
     *
     *
     * What image span should be used is decided based on the value of [MVPPresenter.mTranslationStatus].

     * @param txt a string that should be displayed in the translation field
     */
    private fun showTranslation(txt: String) {
        if (mTranslation == null) {
            Log.i(Config.appName, "Can not load since the translation text view is null.")
            return
        }
        val ss = SpannableString("  " + txt)
        val status = mPresenter!!.translationStatus
        val image: ImageSpan?
        if (status == MVPPresenter.TRANSLATION_OK) {
            image = mSpanSuccess
        } else if (status == MVPPresenter.TRANSLATION_FAILURE || status == MVPPresenter.TRANSLATION_PROBLEM) {
            image = mSpanFailure
        } else {
            image = null
        }
        if (image != null) {
            ss.setSpan(image, 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            mTranslation!!.text = ss
        } else {
            mTranslation!!.text = txt
        }
    }

    override fun setQuote(quote: Quote?) {
        if (quote != null) {
            mQuoteText!!.text = quote.quote
            mQuoteAuthor!!.text = quote.author

        }
        setCheckboxVisibility(View.VISIBLE)
    }


    /**
     * Enables/Disables the button that activates phrase retrieval

     * @param b true - to disable, false - to enable
     */
    override fun disableButton(b: Boolean) {
        mButton!!.isEnabled = !b
    }

    override fun retrieveResponseFailure(s: String) {
        Toast.makeText(this@MainView, s, Toast.LENGTH_SHORT).show()
    }

    override fun onQuoteProblem(quote: Quote) {
        Log.i(TAG, "onQuoteProblem: some problem with " + quote.toString())
    }

    /**
     * Enables the input field that is initially hidden in the switcher view.
     *
     *
     * This method is executed once the corresponding edit text view is clicked.
     * For this reason the argument is required, but the method does not use it.

     * @param v a click on this view triggers the execution of this method
     */
    fun textViewClicked(@SuppressWarnings("UnusedParameters") v: View) {
        mSwitcher!!.showNext()
        mPresenter!!.enableUserInput(true)
        setCheckboxVisibility(View.INVISIBLE)
        val txt = mQuoteText!!.text.toString()
        if ("" != txt) {
            mUserInput!!.setText(txt)
        }
        mUserInput!!.requestFocus()
    }

    private fun setCheckboxVisibility(visibility: Int) {
        mCheck1!!.visibility = visibility
        mCheck2!!.visibility = visibility
    }


    /**
     * Passes the statuses of the checkboxes to the presenter.

     * @param view a click on this view triggers the execution of this method
     */
    fun onCategoryStatusChange(@SuppressWarnings("UnusedParameters") view: View) {
        mPresenter!!.setCategoryStatuses(booleanArrayOf(mCheck1!!.isChecked, mCheck2!!.isChecked))
    }

    /**
     * Enable/disable the user input in the switcher and adjust the
     * button text.

     * @param status true to enable, false to disable
     */
    override fun enableUserInput(status: Boolean) {
        if (status) {
            mSwitcher!!.displayedChild = 1
            mButton!!.setText(getText(R.string.translate))
        } else {
            mSwitcher!!.displayedChild = 0
            mButton!!.setText(getText(R.string.i_feel_lucky))
        }
    }

    companion object {
        private val TAG = Config.appName
        /**
         * a string key under which the content of the phrase field is to be saved
         * when saving the activity state for further recreating
         */
        private val QUOTE_TOKEN = "phrase"

        /**
         * a string key under which the content of the translation field is to be saved
         * when saving the activity state for further recreating
         */
        private val TRANSLATION_TOKEN = "translation"

        /**
         * a string key under which the statuses of the check boxes corresponding to categories
         * are to be saved when saving the activity state for further recreating
         */
        private val CHECK_TOKEN = "categories"

        /**
         * a string key under which the status of the mSwitcher is to be saved
         * when saving the activity state for further recreating
         */
        private val SWITCHER_TOKEN = "switcher"


        /**
         * a string key under which the cache is to be saved
         * when saving the activity state for further recreating
         */
        private val CACHE_TOKEN = "cache"

        /**
         * a string key under which the translation status is to be saved
         * when saving the activity state for further recreating
         */
        private val TRANSLATION_STATUS_TOKEN = "translation_status"
    }


}
