package com.test.application.guanchazhe;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Test {
    public static void main(String[] args) {
//        Courier courier = new Courier();
//        Coder coder1 = new Coder("程序员张三");
//        Coder coder2 = new Coder("程序员李四");
//        Coder coder3 = new Coder("程序员王二麻子");
//        courier.addObserver(coder1);
//        courier.addObserver(coder2);
//        courier.addObserver(coder3);
//
//        courier.postNewExpress("快递到啦！");

        // 十进制习题ID
        int questionId = 5102;
        // 二进制习题ID
        BigDecimal binaryID = new BigDecimal(Integer.toBinaryString(questionId));
        // 二进制习题ID后5位
        BigDecimal binaryLastFiveID = binaryID.divideAndRemainder(new BigDecimal(100000))[1];
        // key = （二进制习题ID + 十进制习题ID + 二进制习题ID后5位） * 秘钥随机数 % 习题ID总数

        BigDecimal add = binaryID.add(new BigDecimal(questionId));
        System.out.println(add);
        BigDecimal add1 = add.add(binaryLastFiveID);
        System.out.println(add1);
        BigDecimal multiply = add1.multiply(new BigDecimal(14442));
        System.out.println(multiply);
        BigDecimal remainder = multiply.remainder(new BigDecimal(10));
        System.out.println(remainder);

        Map<BigDecimal, List<String>> questionMap = new TreeMap<>();
        List<String> strings = questionMap.get(0);
        System.out.println(strings);
    }
}
