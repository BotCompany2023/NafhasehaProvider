package com.sa.nafhasehaprovider.common.extentions

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.sa.nafhasehaprovider.app.NafhasehaProviderApp

import es.dmoral.toasty.Toasty
import java.util.*

/**
< Vampire >
 */


fun getContext(): Context? {
    return NafhasehaProviderApp.instance
}


@TargetApi(Build.VERSION_CODES.KITKAT)
fun showSuccesToast(stringId: Int) {

    Toasty.success(
        Objects.requireNonNull<Context>(getContext()),
        getContext()!!.getString(stringId),
        3
    ).show()

}

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
fun showErrorToast(stringId: Int) {


    Toasty.error(
        Objects.requireNonNull<Context>(getContext()),
        getContext()!!.getString(stringId),
        3
    ).show()

}

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
fun showSuccesToast(stringId: String) {

    Toasty.success(Objects.requireNonNull<Context>(getContext()), stringId, 3).show()

}

@RequiresApi(api = Build.VERSION_CODES.KITKAT)

fun showErrorToast(stringId: String) {

    Toasty.error(Objects.requireNonNull<Context>(getContext()), stringId, 3).show()


}



fun TextView.onSetTextMixed(first: String?, Socend: String?) {
    val text =
        "<font color='darkgreen'>" + first + "\t" + "</font>" + "<font color='gray'>" + Socend + "</font>."

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        this.setText(
            Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY),
            TextView.BufferType.SPANNABLE
        )
    } else {
        this.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE)
    }
}
fun TextView.onSetGreenTextMixed(first: String?, Socend: String?) {
    val text =
        "<font color='darkgreen'>" + first + "\t" + "</font>" + "<font color='red'>" + Socend + "</font>."

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        this.setText(
            Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY),
            TextView.BufferType.SPANNABLE
        )
    } else {
        this.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE)
    }
}


