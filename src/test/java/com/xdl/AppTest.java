package com.xdl;

import org.junit.Test;

import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;


public class AppTest {
    @Test
    public void TestThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("多线程......");
            }
        }).start();
    }

    /**
     * 使用lambda表达式简化流程（匿名内部类）
     */
    @Test
    public void TestLambdaThread() {
        new Thread(() -> System.out.println("lambda表达式,多线程......")).start();
    }

    @Test
    public void TestIntBinaryOperator() {
        int res = App.calculateNum(new IntBinaryOperator() {
            @Override
            public int applyAsInt(int left, int right) {
                return left + right;
            }
        });
        System.out.println(res);
    }
    @Test
    public void TestLambdaIntBinaryOperator() {
        int res = App.calculateNum((left, right) -> left + right);
        System.out.println(res);
    }

    @Test
    public void TestPrintNum() {
         App.printNum(new IntPredicate() {
            @Override
            public boolean test(int value) {
                return value%2==0;
            }
        });
    }
    @Test
    public void TestLambdaPrintNum() {
        App.printNum(value -> value%2==0);
    }

    @Test
    public void TestTypeConvert() {
        Integer res = App.typeConvert(new Function<String, Integer>() {
            @Override
            public Integer apply(String s) {
                return Integer.valueOf(s);
            }
        });
        // 将字符串转换成Integer类型
        System.out.println(res.getClass());
    }
    @Test
    public void TestLambdaTypeConvert() {
        Integer res = App.typeConvert(s -> Integer.valueOf(s));
        // 将字符串转换成Integer类型
        System.out.println(res.getClass());
    }

    @Test
    public void TestForeachArr() {
            App.foreachArr(new IntConsumer() {
                @Override
                public void accept(int value) {
                    System.out.println(value);
            }
        });
    }
    @Test
    public void TestLambdaForeachArr() {
        App.foreachArr(value -> System.out.println(value));
    }
}


