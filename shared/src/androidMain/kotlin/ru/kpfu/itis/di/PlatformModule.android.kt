package ru.kpfu.itis.di

import android.os.SystemClock

actual fun elapsedRealtimeProvider(): Long = SystemClock.elapsedRealtime()