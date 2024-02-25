package com.example.playlistmaker.utils.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.graphics.drawable.toBitmap
import com.example.playlistmaker.R
import kotlin.math.min

class PlaybackButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleAttr) {
    private val minViewSize = resources.getDimensionPixelSize(R.dimen.circularProgressViewMinSize)

    private var imageBitmap: Bitmap?
    private var playBitmap: Bitmap?
    private var pauseBitmap: Bitmap?
    private var imageRect = RectF(0f, 0f, 0f, 0f)
    private var isPlaying = false

    init {
        context.theme.obtainStyledAttributes(
            attrs, R.styleable.PlaybackControlView, defStyleAttr, defStyleRes
        ).apply {
            try {
                playBitmap = getDrawable(R.styleable.PlaybackControlView_playButtonId)?.toBitmap()
                pauseBitmap = getDrawable(R.styleable.PlaybackControlView_pauseButtonId)?.toBitmap()
                isPlaying = getBoolean(R.styleable.PlaybackControlView_isPlaying, false)
                imageBitmap = if (isPlaying) pauseBitmap else playBitmap
            } finally {
                recycle()
            }
        }
    }

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

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        imageRect = RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        if (imageBitmap != null) {
            canvas.drawBitmap(imageBitmap!!, null, imageRect, null)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> return true
            MotionEvent.ACTION_UP -> {
                updatePlaybackState()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun updatePlaybackState() {
        isPlaying = !isPlaying
        imageBitmap = if (isPlaying) pauseBitmap else playBitmap
        invalidate()
    }
}