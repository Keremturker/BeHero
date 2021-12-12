package com.keremturker.behero.utils.extension

import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Patterns
import android.view.View
import android.widget.TextView
import com.keremturker.behero.R
import com.keremturker.behero.model.Address
import java.util.*

fun TextView.makeClickableText(
    fullText: String,
    clickableTextArray: Array<String> = arrayOf(),
    functionArray: Array<() -> Unit> = arrayOf(),
    boldTextArray: Array<String> = arrayOf(),
    multiColorArray: Array<String> = arrayOf(),
    underLine: Boolean = false
) {
    try {
        val spannableString = SpannableString(fullText)
        clickableTextArray.forEachIndexed { index, it ->
            spannableString.withClickableSpan(it, functionArray[index], underLine)
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
            ds.bgColor = Color.parseColor("#F5F5F5")
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

fun String.getDateSplit(splitChar: String = "-"): ArrayList<Int> {

    val list = arrayListOf<Int>()
    try {
        val dateList = this.split(splitChar)
        list.add(dateList[0].toInt())
        list.add(dateList[1].toInt() - 1)
        list.add(dateList[2].toInt())
    } catch (e: java.lang.Exception) {
        list.clear()
        val calendar: Calendar? = Calendar.getInstance()
        val year = calendar!!.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        list.add(dayOfMonth)
        list.add(month)
        list.add(year)
    }
    return list
}

fun String?.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.getBloodImage(): Int {
    return when (this) {
        "AB+" -> R.drawable.ic_ab_positive
        "AB-" -> R.drawable.ic_ab_negative
        "B+" -> R.drawable.ic_b_positive
        "B-" -> R.drawable.ic_b_negative
        "A-" -> R.drawable.ic_a_negative
        "A+" -> R.drawable.ic_a_positive
        "0-", "O-" -> R.drawable.ic_zero_negative
        else -> R.drawable.ic_zero_positive
    }
}

fun Address?.getAddress(): String {
    return if (!this?.countryName.isNullOrEmpty() && !this?.cityName.isNullOrEmpty()) {
        "${this?.cityName},${this?.countryName}"
    } else if (this?.cityName.isNullOrEmpty()) {
        "${this?.countryName}"
    } else if (this?.countryName.isNullOrEmpty()) {
        "${this?.cityName}"
    } else {
        ""
    }
}

