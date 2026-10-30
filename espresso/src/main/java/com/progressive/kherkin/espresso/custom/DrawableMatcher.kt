package com.progressive.kherkin.espresso.custom

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

/**
 * A custom matcher that checks for an image on a view.
 * It checks the target view's src and background elements for a drawable. This should work for any
 * [ImageView] as well as any view with a drawable set as the background.
 *
 * Drawables are compared pixel by pixel, including color, at their intrinsic size. A drawable
 * without a positive intrinsic size (such as a plain color) cannot be compared and never matches.
 */
class DrawableMatcher internal constructor(private val expectedId: Int) : TypeSafeMatcher<View?>(View::class.java) {
    private var resourceName: String? = null
    private var failureReason: String? = null

    override fun matchesSafely(target: View?): Boolean {
        failureReason = null
        if (target == null) {
            return false
        }
        if (expectedId == EMPTY) {
            return if (target is ImageView) {
                target.drawable == null && target.background == null
            } else {
                target.background == null
            }
        }
        if (expectedId == ANY) {
            return if (target is ImageView) {
                target.drawable != null || target.background != null
            } else {
                target.background != null
            }
        }
        val resources = target.context.resources

        val expectedDrawable = ResourcesCompat.getDrawable(resources, expectedId, null)

        resourceName = resources.getResourceEntryName(expectedId)
        if (expectedDrawable == null) {
            return false
        }
        val otherBitmap = getBitmap(expectedDrawable, "expected drawable") ?: return false

        val actualDrawable = if (target is ImageView && target.drawable != null) {
            target.drawable
        } else {
            target.background
        } ?: return false

        val actualBitmap = getBitmap(actualDrawable, "drawable on view") ?: return false
        return actualBitmap.sameAs(otherBitmap)
    }

    private fun getBitmap(drawable: Drawable, label: String): Bitmap? {
        val width = drawable.intrinsicWidth
        val height = drawable.intrinsicHeight
        if (width <= 0 || height <= 0) {
            failureReason = "$label has no positive intrinsic size (${width}x$height) and cannot be compared"
            return null
        }
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val originalBounds = drawable.copyBounds()
        drawable.setBounds(0, 0, width, height)
        drawable.draw(canvas)
        drawable.bounds = originalBounds
        return bitmap
    }

    override fun describeTo(description: Description) {
        description.appendText("with drawable from resource id: ")
        description.appendValue(expectedId)
        if (resourceName != null) {
            description.appendText("[")
            description.appendText(resourceName)
            description.appendText("]")
        }
        failureReason?.let {
            description.appendText(" (")
            description.appendText(it)
            description.appendText(")")
        }
    }

    companion object {
        const val EMPTY = -1
        const val ANY = -2
    }
}
