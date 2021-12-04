package com.keremturker.behero.commons

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.keremturker.behero.R

class CustomButton(context: Context, attributeSet: AttributeSet? = null) :
    CardView(context, attributeSet) {

    private var imgImage: ImageView
    private var txtText: TextView
    private var parentLayout: CardView

    init {
        View.inflate(context, R.layout.custom_button, this)
        imgImage = findViewById(R.id.imgImage)
        txtText = findViewById(R.id.txtText)
        parentLayout = findViewById(R.id.parentLayout)
        setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));

        R.styleable.CustomButton.applyAttributes(attributeSet)
    }

    fun onClickLister(click: (() -> Unit)) {
        parentLayout.setOnClickListener { click.invoke() }
    }

    fun setText(text: String) {
        txtText.text = text
    }

    fun setImage(image: Drawable) {
        imgImage.setImageDrawable(image)
    }

    private fun IntArray.applyAttributes(attrs: AttributeSet?) {
        context.theme.obtainStyledAttributes(attrs, this, 0, 0).apply {
            try {

                setLeftImageFromAttr(this)
                setEnabledFromAttr(this)
                setFontFamily(this)
                setTextFromAttr(this)
                setBackGroundFromAttrs(this)
            } finally {
                recycle()
            }
        }
    }

    private fun setBackGroundFromAttrs(attributeSet: TypedArray) {
        val colors = attributeSet.getColorStateList(R.styleable.CustomButton_backgroundColor)
        parentLayout.backgroundTintList = colors
    }

    private fun setFontFamily(attributeSet: TypedArray) {
        val fontFamilyId: Int =
            attributeSet.getResourceId(R.styleable.CustomButton_android_fontFamily, 0)
        if (fontFamilyId > 0) {
            txtText.typeface = ResourcesCompat.getFont(context, fontFamilyId)
        }
    }

    private fun setTextFromAttr(attributeSet: TypedArray) {
        val text =
            attributeSet.getString(R.styleable.CustomButton_text)
        txtText.text = text

    }

    private fun setLeftImageFromAttr(attributeSet: TypedArray) {
        val imageResId = attributeSet.getDrawable(R.styleable.CustomButton_image)
        imgImage.setImageDrawable(imageResId)
    }


    private fun setEnabledFromAttr(attributeSet: TypedArray) {
        val isEnabled =
            attributeSet.getBoolean(R.styleable.CustomButton_android_enabled, true)
        parentLayout.isEnabled = isEnabled
    }

}