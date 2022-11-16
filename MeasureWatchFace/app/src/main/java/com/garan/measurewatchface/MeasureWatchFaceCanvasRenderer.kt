package com.garan.measurewatchface

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.view.SurfaceHolder
import androidx.wear.watchface.CanvasType
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.DrawMode
import androidx.wear.watchface.RenderParameters
import androidx.wear.watchface.Renderer
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.style.CurrentUserStyleRepository
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class MeasureWatchFaceCanvasRenderer(
    surfaceHolder: SurfaceHolder,
    currentUserStyleRepository: CurrentUserStyleRepository,
    watchState: WatchState,
    val complicationSlotsManager: ComplicationSlotsManager
) : Renderer.CanvasRenderer2<MeasureSharedAssets>(
    surfaceHolder = surfaceHolder,
    currentUserStyleRepository = currentUserStyleRepository,
    watchState = watchState,
    canvasType = CanvasType.HARDWARE,
    interactiveDrawModeUpdateDelayMillis = 16,
    clearWithBackgroundTintBeforeRenderingHighlightLayer = true
) {
    private val interactiveConfig = MeasureWatchFaceRenderConfig(
        textColor = Color.BLUE,
        backgroundColor = Color.RED,
        formatter = DateTimeFormatter.ofPattern("HH:mm:ss"),
        placeholder = "00:00:00"
    )

    private val ambientConfig = MeasureWatchFaceRenderConfig(
        textColor = Color.WHITE,
        backgroundColor = Color.BLACK,
        formatter = DateTimeFormatter.ofPattern("HH:mm"),
        placeholder = "00:00"
    )

    private var currentConfig = interactiveConfig

    init {
        interactiveConfig.updateOffsets(screenBounds)
        ambientConfig.updateOffsets(screenBounds)
    }

    override fun render(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: MeasureSharedAssets
    ) {
        with(currentConfig) {
            canvas.drawColor(backgroundColor)
            canvas.drawText(formatter.format(zonedDateTime), xOffset, yOffset, paint)
        }
    }

    override fun onRenderParametersChanged(renderParameters: RenderParameters) {
        currentConfig = if (renderParameters.drawMode == DrawMode.INTERACTIVE) {
            interactiveConfig
        } else {
            ambientConfig
        }
    }

    override suspend fun createSharedAssets() = MeasureSharedAssets()

    override fun renderHighlightLayer(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: MeasureSharedAssets
    ) {

    }
}
