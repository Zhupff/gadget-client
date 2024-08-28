package zhupff.gadget.basic.theme

import android.view.View
import android.widget.TextView
import com.google.auto.service.AutoService
import zhupff.gadgets.basic.addAfterClearIfNotEmpty
import zhupff.gadgets.theme.Theme
import zhupff.gadgets.theme.ThemeAttribute
import zhupff.gadgets.theme.ThemeAttributeDsl
import java.util.ServiceLoader

object ThemeAttributes : MutableList<ThemeAttribute> by ArrayList<ThemeAttribute>() {

    init {
        val attributes = ServiceLoader.load(ThemeAttribute::class.java).toList()
        addAfterClearIfNotEmpty(attributes)
    }


    @AutoService(ThemeAttribute::class)
    @ThemeAttributeDsl("textColor")
    class TextColor : ThemeAttribute(
        "textColor",
    ) {
        override fun apply(view: View, theme: Theme) {
            val colorStateList = theme.getColorStateList(resourceId, resourceName, resourceType)
            val color: Int? = if (colorStateList == null) theme.getColor(resourceId, resourceName, resourceType) else null
            when (view) {
                is TextView -> {
                    if (colorStateList != null) {
                        view.setTextColor(colorStateList)
                    } else if (color != null) {
                        view.setTextColor(color)
                    }
                }
            }
        }
    }
}