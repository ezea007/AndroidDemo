package com.test.application.guanchazhe;

public class Test {
    public static void main(String[] args) {
        Courier courier = new Courier();
        Coder coder1 = new Coder("程序员张三");
        Coder coder2 = new Coder("程序员李四");
        Coder coder3 = new Coder("程序员王二麻子");
        courier.addObserver(coder1);
        courier.addObserver(coder2);
        courier.addObserver(coder3);

        courier.postNewExpress("快递到啦！");
    }
}
