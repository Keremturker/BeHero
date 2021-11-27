package com.keremturker.behero.utils

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import com.keremturker.behero.R
import com.keremturker.behero.commons.CustomEdittext
import com.keremturker.behero.utils.extension.getDateSplit
import com.keremturker.behero.utils.extension.isValidEmail
import com.keremturker.behero.utils.extension.makeClickableText
import java.util.*

fun Context.showDatePicker(date: String = "", function: (String) -> Unit) {
    val dates = date.getDateSplit()
    val year = dates[2]
    val month = dates[1]
    val dayOfMonth = dates[0]
    val datePickerDialog = DatePickerDialog(
        this, R.style.MyDialogTheme,
        { _, year, month, day ->
            function("$day-${month + 1}-$year")
        }, year, month, dayOfMonth
    )
    datePickerDialog.datePicker.maxDate = Date().time
    datePickerDialog.show()
}

fun String.showAsDialog(
    context: Context,
    function: (() -> Unit)? = null
) {
    val dialog = Dialog(context)
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(R.layout.fragment_custom_dialog)
    dialog.setCanceledOnTouchOutside(true)
    val t: TextView = dialog.findViewById(R.id.txtMessage)
    t.text = this
    val btnAction: Button = dialog.findViewById(R.id.btnAction)

    btnAction.setOnClickListener {
        dialog.dismiss()
        function?.invoke()
    }
    dialog.setCancelable(false)
    dialog.show()
}


fun Context.showMailVerifiedDialog(
    user: FirebaseUser,
    function: (() -> Unit)
) {
    val dialog = Dialog(this)
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(R.layout.custom_mail_verified_dialog)
    dialog.setCanceledOnTouchOutside(true)
    val imgClose: ImageButton = dialog.findViewById(R.id.imgClose)

    imgClose.setOnClickListener {
        dialog.dismiss()
    }

    val txtMessage: TextView = dialog.findViewById(R.id.txtMessage)
    txtMessage.text = getString(R.string.not_verified_mail_text, user.email)
    val txtSendMail: TextView = dialog.findViewById(R.id.txtSendMail)

    txtSendMail.makeClickableText(
        getString(R.string.send_mail_full_text),
        clickableTextArray = arrayOf(getString(R.string.send_mail_clickable_text)),
        functionArray = arrayOf({
            dialog.dismiss()
            function.invoke()
        }),
        multiColorArray = arrayOf(getString(R.string.send_mail_clickable_text))
    )
    dialog.setCancelable(false)
    dialog.show()
}

fun Context.showResetPasswordDialog(
    function: ((String, () -> Unit) -> Unit)
) {
    val dialog = Dialog(this)
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(R.layout.custom_password_change_dialog)
    dialog.setCanceledOnTouchOutside(true)
    val imgClose: ImageButton = dialog.findViewById(R.id.imgClose)

    imgClose.setOnClickListener {
        dialog.dismiss()
    }


    val btnSendMail: Button = dialog.findViewById(R.id.btnSendMail)
    val edtMail: CustomEdittext = dialog.findViewById(R.id.edtMail)

    btnSendMail.setOnClickListener {
        edtMail.apply { showError(!getText().isValidEmail()) }

        if (edtMail.getText().isValidEmail()) {
            function.invoke(edtMail.getText()) { dialog.dismiss() }

        } else {
            Toast.makeText(
                this, getString(R.string.required_filed_text), Toast.LENGTH_SHORT
            ).show()
        }
    }
    dialog.setCancelable(false)
    dialog.show()
}