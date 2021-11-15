package com.keremturker.behero.commons

import android.content.Context
import android.content.res.TypedArray
import android.text.InputFilter
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.keremturker.behero.R

class CustomEdittext(context: Context, attributeSet: AttributeSet? = null) :
    ConstraintLayout(context,attributeSet) {

        private var inputText:EditText
        private var leftImage:ImageView


        init {
            View.inflate(context, R.layout.custom_edittext,this)
            inputText=findViewById(R.id.inputText)
            leftImage=findViewById(R.id.leftImage)

            R.styleable.CustomEdittext.applyAttributes(attributeSet)
        }

    private fun IntArray.applyAttributes(attrs: AttributeSet?) {
        context.theme.obtainStyledAttributes(attrs, this, 0, 0).apply {
            try {
                setImeOptionsFromAttr(this)
                setInputTypeOptionsFromAttr(this)
                setHintOptionsFromAttr(this)
                setMaxLengthFromAttr(this)
                setLeftImageFromAttr(this)
                setSingleLineFromAttr(this)
                setEnabledFromAttr(this)
                setDigitsFromAttr(this)
            } finally {
                recycle()
            }
        }
    }

    private fun setImeOptionsFromAttr(attributeSet: TypedArray) {
        val imeOption = attributeSet.getInt(
            R.styleable.CustomEdittext_android_imeOptions,
            EditorInfo.IME_ACTION_NEXT
        )
        inputText.imeOptions = imeOption
    }

    private fun setHintOptionsFromAttr(attributeSet: TypedArray) {
        val hintOptions = attributeSet.getString(R.styleable.CustomEdittext_android_hint)
        inputText.hint = hintOptions
    }

    private fun setMaxLengthFromAttr(attributeSet: TypedArray) {
        val maxLengthOptions =
            attributeSet.getString(R.styleable.CustomEdittext_android_maxLength)
        inputText.filters = arrayOf<InputFilter>(
            InputFilter.LengthFilter(
                maxLengthOptions?.toInt() ?: 999
            )
        )
    }

    private fun setSingleLineFromAttr(attributeSet: TypedArray) {
        val singleLine =
            attributeSet.getBoolean(R.styleable.CustomEdittext_android_singleLine, true)
        inputText.isSingleLine = singleLine
    }

    private fun setInputTypeOptionsFromAttr(attributeSet: TypedArray) {
        val inputTypeOptions = attributeSet.getInt(
            R.styleable.CustomEdittext_android_inputType,
            0
        )
        if (inputTypeOptions != 0) inputText.inputType = inputTypeOptions

    }

    private fun setLeftImageFromAttr(attributeSet: TypedArray) {
        val imageResId = attributeSet.getDrawable(R.styleable.CustomEdittext_leftImage)
        leftImage.setImageDrawable(imageResId)
    }


    private fun setEnabledFromAttr(attributeSet: TypedArray) {
        val isEnabled =
            attributeSet.getBoolean(R.styleable.CustomEdittext_android_enabled, true)
        inputText.isEnabled = isEnabled
    }

    private fun setDigitsFromAttr(attributeSet: TypedArray) {
        val digits =
            attributeSet.getString(R.styleable.CustomEdittext_android_digits)

        digits?.let {
            inputText.keyListener = DigitsKeyListener.getInstance(it)
        }
    }
}