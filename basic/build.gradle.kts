import zhupff.gadgets.basic.Basic
import zhupff.gadgets.logger.Logger
import zhupff.gadgets.theme.Theme
import zhupff.gadgets.toast.Toast
import zhupff.gadgets.widget.Widget

plugins {
    id("gadget.library")
    id("zhupff.gadgets")
    alias(gvc.plugins.kotlin.kapt)
    alias(gvc.plugins.kotlin.ksp)
}

script {
    configuration("zhupff.gadget.basic") {
        configure()
    }
    dependency {
        autoService("kapt")
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
    Theme {
        theme("api")
        annotation("api")
        dsl("api")
        compile("ksp")
    }
    Toast {
        toast("api")
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