package com.progressive.kherkin.common.testcore

import android.util.Log

/**
 * Simple wrapper for [Logs][Log] to have the same tag.
 */
class IntegrationTestLogger {

    fun info(message: String) {
        Log.i(LOGCAT_TAG, message)
    }

    fun debug(message: String) {
        Log.d(LOGCAT_TAG, message)
    }

    fun verbose(message: String) {
        Log.v(LOGCAT_TAG, message)
    }

    companion object {
        var LOGCAT_TAG = "UI_TESTS"
    }
}

class ComposeTestLogger {

    fun info(message: String) {
        Log.i(LOGCAT_TAG, message)
    }

    companion object {
        const val LOGCAT_TAG = "ComposeStep:"

        /** Opt-in: when true, step logs include raw text values. Off by default so secrets typed in tests stay out of logcat. */
        @JvmStatic
        var logTextValues = false

        /** Returns [text] if [logTextValues] is enabled, otherwise a placeholder with only its length. */
        @JvmStatic
        fun mask(text: String): String = if (logTextValues) text else "<${text.length} chars>"
    }
}