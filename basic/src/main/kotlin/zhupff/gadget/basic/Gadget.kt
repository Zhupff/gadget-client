package zhupff.gadget.basic

import android.app.Application
import zhupff.gadget.basic.theme.GadgetTheme


lateinit var GADGET: Gadget; private set

class Gadget : Application() {

    init {
        GADGET = this
    }

    override fun onCreate() {
        super.onCreate()
        GadgetTheme.loadThemePacksFromResources()
    }
}