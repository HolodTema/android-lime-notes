package com.terabyte.realmnotes.ui.util

import android.content.Context
import android.widget.Toast

fun Context.makeShortToast(str: String) {
    Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
}