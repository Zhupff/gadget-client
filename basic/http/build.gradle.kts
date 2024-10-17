plugins {
    id("gadget.library")
    id("zhupff.gadgets")
}

script {
    configuration("zhupff.gadget.basic.http") {
        configure()
    }
}

dependencies {
    implementation(project(":basic"))
    implementation(gvc.squareup.okhttp)
    implementation(gvc.squareup.retrofit)
    implementation(gvc.androidx.startup)
}