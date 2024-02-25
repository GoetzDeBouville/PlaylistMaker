package com.example.playlistmaker.utils.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import com.example.playlistmaker.R
import kotlin.math.min

class PlaybackControlView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleAttr) {
    private val minViewSize = resources.getDimensionPixelSize(R.dimen.circularProgressViewMinSize)
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val contentWith = when (MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.UNSPECIFIED -> minViewSize
            MeasureSpec.AT_MOST -> widthSize
            MeasureSpec.EXACTLY -> widthSize
            else -> error("Unexpected size widthMode")
        }

        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val contentHeight = when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.UNSPECIFIED -> minViewSize
            MeasureSpec.AT_MOST -> heightSize
            MeasureSpec.EXACTLY -> heightSize
            else -> error("Unexpected size heightMode")
        }

        val size = min(contentWith, contentHeight)
        setMeasuredDimension(size, size)
    }
}