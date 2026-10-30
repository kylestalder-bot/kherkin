package com.progressive.kherkin.espresso.custom

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.ImageView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.progressive.kherkin.espresso.test.R
import org.hamcrest.StringDescription
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DrawableMatcherTest {

    private val context: Context = InstrumentationRegistry.getInstrumentation().context

    private fun <T> onMain(block: () -> T): T {
        var result: T? = null
        InstrumentationRegistry.getInstrumentation().runOnMainSync { result = block() }
        @Suppress("UNCHECKED_CAST")
        return result as T
    }

    private fun imageView(src: Int? = null): ImageView = onMain {
        ImageView(context).apply {
            background = null
            src?.let { setImageResource(it) }
        }
    }

    private fun plainView(background: Int? = null): View = onMain {
        View(context).apply {
            background?.let { setBackgroundResource(it) }
        }
    }

    private fun matches(expectedId: Int, view: View): Boolean = onMain { DrawableMatcher(expectedId).matches(view) }

    @Test
    fun matchesSameDrawable() {
        assertTrue(matches(R.drawable.test_square_red, imageView(R.drawable.test_square_red)))
        assertTrue(matches(R.drawable.test_square_red, plainView(R.drawable.test_square_red)))
    }

    @Test
    fun doesNotMatchSameShapeWithDifferentColor() {
        assertFalse(matches(R.drawable.test_square_blue, imageView(R.drawable.test_square_red)))
        assertFalse(matches(R.drawable.test_square_blue, plainView(R.drawable.test_square_red)))
    }

    @Test
    fun emptyRequiresNoSourceAndNoBackground() {
        assertFalse(matches(DrawableMatcher.EMPTY, imageView(R.drawable.test_square_red)))
        assertTrue(matches(DrawableMatcher.EMPTY, imageView()))
        assertFalse(matches(DrawableMatcher.EMPTY, plainView(R.drawable.test_square_red)))
        assertTrue(matches(DrawableMatcher.EMPTY, plainView()))
    }

    @Test
    fun anyRequiresADrawable() {
        assertTrue(matches(DrawableMatcher.ANY, imageView(R.drawable.test_square_red)))
        assertFalse(matches(DrawableMatcher.ANY, imageView()))
        assertTrue(matches(DrawableMatcher.ANY, plainView(R.drawable.test_square_red)))
        assertFalse(matches(DrawableMatcher.ANY, plainView()))
    }

    @Test
    fun drawableWithoutIntrinsicSizeDoesNotMatchAndExplainsWhy() {
        val view = onMain { View(context).apply { background = ColorDrawable(Color.RED) } }
        val matcher = DrawableMatcher(R.drawable.test_square_red)

        assertFalse(onMain { matcher.matches(view) })
        val description = StringDescription().also { matcher.describeTo(it) }.toString()
        assertTrue(description, description.contains("no positive intrinsic size"))
    }
}
