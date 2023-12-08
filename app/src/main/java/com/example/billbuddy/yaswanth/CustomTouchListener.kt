import android.content.Context
import android.graphics.Matrix
import android.graphics.PointF
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import kotlin.math.sqrt

class CustomTouchListener(context: Context) : View.OnTouchListener {
    private var mode = NONE
    private val matrix = Matrix()
    private val start = PointF()
    private val mid = PointF()
    private var oldDist = 1f

    private val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent): Boolean {
            // Double tap: Zoom out
            matrix.postScale(0.5f, 0.5f, e.x, e.y)
            return true
        }

        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            // Single tap: Zoom in
            matrix.postScale(2.0f, 2.0f, e.x, e.y)
            return true
        }

        override fun onLongPress(e: MotionEvent) {
            // Long press: Reset image to original size
            matrix.reset()
        }
    })

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val imageView = v as ImageView
        imageView.scaleType = ImageView.ScaleType.MATRIX

        if (gestureDetector.onTouchEvent(event)) {
            imageView.imageMatrix = matrix
            return true
        }

        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                matrix.set(imageView.imageMatrix)
                start.set(event.x, event.y)
                mode = DRAG
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                oldDist = spacing(event)
                if (oldDist > 10f) {
                    matrix.set(imageView.imageMatrix)
                    midPoint(mid, event)
                    mode = ZOOM
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> mode = NONE
            MotionEvent.ACTION_MOVE -> {
                if (mode == DRAG) {
                    val dx = event.x - start.x
                    val dy = event.y - start.y
                    matrix.postTranslate(dx, dy)
                } else if (mode == ZOOM) {
                    val newDist = spacing(event)
                    if (newDist > 10f) {
                        val scale = newDist / oldDist
                        matrix.postScale(scale, scale, mid.x, mid.y)
                    }
                }
            }
        }

        imageView.imageMatrix = matrix
        return true
    }

    private fun spacing(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return sqrt(x * x + y * y)
    }

    private fun midPoint(point: PointF, event: MotionEvent) {
        val x = (event.getX(0) + event.getX(1)) / 2
        val y = (event.getY(0) + event.getY(1)) / 2
        point.set(x, y)
    }

    companion object {
        private const val NONE = 0
        private const val DRAG = 1
        private const val ZOOM = 2
    }
}
