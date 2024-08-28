package zhupff.gadget.basic.theme

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import zhupff.gadget.basic.GADGET
import zhupff.gadgets.basic.Filer
import zhupff.gadgets.basic.mkdirsIfNotExists
import zhupff.gadgets.theme.Theme
import zhupff.gadgets.theme.ThemeConfig

val ORIGIN_THEME = Theme(
    GADGET.resources,
    null,
)

val THEME_CONFIG = ThemeConfig(
    "theme__",
    ThemeAttributes,
)

object GadgetTheme
    : MutableLiveData<Theme>(ORIGIN_THEME),
    MutableMap<String, Theme> by HashMap() {

    private val THEMEPACKS_DIR = GADGET.cacheDir.resolve("themepacks").mkdirsIfNotExists()

    fun loadThemePacksFromResources() {
        CoroutineScope(Dispatchers.IO).launch {
            val themePacks = ArrayList<Theme>()
            GADGET.resources.assets.list("themepacks")
                ?.forEach { name ->
                    val themeFile = THEMEPACKS_DIR.resolve(name)
                    if (!themeFile.exists()) {
                        Filer.save(GADGET.assets.open("themepacks/${name}"), themeFile)
                    }
                    if (themeFile.exists()) {
                        val res = ORIGIN_THEME.loadThemeResources(themeFile.path)
                        if (res != null) {
                            themePacks.add(Theme(res, ORIGIN_THEME))
                        }
                    }
                }
            withContext(Dispatchers.Main) {
                themePacks.forEach { theme ->
                    if (get(theme.themeId) == null) {
                        put(theme.themeId, theme)
                    }
                }
            }
        }
    }

    fun switch() {
        if (value !== ORIGIN_THEME) {
            postValue(ORIGIN_THEME)
        } else {
            values.firstOrNull()?.let {
                postValue(it)
            }
        }
    }
}