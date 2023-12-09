package com.example.billbuddy.yaswanth

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import java.util.Locale

class PieChartView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private val colors = listOf(
        Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA
    )
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textSize = 30f
        textAlign = Paint.Align.CENTER
    }
    private var data = listOf<Float>()
    private var labels = listOf<String>()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        Log.d("PieChartView", "onDraw called with data size: ${data.size}")
        if (data.isEmpty()){
            Log.d("PieChartView", "Data is empty, returning")
            return
        }

        val total = data.sum()
        var startAngle = -90f
        val rectSize = Math.min(width, height) / 2.5f
        val rectF = RectF(width / 2 - rectSize, height / 2 - rectSize, width / 2 + rectSize, height / 2 + rectSize)

        data.forEachIndexed { index, value ->
            val angle = 360 * (value / total)

            // Set the paint color based on the category
            paint.color = colors.getOrElse(index) { Color.BLACK }

            canvas.drawArc(rectF, startAngle, angle, true, paint)

            // Draw labels
            val labelAngle = Math.toRadians((startAngle + angle / 2).toDouble())
            val labelRadius = rectSize + 40
            val labelX = (width / 2 + Math.cos(labelAngle) * labelRadius).toFloat()
            val labelY = (height / 2 + Math.sin(labelAngle) * labelRadius).toFloat()
            canvas.drawText(labels.getOrElse(index) { "" }, labelX, labelY, textPaint)

            startAngle += angle
        }
    }

    fun setData(data: List<Float>, labels: List<String>) {
        Log.d("PieChartView", "setData called with data: $data and labels: $labels")

        if (data.size != labels.size) {
            throw IllegalArgumentException("Data and labels size must match")
        }
        this.data = data
        this.labels = labels
        invalidate()
    }

    fun getTimeRangeDates(timeRange: String): Pair<String, String> {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())

        val endDate = dateFormat.format(calendar.time) + " 23:59:59"  // Include time for the end of the day

        when (timeRange) {
            "Day" -> {
                return Pair(dateFormat.format(calendar.time) + " 00:00:00", endDate) // Start and end of the same day
            }
            "Week" -> {
                calendar.add(Calendar.DAY_OF_YEAR, -7)
            }
            "Month" -> {
                calendar.add(Calendar.MONTH, -1)
            }
            else -> {
                // Handle invalid time range
                throw IllegalArgumentException("Invalid time range")
            }
        }

        val startDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
        return Pair(startDate, endDate)
    }
}
