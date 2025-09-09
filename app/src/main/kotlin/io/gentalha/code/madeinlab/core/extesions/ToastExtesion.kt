package io.gentalha.code.madeinlab.core.extesions

import android.content.Context

fun shortToast(message: String, context: Context) {
    android.widget.Toast.makeText(
        context,
        message,
        android.widget.Toast.LENGTH_SHORT
    ).show()
}