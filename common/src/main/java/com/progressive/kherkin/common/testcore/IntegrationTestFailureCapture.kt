package com.progressive.kherkin.common.testcore

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import org.junit.runner.Description
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * When a test fails, this class takes a screenshot and reports the directory of the file. It also
 * can write to a file to record that a test has been retried.
 *
 * Files are written to the app under test's private internal storage
 * (`files/screenshots/<test class>/`) so they are not readable by other apps and are removed when
 * the app is uninstalled. Retrieve them from a debuggable build with
 * `adb shell run-as <applicationId> ls files/screenshots`.
 */
class IntegrationTestFailureCapture {
    fun screenshotFailedTest(description: Description) {
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val filename = description.methodName + ".png"
        device.takeScreenshot(File(getPath(description), filename))
    }

    companion object {
        fun logRetriedTest(description: Description) {
            try {
                val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())
                val currentTime = format.format(Date())
                var data = currentTime + ": Retrying test - " + description.methodName
                val file = File(getPath(description), "retryTest.txt")
                if (!file.exists()) {
                    file.createNewFile()
                } else {
                    data = "\n" + data
                }
                FileWriter(file, true).use { writer ->
                    writer.append(data)
                    writer.flush()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        private fun getPath(description: Description): File {
            val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
            val filePath = File(File(targetContext.filesDir, "screenshots"), description.className)
            if (!filePath.exists()) {
                filePath.mkdirs()
            }
            return filePath
        }
    }
}