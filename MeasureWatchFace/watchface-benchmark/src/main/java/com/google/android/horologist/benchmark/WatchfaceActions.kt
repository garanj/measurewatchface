package com.google.android.horologist.benchmark

import androidx.benchmark.macro.MacrobenchmarkScope

object WatchfaceActions {
    fun MacrobenchmarkScope.installWatchface(watchfaceApp: WatchfaceApp) {
        device.executeShellCommand(
            "am broadcast " +
                    "-a com.google.android.wearable.app.DEBUG_SURFACE " +
                    "--es operation set-watchface " +
                    "--ecn component ${watchfaceApp.packageName}/${watchfaceApp.playerComponentName.className}"
        )
    }

    fun MacrobenchmarkScope.uninstallWatchface() {
        device.executeShellCommand(
            "am broadcast " +
                    "-a com.google.android.wearable.app.DEBUG_SURFACE " +
                    "--es operation unset-watchface"
        )
    }
}