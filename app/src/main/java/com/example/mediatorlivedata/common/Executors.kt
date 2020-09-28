package com.example.mediatorlivedata.common

import androidx.lifecycle.LiveData
import java.util.concurrent.Executors

private val IO_EXECUTOR = Executors.newSingleThreadExecutor()

fun ioThread(func: () -> Unit) {
    IO_EXECUTOR.execute(func)
}