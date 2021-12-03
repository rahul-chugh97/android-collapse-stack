package com.specico.collapstack

import android.animation.ValueAnimator
import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.core.view.marginStart
import androidx.core.view.marginTop

object Utils {

    val screenMetrics: (Context) -> DisplayMetrics = {
        val outMetrics = DisplayMetrics()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            val display = it.display
            display?.getRealMetrics(outMetrics)
        } else {
            val windowManager = it.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            windowManager.defaultDisplay.getMetrics(outMetrics)
        }

        outMetrics
    }

    fun animateMarginTop(view: View, newMargin: Int, animate: Boolean = true) {
        if (animate) {
            ValueAnimator.ofInt(view.marginTop, newMargin).apply {
                addUpdateListener {
                    val params = view.layoutParams as FrameLayout.LayoutParams
                    params.topMargin = it.animatedValue as Int
                    view.layoutParams = params
                }
                duration = ANIM_DURATION
            }.start()
        } else {
            val params = view.layoutParams as FrameLayout.LayoutParams
            params.topMargin = newMargin
            view.layoutParams = params
        }
    }

    fun animateMarginStart(view: View, newMargin: Int, animate: Boolean = true) {
        if (animate) {
            ValueAnimator.ofInt(view.marginStart, newMargin).apply {
                addUpdateListener {
                    val params = view.layoutParams as FrameLayout.LayoutParams
                    params.marginStart = it.animatedValue as Int
                    view.layoutParams = params
                }
                duration = ANIM_DURATION
            }.start()
        } else {
            val params = view.layoutParams as FrameLayout.LayoutParams
            params.marginStart = newMargin
            view.layoutParams = params
        }
    }

    private const val ANIM_DURATION = 500L
    const val MARGIN_TOP_FACTOR = 0.13f
    const val MARGIN_START_FACTOR = 0.07f
}
