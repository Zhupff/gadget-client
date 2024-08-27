package zhupff.gadget

import android.os.Bundle
import android.view.Gravity
import zhupff.gadget.basic.activity.GadgetActivity
import zhupff.gadget.basic.widget.FrameLayout
import zhupff.gadget.basic.widget.Logo
import zhupff.gadget.basic.widget.frameLayoutParams
import zhupff.gadgets.basic.dp
import zhupff.gadgets.widget.dsl.MATCH_PARENT

class MainActivity : GadgetActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
            }
        )
    }
}