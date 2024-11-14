package com.safekaro.partner.ui.common

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.content.res.TypedArray
import com.safekaro.partner.R

class GradientStrokeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rectF = RectF() // Preallocate RectF
    private var strokeWidth = 6f // Default stroke width
    private var cornerRadius = 16f // Default corner radius

    // Define gradient colors
    private var gradientColorStart = Color.RED // Default start color
    private var gradientColorEnd = Color.BLUE // Default end color
    private var fillColor = Color.TRANSPARENT // Default fill color

    init {
        // Obtain styled attributes
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.GradientStrokeView)
        strokeWidth = typedArray.getDimension(R.styleable.GradientStrokeView_strokeWidth, strokeWidth)
        cornerRadius = typedArray.getDimension(R.styleable.GradientStrokeView_cornerRadius, cornerRadius)
        gradientColorStart = typedArray.getColor(R.styleable.GradientStrokeView_gradientColorStart, gradientColorStart)
        gradientColorEnd = typedArray.getColor(R.styleable.GradientStrokeView_gradientColorEnd, gradientColorEnd)
        fillColor = typedArray.getColor(R.styleable.GradientStrokeView_fillColor, fillColor)
        typedArray.recycle()

        // Set up the paint for stroke
        strokePaint.style = Paint.Style.STROKE
        strokePaint.strokeWidth = strokeWidth

        // Set up the paint for fill
        fillPaint.style = Paint.Style.FILL
        fillPaint.color = fillColor // Set the fill color
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Set the bounds for the RectF
        rectF.set(
            strokeWidth / 2,
            strokeWidth / 2,
            width - strokeWidth / 2,
            height - strokeWidth / 2
        )

        // Draw the filled rectangle
        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, fillPaint)

        // Draw the gradient stroke
        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, strokePaint)
    }

    // Adjust the gradient when the view size changes
    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)

        // Update the shader with the new size
        updateShader()
    }

    private fun updateShader() {
        if (width > 0 && height > 0) { // Ensure width and height are valid
            strokePaint.shader = LinearGradient(
                0f, 0f, width.toFloat(), height.toFloat(),
                gradientColorStart, gradientColorEnd,
                Shader.TileMode.CLAMP
            )
        }
    }

    // Public methods to get/set the attributes
    fun setStrokeWidth(width: Float) {
        strokeWidth = width
        strokePaint.strokeWidth = strokeWidth
        invalidate() // Request to redraw the view
    }

    fun setCornerRadius(radius: Float) {
        cornerRadius = radius
        invalidate() // Request to redraw the view
    }

    fun setGradientColors(startColor: Int, endColor: Int) {
        gradientColorStart = startColor
        gradientColorEnd = endColor
        updateShader() // Update the gradient shader
        invalidate() // Request to redraw the view
    }

    fun setFillColor(color: Int) {
        fillColor = color
        fillPaint.color = fillColor
        invalidate() // Request to redraw the view
    }

    // Getters for the properties
    fun getStrokeWidth(): Float = strokeWidth
    fun getCornerRadius(): Float = cornerRadius
    fun getGradientColorStart(): Int = gradientColorStart
    fun getGradientColorEnd(): Int = gradientColorEnd
    fun getFillColor(): Int = fillColor
}