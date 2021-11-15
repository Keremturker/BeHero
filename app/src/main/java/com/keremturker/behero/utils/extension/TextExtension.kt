package com.keremturker.behero.utils.extension

import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView

fun TextView.makeClickableText(
    fullText: String,
    clickableTextArray: Array<String> = arrayOf(),
    functionArray: Array<() -> Unit> = arrayOf(),
    boldTextArray: Array<String> = arrayOf(),
    multiColorArray:Array<String> = arrayOf(),
    underLine: Boolean = false
) {
    try {
        val spannableString = SpannableString(fullText)
        clickableTextArray.forEachIndexed { index, it ->
            spannableString.withClickableSpan(it, functionArray[index],underLine)
        }
        boldTextArray.let {
            boldTextArray.forEachIndexed { index, it ->
                spannableString.setBoldText(it)
            }
        }
        multiColorArray.let {
            multiColorArray.forEachIndexed { index, it ->
                spannableString.setColorChange(it)
            }
        }
        text = spannableString
        movementMethod = LinkMovementMethod.getInstance()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun SpannableString.withClickableSpan(
    clickablePart: String,
    onClickListener: () -> Unit,
    underLine: Boolean
): SpannableString {
    val clickableSpan = object : ClickableSpan() {
        override fun onClick(p0: View) {
            onClickListener()
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.bgColor=Color.parseColor("#FFFFFF")
            ds.isUnderlineText = underLine
        }
    }
    val clickablePartStart = indexOf(clickablePart)
    setSpan(
        clickableSpan,
        clickablePartStart,
        clickablePartStart + clickablePart.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return this
}

fun SpannableString.setBoldText(
    boldPart: String
): SpannableString {
    val clickableSpan = object : ClickableSpan() {

        override fun updateDrawState(ds: TextPaint) {
            ds.typeface = Typeface.DEFAULT_BOLD
        }

        override fun onClick(p0: View) {
        }
    }
    val clickablePartStart = indexOf(boldPart)
    setSpan(
        clickableSpan,
        clickablePartStart,
        clickablePartStart + boldPart.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return this
}

fun SpannableString.setColorChange(
    colorPart: String
): SpannableString {

    val clickablePartStart = indexOf(colorPart)
    setSpan(
        ForegroundColorSpan(Color.parseColor("#FF2156")),
        clickablePartStart,
        clickablePartStart + colorPart.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return this
}
