pluginManagement {
    includeBuild("gradle")
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        gradlePluginPortal()
        maven("https://jitpack.io")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        maven("https://jitpack.io")
    }
    versionCatalogs {
        create("gvc") {
            from("zhupff.gadgets:version-catalog:0")
        }
    }
}

rootProject.name = "Gadget"
include(":app")
include(
    ":basic",
    ":basic:http",
    ":basic:qrcode",
)
include(":theme:theme-dark")
 