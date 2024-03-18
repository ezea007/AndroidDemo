import java.util.Enumeration
import java.util.jar.JarEntry
import java.util.jar.JarFile

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.example.plugin")
}

android {
    namespace = "com.test.application"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.test.application"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ndk {
            abiFilters.add("armeabi-v7a")
            abiFilters.add("arm64-v8a")
        }
    }

    buildTypes {
        debug {
            versionNameSuffix = "-测试包"
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            versionNameSuffix = "-正式包" // 设置对应的版本名称后缀
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
//    kotlinOptions {
//        jvmTarget = "1.8"
//    }
    dataBinding {
        enable = true
    }
}


// 插件化开发，判断是否展示和隐藏
printDependencies {
    enable = false
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("org.greenrobot:eventbus:3.2.0")
    // Glide
    implementation("com.github.bumptech.glide:glide:4.15.1")
    kapt("com.github.bumptech.glide:compiler:4.13.2")
    // navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.6.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.6.0")
    //8.全功能：直播推流（含超低延时直播、RTC连麦）＋短视频＋播放器＋美颜特效
    implementation("com.aliyun.aio:AliVCSDK_Premium:6.4.0")

}

project.beforeEvaluate {
    println("---project：beforeEvaluate Project开始评估，对象是 = " + project.name)
}
project.afterEvaluate {
    println("---project：afterEvaluate Project评估完毕，对象是 = " + project.name)
    // 动态修改版本
    android.applicationVariants.configureEach {
        println(buildType.name)
        println(versionName)
        println(versionCode)

        // 获取 SO 库
        configurations.getByName(name + "CompileClasspath").forEach {
            println("App fine name = " + it.name)
            if (it.name.endsWith(".jar") || it.name.endsWith(".aar")) {
                val enums: Enumeration<*> = JarFile(it).entries()
                while (enums.hasMoreElements()) {
                    val jarEntry: JarEntry = enums.nextElement() as JarEntry
                    if (jarEntry.getName().endsWith(".so")) {
                        println("App ----- so name = " + jarEntry.getName())
                    }
                }
            }
        }
    }
}

// 依赖版本
configurations.all {
    resolutionStrategy {
//        failOnVersionConflict() // 版本冲突报错模式
        eachDependency {
            if (requested.group == "com.github.bumptech.glide" && requested.name == "glide") {
                println("---Gradle：Projec configurations Project找到依赖，版本是 = " + requested.version)
            }
        }
    }
}

//if (hasProperty("isTest")){
//    println("---hasProperty isTest yes")
//}else {
//    println("---hasProperty isTest no")
//}
//project.property("isTest")


//tasks.register("yechaoa", YechaoaTask) {
//    taskName = "我是传入的Task Name "
//}

class YechaoaTask : DefaultTask() {

    @Internal
    var taskName = "default"

    @TaskAction
    fun MyAction1() {
        println("$taskName -- MyAction1")
    }

    @TaskAction
    fun MyAction2() {
        println("$taskName -- MyAction2")
    }
}

tasks.register("yechaoa") {
    println("Task Name = $name")
}




















