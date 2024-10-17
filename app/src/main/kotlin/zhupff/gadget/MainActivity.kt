package zhupff.gadget

import android.os.Bundle
import android.view.Gravity
import androidx.lifecycle.LiveData
import zhupff.gadget.basic.activity.GadgetActivity
import zhupff.gadget.basic.theme.GadgetTheme
import zhupff.gadget.basic.theme.textColor
import zhupff.gadget.basic.widget.FrameLayout
import zhupff.gadget.basic.widget.Logo
import zhupff.gadget.basic.widget.TextView
import zhupff.gadget.basic.widget.frameLayoutParams
import zhupff.gadgets.basic.dp
import zhupff.gadgets.theme.Theme
import zhupff.gadgets.theme.ThemeDispatcher
import zhupff.gadgets.theme.dsl.theme
import zhupff.gadgets.widget.dsl.MATCH_PARENT
import zhupff.gadgets.widget.dsl.WRAP_CONTENT

class MainActivity : GadgetActivity(), ThemeDispatcher {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            splashScreen.setOnExitAnimationListener { splashScreenView ->
//                val iconView = splashScreenView.iconView
//                splashScreenView.remove()
//                setSplashView(iconView?.width ?: 256.dp, iconView?.height ?: 256.dp)
//            }
//        } else {
//            setSplashView()
//        }
        setSplashView()
    }

    private fun setSplashView(logoWidth: Int = 256.dp, logoHeight: Int = 256.dp) {
        setContentView(
            FrameLayout(
                this,
                size = MATCH_PARENT to MATCH_PARENT,
            ) {

                Logo(
                    size = 256.dp to 256.dp,
                ) {
                    frameLayoutParams {
                        gravity = Gravity.CENTER
                    }
                }

                TextView(
                    size = WRAP_CONTENT to WRAP_CONTENT,
                ) {
                    frameLayoutParams {
                        gravity = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM
                        setMargins(0, 0, 0, 300)
                    }
                    text = "Welcome to Gadget!"
                    textSize = 20F
                    theme {
                        textColor(zhupff.gadget.basic.R.color.theme__themeColor)
                    }
                    setOnClickListener {
                        GadgetTheme.switch()
                    }
                }
            }
        )
    }

    override fun observableTheme(): LiveData<Theme> = GadgetTheme
}