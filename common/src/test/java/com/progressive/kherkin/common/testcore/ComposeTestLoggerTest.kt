package com.progressive.kherkin.common.testcore

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test

class ComposeTestLoggerTest {

    @After
    fun tearDown() {
        ComposeTestLogger.logTextValues = false
    }

    @Test
    fun maskHidesTextByDefault() {
        assertEquals("<9 chars>", ComposeTestLogger.mask("SECRET123"))
    }

    @Test
    fun maskReturnsTextWhenOptedIn() {
        ComposeTestLogger.logTextValues = true
        assertEquals("SECRET123", ComposeTestLogger.mask("SECRET123"))
    }
}
