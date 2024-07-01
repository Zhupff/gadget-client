plugins {
    id("gadget.application")
}

script {
    configuration("zhupff.gadget") {
        configure()
    }
    dependency {
        basic {
        }
    }
}