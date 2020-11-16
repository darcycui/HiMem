package com.mem

object MemTest {
    external fun stringFromJNI(): String

    external fun mmapSmall()

    external fun signalTest()

    fun initNative() {

    }

    // Used to load the 'native-lib' library on application startup.
    init {
        System.loadLibrary("native-lib")
    }
}