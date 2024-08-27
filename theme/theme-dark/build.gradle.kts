import zhupff.gadgets.theme.Theme

plugins {
    id("gadget.theme")
    id("zhupff.gadgets")
}

script {
    configuration("zhupff.gadget.theme.dark") {
        configure()
    }
}

gadgets {
    Theme {
        themePack()
    }
}