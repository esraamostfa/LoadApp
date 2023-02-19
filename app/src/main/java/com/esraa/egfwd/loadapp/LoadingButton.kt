package com.esraa.egfwd.loadapp

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.Style
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0f
    private var heightSize = 0f
    private val buttonHeight = resources.getDimension(R.dimen.button_height)
    private val buttonTop = heightSize - buttonHeight


    private val valueAnimator = ValueAnimator()

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

    }

    private val paint = Paint().apply {
        color = ResourcesCompat.getColor(resources,R.color.purple_200, null)
        style = Style.FILL
    }

    private val textPaint = Paint().apply {
        color = ResourcesCompat.getColor(resources,R.color.white, null)
        style = Style.FILL
        textSize = resources.getDimension(R.dimen.default_text_size)
        typeface = Typeface.create("", Typeface.BOLD)
        textAlign = Paint.Align.CENTER
    }

    private lateinit var text: String
    init {

        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            text = getString(R.styleable.LoadingButton_buttonText)?: resources.getString(R.string.button_name)
        }

    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawRect(
            0f,
            buttonTop,
            widthSize,
            buttonHeight,
            paint
        )

        canvas?.drawText(
            text,
            widthSize/2,
            (heightSize/2) - (textPaint.descent() + textPaint.ascent())/2,
            textPaint
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
       widthSize = w.toFloat()
        heightSize = h.toFloat()
    }

}