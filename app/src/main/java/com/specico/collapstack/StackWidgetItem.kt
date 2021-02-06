package com.specico.collapstack

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

class StackWidgetItem @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    var stackWidgetItemListener: StackWidgetItemListener? = null

    fun setExpandedViewState() {
        stackWidgetItemListener?.onExpandedState()
    }

    fun setCollapsedViewState() {
        stackWidgetItemListener?.onCollapsedState()
    }

    fun setCtaViewState() {
        stackWidgetItemListener?.onCtaState()
    }

}

interface StackWidgetItemListener {
    fun onCollapsedState()
    fun onExpandedState()
    fun onCtaState()
}