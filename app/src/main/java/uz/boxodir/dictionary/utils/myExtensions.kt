package uz.boxodir.dictionary.utils

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.example.dictionary.R


fun String.spannable(query:String, context: Context):SpannableString{
    val span=SpannableString(this)


    val color=ForegroundColorSpan(ContextCompat.getColor(context, R.color.red))
    val startIndex=this.indexOf(query,0,true)
    if (startIndex>-1){
        span.setSpan(color,startIndex,startIndex+query.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    return span
}
fun View.hideKeyboard() {
    val context = this.context ?: return
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}