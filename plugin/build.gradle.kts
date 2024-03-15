plugins {
    id("java-gradle-plugin") // 会自动引入java-library、gradleApi()
    id("org.jetbrains.kotlin.jvm") //支持kotlin编写插件
    id("maven-publish") // 发布到maven
}

gradlePlugin {
    plugins {
        create("pagePlugin") {
            artifacts { "pageanalysisplugin" }
            group = "com.example.plugin"
            version = "1.0.0"
            id = "com.example.plugin" // 插件的唯一标识，使用插件的时候就是这个id
            implementationClass =
                "com.example.plugin.PageAnalysisPlugin" //PageAnalysisPlugin的全类名 取代resources声明
        }
    }
}

publishing {
    repositories {
        maven {
            setUrl(layout.buildDirectory.dir("maven-repo"))
        }
    }
}

dependencies {
    implementation("com.android.tools.build:gradle:8.1.1")
}