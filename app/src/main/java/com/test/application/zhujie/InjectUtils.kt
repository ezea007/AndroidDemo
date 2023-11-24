package com.test.application.zhujie

import android.app.Activity
import android.util.Log
import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException


class InjectUtils {

    fun InjectContentView(activity: Activity) {

        //获取activity对应的class
        val a: Class<*> = activity.javaClass
        //判断当前class是否有ContentView的注解
        if (a.isAnnotationPresent(ContentView::class.java)) {
            //获取注解实例
            val contentView = a.getAnnotation(ContentView::class.java) as ContentView
            //获取注解中的值
            val layoutIt: Int = contentView.layoutResID
            try {
                //获取class的方法，第一个参数是方法名，第二个是方法参数的类型
                val method = a.getMethod("setContentView", Int::class.javaPrimitiveType)
                method.isAccessible = true
                //调用指定对象的此方法，第二个是方法的参数
                method.invoke(activity, layoutIt)
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            }
        }
    }

    fun injectView(activity: Activity) {
        val c: Class<*> = activity.javaClass
        //获取所有的public成员变量
        val fields: Array<Field> = c.fields
        //包括公共、保护、默认（包）访问和私有字段，但不包括继承的字段
//        Field[] fields = c.getDeclaredFields();
        for (field in fields) {
            if (field.isAnnotationPresent(FindView::class.java)) {
                    val findView: FindView = field.getAnnotation(FindView::class.java)
                    val id = findView.value
                    Log.e("111111", "injectView: " + id)
                    try {
                        val method = c.getMethod("findViewById", Int::class.javaPrimitiveType)
                        method.isAccessible = true
                        //获取到view
                        val view = method.invoke(activity, id)
                        field.setAccessible(true)
                        //将获取的view赋值给指定的成员变量field
                        field.set(activity, view)
                    } catch (e: NoSuchMethodException) {
                        e.printStackTrace()
                    } catch (e: InvocationTargetException) {
                    e.printStackTrace()
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
            }
        }
    }


}