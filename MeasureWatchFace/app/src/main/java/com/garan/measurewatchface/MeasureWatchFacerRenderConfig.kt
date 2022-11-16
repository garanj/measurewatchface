package com.garan.measurewatchface

import android.graphics.Paint
import android.graphics.Rect
import java.time.format.DateTimeFormatter

data class MeasureWatchFaceRenderConfig(
    val paint: Paint = Paint(),
    val textColor: Int,
    val backgroundColor: Int,
    val formatter: DateTimeFormatter,
    val placeholder: String,
) {
    var xOffset: Float = 0f
    var yOffset: Float = 0f
    var currentBounds: Rect = Rect(0, 0, 0, 0)

    init {
        paint.textSize = 90f
        paint.color = textColor

    }

    fun updateOffsets(screenBounds: Rect) {
        val textBounds = Rect()
        paint.getTextBounds(placeholder, 0, placeholder.length, textBounds)
        xOffset = (screenBounds.width() - textBounds.width()) / 2f
        yOffset = (screenBounds.height() + textBounds.height()) / 2f
        currentBounds = screenBounds
    }
}