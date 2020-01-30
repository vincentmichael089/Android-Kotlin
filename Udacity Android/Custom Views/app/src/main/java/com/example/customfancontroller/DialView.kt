package com.example.customfancontroller

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import java.lang.Float.min
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

// TODO: this enum is of type Int because the values are string resources rather than actual strings.
private enum class FanSpeed(val label: Int){
    OFF(R.string.fan_off),
    LOW(R.string.fan_low),
    MEDIUM(R.string.fan_medium),
    HIGH(R.string.fan_high);

    fun next() =
        when(this){
            OFF->LOW
            LOW->MEDIUM
            MEDIUM->HIGH
            HIGH->OFF
        }

}

private const val RADIUS_OFFSET_LABEL = 30
private const val RADIUS_OFFSET_INDICATOR = -35

// Variables to cache the attribute values.
private var fanSpeedLowColor = 0
private var fanSpeedMediumColor = 0
private var fanSeedMaxColor = 0

// TODO: The @JvmOverloads annotation instructs the Kotlin compiler to generate overloads for this
//  function that substitute default parameter values.
class DialView @JvmOverloads constructor(
    context : Context,
    attrs: AttributeSet? = null,
    defStyleAttr : Int = 0
) : View(context, attrs, defStyleAttr){
    // TODO: These values are created and initialized here instead of when the view is actually drawn
    //  to ensure that the actual drawing step runs as fast as possible.
    private var radius = 0.0f // radius of circle, 0 when initialised
    private var fanSpeed = FanSpeed.OFF // active selection, 0 when initialised

    // position variable which will be used to draw label and indicator circle position
    // , 0  when initialised
    private val pointPosition: PointF = PointF(0.0f, 0.0f)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create("",Typeface.BOLD)
    }

    init {
        isClickable = true

        //  the following code using the withStyledAttributes extension function.
        // You supply the attributes and view, and and set your local variables.
        context.withStyledAttributes(attrs, R.styleable.DialView) {
            fanSpeedLowColor = getColor(R.styleable.DialView_fanColor1, 0)
            fanSpeedMediumColor = getColor(R.styleable.DialView_fanColor2, 0)
            fanSeedMaxColor = getColor(R.styleable.DialView_fanColor3, 0)
        }
    }

    override fun performClick(): Boolean {
        if (super.performClick()) return true

        fanSpeed = fanSpeed.next()
        contentDescription = resources.getString(fanSpeed.label)
        invalidate()
        return true
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        radius = (min(width, height) / 2.0 * 0.8).toFloat()
    }

    private fun PointF.computeXYForSpeed(pos: FanSpeed, radius: Float) {
        // Angles are in radians.
        val startAngle = Math.PI * (9 / 8.0)
        val angle = startAngle + pos.ordinal * (Math.PI / 4)
        x = (radius * cos(angle)).toFloat() + width / 2
        y = (radius * sin(angle)).toFloat() + height / 2
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        // Draw the dial.
        paint.color = when (fanSpeed) {
            FanSpeed.OFF -> Color.GRAY
            FanSpeed.LOW -> fanSpeedLowColor
            FanSpeed.MEDIUM -> fanSpeedMediumColor
            FanSpeed.HIGH -> fanSeedMaxColor
        } 
        canvas?.drawCircle((width/2).toFloat(),(height/2).toFloat(),radius,paint)

        // Draw the indicator circle.
        val markerRadius = radius + RADIUS_OFFSET_INDICATOR
        pointPosition.computeXYForSpeed(fanSpeed, markerRadius)
        paint.color = Color.BLACK
        canvas?.drawCircle(pointPosition.x, pointPosition.y, radius/12, paint)

        // Draw the fan speed labels
        val labelRadius = radius + RADIUS_OFFSET_LABEL
        for(i in FanSpeed.values()){
            pointPosition.computeXYForSpeed(i, labelRadius)
            val label =  resources.getString(i.label)
            canvas?.drawText(label,pointPosition.x, pointPosition.y, paint)
        }
    }
}