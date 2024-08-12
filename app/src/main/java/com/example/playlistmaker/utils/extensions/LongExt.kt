package com.example.playlistmaker.utils.extensions

import java.text.SimpleDateFormat
import java.util.Locale

fun Long.toMinutes() = SimpleDateFormat("mm:ss", Locale.getDefault()).format(this)