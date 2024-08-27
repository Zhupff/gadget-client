package zhupff.gadget.basic.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import zhupff.gadget.basic.R
import zhupff.gadgets.widget.WidgetDsl
import zhupff.gadgets.widget.dsl.center
import zhupff.gadgets.widget.dsl.findViewById
import zhupff.gadgets.widget.dsl.infinite
import zhupff.gadgets.widget.dsl.linearInterpolator
import zhupff.gadgets.widget.dsl.rotationAnimator

@WidgetDsl("Logo")
class Logo @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    companion object {
        private const val ID_PART_A = "partA"
        private const val ID_PART_B = "partB"
    }

    init {
        ConstraintLayout(
            id = ID_PART_A,
            layoutParams = ConstraintLayoutParams(0, 0).apply {
                center()
                dimensionRatio = "1"
            },
        ) {
            setBackgroundResource(R.drawable.logo_part_a)

            val horizontalGuideline = Guideline(
                id = "horizontalGuideline",
                layoutParams = ConstraintLayoutParams(0, 0).apply {
                    orientation = ConstraintLayoutParams.HORIZONTAL
                    guidePercent = 0.5F
                },
            ) {
            }

            val verticalGuideline = Guideline(
                id = "verticalGuideline",
                layoutParams = ConstraintLayoutParams(0, 0).apply {
                    orientation = ConstraintLayoutParams.VERTICAL
                    guidePercent = 0.5F
                },
            ) {
            }

            View(
                id = ID_PART_B,
                layoutParams = ConstraintLayoutParams(0, 0).apply {
                    topToTop = horizontalGuideline.id
                    startToStart = verticalGuideline.id
                    matchConstraintPercentWidth = 0.42F
                    matchConstraintPercentHeight = 0.42F
                },
            ) {
                setBackgroundResource(R.drawable.logo_part_b)
            }
        }
    }

    private val vPartB: View = findViewById(ID_PART_B)!!

    private val rotationAnimation = rotationAnimator(vPartB, 0F, 360F) {
        linearInterpolator()
        infinite()
        duration = 3600L
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        rotationAnimation.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        rotationAnimation.end()
    }
}