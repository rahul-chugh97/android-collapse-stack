package com.specico.collapstack

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.children
import com.specico.collapstack.Utils.MARGIN_TOP_FACTOR
import com.specico.collapstack.Utils.animateMarginTop
import com.specico.collapstack.Utils.screenMetrics

class StackWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val widgetItemsList: MutableList<StackWidgetItem> = emptyList<StackWidgetItem>().toMutableList()
    var currentExpandedIndex = 0
    private val screenHeight = screenMetrics(context).heightPixels
    var topSafeArea = 0

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (hasWindowFocus && widgetItemsList.isEmpty()) {
            children.forEachIndexed { index, childView ->
                if (childView is StackWidgetItem) {
                    widgetItemsList.add(childView)
                    childView.indexInStack = widgetItemsList.indexOf(childView)
                    if (index > 0) {
                        val params = childView.layoutParams as FrameLayout.LayoutParams
                        params.topMargin = screenHeight
                        childView.layoutParams = params
                    }
                }
            }

            if (widgetItemsList.size < 2) {
                throw Exception("Minimum 2 StackWidgetItem should be added")
            }

            if (widgetItemsList.size > 4) {
                throw Exception("Maximum 4 StackWidgetItem can be added for good UX")
            }

            if (widgetItemsList.isNotEmpty()) {
                setStackSelection(0, false)
                currentExpandedIndex = 0
            }
        }
    }

    fun setStackSelection(index: Int, animate: Boolean = true) {
        if (widgetItemsList.isNotEmpty()) {
            currentExpandedIndex = index

            for (item in widgetItemsList) {
                // loop all stack items and let them update based on current selection
                val marginTop = (item.indexInStack * MARGIN_TOP_FACTOR * screenHeight) + topSafeArea
                item.onStackSelection(currentExpandedIndex, topSafeArea, animate)
                if (currentExpandedIndex == item.indexInStack) {
                    if (item.indexInStack == 0) {
                        animateMarginTop(item, 0, animate)
                    } else {
                        animateMarginTop(item, marginTop.toInt(), animate)
                    }
                } else if (currentExpandedIndex < item.indexInStack) {
                    animateMarginTop(item, screenHeight, animate)
                } else if (currentExpandedIndex > item.indexInStack) {
                    animateMarginTop(item, marginTop.toInt(), animate)
                }
                item.bringToFront()
            }
        }
    }

    fun nextStackSelection(animate: Boolean = true) {
        if (widgetItemsList.isNotEmpty() && currentExpandedIndex < (widgetItemsList.size-1)) {
            currentExpandedIndex++
            setStackSelection(currentExpandedIndex, animate)
        }
    }

    fun getCollapsedViewContainer(): ViewGroup {
        return findViewById(R.id.collapsedView)
    }

    fun getExpandedViewContainer(): ViewGroup {
        return findViewById(R.id.expandedView)
    }
}
