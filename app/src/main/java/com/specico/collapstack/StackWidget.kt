package com.specico.collapstack

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.view.children

class StackWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    var viewWidth = 0
    var viewHeight = 0
    val widgetItemsList: MutableList<StackWidgetItem> = emptyList<StackWidgetItem>().toMutableList()
    lateinit var stackWidgetListener: StackWidgetListener
    init {
        stackWidgetListener = object: StackWidgetListener {
            @SuppressLint("ClickableViewAccessibility")
            override fun onStackWidgetItemChanged(visibleWidgetItem: StackWidgetItem) {
                try {
                    val visibleWidgetItemIndex = widgetItemsList.indexOf(visibleWidgetItem)

                    val nextWidgetItemIndex = visibleWidgetItemIndex + 1
                    if (nextWidgetItemIndex == widgetItemsList.size) {

                        visibleWidgetItem.visibility = View.VISIBLE
                        visibleWidgetItem.bringToFront()
                        translateY(visibleWidgetItem, (visibleWidgetItemIndex * 0.15 * viewHeight).toFloat())
                        visibleWidgetItem.setExpandedViewState()
                        return
                    }
                    val nextWidgetItem = widgetItemsList[nextWidgetItemIndex]

                    visibleWidgetItem.visibility = View.VISIBLE
                    visibleWidgetItem.bringToFront()
                    nextWidgetItem.visibility = View.VISIBLE
                    nextWidgetItem.bringToFront()

                    translateY(visibleWidgetItem, (visibleWidgetItemIndex * 0.15 * viewHeight).toFloat())
                    visibleWidgetItem.setExpandedViewState()

                    translateY(nextWidgetItem, (viewHeight * 0.9).toFloat())
                    nextWidgetItem.setCtaViewState()

                    nextWidgetItem.setOnClickListener {
                        translateY(visibleWidgetItem, (visibleWidgetItemIndex * 0.15 * viewHeight).toFloat())
                        visibleWidgetItem.setCollapsedViewState()
                        visibleWidgetItem.setOnClickListener {
                            resetViewsBelow(nextWidgetItem)
                            stackWidgetListener.onStackWidgetItemChanged(visibleWidgetItem)
                        }
                        stackWidgetListener.onStackWidgetItemChanged(widgetItemsList[nextWidgetItemIndex])
                    }

                    // experimental (with bugs) custom fling for changing state
                    /*nextWidgetItem.setOnTouchListener(object: OnSwipeTouchListener(context) {
                        override fun onSwipeTop() {
                            super.onSwipeTop()
                            stackWidgetListener.onStackWidgetItemChanged(widgetItemsList[nextWidgetItemIndex])
                            visibleWidgetItem.setCollapsedViewState()
                        }

                        override fun onSwipeBottom() {
                            super.onSwipeBottom()
                            stackWidgetListener.onStackWidgetItemChanged(widgetItemsList[widgetItemsList.indexOf(nextWidgetItem)-1])
                        }

                        override fun onTap() {
                            super.onTap()
                            nextWidgetItem.performClick()
                        }
                    })*/

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun resetViewsBelow(visibleView: StackWidgetItem)  {
        val viewIndex = widgetItemsList.indexOf(visibleView)
        for(view in widgetItemsList) {
            val index = widgetItemsList.indexOf(view)
            if(index > viewIndex) {
                view.y = viewHeight.toFloat()
            }
        }
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if(hasWindowFocus && widgetItemsList.isEmpty()) {
            for(childView in children) {
                if(childView is StackWidgetItem) {
                    widgetItemsList.add(childView)
                }
            }

            if(widgetItemsList.size < 2) {
                throw Exception("Minimum 2 StackWidgetItem should be added")
            }

            if(widgetItemsList.size > 4) {
                throw Exception("Maximum 4 StackWidgetItem can be added for good UX")
            }

            for(viewId in widgetItemsList) {
                viewId.translationY = height.toFloat()
                viewId.visibility = View.GONE
            }
            if(widgetItemsList.isNotEmpty()) {
                stackWidgetListener.onStackWidgetItemChanged(widgetItemsList[0])
            }
            else {
                Toast.makeText(context, "Please insert at least 2 stack views", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun performClickNextWidgetItem(currentWidgetItem: StackWidgetItem) {
        val currentWidgetIndex = widgetItemsList.indexOf(currentWidgetItem)
        val nextWidgetIndex = currentWidgetIndex + 1

        if(nextWidgetIndex == widgetItemsList.size) {
            return
        }
        val nextWidgetItem = widgetItemsList[nextWidgetIndex]
        nextWidgetItem.performClick()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewWidth = w
        viewHeight = h
    }

    fun translateY(view: View, toPosition: Float) {
        ObjectAnimator.ofFloat(view, "y", toPosition).apply {
            duration = 500
            start()
        }
    }
}

interface StackWidgetListener {
    fun onStackWidgetItemChanged(view: StackWidgetItem)
}