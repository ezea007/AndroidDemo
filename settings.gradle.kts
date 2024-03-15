pluginManagement {
    repositories {
//        maven { setUrl("./custom_plugin_repo") }
        maven { setUrl("./plugin/build/maven-repo") }
        maven { setUrl("https://maven.aliyun.com/repository/public") }
        maven { setUrl("https://maven.aliyun.com/repository/public") }
        maven { setUrl("https://maven.aliyun.com/repository/google") }
        maven { setUrl("https://maven.aliyun.com/repository/gradle-plugin") }
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven { setUrl("https://maven.aliyun.com/repository/public") }
        maven { setUrl("https://maven.aliyun.com/repository/google") }
        maven { setUrl("https://maven.aliyun.com/repository/gradle-plugin") }
        google()
        mavenCentral()
    }
}

rootProject.name = "Application"
include(":app")

println("---Gradle：开始初始化了")
gradle.settingsEvaluated {
    println("---Gradle：settingsEvaluated Settings对象评估完毕")
}
gradle.projectsLoaded {
    println("---Gradle：projectsLoaded 准备加载Project对象了")
}
gradle.allprojects {
    beforeEvaluate {
        println("---Gradle：Projec beforeEvaluate Project开始评估，对象是 = " + project.name)
    }
    afterEvaluate {
        println("---Gradle：Projec afterEvaluate Project评估完毕，对象是 = " + project.name)
    }
}
gradle.projectsEvaluated {
    println("---Gradle：projectsEvaluated 所有Project对象评估完毕")
}
include(":plugin")
