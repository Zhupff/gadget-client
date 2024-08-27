buildscript {
    dependencies {
        classpath("zhupff.gadgets:gadget-basic:0")
        classpath("zhupff.gadgets:gadget-logger:0")
        classpath("zhupff.gadgets:gadget-theme:0")
        classpath("zhupff.gadgets:gadget-toast:0")
        classpath("zhupff.gadgets:gadget-widget:0")
    }
}
plugins {
    alias(gvc.plugins.android.application) apply false
    alias(gvc.plugins.android.library) apply false
    alias(gvc.plugins.kotlin.android) apply false
    alias(gvc.plugins.kotlin.jvm) apply false
    alias(gvc.plugins.kotlin.kapt) apply false
    alias(gvc.plugins.kotlin.ksp) apply false

    id("zhupff.gadgets") version "0" apply false
}