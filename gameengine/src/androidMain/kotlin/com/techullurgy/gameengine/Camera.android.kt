package com.techullurgy.gameengine

@Volatile
private var instance: Camera? = null

actual fun Camera.Companion.getInstance(): Camera = instance!!

actual fun Camera.Companion.getInstance(
    width: Int,
    height: Int
): Camera {
    if (instance == null) {
        synchronized(this) {
            if (instance == null) {
                instance = Camera(width, height)
            }
        }
    }

    return instance!!
}