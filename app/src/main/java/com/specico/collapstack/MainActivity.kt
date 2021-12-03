package com.specico.collapstack

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // create instance of StackWidget add StackWidgetItems created with dummy data
        val stackWidget: StackWidget = findViewById(R.id.stackWidget)

        for (index in 1..4) {
            val stackWidgetItem = getDefaultStackWidgetItem()
            stackWidget.addView(stackWidgetItem)
        }

        val spacer: View = findViewById(R.id.spacer)
        spacer.addOnLayoutChangeListener { view, _, _, _, _, _, _, _, _ ->
            stackWidget.topSafeArea = view.height
        }

        val cta: AppCompatButton = findViewById(R.id.cta)
        cta.setOnClickListener {
//            stackWidget.setStackSelection(2, false)
            stackWidget.nextStackSelection()
        }
    }

    // to get a stack item with basic settings done
    private fun getDefaultStackWidgetItem(): StackWidgetItem {
        return View.inflate(this, R.layout.stack_widget_item, null) as StackWidgetItem
    }
}
