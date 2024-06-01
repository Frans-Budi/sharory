package com.fransbudikashira.storyapp.ui.components

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.fransbudikashira.storyapp.R

class TitleText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {

    private var textColor: Int = ContextCompat.getColor(context, R.color.textColor)

    init {
        setTextColor(textColor)
        textSize = 22f
        setTypeface(null, Typeface.BOLD)
    }
}