package com.fransbudikashira.storyapp.ui.components

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.fransbudikashira.storyapp.R
import com.fransbudikashira.storyapp.utils.isValidEmail

class NameEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), View.OnTouchListener {

    private var iconStart: Drawable
    private var clearButtonImage: Drawable
    private var background: Drawable

    init {
        background = ContextCompat.getDrawable(context, R.drawable.custom_input) as Drawable
        iconStart = ContextCompat.getDrawable(context, R.drawable.custom_person_icon) as Drawable
        clearButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_clear) as Drawable

        setButtonDrawables()
        setBackgroundDrawable(background)

        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        textSize = 16f
        inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

        // Clear button
        setOnTouchListener(this)

        // Menambahkan aksi ketika ada perubahan text akan memunculkan clear button
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) showClearButton() else hideClearButton()

                error = if (s.toString().count() < 3)
                    context.getString(R.string.invalid_name) else null
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun showClearButton() {
        setButtonDrawables(endOfTheText = clearButtonImage)
    }

    private fun hideClearButton() {
        setButtonDrawables()
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = iconStart,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null,
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val clearButtonStart: Float
            val clearButtonEnd: Float
            var isClearButtonClicked = false

            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                clearButtonEnd = (clearButtonImage.intrinsicWidth + paddingStart).toFloat()
                when {
                    event.x < clearButtonEnd -> isClearButtonClicked = true
                }
            } else {
                clearButtonStart = (width - paddingEnd - clearButtonImage.intrinsicWidth).toFloat()
                when {
                    event.x > clearButtonStart -> isClearButtonClicked = true
                }
            }

            // Ketika button close diklik
            if (isClearButtonClicked) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        clearButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_clear) as Drawable
                        showClearButton()
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        clearButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_clear) as Drawable
                        when {
                            text != null -> text?.clear()
                        }
                        hideClearButton()
                        return  true
                    }
                    else -> return false
                }
            } else return false
        }
        return false
    }

}