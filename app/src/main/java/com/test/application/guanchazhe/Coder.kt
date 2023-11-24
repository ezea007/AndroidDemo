package com.test.application.guanchazhe

import java.util.Observable
import java.util.Observer

class Coder : Observer {
    var name: String? = null

    constructor(name: String?) {
        this.name = name
    }
    override fun update(o: Observable?, arg: Any?) {

        System.out.println("嘿，" + name + "，您的快递到了");



        fun toString(name: String): String {
            return "程序员:" + name;
        }
    }
}