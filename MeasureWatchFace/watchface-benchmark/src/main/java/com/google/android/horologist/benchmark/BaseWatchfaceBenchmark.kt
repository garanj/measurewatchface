/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.horologist.benchmark

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.Metric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.filters.LargeTest
import com.google.android.horologist.benchmark.WatchfaceActions.ambientMode
import com.google.android.horologist.benchmark.WatchfaceActions.disableChargingScreen
import com.google.android.horologist.benchmark.WatchfaceActions.installWatchface
import com.google.android.horologist.benchmark.WatchfaceActions.interactiveMode
import com.google.android.horologist.benchmark.WatchfaceActions.uninstallWatchface
import org.junit.Rule
import org.junit.Test

@LargeTest
abstract class BaseWatchfaceBenchmark {

    @get:Rule
    val benchmarkRule: MacrobenchmarkRule = MacrobenchmarkRule()

    abstract val watchface: WatchfaceApp

    @Test
    fun startup(): Unit = benchmarkRule.measureRepeated(
        packageName = watchface.packageName,
        metrics = metrics(),
        // Can't use Partial
        // java.lang.RuntimeException: The baseline profile install broadcast was not
        //         received. This most likely means that the profileinstaller library
        //         is missing from the target apk.
        compilationMode = CompilationMode.Full(),
        iterations = 3,
        startupMode = StartupMode.WARM,
        setupBlock = {
        }
    ) {
        onStartup()

        installWatchface(watchface)

        Thread.sleep(2_000)

        ambientMode()

        Thread.sleep(2_000)

        interactiveMode()

        uninstallWatchface()
    }

    open fun metrics(): List<Metric> = listOf(FrameTimingMetric())

    open fun MacrobenchmarkScope.onStartup() {
//        disableChargingScreen()
        interactiveMode()
    }
}
