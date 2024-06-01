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


class PasswordEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), View.OnTouchListener {

    private var background: Drawable
    private var iconStart: Drawable
    private var iconEndVisible: Drawable
    private var iconEndInvisible: Drawable
    private var isPasswordVisible = false

    init {
        background = ContextCompat.getDrawable(context, R.drawable.custom_input) as Drawable
        iconStart = ContextCompat.getDrawable(context, R.drawable.custom_lock_icon) as Drawable
        iconEndVisible = ContextCompat.getDrawable(context, R.drawable.ic_visibility) as Drawable
        iconEndInvisible = ContextCompat.getDrawable(context, R.drawable.ic_visibility_off) as Drawable

        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        textSize = 16f

        setButtonDrawables(
            endOfTheText = iconEndInvisible
        )
        setBackgroundDrawable(background)

        // Set up the toggle password visibility functionality
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                error = if (s.toString().count() < 8)
                    context.getString(R.string.invalid_password) else null
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun togglePasswordVisibility() {
        if (isPasswordVisible) {
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            setButtonDrawables(endOfTheText = iconEndVisible)
        } else {
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            setButtonDrawables(endOfTheText = iconEndInvisible)
        }
        // Move cursor to the end of the text
        setSelection(text?.length ?: 0)
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
            val toggleButtonStart: Float
            val toggleButtonEnd: Float
            var isToggleButtonClicked = false

            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                toggleButtonEnd = (iconEndInvisible.intrinsicWidth + paddingStart).toFloat()
                if (event.x < toggleButtonEnd) isToggleButtonClicked = true
            } else {
                toggleButtonStart = (width - paddingEnd - iconEndInvisible.intrinsicWidth).toFloat()
                if (event.x > toggleButtonStart) isToggleButtonClicked = true
            }

            if (isToggleButtonClicked) {
                if (event.action == MotionEvent.ACTION_UP) {
                    isPasswordVisible = !isPasswordVisible
                    togglePasswordVisibility()
                    return true
                }
                return true
            }
            return false
        }
        return false
    }
}