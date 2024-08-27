package zhupff.gadget.basic.widget

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.Keep
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Guideline
import zhupff.gadgets.widget.LayoutParamsDsl
import zhupff.gadgets.widget.WidgetDsl

@Keep
@SuppressLint("StaticFieldLeak")
private object WidgetDsl {

    @WidgetDsl("View")
    private val view: View? = null

    @WidgetDsl("TextView")
    private val textView: AppCompatTextView? = null

    @WidgetDsl("ImageView")
    private val imageView: AppCompatImageView? = null

    @WidgetDsl("FrameLayout")
    private val frameLayout: FrameLayout? = null

    @WidgetDsl("ConstraintLayout")
    private val constraintLayout: ConstraintLayout? = null

    @WidgetDsl("Guideline")
    private val guideline: Guideline? = null



    @LayoutParamsDsl("ViewLayoutParams")
    private val viewLayoutParams: ViewGroup.LayoutParams? = null

    @LayoutParamsDsl("MarginLayoutParams")
    private val marginLayoutParams: ViewGroup.MarginLayoutParams? = null

    @LayoutParamsDsl("FrameLayoutParams")
    private val frameLayoutParams: FrameLayout.LayoutParams? = null

    @LayoutParamsDsl("ConstraintLayoutParams")
    private val constraintLayoutParams: ConstraintLayout.LayoutParams? = null
}