package zhupff.gadget.basic.fragment

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import zhupff.gadgets.basic.OnConfigurationChangedDispatcher
import zhupff.gadgets.basic.OnConfigurationChangedListener
import zhupff.gadgets.logger.logV
import java.util.concurrent.CopyOnWriteArraySet
import java.util.concurrent.atomic.AtomicInteger

abstract class GadgetFragment : Fragment(), OnConfigurationChangedDispatcher {

    companion object {
        private val ID = AtomicInteger(0)
    }

    val ID_TAG: String = "${this::class.java.simpleName}(${hashCode()})[${ID.getAndIncrement()}]"

    protected lateinit var windowInsetsControllerCompat: WindowInsetsControllerCompat
        private set

    protected val onConfigurationChangedListeners = CopyOnWriteArraySet<OnConfigurationChangedListener>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ID_TAG.logV("onAttach($context)")
        windowInsetsControllerCompat = WindowInsetsControllerCompat(requireActivity().window, requireActivity().window.decorView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ID_TAG.logV("onCreate($savedInstanceState)")
    }

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        ID_TAG.logV("onCreateView($inflater, $container, $savedInstanceState)")
        return createView(inflater, container, savedInstanceState) ?: super.onCreateView(inflater, container, savedInstanceState)
    }

    open fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ID_TAG.logV("onViewCreated($view, $savedInstanceState)")
    }

    override fun onStart() {
        super.onStart()
        ID_TAG.logV("onStart()")
    }

    override fun onResume() {
        super.onResume()
        ID_TAG.logV("onResume()")
    }

    override fun onPause() {
        super.onPause()
        ID_TAG.logV("onPause()")
    }

    override fun onStop() {
        super.onStop()
        ID_TAG.logV("onStop()")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ID_TAG.logV("onDestroyView()")
    }

    override fun onDestroy() {
        super.onDestroy()
        ID_TAG.logV("onDestroy()")
    }

    override fun onDetach() {
        super.onDetach()
        ID_TAG.logV("onDetach()")
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


    fun addSelf(fragmentManager: FragmentManager, container: ViewGroup) {
        addSelf(fragmentManager, container.id)
    }
    fun addSelf(fragmentManager: FragmentManager, containerId: Int) {
        fragmentManager.beginTransaction()
            .add(containerId, this, ID_TAG)
            .commitAllowingStateLoss()
    }

    fun removeSelf() {
        parentFragmentManager.beginTransaction()
            .remove(this)
            .commitAllowingStateLoss()
    }
}