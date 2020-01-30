package com.example.minipaint

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import androidx.core.content.res.ResourcesCompat

private const val STROKE_WIDTH = 12f // has to be float

class MyCanvasView(context: Context) : View(context){

    // bitmap and canvas for caching what has been drawn before.
    private lateinit var extraCanvas : Canvas
    private lateinit var extraBitmap: Bitmap

    private val backgroundColor = ResourcesCompat.getColor(resources, R.color.colorBackground, null)
    private val drawColor = ResourcesCompat.getColor(resources, R.color.colorPaint, null)

    private val paint = Paint().apply{
        color = drawColor
        isAntiAlias = true // Smooths out edges of what is drawn without affecting shape.
        isDither = true // Dithering affects how colors with higher-precision than the device are down-sampled.
        style = Paint.Style.STROKE // default: FILL
        strokeJoin = Paint.Join.ROUND // default: MITER
        strokeCap = Paint.Cap.ROUND // default: BUTT
        strokeWidth = STROKE_WIDTH // default: Hairline-width (really thin)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // a new bitmap and canvas are created every time the function executes. You need a new bitmap,
        // because the size has changed. However, this is a memory leak, leaving the old bitmaps around.
        // To fix this, recycle extraBitmap before creating the next one.
        if (::extraBitmap.isInitialized) extraBitmap.recycle()
        extraBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888) //ARGB_8888 stores each color in 4 bytes and is recommended.
        extraCanvas = Canvas(extraBitmap)
        extraCanvas.drawColor(backgroundColor)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawBitmap(extraBitmap,0f,0f, null)
    }
}