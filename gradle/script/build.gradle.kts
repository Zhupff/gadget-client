plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    compileOnly(gvc.android.gradle)
    compileOnly(gvc.kotlin.gradle.plugin)
    compileOnly(gvc.ksp.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("ApplicationScript") {
            id = "gadget.application"
            implementationClass = "ApplicationScript"
        }
        register("LibraryScript") {
            id = "gadget.library"
            implementationClass = "LibraryScript"
        }
        register("ThemeScript") {
            id = "gadget.theme"
            implementationClass = "ThemeScript"
        }
        register("JvmScript") {
            id = "gadget.jvm"
            implementationClass = "JvmScript"
        }
    }
}