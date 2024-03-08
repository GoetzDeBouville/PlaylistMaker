package com.example.playlistmaker.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.playlistmaker.utils.Tools.isConnected

class NetworkStatusReciever : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == ACTION) {
            val isConnected = intent.getBooleanExtra("state", false)
            Log.i("MyLog", "CONNECTIVITY_CHANGE = $isConnected")
            if (!isConnected && context?.isConnected() == false) {
                Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Connected to internet", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        const val ACTION = "android.net.conn.CONNECTIVITY_CHANGE"
    }
}