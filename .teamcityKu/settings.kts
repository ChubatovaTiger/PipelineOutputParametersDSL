import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.pipelines.*
import jetbrains.buildServer.configs.kotlin.triggers.vcs

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2025.11"

project {

    buildType(Build)

    pipeline(PPl2)
    pipeline(Pipeline1)
}

object Build : BuildType({
    name = "Build"

    outputParams {
        param("a", "b")
        param("teamcity.a", "%teamcity.a%")
    }

    vcs {
        root(DslContext.settingsRoot)
    }
})


object PPl2 : Pipeline({
    name = "PPl2"

    repositories {
        repository(DslContext.settingsRoot)
    }

    triggers {
        vcs {
        }
    }

    dependencies {
        snapshot(Pipeline1) {
            reuseBuilds = ReuseBuilds.NO
        }
    }

    job(PPl2_Job1)
})

object PPl2_Job1 : Job({
    id("Job1")
    name = "Job 1"

    steps {
        script {
            scriptContent = "echo %dep.Ppldsl_Pipeline1.ppl1%"
        }
    }
})


object Pipeline1 : Pipeline({
    name = "Pipeline1"

    repositories {
        repository(DslContext.settingsRoot)
    }

    params {
        param("ppl1", "val1")
    }

    triggers {
        vcs {
        }
    }

    outputParams {
        param("ppl1", "%ppl1%")
        param("ppl2", "2")
    }

    job(Pipeline1_Job1)
})

object Pipeline1_Job1 : Job({
    id("Job1")
    name = "Job 1"
})
