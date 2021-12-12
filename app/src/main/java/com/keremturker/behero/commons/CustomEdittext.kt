package com.keremturker.behero.commons

import android.content.Context
import android.content.res.TypedArray
import android.os.Bundle
import android.os.Parcelable
import android.text.InputFilter
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import com.keremturker.behero.R
import com.keremturker.behero.utils.Constants.emptyText
import com.keremturker.behero.utils.extension.restoreChildViewStates
import com.keremturker.behero.utils.extension.saveChildViewStates
import com.keremturker.behero.utils.extension.visibleIf


class CustomEdittext(context: Context, attributeSet: AttributeSet? = null) :
    ConstraintLayout(context, attributeSet) {

    private var inputText: EditText
    private var leftImage: ImageView
    private var underLine: View

    companion object {
        private const val SPARSE_STATE_KEY = "SPARSE_STATE_KEY"
        private const val SUPER_STATE_KEY = "SUPER_STATE_KEY"
    }


    override fun dispatchSaveInstanceState(container: SparseArray<Parcelable>) {
        dispatchFreezeSelfOnly(container)
    }

    override fun dispatchRestoreInstanceState(container: SparseArray<Parcelable>) {
        dispatchThawSelfOnly(container)
    }

    override fun onSaveInstanceState(): Parcelable? {
        Log.i("ByHand", "onSaveInstanceState")
        return Bundle().apply {
            Log.i("ByHand", "Writing children state to sparse array")
            putParcelable(SUPER_STATE_KEY, super.onSaveInstanceState())
            putSparseParcelableArray(SPARSE_STATE_KEY, saveChildViewStates())
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        Log.i("ByHand", "onRestoreInstanceState")
        var newState = state
        if (newState is Bundle) {
            Log.i("ByHand", "Reading children children state from sparse array")
            val childrenState = newState.getSparseParcelableArray<Parcelable>(SPARSE_STATE_KEY)
            childrenState?.let { restoreChildViewStates(it) }
            newState = newState.getParcelable(SUPER_STATE_KEY)
        }
        super.onRestoreInstanceState(newState)
    }


    init {
        View.inflate(context, R.layout.custom_edittext, this)
        inputText = findViewById(R.id.inputText)
        leftImage = findViewById(R.id.leftImage)
        underLine = findViewById(R.id.underLine)

        R.styleable.CustomEdittext.applyAttributes(attributeSet)

        inputText.addTextChangedListener {
            underLine.visibleIf(false)
        }
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
                setMinLineFromAttr(this)
                setMaxLineFromAttr(this)
                setFontFamily(this)
            } finally {
                recycle()
            }
        }
    }

    fun getText() = inputText.text.toString()
    fun setText(text: String) = inputText.setText(text)
    fun clearText() = inputText.setText(emptyText())
    fun showError(isShow: Boolean) = underLine.visibleIf(isShow)


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
            attributeSet.getBoolean(
                R.styleable.CustomEdittext_android_singleLine,
                true
            )
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

    private fun setMinLineFromAttr(attributeSet: TypedArray) {
        val minLine =
            attributeSet.getInt(R.styleable.CustomEdittext_android_minLines, 1)
        inputText.minLines = minLine
    }

    private fun setMaxLineFromAttr(attributeSet: TypedArray) {
        val maxLine =
            attributeSet.getInt(R.styleable.CustomEdittext_android_maxLines, 1)
        inputText.maxLines = maxLine
    }

    private fun setFontFamily(attributeSet: TypedArray) {
        val fontFamilyId: Int =
            attributeSet.getResourceId(R.styleable.CustomEdittext_android_fontFamily, 0)
        if (fontFamilyId > 0) {
            inputText.typeface = ResourcesCompat.getFont(context, fontFamilyId)
        }
    }
}