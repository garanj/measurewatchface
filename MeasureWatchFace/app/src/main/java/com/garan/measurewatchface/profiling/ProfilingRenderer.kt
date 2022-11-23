package com.garan.measurewatchface.profiling

import android.graphics.Canvas
import android.graphics.Rect
import android.view.SurfaceHolder
import androidx.annotation.IntRange
import androidx.tracing.Trace
import androidx.tracing.trace
import androidx.wear.watchface.CanvasType
import androidx.wear.watchface.DrawMode
import androidx.wear.watchface.RenderParameters
import androidx.wear.watchface.Renderer
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.style.CurrentUserStyleRepository
import java.time.ZonedDateTime

abstract class ProfilingRenderer<T : Renderer.SharedAssets>(
    surfaceHolder: SurfaceHolder,
    currentUserStyleRepository: CurrentUserStyleRepository,
    watchState: WatchState,
    @CanvasType private val canvasType: Int,
    @IntRange(from = 0, to = 60000)
    interactiveDrawModeUpdateDelayMillis: Long,
    clearWithBackgroundTintBeforeRenderingHighlightLayer: Boolean
) : Renderer.CanvasRenderer2<T>(
    surfaceHolder,
    currentUserStyleRepository,
    watchState,
    canvasType,
    interactiveDrawModeUpdateDelayMillis,
    clearWithBackgroundTintBeforeRenderingHighlightLayer
) {
    var lastDrawMode: DrawMode = DrawMode.INTERACTIVE

    init {
        Trace.beginAsyncSection("DrawMode-$lastDrawMode", 0)
    }

    final override fun render(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: T
    ) {
        trace("render") {
            renderWatchface(canvas, bounds, zonedDateTime, sharedAssets)
        }
    }

    abstract fun renderWatchface(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: T
    )

    final override fun onRenderParametersChanged(renderParameters: RenderParameters) {
        trace("onRenderParametersChanged") {
            if (renderParameters.drawMode != lastDrawMode) {
                Trace.endAsyncSection("DrawMode-$lastDrawMode", 0)

                lastDrawMode = renderParameters.drawMode
                Trace.beginAsyncSection("DrawMode-$lastDrawMode", 0)
            }

            onWatchfaceRenderParametersChanged(renderParameters)
        }
    }

    open fun onWatchfaceRenderParametersChanged(renderParameters: RenderParameters) {
    }

    override fun onDestroy() {
        Trace.endAsyncSection("DrawMode-$lastDrawMode", 0)
    }

    final override fun renderHighlightLayer(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: T
    ) {
        trace("renderHighlightLayer") {
            renderWatchfaceHighlightLayer(canvas, bounds, zonedDateTime, sharedAssets)
        }
    }

    open fun renderWatchfaceHighlightLayer(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: T
    ) {
    }
}