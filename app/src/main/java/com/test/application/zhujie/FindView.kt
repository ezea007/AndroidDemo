package com.test.application.zhujie

import androidx.annotation.IdRes

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class FindView(@IdRes val value: Int)
