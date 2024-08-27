import zhupff.gadgets.basic.Basic
import zhupff.gadgets.logger.Logger
import zhupff.gadgets.widget.Widget

plugins {
    id("gadget.library")
    id("zhupff.gadgets")
    alias(gvc.plugins.kotlin.ksp)
}

script {
    configuration("zhupff.gadget.basic") {
        configure()
    }
}

gadgets {
    Basic {
        jvm("api")
        android("api")
    }
    Logger {
        logger("api")
    }
    Widget {
        widget("api")
        annotation("api")
        dsl("api")
        compile("ksp")
    }
}

dependencies {
    api(gvc.androidx.constraintlayout)
}