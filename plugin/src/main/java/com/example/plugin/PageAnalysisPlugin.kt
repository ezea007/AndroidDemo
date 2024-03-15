package com.example.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.ApplicationVariant
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ModuleVersionIdentifier
import java.util.function.Consumer


class PageAnalysisPlugin : Plugin<Project> {
    private val TAG = "DependenciesPlugin >>>>> "
    override fun apply(project: Project) {
        println(TAG + this.javaClass.name)
        val extension: DependenciesPluginExtension = project.extensions.create(
            "printDependencies",
            DependenciesPluginExtension::class.java
        )
        project.afterEvaluate { pro: Project? ->

            /*
             * 扩展的配置要在 project.afterEvaluate 之后获取哦
             * 因为配置阶段完成，才能读取参数
             * 且配置完成，才能拿到所有的依赖
             */
            if (extension.enable) {
                // debug/release也可以加配置
                println(TAG + "已开启依赖打印")
                val androidExtension =
                    project.extensions.getByType(
                        AppExtension::class.java
                    )
                androidExtension.applicationVariants.all { applicationVariant: ApplicationVariant ->
                    println(TAG + ">>>>>>>>  applicationVariant.getName() = " + applicationVariant.name)
                    // 方式一：build.gradle 文件中添加的依赖
                    val configuration: Configuration = project.configurations
                        .getByName(applicationVariant.name + "CompileClasspath")
                    val allDependencies: Set<Dependency> =
                        configuration.getAllDependencies()
                    //                for (Dependency dependency : allDependencies) {
//                    System.out.println(TAG + "dependency === " + dependency.getGroup() + ":" + dependency.getName() + ":" + dependency.getVersion());
//                }
                    val androidLibs: MutableList<String> =
                        ArrayList()
                    val otherLibs: MutableList<String> =
                        ArrayList()

                    // 方式二：所有的依赖，包括依赖中的依赖
                    configuration.getResolvedConfiguration().getLenientConfiguration()
                        .getAllModuleDependencies().forEach { resolvedDependency ->
                            val identifier: ModuleVersionIdentifier =
                                resolvedDependency.getModule().getId()
                            //System.out.println(TAG + "identifier === " + identifier.getGroup() + ":" + identifier.getName() + ":" + identifier.getVersion());
                            if (identifier.group.contains("androidx") || identifier.group
                                    .contains("com.google") || identifier.group
                                    .contains("org.jetbrains")
                            ) {
                                androidLibs.add(identifier.group + ":" + identifier.name + ":" + identifier.version)
                            } else {
                                otherLibs.add(identifier.group + ":" + identifier.name + ":" + identifier.version)
                            }
                        }
                    println("--------------官方库 start--------------")
                    androidLibs.forEach(Consumer { x: String? ->
                        println(
                            x
                        )
                    })
                    println("--------------官方库 end--------------")
                    println("--------------三方库 start--------------")
                    otherLibs.forEach(Consumer { x: String? ->
                        println(
                            x
                        )
                    })
                    println("--------------三方库 end--------------")
                }
            } else {
                println(TAG + "已关闭依赖打印")
            }
        }
    }
}


