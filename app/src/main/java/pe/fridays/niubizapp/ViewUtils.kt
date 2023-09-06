package pe.fridays.niubizapp

import android.util.Log
import android.widget.EditText
import org.apache.commons.lang3.StringUtils
import java.lang.NumberFormatException
import java.text.DecimalFormat

fun EditText.toDouble(): Double {
    return try {
        this.text.toString().toDouble()
    } catch (e: NumberFormatException) {
        0.0
    }
}

fun StringBuilder.addLineBreak() {
    this.append("\n")
}

fun StringBuilder.addDoubleLineBreak() {
    this.append("\n${" ".repeat(42)}\n${" ".repeat(42)}\n")
}

fun StringBuilder.appendRowLeft(str: String) {
    this.append(String.format("%1$42s", str))
}

fun StringBuilder.appendRowRight(str: String) {
    this.append(String.format("%1$-42s", str))
}

fun StringBuilder.appendRowCenter(str: String) {
    this.append(StringUtils.center(str, 42, ' '))
}

fun StringBuilder.appendTwoWordsInRow(strStart: String, strEnd: String) {
    val length = strStart.length + strEnd.length
    val diff = 42 - length
    if (diff >= 0) {
        this.append("$strStart${" ".repeat(diff)}$strEnd")
    }
}

fun Double.toAmountString(): String {
    val format = DecimalFormat("#.00")
    Log.e("ASD", format.format(this))
    return format.format(this).replace(".", "").replace(",", "")
}

fun String?.toExtCom() = if (isNullOrEmpty()) "" else "&EXTCOM=$this"