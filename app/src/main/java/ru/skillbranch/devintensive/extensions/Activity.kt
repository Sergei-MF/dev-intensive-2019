package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Спрятать клавиатуру
 */
fun Activity.hideKeyboard() {
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

