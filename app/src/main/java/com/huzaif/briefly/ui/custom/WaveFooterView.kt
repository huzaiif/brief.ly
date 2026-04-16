package com.huzaif.briefly.ui.custom

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.content.ContextCompat
import com.huzaif.briefly.R

class WaveFooterView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val mainPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val secondPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val thirdPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    
    private val mainPath = Path()
    private val secondPath = Path()
    private val thirdPath = Path()
    
    private val gradientMatrix = Matrix()
    private var animValue = 0f
    
    private val startColor = ContextCompat.getColor(context, R.color.grad_primary_start)
    private val endColor = ContextCompat.getColor(context, R.color.grad_primary_end)
    private val accentColor = ContextCompat.getColor(context, R.color.grad_secondary_start)

    private val animator = ValueAnimator.ofFloat(0f, 1f).apply {
        duration = 5000 
        repeatCount = ValueAnimator.INFINITE
        repeatMode = ValueAnimator.REVERSE
        interpolator = AccelerateDecelerateInterpolator()
        addUpdateListener {
            animValue = it.animatedValue as Float
            invalidate()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        animator.start()
    }

    override fun onDetachedFromWindow() {
        animator.cancel()
        super.onDetachedFromWindow()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val w = width.toFloat()
        val h = height.toFloat()

        if (w == 0f || h == 0f) return


        val colors = intArrayOf(startColor, endColor, accentColor, startColor)
        val positions = floatArrayOf(0f, 0.4f, 0.8f, 1f)
        val shader = LinearGradient(
            0f, 0f, w * 2.0f, h,
            colors, positions,
            Shader.TileMode.MIRROR
        )
        
        gradientMatrix.setTranslate(-animValue * w * 0.7f, 0f)
        shader.setLocalMatrix(gradientMatrix)
        mainPaint.shader = shader


        secondPaint.color = endColor
        secondPaint.alpha = 45
        thirdPaint.color = startColor
        thirdPaint.alpha = 30

        val moveRange = 30f 
        val offset = animValue * moveRange


        thirdPath.reset()
        thirdPath.moveTo(0f, h)
        thirdPath.lineTo(w, h)
        // Parallel slant: Left higher, Right lower (measured from top 0)
        val tRightHeight = h * 0.28f + (offset * 0.4f)
        val tLeftHeight = h * 0.08f - (offset * 0.4f)
        thirdPath.lineTo(w, tRightHeight)
        thirdPath.cubicTo(w * 0.7f, tRightHeight - 55f, w * 0.3f, tLeftHeight + 55f, 0f, tLeftHeight)
        thirdPath.close()
        canvas.drawPath(thirdPath, thirdPaint)


        secondPath.reset()
        secondPath.moveTo(0f, h)
        secondPath.lineTo(w, h)
        val sRightHeight = h * 0.24f - (offset * 0.5f)
        val sLeftHeight = h * 0.04f + (offset * 0.5f)
        secondPath.lineTo(w, sRightHeight)
        secondPath.cubicTo(w * 0.75f, sRightHeight - 80f, w * 0.25f, sLeftHeight + 80f, 0f, sLeftHeight)
        secondPath.close()
        canvas.drawPath(secondPath, secondPaint)


        mainPath.reset()
        mainPath.moveTo(0f, h)
        mainPath.lineTo(w, h)
        

        val mRightHeight = h * 0.30f + offset
        val mLeftHeight = h * 0.05f - offset 

        mainPath.lineTo(w, mRightHeight)
        mainPath.cubicTo(
            w * 0.65f, mRightHeight - 100f, 
            w * 0.35f, mLeftHeight + 100f,  
            0f, mLeftHeight
        )
        mainPath.close()

        canvas.drawPath(mainPath, mainPaint)
    }
}
