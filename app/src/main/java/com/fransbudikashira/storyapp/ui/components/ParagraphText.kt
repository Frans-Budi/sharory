package com.fransbudikashira.storyapp.ui.components

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.fransbudikashira.storyapp.R

class ParagraphText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {

    private var textColor: Int = ContextCompat.getColor(context, R.color.textGrayColor)

    init {
        setTextColor(textColor)
        textSize = 14f
        setTypeface(null, Typeface.NORMAL)
        setLineSpacing(0f, 1.15f)
    }
}