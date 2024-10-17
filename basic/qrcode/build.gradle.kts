import zhupff.gadgets.qrcode.QRCode

plugins {
    id("gadget.library")
    id("zhupff.gadgets")
}

script {
    configuration("zhupff.gadget.basic.qrcode") {
        configure()
    }
}

gadgets {
    QRCode {
        qrcode("implementation")
    }
}

dependencies {
    implementation(project(":basic"))
}