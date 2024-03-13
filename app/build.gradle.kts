plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
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
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    dataBinding {
        enable = true
    }
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
}

project.beforeEvaluate {
    println("---project：beforeEvaluate Project开始评估，对象是 = " + project.name)
}
project.afterEvaluate {
    println("---project：afterEvaluate Project评估完毕，对象是 = " + project.name)
}
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
