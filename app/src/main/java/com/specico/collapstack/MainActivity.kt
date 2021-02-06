package com.specico.collapstack




import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.textfield.TextInputEditText


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainFrameLayout: ConstraintLayout = findViewById(R.id.container)

        // create instance of StackWidget add StackWidgetItems created with dummy data
        val stackWidget = StackWidget(this)
        stackWidget.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        stackWidget.setBackgroundColor(Color.parseColor("#131922"))

        stackWidget.addView(getFullNameStackWidgetItem())
        stackWidget.addView(getAgeStackWidgetItem())
        stackWidget.addView(getGenderStackWidgetItem())
        stackWidget.addView(getProfessionStackWidgetItem())

        mainFrameLayout.addView(stackWidget)
    }

    // to get a dummy stack item for full name
    private fun getFullNameStackWidgetItem(): StackWidgetItem {
        val defaultStackItem = getDefaultStackWidgetItem(
                collapsedTitle = "Your Full Name is",
                expandedTitle = "Introduce yourself",
                ctaTitle = "Proceed to introduction"
        )
        val expandedContainerView = defaultStackItem.findViewById<LinearLayout>(R.id.expandedContainerView)
        val expandedEt = getStyledEditText(hint = "Enter your full name here")
        expandedEt.setOnEditorActionListener { v, actionId, event ->
            if ((event != null && (event.keyCode == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_GO)) {
                val stackGroup: StackWidget = defaultStackItem.parent as StackWidget
                stackGroup.performClickNextWidgetItem(defaultStackItem)
                hideKeyboard(expandedEt)
            }
            return@setOnEditorActionListener false
        }
        defaultStackItem.stackWidgetItemListener = object: StackWidgetItemListener {
            override fun onCollapsedState() {
                defaultStackItem.findViewById<TextView>(R.id.collapsedSubtitle).text = expandedEt.text.toString()
                showCollapsedLayout(defaultStackItem)
            }

            override fun onExpandedState() {
                showExpandedLayout(defaultStackItem)
            }

            override fun onCtaState() {
                showCtaLayout(defaultStackItem)
            }
        }
        expandedContainerView.addView(expandedEt)
        return defaultStackItem
    }

    // to get a dummy stack item for age
    private fun getAgeStackWidgetItem(): StackWidgetItem {
        val defaultStackItem = getDefaultStackWidgetItem(
                collapsedTitle = "Your age is",
                expandedTitle = "Tell us how old are you?",
                ctaTitle = "Proceed to age section"
        )
        val expandedContainerView = defaultStackItem.findViewById<LinearLayout>(R.id.expandedContainerView)
        val expandedEt = getStyledEditText(hint = "Enter your age here")
        expandedEt.setOnEditorActionListener { v, actionId, event ->
            if ((event != null && (event.keyCode == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_GO)) {
                val stackGroup: StackWidget = defaultStackItem.parent as StackWidget
                stackGroup.performClickNextWidgetItem(defaultStackItem)
                hideKeyboard(expandedEt)
            }
            return@setOnEditorActionListener false
        }
        defaultStackItem.stackWidgetItemListener = object: StackWidgetItemListener {
            override fun onCollapsedState() {
                defaultStackItem.findViewById<TextView>(R.id.collapsedSubtitle).text = expandedEt.text.toString()
                showCollapsedLayout(defaultStackItem)
            }

            override fun onExpandedState() {
                showExpandedLayout(defaultStackItem)
            }

            override fun onCtaState() {
                showCtaLayout(defaultStackItem)
            }
        }
        expandedContainerView.addView(expandedEt)
        return defaultStackItem
    }

    // to get a dummy stack item for gender
    private fun getGenderStackWidgetItem(): StackWidgetItem {
        val defaultStackItem = getDefaultStackWidgetItem(
                collapsedTitle = "Your are a",
                expandedTitle = "Tell us your gender?",
                ctaTitle = "Proceed to gender section"
        )
        val expandedContainerView = defaultStackItem.findViewById<LinearLayout>(R.id.expandedContainerView)
        val radioGroup = getStyledRadioGroup(titles = arrayOf("Male", "Female"))
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            if(checkedId > -1) {
                val radioButton: RadioButton = group.findViewById(checkedId)
                defaultStackItem.findViewById<TextView>(R.id.collapsedSubtitle).text = radioButton.text.toString()
            }
        }
        expandedContainerView.addView(radioGroup)
        defaultStackItem.stackWidgetItemListener = object: StackWidgetItemListener {
            override fun onCollapsedState() {
                showCollapsedLayout(defaultStackItem)
            }

            override fun onExpandedState() {
                showExpandedLayout(defaultStackItem)
            }

            override fun onCtaState() {
                showCtaLayout(defaultStackItem)
            }
        }
        return defaultStackItem
    }

    // to get a dummy stack item for profession
    private fun getProfessionStackWidgetItem(): StackWidgetItem {
        val defaultStackItem = getDefaultStackWidgetItem(
                collapsedTitle = "Your Profession is",
                expandedTitle = "Tell us what you do?",
                ctaTitle = "Proceed to profession details"
        )
        val expandedContainerView = defaultStackItem.findViewById<LinearLayout>(R.id.expandedContainerView)
        val expandedEt = getStyledEditText(hint = "Enter your profession here")
        expandedEt.setOnEditorActionListener { v, actionId, event ->
            if ((event != null && (event.keyCode == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_GO)) {
                val stackGroup: StackWidget = defaultStackItem.parent as StackWidget
                stackGroup.performClickNextWidgetItem(defaultStackItem)
                hideKeyboard(expandedEt)
            }
            return@setOnEditorActionListener false
        }
        defaultStackItem.stackWidgetItemListener = object: StackWidgetItemListener {
            override fun onCollapsedState() {
                defaultStackItem.findViewById<TextView>(R.id.collapsedSubtitle).text = expandedEt.text.toString()
                showCollapsedLayout(defaultStackItem)
            }

            override fun onExpandedState() {
                showExpandedLayout(defaultStackItem)
            }

            override fun onCtaState() {
                showCtaLayout(defaultStackItem)
            }
        }
        expandedContainerView.addView(expandedEt)
        return defaultStackItem
    }

    // to get a stack item with basic settings done
    private fun getDefaultStackWidgetItem(collapsedTitle: String?, expandedTitle: String?, ctaTitle: String?): StackWidgetItem {
        val view: StackWidgetItem = View.inflate(this, R.layout.stack_widget_item, null) as StackWidgetItem
        val collapsedLayout: View = View.inflate(this, R.layout.collapsed_state_layout, null)
        view.addView(collapsedLayout, 0)
        val expandedLayout: View = View.inflate(this, R.layout.expanded_state_layout, null)
        view.addView(expandedLayout, 1)
        val ctaLayout: View = View.inflate(this, R.layout.cta_state_layout, null)
        view.addView(ctaLayout, 2)

        val collapsedTitleTv: TextView = collapsedLayout.findViewById(R.id.collapsedTitle)
        if(collapsedTitle.isNullOrEmpty().not()) {
            collapsedTitleTv.text = collapsedTitle
        }
        else {
            collapsedTitleTv.visibility = LinearLayout.GONE
        }

        val expandedTitleTv: TextView = expandedLayout.findViewById(R.id.expandedTitle)
        if(expandedTitle.isNullOrEmpty().not()) {
            expandedTitleTv.text = expandedTitle
        }
        else {
            expandedTitleTv.visibility = LinearLayout.GONE
        }

        val ctaTitleTv: TextView = ctaLayout.findViewById(R.id.ctaTitle)
        if(ctaTitle.isNullOrEmpty().not()) {
            ctaTitleTv.text = ctaTitle
        }
        else {
            ctaTitleTv.visibility = LinearLayout.GONE
        }

        return view
    }

    // show only expanded view inside stack group --> stack item in expanded state
    fun showExpandedLayout(stackWidgetItem: StackWidgetItem) {
        val collapsedView: ConstraintLayout = stackWidgetItem.findViewById(R.id.collapsedView)
        collapsedView.visibility = LinearLayout.GONE
        val ctaView: ConstraintLayout = stackWidgetItem.findViewById(R.id.ctaView)
        ctaView.visibility = LinearLayout.GONE
        val expandedView: ConstraintLayout = stackWidgetItem.findViewById(R.id.expandedView)
        expandedView.visibility = LinearLayout.VISIBLE
    }

    // show only collapsed view inside stack group --> stack item in collapsed state
    fun showCollapsedLayout(stackWidgetItem: StackWidgetItem) {
        val expandedView: ConstraintLayout = stackWidgetItem.findViewById(R.id.expandedView)
        expandedView.visibility = LinearLayout.GONE
        val ctaView: ConstraintLayout = stackWidgetItem.findViewById(R.id.ctaView)
        ctaView.visibility = LinearLayout.GONE
        val collapsedView: ConstraintLayout = stackWidgetItem.findViewById(R.id.collapsedView)
        collapsedView.visibility = LinearLayout.VISIBLE
    }

    // show only CTA view inside stack group --> stack item in CTA state
    fun showCtaLayout(stackWidgetItem: StackWidgetItem) {
        val expandedView: ConstraintLayout = stackWidgetItem.findViewById(R.id.expandedView)
        expandedView.visibility = LinearLayout.GONE
        val collapsedView: ConstraintLayout = stackWidgetItem.findViewById(R.id.collapsedView)
        collapsedView.visibility = LinearLayout.GONE
        val ctaView: ConstraintLayout = stackWidgetItem.findViewById(R.id.ctaView)
        ctaView.visibility = LinearLayout.VISIBLE
    }

    // to get a custom styled EditText
    private fun getStyledEditText(hint: String): TextInputEditText {
        val editText = TextInputEditText(this)
        editText.setTextColor(Color.parseColor("#ffffff"))
        editText.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#777777"))
        editText.hint = hint
        editText.setHintTextColor(Color.parseColor("#777777"))
        editText.textSize = 18f
        editText.typeface = Typeface.create("serif-monospace", Typeface.NORMAL)
        editText.setLines(1)
        editText.isSingleLine = true
        editText.imeOptions = EditorInfo.IME_ACTION_GO
        return editText
    }

    // to get a custom styled RadioGroup, each RadioButton with title in list
    private fun getStyledRadioGroup(titles: Array<String>): RadioGroup {
        val radioButtonGroup = RadioGroup(this)
        radioButtonGroup.orientation = RadioGroup.HORIZONTAL

        for(title in titles) {
            val radiobutton = RadioButton(this)
            radiobutton.text = title
            radiobutton.setTextColor(Color.parseColor("#ffffff"))
            radiobutton.textSize = 18f
            radiobutton.typeface = Typeface.create("serif-monospace", Typeface.NORMAL)
            radiobutton.buttonTintList = ColorStateList.valueOf(Color.parseColor("#777777"))
            radiobutton.layoutParams = LinearLayout.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT, 1f)
            radioButtonGroup.addView(radiobutton)
        }
        return radioButtonGroup
    }

    // Helper function to hide keyboard during state transitions
    private fun hideKeyboard(view: View) {
        view.clearFocus()
        view.let { v ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }
}