package com.huzaif.briefly.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.random.Random

class WordCloudCanvas @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
    }

    private var words = listOf<Pair<String, Int>>()

    fun setWords(text: String) {
        val stopWords = setOf("the", "a", "an", "and", "or", "but", "in", "on", "at", "to", "for", "with", "is", "are", "was", "were")
        val wordMap = text.lowercase()
            .split(Regex("[\\s,.]+"))
            .filter { it.length > 3 && it !in stopWords }
            .groupingBy { it }
            .eachCount()
            .toList()
            .sortedByDescending { it.second }
            .take(15)
        
        words = wordMap
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (words.isEmpty()) return

        val maxFreq = words.maxOf { it.second }.toFloat()
        val centerX = width / 2f
        val centerY = height / 2f

        words.forEachIndexed { index, (word, freq) ->
            val size = 30f + (freq / maxFreq) * 50f
            paint.textSize = size
            paint.color = getRandomColor()
            
            // Simple random positioning for visualization
            val offsetX = (Random.nextFloat() - 0.5f) * (width * 0.7f)
            val offsetY = (Random.nextFloat() - 0.5f) * (height * 0.7f)
            
            canvas.drawText(word, centerX + offsetX, centerY + offsetY, paint)
        }
    }

    private fun getRandomColor(): Int {
        val colors = listOf(Color.BLUE, Color.RED, Color.GREEN, Color.MAGENTA, Color.BLACK)
        return colors.random()
    }
}
