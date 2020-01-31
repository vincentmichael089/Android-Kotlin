package com.example.clippingexample

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View

class ClippedView @JvmOverloads constructor(
    context : Context,
    attrs : AttributeSet? = null,
    defStyleAttr : Int = 0
) : View(context, attrs, defStyleAttr){

    private val paint = Paint().apply{
        isAntiAlias = true
        strokeWidth = resources.getDimension(R.dimen.strokeWidth)
        textSize = resources.getDimension(R.dimen.textSize)
    }

    private val path = Path()

    private val clipRectRight = resources.getDimension(R.dimen.clipRectRight)
    private val clipRectLeft = resources.getDimension(R.dimen.clipRectLeft)
    private val clipRectTop = resources.getDimension(R.dimen.clipRectTop)
    private val clipRectBottom = resources.getDimension(R.dimen.clipRectBottom)

    private val rectInset = resources.getDimension(R.dimen.rectInset)
    private val smallRectOffset = resources.getDimension(R.dimen.smallRectOffset)

    private val circleRadius = resources.getDimension(R.dimen.circleRadius)

    private val textOffset = resources.getDimension(R.dimen.textOffset)
    private val textSize = resources.getDimension(R.dimen.textSize)

    // setup column and row location
    private val columnOne = rectInset
    private val columnTwo = columnOne + rectInset + clipRectRight

    private val rowOne = rectInset
    private val rowTwo = rowOne + rectInset + clipRectBottom
    private val rowThree = rowTwo + rectInset + clipRectBottom
    private val rowFour = rowThree + rectInset + clipRectBottom
    private val textRow = rowFour + (1.5f * clipRectBottom)

    private var rectF = RectF(
        rectInset,
        rectInset,
        clipRectRight - rectInset,
        clipRectBottom - rectInset
    )

    private val rejectRow = rowFour + rectInset + 2*clipRectBottom

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBackAndUnclippedRectangle(canvas)
        drawDifferenceClippingExample(canvas)
        drawCircularClippingExample(canvas)
        drawIntersectionClippingExample(canvas)
        drawCombinedClippingExample(canvas)
        drawRoundedRectangleClippingExample(canvas)
        drawOutsideClippingExample(canvas)
        drawSkewedTextExample(canvas)
        drawTranslatedTextExample(canvas)
        drawQuickRejectExample(canvas)
    }

    //
    private fun drawClippedRectangle(canvas: Canvas){
        canvas.clipRect(clipRectLeft, clipRectTop, clipRectRight, clipRectBottom)
        canvas.drawColor(Color.WHITE)

        paint.color = Color.RED
        canvas.drawLine(clipRectTop, clipRectLeft, clipRectRight, clipRectBottom, paint)

        paint.color = Color.GREEN
        canvas.drawCircle(circleRadius,clipRectBottom-circleRadius,circleRadius, paint)

        paint.color = Color.BLUE
        paint.textSize = textSize
        paint.textAlign = Paint.Align.RIGHT
        canvas.drawText(context.getString(R.string.clipping),
            clipRectRight,
            textOffset,
            paint
        )
        //Note: The Paint.Align property specifies which side of the text to align to the origin
        // (not which side of the origin the text goes, or where in the region it is aligned!).
        // Aligning the right side of the text to the origin places it on the left of the origin.
    }

    private fun drawBackAndUnclippedRectangle(canvas: Canvas) {
        canvas.drawColor(Color.GRAY)
        canvas.save() //Saves the current state of the canvas.
        canvas.translate(columnOne,rowOne)
        drawClippedRectangle(canvas)
        canvas.restore() //Restores the previous state of the canvas that was saved.
    }

    private fun drawDifferenceClippingExample(canvas: Canvas) {
        //The code does the following:
        //
        //Save the canvas.
        //Translate the origin of the canvas into open space to the first row, second column, to the right of the first rectangle.
        //Apply two clipping rectangles. The DIFFERENCE operator subtracts the second rectangle from the first one.

        canvas.save()
        // Move the origin to the right for the next rectangle.
        canvas.translate(columnTwo,rowOne)
        // Use the subtraction of two clipping rectangles to create a frame.
        canvas.clipRect(
            2 * rectInset,2 * rectInset,
            clipRectRight - 2 * rectInset,
            clipRectBottom - 2 * rectInset
        )
        // The method clipRect(float, float, float, float, Region.Op
        // .DIFFERENCE) was deprecated in API level 26. The recommended
        // alternative method is clipOutRect(float, float, float, float),
        // which is currently available in API level 26 and higher.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            canvas.clipRect(
                4 * rectInset,4 * rectInset,
                clipRectRight - 4 * rectInset,
                clipRectBottom - 4 * rectInset,
                Region.Op.DIFFERENCE
            )
        else {
            canvas.clipOutRect(
                4 * rectInset,4 * rectInset,
                clipRectRight - 4 * rectInset,
                clipRectBottom - 4 * rectInset
            )
        }
        drawClippedRectangle(canvas)
        canvas.restore()
    }

    private fun drawCircularClippingExample(canvas: Canvas) {

        canvas.save()
        canvas.translate(columnOne, rowTwo)
        // Clears any lines and curves from the path but unlike reset(),
        // keeps the internal data structure for faster reuse.
        path.rewind()
        path.addCircle(
            circleRadius,clipRectBottom - circleRadius,
            circleRadius,Path.Direction.CCW
        )
        // The method clipPath(path, Region.Op.DIFFERENCE) was deprecated in
        // API level 26. The recommended alternative method is
        // clipOutPath(Path), which is currently available in
        // API level 26 and higher.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            canvas.clipPath(path, Region.Op.DIFFERENCE)
        } else {
            canvas.clipOutPath(path)
        }
        drawClippedRectangle(canvas)
        canvas.restore()
    }

    private fun drawIntersectionClippingExample(canvas: Canvas) {
        canvas.save()
        canvas.translate(columnTwo,rowTwo)
        canvas.clipRect(
            clipRectLeft,clipRectTop,
            clipRectRight - smallRectOffset,
            clipRectBottom - smallRectOffset
        )
        // The method clipRect(float, float, float, float, Region.Op
        // .INTERSECT) was deprecated in API level 26. The recommended
        // alternative method is clipRect(float, float, float, float), which
        // is currently available in API level 26 and higher.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            canvas.clipRect(
                clipRectLeft + smallRectOffset,
                clipRectTop + smallRectOffset,
                clipRectRight,clipRectBottom,
                Region.Op.INTERSECT
            )
        } else {
            canvas.clipRect(
                clipRectLeft + smallRectOffset,
                clipRectTop + smallRectOffset,
                clipRectRight,clipRectBottom
            )
        }
        drawClippedRectangle(canvas)
        canvas.restore()
    }

    private fun drawCombinedClippingExample(canvas: Canvas) {
        canvas.save()
        canvas.translate(columnOne, rowThree)
        path.rewind()
        path.addCircle(
            clipRectLeft + rectInset + circleRadius,
            clipRectTop + circleRadius + rectInset,
            circleRadius,Path.Direction.CCW
        )
        path.addRect(
            clipRectRight / 2 - circleRadius,
            clipRectTop + circleRadius + rectInset,
            clipRectRight / 2 + circleRadius,
            clipRectBottom - rectInset,Path.Direction.CCW
        )
        canvas.clipPath(path)
        drawClippedRectangle(canvas)
        canvas.restore()
    }

    private fun drawRoundedRectangleClippingExample(canvas: Canvas) {
        canvas.save()
        canvas.translate(columnTwo,rowThree)
        path.rewind()
        path.addRoundRect(
            rectF,clipRectRight / 4,
            clipRectRight / 4, Path.Direction.CCW
        )
        canvas.clipPath(path)
        drawClippedRectangle(canvas)
        canvas.restore()
    }

    private fun drawOutsideClippingExample(canvas: Canvas) {
        canvas.save()
        canvas.translate(columnOne,rowFour)
        canvas.clipRect(2 * rectInset,2 * rectInset,
            clipRectRight - 2 * rectInset,
            clipRectBottom - 2 * rectInset)
        drawClippedRectangle(canvas)
        canvas.restore()
    }

    private fun drawTranslatedTextExample(canvas: Canvas) {
        canvas.save()
        paint.color = Color.GREEN
        // Align the RIGHT side of the text with the origin.
        paint.textAlign = Paint.Align.LEFT
        // Apply transformation to canvas.
        canvas.translate(columnTwo,textRow)
        // Draw text.
        canvas.drawText(context.getString(R.string.translated),
            clipRectLeft,clipRectTop,paint)
        canvas.restore()
    }

    private fun drawSkewedTextExample(canvas: Canvas) {
        canvas.save()
        paint.color = Color.YELLOW
        paint.textAlign = Paint.Align.RIGHT
        // Position text.
        canvas.translate(columnTwo, textRow)
        // Apply skew transformation.
        canvas.skew(0.2f, 0.3f)
        canvas.drawText(context.getString(R.string.skewed),
            clipRectLeft, clipRectTop, paint)
        canvas.restore()
    }

    private fun drawQuickRejectExample(canvas: Canvas) {
        //The quickReject() method allows you to check whether a specified rectangle or path would lie completely outside the currently visible regions, after all transformations have been applied.
        //
        //The quickReject() method is incredibly useful when you are constructing more complex drawings and need to do so as fast as possible. With quickReject(), you can decide efficiently which objects you do not have to draw at all, and there is no need to write your own intersection logic.
        //
        //The quickReject() method returns true if the rectangle or path would not be visible at all on the screen. For partial overlaps, you still have to do your own checking.
        //The EdgeType is either AA (Antialiased: Treat edges by rounding-out, because they may be antialiased) or BW (Black-White: Treat edges by just rounding to nearest pixel boundary) for just rounding to the nearest pixel.
        val inClipRectangle = RectF(clipRectRight / 2,
            clipRectBottom / 2,
            clipRectRight * 2,
            clipRectBottom * 2)

        val notInClipRectangle = RectF(RectF(clipRectRight+1,
            clipRectBottom+1,
            clipRectRight * 2,
            clipRectBottom * 2))

        canvas.save()
        canvas.translate(columnOne, rejectRow)
        canvas.clipRect(
            clipRectLeft,clipRectTop,
            clipRectRight,clipRectBottom
        )
        if (canvas.quickReject(
                inClipRectangle, Canvas.EdgeType.AA)) {
            canvas.drawColor(Color.WHITE)
        }
        else {
            canvas.drawColor(Color.BLACK)
            canvas.drawRect(inClipRectangle, paint
            )
        }
        canvas.restore()
    }
}