package com.example.rmcontrol

import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnTouchListener

/**
 * A class that can be used as a TouchListener on any view (e.g. a Button).
 * It cyclically runs a clickListener, emulating keyboard-like behavior. First
 * click is fired immediately, next one after the initialInterval, and subsequent
 * ones after the normalInterval.
 *
 * Interval is scheduled after the onClick completes, so it has to run fast.
 * If it runs slow, it does not generate skipped onClicks. Can be rewritten to
 * achieve this.
 */
class RepeatListener(
    private val initialInterval: Int,
    private val normalInterval: Int,
    private val clickListener: OnClickListener
) : OnTouchListener {

    private val handler = Handler()
    private var touchedView: View? = null

    private val handlerRunnable: Runnable = object : Runnable {
        override fun run() {
            touchedView?.let { view ->
                if (view.isEnabled) {
                    handler.postDelayed(this, normalInterval.toLong())
                    clickListener.onClick(view)
                } else {
                    handler.removeCallbacks(this)
                    view.isPressed = false
                    touchedView = null
                }
            }
        }
    }

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                handler.removeCallbacks(handlerRunnable)
                handler.postDelayed(handlerRunnable, initialInterval.toLong())
                touchedView = view
                view.isPressed = true
                clickListener.onClick(view)
                return true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                handler.removeCallbacks(handlerRunnable)
                touchedView?.isPressed = false
                touchedView = null
                return true
            }
        }
        return false
    }
}