package com.keremturker.behero.utils

import android.app.DatePickerDialog
import android.content.Context
import com.keremturker.behero.R
import com.keremturker.behero.utils.extension.getDateSplit
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