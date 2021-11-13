package com.keremturker.behero.utils.extension

import android.view.View
import android.view.View.*

fun View.setVisible() {
    visibility = VISIBLE
}

fun View.setGone() {
    visibility = GONE
}

fun View.setInvisible() {
    visibility = INVISIBLE
}

fun View.visibleIf(visible: Boolean) {
    visibility = if (visible) VISIBLE else GONE
}

