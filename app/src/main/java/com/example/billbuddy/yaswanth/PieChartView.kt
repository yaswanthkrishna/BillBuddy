package com.example.billbuddy.yaswanth

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import java.util.Locale

class PieChartView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }
    private val textPaint = Paint().apply {
        isAntiAlias = true
        color = Color.BLACK
        textSize = 30f
        textAlign = Paint.Align.CENTER
    }
    private var data = listOf<Float>()
    private var labels = listOf<String>()
    private var colors = listOf(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN)
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (data.isEmpty()) return

        val total = data.sum()
        var startAngle = -90f
        val rectSize = Math.min(width, height) / 2.5f
        val rectF = RectF(
            width / 2 - rectSize,
            height / 2 - rectSize,
            width / 2 + rectSize,
            height / 2 + rectSize
        )

        data.forEachIndexed { index, value ->
            val angle = 360 * (value / total)
            paint.color = colors.getOrElse(index) { Color.BLACK }
            canvas.drawArc(rectF, startAngle, angle, true, paint)

            // Draw labels
            val labelAngle = Math.toRadians((startAngle + angle / 2).toDouble())
            val labelRadius = rectSize + 40
            val labelX = (width / 2 + Math.cos(labelAngle) * labelRadius).toFloat()
            val labelY = (height / 2 + Math.sin(labelAngle) * labelRadius).toFloat()
            canvas.drawText(labels[index], labelX, labelY, textPaint)

            startAngle += angle
        }
    }
    private fun getTimeRangeDates(timeRange: String): Pair<String, String> {
        val calendar = Calendar.getInstance()
        val endDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)

        when (timeRange) {
            "Day" -> {
                // Start and end dates are the same for 'Day'
                return Pair(endDate, endDate)
            }
            "Week" -> {
                calendar.add(Calendar.DAY_OF_YEAR, -7)
            }
            "Month" -> {
                calendar.add(Calendar.MONTH, -1)
            }
        }

        val startDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
        return Pair(startDate, endDate)
    }
    fun setData(data: List<Float>, labels: List<String>) {
        this.data = data
        this.labels = labels
        invalidate()
    }
}