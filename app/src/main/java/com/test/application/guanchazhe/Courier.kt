package com.test.application.guanchazhe

import java.util.Observable

class Courier : Observable() {
    fun postNewExpress(content: String) {
        //标识状态或者内容发生改变（此处为快递到了）
        setChanged();
        //通知所有观察者
        notifyObservers(content);
    }
}