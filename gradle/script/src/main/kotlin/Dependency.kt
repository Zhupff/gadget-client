import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.dsl.DependencyHandler

class Dependency<S : Script> internal constructor(
    val script: S,
) {

    init {
        assert(script[Dependency::class.java] == null)
        script[Dependency::class.java] = this
    }

    private val project: Project = script.project

    private val gvc: VersionCatalog = project.gvc

    private val dependencies: DependencyHandler = project.dependencies

    private fun project(name: String): Project = project.rootProject.findProject(name)!!



    // region begin: third-part dependencies

    fun autoService(compile: String = "") {
        dependencies.add("implementation", gvc.findLibrary("autoservice-annotation").get())
        if (compile.isNotEmpty()) {
            dependencies.add(compile, gvc.findLibrary("autoservice-processor").get())
        }
    }

    // region end



    fun basic(
        closure: Basic.() -> Unit = {},
    ) {
        dependencies.add("implementation", project(":basic"))
        Basic().closure()
    }

    inner class Basic internal constructor() {

        fun http() {
            dependencies.add("implementation", project(":basic:http"))
        }

        fun qrcode() {
            dependencies.add("implementation", project(":basic:qrcode"))
        }
    }
}



fun <S : Script> S.dependency(
    closure: Dependency<S>.() -> Unit = {},
) {
    Dependency(this).closure()
}