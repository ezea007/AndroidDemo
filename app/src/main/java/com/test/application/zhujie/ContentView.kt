package com.test.application.zhujie

@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)  //用于描述类
@Retention(AnnotationRetention.BINARY) //运行时注解
annotation class ContentView(val layoutResID: Int)
