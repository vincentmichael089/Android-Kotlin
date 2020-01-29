package com.example.customfancontroller

import android.content.Context
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View

// TODO: this enum is of type Int because the values are string resources rather than actual strings.
private enum class FanSpeed(val label: Int){
    OFF(R.string.fan_off),
    LOW(R.string.fan_low),
    MEDIUM(R.string.fan_medium),
    HIGH(R.string.fan_high)
}

private const val RADIUS_OFFSET_LABEL = 30
private const val RADIUS_OFFSET_INDICATOR = -35

// TODO: The @JvmOverloads annotation instructs the Kotlin compiler to generate overloads for this
//  function that substitute default parameter values.
class DialView @JvmOverloads constructor(
    context : Context,
    attrs: AttributeSet? = null,
    defStyleAttr : Int = 0
) : View(context, attrs, defStyleAttr){
    // TODO: These values are created and initialized here instead of when the view is actually drawn
    //  to ensure that the actual drawing step runs as fast as possible.
    private var radius = 0.0f // radius of circle
    private var fanSpeed = FanSpeed.OFF // active selection

    // position variable which will be used to draw label and indicator circle position
    private val pointPosition: PointF = PointF(0.0f, 0.0f)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create("",Typeface.BOLD)
    }
}