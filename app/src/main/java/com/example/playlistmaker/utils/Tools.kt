package com.example.playlistmaker.utils

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.playlistmaker.R
import com.google.android.material.snackbar.Snackbar

object Tools {
    const val CLICK_DEBOUNCE_DELAY_MS = 500L
    const val SEARCH_DEBOUNCE_DELAY_MS = 2000L
    const val PLAYLIST_DATA = "playlist"

    fun showSnackbar(
        view: View,
        message: String,
        context: Context
    ) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        val snackTextColor = ContextCompat.getColor(context, R.color.snack_text)
        val backgroundColor = ContextCompat.getColor(context, R.color.text_color)

        val textView =
            snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        textView.textSize = 16f
        textView.setTextColor(snackTextColor)
        snackbar.view.setBackgroundColor(backgroundColor)
        snackbar.show()
    }

    fun amountTextFormater(amount: Int): String {
        val lastDigit = amount % 10
        val lastTwoDigits = amount % 100

        return when {
            lastTwoDigits in 11..14 -> "$amount треков"
            lastDigit == 1 -> "$amount трек"
            lastDigit in 2..4 -> "$amount трека"
            else -> "$amount треков"
        }
    }

    fun durationTextFormater(duration: Int): String {
        val lastDigit = duration % 10
        val lastTwoDigits = duration % 100

        return when {
            lastTwoDigits in 11..14 -> "$duration минут"
            lastDigit == 1 -> "$duration минута"
            lastDigit in 2..4 -> "$duration минуты"
            else -> "$duration минут"
        }
    }

    fun vibroManager(context: Context, duration: Long) {
        val vibrationEffect =
            VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator.vibrate(vibrationEffect)
        } else {
            @Suppress("DEPRECATION")
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(vibrationEffect)
        }
    }
}