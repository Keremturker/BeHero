package com.keremturker.behero.utils.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Parcelable
import android.util.SparseArray
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.keremturker.behero.R

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

fun ImageView.setImage(bitmap: Bitmap) {
    val option = RequestOptions()
        .error(R.drawable.ic_launcher_background)

    Glide.with(context)
        .setDefaultRequestOptions(option)
        .load(bitmap)
        .into(this)
}

fun Context.getBitmapFromVectorDrawable(drawableId: Int): Bitmap? {
    return try {
        val drawable = ContextCompat.getDrawable(this, drawableId)
        val bitmap = Bitmap.createBitmap(
            drawable!!.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        bitmap
    } catch (e: Exception) {
        null
    }
}

