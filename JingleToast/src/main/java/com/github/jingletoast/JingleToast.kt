package com.github.jingletoast

import android.content.Context
import android.graphics.Color
import android.widget.LinearLayout
import com.airbnb.lottie.LottieAnimationView

import android.widget.Toast
import android.view.Gravity
import android.view.LayoutInflater
import android.graphics.drawable.GradientDrawable
import android.view.View
import java.lang.Exception

class JingleToast(context: Context?) : View(context) {
    class makeToast {
        private var context: Context
        private var duration: Int
        private var gravity = 0
        private var style = 0
        private var backgroundcolor: String? = null
        private var message: String
        var background_ln: LinearLayout? = null

        constructor(context: Context, message: String, duration: Int) {
            this.context = context
            this.message = message
            this.duration = duration
        }

        constructor(context: Context, message: String, duration: Int, style: Int) {
            this.context = context
            this.message = message
            this.duration = duration
            this.style = style
        }

        constructor(context: Context, message: String, duration: Int, style: Int, gravity: Int) {
            this.context = context
            this.message = message
            this.duration = duration
            this.style = style
            this.gravity = gravity
        }

        constructor(
            context: Context,
            message: String,
            duration: Int,
            style: Int,
            gravity: Int,
            backgroundcolor: String?
        ) {
            this.context = context
            this.message = message
            this.duration = duration
            this.style = style
            this.backgroundcolor = backgroundcolor
            this.gravity = gravity
        }

        fun setStyle(
            style: Int,
            background: LinearLayout?,
            lottieAnimationView: LottieAnimationView?
        ) {
            when (style) {
                SUCCESS -> {
                    toast_image!!.setAnimation("success_toast.json")
                    customView(background, Color.parseColor("#00C853"))
                }
                WARNING -> {
                    toast_image!!.setAnimation("warning_toast.json")
                    customView(background, Color.parseColor("#FF6D00"))
                }
                FAILURE -> {
                    toast_image!!.setAnimation("failure_toast.json")
                    customView(background, Color.parseColor("#d50000"))
                }
                INFO -> {
                    toast_image!!.setAnimation("info_toast.json")
                    customView(background, Color.parseColor("#0091EA"))
                }
                CUSTUM -> {
                    toast_image!!.setAnimation("info_toast.json")
                    customView(
                        background, Color.parseColor(
                            backgroundcolor
                        )
                    )
                }
                else -> {
                    toast_image!!.setAnimation("info_toast.json")
                    customView(background, Color.parseColor("#0091EA"))
                }
            }
        }

        fun setGravity(gravity: Int, toast: Toast?) {
            when (gravity) {
                1 -> toast!!.setGravity(Gravity.TOP or Gravity.FILL_HORIZONTAL, 0, 20)
                2 -> toast!!.setGravity(Gravity.CENTER or Gravity.FILL_HORIZONTAL, 0, 0)
                3 -> toast!!.setGravity(Gravity.BOTTOM or Gravity.FILL_HORIZONTAL, 0, 20)
                else -> toast!!.setGravity(Gravity.TOP or Gravity.FILL_HORIZONTAL, 0, 0)
            }
        }

        fun show() {
            if (toast != null) {
                try {
                    toast!!.cancel()
                } catch (e: Exception) {
                }
            }
            toast = Toast(context)
            layout = LayoutInflater.from(context).inflate(R.layout.toast_animated, null, false)

            text = layout?.findViewById(R.id.toast_txt)
            toast_image = layout?.findViewById(R.id.toast_image)
            background_ln = layout?.findViewById(R.id.toast_color_layout)
            setStyle(style, background_ln, toast_image)
            setGravity(gravity, toast)
            text?.setCharacterDelay(20)
            text?.animateText(message)
            toast!!.duration = duration
            toast!!.view = layout
            toast!!.show()
        }
    }

    companion object {
        const val LENGTH_SHORT = 0
        const val LENGTH_LONG = 1
        const val SUCCESS = 1
        const val WARNING = 2
        const val FAILURE = 3
        const val INFO = 4
        const val CUSTUM = 5
        var TOP = 1
        var CENTER = 2
        var BOTTOM = 3
        var DEFAULTTOAST = "DEFAULT"
        var SUCCESSTOAST = "SUCCESS"
        var FAILTOAST = "FAIL"
        var WARNINGTOAST = "WARN"
        var INFOTOAST = "INFO"
        var CUSTOMTOAST = "CUSTOM"
        var toast: Toast? = null
        var toast_image: LottieAnimationView? = null
        var layout: View? = null

        var text: TypeWriter? = null


        private fun customView(v: View?, backgroundColor: Int) {
            val shape = GradientDrawable()
            shape.shape = GradientDrawable.RECTANGLE
            shape.cornerRadii = floatArrayOf(20f, 20f, 20f, 20f, 20f, 20f, 20f, 20f)
            shape.setColor(backgroundColor)
            shape.setStroke(3, backgroundColor)
            v!!.background = shape
        }

    }


}