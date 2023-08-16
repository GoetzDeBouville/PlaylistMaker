package com.example.playlistmaker.data.sharing.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.example.playlistmaker.domain.sharing.model.EmailData

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {
    override fun shareApp(link: String, title: String) {
        Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, link)
        }.also { shareIntent ->
            val chooserIntent = Intent.createChooser(shareIntent, title)
            chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            Log.e("ShareApp", "ShareApp started")
            context.startActivity(chooserIntent)
        }
    }

    override fun openEmail(emailData: EmailData, chooserTitle: String, noEmailAppMessage: String) {
        val intentSend = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, emailData.email)
            putExtra(Intent.EXTRA_SUBJECT, emailData.subject)
            putExtra(Intent.EXTRA_TEXT, emailData.text)
            Log.e("OpenEmail", "OpenEmail started")
        }
        if (intentSend.resolveActivity(context.packageManager) != null) {
            val chooserIntent = Intent.createChooser(intentSend, chooserTitle)
            chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooserIntent)
        } else {
            Toast.makeText(context, noEmailAppMessage, Toast.LENGTH_LONG).show()
        }
    }

    override fun openTerms(termsLink: String) {
        val intentOffer = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(termsLink)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            Log.e("OpenTerms", "OpenTerms started")
        }
        context.startActivity(intentOffer)
    }
}