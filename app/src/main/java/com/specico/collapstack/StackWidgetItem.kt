package com.specico.collapstack

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.specico.collapstack.Utils.MARGIN_START_FACTOR
import com.specico.collapstack.Utils.animateMarginStart
import com.specico.collapstack.Utils.animateMarginTop
import com.specico.collapstack.Utils.screenMetrics

class StackWidgetItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var indexInStack = 0
    private val screenWidth = screenMetrics(context).widthPixels

    fun setExpandedViewState() {
        val collapsedView: ConstraintLayout = findViewById(R.id.collapsedView)
        collapsedView.visibility = LinearLayout.GONE
        val expandedView: ConstraintLayout = findViewById(R.id.expandedView)
        expandedView.visibility = LinearLayout.VISIBLE
        val bottomSafeArea = 0.1 * screenMetrics(context).heightPixels
        val params = expandedView.layoutParams as LayoutParams
        params.bottomMargin = bottomSafeArea.toInt()
        expandedView.layoutParams = params
        val bgView: View = findViewById(R.id.bg)
        bgView.x = -2f
        if (indexInStack == 0) {
            bgView.y = -2f
            animateMarginTop(this, 0, true)
        }
    }

    fun setCollapsedViewState(topSafeArea: Int) {
        val expandedView: ConstraintLayout = findViewById(R.id.expandedView)
        expandedView.visibility = LinearLayout.GONE
        val collapsedView: ConstraintLayout = findViewById(R.id.collapsedView)
        collapsedView.visibility = LinearLayout.VISIBLE
        collapsedView.setOnClickListener {
            val parent = (this.parent as StackWidget)
            parent.currentExpandedIndex = indexInStack
            parent.setStackSelection(indexInStack, true)
        }
        val bgView: View = findViewById(R.id.bg)
        bgView.x = 0f
        if (indexInStack == 0) {
            bgView.y = 0f
            animateMarginTop(this, topSafeArea, true)
        }
    }

    fun onStackSelection(newIndex: Int, topSafeArea: Int, animate: Boolean = true) {

        val params = layoutParams as FrameLayout.LayoutParams
        val bgView: View = findViewById(R.id.bg)
        val bgParams = bgView.layoutParams as LayoutParams

        when {
            newIndex == indexInStack -> {
                setExpandedViewState()
                animateMarginStart(this, 0, animate)
                bgParams.marginStart = -1
                if (indexInStack == 0) {
                    val expandedView: ConstraintLayout = findViewById(R.id.expandedView)
                    expandedView.setPadding(expandedView.paddingLeft, topSafeArea, expandedView.paddingRight, expandedView.paddingBottom)
                }
            }
            newIndex < indexInStack -> {
                // hide this view by pushing and marginStart = 0
                setCollapsedViewState(topSafeArea)
                animateMarginStart(this, 0, animate)
                bgParams.marginStart = -1
            }
            newIndex > indexInStack -> {
                // adjust marginStart
                setCollapsedViewState(topSafeArea)
                animateMarginStart(this, ((newIndex - indexInStack) * MARGIN_START_FACTOR * screenWidth).toInt(), animate)
                bgParams.marginStart = 0
            }
        }
        layoutParams = params
        bgView.layoutParams = bgParams
    }
}
