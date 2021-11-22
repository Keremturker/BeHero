package com.keremturker.behero.utils.extension

import android.os.Parcelable
import android.util.SparseArray
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.core.view.children

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

fun ViewGroup.saveChildViewStates(): SparseArray<Parcelable> {
    val childViewStates = SparseArray<Parcelable>()
    children.forEach { child -> child.saveHierarchyState(childViewStates) }
    return childViewStates
}

fun ViewGroup.restoreChildViewStates(childViewStates: SparseArray<Parcelable>) {
    children.forEach { child -> child.restoreHierarchyState(childViewStates) }
}

