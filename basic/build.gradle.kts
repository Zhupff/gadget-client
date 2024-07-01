import zhupff.gadgets.basic.Basic
import zhupff.gadgets.logger.Logger

plugins {
    id("gadget.library")
    id("zhupff.gadgets")
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
}