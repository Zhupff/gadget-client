import zhupff.gadgets.theme.Theme

plugins {
    id("gadget.application")
    id("zhupff.gadgets")
}

script {
    configuration("zhupff.gadget") {
        configure()
    }
    dependency {
        basic {
            http()
        }
    }
}

gadgets {
    Theme {
        themeMerge()
    }
}