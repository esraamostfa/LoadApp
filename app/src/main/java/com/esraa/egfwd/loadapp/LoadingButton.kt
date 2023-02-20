package com.esraa.egfwd.loadapp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.Style
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.withStyledAttributes

enum class ButtonState(status: String) {
   LOADING("loading"),
    COMPLETED("completed")
}

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0f
    private var heightSize = 0f
    private val buttonHeight = resources.getDimension(R.dimen.button_height)
    private val buttonTop = heightSize - buttonHeight

    private val paint = Paint().apply {
         style = Style.FILL
    }

    private val textPaint = Paint().apply {
        color = ResourcesCompat.getColor(resources,R.color.white, null)
        style = Style.FILL
        textSize = resources.getDimension(R.dimen.default_text_size)
        typeface = Typeface.create("", Typeface.BOLD)
        textAlign = Paint.Align.CENTER
    }

    private lateinit var  loadingOval : RectF
    private var loadingAngle = 0f
    private var progressWidth = 0f

    private var text = resources.getString(R.string.button_download)
    init {

        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            loadingAngle = getFloat(R.styleable.LoadingButton_loadingAngle, 0f)
        }

        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            progressWidth = getFloat(R.styleable.LoadingButton_progressWidth, 0f)
        }

    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        //Draw Button Background
        paint.color = ResourcesCompat.getColor(resources,R.color.purple_200, null)
        canvas?.drawRect(
            0f,
            buttonTop,
            widthSize,
            buttonHeight,
            paint
        )

        //Draw Button Downloading Progress Background
        paint.color = ResourcesCompat.getColor(resources,R.color.teal_700, null)
        canvas?.drawRect(
            0f,
            buttonTop,
            progressWidth*widthSize,
            buttonHeight,
            paint
        )

        //Draw Button Text
        canvas?.drawText(
            text,
            widthSize/2,
            (heightSize/2) - (textPaint.descent() + textPaint.ascent())/2,
            textPaint
        )

        //Draw Downloading Progress Wheal
        paint.color = ResourcesCompat.getColor(resources,R.color.accent, null)
        canvas?.drawArc(loadingOval, 0F, loadingAngle, true, paint)

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
       widthSize = w.toFloat()
        heightSize = h.toFloat()
        loadingOval = RectF(widthSize*0.13f, heightSize*0.2f, widthSize*0.25f, heightSize*0.8f)
    }

    fun setButtonStatus(status: ButtonState) {
        text = if (status== ButtonState.LOADING) {
            resources.getString(R.string.button_loading)
        } else {
            resources.getString(R.string.button_download)
        }
        invalidate()
    }


    fun setLoadingAngle(angle: Float) {
        loadingAngle = angle
        if (loadingAngle==360f) {
            loadingAngle =0f
        }
        invalidate()
    }
    fun setProgressWidth(progress: Float) {
        progressWidth = progress
        if (progressWidth==1f) {
            progressWidth =0f
        }
        invalidate()
    }

}