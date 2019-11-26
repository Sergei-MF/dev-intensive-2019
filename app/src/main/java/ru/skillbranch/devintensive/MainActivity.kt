package ru.skillbranch.devintensive

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.devintensive.extensions.toDp
import ru.skillbranch.devintensive.extensions.toPx
import ru.skillbranch.devintensive.models.Bender

class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var benderImage: ImageView
    lateinit var sendBtn: ImageView
    lateinit var textTxt: TextView
    lateinit var messageEt: EditText
    lateinit var benderObj: Bender
    lateinit var rootView: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        benderImage = iv_bender
        textTxt = tv_text
        messageEt = et_message
        sendBtn = iv_send
        rootView = root_view

        savedInstanceState.restoreInstanceState()

        sendBtn.setOnClickListener(this)
        //Назначение actionDone
        messageEt.setActionDoneClickListener()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_send -> {
                handleAnswer()
            }
        }
    }

    /**
     * Обработка ответа
     */
    private fun handleAnswer() {
        val (phrase, color) = benderObj.listenAnswer(messageEt.text.toString())
        messageEt.setText("")
        val (r, g, b) = color
        benderImage.setColorFilter(Color.rgb(r, g, b), PorterDuff.Mode.MULTIPLY)
        textTxt.text = phrase
    }

    /**
     * Сохранение состояния активити
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("STATUS", benderObj.status.name)
        outState.putString("QUESTION", benderObj.question.name)

        //сохранение введенного текста в поле EditText (et_message) при пересоздании Activity
        outState.putString("ET_TEXT", messageEt.text.toString())
    }

    /**
     * Восстановление состояния активити
     */
    private fun Bundle?.restoreInstanceState() {
        val status = this?.getString("STATUS") ?: Bender.Status.NORMAL.name
        val question = this?.getString("QUESTION") ?: Bender.Question.NAME.name
        benderObj = Bender(Bender.Status.valueOf(status), Bender.Question.valueOf(question))
        textTxt.text = benderObj.askQuestion()

        val (r, g, b) = benderObj.status.color
        benderImage.setColorFilter(Color.rgb(r, g, b), PorterDuff.Mode.MULTIPLY)

        //восстановление введенного текста в поле EditText (et_message) при пересоздании Activity
        val messageEtText = this?.getString("ET_TEXT") ?: ""
        messageEt.setText(messageEtText)
    }

    /**
     * Назначение actionDone
     */
    private fun EditText.setActionDoneClickListener() {
        this.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                handleAnswer()
                //todo hide keyboard
                hideKeyboard()
                true
            } else false
        }
    }

    /**
     * Спрятать клавиатуру
     */
    private fun Activity.hideKeyboard() {
        currentFocus?.let { focus ->
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).apply {
                hideSoftInputFromWindow(focus.windowToken, 0)
            }
        }
    }

    /**
     * открыта ли Software Keyboard
     */
    fun Activity.isKeyboardOpen(): Boolean {
        return measureVisibleDisplayFrameHeight(window.decorView.rootView) >= 250.toPx()
    }

    /**
     * закрыта ли Software Keyboard
     */
    fun Activity.isKeyboardClosed(): Boolean {
        return measureVisibleDisplayFrameHeight(window.decorView.rootView) < 250.toPx()
    }

    /**
     * Измерение высоты экрана
     */
    fun measureVisibleDisplayFrameHeight(rootView: View): Int {
        var heightDiff = -1
//        rootView.viewTreeObserver.addOnGlobalLayoutListener {
        val rect = Rect()
        rootView.getWindowVisibleDisplayFrame(rect)
        heightDiff = rootView.rootView.height - (rect.bottom - rect.top)
//        }
        return heightDiff
    }
}
