package zhupff.gadget.basic.activity

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import zhupff.gadget.basic.theme.THEME_CONFIG
import zhupff.gadgets.basic.OnConfigurationChangedDispatcher
import zhupff.gadgets.basic.OnConfigurationChangedListener
import zhupff.gadgets.logger.logV
import zhupff.gadgets.theme.ThemeFactory
import java.util.concurrent.CopyOnWriteArraySet
import java.util.concurrent.atomic.AtomicInteger

abstract class GadgetActivity : AppCompatActivity(), OnConfigurationChangedDispatcher {
    companion object {
        private val ID = AtomicInteger(0)
    }

    val ID_TAG: String = "${this::class.java.simpleName}(${hashCode()})[${ID.getAndIncrement()}]"

    protected val onConfigurationChangedListeners = CopyOnWriteArraySet<OnConfigurationChangedListener>()

    protected lateinit var windowInsetsControllerCompat: WindowInsetsControllerCompat
        private set

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        ID_TAG.logV("onAttachedToWindow()")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        ID_TAG.logV("onRestoreInstanceState($savedInstanceState)")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableThemeInflate()
        super.onCreate(savedInstanceState)
        ID_TAG.logV("onCreate($savedInstanceState)")
        windowInsetsControllerCompat = WindowInsetsControllerCompat(window, window.decorView)
        windowInsetsControllerCompat.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    override fun onRestart() {
        super.onRestart()
        ID_TAG.logV("onRestart()")
    }

    override fun onStart() {
        super.onStart()
        ID_TAG.logV("onStart()")
    }

    override fun onResume() {
        super.onResume()
        ID_TAG.logV("onResume()")
    }

    override fun onTopResumedActivityChanged(isTopResumedActivity: Boolean) {
        super.onTopResumedActivityChanged(isTopResumedActivity)
        ID_TAG.logV("onTopResumedActivityChanged($isTopResumedActivity)")
    }

    override fun onPause() {
        super.onPause()
        ID_TAG.logV("onPause()")
    }

    override fun onStop() {
        super.onStop()
        ID_TAG.logV("onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        ID_TAG.logV("onDestroy()")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        ID_TAG.logV("onSaveInstanceState(${outState})")
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        ID_TAG.logV("onDetachedFromWindow()")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        ID_TAG.logV("onConfigurationChanged($newConfig)")
        onConfigurationChangedListeners.forEach { it.onConfigurationChanged(newConfig) }
    }

    override fun addOnConfigurationChangedListener(listener: OnConfigurationChangedListener): Boolean =
        onConfigurationChangedListeners.add(listener)

    override fun removeOnConfigurationChangedListener(listener: OnConfigurationChangedListener): Boolean =
        onConfigurationChangedListeners.remove(listener)

    override fun clearOnConfigurationChangedListeners() {
        onConfigurationChangedListeners.clear()
    }

    protected open fun enableThemeInflate() {
        layoutInflater.factory2 = ThemeFactory(
            layoutInflater.factory2,
            THEME_CONFIG,
        )
    }
}